package org.phobos.core.gui.screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.ColorPicker;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.EnumColorPicker;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.InteractiveElement;
import org.phobos.core.gui.NamedColorPicker;
import org.phobos.core.proxy.ProxyConfig;
import org.phobos.core.proxy.ProxyManager;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.user.UserSession;

public class ProxyEntryElement extends InteractiveElement {
   public final List<ColorPicker> colorPickerList = new ArrayList<>();
   public final Color color;
   public boolean flag;
   public boolean flag2;
   public final ProxyConfig proxyConfig;
   public final Color color2 = new Color(70, 70, 70, 70);

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor13(),
         this.getGuiManager().getUiTheme().getUIShader2()
      );
      this.handleText2(this.proxyConfig.getName(), 2.0F, 3.0F, Color.WHITE, true);
      Color color3 = ColorUtils.resolveColor4(this.getGuiManager().getUiTheme().getColor10());
      String text3 = this.proxyConfig.getText2();
      if (text3 != null) {
         String text = "Author: " + text3;
         double d3 = this.computeText5(text);
         this.handleText2(text, (float)(this.getFloatValue3() - d3 - 3.0), 3.0F, color3, true);
      }

      if (this.getFloatValue13().getFloatValue2() >= 0.05) {
         this.flag = true;
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         float f = 0.0F;

         for (ColorPicker colorPicker : this.colorPickerList) {
            boolean flag = colorPicker.testDouble(d, d2);
            colorPicker.setFloatValue(this.getFloatValue2() + 2.0F);
            colorPicker.setFloatValue3(this.getFloatValue() + this.getFloatValue4() + 2.0F + f);
            colorPicker.setFloatValue4(this.getFloatValue3() - 4.0F);
            colorPicker.setFloatValue2(this.getFloatValue4());
            colorPicker.run();
            if (colorPicker instanceof NamedColorPicker namedColorPicker) {
               this.handleText2(namedColorPicker.getName(), 4.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag);
               Color color = namedColorPicker.getAnimatedColor().createColor();
               RenderUtils.handleMatrixStack12(
                  this.getMatrixStack(),
                  color,
                  namedColorPicker.getFloatValue(),
                  namedColorPicker.getFloatValue3(),
                  namedColorPicker.getFloatValue() + namedColorPicker.getFloatValue4(),
                  namedColorPicker.getFloatValue3() + namedColorPicker.getFloatValue2(),
                  0.0F,
                  () -> this.handleDouble9(
                     namedColorPicker.getFloatValue(),
                     namedColorPicker.getFloatValue3(),
                     namedColorPicker.getFloatValue4(),
                     namedColorPicker.getFloatValue2(),
                     color,
                     this.getGuiManager().getUiTheme().getUIShader2()
                  )
               );
            }

            if (colorPicker instanceof EnumColorPicker<?> enumColorPicker) {
               Color color2 = enumColorPicker.getAnimatedColor().createColor();
               RenderUtils.handleMatrixStack12(
                  this.getMatrixStack(),
                  color2,
                  enumColorPicker.getFloatValue(),
                  enumColorPicker.getFloatValue3(),
                  enumColorPicker.getFloatValue() + enumColorPicker.getFloatValue4(),
                  enumColorPicker.getFloatValue3() + enumColorPicker.getFloatValue2(),
                  0.0F,
                  () -> this.handleDouble9(
                     enumColorPicker.getFloatValue(),
                     enumColorPicker.getFloatValue3(),
                     enumColorPicker.getFloatValue4(),
                     enumColorPicker.getFloatValue2(),
                     color2,
                     this.getGuiManager().getUiTheme().getUIShader2()
                  )
               );
               this.handleText2(enumColorPicker.getName(), 4.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag);
               String text2 = enumColorPicker.getEnumValue().equals(PUBLICPRIVATEEnum.PUBLIC) ? "Public" : "Private";
               this.handleText2(
                  text2, (float)(enumColorPicker.getFloatValue4() - this.computeText5(text2)) - 2.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag
               );
            }

            f += this.getFloatValue4() + 2.0F;
         }

         this.run3();
      } else {
         if (this.flag) {
            this.flag = false;

            for (ColorPicker colorPicker2 : this.colorPickerList) {
               colorPicker2.getAnimatedColor().handleColor3(this.color);
            }
         }
      }
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? this.value2() : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
   }

   public boolean isFlag3() {
      return this.flag2;
   }

   public ProxyEntryElement(GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, ProxyConfig proxyConfig) {
      super(guiManager, guiPanel, f, f2, f3, f4, null);
      this.color = new Color(0, 0, 0, 0);
      this.proxyConfig = proxyConfig;
      this.colorPickerList
         .add(
            new NamedColorPicker("Attach") {
               @Override
               public void run2() {
                  if (!ClientHolder.getPhobosClient().getProxyManager().testProxyConfig(proxyConfig)) {
                     ClientHolder.getPhobosClient().getProxyManager().handleProxyConfig(proxyConfig);
                  }
               }

               @Override
               public void run() {
                  this.animatedColor
                     .handleColor2(
                        !ClientHolder.getPhobosClient().getProxyManager().testProxyConfig(proxyConfig)
                           ? ProxyEntryElement.this.getGuiManager().getUiTheme().getColor13()
                           : ProxyEntryElement.this.getGuiManager().getUiTheme().getColor9()
                     );
                  this.animatedColor.handleFloat(10.0F, 0.1F);
               }
            }
         );
      this.colorPickerList
         .add(
            new NamedColorPicker("Detach") {
               @Override
               public void run() {
                  this.animatedColor
                     .handleColor2(
                        ClientHolder.getPhobosClient().getProxyManager().testProxyConfig(proxyConfig)
                           ? ProxyEntryElement.this.getGuiManager().getUiTheme().getColor13()
                           : ProxyEntryElement.this.getGuiManager().getUiTheme().getColor9()
                     );
                  this.animatedColor.handleFloat(10.0F, 0.1F);
               }

               @Override
               public void run2() {
                  if (ClientHolder.getPhobosClient().getProxyManager().testProxyConfig(proxyConfig)) {
                     ClientHolder.getPhobosClient().getProxyManager().handleProxyConfig3(proxyConfig);
                  }
               }
            }
         );
      if (proxyConfig.isFlag2()) {
         assert proxyConfig.getText2() != null;
         if (proxyConfig.getText2().equalsIgnoreCase(UserSession.userSession.getText())
            || UserSession.userSession.getUserProfile().getPermission().isAdmin()) {
            this.colorPickerList
               .add(
                  new EnumColorPicker<PUBLICPRIVATEEnum>("Visibility", proxyConfig.getPUBLICPRIVATEEnum()) {
                     @Override
                     public void run() {
                        this.animatedColor.handleColor2(ProxyEntryElement.this.getGuiManager().getUiTheme().getColor13());
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }

                     @Override
                     public void run3() {
                        ClientHolder.getPhobosClient()
                           .getCloudConfigApi()
                           .handleText4(
                              proxyConfig.getName(),
                              ConfigScriptEnum.Proxy,
                              this.getEnumValue(),
                              ProxyManager::run9,
                              ProxyEntryElement.this.getGuiPanel()::run
                           );
                     }
                  }
               );
            this.colorPickerList
               .add(
                  new NamedColorPicker("Delete") {
                     @Override
                     public void run() {
                        this.animatedColor
                           .handleColor2(
                              ColorUtils.createColor6(Color.RED, ProxyEntryElement.this.getGuiManager().getUiTheme().getColor9().getAlpha() / 255.0F)
                           );
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }

                     @Override
                     public void run2() {
                        ClientHolder.getPhobosClient()
                           .getCloudConfigApi()
                           .handleText6(
                              proxyConfig.getName(), ConfigScriptEnum.Proxy, ProxyManager::run9, ProxyEntryElement.this.getGuiPanel()::run
                           );
                     }
                  }
               );
         }
      } else {
         this.colorPickerList
            .add(
               new NamedColorPicker("Publish") {
                  @Override
                  public void run() {
                     this.animatedColor.handleColor2(ProxyEntryElement.this.getGuiManager().getUiTheme().getColor9());
                     this.animatedColor.handleFloat(10.0F, 0.1F);
                  }

                  @Override
                  public void run2() {
                     ClientHolder.getPhobosClient()
                        .getCloudConfigApi()
                        .handleText3(
                           proxyConfig.getName(),
                           ConfigScriptEnum.Proxy,
                           PUBLICPRIVATEEnum.PRIVATE,
                           proxyConfig.createJsonPrimitive().toString(),
                           ProxyManager::run9,
                           ProxyEntryElement.this.getGuiPanel()::run
                        );
                  }
               }
            );
      }
   }

   public boolean isFlag2() {
      return this.flag;
   }

   public Color getColor2() {
      return this.color2;
   }

   public Color getColor() {
      return this.color;
   }

   public ProxyConfig getProxyConfig() {
      return this.proxyConfig;
   }

   public List<ColorPicker> getColorPickerList() {
      return this.colorPickerList;
   }

   public float value2() {
      float f = 0.0F;

      for (ColorPicker colorPicker : this.colorPickerList) {
         f += colorPicker.getFloatValue2() + 2.0F;
      }

      return f;
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (!this.isFlag10()) {
               return;
            }

            for (ColorPicker colorPicker : this.colorPickerList) {
               colorPicker.handleDouble(d, d2);
            }
            break;
         case 1:
            if (this.isFlag7()) {
               this.setFlag10(!this.isFlag10());
            }
      }
   }
}
