package org.phobos.core.gui.element;

import java.awt.Color;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.phobos.api.TimeGate;
import org.phobos.api.KeyValue;
import org.phobos.module.client.UIModule;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.setting.ItemCountSetting;
import org.phobos.core.gui.ModuleElement;

public class ItemCountElement extends SettingElement<KeyValue<Item, Integer>, ItemCountSetting> {
   @HashCodeExclude
   public final Color color = new Color(0, 0, 0, 70);
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;
   public boolean flag5;
   public boolean flag10;
   public boolean flag11;
   public final ItemStack itemStack;
   public boolean flag12;
   public String text;

   public String getText() {
      return this.text;
   }

   @Override
   public void handleChar2(char c, int i) {
      if (this.flag5) {
         this.handleChar(c);
      }
   }

   @Override
   public void handleDouble2(double d, double d2) {
      float f = UIModule.getUIModule().height.getValue() * 0.8F;
      String text = String.valueOf(this.getValue().getObject2());
      float f2 = (float)this.computeText5("+");
      float f3 = this.getFloatValue2() + this.getFloatValue3() - f2 - 2.0F;
      float f4 = this.getFloatValue() + 3.0F;
      this.flag10 = this.testDouble2(d, d2, f3 - 2.0F, f4, f2 + 2.0F, this.getFloatValue4() - 6.0F);
      this.flag2 = this.testDouble2(d, d2, f3 - f2 * 2.0F, f4, f2 + 2.0F, this.getFloatValue4() - 6.0F);
      this.flag11 = this.testDouble2(d, d2, f3 - f2 * 3.0F, f4, f2 + 2.0F, this.getFloatValue4() - 6.0F);
      this.flag4 = this.testDouble2(
         d,
         d2,
         this.getFloatValue2() + 2.0F + 16.0F * f,
         this.getFloatValue() + 3.0F,
         this.getFloatValue3() - f2 * 3.0F - 8.0F - 16.0F * f,
         this.computeText(text)
      );
      this.handleDouble12(0.0, 0.0, this.getFloatValue3(), this.getFloatValue4(), this.color);
      this.handleText2("+", this.getFloatValue3() - f2 - 4.0F, 3.0F, Color.WHITE, true, this.flag10);
      this.handleText2("R", this.getFloatValue3() - f2 * 2.0F - 6.0F, 3.0F, Color.WHITE, true, this.flag2);
      this.handleText2("-", this.getFloatValue3() - f2 * 3.0F - 6.0F, 3.0F, Color.WHITE, true, this.flag11);
      this.getMatrixStack().push();
      this.getMatrixStack().translate(this.getFloatValue2() + 1.08F, this.getFloatValue() + 1.08F, 0.0F);
      this.getMatrixStack().scale(f, f, 1.0F);
      this.getGuiGraphics().getDrawContext().drawItem(this.itemStack, 0, 0);
      this.getMatrixStack().pop();
      this.handleDouble(-4.0, -4.0, this.getFloatValue3() - f2 * 3.0F - 8.0F, this.getFloatValue4() + 4.0F);
      if (this.flag5) {
         this.handleText2(this.text + this.describe(), 2.0F + 16.0F * f, 3.0F, Color.WHITE, true, false);
      } else {
         this.handleText2(text, 2.0F + 16.0F * f, 3.0F, Color.WHITE, true, this.flag4);
      }

      this.run3();
   }

   public void run2() {
      this.setFlag(false);

      try {
         int i = Integer.parseInt(this.getText());
         this.setValue(this.getValue().resolveKeyValue3(item -> item, integer -> this.getSetting().computeInt(i)));
      } catch (Exception exception) {
         this.setText("");
         this.setValue(this.getValue().resolveKeyValue3(item -> item, integer -> this.getSetting().getIntValue3()));
      }
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      if (this.flag5) {
         if (i == 257) {
            this.run2();
         }

         if (i == 259) {
            this.run();
         }
      }
   }

   public void handleChar(char c) {
      if (Character.isDigit(c) || c == '.' || c == '-') {
         if (c != '-' || this.getSetting().getIntValue3() < 0) {
            this.setText(this.getText() + c);
         }
      }
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      switch (i) {
         case 0:
            if (this.flag10) {
               this.setValue(
                  this.getValue().resolveKeyValue3(item -> item, integer -> this.getSetting().computeInt(integer + this.getSetting().getIntValue4()))
               );
               this.run15();
               return;
            }

            if (this.flag2) {
               this.setValue(this.getValue().resolveKeyValue3(item -> item, integer -> this.getSetting().computeInt(this.getSetting().getIntValue2())));
               this.run15();
               return;
            }

            if (this.flag11) {
               this.setValue(
                  this.getValue().resolveKeyValue3(item -> item, integer -> this.getSetting().computeInt(integer - this.getSetting().getIntValue4()))
               );
               this.run15();
               return;
            }
            break;
         case 1:
            if (this.flag5) {
               this.run2();
               return;
            }

            float f = UIModule.getUIModule().height.getValue() * 0.8F;
            float f2 = (float)this.computeText5("+");
            String text = String.valueOf(this.getValue().getObject2());
            if (this.testDouble2(
               d,
               d2,
               this.getFloatValue2() + 2.0F + 16.0F * f,
               this.getFloatValue() + 3.0F,
               this.getFloatValue3() - f2 * 3.0F - 8.0F - 16.0F * f,
               this.computeText(text)
            )) {
               this.run7();
               this.run15();
               this.flag5 = true;
            }
      }
   }

   public void run7() {
      this.text = "";
   }

   public ItemCountElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      ItemCountSetting itemCountSetting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement, text, itemCountSetting);
      this.itemStack = InventoryUtils.createItemStack(itemCountSetting.getValue().getObject(), 1, builder -> {
         if (itemCountSetting.isFlag()) {
            builder.add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
         }
      });
   }

   public void setText(String text) {
      this.text = text;
   }

   public boolean isFlag() {
      return this.flag5;
   }

   public void setFlag(boolean flag) {
      this.flag5 = flag;
   }

   public String describe() {
      if (this.normal.testLong2(500L)) {
         this.flag = !this.flag;
      }

      return this.flag ? "_" : "";
   }

   public void run() {
      if (!this.getText().isEmpty()) {
         this.setText(this.getText().substring(0, this.getText().length() - 1));
      }
   }
}
