package org.redgear.lambda.concurent.actor;

import org.redgear.lambda.api.Experimental;
import org.redgear.lambda.concurent.actor.impl.BaseActorSystem;

/**
 * Created by dcallis on 1/8/2016.
 */
@Experimental
public interface ActorSystem {


	ActorRef createActor(Class<? extends Actor> clazz);


	static ActorSystem basicActorSystem() {
		return new BaseActorSystem();
	}
}
