package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func0<Out> extends Func<Void, Out> {

	default int arity(){
		return 0;
	}

	default Out apply(Void v) throws Exception {
		return apply();
	}

	default Try<Out> tryApply(Void v) {
		return tryApply();
	}

	Out apply() throws Exception;

	default Try<Out> tryApply() {
		return Try.of(this);
	}

}
