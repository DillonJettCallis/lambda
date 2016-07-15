package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;
import org.redgear.lambda.tuple.*;

/**
 * Created by dcallis on 1/11/2016.
 */
@FunctionalInterface
public interface Func5<In1, In2, In3, In4, In5, Out> extends Func<Tuple5<In1, In2, In3, In4, In5>, Out> {


	Out checkedApply(In1 in1, In2 in2, In3 in3, In4 in4, In5 in5) throws Exception;

	@Override
	default Out checkedApply(Tuple5<In1, In2, In3, In4, In5> in) throws Exception {
		return checkedApply(in.v1, in.v2, in.v3, in.v4, in.v5);
	}

	default Try<Out> tryApply(In1 in1, In2 in2, In3 in3, In4 in4, In5 in5) {
		return Try.of(curry(in1, in2, in3, in4, in5));
	}

	default Try<Out> tryApply(Tuple5<In1, In2, In3, In4, In5> in) {
		return Try.of(curry(in));
	}

	default Out apply(In1 in1, In2 in2, In3 in3, In4 in4, In5 in5) {
		return tryApply(in1, in2, in3, in4, in5).get();
	}

	default Out apply(Tuple5<In1, In2, In3, In4, In5> in) {
		return tryApply(in).get();
	}

	default Func0<Out> curry(In1 in1, In2 in2, In3 in3, In4 in4, In5 in5) {
		return () -> checkedApply(in1, in2, in3, in4, in5);
	}

	default Func0<Out> curry(Tuple5<In1, In2, In3, In4, In5> in) {
		return () -> checkedApply(in.v1, in.v2, in.v3, in.v4, in.v5);
	}

	default Func1<In5, Out> curry(In1 in1, In2 in2, In3 in3, In4 in4) {
		return (In5 in5) -> checkedApply(in1, in2, in3, in4, in5);
	}

	default Func1<In5, Out> curry(Tuple4<In1, In2, In3, In4> in) {
		return (In5 in5) -> checkedApply(in.v1, in.v2, in.v3, in.v4, in5);
	}

	default Func2<In4, In5, Out> curry(In1 in1, In2 in2, In3 in3) {
		return (In4 in4, In5 in5) -> checkedApply(in1, in2, in3, in4, in5);
	}

	default Func2<In4, In5, Out> curry(Tuple3<In1, In2, In3> in) {
		return (In4 in4, In5 in5) -> checkedApply(in.v1, in.v2, in.v3, in4, in5);
	}

	default Func3<In3, In4, In5, Out> curry(In1 in1, In2 in2) {
		return (In3 in3, In4 in4, In5 in5) -> checkedApply(in1, in2, in3, in4, in5);
	}

	default Func3<In3, In4, In5, Out> curry(Tuple2<In1, In2> in) {
		return (In3 in3, In4 in4, In5 in5) -> checkedApply(in.v1, in.v2, in3, in4, in5);
	}

	default Func4<In2, In3, In4, In5, Out> curry(In1 in1) {
		return (In2 in2, In3 in3, In4 in4, In5 in5) -> checkedApply(in1, in2, in3, in4, in5);
	}

	default Func4<In2, In3, In4, In5, Out> curry(Tuple1<In1> in) {
		return (In2 in2, In3 in3, In4 in4, In5 in5) -> checkedApply(in.v1, in2, in3, in4, in5);
	}

	static <In1, In2, In3, In4, In5, Out> Func5<In1, In2, In3, In4, In5, Out> lift(Func5<In1, In2, In3, In4, In5, Out> func) {
		return func;
	}

}
