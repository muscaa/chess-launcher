package muscaa.chess.launcher.gui.panels.tabs.play;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

import com.formdev.flatlaf.FlatClientProperties;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.version.SetupStages;
import muscaa.chess.launcher.version.Version;
import muscaa.chess.launcher.version.VersionStatus;

public class LaunchPanel extends JPanel {
	
	private static final long serialVersionUID = 8959004286375243427L;
	
	private VersionsComboBoxModel versionsModel;
	private JComboBox<Version> versions;
	private JButton launch;
	
	private CardLayout progressLayout;
	private JProgressBar progress;
	private JLabel progressLabel;
	private SetupStages stages;
	
	public LaunchPanel() {
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		add(new JSeparator(), BorderLayout.NORTH);
		addVersionAndLaunch();
		addProgress();
	}
	
	private void addVersionAndLaunch() {
		versionsModel = new VersionsComboBoxModel(true);
		versions = new JComboBox<>(versionsModel);
		versions.addActionListener(e -> {
			launch.setText(getLaunchButtonText());
		});
		
		launch = new JButton(getLaunchButtonText());
		launch.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
		launch.setPreferredSize(new Dimension(200, launch.getPreferredSize().height));
		launch.addActionListener(e -> {
			if (stages.isRunning()) return;
			
			Thread t = new Thread(() -> {
				try {
					show("progress");
					Version v = versionsModel.getVersion();
					if (versionsModel.getStatus() != VersionStatus.UP_TO_DATE) {
						v.install(stages);
					}
					v.launch(stages);
					
					show("empty");
					stages.end();
					
					ChessLauncher.INSTANCE.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
					
					stages.end();
					progressLabel.setText(e1.getMessage());
					progress.setValue(0);
				}
			});
			t.setName(getLaunchButtonText() + " Thread");
			t.setDaemon(true);
			t.start();
		});
		ChessLauncher.INSTANCE.mainFrame.getRootPane().setDefaultButton(launch);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(10, 10));
		panel.add(versions, BorderLayout.CENTER);
		panel.add(launch, BorderLayout.EAST);
		
		add(panel, BorderLayout.CENTER);
	}
	
	private void addProgress() {
		JPanel panel = new JPanel();
		panel.setLayout(progressLayout = new CardLayout());
		
		panel.add("empty", new JPanel());
		
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BorderLayout(10, 10));
		
		progress = new JProgressBar();
		progressLabel = new JLabel();
		
		progressPanel.add(progress, BorderLayout.SOUTH);
		progressPanel.add(progressLabel, BorderLayout.CENTER);
		
		panel.add("progress", progressPanel);
		
		add(panel, BorderLayout.SOUTH);
		
		stages = new SetupStages(progress, progressLabel);
	}
	
	private void show(String name) {
		progressLayout.show(progress.getParent().getParent(), name);
	}
	
	private String getLaunchButtonText() {
		return switch (versionsModel.getStatus()) {
			case NOT_INSTALLED -> "Install & Launch";
			case OUTDATED -> "Update & Launch";
			case UP_TO_DATE -> "Launch";
		};
	}
}