package graph.minimumpaths.FloydWarshall;

import graph.Graph;
import graph.GraphEdge;
import graph.GraphNode;
import graph.GraphPath;
import graph.GraphWeightedEdge;
import graph.MalformedGraphException;
import graph.minimumpaths.MinimumPathsAlgorithm;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*-
 * https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm Notably :
 * - O(n³) complexity
 * - no negative cycle
 * - When a graph is dense (i.e.,|E|~|V|²), the Floyd-Warshall algorithm tends to
 * perform better in practice.
 * - When the graph is sparse (i.e., |E| << |V|²), Dijkstra tends to dominate.
 */
public class FloydWarshallAlgorithm implements MinimumPathsAlgorithm {

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>> Double minimumDistance(
      Graph<T, V> graph, T startNode, T endNode) {
    if (!graph.getNodes().contains(startNode) || !graph.getNodes().contains(endNode)) {
      throw new IllegalArgumentException("Graph doesn't contain the nodes.");
    }

    Map<GraphEdge<T>, Double> minimumDistances = minimumDistances(graph);
    GraphEdge<T> edge = new GraphEdge<>(startNode, endNode);

    return minimumDistances.get(edge);
  }

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>> GraphPath<T> minimumPath(
      Graph<T, V> graph, T startNode, T endNode) {
    if (!graph.getNodes().contains(startNode) || !graph.getNodes().contains(endNode)) {
      throw new IllegalArgumentException("Graph doesn't contain the nodes.");
    }

    Map<GraphEdge<T>, GraphPath<T>> minimumDistances = minimumPaths(graph);
    GraphEdge<T> edge = new GraphEdge<>(startNode, endNode);

    return minimumDistances.get(edge);
  }

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>>
      Map<GraphEdge<T>, Double> minimumDistances(Graph<T, V> graph) {
    Objects.requireNonNull(graph);
    int edgesSize = graph.getNodes().size();
    Double[][] dist = new Double[edgesSize][edgesSize];
    for (Double[] row : dist) {
      Arrays.fill(row, Double.POSITIVE_INFINITY);
    }
    for (GraphWeightedEdge<T> edge : graph.getEdges()) {
      dist[edge.getFirstNode().getGraphIndex()][edge.getLastNode().getGraphIndex()] = edge.weight;
    }
    for (int i = 0; i < edgesSize; i++) {
      dist[i][i] = 0.0;
    }
    for (int k = 0; k < edgesSize; k++) {
      for (int i = 0; i < edgesSize; i++) {
        for (int j = 0; j < edgesSize; j++) {
          if (dist[i][j] > dist[i][k] + dist[k][j]) {
            dist[i][j] = dist[i][k] + dist[k][j];
          }
        }
      }
      checkNegativePath(dist);
    }
    HashMap<GraphEdge<T>, Double> result = new HashMap<>();
    for (int i = 0; i < edgesSize; i++) {
      for (int j = 0; j < edgesSize; j++) {
        GraphEdge<T> edge = new GraphEdge<>(graph.getNodes().get(i), graph.getNodes().get(j));
        result.put(edge, dist[i][j]);
      }
    }
    return result;
  }

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>>
      Map<GraphEdge<T>, GraphPath<T>> minimumPaths(Graph<T, V> graph) {
    Objects.requireNonNull(graph);
    int verticesSize = graph.getNodes().size();
    Double[][] dist = new Double[verticesSize][verticesSize];
    for (Double[] row : dist) {
      Arrays.fill(row, Double.POSITIVE_INFINITY);
    }
    Integer[][] prev = new Integer[verticesSize][verticesSize];
    for (Integer[] row : prev) {
      Arrays.fill(row, null);
    }
    for (GraphWeightedEdge<T> edge : graph.getEdges()) {
      dist[edge.getFirstNode().getGraphIndex()][edge.getLastNode().getGraphIndex()] = edge.weight;
      prev[edge.getFirstNode().getGraphIndex()][edge.getLastNode().getGraphIndex()] =
          edge.getFirstNode().getGraphIndex();
    }
    for (int i = 0; i < verticesSize; i++) {
      dist[i][i] = 0.0;
      prev[i][i] = i;
    }
    for (int k = 0; k < verticesSize; k++) {
      for (int i = 0; i < verticesSize; i++) {
        for (int j = 0; j < verticesSize; j++) {
          if (dist[i][j] > dist[i][k] + dist[k][j]) {
            dist[i][j] = dist[i][k] + dist[k][j];
            prev[i][j] = prev[k][j];
          }
        }
      }
      checkNegativePath(dist);
    }
    HashMap<GraphEdge<T>, GraphPath<T>> result = new HashMap<>();
    for (int i = 0; i < verticesSize; i++) {
      for (int j = 0; j < verticesSize; j++) {
        GraphEdge<T> edge = new GraphEdge<>(graph.getNodes().get(i), graph.getNodes().get(j));
        GraphPath<T> minimalPath = computeMinimalPath(graph, i, j, prev);
        result.put(edge, minimalPath);
      }
    }
    return result;
  }

  private <T extends GraphNode, V extends GraphWeightedEdge<T>> GraphPath<T> computeMinimalPath(
      Graph<T, V> graph, int i, int j, Integer[][] prev) {
    GraphPath<T> result = new GraphPath<>();
    if (prev[i][j] == null) {
      return result;
    }
    T firstNode;
    T lastNode;
    while (i != j) {
      lastNode = graph.getNodes().get(j);
      j = prev[i][j];
      firstNode = graph.getNodes().get(j);
      V edge = graph.getEdge(firstNode, lastNode);
      result.prepend(edge);
    }
    return result;
  }

  private void checkNegativePath(Double[][] dist) {
    for (int i = 0; i < dist.length; i++) {
      if (dist[i][i] < 0) {
        throw new MalformedGraphException(
            "No negative path should exist for this algorithm to work");
      }
    }
  }
}
