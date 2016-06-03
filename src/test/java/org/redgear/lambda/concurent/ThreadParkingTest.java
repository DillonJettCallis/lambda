package org.redgear.lambda.concurent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by dcallis on 5/13/2016.
 */
public class ThreadParkingTest {

	private static final Logger log = LoggerFactory.getLogger(ThreadParkingTest.class);

	@Test
	public void test() {
		Thread mainThread = Thread.currentThread();
		AtomicLong unParkTime = new AtomicLong();

		Thread workerThread = new Thread(() -> {

			try {
				log.info("In Worker");
				Thread.sleep(1000);
				log.info("Done sleeping");
				unParkTime.set(System.currentTimeMillis());
				LockSupport.unpark(mainThread);
				log.info("Unparked main thread");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		});

		log.info("Running worker");
		workerThread.start();
		log.info("About to park main");
		LockSupport.park();
		long parkTime = System.currentTimeMillis();
		log.info("Main thead unparked: Unparking time: {}", parkTime - unParkTime.get());

	}

}
