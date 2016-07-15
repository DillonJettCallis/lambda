package org.redgear.lambda.collection.impl;

import org.redgear.lambda.collection.Graph;
import org.redgear.lambda.collection.ImmutableList;
import org.redgear.lambda.collection.Seq;
import org.redgear.lambda.control.Option;
import org.redgear.lambda.function.Func2;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public class GraphImpl<Vertex, Edge> implements Graph<Vertex, Edge> {

	private static final Logger log = LoggerFactory.getLogger(GraphImpl.class);
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
		if(first == null || second == null)
			throw new IllegalArgumentException("Null Vertexes are not allowed.");

		if(first.equals(second))
			throw new IllegalArgumentException("Vertex can't have an edge with itself.");

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
		List<Tuple2<ImmutableList<Vertex>, Integer>> sorted = new ArrayList<>();
		sorted.add(Tuple.of(ImmutableList.from(start), 0));
		Set<Vertex> checked = new HashSet<>();
		checked.add(start);

		while(!sorted.isEmpty()) {
			log.info("Sorted: {}", sorted);

			Tuple2<ImmutableList<Vertex>, Integer> next = sorted.remove(0);

			if(next.v1.head().equals(end))
				return next.v1.reverse().toList();

			checked.add(next.v1.head());

			for(Tuple2<Vertex, Edge> pair : getAllRelationships(next.v1.head())){

				if(!checked.contains(pair.v1)) {

					sorted.add(Tuple.of(next.v1.prepend(pair.v1), next.v2 + weight.apply(pair.v2)));
				}
			}

			Collections.sort(sorted, Comparator.comparing(Tuple2::getV2));

		}

		//No chain found
		return new ArrayList<>();
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
