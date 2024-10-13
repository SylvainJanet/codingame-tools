package graph;

import java.util.List;
import java.util.Set;

public class GraphTestImpl
    implements Graph<GraphNodeTestImpl, GraphWeightedEdge<GraphNodeTestImpl>> {

  private final List<GraphNodeTestImpl> nodes;
  private final Set<GraphWeightedEdge<GraphNodeTestImpl>> edges;

  public GraphTestImpl(
      List<GraphNodeTestImpl> nodes, Set<GraphWeightedEdge<GraphNodeTestImpl>> edges) {
    this.nodes = nodes;
    this.edges = edges;
    this.checkGraph();
  }

  @Override
  public List<GraphNodeTestImpl> getNodes() {
    return nodes;
  }

  @Override
  public Set<GraphWeightedEdge<GraphNodeTestImpl>> getEdges() {
    return edges;
  }
}
