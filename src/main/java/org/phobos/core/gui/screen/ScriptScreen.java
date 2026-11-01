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
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.gui.ScreenDrawContext;
import org.phobos.core.script.Script;
import org.phobos.core.script.ScriptManager;

public class ScriptScreen extends BaseScreen<ScriptScreen> implements MinecraftHolder, JsonSerializable {
   public static ScriptScreen scriptScreen = new ScriptScreen();
   public final ScriptGuiManager scriptGuiManager;
   public final ScriptPanel scriptPanel;
   public final ScriptPanel scriptPanel2;
   public final GuiGraphics guiGraphics = new ScreenDrawContext();

   public ScriptScreen() {
      super(Text.literal(""));
      this.scriptGuiManager = new ScriptGuiManager(this.guiGraphics);
      Supplier<Float> supplier = () -> 54.0F;
      Supplier<Float> supplier2 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 - 217.0);
      this.scriptPanel = this.scriptGuiManager.resolveScriptPanel("Internal", false);
      this.scriptPanel.setFloatValue4(15.0F);
      this.scriptPanel.setFloatValue2(supplier2.get());
      this.scriptPanel.setFloatValueSupplier(supplier2);
      this.scriptPanel.setFloatValue(supplier.get());
      this.scriptPanel.setFloatValueSupplier2(supplier);
      Supplier<Float> supplier3 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 + 1.0);
      this.scriptPanel2 = this.scriptGuiManager.resolveScriptPanel("Cloud", true);
      this.scriptPanel2.setFloatValue4(15.0F);
      this.scriptPanel2.setFloatValue2(supplier3.get());
      this.scriptPanel2.setFloatValueSupplier(supplier3);
      this.scriptPanel2.setFloatValue(supplier.get());
      this.scriptPanel2.setFloatValueSupplier2(supplier);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(this.scriptPanel.getName(), this.scriptPanel.createJsonPrimitive());
      jsonObject.add(this.scriptPanel2.getName(), this.scriptPanel2.createJsonPrimitive());
      return jsonObject;
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   public void run2() {
      this.scriptPanel.run2();
      this.scriptPanel2.run2();
   }

   public void clear() {
      this.scriptPanel2.getGuiElementList2().clear();
   }

   public void handleScript(Script script) {
      this.scriptPanel
         .getGuiElementList2()
         .removeIf(
            guiElement -> guiElement instanceof ScriptEntryElement scriptEntryElement && scriptEntryElement.getScript().equals(script)
         );
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      JsonElement jsonElement2 = jsonObject.get(this.scriptPanel.getName());
      JsonElement jsonElement3 = jsonObject.get(this.scriptPanel2.getName());
      if (jsonElement2 != null) {
         this.scriptPanel.handleJsonElement(jsonElement2);
      }

      if (jsonElement3 != null) {
         this.scriptPanel2.handleJsonElement(jsonElement3);
      }
   }

   public ScriptScreen getScriptScreen() {
      return this;
   }

   public boolean testGuiPanel(GuiPanel guiPanel, double d, double d2) {
      float f = guiPanel.getFloatValue2();
      float f2 = guiPanel.getFloatValue();
      float f3 = f + guiPanel.getFloatValue3();
      float f4 = f2 + guiPanel.getFloatValue6();
      return d >= f && d <= f3 && d2 >= f2 && d2 <= f4;
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   @Override
   public void handleDrawContext(DrawContext drawContext, int i, int i2, float f) {
      this.guiGraphics.setDrawContext(drawContext);
      this.guiGraphics.setIntValue2(0);
      this.start();
      this.scriptGuiManager.handleDouble2(i, i2);
      this.run4();
   }

   public boolean shouldPause() {
      return false;
   }

   public void handleScript2(Script script) {
      ScriptEntryElement scriptEntryElement = new ScriptEntryElement(
         this.scriptGuiManager,
         this.scriptPanel,
         this.scriptPanel.getFloatValue2() + 2.0F,
         this.scriptPanel.getFloatValue(),
         212.0F,
         15.0F,
         script
      );
      scriptEntryElement.run5();
      if (script.isFlag2()) {
         this.scriptPanel2.handleGuiElement(scriptEntryElement);
      } else {
         this.scriptPanel.handleGuiElement(scriptEntryElement);
      }
   }

   public void init() {
      if (MenuNavigation.baseScreen == this) {
         this.scriptPanel.close();
         this.scriptPanel2.close();
      }

      MenuNavigation.baseScreen = this;
      ScriptManager.run9();
      this.scriptPanel.run5();
      this.scriptPanel2.run5();
   }

   public void handleIterable(Iterable<Script> iterable) {
      this.scriptPanel.getGuiElementList2().clear();
      iterable.forEach(this::handleScript2);
   }

   public List<GuiPanel> getGuiPanelList() {
      return new ArrayList<>(List.of(this.scriptPanel, this.scriptPanel2));
   }

   public ScriptGuiManager getScriptGuiManager() {
      return this.scriptGuiManager;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.scriptPanel.handleDouble3(mouseX, mouseY, button);
      this.scriptPanel2.handleDouble3(mouseX, mouseY, button);
      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      this.scriptPanel.handleInt2(keyCode, scanCode, modifiers);
      this.scriptPanel2.handleInt2(keyCode, scanCode, modifiers);
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.testGuiPanel(this.scriptPanel, mouseX, mouseY)) {
         float f = this.scriptPanel.getFloatValue6();
         float f2 = this.height - (this.scriptPanel.getFloatValue() + this.scriptPanel.getFloatValue4());
         if (verticalAmount > 0.0 && this.scriptPanel.getFloatAnimator9().getFloatValue() <= -6.0F) {
            this.scriptPanel
               .getFloatAnimator9()
               .setFloatValue(MathUtils.computeFloat3(this.scriptPanel.getFloatAnimator9().getFloatValue() + 15.0F, Float.MIN_VALUE, 0.0F));
         } else if (verticalAmount < 0.0 && f > f2 && this.scriptPanel.getFloatAnimator9().getFloatValue() + f > f2 - 26.0F) {
            this.scriptPanel.getFloatAnimator9().handleFloat2(-15.0F);
         }
      }

      if (this.testGuiPanel(this.scriptPanel2, mouseX, mouseY)) {
         float f3 = this.scriptPanel2.getFloatValue6();
         float f4 = this.height - (this.scriptPanel2.getFloatValue() + this.scriptPanel2.getFloatValue4());
         if (verticalAmount > 0.0 && this.scriptPanel2.getFloatAnimator9().getFloatValue() <= -6.0F) {
            this.scriptPanel2
               .getFloatAnimator9()
               .setFloatValue(MathUtils.computeFloat3(this.scriptPanel2.getFloatAnimator9().getFloatValue() + 15.0F, Float.MIN_VALUE, 0.0F));
         } else if (verticalAmount < 0.0 && f3 > f4 && this.scriptPanel.getFloatAnimator9().getFloatValue() + f3 > f4 - 26.0F) {
            this.scriptPanel2.getFloatAnimator9().handleFloat2(-15.0F);
         }
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Override
   public void close() {
      super.close();
      this.scriptPanel.close();
      this.scriptPanel2.close();
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.scriptPanel.handleDouble8(mouseX, mouseY, button);
      this.scriptPanel2.handleDouble8(mouseX, mouseY, button);
      return super.mouseReleased(mouseX, mouseY, button);
   }

   public ScriptPanel getScriptPanel() {
      return this.scriptPanel;
   }

   public boolean charTyped(char chr, int modifiers) {
      this.scriptPanel.handleChar2(chr, modifiers);
      this.scriptPanel2.handleChar2(chr, modifiers);
      return super.charTyped(chr, modifiers);
   }

   public ScriptPanel getScriptPanel2() {
      return this.scriptPanel2;
   }
}
