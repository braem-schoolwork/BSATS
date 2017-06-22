package gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Builds the associated panels for whatever sequence type (DNA, RNA, protein) is selected from
 * the main window. Also will return an 2d integer array representation of the scoring matrix.
 * 
 * @author Noah
 *
 */

public class CustomMatrixHeader extends JPanel{
	
	private JTextField[][] textFieldMatrix;
	private String[] rowHeader;
	private String[] columnHeader;
	
	CustomMatrixHeader(Definitions tab){
		if(tab.equals(Definitions.DNA))
			buildDNAPane();
		else if(tab.equals(Definitions.RNA))
			buildRNAPane();
		else if(tab.equals(Definitions.PROTEIN))
			buildProteinPane();
	}
	/**
	 * Returns an integer representation of the Custom Matrix text fields.
	 * If a value is not entered in a text field a zero is placed in the
	 * corresponding spot in the integer array.
	 * @return 2D int array corresponding to the scores.
	 */
	public int[][] getScoringMatrix(){
		int[][] scoringMatrix = new int[textFieldMatrix.length][textFieldMatrix[0].length];
		
		for(int i=0; i<scoringMatrix.length; i++){
			for(int j=0; j<scoringMatrix[i].length; j++){
				if(textFieldMatrix[i][j].getText().isEmpty())
					scoringMatrix[i][j] = Integer.parseInt(textFieldMatrix[j][i].getText().toString());
				else
					scoringMatrix[i][j] = Integer.parseInt(textFieldMatrix[i][j].getText().toString());
			}
		}
		return scoringMatrix;
	}

	/**
	 * Returns the row headers of the window.
	 * @return string array with each element corresponding to a header.
	 */
	public String getRowHeaders()
	{
		String row = "";
		for(String element : rowHeader)
			row += element;
		return row;
	}

	/**
	 * Returns the column headers of the window.
	 * @return string array with each element corresponding to a header.
	 */
	public String getColumnHeaders()
	{
		String column = "";
		for(String element : columnHeader)
			column += element;
		return column;
	}
	/**
	 * Constructs DNA scoring matrix in gui
	 */
	private void buildDNAPane() {

		//Array for dna base labels
		JLabel[] dnaLabels = new JLabel[8]; 
		
		dnaLabels[0] = new JLabel("Ade");
		dnaLabels[1] = new JLabel("Cys");
		dnaLabels[2] = new JLabel("Gua");
		dnaLabels[3] = new JLabel("Thy");
		dnaLabels[4] = new JLabel("Ade");
		dnaLabels[5] = new JLabel("Cys");
		dnaLabels[6] = new JLabel("Gua");
		dnaLabels[7] = new JLabel("Thy");
		
		//create all the input fields
		textFieldMatrix = new JTextField[4][4];
		rowHeader = new String[4];
		columnHeader = new String[4];
		
		for(int i=0; i<textFieldMatrix.length; i++)
			for(int j=0; j<textFieldMatrix[i].length; j++)
				if(i == j)
					textFieldMatrix[i][j] = new JTextField("1");
				else
					textFieldMatrix[i][j] = new JTextField("-1");
		
		//have the matrix sit in its own panel
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(0,5));
		
		//Populate the grid with components
		int k = 0; //place an jlabel as aligner after row filled
		
		for(int i=0; i<4 ;i++){
			
			gridPanel.add(dnaLabels[i]);

			
			for(int j=0; j<4 ;j++){
				rowHeader[i] = dnaLabels[i].getText().substring(0, 1);
				if(j <= k){
					gridPanel.add(textFieldMatrix[i][j]);
				}
				else{
					gridPanel.add(new JLabel());
				}	
			}
			k +=1;
		}
		gridPanel.add(new JLabel());
		for(int i=4; i< 8; i++){
			gridPanel.add(dnaLabels[i]);
			columnHeader[i-4] = dnaLabels[i].getText().substring(0, 1);
		}
		add(gridPanel);
	}
/**
 * Constructs RNA scoring matrix in gui
 */
public void buildRNAPane() {
	
		//Array for dna base labels
		JLabel[] rnaLabels = new JLabel[8]; 
		
		rnaLabels[0] = new JLabel("Ade");
		rnaLabels[1] = new JLabel("Cys");
		rnaLabels[2] = new JLabel("Gua");
		rnaLabels[3] = new JLabel("Ura");
		rnaLabels[4] = new JLabel("Ade");
		rnaLabels[5] = new JLabel("Cys");
		rnaLabels[6] = new JLabel("Gua");
		rnaLabels[7] = new JLabel("Ura");
		
		//create all the input fields
		textFieldMatrix = new JTextField[4][4];
		rowHeader = new String[4];
		columnHeader = new String[4];

		for(int i=0; i<textFieldMatrix.length; i++)
			for(int j=0; j<textFieldMatrix[i].length; j++)
				if(i == j)
					textFieldMatrix[i][j] = new JTextField("1");
				else
					textFieldMatrix[i][j] = new JTextField("-1");

		//have the matrix sit in its own panel
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(0,5));

		//Populate the grid with components
		int k = 0; //place an jlabel as aligner after row filled

		for(int i=0; i<4 ;i++){

			gridPanel.add(rnaLabels[i]);
			rowHeader[i] = rnaLabels[i].getText().substring(0, 1);

			for(int j=0; j<4 ;j++){

				if(j <= k){
					gridPanel.add(textFieldMatrix[i][j]);
				}
				else{
					gridPanel.add(new JLabel());
				}
			}
			k +=1;
		}
		gridPanel.add(new JLabel());
		for(int i=4; i< 8; i++){
			gridPanel.add(rnaLabels[i]);
			columnHeader[i-4] = rnaLabels[i].getText().substring(0, 1);
		}
		add(gridPanel);
	}
/*
 * Constructs protein scoring matrix in gui
 */
	public void buildProteinPane() {
		
		//Array for protein base labels
		JLabel[] proteinLabels = new JLabel[42];
		
		proteinLabels[0] = new JLabel("A");
		proteinLabels[1] = new JLabel("R");
		proteinLabels[2] = new JLabel("N");
		proteinLabels[3] = new JLabel("D");
		proteinLabels[4] = new JLabel("C");
		proteinLabels[5] = new JLabel("Q");
		proteinLabels[6] = new JLabel("E");
		proteinLabels[7] = new JLabel("G");
		proteinLabels[8] = new JLabel("H");
		proteinLabels[9] = new JLabel("I");
		proteinLabels[10] = new JLabel("L");
		proteinLabels[11] = new JLabel("K");
		proteinLabels[12] = new JLabel("M");
		proteinLabels[13] = new JLabel("F");
		proteinLabels[14] = new JLabel("P");
		proteinLabels[15] = new JLabel("S");
		proteinLabels[16] = new JLabel("T");
		proteinLabels[17] = new JLabel("W");
		proteinLabels[18] = new JLabel("Y");
		proteinLabels[19] = new JLabel("V");
		proteinLabels[20] = new JLabel("*");
		
		proteinLabels[21] = new JLabel("A");
		proteinLabels[22] = new JLabel("R");
		proteinLabels[23] = new JLabel("N");
		proteinLabels[24] = new JLabel("D");
		proteinLabels[25] = new JLabel("C");
		proteinLabels[26] = new JLabel("Q");
		proteinLabels[27] = new JLabel("E");
		proteinLabels[28] = new JLabel("G");
		proteinLabels[29] = new JLabel("H");
		proteinLabels[30] = new JLabel("I");
		proteinLabels[31] = new JLabel("L");
		proteinLabels[32] = new JLabel("K");
		proteinLabels[33] = new JLabel("M");
		proteinLabels[34] = new JLabel("F");
		proteinLabels[35] = new JLabel("P");
		proteinLabels[36] = new JLabel("S");
		proteinLabels[37] = new JLabel("T");
		proteinLabels[38] = new JLabel("W");
		proteinLabels[39] = new JLabel("Y");
		proteinLabels[40] = new JLabel("V");
		proteinLabels[41] = new JLabel("*");
				
		//create all the input fields
		textFieldMatrix = new JTextField[21][21];
		rowHeader = new String[21];
		columnHeader = new String[21];

		for(int i=0; i<textFieldMatrix.length; i++)
			for(int j=0; j<textFieldMatrix[i].length; j++)
				if(i == j)
					textFieldMatrix[i][j] = new JTextField("1");
				else
					textFieldMatrix[i][j] = new JTextField("-1");

		//have the matrix sit in its own panel
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(0,22));

		//Populate the grid with components
		int k = 0; //place an jlabel as aligner after row filled

		for(int i=0; i<21 ;i++){

			gridPanel.add(proteinLabels[i]);

			for(int j=0; j<21 ;j++){
				rowHeader[i] = proteinLabels[i].getText().substring(0, 1);
				if(j <= k){
					gridPanel.add(textFieldMatrix[i][j]);
				}
				else{
					gridPanel.add(new JLabel());
				}
			}
			k +=1;
		}
		gridPanel.add(new JLabel());
		for(int i=21; i< 42; i++){
			gridPanel.add(proteinLabels[i]);
			columnHeader[i-21] = proteinLabels[i].getText().substring(0, 1);
		}
		add(gridPanel);
	}

}
