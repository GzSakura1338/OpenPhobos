package org.phobos.core.render.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import org.earthhack.ducks.IClientPlayerNetworkHandler;
import org.phobos.api.NetworkPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.Render3d;

public class SkyboxTextureUtils implements MinecraftHolder {
   public static String text;
   public static boolean flag = false;
   public static final Identifier identifier = Identifier.of("phobos", "textures/skybox.png");
   public static NativeImageBackedTexture nativeImageBackedTexture;

   public static NativeImageBackedTexture resolveNativeImageBackedTexture(Path path) {
      File file = path.toFile();
      if (!file.exists()) {
         return null;
      }

      try {
         BufferedImage bufferedImage = ImageIO.read(file);
         return Render3d.resolveNativeImageBackedTexture(bufferedImage);
      } catch (Throwable throwable) {
         return null;
      }
   }

   public static void handlePacket(Packet<?> packet) {
      if (mc.player != null && packet != null) {
         ((IClientPlayerNetworkHandler)(Object)mc.player.networkHandler).handlePacket(packet);
      }
   }

   public static boolean testText(String text) {
      return SkyboxTextureUtils.text != null && SkyboxTextureUtils.text.equals(text);
   }

   public static void handleNetworkPacket(NetworkPacket networkPacket) {
      if (ClientHolder.getPhobosClient() != null) {
         ClientHolder.getPhobosClient().getPacketBus().handleNetworkPacket2(networkPacket);
      }
   }

   public static void run() {
      if (check()) {
         RenderSystem.setShaderTexture(0, nativeImageBackedTexture.getGlId());
      }
   }

   public static boolean check() {
      return nativeImageBackedTexture != null;
   }

   public static String describeText(String text) {
      if (nativeImageBackedTexture != null) {
         SkyboxTextureUtils.text = null;
         nativeImageBackedTexture.close();
      }

      String text2 = System.getProperty("user.home");
      Path path = Path.of(text2, "earthhack", "textures");
      if (!path.toFile().exists()) {
         return "Textures folder not found";
      }

      Path path2 = path.resolve(text);
      if (!path2.toFile().exists()) {
         return "File not found";
      }

      try {
         BufferedImage bufferedImage = ImageIO.read(path2.toFile());
         nativeImageBackedTexture = Render3d.resolveNativeImageBackedTexture(bufferedImage);
         SkyboxTextureUtils.text = text;
         return null;
      } catch (Throwable throwable) {
         return throwable.getMessage();
      }
   }
}
