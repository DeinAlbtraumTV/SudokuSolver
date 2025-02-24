package de.a1btraum.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.a1btraum.solver.WaveFunctionCollapseSolver;
import de.a1btraum.solver.rules.BoxRule;
import de.a1btraum.solver.rules.LineRule;
import de.a1btraum.solver.rules.ThermoRule;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static de.a1btraum.solver.WaveFunctionCollapseSolver.SolverBuilder;

public class SudokuSolver {
	public static void main(String[] args) throws FileNotFoundException {
		// Read file
		JsonObject json = JsonParser.parseReader(new FileReader("sudoku.json")).getAsJsonObject();

		// Build Solver
		WaveFunctionCollapseSolver solver = new SolverBuilder(json)
				.registerRule(new LineRule())
				.registerRule(new BoxRule())
				.registerRule(new ThermoRule())
				.build();

		// Solve the sudoku
		// Each call to collapse is one step
		while (!solver.collapse());

		System.out.println("Solution:");
		System.out.println(solver.getSolvedState());

		boolean proper = !solver.hasMultipleSolutions();

		System.out.println("Proper Sudoku: " + (proper ? "true" : "false"));

		if (!proper) {
			System.out.println("Alternative Solution:");
			System.out.println(solver.getSolvedState());
		}
	}
}
