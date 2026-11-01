package org.phobos.api;

import java.util.UUID;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.math.Vector3;
import org.phobos.core.animation.StickSimulation3d;
import org.phobos.core.animation.StickSimulation2d;
import org.phobos.core.animation.StickSimulation2d;
import org.phobos.core.animation.RopeSimulation;
import org.phobos.module.client.CapesModule;
import org.phobos.type.VanillaBasicEnum;

public interface PlayerAnimator {
   void setSimulation(Animatable var1);

   default boolean testAnimatable(Animatable animatable) {
      VanillaBasicEnum vanillaBasicEnum = CapesModule.getVanillaBasicEnum();
      if (vanillaBasicEnum == VanillaBasicEnum.Basic && animatable.getClass() != StickSimulation2d.class) {
         return true;
      } else {
         return vanillaBasicEnum == VanillaBasicEnum.Polygons && animatable.getClass() != StickSimulation3d.class
            ? true
            : vanillaBasicEnum == VanillaBasicEnum.Dungeons && animatable.getClass() != RopeSimulation.class;
      }
   }

   Vector3 getLastPlayerAnimatorPosition();

   default Animatable getAnimatable() {
      return switch (CapesModule.getVanillaBasicEnum()) {
         case Polygons -> new StickSimulation2d();
         case Dungeons -> new StickSimulation3d();
         case Vanilla -> new RopeSimulation();
         default -> null;
      };
   }

   default void handleNumericStats(NumericStats numericStats) {
      Animatable animatable = this.getSimulation();
      if (animatable != null && !animatable.check()) {
         double d = numericStats.value4() - numericStats.value5();
         double d2 = numericStats.value8() - numericStats.value();
         float f2 = numericStats.value11();
         double d3 = MathHelper.sin(f2 * (float) (Math.PI / 180.0));
         double d4 = -MathHelper.cos(f2 * (float) (Math.PI / 180.0));
         float f3 = CapesModule.value2();
         float f4 = CapesModule.value3();
         if (numericStats.check3()) {
            f3 *= 2.0F;
         }

         double d5 = MathHelper.clamp((numericStats.value12() - numericStats.value3()) * 10.0, 0.0, 1.0);
         if (numericStats.check3()) {
            animatable.setFloatValue2(CapesModule.value() / 10.0F);
         } else {
            animatable.setFloatValue2(CapesModule.value());
         }

         Vector3 vector3 = new Vector3(0.0F, -1.0F, 0.0F);
         StickSimulation2d.Vector2 vector2 = new StickSimulation2d.Vector2(
            (float)(numericStats.value5() - numericStats.value6()), (float)(numericStats.value() - numericStats.value2())
         );
         vector2.resolveVector25(-numericStats.value10());
         double d6 = d * d3 + d2 * d4 + d5 + (numericStats.check2() && !animatable.isFlag() ? 3 : 0);
         double d7 = (numericStats.value3() - numericStats.value12()) * f3 + (numericStats.check2() && !animatable.isFlag() ? 1 : 0);
         double d8 = -vector2.floatValue * f4;
         animatable.setFlag(numericStats.check2());
         Vector3 vector32 = new Vector3((float)d6, (float)d7, (float)d8);
         if (numericStats.check()) {
            float f = numericStats.value13() + 90.0F;
            vector3.resolveVector33(f);
            vector32.resolveVector33(f);
         }

         animatable.setVector3(vector3);
         animatable.handleVector3(vector32);
         animatable.run3();
      }
   }

   default void handleInt(int i) {
      Animatable animatable = this.getSimulation();
      if (animatable == null || this.testAnimatable(animatable)) {
         animatable = this.getAnimatable();
         this.setSimulation(animatable);
      }

      if (animatable != null && animatable.testInt(i)) {
         this.run();
      }
   }

   void run();

   Animatable getSimulation();

   UUID getUUID();

   void setLastPlayerAnimatorPosition(Vector3 var1);
}
