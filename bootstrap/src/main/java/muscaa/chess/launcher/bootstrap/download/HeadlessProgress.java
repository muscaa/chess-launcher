package muscaa.chess.launcher.bootstrap.download;

public class HeadlessProgress implements IProgress {
	
	@Override
	public void init(int stages) throws Exception {
		System.out.println("Total stages: " + stages);
	}
	
	@Override
	public void update(int stage, String name) throws Exception {
		System.out.println(stage + ": " + name);
	}
	
	@Override
	public void end() throws Exception {
		System.out.println("Done");
	}
}
