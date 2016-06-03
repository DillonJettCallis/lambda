package org.redgear.lambda.control;

import org.redgear.lambda.WrappedCheckedException;
import org.redgear.lambda.function.Func0;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dcallis on 11/20/2015.
 */
public interface Try<Type> {

	boolean isSuccessful();

	Type get();

	<Next> Try<Next> map(Function<? super Type, ? extends Next> func);

	<Next> Try<Next> flatMap(Function<? super Type, ? extends Try<Next>> func);

	void forEach(Consumer<? super Type> func);

	void onSuccess(Consumer<? super Type> func);

	void onFailure(Consumer<? super Throwable> func);

	Try<Type> recover(Function<? super Throwable, ? extends Type> func);

	Option<Type> toOption();

	static Try<Void> run(Runnable source) {
		return of(() -> {
			source.run();
			return null;
		});
	}

	static <Type> Try<Type> of(Func0<Type> source) {
		try {
			Type t = source.checkedApply();
			return new Success<>(t);
		} catch (Exception e){
			return new Failure<>(e);
		}
	}

	static <Type> Success<Type> success(Type value) {
		return new Success<>(value);
	}


	static <Type> Failure<Type> failure(Throwable cause) {
		return new Failure<>(cause);
	}

	static <Inner> Try<Inner> flatten(Try<Try<Inner>> tri) {
		if(tri.isSuccessful())
			return tri.get();
		else
			return tri.map(x -> null);
	}


	class Success<Type> implements Try<Type> {

		private Type value;

		Success(Type value){
			this.value = value;
		}

		@Override
		public boolean isSuccessful() {
			return true;
		}

		@Override
		public Type get() {
			return value;
		}

		@Override
		public <Next> Try<Next> map(Function<? super Type, ? extends Next> func) {
			return Try.of(() -> func.apply(value));
		}

		@Override
		public <Next> Try<Next> flatMap(Function<? super Type, ? extends Try<Next>> func) {
			return flatten(Try.of(() -> func.apply(value)));
		}

		@Override
		public void forEach(Consumer<? super Type> func) {
			onSuccess(func);
		}

		@Override
		public void onSuccess(Consumer<? super Type> func) {
			func.accept(value);
		}

		@Override
		public void onFailure(Consumer<? super Throwable> func) {

		}

		@Override
		public Try<Type> recover(Function<? super Throwable, ? extends Type> func) {
			return this;
		}

		public Option.Some<Type> toOption() {
			return Option.some(value);
		}
	}

	class Failure<Type> implements Try<Type> {

		private Throwable exception;

		Failure(Throwable exception){
			this.exception = exception;
		}

		@Override
		public boolean isSuccessful() {
			return false;
		}

		@Override
		public Type get() {
			if(exception instanceof RuntimeException)
				throw (RuntimeException) exception;
			else
				throw new WrappedCheckedException(exception);
		}

		@Override
		public <Next> Try<Next> map(Function<? super Type, ? extends Next> func) {
			return new Failure<>(exception);
		}

		@Override
		public <Next> Try<Next> flatMap(Function<? super Type, ? extends Try<Next>> func) {
			return new Failure<>(exception);
		}

		@Override
		public void forEach(Consumer<? super Type> func) {

		}

		@Override
		public void onSuccess(Consumer<? super Type> func) {

		}

		@Override
		public void onFailure(Consumer<? super Throwable> func) {
			if(exception instanceof WrappedCheckedException)
				func.accept(exception.getCause());
			else
				func.accept(exception);
		}

		@Override
		public Try<Type> recover(Function<? super Throwable, ? extends Type> func) {
			if(exception instanceof WrappedCheckedException)
				return Try.of(() -> func.apply(exception.getCause()));
			else
				return Try.of(() -> func.apply(exception));
		}

		public Option.None<Type> toOption() {
			return Option.none();
		}
	}

}

