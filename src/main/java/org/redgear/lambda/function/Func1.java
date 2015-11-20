package org.redgear.lambda.function;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func1<In, Out> extends Func<In, Out> {

	default int arity(){
		return 1;
	}

}
