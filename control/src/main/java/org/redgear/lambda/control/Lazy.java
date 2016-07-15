package org.redgear.lambda.control;

import java.util.function.Supplier;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class Lazy<T> implements Supplier<T> {

	private Supplier<T> source;
	private transient T value;
	private transient volatile boolean initilized = false;

	public Lazy(Supplier<T> source){
		this.source = source;
	}

	public static <T> Lazy<T> of(Supplier<T> source){
		if(source instanceof Lazy)
			return (Lazy<T>) source;
		else
			return new Lazy<>(source);
	}

	@Override
	public String toString(){
		if(initilized)
			return "Lazy(" + value + ")";
		else
			return "Lazy(?)";
	}

	@Override
	public T get() {
		if(!initilized){
			synchronized (this){
				if(!initilized){
					T t = source.get();
					value = t;
					initilized = true;
					source = null; //Release resources
					return t;
				}
			}
		}

		return value;
	}
}
