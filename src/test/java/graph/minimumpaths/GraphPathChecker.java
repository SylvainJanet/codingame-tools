package graph.minimumpaths;

import static org.assertj.core.api.Assertions.assertThat;

import graph.GraphNodeTestImpl;
import graph.GraphPath;
import graph.GraphTestImpl;
import graph.GraphWeightedEdge;
import java.util.List;

public class GraphPathChecker {

  public static void assertPathValidity(
      GraphPath<GraphNodeTestImpl> actualPath,
      int expectedStart,
      int expectedEnd,
      double expectedLength,
      GraphTestImpl graph) {
    List<GraphWeightedEdge<GraphNodeTestImpl>> edges = actualPath.edges();
    if (edges.isEmpty()) {
      assertThat(actualPath.length()).isZero();
      return;
    }
    assertThat(actualPath.start()).isEqualTo(graph.getNodes().get(expectedStart));
    assertThat(actualPath.end()).isEqualTo(graph.getNodes().get(expectedEnd));
    assertThat(actualPath.length()).isEqualTo(expectedLength);

    double sumLength = edges.getFirst().weight;
    GraphNodeTestImpl previousLast = edges.getFirst().getLastNode();
    for (int i = 1; i < edges.size(); i++) {
      GraphNodeTestImpl currentFirst = edges.get(i).getFirstNode();
      assertThat(previousLast).isEqualTo(currentFirst);
      sumLength += edges.get(i).weight;
      previousLast = edges.get(i).getLastNode();
    }
    assertThat(sumLength).isEqualTo(expectedLength);
  }
}
