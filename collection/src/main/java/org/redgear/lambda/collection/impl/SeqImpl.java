 package org.redgear.lambda.collection.impl;

 import org.redgear.lambda.collection.*;
 import org.redgear.lambda.control.Option;
 import org.redgear.lambda.tuple.Tuple;
 import org.redgear.lambda.tuple.Tuple2;

 import java.util.*;
 import java.util.function.BiFunction;
 import java.util.function.Consumer;
 import java.util.function.Function;
 import java.util.stream.Collector;
 import java.util.stream.Collectors;
 import java.util.stream.Stream;

/**
 * Created by dcallis on 1/1/2016.
 */
public class SeqImpl<T> implements Seq<T> {

	private final Stream<T> source;

	public SeqImpl(Stream<T> source) {
		this.source = source;
	}



	@Override
	public Seq<T> concat(Stream<T> other) {
		return new SeqImpl<>(Stream.concat(source, other));
	}

	@Override
	public Seq<T> concat(Seq<T> source) {
		return concat(source.toStream());
	}

	@Override
	public <U> Seq<Tuple2<T, U>> zip(Iterator<U> other) {
		return zipWith(other, Tuple::of);
	}

	@Override
	public <U, R> Seq<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func) {
		return new SeqImpl<>(source.flatMap(l -> other.hasNext() ? Stream.of(func.apply(l, other.next())) : Stream.empty()));
	}

	@Override
	public Seq<Tuple2<T, Integer>> zipWithIndex() {
		return zip(Stream.iterate(0, n -> n + 1).iterator());
	}

	@Override
	public FluentIterator<T> iterator() {
		return FluentIterator.from(source.iterator());
	}

	@Override
	public void forEach(Consumer<? super T> consumer) {
		source.forEach(consumer);
	}

	@Override
	public Seq<T> take(int num) {
		return new SeqImpl<>(source.limit(num));
	}

	@Override
	public Seq<T> takeWhile(Function<? super T, ? extends Boolean> func) {
		return Seq.from(iterator().takeWhile(func));
	}

	@Override
	public Seq<T> drop(int num) {
		return new SeqImpl<>(source.skip(num));
	}

	@Override
	public Seq<T> dropWhile(Function<? super T, ? extends Boolean> func) {
		return Seq.from(iterator().dropWhile(func));
	}

	@Override
	public Seq<T> slice(int start, int take) {
		return drop(start).take(take);
	}

	@Override
	public Seq<T> sort() {
		return new SeqImpl<>(source.sorted());
	}

	@Override
	public Seq<T> sort(Comparator<T> comparator) {
		return new SeqImpl<>(source.sorted(comparator));
	}

	@Override
	public Seq<T> reverse() {
		List<T> contents = source.collect(Collectors.toList());
		Collections.reverse(contents);
		return new SeqImpl<>(contents.stream());
	}

	@Override
	public Seq<T> concat(Iterable<T> other) {
		return concat(StreamBuilder.from(other).stream());
	}

	@Override
	public Seq<T> concat(Collection<T> source) {
		return concat(source.stream());
	}

	@Override
	public Seq<T> concat(Iterator<T> other) {
		return concat(StreamBuilder.from(other).stream());
	}

	@Override
	public <R> Seq<R> map(Function<? super T, ? extends R> func) {
		return new SeqImpl<>(source.map(func));
	}

	@Override
	public Seq<T> filter(Function<? super T, ? extends Boolean> predicate) {
		return new SeqImpl<>(source.filter(predicate::apply));
	}

	@Override
	public <R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		return iterator().fold(start, func);
	}

	@Override
	public Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func) {
		return iterator().reduce(func);
	}

	@Override
	public <R> Seq<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
		return Seq.from(iterator().flatMap(mapper));
	}

	@Override
	public Seq<T> peek(Consumer<? super T> action) {
		return new SeqImpl<>(source.peek(action));
	}

	@Override
	public boolean anyMatch(Function<? super T, ? extends Boolean> func) {
		return source.anyMatch(func::apply);
	}

	@Override
	public boolean allMatch(Function<? super T, ? extends Boolean> func) {
		return source.allMatch(func::apply);
	}

	@SafeVarargs
	@Override
	public final Seq<T> concat(T... other) {
		return concat(Arrays.stream(other));
	}

	@Override
	public FluentList<T> toList() {
		return FluentList.from(this);
	}

	@Override
	public LazyList<T> toLazyList() {
		return LazyList.from(this);
	}

	@Override
	public Chain<T> toChain() {
		return Chain.from(this);
	}

	@Override
	public FluentSet<T> toSet() {
		return FluentSet.from(this);
	}

	@Override
	public Seq<T> toSeq() {
		return this;
	}

	@Override
	public Stream<T> toStream() {
		return source;
	}

}
