package graph;

import java.util.Objects;

public class GraphEdge<T extends GraphNode> {
  protected T firstNode;
  protected T lastNode;

  public GraphEdge(T firstNode, T lastNode) {
    Objects.requireNonNull(firstNode);
    Objects.requireNonNull(lastNode);
    this.firstNode = firstNode;
    this.lastNode = lastNode;
  }

  protected GraphEdge() {}

  public T getFirstNode() {
    return firstNode;
  }

  public T getLastNode() {
    return lastNode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GraphEdge<?> that = (GraphEdge<?>) o;
    return Objects.equals(firstNode, that.firstNode) && Objects.equals(lastNode, that.lastNode);
  }

  @Override
  public int hashCode() {
    int hashFirst = 0;
    int hashSecond = 0;
    if (firstNode != null) {
      hashFirst = firstNode.hashCode();
    }
    if (lastNode != null) {
      hashSecond = lastNode.hashCode();
    }
    int maxHash = Math.max(hashFirst, hashSecond);
    int minHash = Math.min(hashFirst, hashSecond);
    return minHash * 31 + maxHash;
  }
}
