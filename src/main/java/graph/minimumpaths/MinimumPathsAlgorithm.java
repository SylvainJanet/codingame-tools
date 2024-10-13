package graph.minimumpaths;

import graph.Graph;
import graph.GraphEdge;
import graph.GraphNode;
import graph.GraphPath;
import graph.GraphWeightedEdge;
import java.util.Map;

public interface MinimumPathsAlgorithm {

  <T extends GraphNode, V extends GraphWeightedEdge<T>> Double minimumDistance(
      Graph<T, V> graph, T startNode, T endNode);

  <T extends GraphNode, V extends GraphWeightedEdge<T>> GraphPath<T> minimumPath(
      Graph<T, V> graph, T startNode, T endNode);

  <T extends GraphNode, V extends GraphWeightedEdge<T>> Map<GraphEdge<T>, Double> minimumDistances(
      Graph<T, V> graph);

  <T extends GraphNode, V extends GraphWeightedEdge<T>>
      Map<GraphEdge<T>, GraphPath<T>> minimumPaths(Graph<T, V> graph);
}
