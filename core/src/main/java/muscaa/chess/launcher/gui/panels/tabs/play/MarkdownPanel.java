package muscaa.chess.launcher.gui.panels.tabs.play;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import com.github.rjeschke.txtmark.Processor;

public class MarkdownPanel extends JPanel {
	
	private static final long serialVersionUID = 5025547644427219933L;
	
	private final JEditorPane editor;
	
	public MarkdownPanel() {
		editor = new JEditorPane();
		editor.setContentType("text/html");
		editor.setEditable(false);
		
		setLayout(new BorderLayout());
		
		add(editor, BorderLayout.CENTER);
	}
	
	public void setMarkdown(String md) {
		String html = Processor.process(md);
		editor.setText("""
				<html>
		  			<head>
		  				<style>
		  					body {
		  						font-family: Segoe UI;
		  						font-size: 14;
		  					}
		  				</style>
		  			</head>
		  			
		  			<body>
		  		""" + html + """
		  			</body>
		  		</html>
				""");
	}
}
