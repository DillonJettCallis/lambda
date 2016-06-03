package org.redgear.lambda.concurent;

import org.junit.Test;
import org.redgear.lambda.concurent.cont.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by dcallis on 5/13/2016.
 */
public class ContinuationTest {

	private static final Logger log = LoggerFactory.getLogger(ContinuationTest.class);

	@Test
	public void test() {
		long start = System.currentTimeMillis();
		Thread main = Thread.currentThread();

		Continuation.continuation(0)
			.map(i -> i + 10)
			.map(String::valueOf)
			.map(in -> {
				log.info("Running....");
				return in;
			}).map(in -> {
				log.info("Trying...");

				if(System.currentTimeMillis() - start < 1000) {
					Continuation.cont();
				}

				return in;
			}).map(in -> {
				log.info("Finished: {}", in);
				LockSupport.unpark(main);
				return in;
			}).handle();

		LockSupport.park();
	}


}
