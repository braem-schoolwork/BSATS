package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.JComponent;

public class SequenceCell extends JComponent {
	private String value;
	private ArrayList<ArrowDirections> directions;
	private Color backgroundColor;
	private final int SIZE = 50;
	private Color weightedBackgroundColor;
	
	public enum ArrowDirections{
		NORTH, NORTHWEST, WEST;
	}

	public SequenceCell(String value){
		this.value = value;
		directions = new ArrayList<>();
		weightedBackgroundColor = new Color(0,0,0,0);
	}
	
	public void addDirection(ArrowDirections direction){
		if(!directions.contains(direction)){
			directions.add(direction);
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Color oldColor = g.getColor();
		g.setColor(weightedBackgroundColor);
		g.fillRect(0, 0, SIZE-1, SIZE-1);
		g.setColor(oldColor);
		g.drawRect(0, 0, SIZE-1, SIZE-1);
		
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int stringWidth = metrics.stringWidth(value);
		int stringHeight = metrics.getAscent();
		

		int midpoint = (SIZE-1)/2;
		Polygon NWtriangle = new Polygon(new int[]{0,0,5}, new int[]{0,5,0}, 3);
		Polygon Wtriangle = new Polygon(new int[]{midpoint, midpoint-4,midpoint+4}, new int[]{0,4,4}, 3);
		Polygon Ntriangle = new Polygon(new int[]{0,4,4},new int[]{midpoint,midpoint+4,midpoint-4},3);
		g.setColor(Color.RED);
		
		if(directions.contains(ArrowDirections.NORTH)){
			g.drawPolygon(Ntriangle);
			g.fillPolygon(Ntriangle);
		} else if (directions.contains(ArrowDirections.NORTHWEST)){
			g.drawPolygon(NWtriangle);
			g.fillPolygon(NWtriangle);
		} else if (directions.contains(ArrowDirections.WEST)){
			g.drawPolygon(Wtriangle);
			g.fillPolygon(Wtriangle);
		}
		
		g.setColor(oldColor);
		g.drawString(value, (SIZE-stringWidth)/2, (SIZE+stringHeight)/2);
		
	}
	
	@Override
	public Dimension getPreferredSize() {return new Dimension(SIZE,SIZE);};
	
	@Override 
	public Dimension getMinimumSize() {return new Dimension(SIZE,SIZE);};
	
	@Override
	public Dimension getMaximumSize() {return new Dimension(SIZE,SIZE);}

	public void setCellBackground(Color weightedBackgroundColor) {
		this.weightedBackgroundColor = weightedBackgroundColor;
		repaint();		
	};
}
