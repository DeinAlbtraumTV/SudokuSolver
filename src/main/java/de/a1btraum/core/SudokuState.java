package de.a1btraum.core;

import com.google.gson.JsonArray;
import de.a1btraum.util.Pair;

import java.util.Arrays;

/**
 * State Holder for the actual Sudoku grid <br>
 * Makes it easy to take snapshots of the grid for backtracking
 */
public class SudokuState {

	private final int[][] grid;

	private final int boxWidth;
	private final int boxHeight;

	/**
	 * Read the json array data and populate the grid with it
	 */
	public SudokuState(int boxWidth, int boxHeight, JsonArray sudoku) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;

		if (sudoku == null) throw new IllegalArgumentException("Sudoku can't be null");

		int rows = sudoku.size();
		if (rows <= 0) throw new IllegalStateException("Can't have empty Sudoku Grid");

		int cols = sudoku.get(0).getAsJsonArray().size();
		grid = new int[rows][cols];

		for (int i = 0; i < rows; i++) {
			JsonArray row = sudoku.get(i).getAsJsonArray();
			for (int j = 0; j < cols; j++) {
				grid[i][j] = row.get(j).getAsInt();
			}
		}
	}

	private SudokuState(int boxWidth, int boxHeight, int[][] grid) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.grid = grid;
	}

	public Pair<Integer, Integer> getDim() {
		return new Pair<>(boxWidth, boxHeight);
	}

	public int getBoxWidth() {
		return boxWidth;
	}

	public int getBoxHeight() {
		return boxHeight;
	}

	public int getFieldWidth() {
		return grid[0].length;
	}

	public int getFieldHeight() {
		return grid.length;
	}

	public int get(Pair<Integer, Integer> pos) {
		return get(pos.getVal1(), pos.getVal2());
	}

	public int get(int row, int col) {
		return grid[row][col];
	}

	public void set(int row, int col, int value) {
		grid[row][col] = value;
	}

	public void set(Pair<Integer, Integer> pos, int value) {
		set(pos.getVal1(), pos.getVal2(), value);
	}

	/**
	 * @return If this State is considered to be solved (no 0 present)
	 */
	public boolean isSolved() {
		for (int[] rows : grid) {
			for (int val : rows) {
				if (val == 0) return false;
			}
		}

		return true;
	}

	/**
	 * @return Deep copy of the current state for backtracking purposes
	 */
	public SudokuState snapshot() {
		// High level Lambda fuckery
		return new SudokuState(
				boxWidth,
				boxHeight,
				Arrays.stream(grid) // Make a stream from the rows of the sudoku
						.map(int[]::clone) // Clone each row
						.toArray($ -> grid.clone()) // Collect them into a cloned grid
		);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < grid.length; i++) {
			if (i % boxHeight == 0) appendVerticalBorder(builder);
			for (int j = 0; j < grid[i].length; j++) {
				if (j % boxWidth == 0) builder.append("|   ");

				builder.append(grid[i][j]).append("   ");

				if (j == grid[i].length - 1) builder.append("|   ");
			}

			builder.append("\n");
			if (i == grid.length - 1) appendVerticalBorder(builder);
		}

		return builder.toString();
	}

	private void appendVerticalBorder(StringBuilder builder) {
		int maxLength = (int) Math.ceil((boxWidth * boxHeight) / 10f);

		for (int i = 0; i < boxWidth; i++) {
			builder.append("|---")
				.append("-".repeat(maxLength * boxWidth))
				.append("---".repeat(boxWidth));
		}

		builder.append("|\n");
	}
}
