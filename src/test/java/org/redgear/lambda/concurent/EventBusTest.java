package org.redgear.lambda.concurent;

import org.junit.Assert;
import org.junit.Test;
import org.redgear.lambda.concurent.event.EventBus;
import org.redgear.lambda.concurent.event.Subscribe;
import org.redgear.lambda.concurent.event.impl.LocalEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dcallis on 2/8/2016.
 */
public class EventBusTest {

	private static final Logger log = LoggerFactory.getLogger(EventBusTest.class);

	@Test
	public void localEventBusTest() throws InterruptedException {
		EventBus eventBus = new LocalEventBus();
		EventHandler handler = new EventHandler();


		eventBus.subscribe(handler);

		eventBus.publish("Test 1");

		Thread.sleep(1000);

		Assert.assertEquals(1, handler.calls);
	}



	public static class EventHandler {

		public int calls = 0;

		@Subscribe
		public void handleEvent(String message) {
			log.debug("Handling event: {}", message);

			calls++;
		}


	}

}
