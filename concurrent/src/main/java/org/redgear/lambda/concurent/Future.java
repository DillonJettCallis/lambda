package org.redgear.lambda.concurent;

import org.redgear.lambda.concurent.impl.FuturePromiseImpl;
import org.redgear.lambda.concurent.impl.JavaFutureWaiter;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.control.Try;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.concurrent.ForkJoinPool.commonPool;

/**
 * Created by dcallis on 1/8/2016.
 */
public interface Future<Type> extends Awaitable {

	static <Type> Future<Type> from(CompletableFuture<Type> source) {
		return new FuturePromiseImpl<>(source);
	}

	static <Type> Future<Type> from(java.util.concurrent.Future<Type> source) {
		return JavaFutureWaiter.instance.wait(source);
	}

	static <Type> Future<Type> from(Supplier<Type> func) {
		return from(func, commonPool());
	}

	static <Type> Future<Type> from(Supplier<Type> func, ExecutorService ex) {
		return from(CompletableFuture.supplyAsync(func, ex));
	}

	static Future<Void> from(Runnable func) {
		return from(func, commonPool());
	}

	static Future<Void> from(Runnable func, ExecutorService ex) {
		return from(CompletableFuture.runAsync(func, ex));
	}

	static <Type> Future<Type> successful(Type value) {
		return Promise.successful(value).future();
	}

	static <Type> Future<Type> failed(Throwable value) {
		return Promise.<Type>failed(value).future();
	}

	static <Type> Future<Type> completed(Try<Type> value) {
		return Promise.completed(value).future();
	}

	boolean isSuccessful();

	boolean isCompleted();

	default <Next> Future<Next> map(Function<? super Type, ? extends Next> func) {
		return map(func, commonPool());
	}

	<Next> Future<Next> map(Function<? super Type, ? extends Next> func, ExecutorService ex);

	default <Next> Future<Next> flatMap(Function<? super Type, ? extends Future<Next>> func) {
		return flatMap(func, commonPool());
	}

	<Next> Future<Next> flatMap(Function<? super Type, ? extends Future<Next>> func, ExecutorService ex);

	default void forEach(Consumer<? super Type> func) {
		forEach(func, commonPool());
	}

	default void forEach(Consumer<? super Type> func, ExecutorService ex) {
		onSuccess(func, ex);
	}

	default void onSuccess(Consumer<? super Type> func) {
		onSuccess(func, commonPool());
	}

	void onSuccess(Consumer<? super Type> func, ExecutorService ex);

	default void onFailure(Consumer<? super Throwable> func) {
		onFailure(func, commonPool());
	}

	void onFailure(Consumer<? super Throwable> func, ExecutorService ex);

	default Future<Type> recover(Function<? super Throwable, ? extends Type> func) {
		return recover(func, commonPool());
	}

	Future<Type> recover(Function<? super Throwable, ? extends Type> func, ExecutorService ex);

	default void onComplete(Consumer<? super Try<Type>> func) {
		onComplete(func, commonPool());
	}

	void onComplete(Consumer<? super Try<Type>> func, ExecutorService ex);

	default Future<Type> filter(Predicate<? super Type> func) {
		return filter(func, commonPool());
	}

	Future<Type> filter(Predicate<? super Type> func, ExecutorService ex);

	Option<Try<Type>> get();

	Future<Type> ready(long time, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

	default void await(long time, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		ready(time, unit);
	}

}
