package muscaa.chess.launcher.version;

import fluff.files.Folder;

public abstract class AbstractSetup {
	
	protected final String id;
	
	public AbstractSetup(String id) {
		this.id = id;
	}
	
	public abstract void install(SetupStages stages, Folder dir, Version version) throws VersionException;
	
	public abstract void launch(SetupStages stages, Folder dir, Version version) throws VersionException;
	
	public abstract VersionStatus getStatus(Folder dir, Version version) throws VersionException;
	
	public String getID() {
		return id;
	}
}
