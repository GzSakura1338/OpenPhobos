package org.phobos.core.gui.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.phobos.api.GuiGraphics;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.JsonSerializable;
import org.phobos.core.gui.BaseScreen;
import org.phobos.core.gui.element.BooleanElement;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.proxy.ProxyConfig;
import org.phobos.core.proxy.ProxyManager;
import org.phobos.core.gui.ScreenDrawContext;
import org.phobos.core.gui.element.StringElement;
import org.phobos.core.setting.StringSetting;

public class ProxyScreen extends BaseScreen<ProxyScreen> implements MinecraftHolder, JsonSerializable {
   public final GuiGraphics guiGraphics = new ScreenDrawContext();
   public final ProxyGuiManager proxyGuiManager = new ProxyGuiManager(this.guiGraphics);
   public final ProxyPanel proxyPanel;
   public final ProxyPanel proxyPanel2;
   public static ProxyScreen proxyScreen = new ProxyScreen();

   public void run() {
      this.proxyPanel.getGuiElementList2().clear();
   }

   @Override
   public void handleDrawContext(DrawContext drawContext, int i, int i2, float f) {
      this.guiGraphics.setDrawContext(drawContext);
      this.guiGraphics.setIntValue2(0);
      this.start();
      this.proxyGuiManager.handleDouble2(i, i2);
      this.run4();
   }

   public void run3() {
      this.proxyPanel2.getGuiElementList2().removeIf(guiElement -> guiElement instanceof ProxyEntryElement);
   }

   public boolean shouldPause() {
      return false;
   }

   public void handleIterable(Iterable<ProxyConfig> iterable) {
      this.proxyPanel2.getGuiElementList2().clear();
      iterable.forEach(this::handleProxyConfig);
   }

   public void run2() {
      this.proxyPanel2.run2();
      this.proxyPanel.run2();
   }

   public List<GuiPanel> getGuiPanelList() {
      return new ArrayList<>(List.of(this.proxyPanel2, this.proxyPanel));
   }

   public ProxyScreen getScriptScreen() {
      return this;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      this.proxyPanel2.handleInt2(keyCode, scanCode, modifiers);
      this.proxyPanel.handleInt2(keyCode, scanCode, modifiers);
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   public void handleProxyConfig(ProxyConfig proxyConfig) {
      ProxyEntryElement proxyEntryElement = new ProxyEntryElement(
         this.proxyGuiManager,
         this.proxyPanel2,
         this.proxyPanel2.getFloatValue2() + 2.0F,
         this.proxyPanel2.getFloatValue(),
         this.proxyPanel2.getFloatValue5() - 4.0F,
         15.0F,
         proxyConfig
      );
      proxyEntryElement.run5();
      if (proxyConfig.isFlag2()) {
         this.proxyPanel.handleGuiElement(proxyEntryElement);
      } else {
         this.proxyPanel2.handleGuiElement(proxyEntryElement);
      }
   }

   public void init() {
      if (MenuNavigation.baseScreen == this) {
         this.proxyPanel2.close();
         this.proxyPanel.close();
      }

      MenuNavigation.baseScreen = this;
      ProxyManager.run9();
      this.proxyPanel2.run5();
      this.proxyPanel.run5();
   }

   public ProxyScreen() {
      super(Text.literal(""));
      Supplier<Float> supplier = () -> 54.0F;
      Supplier<Float> supplier2 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 - 217.0);
      this.proxyPanel2 = this.proxyGuiManager.resolveProxyPanel("Internal", false);
      this.proxyPanel2
         .handleGuiElement2(
            new StringElement(
               this.proxyGuiManager,
               this.proxyPanel2,
               0.0F,
               0.0F,
               212.0F,
               15.0F,
               null,
               "Register",
               new StringSetting("Register", "name -> type -> address:port?@username:password")
            ) {
               public void setValue(String text) {
                  if (text.contains("->")) {
                     try {
                        String[] texts4 = text.split("->");
                        String text6 = texts4[0].trim();
                        String text7 = texts4[1].trim();
                        if (text.contains("@")) {
                           String[] texts = texts4[2].trim().split("@");
                           String[] texts2 = texts[0].split(":");
                           String[] texts3 = texts[1].split(":");
                           String text2 = texts2[0];
                           String text3 = texts2[1];
                           String text4 = texts3[0];
                           String text5 = texts3[1];
                           ClientHolder.getPhobosClient().getProxyManager().handleText(text6, text2, text7, text3, text4, text5);
                           return;
                        }

                        String[] texts5 = texts4[2].split(":");
                        String text8 = texts5[0];
                        String text9 = texts5[1];
                        ClientHolder.getPhobosClient().getProxyManager().handleText(text6, text8, text7, text9, null, null);
                     } catch (Throwable var12) {
                     }
                  }
               }
            }
         );
      this.proxyPanel2
         .handleGuiElement2(
            new BooleanElement(
               this.proxyGuiManager, this.proxyPanel2, 0.0F, 0.0F, 212.0F, 15.0F, null, "Force-Detach", new BooleanSetting("Force-Detach") {
                  public void setValue(Boolean booleanValue) {
                     super.setValue(booleanValue);
                     if (booleanValue) {
                        ClientHolder.getPhobosClient().getProxyManager().run2();
                        this.setValue(false);
                     }
                  }
               }
            )
         );
      this.proxyPanel2.setFloatValue4(15.0F);
      this.proxyPanel2.setFloatValue2(supplier2.get());
      this.proxyPanel2.setFloatValueSupplier(supplier2);
      this.proxyPanel2.setFloatValue(supplier.get());
      this.proxyPanel2.setFloatValueSupplier2(supplier);
      Supplier<Float> supplier3 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 + 1.0);
      this.proxyPanel = this.proxyGuiManager.resolveProxyPanel("Cloud", true);
      this.proxyPanel.setFloatValue4(15.0F);
      this.proxyPanel.setFloatValue2(supplier3.get());
      this.proxyPanel.setFloatValueSupplier(supplier3);
      this.proxyPanel.setFloatValue(supplier.get());
      this.proxyPanel.setFloatValueSupplier2(supplier);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(this.proxyPanel2.getName(), this.proxyPanel2.createJsonPrimitive());
      jsonObject.add(this.proxyPanel.getName(), this.proxyPanel.createJsonPrimitive());
      return jsonObject;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      JsonElement jsonElement2 = jsonObject.get(this.proxyPanel2.getName());
      JsonElement jsonElement3 = jsonObject.get(this.proxyPanel.getName());
      if (jsonElement2 != null) {
         this.proxyPanel2.handleJsonElement(jsonElement2);
      }

      if (jsonElement3 != null) {
         this.proxyPanel.handleJsonElement(jsonElement3);
      }
   }

   public boolean testGuiPanel(GuiPanel guiPanel, double d, double d2) {
      float f = guiPanel.getFloatValue2();
      float f2 = guiPanel.getFloatValue();
      float f3 = f + guiPanel.getFloatValue3();
      float f4 = f2 + guiPanel.getFloatValue6();
      return d >= f && d <= f3 && d2 >= f2 && d2 <= f4;
   }

   public boolean shouldCloseOnEsc() {
      return true;
   }

   @Override
   public void close() {
      super.close();
      this.proxyPanel2.close();
      this.proxyPanel.close();
   }

   public ProxyPanel getProxyPanel2() {
      return this.proxyPanel2;
   }

   public ProxyPanel getProxyPanel() {
      return this.proxyPanel;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.proxyPanel2.handleDouble3(mouseX, mouseY, button);
      this.proxyPanel.handleDouble3(mouseX, mouseY, button);
      return super.mouseClicked(mouseX, mouseY, button);
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public boolean charTyped(char chr, int modifiers) {
      this.proxyPanel2.handleChar2(chr, modifiers);
      this.proxyPanel.handleChar2(chr, modifiers);
      return super.charTyped(chr, modifiers);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.proxyPanel2.handleDouble8(mouseX, mouseY, button);
      this.proxyPanel.handleDouble8(mouseX, mouseY, button);
      return super.mouseReleased(mouseX, mouseY, button);
   }

   public ProxyGuiManager getProxyGuiManager() {
      return this.proxyGuiManager;
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.testGuiPanel(this.proxyPanel2, mouseX, mouseY)) {
         float f = this.proxyPanel2.getFloatValue6();
         float f2 = this.height - (this.proxyPanel2.getFloatValue() + this.proxyPanel2.getFloatValue4());
         if (verticalAmount > 0.0 && this.proxyPanel2.getFloatAnimator9().getFloatValue() <= -6.0F) {
            this.proxyPanel2
               .getFloatAnimator9()
               .setFloatValue(MathUtils.computeFloat3(this.proxyPanel2.getFloatAnimator9().getFloatValue() + 15.0F, Float.MIN_VALUE, 0.0F));
         } else if (verticalAmount < 0.0 && f > f2 && this.proxyPanel2.getFloatAnimator9().getFloatValue() + f > f2 - 26.0F) {
            this.proxyPanel2.getFloatAnimator9().handleFloat2(-15.0F);
         }
      }

      if (this.testGuiPanel(this.proxyPanel, mouseX, mouseY)) {
         float f3 = this.proxyPanel.getFloatValue6();
         float f4 = this.height - (this.proxyPanel.getFloatValue() + this.proxyPanel.getFloatValue4());
         if (verticalAmount > 0.0 && this.proxyPanel.getFloatAnimator9().getFloatValue() <= -6.0F) {
            this.proxyPanel.getFloatAnimator9().setFloatValue(Math.max(this.proxyPanel.getFloatAnimator9().getFloatValue() + 15.0F, 0.0F));
         } else if (verticalAmount < 0.0 && f3 > f4 && this.proxyPanel.getFloatAnimator9().getFloatValue() + f3 > f4 - 26.0F) {
            this.proxyPanel.getFloatAnimator9().handleFloat2(-15.0F);
         }
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
}
