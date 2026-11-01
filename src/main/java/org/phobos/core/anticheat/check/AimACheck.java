package org.phobos.core.anticheat.check;

import java.util.List;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.AntiCheatCheck;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.entity.EntityListUtils;
import org.phobos.core.math.VectorMath;

public class AimACheck extends AntiCheatCheck {
   public static final Logger LOGGER = LoggerFactory.getLogger(AimACheck.class);

   public float computeAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity, Vec3d vec3d) {
      return abstractClientPlayerEntity.getYaw()
         + MathHelper.wrapDegrees(
            (float)Math.toDegrees(Math.atan2(vec3d.getZ() - abstractClientPlayerEntity.getZ(), vec3d.getX() - abstractClientPlayerEntity.getX()))
               - 90.0F
               - abstractClientPlayerEntity.getYaw()
         );
   }

   public AimACheck(AntiCheatPlayer antiCheatPlayer) {
      super("AimA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 30;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getAim().getValue();
   }

   public float computeAbstractClientPlayerEntity2(AbstractClientPlayerEntity abstractClientPlayerEntity, Vec3d vec3d) {
      double d = vec3d.getX() - abstractClientPlayerEntity.getX();
      double d2 = vec3d.getY() - (abstractClientPlayerEntity.getY() + abstractClientPlayerEntity.getEyeHeight(abstractClientPlayerEntity.getPose()));
      double d3 = vec3d.getZ() - abstractClientPlayerEntity.getZ();
      double d4 = Math.sqrt(d * d + d3 * d3);
      return abstractClientPlayerEntity.getPitch()
         + MathHelper.wrapDegrees((float)(-Math.toDegrees(Math.atan2(d2, d4))) - abstractClientPlayerEntity.getPitch());
   }

   @Override
   public void run3() {
      if (!this.antiCheatPlayer.vec2f.equals(this.antiCheatPlayer.vec2f2)) {
         float f = this.antiCheatPlayer.vec2f.y - this.antiCheatPlayer.vec2f2.y;
         float f2 = this.antiCheatPlayer.vec2f.x - this.antiCheatPlayer.vec2f2.x;
         if (!(f < 25.0F) && !(f2 < 25.0F)) {
            List<Entity> list = EntityListUtils.getList()
               .stream()
               .filter(entityx -> !entityx.equals(this.antiCheatPlayer.abstractClientPlayerEntity))
               .filter(entityx -> VectorMath.testVec3d(entityx.getVelocity()))
               .filter(entityx -> entityx.distanceTo(this.antiCheatPlayer.abstractClientPlayerEntity) < 6.0)
               .toList();
            double d = 0.0;
            double d2 = 0.0;
            boolean flag = false;
            boolean flag2 = false;
            Entity entity2 = null;

            for (Entity entity : list) {
               d = Math.abs(this.computeAbstractClientPlayerEntity(this.antiCheatPlayer.abstractClientPlayerEntity, entity.getEyePos()) - entity.getYaw());
               d2 = Math.abs(this.computeAbstractClientPlayerEntity2(this.antiCheatPlayer.abstractClientPlayerEntity, entity.getEyePos()) - entity.getPitch());
               flag = d < 2.0;
               flag2 = d2 < 2.0;
               if (flag || flag2) {
                  entity2 = entity;
                  break;
               }
            }

            if (flag && flag2) {
               this.handleText(String.format("Target: %s Divisor: %.2f, %.2f  Delta: %.2f,%.2f & YP", entity2.getName().getString(), d, d2, f, f2));
            } else if (flag) {
               this.handleText(String.format("Target: %s Divisor: %.2f Delta: %.2f & Y", entity2.getName().getString(), d, f));
            } else if (flag2) {
               this.handleText(String.format("Target: %s Divisor: %.2f Delta: %.2f & P", entity2.getName().getString(), d2, f2));
            }
         }
      }
   }
}
