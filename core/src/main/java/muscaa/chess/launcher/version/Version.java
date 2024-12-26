package muscaa.chess.launcher.version;

public class Version {
	
	private final String name;
	private final boolean snapshot;
	private final boolean updateable;
	private final AbstractSetup setup;
	
	public Version(String name, boolean snapshot, boolean updateable, AbstractSetup setup) {
		this.name = name;
		this.snapshot = snapshot;
		this.updateable = updateable;
		this.setup = setup;
	}
	
	public void install() throws VersionException {
		if (setup == null) throw new VersionException("No setup for version " + name + ". Is your launcher up to date?");
		
		setup.install(this);
	}
	
	public void launch() throws VersionException {
		if (setup == null) throw new VersionException("No setup for version " + name + ". Is your launcher up to date?");
		
		setup.launch(this);
	}
	
	public VersionState getState() throws VersionException {
		if (setup == null) throw new VersionException("No setup for version " + name + ". Is your launcher up to date?");
		
		return setup.getState(this);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isSnapshot() {
		return snapshot;
	}
	
	public boolean isUpdateable() {
		return updateable;
	}
	
	public AbstractSetup getSetup() {
		return setup;
	}
}
