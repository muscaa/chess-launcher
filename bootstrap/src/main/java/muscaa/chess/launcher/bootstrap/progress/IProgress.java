package muscaa.chess.launcher.bootstrap.progress;

public interface IProgress {
	
	void init(int stages) throws Exception;
	
	void update(int stage, String name) throws Exception;
	
	void end() throws Exception;
}
