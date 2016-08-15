package org.redgear.lambda.collection;

import org.redgear.lambda.tuple.Tuple2;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 7/16/2016.
 */
public interface Concatable<T> {

	Concatable<T> concat(T... source);

	Concatable<T> concat(Iterator<T> source);

	Concatable<T> concat(Iterable<T> source);

	Concatable<T> concat(Collection<T> source);

	Concatable<T> concat(Stream<T> source);

	Concatable<T> concat(Seq<T> source);

	<U> Concatable<Tuple2<T, U>> zip(Iterator<U> other);

	<U, R> Concatable<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func);

	Concatable<Tuple2<T, Integer>> zipWithIndex();

}
