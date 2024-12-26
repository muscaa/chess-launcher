package muscaa.chess.launcher.utils;

import java.io.File;

import fluff.files.FileHelper;
import fluff.files.Folder;
import muscaa.chess.launcher.bootstrap.Bootstrap;

public class FileUtils extends FileHelper {
	
	public static final File launcherDir = Bootstrap.INSTANCE.dir;
	public static final Folder launcherConfig = new Folder(launcherDir, "config");
	
	public static final File dir = launcherDir.getParentFile();
	public static final Folder versions = new Folder(dir, "versions");
}
