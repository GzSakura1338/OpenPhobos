package org.phobos.module.client;

import java.awt.Color;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class ColorsModule extends Module {
   public static float floatValue;
   public static ColorsModule colorsModule;
   public final Setting<Integer> speed;
   public static final Color color = new Color(0.0F, 0.0F, 0.0F, 0.0F);
   public final Setting<Color> color2 = this.resolveSetting2(new ColorSetting("Color", Color.WHITE).resolveSetting7("Global color"));

   public static ColorsModule getColorsModule() {
      return colorsModule;
   }

   public static float computeDouble(double d) {
      return (float)(floatValue - d * 0.001F);
   }

   public ColorsModule(PhobosClient phobosClient) {
      super(phobosClient, "Colors", Categories.client, "Controls global color");
      this.speed = this.resolveSetting2(new NumberSetting<>("Speed", 50, 0, 100).resolveSetting7("Rainbow speed"));
      phobosClient.getEventBus().handleObject(new ColorsModule.ColorsUploader(phobosClient));
      colorsModule = this;
   }

   private static class ColorsUploader extends Feature {
      public ColorsUploader(PhobosClient phobosClient) {
         this.handleListener(
            new Listener<TickEvent2>() {
               public void handleObject2(TickEvent2 tickEvent2) {
                  if (phobosClient.getModuleManager() != null) {
                     if (!ColorsModule.colorsModule.speed.getValue().equals(0)) {
                        ColorsModule.floatValue = (float)(System.currentTimeMillis() % (360 * ColorsModule.colorsModule.speed.getValue()))
                           / (360.0F * ColorsModule.colorsModule.speed.getValue().intValue());
                     }

                     if (phobosClient.getModuleManager() != null) {
                        phobosClient.getModuleManager()
                           .getList()
                           .forEach(
                              module -> module.stream()
                                 .forEach(
                                    setting -> {
                                       if (setting instanceof ColorSetting colorSetting) {
                                          if (colorSetting.isFlag2()) {
                                             colorSetting.handleRunnable(() -> colorSetting.handleColor(ColorsModule.colorsModule.color2.getValue()));
                                             return;
                                          }

                                          if (colorSetting.isFlag()) {
                                             Color color;
                                             if (colorSetting.isFlag3()) {
                                                color = new Color(
                                                   ColorUtils.computeFloat2(
                                                      0.0F,
                                                      colorSetting.getColor(),
                                                      colorSetting.getFloatValue22(),
                                                      colorSetting.getFloatValue3(),
                                                      colorSetting.getFloatValue()
                                                   )
                                                );
                                             } else {
                                                color = ColorUtils.resolveColor5(
                                                   (int)Math.max(colorSetting.getFloatValue22() * 100.0F * 30.0F, 30.0F),
                                                   0,
                                                   colorSetting.getFloatValue3(),
                                                   colorSetting.getFloatValue()
                                                );
                                             }

                                             colorSetting.handleRunnable(() -> colorSetting.handleColor(color));
                                          }
                                       }
                                    }
                                 )
                           );
                     }

                     if (phobosClient.getHudManager() != null) {
                        phobosClient.getHudManager()
                           .getColorPickerList()
                           .forEach(
                              hudElement -> hudElement.stream()
                                 .forEach(
                                    setting -> {
                                       if (setting instanceof ColorSetting colorSetting) {
                                          if (colorSetting.isFlag2()) {
                                             colorSetting.handleColor(ColorsModule.colorsModule.color2.getValue());
                                             return;
                                          }

                                          if (colorSetting.isFlag()) {
                                             Color color;
                                             if (colorSetting.isFlag3()) {
                                                color = new Color(
                                                   ColorUtils.computeFloat2(
                                                      0.0F,
                                                      colorSetting.getColor(),
                                                      colorSetting.getFloatValue22(),
                                                      colorSetting.getFloatValue3(),
                                                      colorSetting.getFloatValue()
                                                   )
                                                );
                                             } else {
                                                color = ColorUtils.resolveColor5(
                                                   (int)Math.max(colorSetting.getFloatValue22() * 100.0F * 30.0F, 30.0F),
                                                   0,
                                                   colorSetting.getFloatValue3(),
                                                   colorSetting.getFloatValue()
                                                );
                                             }

                                             colorSetting.handleColor(color);
                                          }
                                       }
                                    }
                                 )
                           );
                     }
                  }
               }
            }
         );
      }
   }
}
