package org.phobos.core.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;

@MethodsReturnNonnullByDefault
public class MutableBlockPos extends Mutable {
   public void handleInt(int i) {
      super.setZ(this.getZ() + i);
   }

   public void handleDirection(Direction direction) {
      this.set(this.getX() + direction.getOffsetX(), this.getY() + direction.getOffsetY(), this.getZ() + direction.getOffsetZ());
   }

   public MutableBlockPos setY(int i) {
      super.setY(i);
      return this;
   }

   public MutableBlockPos setZ(int i) {
      super.setZ(i);
      return this;
   }

   public void handleInt2(int i) {
      super.setX(this.getX() + i);
   }

   public void handlePosition(Position position) {
      this.set(position.getX(), position.getY(), position.getZ());
   }

   public void handleInt3(int i) {
      super.setY(this.getY() + i);
   }

   public MutableBlockPos setX(int i) {
      super.setX(i);
      return this;
   }

   public void handleEntity(Entity entity) {
      Vec3d vec3d = entity.getPos();
      this.set(vec3d.x, vec3d.y, vec3d.z);
   }
}
