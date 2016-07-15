package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.SeqImpl;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.function.Func1;
import org.redgear.lambda.tuple.Tuple2;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dcallis on 1/1/2016.
 */
public interface Seq<T> extends Stream<T>, Traversable<T> {

	@Override
	Iterator<T> iterator();

	@Override
	<R> Seq<R> map(Function<? super T, ? extends R> func);

	@Override
	Seq<T> filter(Predicate<? super T> predicate);

	@Override
	<R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

	<R> Seq<R> flatMapIt(Function<? super T, ? extends Iterable<R>> function);

	<R> Seq<R> flatMapOp(Function<? super T, ? extends Optional<? extends R>> function);

	@Override
	Seq<T> distinct();

	@Override
	Seq<T> sorted();

	@Override
	Seq<T> sorted(Comparator<? super T> comparator);

	@Override
	Seq<T> peek(Consumer<? super T> action);

	@Override
	Seq<T> limit(long maxSize);

	@Override
	Seq<T> skip(long n);

	T fold(T start, BiFunction<? super T, ? super T, ? extends T> func);

	<R> R foldLeft(R start, BiFunction<? super R, ? super T, ? extends R> func);

	<R> R foldRight(R start, BiFunction<? super T, ? super R, ? extends R> func);


	default Seq<T> drop(int num) {
		return skip(num);
	}

	default Seq<T> dropWhile(Function<? super T, Boolean> func) {
		AtomicBoolean test = new AtomicBoolean(false);

		return filter(item -> {
			if(test.get())
				return true;
			else {
				boolean result = func.apply(item);

				if(result)
					return false;
				else {
					test.set(true);
					return true;
				}
			}

		});
	}

	default Seq<T> dropUntil(Function<? super T, Boolean> func) {
		return dropWhile(it -> !func.apply(it));
	}


	default Seq<T> take(int num) {
		return limit(num);
	}


	default Seq<T> takeWhile(Function<? super T, Boolean> func) {
		AtomicBoolean test = new AtomicBoolean(false);

		return filter(item -> {
			if(test.get())
				return false;
			else {
				boolean result = func.apply(item);

				if(result)
					return true;
				else {
					test.set(true);
					return false;
				}
			}

		});
	}

	default Seq<T> takeUntil(Function<? super T, Boolean> func) {
		return takeWhile(it -> !func.apply(it));
	}

	Seq<T> concat(Iterator<T> other);

	default Seq<T> concat(Iterable<T> other) {
		return concat(other.iterator());
	}

	default Seq<T> concat(Stream<T> other) {
		return concat(other.iterator());
	}

	default Seq<T> concat(T... other) {
		return concat(new ArrayIterator<>(other));
	}

	default Seq<T> concat(T other) {
		return concat(new SingletonIterator<>(other));
	}

	default Seq<T> concat(Seq<T> other) {
		return concat(other.iterator());
	}

	default Seq<T> concat(StreamList<T> other) {
		return concat(other.iterator());
	}

	default Seq<T> concat(ImmutableList<T> other) {
		return concat(other.iterator());
	}

	Seq<T> tail();

	Seq<T> init();

	Seq<T> reverse();

	default Seq<T> subStream(int start, int take) {
		return drop(start).take(take);
	}

	default Seq<T> subStream(Function<? super T, Boolean> start, Function<? super T, Boolean> end) {
		return dropUntil(start).takeUntil(end);
	}

	Stream<T> toStream();

	Seq<T> realize();

	default Seq<Tuple2<T, Integer>> zipWithIndex() {
		return zipWith(Stream.iterate(0, i -> i + 1));
	}

	<Other> Seq<Tuple2<T, Other>> zipWith(Stream<Other> otherSteam);

	default <Next> Seq<Next> castFiltered(Class<Next> next) {
		return filter(next::isInstance).map(next::cast);
	}

	default <Next> Seq<Next> cast(Class<Next> next) {
		return map(next::cast);
	}

	@Override
	void forEach(Consumer<? super T> consumer);

	default <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
		return collect(Collectors.toMap(keyMapper, valueMapper));
	}

	static <T> Seq<T> from(Stream<T> source){
		return SeqImpl.from(source);
	}

	static <T> Seq<T> from(Iterable<T> source){
		return SeqImpl.from(source);
	}

	static <T> Seq<T> from(Iterator<T> source){
		return SeqImpl.from(source);
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	static <T> Seq<T> from(T... source){
		return SeqImpl.from(source);
	}

	static <T> Seq<T> from(T source){
		return SeqImpl.from(source);
	}

	static <K, V> Seq<Tuple2<K, V>> from(Map<K, V> source){
		return SeqImpl.from(source.entrySet().stream().map(entry -> new Tuple2<>(entry.getKey(), entry.getValue())));
	}

	@Override
	Spliterator<T> spliterator();
}
