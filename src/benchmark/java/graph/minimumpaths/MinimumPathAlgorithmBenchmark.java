package graph.minimumpaths;

import graph.GraphGenerator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class MinimumPathAlgorithmBenchmark {

  private static final String BENCHMARKING_RESULTS_FOLDER_PATH =
      "src/benchmark/resources/results/generated/minimumpaths/";

  private static final String BENCHMARKING_GRAPHS_TO_IMPORT =
      "src/benchmark/resources/graphs/created/";

  private static final Logger logger =
      Logger.getLogger(MinimumPathAlgorithmBenchmark.class.getName());

  public static void main(String[] args) throws RunnerException, IOException {

    // two options :
    // 1. generate random graphs and export them in a file. If already done for the configuration,
    // they will not be generated but loaded from the file. Either way, the graphs will then be put
    // in cache
    BenchmarkParamValues paramsValues = MinimumPathAlgorithmBenchmarkConfig.paramsValues;
    GraphGenerator.generateCachedGraphsWithNoNegativeCycle(paramsValues);

    // 2. load graphs from a JSON file. The configuration should match the benchmarking
    // configuration. However, it's up to you to ensure that the graphs math that configuration (or
    // not, if you don't care)
    // set the appropriate value in the benchmark config too, in
    // MinimumPathAlgorithmBenchmarkConfig.
    // BenchmarkParamValues paramsValues =
    //    GraphGenerator.loadGraphsFromFileToCache(BENCHMARKING_GRAPHS_TO_IMPORT);

    Map<String, Map<String, String>> algorithmsBenchmarks = getBenchmarkings();

    int benchMarkNbr = 0;
    int totalBenchMarkNbr =
        algorithmsBenchmarks.values().stream().map(Map::size).reduce(0, Integer::sum);
    for (Map.Entry<String, Map<String, String>> algorithm : algorithmsBenchmarks.entrySet()) {
      String algorithmName = algorithm.getKey();
      Map<String, String> includes = algorithm.getValue();
      for (Map.Entry<String, String> entry : includes.entrySet()) {
        String className = entry.getKey();
        String includeFileName = entry.getValue();
        new File(benchmarkResultDir(paramsValues)).mkdir();
        benchMarkNbr++;
        logger.info("Starting benchmark " + benchMarkNbr + "/" + totalBenchMarkNbr);
        Options opt =
            new OptionsBuilder()
                .shouldDoGC(false)
                .include(className)
                .resultFormat(ResultFormatType.JSON)
                .result(
                    benchmarkResultDir(paramsValues)
                        + fileName(algorithmName, includeFileName)
                        + ".json")
                .build();
        new Runner(opt).run();
      }
    }
  }

  private static String benchmarkResultDir(BenchmarkParamValues paramsValues) {
    return BENCHMARKING_RESULTS_FOLDER_PATH + paramsValues.name() + "/";
  }

  private static String fileName(String algorithmName, String includeFileName) {
    return algorithmName + "_" + includeFileName;
  }

  private static Map<String, Map<String, String>> getBenchmarkings() {
    Map<String, Map<String, String>> algorithmsBenchmarks = new HashMap<>();
    Map<String, String> includesMinPaths;

    includesMinPaths =
        Map.of(
            "MinPath" + "\\.",
            "minPath",
            "MinPaths" + "\\.",
            "minPaths",
            "MinDistance" + "\\.",
            "minDistance",
            "MinDistances" + "\\.",
            "minDistances");
    algorithmsBenchmarks.put("Comparison", includesMinPaths);

    return algorithmsBenchmarks;
  }
}
