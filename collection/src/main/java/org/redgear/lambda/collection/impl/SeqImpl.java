package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.collection.StreamBuilder;
import org.redgear.lambda.collection.StreamList;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static org.redgear.lambda.control.Option.none;
import static org.redgear.lambda.control.Option.option;

/**
 * Created by dcallis on 1/1/2016.
 */
public class SeqImpl<T> implements Seq<T> {

	private final Stream<T> source;

	public SeqImpl(Stream<T> source) {
		this.source = source;
	}

	public static <T> Seq<T> from(Stream<T> source) {
		return new SeqImpl<>(source);
	}

	public static <T> Seq<T> from(Iterable<T> source){
		if(source instanceof Collection) {
			return from(((Collection<T>) source).stream());
		} else {
			return from(StreamBuilder.from(source).stream());
		}
	}

	public static <T> Seq<T> from(Iterator<T> source){
		return from(StreamBuilder.from(source).stream());
	}

	@SafeVarargs
	public static <T> Seq<T> from(T... source){
		return from(Arrays.stream(source));
	}

	public static <T> Seq<T> from(T source){
		return from(Stream.of(source));
	}


	@Override
	public T fold(T start, BiFunction<? super T, ? super T, ? extends T> func) {
		return source.reduce(start, func::apply);
	}

	@Override
	public <R> R foldLeft(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		R result = start;

		Iterable<T> it = source::iterator;

		for(T element : it)
			result = func.apply(result, element);

		return result;
	}

	@Override
	public <R> R foldRight(R start, BiFunction<? super T, ? super R, ? extends R> func) {
		return reverse().foldLeft(start, (r, t) -> func.apply(t, r));
	}

	@Override
	public Seq<T> concat(Stream<T> other) {
		return from(Stream.concat(source, other));
	}

	@Override
	public Seq<T> concat(Iterable<T> other) {
		return concat(StreamBuilder.from(other).stream());
	}

	@Override
	public Seq<T> concat(Iterator<T> other) {
		return concat(StreamBuilder.from(other).stream());
	}

	@Override
	public Seq<T> concat(T other) {
		return concat(Stream.of(other));
	}

	@Override
	public Seq<T> concat(T... other) {
		return concat(Stream.of(other));
	}

	@Override
	public Seq<T> reverse() {
		LinkedList<T> values = new LinkedList<>();

		forEach(values::addFirst);

		return from(values.stream());
	}

	@Override
	public Stream<T> toStream() {
		return source;
	}

	@Override
	public Seq<T> realize() {
		return toStreamList().realize();
	}

	@Override
	public <Other> Seq<Tuple2<T, Other>> zipWith(Stream<Other> otherStream) {
		Iterator<T> left = source.iterator();
		Iterator<Other> right = otherStream.iterator();

		return StreamBuilder.from(() -> {

			if(left.hasNext() && right.hasNext()) {
				return Option.some(Tuple.of(left.next(), right.next()));
			}
			else
				return Option.<Tuple2<T, Other>>none();

		}).seq();
	}

	@Override
	public Seq<T> filter(Predicate<? super T> predicate) {
		return from(source.filter(predicate));
	}

	@Override
	public <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
		return from(source.map(mapper));
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return source.mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return source.mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return source.mapToDouble(mapper);
	}

	@Override
	public <R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return from(source.flatMap(mapper));
	}

	@Override
	public <R> Seq<R> flatMapIt(Function<? super T, ? extends Iterable<R>> function) {
		return flatMap(it -> Seq.from(function.apply(it)));
	}

	@Override
	public <R> StreamList<R> flatMapOp(Function<? super T, ? extends Optional<? extends R>> function) {
		return null;
	}

	@Override
	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return source.flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return source.flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return source.flatMapToDouble(mapper);
	}

	@Override
	public Seq<T> distinct() {
		return from(source.distinct());
	}

	@Override
	public Seq<T> sorted() {
		return from(source.sorted());
	}

	@Override
	public Seq<T> sorted(Comparator<? super T> comparator) {
		return from(source.sorted(comparator));
	}

	@Override
	public Seq<T> peek(Consumer<? super T> action) {
		return from(source.peek(action));
	}

	@Override
	public Seq<T> limit(long maxSize) {
		return from(source.limit(maxSize));
	}

	@Override
	public Seq<T> skip(long n) {
		return from(source.skip(n));
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		source.forEach(action);
	}

	@Override
	public void forEachOrdered(Consumer<? super T> action) {
		source.forEachOrdered(action);
	}

	@Override
	public Object[] toArray() {
		return source.toArray();
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return source.toArray(generator);
	}

	@Override
	public T reduce(T identity, BinaryOperator<T> accumulator) {
		return source.reduce(identity, accumulator);
	}

	@Override
	public Optional<T> reduce(BinaryOperator<T> accumulator) {
		return source.reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return source.reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return source.collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super T, A, R> collector) {
		return source.collect(collector);
	}

	@Override
	public Optional<T> min(Comparator<? super T> comparator) {
		return source.min(comparator);
	}

	@Override
	public Optional<T> max(Comparator<? super T> comparator) {
		return source.max(comparator);
	}

	@Override
	public long count() {
		return source.count();
	}

	@Override
	public boolean anyMatch(Predicate<? super T> predicate) {
		return source.anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super T> predicate) {
		return source.allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super T> predicate) {
		return source.noneMatch(predicate);
	}

	@Override
	public Optional<T> findFirst() {
		return source.findFirst();
	}

	@Override
	public Optional<T> findAny() {
		return source.findAny();
	}

	@Override
	public T head() {
		return headOption().get();
	}

	@Override
	public Option<T> headOption() {
		return Option.option(source.findFirst());
	}

	@Override
	public Seq<T> tail() {
		return from(source.skip(1));
	}

	@Override
	public Seq<T> init() {
		return isEmpty() ? Seq.from(Stream.empty()) : from(new Iterator<T>() {

			private Iterator<T> source = SeqImpl.this.iterator();
			private T next = source.next();

			@Override
			public boolean hasNext() {
				return source.hasNext();
			}

			@Override
			public T next() {
				if(!hasNext()) {
					throw new NoSuchElementException("No such element in SeqImpl");
				}

				T next = this.next;

				this.next = source.next();

				return next;
			}
		});
	}

	@Override
	public T last() {
		return lastOption().get();
	}

	@Override
	public Option<T> lastOption() {
		return this.<Option<T>>foldLeft(none(), (junk, next) -> option(next));
	}

	@Override
	public boolean isEmpty() {
		return !iterator().hasNext();
	}

	@Override
	public Iterator<T> iterator() {
		return source.iterator();
	}

	@Override
	public Spliterator<T> spliterator() {
		return source.spliterator();
	}

	@Override
	public boolean isParallel() {
		return source.isParallel();
	}

	@Override
	public Stream<T> sequential() {
		return source.sequential();
	}

	@Override
	public Stream<T> parallel() {
		return source.parallel();
	}

	@Override
	public Stream<T> unordered() {
		return source.unordered();
	}

	@Override
	public Seq<T> onClose(Runnable closeHandler) {
		return from(source.onClose(closeHandler));
	}

	@Override
	public void close() {
		source.close();
	}
}
