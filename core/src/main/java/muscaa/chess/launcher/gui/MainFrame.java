package muscaa.chess.launcher.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.config.configs.Settings;
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
		setMinimumSize(new Dimension(Settings.WINDOW_WIDTH.getDefaultValue(), Settings.WINDOW_HEIGHT.getDefaultValue()));
		setSize(Settings.WINDOW_WIDTH.get(), Settings.WINDOW_HEIGHT.get());
		
		if (Settings.WINDOW_X.isSet() && Settings.WINDOW_Y.isSet()) {
			setLocation(Settings.WINDOW_X.get(), Settings.WINDOW_Y.get());
		} else {
			setLocationRelativeTo(null);
		}
		
		setIconImages(List.of(
				IconUtils.getIcon("/icon_64.png"),
				IconUtils.getIcon("/icon_32.png")
				));
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Settings.WINDOW_WIDTH.set(getWidth());
				Settings.WINDOW_HEIGHT.set(getHeight());
				Settings.save();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				Settings.WINDOW_X.set(getX());
				Settings.WINDOW_Y.set(getY());
				Settings.save();
			}
		});
		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (getExtendedState() != ICONIFIED) {
					Settings.WINDOW_STATE.set(getExtendedState());
					Settings.save();
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				if (Settings.WINDOW_STATE.isSet()) {
					setExtendedState(Settings.WINDOW_STATE.get());
				}
			}
		});
	}
}
