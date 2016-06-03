package org.redgear.lambda.tuple;

/**
 * Created by dcallis on 11/20/2015.
 */
public class Tuple0 implements Tuple {


	@Override
	public int arity() {
		return 0;
	}

	@Override
	public Tuple0 reverse() {
		return this;
	}


}
