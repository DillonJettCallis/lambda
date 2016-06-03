package org.redgear.lambda.concurent.actor.impl;

import org.redgear.lambda.concurent.actor.ActorRef;
import org.redgear.lambda.concurent.actor.MessageContext;
import org.redgear.lambda.concurent.Promise;

/**
 * Created by dcallis on 1/8/2016.
 */
public class MessageContextImpl implements MessageContext{

	private final ActorRef sender;
	private final Promise<Object> promise;

	public MessageContextImpl(ActorRef sender) {
		this.sender = sender;
		this.promise = null;
	}

	public MessageContextImpl(ActorRef sender, Promise<Object> promise) {
		this.sender = sender;
		this.promise = promise;
	}

	@Override
	public ActorRef sender() {
		return sender;
	}

	@Override
	public void respond(Object message) {
		if(promise != null)
			promise.success(message);
	}

}
