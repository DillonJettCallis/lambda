package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;

import java.util.function.Function;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func<In, Out> extends Function<In, Out>{

	default Out apply(In in) {
		return tryApply(in).get();
	}

	Out checkedApply(In in) throws Exception;

	default int arity(){
		return 1;
	}

	default <Last> Func<In, Last> andThen(Func<Out, Last> other) {
		return in -> other.apply(apply(in));
	}

	default <First> Func<First, Out> compose(Func<First, In> other) {
		return first -> apply(other.apply(first));
	}

	default Try<Out> tryApply(In in) {
		return Try.of(() -> checkedApply(in));
	}


	static <Out> Func0<Out> lift(Func0<Out> func) {
		return func;
	}

	static <In, Out> Func1<In, Out> lift(Func1<In, Out> func) {
		return func;
	}

	static <In1, In2, Out> Func2<In1, In2, Out> lift(Func2<In1, In2, Out> func) {
		return func;
	}

	static <In1, In2, In3, Out> Func3<In1, In2, In3, Out> lift(Func3<In1, In2, In3, Out> func) {
		return func;
	}

	static <In1, In2, In3, In4, Out> Func4<In1, In2, In3, In4, Out> lift(Func4<In1, In2, In3, In4, Out> func) {
		return func;
	}

	static <In1, In2, In3, In4, In5, Out> Func5<In1, In2, In3, In4, In5, Out> lift(Func5<In1, In2, In3, In4, In5, Out> func) {
		return func;
	}

	static <In> Consumer1<In> lift(Consumer1<In> func) {
		return func;
	}

	static <In1, In2> Consumer2<In1, In2> lift(Consumer2<In1, In2> func) {
		return func;
	}
}
