package org.redgear.lambda.concurent.proc;

import java.util.function.Function;

/**
 * Created by dcallis on 5/13/2016.
 */
public interface Processor<In> {

	void process(In in);

	default void process(In...in) {
		for(In i : in) {
			process(i);
		}
	}


}
