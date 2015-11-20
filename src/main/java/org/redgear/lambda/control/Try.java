package org.redgear.lambda.control;

import org.redgear.lambda.function.Func0;

/**
 * Created by dcallis on 11/20/2015.
 */
public interface Try<Type> {


	static <Type> Try<Type> of(Func0<Type> source) {
		try {
			Type t = source.apply();
			return new Success<>(t);
		} catch (Exception e){
			return new Failure<>(e);
		}

	}


	class Success<Type> implements Try<Type> {

		private Type value;

		Success(Type value){
			this.value = value;
		}

	}

	class Failure<Type> implements Try<Type> {

		private Throwable throwable;

		Failure(Throwable throwable){
			this.throwable = throwable;
		}

	}

}

