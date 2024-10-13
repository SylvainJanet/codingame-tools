package graph.minimumpaths.Dijkstra;

import graph.minimumpaths.MinimumPathsAlgorithmTest;
import org.junit.jupiter.api.BeforeAll;

public class DijkstraAlgorithmTest extends MinimumPathsAlgorithmTest {

  @BeforeAll
  static void setUp() {
    algorithm = new DijkstraAlgorithm();
  }
}
