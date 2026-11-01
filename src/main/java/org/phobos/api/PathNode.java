package org.phobos.api;

public interface PathNode<N extends PathNode<N>> extends Comparable<N> {
   int value();

   N[] getBlockNodeArray();

   double computePathNode(N var1);

   int value2();

   default boolean alwaysTrue() {
      return true;
   }

   default double computePathNode2(N pathNode) {
      return Math.sqrt(this.computePathNode(pathNode));
   }
}
