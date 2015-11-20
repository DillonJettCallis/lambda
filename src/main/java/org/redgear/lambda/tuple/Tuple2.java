package org.redgear.lambda.tuple;

/**
 * Created by dcallis on 11/20/2015.
 */
public class Tuple2<Type1, Type2> implements Tuple {

	private Type1 val1;

	private Type2 val2;

	@Override
	public int arity() {
		return 2;
	}

	public Type1 getVal1() {
		return val1;
	}

	public void setVal1(Type1 val1) {
		this.val1 = val1;
	}

	public Type2 getVal2() {
		return val2;
	}

	public void setVal2(Type2 val2) {
		this.val2 = val2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

		if (val1 != null ? !val1.equals(tuple2.val1) : tuple2.val1 != null) return false;
		return !(val2 != null ? !val2.equals(tuple2.val2) : tuple2.val2 != null);

	}

	@Override
	public int hashCode() {
		int result = val1 != null ? val1.hashCode() : 0;
		result = 31 * result + (val2 != null ? val2.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Tuple2{" +
				"val1=" + val1 +
				", val2=" + val2 +
				'}';
	}
}
