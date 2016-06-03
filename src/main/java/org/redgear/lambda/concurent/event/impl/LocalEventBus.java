package org.redgear.lambda.concurent.event.impl;

import org.redgear.lambda.collection.ImmutableList;
import org.redgear.lambda.concurent.Future;
import org.redgear.lambda.concurent.event.EventBus;
import org.redgear.lambda.concurent.event.Subscribe;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.control.Try;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * Created by dcallis on 1/11/2016.
 */
public class LocalEventBus implements EventBus {

	private final ExecutorService ex;
	private final Map<Class, ImmutableList<Consumer>> handlerMap = new ConcurrentHashMap<>();

	public LocalEventBus(ExecutorService ex) {
		this.ex = ex;
	}

	public LocalEventBus() {
		this(ForkJoinPool.commonPool());
	}

	public void subscribe(Class eventClass, Consumer handler) {
		handlerMap.compute(eventClass, (c, subscribers) -> Option.option(subscribers).map(s -> s.prepend(handler)).getOrElse(ImmutableList.from(handler)));
	}

	@Override
	public void subscribe(Object obj) {
		Arrays.stream(obj.getClass().getDeclaredMethods())
				.filter(method -> null != method.getAnnotation(Subscribe.class))
				.filter(method -> method.getParameterCount() == 1)
				.forEach(method -> {
					Class<?> eventType = method.getParameterTypes()[0];

					subscribe(eventType, event -> Try.of(() -> method.invoke(obj, event)).get());
				});
	}

	@Override
	public void publish(Object event) {
		ex.execute(() -> handlerMap.get(event.getClass()).forEach(h -> h.accept(event)));
	}
}
