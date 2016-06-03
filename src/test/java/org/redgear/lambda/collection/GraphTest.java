package org.redgear.lambda.collection;

import org.junit.Assert;
import org.junit.Test;
import org.redgear.lambda.function.Consumer2;
import org.redgear.lambda.function.Func3;
import org.redgear.lambda.tuple.Tuple;
import org.redgear.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by LordBlackHole on 6/2/2016.
 */
public class GraphTest {

	private static final Logger log = LoggerFactory.getLogger(GraphTest.class);

	@Test
	public void basicTest() {
		Graph<String, Integer> graph = Graph.graph();


		graph.addEdge("First", "Second", 1);
		graph.addEdge("Third", "First", 2);
		graph.addEdge("Second", "Fourth", 3);
		graph.addEdge("Fourth", "First", 4);
		graph.addEdge("Second", "Third", 5);


		assertEquals(1, graph.getEdge("First", "Second").get().intValue());
		assertEquals(2, graph.getEdge("First", "Third").get().intValue());
		assertEquals(3, graph.getEdge("Fourth", "Second").get().intValue());
		assertEquals(4, graph.getEdge("Fourth", "First").get().intValue());
		assertEquals(5, graph.getEdge("Third", "Second").get().intValue());
	}

	@Test
	public void removeTest() {
		Graph<String, Integer> graph = Graph.graph();

		graph.addEdge("First", "Second", 1);

		assertEquals(1, graph.getEdge("First", "Second").get().intValue());

		graph.removeEdge("First", "Second");

		assertTrue(graph.getEdge("First", "Second").isEmpty());
	}

	@Test
	public void updateTest() {
		Graph<String, Integer> graph = Graph.graph();


		graph.addEdge("First", "Second", 1);

		assertEquals(1, graph.getEdge("First", "Second").get().intValue());

		graph.updateEdge("First", "Second", edge -> edge + 1);

		assertEquals(2, graph.getEdge("First", "Second").get().intValue());
	}

	@Test
	public void vertexListTest() {
		Graph<String, Integer> graph = Graph.graph();


		graph.addEdge("First", "Second", 1);
		graph.addEdge("Third", "First", 2);
		graph.addEdge("Second", "Fourth", 3);
		graph.addEdge("Fourth", "First", 4);
		graph.addEdge("Second", "Third", 5);

		Set<String> vertexes = graph.getVertexes();

		assertEquals(4, vertexes.size());
		assertTrue(vertexes.contains("First"));
		assertTrue(vertexes.contains("Second"));
		assertTrue(vertexes.contains("Third"));
		assertTrue(vertexes.contains("Fourth"));
	}

	@Test
	public void emptyTest() {
		assertTrue(Graph.<String, Integer>graph().getEdge("First", "Second").isEmpty());
	}

	@Test
	public void getOrDefaultTest() {
		Graph<String, Integer> graph = Graph.graph();

		assertEquals(1, graph.getEdgeOrDefault("First", "Second", 1).intValue());

		assertEquals(1, graph.getEdge("First", "Second").get().intValue());
	}

	@Test
	public void hasEdgeTest() {
		Graph<String, Integer> graph = Graph.graph();

		graph.addEdge("First", "Second", 1);

		assertTrue(graph.hasEdge("First", "Second"));
		assertFalse(graph.hasEdge("First", "Third"));
	}

	@Test
	public void removeNoneTest() {
		Graph<String, Integer> graph = Graph.graph();

		assertTrue(graph.removeEdge("First", "Second").isEmpty());
	}


	@Test
	public void getEdgesTest() {
		Graph<String, Integer> graph = Graph.graph();

		graph.addEdge("First", "Second", 1);
		graph.addEdge("Third", "First", 2);
		graph.addEdge("Second", "Fourth", 3);
		graph.addEdge("Fourth", "First", 4);
		graph.addEdge("Second", "Third", 5);


		Collection<Integer> edges = graph.getEdges();

		assertEquals(5, edges.size());
		assertTrue(edges.contains(1));
		assertTrue(edges.contains(2));
		assertTrue(edges.contains(3));
		assertTrue(edges.contains(4));
		assertTrue(edges.contains(5));
	}

	@Test
	public void getNodesTest() {
		Graph<String, Integer> graph = Graph.graph();

		graph.addEdge("First", "Second", 1);
		graph.addEdge("Third", "First", 2);
		graph.addEdge("Second", "Fourth", 3);
		graph.addEdge("Fourth", "First", 4);
		graph.addEdge("Second", "Third", 5);

		Set<Graph.Node<String, Integer>> nodes = graph.getNodes();

		List<Graph.Node<String, Integer>> sorted = Seq.from(nodes)
				.sorted((f, s) -> f.getEdge() - s.getEdge())
				.toList();

		Func3<String, String, Integer, Void> checker = (first, second, num) -> {

			Graph.Node<String, Integer> node = sorted.remove(0);

			assertEquals(first, node.getFirst());
			assertEquals(second, node.getSecond());
			assertEquals(num, node.getEdge());

			return null;
		};

		//This test does rely on String.compare being consistent.
		checker.apply("First", "Second", 1);
		checker.apply("First", "Third", 2);
		checker.apply("Fourth", "Second", 3);
		checker.apply("First", "Fourth", 4);
		checker.apply("Second", "Third", 5);

		assertEquals(0, sorted.size());
	}


	@Test
	public void relationShipTest() {
		Graph<String, Integer> graph = Graph.graph();

		graph.addEdge("First", "Second", 1);
		graph.addEdge("Third", "First", 2);
		graph.addEdge("Second", "Fourth", 3);
		graph.addEdge("Fourth", "First", 4);
		graph.addEdge("Second", "Third", 5);

		Collection<Tuple2<String, Integer>> relationships = graph.getAllRelationships("First");

		List<Tuple2<String, Integer>> sorted = Seq.from(relationships)
				.sorted((f, s) -> f.v2 - s.v2)
				.toList();


		Consumer2<String, Integer> checker = (vertex, edge) -> {
			assertEquals(Tuple.of(vertex, edge), sorted.remove(0));
		};


		checker.apply("Second", 1);
		checker.apply("Third", 2);
		checker.apply("Fourth", 4);

		assertEquals(0, sorted.size());
	}


	@Test
	public void getAllMatchingNodesTest() {
		Graph<String, Integer> graph = Graph.graph();

		graph.addEdge("First", "Second", 1);
		graph.addEdge("Third", "First", 2);
		graph.addEdge("Second", "Fourth", 3);
		graph.addEdge("Fourth", "First", 4);
		graph.addEdge("Second", "Third", 5);

		assertEquals(Tuple.of("First", "Second"), graph.getAllMatchingNodes(1).iterator().next());
	}
}
