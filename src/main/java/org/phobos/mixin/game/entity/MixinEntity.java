package org.phobos.mixin.game.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.earthhack.ducks.IEntity;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.event.IsGlowingEvent;
import org.phobos.event.MoveEvent;
import org.phobos.event.UpdateVelocityEvent;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.movement.NoPushModule;
import org.phobos.module.movement.NoSlowModule;
import org.phobos.module.movement.PhaseModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements MinecraftHolder, EventSubscriber, IEntity {
   @Unique
   private boolean painted;
   @Unique
   private boolean clientSided;
   @Unique
   private final Map<String, Object> properties = new HashMap<>();
   @Unique
   private EntityPose serverPose;
   @Shadow
   private World world;
   @Unique
   private volatile long attackTime;

   @Shadow
   public abstract Vec3d getVelocity();

   @Shadow
   public abstract float getYaw();

   @Shadow
   public abstract void setPitch(float var1);

   @Shadow
   public abstract BlockPos getVelocityAffectingPos();

   @Shadow
   public abstract void setYaw(float var1);

   @Shadow
   public abstract float getPitch();

   @Shadow
   public abstract double getY();

   @Shadow
   private static List<VoxelShape> findCollisionsForMovement(
      @Nullable Entity entity, World world, List<VoxelShape> regularCollisions, Box movingEntityBoundingBox
   ) {
      return null;
   }

   @Shadow
   public abstract float getStepHeight();

   @Shadow
   public abstract double getFinalGravity();

   @Shadow
   public abstract void setVelocity(Vec3d var1);

   @Shadow
   public abstract void setPosition(double var1, double var3, double var5);

   @Shadow
   public abstract void setVelocity(double var1, double var3, double var5);

   @Shadow
   public static Vec3d adjustMovementForCollisions(@Nullable Entity entity, Vec3d movement, Box entityBoundingBox, World world, List<VoxelShape> collisions) {
      return null;
   }

   @Shadow
   public abstract Box getBoundingBox();

   @Shadow
   public abstract void move(MovementType var1, Vec3d var2);

   @Shadow
   public abstract World getWorld();

   @Shadow
   private static float[] collectStepHeights(Box collisionBox, List<VoxelShape> collisions, float f, float stepHeight) {
      return null;
   }

   @Shadow
   public abstract boolean isOnGround();

   @Shadow
   public abstract boolean isSprinting();

   @Shadow
   protected abstract Vec3d adjustMovementForSneaking(Vec3d var1, MovementType var2);

   @Shadow
   private static Vec3d adjustMovementForCollisions(Vec3d movement, Box entityBoundingBox, List<VoxelShape> collisions) {
      return null;
   }

   @Override
   public void setServerPose(EntityPose pose) {
      this.serverPose = pose;
   }

   @Override
   public boolean isPainted() {
      return this.painted;
   }

   @Unique
   private Vec3d createVec3d(Vec3d input, float speed, float yaw) {
      double d = input.lengthSquared();
      if (d < AntiCheatModule.Client.computeDouble2(1.0E-7)) {
         return Vec3d.ZERO;
      }

      Vec3d vec = (d > 1.0 ? input.normalize() : input).multiply(speed);
      float sin = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
      float cos = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
      return new Vec3d(vec.x * cos - vec.z * sin, vec.y, vec.z * cos + vec.x * sin);
   }

   @Nullable
   @Override
   public EntityPose getServerPose() {
      return this.serverPose;
   }

   @Override
   public void setClientSide(boolean clientSide) {
      this.clientSided = clientSide;
   }

   @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
   private void adjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
      Box box = this.getBoundingBox();
      List<VoxelShape> list = this.getWorld().getEntityCollisions((Entity)(Object)this, box.stretch(movement));
      Vec3d vec3d = movement.lengthSquared() == 0.0 ? movement : adjustMovementForCollisions((Entity)(Object)this, movement, box, this.getWorld(), list);
      boolean bl = movement.x != vec3d.x;
      boolean bl2 = movement.y != vec3d.y;
      boolean bl3 = movement.z != vec3d.z;
      boolean bl4 = bl2 && movement.y < 0.0;
      if (this.getStepHeight() > 0.0F && (bl4 || this.isOnGround()) && (bl || bl3)) {
         Box box2 = bl4 ? box.offset(0.0, vec3d.y, 0.0) : box;
         Box box3 = box2.stretch(movement.x, this.getStepHeight(), movement.z);
         if (!bl4) {
            box3 = box3.stretch(0.0, AntiCheatModule.Client.computeDouble(-1.0E-5F), 0.0);
         }

         List<VoxelShape> list2 = findCollisionsForMovement((Entity)(Object)this, this.world, list, box3);
         float f = (float)vec3d.y;
         float[] fs = collectStepHeights(box2, list2, this.getStepHeight(), f);
         float[] var14 = fs;
         int var15 = fs.length;

         for (int var16 = 0; var16 < var15; var16++) {
            float g = var14[var16];
            Vec3d vec3d2 = adjustMovementForCollisions(new Vec3d(movement.x, g, movement.z), box2, list2);
            if (vec3d2.horizontalLengthSquared() > vec3d.horizontalLengthSquared()) {
               double d = box.minY - box2.minY;
               cir.setReturnValue(vec3d2.add(0.0, -d, 0.0));
               return;
            }
         }
      }

      cir.setReturnValue(vec3d);
   }

   @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;playExtinguishSound()V"))
   private void moveHook(Entity instance) {
   }

   @Override
   public void setPainted(boolean value) {
      this.painted = value;
   }

   @Redirect(
      method = "move",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"
      )
   )
   private Vec3d moveHook(Entity instance, Vec3d movement, MovementType type) {
      Vec3d sneaking = this.adjustMovementForSneaking(movement, type);
      if (instance instanceof ClientPlayerEntity) {
         MoveEvent event = this.invoke(new MoveEvent(sneaking));
         return event.getVec3d();
      } else {
         return sneaking;
      }
   }

   @Inject(method = "isCrawling", at = @At("HEAD"), cancellable = true)
   private void isCrawlingHook(CallbackInfoReturnable<Boolean> cir) {
      if ((Object)this instanceof ClientPlayerEntity) {
         if (NoSlowModule.getNoSlowModule().check7() && NoSlowModule.getNoSlowModule().getBooleanValueSetting().getValue()) {
            cir.setReturnValue(false);
         }
      }
   }

   @Inject(method = "getPose", at = @At("RETURN"), cancellable = true)
   private void getPoseHook(CallbackInfoReturnable<EntityPose> cir) {
      if (mc.player != null) {
         if ((Object)this == mc.player) {
            if (AntiCheatModule.getAntiCheatModule().getLegacyPoses().getValue()
               && cir.getReturnValue() == EntityPose.SWIMMING
               && mc.player != null
               && mc.player.input != null) {
               cir.setReturnValue(mc.player.input.sneaking ? EntityPose.CROUCHING : EntityPose.STANDING);
               return;
            }

            if (PhaseModule.check()
               && mc.player != null
               && mc.world != null
               && BlockUtils.testPlayerEntity(mc.player, mc.world)
               && cir.getReturnValue() != EntityPose.SWIMMING) {
               cir.setReturnValue(EntityPose.STANDING);
               return;
            }

            if (PlayerActionUtils.check()) {
               cir.setReturnValue(EntityPose.STANDING);
            }
         }
      }
   }

   @Override
   public boolean isClientSide() {
      return this.clientSided;
   }

   @Override
   public void setAttackTime(long attackTime) {
      this.attackTime = attackTime;
   }

   @Override
   public Object get(String name) {
      return this.properties.get(name);
   }

   @Inject(method = "pushAwayFrom", at = @At(value = "INVOKE", target = "Ljava/lang/Math;sqrt(D)D", shift = Shift.BEFORE), cancellable = true)
   private void pushAwayFromHook(Entity entity, CallbackInfo ci) {
      if (Objects.equals(this, mc.player) || Objects.equals(entity, mc.player)) {
         if (NoPushModule.check2()) {
            ci.cancel();
         }
      }
   }

   @Override
   public void put(String name, Object value) {
      this.properties.put(name, value);
   }

   @Override
   public long getAttackTime() {
      return this.attackTime;
   }

   @Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
   private void isGlowingHook(CallbackInfoReturnable<Boolean> cir) {
      IsGlowingEvent event = this.invoke(new IsGlowingEvent((Entity)(Object)this, (Boolean)cir.getReturnValue()));
      if (this.painted) {
         cir.setReturnValue(false);
         this.painted = false;
      } else {
         cir.setReturnValue(event.isFlag());
      }
   }

   @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
   private void updateVelocityHook(float speed, Vec3d input, CallbackInfo ci) {
      if (mc.player != null) {
         if ((Object)this == mc.player) {
            ci.cancel();
            UpdateVelocityEvent event = this.invoke(
               new UpdateVelocityEvent(input, speed, mc.player.getYaw(), this.createVec3d(input, speed, mc.player.getYaw()))
            );
            mc.player.setVelocity(mc.player.getVelocity().add(event.getVec3d()));
         }
      }
   }

   @Inject(method = "movementInputToVelocity", at = @At("HEAD"), cancellable = true)
   private static void movementInputToVelocity(Vec3d movementInput, float speed, float yaw, CallbackInfoReturnable<Vec3d> cir) {
      double d = movementInput.lengthSquared();
      if (d < AntiCheatModule.Client.computeDouble2(1.0E-7)) {
         cir.setReturnValue(Vec3d.ZERO);
      } else {
         Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
         float f = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
         float g = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
         cir.setReturnValue(new Vec3d(vec3d.x * g - vec3d.z * f, vec3d.y, vec3d.z * g + vec3d.x * f));
      }
   }
}
