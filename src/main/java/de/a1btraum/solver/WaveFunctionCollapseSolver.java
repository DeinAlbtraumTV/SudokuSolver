package de.a1btraum.solver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.a1btraum.core.SudokuState;
import de.a1btraum.solver.rules.IRule;
import de.a1btraum.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

public class WaveFunctionCollapseSolver {

	private final List<IRule> rules;
	private final Stack<SolveStep> steps;
	private final SudokuState initialState;

	private final int maxNum;

	private boolean solved;

	private WaveFunctionCollapseSolver(SudokuState initialState, List<IRule> rules) {
		this.rules = rules;
		this.initialState = initialState;
		this.steps = new Stack<>();

		this.maxNum = initialState.getBoxWidth() * initialState.getBoxHeight();

		this.solved = false;
	}

	public SudokuState getCurrentState() {
		SudokuState state;

		if (steps.isEmpty()) {
			state = initialState;
		} else {
			state = steps.peek().getState();
		}

		return state;
	}

	/**
	 * Backtrack in case we don't have any valid values left (aka we hit a dead end)
	 * This reverts cells back into a superposition until we can make another move or run out of steps
	 */
	private void backtrack() {
		boolean backtrack = true;

		while (backtrack) {
			if (steps.isEmpty()) throw new IllegalStateException("Sudoku is not solvable with current ruleset");
			SolveStep failedStep = steps.peek();

			failedStep.getTriedValues().add(failedStep.getChangeValue());
			failedStep.getState().set(failedStep.getChangePos(), 0);

			List<Integer> valuesLeft = getAllowedValues(failedStep.getChangePos());

			if (!valuesLeft.isEmpty()) backtrack = false;
			else {
				steps.pop();
			}
		}
	}

	/**
	 * @param row Row Index
	 * @param col Column Index
	 * @return All allowed values for a given position that were not yet tried in the current step
	 */
	private List<Integer> getAllowedValues(int row, int col) {
		// Range from 1 - maxNum
		IntStream allVals = IntStream.range(1, maxNum + 1);

		List<Integer> possibleValues;

		if (steps.isEmpty()) {
			// Convert to List<Integer>
			possibleValues = allVals.boxed().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		} else {
			// Filter out values we already tried
			possibleValues = allVals.filter(i -> !steps.peek().getTriedValues().contains(i))
					// Convert to List<Integer>
					.boxed().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		}

		for (IRule rule : rules) {
			rule.getPossibleValues(getCurrentState(), row, col, possibleValues);
		}

		return possibleValues;
	}

	/**
	 * @param pos Row Index, Column Index
	 * @return All allowed values for a given position that were not yet tried in the current step
	 */
	private List<Integer> getAllowedValues(Pair<Integer, Integer> pos) {
		return getAllowedValues(pos.getVal1(), pos.getVal2());
	}

	private Pair<Pair<Integer, Integer>, List<Integer>> getMostStableField() {
		int lowestEntropy = Integer.MAX_VALUE;
		Pair<Integer, Integer> lowestEntropyPosition = null;
		List<Integer> lowestEntropyValues = new ArrayList<>();

		SudokuState state = getCurrentState();

		for (int i = 0; i < state.getFieldHeight(); i++) {
			for (int j = 0; j < state.getFieldWidth(); j++) {
				if (state.get(i, j) != 0) continue;

				List<Integer> allowedValues = getAllowedValues(i, j);
				if (!allowedValues.isEmpty()) {
					if (allowedValues.size() < lowestEntropy) {
						lowestEntropy = allowedValues.size();
						lowestEntropyPosition = new Pair<>(i, j);
						lowestEntropyValues = allowedValues;
					}
				}

				// Won't find lower entropy field, so we can skip the rest
				if (lowestEntropy == 1) break;
			}

			// Won't find lower entropy field, so we can skip the rest
			if (lowestEntropy == 1) break;
		}

		if (lowestEntropyPosition == null) {
			return null;
		}

		return new Pair<>(lowestEntropyPosition, lowestEntropyValues);
	}

	/**
	 * Collapse one field from the superposition into a valid state
	 * @return If the sudoku is now solved
	 */
	public boolean collapse() {
		Pair<Pair<Integer, Integer>, List<Integer>> toCollapse = getMostStableField();

		while (toCollapse == null) {
			backtrack();
			toCollapse = getMostStableField();
		}

		SudokuState state = getCurrentState();

		Pair<Integer, Integer> pos = toCollapse.getVal1();
		List<Integer> values = toCollapse.getVal2();

		if (steps.isEmpty() || !steps.peek().getChangePos().equals(pos)) {
			state = state.snapshot();
			state.set(pos, values.get(0));
			steps.add(new SolveStep(
					state,
					pos,
					values.get(0),
					new ArrayList<>()
			));
		} else {
			state.set(pos, values.get(0));
			SolveStep step = steps.peek();
			step.setChangeValue(values.get(0));
		}

		if (state.isSolved()) {
			solved = true;
			return true;
		}

		return false;
	}

	/**
	 * @throws IllegalStateException if called before {@code collapse} returned true
	 * @return The solved Sudoku
	 */
	public SudokuState getSolvedState() {
		if (!solved) throw new IllegalStateException("Can't get solved state: Sudoku not solved yet");

		return steps.peek().getState();
	}

	/**
	 * This function backtracks the solver to a point where it can continue searching for another solution after a valid one has been found <br>
	 * Only call this after the solver has found a valid solution with {@code collapse} <br>
	 * Can be called multiple times to find all valid solutions as long as the previous call returned true
	 * @return If there are multiple valid solutions
	 */
	public boolean hasMultipleSolutions() {
		if (!solved) throw new IllegalStateException("Can't check for multiple solutions: No valid solution discovered yet");

		backtrack();
		solved = false;

		try {
			while (!collapse());
		} catch (IllegalStateException e) {
			return false;
		}

		return true;
	}

	/**
	 * Builder class for the solver to streamline adding new rules
	 */
	public static class SolverBuilder {

		private final JsonObject data;
		private final SudokuState initialState;
		private final List<IRule> rules;

		public SolverBuilder(JsonObject data) {
			this.data = data;
			rules = new ArrayList<>();

			JsonArray dimensions = data.getAsJsonArray("Dimension");

			if (dimensions == null) throw new IllegalStateException("Dimension can't be null");
			if (dimensions.size() != 2) throw new IllegalStateException("Invalid Dimensions");

			initialState = new SudokuState(dimensions.get(0).getAsInt(), dimensions.get(1).getAsInt(), data.getAsJsonArray("Sudoku"));
		}

		/**
		 * Attempt to register a rule. If the rule fails to load it will NOT be active
		 * @param rule Instance of the rule to load
		 * @return {@code this}
		 */
		public SolverBuilder registerRule(IRule rule) {
			if (rule.loadData(data)) {
				rules.add(rule);
				System.out.println("Activated rule: " + rule.getClass().getName());
			} else {
				System.out.println("Failed to load rule: " + rule.getClass().getName());
			}

			return this;
		}

		/**
		 * Attempt to register rules. If a rule fails to load that rule will NOT be active
		 * @param rules Instances of the rules to load
		 * @return {@code this}
		 */
		public SolverBuilder registerRule(IRule... rules) {
			for (IRule rule : rules) {
				registerRule(rule);
			}
			return this;
		}

		/**
		 * Attempt to register rules. If a rule fails to load that rule will NOT be active
		 * @param rules List of the rules to load
		 * @return {@code this}
		 */
		public SolverBuilder registerRule(List<IRule> rules) {
			for (IRule rule : rules) {
				registerRule(rule);
			}
			return this;
		}

		/**
		 * Build the WaveFunctionCollapseSolver
		 * @return Built {@code WaveFunctionCollapseSolver}
		 */
		public WaveFunctionCollapseSolver build() {
			return new WaveFunctionCollapseSolver(initialState, rules);
		}
	}
}
