package org.phobos.core.gui.element;

import java.awt.Color;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.type.NormalHoldEnum;
import org.phobos.core.bind.BindKey;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.gui.ModuleElement;

public class BindElement extends SettingValueElement<KeyCombination, BindSetting> {
   public boolean flag;
   public boolean flag2;

   @Override
   public void handleDouble2(double d, double d2) {
      this.handleFlag(this.isFlag10());
      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         this.getGuiManager().getUiTheme().getColor7(),
         this.getGuiManager().getUiTheme().getUIShader5()
      );
      String text = this.getText3();
      this.handleText2(this.flag ? "Press a key" : text + ": " + this.getValue(), 2.0F, 3.0F, Color.WHITE, true);
      if (this.getFloatValue13().getFloatValue2() > 0.05) {
         this.handleDouble(0.0, this.getFloatValue4(), this.getFloatValue3(), this.getFloatValue13().getFloatValue2());
         this.handleDouble12(
            2.0,
            this.getFloatValue4() + 2.0F,
            this.getFloatValue3() - 2.0F,
            this.getFloatValue4() + 2.0F + this.getFloatValue4(),
            this.getGuiManager().getUiTheme().getColor7()
         );
         this.handleText2(
            "Toggle: " + this.getValue().getNormalHoldEnum().name(),
            4.0F,
            this.getFloatValue4() + 5.0F,
            Color.WHITE,
            true,
            this.testDouble2(
               d, d2, this.getFloatValue2() + 2.0F, this.getFloatValue() + this.getFloatValue4() + 2.0F, this.getFloatValue3() - 2.0F, this.getFloatValue4()
            )
         );
         this.run3();
      }
   }

   @Override
   public void handleFlag(boolean flag) {
      this.getFloatValue13().setFloatValue(flag ? this.getFloatValue4() + 2.0F : 0.0F);
      this.getFloatValue13().handleFloat3(10.0F, 0.1F);
      if (flag && this.getValue().isEmpty()) {
         this.setFlag10(!this.isFlag10());
      }
   }

   public BindElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      BindSetting bindSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, bindSetting);
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (this.isFlag7()) {
               this.run15();
               this.run9();
               return;
            }

            if (!this.isFlag10()) {
               return;
            }

            if (this.testDouble2(
               d, d2, this.getFloatValue2(), this.getFloatValue() + this.getFloatValue4() + 2.0F, this.getFloatValue3() - 2.0F, this.getFloatValue4()
            )) {
               NormalHoldEnum normalHoldEnum = this.getValue().getNormalHoldEnum();
               this.getValue().setNormalHoldEnum(NormalHoldEnum.other(normalHoldEnum));
               this.run15();
            }
            break;
         case 1:
            if (this.isFlag7() && !this.getValue().isEmpty()) {
               this.setFlag10(!this.isFlag10());
               this.run14();
            }
            break;
         case 2:
            if (this.isFlag7()) {
               this.run15();
               this.flag2 = true;
               this.run9();
               return;
            }
            break;
         default:
            if (!this.flag) {
               return;
            }

            BindKey bindKey = ClientHolder.getPhobosClient().getBindManager().resolveBindKey(KeyboardMouseEnum.Mouse, i);
            if (bindKey == null) {
               return;
            }

            this.setValue(this.getValue().createKeyCombination2(bindKey));
            this.run9();
      }
   }

   public void run9() {
      this.flag = !this.flag;
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      if (this.flag) {
         if (i != 256) {
            if (i != 259 && i != 261) {
               BindKey bindKey = ClientHolder.getPhobosClient().getBindManager().resolveBindKey(KeyboardMouseEnum.Keyboard, i);
               if (bindKey != null) {
                  if (this.flag2) {
                     this.getValue().getBindKeySet().add(bindKey);
                     this.flag2 = false;
                  } else {
                     this.setValue(this.getValue().createKeyCombination2(bindKey));
                  }
               }
            } else {
               this.setValue(KeyCombination.createKeyCombination());
            }
         }

         this.run9();
      }
   }
}
