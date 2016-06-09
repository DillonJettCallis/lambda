package org.redgear.lambda.concurent;

import org.junit.Test;
import org.redgear.lambda.concurent.actor.*;
import org.redgear.lambda.concurent.actor.MessageDelegator.DelegatingActor;
import org.redgear.lambda.concurent.actor.MessageDelegator.MessageDelegatorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;

/**
 * Created by dcallis on 2/26/2016.
 */
public class BasicActorTest {

	private static final Logger log = LoggerFactory.getLogger(BasicActorTest.class);

	@Test
	public void basicActorTest() throws InterruptedException, ExecutionException, TimeoutException {
		ActorSystem system = ActorSystem.basicActorSystem();

		ActorRef mathActor = system.createActor(CalculatorActor.class);


		Future<Integer> result = mathActor.ask(new MathMessage(2, 5), Integer.class);

		AtomicInteger atom = new AtomicInteger(0);

		Promise<Void> last = Promise.promise();

		result.onSuccess(i -> {
			log.info("MathActor result: {}", i);
			atom.set(i);
			last.success(null);
		});
		result.onFailure(t -> {
			log.error("Something went wrong", t);
			fail();
			last.failure(t);
		});

		last.future().await(2, TimeUnit.SECONDS);

		if(atom.get() == 0)
			fail();
	}



	public static class CalculatorActor extends DelegatingActor {

		@Override
		public MessageDelegatorBuilder build() {
			log.info("Message received");

			return MessageDelegator.builder().handle(MathMessage.class, (math, context) -> {
				int result = math.num1 + math.num2;

				context.respond(result);
			});
		}
	}

	public static class MathMessage {

		int num1;
		int num2;

		public MathMessage(int num1, int num2) {
			this.num1 = num1;
			this.num2 = num2;
		}
	}
}
