package muscaa.chess.launcher.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.FlatClientProperties;

import muscaa.chess.launcher.gui.panels.tabs.play.PlayPanel;

public class TabsPanel extends JPanel {
	
	private static final long serialVersionUID = 507064802342134657L;
	
	private JTabbedPane tabs;
	
	public TabsPanel() {
		setLayout(new BorderLayout());
		
		tabs = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
		addTab("Play", new PlayPanel());
		//addTab("Mods", new ModsPanel());
		//addTab("Settings", new SettingsPanel());
		
		add(tabs, BorderLayout.CENTER);
	}
	
	public JPanel addTab(String title, Component component) {
		tabs.addTab(title, component);
		
		int index = tabs.indexOfTab(title);
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel titleLabel = new JLabel(title);
		titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
		panel.add(titleLabel, BorderLayout.CENTER);
		
		tabs.setTabComponentAt(index, panel);
		
		return panel;
	}
}
