package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphPath<T extends GraphNode> {

  List<GraphWeightedEdge<T>> edges;
  double length;

  public GraphPath() {
    edges = new ArrayList<>();
    length = 0;
  }

  public static <T extends GraphNode> GraphPath<T> fromNodes(List<T> nodes, Graph<T, ?> graph) {
    GraphPath<T> res = new GraphPath<>();
    if (nodes.size() >= 2) {
      T prev = nodes.getFirst();
      for (int i = 1; i < nodes.size(); i++) {
        T next = nodes.get(i);
        GraphWeightedEdge<T> edge = graph.getEdge(prev, next);
        res.append(edge);
        prev = next;
      }
    }
    return res;
  }

  public List<GraphWeightedEdge<T>> edges() {
    return List.copyOf(edges);
  }

  public T start() {
    if (edges.isEmpty()) {
      return null;
    }
    return edges.getFirst().firstNode;
  }

  public T end() {
    if (edges.isEmpty()) {
      return null;
    }
    return edges.getLast().lastNode;
  }

  public double length() {
    return length;
  }

  public void append(GraphWeightedEdge<T> edge) {
    Objects.requireNonNull(edge);
    if (edges.isEmpty()) {
      appendValidEdge(edge);
      return;
    }
    GraphEdge<T> lastEdge = edges.getLast();
    if (!lastEdge.lastNode.equals(edge.firstNode)) {
      throw new MalformedPathException("New edge is not connected to last edge of path");
    }
    appendValidEdge(edge);
  }

  public void prepend(GraphWeightedEdge<T> edge) {
    Objects.requireNonNull(edge);
    if (edges.isEmpty()) {
      prependValidEdge(edge);
      return;
    }
    GraphEdge<T> firstEdge = edges.getFirst();
    if (!firstEdge.firstNode.equals(edge.lastNode)) {
      throw new MalformedPathException("New edge is not connected to first edge of path");
    }
    prependValidEdge(edge);
  }

  private void appendValidEdge(GraphWeightedEdge<T> edge) {
    edges.add(edge);
    length += edge.weight;
  }

  private void prependValidEdge(GraphWeightedEdge<T> edge) {
    edges.addFirst(edge);
    length += edge.weight;
  }
}
