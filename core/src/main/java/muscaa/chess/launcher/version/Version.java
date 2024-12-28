package muscaa.chess.launcher.version;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import fluff.core.utils.StringUtils;
import fluff.files.Folder;
import fluff.json.JSON;
import fluff.json.JSONObject;
import muscaa.chess.launcher.utils.FileUtils;

public class Version {
	
	private final String name;
	private final String string;
	private final boolean updateable;
	private final AbstractSetup setup;
	private final boolean snapshot;
	
	Version(String name, String string, boolean updateable, AbstractSetup setup) {
		this.name = name;
		this.string = string;
		this.updateable = updateable;
		this.setup = setup;
		this.snapshot = string.contains("snapshot");
	}
	
	Version(File dir, Map<String, AbstractSetup> setups) throws VersionException {
		File versionFile = new File(dir, "version.json");
		if (!versionFile.exists()) throw new VersionException("File version.json not found.");
		
		JSONObject json = JSON.object(FileUtils.read(versionFile).String());
		
		this.name = json.getString("name");
		this.string = json.getString("string");
		this.updateable = json.getBoolean("updateable");
		this.setup = setups.get(json.getString("setup"));
		this.snapshot = string.contains("snapshot");
		
		if (getStatus() == VersionStatus.NOT_INSTALLED) throw new VersionException("Version " + name + " not installed.");
	}
	
	public void install(SetupStages stages) throws VersionException {
		stages.beginIndeterminate(StringUtils.format("Installing ${}...", name));
		
		VersionStatus status = getStatus();
		
		Folder dir = new Folder(FileUtils.versions, name);
		if (status != VersionStatus.NOT_INSTALLED) {
			stages.begin("Removing old files...", 1);
			FileUtils.deleteContents(dir);
			stages.progress(1);
		}
		
		setup.install(stages, dir, this);
		
		stages.begin("Writing version.json...", 1);
		JSONObject json = JSON.object()
				.put("name", name)
				.put("string", string)
				.put("updateable", updateable)
				.put("setup", setup.getID());
		
		FileUtils.write(new File(dir, "version.json"))
				.append(json.toPrettyString())
				.close();
		stages.progress(1);
	}
	
	public void launch(SetupStages stages) throws VersionException {
		stages.beginIndeterminate(StringUtils.format("Launching ${}...", name));
		
		VersionStatus status = getStatus();
		if (status == VersionStatus.NOT_INSTALLED) throw new VersionException("Version " + name + " not installed.");
		
		Folder dir = new Folder(FileUtils.versions, name);
		
		setup.launch(stages, dir, this);
	}
	
	public VersionStatus getStatus() throws VersionException {
		if (setup == null) throw new VersionException("No setup for version " + name + ". Is your launcher up to date?");
		
		File dir = new File(FileUtils.versions, name);
		if (!dir.exists()) return VersionStatus.NOT_INSTALLED;
		
		File versionFile = new File(dir, "version.json");
		if (!versionFile.exists()) return VersionStatus.NOT_INSTALLED;
		
		JSONObject json = JSON.object(FileUtils.read(versionFile).String());
		
		VersionStatus status = setup.getStatus(new Folder(FileUtils.versions, name), this);
		if (status != VersionStatus.NOT_INSTALLED && updateable && !string.equals(json.getString("string"))) {
			return VersionStatus.OUTDATED;
		}
		return status;
	}
	
	public String getName() {
		return name;
	}
	
	public String getString() {
		return string;
	}
	
	public boolean isUpdateable() {
		return updateable;
	}
	
	public AbstractSetup getSetup() {
		return setup;
	}
	
	public boolean isSnapshot() {
		return snapshot;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, string);
	}
}
