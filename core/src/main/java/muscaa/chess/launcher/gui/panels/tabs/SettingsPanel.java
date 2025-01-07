package muscaa.chess.launcher.gui.panels.tabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.formdev.flatlaf.FlatClientProperties;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.config.configs.Settings;
import muscaa.chess.launcher.gui.MainFrame;

public class SettingsPanel extends JPanel {
	
	private static final long serialVersionUID = 8959004286375243427L;
	
	private final JPanel rows;
	private final GridBagConstraints gbc;
	
	public SettingsPanel() {
		setLayout(new BorderLayout());
		
		rows = new JPanel();
		rows.setLayout(new GridBagLayout());
		
		gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.NORTH;
		
		init();
		
		gbc.weighty = 1;
		addRow(null);
		
		JScrollPane scroll = new JScrollPane(rows);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		
		add(scroll, BorderLayout.CENTER);
	}
	
	private void init() {
		addWindowSettings();
	}
	
	private void addWindowSettings() {
		JLabel title = new JLabel("Window Settings", JLabel.CENTER);
		title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
		addRow(title);
		
		addRow(new JSeparator());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 10, 10));
		
		JButton center = new JButton("Center Window");
		center.addActionListener(e -> {
			MainFrame window = ChessLauncher.INSTANCE.mainFrame;
			window.setLocationRelativeTo(null);
		});
		panel.add(center);
		
		JButton reset = new JButton("Reset Position & Size");
		reset.addActionListener(e -> {
			MainFrame window = ChessLauncher.INSTANCE.mainFrame;
			window.setSize(Settings.WINDOW_WIDTH.getDefaultValue(), Settings.WINDOW_HEIGHT.getDefaultValue());
			window.setLocationRelativeTo(null);
		});
		panel.add(reset);
		
		addRow(panel);
	}
	
	private void addRow(Component component) {
		JPanel row = new JPanel();
		row.setLayout(new BorderLayout());
		if (component != null) row.add(component);
		
		rows.add(row, gbc);
		gbc.gridy++;
	}
}
