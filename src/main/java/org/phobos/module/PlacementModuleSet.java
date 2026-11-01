package org.phobos.module;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class PlacementModuleSet implements Iterable<PlacementModule> {
   public final Set<PlacementModule> placementModuleSet = new HashSet<>();

   @NotNull
   @Override
   public Iterator<PlacementModule> iterator() {
      return this.placementModuleSet.iterator();
   }

   public void handlePlacementModule(PlacementModule placementModule) {
      this.placementModuleSet.add(placementModule);
   }
}
