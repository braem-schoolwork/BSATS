package gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import structures.ObservableMatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

//open with windowbuilder
public class MainWindow extends JFrame implements Observer
{

	private JPanel contentPane;
	private Header header;
	private JPanel grid;
	private ObservableMatrix<Integer> algorithmData;
	private JScrollPane scrollableGrid;
	private SequenceFooter sequenceFooter;
	private final Color backgroundColor = new Color(0x336699);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		launchGUI();
	}
	
	public static void launchGUI(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws MalformedURLException 
	 */
	public MainWindow() throws MalformedURLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JFXPanel helpMenu = new JFXPanel();
		Platform.runLater( () -> {
			WebView webView = new WebView();
			webView.setMaxWidth(232);
			webView.getEngine().loadContent( "<html> Hello!");
			helpMenu.setScene(new Scene(webView));
		});
		JScrollPane scrollPane = new JScrollPane(helpMenu);
		scrollPane.setPreferredSize(new Dimension(250,500));
		contentPane.add(scrollPane, BorderLayout.LINE_END);
		scrollPane.addMouseListener(new HelpMenuMouseListener(helpMenu, "res/gui_help/helpwindow.html"));

		header = new Header(helpMenu);
		contentPane.add(header, BorderLayout.PAGE_START);
		algorithmData = (ObservableMatrix<Integer>) header.getObservableMatrix();
		algorithmData.addObserver(this);

		grid = new SequenceGrid(algorithmData, backgroundColor);
		scrollableGrid = new JScrollPane(grid);
		scrollableGrid.addMouseListener(new HelpMenuMouseListener(helpMenu, "res/gui_help/alignedmatrix.html"));

		String sequenceA = "-" + header.getSequenceA();
		JPanel rowHeader = new JPanel();
		rowHeader.setLayout(new BoxLayout(rowHeader,BoxLayout.Y_AXIS));
		for(char c : sequenceA.toCharArray()){
			rowHeader.add(new SequenceCell(String.valueOf(c)));
		}
		scrollableGrid.setRowHeaderView(rowHeader);

		String sequenceB = "-" + header.getSequenceB();
		JPanel columnHeader = new JPanel();
		columnHeader.setLayout(new BoxLayout(columnHeader,BoxLayout.X_AXIS));
		for(char c : sequenceB.toCharArray()){
			columnHeader.add(new SequenceCell(String.valueOf(c)));
		}
		scrollableGrid.setColumnHeaderView(columnHeader);

		contentPane.add(scrollableGrid, BorderLayout.CENTER);

		
		sequenceFooter = new SequenceFooter(helpMenu);
		contentPane.add(sequenceFooter, BorderLayout.PAGE_END);
		
		this.pack();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//matrix data has changed
		System.out.println("Matrix data change!");
		if(null == algorithmData){
			scrollableGrid.setViewportView(new SequenceGrid(new ObservableMatrix<Integer>(Integer.class, new Integer[][]{{}}), backgroundColor));
		} else {
			scrollableGrid.setViewportView(new SequenceGrid(algorithmData, backgroundColor));
		}
		
		String sequenceA = "-" + header.getSequenceA();
		JPanel rowHeader = new JPanel();
		rowHeader.setLayout(new BoxLayout(rowHeader,BoxLayout.Y_AXIS));
		for(char c : sequenceA.toCharArray()){
			rowHeader.add(new SequenceCell(String.valueOf(c)));
		}
		scrollableGrid.setRowHeaderView(rowHeader);

		String sequenceB = "-" + header.getSequenceB();
		JPanel columnHeader = new JPanel();
		columnHeader.setLayout(new BoxLayout(columnHeader,BoxLayout.X_AXIS));
		for(char c : sequenceB.toCharArray()){
			columnHeader.add(new SequenceCell(String.valueOf(c)));
		}
		scrollableGrid.setColumnHeaderView(columnHeader);

		sequenceFooter.setGappedSequences(header.getGappedSequenceA(), header.getGappedSequenceB());
		sequenceFooter.setScore(header.getScore());
	}

}