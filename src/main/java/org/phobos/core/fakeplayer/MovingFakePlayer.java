package org.phobos.core.fakeplayer;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.ClientHolder;
import org.phobos.core.movement.MovementModel;

public class MovingFakePlayer extends FakePlayer implements MinecraftHolder {
   public boolean flag3;
   public Function<Vec3d, Vec3d> vec3dFunction = Function.identity();
   public boolean flag4;
   public MovementModel movementModel;

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public void handleDouble(double d, boolean flag) {
      if (flag) {
         this.onLanding();
      } else if (d < 0.0) {
         this.fallDistance -= (float)d;
      }
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   public void setVec3dFunction(Function<Vec3d, Vec3d> function) {
      this.vec3dFunction = function;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public void setMovementModel(MovementModel movementModel) {
      this.movementModel = movementModel;
   }

   @Override
   public Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type) {
      return super.adjustMovementForSneaking(movement, type);
   }

   public MovingFakePlayer(ClientWorld clientWorld, GameProfile gameProfile) {
      super(clientWorld, gameProfile);
   }

   public MovingFakePlayer(ClientWorld clientWorld) {
      super(clientWorld);
   }

   @Override
   public void copyFrom(Entity original) {
      try {
         super.copyFrom(original);
         this.setOnGround(original.isOnGround());
         this.verticalCollision = original.verticalCollision;
         this.groundCollision = original.groundCollision;
         this.horizontalCollision = original.horizontalCollision;
      } catch (Throwable var3) {
      }
   }

   public MovementModel getMovementModel() {
      return this.movementModel;
   }

   public Vec3d resolveVec3d(Vec3d vec3d) {
      if (mc.world == null) {
         return vec3d;
      }

      Box box = this.getBoundingBox();
      List<VoxelShape> list = mc.world.getEntityCollisions(this, box.stretch(vec3d));
      Vec3d vec3d5 = vec3d.lengthSquared() == 0.0 ? vec3d : Entity.adjustMovementForCollisions(this, vec3d, box, mc.world, list);
      boolean flag = vec3d.x != vec3d5.x;
      boolean flag2 = vec3d.y != vec3d5.y;
      boolean flag3 = vec3d.z != vec3d5.z;
      boolean flag4 = this.isOnGround() || flag2 && vec3d.y < 0.0;
      if (this.getStepHeight() > 0.0F && flag4 && (flag || flag3)) {
         Vec3d vec3d3 = Entity.adjustMovementForCollisions(this, new Vec3d(vec3d.x, this.getStepHeight(), vec3d.z), box, mc.world, list);
         Vec3d vec3d4 = Entity.adjustMovementForCollisions(this, new Vec3d(0.0, this.getStepHeight(), 0.0), box.stretch(vec3d.x, 0.0, vec3d.z), mc.world, list);
         if (vec3d4.y < this.getStepHeight()) {
            Vec3d vec3d2 = Entity.adjustMovementForCollisions(this, new Vec3d(vec3d.x, 0.0, vec3d.z), box.offset(vec3d4), mc.world, list).add(vec3d4);
            if (vec3d2.horizontalLengthSquared() > vec3d3.horizontalLengthSquared()) {
               vec3d3 = vec3d2;
            }
         }

         if (vec3d3.horizontalLengthSquared() > vec3d5.horizontalLengthSquared()) {
            return vec3d3.add(Entity.adjustMovementForCollisions(this, new Vec3d(0.0, -vec3d3.y + vec3d.y, 0.0), box.offset(vec3d3), mc.world, list));
         }
      }

      return vec3d5;
   }

   public Function<Vec3d, Vec3d> getVec3dFunction() {
      return this.vec3dFunction;
   }

   public void run2() {
      Vec3d vec3d = this.getVelocity();
      double d = vec3d.x;
      double d2 = vec3d.y;
      double d3 = vec3d.z;
      if (Math.abs(vec3d.x) < 0.003) {
         d = 0.0;
      }

      if (Math.abs(vec3d.y) < 0.003) {
         d2 = 0.0;
      }

      if (Math.abs(vec3d.z) < 0.003) {
         d3 = 0.0;
      }

      this.setVelocity(d, d2, d3);
      if (this.hasStatusEffect(StatusEffects.SLOW_FALLING) || this.hasStatusEffect(StatusEffects.LEVITATION)) {
         this.onLanding();
      }

      this.sidewaysSpeed *= 0.98F;
      this.forwardSpeed *= 0.98F;
      Vec3d vec3d2 = new Vec3d(this.sidewaysSpeed, this.upwardSpeed, this.forwardSpeed);
      this.travel(vec3d2);
   }

   @Override
   public float getStepHeight() {
      return this.flag3
         ? 2.0F
         : (
            this.flag4 && ClientHolder.getPhobosClient().getMotionManager().isFlag42()
               ? ClientHolder.getPhobosClient().getMotionManager().getFloatValue6()
               : super.getStepHeight()
         );
   }

   @Override
   public void move(MovementType movementType, Vec3d movement) {
      movement = this.vec3dFunction.apply(movement);
      if (this.movementMultiplier.lengthSquared() > 1.0E-7) {
         movement = movement.multiply(this.movementMultiplier);
         this.movementMultiplier = Vec3d.ZERO;
         this.setVelocity(Vec3d.ZERO);
      }

      Vec3d vec3d2;
      double d;
      if ((d = (vec3d2 = this.resolveVec3d(movement = this.adjustMovementForSneaking(movement, movementType))).lengthSquared()) > 1.0E-7) {
         if (this.fallDistance != 0.0F
            && d >= 1.0
            && this.getWorld()
                  .raycast(new RaycastContext(this.getPos(), this.getPos().add(vec3d2), ShapeType.FALLDAMAGE_RESETTING, FluidHandling.WATER, this))
                  .getType()
               != Type.MISS) {
            this.onLanding();
         }

         this.setPosition(this.getX() + vec3d2.x, this.getY() + vec3d2.y, this.getZ() + vec3d2.z);
      }

      boolean flag = !MathHelper.approximatelyEquals(movement.x, vec3d2.x);
      boolean flag2 = !MathHelper.approximatelyEquals(movement.z, vec3d2.z);
      this.horizontalCollision = flag || flag2;
      this.verticalCollision = movement.y != vec3d2.y;
      this.groundCollision = this.verticalCollision && movement.y < 0.0;
      this.collidedSoftly = this.horizontalCollision && this.hasCollidedSoftly(vec3d2);
      this.setOnGround(this.groundCollision, vec3d2);
      BlockPos blockPos = this.getLandingPos();
      BlockState blockState = this.getWorld().getBlockState(blockPos);
      this.handleDouble(vec3d2.y, this.isOnGround());
      if (this.horizontalCollision) {
         Vec3d vec3d = this.getVelocity();
         this.setVelocity(flag ? 0.0 : vec3d.x, vec3d.y, flag2 ? 0.0 : vec3d.z);
      }

      Block block = blockState.getBlock();
      if (movement.y != vec3d2.y) {
         block.onEntityLand(this.getWorld(), this);
      }

      this.tryCheckBlockCollision();
      float f = this.getVelocityMultiplier();
      this.setVelocity(this.getVelocity().multiply(f, 1.0, f));
   }

   @Override
   public boolean isLogicalSideForUpdatingMovement() {
      return true;
   }

   public MovingFakePlayer resolveMovingFakePlayer(Entity entity) {
      this.copyFrom(entity);
      return this;
   }
}
