package org.redgear.lambda.concurent.event;

import org.redgear.lambda.api.Experimental;
import org.redgear.lambda.concurent.Future;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dcallis on 1/11/2016.
 */
@Experimental
public interface EventBus {

	void subscribe(Object obj);

	void publish(Object event);

}
