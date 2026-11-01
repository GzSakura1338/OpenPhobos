package org.phobos.module.client;

import com.google.gson.JsonElement;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.FontState;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;

public class FontModule extends Module {
   public final Setting<String> font2d = this.resolveSetting2(new StringSetting("Font2d", "Verdana").resolveSetting7("2D Font (Hud, UI)"));
   public final Setting<String> font3d = this.resolveSetting2(new StringSetting("Font3d", "FreeSerif").resolveSetting7("3D Font (Nametags, Items)"));
   public final Setting<Boolean> antiAliasing;
   public static final Logger LOGGER = LoggerFactory.getLogger(FontModule.class);
   public static FontModule fontModule;
   public final Setting<Boolean> metrics;
   public final Setting<Float> size = this.resolveSetting2(new NumberSetting<>("Size", 9.0F, 1.0F, 16.0F).resolveSetting7("Font size"));

   public Setting<Float> getSize() {
      return this.size;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.antiAliasing.setFlag4(true);
      this.metrics.setFlag4(true);
      super.handleJsonElement(jsonElement);
   }

   public Setting<String> getFont2d() {
      return this.font2d;
   }

   public byte[] values() {
      try {
         return new FileInputStream(this.describe() + FileSystems.getDefault().getSeparator() + this.font2d.getValue() + ".ttf").readAllBytes();
      } catch (IOException iOException2) {
         try {
            return PhobosClient.class.getClassLoader().getResourceAsStream("assets/phoboslite/fonts/opensans.ttf").readAllBytes();
         } catch (IOException iOException) {
            throw new RuntimeException(iOException);
         }
      }
   }

   public String describe() {
      String text = System.getProperty("os.name").toLowerCase();
      if (text.contains("windows")) {
         return "C:\\Windows\\Fonts";
      } else if (text.contains("darwin")) {
         return "/Library/Fonts/";
      } else {
         return text.contains("linux") ? "/usr/share/fonts/truetype" : "none";
      }
   }

   public FontModule(PhobosClient phobosClient) {
      super(phobosClient, "Font", Categories.client, "Uses custom font");
      this.antiAliasing = this.resolveSetting2(new BooleanSetting("Anti-Aliasing").resolveSetting7("Only affects 3D Font"));
      this.metrics = this.resolveSetting2(new BooleanSetting("Metrics").resolveSetting7("Enables font metrics"));
      fontModule = this;
      this.font2d.handleConsumer2(text -> {
         try {
            PhobosClient.handleRunnable2(phobosClient.getRenderManager()::run6);
         } catch (RuntimeException var3) {
         }
      });
      this.font3d.handleConsumer2(text -> PhobosClient.handleRunnable2(this::run7));
      this.antiAliasing.handleConsumer2(booleanValue -> FontState.getFontState().setFlag(booleanValue));
      this.metrics.handleConsumer2(booleanValue -> {
         FontState.getFontState().setFlag2(booleanValue);
         PhobosClient.handleRunnable2(this::run7);
      });
      this.run12();
   }

   @Override
   public void run4() {
      FontModule.Dispatcher.run();
   }

   public Setting<Boolean> getAntiAliasing() {
      return this.antiAliasing;
   }

   @Override
   public void handleFlag3(boolean flag) {
      super.handleFlag3(flag);
      if (flag) {
         this.run12();
      }
   }

   public static FontModule getFontModule() {
      return fontModule;
   }

   public Setting<String> getFont3d() {
      return this.font3d;
   }

   public Setting<Boolean> getMetrics() {
      return this.metrics;
   }

   public void run7() {
      this.phobosClient.getRenderManager().setSetupEvent(new SetupEvent(new Font[]{Font.decode(this.font3d.getValue())}, 16.0F, 36, 2));
   }

   @Override
   public void run3() {
      FontModule.Dispatcher.run();
   }

   public static final class Dispatcher {
      public static final List<Consumer<Boolean>> consumerList = new ArrayList<>();

      public static void run() {
         for (Consumer<Boolean> consumer : consumerList) {
            consumer.accept(HudModule.getHudModule().check4());
         }
      }

      public Dispatcher() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }

      public static void handleConsumer(Consumer<Boolean> consumer) {
         consumerList.add(consumer);
      }
   }
}
