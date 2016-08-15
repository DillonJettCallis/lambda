package org.redgear.lambda.concurent.impl;

import org.redgear.lambda.concurent.Channel;
import org.redgear.lambda.concurent.InChannel;
import org.redgear.lambda.concurent.OutChannel;
import org.redgear.lambda.control.Option;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by LordBlackHole on 8/14/2016.
 */
public class ChannelImpl<Type> implements Channel<Type> {

	private final BlockingQueue<Type> queue;

	public ChannelImpl(int capacity) {
		this.queue = new ArrayBlockingQueue<>(capacity);
	}

	public ChannelImpl() {
		this.queue = new LinkedBlockingDeque<>();
	}

	public ChannelImpl(BlockingQueue<Type> queue) {
		this.queue = queue;
	}


	@Override
	public InChannel<Type> getInChannel() {
		return new InChannel<Type>() {

			@Override
			public void put(Type t) throws InterruptedException {
				queue.put(t);
			}

			@Override
			public boolean offer(Type t) {
				return queue.offer(t);
			}

			@Override
			public boolean offer(Type t, long timeOut, TimeUnit unit) throws InterruptedException {
				return queue.offer(t, timeOut, unit);
			}

		};
	}

	@Override
	public OutChannel<Type> getOutChannel() {
		return new OutChannel<Type>() {

			@Override
			public Option<Type> poll() {
				return Option.option(queue.poll());
			}

			@Override
			public Option<Type> poll(long timeOut, TimeUnit unit) {
				try {
					return Option.option(queue.poll(timeOut, unit));
				} catch (InterruptedException e) {
					return Option.none();
				}
			}

			@Override
			public Type take() throws InterruptedException {
				return queue.take();
			}

		};
	}
}
