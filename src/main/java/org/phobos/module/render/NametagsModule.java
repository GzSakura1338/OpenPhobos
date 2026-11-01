package org.phobos.module.render;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase.ShaderProgram;
import net.minecraft.client.render.RenderPhase.Texture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MatrixUtil;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.world.World;
import org.earthhack.ducks.IBufferBuilderStorage;
import org.earthhack.ducks.IEntity;
import org.earthhack.ducks.ILivingEntity;
import org.earthhack.ducks.IPlayerListEntry;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.user.UserTagManager;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.module.player.LegacyModule;
import org.phobos.core.util.StreamUtils;
import org.phobos.core.util.OneShotFlag;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.combat.SurroundManager.TotemCalculation;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.Listener;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;
import org.phobos.module.client.MediaModule;

public class NametagsModule extends Module {
   public Setting<NametagsModule.Prediction> predictionSetting;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Boolean> booleanValueSetting4;
   public static final Logger LOGGER = LoggerFactory.getLogger(NametagsModule.class);
   public Setting<Boolean> booleanValueSetting5;
   public static final Function<Identifier, RenderLayer> identifierFunction = Util.memoize(
      identifier -> RenderLayer.of(
         "gui_textured",
         VertexFormats.POSITION_TEXTURE_COLOR,
         DrawMode.QUADS,
         786432,
         MultiPhaseParameters.builder()
            .texture(new Texture(identifier, false, false))
            .program(NametagsModule.shaderProgram)
            .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
            .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
            .build(false)
      )
   );
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Boolean> booleanValueSetting9;
   public static NametagsModule nametagsModule;
   public Setting<Boolean> booleanValueSetting10;
   public Setting<Boolean> booleanValueSetting11;
   public Setting<Boolean> booleanValueSetting12;
   public static final ShaderProgram shaderProgram = new ShaderProgram(GameRenderer::getPositionTexColorProgram);
   public Setting<Boolean> booleanValueSetting13;
   public Setting<Boolean> booleanValueSetting14;
   public Setting<Boolean> booleanValueSetting15;
   public Setting<Boolean> booleanValueSetting16;
   public Setting<Color> colorSetting;
   public Map<UserTagManager.SharedESP, Long> sharedESPMap;
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting17;
   public Setting<Color> colorSetting2;
   public Setting<Color> colorSetting3;
   public Setting<Boolean> booleanValueSetting18;
   public Setting<Boolean> booleanValueSetting19;
   public Setting<Boolean> booleanValueSetting20;
   public Setting<Float> floatValueSetting2;
   public Setting<Color> colorSetting4;
   public Setting<Boolean> booleanValueSetting21;
   private static Object[] phobos$g1806f52b8;

   public void handleMatrixStack(MatrixStack matrixStack, PlayerEntity playerEntity) {
      List<ItemStack> list = new CopyOnWriteArrayList<>();
      if (!playerEntity.getOffHandStack().isEmpty() && this.booleanValueSetting2.getValue()) {
         ItemStack itemStack = playerEntity.getOffHandStack();
         if (!playerEntity.isMainPlayer() || !itemStack.isOf(Items.SHIELD) || !LegacyModule.getLegacyModule().check7()) {
            list.add(itemStack);
         }
      }

      playerEntity.getInventory().armor.forEach(itemStack -> {
         if (!itemStack.isEmpty()) {
            list.add(itemStack);
         }
      });
      if (!playerEntity.getMainHandStack().isEmpty() && this.booleanValueSetting2.getValue()) {
         list.add(playerEntity.getMainHandStack());
      }

      Collections.reverse(list);
      float f = 0.0F;
      float f2 = 0.0F;
      int i = 0;

      for (ItemStack itemStack2 : list) {
         f -= 8.0F;
         f2 -= this.computeFloat(8.0F);
         if (itemStack2.getEnchantments().getSize() > i) {
            i = itemStack2.getEnchantments().getSize();
         }
      }

      float f3 = this.computeInt(i);
      float f4 = this.booleanValueSetting16.getValue() ? 13.5F : 0.0F;
      Immediate immediate = ((IBufferBuilderStorage)(Object)mc.getBufferBuilders()).getBackingContext();

      for (ItemStack itemStack3 : list) {
         matrixStack.push();
         matrixStack.translate(f, f3, 0.0F);
         matrixStack.translate(8.0F, 8.0F, 0.0F);
         matrixStack.scale(16.0F, 16.0F, 0.0F);
         matrixStack.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 0.0F));
         GL11.glDepthFunc(519);
         handleItemStack(itemStack3, 0.0F, 0.0F, 0.0F, matrixStack, immediate, mc.world);
         GL11.glDepthFunc(519);
         immediate.draw();
         matrixStack.pop();
         GL11.glDepthFunc(519);
         if (this.booleanValueSetting12.getValue()) {
            this.handleMatrixStack6(matrixStack, mc.textRenderer, itemStack3, (int)f, (int)f3);
         }

         if (itemStack3.getCount() != 1) {
            String text = String.valueOf(itemStack3.getCount());
            this.handleMatrixStack7(
               matrixStack,
               text,
               f2 + (this.booleanValueSetting16.getValue() ? this.computeFloat(13.26F) : 17.0F) - mc.textRenderer.getWidth(text),
               f3 + this.computeFloat(9.0F) - this.computeFloat(f4) + (this.booleanValueSetting16.getValue() ? 7.4F : 0.0F)
            );
         }

         matrixStack.scale(0.5F, 0.5F, 0.5F);
         if (this.booleanValueSetting10.getValue()) {
            this.handleMatrixStack2(matrixStack, itemStack3, f2 + 2.0F, f3 - this.computeFloat(4.5F) - f4);
         }

         if (this.booleanValueSetting6.getValue()) {
            this.handleMatrixStack3(matrixStack, itemStack3, f2 + 2.0F, f3 - f4);
         }

         matrixStack.scale(2.0F, 2.0F, 2.0F);
         f += 16.0F;
         f2 += this.computeFloat(16.0F);
      }

      ItemStack itemStack4 = playerEntity.getMainHandStack();
      if (!itemStack4.isEmpty()) {
         matrixStack.scale(0.5F, 0.5F, 0.5F);
         if (this.booleanValueSetting9.getValue()) {
            this.handleMatrixStack4(matrixStack, itemStack4, f3 - this.computeFloat(4.5F) - f4 - 12.0F);
         }

         matrixStack.scale(0.5F, 0.5F, 0.5F);
         matrixStack.scale(2.0F, 2.0F, 2.0F);
      }
   }

   public float computeFloat(float f) {
      return f / (this.booleanValueSetting16.getValue() ? 0.57F : 1.0F);
   }

   public float computePlayerEntity(PlayerEntity playerEntity) {
      if (AnimationsModule.check()) {
         return 0.43F - MathHelper.lerp(((ILivingEntity)(Object)playerEntity).getGlobalAnimation().getFloatValue2(), 0.0F, 0.3F);
      } else {
         return playerEntity.isSneaking() ? 0.4F : 0.43F;
      }
   }

   public Color resolveColor(UUID uUID, int i) {
      Color color = this.resolveColor4(uUID, i);
      Color color2 = this.resolveColor2(this.colorSetting4.getValue(), i);
      return color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255
         ? color2
         : this.resolveColor2(ColorUtils.createColor6(color, color2.getAlpha() / 255.0F), i);
   }

   public void handleText(
      String text, float f, PlayerEntity playerEntity, double d, double d2, double d3, Camera camera, float f2, UserTagManager.TelemetryUser telemetryUser
   ) {
      boolean flag = telemetryUser != null && this.booleanValueSetting7.getValue();
      float f3 = FreeLookModule.computeFloat(camera.getYaw());
      float f4 = playerEntity.equals(mc.player)
         ? FreeLookModule.computeFloat2(camera.getPitch())
         : (this.booleanValueSetting17.getValue() ? camera.getPitch() : 0.0F);
      Vec3d vec3d = camera.getPos();
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.push();
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      matrixStack.translate(d - vec3d.getX(), d2 + playerEntity.getHeight() + this.computePlayerEntity(playerEntity) - vec3d.getY(), d3 - vec3d.getZ());
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f3));
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f4));
      matrixStack.scale(-f2, -f2, -1.0F);
      Color color = this.resolveColor3(playerEntity);
      int i = (this.booleanValueSetting16.getValue() ? -2 : -3) + (flag ? -20 : 0);
      int i2 = this.booleanValueSetting16.getValue() ? 0 : -2;
      int i3 = 4 - (flag ? -20 : 0);
      int i4 = 12;
      int i5 = (int)(flag && this.booleanValueSetting16.getValue() ? 7.2F : (flag ? 8.0F : 0.0F));
      float f5 = 0.0F;
      float f6 = this.booleanValueSetting16.getValue() ? 2.5F : 0.0F;
      float f7 = f / (this.booleanValueSetting16.getValue() ? 1.76F : 1.0F);
      GL11.glDepthFunc(519);
      RenderUtils.handleMatrixStack20(matrixStack, this.colorSetting2.getValue(), -f7 + i + i5, i2, -f7 + i + f7 * 2.0F + i3, i2 + i4 + f6);
      RenderUtils.handleMatrixStack14(matrixStack, this.resolveColor5(playerEntity), -f7 + i + i5, i2, -f7 + i + f7 * 2.0F + i3 - (-f7 + i + i5), i4 + f6);
      if (flag) {
         RenderSystem.setShaderTexture(0, telemetryUser.getIdentifier());
         RenderUtils.handleMatrixStack2(
            matrixStack, -f7 + i + (this.booleanValueSetting16.getValue() ? 9.6F : 10.0F), this.booleanValueSetting16.getValue() ? 2.0 : -1.2F, 10.0
         );
      }

      this.handleMatrixStack8(matrixStack, text, -f + f5, 0.0F, color);
      if (this.booleanValueSetting5.getValue()) {
         this.handleMatrixStack(matrixStack, playerEntity);
      }

      GL11.glDepthFunc(515);
      matrixStack.pop();
   }

   public float computeInt(int i) {
      if (this.booleanValueSetting6.getValue() && i > 3) {
         float f = -14.0F;
         return f - (MathUtils.computeInt3(i, 0, 5) - 3) * this.computeFloat(4.5F);
      } else {
         return -18.0F;
      }
   }

   public static void handleItemStack(
      ItemStack itemStack, float f, float f2, float f3, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, World world
   ) {
      BakedModel bakedModel = mc.getItemRenderer().getModel(itemStack, world, null, 0);
      if (!itemStack.isEmpty()) {
         matrixStack.push();
         if (itemStack.isOf(Items.TRIDENT)) {
            bakedModel = mc.getItemRenderer().getModels().getModelManager().getModel(ModelIdentifier.ofVanilla("trident", "inventory"));
         } else if (itemStack.isOf(Items.SPYGLASS)) {
            bakedModel = mc.getItemRenderer().getModels().getModelManager().getModel(ModelIdentifier.ofVanilla("spyglass", "inventory"));
         }

         bakedModel.getTransformation().getTransformation(ModelTransformationMode.GUI).apply(false, matrixStack);
         matrixStack.translate(-0.5F + f, -0.5F + f2, -0.5F + f3);
         if (!bakedModel.isBuiltin()) {
            RenderLayer renderLayer = resolveRenderLayer2(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
            VertexConsumer vertexConsumer;
            if (ItemRenderer.usesDynamicDisplay(itemStack) && itemStack.hasGlint()) {
               net.minecraft.client.util.math.MatrixStack.Entry entry = matrixStack.peek().copy();
               MatrixUtil.scale(entry.getPositionMatrix(), 0.5F);
               vertexConsumer = ItemRenderer.getDynamicDisplayGlintConsumer(vertexConsumerProvider, renderLayer, entry);
            } else {
               vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, renderLayer, true, itemStack.hasGlint());
            }

            mc.getItemRenderer().renderBakedItemModel(bakedModel, itemStack, 15728880, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);
         } else {
            OneShotFlag.run();
            mc.getItemRenderer()
               .builtinModelItemRenderer
               .render(itemStack, ModelTransformationMode.GUI, matrixStack, vertexConsumerProvider, 15728880, OverlayTexture.DEFAULT_UV);
         }

         matrixStack.pop();
      }
   }

   public String describeSharedESP(UserTagManager.SharedESP sharedESP) {
      StringBuilder stringBuilder = new StringBuilder();
      if (this.booleanValueSetting8.getValue() && this.booleanValueSetting.getValue()) {
         stringBuilder.append(sharedESP.gameMode()).append(" ");
      }

      stringBuilder.append(sharedESP.name());
      if (this.booleanValueSetting.getValue()) {
         if (this.booleanValueSetting11.getValue()) {
            stringBuilder.append(" ").append(this.describeFloat(sharedESP.health())).append(MathUtils.computeFloat5(sharedESP.health(), 1)).append("§r");
         }

         if (this.booleanValueSetting3.getValue()) {
            stringBuilder.append(" ").append(sharedESP.latency()).append("ms");
         }
      }

      return stringBuilder.toString();
   }

   public Color resolveColor2(Color color, int i) {
      if (this.phobosClient.getUserTagManager().testInt(i)) {
         float f = color.getAlpha();
         float f2 = f / 3000.0F;
         float f3 = MathHelper.clamp(
               (int)(f2 * (float)(this.phobosClient.getUserTagManager().computeInt2(i) + 3000L - System.currentTimeMillis())), 0, (int)f
            )
            / 255.0F;
         return ColorUtils.createColor6(color, f3);
      } else if (this.testInt(i)) {
         float f4 = color.getAlpha();
         float f5 = f4 / 3000.0F;
         float f6 = MathHelper.clamp((int)(f5 * (float)(this.computeInt2(i) + 3000L - System.currentTimeMillis())), 0, (int)f4) / 255.0F;
         return ColorUtils.createColor6(color, f6);
      } else {
         return color;
      }
   }

   public long computeInt2(int i) {
      Entry<UserTagManager.SharedESP, Long> entry = this.sharedESPMap
         .entrySet()
         .stream()
         .filter(entryx -> ((UserTagManager.SharedESP)entryx.getKey()).id() == i)
         .findFirst()
         .orElse(null);
      return entry != null ? entry.getValue() : 0L;
   }

   public Color resolveColor3(PlayerEntity playerEntity) {
      if (this.phobosClient.getFriendList().testUUID(playerEntity.getUuid())) {
         return this.colorSetting.getValue();
      } else {
         return this.phobosClient.getEnemyList().testUUID(playerEntity.getUuid()) ? this.colorSetting3.getValue() : Color.WHITE;
      }
   }

   public Color resolveColor4(UUID uUID, int i) {
      if (this.phobosClient.getFriendList().testUUID(uUID)) {
         return this.resolveColor2(this.colorSetting.getValue(), i);
      } else {
         return this.phobosClient.getEnemyList().testUUID(uUID) ? this.resolveColor2(this.colorSetting3.getValue(), i) : this.resolveColor2(Color.WHITE, i);
      }
   }

   public void handleMatrixStack2(MatrixStack matrixStack, ItemStack itemStack, float f, float f2) {
      if (itemStack.isDamageable()) {
         int i = itemStack.getMaxDamage();
         int i2 = itemStack.getDamage();
         int i3 = (int)((float)(i - i2) / i * 100.0F);
         this.handleMatrixStack8(matrixStack, i3 + "%", f * 2.0F, f2 * 2.0F, ColorUtils.createColor((float)(i - i2) / i * 120.0F, 100.0F, 50.0F, 1.0F));
      }
   }

   public void handleMatrixStack3(MatrixStack matrixStack, ItemStack itemStack, float f, float f2) {
      if (!itemStack.getItem().equals(Items.ENCHANTED_GOLDEN_APPLE)) {
         if (itemStack.hasEnchantments()) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);
            float f3 = 0.0F;
            int i3 = 0;

            for (RegistryEntry<Enchantment> registryEntry : itemEnchantmentsComponent.getEnchantments()) {
               if (i3 >= 4) {
                  int i = itemEnchantmentsComponent.getLevel(registryEntry);
                  String text = registryEntry.getIdAsString();
                  String text2 = InventoryUtils.describeText2(text, i);
                  this.handleMatrixStack7(matrixStack, text2 + "...", f * 2.0F, (f2 + f3) * 2.0F);
                  break;
               }

               int i2 = itemEnchantmentsComponent.getLevel(registryEntry);
               String text3 = registryEntry.getIdAsString();
               String text4 = InventoryUtils.describeText2(text3, i2);
               this.handleMatrixStack7(matrixStack, text4, f * 2.0F, (f2 + f3) * 2.0F);
               f3 += this.computeFloat(4.5F);
               i3++;
            }
         }
      }
   }

   public void handleText2(String text, float f, UUID uUID, PlayerEntity playerEntity, int i, float f2, double d, double d2, double d3, Camera camera, float f3) {
      float f4 = FreecamModule.getFreecamModule().check7() ? camera.getYaw() : FreeLookModule.computeFloat(camera.getYaw());
      float f5 = FreecamModule.getFreecamModule().check7() ? camera.getPitch() : (this.booleanValueSetting17.getValue() ? camera.getPitch() : 0.0F);
      Vec3d vec3d = camera.getPos();
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.push();
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      matrixStack.translate(d - vec3d.getX(), d2 + playerEntity.getHeight() + 0.43F - vec3d.getY(), d3 - vec3d.getZ());
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f4));
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f5));
      GL11.glDepthFunc(519);
      matrixStack.scale(-f3, -f3, -1.0F);
      Color color = this.resolveColor2(Color.WHITE, i);
      int i2 = this.booleanValueSetting16.getValue() ? -2 : -3;
      int i3 = this.booleanValueSetting16.getValue() ? 0 : -2;
      int i4 = 4;
      int i5 = 12;
      int i6 = 0;
      float f6 = 0.0F;
      float f7 = this.booleanValueSetting16.getValue() ? 2.5F : 0.0F;
      float f8 = f2 / (this.booleanValueSetting16.getValue() ? 1.76F : 1.0F);
      RenderUtils.handleMatrixStack20(
         matrixStack, this.resolveColor2(this.colorSetting2.getValue(), i), -f8 + i2 + i6, i3, -f8 + i2 + f8 * 2.0F + i4, i3 + i5 + f7
      );
      RenderUtils.handleMatrixStack13(
         matrixStack,
         this.resolveColor(uUID, i),
         -f8 + i2 + i6,
         i3,
         -f8 + i2 + f8 * 2.0F + i4 - (-f8 + i2 + i6),
         i5 + f7,
         this.floatValueSetting2.getValue().floatValue()
      );
      this.handleMatrixStack8(matrixStack, text, -f2 + f6, 0.0F, color);
      GL11.glDepthFunc(515);
      matrixStack.pop();
   }

   public static boolean check2() {
      return nametagsModule.check7();
   }

   public static void handleItemStack2(
      ItemStack itemStack, float f, float f2, float f3, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, World world
   ) {
      BakedModel bakedModel = mc.getItemRenderer().getModel(itemStack, world, null, 0);
      if (!itemStack.isEmpty()) {
         matrixStack.push();
         if (itemStack.isOf(Items.TRIDENT)) {
            bakedModel = mc.getItemRenderer().getModels().getModelManager().getModel(ModelIdentifier.ofVanilla("trident", "inventory"));
         } else if (itemStack.isOf(Items.SPYGLASS)) {
            bakedModel = mc.getItemRenderer().getModels().getModelManager().getModel(ModelIdentifier.ofVanilla("spyglass", "inventory"));
         }

         bakedModel.getTransformation().getTransformation(ModelTransformationMode.GUI).apply(false, matrixStack);
         matrixStack.translate(-0.5F + f, -0.5F + f2, -0.5F + f3);
         if (!bakedModel.isBuiltin()) {
            RenderLayer renderLayer = resolveRenderLayer2(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, renderLayer, true, false);
            mc.getItemRenderer().renderBakedItemModel(bakedModel, itemStack, 15728880, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);
         } else {
            OneShotFlag.run();
            mc.getItemRenderer()
               .builtinModelItemRenderer
               .render(itemStack, ModelTransformationMode.GUI, matrixStack, vertexConsumerProvider, 15728880, OverlayTexture.DEFAULT_UV);
         }

         matrixStack.pop();
      }
   }

   public Color resolveColor5(PlayerEntity playerEntity) {
      Color color = this.resolveColor3(playerEntity);
      Color color2 = this.colorSetting4.getValue();
      return color.equals(Color.WHITE) ? color2 : ColorUtils.createColor6(color, color2.getAlpha() / 255.0F);
   }

   public void handleMatrixStack4(MatrixStack matrixStack, ItemStack itemStack, float f) {
      String text = itemStack.getName().getString();
      float f2 = (this.booleanValueSetting16.getValue() ? this.phobosClient.getRenderManager().computeText3(text) : mc.textRenderer.getWidth(text)) / 4.0F;
      this.handleMatrixStack7(matrixStack, text, -f2 * 2.0F, f * 2.0F);
   }

   public NametagsModule(PhobosClient phobosClient) {
      super(phobosClient, "Nametags", Categories.render, "Displays additional information");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      Boolean var3 = true & true;
      BooleanSetting var2 = new BooleanSetting("Other", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.booleanValueSetting = var4;
      Boolean var6 = true & true;
      BooleanSetting var5 = new BooleanSetting("Health", var6);
      Setting var7 = var5.resolveSetting7("Displays health");
      Setting var8 = this.resolveSetting2(var7);
      this.booleanValueSetting11 = var8;
      NametagsModule.Prediction var10 = NametagsModule.Prediction.None;
      EnumSetting var9 = new EnumSetting<>("HealthPredict", var10);
      Setting var11 = this.resolveSetting2(var9);
      this.predictionSetting = var11;
      Boolean var13 = true & true;
      BooleanSetting var12 = new BooleanSetting("Latency", var13);
      Setting var14 = var12.resolveSetting7("Displays ping to the server");
      Setting var15 = this.resolveSetting2(var14);
      this.booleanValueSetting3 = var15;
      Boolean var17 = true & true;
      BooleanSetting var16 = new BooleanSetting("Gamemode", var17);
      Setting var18 = var16.resolveSetting7("Displays gamemode");
      Setting var19 = this.resolveSetting2(var18);
      this.booleanValueSetting8 = var19;
      Boolean var21 = true & true;
      BooleanSetting var20 = new BooleanSetting("Telemetry", var21);
      Setting var22 = var20.resolveSetting7("Displays an icon if a player is using phobos");
      Setting var23 = this.resolveSetting2(var22);
      this.booleanValueSetting7 = var23;
      Boolean var25 = false & true;
      BooleanSetting var24 = new BooleanSetting("Replace", var25);
      Setting var26 = var24.resolveSetting7("Replaces players name with their phobos name");
      Setting var27 = this.resolveSetting2(var26);
      this.booleanValueSetting4 = var27;
      Boolean var29 = false & true;
      BooleanSetting var28 = new BooleanSetting("Items", var29);
      Setting var30 = var28.resolveSetting7("Displays equipment");
      Setting var31 = this.resolveSetting2(var30);
      this.booleanValueSetting5 = var31;
      Boolean var33 = false & true;
      BooleanSetting var32 = new BooleanSetting("Hands", var33);
      Setting var34 = var32.resolveSetting7("Displays items the player is holding");
      Setting var35 = this.resolveSetting2(var34);
      this.booleanValueSetting2 = var35;
      Boolean var37 = true & true;
      BooleanSetting var36 = new BooleanSetting("Durability", var37);
      Setting var38 = var36.resolveSetting7("Displays durability");
      Setting var39 = this.resolveSetting2(var38);
      this.booleanValueSetting10 = var39;
      Boolean var41 = false & true;
      BooleanSetting var40 = new BooleanSetting("Bar", var41);
      Setting var42 = var40.resolveSetting7("Displays minecraft damage bar");
      Setting var43 = this.resolveSetting2(var42);
      this.booleanValueSetting12 = var43;
      Boolean var45 = true & true;
      BooleanSetting var44 = new BooleanSetting("Enchants", var45);
      Setting var46 = var44.resolveSetting7("Displays item enchantments");
      Setting var47 = this.resolveSetting2(var46);
      this.booleanValueSetting6 = var47;
      Boolean var49 = false & true;
      BooleanSetting var48 = new BooleanSetting("Name", var49);
      Setting var50 = var48.resolveSetting7("Displays holding item name");
      Setting var51 = this.resolveSetting2(var50);
      this.booleanValueSetting9 = var51;
      Boolean var53 = true & true;
      BooleanSetting var52 = new BooleanSetting("Vertical", var53);
      Setting var54 = var52.resolveSetting7("Applies vertical rotation to the nametag");
      Setting var55 = this.resolveSetting2(var54);
      this.booleanValueSetting17 = var55;
      Color var57 = new Color(0, 0, 0, 50);
      ColorSetting var56 = new ColorSetting("Background", var57);
      Setting var58 = var56.resolveSetting7("Background fill");
      Setting var59 = this.resolveSetting2(var58);
      this.colorSetting2 = var59;
      Color var61 = new Color(255, 255, 255, 0);
      ColorSetting var60 = new ColorSetting("Outline", var61);
      Setting var62 = var60.resolveSetting7("Outline color");
      Setting var63 = this.resolveSetting2(var62);
      this.colorSetting4 = var63;
      Float var65 = (float)1.0F;
      Float var66 = (float)0.1F;
      Float var67 = (float)2.0F;
      NumberSetting var64 = new NumberSetting<>("Width", var65, var66, var67);
      Visibility var84 = this::check;
      Setting var85 = var64.resolveSetting3(var84);
      Setting var86 = this.resolveSetting2(var85);
      this.floatValueSetting2 = var86;
      Boolean var88 = false & true;
      BooleanSetting var87 = new BooleanSetting("Font", var88);
      Setting var89 = var87.resolveSetting7("Custom font");
      Setting var90 = this.resolveSetting2(var89);
      this.booleanValueSetting16 = var90;
      Boolean var92 = false & true;
      BooleanSetting var91 = new BooleanSetting("Shadow", var92);
      Setting var93 = var91.resolveSetting7("Font shadow");
      Setting var94 = this.resolveSetting2(var93);
      this.booleanValueSetting18 = var94;
      Float var96 = (float)1.0F;
      Float var97 = (float)0.5F;
      Float var98 = (float)2.0F;
      NumberSetting var95 = new NumberSetting<>("Scale", var96, var97, var98);
      Setting var99 = var95.resolveSetting7("Nametag scale");
      Setting var100 = this.resolveSetting2(var99);
      this.floatValueSetting = var100;
      Boolean var102 = true & true;
      BooleanSetting var101 = new BooleanSetting("Totems", var102);
      Setting var103 = var101.resolveSetting7("Displays how many totems they popped");
      Setting var104 = this.resolveSetting2(var103);
      this.booleanValueSetting13 = var104;
      BooleanSetting var105 = new BooleanSetting("Predict");
      Setting var106 = var105.resolveSetting7("Displays predicted totems");
      Setting var107 = this.resolveSetting2(var106);
      this.booleanValueSetting20 = var107;
      BooleanSetting var108 = new BooleanSetting("Dormant");
      Setting var109 = var108.resolveSetting7("Does not immediately remove nametags");
      Setting var110 = this.resolveSetting2(var109);
      this.booleanValueSetting19 = var110;
      Boolean var112 = true & true;
      BooleanSetting var111 = new BooleanSetting("Dead", var112);
      Setting var113 = var111.resolveSetting7("Renders the nametags on dead players");
      Setting var114 = this.resolveSetting2(var113);
      this.booleanValueSetting15 = var114;
      BooleanSetting var115 = new BooleanSetting("Sort");
      Setting var116 = var115.resolveSetting7("Sorts entities by distance to prevent weird overlapping");
      Setting var117 = this.resolveSetting2(var116);
      this.booleanValueSetting14 = var117;
      BooleanSetting var118 = new BooleanSetting("Self");
      Setting var119 = var118.resolveSetting7("Renders the nametag on the local player");
      Setting var120 = this.resolveSetting2(var119);
      this.booleanValueSetting21 = var120;
      Color var122 = Color.CYAN;
      ColorSetting var121 = new ColorSetting("Friend", var122);
      Setting var123 = var121.resolveSetting7("Friend color");
      Setting var124 = this.resolveSetting2(var123);
      this.colorSetting = var124;
      Color var126 = Color.RED;
      ColorSetting var125 = new ColorSetting("Enemy", var126);
      Setting var127 = var125.resolveSetting7("Enemy color");
      Setting var128 = this.resolveSetting2(var127);
      this.colorSetting3 = var128;
      ConcurrentHashMap var129 = new ConcurrentHashMap();
      this.sharedESPMap = var129;
      Builder var130 = this.createBuilder();
      Builder var131 = var130.resolveBuilder4("Render");
      SettingGroup[] var132 = new SettingGroup[2];
      Setting[] var133 = new Setting[3];
      Setting var134 = this.colorSetting2;
      var133[0] = var134;
      Setting var135 = this.colorSetting4;
      var133[1] = var135;
      Setting var136 = this.floatValueSetting2;
      var133[2] = var136;
      SettingGroup var137 = this.resolveSettingGroup2("Decorations", var133);
      var132[0] = var137;
      Setting var138 = this.booleanValueSetting16;
      Setting[] var139 = new Setting[1];
      Setting var140 = this.booleanValueSetting18;
      var139[0] = var140;
      SettingGroup var141 = this.resolveSettingGroup(var138, var139);
      var132[1] = var141;
      Builder var142 = var131.resolveBuilder5(var132);
      Setting[] var143 = new Setting[2];
      Setting var144 = this.booleanValueSetting17;
      var143[0] = var144;
      Setting var145 = this.floatValueSetting;
      var143[1] = var145;
      Builder var146 = var142.resolveBuilder(var143);
      var146.getSettingSubsection();
      Builder var147 = this.createBuilder();
      Setting var148 = this.booleanValueSetting5;
      Builder var149 = var147.resolveBuilder6(var148);
      SettingGroup[] var150 = new SettingGroup[1];
      Setting var151 = this.booleanValueSetting10;
      Setting[] var152 = new Setting[1];
      Setting var153 = this.booleanValueSetting12;
      var152[0] = var153;
      SettingGroup var154 = this.resolveSettingGroup(var151, var152);
      var150[0] = var154;
      Builder var155 = var149.resolveBuilder5(var150);
      Setting[] var156 = new Setting[3];
      Setting var157 = this.booleanValueSetting2;
      var156[0] = var157;
      Setting var158 = this.booleanValueSetting6;
      var156[1] = var158;
      Setting var159 = this.booleanValueSetting9;
      var156[2] = var159;
      Builder var160 = var155.resolveBuilder(var156);
      var160.getSettingSubsection();
      Builder var161 = this.createBuilder();
      Setting var162 = this.booleanValueSetting;
      Builder var163 = var161.resolveBuilder6(var162);
      SettingGroup[] var164 = new SettingGroup[1];
      Setting var165 = this.booleanValueSetting11;
      Setting[] var166 = new Setting[1];
      Setting var167 = this.predictionSetting;
      var166[0] = var167;
      SettingGroup var168 = this.resolveSettingGroup(var165, var166);
      var164[0] = var168;
      Builder var169 = var163.resolveBuilder5(var164);
      Setting[] var170 = new Setting[2];
      Setting var171 = this.booleanValueSetting3;
      var170[0] = var171;
      Setting var172 = this.booleanValueSetting8;
      var170[1] = var172;
      Builder var173 = var169.resolveBuilder(var170);
      var173.getSettingSubsection();
      Setting var174 = this.booleanValueSetting13;
      Setting[] var175 = new Setting[1];
      Setting var176 = this.booleanValueSetting20;
      var175[0] = var176;
      this.resolveSettingGroup(var174, var175);
      Setting var177 = this.booleanValueSetting7;
      Setting[] var178 = new Setting[1];
      Setting var179 = this.booleanValueSetting4;
      var178[0] = var179;
      this.resolveSettingGroup(var177, var178);
      var var180 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (mc.getCameraEntity() != null) {
               NametagsModule.this.sharedESPMap.entrySet().removeIf(entry -> entry.getValue() + 3000L < System.currentTimeMillis());
               Vec3d vec3d4 = FreecamModule.getFreecamModule().check7()
                  ? FreecamModule.getFreecamModule().createVec3d()
                  : EntityInterpolation.createVec3d5(mc.getCameraEntity(), renderContext.getFloatValue());
               double d15 = clientPlayerEntity.getX();
               double d16 = clientPlayerEntity.getY();
               double d17 = clientPlayerEntity.getZ();
               Camera camera = mc.gameRenderer.getCamera();

               for (Entity entity : NametagsModule.this.booleanValueSetting14.getValue()
                  ? NametagsModule.this.resolveIterable(clientPlayerEntity, clientWorld)
                  : clientWorld.getEntities()) {
                  if (entity instanceof PlayerEntity playerEntity
                     && Render3d.testEntity(entity)
                     && (NametagsModule.this.booleanValueSetting15.getValue() || playerEntity.isAlive())
                     && (
                        !playerEntity.equals(clientPlayerEntity)
                           || (FreeLookModule.alwaysTrue() || NametagsModule.this.booleanValueSetting21.getValue())
                              && !mc.options.getPerspective().isFirstPerson()
                           || FreecamModule.getFreecamModule().check7()
                     )) {
                     String text = NametagsModule.this.describePlayerEntity2(playerEntity);
                     Vec3d vec3d = EntityInterpolation.createVec3d5(playerEntity, renderContext.getFloatValue());
                     Vec3d vec3d2 = EntityInterpolation.createVec3d(playerEntity, renderContext.getFloatValue());
                     double d = playerEntity.getX() - vec3d.getX();
                     double d2 = playerEntity.getY() - vec3d.getY();
                     double d3 = playerEntity.getZ() - vec3d.getZ();
                     int i = NametagsModule.this.booleanValueSetting16.getValue()
                        ? (int)var1.getRenderManager().computeText3(text)
                        : mc.textRenderer.getWidth(text);
                     float f = i / 2.0F;
                     double d4 = d15 - vec3d4.getX() - d;
                     double d5 = d16 - vec3d4.getY() - d2;
                     double d6 = d17 - vec3d4.getZ() - d3;
                     double d7 = FreecamModule.getFreecamModule().check7()
                        ? vec3d4.distanceTo(vec3d2.add(0.0, playerEntity.getHeight(), 0.0))
                        : Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
                     if (!(d7 > 4096.0)) {
                        float f2 = 0.0018F + 0.003F * (float)d7;
                        if (d7 <= 8.0) {
                           f2 = 0.0245F;
                        }

                        NametagsModule.this.handleText(
                           text,
                           f,
                           playerEntity,
                           d,
                           d2,
                           d3,
                           camera,
                           f2 * NametagsModule.this.floatValueSetting.getValue(),
                           var1.getUserTagManager().resolveTelemetryUser(playerEntity.getNameForScoreboard())
                        );
                     }
                  }
               }

               if (NametagsModule.this.booleanValueSetting19.getValue()) {
                  List<UserTagManager.SharedESP> list = new ArrayList<>(NametagsModule.this.sharedESPMap.keySet());
                  if (ConfigManager.getCloudSettings().shared.getValue()) {
                     list.addAll(var1.getUserTagManager().getSharedESPMap().keySet());
                  }

                  RenderSystem.enableBlend();

                  for (UserTagManager.SharedESP sharedESP : list) {
                     if (clientWorld.getEntityById(sharedESP.id()) == null) {
                        String text2 = NametagsModule.this.describeSharedESP(sharedESP);
                        Vec3d vec3d3 = Vec3d.ZERO;
                        double d8 = sharedESP.x() - vec3d3.getX();
                        double d9 = sharedESP.y() - vec3d3.getY();
                        double d10 = sharedESP.z() - vec3d3.getZ();
                        int i2 = NametagsModule.this.booleanValueSetting16.getValue()
                           ? (int)var1.getRenderManager().computeText3(text2)
                           : mc.textRenderer.getWidth(text2);
                        float f3 = i2 / 2.0F;
                        double d11 = (FreecamModule.getFreecamModule().check7() ? vec3d4.getX() : d15 - vec3d4.getX()) - d8;
                        double d12 = (FreecamModule.getFreecamModule().check7() ? vec3d4.getY() : d16 - vec3d4.getY()) - d9;
                        double d13 = (FreecamModule.getFreecamModule().check7() ? vec3d4.getZ() : d17 - vec3d4.getZ()) - d10;
                        double d14 = Math.sqrt(d11 * d11 + d12 * d12 + d13 * d13);
                        if (!(d14 > 4096.0)) {
                           float f4 = 0.0018F + 0.003F * (float)d14;
                           if (d14 <= 8.0) {
                              f4 = 0.0245F;
                           }

                           NametagsModule.this.handleText2(
                              text2,
                              sharedESP.height(),
                              sharedESP.uuid(),
                              clientPlayerEntity,
                              sharedESP.id(),
                              f3,
                              d8,
                              d9,
                              d10,
                              camera,
                              f4 * NametagsModule.this.floatValueSetting.getValue()
                           );
                        }
                     }
                  }

                  RenderSystem.disableBlend();
               }
            }
         }
      };
      this.handleListener(var180);
      var var181 = new SafeListener<RemoveEntityEvent>() {
         public void handleObject(
            RemoveEntityEvent removeEntityEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (removeEntityEvent.getEntity() instanceof PlayerEntity playerEntity && NametagsModule.this.booleanValueSetting19.getValue()) {
               if (!(playerEntity instanceof FakePlayer) && !(playerEntity instanceof IEntity iEntity && iEntity.isClientSide())) {
                  Vec3d vec3d = EntityInterpolation.createVec3d(playerEntity, TimerManager.getFloatValue());
                  NametagsModule.this.sharedESPMap.entrySet().removeIf(entry -> entry.getKey().id() == playerEntity.getId());
                  PlayerListEntry playerListEntry;
                  NametagsModule.this.sharedESPMap
                     .put(
                        new UserTagManager.SharedESP(
                           playerEntity.getId(),
                           vec3d.getX(),
                           vec3d.getY(),
                           vec3d.getZ(),
                           playerEntity.getNameForScoreboard(),
                           playerEntity.getUuid(),
                           NametagsModule.this.describePlayerEntity(clientPlayerEntity),
                           EntityUtils.computeLivingEntity(clientPlayerEntity),
                           mc.getNetworkHandler() != null
                                 && (playerListEntry = mc.getNetworkHandler().getPlayerListEntry(clientPlayerEntity.getUuid())) != null
                              ? playerListEntry.getLatency()
                              : 0,
                           playerEntity.getHeight()
                        ),
                        System.currentTimeMillis()
                     );
               }
            }
         }
      };
      this.handleListener(var181);
      var var182 = new Listener<AddEntityEvent>() {
         public void handleObject2(AddEntityEvent addEntityEvent) {
            if (addEntityEvent.getEntity() instanceof PlayerEntity playerEntity && NametagsModule.this.booleanValueSetting19.getValue()) {
               if (!(playerEntity instanceof FakePlayer) && !(playerEntity instanceof IEntity iEntity && iEntity.isClientSide())) {
                  NametagsModule.this.sharedESPMap.entrySet().removeIf(entry -> entry.getKey().id() == playerEntity.getId());
               }
            }
         }
      };
      this.handleListener(var182);
      nametagsModule = this;
   }

   public boolean testInt(int i) {
      return this.sharedESPMap.entrySet().stream().anyMatch(entry -> entry.getKey().id() == i);
   }

   public String describeInt(int i, int i2) {
      if (i >= i2) {
         return "§c";
      } else {
         return i >= i2 / 2 ? "§6" : "§a";
      }
   }

   public String describeInt2(int i) {
      if (i >= 300) {
         return "§c";
      } else {
         return i >= 100 ? "§e" : "§a";
      }
   }

   public static RenderLayer resolveRenderLayer2(Identifier identifier) {
      return identifierFunction.apply(identifier);
   }

   public String describePlayerEntity(PlayerEntity playerEntity) {
      if (playerEntity.isCreative()) {
         return "[C]";
      } else {
         return playerEntity.isSpectator() ? "[SP]" : "[S]";
      }
   }

   public String describeInt3(int i) {
      if (i > 10) {
         return "§c";
      } else {
         return i > 5 ? "§6" : "§a";
      }
   }

   public void handleMatrixStack5(MatrixStack matrixStack, int i, int i2, int i3, int i4, int i5) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      if (i < i3) {
         int i6 = i;
         i = i3;
         i3 = i6;
      }

      if (i2 < i4) {
         int i7 = i2;
         i2 = i4;
         i4 = i7;
      }

      float f = Argb.getAlpha(i5) / 255.0F;
      float f2 = Argb.getRed(i5) / 255.0F;
      float f3 = Argb.getGreen(i5) / 255.0F;
      float f4 = Argb.getBlue(i5) / 255.0F;
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      bufferBuilder.vertex(matrix4f, i, i2, 0.0F).color(f2, f3, f4, f);
      bufferBuilder.vertex(matrix4f, i, i4, 0.0F).color(f2, f3, f4, f);
      bufferBuilder.vertex(matrix4f, i3, i4, 0.0F).color(f2, f3, f4, f);
      bufferBuilder.vertex(matrix4f, i3, i2, 0.0F).color(f2, f3, f4, f);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public void handleMatrixStack6(MatrixStack matrixStack, TextRenderer textRenderer, ItemStack itemStack, int i, int i2) {
      if (!itemStack.isEmpty()) {
         matrixStack.push();
         if (itemStack.isItemBarVisible()) {
            RenderSystem.disableDepthTest();
            int i3 = itemStack.getItemBarStep();
            int i4 = itemStack.getItemBarColor();
            int i5 = i + 2;
            int i6 = i2 + 13;
            this.handleMatrixStack5(matrixStack, i5, i6, i5 + 13, i6 + 2, -16777216);
            this.handleMatrixStack5(matrixStack, i5, i6, i5 + i3, i6 + 1, i4 | 0xFF000000);
            RenderSystem.enableDepthTest();
         }

         ClientPlayerEntity clientPlayerEntity = mc.player;
         float f = clientPlayerEntity == null
            ? 0.0F
            : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(itemStack.getItem(), mc.getRenderTickCounter().getTickDelta(false));
         if (f > 0.0F) {
            RenderSystem.disableDepthTest();
            int i7 = i2 + MathHelper.floor(16.0F * (1.0F - f));
            int i8 = i7 + MathHelper.ceil(16.0F * f);
            this.handleMatrixStack5(matrixStack, i, i7, i + 16, i8, Integer.MAX_VALUE);
            RenderSystem.enableDepthTest();
         }

         matrixStack.pop();
      }
   }

   public boolean check() {
      return this.colorSetting4.getValue().getAlpha() > 1;
   }

   public Iterable<Entity> resolveIterable(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      List<Entity> list = new ArrayList<>();
      Iterables.addAll(list, clientWorld.getEntities());
      return StreamUtils.resolveStream(
            list.stream(), entity -> -entity.getPos().add(0.0, entity.getHeight(), 0.0).squaredDistanceTo(clientPlayerEntity.getEyePos())
         )
         .toList();
   }

   public void handleMatrixStack7(MatrixStack matrixStack, String text, float f, float f2) {
      if (this.booleanValueSetting16.getValue()) {
         this.phobosClient.getRenderManager().handleMatrixStack(matrixStack, text, f, f2, 1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.phobosClient.getRenderManager().getText3dRenderer().handleMatrixStack(matrixStack, text, f, f2, -1);
      }
   }

   public String describePlayerEntity2(PlayerEntity playerEntity) {
      StringBuilder stringBuilder = new StringBuilder();
      if (this.booleanValueSetting8.getValue() && this.booleanValueSetting.getValue()) {
         stringBuilder.append(this.describePlayerEntity(playerEntity)).append(" ");
      }

      stringBuilder.append(MediaModule.describeText(this.describePlayerEntity3(playerEntity)));
      if (this.booleanValueSetting.getValue()) {
         if (this.booleanValueSetting11.getValue()) {
            float f3 = EntityUtils.computeLivingEntity(playerEntity);
            switch ((NametagsModule.Prediction)this.predictionSetting.getValue()) {
               case Display:
                  stringBuilder.append(" ").append(this.describeFloat(f3)).append(MathUtils.computeFloat5(f3, 1)).append("§r");
                  break;
               case None:
                  float f = this.phobosClient.getFeatureRegistry().getDamageTracker().computeLivingEntity(playerEntity, f3);
                  stringBuilder.append(" ").append(this.describeFloat(f3)).append(MathUtils.computeFloat5(f3, 1)).append("§r");
                  stringBuilder.append(" §7[§r").append(this.describeFloat(f)).append(MathUtils.computeFloat5(f, 1)).append("§7]§r");
                  break;
               case Override:
                  float f2 = this.phobosClient.getFeatureRegistry().getDamageTracker().computeLivingEntity(playerEntity, f3);
                  stringBuilder.append(" ").append(this.describeFloat(f2)).append(MathUtils.computeFloat5(f2, 1)).append("§r");
            }
         }

         if (this.booleanValueSetting3.getValue() && mc.getNetworkHandler() != null) {
            PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid());
            if (playerListEntry != null) {
               int i = playerListEntry.getLatency();
               stringBuilder.append(" ").append(this.describeInt2(i)).append(i).append("ms").append("§r");
            }
         }
      }

      int i2 = this.phobosClient.getEntityIdManager().computeEntity(playerEntity);
      if (this.booleanValueSetting13.getValue() && i2 != -1 && i2 != 0) {
         TotemCalculation totemCalculation;
         if (this.booleanValueSetting20.getValue()
            && (totemCalculation = this.phobosClient.getSurroundManager().resolveTotemCalculation(playerEntity)) != null) {
            stringBuilder.append(" ")
               .append(this.describeInt(i2, totemCalculation.current()))
               .append(i2)
               .append("§r")
               .append("/")
               .append(totemCalculation.current());
            if (totemCalculation.previous() != -1 && totemCalculation.previous() != 0) {
               stringBuilder.append("|").append(totemCalculation.previous());
            }
         } else {
            stringBuilder.append(" ").append(this.describeInt3(i2)).append(-i2).append("§r");
         }
      }

      return stringBuilder.toString();
   }

   public String describePlayerEntity3(PlayerEntity playerEntity) {
      String text = playerEntity.getNameForScoreboard();
      if (mc.getNetworkHandler() == null) {
         return text;
      }

      if (this.booleanValueSetting7.getValue()) {
         PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid());
         if (playerListEntry == null) {
            return text;
         } else {
            IPlayerListEntry iPlayerListEntry = (IPlayerListEntry)(Object)playerListEntry;
            UserTagManager.TelemetryUser telemetryUser = iPlayerListEntry.getUser();
            if (telemetryUser == null) {
               return text;
            } else {
               return this.booleanValueSetting4.getValue() ? telemetryUser.getText3() : telemetryUser.getText();
            }
         }
      } else {
         return text;
      }
   }

   public String describeFloat(float f) {
      return ChatTextUtils.describeFloat(f);
   }

   public void handleMatrixStack8(MatrixStack matrixStack, String text, float f, float f2, Color color) {
      if (this.booleanValueSetting16.getValue()) {
         this.phobosClient
            .getRenderManager()
            .handleMatrixStack2(
               matrixStack,
               text,
               f,
               f2,
               color.getRed() / 255.0F,
               color.getGreen() / 255.0F,
               color.getBlue() / 255.0F,
               color.getAlpha() / 255.0F,
               this.booleanValueSetting18.getValue()
            );
      } else {
         this.phobosClient
            .getRenderManager()
            .getText3dRenderer()
            .handleMatrixStack3(matrixStack, text, f, f2, color.hashCode(), this.booleanValueSetting18.getValue());
      }
   }

   private enum Prediction implements TextProvider {
      None("Does not use predicted health"),
      Display("Shows predicted health in brackets"),
      Override("Overrides health with prediction");

      public final String text;

      Prediction(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
