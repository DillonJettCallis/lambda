package org.redgear.lambda.concurent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by dcallis on 1/28/2016.
 */
public interface Awaitable {
	
	void await(long time, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

	Awaitable ready(long time, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
	
	
	
}
