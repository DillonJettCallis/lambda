package org.redgear.lambda.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by dcallis on 7/28/2015.
 *
 */
public class JoiningIterator<T> implements FluentIterator<T> {

	private Iterator<? extends Iterator<? extends T>> source;
	private Iterator<? extends T> current;

	private JoiningIterator(Iterator<? extends Iterator<? extends T>> source) {
		this.source = source;
	}

	@SafeVarargs
	public static <T> JoiningIterator<T> from(Iterator<? extends T>... source) {
		return new JoiningIterator<>(ArrayIterator.from(source));
	}

	public static <T> JoiningIterator<T> from(Iterator<? extends Iterator<? extends T>> source) {
		return new JoiningIterator<>(source);
	}

	private boolean advance() {
		if(source != null && source.hasNext()) {
			current = source.next();
			return hasNext();
		} else {
			//No more elements. Set current and source to null to GC them.
			current = null;
			source = null;
			return false;
		}
	}

	@Override
	public boolean hasNext() {
		return current != null && current.hasNext() || advance();
	}

	@Override
	public T next() {
		T next = current.next();
		return next;
	}
}
