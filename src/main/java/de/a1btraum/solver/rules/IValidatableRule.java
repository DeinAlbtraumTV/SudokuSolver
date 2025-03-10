package de.a1btraum.solver.rules;

import de.a1btraum.core.SudokuState;

public interface IValidatableRule extends IRule {
	boolean validate(SudokuState state);
}
