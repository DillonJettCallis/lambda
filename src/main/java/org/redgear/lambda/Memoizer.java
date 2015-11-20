package org.redgear.lambda;

import java.util.function.Supplier;

/**
 * Created by dcallis on 7/27/2015.
 *
 */
@FunctionalInterface
public interface Memoizer<T> extends Supplier<T> {


}
