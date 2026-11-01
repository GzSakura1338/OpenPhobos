package org.phobos.core.gui.screen;

import java.awt.Color;
import java.awt.Desktop;
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
import org.phobos.core.render.RenderUtils;
import org.phobos.core.script.Script;
import org.phobos.core.script.ScriptManager;
import org.phobos.core.user.UserSession;

public class ScriptEntryElement extends InteractiveElement {
   public final Script script;
   public final Color color;
   public final List<ColorPicker> colorPickerList = new ArrayList<>();
   public final Color color2;
   public boolean flag;
   public boolean flag2;

   public Color getColor() {
      return this.color2;
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
         this.getGuiManager().getUiTheme().getUIShader2()
      );
      this.handleText2(this.script.getName(), 2.0F, 3.0F, Color.WHITE, true);
      Color color3 = ColorUtils.resolveColor4(this.getGuiManager().getUiTheme().getColor10());
      String text3 = this.script.getText2();
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
               Color color = this.resolveColor(namedColorPicker.getAnimatedColor().createColor());
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
               this.handleText2(namedColorPicker.getName(), 4.0F, this.getFloatValue4() + 5.0F + f, Color.WHITE, true, flag);
            }

            if (colorPicker instanceof EnumColorPicker<?> enumColorPicker) {
               Color color2 = this.resolveColor(enumColorPicker.getAnimatedColor().createColor());
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
               colorPicker2.getAnimatedColor().handleColor3(this.color2);
            }
         }
      }
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

   public boolean isFlag2() {
      return this.flag;
   }

   public float value2() {
      float f = 0.0F;

      for (ColorPicker colorPicker : this.colorPickerList) {
         f += colorPicker.getFloatValue2() + 2.0F;
      }

      return f;
   }

   public Script getScript() {
      return this.script;
   }

   public Color getColor2() {
      return this.color;
   }

   public ScriptEntryElement(
      GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, Script script
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, null);
      this.color = new Color(70, 70, 70, 150);
      this.color2 = new Color(0, 0, 0, 0);
      this.script = script;
      this.colorPickerList
         .add(
            new NamedColorPicker("Start") {
               @Override
               public void run() {
                  this.animatedColor
                     .handleColor2(
                        script.isFlag22()
                           ? ScriptEntryElement.this.color
                           : ColorUtils.createColor6(Color.GREEN, ScriptEntryElement.this.getGuiManager().getUiTheme().getColor13().getAlpha() / 255.0F)
                     );
                  this.animatedColor.handleFloat(10.0F, 0.1F);
               }

               @Override
               public void run2() {
                  if (!script.isFlag22()) {
                     script.getPhobosClient().getScriptManager().handleScript4(script);
                  }
               }
            }
         );
      this.colorPickerList
         .add(
            new NamedColorPicker("Stop") {
               @Override
               public void run() {
                  this.animatedColor
                     .handleColor2(
                        script.isFlag22()
                           ? ColorUtils.createColor6(Color.RED, ScriptEntryElement.this.getGuiManager().getUiTheme().getColor13().getAlpha() / 255.0F)
                           : ScriptEntryElement.this.color
                     );
                  this.animatedColor.handleFloat(10.0F, 0.1F);
               }

               @Override
               public void run2() {
                  if (script.isFlag22()) {
                     script.getPhobosClient().getScriptManager().handleScript3(script);
                  }
               }
            }
         );
      if (script.isFlag2()) {
         if (UserSession.userSession.getText().equalsIgnoreCase(script.getText2())
            || UserSession.userSession.getUserProfile().getPermission().isAdmin()) {
            this.colorPickerList
               .add(
                  new EnumColorPicker<PUBLICPRIVATEEnum>("Visibility", script.getPUBLICPRIVATEEnum()) {
                     @Override
                     public void run() {
                        this.animatedColor.handleColor2(ScriptEntryElement.this.getGuiManager().getUiTheme().getColor13());
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }

                     @Override
                     public void run3() {
                        if (script.isFlag22()) {
                           script.getPhobosClient().getScriptManager().handleScript3(script);
                        }

                        ClientHolder.getPhobosClient()
                           .getCloudConfigApi()
                           .handleText4(
                              script.getName(),
                              ConfigScriptEnum.Script,
                              this.getEnumValue(),
                              ScriptManager::run9,
                              ScriptEntryElement.this.getGuiPanel()::run
                           );
                     }
                  }
               );
            this.colorPickerList
               .add(
                  new NamedColorPicker("Delete") {
                     @Override
                     public void run2() {
                        if (script.isFlag22()) {
                           script.getPhobosClient().getScriptManager().handleScript3(script);
                        }

                        ClientHolder.getPhobosClient()
                           .getCloudConfigApi()
                           .handleText6(
                              script.getName(), ConfigScriptEnum.Script, ScriptManager::run9, ScriptEntryElement.this.getGuiPanel()::run
                           );
                     }

                     @Override
                     public void run() {
                        this.animatedColor
                           .handleColor2(
                              ColorUtils.createColor6(Color.RED, ScriptEntryElement.this.getGuiManager().getUiTheme().getColor9().getAlpha() / 255.0F)
                           );
                        this.animatedColor.handleFloat(10.0F, 0.1F);
                     }
                  }
               );
         }
      } else {
         this.colorPickerList.add(new NamedColorPicker("Edit") {
            @Override
            public void run() {
               this.animatedColor.handleColor2(ColorUtils.resolveColor4(ScriptEntryElement.this.getGuiManager().getUiTheme().getColor13()));
               this.animatedColor.handleFloat(10.0F, 0.1F);
            }

            @Override
            public void run2() {
               try {
                  Desktop.getDesktop().open(script.getFile());
               } catch (Exception var2) {
               }
            }
         });
         this.colorPickerList
            .add(
               new NamedColorPicker("Publish") {
                  @Override
                  public void run2() {
                     if (script.isFlag22()) {
                        script.getPhobosClient().getScriptManager().handleScript3(script);
                     }

                     ClientHolder.getPhobosClient()
                        .getCloudConfigApi()
                        .handleText3(
                           script.getName(),
                           ConfigScriptEnum.Script,
                           PUBLICPRIVATEEnum.PRIVATE,
                           script.getText22(),
                           ScriptManager::run9,
                           ScriptEntryElement.this.getGuiPanel()::run
                        );
                  }

                  @Override
                  public void run() {
                     this.animatedColor.handleColor2(ScriptEntryElement.this.getGuiManager().getUiTheme().getColor9());
                     this.animatedColor.handleFloat(10.0F, 0.1F);
                  }
               }
            );
      }
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? this.value2() : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
   }

   public List<ColorPicker> getColorPickerList() {
      return this.colorPickerList;
   }

   public boolean isFlag3() {
      return this.flag2;
   }
}
