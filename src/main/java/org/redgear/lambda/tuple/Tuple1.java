package org.redgear.lambda.tuple;

/**
 * Created by dcallis on 11/20/2015.
 */
public class Tuple1<Type1> implements Tuple {

	private Type1 val1;

	@Override
	public int arity() {
		return 1;
	}

	public Type1 getVal1() {
		return val1;
	}

	public void setVal1(Type1 val1) {
		this.val1 = val1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple1<?> tuple1 = (Tuple1<?>) o;

		return !(val1 != null ? !val1.equals(tuple1.val1) : tuple1.val1 != null);

	}

	@Override
	public int hashCode() {
		return val1 != null ? val1.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Tuple1{" +
				"val1=" + val1 +
				'}';
	}
}
