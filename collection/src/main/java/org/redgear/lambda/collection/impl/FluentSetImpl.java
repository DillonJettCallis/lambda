package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.*;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 7/17/2016.
 */
public class FluentSetImpl<T> extends AbstractSet<T> implements FluentSet<T> {

	private final HashSet<T> source;

	public FluentSetImpl(HashSet<T> source) {
		this.source = source;
	}

	@Override
	public FluentIterator<T> iterator() {
		return FluentIterator.from(source.iterator());
	}

	@Override
	public int size() {
		return source.size();
	}

	@Override
	public <R> FluentSet<R> map(Function<? super T, ? extends R> func) {
		HashSet<R> result = new HashSet<>(size());

		for(T in : source)
			result.add(func.apply(in));

		return new FluentSetImpl<>(result);
	}

	@Override
	public <R> FluentSet<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func) {
		HashSet<R> result = new HashSet<>(size());

		for(T in : source)
			for(R out : func.apply(in))
				result.add(out);


		return new FluentSetImpl<>(result);
	}

	@Override
	public FluentSet<T> filter(Function<? super T, ? extends Boolean> func) {
		HashSet<T> result = new HashSet<>(size());

		for(T in : source)
			if(func.apply(in))
				result.add(in);


		return new FluentSetImpl<>(result);
	}

	@Override
	public <R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		R result = start;

		for(T in : source)
			result = func.apply(result, in);


		return result;
	}

	@Override
	public Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func) {
		return iterator().reduce(func);
	}

	@Override
	public FluentSet<T> peek(Consumer<? super T> func) {
		forEach(func);
		return this;
	}

	@Override
	public boolean anyMatch(Function<? super T, ? extends Boolean> func) {

		for(T in : source) {
			if(func.apply(in))
				return true;
		}

		return false;
	}

	@Override
	public boolean allMatch(Function<? super T, ? extends Boolean> func) {
		for(T in : source) {
			if(!func.apply(in))
				return false;
		}

		return true;
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		iterator().forEach(action);
	}

	@Override
	public FluentSet<T> concat(T... source) {
		HashSet<T> result = new HashSet<T>(size() + source.length);

		result.addAll(this.source);
		result.addAll(Arrays.asList(source));

		return new FluentSetImpl<>(result);
	}

	@Override
	public FluentSet<T> concat(Iterator<T> source) {
		HashSet<T> result = new HashSet<>(size());

		result.addAll(this.source);

		while(source.hasNext())
			result.add(source.next());

		return new FluentSetImpl<>(result);
	}

	@Override
	public FluentSet<T> concat(Iterable<T> source) {
		HashSet<T> result = new HashSet<>(size());

		result.addAll(this.source);

		for(T in : source)
			result.add(in);

		return new FluentSetImpl<>(result);
	}

	@Override
	public FluentSet<T> concat(Collection<T> source) {
		HashSet<T> result = new HashSet<>(size() + source.size());

		result.addAll(this.source);
		result.addAll(source);

		return new FluentSetImpl<>(result);
	}

	@Override
	public FluentSet<T> concat(Stream<T> source) {
		return concat(source.iterator());
	}

	@Override
	public FluentSet<T> concat(Seq<T> source) {
		return concat(source.iterator());
	}

	@Override
	public <U> FluentSet<Tuple2<T, U>> zip(Iterator<U> other) {
		return FluentSet.from(iterator().zip(other));
	}

	@Override
	public <U, R> FluentSet<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func) {
		return FluentSet.from(iterator().zipWith(other, func));
	}

	@Override
	public FluentSet<Tuple2<T, Integer>> zipWithIndex() {
		return FluentSet.from(iterator().zipWithIndex());
	}

	@Override
	public FluentList<T> toList() {
		return FluentList.from(source);
	}

	@Override
	public LazyList<T> toLazyList() {
		return LazyList.from(source.iterator());
	}

	@Override
	public Chain<T> toChain() {
		return Chain.from(source);
	}

	@Override
	public FluentSet<T> toSet() {
		return this;
	}

	@Override
	public Seq<T> toSeq() {
		return Seq.from(source.stream());
	}

	@Override
	public Stream<T> toStream() {
		return source.stream();
	}

}
