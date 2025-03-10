package de.a1btraum.solver.rules.area;

public final class AreaRules {
	public static final AreaRule VRule = new SumAreaRule(5, "V");
	public static final AreaRule XRule = new SumAreaRule(10, "X");
	public static final AreaRule FreeformRule = new UniqueAreaRule("Freeform");

	// prevent instances of this class
	private AreaRules() {}
}
