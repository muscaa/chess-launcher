package muscaa.chess.launcher.version;

public abstract class AbstractSetup {
	
	protected final String id;
	
	public AbstractSetup(String id) {
		this.id = id;
	}
	
	public abstract void install(Version version) throws VersionException;
	
	public abstract void launch(Version version) throws VersionException;
	
	public abstract VersionState getState(Version version) throws VersionException;
	
	public String getID() {
		return id;
	}
}
