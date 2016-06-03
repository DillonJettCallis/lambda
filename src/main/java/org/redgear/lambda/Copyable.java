package org.redgear.lambda;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public interface Copyable<T> {

	T copy();

	default T deepCopy() {
		return copy();
	}

}
