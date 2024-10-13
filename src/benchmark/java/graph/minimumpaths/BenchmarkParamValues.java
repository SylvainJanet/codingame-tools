package graph.minimumpaths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import graph.GraphGenerator;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record BenchmarkParamValues(
    String name,
    int[] numberOfNodes,
    double[] edgesDensity,
    boolean[] oriented,
    boolean[] uniformWeights,
    boolean[] negativeWeights,
    int numberOfGraphs)
    implements Serializable {

  public static BenchmarkParamValues fullBenchmarkParams() {
    return new BenchmarkParamValues(
        "FullBenchmark",
        new int[] {25, 50, 75, 100, 125},
        new double[] {0.1, 0.25, 0.5, 0.75, 0.9},
        new boolean[] {true, false},
        new boolean[] {false, true},
        new boolean[] {false, true},
        200);
  }

  public static BenchmarkParamValues fullBenchmarkNoNegativeParams() {
    return new BenchmarkParamValues(
        "FullBenchmarkNoNegativeWeight",
        new int[] {25, 50, 75, 100, 125},
        new double[] {0.1, 0.25, 0.5, 0.75, 0.9},
        new boolean[] {true, false},
        new boolean[] {false, true},
        new boolean[] {false},
        200);
  }

  public static BenchmarkParamValues smallBenchmarkParams() {
    return new BenchmarkParamValues(
        "SmallBenchmark",
        new int[] {50, 75, 100},
        new double[] {0.25, 0.5, 0.75},
        new boolean[] {true},
        new boolean[] {false},
        new boolean[] {false},
        200);
  }

  public static BenchmarkParamValues tinyBenchmarkParams() {
    return new BenchmarkParamValues(
        "TinyBenchmark",
        new int[] {5},
        new double[] {0.5},
        new boolean[] {true},
        new boolean[] {false},
        new boolean[] {false},
        2);
  }

  public static BenchmarkParamValues importedFromFile() {
    return new BenchmarkParamValues(
        "ImportedFromFile",
        new int[] {4, 11},
        new double[] {0.5},
        new boolean[] {true},
        new boolean[] {false},
        new boolean[] {false},
        2);
  }

  public Set<GraphGenerator.State> states() {
    Set<GraphGenerator.State> states = new HashSet<>();
    for (int numberOfNode : this.numberOfNodes()) {
      for (double edgeDensity : this.edgesDensity()) {
        for (boolean oriented : this.oriented()) {
          for (boolean uniformWeights : this.uniformWeights()) {
            for (boolean negativeWeights : this.negativeWeights()) {
              states.add(
                  new GraphGenerator.State(
                      numberOfNode, edgeDensity, oriented, uniformWeights, negativeWeights));
            }
          }
        }
      }
    }
    return states;
  }

  public void saveToFile(String cacheConfigFilePath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(new File(cacheConfigFilePath), this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BenchmarkParamValues that = (BenchmarkParamValues) o;
    return Objects.deepEquals(name, that.name)
        && numberOfGraphs == that.numberOfGraphs
        && Objects.deepEquals(oriented, that.oriented)
        && Objects.deepEquals(numberOfNodes, that.numberOfNodes)
        && Objects.deepEquals(edgesDensity, that.edgesDensity)
        && Objects.deepEquals(uniformWeights, that.uniformWeights)
        && Objects.deepEquals(negativeWeights, that.negativeWeights);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        Arrays.hashCode(numberOfNodes),
        Arrays.hashCode(edgesDensity),
        Arrays.hashCode(oriented),
        Arrays.hashCode(uniformWeights),
        Arrays.hashCode(negativeWeights),
        numberOfGraphs);
  }

  @Override
  public String toString() {
    return "BenchmarkParamValues{"
        + "name='"
        + name
        + '\''
        + ", numberOfNodes="
        + Arrays.toString(numberOfNodes)
        + ", edgesDensity="
        + Arrays.toString(edgesDensity)
        + ", oriented="
        + Arrays.toString(oriented)
        + ", uniformWeights="
        + Arrays.toString(uniformWeights)
        + ", negativeWeights="
        + Arrays.toString(negativeWeights)
        + ", numberOfGraphs="
        + numberOfGraphs
        + '}';
  }
}
