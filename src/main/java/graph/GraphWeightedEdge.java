package graph;

public class GraphWeightedEdge<T extends GraphNode> extends GraphEdge<T> {
  public double weight;

  public GraphWeightedEdge(T firstNode, T lastNode, double weight) {
    super(firstNode, lastNode);
    this.weight = weight;
  }

  protected GraphWeightedEdge() {
    super();
  }
}
