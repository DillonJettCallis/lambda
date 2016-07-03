package org.redgear.lambda.collection;

import org.redgear.lambda.tuple.Tuple2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 6/9/2016.
 */
public interface FluentIterable<T> extends Iterable<T> {

	@Override
	FluentIterator<T> iterator();

	boolean isEmpty();

	default boolean isNotEmpty() {
		return !isEmpty();
	}

	FluentIterable<T> concat(Iterator<T> other);

	FluentIterable<T> concat(Iterable<T> other);

	FluentIterable<T> concat(Stream<T> other);

	FluentIterable<T> concat(T... other);

	FluentIterable<T> concat(T other);

	FluentIterable<T> concat(Seq<T> other);

	FluentIterable<T> concat(StreamList<T> other);

	FluentIterable<T> concat(ImmutableList<T> other);

	List<T> toList();

	Stream<T> toStream();

	Set<T> toSet();

	LazyList<T> toLazyList();

	Seq<T> toSeq();

	StreamList<T> toStreamList();

	ImmutableList<T> toImmutableList();

	default <Key, Value> Map<Key, Value> toMap(Function<? super T, ? extends Key> keyMapper, Function<? super T, ? extends Value> valueMapper) {
		return toStream().collect(Collectors.toMap(keyMapper, valueMapper));
	}




	<R> FluentIterable<R> map(Function<? super T, ? extends R> func);

	<R> FluentIterable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func);

	FluentIterable<T> filter(Function<? super T, Boolean> func);

	FluentIterable<T> peek(Consumer<? super T> action);

	void forEach(Consumer<? super T> consumer);

	T fold(T start, BiFunction<? super T, ? super T, ? extends T> func);

	<R> R foldLeft(R start, BiFunction<? super R, ? super T, ? extends R> func);

	<R> R foldRight(R start, BiFunction<? super T, ? super R, ? extends R> func);

	T reduce(BiFunction<? super T, ? super T, ? extends T> func);

	default <Next> FluentIterable<Next> castFiltered(Class<Next> next) {
		return filter(next::isInstance).map(next::cast);
	}

	default <Next> FluentIterable<Next> cast(Class<Next> next) {
		return map(next::cast);
	}

	boolean forAny(Function<? super T, Boolean> func);

	boolean forAll(Function<? super T, Boolean> func);

	<Other> FluentIterable<Tuple2<T, Other>> zipWith(Iterable<Other> other);
}
