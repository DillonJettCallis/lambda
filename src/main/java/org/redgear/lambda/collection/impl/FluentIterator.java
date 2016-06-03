package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.EmptyIterator;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

/**
 * Created by dcallis on 5/31/2016.
 */
public class FluentIterator<T> implements Seq<T>, Iterator<T> {

	private final Iterator<T> source;

	public FluentIterator(Iterator<T> source) {
		this.source = source;
	}

	public static <T> FluentIterator<T> from(Iterator<T> source) {
		return new FluentIterator<>(source);
	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}

	@Override
	public T next() {
		return source.next();
	}

	@Override
	public Iterator<T> iterator() {
		return source;
	}

	@Override
	public <R> Seq<R> map(Function<? super T, ? extends R> func) {
		return from(new Iterator<R>() {
			@Override
			public boolean hasNext() {
				return source.hasNext();
			}

			@Override
			public R next() {
				return func.apply(source.next());
			}
		});
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return toStream().mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return toStream().mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return toStream().mapToDouble(mapper);
	}

	@Override
	public Seq<T> filter(Predicate<? super T> predicate) {
		return flatMap(i -> predicate.test(i) ? Stream.of(i) : Stream.empty());
	}

	@Override
	public <R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return from(new Iterator<R>() {

			private Iterator<? extends R> result = EmptyIterator.instance();

			@Override
			public boolean hasNext() {
				if(result.hasNext())
					return true;
				else {
					if(source.hasNext()) {
						result = mapper.apply(source.next()).iterator();
						return hasNext();
					} else
						return false;
				}
			}

			@Override
			public R next() {
				return result.next();
			}
		});
	}

	@Override
	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return null;
	}

	@Override
	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return null;
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return null;
	}

	@Override
	public <R> Seq<R> flatMapIt(Function<? super T, ? extends Iterable<R>> function) {
		return null;
	}

	@Override
	public <R> Seq<R> flatMapOp(Function<? super T, ? extends Optional<? extends R>> function) {
		return null;
	}

	@Override
	public Seq<T> distinct() {
		return null;
	}

	@Override
	public Seq<T> sorted() {
		return null;
	}

	@Override
	public Seq<T> sorted(Comparator<? super T> comparator) {
		return null;
	}

	@Override
	public Seq<T> peek(Consumer<? super T> action) {
		return null;
	}

	@Override
	public Seq<T> limit(long maxSize) {
		return null;
	}

	@Override
	public Seq<T> skip(long n) {
		return null;
	}

	@Override
	public T fold(T start, BiFunction<? super T, ? super T, ? extends T> func) {
		return null;
	}

	@Override
	public <R> R foldLeft(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		return null;
	}

	@Override
	public <R> R foldRight(R start, BiFunction<? super T, ? super R, ? extends R> func) {
		return null;
	}

	@Override
	public Seq<T> concat(Iterator<T> other) {
		return null;
	}

	@Override
	public T head() {
		return null;
	}

	@Override
	public Option<T> headOption() {
		return null;
	}

	@Override
	public Seq<T> tail() {
		return null;
	}

	@Override
	public Seq<T> init() {
		return null;
	}

	@Override
	public T last() {
		return null;
	}

	@Override
	public Option<T> lastOption() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Seq<T> reverse() {
		return null;
	}

	@Override
	public Stream<T> toStream() {
		return null;
	}

	@Override
	public Seq<T> realize() {
		return null;
	}

	@Override
	public <Other> Seq<Tuple2<T, Other>> zipWith(Stream<Other> otherSteam) {
		return null;
	}

	@Override
	public void forEach(Consumer<? super T> consumer) {

	}

	@Override
	public void forEachOrdered(Consumer<? super T> action) {

	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return null;
	}

	@Override
	public T reduce(T identity, BinaryOperator<T> accumulator) {
		return null;
	}

	@Override
	public Optional<T> reduce(BinaryOperator<T> accumulator) {
		return null;
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return null;
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return null;
	}

	@Override
	public <R, A> R collect(Collector<? super T, A, R> collector) {
		return null;
	}

	@Override
	public Optional<T> min(Comparator<? super T> comparator) {
		return null;
	}

	@Override
	public Optional<T> max(Comparator<? super T> comparator) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public boolean anyMatch(Predicate<? super T> predicate) {
		return false;
	}

	@Override
	public boolean allMatch(Predicate<? super T> predicate) {
		return false;
	}

	@Override
	public boolean noneMatch(Predicate<? super T> predicate) {
		return false;
	}

	@Override
	public Optional<T> findFirst() {
		return null;
	}

	@Override
	public Optional<T> findAny() {
		return null;
	}

	@Override
	public Spliterator<T> spliterator() {
		return null;
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public Stream<T> sequential() {
		return null;
	}

	@Override
	public Stream<T> parallel() {
		return null;
	}

	@Override
	public Stream<T> unordered() {
		return null;
	}

	@Override
	public Stream<T> onClose(Runnable closeHandler) {
		return null;
	}

	@Override
	public void close() {

	}

}
