package graph.minimumpaths.Dijkstra;

import graph.GraphNodeBenchmarkImpl;
import graph.GraphPath;
import org.openjdk.jmh.annotations.Benchmark;

public class DijkstraBenchmarkMinPath extends DijkstraBenchmarkConfig {

  @Benchmark
  public GraphPath<GraphNodeBenchmarkImpl> testAlgorithm_minimumPath() {
    return algorithm.minimumPath(graph, randomNode1, randomNode2);
  }
}
