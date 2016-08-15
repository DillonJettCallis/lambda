package org.redgear.lambda.concurent;

import java.util.concurrent.TimeUnit;

/**
 * Created by LordBlackHole on 8/14/2016.
 */
public interface InChannel<Type> {


	void put(Type t) throws InterruptedException;

	boolean offer(Type t);

	boolean offer(Type t, long timeOut, TimeUnit unit) throws InterruptedException;


}
