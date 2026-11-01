package org.phobos.core.event.events;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;

public class CrystalRenderEvent extends CancellableEvent {
   public float floatValue;
   public int intValue;
   public EndCrystalEntity endCrystalEntity;
   public VertexConsumerProvider vertexConsumerProvider;
   public ModelPart modelPart;
   public ModelPart modelPart2;
   public MatrixStack matrixStack;
   public float floatValue2;

   public EndCrystalEntity getEndCrystalEntity() {
      return this.endCrystalEntity;
   }

   public VertexConsumerProvider getVertexConsumerProvider() {
      return this.vertexConsumerProvider;
   }

   public ModelPart getModelPart() {
      return this.modelPart;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public ModelPart getModelPart2() {
      return this.modelPart2;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public CrystalRenderEvent(
      EndCrystalEntity endCrystalEntity,
      float f,
      float f2,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i,
      ModelPart modelPart,
      ModelPart modelPart2
   ) {
      this.endCrystalEntity = endCrystalEntity;
      this.floatValue = f;
      this.floatValue2 = f2;
      this.matrixStack = matrixStack;
      this.vertexConsumerProvider = vertexConsumerProvider;
      this.intValue = i;
      this.modelPart = modelPart;
      this.modelPart2 = modelPart2;
   }
}
