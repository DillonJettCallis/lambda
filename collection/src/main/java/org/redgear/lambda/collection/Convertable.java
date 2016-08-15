package org.redgear.lambda.collection;

import java.util.stream.Stream;

/**
 * Created by LordBlackHole on 7/15/2016.
 */
public interface Convertable<T> {


	FluentList<T> toList();

	LazyList<T> toLazyList();

	Chain<T> toChain();

	FluentSet<T> toSet();

	//Bag

	Seq<T> toSeq();

	Stream<T> toStream();

}
