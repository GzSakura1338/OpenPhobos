package org.phobos.core.anticheat.check;

import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.AntiCheatPlayerRegistry;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.anticheat.RunnableCheck;

public class AimFCheck extends RunnableCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(AimFCheck.class);

   public AimFCheck(AntiCheatPlayer antiCheatPlayer) {
      super("AimF", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 2;
   }

   @Override
   public void run() {
      if (!this.isFlag4()) {
         this.antiCheatPlayer
            .scheduledExecutorService2
            .execute(
               () -> {
                  int i3 = 0;

                  for (int i2 = 0; i2 < 20; i2++) {
                     Vec2f vec2f = this.antiCheatPlayer.vec2fList.get(i2);
                     if (vec2f != null) {
                        Vec3d vec3d = this.antiCheatPlayer.vec3dList.get(i2);
                        MovingFakePlayer movingFakePlayer = new MovingFakePlayer(mc.world);
                        movingFakePlayer.copyFrom(this.antiCheatPlayer.abstractClientPlayerEntity);
                        movingFakePlayer.setPosition(vec3d.x, vec3d.y, vec3d.z);
                        movingFakePlayer.setYaw(vec2f.y);
                        movingFakePlayer.setPitch(vec2f.x);
                        int i = i2;
                        AntiCheatPlayer antiCheatPlayer = AntiCheatPlayerRegistry.getAntiCheatPlayerRegistry()
                           .getList2()
                           .stream()
                           .filter(antiCheatPlayerx -> movingFakePlayer.distanceTo(antiCheatPlayerx.abstractClientPlayerEntity) < 15.0)
                           .filter(
                              antiCheatPlayerx -> {
                                 MovingFakePlayer movingFakePlayer2 = new MovingFakePlayer(mc.world);
                                 movingFakePlayer2.copyFrom(antiCheatPlayerx.abstractClientPlayerEntity);
                                 Vec3d vec3dx = antiCheatPlayerx.vec3dList.get(i);
                                 movingFakePlayer2.setPosition(vec3dx.x, vec3dx.y, vec3dx.z);
                                 EntityHitResult entityHitResult = RaycastUtils.resolveEntityHitResult2(movingFakePlayer, movingFakePlayer2);
                                 return entityHitResult != null
                                    && entityHitResult.getEntity() != null
                                    && entityHitResult.getEntity().equals(antiCheatPlayerx.abstractClientPlayerEntity);
                              }
                           )
                           .findAny()
                           .orElse(null);
                        if (antiCheatPlayer != null) {
                           float[] floats = RotationUtils.values2(movingFakePlayer, antiCheatPlayer.vec3dList.get(i2));
                           if (Math.abs(vec2f.y - floats[0]) < 5.0F) {
                              LOGGER.info("Rot");
                              i3++;
                           }

                           if (i3 > 15) {
                              break;
                           }
                        }
                     }
                  }

                  if (i3 > 15) {
                     this.run7();
                  }
               }
            );
      }
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue();
   }
}
