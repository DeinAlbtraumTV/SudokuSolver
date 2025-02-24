package de.a1btraum.solver.rules;

import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoxRule implements IRule {
	@Override
	public boolean loadData(JsonObject object) {
		// No data to load for this rule
		return true;
	}

	@Override
	public void getPossibleValues(SudokuState state, int row, int col, List<Integer> current) {
		Set<Integer> toRemove = new HashSet<>();

		int boxRowStart = row - row % state.getBoxHeight();
		int boxColStart = col - col % state.getBoxWidth();

		for (int i = 0; i < state.getBoxHeight(); i++) {
			for (int j = 0; j < state.getBoxWidth(); j++) {
				toRemove.add(state.get(boxRowStart + i, boxColStart + j));
			}
		}

		current.removeAll(toRemove);
	}
}
