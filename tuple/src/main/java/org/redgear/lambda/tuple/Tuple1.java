package org.redgear.lambda.tuple;

import java.util.function.Function;

/**
 * Created by dcallis on 11/20/2015.
 */
public class Tuple1<T1> implements Tuple {

	public final T1 v1;

	public Tuple1(T1 v1) {
		this.v1 = v1;
	}

	@Override
	public int arity() {
		return 1;
	}

	@Override
	public Tuple1<T1> reverse() {
		return this;
	}

	public T1 getV1() {
		return v1;
	}

	public <R1> Tuple1<R1> map(Function<T1, R1> func) {
		return Tuple.of(func.apply(v1));
	}

	public <R1> Tuple1<R1> map1(Function<T1, R1> func) {
		return Tuple.of(func.apply(v1));
	}

	public <T2> Tuple2<T1, T2> concat(T2 v2){
		return Tuple.of(v1, v2);
	}

	public <T2> Tuple2<T1, T2> concat(Tuple1<T2> v2){
		return Tuple.of(v1, v2.getV1());
	}

	public <T2, T3> Tuple3<T1, T2, T3> concat(T2 v2, T3 v3) {
		return Tuple.of(v1, v2, v3);
	}

	public <T2, T3> Tuple3<T1, T2, T3> concat(Tuple2<T2, T3> other) {
		return Tuple.of(v1, other.getV1(), other.getV2());
	}

	public <T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(T2 v2, T3 v3, T4 v4) {
		return Tuple.of(v1, v2, v3, v4);
	}

	public <T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple3<T2, T3, T4> other) {
		return Tuple.of(v1, other.v1, other.v2, other.v3);
	}

	public <T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(T2 v2, T3 v3, T4 v4, T5 v5) {
		return Tuple.of(v1, v2, v3, v4, v5);
	}

	public <T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple4<T2, T3, T4, T5> other) {
		return Tuple.of(v1, other.v1, other.v2, other.v3, other.v4);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple1<?> tuple1 = (Tuple1<?>) o;

		return !(v1 != null ? !v1.equals(tuple1.v1) : tuple1.v1 != null);

	}

	@Override
	public int hashCode() {
		return v1 != null ? v1.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Tuple1{" +
				"v1=" + v1 +
				'}';
	}
}
