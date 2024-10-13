package graph;

import java.util.Objects;

public class GraphNodePair<T extends GraphNode> {
  public final T aNode;
  public final T anotherNode;

  public GraphNodePair(T aNode, T anotherNNode) {
    Objects.requireNonNull(aNode);
    Objects.requireNonNull(anotherNNode);
    this.aNode = aNode;
    this.anotherNode = anotherNNode;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GraphNodePair)) return false;
    GraphNodePair<T> up = (GraphNodePair<T>) o;
    return (up.aNode.equals(this.aNode) && up.anotherNode.equals(this.anotherNode))
        || (up.aNode.equals(this.anotherNode) && up.anotherNode.equals(this.aNode));
  }

  @Override
  public int hashCode() {
    int hash = 17;
    int hashFirst = aNode.hashCode();
    int hashSecond = anotherNode.hashCode();
    int maxHash = Math.max(hashFirst, hashSecond);
    int minHash = Math.min(hashFirst, hashSecond);
    hash = hash * 31 + maxHash;
    hash = hash * 31 + minHash;
    return hash;
  }
}
