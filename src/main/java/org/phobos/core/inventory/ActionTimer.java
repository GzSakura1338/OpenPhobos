package org.phobos.core.inventory;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;

public class ActionTimer extends InventoryScreen {
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public boolean flag = false;
   public final int intValue;
   public static final int intValue2 = 3;
   public static final int intValue3 = 1;
   public static final int intValue4 = 2;

   public boolean testSetting(Setting<NumberRange.Mutable<Long>> setting) {
      if (this.normal.testSetting2(setting)) {
         setting.getValue().run6();
         return true;
      } else {
         return false;
      }
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void run() {
      if (!this.flag) {
         this.normal.run();
      }

      this.flag = true;
   }

   public ActionTimer(PlayerEntity playerEntity, int i) {
      super(playerEntity);
      this.normal.run();
      this.intValue = i;
   }

   public TimeGate.Normal getNormal() {
      return this.normal;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public boolean testLong(long l) {
      return this.normal.testLong(l);
   }

   public boolean testInt(int i) {
      return i == this.intValue;
   }
}
