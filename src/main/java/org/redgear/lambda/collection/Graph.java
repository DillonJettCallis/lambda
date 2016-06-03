package org.redgear.lambda.collection;

import org.redgear.lambda.collection.impl.GraphImpl;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.tuple.Tuple2;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public interface Graph<Vertex, Edge> {

	static <Vertex extends Comparable<Vertex>, Edge> Graph<Vertex, Edge> graph() {
		return new GraphImpl<>(Comparator.naturalOrder());
	}

	static <Vertex, Edge> Graph<Vertex, Edge> graph(Comparator<? super Vertex> comparator) {
		return new GraphImpl<>(comparator);
	}

	Option<Edge> getEdge(Vertex first, Vertex second);

	Edge getEdgeOrDefault(Vertex first, Vertex second, Edge defaultEdge);

	boolean hasEdge(Vertex first, Vertex second);

	void addEdge(Vertex first, Vertex second, Edge edge);

	Option<Edge> removeEdge(Vertex first, Vertex second);

	Option<Edge> updateEdge(Vertex first, Vertex second, Function<? super Edge, ? extends Edge> func);

	Set<Vertex> getVertexes();

	Collection<Edge> getEdges();

	Set<Node<Vertex, Edge>> getNodes();

	Collection<Tuple2<Vertex, Edge>> getAllRelationships(Vertex vertex);

	Collection<Tuple2<Vertex, Vertex>> getAllMatchingNodes(Edge edge);

	public interface Node<Vertex, Edge> {

		Vertex getFirst();

		Vertex getSecond();

		Edge getEdge();

	}



}
