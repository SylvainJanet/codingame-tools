package graph.minimumpaths.FloydWarshall;

import graph.GraphEdge;
import graph.GraphNodeBenchmarkImpl;
import graph.GraphPath;
import java.util.Map;
import org.openjdk.jmh.annotations.Benchmark;

public class FloydWarshallBenchmarkMinPaths extends FloydWarshallBenchmarkConfig {

  @Benchmark
  public Map<GraphEdge<GraphNodeBenchmarkImpl>, GraphPath<GraphNodeBenchmarkImpl>>
      testAlgorithm_minimumPaths() {
    return algorithm.minimumPaths(graph);
  }
}
