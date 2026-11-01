package org.phobos.core.render.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.InputStream;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.phobos.core.PhobosClient;

public class ImageTexture {
   public boolean flag;
   public NativeImageBackedTexture nativeImageBackedTexture;

   public ImageTexture resolveImageTexture(String text, String text2, boolean flag) {
      String text3 = "/assets/" + text + "/" + text2;
      InputStream inputStream = PhobosClient.class.getResourceAsStream(text3);
      if (inputStream == null) {
         throw new IllegalArgumentException("Unable to find path: " + text3);
      }

      try {
         NativeImage nativeImage = NativeImage.read(inputStream);
         this.nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
      } catch (Throwable throwable) {
         throw new IllegalArgumentException("Failed to parse image", throwable);
      }

      this.flag = !flag;
      return this;
   }

   public static ImageTexture resolveImageTexture2(String text, String text2, boolean flag) {
      return new ImageTexture().resolveImageTexture(text, text2, flag);
   }

   public void run() {
      RenderSystem.setShaderTexture(0, this.nativeImageBackedTexture.getGlId());
      if (!this.flag) {
         TextureIdUtils.handleNativeImageBackedTexture(this.nativeImageBackedTexture);
         this.flag = true;
      }
   }
}
