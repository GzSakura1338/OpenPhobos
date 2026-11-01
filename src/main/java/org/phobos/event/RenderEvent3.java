package org.phobos.event;

import java.util.List;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.phobos.core.event.events.CancellableEvent;

public class RenderEvent3<T extends LivingEntity, M extends EntityModel<T>> extends CancellableEvent {
   public final MatrixStack matrixStack;
   public final M entityModel;
   public final float floatValue;
   public final T livingEntity;
   public final int intValue;
   public final List<FeatureRenderer<T, M>> featureRendererList;
   public final float floatValue2;

   public RenderEvent3(T livingEntity, float f, float f2, MatrixStack matrixStack, int i, M entityModel, List<FeatureRenderer<T, M>> list) {
      this.livingEntity = livingEntity;
      this.floatValue2 = f;
      this.floatValue = f2;
      this.matrixStack = matrixStack;
      this.intValue = i;
      this.entityModel = entityModel;
      this.featureRendererList = list;
   }

   public static class Fire<E extends Entity> extends CancellableEvent {
      public final E entity;

      public E getEntity() {
         return this.entity;
      }

      public Fire(E entity) {
         this.entity = entity;
      }
   }
}
