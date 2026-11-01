package org.phobos.mixin.game.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.gui.BaseScreen;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.player.ScaffoldFlags;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.data.StackRecord;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.TickMovementEvent;
import org.phobos.event.TickMovementEvent2;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.NoPushModule;
import org.phobos.module.misc.ParticlesModule;
import org.phobos.module.misc.PortalsModule;
import org.phobos.module.movement.SprintModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity
   extends AbstractClientPlayerEntity
   implements EventSubscriber,
   org.earthhack.ducks.IClientPlayerEntity,
   MinecraftHolder {
   @Shadow
   public boolean lastSprinting;
   @Shadow
   public boolean autoJumpEnabled;
   @Shadow
   public double lastX;
   @Unique
   private int intValue;
   @Shadow
   public double lastZ;
   @Shadow
   public boolean lastSneaking;
   @Shadow
   public boolean lastOnGround;
   @Shadow
   @Final
   public ClientPlayNetworkHandler networkHandler;
   @Shadow
   @Final
   public MinecraftClient client;
   @Shadow
   public float lastYaw;
   @Shadow
   public float lastPitch;
   @Shadow
   public int ticksSinceLastPositionPacketSent;
   @Shadow
   public Input input;
   @Shadow
   public double lastBaseY;
   @Unique
   private int groundTicks;

   @ModifyArg(
      method = "move",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
      ),
      index = 1
   )
   private Vec3d moveHook1(Vec3d vec) {
      return this.invoke(new MoveEvent2(vec, false)).getVec3d();
   }

   @Override
   public void invokeCrashInternal() {
      throw new RuntimeException("[Phobos] Force crash!");
   }

   @Override
   public int getIntValue() {
      return this.intValue;
   }

   @Inject(method = "closeScreen", at = @At("HEAD"))
   private void closeHook(CallbackInfo ci) {
      if (mc.currentScreen instanceof BaseScreen<?> themedScreen) {
         themedScreen.close();
      }
   }

   @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
   private boolean tickMovementHook(boolean original) {
      return this.invoke(new TickMovementEvent(original)).isFlag();
   }

   @Inject(method = "addEnchantedHitParticles", at = @At("HEAD"), cancellable = true)
   private void addEnchantedHitParticlesHook(Entity target, CallbackInfo ci) {
      ci.cancel();
      int multiplier = ParticlesModule.value();
      if (multiplier != 0) {
         for (int i = 0; i < multiplier; i++) {
            this.client.particleManager.addEmitter(target, ParticleTypes.ENCHANTED_HIT);
         }
      }
   }

   @ModifyExpressionValue(method = "canStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isWalking()Z"))
   private boolean canStartSprintingHook(boolean original) {
      if (!SprintModule.check()) {
         return original;
      }

      float forwards = Math.abs(this.input.movementSideways);
      float sideways = Math.abs(this.input.movementForward);
      return this.isSubmergedInWater() ? forwards > 1.0E-5F || sideways > 1.0E-5F : forwards > 0.8 || sideways > 0.8;
   }

   @Shadow
   public abstract boolean isCamera();

   public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
      super(world, profile);
      throw new IllegalStateException("MixinClientPlayerEntity constructor called!");
   }

   @Shadow
   public abstract void sendSprintingPacket();

   @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
   private void onPushOutOfBlocksHook(double x, double y, CallbackInfo ci) {
      if (NoPushModule.check2()) {
         ci.cancel();
      }
   }

   @Override
   public int getGroundTicks() {
      return this.groundTicks;
   }

   @Inject(method = "tick", at = @At("TAIL"))
   private void playerTickHook2(CallbackInfo ci) {
   }

   @Inject(method = "dropSelectedItem", at = @At("HEAD"))
   private void onDropSelectedItem(boolean all, CallbackInfoReturnable<Boolean> cir) {
      ItemStack stack = this.getInventory().dropSelectedItem(all);
      this.invoke(new StackRecord(stack));
   }

   @Inject(method = "move", at = @At("RETURN"))
   private void moveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
      if (this.isOnGround()) {
         this.groundTicks++;
         this.intValue = 0;
      } else {
         this.groundTicks = 0;
         this.intValue++;
      }
   }

   @ModifyExpressionValue(method = "tickNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;shouldPause()Z"))
   private boolean tickNauseaHook(boolean original) {
      return !PortalsModule.check() ? true : original;
   }

   @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
   private void canStartSprintingHook(CallbackInfoReturnable<Boolean> cir) {
      if (mc.player != null) {
         if (PlayerActionUtils.testClientPlayerEntity3(mc.player)) {
            cir.setReturnValue(false);
         }
      }
   }

   @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
   private void playerTickHook1(CallbackInfo ci) {
      this.invoke(new PlayerTickEvent(), ci);
   }

   @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
   private boolean modifyMovement(boolean original) {
      return !SprintModule.check() ? original : Math.abs(this.input.movementSideways) > 1.0E-5F || Math.abs(this.input.movementForward) > 1.0E-5F;
   }

   @Shadow
   @Override
   public abstract boolean isSneaking();

   @Inject(method = "addCritParticles", at = @At("HEAD"), cancellable = true)
   private void addCritParticlesHook(Entity target, CallbackInfo ci) {
      ci.cancel();
      int multiplier = ParticlesModule.value();
      if (multiplier != 0) {
         for (int i = 0; i < multiplier; i++) {
            this.client.particleManager.addEmitter(target, ParticleTypes.CRIT);
         }
      }
   }

   @Override
   public void superTick() {
      super.tick();
   }

   @Shadow
   @Override
   public abstract float getYaw(float var1);

   @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasVehicle()Z", ordinal = 1))
   private boolean tickMovementHook(ClientPlayerEntity player) {
      TickMovementEvent2 event = this.invoke(new TickMovementEvent2());
      return event.isCancelled() ? true : player.hasVehicle();
   }

   @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
   private void sendMovementPackets(CallbackInfo ci) {
      ci.cancel();
      PlayerMoveEvent event = this.invoke(new PlayerMoveEvent(this.getX(), this.getY(), this.getZ(), this.isOnGround(), this.getYaw(), this.getPitch()));
      if (!event.isCancelled()) {
         ScaffoldFlags action = this.invoke(new ScaffoldFlags(this.isSprinting(), this.isSneaking()));
         boolean sprinting = action.isFlag();
         if (sprinting != this.lastSprinting) {
            Mode mode = sprinting ? Mode.START_SPRINTING : Mode.STOP_SPRINTING;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
            this.lastSprinting = sprinting;
         }

         boolean sneaking = action.isFlag2();
         if (sneaking != this.lastSneaking) {
            Mode mode = sneaking ? Mode.PRESS_SHIFT_KEY : Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
            this.lastSneaking = sneaking;
         }

         if (this.isCamera()) {
            float yaw = event.getFloatValue();
            float pitch = event.getFloatValue2();
            double x = event.getDoubleValue2();
            double y = event.getDoubleValue3();
            double z = event.getDoubleValue();
            boolean onGround = event.isFlag();
            float packetYaw = DisablerModule.check2() ? yaw + 3.6E7F : yaw;
            double d = x - this.lastX;
            double e = y - this.lastBaseY;
            double f = z - this.lastZ;
            double g = yaw - this.lastYaw;
            double h = pitch - this.lastPitch;
            this.ticksSinceLastPositionPacketSent++;
            boolean bl2 = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl3 = g != 0.0 || h != 0.0;
            if (this.hasVehicle()) {
               Vec3d vec3d = this.getVelocity();
               this.networkHandler.sendPacket(new Full(vec3d.x, -999.0, vec3d.z, packetYaw, pitch, onGround));
               bl2 = false;
            } else if (bl2 && bl3) {
               this.networkHandler.sendPacket(new Full(x, y, z, packetYaw, pitch, onGround));
            } else if (bl2) {
               this.networkHandler.sendPacket(new PositionAndOnGround(x, y, z, onGround));
            } else if (bl3) {
               this.networkHandler.sendPacket(new LookAndOnGround(packetYaw, pitch, onGround));
            } else if (this.lastOnGround != this.isOnGround()) {
               this.networkHandler.sendPacket(new OnGroundOnly(onGround));
            }

            if (bl2) {
               this.lastX = x;
               this.lastBaseY = y;
               this.lastZ = z;
               this.ticksSinceLastPositionPacketSent = 0;
            }

            if (bl3) {
               this.lastYaw = yaw;
               this.lastPitch = pitch;
            }

            this.lastOnGround = onGround;
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
         }

         this.invoke(new UnusedEvent());
      }
   }
}
