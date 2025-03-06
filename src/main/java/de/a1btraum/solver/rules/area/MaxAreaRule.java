package de.a1btraum.solver.rules.area;

import de.a1btraum.core.SudokuState;
import de.a1btraum.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaxAreaRule extends AreaRule {
	private final int AREA_MAX;

	public MaxAreaRule(int areaMax, String memberName) {
		super(memberName);
		AREA_MAX = areaMax;
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
}
