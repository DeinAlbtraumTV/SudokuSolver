package de.a1btraum.solver.rules.area;

import de.a1btraum.core.SudokuState;
import de.a1btraum.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueAreaRule extends AreaRule {
	public UniqueAreaRule(String memberName) {
		super(memberName);
	}

	@Override
	public void getPossibleValues(SudokuState state, int row, int col, List<Integer> current) {
		Set<Integer> toRemove = new HashSet<>();

		for (Area area : areas) {
			if (!area.getPoints().contains(new Pair<>(row, col))) continue;

			for (Pair<Integer, Integer> pos : area.getPoints()) {
				toRemove.add(state.get(pos));
			}
		}

		current.removeAll(toRemove);
	}
}
