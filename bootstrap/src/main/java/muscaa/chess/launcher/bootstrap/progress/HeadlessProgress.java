package muscaa.chess.launcher.bootstrap.progress;

import muscaa.chess.launcher.bootstrap.Bootstrap;

public class HeadlessProgress implements IProgress {
	
	private final Bootstrap bootstrap;
	
	private int stages;
	
	public HeadlessProgress(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}
	
	@Override
	public void init(int stages) throws Exception {
		this.stages = stages;
		
		if (!bootstrap.isDebug()) return;
		
		System.out.println("Total stages: " + stages);
	}
	
	@Override
	public void update(int stage, String name) throws Exception {
		if (!bootstrap.isDebug()) return;
		
		System.out.println(stage + "/" + stages + ": " + name);
	}
	
	@Override
	public void end() throws Exception {
		if (!bootstrap.isDebug()) return;
		
		System.out.println("Done");
	}
}
