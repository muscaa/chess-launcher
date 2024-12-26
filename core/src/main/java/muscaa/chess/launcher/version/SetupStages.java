package muscaa.chess.launcher.version;

import java.awt.EventQueue;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SetupStages {
	
	private final JProgressBar bar;
	private final JLabel label;
	
	private String name;
	private int max;
	private int progress;
	
	public SetupStages(JProgressBar bar, JLabel label) {
		this.bar = bar;
		this.label = label;
		
		reset();
	}
	
	private void reset() {
		name = null;
		max = 0;
		progress = 0;
		
		update();
	}
	
	private void update() {
		String name = this.name;
		int max = this.max;
		int progress = this.progress;
		
		EventQueue.invokeLater(() -> {
			label.setText(name);
			bar.setMinimum(0);
			bar.setMaximum(max);
			bar.setIndeterminate(progress == 0 && max < 0);
			bar.setValue(progress);
		});
	}
	
	public void begin(String newName, int newMax) {
		if (newName == null && name == null) throw new IllegalStateException("New stage cannot be null!");
		
		if (newName != null) name = newName;
		max = newMax < 0 ? -100 : newMax;
		progress = 0;
		
		update();
	}
	
	public void beginIndeterminate(String newName) {
		begin(newName, -1);
	}
	
	public void progress(int newValue) {
		if (!isRunning()) throw new IllegalStateException("No stage in progress!");
		
		progress = max == 0 ? 0 : (100 * newValue) / max;
		
		update();
	}
	
	public void end() {
		if (!isRunning()) throw new IllegalStateException("No stage in progress!");
		
		reset();
	}
	
	public boolean isRunning() {
		return name != null;
	}
}
