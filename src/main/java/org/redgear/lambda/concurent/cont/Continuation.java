package org.redgear.lambda.concurent.cont;

import org.redgear.lambda.concurent.Future;
import org.redgear.lambda.concurent.Promise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

/**
 * Created by dcallis on 5/13/2016.
 */
public interface Continuation<T> {

	ContinuationHandler handler = new ContinuationHandler();

	static <T> Continuation<T> continuation(T in) {
		return new Continuation<T>() {
			private final T source = in;

			@Override
			public T value() {
				return source;
			}
		};
	}

	static void cont() {
		throw new ContinueException();
	}


	T value();

	default <Out> Continuation<Out> map(Function<? super T, ? extends Out> func) {
		Continuation<T> source = this;

		return new Continuation<Out>(){

			private Out out = null;

			@Override
			public Out value() {
				if(out == null) {
					out = func.apply(source.value());
				}

				return out;
			}
		};
	}

	default Future<T> handle() {
		Promise<T> promise = Promise.promise();
		handler.handle(this, promise);
		return promise.future();
	}


	class ContinueException extends RuntimeException {

	}

	class ContinuationHandler {

		private <T> void handle(Continuation<T> cont, Promise<T> promise) {
			ForkJoinPool.commonPool().execute(() -> {

				try {
					T value = cont.value();
					promise.success(value);
				} catch (ContinueException e) {
					handle(cont, promise);
				} catch (Exception e) {
					promise.failure(e);
				}

			});
		}

	}

}
