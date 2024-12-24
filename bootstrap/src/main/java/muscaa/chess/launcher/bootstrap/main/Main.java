package muscaa.chess.launcher.bootstrap.main;

import muscaa.chess.launcher.bootstrap.Bootstrap;
import muscaa.chess.launcher.bootstrap.JitpackBootstrap;

public class Main {
	
	public static void main(String[] args) throws Exception {
		Bootstrap.INSTANCE = new JitpackBootstrap();
		Bootstrap.INSTANCE.launch(args);
	}
}
