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
import muscaa.chess.launcher.utils.FileUtils;
import muscaa.chess.launcher.version.AbstractSetup;
import muscaa.chess.launcher.version.SetupStages;
import muscaa.chess.launcher.version.Version;
import muscaa.chess.launcher.version.VersionException;
import muscaa.chess.launcher.version.VersionManager;
import muscaa.chess.launcher.version.VersionStatus;

public class SetupV1 extends AbstractSetup {
	
	private final HTTP http = new HTTP();
	
	public SetupV1() {
		super("v1");
	}
	
	@Override
	public void install(SetupStages stages, Folder dir, Version version) throws VersionException {
		stages.beginIndeterminate(StringUtils.format("Downloading ${}/bundle.zip...", version.getString()));
		
		InputStream in = http.GET(StringUtils.format("${}/client-bootstrap/${}/bundle.zip", VersionManager.URL, version.getString()))
				.setTimeout(Duration.ofSeconds(3))
				.setHead(VersionManager.HEAD)
				.send()
				.getBody()
				.getNoClose(HTTPBodyParser.INPUT_STREAM);
		
		try (ZipInputStream zipIn = new ZipInputStream(in)) {
			int available = in.available();
			stages.begin(null, available);
			
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
				
				stages.progress(available - in.available());
			}
		} catch (IOException e) {
			throw new VersionException(e);
		}
	}
	
	@Override
	public void launch(SetupStages stages, Folder dir, Version version) throws VersionException {
		File bootstrapFile = new File(dir, "client-bootstrap.jar");
		if (!bootstrapFile.exists()) throw new VersionException("Version not installed!");
		
		stages.begin("Creating process...", 1);
		ProcessBuilder pb = new ProcessBuilder()
				.command("javaw", "-jar", bootstrapFile.getAbsolutePath())
				.directory(FileUtils.dir);
		
		try {
			pb.start();
			stages.progress(1);
		} catch (IOException e) {
			throw new VersionException(e);
		}
	}
	
	@Override
	public VersionStatus getStatus(Folder dir, Version version) throws VersionException {
		File bootstrapFile = new File(dir, "client-bootstrap.jar");
		if (!bootstrapFile.exists()) return VersionStatus.NOT_INSTALLED;
		
		return VersionStatus.UP_TO_DATE;
	}
}
