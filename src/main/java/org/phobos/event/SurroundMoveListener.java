package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.phobos.module.combat.SurroundModule;

public final class SurroundMoveListener extends ModuleListener.Safe<SurroundModule, MoveEvent2> {
   public void handleObject(
      MoveEvent2 moveEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.booleanValueSetting10.getValue() && !this.module.longValueSetting2.getValue().equals(0L)) {
         if (!this.module.normal3.testNumber(this.module.longValueSetting2.getValue())) {
            moveEvent2.setVec3d(Vec3d.ZERO);
            moveEvent2.setFlag(true);
         }
      }
   }

   public SurroundMoveListener(SurroundModule surroundModule) {
      super(surroundModule, MoveEvent2.class, Integer.MAX_VALUE);
   }
}
