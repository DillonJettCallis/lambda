package org.redgear.lambda.concurent;

import org.redgear.lambda.concurent.impl.FuturePromiseImpl;
import org.redgear.lambda.control.Try;

import java.util.concurrent.CompletableFuture;

/**
 * Created by dcallis on 1/8/2016.
 */
public interface Promise<Type> {

	static <Type> Promise<Type> from(CompletableFuture<Type> source) {
		return new FuturePromiseImpl<>(source);
	}

	static <Type> Promise<Type> promise() {
		return new FuturePromiseImpl<>();
	}

	static <Type> Promise<Type> successful(Type source) {
		Promise<Type> result = promise();

		result.success(source);

		return result;
	}

	static <Type> Promise<Type> failed(Throwable cause) {
		Promise<Type> result = promise();

		result.failure(cause);

		return result;
	}

	static <Type> Promise<Type> completed(Try<Type> source) {
		Promise<Type> result = promise();

		result.complete(source);

		return result;
	}

	Future<Type> future();

	boolean isCompleted();

	boolean isSuccessful();

	void complete(Try<Type> value);

	void completeWith(Future<Type> future);

	void success(Type value);

	void failure(Throwable cause);

	void cancel();

}
