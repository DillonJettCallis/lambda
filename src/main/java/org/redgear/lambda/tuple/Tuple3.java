package org.redgear.lambda.tuple;

import org.redgear.lambda.function.Func;
import org.redgear.lambda.function.Func1;
import org.redgear.lambda.function.Func3;

/**
 * Created by dcallis on 11/23/2015.
 */
public class Tuple3<T1, T2, T3> implements Tuple {

	public final T1 v1;
	public final T2 v2;
	public final T3 v3;

	public Tuple3(T1 v1, T2 v2, T3 v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	@Override
	public int arity() {
		return 3;
	}

	public <R1, R2, R3> Tuple3<R1, R2, R3> map(Func3<T1, T2, T3, Tuple3<R1, R2, R3>> func) {
		return func.apply(this);
	}

	public <R1> Tuple3<R1, T2, T3> map1(Func1<T1, R1> func) {
		return Tuple.of(func.apply(v1), v2, v3);
	}

	public <R2> Tuple3<T1, R2, T3> map2(Func1<T2, R2> func) {
		return Tuple.of(v1, func.apply(v2), v3);
	}

	public <R3> Tuple3<T1, T2, R3> map3(Func1<T3, R3> func) {
		return Tuple.of(v1, v2, func.apply(v3));
	}

	public <T4> Tuple4<T1, T2, T3, T4> concat(T4 v4) {
		return Tuple.of(v1, v2, v3, v4);
	}

	public <T4> Tuple4<T1, T2, T3, T4> concat(Tuple1<T4> other) {
		return Tuple.of(v1, v2, v3, other.v1);
	}

	public <T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(T4 v4, T5 v5) {
		return Tuple.of(v1, v2, v3, v4, v5);
	}

	public <T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple2<T4, T5> other) {
		return Tuple.of(v1, v2, v3, other.v1, other.v2);
	}

	@Override
	public Tuple3<T3, T2, T1> reverse() {
		return Tuple.of(v3, v2, v1);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;

		if (v1 != null ? !v1.equals(tuple3.v1) : tuple3.v1 != null) return false;
		if (v2 != null ? !v2.equals(tuple3.v2) : tuple3.v2 != null) return false;
		return !(v3 != null ? !v3.equals(tuple3.v3) : tuple3.v3 != null);

	}

	@Override
	public int hashCode() {
		int result = v1 != null ? v1.hashCode() : 0;
		result = 31 * result + (v2 != null ? v2.hashCode() : 0);
		result = 31 * result + (v3 != null ? v3.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Tuple3{" +
				"v1=" + v1 +
				", v2=" + v2 +
				", v3=" + v3 +
				'}';
	}
}
