package org.phobos.core.gui.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.PanelHolder;
import org.phobos.api.GuiGraphics;
import org.phobos.api.JsonSerializable;
import org.phobos.api.ConfigInfo;
import org.phobos.core.gui.BaseScreen;
import org.phobos.core.ClientHolder;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.config.ConfigMeta;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.gui.PanelRegistry;
import org.phobos.core.gui.ScreenDrawContext;

public class ConfigScreen extends BaseScreen<ConfigScreen> implements JsonSerializable, PanelHolder {
   public final ConfigPanel configPanel;
   public final ConfigPanel configPanel2;
   public boolean flag3 = true;
   public final GuiGraphics guiGraphics = new ScreenDrawContext();
   public final ConfigGuiManager configGuiManager = new ConfigGuiManager(this.guiGraphics);
   public static ConfigScreen configScreen = new ConfigScreen();

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(this.configPanel2.getName(), this.configPanel2.createJsonPrimitive());
      jsonObject.add(this.configPanel.getName(), this.configPanel.createJsonPrimitive());
      return jsonObject;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      JsonElement jsonElement2 = jsonObject.get(this.configPanel2.getName());
      JsonElement jsonElement3 = jsonObject.get(this.configPanel.getName());
      if (jsonElement2 != null) {
         this.configPanel2.handleJsonElement(jsonElement2);
      }

      if (jsonElement3 != null) {
         this.configPanel.handleJsonElement(jsonElement3);
      }
   }

   public void handleConfigEntryElement(@NotNull ConfigEntryElement configEntryElement) {
      this.configPanel.handleConfigEntryElement(configEntryElement);
   }

   public void run3() {
      this.configPanel2.getGuiElementList2().clear();
   }

   @Nullable
   public ConfigEntryElement resolveConfigEntryElement2(String text) {
      return this.configPanel.resolveConfigEntryElement2(text);
   }

   public ConfigEntryElement resolveConfigEntryElement(ConfigMeta configMeta) {
      ConfigEntryElement configEntryElement = new ConfigEntryElement(
         this.configGuiManager,
         this.configPanel2,
         this.configPanel2.getFloatValue2() + 2.0F,
         this.configPanel2.getFloatValue(),
         this.configPanel2.getFloatValue5() - 4.0F,
         15.0F,
         configMeta
      );
      configEntryElement.run5();
      return this.configPanel2.resolveConfigEntryElement(configEntryElement, configMeta);
   }

   @Override
   public List<GuiPanel> getGuiPanelList() {
      return new ArrayList<>(List.of(this.configPanel2, this.configPanel));
   }

   public ConfigEntryElement resolveConfigEntryElement3(ConfigMeta configMeta) {
      return this.configPanel
         .getGuiElementList2()
         .stream()
         .filter(guiElement -> guiElement instanceof ConfigEntryElement configEntryElement && configEntryElement.testConfigMeta(configMeta))
         .map(guiElement -> (ConfigEntryElement)guiElement)
         .findAny()
         .orElse(null);
   }

   public void run() {
      this.configPanel.getGuiElementList2().clear();
   }

   public void init() {
      if (MenuNavigation.baseScreen == this) {
         this.configPanel2.close();
         this.configPanel.close();
      }

      MenuNavigation.baseScreen = this;
      if (this.flag3) {
         ConfigManager.run9();
         this.flag3 = false;
      }

      this.configPanel2.run5();
      this.configPanel.run5();
   }

   public ConfigEntryElement resolveConfigEntryElement4(ConfigMeta configMeta) {
      ConfigEntryElement configEntryElement = new ConfigEntryElement(
         this.configGuiManager,
         this.configPanel2,
         this.configPanel2.getFloatValue2() + 2.0F,
         this.configPanel2.getFloatValue(),
         this.configPanel2.getFloatValue5() - 4.0F,
         15.0F,
         configMeta
      );
      configEntryElement.run5();
      return this.configPanel.resolveConfigEntryElement(configEntryElement, configMeta);
   }

   public ConfigScreen getScriptScreen() {
      return this;
   }

   public void run2() {
      this.configPanel2.run2();
      this.configPanel.run2();
   }

   public void handleConfigEntryElement2(@NotNull ConfigEntryElement configEntryElement) {
      this.configPanel2.handleConfigEntryElement(configEntryElement);
   }

   @Override
   public void handleDrawContext(DrawContext drawContext, int i, int i2, float f) {
      this.guiGraphics.setFlag(false);
      this.guiGraphics.setDrawContext(drawContext);
      this.guiGraphics.setIntValue2(0);
      this.start();

      try {
         this.configGuiManager.handleDouble2(i, i2);
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }

      this.run4();
      if (!this.guiGraphics.isFlag5()) {
         CursorUtils.run2();
      }
   }

   public boolean testGuiPanel(GuiPanel guiPanel, double d, double d2) {
      float f = guiPanel.getFloatValue2();
      float f2 = guiPanel.getFloatValue();
      float f3 = f + guiPanel.getFloatValue3();
      float f4 = f2 + guiPanel.getFloatValue6();
      return d >= f && d <= f3 && d2 >= f2 && d2 <= f4;
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   public ConfigScreen() {
      super(Text.literal(""));
      Supplier<Float> supplier = () -> 54.0F;
      Supplier<Float> supplier2 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 - 217.0);
      this.configPanel2 = this.configGuiManager.resolveConfigPanel("Internal", false);
      this.configPanel2.setFloatValue4(15.0F);
      this.configPanel2.setFloatValue2(supplier2.get());
      this.configPanel2.setFloatValueSupplier(supplier2);
      this.configPanel2.setFloatValue(supplier.get());
      this.configPanel2.setFloatValueSupplier2(supplier);
      Supplier<Float> supplier3 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 + 1.0);
      this.configPanel = this.configGuiManager.resolveConfigPanel("Cloud", true);
      this.configPanel.setFloatValue4(15.0F);
      this.configPanel.setFloatValue2(supplier3.get());
      this.configPanel.setFloatValueSupplier(supplier3);
      this.configPanel.setFloatValue(supplier.get());
      this.configPanel.setFloatValueSupplier2(supplier);
      PanelRegistry.getINSTANCE().handlePanelHolder(this);
   }

   public void handleIterable(Iterable<ConfigInfo> iterable) {
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.configPanel2.handleDouble3(mouseX, mouseY, button);
      this.configPanel.handleDouble3(mouseX, mouseY, button);
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void close() {
      super.close();
      this.configPanel2.close();
      this.configPanel.close();
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public ConfigPanel getConfigPanel2() {
      return this.configPanel2;
   }

   public ConfigGuiManager getConfigGuiManager() {
      return this.configGuiManager;
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public boolean shouldPause() {
      return false;
   }

   public void handleGuiPanel(GuiPanel guiPanel, double d) {
      if (d > 0.0 && guiPanel.getFloatAnimator9().getFloatValue() < 0.0F) {
         guiPanel.getFloatAnimator9().setFloatValue(Math.min(guiPanel.getFloatAnimator9().getFloatValue() + 15.0F, 0.0F));
      }

      if (d < 0.0) {
         if (guiPanel.check()) {
            float f = guiPanel.getFloatValue6();
            float f2 = this.height - (guiPanel.getFloatValue() + guiPanel.getFloatValue4());
            if (f > f2 && guiPanel.getFloatAnimator9().getFloatValue() + f > f2 - 26.0F) {
               guiPanel.getFloatAnimator9().handleFloat2(-15.0F);
            }
         } else {
            float f3 = guiPanel.getFloatValue33() + guiPanel.getFloatAnimator9().getFloatValue();
            float f4 = guiPanel.getFloatValue6();
            if (f4 < f3) {
               guiPanel.getFloatAnimator9().handleFloat2(-15.0F);
            }
         }
      }
   }

   public boolean shouldCloseOnEsc() {
      return true;
   }

   public ConfigPanel getConfigPanel() {
      return this.configPanel;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      this.configPanel2.handleInt2(keyCode, scanCode, modifiers);
      this.configPanel.handleInt2(keyCode, scanCode, modifiers);
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.testGuiPanel(this.configPanel2, mouseX, mouseY)) {
         this.handleGuiPanel(this.configPanel2, verticalAmount);
      }

      if (this.testGuiPanel(this.configPanel, mouseX, mouseY)) {
         this.handleGuiPanel(this.configPanel, verticalAmount);
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean charTyped(char chr, int modifiers) {
      this.configPanel2.handleChar2(chr, modifiers);
      return super.charTyped(chr, modifiers);
   }
}
