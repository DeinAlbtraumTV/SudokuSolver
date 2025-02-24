package de.a1btraum.solver.rules;

import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineRule implements IRule {
	@Override
	public boolean loadData(JsonObject object) {
		// No data to load for this rule
		return true;
	}

	@Override
	public void getPossibleValues(SudokuState state, int row, int col, List<Integer> current) {
		Set<Integer> toRemove = new HashSet<>();

		// All Elements in current row
		for (int i = 0; i < state.getFieldWidth(); i++) {
			toRemove.add(state.get(row, i));
		}

		// All Elements in current col
		for (int i = 0; i < state.getFieldWidth(); i++) {
			toRemove.add(state.get(i, col));
		}

		current.removeAll(toRemove);
	}
}
