package org.phobos.core.movement;

import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.player.PlayerActionUtils;

public class ThresholdGate implements MinecraftHolder {
   public final ThresholdGate.Mode mode;
   public static final double doubleValue = 0.99999999999998;
   public double doubleValue2;

   @Contract(pure = true)
   public boolean check() {
      return this.testDouble(0.99999999999998);
   }

   public boolean testFlag(boolean flag) {
      return this.testDouble2(0.99999999999998, flag);
   }

   public boolean testFlag2(boolean flag) {
      return this.testDouble3(0.99999999999998, flag);
   }

   public void handleDouble(double d, double d2) {
      assert mc.player != null && mc.world != null;
      switch (this.mode) {
         case POSITION:
            if (!mc.world
               .getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(d - mc.player.getX(), 0.0, d2 - mc.player.getZ()))
               .iterator()
               .hasNext()) {
               mc.player.setPosition(d, mc.player.getY(), d2);
            }
            break;
         case MOTION:
            if (d != mc.player.getX()) {
               PlayerActionUtils.handleClientPlayerEntity24(mc.player, d - mc.player.getX());
            }

            if (d2 != mc.player.getZ()) {
               PlayerActionUtils.handleClientPlayerEntity26(mc.player, d2 - mc.player.getZ());
            }
      }
   }

   @Contract(pure = true)
   public boolean testDouble(double d) {
      assert mc.player != null;
      return mc.player.getX() == Math.floor(mc.player.getX()) + d;
   }

   public ThresholdGate(double d, ThresholdGate.Mode mode) {
      this.doubleValue2 = d;
      this.mode = mode;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   @Contract(pure = true)
   public boolean check2() {
      return this.testDouble4(0.99999999999998);
   }

   public boolean testDouble2(@Range(from = -1L, to = 1L) double d, boolean flag) {
      assert mc.player != null;
      if (flag) {
         PlayerActionUtils.handleClientPlayerEntity24(mc.player, 0.0);
      }

      double d2 = (d > 0.0 ? MathHelper.floor(mc.player.getX()) : Math.round(mc.player.getX())) + d;
      if (mc.player.getX() != d2) {
         if (d2 > mc.player.getX()) {
            this.handleDouble(Math.min(mc.player.getX() + this.doubleValue2, d2), mc.player.getZ());
         } else {
            this.handleDouble(Math.max(mc.player.getX() - this.doubleValue2, d2), mc.player.getZ());
         }
      }

      return this.testDouble(d);
   }

   public boolean testDouble3(@Range(from = -1L, to = 1L) double d, boolean flag) {
      assert mc.player != null;
      if (flag) {
         PlayerActionUtils.handleClientPlayerEntity26(mc.player, 0.0);
      }

      double d2 = (d > 0.0 ? MathHelper.floor(mc.player.getZ()) : Math.round(mc.player.getZ())) + d;
      if (mc.player.getZ() != d2) {
         if (d2 > mc.player.getZ()) {
            this.handleDouble(mc.player.getX(), Math.min(mc.player.getZ() + this.doubleValue2, d2));
         } else {
            this.handleDouble(mc.player.getX(), Math.max(mc.player.getZ() - this.doubleValue2, d2));
         }
      }

      return this.testDouble4(d);
   }

   @Contract(pure = true)
   public boolean testDouble4(double d) {
      assert mc.player != null;
      return mc.player.getZ() == Math.floor(mc.player.getZ()) + d;
   }

   public enum Mode {
      POSITION,
      MOTION;
   }
}
