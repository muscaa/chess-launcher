package muscaa.chess.launcher;

import java.io.File;

import muscaa.chess.launcher.bootstrap.Bootstrap;
import muscaa.chess.launcher.gui.MainFrame;
import muscaa.chess.launcher.gui.panels.MainPanel;
import muscaa.chess.launcher.version.VersionManager;

public class ChessLauncher {
	
	public static final ChessLauncher INSTANCE = new ChessLauncher();
	
	public final String launcherVersion = Bootstrap.INSTANCE.getInstalled();
	public final File gameDir = Bootstrap.INSTANCE.dir.getParentFile();
	public final MainFrame mainFrame = new MainFrame();
	public final VersionManager versions = new VersionManager();
	
	public void start() {
		mainFrame.setContentPane(new MainPanel());
		mainFrame.setVisible(true);
	}
	
	public void stop() {
		mainFrame.dispose();
	}
}
