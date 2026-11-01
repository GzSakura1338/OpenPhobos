package org.phobos.core.path;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.OpenSet;
import org.phobos.api.PathNode;
import org.phobos.core.task.CancelToken;

public class AStarPathFinder<N extends PathNode<N>> extends PathFinder<N> {
   public final Map<N, Double> map2 = new HashMap<>();

   @Override
   public void handlePathNode(N pathNode, N pathNode2) {
      double d = this.map2.getOrDefault(pathNode, Double.POSITIVE_INFINITY) + this.computePathNode(pathNode, pathNode2);
      if (d < this.map2.getOrDefault(pathNode2, Double.POSITIVE_INFINITY)) {
         this.handlePathNode2(pathNode, pathNode2, d);
      }
   }

   @Nullable
   @Override
   public List<N> resolveObject(CancelToken cancelToken) {
      this.map2.put(this.pathNode2, 0.0);
      return super.resolveObject(cancelToken);
   }

   public double computePathNode(N pathNode, N pathNode2) {
      return 1.0;
   }

   public AStarPathFinder(N pathNode, N pathNode2) {
      super(pathNode, pathNode2);
   }

   public void handlePathNode2(N pathNode, N pathNode2, double d) {
      this.map.put(pathNode2, pathNode);
      if (this.map2.containsKey(pathNode2)) {
         this.openSet.handleObject(pathNode2, () -> this.map2.put(pathNode2, d));
      } else {
         this.map2.put(pathNode2, d);
         this.openSet.add(pathNode2);
      }
   }

   @Override
   public OpenSet<N> getOpenSet2() {
      return OpenSet.createValue(
         new TreeSet<>(Comparator.<N>comparingDouble(pathNode -> this.map2.get(pathNode)).thenComparing(pathNode -> (N)pathNode))
      );
   }
}
