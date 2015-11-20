package org.redgear.lambda;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by dcallis on 7/27/2015.
 *
 */
public class RefreashingMemoizer<T> implements Memoizer<T>{

	private final Supplier<T> source;
	private final long nanos;
	private transient T value;
	private transient volatile long lastTime = -1;

	public RefreashingMemoizer(Supplier<T> source, long expireTime, TimeUnit unit) {
		this.source = source;
		this.nanos = unit.toNanos(expireTime);
	}

	@Override
	public String toString(){
		if(lastTime == -1)
			return "Lazy(" + value + ")";
		else
			return "Lazy(?)";
	}

	private synchronized T load(long time){
		if(time > lastTime){
			value = source.get();
			return value;
		}
		else
			return value;
	}

	@Override
	public T get() {
		long currTime = System.nanoTime();

		if(value != null && lastTime + nanos > currTime)
			return value;
		else
			return load(currTime);

	}
}
