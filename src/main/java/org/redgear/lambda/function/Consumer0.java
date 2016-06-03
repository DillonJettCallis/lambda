package org.redgear.lambda.function;

/**
 * Created by dcallis on 11/27/2015.
 */
@FunctionalInterface
public interface Consumer0 extends Func0<Void>, Runnable {

	@Override
	default void run() {
		apply();
	}

	static Consumer0 lift(Consumer0 func) {
		return func;
	}

	static Consumer0 from(Runnable func) {
		return () -> {
			func.run();
			return null;
		};
	}

	static Consumer0 from(Func0<?> func) {
		return () -> {
			func.checkedApply();
			return null;
		};
	}
}
