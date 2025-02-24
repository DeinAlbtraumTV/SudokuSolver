package de.a1btraum.solver;

import de.a1btraum.core.SudokuState;
import de.a1btraum.util.Pair;

import java.util.List;

public class SolveStep {

	private final SudokuState state;
	private final Pair<Integer, Integer> changePos;
	private int changeValue;
	private final List<Integer> triedValues;

	public SolveStep(SudokuState state, Pair<Integer, Integer> changePos, int changeValue, List<Integer> triedValues) {
		this.state = state;
		this.changePos = changePos;
		this.changeValue = changeValue;
		this.triedValues = triedValues;
	}

	public SudokuState getState() {
		return state;
	}

	public Pair<Integer, Integer> getChangePos() {
		return changePos;
	}

	public void setChangeValue(int changeValue) {
		this.changeValue = changeValue;
	}

	public int getChangeValue() {
		return changeValue;
	}

	public List<Integer> getTriedValues() {
		return triedValues;
	}
}
