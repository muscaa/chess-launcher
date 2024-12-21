package muscaa.chess.launcher.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Properties;

public class Updater {
	
	public static final String BASE_URL = "base-url";
	public static final String HEADERS = "headers";
	public static final String LINE_SPLIT = "line-split";
	public static final String DOWNLOAD_FILE = "download-file";
	public static final String INSTALL_LOCATION = "install-location";
	public static final String AUTO_UPDATE = "auto-update";
	public static final String VERSION = "version";
	
	public static final String USER_AGENT = "User-Agent";
	
	public static final String CHESS_LAUNCHER_JAR = "chess-launcher.jar";
	public static final String CHESS_LAUNCHER_VERSION = "chess-launcher.version";
	
	public static Updater INSTANCE;
	
	public final Properties properties;
	public URLClassLoader loader;
	
	public Updater() throws Exception {
		properties = new Properties();
		
		boolean empty = false;
		File file = new File("chess-updater.properties");
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			in.close();
			
			empty = properties.isEmpty();
		} else {
			System.out.println("No chess-updater.properties file found. Using defaults.");
			System.out.println("To dump the default properties, create an empty chess-updater.properties file.");
		}
		
		properties.putIfAbsent(BASE_URL, "https://jitpack.io/com/github/muscaa/chess-launcher/");
		properties.putIfAbsent(HEADERS, String.join(",", USER_AGENT));
		properties.putIfAbsent(LINE_SPLIT, "\n");
		properties.putIfAbsent(DOWNLOAD_FILE, "chess-launcher-${version.latest}.jar");
		properties.putIfAbsent(INSTALL_LOCATION, "${user.home}/.chess/");
		properties.putIfAbsent(AUTO_UPDATE, "true");
		properties.putIfAbsent(VERSION, "1.0.0");
		
		properties.putIfAbsent(USER_AGENT, "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.200 Mobile Safari/537.36");
		
		if (empty) {
			FileOutputStream out = new FileOutputStream(file);
			properties.store(out, "Default chess-updater properties");
			out.close();
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
		
		if (installed != null) System.setProperty("version.installed", installed);
		if (latest != null) System.setProperty("version.latest", latest);
		
		File launcher = getFile(CHESS_LAUNCHER_JAR, false);
		
		if (!latest.equals(installed) || !launcher.exists()) {
			download(latest);
		}
		
		loader = new URLClassLoader(new URL[] { launcher.toURI().toURL() }, Updater.class.getClassLoader());
		
		Class<?> mainClass = loader.loadClass("muscaa.chess.launcher.main.Main");
		Method main = mainClass.getMethod("main", String[].class);
		main.setAccessible(true);
		main.invoke(null, (Object) args);
	}
	
	public void download(String version) throws Exception {
		String downloadFileProp = getPropertyFormatted(DOWNLOAD_FILE);
		
		String baseUrlProp = getPropertyFormatted(BASE_URL);
		if (!baseUrlProp.endsWith("/")) baseUrlProp += "/";
		
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(new URI(baseUrlProp + version + "/" + downloadFileProp))
				.timeout(Duration.ofSeconds(3))
				.GET();
		
		String[] headersPropSplit = getPropertyFormatted(HEADERS).split(",");
		for (String key : headersPropSplit) {
			builder.header(key, getPropertyFormatted(key));
		}
		
		HttpResponse<InputStream> response = HttpClient.newBuilder()
				.build()
				.send(builder.build(), BodyHandlers.ofInputStream());
		
		FileOutputStream out = new FileOutputStream(getFile(CHESS_LAUNCHER_JAR, true));
		InputStream in = response.body();
		in.transferTo(out);
		in.close();
		out.close();
		
		Files.writeString(getFile(CHESS_LAUNCHER_VERSION, false).toPath(), version);
	}
	
	public File getFile(String name, boolean mkdirs) {
		String installLocationProp = getPropertyFormatted(INSTALL_LOCATION);
		File file = new File(installLocationProp, "launcher/" + name);
		if (mkdirs) file.getParentFile().mkdirs();
		return file;
	}
	
	public String getLatest() {
		try {
			String autoUpdateProp = getPropertyFormatted(AUTO_UPDATE);
			if (!Boolean.parseBoolean(autoUpdateProp)) return getPropertyFormatted(VERSION);
			
			String baseUrlProp = getPropertyFormatted(BASE_URL);
			HttpRequest.Builder builder = HttpRequest.newBuilder()
					  .uri(new URI(baseUrlProp))
					  .timeout(Duration.ofSeconds(3))
					  .GET();
			
			String[] headersPropSplit = getPropertyFormatted(HEADERS).split(",");
			for (String key : headersPropSplit) {
				builder.header(key, getPropertyFormatted(key));
			}
			
			HttpResponse<String> response = HttpClient.newBuilder()
					  .build()
					  .send(builder.build(), BodyHandlers.ofString());
			
			String lineSplitProp = getPropertyFormatted(LINE_SPLIT);
			String[] split = response.body().split(lineSplitProp);
			
			String latest = split[split.length - 1].trim();
			while (latest.startsWith("/")) latest = latest.substring(1);
			while (latest.endsWith("/")) latest = latest.substring(0, latest.length() - 1);
			
			return latest;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getInstalled() {
		try {
			return Files.readString(getFile(CHESS_LAUNCHER_VERSION, false).toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public String getPropertyFormatted(String key) {
    	String prop = properties.getProperty(key);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < prop.length(); i++) {
            char dollar = prop.charAt(i);
            
            if (dollar == '$' && i + 2 < prop.length()) {
                char openCurly = prop.charAt(i + 1);
                
                if (openCurly == '{') {
                	StringBuilder arg = new StringBuilder();
                	int j;
                	for (j = i + 2; j < prop.length(); j++) {
						char closeCurly = prop.charAt(j);
						if (closeCurly == '}') break;
						
						arg.append(closeCurly);
                	}
                    
                	if (j != prop.length()) {
                		sb.append(System.getProperty(arg.toString()));
						i = j;
                		continue;
                	}
                }
            }
            
            sb.append(dollar);
        }
        return sb.toString();
    }
    
	public static File getUpdaterJar() {
		try {
			return new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
