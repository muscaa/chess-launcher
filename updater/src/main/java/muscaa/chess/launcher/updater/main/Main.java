package muscaa.chess.launcher.updater.main;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import muscaa.chess.launcher.updater.Updater;

public class Main {
	
	public static void main(String[] args) {
		try {
			Updater.INSTANCE = new Updater();
			Updater.INSTANCE.launch(args);
		} catch (Exception e) {
			e.printStackTrace();
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
            
            JOptionPane.showMessageDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
