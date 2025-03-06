package de.a1btraum.solver.rules.area;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;
import de.a1btraum.solver.rules.IRule;
import de.a1btraum.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class AreaRule implements IRule {
	private final String memberName;

	Area[] areas;

	AreaRule(final String memberName) {
		this.memberName = memberName;
	}

	@Override
	public boolean loadData(JsonObject object) {
		JsonArray areaData = object.getAsJsonArray(memberName);

		if (areaData == null) return false;
		if (areaData.isEmpty()) return false;

		areas = new Area[areaData.size()];
		for (int i = 0; i < areaData.size(); i++) {
			areas[i] = new Area(areaData.get(i).getAsJsonArray());
		}

		return true;
	}

	@Override
	public abstract void getPossibleValues(SudokuState state, int row, int col, List<Integer> current);

	static class Area {
		private final List<Pair<Integer, Integer>> points;

		Area(JsonArray data) {
			points = new ArrayList<>(data.size());

			for (int i = 0; i < data.size(); i++) {
				JsonArray entry = data.get(i).getAsJsonArray();

				points.add(new Pair<>(entry.get(0).getAsInt(), entry.get(1).getAsInt()));
			}
		}

		public List<Pair<Integer, Integer>> getPoints() {
			return points;
		}
	}
}
