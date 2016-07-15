package org.redgear.lambda.function;

import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by dcallis on 3/16/2016.
 */
@FunctionalInterface
public interface Consumer2<In1, In2> extends Func2<In1, In2, Void>, BiConsumer<In1, In2>, Consumer<Tuple2<In1, In2>> {


	default void accept(In1 in1, In2 in2) {
		apply(in1, in2);
	}

	default void accept(Tuple2<In1, In2> in) {
		apply(in);
	}

	default Void checkedApply(In1 in1, In2 in2) throws Exception {
		checkedAccept(in1, in2);
		return null;
	}

	void checkedAccept(In1 in1, In2 in2) throws Exception;

	static <In1, In2> Consumer2<In1, In2> lift(Consumer2<In1, In2> func) {
		return func;
	}

	static <In1, In2> Consumer2<In1, In2> from(Consumer<Tuple2<In1, In2>> func) {
		return (l, r) -> func.accept(Tuple.of(l, r));
	}

	static <In1, In2> Consumer2<In1, In2> from(Func2<In1, In2, ?> func) {
		return func::checkedApply;
	}
}
