package org.phobos.core.hud.element;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;
import javax.imageio.ImageIO;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.phobos.core.PhobosClient.InitEvent;
import org.phobos.event.Listener;
import org.phobos.core.hud.HudElement;
import org.phobos.core.hud.HudManager;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;

public class ImageHudElement extends HudElement {
   public NativeImageBackedTexture nativeImageBackedTexture;
   public final Setting<String> path = this.resolveSetting2(new StringSetting("Path", ""));
   public final ReentrantLock reentrantLock;
   public final Setting<Float> width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.01F, 5.0F));
   public final Setting<Float> height = this.resolveSetting2(new NumberSetting<>("Height", 1.0F, 0.01F, 5.0F));

   public void handleFloat5(float f, float f2) {
      if (this.nativeImageBackedTexture != null) {
         NativeImage nativeImage = this.nativeImageBackedTexture.getImage();
         if (nativeImage != null) {
            this.setFloatValue3(nativeImage.getWidth() * f);
            this.setFloatValue4(nativeImage.getHeight() * f2);
         }
      }
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (this.nativeImageBackedTexture == null) {
         this.setFloatValue3(0.0F);
         this.setFloatValue4(0.0F);
      } else if (!this.reentrantLock.isLocked()) {
         float f = this.getFloatValue2();
         float f2 = this.getFloatValue();
         float f3 = this.getFloatValue3();
         float f4 = this.getFloatValue4();
         HudManager.getHudManager().handleConsumer(drawContextx -> {
            if (this.nativeImageBackedTexture != null && !this.reentrantLock.isLocked()) {
               RenderSystem.setShaderTexture(0, this.nativeImageBackedTexture.getGlId());
               RenderUtils.handleMatrixStack3(drawContextx.getMatrices(), f, f2, f3, f4);
            }
         });
      }
   }

   public ImageHudElement() {
      super("Image", 0.0F, 0.0F, 0.0F, 0.0F);
      this.reentrantLock = new ReentrantLock();
      this.handleListener(new Listener<InitEvent>(Integer.MIN_VALUE) {
         public void handleObject2(InitEvent initEvent) {
            ImageHudElement.this.path.setValue(ImageHudElement.this.path.getValue());
         }
      });
      this.path.handleConsumer2(text -> mc.submit(() -> {
         Path path = Path.of(text);
         if (Files.exists(path)) {
            try {
               this.reentrantLock.lock();
               BufferedImage bufferedImage = ImageIO.read(path.toFile());
               this.nativeImageBackedTexture = Render3d.resolveNativeImageBackedTexture(bufferedImage);
               NativeImage nativeImage = this.nativeImageBackedTexture.getImage();
               if (nativeImage != null) {
                  this.setFloatValue3(nativeImage.getWidth() * this.width.getValue());
                  this.setFloatValue4(nativeImage.getHeight() * this.height.getValue());
                  return;
               }
            } catch (Throwable var8) {
               return;
            } finally {
               this.reentrantLock.unlock();
            }
         }
      }));
      this.width.handleConsumer2(floatValue -> this.handleFloat5(floatValue, this.height.getValue()));
      this.height.handleConsumer2(floatValue -> this.handleFloat5(this.width.getValue(), floatValue));
   }
}
