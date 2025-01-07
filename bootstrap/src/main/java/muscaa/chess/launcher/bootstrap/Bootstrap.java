package muscaa.chess.launcher.bootstrap;

import java.io.File;

public abstract class Bootstrap {
	
	public static Bootstrap INSTANCE;
	
	public final File dir;
	public final OpenClassLoader loader;
	
	public Bootstrap(File dir, OpenClassLoader loader) {
		this.dir = dir;
		this.loader = loader;
	}
	
	public abstract void launch(String[] args) throws Exception;
	
	public abstract String getLatest();
	
	public abstract String getInstalled();
	
	public abstract boolean isDebug();
	
	protected void debug(Object o) {
		if (!isDebug()) return;
		
		System.out.println(o);
	}
}
