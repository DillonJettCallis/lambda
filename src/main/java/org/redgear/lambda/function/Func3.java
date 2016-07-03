package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;
import org.redgear.lambda.tuple.Tuple1;
import org.redgear.lambda.tuple.Tuple2;
import org.redgear.lambda.tuple.Tuple3;

/**
 * Created by dcallis on 11/23/2015.
 */
@FunctionalInterface
public interface Func3<In1, In2, In3, Out> extends Func<Tuple3<In1, In2, In3>, Out>{

	Out checkedApply(In1 in1, In2 in2, In3 in3) throws Exception;

	@Override
	default Out checkedApply(Tuple3<In1, In2, In3> in) throws Exception{
		return checkedApply(in.getV1(), in.getV2(), in.getV3());
	}

	default Try<Out> tryApply(In1 in1, In2 in2, In3 in3) {
		return Try.of(curry(in1, in2, in3));
	}

	default Try<Out> tryApply(Tuple3<In1, In2, In3> in){
		return Try.of(curry(in));
	}

	default Out apply(In1 in1, In2 in2, In3 in3){
		return tryApply(in1, in2, in3).get();
	}

	default Out apply(Tuple3<In1, In2, In3> in){
		return tryApply(in).get();
	}

	default Func2<In2, In3, Out> curry(In1 in1){
		return (in2, in3) -> checkedApply(in1, in2, in3);
	}

	default Func2<In2, In3, Out> curry(Tuple1<In1> in){
		return curry(in.getV1());
	}

	default Func1<In3, Out> curry(In1 in1, In2 in2){
		return in3 -> checkedApply(in1, in2, in3);
	}

	default Func1<In3, Out> curry(Tuple2<In1, In2> in){
		return curry(in.getV1(), in.getV2());
	}

	default Func0<Out> curry(In1 in1, In2 in2, In3 in3){
		return () -> checkedApply(in1, in2, in3);
	}

	default Func0<Out> curry(Tuple3<In1, In2, In3> in){
		return curry(in.getV1(), in.getV2(), in.getV3());
	}

	default Func3<In3, In2, In1, Out> reverse(){
		return (in3, in2, in1) -> checkedApply(in1, in2, in3);
	}

	static <In3, In2, In1, Out> Func3<In3, In2, In1, Out> lift(Func3<In3, In2, In1, Out> func) {
		return func;
	}
}
