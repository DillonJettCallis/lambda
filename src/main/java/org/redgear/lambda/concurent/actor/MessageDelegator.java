package org.redgear.lambda.concurent.actor;

import org.redgear.lambda.api.Experimental;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Created by dcallis on 1/8/2016.
 */
@Experimental
public class MessageDelegator implements Actor {

	private final Map<Class<?>, BiConsumer<Object, MessageContext>> handlers;

	private MessageDelegator(Map<Class<?>, BiConsumer<Object, MessageContext>> handlers) {
		this.handlers = Collections.unmodifiableMap(handlers);
	}

	public void accept(Object message, MessageContext context) {
		Optional<BiConsumer<Object, MessageContext>> messageContextBiConsumer = handlers.entrySet().stream()
				.filter(entry -> entry.getKey().isInstance(message))
				.findFirst()
				.map(Map.Entry::getValue);

		messageContextBiConsumer.ifPresent(func -> func.accept(message, context));
		messageContextBiConsumer.orElseThrow(() -> new NoMatchFoundException("MessageDelegating Actor cannot handle message of type: " + message.getClass()));
	}


	public static MessageDelegatorBuilder builder() {
		return new MessageDelegatorBuilder();
	}

	public static class MessageDelegatorBuilder {
		private final Map<Class<?>, BiConsumer<Object, MessageContext>> handlers = new LinkedHashMap<>();

		public <MessageType> MessageDelegatorBuilder handle(Class<MessageType> clazz, BiConsumer<MessageType, MessageContext> handler) {
			handlers.put(clazz, (BiConsumer<Object, MessageContext>) handler);
			return this;
		}

		public MessageDelegator build() {
			return new MessageDelegator(handlers);
		}
	}

	public static class NoMatchFoundException extends RuntimeException {

		public NoMatchFoundException(String message) {
			super(message);
		}

	}

	public static abstract class DelegatingActor implements Actor {

		private MessageDelegator delegator = build().build();

		public abstract MessageDelegatorBuilder build();

		public void accept(Object message, MessageContext context) {
			delegator.accept(message, context);
		}

	}

}
