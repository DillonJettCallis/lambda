package org.redgear.lambda.concurent.actor;

import org.redgear.lambda.api.Experimental;
import org.redgear.lambda.concurent.Future;

/**
 * Created by dcallis on 1/8/2016.
 */
@Experimental
public interface ActorRef {


	void send(Object message);

	Future<Object> ask(Object message);

	<ResponseType> Future<ResponseType> ask(Object message, Class<ResponseType> responseType);


}
