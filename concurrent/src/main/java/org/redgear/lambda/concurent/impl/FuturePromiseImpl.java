package org.redgear.lambda.concurent.impl;

import org.redgear.lambda.concurent.Future;
import org.redgear.lambda.concurent.Promise;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.control.Try;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by dcallis on 1/8/2016.
 */
public class FuturePromiseImpl<Type> implements Future<Type>, Promise<Type> {

	private final CompletableFuture<Type> inner;

	public FuturePromiseImpl(CompletableFuture<Type> inner) {
		this.inner = inner;
	}

	public FuturePromiseImpl() {
		this.inner = new CompletableFuture<>();
	}

	private static <Type> FuturePromiseImpl<Type> from(CompletableFuture<Type> inner) {
		return new FuturePromiseImpl<>(inner);
	}

	@Override
	public boolean isSuccessful() {
		return inner.isDone() && !inner.isCompletedExceptionally();
	}

	@Override
	public void complete(Try<Type> value) {
		Objects.requireNonNull(value, "value was null");

		value.onSuccess(inner::complete);
		value.onFailure(inner::completeExceptionally);
	}

	@Override
	public void completeWith(Future<Type> future) {
		Objects.requireNonNull(future, "future was null");

		if(future == this)
			throw new IllegalArgumentException("Attempt to complete a Promise with it's own Future.");

		future.onComplete(this::complete);
	}

	@Override
	public void success(Type value) {
		if(inner.isDone())
			throw new IllegalStateException("Attempt to complete an already completed Promise.");

		inner.complete(value);
	}

	@Override
	public void failure(Throwable cause) {
		if(inner.isDone())
			throw new IllegalStateException("Attempt to complete an already completed Promise.");

		inner.completeExceptionally(cause);
	}

	@Override
	public void cancel() {
		inner.cancel(false);
	}

	@Override
	public Future<Type> future() {
		return this;
	}

	@Override
	public boolean isCompleted() {
		return inner.isDone();
	}

	@Override
	public <Next> Future<Next> map(Function<? super Type, ? extends Next> func, ExecutorService ex) {
		return from(inner.thenApplyAsync(func, ex));
	}

	@Override
	public <Next> Future<Next> flatMap(Function<? super Type, ? extends Future<Next>> func, ExecutorService ex) {
		Promise<Next> result = new FuturePromiseImpl<>();

		onSuccess(value -> result.completeWith(func.apply(value)), ex);
		onFailure(result::failure);

		return result.future();
	}

	@Override
	public void onSuccess(Consumer<? super Type> func, ExecutorService ex) {
		inner.thenAcceptAsync(func, ex);
	}

	@Override
	public void onFailure(Consumer<? super Throwable> func, ExecutorService ex) {
		onComplete(tri -> tri.onFailure(func), ex);
	}

	@Override
	public void onComplete(Consumer<? super Try<Type>> func, ExecutorService ex) {
		inner.whenCompleteAsync((value, e) -> {
			if(e == null)
				func.accept(Try.success(value));
			else
				func.accept(Try.<Type>failure(e));
		}, ex);
	}

	@Override
	public Future<Type> recover(Function<? super Throwable, ? extends Type> func, ExecutorService ex) {
		Promise<Type> result = new FuturePromiseImpl<>();

		onSuccess(result::success, ex);
		onFailure(e -> result.success(func.apply(e)), ex);

		return result.future();
	}

	@Override
	public Future<Type> filter(Predicate<? super Type> func, ExecutorService ex) {
		Promise<Type> result = new FuturePromiseImpl<>();

		onSuccess(value -> {
			if(func.test(value))
				result.success(value);
			else
				result.failure(new NoSuchElementException("Value failed predicate"));
		}, ex);
		onFailure(result::failure, ex);

		return result.future();
	}

	@Override
	public Option<Try<Type>> get() {
		if(isCompleted())
			return Option.some(Try.of(inner::get));
		else
			return Option.none();
	}

	@Override
	public Future<Type> ready(long time, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		inner.get(time, unit);
		return this;
	}
}
