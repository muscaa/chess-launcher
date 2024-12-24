package muscaa.chess.launcher.main;

import java.io.File;

import muscaa.chess.launcher.bootstrap.Bootstrap;
import muscaa.chess.launcher.bootstrap.OpenClassLoader;

class DevBootstrap extends Bootstrap {
	
	@SuppressWarnings("resource")
	public DevBootstrap() {
		super(new File("run"), new OpenClassLoader(DevBootstrap.class.getClassLoader()));
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
}
