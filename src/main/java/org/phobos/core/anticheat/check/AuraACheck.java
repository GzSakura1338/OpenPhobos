package org.phobos.core.anticheat.check;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.world.RaycastUtils;

public class AuraACheck extends AntiCheatCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(AuraACheck.class);
   public boolean flag = false;
   public long longValue = 0L;

   @Override
   public void run3() {
      if (this.longValue > 0L && !this.flag) {
         this.longValue -= 50L;
      }

      this.flag = false;
      if (this.longValue > 300L) {
         this.run7();
         this.longValue = 0L;
      }
   }

   @Override
   public void run10() {
      if (!this.isFlag4()) {
         this.antiCheatPlayer
            .scheduledExecutorService2
            .execute(
               () -> {
                  this.resolveList2(4.5)
                     .stream()
                     .filter(entity -> entity instanceof PlayerEntity)
                     .filter(entity -> {
                        EntityHitResult entityHitResult = RaycastUtils.resolveEntityHitResult2(this.antiCheatPlayer.abstractClientPlayerEntity, entity);
                        return entityHitResult != null && entityHitResult.getEntity() != null && entityHitResult.getEntity().equals(entity);
                     })
                     .min(
                        (entity, entity2) -> (int)(
                           (entity.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity) - entity2.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity))
                              * 100.0F
                        )
                     )
                     .orElseThrow();
                  this.longValue += 150L;
                  this.flag = true;
               }
            );
      }
   }

   public AuraACheck(AntiCheatPlayer antiCheatPlayer) {
      super("AuraA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 2;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getCombat().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getAura().getValue();
   }
}
