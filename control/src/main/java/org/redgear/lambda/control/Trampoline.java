package org.redgear.lambda.control;

import org.redgear.lambda.control.impl.TrampolineImpl;

import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/18/2016.
 */
public interface Trampoline<In, Out> extends Function<In, Out> {

	static <In, Out> Trampoline<In, Out> of(Function<In, Either<In, Out>> func) {
		return new TrampolineImpl<>(func);
	}

}
