package graph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Graph<T extends GraphNode, V extends GraphWeightedEdge<T>> {
  List<T> getNodes();

  Set<V> getEdges();

  default V getEdge(T firstNode, T lastNode) {
    return getEdges().stream()
        .filter(edge -> edge.firstNode.equals(firstNode) && edge.lastNode.equals(lastNode))
        .findFirst()
        .orElse(null);
  }

  default Set<T> nodesAccessibleFrom(T node) {
    return this.getEdges().stream()
        .filter(n -> n.getFirstNode().equals(node))
        .map(GraphEdge::getLastNode)
        .collect(Collectors.toSet());
  }

  default void checkGraph() {
    List<T> nodes = getNodes();
    Set<V> edges = getEdges();
    for (V edge : edges) {
      T first = edge.firstNode;
      T lastNode = edge.lastNode;
      if (!nodes.contains(first) || !nodes.contains(lastNode)) {
        throw new MalformedGraphException(
            "Graph should contain every nodes contained in its edges.");
      }
    }

    for (V edge : edges) {
      T firstNode = edge.firstNode;
      T lastNode = edge.lastNode;
      long count =
          edges.stream().filter(v -> v.firstNode == firstNode && v.lastNode == lastNode).count();
      if (count > 1) {
        throw new MalformedGraphException("Graph should contain only one edge for each start/end");
      }
    }

    for (int i = 0; i < nodes.size(); i++) {
      T node = nodes.get(i);
      if (node.getGraphIndex() != i) {
        throw new MalformedGraphException("Graph node index should be set properly");
      }
    }
  }
}
