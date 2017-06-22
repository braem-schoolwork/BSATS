package gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class HelpMenuMouseListener extends MouseAdapter{
	private JFXPanel targetHtmlComponent;
	private URL targetPage;

	public HelpMenuMouseListener(JFXPanel targetHtmlComponent, String path) throws MalformedURLException{
		this.targetHtmlComponent = targetHtmlComponent;
		this.targetPage = new File(path).toURI().toURL();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		Platform.runLater( () -> {
			WebView webView = new WebView();
			webView.setMaxWidth(232);
			webView.getEngine().load(targetPage.toExternalForm());
			targetHtmlComponent.setScene(new Scene(webView));
		});
	}
}
