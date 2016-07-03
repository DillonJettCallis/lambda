package org.redgear.lambda.collection.impl;

import org.redgear.lambda.GenericUtils;
import org.redgear.lambda.collection.Graph;
import org.redgear.lambda.collection.ImmutableList;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.function.Func2;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public class GraphImpl<Vertex, Edge> implements Graph<Vertex, Edge> {

	private final Map<Tuple2<Vertex, Vertex>, Edge> inner = new HashMap<>();
	private final Comparator<? super Vertex> comparator;

	public GraphImpl(Comparator<? super Vertex> comparator) {
		this.comparator = comparator;
	}

	private Tuple2<Vertex, Vertex> tuple(Vertex first, Vertex second) {
		if(comparator.compare(first, second) > 0)
			return Tuple.of(second, first);
		else
			return Tuple.of(first, second);
	}

	@Override
	public Option<Edge> getEdge(Vertex first, Vertex second) {
		return Option.option(inner.get(tuple(first, second)));
	}

	@Override
	public Edge getEdgeOrDefault(Vertex first, Vertex second, Edge defaultEdge) {
		Option<Edge> prev = getEdge(first, second);

		if(prev.isPresent())
			return prev.get();
		else {
			addEdge(first, second, defaultEdge);
			return defaultEdge;
		}
	}

	@Override
	public boolean hasEdge(Vertex first, Vertex second) {
		return inner.containsKey(tuple(first, second));
	}

	@Override
	public void addEdge(Vertex first, Vertex second, Edge edge) {
		inner.put(tuple(first, second), edge);
	}

	@Override
	public Option<Edge> removeEdge(Vertex first, Vertex second) {
		return Option.option(inner.remove(tuple(first, second)));
	}

	@Override
	public Option<Edge> updateEdge(Vertex first, Vertex second, Function<? super Edge, ? extends Edge> func) {
		return Option.option(inner.computeIfPresent(tuple(first, second), (key, edge) -> func.apply(edge)));
	}

	@Override
	public Set<Vertex> getVertexes() {
		return Seq.from(inner.keySet()).flatMap(Func2.lift((l, r) -> Seq.from(l, r))).toSet();
	}

	@Override
	public Collection<Edge> getEdges() {
		return inner.values();
	}

	@Override
	public Set<Node<Vertex, Edge>> getNodes() {
		return Seq.from(inner).map(Func2.lift((t, edge) -> node(t.v1, t.v2, edge))).toSet();
	}

	@Override
	public Collection<Tuple2<Vertex, Edge>> getAllRelationships(Vertex vertex) {
		return Seq.from(inner)
				.flatMapIt(Func2.lift(( t, edge) -> {

			if(t.v1.equals(vertex))
				return Option.some(Tuple.of(t.v2, edge));
			else if (t.v2.equals(vertex))
				return Option.some(Tuple.of(t.v1, edge));
			else
				return Option.none();
		})).toList();
	}

	@Override
	public Collection<Tuple2<Vertex, Vertex>> getAllMatchingNodes(Edge edge) {
		return Seq.from(inner).filter(p -> p.v2.equals(edge)).map(Tuple2::getV1).toSet();
	}

	@Override
	public List<Vertex> traverse(Vertex start, Vertex end, Function<? super Edge, ? extends Integer> weight) {
		Tuple2<ImmutableList<Vertex>, Integer> result = traverse(ImmutableList.from(start), 0, start, end, weight::apply);

		if(result == null)
			return GenericUtils.list();
		else {
			return result.v1.reverse().toList();
		}
	}

	private Tuple2<ImmutableList<Vertex>, Integer> traverse(ImmutableList<Vertex> working, int value, Vertex start, Vertex end, ToIntFunction<? super Edge> weight) {
		if(start == end)
			return Tuple.of(working, value);

		Set<Vertex> checked = working.toSet();
		Collection<Tuple2<Vertex, Edge>> relations = getAllRelationships(start);

		return Seq.from(relations)
				.filter(t -> !checked.contains(t.v1))
				.map(v -> traverse(working.prepend(v.v1), value + weight.applyAsInt(v.v2), v.v1, end, weight))
				.filter(Objects::nonNull)
				.sorted(Comparator.comparingInt(Tuple2::getV2))
				.headOption()
				.orNull();
	}

	private static <Vertex, Edge> Node<Vertex, Edge> node(Vertex first, Vertex second, Edge edge) {
		return new NodeImpl<>(first, second, edge);
	}

	private static class NodeImpl<Vertex, Edge> implements Node<Vertex, Edge> {

		private final Vertex first;
		private final Vertex second;
		private final Edge edge;

		NodeImpl(Vertex first, Vertex second, Edge edge) {
			this.first = first;
			this.second = second;
			this.edge = edge;
		}

		@Override
		public Vertex getFirst() {
			return first;
		}

		@Override
		public Vertex getSecond() {
			return second;
		}

		@Override
		public Edge getEdge() {
			return edge;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			NodeImpl<?, ?> node = (NodeImpl<?, ?>) o;

			return first.equals(node.first) && second.equals(node.second);
		}

		@Override
		public int hashCode() {
			int result = first.hashCode();
			result = 31 * result + second.hashCode();
			return result;
		}
	}
}
