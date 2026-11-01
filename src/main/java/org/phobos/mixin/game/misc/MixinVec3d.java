package org.phobos.mixin.game.misc;

import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IVec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Vec3d.class)
public abstract class MixinVec3d implements IVec3d {
   @Mutable
   @Shadow
   @Final
   public double y;
   @Mutable
   @Shadow
   @Final
   public double z;
   @Mutable
   @Shadow
   @Final
   public double x;

   @Override
   public void setZ(double value) {
      this.z = value;
   }

   @Override
   public void setY(double value) {
      this.y = value;
   }

   @Override
   public void setX(double value) {
      this.x = value;
   }
}
