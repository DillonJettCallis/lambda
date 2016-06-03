package org.redgear.lambda.concurent.actor;

import org.redgear.lambda.api.Experimental;

/**
 * Created by dcallis on 1/8/2016.
 */
@Experimental
public interface MessageContext {

	ActorRef sender();

	void respond(Object message);


}
