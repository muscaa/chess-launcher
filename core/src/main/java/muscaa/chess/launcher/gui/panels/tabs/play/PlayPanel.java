package muscaa.chess.launcher.gui.panels.tabs.play;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import muscaa.chess.launcher.ChessLauncher;

public class PlayPanel extends JPanel {
	
	private static final long serialVersionUID = 8959004286375243427L;
	
	public PlayPanel() {
		setLayout(new BorderLayout());
		
		add(new LaunchPanel(), BorderLayout.SOUTH);
		
		MarkdownPanel md = new MarkdownPanel();
		ChessLauncher.INSTANCE.versions.getNews()
				.thenAccept(md::setMarkdown);
		JScrollPane scroll = new JScrollPane(md);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		add(scroll, BorderLayout.CENTER);
	}
}
