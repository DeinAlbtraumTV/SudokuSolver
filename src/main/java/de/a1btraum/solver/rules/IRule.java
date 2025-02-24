package de.a1btraum.solver.rules;

import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;

import java.util.List;

/**
 * Interface to make our ruleset modular
 */
public interface IRule {

	/**
	 * Loads the rule data from JSON
	 * @param object Sudoku JSON data
	 * @return If the rule is enabled (data for this rule was present)
	 */
	boolean loadData(JsonObject object);

	/**
	 * Get all values this rule allows for a given position <br>
	 * Modifies the List passed as current
	 * @param state Current state
	 * @param row Row Index
	 * @param col Column Index
	 * @param current Currently valid values
	 */
	void getPossibleValues(SudokuState state, int row, int col, List<Integer> current);

}
