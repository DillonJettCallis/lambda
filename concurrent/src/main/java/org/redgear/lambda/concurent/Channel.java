package org.redgear.lambda.concurent;

/**
 * Created by LordBlackHole on 8/14/2016.
 */
public interface Channel<Type> {


	InChannel<Type> getInChannel();

	OutChannel<Type> getOutChannel();


}
