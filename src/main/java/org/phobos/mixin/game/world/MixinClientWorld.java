package org.phobos.mixin.game.world;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeParticleConfig;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.PlaySoundEvent;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.module.render.AmbienceModule;
import org.phobos.module.render.AmbienceModule;
import org.phobos.module.misc.WeatherModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends MixinWorld implements EventSubscriber {
   @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
   private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
      if (AmbienceModule.getAmbienceModule().check7()
         && AmbienceModule.getAmbienceModule().getSky().getValue()
         && AmbienceModule.getAmbienceModule().getSkyMode().getValue() == AmbienceModule.SkyMode.Color) {
         Color skyColor = AmbienceModule.getAmbienceModule().getSkyColor().getValue();
         cir.setReturnValue(ColorUtils.createVec3d(skyColor));
      }
   }

   @Shadow
   public abstract void addParticle(ParticleEffect var1, double var2, double var4, double var6, double var8, double var10, double var12);

   @Inject(method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V", at = @At("HEAD"), cancellable = true)
   private void playSoundHook(
      double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch, boolean useDistance, long seed, CallbackInfo ci
   ) {
      this.invoke(new PlaySoundEvent(x, y, z, event), ci);
   }

   @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
   private void addEntityHook(Entity entity, CallbackInfo ci) {
      if (entity == null) {
         ci.cancel();
      } else if (!(entity instanceof ItemEntity)) {
         this.invoke(new AddEntityEvent(entity), ci);
      }
   }

   @Shadow
   @Nullable
   public abstract Entity getEntityById(int var1);

   @Inject(
      method = "randomBlockDisplayTick",
      at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = Shift.AFTER)
   )
   private void randomBlockDisplayTickHook(int centerX, int centerY, int centerZ, int radius, Random random, Block block, Mutable pos, CallbackInfo ci) {
      WorldView view = (WorldView)(Object)this;
      if (!((Biome)view.getBiome(pos).value()).getParticleConfig().isPresent()) {
         BiomeParticleConfig config = WeatherModule.getBiomeParticleConfig();
         if (config != null && config.shouldAddParticle(random)) {
            WeatherModule.setFlag(true);
            this.addParticle(
               config.getParticle(), pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0
            );
         }
      }
   }

   @Inject(method = "removeEntity", at = @At("HEAD"), cancellable = true)
   private void removeEntityHook(int entityId, RemovalReason removalReason, CallbackInfo ci) {
      Entity entity = this.getEntityById(entityId);
      if (entity != null) {
         if (entity instanceof ItemEntity) {
            return;
         }

         this.invoke(new RemoveEntityEvent(entity, removalReason), ci);
      }
   }
}
