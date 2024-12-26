package muscaa.chess.launcher.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.utils.IconUtils;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -8152603400383116276L;
	
	public MainFrame() {
		super("Chess Launcher");
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChessLauncher.INSTANCE.stop();
			}
		});
		setMinimumSize(new Dimension(640, 480));
		setLocationRelativeTo(null);
		setIconImages(List.of(
				IconUtils.getIcon("/icon_64.png"),
				IconUtils.getIcon("/icon_32.png")
				));
	}
}
