package org.phobos.core.gui.screen;

import com.google.gson.JsonElement;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.phobos.api.GuiGraphics;
import org.phobos.api.JsonSerializable;
import org.phobos.core.gui.BaseScreen;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.gui.ScreenDrawContext;
import org.phobos.core.script.ScriptManager;

public class InfoScreen extends BaseScreen<InfoScreen> implements JsonSerializable {
   public final GuiGraphics guiGraphics = new ScreenDrawContext();
   public final InfoPanel infoPanel;
   public static InfoScreen infoScreen = new InfoScreen();
   public final InfoGuiManager infoGuiManager = new InfoGuiManager(this.guiGraphics);

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.infoPanel.handleJsonElement(jsonElement);
   }

   public void init() {
      if (MenuNavigation.baseScreen == this) {
         this.infoPanel.close();
      }

      MenuNavigation.baseScreen = this;
      ScriptManager.run9();
      this.infoPanel.run5();
   }

   @Override
   public void handleDrawContext(DrawContext drawContext, int i, int i2, float f) {
      this.guiGraphics.setDrawContext(drawContext);
      this.guiGraphics.setIntValue2(0);
      this.start();
      this.infoGuiManager.handleDouble2(i, i2);
      this.run4();
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   public InfoScreen() {
      super(Text.literal(""));
      Supplier<Float> supplier = () -> 54.0F;
      Supplier<Float> supplier2 = () -> (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 - 217.0);
      this.infoPanel = this.infoGuiManager.resolveGuiPanel("Store");
      this.infoPanel.setFloatValue4(15.0F);
      this.infoPanel.setFloatValue2(supplier2.get());
      this.infoPanel.setFloatValueSupplier(supplier2);
      this.infoPanel.setFloatValue(supplier.get());
      this.infoPanel.setFloatValueSupplier2(supplier);
   }

   public InfoScreen getScriptScreen() {
      return this;
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return this.infoPanel.createJsonPrimitive();
   }
}
