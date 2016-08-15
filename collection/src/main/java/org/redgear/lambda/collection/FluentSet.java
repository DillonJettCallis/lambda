package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.FluentSetImpl;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 7/17/2016.
 */
public interface FluentSet<T> extends Set<T>, Monadic<T>, Convertable<T>, Concatable<T> {

	@Override
	FluentIterator<T> iterator();

	@Override
	<R> FluentSet<R> map(Function<? super T, ? extends R> func);

	@Override
	<R> FluentSet<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func);

	@Override
	FluentSet<T> filter(Function<? super T, ? extends Boolean> func);

	@Override
	FluentSet<T> peek(Consumer<? super T> func);

	@Override
	void forEach(Consumer<? super T> action);



	FluentSet<T> concat(T... source);

	FluentSet<T> concat(Iterator<T> source);

	FluentSet<T> concat(Iterable<T> source);

	FluentSet<T> concat(Collection<T> source);

	FluentSet<T> concat(Stream<T> source);

	FluentSet<T> concat(Seq<T> source);

	<U> FluentSet<Tuple2<T, U>> zip(Iterator<U> other);

	<U, R> FluentSet<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func);

	FluentSet<Tuple2<T, Integer>> zipWithIndex();








	static <T> FluentSet<T> from() {
		return new FluentSetImpl<>(new HashSet<>());
	}

	@SafeVarargs
	static <T> FluentSet<T> from(T... source) {
		return from(Arrays.asList(source));
	}

	static <T> FluentSet<T> from(Iterator<T> source) {
		HashSet<T> list = new HashSet<>();
		while(source.hasNext())
			list.add(source.next());
		return new FluentSetImpl<>(list);
	}

	static <T> FluentSet<T> from(Iterable<T> source) {
		return from(source.iterator());
	}

	static <T> FluentSet<T> from(Collection<T> source) {
		HashSet<T> list = new HashSet<>();
		list.addAll(source);
		return new FluentSetImpl<>(list);
	}

	static <T> FluentSet<T> from(Stream<T> source) {
		return new FluentSetImpl<>(source.collect(Collectors.toCollection(HashSet::new)));
	}

	static <T> FluentSet<T> from(Seq<T> source) {
		return new FluentSetImpl<>(source.toStream().collect(Collectors.toCollection(HashSet::new)));
	}

	static <K, V> FluentSet<Tuple2<K, V>> from(Map<K, V> source) {
		return new FluentSetImpl<>(source.entrySet().stream().map(Tuple::from).collect(Collectors.toCollection(HashSet::new)));
	}
}
