package gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by braem on 11/30/2016.
 */
public class ComboboxPageRenderer extends DefaultListCellRenderer {
    private JFXPanel helpPage;
    private String[] pages;
    public ComboboxPageRenderer(JFXPanel helpPage, String[] pages) {
        this.helpPage = helpPage;
        this.pages = pages;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);

        if (-1 < index && null != value) {
                Platform.runLater( () -> {
                    WebView webView = new WebView();
                    webView.setMaxWidth(232);
                    try {
                        webView.getEngine().load((new File(pages[index]).toURI().toURL()).toExternalForm());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    helpPage.setScene(new Scene(webView));
                });
                //helpPage.setPage(new File(pages[index]).toURI().toURL());

        }
        return comp;
    }
}