package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.math.MathUtils;
import org.phobos.module.combat.WTapModule;

public final class WTapAttackListener extends ModuleListener.Safe<WTapModule, AttackEvent2> {
   public WTapAttackListener(WTapModule wTapModule) {
      super(wTapModule, AttackEvent2.class);
   }

   public void handleObject(
      AttackEvent2 attackEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!clientPlayerEntity.isDead()
         && mc.currentScreen == null
         && clientPlayerEntity.isSprinting()
         && (this.module.chance.getValue().equals(100.0) || !(Math.random() * 100.0 > this.module.chance.getValue()))
         && this.module.longValue == 0L
         && this.module.longValue2 == 0L) {
         this.module.longValue = System.currentTimeMillis() + 40L + MathUtils.computeInt5(0, 325);
      }
   }
}
