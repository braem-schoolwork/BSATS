package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class HtmlComponent extends JComponent {
	JEditorPane content;
	
	public HtmlComponent(URL startPage){
		content = new JEditorPane();
		content.setEditable(false);
		
		try {
			content.setPage(startPage);
		} catch (IOException e){
			content.setContentType("text/html");
			content.setText("<html>Could not load page</html>");
		}		
		setLayout(new BorderLayout());
		add(content);
	}
	
	public void setPage(URL page){
		try {
			content.setPage(page);
		} catch (IOException e){
			content.setContentType("text/html");
			content.setText("<html>Could not load page</html>");
		}		
	}
	
}
