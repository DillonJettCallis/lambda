package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;

import java.util.function.Supplier;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func0<Out> extends Func<Void, Out>, Supplier<Out> {

	default int arity(){
		return 0;
	}

	default Out get() {
		return apply();
	}

	Out checkedApply() throws Exception;

	default Out checkedApply(Void v) throws Exception {
		return checkedApply();
	}

	default Out apply() {
		return tryApply().get();
	}

	default Out apply(Void v) {
		return apply();
	}

	default Try<Out> tryApply() {
		return Try.of(this);
	}

	default Try<Out> tryApply(Void v) {
		return tryApply();
	}

	static <Out> Func0<Out> lift(Func0<Out> func) {
		return func;
	}
}
