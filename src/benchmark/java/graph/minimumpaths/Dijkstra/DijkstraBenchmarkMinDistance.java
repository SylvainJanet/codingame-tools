package graph.minimumpaths.Dijkstra;

import org.openjdk.jmh.annotations.Benchmark;

public class DijkstraBenchmarkMinDistance extends DijkstraBenchmarkConfig {

  @Benchmark
  public Double testAlgorithm_minimumDistance() {
    return algorithm.minimumDistance(graph, randomNode1, randomNode2);
  }
}
