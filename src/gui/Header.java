package gui;

import gui.ArgumentCollector.Algorithm;
import gui.ArgumentCollector.Event;
import gui.ArgumentCollector.Matrix;

import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javafx.embed.swing.JFXPanel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import neobio.alignment.InvalidScoringMatrixException;
import neobio.alignment.InvalidSequenceException;
import neobio.alignment.ScoringMatrix;
import neobio.alignment.Steppable.NeedlemanWunschSteppableAlgorithm;
import neobio.alignment.Steppable.SmithWatermanSteppableAlgorithm;
import neobio.alignment.Steppable.SteppableAlgorithm;
import structures.ObservableMatrix;
import structures.StringHelper;
import FileIO.Parser;



public class Header extends JPanel implements Observer {
	private SteppableAlgorithm algorithm;
	private ArgumentCollector collector;
	private ObservableMatrix<?> algorithmData = new ObservableMatrix<>(
			Integer.class, new Integer[][] { {1} });
	private JPanel inputButtons;
	private JPanel matrixPanel;
	private boolean layoutInitialized;
	private JTextField sequenceAField;
	private JTextField sequenceBField;
	private HelpMenuMouseListener sequenceFieldMListener;
	private JFXPanel helpPage;
	private JButton editMatrixButton = new JButton("Edit Matrix");
	private JComboBox<String> dnaSchemaSelector;
	private JComboBox<String> rnaSchemaSelector;
	private JComboBox<String> proteinSchemaSelector;

	public Header(JFXPanel helpPage) {
	    this.helpPage = helpPage;
		layoutInitialized = false;
		collector = new ArgumentCollector();
		collector.addObserver(this);

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		// Sequence Selection
		String[] sequenceTypes = { "DNA", "RNA", "Protein" };
		String[] sequencePages = {"res/gui_help/dna.html", "res/gui_help/rna.html", "res/gui_help/protein.html"};
		JComboBox<String> sequenceList = new JComboBox<String>(sequenceTypes);

		// Default values
		sequenceList.setSelectedIndex(0);
		collector.setType(Definitions.DNA);

		sequenceList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Convert the selected text into the ArgumentCollector enum
				String sequenceType = (String) sequenceList.getSelectedItem();
				collector.setType(Definitions.valueOf(sequenceType.toUpperCase()));
			}
		});
        ComboboxPageRenderer sequenceRenderer = new ComboboxPageRenderer(helpPage, sequencePages);
        sequenceList.setRenderer(sequenceRenderer);

        sequenceAField = new JTextField(15);

		// Default value
		sequenceAField.setText("");
		collector.setSequenceA("");
		//Add a listener for when the contents in the field change.
		sequenceAField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				collector.setSequenceA(sequenceAField.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				collector.setSequenceA(sequenceAField.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				collector.setSequenceA(sequenceAField.getText());
			}
		});
		((AbstractDocument) sequenceAField.getDocument()).setDocumentFilter(new DNAFilter());

		sequenceBField = new JTextField(15);
		// Default value
		sequenceBField.setText("");
		collector.setSequenceB("");
		//Add a listener for when the contents in the field change.
		sequenceBField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				collector.setSequenceB(sequenceBField.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				collector.setSequenceB(sequenceBField.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				collector.setSequenceB(sequenceBField.getText());
			}
		});
		((AbstractDocument) sequenceBField.getDocument()).setDocumentFilter(new DNAFilter());


		// Input Buttons
		CardLayout inputCardLayout = new CardLayout();
		inputButtons = new JPanel(inputCardLayout);
		GridLayout buttonGridLayout = new GridLayout(0,4);

		//RNA Input Buttons
		JPanel RNAInputButtons = new JPanel(buttonGridLayout);
		JSequenceInputButton rnaA = new JSequenceInputButton("A");
		JSequenceInputButton rnaU = new JSequenceInputButton("U");
		JSequenceInputButton rnaG = new JSequenceInputButton("G");
		JSequenceInputButton rnaC = new JSequenceInputButton("C");
		try {
            rnaA.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/adenine.html"));
            rnaU.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/uracil.html"));
            rnaG.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/guanine.html"));
            rnaC.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/cytosine.html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RNAInputButtons.add(rnaA); RNAInputButtons.add(rnaU); RNAInputButtons.add(rnaG); RNAInputButtons.add(rnaC);
		inputButtons.add(RNAInputButtons, "RNA");

		//DNA Input Buttons
		JPanel DNAInputButtons = new JPanel(buttonGridLayout);
		JSequenceInputButton dnaA = new JSequenceInputButton("A");
		JSequenceInputButton dnaT = new JSequenceInputButton("T");
		JSequenceInputButton dnaG = new JSequenceInputButton("G");
		JSequenceInputButton dnaC = new JSequenceInputButton("C");
		try {
            dnaA.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/adenine.html"));
            dnaT.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/thymine.html"));
            dnaG.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/guanine.html"));
            dnaC.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/cytosine.html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DNAInputButtons.add(dnaA); DNAInputButtons.add(dnaT); DNAInputButtons.add(dnaG); DNAInputButtons.add(dnaC);
		inputButtons.add(DNAInputButtons, "DNA");

		//Protein Input Buttons
		JPanel proteinInputButtons = new JPanel(buttonGridLayout);
		JSequenceInputButton proA = new JSequenceInputButton("A");
		JSequenceInputButton proR = new JSequenceInputButton("R");
		JSequenceInputButton proN = new JSequenceInputButton("N");
		JSequenceInputButton proD = new JSequenceInputButton("D");
		JSequenceInputButton proC = new JSequenceInputButton("C");
		JSequenceInputButton proQ = new JSequenceInputButton("Q");
		JSequenceInputButton proE = new JSequenceInputButton("E");
		JSequenceInputButton proG = new JSequenceInputButton("G");
		JSequenceInputButton proH = new JSequenceInputButton("H");
		JSequenceInputButton proI = new JSequenceInputButton("I");
		JSequenceInputButton proL = new JSequenceInputButton("L");
		JSequenceInputButton proK = new JSequenceInputButton("K");
		JSequenceInputButton proM = new JSequenceInputButton("M");
		JSequenceInputButton proF = new JSequenceInputButton("F");
		JSequenceInputButton proP = new JSequenceInputButton("P");
		JSequenceInputButton proS = new JSequenceInputButton("S");
		JSequenceInputButton proT = new JSequenceInputButton("T");
		JSequenceInputButton proW = new JSequenceInputButton("W");
		JSequenceInputButton proY = new JSequenceInputButton("Y");
		JSequenceInputButton proV = new JSequenceInputButton("V");
		try {
		    proA.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/alanine.html"));
		    proR.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/arginine.html"));
		    proN.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/asparagine.html"));
		    proD.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/aspartic.html"));
		    proC.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/cysteine.html"));
		    proQ.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/glutamine.html"));
		    proE.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/glutamic.html"));
		    proG.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/glycine.html"));
		    proH.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/histidine.html"));
		    proI.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/isoleucine.html"));
		    proL.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/leucine.html"));
		    proK.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/lysine.html"));
		    proM.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/methionine.html"));
		    proF.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/phenylalanine.html"));
		    proP.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/proline.html"));
		    proS.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/serine.html"));
		    proT.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/threonine.html"));
		    proW.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/tryptophan.html"));
		    proY.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/tyrosine.html"));
		    proV.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/valine.html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        proteinInputButtons.add(proA); proteinInputButtons.add(proR); proteinInputButtons.add(proN); proteinInputButtons.add(proD);
        proteinInputButtons.add(proC); proteinInputButtons.add(proQ); proteinInputButtons.add(proE); proteinInputButtons.add(proG);
        proteinInputButtons.add(proH); proteinInputButtons.add(proI); proteinInputButtons.add(proL); proteinInputButtons.add(proK);
        proteinInputButtons.add(proM); proteinInputButtons.add(proF); proteinInputButtons.add(proP); proteinInputButtons.add(proS);
        proteinInputButtons.add(proT); proteinInputButtons.add(proW); proteinInputButtons.add(proY); proteinInputButtons.add(proV);
		inputButtons.add(proteinInputButtons, "PROTEIN");
		
		inputCardLayout.show(inputButtons, "DNA");

		// Algorithm Selector
		String[] algorithmNames = { "Needleman Wunsch", "Smith Waterman" };
		String[] algorithmPages = {"res/gui_help/needlemanwunsch.html", "res/gui_help/smithwaterman.html"};
		JComboBox<String> algorithmSelector = new JComboBox<String>(
				algorithmNames);

		// Default value
		algorithmSelector.setSelectedIndex(0);
		collector.setAlgorithm(Algorithm.NEEDLEMANWUNSCH);

		algorithmSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String algorithm = (String) algorithmSelector.getSelectedItem();
				if (algorithm.equals("Needleman Wunsch")) {
					collector.setAlgorithm(Algorithm.NEEDLEMANWUNSCH);
				} else if (algorithm.equals("Smith Waterman")) {
					collector.setAlgorithm(Algorithm.SMITHWATERMAN);
				}
			}
		});
		ComboboxPageRenderer algorithmRenderer = new ComboboxPageRenderer(helpPage, algorithmPages);
		algorithmSelector.setRenderer(algorithmRenderer);

		// Browse and clear buttons
		JFileChooser fileChooser = new JFileChooser();

		JButton sequenceABrowse = new JButton("...");
		sequenceABrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(Header.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						String seq = Parser.parsePath(file.toPath());
						sequenceAField.removeAll();
						sequenceAField.setText(seq);
					} catch (IOException exc) {

					}
				}
			}
		});

		JButton sequenceBBrowse = new JButton("...");
		sequenceBBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(Header.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						String seq = Parser.parsePath(file.toPath());
						sequenceBField.removeAll();
						sequenceBField.setText(seq);
					} catch (IOException exc) {

					}
				}
			}
		});

		// Algorithm control buttons
		JButton play = new JButton("Play");
		JButton stepForward = new JButton("Step Forward");
		JButton stepBackward = new JButton("Step Backward");
		JPanel algorithmControls = new JPanel();
		algorithmControls.add(play);
		algorithmControls.add(stepForward);
		algorithmControls.add(stepBackward);

		ActionListener algorithmControlsListener = e -> {
            if(collector.areYouReady())
            {
                if (e.getSource() == play)
                    algorithm.full();
                else if (e.getSource() == stepForward)
                    algorithm.stepForward();
                else if (e.getSource() == stepBackward)
                    algorithm.stepBackward();
            }
        };

		play.addActionListener(algorithmControlsListener);
		stepForward.addActionListener(algorithmControlsListener);
		stepBackward.addActionListener(algorithmControlsListener);

		// Scoring Selector
		CardLayout matrixCardLayout = new CardLayout();
		matrixPanel = new JPanel(matrixCardLayout);

		String[] dnaSchema = {"Simple", "Transition", "Identity", "Custom" };
		dnaSchemaSelector = new JComboBox<String>(dnaSchema);
		dnaSchemaSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = dnaSchemaSelector.getSelectedItem().toString();
				switch(selectedItem)
				{
					case "Simple": collector.setMatrixType(Matrix.SIMPLE);
						break;
					case "Transition": collector.setMatrixType(Matrix.TRANSITION);
						break;
					case "Identity": collector.setMatrixType(Matrix.IDENTITY);
						break;
					case "Custom": collector.setMatrixType(Matrix.NCUSTOM);
						break;
				}
				collector.setCustomMatrixColumnHeader("ACGT");
				collector.setCustomMatrixRowHeader("ACGT");
			}
		});

		String[] rnaSchema = {"Simple", "Transition", "Identity", "Custom" };
		rnaSchemaSelector = new JComboBox<String>(rnaSchema);
		rnaSchemaSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = rnaSchemaSelector.getSelectedItem().toString();
				switch(selectedItem)
				{
					case "Simple": collector.setMatrixType(Matrix.SIMPLE);
						break;
					case "Transition": collector.setMatrixType(Matrix.TRANSITION);
						break;
					case "Identity": collector.setMatrixType(Matrix.IDENTITY);
						break;
					case "Custom": collector.setMatrixType(Matrix.NCUSTOM);
						break;
				}
				collector.setCustomMatrixColumnHeader("ACGU");
				collector.setCustomMatrixRowHeader("ACGU");
			}

		});

		String[] proteinSchema = {"PAM100", "PAM150", "PAM250", "BLOSSUM60", "BLOSSUM62", "Custom"};
		proteinSchemaSelector = new JComboBox<String>(proteinSchema);
		proteinSchemaSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = proteinSchemaSelector.getSelectedItem().toString();
				switch(selectedItem)
				{
					case "PAM100": collector.setMatrixType(Matrix.PAM100);
						break;
					case "PAM150": collector.setMatrixType(Matrix.PAM150);
						break;
					case "PAM250": collector.setMatrixType(Matrix.PAM250);
						break;
					case "BLOSSUM60": collector.setMatrixType(Matrix.BLOSSUM60);
						break;
					case "BLOSSUM62": collector.setMatrixType(Matrix.BLOSSUM62);
						break;
					case "Custom": collector.setMatrixType(Matrix.PCUSTOM);
						break;
				}
				collector.setCustomMatrixColumnHeader("ARNDCQEGHILKMFPSTWYV");
				collector.setCustomMatrixRowHeader("ARNDCQEGHILKMFPSTWYV");
			}
		});

        try {
            dnaSchemaSelector.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/scoringmatrix.html"));
            rnaSchemaSelector.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/scoringmatrix.html"));
            proteinSchemaSelector.addMouseListener(new HelpMenuMouseListener(helpPage,"res/gui_help/scoringmatrix.html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Default value
		dnaSchemaSelector.setSelectedIndex(0);
		collector.setMatrixType(Matrix.SIMPLE);

		matrixPanel.add(dnaSchemaSelector, "DNA");
		matrixPanel.add(rnaSchemaSelector, "RNA");
		matrixPanel.add(proteinSchemaSelector, "PROTEIN");
		matrixCardLayout.show(matrixPanel, "DNA");

		editMatrixButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Create the custom matrix frame
				Definitions customMatrixWindowType = collector.getType();
				CustomMatrixWindow customMatrixWindow = CustomMatrixWindow.getInstance(customMatrixWindowType, collector);
				//CustomMatrixWindow customMatrixWindow = new CustomMatrixWindow(
					//	customMatrixWindowType, collector);

				customMatrixWindow.setLocationRelativeTo(null);
				customMatrixWindow.setVisible(true);
			}
		});
		//editMatrixButton.setEnabled(false);

		// Gap Penalty button
		collector.setGapPenalty(1);
		JPanel gapPenalty = new JPanel();
		JLabel gapPenaltyLabel = new JLabel("Gap Penalty");
		JTextField gapPenaltyText = new JTextField("1",2);
		((AbstractDocument) gapPenaltyText.getDocument()).setDocumentFilter(new GapFilter());
		gapPenaltyText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = gapPenaltyText.getText();
				collector.setGapPenalty(Integer.valueOf(text) * -1);
			}
		});
		collector.setGapPenalty(-1);
		gapPenalty.add(gapPenaltyLabel);
		gapPenalty.add(gapPenaltyText);
		
		
		// Help Button
		JButton helpButton = new JButton("?");
		helpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					HelpmenuWindow helpWindow = new HelpmenuWindow();
					helpWindow.setVisible(true);
					//Desktop.getDesktop().browse(new File("res/menu.html").toURI());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		// Hover listeners for all components
		try {
		    sequenceFieldMListener = new HelpMenuMouseListener(helpPage,
					"res/gui_help/dnasequence.html");
			sequenceList.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/sequenceType.html"));
			sequenceAField.addMouseListener(sequenceFieldMListener);
			sequenceBField.addMouseListener(sequenceFieldMListener);
			algorithmSelector.addMouseListener(new HelpMenuMouseListener(
					helpPage, "res/gui_help/algorithm.html"));
			sequenceABrowse.addMouseListener(new HelpMenuMouseListener(
					helpPage, "res/gui_help/choosefile.html"));
			sequenceBBrowse.addMouseListener(new HelpMenuMouseListener(
					helpPage, "res/gui_help/choosefile.html"));
			matrixPanel.addMouseListener(new HelpMenuMouseListener( //changed from schemaselector
					helpPage, "res/gui_help/scoringmatrix.html"));
			editMatrixButton.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/custommatrix.html"));
			gapPenalty.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/gappenalty.html"));
			play.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/play.html"));
			stepForward.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/stepforward.html"));
			stepBackward.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/stepbackward.html"));
			helpButton.addMouseListener(new HelpMenuMouseListener(helpPage,
					"res/gui_help/help.html"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup().addComponent(sequenceList)
								.addComponent(sequenceAField)
								.addComponent(sequenceBField)
								.addComponent(inputButtons))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(algorithmSelector) //TODO change this to algorithmPanel
								.addComponent(sequenceABrowse)
								.addComponent(sequenceBBrowse)
								.addComponent(algorithmControls))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(matrixPanel)
								.addComponent(editMatrixButton)
								.addComponent(gapPenalty)
								.addComponent(helpButton)));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup().addComponent(sequenceList)
								.addComponent(algorithmSelector)
								.addComponent(matrixPanel))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(sequenceAField)
								.addComponent(sequenceABrowse)
								.addComponent(editMatrixButton))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(sequenceBField)
								.addComponent(sequenceBBrowse)
								.addComponent(gapPenalty))
				.addGroup(
						layout.createParallelGroup(Alignment.CENTER).addComponent(inputButtons)
								.addComponent(algorithmControls)
								.addComponent(helpButton)));

		collector.readyToRock();
		layoutInitialized = true;

		try {
			algorithmData = new ObservableMatrix<>(Integer.class);
			algorithm = new NeedlemanWunschSteppableAlgorithm(
					collector.getSequenceA(), collector.getSequenceB(), new ScoringMatrix(
							collector.getCustomMatrixColumnHeader(),
							collector.getCustomMatrixRowHeader(),
							collector.getMatrixData(),
							collector.getGapPenalty()), algorithmData);
		} catch (InvalidScoringMatrixException e) {
			System.err.println("Error setting default scoring scheme: " + e);
		} catch (InvalidSequenceException e) {
			System.err.println("Error setting default sequences: " + e);
		}


	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//Update algorithm		
		try {
			if(algorithm != null && arg1 != null)
			{
				switch ((Event) arg1) {
					case ALGORITHM_CHANGE:
						switch (collector.getAlgorithm()) {
							case NEEDLEMANWUNSCH:
								algorithm = new NeedlemanWunschSteppableAlgorithm(
										collector.getSequenceA(), collector.getSequenceB(),
										new ScoringMatrix(
												collector.getCustomMatrixColumnHeader(),
												collector.getCustomMatrixRowHeader(),
												collector.getMatrixData(),
												collector.getGapPenalty()),
												algorithmData,
												algorithm.getCurrentRow(),
												algorithm.getCurrentColumn());
								break;
							case SMITHWATERMAN:
								algorithm = new SmithWatermanSteppableAlgorithm(
										collector.getSequenceA(), collector.getSequenceB(),
										new ScoringMatrix(
												collector.getCustomMatrixColumnHeader(),
												collector.getCustomMatrixRowHeader(),
												collector.getMatrixData(),
												collector.getGapPenalty()),
												algorithmData,
												algorithm.getCurrentRow(),
												algorithm.getCurrentColumn());
								break;
						}
						break;
					case MATRIX_TYPE_CHANGE:
					case MATRIX_DATA_CHANGED:
					case GAP_PENALTY_CHANGE:
						//System.out.println(Arrays.deepToString(collector.getMatrixData()));
						algorithm.setScoringScheme(new ScoringMatrix(
								collector.getCustomMatrixColumnHeader(),
								collector.getCustomMatrixRowHeader(),
								collector.getMatrixData(),
								collector.getGapPenalty()
						));
						break;
					case SEQUENCE_A_CHANGE:
						break;
					case SEQUENCE_B_CHANGE:
						if(!StringHelper.isNullOrWhitespsace(collector.getSequenceA()) && !StringHelper.isNullOrWhitespsace(collector.getSequenceB()))
							algorithm.loadSequences(collector.getSequenceA(), collector.getSequenceB());
						break;
					case SEQUENCE_TYPE_CHANGE:
						algorithm.setScoringScheme(new ScoringMatrix(collector
								.getCustomMatrixColumnHeader(), collector
								.getCustomMatrixRowHeader(), collector
								.getMatrixData(), collector.getGapPenalty()));
						algorithm.loadSequences(collector.getSequenceA(),
								collector.getSequenceB());
						break;
					default:
						// panic();
						break;
				}
				if(collector.areYouReady())
				{
					algorithm.resume();
				}

				if(null != algorithm){
					ObservableMatrix oldData = algorithmData;
					ObservableMatrix newData = algorithm.getObservableMatrix();
					if(oldData != newData){
						newData.transferObserversFrom(oldData);
						oldData.deleteObservers();
					}
					algorithmData = newData;
				}
			}
		} catch (InvalidSequenceException e) {
			System.err.println("An invalid sequence was just loaded: " + e);
		} catch (InvalidScoringMatrixException e) {
			System.err.println("An invalid scoring matrix was specified: " + e);
		}
		
		// Update input buttons
		if(layoutInitialized && (Event) arg1 == Event.SEQUENCE_TYPE_CHANGE){
			CardLayout inputButtonCardLayout = (CardLayout) inputButtons.getLayout();
			inputButtonCardLayout.show(inputButtons, collector.getType().toString());
		}
		// Update schema combobox
		if(layoutInitialized && (Event) arg1 == Event.SEQUENCE_TYPE_CHANGE){
			CardLayout matrixCardLayout = (CardLayout) matrixPanel.getLayout();
			matrixCardLayout.show(matrixPanel, collector.getType().toString());
		}

		// Update JTextFields
		if(layoutInitialized && (Event) arg1 == Event.SEQUENCE_TYPE_CHANGE){
			DocumentFilter filter;
			switch(collector.getType()){
				default:
				case DNA:
					sequenceAField.removeMouseListener(sequenceFieldMListener);
					sequenceBField.removeMouseListener(sequenceFieldMListener);
					try {
						sequenceFieldMListener = new HelpMenuMouseListener(helpPage,
                        "res/gui_help/dnasequence.html");
					} catch (MalformedURLException e) {
                    e.printStackTrace();
					}
					sequenceAField.addMouseListener(sequenceFieldMListener);
					sequenceBField.addMouseListener(sequenceFieldMListener);
					filter = new DNAFilter();
					break;
				case PROTEIN:
					filter = new AminoFilter();
					sequenceAField.removeMouseListener(sequenceFieldMListener);
					sequenceBField.removeMouseListener(sequenceFieldMListener);
					try {
						sequenceFieldMListener = new HelpMenuMouseListener(helpPage,
								"res/gui_help/protein.html");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					sequenceAField.addMouseListener(sequenceFieldMListener);
					sequenceBField.addMouseListener(sequenceFieldMListener);
					break;
				case RNA:
					filter = new RNAFilter();
					sequenceAField.removeMouseListener(sequenceFieldMListener);
			    	sequenceBField.removeMouseListener(sequenceFieldMListener);
			    	try {
			    		sequenceFieldMListener = new HelpMenuMouseListener(helpPage,
                        "res/gui_help/rnasequence.html");
			    	} catch (MalformedURLException e) {
			    		e.printStackTrace();
			    	}
                sequenceAField.addMouseListener(sequenceFieldMListener);
			    sequenceBField.addMouseListener(sequenceFieldMListener);
				break;
			}

			((AbstractDocument) sequenceAField.getDocument()).setDocumentFilter(filter);
			((AbstractDocument) sequenceBField.getDocument()).setDocumentFilter(filter);
			
			sequenceAField.setText("");
			sequenceBField.setText("");


		}

		if((Event) arg1 == Event.MATRIX_TYPE_CHANGE){
		    System.out.println(collector.getMatrixType());
			if(collector.getMatrixType() == Matrix.NCUSTOM || collector.getMatrixType() == Matrix.PCUSTOM){
				editMatrixButton.setEnabled(true);
			} else {
				editMatrixButton.setEnabled(false);
			}
		}

		if((Event) arg1 == Event.SEQUENCE_TYPE_CHANGE && collector.areYouReady()){
			switch(collector.getType()){
			case BLANK:
				break;
			case DNA:
				dnaSchemaSelector.setSelectedIndex(0);
				collector.setMatrixType(Matrix.SIMPLE);
				break;
			case PROTEIN:
				proteinSchemaSelector.setSelectedIndex(0);
				collector.setMatrixType(Matrix.PAM100);
				break;
			case RNA:
				rnaSchemaSelector.setSelectedIndex(0);
				collector.setMatrixType(Matrix.SIMPLE);
				break;
			default:
				break;

			}
		}

	}

	ObservableMatrix<?> getObservableMatrix() {
		return algorithmData;
	}

	String getSequenceA() {
		return collector.getSequenceA();
	}

	String getSequenceB() {
		return collector.getSequenceB();
	}

	public int getScore() {
		int max = 0;
		for(Object o : algorithm.getObservableMatrix()){
			if(null != o && o instanceof Integer){
				int i = (Integer) o;
				if(i > max)
				max = i;
			}
		}
		return max;
	}

	public String getGappedSequenceA() {
		if(!algorithm.hasMore()){
			return algorithm.getSequenceAlignment()[0];
		} else {
			return "";
		}
	}

	public String getGappedSequenceB() {
		if(!algorithm.hasMore()){
			return algorithm.getSequenceAlignment()[1];
		} else {
			return "";
		}
	}
}
