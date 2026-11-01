package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.attribute.EntityAttributes;
import org.phobos.core.math.MathUtils;
import org.phobos.module.combat.WTapModule;

public final class WTapTickListener extends ModuleListener.Safe<WTapModule, TickEvent2> {
   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (mc.currentScreen == null && !clientPlayerEntity.isDead()) {
         if (!clientPlayerEntity.isSprinting() && this.module.longValue > 0L) {
            this.module.longValue = 0L;
            this.module.run();
         } else if (System.currentTimeMillis() - this.module.longValue > 0L && this.module.longValue != 0L) {
            mc.options.forwardKey.setPressed(false);
            this.handleClientPlayerEntity(clientPlayerEntity);
            this.module.flag = true;
            this.module.longValue2 = System.currentTimeMillis() + 90L + MathUtils.computeInt5(0, 50);
            this.module.longValue = 0L;
         } else if (System.currentTimeMillis() - this.module.longValue2 > 0L && this.module.longValue2 != 0L) {
            mc.options.forwardKey.setPressed(true);
            this.handleClientPlayerEntity(clientPlayerEntity);
            this.module.longValue2 = 0L;
         } else {
            this.module.run();
         }
      }
   }

   public WTapTickListener(WTapModule wTapModule) {
      super(wTapModule, TickEvent2.class);
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      float f = (float)clientPlayerEntity.getAttributeValue(EntityAttributes.PLAYER_SNEAKING_SPEED);
      clientPlayerEntity.input.tick(clientPlayerEntity.shouldSlowDown(), f);
   }
}
