package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func<In, Out> {

	Out apply(In in) throws Exception;

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
		return Try.of(() -> apply(in));
	}

}
