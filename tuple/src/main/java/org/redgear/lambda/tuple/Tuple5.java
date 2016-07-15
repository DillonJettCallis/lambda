package org.redgear.lambda.tuple;

import java.util.function.Function;

/**
 * Created by dcallis on 1/11/2016.
 */
public class Tuple5<T1, T2, T3, T4, T5> implements Tuple {

	public final T1 v1;
	public final T2 v2;
	public final T3 v3;
	public final T4 v4;
	public final T5 v5;

	public Tuple5(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
		this.v5 = v5;
	}


	@Override
	public int arity() {
		return 5;
	}

	@Override
	public Tuple5<T5, T4, T3, T2, T1> reverse() {
		return Tuple.of(v5, v4, v3, v2, v1);
	}


	public <R1> Tuple5<R1, T2, T3, T4, T5> map1(Function<? super T1, ? extends R1> func) {
		return Tuple.of(func.apply(v1), v2, v3, v4, v5);
	}


	public <R2> Tuple5<T1, R2, T3, T4, T5> map2(Function<? super T2, ? extends R2> func) {
		return Tuple.of(v1, func.apply(v2), v3, v4, v5);
	}

	public <R3> Tuple5<T1, T2, R3, T4, T5> map3(Function<? super T3, ? extends R3> func) {
		return Tuple.of(v1, v2, func.apply(v3), v4, v5);
	}


	public <R4> Tuple5<T1, T2, T3, R4, T5> map4(Function<? super T4, ? extends R4> func) {
		return Tuple.of(v1, v2, v3, func.apply(v4), v5);
	}


	public <R5> Tuple5<T1, T2, T3, T4, R5> map5(Function<? super T5, ? extends R5> func) {
		return Tuple.of(v1, v2, v3, v4, func.apply(v5));
	}

	public T1 getV1() {
		return v1;
	}

	public T2 getV2() {
		return v2;
	}

	public T3 getV3() {
		return v3;
	}

	public T4 getV4() {
		return v4;
	}

	public T5 getV5() {
		return v5;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple5<?, ?, ?, ?, ?> tuple5 = (Tuple5<?, ?, ?, ?, ?>) o;

		if (v1 != null ? !v1.equals(tuple5.v1) : tuple5.v1 != null) return false;
		if (v2 != null ? !v2.equals(tuple5.v2) : tuple5.v2 != null) return false;
		if (v3 != null ? !v3.equals(tuple5.v3) : tuple5.v3 != null) return false;
		if (v4 != null ? !v4.equals(tuple5.v4) : tuple5.v4 != null) return false;
		return v5 != null ? v5.equals(tuple5.v5) : tuple5.v5 == null;

	}

	@Override
	public int hashCode() {
		int result = v1 != null ? v1.hashCode() : 0;
		result = 31 * result + (v2 != null ? v2.hashCode() : 0);
		result = 31 * result + (v3 != null ? v3.hashCode() : 0);
		result = 31 * result + (v4 != null ? v4.hashCode() : 0);
		result = 31 * result + (v5 != null ? v5.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Tuple5{" +
				"v1=" + v1 +
				", v2=" + v2 +
				", v3=" + v3 +
				", v4=" + v4 +
				", v5=" + v5 +
				'}';
	}
}
