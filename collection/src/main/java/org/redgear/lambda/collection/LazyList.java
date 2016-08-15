package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.LazyListImpl;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.redgear.lambda.control.Option.none;
import static org.redgear.lambda.control.Option.some;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public interface LazyList<T> extends List<T>, Monadic<T>, Convertable<T>, Concatable<T> {

	<R> LazyList<R> map(Function<? super T, ? extends R> func);

	<R> LazyList<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func);

	LazyList<T> filter(Function<? super T, ? extends Boolean> func);

	LazyList<T> peek(Consumer<? super T> func);

	@Override
	default void forEach(Consumer<? super T> func) {
		iterator().forEachRemaining(func);
	}



	LazyList<T> concat(T... source);

	LazyList<T> concat(Iterator<T> source);

	LazyList<T> concat(Iterable<T> source);

	LazyList<T> concat(Collection<T> source);

	LazyList<T> concat(Stream<T> source);

	LazyList<T> concat(Seq<T> source);

	<U> LazyList<Tuple2<T, U>> zip(Iterator<U> other);

	<U, R> LazyList<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func);

	LazyList<Tuple2<T, Integer>> zipWithIndex();



	@Override
	LazyList<T> subList(int start, int end);

	static <T> LazyList<T> from(Iterator<T> source){
		return new LazyListImpl<>(source);
	}

	static <T> LazyList<T> from(Stream<T> source){
		return new LazyListImpl<>(source.iterator());
	}

	static <T> LazyList<T> from(Seq<T> source){
		return new LazyListImpl<>(source.iterator());
	}

	static <T> LazyList<T> from(Iterable<T> source){
		if(source instanceof LazyList)
			return (LazyList<T>) source;
		else
			return new LazyListImpl<>(source.iterator());
	}

	@SafeVarargs
	static <T> LazyList<T> from(T... source){
		return new LazyListImpl<>(ArrayIterator.from(source));
	}
}
