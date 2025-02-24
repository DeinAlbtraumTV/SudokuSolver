package de.a1btraum.solver.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Thermo Rule: <br>
 * Input: Path, can go diagonal <br>
 * Ensures all numbers on this path are in ascending order
 */
public class ThermoRule implements IRule {
	ThermoPath[] paths;

	@Override
	public boolean loadData(JsonObject object) {
		JsonArray pathData = object.getAsJsonArray("Thermo");

		if (pathData == null) return false;
		if (pathData.isEmpty()) return false;

		paths = new ThermoPath[pathData.size()];
		for (int i = 0; i < pathData.size(); i++) {
			paths[i] = new ThermoPath(pathData.get(i).getAsJsonArray());
		}

		return true;
	}

	@Override
	public void getPossibleValues(SudokuState state, int row, int col, List<Integer> current) {
		Set<Integer> toRemove = new HashSet<>();

		for (Integer val : current) {
			for (ThermoPath path : paths) {
				// Check all values for each path
				// If a path doesn't allow the value -> discard it
				if (!path.isAllowed(state, row, col, val)) {
					toRemove.add(val);
				}
			}
		}

		current.removeAll(toRemove);
	}

	private static class ThermoPath extends AbstractPath {
		private ThermoPath(JsonArray data) {
			super(data);
		}

		@Override
		boolean prevLocationFits(int prev, int cur) {
			return prev == 0 || prev < cur;
		}

		@Override
		boolean nextLocationFits(int next, int cur) {
			return next == 0 || cur < next;
		}
	}
}
