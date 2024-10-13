package graph.minimumpaths.FloydWarshall;

import org.openjdk.jmh.annotations.Benchmark;

public class FloydWarshallBenchmarkMinDistance extends FloydWarshallBenchmarkConfig {

  @Benchmark
  public Double testAlgorithm_minimumDistance() {
    return algorithm.minimumDistance(graph, randomNode1, randomNode2);
  }
}
