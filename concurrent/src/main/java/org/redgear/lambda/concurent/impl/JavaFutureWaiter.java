package org.redgear.lambda.concurent.impl;

import org.redgear.lambda.concurent.Future;
import org.redgear.lambda.concurent.Promise;
import org.redgear.lambda.control.Try;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Created by dcallis on 4/6/2016.
 */
public class JavaFutureWaiter {

	public static JavaFutureWaiter instance = new JavaFutureWaiter();

	private final ExecutorService ex = Executors.newSingleThreadExecutor();

	private JavaFutureWaiter() {

	}

	public <T> Future<T> wait(Supplier<Boolean> checker, Try.CheckedSupplier<T> source) {
		if(checker.get()) {
			return Future.completed(Try.of(source));
		}

		Promise<T> promise = Promise.promise();

		enqueue(checker, source, promise);

		return promise.future();
	}

	public <T> Future<T> wait(java.util.concurrent.Future<T> source) {
		if(source instanceof CompletableFuture) {
			return Future.from((CompletableFuture<T>) source);
		}

		if(source.isDone()) {
			return Future.completed(Try.of(source::get));
		}

		return wait(source::isDone, source::get);
	}


	private <T> void enqueue(Supplier<Boolean> checker, Try.CheckedSupplier<T> source, Promise<T> promise) {
		ex.execute(() -> {

			if(checker.get()) {
				promise.complete(Try.of(source));
			} else {
				enqueue(checker, source, promise);
			}

		});
	}


}
