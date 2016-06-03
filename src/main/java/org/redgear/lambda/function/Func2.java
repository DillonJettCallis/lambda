package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;
import org.redgear.lambda.tuple.Tuple1;
import org.redgear.lambda.tuple.Tuple2;

import java.util.function.BiFunction;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func2<In1, In2, Out> extends Func<Tuple2<In1, In2>, Out> {

	default int arity(){
		return 2;
	}

	Out checkedApply(In1 in1, In2 in2) throws Exception;

	default Out checkedApply(Tuple2<In1, In2> in) throws Exception{
		return checkedApply(in.getV1(), in.getV2());
	}

	default Out apply(In1 in1, In2 in2){
		return tryApply(in1, in2).get();
	}

	default Out apply(Tuple2<In1, In2> in) {
		return apply(in.getV1(), in.getV2());
	}

	default Try<Out> tryApply(In1 in1, In2 in2) {
		return Try.of(() -> checkedApply(in1, in2));
	}

	default Try<Out> tryApply(Tuple2<In1, In2> in) {
		return Try.of(() -> checkedApply(in.getV1(), in.getV2()));
	}

	default Func1<In2, Out> curry(In1 in1) {
		return in2 -> apply(in1, in2);
	}

	default Func1<In2, Out> curry(Tuple1<In1> in) {
		return curry(in.getV1());
	}

	default Func0<Out> curry(In1 in1, In2 in2) {
		return () -> apply(in1, in2);
	}

	default Func0<Out> curry(Tuple2<In1, In2> in) {
		return curry(in.getV1(), in.getV2());
	}

	default Func2<In2, In1, Out> reverse(){
		return (in1, in2) -> apply(in2, in1);
	}

	static <In1, In2, Out> Func2<In1, In2, Out> lift(Func2<In1, In2, Out> func) {
		return func;
	}

	static <In1, In2, Out> Func2<In1, In2, Out> from(BiFunction<In1, In2, Out> func) {
		return func::apply;
	}

	default BiFunction<In1, In2, Out> toBiFunction() {
		return this::apply;
	}
}
