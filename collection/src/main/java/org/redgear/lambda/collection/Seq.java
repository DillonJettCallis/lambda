package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.SeqImpl;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by dcallis on 1/1/2016.
 */
public interface Seq<T> extends Iterable<T>, Monadic<T>, Convertable<T>, Concatable<T>, Slicable<T>, Ordered<T> {

	@Override
	<R> Seq<R> map(Function<? super T, ? extends R> func);

	@Override
	Seq<T> filter(Function<? super T, ? extends Boolean> predicate);

	@Override
	<R> Seq<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper);

	@Override
	Seq<T> peek(Consumer<? super T> action);

	@Override
	Seq<T> concat(T... source);

	@Override
	Seq<T> concat(Iterator<T> source);

	@Override
	Seq<T> concat(Iterable<T> source);

	@Override
	Seq<T> concat(Collection<T> source);

	@Override
	Seq<T> concat(Stream<T> source);

	@Override
	Seq<T> concat(Seq<T> source);

	@Override
	<U> Seq<Tuple2<T, U>> zip(Iterator<U> other);

	@Override
	<U, R> Seq<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func);

	@Override
	Seq<Tuple2<T, Integer>> zipWithIndex();

	@Override
	FluentIterator<T> iterator();

	default <Next> Seq<Next> castFiltered(Class<Next> next) {
		return filter(next::isInstance).map(next::cast);
	}

	default <Next> Seq<Next> cast(Class<Next> next) {
		return map(next::cast);
	}

	@Override
	void forEach(Consumer<? super T> consumer);


	Seq<T> take(int num);

	Seq<T> takeWhile(Function<? super T, ? extends Boolean> func);

	Seq<T> drop(int num);

	Seq<T> dropWhile(Function<? super T, ? extends Boolean> func);

	Seq<T> slice(int start, int take);



	Seq<T> sort();

	Seq<T> sort(Comparator<T> comparator);

	Seq<T> reverse();



	static <T> Seq<T> from(Stream<T> source){
		return new SeqImpl<>(source);
	}

	static <T> Seq<T> from(Collection<T> source){
		return from(source.stream());
	}

	static <T> Seq<T> from(Iterable<T> source){
		return from(StreamBuilder.from(source).stream());
	}

	static <T> Seq<T> from(Iterator<T> source){
		return from(StreamBuilder.from(source).stream());
	}

	@SafeVarargs
	static <T> Seq<T> from(T... source){
		return from(Arrays.stream(source));
	}

	static <K, V> Seq<Tuple2<K, V>> from(Map<K, V> source){
		return from(source.entrySet().stream().map(entry -> new Tuple2<>(entry.getKey(), entry.getValue())));
	}
}
