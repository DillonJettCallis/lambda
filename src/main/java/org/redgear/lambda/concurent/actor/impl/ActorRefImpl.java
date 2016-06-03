package org.redgear.lambda.concurent.actor.impl;

import org.redgear.lambda.concurent.*;
import org.redgear.lambda.concurent.actor.Actor;
import org.redgear.lambda.concurent.actor.ActorRef;
import org.redgear.lambda.concurent.actor.MessageContext;

/**
 * Created by dcallis on 1/8/2016.
 */
public class ActorRefImpl implements ActorRef {


	private final Agent<Actor> agent;

	public ActorRefImpl(Actor actor) {
		this.agent = Agent.from(actor);
	}


	@Override
	public void send(Object message) {
		agent.forEach(actor -> actor.accept(message, new MessageContextImpl(this)));
	}

	@Override
	public Future<Object> ask(Object message) {
		Promise<Object> promise = Promise.promise();

		agent.forEach(actor -> {
			MessageContext context = new MessageContextImpl(this, promise);

			actor.accept(message, context);

			if(!promise.isCompleted())
				promise.cancel();
		});

		return promise.future();
	}

	@Override
	public <ResponseType> Future<ResponseType> ask(Object message, Class<ResponseType> responseType) {
		return ask(message).filter(responseType::isInstance).map(responseType::cast);
	}
 }
