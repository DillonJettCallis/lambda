package org.redgear.lambda.collection.impl;

import org.redgear.lambda.tuple.Tuple2;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.redgear.lambda.GenericUtils.*;

/**
 * Created by LordBlackHole on 6/5/2016.
 */
public class FluentIterators {


	public static <In, Out> Iterable<Out> map(Iterable<In> it, Function<? super In, ? extends Out> func) {
		List<Out> result = list();

		for(In i : it)
			result.add(func.apply(i));

		return result;
	}

	public static <In, Out> Iterable<Out> flatMap(Iterable<In> it, Function<? super In, ? extends Iterable<Out>> func) {
		List<Out> result = list();

		for(In i : it)
			for(Out o : func.apply(i))
				result.add(o);

		return result;
	}


	public static <In> Iterable<In> filter(Iterable<In> it, Function<? super In, Boolean> func) {
		List<In> result = list();

		for(In i : it)
			if(func.apply(i))
				result.add(i);

		return result;
	}

	public static <Left, Right> Iterable<Tuple2<Left, Right>> zip(Iterable<Left> left, Iterable<Right> right) {
		List<Tuple2<Left, Right>> result = list();

		Iterator<Left> leftIt = left.iterator();
		Iterator<Right> rightIt = right.iterator();

		while(leftIt.hasNext() && rightIt.hasNext()) {
			result.add(tuple(leftIt.next(), rightIt.next()));
		}

		return result;
	}

	public static <In> Iterable<Tuple2<In, Integer>> zipWithIndex(Iterable<In> it) {
		List<Tuple2<In, Integer>> result = list();

		int index = 0;

		for(In i : it)
			result.add(tuple(i, index++));

		return result;
	}

}
