package org.phobos.core.entity;

import java.util.Map;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.MathHelper;
import org.phobos.module.render.ShaderModule;
import org.phobos.core.Feature;

public class EntityFadeManager extends Feature {
   public static EntityFadeManager entityFadeManager;
   public final Map<Entity, Float> entityMap = new ConcurrentHashMap<>();

   public Map<Entity, Float> getEntityMap() {
      return this.entityMap;
   }

   public void handleEntity(Entity entity, float f) {
      this.entityMap.put(entity, f);
   }

   public void handleEntity2(Entity entity) {
      this.entityMap.remove(entity);
   }

   public void handleEntityRenderDispatcher(EntityRenderDispatcher entityRenderDispatcher, MatrixStack matrixStack, double d, double d2, double d3, float f) {
      for (Map.Entry<Entity, Float> entry : this.entityMap.entrySet()) {
         Entity entity = entry.getKey();
         if (entity != null) {
            float f2 = Float.isNaN(entry.getValue()) ? f : entry.getValue();
            double d4 = MathHelper.lerp(f2, entity.lastRenderX, entity.getX());
            double d5 = MathHelper.lerp(f2, entity.lastRenderY, entity.getY());
            double d6 = MathHelper.lerp(f2, entity.lastRenderZ, entity.getZ());
            float f3 = MathHelper.lerp(f2, entity.prevYaw, entity.getYaw());
            entityRenderDispatcher.render(
               entity,
               d4 - d,
               d5 - d2,
               d6 - d3,
               f3,
               f2,
               matrixStack,
               (VertexConsumerProvider)(entity instanceof EndCrystalEntity
                     && ShaderModule.getShaderModule().check7()
                     && ShaderModule.getShaderModule().getBooleanValueSetting6().getValue()
                  ? mc.getBufferBuilders().getOutlineVertexConsumers()
                  : mc.getBufferBuilders().getEntityVertexConsumers()),
               entityRenderDispatcher.getLight(entity, f)
            );
         }
      }

      this.entityMap.clear();
   }

   public void handleEntity3(Entity entity) {
      this.entityMap.put(entity, Float.NaN);
   }

   public EntityFadeManager() {
      entityFadeManager = this;
   }

   public static void handleEntityRenderDispatcher2(
      EntityRenderDispatcher entityRenderDispatcher, MatrixStack matrixStack, double d, double d2, double d3, float f
   ) {
      if (entityFadeManager != null) {
         entityFadeManager.handleEntityRenderDispatcher(entityRenderDispatcher, matrixStack, d, d2, d3, f);
      }
   }

   public static EntityFadeManager getEntityFadeManager() {
      return entityFadeManager;
   }
}
