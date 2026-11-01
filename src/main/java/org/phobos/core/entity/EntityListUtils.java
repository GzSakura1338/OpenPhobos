package org.phobos.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import org.phobos.api.MinecraftHolder;

public final class EntityListUtils implements MinecraftHolder {
   public static List<Entity> getList() {
      if (mc.world == null) {
         return Collections.emptyList();
      }

      List<Entity> list = new ArrayList<>();

      for (Entity entity : mc.world.getEntities()) {
         list.add(entity);
      }

      return list;
   }

   public EntityListUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
