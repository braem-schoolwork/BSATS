package gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Filters text input into all JTextFields allowing only
 * nucleotide base pairs ATGC(U) and protein aminos to be
 * entered as input.
 * 
 * @author Noah
 *
 */
public class DNAFilter extends DocumentFilter{
	Document document;
	StringBuilder sb;
	Pattern pattern = Pattern.compile("[ATGC]*");
	Matcher matcher;
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException{
		document = fb.getDocument();
		sb = new StringBuilder();
		sb.append(document.getText(0, document.getLength()));
		sb.insert(offset, string);
		
		if(validate(sb.toString())){
			super.insertString(fb, offset, string, attr);
		}
		else{
			//show error
		}
	}
	public void removeString(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException{
		document = fb.getDocument();
		sb = new StringBuilder();
		sb.append(document.getText(0, document.getLength()));
		sb.delete(offset, offset+length);
		
		if(validate(sb.toString())){
			super.remove(fb, offset, length);
		}
		else{
			//error
		}
	}
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
		document = fb.getDocument();
		sb = new StringBuilder();
		sb.append(document.getText(0, document.getLength()));
		sb.replace(offset, offset+length, text);
		
		if(validate(sb.toString())){
			super.replace(fb, offset, length, text, attrs);
		}
		else{
			//error
		}
	}
	//check if value from text field is an integer
	public boolean validate(String text){
		 	
	        matcher = pattern.matcher(text);
	        boolean isMatch = matcher.matches();
	        return isMatch;
	}

}
