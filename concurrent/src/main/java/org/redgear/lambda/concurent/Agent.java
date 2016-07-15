package org.redgear.lambda.concurent;

import org.redgear.lambda.concurent.impl.FutureAgent;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.concurrent.ForkJoinPool.commonPool;

/**
 * Created by dcallis on 1/8/2016.
 */
public interface Agent<Type> {


	static <Type> Agent<Type> from(Type initial) {
		return from(initial, commonPool());
	}

	static <Type> Agent<Type> from(Type initial, ExecutorService ex) {
		return new FutureAgent<>(initial, ex);
	}

	static <Type> Agent<Type> from(Future<Type> source) {
		return from(source, commonPool());
	}

	static <Type> Agent<Type> from(Future<Type> source, ExecutorService ex) {
		return new FutureAgent<>(source, null, ex);
	}

	static <Type> Agent<Type> from(Type initial, Future<Type> source) {
		return from(initial, source, commonPool());
	}

	static <Type> Agent<Type> from(Type initial, Future<Type> source, ExecutorService ex) {
		return new FutureAgent<>(source, initial, ex);
	}

	Type getNow();

	Future<Type> future();

	Future<Type> get();

	Future<Type> set(Type value);

	Future<Type> alter(Function<? super Type, ? extends Type> func);

	Future<Type> alter(Consumer<? super Type> func);

	void forEach(Consumer<? super Type> func);

	default <Next> Agent<Next> map(Function<? super Type, ? extends Next> func) {
		return map(func, getExecutorService());
	}

	<Next> Agent<Next> map(Function<? super Type, ? extends Next> func, ExecutorService ex);

	default <Next> Agent<Next> flatMap(Function<? super Type, ? extends Agent<Next>> func) {
		return flatMap(func, getExecutorService());
	}

	<Next> Agent<Next> flatMap(Function<? super Type, ? extends Agent<Next>> func, ExecutorService ex);

	ExecutorService getExecutorService();

	void setExecutorService(ExecutorService ex);

}
