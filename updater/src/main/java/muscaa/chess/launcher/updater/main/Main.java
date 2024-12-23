package muscaa.chess.launcher.updater.main;

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
	
	public static URLClassLoader LOADER;
	
	public static void main(String[] args) {
		try {
			boolean empty = false;
			File file = new File("chess-updater.properties");
			if (file.exists()) {
				FileInputStream in = new FileInputStream(file);
				PROPERTIES.load(in);
				in.close();
				
				empty = PROPERTIES.isEmpty();
			} else {
				System.out.println("No chess-updater.properties file found. Using defaults.");
				System.out.println("To dump the default properties, create an empty chess-updater.properties file.");
			}
			
			PROPERTIES.putIfAbsent(BOOTSTRAP_JAR_URL, "https://github.com/muscaa/chess-launcher/releases/download/bootstrap/chess-bootstrap.jar");
			PROPERTIES.putIfAbsent(BOOTSTRAP_MD5_URL, "https://github.com/muscaa/chess-launcher/releases/download/bootstrap/chess-bootstrap.md5");
			PROPERTIES.putIfAbsent(INSTALL_DIR, "${user.home}/.chess/");
			
			if (empty) {
				FileOutputStream out = new FileOutputStream(file);
				PROPERTIES.store(out, "Default chess-updater properties");
				out.close();
			}
			
			File launcherDir = new File(getPropertyFormatted(INSTALL_DIR), "launcher");
			File loaderJarFile = new File(launcherDir, "bootstrap.jar");
			File loaderMd5File = new File(launcherDir, "bootstrap.md5");
			
			InputStream in = new URL(getPropertyFormatted(BOOTSTRAP_MD5_URL)).openStream();
			String latestMd5 = new String(in.readAllBytes());
			in.close();
			
			if (loaderJarFile.exists() && loaderMd5File.exists()) {
				String installedMd5 = Files.readString(loaderMd5File.toPath());
				if (!latestMd5.equals(installedMd5)) {
					download(loaderJarFile, loaderMd5File, latestMd5);
				}
			} else {
				download(loaderJarFile, loaderMd5File, latestMd5);
			}
			
			JarFile loaderJar = new JarFile(loaderJarFile);
			String mainClassName = loaderJar.getManifest().getMainAttributes().getValue("Main-Class");
			loaderJar.close();
			
			LOADER = new URLClassLoader(new URL[] { loaderJarFile.toURI().toURL() }, Main.class.getClassLoader());
			Class<?> mainClass = LOADER.loadClass(mainClassName);
			Method main = mainClass.getMethod("main", String[].class);
			main.setAccessible(true);
			main.invoke(null, (Object) args);
		} catch (Exception e) {
			e.printStackTrace();
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
            
            JOptionPane.showMessageDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
		}
	}
	
	public static void download(File loaderJarFile, File loaderMd5File, String md5) throws Exception {
		loaderJarFile.getParentFile().mkdirs();
		loaderMd5File.getParentFile().mkdirs();
		
		InputStream in = new URL(getPropertyFormatted(BOOTSTRAP_JAR_URL)).openStream();
		FileOutputStream out = new FileOutputStream(loaderJarFile);
		in.transferTo(out);
		out.close();
		in.close();
		
		Files.writeString(loaderMd5File.toPath(), md5);
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
}
