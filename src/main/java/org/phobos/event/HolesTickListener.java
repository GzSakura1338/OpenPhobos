package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.chunk.Hole;
import org.phobos.module.render.HolesModule;

public final class HolesTickListener extends ModuleListener.Safe<HolesModule, TickEvent2> {
   public HolesTickListener(HolesModule holesModule) {
      super(holesModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.holePriorityQueue.clear();
      HolesModule.intValue++;
      double d = MathHelper.square(this.module.doubleValueSetting.getValue());

      for (Hole hole : this.phobosClient.getHoleChunkScanner().getCollection()) {
         if (hole.isFlag2() && hole.getIntValue5() != HolesModule.intValue && hole.computeEntity(clientPlayerEntity) <= d) {
            hole.setIntValue5(HolesModule.intValue);
            this.module.holePriorityQueue.add(hole);
            if (this.module.holePriorityQueue.size() > this.module.integerSetting.getValue()) {
               this.module.holePriorityQueue.poll();
            }
         }
      }
   }
}
