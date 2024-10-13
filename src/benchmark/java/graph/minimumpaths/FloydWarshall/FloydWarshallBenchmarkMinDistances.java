package graph.minimumpaths.FloydWarshall;

import graph.GraphEdge;
import graph.GraphNodeBenchmarkImpl;
import java.util.Map;
import org.openjdk.jmh.annotations.Benchmark;

public class FloydWarshallBenchmarkMinDistances extends FloydWarshallBenchmarkConfig {

  @Benchmark
  public Map<GraphEdge<GraphNodeBenchmarkImpl>, Double> testAlgorithm_minimumDistances() {
    return algorithm.minimumDistances(graph);
  }
}
