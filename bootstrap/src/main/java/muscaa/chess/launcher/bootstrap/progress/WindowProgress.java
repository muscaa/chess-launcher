package muscaa.chess.launcher.bootstrap.progress;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import muscaa.chess.launcher.bootstrap.Bootstrap;

public class WindowProgress extends HeadlessProgress {
	
	private JFrame frame;
	private JProgressBar progressBar;
	
	public WindowProgress(Bootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	public void init(int stages) throws Exception {
		super.init(stages);
		
		frame = new JFrame();
		frame.setTitle("Chess Launcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(stages);
		progressBar.setValue(0);
		frame.add(progressBar);
		
		frame.setVisible(true);
	}
	
	@Override
	public void update(int stage, String name) throws Exception {
		super.update(stage, name);
		
		progressBar.setValue(stage);
	}
	
	@Override
	public void end() throws Exception {
		super.end();
		
		frame.dispose();
	}
}
