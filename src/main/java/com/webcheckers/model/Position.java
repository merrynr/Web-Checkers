package com.webcheckers.model;
/**
 * this class handles the row and column index of moves
 * @author priya, Dante Secada-Oz
 *
 */
public class Position {

	/** Row of position */
	private int row;
	/** Col of position */
	private int cell;

	/** Constructor for Position
	 */
	public Position(int row, int cell) {

		this.row = row;
		this.cell = cell;
	}

	/** Row of position
	 *
	 * @return row index of position
	 * */
	public int getRow() {
		return row;
	}

	/** Col of position
	 *
	 * @return col index of position
	 * */
	public int getCell() {
		return cell;
	}

	/** Reflect
	 *
	 * @return col index of position
	 * */
	public int reflectRow() {
		return 7 - row;
	}

	public int reflectCell() {
		return 7 - cell;
	}
}
