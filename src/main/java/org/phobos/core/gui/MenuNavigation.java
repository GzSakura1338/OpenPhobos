package org.phobos.core.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.Named;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.JsonSerializable;
import org.phobos.event.Listener;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.RenderEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.client.ColorsModule;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.core.gui.screen.ClickGuiScreen;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.screen.ConfigScreen;
import org.phobos.core.Feature;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.screen.HudScreen;
import org.phobos.core.render.texture.ImageTexture;
import org.phobos.core.gui.screen.InfoScreen;
import org.phobos.core.PhobosClient;
import org.phobos.core.gui.screen.ProxyScreen;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.gui.screen.ScriptScreen;

public class MenuNavigation extends Feature implements JsonSerializable, Named {
   public final FloatAnimator floatAnimator;
   public static final ImageTexture imageTexture = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/store.png", true);
   public static BaseScreen<?> baseScreen = null;
   public final FloatAnimator floatAnimator2;
   public final FloatAnimator floatAnimator3 = new FloatAnimator();
   public static final ImageTexture imageTexture2 = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/configs.png", true);
   public static final ImageTexture imageTexture3 = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/hud.png", true);
   public int intValue;
   public final FloatAnimator floatAnimator4;
   public static final ImageTexture imageTexture4 = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/menu.png", true);
   public final FloatAnimator floatAnimator5;
   public final FloatAnimator floatAnimator6;

   public static double value() {
      return mc.mouse.getX() * mc.getWindow().getScaledWidth() / mc.getWindow().getWidth();
   }

   public void handleDrawContext(DrawContext drawContext) {
      this.floatAnimator2
         .setFloatValue(
            mc.currentScreen instanceof HudScreen
               ? 0.0F
               : (mc.currentScreen instanceof ClickGuiScreen ? 20.0F : (mc.currentScreen instanceof ConfigScreen ? 40.0F : 60.0F))
         );
      this.floatAnimator2.handleFloat3(10.0F, 0.1F);
      this.floatAnimator6.setFloatValue(mc.currentScreen instanceof InfoScreen ? 6.0F : 0.0F);
      this.floatAnimator6.handleFloat3(10.0F, 0.05F);
      this.floatAnimator.setFloatValue(mc.currentScreen instanceof HudScreen ? 6.0F : 0.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.05F);
      this.floatAnimator3.handleFloat3(10.0F, 0.1F);
      double d8 = mc.getWindow().getScaledWidth() / 2.0;
      if (this.floatAnimator3.getFloatValue2() >= -15.0F) {
         int i = 80;
         double d = d8 - i / 2.0;
         double d2 = 0.0F + this.floatAnimator3.getFloatValue2();
         double d3 = i;
         double d4 = 20.0;
         MatrixStack matrixStack = drawContext.getMatrices();
         Color color = ColorsModule.getColorsModule().color2.getValue();
         Color color2 = ColorUtils.createColor6(color, 0.2F);
         RenderUtils.handleMatrixStack38(
            matrixStack,
            color2,
            d + this.floatAnimator2.getFloatValue2(),
            d2,
            d + this.floatAnimator2.getFloatValue2() + 20.0,
            d2 + d4,
            0.0F,
            0.0F,
            this.floatAnimator.getFloatValue2(),
            this.floatAnimator6.getFloatValue2(),
            30.0F
         );
         double d5 = 11.6;
         double d6 = d5 / 2.8;
         double d7 = 0.0;
         RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
         imageTexture3.run();
         RenderUtils.handleMatrixStack4(matrixStack, color2, d + d6 + d7, d2 + d6, d5);
         d7 += 20.0;
         imageTexture4.run();
         RenderUtils.handleMatrixStack4(matrixStack, color2, d + d6 + d7, d2 + d6, d5);
         d7 += 20.0;
         imageTexture2.run();
         RenderUtils.handleMatrixStack4(matrixStack, color2, d + d6 + d7, d2 + d6, d5);
         d7 += 20.0;
         imageTexture.run();
         RenderUtils.handleMatrixStack4(matrixStack, color2, d + d6 + d7, d2 + d6, d5);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (!(mc.currentScreen instanceof ClickGuiScreen)
         && !(mc.currentScreen instanceof HudScreen)
         && !(mc.currentScreen instanceof ScriptScreen)
         && !(mc.currentScreen instanceof ConfigScreen)
         && !(mc.currentScreen instanceof ProxyScreen)
         && !(mc.currentScreen instanceof InfoScreen)) {
         this.floatAnimator3.setFloatValue(-20.0F);
      } else if (!(Math.abs(d8 - value()) > 60.0) && !(Math.abs(0.0 - value2()) > 30.0)) {
         if (mc.currentScreen instanceof HudScreen hudScreen && hudScreen.getDragCapture() != null) {
            this.floatAnimator3.setFloatValue(-20.0F);
         } else {
            this.intValue++;
            if (this.intValue >= (mc.currentScreen instanceof HudScreen ? 300 : 100)) {
               this.floatAnimator3.setFloatValue(0.0F);
            }
         }
      } else {
         this.floatAnimator3.setFloatValue(-20.0F);
      }
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      this.handleJsonSerializable(ClickGuiScreen.clickGuiScreen, jsonObject.get("clickgui"));
      this.handleJsonSerializable(ScriptScreen.scriptScreen, jsonObject.get("scripts"));
      this.handleJsonSerializable(ConfigScreen.configScreen, jsonObject.get("configs"));
      this.handleJsonSerializable(InfoScreen.infoScreen, jsonObject.get("store"));
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add("clickgui", ClickGuiScreen.clickGuiScreen.createJsonPrimitive());
      jsonObject.add("scripts", ScriptScreen.scriptScreen.createJsonPrimitive());
      jsonObject.add("configs", ConfigScreen.configScreen.createJsonPrimitive());
      jsonObject.add("store", InfoScreen.infoScreen.createJsonPrimitive());
      return jsonObject;
   }

   public MenuNavigation(PhobosClient phobosClient) {
      this.floatAnimator2 = new FloatAnimator();
      this.floatAnimator6 = new FloatAnimator();
      this.floatAnimator = new FloatAnimator();
      this.floatAnimator4 = new FloatAnimator();
      this.floatAnimator5 = new FloatAnimator();
      this.intValue = 0;
      this.handleListener(
         new SafeListener<RenderEvent2>(-2147483647) {
            public void handleObject(
               RenderEvent2 renderEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.currentScreen == null) {
                  MenuNavigation.this.floatAnimator5.setFloatValue(0.0F);
               }

               MenuNavigation.this.floatAnimator5.handleFloat3(10.0F, 0.1F);
               if (!(mc.currentScreen instanceof BaseScreen)) {
                  MenuNavigation.this.handleDrawContext(renderEvent2.getDrawContext());
               }
            }
         }
      );
      this.handleListener(
         new Listener<OnMouseButtonEvent>() {
            public void handleObject2(OnMouseButtonEvent onMouseButtonEvent) {
               if (onMouseButtonEvent.getKeyboardMouseEnum().equals(KeyboardMouseEnum.Mouse)
                  && onMouseButtonEvent.getIntValue() == 1
                  && !(MenuNavigation.this.floatAnimator3.getFloatValue2() <= -19.0F)
                  && MenuNavigation.this.intValue > 100) {
                  double d = MenuNavigation.value();
                  double d2 = MenuNavigation.value2();
                  double d3 = MinecraftHolder.mc.getWindow().getScaledWidth() / 2.0;
                  double d4 = MenuNavigation.this.floatAnimator3.getFloatValue2();
                  double d5 = 20.0;
                  double d6 = 20.0;
                  double d7 = d3 - 40.0;
                  double d8 = 0.0 + d4;
                  double d9 = d7 + d5;
                  double d10 = d8 + d6;
                  if (d >= d7 && d <= d9 && d2 >= d8 && d2 <= d10) {
                     MinecraftHolder.mc.setScreen(HudScreen.hudScreen.resolveScreen2(MenuNavigation.baseScreen));
                     MenuNavigation.this.floatAnimator3.setFloatValue(-20.0F);
                     MenuNavigation.this.intValue = 0;
                  } else {
                     double d11 = d7 + 20.0;
                     double d12 = 0.0 + d4;
                     double d13 = d11 + d5;
                     double d14 = d12 + d6;
                     if (d >= d11 && d <= d13 && d2 >= d12 && d2 <= d14) {
                        MinecraftHolder.mc.setScreen(ClickGuiScreen.clickGuiScreen.resolveScreen2(MenuNavigation.baseScreen));
                        MenuNavigation.this.floatAnimator3.setFloatValue(-20.0F);
                        MenuNavigation.this.intValue = 0;
                     } else {
                        double d15 = d11 + 20.0;
                        double d16 = 0.0 + d4;
                        double d17 = d15 + d5;
                        double d18 = d16 + d6;
                        if (d >= d15 && d <= d17 && d2 >= d16 && d2 <= d18) {
                           MinecraftHolder.mc.setScreen(ConfigScreen.configScreen.resolveScreen2(MenuNavigation.baseScreen));
                           MenuNavigation.this.floatAnimator3.setFloatValue(-20.0F);
                           MenuNavigation.this.intValue = 0;
                        } else {
                           double d19 = d15 + 20.0;
                           double d20 = 0.0 + d4;
                           double d21 = d19 + d5;
                           double d22 = d20 + d6;
                           if (d >= d19 && d <= d21 && d2 >= d20 && d2 <= d22) {
                              MinecraftHolder.mc.setScreen(InfoScreen.infoScreen.resolveScreen2(MenuNavigation.baseScreen));
                              MenuNavigation.this.floatAnimator3.setFloatValue(-20.0F);
                              MenuNavigation.this.intValue = 0;
                           }
                        }
                     }
                  }
               }
            }
         }
      );
   }

   @Override
   public String getName() {
      return "ui.json";
   }

   public <T extends JsonSerializable> void handleJsonSerializable(T jsonSerializable, JsonElement jsonElement) {
      if (jsonElement != null) {
         try {
            jsonSerializable.handleJsonElement(jsonElement);
         } catch (Exception var4) {
         }
      }
   }

   public static double value2() {
      return mc.mouse.getY() * mc.getWindow().getScaledHeight() / mc.getWindow().getHeight();
   }

   public FloatAnimator getFloatAnimator4() {
      return this.floatAnimator4;
   }

   public FloatAnimator getFloatAnimator5() {
      return this.floatAnimator5;
   }

   public static void handleScreen(Screen screen) {
      mc.setScreen(baseScreen == null ? screen : baseScreen);
   }

   public static boolean check() {
      return mc.currentScreen instanceof ClickGuiScreen || mc.currentScreen instanceof HudScreen || mc.currentScreen instanceof ConfigScreen;
   }
}
