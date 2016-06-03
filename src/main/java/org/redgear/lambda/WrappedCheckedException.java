package org.redgear.lambda;

/**
 * Created by dcallis on 11/23/2015.
 */
public class WrappedCheckedException extends RuntimeException {

	public WrappedCheckedException(Throwable cause){
		super(cause.getMessage(), cause);
	}

}
