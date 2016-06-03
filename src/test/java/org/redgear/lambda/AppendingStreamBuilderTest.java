package org.redgear.lambda;

import org.junit.Assert;
import org.junit.Test;
import org.redgear.lambda.collection.StreamBuilder.AppendingStreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by dcallis on 1/6/2016.
 */
public class AppendingStreamBuilderTest {

	private static final Logger log = LoggerFactory.getLogger(AppendingStreamBuilderTest.class);

	@Test
	public void appendingTest() throws InterruptedException {

		AppendingStreamBuilder<String> builder = new AppendingStreamBuilder<>();
		AtomicBoolean finished = new AtomicBoolean(false);
		AtomicBoolean added = new AtomicBoolean(false);

		builder.add("Test 1").add("Test 2");

		new Thread(() -> {
			List<String> tests = builder.stream().peek(log::debug).collect(Collectors.toList());

			Assert.assertEquals(6, tests.size());
			finished.set(true);
			Assert.assertTrue(added.get());
		}).start();

		Thread.sleep(500);

		builder.add("Test 3").add("Test 4").add("Test 5").add("Test 6");
		added.set(true);

		Thread.sleep(500);

		builder.close();

		Thread.sleep(500);

		Assert.assertTrue("Thread did not finish", finished.get());
	}



}
