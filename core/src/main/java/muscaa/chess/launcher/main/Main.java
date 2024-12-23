package muscaa.chess.launcher.main;

import java.awt.Dimension;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.bootstrap.Bootstrap;

public class Main {
	
	public static void main(String[] args) throws Exception {
		// fix for flatlaf no ui found errors
		Thread.currentThread().setContextClassLoader(Bootstrap.INSTANCE.loader);
		UIManager.getDefaults().put("ClassLoader", Bootstrap.INSTANCE.loader);
		
		FlatDarkLaf.setup();
		setUIDefaults();
		
    	ChessLauncher.INSTANCE.start();
	}
	
	private static void setUIDefaults() {
		UIManager.put("defaultFont", UIManager.getFont("h3.regular.font"));
		UIManager.put("TitlePane.buttonSize", new Dimension(35, 24));
		UIManager.put("TitlePane.centerTitle", true);
		UIManager.put("Separator.stripeIndent", 0);
	}
}
