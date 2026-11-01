package org.phobos.api;

import org.jetbrains.annotations.Nullable;

public interface PathState<N extends PathNode<N>> {
   @Nullable
   N getPathNode3();

   @Nullable
   N resolvePathNode(N var1);

   N getPathNode();

   N getPathNode2();
}
