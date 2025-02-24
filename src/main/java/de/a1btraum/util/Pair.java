package de.a1btraum.util;

import java.util.Objects;

public class Pair<R, L> implements Comparable<Pair<R, L>> {
	private final R val1;
	private final L val2;

	public Pair(R val1, L val2) {
		this.val1 = val1;
		this.val2 = val2;
	}

	public R getVal1() {
		return val1;
	}

	public L getVal2() {
		return val2;
	}

	// Boilerplate Methods below
	// These are needed though as we rely on the hashCodes for saving memory

	@Override
	public int compareTo(Pair<R, L> o) {
		if (val1.equals(o.val1) && val2.equals(o.val2)) return 0;
		if (val1.equals(o.val1) && !val2.equals(o.val2)) return 1;

		return -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(val1, pair.val1) && Objects.equals(val2, pair.val2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(val1, val2);
	}
}
