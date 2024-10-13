package graph.minimumpaths;

import graph.GraphNodeTestImpl;
import graph.GraphTestImpl;
import graph.GraphWeightedEdge;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class GraphTestExemples {

  public static GraphTestImpl getSimpleGraph() {
    List<GraphNodeTestImpl> nodes = IntStream.range(0, 4).mapToObj(GraphNodeTestImpl::new).toList();
    Set<GraphWeightedEdge<GraphNodeTestImpl>> edges = new HashSet<>();
    edges.add(new GraphWeightedEdge<>(nodes.get(0), nodes.get(2), 2));
    edges.add(new GraphWeightedEdge<>(nodes.get(1), nodes.get(0), 4));
    edges.add(new GraphWeightedEdge<>(nodes.get(1), nodes.get(2), 3));
    edges.add(new GraphWeightedEdge<>(nodes.get(2), nodes.get(3), 2));
    edges.add(new GraphWeightedEdge<>(nodes.get(3), nodes.get(1), 1));

    return new GraphTestImpl(nodes, edges);
  }

  public static double[][] getSimpleGraphExpectedDistances() {
    double[][] expected = new double[4][4];
    expected[0][0] = 0;
    expected[0][1] = 5;
    expected[0][2] = 2;
    expected[0][3] = 4;
    expected[1][0] = 4;
    expected[1][1] = 0;
    expected[1][2] = 3;
    expected[1][3] = 5;
    expected[2][0] = 7;
    expected[2][1] = 3;
    expected[2][2] = 0;
    expected[2][3] = 2;
    expected[3][0] = 5;
    expected[3][1] = 1;
    expected[3][2] = 4;
    expected[3][3] = 0;
    return expected;
  }

  public static GraphTestImpl getComplexGraph() {
    List<GraphNodeTestImpl> nodes =
        IntStream.range(0, 11).mapToObj(GraphNodeTestImpl::new).toList();
    Set<GraphWeightedEdge<GraphNodeTestImpl>> edges = new HashSet<>();
    edges.add(new GraphWeightedEdge<>(nodes.get(0), nodes.get(0), 1));
    edges.add(new GraphWeightedEdge<>(nodes.get(0), nodes.get(1), 2));
    edges.add(new GraphWeightedEdge<>(nodes.get(0), nodes.get(2), 3));
    edges.add(new GraphWeightedEdge<>(nodes.get(0), nodes.get(3), 3));

    edges.add(new GraphWeightedEdge<>(nodes.get(1), nodes.get(2), 1));

    edges.add(new GraphWeightedEdge<>(nodes.get(2), nodes.get(0), 2));
    edges.add(new GraphWeightedEdge<>(nodes.get(2), nodes.get(2), 1));
    edges.add(new GraphWeightedEdge<>(nodes.get(2), nodes.get(7), 2));

    edges.add(new GraphWeightedEdge<>(nodes.get(3), nodes.get(2), 1));
    edges.add(new GraphWeightedEdge<>(nodes.get(3), nodes.get(4), 2));

    edges.add(new GraphWeightedEdge<>(nodes.get(4), nodes.get(5), 1));
    edges.add(new GraphWeightedEdge<>(nodes.get(4), nodes.get(6), 1));

    edges.add(new GraphWeightedEdge<>(nodes.get(5), nodes.get(7), 1));

    edges.add(new GraphWeightedEdge<>(nodes.get(6), nodes.get(5), 2));
    edges.add(new GraphWeightedEdge<>(nodes.get(6), nodes.get(8), 3));

    edges.add(new GraphWeightedEdge<>(nodes.get(7), nodes.get(2), 1));

    edges.add(new GraphWeightedEdge<>(nodes.get(8), nodes.get(5), 4));

    edges.add(new GraphWeightedEdge<>(nodes.get(9), nodes.get(2), 3));
    edges.add(new GraphWeightedEdge<>(nodes.get(9), nodes.get(10), 2));

    edges.add(new GraphWeightedEdge<>(nodes.get(10), nodes.get(9), 2));

    return new GraphTestImpl(nodes, edges);
  }

  public static double[][] getComplexGraphExpectedDistances() {
    double[][] expected = new double[11][11];
    expected[0][0] = 0;
    expected[0][1] = 2;
    expected[0][2] = 3;
    expected[0][3] = 3;
    expected[0][4] = 5;
    expected[0][5] = 6;
    expected[0][6] = 6;
    expected[0][7] = 5;
    expected[0][8] = 9;
    expected[0][9] = Double.POSITIVE_INFINITY;
    expected[0][10] = Double.POSITIVE_INFINITY;

    expected[1][0] = 3;
    expected[1][1] = 0;
    expected[1][2] = 1;
    expected[1][3] = 6;
    expected[1][4] = 8;
    expected[1][5] = 9;
    expected[1][6] = 9;
    expected[1][7] = 3;
    expected[1][8] = 12;
    expected[1][9] = Double.POSITIVE_INFINITY;
    expected[1][10] = Double.POSITIVE_INFINITY;

    expected[2][0] = 2;
    expected[2][1] = 4;
    expected[2][2] = 0;
    expected[2][3] = 5;
    expected[2][4] = 7;
    expected[2][5] = 8;
    expected[2][6] = 8;
    expected[2][7] = 2;
    expected[2][8] = 11;
    expected[2][9] = Double.POSITIVE_INFINITY;
    expected[2][10] = Double.POSITIVE_INFINITY;

    expected[3][0] = 3;
    expected[3][1] = 5;
    expected[3][2] = 1;
    expected[3][3] = 0;
    expected[3][4] = 2;
    expected[3][5] = 3;
    expected[3][6] = 3;
    expected[3][7] = 3;
    expected[3][8] = 6;
    expected[3][9] = Double.POSITIVE_INFINITY;
    expected[3][10] = Double.POSITIVE_INFINITY;

    expected[4][0] = 5;
    expected[4][1] = 7;
    expected[4][2] = 3;
    expected[4][3] = 8;
    expected[4][4] = 0;
    expected[4][5] = 1;
    expected[4][6] = 1;
    expected[4][7] = 2;
    expected[4][8] = 4;
    expected[4][9] = Double.POSITIVE_INFINITY;
    expected[4][10] = Double.POSITIVE_INFINITY;

    expected[5][0] = 4;
    expected[5][1] = 6;
    expected[5][2] = 2;
    expected[5][3] = 7;
    expected[5][4] = 9;
    expected[5][5] = 0;
    expected[5][6] = 10;
    expected[5][7] = 1;
    expected[5][8] = 13;
    expected[5][9] = Double.POSITIVE_INFINITY;
    expected[5][10] = Double.POSITIVE_INFINITY;

    expected[6][0] = 6;
    expected[6][1] = 8;
    expected[6][2] = 4;
    expected[6][3] = 9;
    expected[6][4] = 11;
    expected[6][5] = 2;
    expected[6][6] = 0;
    expected[6][7] = 3;
    expected[6][8] = 3;
    expected[6][9] = Double.POSITIVE_INFINITY;
    expected[6][10] = Double.POSITIVE_INFINITY;

    expected[7][0] = 3;
    expected[7][1] = 5;
    expected[7][2] = 1;
    expected[7][3] = 6;
    expected[7][4] = 8;
    expected[7][5] = 9;
    expected[7][6] = 9;
    expected[7][7] = 0;
    expected[7][8] = 12;
    expected[7][9] = Double.POSITIVE_INFINITY;
    expected[7][10] = Double.POSITIVE_INFINITY;

    expected[8][0] = 8;
    expected[8][1] = 10;
    expected[8][2] = 6;
    expected[8][3] = 11;
    expected[8][4] = 13;
    expected[8][5] = 4;
    expected[8][6] = 14;
    expected[8][7] = 5;
    expected[8][8] = 0;
    expected[8][9] = Double.POSITIVE_INFINITY;
    expected[8][10] = Double.POSITIVE_INFINITY;

    expected[9][0] = 5;
    expected[9][1] = 7;
    expected[9][2] = 3;
    expected[9][3] = 8;
    expected[9][4] = 10;
    expected[9][5] = 11;
    expected[9][6] = 11;
    expected[9][7] = 5;
    expected[9][8] = 14;
    expected[9][9] = 0;
    expected[9][10] = 2;

    expected[10][0] = 7;
    expected[10][1] = 9;
    expected[10][2] = 5;
    expected[10][3] = 10;
    expected[10][4] = 12;
    expected[10][5] = 13;
    expected[10][6] = 13;
    expected[10][7] = 7;
    expected[10][8] = 16;
    expected[10][9] = 2;
    expected[10][10] = 0;
    return expected;
  }
}
