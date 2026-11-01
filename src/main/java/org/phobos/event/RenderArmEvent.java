package org.phobos.event;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.core.event.events.CancellableEvent;

public class RenderArmEvent extends CancellableEvent {
   public final PlayerEntityModel<AbstractClientPlayerEntity> abstractClientPlayerEntityPlayerEntityModel;
   public final AbstractClientPlayerEntity abstractClientPlayerEntity;
   public final MatrixStack matrixStack;
   public final ModelPart modelPart;
   public final ModelPart modelPart2;

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   public ModelPart getModelPart2() {
      return this.modelPart2;
   }

   public PlayerEntityModel<AbstractClientPlayerEntity> getAbstractClientPlayerEntityPlayerEntityModel() {
      return this.abstractClientPlayerEntityPlayerEntityModel;
   }

   public ModelPart getModelPart() {
      return this.modelPart;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public RenderArmEvent(
      MatrixStack matrixStack,
      PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel,
      AbstractClientPlayerEntity abstractClientPlayerEntity,
      ModelPart modelPart,
      ModelPart modelPart2
   ) {
      this.matrixStack = matrixStack;
      this.abstractClientPlayerEntityPlayerEntityModel = playerEntityModel;
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
      this.modelPart = modelPart;
      this.modelPart2 = modelPart2;
   }
}
