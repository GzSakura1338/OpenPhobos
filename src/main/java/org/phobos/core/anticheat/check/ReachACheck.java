package org.phobos.core.anticheat.check;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.world.RaycastUtils;

public class ReachACheck extends AntiCheatCheck {
   @Override
   public int value() {
      return 4;
   }

   @Override
   public void run10() {
      if (!this.isFlag4()) {
         this.antiCheatPlayer
            .scheduledExecutorService2
            .schedule(
               () -> {
                  try {
                     Entity entity = this.resolveList2(8.0)
                        .stream()
                        .filter(entityx -> entityx instanceof PlayerEntity)
                        .filter(entityx -> {
                           EntityHitResult entityHitResult = RaycastUtils.resolveEntityHitResult2(this.antiCheatPlayer.abstractClientPlayerEntity, entityx);
                           return entityHitResult != null && entityHitResult.getEntity() != null && entityHitResult.getEntity().equals(entityx);
                        })
                        .min(
                           (entityx, entity2) -> (int)(
                              (
                                    entityx.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity)
                                       - entity2.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity)
                                 )
                                 * 100.0F
                           )
                        )
                        .orElseThrow();
                     double d = entity.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity);
                     if (d < 6.0 && d > 3.5) {
                        this.handleText("Target: %s Distance: %.2f".formatted(entity.getNameForScoreboard(), d));
                     }
                  } catch (NoSuchElementException var4) {
                  }
               },
               50L,
               TimeUnit.MILLISECONDS
            );
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getCombat().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getReach().getValue();
   }

   public ReachACheck(AntiCheatPlayer antiCheatPlayer) {
      super("ReachA", antiCheatPlayer);
   }
}
