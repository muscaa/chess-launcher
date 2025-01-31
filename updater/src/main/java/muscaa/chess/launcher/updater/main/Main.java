package muscaa.chess.launcher.updater.main;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Properties;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class Main {
	
	public static final String BOOTSTRAP_JAR_URL = "bootstrap-jar-url";
	public static final String BOOTSTRAP_MD5_URL = "bootstrap-md5-url";
	public static final String INSTALL_DIR = "install-dir";
	
	public static final Properties PROPERTIES = new Properties();
	
	public static boolean DEBUG;
	public static URLClassLoader LOADER;
	
	public static void main(String[] args) {
		try {
			DEBUG = args.length > 0 && args[0].equals("--debug");
			
			boolean empty = false;
			File file = new File("chess-updater.properties");
			if (file.exists()) {
				FileInputStream in = new FileInputStream(file);
				PROPERTIES.load(in);
				in.close();
				
				empty = PROPERTIES.isEmpty();
			} else {
				debug("No chess-updater.properties file found. Using defaults.");
				debug("To dump the default properties, create an empty chess-updater.properties file.");
			}
			
			PROPERTIES.putIfAbsent(BOOTSTRAP_JAR_URL, "https://github.com/muscaa/chess-launcher/releases/download/bootstrap/bootstrap.jar");
			PROPERTIES.putIfAbsent(BOOTSTRAP_MD5_URL, "https://github.com/muscaa/chess-launcher/releases/download/bootstrap/bootstrap.md5");
			PROPERTIES.putIfAbsent(INSTALL_DIR, "${user.home}/.chess/");
			
			if (empty) {
				FileOutputStream out = new FileOutputStream(file);
				PROPERTIES.store(out, "Default chess-updater properties");
				out.close();
			}
			
			File launcherDir = new File(getPropertyFormatted(INSTALL_DIR), "launcher");
			File bootstrapJarFile = new File(launcherDir, "bootstrap.jar");
			File bootstrapMd5File = new File(launcherDir, "bootstrap.md5");
			
			String latestMd5 = getLatestMd5();
			
			if (!bootstrapJarFile.exists() || !bootstrapMd5File.exists()) {
				if (latestMd5 == null) throw new Exception("Failed to get bootstrap md5. Are you connected to the internet?");
				
				download(bootstrapJarFile, bootstrapMd5File, latestMd5);
			} else {
				try {
					if (latestMd5 == null) throw new Exception("Failed to get bootstrap md5. Are you connected to the internet?");
					
					String installedMd5 = Files.readString(bootstrapMd5File.toPath());
					if (!latestMd5.equals(installedMd5)) {
						download(bootstrapJarFile, bootstrapMd5File, latestMd5);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			debug("Loading bootstrap jar...");
			
			JarFile bootstrapJar = new JarFile(bootstrapJarFile);
			String mainClassName = bootstrapJar.getManifest().getMainAttributes().getValue("Main-Class");
			bootstrapJar.close();
			
			LOADER = new URLClassLoader(new URL[] { bootstrapJarFile.toURI().toURL() }, Main.class.getClassLoader());
			Class<?> mainClass = LOADER.loadClass(mainClassName);
			Method main = mainClass.getMethod("main", String[].class);
			main.setAccessible(true);
			main.invoke(null, (Object) args);
		} catch (Exception e) {
			e.printStackTrace();
			
			if (!GraphicsEnvironment.isHeadless()) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
	            
	            JOptionPane.showMessageDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
            System.exit(1);
		}
	}
	
	public static void download(File loaderJarFile, File loaderMd5File, String md5) throws Exception {
		debug("Downloading bootstrap jar...");
		
		loaderJarFile.getParentFile().mkdirs();
		loaderMd5File.getParentFile().mkdirs();
		
		InputStream in = new URL(getPropertyFormatted(BOOTSTRAP_JAR_URL)).openStream();
		FileOutputStream out = new FileOutputStream(loaderJarFile);
        long transferred = 0;
        byte[] buffer = new byte[8192];
        int read;
        while ((read = in.read(buffer, 0, 8192)) >= 0) {
            out.write(buffer, 0, read);
            transferred += read;
            
            debug(transferred);
        }
		out.close();
		in.close();
		
		Files.writeString(loaderMd5File.toPath(), md5);
	}
	
	public static String getLatestMd5() {
		try {
			InputStream in = new URL(getPropertyFormatted(BOOTSTRAP_MD5_URL)).openStream();
			String latestMd5 = new String(in.readAllBytes());
			in.close();
			return latestMd5;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public static String getPropertyFormatted(String key) throws Exception {
    	String prop = PROPERTIES.getProperty(key);
        
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
    
    public static void debug(Object o) {
    	if (!DEBUG) return;
    	
    	System.out.println(o);
    }
}
