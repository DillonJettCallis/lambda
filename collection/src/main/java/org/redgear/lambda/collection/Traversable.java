package org.redgear.lambda.collection;

import org.redgear.lambda.control.Option;

import java.util.Iterator;

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

	boolean isEmpty();

	default boolean isNotEmpty() {
		return !isEmpty();
	}

	@Override
	default FluentIterator<T> iterator() {
		return new FluentIterator<T>() {

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
}
