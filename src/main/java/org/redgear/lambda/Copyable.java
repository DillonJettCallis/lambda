package org.redgear.lambda;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
@FunctionalInterface
public interface Copyable<T> extends Cloneable{

	default T copy(){
		return clone();
	}

	T clone();

}
