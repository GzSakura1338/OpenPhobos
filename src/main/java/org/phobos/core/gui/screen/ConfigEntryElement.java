package org.phobos.core.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.phobos.api.ConfigInfo;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.ClipStack;
import org.phobos.core.gui.ColorPicker;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.config.ConfigMeta;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.gui.EnumColorPicker;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.HoverRegion;
import org.phobos.core.render.texture.ImageTexture;
import org.phobos.core.gui.InteractiveElement;
import org.phobos.core.gui.NamedColorPicker;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.user.UserSession;

public class ConfigEntryElement extends InteractiveElement {
   public boolean flag;
   public final List<ColorPicker> colorPickerList = new ArrayList<>();
   public final Color color;
   public boolean flag2;
   public final Color color2;
   public static final ImageTexture imageTexture = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/unpinned.png", true);
   public final FloatAnimator floatAnimator;
   public final ConfigMeta configMeta;
   public static final ImageTexture imageTexture2 = ImageTexture.resolveImageTexture2("phoboslite", "textures/ui/pinned.png", true);
   public ConfigInfo configInfo;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();

   public float value2() {
      float f = 0.0F;

      for (ColorPicker colorPicker : this.colorPickerList) {
         f += colorPicker.getFloatValue2() + 2.0F;
      }

      return f;
   }

   @Override
   public HoverRegion getHoverRegion() {
      return HoverRegion.createHoverRegion(this.getFloatValue2() + 2.0F, this.getFloatValue() + 2.0F, 8.0F, 8.0F);
   }

   public boolean isFlag3() {
      return this.flag2;
   }

   public void run() {
      if (this.configInfo != null && !this.configInfo.isFlag2()) {
         if (ConfigManager.getConfigManager().testConfigInfo(this.configInfo)) {
            this.colorPickerList.removeIf(colorPicker -> colorPicker instanceof NamedColorPicker namedColorPicker && namedColorPicker.getName().equals("Update"));
            this.colorPickerList
               .add(
                  new NamedColorPicker("Update") {
                     @Override
                     public void run2() {
                        ClientHolder.getPhobosClient()
                           .getCloudConfigApi()
                           .handleText5(ConfigEntryElement.this.configInfo.getName(), ConfigEntryElement.this.configInfo.getText());
                     }

                     @Override
                     public void run() {
                        this.animatedColor
                           .handleColor2(
                              this.isFlag()
                                 ? ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9().brighter()
                                 : ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9()
                           );
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }
                  }
               );
         }

         if (!ConfigManager.getConfigManager().testConfigInfo2(this.configInfo)) {
            this.colorPickerList
               .add(
                  new NamedColorPicker("Publish") {
                     @Override
                     public void run() {
                        this.animatedColor
                           .handleColor2(
                              this.isFlag()
                                 ? ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9().brighter()
                                 : ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9()
                           );
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }

                     @Override
                     public void run2() {
                        if (ConfigEntryElement.this.configInfo != null) {
                           ClientHolder.getPhobosClient()
                              .getCloudConfigApi()
                              .handleText3(
                                 ConfigEntryElement.this.configInfo.getName(),
                                 ConfigScriptEnum.Config,
                                 PUBLICPRIVATEEnum.PRIVATE,
                                 ConfigEntryElement.this.configInfo.getText()
                              );
                        }
                     }
                  }
               );
         }
      }
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (this.configInfo != null
               && this.configInfo.isFlag2()
               && this.isFlag7()
               && UserSession.flag2
               && this.testDouble2(d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + 2.0F, 8.0, 8.0)) {
               ClientHolder.getPhobosClient().getCloudConfigApi().handleText2(this.configInfo.getName(), ConfigScriptEnum.Config, !this.configInfo.isFlag());
               return;
            }

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

   public boolean isFlag() {
      return this.configInfo != null ? this.configInfo.isFlag() : this.configMeta != null && this.configMeta.isFlag();
   }

   public boolean testConfigMeta(ConfigMeta configMeta) {
      return Objects.equals(this.configMeta, configMeta);
   }

   public String describe() {
      return this.configInfo != null ? this.configInfo.getName() : (this.configMeta != null ? this.configMeta.getName() : "Unknown");
   }

   @Override
   public String toString() {
      return this.configInfo.describe();
   }

   public void run7() {
      this.colorPickerList.add(new NamedColorPicker("Load") {
         @Override
         public void run() {
            Color color = ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9();
            this.animatedColor.handleColor2(this.isFlag() ? color.brighter().brighter() : color);
            this.animatedColor.handleFloat(10.0F, 0.1F);
         }

         @Override
         public void run2() {
            if (ConfigEntryElement.this.configInfo != null) {
               String text = ConfigEntryElement.this.configInfo.getName();
               if (ConfigEntryElement.this.configInfo.isFlag2()) {
                  ClientHolder.getPhobosClient().getCloudConfigApi().handleText7(text, ConfigScriptEnum.Config);
                  ClientHolder.getPhobosClient().getCloudConfigApi().handleText9(text);
               }

               ConfigEntryElement.this.configInfo.run();
               if (ConfigEntryElement.this.configMeta != null) {
                  ConfigEntryElement.this.configMeta.run();
               }
            }
         }
      });
      if (this.configInfo.isFlag2()) {
         if (this.configInfo.getText2().equalsIgnoreCase(UserSession.userSession.getText())
            || UserSession.userSession.getUserProfile().getPermission().isAdmin()) {
            this.colorPickerList
               .add(
                  new EnumColorPicker<PUBLICPRIVATEEnum>("Visibility", this.configInfo.getPUBLICPRIVATEEnum()) {
                     @Override
                     public void run3() {
                        if (ConfigEntryElement.this.configInfo != null) {
                           ClientHolder.getPhobosClient()
                              .getCloudConfigApi()
                              .handleText4(ConfigEntryElement.this.configInfo.getName(), ConfigScriptEnum.Config, this.getEnumValue());
                        }
                     }

                     @Override
                     public void run() {
                        this.animatedColor
                           .handleColor2(
                              this.isFlag()
                                 ? ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9().brighter()
                                 : ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9()
                           );
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }
                  }
               );
            this.colorPickerList.add(new NamedColorPicker("Delete") {
               @Override
               public void run() {
                  Color color = ColorUtils.createColor6(Color.RED, ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9().getAlpha() / 255.0F);
                  this.animatedColor.handleColor2(this.isFlag() ? color.brighter() : color);
                  this.animatedColor.handleFloat(10.0F, 0.1F);
               }

               @Override
               public void run2() {
                  if (ConfigEntryElement.this.configInfo != null) {
                     ClientHolder.getPhobosClient().getCloudConfigApi().handleText6(ConfigEntryElement.this.configInfo.getName(), ConfigScriptEnum.Config);
                  }
               }
            });
         }

         this.colorPickerList
            .add(
               new NamedColorPicker("Download") {
                  @Override
                  public void run2() {
                     if (ConfigEntryElement.this.configInfo != null) {
                        String text = ConfigEntryElement.this.configInfo.getName();
                        ClientHolder.getPhobosClient().getCloudConfigApi().handleText9(text);
                        ClientHolder.getPhobosClient().getCloudConfigApi().handleText7(text, ConfigScriptEnum.Config);
                        ClientHolder.getPhobosClient().getConfigManager().resolveResult2(text, ConfigEntryElement.this.configInfo.getText());
                        ClientHolder.getPhobosClient().getConfigManager().run();
                        if (ConfigEntryElement.this.configMeta != null) {
                           ConfigEntryElement.this.configMeta.run();
                        }
                     }
                  }

                  @Override
                  public void run() {
                     this.animatedColor
                        .handleColor2(
                           this.isFlag()
                              ? ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9().brighter()
                              : ConfigEntryElement.this.getGuiManager().getUiTheme().getColor9()
                        );
                     this.animatedColor.handleFloat(10.0F, 0.1F);
                  }
               }
            );
      }

      this.run();
   }

   public boolean check() {
      return this.configInfo != null ? this.configInfo.check() : this.configMeta != null && this.configMeta.check();
   }

   public ConfigEntryElement(GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, ConfigMeta configMeta) {
      super(guiManager, guiPanel, f, f2, f3, f4, null);
      this.floatAnimator = new FloatAnimator();
      this.color2 = new Color(70, 70, 70, 70);
      this.color = new Color(0, 0, 0, 0);
      this.configMeta = configMeta;
   }

   @Override
   public CursorUtils.Style getStyle() {
      return CursorUtils.Style.Link;
   }

   public String describeInstant(Instant instant, Instant instant2) {
      if (instant2.isAfter(instant)) {
         return "Just now";
      } else {
         long l = Duration.between(instant2, instant).toMinutes();
         long l2 = Duration.between(instant2, instant).toHours();
         long l3 = Duration.between(instant2, instant).toDays();
         long l4 = l3 / 30L;
         if (l4 > 3L) {
            return "Long time ago";
         } else if (l4 >= 1L) {
            return "%s month%s ago".formatted(l4, l4 == 1L ? "" : "s");
         } else if (l3 >= 1L) {
            return "%s day%s ago".formatted(l3, l3 == 1L ? "" : "s");
         } else if (l2 >= 1L) {
            return "%s hour%s ago".formatted(l2, l2 == 1L ? "" : "s");
         } else {
            return l >= 1L ? "%s minute%s ago".formatted(l, l == 1L ? "" : "s") : "Just now";
         }
      }
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? this.value2() : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      this.floatAnimator.setFloatValue(!this.isFlag10() && !this.isFlag7() ? -1.0F : 1.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.1F);
      boolean flag2 = this.isFlag() || this.configInfo != null && this.configInfo.isFlag2() && (this.isFlag10() || this.isFlag7()) && UserSession.flag2;
      this.floatAnimator2.setFloatValue(flag2 ? 10.0F : 0.0F);
      this.floatAnimator2.handleFloat3(10.0F, 0.1F);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor13(),
         this.configInfo == null ? GuiManager.UIShader.Plasma : this.getGuiManager().getUiTheme().getUIShader2()
      );
      String text4 = this.configInfo == null ? (this.configMeta != null ? this.configMeta.getName() : "Unknown") : this.configInfo.getName();
      double d5 = this.computeText5(text4) + 1.0;
      float f2 = this.floatAnimator2.getFloatValue2();
      if (f2 > 0.1F && (this.configInfo == null || this.configInfo.isFlag2())) {
         boolean flag = this.testDouble2(d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + 2.0F, 8.0, 8.0)
            && UserSession.flag2
            && this.configInfo != null;
         ImageTexture imageTexture = this.isFlag()
            ? (flag ? ConfigEntryElement.imageTexture : imageTexture2)
            : (flag ? imageTexture2 : ConfigEntryElement.imageTexture);
         ClipStack clipStack = this.getClipStack();
         ClientHolder.getPhobosClient()
            .getRenderManager()
            .handleConsumer(
               drawContext -> {
                  clipStack.handleDrawContext2(drawContext);
                  drawContext.enableScissor(
                     (int)this.getFloatValue2(),
                     (int)this.getFloatValue(),
                     (int)this.getFloatValue2() + 20,
                     (int)(this.getFloatValue() + this.getFloatValue4())
                  );
                  imageTexture.run();
                  this.getMatrixStack().push();
                  this.getMatrixStack().translate(0.0F, 0.0F, 100.0F);
                  RenderSystem.disableBlend();
                  RenderUtils.handleMatrixStack2(
                     this.getMatrixStack(), this.getFloatValue2() - 10.0F + this.floatAnimator2.getFloatValue2() + 2.0F, this.getFloatValue() + 2.0F, 8.0
                  );
                  this.getMatrixStack().pop();
                  drawContext.disableScissor();
                  clipStack.handleDrawContext(drawContext);
               }
            );
      }

      this.handleText5(text4, 2.0F + f2, 3.0F, this.check() ? Color.RED : Color.WHITE, true, this.isFlag());
      String text5 = this.configInfo == null ? (this.configMeta != null ? this.configMeta.getText() : "Unknown") : this.configInfo.getText2();
      Color color3 = ColorUtils.resolveColor4(this.getGuiManager().getUiTheme().getColor10());
      if (text5 != null) {
         double d3 = this.computeText5(text5) * 0.7F;
         this.handleText4(text5, (float)(d5 + 8.0 + f2), 5.0F, Color.WHITE, 0.7F, true);
         f2 += (float)(d5 + d3 + 10.0);
      }

      if (this.configMeta != null && this.configMeta.isFlag2()) {
         String text = "Downloads: " + this.configMeta.getIntValue();
         if (this.floatAnimator.getFloatValue2() > -0.5F) {
            this.handleDouble(f2 + 4.0F, 0.0, this.getFloatValue3(), this.getFloatValue4());
            f2 += (float)(this.computeText5(text) * 0.5 * this.floatAnimator.getFloatValue2() - 23.0);
            this.handleText4(text, f2, 5.0F, Color.WHITE, 0.7F, true);
            this.run3();
         }
      }

      if (this.configInfo != null) {
         Instant instant = this.configInfo.getBuildMetadata().resolveObject("date", Instant.class);
         if (instant != null) {
            String text2 = this.describeInstant(Instant.now(), instant);
            double d4 = this.computeText5(text2) * 0.7;
            this.handleText4(text2, (float)(this.getFloatValue3() - d4 - 3.0), 5.0F, color3, 0.7F, true);
         }
      }

      if (this.getFloatValue13().getFloatValue2() >= 0.05) {
         this.flag = true;
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         float f = 0.0F;

         for (ColorPicker colorPicker : this.colorPickerList) {
            boolean flag2 = colorPicker.testDouble(d, d2);
            colorPicker.setFlag(flag2);
            colorPicker.setFloatValue(this.getFloatValue2() + 2.0F);
            colorPicker.setFloatValue3(this.getFloatValue() + this.getFloatValue4() + 2.0F + f);
            colorPicker.setFloatValue4(this.getFloatValue3() - 4.0F);
            colorPicker.setFloatValue2(this.getFloatValue4());
            colorPicker.run();
            if (colorPicker instanceof NamedColorPicker namedColorPicker) {
               this.handleText2(namedColorPicker.getName(), 4.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag2);
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
               this.handleText2(enumColorPicker.getName(), 4.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag2);
               String text3 = enumColorPicker.getEnumValue().equals(PUBLICPRIVATEEnum.PUBLIC) ? "Public" : "Private";
               this.handleText2(
                  text3, (float)(enumColorPicker.getFloatValue4() - this.computeText5(text3)) - 2.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag2
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

   public ConfigInfo getConfigInfo() {
      return this.configInfo;
   }

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }

   public ConfigMeta getConfigMeta() {
      return this.configMeta;
   }

   public boolean isFlag2() {
      return this.flag;
   }

   public List<ColorPicker> getColorPickerList() {
      return this.colorPickerList;
   }

   public Color getColor() {
      return this.color;
   }

   public FloatAnimator getFloatAnimator2() {
      return this.floatAnimator2;
   }

   public void setFlag(boolean flag) {
      this.flag2 = flag;
   }

   public void setConfigInfo(ConfigInfo configInfo) {
      this.configInfo = configInfo;
   }

   public Color getColor2() {
      return this.color2;
   }

   public void setFlag2(boolean flag) {
      this.flag = flag;
   }
}
