package graph;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import graph.minimumpaths.BenchmarkParamValues;
import graph.minimumpaths.FloydWarshall.FloydWarshallAlgorithm;
import graph.minimumpaths.InvalidBenchmarkConfigurationException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphGenerator {
  private static final Logger logger = Logger.getLogger(GraphGenerator.class.getName());
  private static final String CACHE_DIR_PATH = "src/benchmark/resources/graphs/generated/";
  private static final String CACHE_FILE_NAME_PREFIX = "generatedGraphs";
  private static final String CACHE_CONFIG_NAME = "config.json";
  private static final double ADD_NEGATIVE_BATCH_SIZE_RATIO = 0.05;
  private static final Random random = new Random();

  public static Map<State, List<GraphBenchmarkImpl>> graphs = new HashMap<>();

  @JsonDeserialize(keyUsing = StateDeserializer.class)
  public record State(
      int numberOfNodes,
      double edgesDensity,
      boolean oriented,
      boolean uniformWeights,
      boolean negativeWeights)
      implements Serializable {

    public State copyWithoutNegativeWeights() {
      return new State(numberOfNodes, edgesDensity, oriented, uniformWeights, false);
    }
  }

  private static class StateDeserializer extends KeyDeserializer {

    @Override
    public State deserializeKey(String key, DeserializationContext ctxt) {
      String pattern =
          "State\\[numberOfNodes=(.*), edgesDensity=(.*), oriented=(.*), uniformWeights=(.*),"
              + " negativeWeights=(.*)]";
      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(key);
      m.matches();
      return new State(
          Integer.parseInt(m.group(1)),
          Double.parseDouble(m.group(2)),
          Boolean.parseBoolean(m.group(3)),
          Boolean.parseBoolean(m.group(4)),
          Boolean.parseBoolean(m.group(5)));
    }
  }

  public static BenchmarkParamValues loadGraphsFromFileToCache(String pathToImportDir)
      throws IOException {
    if (!graphs.isEmpty()) {
      logger.warning("Graphs have already been loaded.");
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    BenchmarkParamValues cacheConfig =
        mapper.readValue(new File(pathToImportDir + "/config.json"), BenchmarkParamValues.class);

    graphs = mapper.readValue(new File(pathToImportDir + "/graphs.json"), new TypeReference<>() {});

    writeCacheToFile(cacheConfig);
    return cacheConfig;
  }

  private static String getCachedDirectoryForParamValues(BenchmarkParamValues params) {
    return CACHE_DIR_PATH + params.name() + "/";
  }

  private static void writeCacheToFile(BenchmarkParamValues benchmarkParamValues)
      throws IOException {
    if (graphs.isEmpty()) {
      logger.warning("No graphs to write to cache.");
      return;
    }
    String cacheDirPath = getCachedDirectoryForParamValues(benchmarkParamValues);
    String cacheConfigFilePath = cacheDirPath + CACHE_CONFIG_NAME;

    File cacheDir = new File(cacheDirPath);
    if (cacheDir.exists()) {
      logger.warning("Rewriting cache for the configuration");
    } else {
      cacheDir.mkdirs();
    }

    benchmarkParamValues.saveToFile(cacheConfigFilePath);

    graphs.forEach(
        (state, graphList) -> {
          String cacheFilePath =
              cacheDirPath + CACHE_FILE_NAME_PREFIX + "_" + state.hashCode() + ".bin";
          try (ObjectOutputStream out =
              new ObjectOutputStream(
                  new BufferedOutputStream(new FileOutputStream(cacheFilePath)))) {
            out.writeUnshared(graphList);
          } catch (IOException e) {
            logger.warning("Error while trying to write cache : " + cacheFilePath);
          }
        });
  }

  public static void readCacheFromFile(
      BenchmarkParamValues benchmarkParamValues,
      int numberOfNodes,
      double edgesDensity,
      boolean uniformWeights,
      boolean oriented,
      boolean negativeWeights)
      throws IOException {
    State state = new State(numberOfNodes, edgesDensity, uniformWeights, oriented, negativeWeights);
    if (graphs.get(state) != null) {
      logger.warning("Graphs have already been loaded.");
      return;
    }
    String cacheDirPath = getCachedDirectoryForParamValues(benchmarkParamValues);
    String cacheConfigFilePath = cacheDirPath + "/" + CACHE_CONFIG_NAME;

    File cacheDir = new File(cacheDirPath);
    if (!cacheDir.exists()) {
      throw new FileNotFoundException("Cache not found : " + cacheDirPath);
    }

    ObjectMapper mapper = new ObjectMapper();
    BenchmarkParamValues cacheConfig =
        mapper.readValue(new File(cacheConfigFilePath), BenchmarkParamValues.class);

    if (!benchmarkParamValues.equals(cacheConfig)) {
      throw new InvalidBenchmarkConfigurationException("Configuration is not valid");
    }

    graphs = new HashMap<>();

    String cacheFilePath = cacheDirPath + CACHE_FILE_NAME_PREFIX + "_" + state.hashCode() + ".bin";

    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(cacheFilePath)))) {

      graphs.put(state, (List<GraphBenchmarkImpl>) in.readUnshared());

    } catch (IOException | ClassNotFoundException e) {
      {
        logger.warning("Error while trying to write cache : " + cacheFilePath);
      }
    }
  }

  public static void readCacheFromFile(BenchmarkParamValues benchmarkParamValues)
      throws IOException {
    if (!graphs.isEmpty()) {
      logger.warning("Graphs have already been loaded.");
      return;
    }
    String cacheDirPath = getCachedDirectoryForParamValues(benchmarkParamValues);
    String cacheConfigFilePath = cacheDirPath + "/" + CACHE_CONFIG_NAME;

    File cacheDir = new File(cacheDirPath);
    if (!cacheDir.exists()) {
      throw new FileNotFoundException("Cache not found : " + cacheDirPath);
    }

    ObjectMapper mapper = new ObjectMapper();
    BenchmarkParamValues cacheConfig =
        mapper.readValue(new File(cacheConfigFilePath), BenchmarkParamValues.class);


    if (!benchmarkParamValues.equals(cacheConfig)) {
      throw new InvalidBenchmarkConfigurationException("Configuration is not valid");
    }

    graphs = new HashMap<>();

    Set<State> states = cacheConfig.states();

    states.forEach(
        state -> {
          String cacheFilePath =
              cacheDirPath + CACHE_FILE_NAME_PREFIX + "_" + state.hashCode() + ".bin";

          try (ObjectInputStream in =
              new ObjectInputStream(new BufferedInputStream(new FileInputStream(cacheFilePath)))) {
            graphs.put(state, (List<GraphBenchmarkImpl>) in.readUnshared());

          } catch (IOException | ClassNotFoundException e) {
            {
              logger.warning("Error while trying to write cache : " + cacheFilePath);
            }
          }
        });
  }

  public static void generateCachedGraphsWithNoNegativeCycle(
      BenchmarkParamValues benchmarkParamValues) throws IOException {
    if (!graphs.isEmpty()) {
      logger.warning("Graphs have already been generated.");
      writeCacheToFile(benchmarkParamValues);
      return;
    }
    if (new File(getCachedDirectoryForParamValues(benchmarkParamValues)).exists()) {
      logger.info("Graphs exists for this configuration.");
      // readCacheFromFile(benchmarkParamValues);
      return;
    }
    logger.info("Generating graphs and states...");
    logger.info("Generating states...");
    Set<State> states = benchmarkParamValues.states();
    logger.info(states.size() + " states generated.");

    logger.info("Generating graphs...");
    AtomicInteger stateNbr = new AtomicInteger();
    states.forEach(
        (state) -> {
          stateNbr.getAndIncrement();
          logger.info(
              "For the state " + state.toString() + " " + stateNbr.get() + "/" + states.size());
          List<GraphBenchmarkImpl> graphsForState = new ArrayList<>();
          for (int i = 1; i <= benchmarkParamValues.numberOfGraphs(); i++) {
            logger.info(
                "Generating graph... "
                    + i
                    + "/"
                    + benchmarkParamValues.numberOfGraphs()
                    + " - state "
                    + state
                    + " "
                    + stateNbr.get()
                    + "/"
                    + states.size());
            graphsForState.add(generateWithNoNegativeCycle(state));
            logger.info("Graph generated.");
          }
          graphs.put(state, graphsForState);
        });
    writeCacheToFile(benchmarkParamValues);
  }

  public static List<GraphBenchmarkImpl> getCachedGraphWithNoNegativeCycle(
      int numberOfNodes,
      double edgesDensity,
      boolean oriented,
      boolean uniformWeights,
      boolean negativeWeights) {
    State state = new State(numberOfNodes, edgesDensity, oriented, uniformWeights, negativeWeights);
    return graphs.get(state);
  }

  private static GraphBenchmarkImpl generateWithNoNegativeCycle(State state) {
    GraphBenchmarkImpl graph = generateOnlyPositiveWeights(state);
    if (!state.negativeWeights) {
      return graph;
    }
    FloydWarshallAlgorithm algorithm = new FloydWarshallAlgorithm();
    int edgesToReplace = (int)Math.floor(ADD_NEGATIVE_BATCH_SIZE_RATIO * computeNumberOfEdgesToGenerate(state.numberOfNodes(), state.edgesDensity()));
    if (edgesToReplace == 0) {
      edgesToReplace = 1;
    }
    while (true) {
      Map<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>, Double> replacedWeights = generateNegativeEdge(graph,edgesToReplace);
      if (replacedWeights.isEmpty()) {
        return graph;
      }
      try {
        algorithm.minimumDistances(graph);
      } catch (MalformedGraphException e) {
        replacedWeights.forEach((edge, oldWeight) -> edge.weight = oldWeight);

        return graph;
      }
    }
  }

  private static Map<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>, Double> generateNegativeEdge(GraphBenchmarkImpl graph, int edgesToReplace) {
    List<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> positiveWeightsAsList = graph.getEdges().stream().filter(e -> e.weight > 0).collect(Collectors.toList());

    for (int i = positiveWeightsAsList.size() - 1; i >= positiveWeightsAsList.size() - edgesToReplace; --i)
    {
      Collections.swap(positiveWeightsAsList, i , random.nextInt(i + 1));
    }

    List<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> randomEdges = positiveWeightsAsList.subList(positiveWeightsAsList.size() - edgesToReplace, positiveWeightsAsList.size());

    Map<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>, Double> res = new HashMap<>();
    randomEdges.forEach(e -> res.put(e, e.weight));
    randomEdges.forEach(e -> e.weight = -e.weight/4);
    return res;
  }

  public static GraphBenchmarkImpl generateOnlyPositiveWeights(State state) {

    List<GraphNodeBenchmarkImpl> nodes =
        IntStream.range(0, state.numberOfNodes()).mapToObj(GraphNodeBenchmarkImpl::new).toList();

    int numberOfEdges = computeNumberOfEdgesToGenerate(state.numberOfNodes(), state.edgesDensity());

    Set<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> edges =
        generateEdges(
            nodes,
            numberOfEdges,
            state.oriented(),
            state.uniformWeights()
        );

    return new GraphBenchmarkImpl(nodes, edges);
  }

  private static Set<GraphNodePair<GraphNodeBenchmarkImpl>> generateAllPossiblePairs(
      List<GraphNodeBenchmarkImpl> nodes) {
    Set<GraphNodePair<GraphNodeBenchmarkImpl>> result = new HashSet<>();
    for (GraphNodeBenchmarkImpl aNode : nodes) {
      for (GraphNodeBenchmarkImpl anotherNode : nodes) {
        result.add(new GraphNodePair<>(aNode, anotherNode));
      }
    }
    return result;
  }

  private static Set<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> generateEdges(
      List<GraphNodeBenchmarkImpl> nodes,
      int numberOfEdges,
      boolean oriented,
      boolean uniformWeights) {

    Set<GraphNodePair<GraphNodeBenchmarkImpl>> allPossibleEdges = generateAllPossiblePairs(nodes);
    Set<GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl>> result = new HashSet<>();

    int generated = 0;

    while (generated < numberOfEdges) {
      GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl> edge =
          generateEdge(allPossibleEdges, uniformWeights);

      result.add(edge);
      generated++;
      if (generated == numberOfEdges) {
        break;
      }

      if (!oriented
          && result.stream()
              .anyMatch(
                  v ->
                      v.getFirstNode().equals(edge.getLastNode())
                          && v.getLastNode().equals(edge.getFirstNode()))) {
        allPossibleEdges.remove(new GraphNodePair<>(edge.getFirstNode(), edge.getLastNode()));
      }

      if (!edge.getFirstNode().equals(edge.getLastNode())) {
        GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl> otherEdge =
            new GraphWeightedEdgeBenchmarkImpl<>(
                edge.getLastNode(), edge.getFirstNode(), edge.weight);
        allPossibleEdges.remove(new GraphNodePair<>(edge.getFirstNode(), edge.getLastNode()));
        result.add(otherEdge);
        generated++;
      }
      allPossibleEdges.remove(new GraphNodePair<>(edge.getFirstNode(), edge.getLastNode()));
    }
    return result;
  }

  private static GraphWeightedEdgeBenchmarkImpl<GraphNodeBenchmarkImpl> generateEdge(
      Set<GraphNodePair<GraphNodeBenchmarkImpl>> availableEdges,
      boolean uniformWeights) {
    double weight = generatePositiveWeights(uniformWeights);
    int item = random.nextInt(availableEdges.size());
    Iterator<GraphNodePair<GraphNodeBenchmarkImpl>> iter = availableEdges.iterator();
    for (int i = 0; i < item; i++) {
      iter.next();
    }
    GraphNodePair<GraphNodeBenchmarkImpl> randomPair = iter.next();
    return new GraphWeightedEdgeBenchmarkImpl<>(randomPair.aNode, randomPair.anotherNode, weight);
  }

  private static double generatePositiveWeights(boolean uniformWeights) {
    if (uniformWeights) {
      return 1;
    }
    int min = 1;
    int max = 100;
    return random.nextInt(min,max * 4)/4.0;
  }

  private static int computeNumberOfPossibleEdges(int numberOfNodes) {
    return (int) (1.0 / 2 * numberOfNodes * (numberOfNodes + 1));
  }

  private static int computeNumberOfEdgesToGenerate(int numberOfNodes, double edgesDensity) {
    return (int) (computeNumberOfPossibleEdges(numberOfNodes) * edgesDensity);
  }
}
