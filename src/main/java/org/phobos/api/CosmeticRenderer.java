package org.phobos.api;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.core.render.PlayerModelContext;

public interface CosmeticRenderer {
   default void handlePlayerModelContext(
      PlayerModelContext playerModelContext, int i, ModelPart modelPart, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i2, int i3
   ) {
      this.handleAbstractClientPlayerEntity(playerModelContext.getAbstractClientPlayerEntity(), i, modelPart, matrixStack, vertexConsumerProvider, i2, i3);
   }

   @Deprecated
   default void handleAbstractClientPlayerEntity(
      AbstractClientPlayerEntity abstractClientPlayerEntity,
      int i,
      ModelPart modelPart,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i2,
      int i3
   ) {
   }

   boolean alwaysTrue();

   default VertexConsumer resolveVertexConsumer(
      AbstractClientPlayerEntity abstractClientPlayerEntity, VertexConsumerProvider vertexConsumerProvider, PlayerModelContext playerModelContext
   ) {
      return null;
   }
}
