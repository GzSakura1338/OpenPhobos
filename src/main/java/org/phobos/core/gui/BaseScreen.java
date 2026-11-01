package org.phobos.core.gui;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.phobos.module.client.UIModule;
import org.phobos.core.ClientHolder;

public abstract class BaseScreen<T extends Screen> extends Screen {
   public boolean flag = false;
   public boolean flag2 = false;

   public abstract void handleDrawContext(DrawContext var1, int var2, int var3, float var4);

   public boolean isFlag() {
      return this.flag;
   }

   public T resolveScreen(boolean flag) {
      this.flag = flag;
      return this.getScriptScreen();
   }

   public void close() {
      super.close();
      this.flag = false;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public T getScreen() {
      return this.resolveScreen(true);
   }

   public abstract T getScriptScreen();

   public T resolveScreen2(BaseScreen<?> baseScreen) {
      return this.resolveScreen(baseScreen != null && baseScreen.flag);
   }

   public boolean isFlag22() {
      return this.flag2;
   }

   public void handleMatrixStack(MatrixStack matrixStack) {
      BackgroundEffects.backgroundEffects.handleMatrixStack(matrixStack);
   }

   public BaseScreen(Text text) {
      super(text);
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.flag2 = false;
      if (this.flag) {
         context.fill(0, 0, this.width, this.height, Color.BLACK.hashCode());
      }

      if (UIModule.getUIModule().effects.getValue()) {
         this.handleMatrixStack(context.getMatrices());
      }

      ClientHolder.getPhobosClient().getMenuNavigation().handleDrawContext(context);
      this.handleDrawContext(context, mouseX, mouseY, delta);
   }

   public boolean alwaysFalse() {
      return false;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }
}
