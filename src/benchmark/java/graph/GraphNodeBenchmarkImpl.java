package graph;

import java.io.Serializable;
import java.util.Objects;

public class GraphNodeBenchmarkImpl implements GraphNode, Serializable {

  private final int graphIndex;

  public GraphNodeBenchmarkImpl(int graphIndex) {
    this.graphIndex = graphIndex;
  }

  public GraphNodeBenchmarkImpl() {
    this(-1);
  }

  @Override
  public int getGraphIndex() {
    return graphIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GraphNodeBenchmarkImpl that = (GraphNodeBenchmarkImpl) o;
    return graphIndex == that.graphIndex;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(graphIndex);
  }
}
