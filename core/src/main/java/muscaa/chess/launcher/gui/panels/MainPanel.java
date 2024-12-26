package muscaa.chess.launcher.gui.panels;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.formdev.flatlaf.FlatClientProperties;

import muscaa.chess.launcher.ChessLauncher;

public class MainPanel extends JPanel {
	
	private static final long serialVersionUID = -1744059627812464798L;
	
	public MainPanel() {
		setLayout(new BorderLayout());
		
		JPanel footer = new JPanel();
		footer.setLayout(new BorderLayout());
		footer.add(new JSeparator(), BorderLayout.NORTH);
		
		JLabel version = new JLabel("version " + ChessLauncher.INSTANCE.launcherVersion);
		version.putClientProperty(FlatClientProperties.STYLE_CLASS, "mini");
		version.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 0));
		footer.add(version, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);
		
		add(new TabsPanel(), BorderLayout.CENTER);
	}
}
