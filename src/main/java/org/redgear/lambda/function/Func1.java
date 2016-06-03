package org.redgear.lambda.function;

import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.function.Function;

/**
 * Created by dcallis on 11/20/2015.
 */
@FunctionalInterface
public interface Func1<In, Out> extends Func<In, Out> {

	default int arity(){
		return 1;
	}

	default Func0<Out> curry(In in){
		return () -> apply(in);
	}

	static <In, Out> Func1<In, Out> lift(Func1<In, Out> func){
		return func;
	}

	static <In, Out> Func1<In, Out> from(Function<In, Out> func){
		return func::apply;
	}
}
