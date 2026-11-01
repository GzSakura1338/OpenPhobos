package org.phobos.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IBufferBuilderStorage;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.client.MediaModule;

public class ItemsModule extends Module {
   public final Setting<Boolean> thrownText;
   public final Setting<Color> boxFill;
   public final Setting<Boolean> itemsData;
   public final Setting<Double> offset;
   public final Setting<Float> width;
   public final Setting<Boolean> thrown;
   public final Setting<Boolean> font;
   public final Setting<Boolean> itemsText;
   public final Setting<Color> line;
   public final Setting<Color> itemsDataColor;
   public final Setting<Boolean> properties;
   public final Setting<Boolean> shadow;
   public final Setting<Float> scale;
   public final Setting<Boolean> thrownBox;
   public final Setting<Boolean> items = this.resolveSetting2(new BooleanSetting("Items", false).resolveSetting7("Items on the ground"));
   public final Setting<Color> textColor;
   public final Setting<Boolean> itemsBox;

   public ItemsModule(PhobosClient phobosClient) {
      super(phobosClient, "Items", Categories.render, "Renders items and projectiles");
      this.itemsData = this.resolveSetting2(new BooleanSetting("ItemsData", false).resolveSetting7("Displays container contents"));
      this.itemsDataColor = this.resolveSetting2(new ColorSetting("ItemsDataColor", new Color(0, 0, 0, 50)));
      this.itemsText = this.resolveSetting2(new BooleanSetting("ItemsText", false).resolveSetting7("Nametag data"));
      this.itemsBox = this.resolveSetting2(new BooleanSetting("ItemsBox", false).resolveSetting7("Bounding box"));
      this.thrown = this.resolveSetting2(new BooleanSetting("Thrown", false).resolveSetting7("Projectiles"));
      this.thrownText = this.resolveSetting2(new BooleanSetting("ThrownText", false).resolveSetting7("Nametag data"));
      this.thrownBox = this.resolveSetting2(new BooleanSetting("ThrownBox", false).resolveSetting7("Bounding box"));
      this.properties = this.resolveSetting2(new BooleanSetting("Properties", true).resolveSetting7("Shows how much is in the stack"));
      this.font = this.resolveSetting2(new BooleanSetting("Font", false).resolveSetting7("Custom font"));
      this.textColor = this.resolveSetting2(new ColorSetting("TextColor", Color.WHITE));
      this.shadow = this.resolveSetting2(new BooleanSetting("Shadow", false).resolveSetting7("Font shadow"));
      this.offset = this.resolveSetting2(new NumberSetting<>("Offset", 0.4, 0.0, 1.0).resolveSetting7("Vertical offset"));
      this.scale = this.resolveSetting2(new NumberSetting<>("Scale", 1.0F, 0.5F, 2.0F).resolveSetting7("Font size"));
      this.boxFill = this.resolveSetting2(new ColorSetting("BoxFill", new Color(255, 255, 255, 100)).getColorSetting());
      this.line = this.resolveSetting2(new ColorSetting("Line", Color.WHITE).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 2.0F));
      this.createBuilder()
         .resolveBuilder4("Text")
         .resolveBuilder5(this.resolveSettingGroup(this.font, this.shadow))
         .resolveBuilder(this.textColor, this.properties, this.scale)
         .getSettingSubsection();
      this.createBuilder()
         .resolveBuilder6(this.items)
         .resolveBuilder5(this.resolveSettingGroup(this.itemsData, this.itemsDataColor))
         .resolveBuilder(this.itemsText, this.itemsBox)
         .getSettingSubsection();
      this.resolveSettingGroup(this.thrown, this.thrownText, this.thrownBox);
      this.resolveSettingGroup2("Box", this.boxFill, this.line, this.width);
      this.handleListener(
         new SafeListener<RenderContext>(-1) {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.getCameraEntity() != null) {
                  Vec3d vec3d3 = FreecamModule.getFreecamModule().check7()
                     ? FreecamModule.getFreecamModule().createVec3d()
                     : EntityInterpolation.createVec3d5(mc.getCameraEntity(), renderContext.getFloatValue());
                  Camera camera = mc.gameRenderer.getCamera();
                  if (camera != null) {
                     for (Entity entity : clientWorld.getEntities()) {
                        if (Render3d.testEntity(entity)
                           && (
                              entity instanceof ItemEntity && ItemsModule.this.items.getValue() && ItemsModule.this.itemsText.getValue()
                                 || entity instanceof ProjectileEntity && ItemsModule.this.thrown.getValue() && ItemsModule.this.thrownText.getValue()
                           )
                           && !(entity instanceof ExperienceBottleEntity)
                           && entity.isAlive()) {
                           String text = ItemsModule.this.describeEntity(entity);
                           if (text != null) {
                              Vec3d vec3d = EntityInterpolation.createVec3d5(entity, renderContext.getFloatValue());
                              Vec3d vec3d2 = EntityInterpolation.createVec3d(entity, renderContext.getFloatValue());
                              double d = entity.getX() - vec3d.getX();
                              double d2 = entity.getY() - vec3d.getY();
                              double d3 = entity.getZ() - vec3d.getZ();
                              int i = ItemsModule.this.font.getValue()
                                 ? (int)phobosClient.getRenderManager().computeText3(text)
                                 : mc.textRenderer.getWidth(text);
                              float f = i / 2.0F;
                              double d4 = clientPlayerEntity.getX() - vec3d3.getX() - d;
                              double d5 = clientPlayerEntity.getY() - vec3d3.getY() - d2;
                              double d6 = clientPlayerEntity.getZ() - vec3d3.getZ() - d3;
                              double d7 = FreecamModule.getFreecamModule().check7()
                                 ? vec3d3.distanceTo(vec3d2.add(0.0, entity.getHeight(), 0.0))
                                 : Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
                              if (!(d7 > 4096.0)) {
                                 float f2 = 0.0018F + 0.003F * (float)d7;
                                 if (d7 <= 8.0) {
                                    f2 = 0.0245F;
                                 }

                                 ItemsModule.this.handleItemEntity(
                                    ItemsModule.this.itemsData.getValue() && entity instanceof ItemEntity itemEntity ? itemEntity : null,
                                    text,
                                    f,
                                    d,
                                    d2,
                                    d3,
                                    camera,
                                    f2 * ItemsModule.this.scale.getValue()
                                 );
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>(-2) {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (Entity entity : clientWorld.getEntities()) {
                  if ((
                        entity instanceof ItemEntity && ItemsModule.this.items.getValue() && ItemsModule.this.itemsBox.getValue()
                           || entity instanceof ProjectileEntity && ItemsModule.this.thrown.getValue() && ItemsModule.this.thrownBox.getValue()
                     )
                     && entity.isAlive()) {
                     Box box = EntityInterpolation.resolveBox(entity, renderContext.getFloatValue());
                     Render3d.handleCuboid(
                        Cuboid.createBuilder()
                           .resolveBuilder11(box)
                           .resolveBuilder18(ItemsModule.this.boxFill.getValue(), ItemsModule.this.line.getValue())
                           .resolveBuilder12(ItemsModule.this.width.getValue())
                           .getCuboid()
                     );
                  }
               }
            }
         }
      );
   }

   public void handleItemEntity(@Nullable ItemEntity itemEntity, String text, float f, double d, double d2, double d3, Camera camera, float f2) {
      Vec3d vec3d = camera.getPos();
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.push();
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      matrixStack.translate(d - vec3d.getX(), d2 + this.offset.getValue() - vec3d.getY(), d3 - vec3d.getZ());
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      matrixStack.scale(-f2, -f2, -1.0F);
      GL11.glDepthFunc(519);
      Color color = this.textColor.getValue();
      if (this.font.getValue()) {
         this.phobosClient
            .getRenderManager()
            .handleMatrixStack2(
               matrixStack,
               text,
               -f,
               0.0F,
               color.getRed() / 255.0F,
               color.getGreen() / 255.0F,
               color.getBlue() / 255.0F,
               color.getAlpha() / 255.0F,
               this.shadow.getValue()
            );
      } else {
         this.phobosClient.getRenderManager().getText3dRenderer().handleMatrixStack3(matrixStack, text, -f, 0.0F, color.hashCode(), this.shadow.getValue());
      }

      if (itemEntity != null && InventoryUtils.testItemStack3(itemEntity.getStack())) {
         ItemStack[] itemStacks = new ItemStack[27];
         InventoryUtils.handleItemStack(itemEntity.getStack(), itemStacks);
         matrixStack.push();
         float f3 = 80.0F;
         float f4 = 54.0F;
         RenderUtils.handleMatrixStack20(matrixStack, this.itemsDataColor.getValue(), -f3, 8.0, -f3 + f3 * 2.0F, 8.0F + f4);
         float f5 = -f3;
         float f6 = 8.0F;
         int i = 0;
         Immediate immediate = ((IBufferBuilderStorage)(Object)mc.getBufferBuilders()).getBackingContext();

         for (ItemStack itemStack : itemStacks) {
            matrixStack.push();
            matrixStack.translate(f5, f6, 0.0F);
            matrixStack.translate(8.0F, 8.0F, 0.0F);
            matrixStack.scale(16.0F, 16.0F, 0.0F);
            matrixStack.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 0.0F));
            GL11.glDepthFunc(519);
            NametagsModule.handleItemStack(itemStack, 0.0F, 0.0F, 0.0F, matrixStack, immediate, mc.world);
            GL11.glDepthFunc(519);
            immediate.draw();
            matrixStack.pop();
            f5 += 18.0F;
            if (++i >= 9) {
               f6 += 18.0F;
               f5 = -f3;
               i = 0;
            }
         }

         matrixStack.pop();
      }

      RenderSystem.disableBlend();
      GL11.glDepthFunc(515);
      matrixStack.pop();
   }

   public String describeEntity(Entity entity) {
      if (entity instanceof ItemEntity itemEntity) {
         return (this.properties.getValue() ? itemEntity.getStack().getCount() + "x " : "") + entity.getName().getString();
      } else if (entity instanceof ThrownItemEntity thrownItemEntity) {
         return thrownItemEntity.getOwner() != null ? MediaModule.describeText(thrownItemEntity.getOwner().getNameForScoreboard()) : "Unknown";
      } else {
         return null;
      }
   }
}
