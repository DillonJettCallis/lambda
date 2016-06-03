package org.redgear.lambda.function;

import java.util.function.Consumer;

/**
 * Created by dcallis on 11/27/2015.
 */
@FunctionalInterface
public interface Consumer1<In> extends Func1<In, Void>, Consumer<In> {

	default void accept(In in) {
		apply(in);
	}

	default Void checkedApply(In in) throws Exception {
		checkedAccept(in);
		return null;
	}

	void checkedAccept(In in) throws Exception;


	static <In> Consumer1<In> lift(Consumer1<In> func) {
		return func;
	}

	static <In> Consumer1<In> from(Consumer<? super In> func) {
		return func::accept;
	}

	static <In> Consumer1<In> from(Func1<In, ?> func) {
		return func::checkedApply;
	}
}
