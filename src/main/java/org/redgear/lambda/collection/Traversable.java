package org.redgear.lambda.collection;

import org.redgear.lambda.ToString;
import org.redgear.lambda.control.Option;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dcallis on 4/5/2016.
 */
public interface Traversable<T> extends Iterable<T> {


	T head();

	Option<T> headOption();

	Traversable<T> tail();

	Traversable<T> init();

	T last();

	Option<T> lastOption();

	Traversable<T> take(int num);

	Traversable<T> drop(int num);

	boolean isEmpty();

	default boolean isNotEmpty() {
		return !isEmpty();
	}

	@Override
	default Iterator<T> iterator() {
		return new Iterator<T>() {

			private Traversable<T> next = Traversable.this;

			@Override
			public boolean hasNext() {
				return next.isNotEmpty();
			}

			@Override
			public T next() {
				T head = next.head();

				next = next.tail();

				return head;
			}
		};
	}

	Traversable<T> concat(Iterator<T> other);

	default Traversable<T> concat(Iterable<T> other) {
		return concat(other.iterator());
	}

	default Traversable<T> concat(Stream<T> other) {
		return concat(other.iterator());
	}

	default Traversable<T> concat(T... other) {
		return concat(new ArrayIterator<>(other));
	}

	default Traversable<T> concat(T other) {
		return concat(new SingletonIterator<>(other));
	}

	default Traversable<T> concat(Seq<T> other) {
		return concat(other.iterator());
	}

	default Traversable<T> concat(StreamList<T> other) {
		return concat(other.iterator());
	}

	default Traversable<T> concat(ImmutableList<T> other) {
		return concat(other.iterator());
	}

	default List<T> toList() {
		return toStream().collect(Collectors.toList());
	}

	Stream<T> toStream();

	default Set<T> toSet() {
		return toStream().collect(Collectors.toSet());
	}

	default <Key, Value> Map<Key, Value> toMap(Function<? super T, ? extends Key> keyMapper, Function<? super T, ? extends Value> valueMapper) {
		return toStream().collect(Collectors.toMap(keyMapper, valueMapper));
	}

	default LazyList<T> toLazyList() {
		return LazyList.from(this);
	}

	default Seq<T> toSeq() {
		return Seq.from(this);
	}

	default StreamList<T> toStreamList() {
		return StreamList.from(this);
	}

	default ImmutableList<T> toImmutableList() {
		return ImmutableList.from(this);
	}

	default String mkString() {
		return toStream().map(String::valueOf).collect(Collectors.joining());
	}

	default String mkString(CharSequence delimiter) {
		return toStream().map(String::valueOf).collect(Collectors.joining(delimiter));
	}

	default String mkString(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return toStream().map(String::valueOf).collect(Collectors.joining(delimiter, prefix, suffix));
	}

	default ToString mkStringLazily() {
		return ToString.from(this::mkString);
	}
}
