package org.redgear.lambda.tuple;

import org.redgear.lambda.function.Func;

/**
 * Created by dcallis on 11/20/2015.
 */
public class Tuple2<T1, T2> implements Tuple {

	public final T1 v1;

	public final T2 v2;

	public Tuple2(T1 v1, T2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public int arity() {
		return 2;
	}

	public T1 getV1() {
		return v1;
	}

	public T2 getV2() {
		return v2;
	}

	public <R1, R2> Tuple2<R1, R2> map(Func<Tuple2<T1, T2>, Tuple2<R1, R2>> func) {
		return func.apply(this);
	}

	public <R1> Tuple2<R1, T2> map1(Func<T1, R1> func) {
		return Tuple.of(func.apply(v1), v2);
	}

	public <R2> Tuple2<T1, R2> map2(Func<T2, R2> func) {
		return Tuple.of(v1, func.apply(v2));
	}

	public <T3> Tuple3<T1, T2, T3> concat(T3 v3) {
		return Tuple.of(v1, v2, v3);
	}

	public <T3> Tuple3<T1, T2, T3> concat(Tuple1<T3> other) {
		return Tuple.of(v1, v2, other.getV1());
	}

	public <T3, T4> Tuple4<T1, T2, T3, T4> concat(T3 v3, T4 v4) {
		return Tuple.of(v1, v2, v3, v4);
	}

	public <T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple2<T3, T4> other) {
		return Tuple.of(v1, v2, other.v1, other.v2);
	}

	public <T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(T3 v3, T4 v4, T5 v5) {
		return Tuple.of(v1, v2, v3, v4, v5);
	}

	public <T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple3<T3, T4, T5> other) {
		return Tuple.of(v1, v2, other.v1, other.v2, other.v3);
	}

	public Tuple2<T2, T1> reverse() {
		return Tuple.of(v2, v1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

		if (v1 != null ? !v1.equals(tuple2.v1) : tuple2.v1 != null) return false;
		return !(v2 != null ? !v2.equals(tuple2.v2) : tuple2.v2 != null);

	}

	@Override
	public int hashCode() {
		int result = v1 != null ? v1.hashCode() : 0;
		result = 31 * result + (v2 != null ? v2.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Tuple2{" +
				"v1=" + v1 +
				", v2=" + v2 +
				'}';
	}
}
