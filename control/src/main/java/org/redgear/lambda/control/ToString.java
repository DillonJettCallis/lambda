package org.redgear.lambda.control;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class ToString implements Supplier<String> {

	private final Lazy<String> message;

	public <T> ToString(Supplier<T> message){
		this.message = Lazy.of(() -> String.valueOf(message.get()));
	}

	public <T> ToString(T source, Function<? super T, String> func){
		this.message = Lazy.of(() -> func.apply(source));
	}

	public static <T> ToString from(Supplier<T> message) {
		return new ToString(message);
	}

	public static <T> ToString from(T source, Function<? super T, String> func){
		return new ToString(source, func);
	}

	@Override
	public String toString(){
		return get();
	}

	@Override
	public String get() {
		return message.get();
	}
}
