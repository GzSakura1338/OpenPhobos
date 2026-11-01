package org.phobos.mixin.game.misc;

import java.io.File;
import java.util.function.Consumer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.util.ScreenshotFile;
import org.phobos.core.event.EventCallbackUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenshotRecorder.class)
public abstract class MixinScreenshotRecorder {
   @Shadow
   @Final
   private static Logger LOGGER;
   @Shadow
   @Final
   public static String SCREENSHOTS_DIRECTORY;

   @Shadow
   private static File getScreenshotFilename(File directory) {
      return null;
   }

   @Inject(method = "saveScreenshotInner", at = @At("HEAD"), cancellable = true)
   private static void saveScreenshotInnerHook(
      File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci
   ) {
      ci.cancel();
      NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(framebuffer);
      File file = new File(gameDirectory, SCREENSHOTS_DIRECTORY);
      file.mkdir();
      File file2 = fileName == null ? getScreenshotFilename(file) : new File(file, fileName);
      Util.getIoWorkerExecutor()
         .execute(
            () -> {
               try {
                  nativeImage.writeTo(file2);
                  EventCallbackUtils.handleObject(new ScreenshotFile(file2));
                  MutableText text = Text.literal(file2.getName())
                     .formatted(Formatting.UNDERLINE)
                     .styled(style -> style.withClickEvent(new ClickEvent(Action.OPEN_FILE, file2.getAbsolutePath())));
                  messageReceiver.accept(Text.translatable("screenshot.success", new Object[]{text}));
               } catch (Exception exception) {
                  LOGGER.warn("Couldn't save screenshot", exception);
                  messageReceiver.accept(Text.translatable("screenshot.failure", new Object[]{exception.getMessage()}));
               } finally {
                  nativeImage.close();
               }
            }
         );
   }
}
