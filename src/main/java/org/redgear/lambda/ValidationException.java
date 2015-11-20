package org.redgear.lambda;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class ValidationException extends RuntimeException{

	public ValidationException(){
		super();
	}

	public ValidationException(String message){
		super(message);
	}

	public ValidationException(String message, Throwable cause){
		super(message, cause);
	}
}
