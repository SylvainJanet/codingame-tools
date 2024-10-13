package graph.minimumpaths;

import static org.assertj.core.api.Assertions.assertThat;

import graph.GraphEdge;
import graph.GraphNodeTestImpl;
import graph.GraphPath;
import graph.GraphTestImpl;
import java.util.Map;
import org.junit.jupiter.api.Test;

public abstract class MinimumPathsAlgorithmTest {

  protected static MinimumPathsAlgorithm algorithm;

  @Test
  void minimumDistance_Simple() {
    GraphTestImpl graphTest = GraphTestExemples.getSimpleGraph();
    double[][] expected = GraphTestExemples.getSimpleGraphExpectedDistances();

    testMinimumDistance(graphTest, expected);
  }

  @Test
  void minimumPath_Simple() {
    GraphTestImpl graphTest = GraphTestExemples.getSimpleGraph();
    double[][] expected = GraphTestExemples.getSimpleGraphExpectedDistances();

    testMinimumPath(graphTest, expected);
  }

  @Test
  void minimumDistances_Simple() {
    GraphTestImpl graphTest = GraphTestExemples.getSimpleGraph();
    double[][] expected = GraphTestExemples.getSimpleGraphExpectedDistances();

    testMinimumDistances(graphTest, expected);
  }

  @Test
  void minimumPaths_Simple() {
    GraphTestImpl graphTest = GraphTestExemples.getSimpleGraph();
    double[][] expected = GraphTestExemples.getSimpleGraphExpectedDistances();

    testMinimumPaths(graphTest, expected);
  }

  @Test
  void minimumDistance_Complex() {
    GraphTestImpl graphTest = GraphTestExemples.getComplexGraph();
    double[][] expected = GraphTestExemples.getComplexGraphExpectedDistances();

    testMinimumDistance(graphTest, expected);
  }

  @Test
  void minimumPath_Complex() {
    GraphTestImpl graphTest = GraphTestExemples.getComplexGraph();
    double[][] expected = GraphTestExemples.getComplexGraphExpectedDistances();

    testMinimumPath(graphTest, expected);
  }

  @Test
  void minimumDistances_Complex() {
    GraphTestImpl graphTest = GraphTestExemples.getComplexGraph();
    double[][] expected = GraphTestExemples.getComplexGraphExpectedDistances();

    testMinimumDistances(graphTest, expected);
  }

  @Test
  void minimumPaths_Complex() {
    GraphTestImpl graphTest = GraphTestExemples.getComplexGraph();
    double[][] expected = GraphTestExemples.getComplexGraphExpectedDistances();

    testMinimumPaths(graphTest, expected);
  }

  private static void testMinimumPaths(GraphTestImpl graphTest, double[][] expected) {
    Map<GraphEdge<GraphNodeTestImpl>, GraphPath<GraphNodeTestImpl>> result =
        algorithm.minimumPaths(graphTest);

    int numberOfNodes = graphTest.getNodes().size();
    for (int i = 0; i < numberOfNodes; i++) {
      for (int j = 0; j < numberOfNodes; j++) {
        GraphNodeTestImpl firstNode = graphTest.getNodes().get(i);
        GraphNodeTestImpl lastNode = graphTest.getNodes().get(j);
        GraphEdge<GraphNodeTestImpl> pair = new GraphEdge<>(firstNode, lastNode);

        GraphPath<GraphNodeTestImpl> actualPath = result.get(pair);
        if (i == j) {
          assertThat(actualPath.start()).isNull();
          assertThat(actualPath.end()).isNull();
          assertThat(actualPath.length()).isZero();
        } else {
          GraphPathChecker.assertPathValidity(actualPath, i, j, expected[i][j], graphTest);
        }
      }
    }
  }

  private static void testMinimumDistances(GraphTestImpl graphTest, double[][] expected) {
    Map<GraphEdge<GraphNodeTestImpl>, Double> result = algorithm.minimumDistances(graphTest);

    int numberOfNodes = graphTest.getNodes().size();
    for (int i = 0; i < numberOfNodes; i++) {
      for (int j = 0; j < numberOfNodes; j++) {
        GraphNodeTestImpl firstNode = graphTest.getNodes().get(i);
        GraphNodeTestImpl lastNode = graphTest.getNodes().get(j);
        GraphEdge<GraphNodeTestImpl> pair = new GraphEdge<>(firstNode, lastNode);

        Double actualDistance = result.get(pair);
        double expectedDistance = expected[i][j];
        assertThat(actualDistance).isEqualTo(expectedDistance);
      }
    }
  }

  private static void testMinimumPath(GraphTestImpl graphTest, double[][] expected) {
    int numberOfNodes = graphTest.getNodes().size();
    for (int i = 0; i < numberOfNodes; i++) {
      for (int j = 0; j < numberOfNodes; j++) {
        GraphNodeTestImpl firstNode = graphTest.getNodes().get(i);
        GraphNodeTestImpl lastNode = graphTest.getNodes().get(j);

        GraphPath<GraphNodeTestImpl> actualPath =
            algorithm.minimumPath(graphTest, firstNode, lastNode);
        if (i == j) {
          assertThat(actualPath.start()).isNull();
          assertThat(actualPath.end()).isNull();
          assertThat(actualPath.length()).isZero();
        } else {
          GraphPathChecker.assertPathValidity(actualPath, i, j, expected[i][j], graphTest);
        }
      }
    }
  }

  private static void testMinimumDistance(GraphTestImpl graphTest, double[][] expected) {
    int numberOfNodes = graphTest.getNodes().size();
    for (int i = 0; i < numberOfNodes; i++) {
      for (int j = 0; j < numberOfNodes; j++) {
        GraphNodeTestImpl firstNode = graphTest.getNodes().get(i);
        GraphNodeTestImpl lastNode = graphTest.getNodes().get(j);

        Double actualDistance = algorithm.minimumDistance(graphTest, firstNode, lastNode);
        double expectedDistance = expected[i][j];
        assertThat(actualDistance).isEqualTo(expectedDistance);
      }
    }
  }
}
