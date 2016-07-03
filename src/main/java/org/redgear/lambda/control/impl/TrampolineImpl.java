package org.redgear.lambda.control.impl;

import org.redgear.lambda.control.Either;
import org.redgear.lambda.control.Trampoline;
import org.redgear.lambda.function.Func1;

import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/18/2016.
 */
public class TrampolineImpl<In, Out> implements Trampoline<In, Out> {

	private final Function<In, Either<In, Out>> func;

	public TrampolineImpl(Function<In, Either<In, Out>> func) {
		this.func = func;
	}

	public Out checkedApply(In in) {

		while(true) {
			Either<In, Out> either = func.apply(in);

			if (either.isLeft())
				in = either.getLeft();
			else
				return either.getRight();
		}
	}

}