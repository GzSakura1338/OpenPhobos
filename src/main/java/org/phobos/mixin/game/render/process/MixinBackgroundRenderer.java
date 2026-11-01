package org.phobos.mixin.game.render.process;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.Render3d;
import org.phobos.event.ApplyFogEvent;
import org.phobos.module.render.AmbienceModule;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
   @Shadow
   private static float red;
   @Shadow
   private static float green;
   @Shadow
   private static float blue;

   @Unique
   private static <T> T post(T obj) {
      ClientHolder.getEventBus().handleObject2(obj);
      return obj;
   }

   @Inject(
      method = "getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;",
      at = @At("HEAD"),
      cancellable = true
   )
   private static void getFogModifierHook(Entity entity, float tickDelta, CallbackInfoReturnable<Object> cir) {
      NoRenderModule.Blindness event = post(new NoRenderModule.Blindness());
      if (event.isCancelled()) {
         cir.setReturnValue(null);
      }
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", shift = Shift.BEFORE))
   private static void renderHook(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
      if (AmbienceModule.getAmbienceModule().check7() && AmbienceModule.getAmbienceModule().getBackground().getValue()) {
         Color color = AmbienceModule.getAmbienceModule().getBackgroundColor().getValue();
         red = color.getRed() / 255.0F;
         green = color.getGreen() / 255.0F;
         blue = color.getBlue() / 255.0F;
      }
   }

   @Inject(method = "applyFog", at = @At("TAIL"))
   private static void applyFogHook(Camera camera, FogType type, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
      Render3d.floatValue = RenderSystem.getShaderFogStart();
      Render3d.floatValue3 = RenderSystem.getShaderFogEnd();
      Render3d.fogShape = RenderSystem.getShaderFogShape();
      post(new ApplyFogEvent(type, viewDistance, tickDelta, thickFog));
   }
}
