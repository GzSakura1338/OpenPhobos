package org.phobos.core.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.util.Identifier;
import org.phobos.api.WorldRenderStage;

public class WorldRenderStages {
   public final Map<Identifier, WorldRenderStage> identifierMap;
   public static final WorldRenderStages INSTANCE = new WorldRenderStages();
   public final List<Identifier> identifierList = new ArrayList<>();

   public WorldRenderStages() {
      this.identifierMap = new Object2ObjectLinkedOpenHashMap();
   }

   public static WorldRenderStages getINSTANCE() {
      return INSTANCE;
   }
}
