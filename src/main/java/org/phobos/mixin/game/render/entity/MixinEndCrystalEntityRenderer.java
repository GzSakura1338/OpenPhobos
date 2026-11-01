package org.phobos.mixin.game.render.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.earthhack.mixin.EventSubscriber;
import org.joml.Quaternionf;
import org.phobos.core.event.events.CrystalRenderEvent;
import org.phobos.core.render.EntityRenderLayers;
import org.phobos.core.render.Render3d;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.ChamsModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class MixinEndCrystalEntityRenderer implements EventSubscriber {
   @Shadow
   @Final
   public static RenderLayer END_CRYSTAL;
   @Shadow
   @Final
   private ModelPart core;
   @Unique
   private static final RenderLayer END_CRYSTAL_SEE_THROUGH = EntityRenderLayers.resolveRenderLayer(MixinEndCrystalEntityRenderer.TEXTURE);
   @Shadow
   @Final
   private static Identifier TEXTURE;
   @Shadow
   @Final
   private ModelPart bottom;
   @Shadow
   @Final
   public static float SINE_45_DEGREES;
   @Shadow
   @Final
   private ModelPart frame;
   @Unique
   private static final RenderLayer END_CRYSTAL_TRANSPARENT = EntityRenderLayers.resolveRenderLayer2(TEXTURE);

   @Inject(
      method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at = @At("HEAD"),
      cancellable = true
   )
   public void render(
      EndCrystalEntity endCrystalEntity,
      float yaw,
      float tickDelta,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i,
      CallbackInfo ci
   ) {
      ci.cancel();
      CrystalRenderEvent event = new CrystalRenderEvent(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, i, this.core, this.frame);
      ChamsModule.handleCrystalRenderEvent(event);
      if (!event.isCancelled()) {
         matrixStack.push();
         float h = Render3d.computeEndCrystalEntity(endCrystalEntity, tickDelta);
         float j = AnimationsModule.computeFloat((endCrystalEntity.endCrystalAge + tickDelta) * 3.0F);
         VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(
            ChamsModule.check2() ? END_CRYSTAL_SEE_THROUGH : (ChamsModule.check3() ? END_CRYSTAL_TRANSPARENT : END_CRYSTAL)
         );
         matrixStack.push();
         float scale = AnimationsModule.value();
         matrixStack.scale(scale, scale, scale);
         matrixStack.translate(0.0F, -0.5F, 0.0F);
         int k = OverlayTexture.DEFAULT_UV;
         if (endCrystalEntity.shouldShowBottom()) {
            this.bottom.render(matrixStack, vertexConsumer, i, k, ChamsModule.getColor2().hashCode());
         }

         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
         matrixStack.translate(0.0F, 1.5F + h / 2.0F, 0.0F);
         matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         this.frame.render(matrixStack, vertexConsumer, i, k, ChamsModule.getColor2().hashCode());
         float l = 0.875F;
         matrixStack.scale(l, l, l);
         matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
         this.frame.render(matrixStack, vertexConsumer, i, k, ChamsModule.getColor2().hashCode());
         matrixStack.scale(l, l, l);
         matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
         this.core.render(matrixStack, vertexConsumer, i, k, ChamsModule.getColor2().hashCode());
         matrixStack.pop();
         matrixStack.pop();
         BlockPos blockPos = endCrystalEntity.getBeamTarget();
         if (blockPos != null) {
            float m = blockPos.getX() + 0.5F;
            float n = blockPos.getY() + 0.5F;
            float o = blockPos.getZ() + 0.5F;
            float p = (float)(m - endCrystalEntity.getX());
            float q = (float)(n - endCrystalEntity.getY());
            float r = (float)(o - endCrystalEntity.getZ());
            matrixStack.translate(p, q, r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + h, -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, i);
         }
      }
   }
}
