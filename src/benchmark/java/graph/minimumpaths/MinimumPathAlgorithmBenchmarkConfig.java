package graph.minimumpaths;

import graph.GraphBenchmarkImpl;
import graph.GraphGenerator;
import graph.GraphNodeBenchmarkImpl;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 8, time = 4, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 4)
@State(Scope.Thread)
public abstract class MinimumPathAlgorithmBenchmarkConfig {

  // the results order will be sorted by the param names, alphabetically
  // this is not elegant but appears to be unavoidable to control the order
  // @Param({"25", "50", "75", "100", "125"})  // fullBenchmarkParams
  @Param({"25", "50", "75", "100", "125"}) // fullBenchmarkNoNegativeParams
  // @Param({"50", "75", "100"}) // smallBenchmarkParams
  // @Param({"5"}) // tinyBenchmarkParams
  // @Param({"4", "11"}) // importedFromFile
  public int _0_numberOfNodes;

  // @Param({"0.1", "0.25", "0.5", "0.75", "0.9"})  // fullBenchmarkParams
  @Param({"0.1", "0.25", "0.5", "0.75", "0.9"}) // fullBenchmarkNoNegativeParams
  // @Param({"0.25", "0.5", "0.75"}) // smallBenchmarkParams
  // @Param({"0.5"}) // tinyBenchmarkParams
  // @Param({"0.5"}) // importedFromFile
  public double _1_edgesDensity;

  // @Param({"true", "false"})  // fullBenchmarkParams
  @Param({"true", "false"}) // fullBenchmarkNoNegativeParams
  // @Param({"true"}) // smallBenchmarkParams
  // @Param({"true"}) // tinyBenchmarkParams
  // @Param({"true"}) // importedFromFile
  public boolean _2_uniformWeights;

  // @Param({"false", "true"})  // fullBenchmarkParams
  @Param({"false", "true"}) // fullBenchmarkNoNegativeParams
  // @Param({"false"}) // smallBenchmarkParams
  // @Param({"false"}) // tinyBenchmarkParams
  // @Param({"false"}) // importedFromFile
  public boolean _3_oriented;

  // @Param({"false", "true"})  // fullBenchmarkParams
  @Param({"false"}) // fullBenchmarkNoNegativeParams
  // @Param({"false"}) // smallBenchmarkParams
  // @Param({"false"}) // tinyBenchmarkParams
  // @Param({"false"}) // importedFromFile
  public boolean _4_negativeWeights;

  public static BenchmarkParamValues paramsValues = BenchmarkParamValues.fullBenchmarkNoNegativeParams();
  public Random random = new Random();
  public GraphBenchmarkImpl graph;
  public GraphNodeBenchmarkImpl randomNode1;
  public GraphNodeBenchmarkImpl randomNode2;

  @Setup(Level.Trial)
  public void trialSetup() throws IOException {
    GraphGenerator.readCacheFromFile(
        paramsValues,
        _0_numberOfNodes,
        _1_edgesDensity,
        _2_uniformWeights,
        _3_oriented,
        _4_negativeWeights);
  }

  @Setup(Level.Iteration)
  public void setup() {
    List<GraphBenchmarkImpl> graphs =
        GraphGenerator.getCachedGraphWithNoNegativeCycle(
            _0_numberOfNodes, _1_edgesDensity, _2_uniformWeights, _3_oriented, _4_negativeWeights);

    int graphIndex = random.nextInt(graphs.size());
    this.graph = graphs.get(graphIndex);

    int index1 = random.nextInt(0, _0_numberOfNodes);
    int index2 = random.nextInt(0, _0_numberOfNodes);
    this.randomNode1 = this.graph.getNodes().get(index1);
    this.randomNode2 = this.graph.getNodes().get(index2);
  }
}
