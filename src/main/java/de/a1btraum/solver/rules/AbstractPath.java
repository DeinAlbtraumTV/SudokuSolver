package de.a1btraum.solver.rules;

import com.google.gson.JsonArray;
import de.a1btraum.core.SudokuState;
import de.a1btraum.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPath {
	List<Pair<Integer, Integer>> points;

	AbstractPath(JsonArray data) {
		points = new ArrayList<>(data.size());

		for (int i = 0; i < data.size(); i++) {
			JsonArray entry = data.get(i).getAsJsonArray();

			points.add(new Pair<>(entry.get(0).getAsInt(), entry.get(1).getAsInt()));
		}
	}

	abstract boolean prevLocationFits(int prev, int cur);
	abstract boolean nextLocationFits(int next, int cur);

	/**
	 * @param state Current state
	 * @param row Row Index
	 * @param col Column Index
	 * @param value Value to check
	 * @return If the value can be put into that field
	 */
	boolean isAllowed(SudokuState state, int row, int col, int value) {
		boolean valid = true;

		for (int i = 0; i < points.size(); i++) {
			Pair<Integer, Integer> p = points.get(i);
			if (p.getVal1() == row && p.getVal2() == col) {
				if (i > 0) {
					// Previous Path position
					Pair<Integer, Integer> p2 = points.get(i - 1);

					// check if field not empty and value is higher than the one we test
					if (!prevLocationFits(state.get(p2), value)) valid = false;
				}

				if (i < points.size() - 1) {
					// Next Path position
					Pair<Integer, Integer> p2 = points.get(i + 1);

					// check if field not empty and value is lower than the one we test
					if (!nextLocationFits(state.get(p2), value)) valid = false;
				}
			}
		}

		return valid;
	}
}
