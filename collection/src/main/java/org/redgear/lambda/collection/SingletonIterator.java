package org.redgear.lambda.collection;

/**
 * Created by dcallis on 11/16/2015.
 */
public class SingletonIterator<T> implements FluentIterator<T> {

	private final T item;
	private boolean next = true;

	private SingletonIterator(T item){
		this.item = item;
	}

	public static <T> SingletonIterator<T> from(T item){
		return new SingletonIterator<>(item);
	}

	@Override
	public boolean hasNext() {
		return next;
	}

	@Override
	public T next() {
		next = false;
		return item;
	}
}
