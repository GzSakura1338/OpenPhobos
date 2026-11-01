package org.phobos.core.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.phobos.api.CosmeticRenderer;
import org.phobos.module.render.ChamsModule;

public class CosmeticRendererImpl implements CosmeticRenderer {
   public VertexConsumer vertexConsumer = null;

   @Override
   public VertexConsumer resolveVertexConsumer(
      AbstractClientPlayerEntity abstractClientPlayerEntity, VertexConsumerProvider vertexConsumerProvider, PlayerModelContext playerModelContext
   ) {
      Identifier identifier = playerModelContext.getIdentifier();
      if (identifier != null) {
         return ChamsModule.testLivingEntity(abstractClientPlayerEntity)
            ? vertexConsumerProvider.getBuffer(EntityRenderLayers.resolveRenderLayer(identifier))
            : vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(identifier));
      } else {
         return null;
      }
   }

   @Override
   public boolean alwaysTrue() {
      return true;
   }

   @Override
   public void handlePlayerModelContext(
      PlayerModelContext playerModelContext, int i, ModelPart modelPart, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i2, int i3
   ) {
      modelPart.render(matrixStack, this.vertexConsumer, i2, OverlayTexture.DEFAULT_UV);
   }
}
