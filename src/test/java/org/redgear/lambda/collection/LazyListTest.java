package org.redgear.lambda.collection;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dcallis on 4/5/2016.
 */
public class LazyListTest {

	private static final Logger log = LoggerFactory.getLogger(LazyListTest.class);

	@Test
	public void lazynessTest() {
		LazyList<Integer> lazyList = LazyList.from(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


//		for(Integer i : lazyList) {
//			log.debug("Num: {}", i);
//		}


		LazyList<Integer> subList = lazyList.subList(2, 8);


		for(Integer i : subList) {
			log.debug("Sub List: {}", i);
		}

		subList.get(1);

//		Assert.assertEquals(1, subList.lazyIndex());
	}


}
