package org.redgear.lambda.collection;

import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 6/9/2016.
 */
public interface FluentIterator<T> extends Iterator<T>, Monadic<T>, Convertable<T>,  Concatable<T>, Slicable<T> {

	default <R> FluentIterator<R> map(Function<? super T, ? extends R> func) {
		return from(this::hasNext, () -> func.apply(next()));
	}

	default <R> FluentIterator<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> func) {
		return JoiningIterator.from((map(func).map(Iterable::iterator)));
	}

	default FluentIterator<T> filter(Function<? super T, ? extends Boolean> func) {
		return flatMap(in -> func.apply(in) ? () -> SingletonIterator.from(in) : EmptyIterator::instance);
	}

	default <R> R fold(R start, BiFunction<? super R, ? super T, ? extends R> func) {
		R result = start;

		while(hasNext()) {
			result = func.apply(result, next());
		}

		return result;
	}

	default Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> func) {
		if(hasNext())
			return Option.some(fold(next(), func));
		else
			return Option.none();
	}

	default FluentIterator<T> peek(Consumer<? super T> func) {
		return from(this::hasNext, () -> {
			T next = next();
			func.accept(next);
			return next;
		});
	}

	default void forEach(Consumer<? super T> func) {
		Iterator.super.forEachRemaining(func);
	}


	@Override
	default boolean anyMatch(Function<? super T, ? extends Boolean> func) {

		while(hasNext()) {
			if(func.apply(next()))
				return true;
		}

		return false;
	}

	@Override
	default boolean allMatch(Function<? super T, ? extends Boolean> func) {

		while(hasNext()) {
			if(!func.apply(next()))
				return false;
		}

		return true;
	}


	default FluentList<T> toList() {
		return FluentList.from(this);
	}

	default LazyList<T> toLazyList() {
		return LazyList.from(this);
	}

	default Chain<T> toChain() {
		return Chain.from(this);
	}

	default FluentSet<T> toSet() {
		return FluentSet.from(this);
	}

	//FluentSet, Bag

	default Seq<T> toSeq() {
		return Seq.from(this);
	}

	default Stream<T> toStream() {
		return CollectionUtils.stream(this);
	}




	default FluentIterator<T> concat(T... source) {
		return JoiningIterator.from(this, ArrayIterator.from(source));
	}

	default FluentIterator<T> concat(Iterator<T> source) {
		return JoiningIterator.from(this, source);
	}

	default FluentIterator<T> concat(Iterable<T> source) {
		return JoiningIterator.from(this, source.iterator());
	}

	default FluentIterator<T> concat(Collection<T> source) {
		return JoiningIterator.from(this, source.iterator());
	}

	default FluentIterator<T> concat(Stream<T> source) {
		return JoiningIterator.from(this, source.iterator());
	}

	default FluentIterator<T> concat(Seq<T> source) {
		return JoiningIterator.from(this, source.iterator());
	}

	default <U> FluentIterator<Tuple2<T, U>> zip(Iterator<U> other) {
		return from(() -> hasNext() && other.hasNext(), () -> Tuple.of(next(), other.next()));
	}

	default <U, R> FluentIterator<R> zipWith(Iterator<U> other, BiFunction<? super T, ? super U, ? extends R> func) {
		return from(() -> hasNext() && other.hasNext(), () -> func.apply(next(), other.next()));
	}

	default FluentIterator<Tuple2<T, Integer>> zipWithIndex() {
		AtomicInteger index = new AtomicInteger(0);
		return from(this::hasNext, () -> Tuple.of(next(), index.getAndIncrement()));
	}



	default FluentIterator<T> take(int num) {
		ArrayList<T> result = new ArrayList<>(num);

		for(int i = 0; i < num && hasNext(); i++) {
			result.add(next());
		}

		result.trimToSize();

		return from(result.iterator());
	}

	default FluentIterator<T> takeWhile(Function<? super T, ? extends Boolean> func) {
		ArrayList<T> result = new ArrayList<>();

		while(hasNext()) {
			T next = next();
			if(func.apply(next))
				result.add(next);
			else
				break;
		}

		result.trimToSize();

		return from(result.iterator());
	}

	default FluentIterator<T> drop(int num) {
		for(int i = 0; i < num && hasNext(); i++) {
			next();
		}

		return this;
	}

	default FluentIterator<T> dropWhile(Function<? super T, ? extends Boolean> func) {
		while(hasNext()) {
			T next = next();
			if(!func.apply(next))
				return JoiningIterator.from(SingletonIterator.from(next), this);
		}

		return this;
	}

	default FluentIterator<T> slice(int start, int take) {
		return drop(start).take(take);
	}


	static <T> FluentIterator<T> from(Iterator<T> source) {
		if(source instanceof FluentIterator)
			return (FluentIterator<T>) source;
		else
			return from(source::hasNext, source::next);
	}

	static <T> FluentIterator<T> from(BooleanSupplier hasNext, Supplier<T> next) {
		return new FluentIterator<T>() {

			@Override
			public boolean hasNext() {
				return hasNext.getAsBoolean();
			}

			@Override
			public T next() {
				return next.get();
			}
		};
	}
}
