package de.a1btraum.solver.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;
import de.a1btraum.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AreaRule implements IRule {
	public static AreaRule VRule = new AreaRule(5, "V");
	public static AreaRule XRule = new AreaRule(10, "X");

	private final int AREA_MAX;
	private final String memberName;

	Area[] areas;

	private AreaRule(final int areaMax, final String memberName) {
		AREA_MAX = areaMax;
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
	public void getPossibleValues(SudokuState state, int row, int col, List<Integer> current) {
		Set<Integer> toRemove = new HashSet<>();

		for (Area area : areas) {
			if (!area.getPoints().contains(new Pair<>(row, col))) continue;

			int sum = 0;

			for (Pair<Integer, Integer> pos : area.getPoints()) {
				if (pos.equals(new Pair<>(row, col))) continue;

				sum += state.get(pos);
			}

			for (Integer i : current) {
				if (AREA_MAX - sum < i) {
					toRemove.add(i);
				}
			}
		}

		current.removeAll(toRemove);
	}

	private static class Area {
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
