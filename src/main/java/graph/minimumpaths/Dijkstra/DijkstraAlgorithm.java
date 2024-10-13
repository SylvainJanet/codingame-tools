package graph.minimumpaths.Dijkstra;

import graph.Graph;
import graph.GraphEdge;
import graph.GraphNode;
import graph.GraphPath;
import graph.GraphWeightedEdge;
import graph.minimumpaths.MinimumPathsAlgorithm;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/*-
 * https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm Notably :
 * - O(n²) complexity
 * - no negative weight
 * -
 */
public class DijkstraAlgorithm implements MinimumPathsAlgorithm {

  private record AlgorithmResult<T extends GraphNode>(Map<T, Double> dist, Map<T, T> prev) {}

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>> Double minimumDistance(
      Graph<T, V> graph, T startNode, T endNode) {
    AlgorithmResult<T> algorithmResult = executeDijsktra(graph, startNode, endNode);
    return algorithmResult.dist.get(endNode);
  }

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>> GraphPath<T> minimumPath(
      Graph<T, V> graph, T startNode, T endNode) {
    AlgorithmResult<T> algorithmResult = executeDijsktra(graph, startNode, endNode);
    return getShortestPath(graph, startNode, endNode, algorithmResult);
  }

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>>
      Map<GraphEdge<T>, Double> minimumDistances(Graph<T, V> graph) {
    Map<GraphEdge<T>, Double> result = new HashMap<>();
    List<T> nodes = graph.getNodes();
    nodes.forEach(
        startNode -> {
          AlgorithmResult<T> algorithmResult = executeDijsktra(graph, startNode);
          Map<T, Double> dist = algorithmResult.dist;
          dist.forEach(
              (endNode, distance) -> {
                GraphEdge<T> graphEdge = new GraphEdge<>(startNode, endNode);
                result.put(graphEdge, distance);
              });
        });
    return result;
  }

  @Override
  public <T extends GraphNode, V extends GraphWeightedEdge<T>>
      Map<GraphEdge<T>, GraphPath<T>> minimumPaths(Graph<T, V> graph) {
    Map<GraphEdge<T>, GraphPath<T>> result = new HashMap<>();
    List<T> nodes = graph.getNodes();
    nodes.forEach(
        startNode -> {
          AlgorithmResult<T> algorithmResult = executeDijsktra(graph, startNode);
          nodes.forEach(
              endNode -> {
                GraphEdge<T> graphEdge = new GraphEdge<>(startNode, endNode);
                GraphPath<T> path = getShortestPath(graph, startNode, endNode, algorithmResult);
                result.put(graphEdge, path);
              });
        });
    return result;
  }

  private static <T extends GraphNode, V extends GraphWeightedEdge<T>>
      AlgorithmResult<T> executeDijsktra(Graph<T, V> graph, T startNode) {
    return executeDijsktra(graph, startNode, null);
  }

  private static <T extends GraphNode, V extends GraphWeightedEdge<T>>
      AlgorithmResult<T> executeDijsktra(Graph<T, V> graph, T startNode, T endNode) {
    Map<T, Double> dist = new HashMap<>();
    Map<T, T> prev = new HashMap<>();
    Set<T> Q = new HashSet<>(graph.getNodes());

    graph
        .getNodes()
        .forEach(
            n -> {
              dist.put(n, Double.POSITIVE_INFINITY);
              prev.put(n, null);
            });
    dist.put(startNode, 0.0);

    while (!Q.isEmpty()) {
      T u = Q.stream().min(Comparator.comparingDouble(dist::get)).orElse(null);
      Q.remove(u);

      Stream<T> neighboursInSet = graph.nodesAccessibleFrom(u).stream().filter(Q::contains);

      neighboursInSet.forEach(
          v -> {
            double alt = dist.get(u) + graph.getEdge(u, v).weight;
            if (alt < dist.get(v)) {
              dist.put(v, alt);
              prev.put(v, u);
            }
          });

      if (Objects.equals(u, endNode)) {
        return new AlgorithmResult<>(dist, prev);
      }
    }
    return new AlgorithmResult<>(dist, prev);
  }

  private <T extends GraphNode, V extends GraphWeightedEdge<T>> GraphPath<T> getShortestPath(
      Graph<T, V> graph, T startNode, T endNode, AlgorithmResult<T> algorithmResult) {
    Map<T, T> prev = algorithmResult.prev;
    List<T> nodes = new ArrayList<>();
    T u = endNode;
    if (prev.get(u) != null || u.equals(startNode)) {
      while (u != null) {
        nodes.addFirst(u);
        u = prev.get(u);
      }
    }
    return GraphPath.fromNodes(nodes, graph);
  }
}
