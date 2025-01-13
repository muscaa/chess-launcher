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
		  		""" + CSS + """
		  				</style>
		  			</head>
		  			
		  			<body>
		  		""" + html + """
		  			</body>
		  		</html>
				""");
	}
	
	private static final String CSS = """
			:root {
				--base-size-4: 0.25rem;
				--base-size-8: 0.5rem;
				--base-size-16: 1rem;
				--base-size-24: 1.5rem;
				--base-size-40: 2.5rem;
				--base-text-weight-normal: 400;
				--base-text-weight-medium: 500;
				--base-text-weight-semibold: 600;
				--fontStack-monospace: ui-monospace, SFMono-Regular, SF Mono, Menlo, Consolas, Liberation Mono, monospace;
				--fgColor-accent: Highlight;
			    --focus-outlineColor: #1f6feb;
			    --fgColor-default: #f0f6fc;
			    --fgColor-muted: #9198a1;
			    --fgColor-accent: #4493f8;
			    --fgColor-success: #3fb950;
			    --fgColor-attention: #d29922;
			    --fgColor-danger: #f85149;
			    --fgColor-done: #ab7df8;
			    --bgColor-default: #0d1117;
			    --bgColor-muted: #151b23;
			    --bgColor-neutral-muted: #656c7633;
			    --bgColor-attention-muted: #bb800926;
			    --borderColor-default: #3d444d;
			    --borderColor-muted: #3d444db3;
			    --borderColor-neutral-muted: #3d444db3;
			    --borderColor-accent-emphasis: #1f6feb;
			    --borderColor-success-emphasis: #238636;
			    --borderColor-attention-emphasis: #9e6a03;
			    --borderColor-danger-emphasis: #da3633;
			    --borderColor-done-emphasis: #8957e5;
			    --color-prettylights-syntax-comment: #9198a1;
			    --color-prettylights-syntax-constant: #79c0ff;
			    --color-prettylights-syntax-constant-other-reference-link: #a5d6ff;
			    --color-prettylights-syntax-entity: #d2a8ff;
			    --color-prettylights-syntax-storage-modifier-import: #f0f6fc;
			    --color-prettylights-syntax-entity-tag: #7ee787;
			    --color-prettylights-syntax-keyword: #ff7b72;
			    --color-prettylights-syntax-string: #a5d6ff;
			    --color-prettylights-syntax-variable: #ffa657;
			    --color-prettylights-syntax-brackethighlighter-unmatched: #f85149;
			    --color-prettylights-syntax-brackethighlighter-angle: #9198a1;
			    --color-prettylights-syntax-invalid-illegal-text: #f0f6fc;
			    --color-prettylights-syntax-invalid-illegal-bg: #8e1519;
			    --color-prettylights-syntax-carriage-return-text: #f0f6fc;
			    --color-prettylights-syntax-carriage-return-bg: #b62324;
			    --color-prettylights-syntax-string-regexp: #7ee787;
			    --color-prettylights-syntax-markup-list: #f2cc60;
			    --color-prettylights-syntax-markup-heading: #1f6feb;
			    --color-prettylights-syntax-markup-italic: #f0f6fc;
			    --color-prettylights-syntax-markup-bold: #f0f6fc;
			    --color-prettylights-syntax-markup-deleted-text: #ffdcd7;
			    --color-prettylights-syntax-markup-deleted-bg: #67060c;
			    --color-prettylights-syntax-markup-inserted-text: #aff5b4;
			    --color-prettylights-syntax-markup-inserted-bg: #033a16;
			    --color-prettylights-syntax-markup-changed-text: #ffdfb6;
			    --color-prettylights-syntax-markup-changed-bg: #5a1e02;
			    --color-prettylights-syntax-markup-ignored-text: #f0f6fc;
			    --color-prettylights-syntax-markup-ignored-bg: #1158c7;
			    --color-prettylights-syntax-meta-diff-range: #d2a8ff;
			    --color-prettylights-syntax-sublimelinter-gutter-mark: #3d444d;
			}
			
			* {
				-ms-text-size-adjust: 100%;
				-webkit-text-size-adjust: 100%;
				margin: 0;
				color: var(--fgColor-default);
				background-color: var(--bgColor-default);
				font-family: -apple-system,BlinkMacSystemFont,"Segoe UI","Noto Sans",Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji";
				font-size: 16px;
				line-height: 1.5;
				word-wrap: break-word;
			}
			
			h1 {
				margin: .67em 0;
				font-weight: var(--base-text-weight-semibold, 600);
				font-size: 2em;
			}
			
			h2 {
				font-weight: var(--base-text-weight-semibold, 600);
				font-size: 1.5em;
			}
			
			h3 {
				font-weight: var(--base-text-weight-semibold, 600);
				font-size: 1.25em;
			}
			
			h4 {
				font-weight: var(--base-text-weight-semibold, 600);
				font-size: 1em;
			}
			
			h5 {
				font-weight: var(--base-text-weight-semibold, 600);
				font-size: .875em;
			}
			
			h6 {
				font-weight: var(--base-text-weight-semibold, 600);
				font-size: .85em;
				color: var(--fgColor-muted);
			}
			
			p {
				margin-top: 0;
				margin-bottom: 10px;
			}
			
			ul,
			ol {
				margin-top: 0;
				margin-bottom: 0;
				padding-left: 2em;
			}
			
			p,
			blockquote,
			ul,
			ol,
			dl,
			table,
			pre,
			details {
				margin-top: 0;
				margin-bottom: var(--base-size-16);
			}
			""";
}
