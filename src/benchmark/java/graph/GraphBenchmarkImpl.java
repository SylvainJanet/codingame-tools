package graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphBenchmarkImpl
    implements Graph<
            GraphNodeBenchmarkImpl, GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>>,
        Serializable {

  private final List<GraphNodeBenchmarkImpl> nodes;
  private final Set<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> edges;

  public GraphBenchmarkImpl(
      List<GraphNodeBenchmarkImpl> nodes,
      Set<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> edges) {
    this.nodes = nodes;
    this.edges = edges;
    this.checkGraph();
  }

  public GraphBenchmarkImpl() {
    nodes = new ArrayList<>();
    edges = new HashSet<>();
  }

  @Override
  public List<GraphNodeBenchmarkImpl> getNodes() {
    return nodes;
  }

  @Override
  public Set<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> getEdges() {
    return edges;
  }
}
