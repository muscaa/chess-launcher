package muscaa.chess.launcher.main;

import java.io.File;

import muscaa.chess.launcher.bootstrap.Bootstrap;
import muscaa.chess.launcher.bootstrap.OpenClassLoader;

class DevBootstrap extends Bootstrap {
	
	@SuppressWarnings("resource")
	public DevBootstrap() {
		super(new File("run/launcher"), new OpenClassLoader(DevBootstrap.class.getClassLoader()));
		dir.mkdirs();
	}
	
	@Override
	public void launch(String[] args) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getLatest() {
		return getInstalled();
	}
	
	@Override
	public String getInstalled() {
		return "dev";
	}
	
	@Override
	public boolean isDebug() {
		return true;
	}
}
