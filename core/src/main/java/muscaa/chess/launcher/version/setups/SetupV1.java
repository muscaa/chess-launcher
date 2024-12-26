package muscaa.chess.launcher.version.setups;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fluff.core.utils.StringUtils;
import fluff.files.Folder;
import fluff.http.HTTP;
import fluff.http.body.HTTPBodyParser;
import fluff.json.JSON;
import fluff.json.JSONObject;
import muscaa.chess.launcher.utils.FileUtils;
import muscaa.chess.launcher.version.AbstractSetup;
import muscaa.chess.launcher.version.Version;
import muscaa.chess.launcher.version.VersionException;
import muscaa.chess.launcher.version.VersionManager;
import muscaa.chess.launcher.version.VersionState;

public class SetupV1 extends AbstractSetup {
	
	private final HTTP http = new HTTP();
	
	public SetupV1() {
		super("v1");
	}
	
	@Override
	public void install(Version version) throws VersionException {
		Folder dir = new Folder(FileUtils.versions, version.getName());
		FileUtils.deleteContents(dir);
		
		InputStream in = http.GET(StringUtils.format("${}/client-bootstrap/${}/client-bootstrap-${}-bundle.zip", VersionManager.URL, version.getName(), version.getName()))
				.setTimeout(Duration.ofSeconds(3))
				.setHead(VersionManager.HEAD)
				.send()
				.getBody()
				.getNoClose(HTTPBodyParser.INPUT_STREAM);
		
		try (ZipInputStream zipIn = new ZipInputStream(in)) {
			ZipEntry entry;
			while ((entry = zipIn.getNextEntry()) != null) {
				File file = new File(dir, entry.getName());
				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					file.getParentFile().mkdirs();
					
					FileOutputStream out = new FileOutputStream(file);
					zipIn.transferTo(out);
					out.close();
				}
				zipIn.closeEntry();
			}
		} catch (IOException e) {
			throw new VersionException(e);
		}
		
		JSONObject versionObject = JSON.object()
				.put("version", version.getName())
				.put("setup", getID());
		
		FileUtils.write(new File(dir, "version.json"))
				.append(versionObject.toPrettyString())
				.close();
	}
	
	@Override
	public void launch(Version version) throws VersionException {
		
	}
	
	@Override
	public VersionState getState(Version version) throws VersionException {
		File dir = new File(FileUtils.versions, version.getName());
		if (!dir.exists()) return VersionState.NOT_INSTALLED;
		
		File bootstrapFile = new File(dir, "client-bootstrap.jar");
		if (!bootstrapFile.exists()) return VersionState.NOT_INSTALLED;
		
		File versionFile = new File(dir, "version.json");
		if (!versionFile.exists()) return VersionState.NOT_INSTALLED;
		
		JSONObject versionObject = JSON.object(FileUtils.read(versionFile).String());
		String installedVersion = versionObject.getString("version");
		if (!installedVersion.equals(version.getName())) {
			return version.isUpdateable() ? VersionState.OUTDATED : VersionState.NOT_INSTALLED;
		}
		
		return VersionState.UP_TO_DATE;
	}
}
