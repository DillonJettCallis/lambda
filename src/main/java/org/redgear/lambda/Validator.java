package org.redgear.lambda;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class Validator {

	private static final String defaultMessage = "Invalid data";


	public static <T> T notNull(T obj){
		return notNull(obj, defaultMessage);
	}

	public static <T> T notNull(T obj, String message){
		if (obj != null)
			return obj;
		else
			throw new ValidationException(message);
	}

	public static String notEmpty(String str){
		return notEmpty(str, defaultMessage);
	}

	public static String notEmpty(String str, String message){
		if(str != null && !str.isEmpty())
			return str;
		else
			throw new ValidationException(message);
	}

	public static String notBlank(String str) {
		return notBlank(str, defaultMessage);
	}

	public static String notBlank(String str, String message) {
		notEmpty(str, message);

		if(str.chars().allMatch(Character::isWhitespace))
			throw new ValidationException(message);
		else
			return str;
	}

	public static String notBlankAndTrim(String str) {
		return notBlankAndTrim(str, defaultMessage);
	}

	public static String notBlankAndTrim(String str, String message) {
		return notBlank(str, message).trim();
	}

	public static <T, C extends Collection<T>> C notEmpty(C str){
		return notEmpty(str, defaultMessage);
	}

	public static <T, C extends Collection<T>> C notEmpty(C str, String message){
		if(str != null && !str.isEmpty())
			return str;
		else
			throw new ValidationException(message);
	}

	public static <T> T validate(T t, Predicate<T> func) {
		return validate(t, func, defaultMessage);
	}

	public static <T> T validate(T t, Predicate<T> func, String message) {
		Objects.requireNonNull(func, "predicate is null");

		if(t != null && func.test(t))
			return t;
		else
			throw new ValidationException(message);
	}

}
