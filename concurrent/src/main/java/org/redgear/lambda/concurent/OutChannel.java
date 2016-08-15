package org.redgear.lambda.concurent;

import org.redgear.lambda.control.Option;

import java.util.concurrent.TimeUnit;

/**
 * Created by LordBlackHole on 8/14/2016.
 */
public interface OutChannel<Type> {

	Option<Type> poll();

	Option<Type> poll(long timeOut, TimeUnit unit) ;

	Type take() throws InterruptedException;

}
