package de.a1btraum.solver.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhisperRule implements IRule {
	WhisperPath[] paths;

	@Override
	public boolean loadData(JsonObject object) {
		JsonArray pathData = object.getAsJsonArray("Whisper");

		if (pathData == null) return false;
		if (pathData.isEmpty()) return false;

		paths = new WhisperPath[pathData.size()];
		for (int i = 0; i < pathData.size(); i++) {
			paths[i] = new WhisperPath(pathData.get(i).getAsJsonArray());
		}

		return true;
	}

	@Override
	public void getPossibleValues(SudokuState state, int row, int col, List<Integer> current) {
		Set<Integer> toRemove = new HashSet<>();

		for (Integer val : current) {
			for (WhisperPath path : paths) {
				// Check all values for each path
				// If a path doesn't allow the value -> discard it
				if (!path.isAllowed(state, row, col, val)) {
					toRemove.add(val);
				}
			}
		}

		current.removeAll(toRemove);
	}

	private static class WhisperPath extends AbstractPath {
		private WhisperPath(JsonArray data) {
			super(data);
		}

		@Override
		boolean prevLocationFits(int prev, int cur) {
			return prev == 0 || Math.abs(prev - cur) >= 5;
		}

		@Override
		boolean nextLocationFits(int next, int cur) {
			return next == 0 || Math.abs(next - cur) >= 5;
		}
	}
}
