package org.phobos.core.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.phobos.api.MinecraftHolder;

public class SkinUtils implements MinecraftHolder {
   public static Identifier resolveIdentifier(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      try {
         return abstractClientPlayerEntity.getSkinTextures().capeTexture();
      } catch (NullPointerException nullPointerException) {
         return null;
      }
   }

   public static float computeEntity(Entity entity) {
      return entity.getYaw();
   }

   public static Identifier resolveIdentifier2(String text) {
      return Identifier.of(text);
   }

   public static Identifier resolveIdentifier3(String text, String text2) {
      return Identifier.of(text, text2);
   }

   public static boolean testItemStack(ItemStack itemStack) {
      return itemStack.contains(DataComponentTypes.CUSTOM_NAME);
   }

   public static GameProfile resolveGameProfile(ItemStack itemStack) {
      if (!itemStack.getComponents().contains(DataComponentTypes.CUSTOM_MODEL_DATA) && itemStack.getComponents().contains(DataComponentTypes.PROFILE)) {
         ProfileComponent profileComponent = (ProfileComponent)itemStack.get(DataComponentTypes.PROFILE);
         if (profileComponent != null && !profileComponent.isCompleted()) {
            itemStack.remove(DataComponentTypes.PROFILE);
            profileComponent.getFuture()
               .thenAcceptAsync(profileComponentx -> itemStack.set(DataComponentTypes.PROFILE, profileComponentx), MinecraftClient.getInstance());
            profileComponent = null;
         }

         return profileComponent != null ? profileComponent.gameProfile() : null;
      } else {
         return null;
      }
   }

   public static Identifier resolveIdentifier4(GameProfile gameProfile) {
      SkinTextures skinTextures = mc.getSkinProvider().getSkinTextures(gameProfile);
      return skinTextures.textureUrl() == null ? null : skinTextures.texture();
   }

   public static void handleVertexConsumer(VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, int i, int i2) {
      vertexConsumer.vertex(matrix4f, f, f2, f3).color(255, 255, 255, 255).texture(f4, f5).light(i, i2);
   }

   public static void handleVertexConsumer2(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, int i, int i2, float f6, float f7, float f8
   ) {
      handleVertexConsumer5(vertexConsumer, matrix4f, f, f2, f3, f4, f5, i, i2 & 65535, i2 >> 16 & 65535, f6, f7, f8);
   }

   public static float computeEntity2(Entity entity) {
      return entity.getPitch();
   }

   public static boolean testItemStack2(ItemStack itemStack, ItemStack itemStack2) {
      return ItemStack.areItemsAndComponentsEqual(itemStack, itemStack2);
   }

   public static void handleVertexConsumer3(VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5) {
      vertexConsumer.vertex(matrix4f, f, f2, f3).color(255, 255, 255, 255).texture(f4, f5);
   }

   public static void handleEntity(Entity entity, float f) {
      entity.setYaw(f);
   }

   public static Item resolveItem(Identifier identifier) {
      return (Item)Registries.ITEM.get(identifier);
   }

   public static void handleText(Text text) {
      if (mc.player != null) {
         mc.player.sendMessage(text);
      }
   }

   public static void handleVertexConsumer4(VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, int i) {
      handleVertexConsumer(vertexConsumer, matrix4f, f, f2, f3, f4, f5, i & 65535, i >> 16 & 65535);
   }

   public static void handleEntity2(Entity entity, float f) {
      entity.setPitch(f);
   }

   public static Identifier resolveIdentifier5(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      return abstractClientPlayerEntity.getSkinTextures().texture();
   }

   public static void handleVertexConsumer5(
      VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, int i, int i2, int i3, float f6, float f7, float f8
   ) {
      vertexConsumer.vertex(matrix4f, f, f2, f3).color(255, 255, 255, 255).texture(f4, f5).light(i2, i3).overlay(i).normal(f6, f7, f8);
   }
}
