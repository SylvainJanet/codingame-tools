package graph.minimumpaths.FloydWarshall;

import graph.minimumpaths.MinimumPathAlgorithmBenchmarkConfig;
import graph.minimumpaths.MinimumPathsAlgorithm;

public class FloydWarshallBenchmarkConfig extends MinimumPathAlgorithmBenchmarkConfig {

  public MinimumPathsAlgorithm algorithm = new FloydWarshallAlgorithm();
}
