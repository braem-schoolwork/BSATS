package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Creates the window for the custom matrix scoring menu
 * Two main panels are used one for the editable matrix area (contentPane)
 * and another for the button controls at the bottom of the window (controlPane)
 * @author Noah
 *
 */
public class CustomMatrixWindow  extends JFrame {
	
	private static Definitions sequenceType;
	private JPanel contentPane;
	private JPanel controlPane;
	
	private static CustomMatrixWindow instance = null;
	
	public static CustomMatrixWindow getInstance(Definitions type, ArgumentCollector collector){
		if(!type.equals(sequenceType)) {
			instance = null;
		}
		if(instance == null){
			
			synchronized(CustomMatrixWindow.class){
				if(instance == null){
					instance = new CustomMatrixWindow(type, collector);
				}
			}
			
		}
		return instance;
	}
	
	public CustomMatrixWindow(Definitions type, ArgumentCollector collector){
		
		this.sequenceType = type;
		setTitle("Custom Matrix");
		setBounds(100, 100, 450, 300);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//create the JPanel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		CustomMatrixHeader customMatrixTab = new CustomMatrixHeader(type);
		contentPane.add(customMatrixTab, BorderLayout.PAGE_START);
		
		//create panel for button controls
		controlPane = new JPanel();
		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				collector.setCustomMatrixRowHeader(customMatrixTab.getRowHeaders());
				collector.setCustomMatrixColumnHeader(customMatrixTab.getColumnHeaders());
				collector.setMatrixData(customMatrixTab.getScoringMatrix());
				dispose();
			}
			
		});
		//TOOD update
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				dispose();
			}
					
		});
		controlPane.add(updateButton);
		controlPane.add(cancelButton);
		add(controlPane);
		
		this.pack();
	}
}
