package graph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GraphWeightedEdgeBenchmarkImpl<T extends GraphNode> extends GraphWeightedEdge<T>
    implements Externalizable {

  public GraphWeightedEdgeBenchmarkImpl(T firstNode, T lastNode, double weight) {
    super(firstNode, lastNode, weight);
  }

  public GraphWeightedEdgeBenchmarkImpl() {
    super();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(firstNode);
    out.writeObject(lastNode);
    out.writeDouble(weight);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    firstNode = (T) in.readObject();
    lastNode = (T) in.readObject();
    weight = in.readDouble();
  }
}
