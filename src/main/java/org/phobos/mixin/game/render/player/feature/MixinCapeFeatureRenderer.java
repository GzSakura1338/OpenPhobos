package org.phobos.mixin.game.render.player.feature;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.earthhack.ducks.ILivingEntity;
import org.earthhack.mixin.EventSubscriber;
import org.joml.Quaternionf;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.player.SneakAnimationState;
import org.phobos.core.render.EntityRenderLayers;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.ChamsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeFeatureRenderer.class)
public abstract class MixinCapeFeatureRenderer
   extends MixinFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
   implements MinecraftHolder,
   EventSubscriber {
   @Inject(
      method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderHook(
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i,
      AbstractClientPlayerEntity player,
      float f,
      float g,
      float h,
      float j,
      float k,
      float l,
      CallbackInfo ci
   ) {
      ci.cancel();
      if (!player.isInvisible() && player.isPartVisible(PlayerModelPart.CAPE)) {
         SkinTextures skinTextures = player.getSkinTextures();
         if (skinTextures.capeTexture() != null) {
            ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
            if (!itemStack.isOf(Items.ELYTRA)) {
               ILivingEntity accessor = (ILivingEntity)(Object)player;
               boolean sneaking = player.isInSneakingPose();
               accessor.set$sneaking(sneaking);
               if (AnimationsModule.check()) {
                  accessor.getFloatAnimator().setFloatValue(sneaking ? 25.0F : 0.0F);
                  accessor.getFloatAnimator().handleFloat3(50.0F, 0.08F);
               }

               matrixStack.push();
               matrixStack.translate(0.0F, 0.0F, 0.125F);
               double d = MathHelper.lerp(h, player.prevCapeX, player.capeX) - MathHelper.lerp(h, player.prevX, player.getX());
               double e = MathHelper.lerp(h, player.prevCapeY, player.capeY) - MathHelper.lerp(h, player.prevY, player.getY());
               double m = MathHelper.lerp(h, player.prevCapeZ, player.capeZ) - MathHelper.lerp(h, player.prevZ, player.getZ());
               float n = MathHelper.lerpAngleDegrees(h, player.prevBodyYaw, player.bodyYaw);
               double o = MathHelper.sin(n * (float) (Math.PI / 180.0));
               double p = -MathHelper.cos(n * (float) (Math.PI / 180.0));
               float q = (float)e * 10.0F;
               q = MathHelper.clamp(q, -6.0F, 32.0F);
               float r = (float)(d * o + m * p) * 100.0F;
               r = MathHelper.clamp(r, 0.0F, 150.0F);
               float s = (float)(d * p - m * o) * 100.0F;
               s = MathHelper.clamp(s, -20.0F, 20.0F);
               if (r < 0.0F) {
                  r = 0.0F;
               }

               float t = MathHelper.lerp(h, player.prevStrideDistance, player.strideDistance);
               q += MathHelper.sin(MathHelper.lerp(h, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * t;
               if (player.isInSneakingPose()) {
                  q += 25.0F;
               }

               matrixStack.multiply(this.getRotation(player, RotationAxis.POSITIVE_X, 6.0F + r / 2.0F + q));
               matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(s / 2.0F));
               matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - s / 2.0F));
               VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.getRenderLayer(player, skinTextures));
               this.getContextModel().renderCape(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
               matrixStack.pop();
            }
         }
      }
   }

   @Unique
   private RenderLayer getRenderLayer(AbstractClientPlayerEntity player, SkinTextures skinTextures) {
      return ChamsModule.testLivingEntity(player)
         ? EntityRenderLayers.resolveRenderLayer3(skinTextures.capeTexture())
         : RenderLayer.getEntitySolid(skinTextures.capeTexture());
   }

   @Unique
   private Quaternionf getRotation(AbstractClientPlayerEntity player, RotationAxis instance, float deg) {
      SneakAnimationState event = this.invoke(new SneakAnimationState());
      if (event.getSneakAnimation() == null) {
         return instance.rotationDegrees(deg);
      }

      ILivingEntity accessor = (ILivingEntity)(Object)player;
      float f0 = deg - (accessor.is$sneaking() ? 25.0F : 0.0F);

      return switch (event.getSneakAnimation()) {
         case Lerp -> instance.rotationDegrees(deg);
         case Custom -> instance.rotationDegrees(f0 + accessor.getFloatAnimator().getFloatValue2());
      };
   }
}
