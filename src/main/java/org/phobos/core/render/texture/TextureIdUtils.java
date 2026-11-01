package org.phobos.core.render.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL30;

public final class TextureIdUtils {
   public static int intValue = -1;

   public static int computeIdentifier(Identifier identifier) {
      TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
      AbstractTexture abstractTexture = textureManager.getTexture(identifier);
      return abstractTexture.getGlId();
   }

   public static void handleIdentifier(Identifier identifier) {
      intValue = computeIdentifier(identifier);
   }

   public static void run() {
      if (intValue != -1) {
         GlStateManager._bindTexture(intValue);
         GlStateManager._texParameter(3553, 10241, 9729);
         GlStateManager._texParameter(3553, 10240, 9729);
      }
   }

   public static void setIntValue(int i) {
      intValue = i;
   }

   public static void handleNativeImageBackedTexture(NativeImageBackedTexture nativeImageBackedTexture) {
      GlStateManager._bindTexture(nativeImageBackedTexture.getGlId());
      GL30.glGenerateMipmap(3553);
      GlStateManager._texParameter(3553, 10241, 9987);
      GlStateManager._texParameter(3553, 10240, 9729);
   }

   public static void close() {
      if (intValue != -1) {
         GlStateManager._bindTexture(intValue);
         GlStateManager._texParameter(3553, 10241, 9728);
         GlStateManager._texParameter(3553, 10240, 9728);
         intValue = -1;
      }
   }

   public TextureIdUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
