package org.redgear.lambda;

import javaslang.Function1;
import javaslang.Function2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dcallis on 11/4/2015.
 */
public class ProxyTest {


	public interface Math {

		int add(int a, int b);

		int sub(int a, int b);

	}


	@Test
	public void testProxy(){
		Proxy<Math> mathProxy = new Proxy<>(Math.class);

		mathProxy.addMethod("add", Function2.lift((Integer a, Integer b) -> a + b));
		mathProxy.addMethod("sub", Function2.lift((Integer a, Integer b) -> a - b));


		Math math = mathProxy.getProxiedObject();

		assertEquals(6, math.add(3, 3));
		assertEquals(2, math.sub(5, 3));
	}




}
