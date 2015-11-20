package org.redgear.lambda;

import org.junit.Test;

/**
 * Created by dcallis on 7/17/2015.
 *
 */
public class InitializerTest {



	@Test
	public void testInit(){
		Dummy dummy = Initializer.init(Dummy::new, d -> {
			d.data = 3;
			d.nestedData = Initializer.init(Nested::new, n -> {
				n.deeperData = 5;
				n.otherData = "Data";
			});
		});


	}




	private static class Dummy {

		int data = 0;
		Nested nestedData = null;

	}

	private static class Nested {

		int deeperData = 0;
		String otherData = "";
	}

}
