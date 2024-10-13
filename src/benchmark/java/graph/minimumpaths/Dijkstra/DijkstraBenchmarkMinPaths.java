package graph.minimumpaths.Dijkstra;

import graph.GraphEdge;
import graph.GraphNodeBenchmarkImpl;
import graph.GraphPath;
import java.util.Map;
import org.openjdk.jmh.annotations.Benchmark;

public class DijkstraBenchmarkMinPaths extends DijkstraBenchmarkConfig {

  @Benchmark
  public Map<GraphEdge<GraphNodeBenchmarkImpl>, GraphPath<GraphNodeBenchmarkImpl>>
      testAlgorithm_minimumPaths() {
    return algorithm.minimumPaths(graph);
  }
}
