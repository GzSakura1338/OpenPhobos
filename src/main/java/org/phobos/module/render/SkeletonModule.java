package org.phobos.module.render;

import java.awt.Color;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SkeletonRenderTransformedWorldListener;
import org.phobos.module.Module;

public class SkeletonModule extends Module {
   public Setting<Color> colorSetting;
   public Setting<Color> colorSetting2;
   public Setting<Color> colorSetting3;

   public void handleMatrixStack(MatrixStack matrixStack, ModelPart modelPart) {
      if (modelPart.roll != 0.0F) {
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(modelPart.roll));
      }

      if (modelPart.yaw != 0.0F) {
         matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotation(modelPart.yaw));
      }

      if (modelPart.pitch != 0.0F) {
         matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(modelPart.pitch));
      }
   }

   public SkeletonModule(PhobosClient phobosClient) {
      super(phobosClient, "Skeleton", Categories.render, "Displays bones");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Color var3 = Color.WHITE;
      ColorSetting var2 = new ColorSetting("Color", var3);
      ColorSetting var4 = var2.getColorSetting();
      Setting var5 = var4.resolveSetting6(this);
      this.colorSetting = var5;
      Color var7 = Color.CYAN;
      ColorSetting var6 = new ColorSetting("Friend", var7);
      Setting var8 = var6.resolveSetting6(this);
      this.colorSetting3 = var8;
      Color var10 = Color.RED;
      ColorSetting var9 = new ColorSetting("Enemy", var10);
      Setting var11 = var9.resolveSetting6(this);
      this.colorSetting2 = var11;
      SkeletonRenderTransformedWorldListener var12 = new SkeletonRenderTransformedWorldListener(this);
      this.handleListener(var12);
   }
}
