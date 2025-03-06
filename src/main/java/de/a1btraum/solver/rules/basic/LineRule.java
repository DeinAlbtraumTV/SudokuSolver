package de.a1btraum.solver.rules.basic;

import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;
import de.a1btraum.solver.rules.IRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineRule implements IRule {
	@Override
	public boolean loadData(JsonObject object) {
		if (object.has("Freeform")) {
			System.out.println("Not loading Line Rule: Freeform present");
			return false;
		}
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
