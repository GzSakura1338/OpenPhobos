package org.phobos.mixin.game.render.player.feature;

import com.llamalad7.mixinextras.sugar.Local;
import java.awt.Color;
import java.util.function.Function;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderLayer.MultiPhase;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase.Texture;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.earthhack.ducks.ILivingEntity;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.render.PlayerRenderState;
import org.phobos.core.render.ColorUtils;
import org.phobos.module.render.ChamsModule;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class MixinArmorFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>>
   implements EventSubscriber {
   @Unique
   private static final Function<Identifier, RenderLayer> ARMOR_CUTOUT_NO_CULL_TRANSPARENT = Util.memoize(
      texture -> createArmorCutoutNoCullTransparent("armor_cutout_no_cull", texture, false)
   );

   @Redirect(
      method = "renderArmor",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;ILnet/minecraft/util/Identifier;)V"
      )
   )
   private void renderArmorPartsHook(
      ArmorFeatureRenderer<T, M, A> instance,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      A model,
      int rgba,
      Identifier identifier,
      @Local(argsOnly = true) T entity
   ) {
      Color col = new Color(rgba, false);
      PlayerRenderState ghost = ((ILivingEntity)(Object)entity).getGhostModel();
      Color color = ghost != null ? ColorUtils.createColor6(col, ghost.getFloatValue2()) : ChamsModule.resolveColor(col);
      if (color.getAlpha() < 255) {
         this.renderArmorPartsTransparent(matrices, vertexConsumers, light, model, color.hashCode(), identifier);
      } else {
         this.renderArmorParts(matrices, vertexConsumers, light, model, color.hashCode(), identifier);
      }
   }

   @Unique
   private void renderArmorPartsTransparent(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, A model, int i, Identifier identifier) {
      VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getArmorCutoutNoCullTransparent(identifier));
      model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, i);
   }

   @Unique
   private static RenderLayer getArmorCutoutNoCullTransparent(Identifier texture) {
      return ARMOR_CUTOUT_NO_CULL_TRANSPARENT.apply(texture);
   }

   @Unique
   private static MultiPhase createArmorCutoutNoCullTransparent(String name, Identifier texture, boolean decal) {
      MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
         .program(RenderPhase.ARMOR_CUTOUT_NO_CULL_PROGRAM)
         .texture(new Texture(texture, false, false))
         .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
         .cull(RenderLayer.DISABLE_CULLING)
         .lightmap(RenderPhase.ENABLE_LIGHTMAP)
         .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
         .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
         .depthTest(decal ? RenderPhase.EQUAL_DEPTH_TEST : RenderPhase.LEQUAL_DEPTH_TEST)
         .build(true);
      return RenderLayer.of(name, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
   }

   @Shadow
   protected abstract void renderArmorParts(MatrixStack var1, VertexConsumerProvider var2, int var3, A var4, int var5, Identifier var6);

   @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
   private void renderArmorHook(
      MatrixStack matrices, VertexConsumerProvider vertexConsumers, T livingEntity, EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo ci
   ) {
      this.invoke(new NoRenderModule.Armor(), ci);
   }
}
