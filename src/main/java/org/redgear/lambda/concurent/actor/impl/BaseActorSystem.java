package org.redgear.lambda.concurent.actor.impl;

import org.redgear.lambda.concurent.actor.Actor;
import org.redgear.lambda.concurent.actor.ActorRef;
import org.redgear.lambda.concurent.actor.ActorSystem;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by dcallis on 1/8/2016.
 */
public class BaseActorSystem implements ActorSystem {


	@Override
	public ActorRef createActor(Class<? extends Actor> clazz) {

		try {
			Actor instance = clazz.getConstructor().newInstance() ;
			return new ActorRefImpl(instance);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ActorCreationException("Failed to create Actor", e);
		}


	}

	public static class ActorCreationException extends RuntimeException {

		public ActorCreationException(String message) {
			super(message);
		}

		public ActorCreationException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
