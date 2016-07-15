package org.redgear.lambda.concurent.impl;

import org.redgear.lambda.concurent.Agent;
import org.redgear.lambda.concurent.Future;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dcallis on 1/8/2016.
 */
public class FutureAgent<Type> implements Agent<Type> {

	private ExecutorService ex;
	private Type currentValue;
	private Future<Type> future;

	public FutureAgent(Future<Type> source, Type initial, ExecutorService ex) {
		this.ex = ex;
		currentValue = initial;
		future = source.map(value -> currentValue = value);
	}

	public FutureAgent(Type initial, ExecutorService ex) {
		this.ex = ex;
		currentValue = initial;
		future = Future.successful(initial);
	}

	@Override
	public Type getNow() {
		return currentValue;
	}

	@Override
	public synchronized Future<Type> alter(Function<? super Type, ? extends Type> func) {
		return future = future.map(value -> currentValue = func.apply(value), ex);
	}

	public Future<Type> future(){
		return future;
	}

	@Override
	public Future<Type> get() {
		return alter(Function.identity());
	}

	@Override
	public Future<Type> set(Type value) {
		return alter(ignored -> value);
	}

	@Override
	public Future<Type> alter(Consumer<? super Type> func) {
		return alter(value -> {
			func.accept(value);
			return value;
		});
	}

	@Override
	public void forEach(Consumer<? super Type> func) {
		alter(func);
	}

	@Override
	public <Next> Agent<Next> map(Function<? super Type, ? extends Next> func, ExecutorService ex) {
		return new FutureAgent<>(get().map(func), null, ex);
	}

	@Override
	public <Next> Agent<Next> flatMap(Function<? super Type, ? extends Agent<Next>> func, ExecutorService ex) {
		return new FutureAgent<>(get().flatMap(value -> func.apply(value).future()), null, ex);
	}

	@Override
	public ExecutorService getExecutorService() {
		return ex;
	}

	@Override
	public void setExecutorService(ExecutorService ex) {
		this.ex = ex;
	}
}
