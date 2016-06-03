package org.redgear.lambda.concurent.proc;

import org.redgear.lambda.control.Try;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by dcallis on 5/16/2016.
 */
public class ExecutorBatcher {

	private final ExecutorService ex;

	public ExecutorBatcher(ExecutorService ex) {
		this.ex = ex;
	}


	public void execute(List<Runnable> tasks) {

		ex.execute(() -> tasks.forEach(Try::run));

	}

}
