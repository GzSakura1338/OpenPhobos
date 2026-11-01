package org.phobos.core.anticheat.check;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class AverageACheck extends SimulationCheck {
   @Override
   public int value() {
      return 10;
   }

   @Override
   public void run3() {
      if (!this.isFlag4()) {
         if (!VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)) {
            if (!this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying() && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0) {
               List<Vec3d> list = VectorMath.resolveList(this.antiCheatPlayer.vec3dList);
               double d = this.computeStream(list.stream().filter(vec3d -> !VectorMath.testVec3d2(vec3d)).map(Vec3d::getX)) / list.size();
               double d2 = this.computeStream(list.stream().filter(vec3d -> !VectorMath.testVec3d2(vec3d)).map(Vec3d::getY)) / list.size();
               double d3 = this.computeStream(list.stream().filter(vec3d -> !VectorMath.testVec3d2(vec3d)).map(Vec3d::getZ)) / list.size();
               double d4 = this.value3() * this.antiCheatPlayer.doubleValue;
               double d5 = this.value4();
               boolean flag = d > d4 || d3 > d4;
               boolean flag2 = d2 > d5;
               if (flag || flag2) {
                  this.handleText("Tick: %.2f, %.2f, %.2f, Max: %.2f, %.2f".formatted(d, d2, d3, d4, d5));
               }
            }
         }
      }
   }

   public int value2() {
      return this.antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST)
         ? Objects.requireNonNull(this.antiCheatPlayer.abstractClientPlayerEntity.getStatusEffect(StatusEffects.JUMP_BOOST)).getAmplifier()
         : 1;
   }

   public double value3() {
      boolean flag = this.antiCheatPlayer.booleanValueList.subList(0, 20).stream().anyMatch(booleanValue -> booleanValue);
      boolean flag2 = this.antiCheatPlayer.flag10 || this.antiCheatPlayer.flag6;
      if (flag) {
         if (this.check()) {
            return 0.55;
         }

         if (this.antiCheatPlayer.booleanValueList2.subList(0, 20).stream().anyMatch(booleanValue -> !booleanValue)) {
            return 0.38;
         }

         if (flag2) {
            return 0.32;
         }
      }

      return 0.25;
   }

   @Override
   public boolean isFlag4() {
      return true;
   }

   public double value4() {
      return 0.26 * this.value2() + this.antiCheatPlayer.abstractClientPlayerEntity.fallDistance / 2.0F;
   }

   public boolean check() {
      int i = 0;
      MutableObject<Boolean> mutableObject = new MutableObject(null);

      for (boolean flag : this.antiCheatPlayer.booleanValueList2) {
         if (mutableObject.getValue() == null) {
            mutableObject.setValue(flag);
         } else if ((Boolean)mutableObject.getValue() != flag) {
            mutableObject.setValue(flag);
            i++;
         }
      }

      return i > 8;
   }

   public double computeStream(Stream<Double> stream) {
      MutableObject<Double> mutableObject = new MutableObject(0.0);
      stream.forEach(doubleValue -> mutableObject.setValue((Double)mutableObject.getValue() + Math.abs(doubleValue)));
      return (Double)mutableObject.getValue();
   }

   public AverageACheck(AntiCheatPlayer antiCheatPlayer) {
      super("AverageA", antiCheatPlayer);
   }
}
