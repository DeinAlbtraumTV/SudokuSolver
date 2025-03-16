package de.a1btraum.util;

public record Pair<R, L>(R val1, L val2) implements Comparable<Pair<R, L>> {
	@Override
	public int compareTo(Pair<R, L> o) {
		if (val1.equals(o.val1) && val2.equals(o.val2)) return 0;
		if (val1.equals(o.val1) && !val2.equals(o.val2)) return 1;

		return -1;
	}
}
