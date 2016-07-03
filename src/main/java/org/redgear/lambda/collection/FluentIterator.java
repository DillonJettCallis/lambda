package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.FluentIterators;

import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by LordBlackHole on 6/9/2016.
 */
public interface FluentIterator<T> extends Iterator<T> {

	default <Out> FluentIterator<Out> map(Function<? super T, ? extends Out> func) {
		return FluentIterators.map(this, func);
	}

	default <Out> FluentIterator<Out> flatMap(Function<? super T, ? extends Iterator<Out>> func) {
		return FluentIterators.flatMap(this, func);
	}

	default FluentIterator<T> filter(Function<? super T, Boolean> func) {
		return FluentIterators.filter(this, func);
	}

	default FluentIterator<T> concat(FluentIterator<T> other) {
		return FluentIterators.concat(this, other);
	}

	default FluentIterator<T> concat(FluentIterator<T>... other) {
		return concat(new ArrayIterator<>(other));
	}

	default FluentIterator<T> concat(Iterator<? extends Iterator<T>> other) {
		return concat(FluentIterators.concat(other));
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
