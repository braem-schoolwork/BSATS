package gui;

import javafx.embed.swing.JFXPanel;

import java.awt.Font;
import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SequenceFooter extends JPanel{
	JLabel labelA = new JLabel();
	JLabel labelB = new JLabel();
	JLabel labelBridge = new JLabel();
	JLabel labelScore = new JLabel();

	public SequenceFooter(JFXPanel helpMenu){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		labelScore.setFont(new Font("Consolas",Font.PLAIN,16));
		labelA.setFont(new Font("Consolas",Font.PLAIN,16));
		labelB.setFont(new Font("Consolas",Font.PLAIN,16));
		labelBridge.setFont(new Font("Consolas",Font.PLAIN,16));
		add(labelScore);
		add(labelA);
		add(labelBridge);
		add(labelB);
		try {
			labelScore.addMouseListener(new HelpMenuMouseListener(helpMenu, "res/gui_help/score.html"));
			labelA.addMouseListener(new HelpMenuMouseListener(helpMenu, "res/gui_help/alignedsequences.html"));
			labelBridge.addMouseListener(new HelpMenuMouseListener(helpMenu, "res/gui_help/alignedsequences.html"));
			labelB.addMouseListener(new HelpMenuMouseListener(helpMenu, "res/gui_help/alignedsequences.html"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void setGappedSequences(String gapped_seq1, String gapped_seq2) {
		String bridge = "";
		for(int i=0; i<gapped_seq1.length(); i++)
			if(gapped_seq1.charAt(i) == gapped_seq2.charAt(i) && gapped_seq1.charAt(i) != '-')
				bridge += "|";
			else
				bridge += " ";
		labelA.setText(gapped_seq1);
		labelB.setText(gapped_seq2);
		labelBridge.setText(bridge);
		invalidate();
	}

	public void setScore(double score){
		java.math.BigDecimal bd = new java.math.BigDecimal(score);
		bd = bd.setScale(3, java.math.BigDecimal.ROUND_HALF_UP);
		labelScore.setText("Score: " + bd);
	}
}
