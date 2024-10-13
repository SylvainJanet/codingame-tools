package graph.minimumpaths.FloydWarshall;

import graph.minimumpaths.MinimumPathsAlgorithmTest;
import org.junit.jupiter.api.BeforeAll;

class FloydWarshallAlgorithmTest extends MinimumPathsAlgorithmTest {

  @BeforeAll
  static void setUp() {
    algorithm = new FloydWarshallAlgorithm();
  }
}
