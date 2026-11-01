package org.phobos.module.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.earthhack.ducks.IBakedQuad;
import org.earthhack.ducks.IEntity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class ItemPhysicsModule extends Module {
   public static final float floatValue = 0.0625F;
   public final Random random;
   public boolean flag;
   public final Setting<Boolean> rotate;
   public final Setting<Boolean> drag = this.resolveSetting2(new BooleanSetting("Drag"));
   public static ItemPhysicsModule itemPhysicsModule;

   public void handleItemStack(
      ItemStack itemStack,
      MatrixStack matrixStack,
      ItemPhysicsModule.ModelInfo modelInfo,
      BakedModel bakedModel,
      VertexConsumerProvider vertexConsumerProvider,
      int i
   ) {
      this.flag = true;
      int i3 = this.computeItemStack(itemStack);

      for (int i2 = 0; i2 < i3; i2++) {
         matrixStack.push();
         if (i2 > 0) {
            float f = (this.random.nextFloat() * 2.0F - 1.0F) * 0.25F;
            float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.25F;
            this.handleMatrixStack(matrixStack, modelInfo, f, 0.0F, f2);
         }

         mc.getItemRenderer()
            .renderItem(itemStack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);
         matrixStack.pop();
         this.handleMatrixStack(matrixStack, modelInfo, 0.0F, Math.max(this.random.nextFloat() * 0.0625F, 0.03125F), 0.0F);
      }

      this.flag = false;
   }

   public void handleItemEntity(ItemEntity itemEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, float f) {
      matrixStack.push();
      ItemStack itemStack = itemEntity.getStack();
      BakedModel bakedModel = this.resolveBakedModel(itemEntity);
      ItemPhysicsModule.ModelInfo modelInfo = this.createModelInfo(bakedModel);
      this.random.setSeed(itemEntity.getId() * 2365798L);
      this.handleMatrixStack4(matrixStack, bakedModel);
      matrixStack.translate(0.0F, modelInfo.offsetY, 0.0F);
      this.handleMatrixStack3(matrixStack, itemEntity);
      this.handleMatrixStack2(matrixStack, itemEntity);
      if (modelInfo.flat) {
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
         matrixStack.translate(0.0F, 0.0F, modelInfo.offsetZ);
      }

      if (this.rotate.getValue()) {
         RotationAxis rotationAxis = modelInfo.flat ? RotationAxis.POSITIVE_Z : RotationAxis.POSITIVE_Y;
         float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * 90.0F;
         matrixStack.multiply(rotationAxis.rotationDegrees(f2));
      }

      if (this.drag.getValue()) {
         Vec3d vec3d = new Vec3d(itemEntity.lastRenderX, itemEntity.lastRenderY, itemEntity.lastRenderZ);
         double d = itemEntity.getPos().distanceTo(vec3d);
         if (d > 0.03505) {
            float f4 = (float)(d * 550.50055F * (this.random.nextFloat() * 2.0F - 1.0F));
            Object object = ((IEntity)(Object)itemEntity).get("lastRotation");
            if (object != null) {
               float f3 = (Float)object;
               f4 = MathHelper.lerp(f, f3, f4);
            }

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4 * 0.5F));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f4));
            ((IEntity)(Object)itemEntity).put("lastRotation", f4);
         }
      }

      this.handleItemStack(itemStack, matrixStack, modelInfo, bakedModel, vertexConsumerProvider, i);
      matrixStack.pop();
   }

   public void handleMatrixStack(MatrixStack matrixStack, ItemPhysicsModule.ModelInfo modelInfo, float f, float f2, float f3) {
      if (modelInfo.flat) {
         float f4 = f2;
         f2 = f3;
         f3 = -f4;
      }

      matrixStack.translate(f, f2, f3);
   }

   public BakedModel resolveBakedModel(ItemEntity itemEntity) {
      ItemStack itemStack = itemEntity.getStack();
      if (itemStack.isOf(Items.TRIDENT)) {
         return mc.getItemRenderer().getModels().getModelManager().getModel(ItemRenderer.TRIDENT);
      } else {
         return itemStack.isOf(Items.SPYGLASS)
            ? mc.getItemRenderer().getModels().getModelManager().getModel(ItemRenderer.SPYGLASS)
            : mc.getItemRenderer().getModel(itemStack, itemEntity.getWorld(), null, itemEntity.getId());
      }
   }

   public void handleMatrixStack2(MatrixStack matrixStack, ItemEntity itemEntity) {
      float f = 1.0E-4F;
      float f2 = (float)mc.gameRenderer.getCamera().getPos().distanceTo(itemEntity.getPos());
      f = Math.min(f * Math.max(1.0F, f2), 0.01F);
      matrixStack.translate(0.0F, f, 0.0F);
   }

   public void handleMatrixStack3(MatrixStack matrixStack, ItemEntity itemEntity) {
      if (itemEntity.isTouchingWater()) {
         matrixStack.translate(0.0F, 0.333F, 0.0F);
      }
   }

   public static ItemPhysicsModule getItemPhysicsModule() {
      return itemPhysicsModule;
   }

   public ItemPhysicsModule(PhobosClient phobosClient) {
      super(phobosClient, "ItemPhysics", Categories.render, "Adds physics to items");
      this.rotate = this.resolveSetting2(new BooleanSetting("Rotate"));
      this.random = Random.createLocal();
      itemPhysicsModule = this;
   }

   public int computeItemStack(ItemStack itemStack) {
      int i = 1;
      if (itemStack.getCount() > 48) {
         i = 5;
      }

      if (itemStack.getCount() > 32) {
         i = 4;
      }

      if (itemStack.getCount() > 16) {
         i = 3;
      }

      if (itemStack.getCount() > 1) {
         i = 2;
      }

      return i;
   }

   public static boolean testItemEntity(ItemEntity itemEntity, float f, float f2, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
      if (itemPhysicsModule.check7()) {
         itemPhysicsModule.handleItemEntity(itemEntity, matrixStack, vertexConsumerProvider, i, f2);
         return true;
      } else {
         return false;
      }
   }

   public void handleMatrixStack4(MatrixStack matrixStack, BakedModel bakedModel) {
      Transformation transformation = bakedModel.getTransformation().ground;
      float f = transformation.translation.y;
      transformation.translation.y = 0.0F;
      transformation.apply(false, matrixStack);
      transformation.translation.y = f;
   }

   public static boolean check() {
      return itemPhysicsModule.check7() && itemPhysicsModule.flag;
   }

   public ItemPhysicsModule.ModelInfo createModelInfo(BakedModel bakedModel) {
      Random random = Random.createLocal();
      float f = Float.MAX_VALUE;
      float f2 = Float.MAX_VALUE;
      float f3 = Float.MAX_VALUE;
      float f4 = Float.MIN_VALUE;
      float f5 = Float.MIN_VALUE;
      float f6 = Float.MIN_VALUE;

      for (Direction direction : Direction.values()) {
         for (BakedQuad bakedQuad : bakedModel.getQuads(null, direction, random)) {
            IBakedQuad iBakedQuad = (IBakedQuad)(Object)bakedQuad;

            for (int i = 0; i < 4; i++) {
               switch (bakedQuad.getFace()) {
                  case DOWN:
                     f2 = Math.min(f2, iBakedQuad.getY(i));
                     break;
                  case UP:
                     f5 = Math.max(f5, iBakedQuad.getY(i));
                     break;
                  case NORTH:
                     f3 = Math.min(f3, iBakedQuad.getZ(i));
                     break;
                  case SOUTH:
                     f6 = Math.max(f6, iBakedQuad.getZ(i));
                     break;
                  case WEST:
                     f = Math.min(f, iBakedQuad.getX(i));
                     break;
                  case EAST:
                     f4 = Math.max(f4, iBakedQuad.getX(i));
               }
            }
         }
      }

      if (f == Float.MAX_VALUE) {
         f = 0.0F;
      }

      if (f2 == Float.MAX_VALUE) {
         f2 = 0.0F;
      }

      if (f3 == Float.MAX_VALUE) {
         f3 = 0.0F;
      }

      if (f4 == Float.MIN_VALUE) {
         f4 = 1.0F;
      }

      if (f5 == Float.MIN_VALUE) {
         f5 = 1.0F;
      }

      if (f6 == Float.MIN_VALUE) {
         f4 = 1.0F;
      }

      float f7 = f4 - f;
      float f8 = f5 - f2;
      float f9 = f6 - f3;
      boolean flag = f7 > 0.0625F && f8 > 0.0625F && f9 <= 0.0625F;
      return new ItemPhysicsModule.ModelInfo(flag, 0.5F - f2, f3 - f2);
   }

   private record ModelInfo(boolean flat, float offsetY, float offsetZ) {
   }
}
