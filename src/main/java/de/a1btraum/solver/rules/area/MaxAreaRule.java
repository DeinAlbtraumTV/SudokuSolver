package de.a1btraum.solver.rules.area;

import de.a1btraum.core.SudokuState;
import de.a1btraum.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaxAreaRule extends AreaRule {
	private final int AREA_TARGET;

	public MaxAreaRule(int areaTarget, String memberName) {
		super(memberName);
		AREA_TARGET = areaTarget;
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
				if (AREA_TARGET - sum < i) {
					toRemove.add(i);
				}
			}
		}

		current.removeAll(toRemove);
	}

	@Override
	public boolean validate(SudokuState state) {
		for (Area area : areas) {
			int sum = 0;

			for (Pair<Integer, Integer> pos : area.getPoints()) {
				sum += state.get(pos);
			}

			if (sum != AREA_TARGET) {
				return false;
			}
		}
		return true;
	}
}
