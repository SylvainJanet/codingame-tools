package graph.minimumpaths.FloydWarshall;

import graph.GraphNodeBenchmarkImpl;
import graph.GraphPath;
import org.openjdk.jmh.annotations.Benchmark;

public class FloydWarshallBenchmarkMinPath extends FloydWarshallBenchmarkConfig {

  @Benchmark
  public GraphPath<GraphNodeBenchmarkImpl> testAlgorithm_minimumPath() {
    return algorithm.minimumPath(graph, randomNode1, randomNode2);
  }
}
