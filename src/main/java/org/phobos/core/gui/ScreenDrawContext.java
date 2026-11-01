package org.phobos.core.gui;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.phobos.api.GuiGraphics;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.UIModule;
import org.phobos.core.ClientHolder;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.RenderUtils;

public class ScreenDrawContext implements GuiGraphics, MinecraftHolder {
   public DrawContext drawContext;
   public int intValue;
   public int intValue2;
   public boolean flag;

   @Override
   public void handleDouble12(double d, double d2, double d3, double d4, Color color) {
      RenderUtils.handleMatrixStack20(this.drawContext.getMatrices(), color, d, d2, d3, d4);
   }

   @Override
   public void handleDouble10(double d, double d2, double d3, double d4, Color color) {
      this.drawContext.drawBorder((int)d, (int)d2, (int)d3, (int)d4, color.hashCode());
   }

   @Override
   public boolean testInt(int i) {
      boolean flag = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), i) == 1;
      if (flag) {
         this.intValue = i;
      }

      return flag;
   }

   public int getIntValue() {
      return this.intValue;
   }

   @Override
   public DrawContext getDrawContext() {
      return this.drawContext;
   }

   @Override
   public void handleText(String text, float f, float f2, Color color, float f3, boolean flag) {
      this.drawContext.getMatrices().push();
      this.drawContext.getMatrices().translate(0.0F, 0.0F, 5.0F);
      if (!UIModule.getUIModule().cPMODE.getValue()) {
         ClientHolder.getPhobosClient().getRenderManager().handleDrawContext(this.drawContext, text, f, f2, color, f3, flag);
      } else {
         String text2 = switch (MathUtils.computeInt2(3)) {
            case 0 -> "CP";
            case 1 -> "FAGGOT";
            default -> "NIGGER";
         };
         ClientHolder.getPhobosClient()
            .getRenderManager()
            .handleDrawContext(this.drawContext, text2, f, f2, Color.RED, MathUtils.computeFloat8(1.5F, 3.0F), flag);
      }

      this.drawContext.getMatrices().pop();
   }

   @Override
   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public double computeText(String text) {
      return ClientHolder.getPhobosClient().getRenderManager().computeText(text);
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   @Override
   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   @Override
   public int getIntValue2() {
      return this.intValue2;
   }

   @Override
   public boolean isFlag5() {
      return this.flag;
   }

   @Override
   public boolean testInt2(int i) {
      if (this.intValue == -1) {
         return false;
      }

      boolean flag = i == this.intValue;
      this.intValue = -1;
      return flag;
   }

   @Override
   public void setDrawContext(DrawContext drawContext) {
      this.drawContext = drawContext;
   }

   @Override
   public double computeText5(String text) {
      return ClientHolder.getPhobosClient().getRenderManager().computeText5(text);
   }
}
