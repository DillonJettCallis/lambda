package org.redgear.lambda.tuple;

import java.util.function.Function;

/**
 * Created by dcallis on 1/8/2016.
 */
public class Tuple4<T1, T2, T3, T4> implements Tuple{


	public final T1 v1;
	public final T2 v2;
	public final T3 v3;
	public final T4 v4;

	public Tuple4(T1 v1, T2 v2, T3 v3, T4 v4) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}


	@Override
	public int arity() {
		return 4;
	}

	public <R> R map(Function<Tuple4<T1, T2, T3, T4>, R> func) {
		return func.apply(this);
	}

	public <R1> Tuple4<R1, T2, T3, T4> map1(Function<? super T1, ? extends R1> func) {
		return Tuple.of(func.apply(v1), v2, v3, v4);
	}

	public <R2> Tuple4<T1, R2, T3, T4> map2(Function<? super T2, ? extends R2> func) {
		return Tuple.of(v1, func.apply(v2), v3, v4);
	}

	public <R3> Tuple4<T1, T2, R3, T4> map3(Function<? super T3, ? extends R3> func) {
		return Tuple.of(v1, v2, func.apply(v3), v4);
	}

	public <R4> Tuple4<T1, T2, T3, R4> map4(Function<? super T4, ? extends R4> func) {
		return Tuple.of(v1, v2, v3, func.apply(v4));
	}

	public <T5> Tuple5<T1, T2, T3, T4, T5> concat(T5 v5) {
		return Tuple.of(v1, v2, v3, v4, v5);
	}

	public <T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple1<T5> v5) {
		return Tuple.of(v1, v2, v3, v4, v5.v1);
	}

	@Override
	public Tuple4<T4, T3, T2, T1> reverse() {
		return Tuple.of(v4, v3, v2, v1);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;

		if (v1 != null ? !v1.equals(tuple4.v1) : tuple4.v1 != null) return false;
		if (v2 != null ? !v2.equals(tuple4.v2) : tuple4.v2 != null) return false;
		if (v3 != null ? !v3.equals(tuple4.v3) : tuple4.v3 != null) return false;
		return v4 != null ? v4.equals(tuple4.v4) : tuple4.v4 == null;

	}

	@Override
	public int hashCode() {
		int result = v1 != null ? v1.hashCode() : 0;
		result = 31 * result + (v2 != null ? v2.hashCode() : 0);
		result = 31 * result + (v3 != null ? v3.hashCode() : 0);
		result = 31 * result + (v4 != null ? v4.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Tuple4{" +
				"v1=" + v1 +
				", v2=" + v2 +
				", v3=" + v3 +
				", v4=" + v4 +
				'}';
	}
}
