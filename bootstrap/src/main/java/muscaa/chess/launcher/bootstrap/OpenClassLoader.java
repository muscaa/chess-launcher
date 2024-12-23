package muscaa.chess.launcher.bootstrap;

import java.net.URL;
import java.net.URLClassLoader;

public class OpenClassLoader extends URLClassLoader {
	
	public OpenClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
}
