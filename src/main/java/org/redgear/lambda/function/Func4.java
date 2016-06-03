package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;
import org.redgear.lambda.tuple.Tuple1;
import org.redgear.lambda.tuple.Tuple2;
import org.redgear.lambda.tuple.Tuple3;
import org.redgear.lambda.tuple.Tuple4;

/**
 * Created by dcallis on 1/8/2016.
 */
@FunctionalInterface
public interface Func4<In1, In2, In3, In4, Out> extends Func<Tuple4<In1, In2, In3, In4>, Out> {


	Out checkedApply(In1 in1, In2 in2, In3 in3, In4 in4) throws Exception;

	@Override
	default Out checkedApply(Tuple4<In1, In2, In3, In4> in) throws Exception {
		return checkedApply(in.v1, in.v2, in.v3, in.v4);
	}

	default Try<Out> tryApply(In1 in1, In2 in2, In3 in3, In4 in4) {
		return Try.of(curry(in1, in2, in3, in4));
	}

	default Try<Out> tryApply(Tuple4<In1, In2, In3, In4> in) {
		return Try.of(curry(in));
	}

	default Out apply(In1 in1, In2 in2, In3 in3, In4 in4) {
		return tryApply(in1, in2, in3, in4).get();
	}

	default Out apply(Tuple4<In1, In2, In3, In4> in) {
		return tryApply(in).get();
	}


	default Func0<Out> curry(In1 in1, In2 in2, In3 in3, In4 in4) {
		return () -> checkedApply(in1, in2, in3, in4);
	}

	default Func0<Out> curry(Tuple4<In1, In2, In3, In4> in) {
		return () -> checkedApply(in.v1, in.v2, in.v3, in.v4);
	}

	default Func1<In4, Out> curry(In1 in1, In2 in2, In3 in3) {
		return (In4 in4) -> checkedApply(in1, in2, in3, in4);
	}

	default Func1<In4, Out> curry(Tuple3<In1, In2, In3> in) {
		return (In4 in4) -> checkedApply(in.v1, in.v2, in.v3, in4);
	}

	default Func2<In3, In4, Out> curry(In1 in1, In2 in2) {
		return (In3 in3, In4 in4) -> checkedApply(in1, in2, in3, in4);
	}

	default Func2<In3, In4, Out> curry(Tuple2<In1, In2> in) {
		return (In3 in3, In4 in4) -> checkedApply(in.v1, in.v2, in3, in4);
	}

	default Func3<In2, In3, In4, Out> curry(In1 in1) {
		return (In2 in2, In3 in3, In4 in4) -> checkedApply(in1, in2, in3, in4);
	}

	default Func3<In2, In3, In4, Out> curry(Tuple1<In1> in) {
		return (In2 in2, In3 in3, In4 in4) -> checkedApply(in.v1, in2, in3, in4);
	}

	static <In1, In2, In3, In4, Out> Func4<In1, In2, In3, In4, Out> lift(Func4<In1, In2, In3, In4, Out> func) {
		return func;
	}
}
