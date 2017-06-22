package structures;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

public class ObservableMatrix<E> extends Observable implements Iterable {
	// Currently a barebones Matrix class - only implemented the observable
	// stuff

	private final Class<E> class_definition;
	private E[][] matrix;
	private ArrayList<Observer> observers = new ArrayList<Observer>();

	@SuppressWarnings("unchecked")
	public ObservableMatrix(Class<E> c, int width, int height) {
		matrix = (E[][]) Array.newInstance(c, height, width);
		this.class_definition = c;
	}

	@SuppressWarnings("unchecked")
	public ObservableMatrix(Class<E> c, E[][] input_matrix) {
		matrix = (E[][]) Array.newInstance(c, input_matrix.length,
				input_matrix[0].length);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				this.set(i, j, input_matrix[i][j]);
			}
		}
		this.class_definition = c;
	}

	public ObservableMatrix(Class<E> c) {
		this.class_definition = c;
	}

	/**
	 * Sets a value in the matrix and informs the observable.
	 * 
	 * @param row
	 *            x position being updated
	 * @param column
	 *            y position being updated
	 * @param value
	 *            generic type being used.
	 */
	public void set(int row, int column, E value) {
		matrix[row][column] = value;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	/**
	 * Returns the specified value in the matrix.
	 * 
	 * @param row
	 *            the x position to get the value from.
	 * @param column
	 *            the y position to get the value from.
	 * @return the value specified of type E.
	 */
	public E get(int row, int column) {
		return matrix[row][column];
	}

	/**
	 * Clears the internal matrix by setting it to null.
	 */
	public void clearInternalMatrix() {
		this.matrix = null;
	}

	public void initializeNewInternalMatrix(int num_rows, int num_cols) {
		this.matrix = (E[][]) Array.newInstance(class_definition, num_rows,
				num_cols);
	}

	/**
	 * Returns the length of the first column of the matrix (number of rows or
	 * height).
	 * 
	 * @return number of rows.
	 */
	public int length() {
		return matrix.length;
	}

	/**
	 * Returns the length of the nth row of the matrix (number of columns or
	 * width).
	 * 
	 * @return number of columns in nth row.
	 */
	public int length(int n) {
		return matrix[n].length;
	}

	@Override
	public Iterator iterator() {
		return new ObservableMatrixIterator();
	}

	private class ObservableMatrixIterator implements Iterator {
		private int row_cursor = 0;
		private int column_cursor = 0;

		public boolean hasNext() {
			return (row_cursor < ObservableMatrix.this.matrix.length);
		}

		public E next() {
			if (this.hasNext()) {
				E value = ObservableMatrix.this.get(row_cursor, column_cursor);
				column_cursor = (column_cursor + 1)
						% ObservableMatrix.this.matrix[row_cursor].length;
				if (column_cursor == 0)
					row_cursor++;
				return value;
			}
			throw new NoSuchElementException();
		}
	}

	@Override
	public synchronized void addObserver(Observer o) {
		observers.add(o);
		super.addObserver(o);
	}

	@Override
	public synchronized void deleteObservers() {
		observers = new ArrayList<Observer>();
		super.deleteObservers();
	}

	@Override
	public synchronized void deleteObserver(Observer o) {
		observers.remove(o);
		super.deleteObserver(o);
	}

	public synchronized void transferObserversFrom(ObservableMatrix<E> oldMatrix) {
		for (Observer obs : oldMatrix.observers) {
			this.addObserver(obs);
		}
	}

	public String toString() {
		String string = "{";
		for (int j = 0; j < matrix.length; j++) {
			string += "{";
			E[] list = matrix[j];
				for (int i = 0; i < list.length; i++) {
					try {
						string += list[i].toString();
					} catch (NullPointerException e){
						string += "<null>";
					}
					if (i + 1 < list.length) {
						string += ",";
					} else {
						string += "}";
					}
				}
			if (j + 1 < matrix.length) {
				string += ",";
			} else {
				string += "}";
			}
		}
		return string;
	}

	public void markFinished() {
		setChanged();
		notifyObservers();
	}
}
