package org.redgear.lambda.tuple;

import java.util.Map;

/**
 * Created by dcallis on 11/20/2015.
 */
public interface Tuple {


	int arity();

	Tuple reverse();


	static <T1> Tuple1<T1> of(T1 v1) {
		return new Tuple1<>(v1);
	}

	static <T1, T2> Tuple2<T1, T2> of(T1 v1, T2 v2) {
		return new Tuple2<>(v1, v2);
	}

	static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 v1, T2 v2, T3 v3) {
		return new Tuple3<>(v1, v2, v3);
	}

	static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 v1, T2 v2, T3 v3, T4 v4) {
		return new Tuple4<>(v1, v2, v3, v4);
	}

	static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
		return new Tuple5<>(v1, v2, v3, v4, v5);
	}

	static <T1, T2> Tuple2<T1, T2> from(Map.Entry<T1, T2> entry) {
		return new Tuple2<>(entry.getKey(), entry.getValue());
	}
}
