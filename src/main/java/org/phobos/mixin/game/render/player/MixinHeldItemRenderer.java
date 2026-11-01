package org.phobos.mixin.game.render.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.earthhack.mixin.EventSubscriber;
import org.joml.Quaternionf;
import org.phobos.core.event.events.HeldItemRenderEvent;
import org.phobos.module.player.LegacyModule;
import org.phobos.event.ApplyEatOrDrinkTransformationEvent;
import org.phobos.event.RenderArmEvent2;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.render.ViewModelModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements EventSubscriber {
   @Shadow
   private float equipProgressMainHand;
   @Shadow
   private ItemStack offHand;

   @Redirect(
      method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V")
   )
   private void renderItemHook(MatrixStack instance, Quaternionf quaternion) {
      if (!ViewModelModule.getViewModelModule().check7() || !ViewModelModule.getViewModelModule().noSway.getValue()) {
         instance.multiply(quaternion);
      }
   }

   @Shadow
   protected abstract void applyEquipOffset(MatrixStack var1, Arm var2, float var3);

   @Shadow
   protected abstract void applyEatOrDrinkTransformation(MatrixStack var1, float var2, Arm var3, ItemStack var4, PlayerEntity var5);

   @Inject(
      method = "updateHeldItems",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;equipProgressMainHand:F", opcode = 181, shift = Shift.AFTER)
   )
   private void updateHeldItemsHook(CallbackInfo ci) {
      if (SilentHotbarModule.getSilentHotbarModule().check7()) {
         SilentHotbarModule.getSilentHotbarManager().run2();
         if (SilentHotbarModule.getSilentHotbarManager().check()) {
            this.equipProgressMainHand = 1.0F;
         }
      }
   }

   @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
   private void renderFirstPersonItemHook(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      ci.cancel();
      this.__renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
   }

   @Shadow
   protected abstract void renderFirstPersonItem(
      AbstractClientPlayerEntity var1,
      float var2,
      float var3,
      Hand var4,
      float var5,
      ItemStack var6,
      float var7,
      MatrixStack var8,
      VertexConsumerProvider var9,
      int var10
   );

   @Inject(
      method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
      at = @At("TAIL")
   )
   private void renderHook(float tickDelta, MatrixStack matrices, Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   @ModifyExpressionValue(
      method = "updateHeldItems",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;")
   )
   private ItemStack updateHeldItemsHook(ItemStack original) {
      return SilentHotbarModule.resolveItemStack2(original);
   }

   @Shadow
   protected abstract void applySwingOffset(MatrixStack var1, Arm var2, float var3);

   @Redirect(
      method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
      )
   )
   private void renderItemHook(
      HeldItemRenderer instance,
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light
   ) {
      this.renderFirstPersonItem(
         player, tickDelta, pitch, hand, swingProgress + ViewModelModule.computeHand(hand), item, equipProgress, matrices, vertexConsumers, light
      );
   }

   @Inject(method = "renderArmHoldingItem", at = @At("HEAD"), cancellable = true)
   private void renderArmHook1(
      MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm, CallbackInfo ci
   ) {
      this.invoke(new RenderArmEvent2(matrices, arm), ci);
   }

   @Shadow
   protected abstract void renderMapInOneHand(MatrixStack var1, VertexConsumerProvider var2, int var3, float var4, Arm var5, float var6, ItemStack var7);

   @Shadow
   protected abstract void applyBrushTransformation(MatrixStack var1, float var2, Arm var3, ItemStack var4, PlayerEntity var5, float var6);

   @Unique
   private void __renderFirstPersonItem(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light
   ) {
      if (!player.isUsingSpyglass()) {
         boolean bl = hand == Hand.MAIN_HAND;
         Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
         matrices.push();
         if (item.isEmpty()) {
            if (bl && !player.isInvisible()) {
               this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            }
         } else if (item.isOf(Items.FILLED_MAP)) {
            if (bl && this.offHand.isEmpty()) {
               this.renderMapInBothHands(matrices, vertexConsumers, light, pitch, equipProgress, swingProgress);
            } else {
               this.renderMapInOneHand(matrices, vertexConsumers, light, equipProgress, arm, swingProgress, item);
            }
         } else if (item.isOf(Items.CROSSBOW)) {
            boolean bl2 = CrossbowItem.isCharged(item);
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               if (AnimationsModule.check3()) {
                  this.applySwingOffset(matrices, arm, swingProgress);
               }

               matrices.translate(i * -0.4785682F, -0.094387F, 0.05731531F);
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * 65.3F));
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * -9.785F));
               float f = item.getMaxUseTime(player) - (player.getItemUseTimeLeft() - tickDelta + 1.0F);
               float g = f / CrossbowItem.getPullTime(item, player);
               if (g > 1.0F) {
                  g = 1.0F;
               }

               if (g > 0.1F) {
                  float h = MathHelper.sin((f - 0.1F) * 1.3F);
                  float j = g - 0.1F;
                  float k = h * j;
                  matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
               }

               matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
               matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
               matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(i * 45.0F));
            } else {
               float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float g = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float h = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               matrices.translate(i * f, g, h);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
               if (bl2 && swingProgress < 0.001F && bl) {
                  matrices.translate(i * -0.641864F, 0.0F, 0.0F);
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * 10.0F));
               }
            }

            HeldItemRenderEvent event = this.invoke(new HeldItemRenderEvent(hand, item, equipProgress, matrices));
            if (!event.isCancelled()) {
               this.renderItem(
                  player,
                  item,
                  bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                  !bl3,
                  matrices,
                  vertexConsumers,
                  light
               );
            }
         } else {
            boolean bl2 = arm == Arm.RIGHT;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
               int l = bl2 ? 1 : -1;
               switch (item.getUseAction()) {
                  case NONE:
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     break;
                  case EAT:
                  case DRINK:
                     this.applyEatOrDrinkTransformation(matrices, tickDelta, arm, item, player);
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     if (AnimationsModule.check3()) {
                        this.applySwingOffset(matrices, arm, swingProgress);
                     }
                     break;
                  case BLOCK:
                     if (!LegacyModule.testMatrixStack(matrices, arm, swingProgress, equipProgress)) {
                        this.applyEquipOffset(matrices, arm, equipProgress);
                     }
                     break;
                  case BOW: {
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     if (AnimationsModule.check3()) {
                        this.applySwingOffset(matrices, arm, swingProgress);
                     }

                     matrices.translate(l * -0.2785682F, 0.18344387F, 0.15731531F);
                     matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-13.935F));
                     matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 35.3F));
                     matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -9.785F));
                     float m = item.getMaxUseTime(player) - (player.getItemUseTimeLeft() - tickDelta + 1.0F);
                     float f = m / 20.0F;
                     f = (f * f + f * 2.0F) / 3.0F;
                     if (f > 1.0F) {
                        f = 1.0F;
                     }

                     if (f > 0.1F) {
                        float g = MathHelper.sin((m - 0.1F) * 1.3F);
                        float h = f - 0.1F;
                        float j = g * h;
                        matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                     }

                     matrices.translate(f * 0.0F, f * 0.0F, f * 0.04F);
                     matrices.scale(1.0F, 1.0F, 1.0F + f * 0.2F);
                     matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(l * 45.0F));
                     break;
                  }
                  case SPEAR:
                     this.applyEquipOffset(matrices, arm, equipProgress);
                     matrices.translate(l * -0.5F, 0.7F, 0.1F);
                     matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-55.0F));
                     matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 35.3F));
                     matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -9.785F));
                     float m = item.getMaxUseTime(player) - (player.getItemUseTimeLeft() - tickDelta + 1.0F);
                     float f = m / 10.0F;
                     if (f > 1.0F) {
                        f = 1.0F;
                     }

                     if (f > 0.1F) {
                        float g = MathHelper.sin((m - 0.1F) * 1.3F);
                        float h = f - 0.1F;
                        float j = g * h;
                        matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                     }

                     matrices.translate(0.0F, 0.0F, f * 0.2F);
                     matrices.scale(1.0F, 1.0F, 1.0F + f * 0.2F);
                     matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(l * 45.0F));
                     break;
                  case BRUSH:
                     this.applyBrushTransformation(matrices, tickDelta, arm, item, player, equipProgress);
               }
            } else if (player.isUsingRiptide()) {
               this.applyEquipOffset(matrices, arm, equipProgress);
               int l = bl2 ? 1 : -1;
               matrices.translate(l * -0.4F, 0.8F, 0.3F);
               matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 65.0F));
               matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -85.0F));
            } else if (!LegacyModule.testMatrixStack(matrices, arm, swingProgress, equipProgress)) {
               float n = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
               float m = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
               float f = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
               int o = bl2 ? 1 : -1;
               matrices.translate(o * n, m, f);
               this.applyEquipOffset(matrices, arm, equipProgress);
               this.applySwingOffset(matrices, arm, swingProgress);
            }

            HeldItemRenderEvent event = this.invoke(new HeldItemRenderEvent(hand, item, equipProgress, matrices));
            if (!event.isCancelled()) {
               this.renderItem(
                  player,
                  item,
                  bl2 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                  !bl2,
                  matrices,
                  vertexConsumers,
                  light
               );
            }
         }

         matrices.pop();
      }
   }

   @Shadow
   protected abstract void renderMapInBothHands(MatrixStack var1, VertexConsumerProvider var2, int var3, float var4, float var5, float var6);

   @Shadow
   public abstract void renderItem(
      LivingEntity var1, ItemStack var2, ModelTransformationMode var3, boolean var4, MatrixStack var5, VertexConsumerProvider var6, int var7
   );

   @Shadow
   protected abstract void renderArmHoldingItem(MatrixStack var1, VertexConsumerProvider var2, int var3, float var4, float var5, Arm var6);

   @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
   private void applyEatOrDrinkTransformationHook(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, PlayerEntity player, CallbackInfo ci) {
      this.invoke(new ApplyEatOrDrinkTransformationEvent(matrices, tickDelta, arm, stack), ci);
   }
}
