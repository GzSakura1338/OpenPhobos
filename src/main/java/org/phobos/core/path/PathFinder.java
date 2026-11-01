package org.phobos.core.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.CancelableFunction;
import org.phobos.api.OpenSet;
import org.phobos.api.PathState;
import org.phobos.api.PathNode;
import org.phobos.core.task.CancelToken;

public abstract class PathFinder<N extends PathNode<N>> implements PathState<N>, CancelableFunction<List<N>> {
   public final Map<N, @Nullable N> map;
   public final N pathNode;
   public OpenSet<N> openSet;
   public final AtomicBoolean atomicBoolean = new AtomicBoolean();
   public final N pathNode2;
   public N pathNode3;

   public Map<N, @Nullable N> getMap() {
      return this.map;
   }

   @Override
   public N getPathNode() {
      return this.pathNode;
   }

   public OpenSet<N> getOpenSet() {
      return this.openSet;
   }

   @Override
   public N getPathNode3() {
      return this.pathNode3;
   }

   @Nullable
   @Override
   public N resolvePathNode(N pathNode) {
      return this.map.get(pathNode);
   }

   public PathFinder(N pathNode, N pathNode2) {
      this.map = new HashMap<>();
      this.pathNode2 = pathNode;
      this.pathNode = pathNode2;
   }

   public boolean testPathNode(N pathNode, N pathNode2) {
      return pathNode.equals(pathNode2);
   }

   @Nullable
   public static <N extends PathNode<N>> List<N> resolveList(@Nullable List<N> list) {
      if (list != null) {
         Collections.reverse(list);
      }

      return list;
   }

   public boolean testPathNode2(N pathNode, N pathNode2) {
      return pathNode2.alwaysTrue();
   }

   @Nullable
   public List<N> resolveObject(CancelToken cancelToken) {
      synchronized (this.atomicBoolean) {
         if (this.atomicBoolean.getAndSet(true)) {
            throw new IllegalStateException("Algorithm " + this + " has run twice");
         }
      }

      if (this.pathNode2.equals(this.pathNode)) {
         return null;
      }

      this.openSet = this.getOpenSet2();
      this.map.put(this.pathNode2, null);
      this.openSet.add(this.pathNode2);
      cancelToken.run();

      while (!this.openSet.isEmpty() && !cancelToken.isCancelled()) {
         this.pathNode3 = this.openSet.removeFirst();
         assert this.pathNode3 != null;
         if (this.testPathNode(this.pathNode3, this.pathNode)) {
            List<N> list = new ArrayList<>();

            while (!cancelToken.isCancelled()) {
               list.add(this.pathNode3);
               if (this.pathNode3 == null) {
                  throw new IllegalStateException("current was null, type: " + this.pathNode2.getClass());
               }

               if (this.pathNode3.equals(this.pathNode2)) {
                  return list;
               }

               this.pathNode3 = this.map.get(this.pathNode3);
            }
         }

         for (N pathNode : this.pathNode3.getBlockNodeArray()) {
            if (pathNode != null && this.testPathNode2(this.pathNode3, pathNode)) {
               this.handlePathNode(this.pathNode3, pathNode);
            }
         }
      }

      return null;
   }

   @Override
   public N getPathNode2() {
      return this.pathNode2;
   }

   public abstract OpenSet<N> getOpenSet2();

   public abstract void handlePathNode(N var1, N var2);

   public static class Result<R extends PathNode<R>> {
      public final List<R> list;
      public PathFinder.Result.Order order;

      public PathFinder.Result<R> resolveResult(PathFinder.Result.Order order) {
         if (this.order != order) {
            this.order = order;
            Collections.reverse(this.list);
         }

         return this;
      }

      @Override
      public boolean equals(Object object) {
         if (object == this) {
            return true;
         } else if (!(object instanceof PathFinder.Result<?> result)) {
            return false;
         } else if (!result.testObject(this)) {
            return false;
         } else {
            Object object2 = this.getList();
            Object object3 = result.getList();
            if (object2 == null ? object3 == null : object2.equals(object3)) {
               Object object4 = this.getOrder();
               Object object5 = result.getOrder();
               return object4 == null ? object5 == null : object4.equals(object5);
            } else {
               return false;
            }
         }
      }

      public void setOrder(PathFinder.Result.Order order) {
         this.order = order;
      }

      @Override
      public String toString() {
         return "Algorithm.Result(path=" + this.getList() + ", order=" + this.getOrder() + ")";
      }

      public List<R> getList() {
         return this.list;
      }

      public Result(List<R> list, PathFinder.Result.Order order) {
         this.list = list;
         this.order = order;
      }

      @Override
      public int hashCode() {
         int i = 59;
         int i2 = 1;
         Object object = this.getList();
         i2 = i2 * 59 + (object == null ? 43 : object.hashCode());
         Object object2 = this.getOrder();
         return i2 * 59 + (object2 == null ? 43 : object2.hashCode());
      }

      public boolean testObject(Object object) {
         return object instanceof PathFinder.Result;
      }

      public PathFinder.Result.Order getOrder() {
         return this.order;
      }

      public enum Order {
         GOAL_TO_START,
         START_TO_GOAL;
      }
   }
}
