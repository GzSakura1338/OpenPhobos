package org.phobos.module.misc;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.util.ScreenshotFile;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class ScreenshotModule extends Module {
   public final Setting<ScreenshotModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", ScreenshotModule.Mode.Clipboard));

   public ScreenshotModule(PhobosClient phobosClient) {
      super(phobosClient, "Screenshot", Categories.misc);
      this.handleListener(
         new SafeListener<ScreenshotFile>() {
            public void handleObject(
               ScreenshotFile screenshotFile,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (ScreenshotModule.this.mode.testObject2(ScreenshotModule.Mode.Clipboard)) {
                  try {
                     BufferedImage bufferedImage = ImageIO.read(screenshotFile.getFile());
                     ScreenshotModule.Image image = new ScreenshotModule.Image(bufferedImage);
                     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                     clipboard.setContents(image, null);
                  } catch (Throwable var8) {
                  }
               }
            }
         }
      );
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   private record Image(BufferedImage img) implements Transferable {

      @Override
      public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
         DataFlavor[] dataFlavors = this.getTransferDataFlavors();

         for (DataFlavor dataFlavor2 : dataFlavors) {
            if (dataFlavor.equals(dataFlavor2)) {
               return true;
            }
         }

         return false;
      }

      @NotNull
      @Override
      public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
         if (dataFlavor.equals(DataFlavor.imageFlavor)) {
            return this.img;
         } else {
            throw new UnsupportedFlavorException(dataFlavor);
         }
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{DataFlavor.imageFlavor};
      }
   }

   private enum Mode {
      Clipboard;
   }
}
