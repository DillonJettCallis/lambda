package org.redgear.lambda.function;

import org.redgear.lambda.control.Try;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func0<Out> extends Func<Void, Out>, Supplier<Out>, Try.CheckedSupplier<Out> {

	default int arity(){
		return 0;
	}

	default Out get() {
		return apply();
	}

	@Override
	default <Last> Func0<Last> andThen(Function<? super Out, ? extends Last> other) {
		return () -> other.apply(apply());
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
