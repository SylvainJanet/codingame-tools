package graph.minimumpaths.Dijkstra;

import graph.GraphEdge;
import graph.GraphNodeBenchmarkImpl;
import java.util.Map;
import org.openjdk.jmh.annotations.Benchmark;

public class DijkstraBenchmarkMinDistances extends DijkstraBenchmarkConfig {

  @Benchmark
  public Map<GraphEdge<GraphNodeBenchmarkImpl>, Double> testAlgorithm_minimumDistances() {
    return algorithm.minimumDistances(graph);
  }
}
