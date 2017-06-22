package gui;

import gui.SequenceCell.ArrowDirections;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import structures.ObservableMatrix;

public class SequenceGrid extends JPanel {

	public SequenceGrid(ObservableMatrix<Integer> matrix, Color backgroundColor) {
		Integer maxValue = 0;
		for (int i = 0; i < matrix.length(); i++) {
			for (int j = 0; j < matrix.length(i); j++) {
				// Determine max value for bg coloring
				try {
					if (matrix.get(i, j) > maxValue) {
						maxValue = matrix.get(i, j);
					}
				} catch (NullPointerException e) {
					// panic()
				}
			}
		}

		if (null != matrix) {
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.FIRST_LINE_START;

			this.setLayout(layout);

			for (int i = 0; i < matrix.length(); i++) {
				for (int j = 0; j < matrix.length(i); j++) {
					c.gridx = j;
					c.gridy = i;

					// Make the last elements have all the weight so they push
					// the
					// other elements to the top left
					if (i == matrix.length() - 1) {
						c.weighty = 1;
					} else {
						c.weighty = 0;
					}

					if (j == matrix.length(i) - 1) {
						c.weightx = 1;
					} else {
						c.weightx = 0;
					}

					Integer elementValue = 0;

					String element;
					try {
						elementValue = matrix.get(i, j);
						element = elementValue.toString();
						if (elementValue > maxValue) {
							maxValue = elementValue;
						}
					} catch (NullPointerException e) {
						elementValue = Integer.MIN_VALUE;
						element = "";
					}

					SequenceCell cell = new SequenceCell(element.toString());

					Integer westValue = null;
					Integer northWestValue = null;
					Integer northValue = null;

					if (i > 0) {
						westValue = matrix.get(i - 1, j);
					}

					if (j > 0) {
						northValue = matrix.get(i, j - 1);
					}

					if (i > 0 && j > 0) {
						northWestValue = matrix.get(i - 1, j - 1);
					}

					if (null == westValue) {
						westValue = Integer.MIN_VALUE;
					}

					if (null == northValue) {
						northValue = Integer.MIN_VALUE;
					}

					if (null == northWestValue) {
						northWestValue = Integer.MIN_VALUE;
					}

					Integer maxAdjacent = Math.max(westValue,
							Math.max(northValue, northWestValue));

					if (maxAdjacent != Integer.MIN_VALUE && elementValue != Integer.MIN_VALUE) {
						if (westValue.equals(maxAdjacent)
								&& null != elementValue) {
							cell.addDirection(ArrowDirections.WEST);
						}

						if (northValue.equals(maxAdjacent)
								&& null != elementValue) {
							cell.addDirection(ArrowDirections.NORTH);
						}

						if (northWestValue.equals(maxAdjacent)
								&& null != elementValue) {
							cell.addDirection(ArrowDirections.NORTHWEST);
						}
					}

					double backgroundWeight = Math.min(1.0,
							Math.max(0.0, elementValue / ((double) maxValue)));
					Color weightedBackgroundColor = new Color(
							backgroundColor.getRed(),
							backgroundColor.getGreen(),
							backgroundColor.getBlue(),
							(int) (255 * backgroundWeight));
					cell.setCellBackground(weightedBackgroundColor);

					this.add(cell, c);
				}
			}
		}

		this.setMaximumSize(new Dimension(50 * matrix.length(), 50 * matrix
				.length()));
	}
}
