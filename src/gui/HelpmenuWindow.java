package gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Creates the window for the custom matrix scoring menu
 * Two main panels are used one for the editable matrix area (contentPane)
 * and another for the button controls at the bottom of the window (controlPane)
 * @author Noah
 *
 */
public class HelpmenuWindow extends JFrame {

    private JPanel contentPane;
    private JPanel controlPane;

    private static HelpmenuWindow instance = null;

    public static HelpmenuWindow getInstance(Definitions type, ArgumentCollector collector){
        if(instance == null){

            synchronized(CustomMatrixWindow.class){
                if(instance == null){
                    instance = new HelpmenuWindow();
                }
            }

        }
        return instance;
    }

    public HelpmenuWindow(){

        setTitle("Help Menu");
        setBounds(0, 0, 1000, 800);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //create the JPanel
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JFXPanel helpMenu = new JFXPanel();
        Platform.runLater( () -> {
            WebView webView = new WebView();
            webView.setMaxWidth(450);
            try {
                webView.getEngine().load((new File("res/menu.html").toURI().toURL()).toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            helpMenu.setScene(new Scene(webView));
        });JScrollPane scrollPane = new JScrollPane(helpMenu);
        contentPane.add(scrollPane);
    }
}
