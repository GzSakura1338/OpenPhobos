package org.phobos.api;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;

public interface GuiGraphics {
   boolean isFlag5();

   double computeText(String var1);

   double computeText5(String var1);

   void setFlag(boolean var1);

   void setIntValue2(int var1);

   void setDrawContext(DrawContext var1);

   void handleText(String var1, float var2, float var3, Color var4, float var5, boolean var6);

   boolean testInt(int var1);

   void handleDouble12(double var1, double var3, double var5, double var7, Color var9);

   DrawContext getDrawContext();

   boolean testInt2(int var1);

   int getIntValue2();

   void handleDouble10(double var1, double var3, double var5, double var7, Color var9);
}
