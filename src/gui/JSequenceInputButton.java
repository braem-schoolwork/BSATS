package gui;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JSequenceInputButton extends JButton {
	private JPanel inputScope;

	public JSequenceInputButton(String inputString){
		super(inputString);
		//Prevent button from stealing input focus
		setFocusable(false);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Component focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
				if(null != focusedComponent && focusedComponent instanceof JTextField){
					JTextField focusedTextField = (JTextField) focusedComponent;
					String currentText = focusedTextField.getText();
					focusedTextField.setText(currentText + inputString);
				}
			}
		});
	}
}
