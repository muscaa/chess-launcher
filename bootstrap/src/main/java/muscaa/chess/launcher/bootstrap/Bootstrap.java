package muscaa.chess.launcher.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.time.Duration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Bootstrap {
	
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	
	private static final String LINE_SPLIT = "\n";
	private static final String URL = "https://jitpack.io/com/github/muscaa/chess-launcher/";
	private static final String[] HEADERS = {
			"User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.200 Mobile Safari/537.36"
	};
	private static final String VERSION_FILE = "version.txt";
	private static final String DOWNLOAD_FILE = "launcher.zip";
	private static final String MAIN_CLASS = "muscaa.chess.launcher.main.Main";
	
	public static final Bootstrap INSTANCE = new Bootstrap();
	
	public final File dir;
	public final OpenClassLoader loader;
	
	public Bootstrap() {
		try {
			dir = new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
			loader = new OpenClassLoader(Bootstrap.class.getClassLoader());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void launch(String[] args) throws Exception {
		String installed = getInstalled();
		String latest = getLatest();
		
		if (installed == null && latest == null) {
			throw new Exception("Could not get latest version and no chess-launcher is installed. Are you connected to the internet?");
		}
		
		System.out.println("Installed: " + installed);
		System.out.println("Latest: " + latest);
		
		File versionFile = new File(dir, VERSION_FILE);
		if (!latest.equals(installed) || !versionFile.exists()) {
			download(latest);
		}
		
		File[] libs = new File(dir, "libs").listFiles();
		for (int i = 0; i < libs.length; i++) {
			loader.addURL(libs[i].toURI().toURL());
		}
		
		Class<?> mainClass = loader.loadClass(MAIN_CLASS);
		Method main = mainClass.getMethod("main", String[].class);
		main.setAccessible(true);
		main.invoke(null, (Object) args);
	}
	
	public void download(String version) throws Exception {
		HttpResponse<InputStream> response = request(BodyHandlers.ofInputStream(), false, version, downloadFile(version));
		
		File downloadZip = new File(dir, DOWNLOAD_FILE);
		FileOutputStream out = new FileOutputStream(downloadZip);
		InputStream in = response.body();
		
		int total = Integer.parseInt(response.headers().firstValue("Content-Length").orElse("-1"));
		System.out.println("Total bytes: " + total);
		
		JFrame frame = new JFrame();
		frame.setTitle(downloadFile(version));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		frame.add(progressBar);
		
		frame.setVisible(true);
		System.out.println("Downloading...");
		
		int transferred = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0) {
            out.write(buffer, 0, read);
            transferred += read;
            
            int value = (int) (transferred * 100 / total);
            progressBar.setValue(value);
            
            if (value % 10 == 0) System.out.println(value + "%");
        }
        
		in.close();
		out.close();
		
		progressBar.setIndeterminate(true);
		
		System.out.println("Deleting old files...");
		File parentDir = downloadZip.getParentFile();
		for (File file : parentDir.listFiles()) {
			if (file.getName().equals(downloadZip.getName())
					|| file.getName().equals("config")
					|| file.getName().equals("bootstrap.jar")
					|| file.getName().equals("bootstrap.md5")
					) {
				continue;
			}
			
			delete(file);
		}
		
		System.out.println("Extracting...");
		extract(downloadZip, parentDir);
		
		System.out.println("Deleting zip...");
		delete(downloadZip);
		
		System.out.println("Writing version...");
		Files.writeString(new File(dir, VERSION_FILE).toPath(), version);
		
		System.out.println("Done.");
		
		frame.dispose();
	}
	
	public void extract(File zip, File outDir) throws Exception {
		ZipInputStream in = new ZipInputStream(new FileInputStream(zip));
		ZipEntry entry;
		while ((entry = in.getNextEntry()) != null) {
			File file = new File(outDir, entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				file.getParentFile().mkdirs();
				
				FileOutputStream out = new FileOutputStream(file);
				in.transferTo(out);
				out.close();
			}
			in.closeEntry();
		}
		in.close();
	}
	
	public <V> HttpResponse<V> request(BodyHandler<V> bodyHandler, boolean dir, String... path) throws Exception {
		int i = 0;
		String[] url = new String[path.length + 1];
		url[i++] = urlPath(URL);
		for (String p : path) {
			url[i++] = urlPath(p);
		}
		
		URI uri = new URI(String.join("/", url) + (dir ? "/" : ""));
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				  .uri(uri)
				  .timeout(Duration.ofSeconds(3))
				  .headers(HEADERS)
				  .GET();
		
		HttpResponse<V> response = HttpClient.newBuilder()
				  .build()
				  .send(builder.build(), bodyHandler);
		
		return response;
	}
	
	public String getLatest() {
		try {
			String[] versions = request(BodyHandlers.ofString(), true).body().split(LINE_SPLIT);
			
			for (int i = versions.length - 1; i >= 0; i--) {
				String version = urlPath(versions[i]);
				
				String[] files = request(BodyHandlers.ofString(), true, version).body().split(LINE_SPLIT);
				for (String file : files) {
					String name = urlPath(file);
					
					if (name.equals(downloadFile(version))) {
						return version;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getInstalled() {
		try {
			return Files.readString(new File(dir, VERSION_FILE).toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String urlPath(String urlPath) {
		urlPath = urlPath.trim();
		int i = 0;
		int j = urlPath.length();
		while (i < j && urlPath.charAt(i) == '/') {
			i++;
		}
		while (i < j && urlPath.charAt(j - 1) == '/') {
			j--;
		}
		urlPath = urlPath.substring(i, j);
		urlPath = urlPath.trim();
		return urlPath.isBlank() ? null : urlPath;
	}
	
	private static void delete(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				delete(f);
			}
		}
		file.delete();
	}
	
	private static String downloadFile(String version) {
		return "chess-launcher-" + version + "-bundle.zip";
	}
}
