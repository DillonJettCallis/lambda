package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;
import org.redgear.lambda.tuple.Tuple2;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func2<In1, In2, Out> extends Func<Tuple2<In1, In2>, Out> {

	Out apply(In1 in1, In2 in2) throws Exception;

	default int arity(){
		return 2;
	}

	default Try<Out> tryApply(In1 in1, In2 in2) {
		return Try.of(() -> apply(in1, in2));
	}

	default Out apply(Tuple2<In1, In2> in) throws Exception {
		return apply(in.getVal1(), in.getVal2());
	}

	default Try<Out> tryApply(Tuple2<In1, In2> in) {
		return Try.of(() -> apply(in.getVal1(), in.getVal2()));
	}

	default Func1<In2, Out> curry(In1 in1) {
		return in2 -> apply(in1, in2);
	}

	default Func0<Out> curry(In1 in1, In2 in2) {
		return () -> apply(in1, in2);
	}

	default Func0<Out> curry(Tuple2<In1, In2> in) {
		return () -> apply(in);
	}

}
