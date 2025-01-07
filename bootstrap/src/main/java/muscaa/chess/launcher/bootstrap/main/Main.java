package muscaa.chess.launcher.bootstrap.main;

import muscaa.chess.launcher.bootstrap.Bootstrap;
import muscaa.chess.launcher.bootstrap.JitpackBootstrap;

public class Main {
	
	public static void main(String[] args) throws Exception {
		JitpackBootstrap bootstrap = new JitpackBootstrap();
		
		if (args.length > 0 && args[0].equals("--debug")) {
			bootstrap.debug = true;
			
			String[] newArgs = new String[args.length - 1];
			System.arraycopy(args, 1, newArgs, 0, newArgs.length);
			args = newArgs;
		}
		
		Bootstrap.INSTANCE = bootstrap;
		Bootstrap.INSTANCE.launch(args);
	}
}
