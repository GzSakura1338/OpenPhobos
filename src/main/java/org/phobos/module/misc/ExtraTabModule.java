package org.phobos.module.misc;

import java.awt.Color;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.user.UserTagManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.chat.TextVisitorUtils;
import org.phobos.core.chat.TextVisitorUtils;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public class ExtraTabModule extends Module {
   public static ExtraTabModule extraTabModule;
   public final Setting<Boolean> telemetryReplace;
   public final Setting<Boolean> color;
   public final Setting<Boolean> telemetry;
   public final Setting<Integer> size = this.resolveSetting2(new NumberSetting<>("Size", 80, 80, 400));

   public Setting<Boolean> getTelemetry() {
      return this.telemetry;
   }

   public Setting<Integer> getSize() {
      return this.size;
   }

   public Setting<Boolean> getBackgroundColor() {
      return this.color;
   }

   public ExtraTabModule(PhobosClient phobosClient) {
      super(phobosClient, "ExtraTab", Categories.misc, "Extends tab functionality");
      this.color = this.resolveSetting2(new BooleanSetting("Color").resolveSetting7("Highlights friends & enemies"));
      this.telemetry = this.resolveSetting2(new BooleanSetting("Telemetry"));
      this.telemetryReplace = this.resolveSetting2(new BooleanSetting("TelemetryReplace"));
      this.resolveSettingGroup(this.telemetry, this.telemetryReplace);
      this.handleListener(new Listener<ExtraTabModule.TabSizeEvent>() {
         public void handleObject2(ExtraTabModule.TabSizeEvent tabSizeEvent) {
            tabSizeEvent.setSize(ExtraTabModule.this.size.getValue());
            tabSizeEvent.setCancelled(true);
         }
      });
      extraTabModule = this;
   }

   public static ExtraTabModule getExtraTabModule() {
      return extraTabModule;
   }

   public Text describeText(Text text, UserTagManager.TelemetryUser telemetryUser) {
      return !this.check7()
         ? text
         : TextVisitorUtils.describeText(text, visit -> TextVisitorUtils.Visit.createVisit2(this.describeVisit(visit, telemetryUser), this.resolveStyle(visit)));
   }

   public Style resolveStyle(TextVisitorUtils.Visit visit) {
      if (!this.color.getValue()) {
         return visit.style();
      } else if (this.phobosClient.getFriendList().contains(visit.string())) {
         return visit.style().withColor(Color.CYAN.hashCode());
      } else {
         return this.phobosClient.getEnemyList().contains(visit.string()) ? visit.style().withColor(Color.RED.hashCode()) : visit.style();
      }
   }

   public String describeVisit(TextVisitorUtils.Visit visit, UserTagManager.TelemetryUser telemetryUser) {
      return this.telemetry.getValue() && telemetryUser != null && this.telemetryReplace.getValue()
         ? visit.string().replaceAll(telemetryUser.getText(), telemetryUser.getText3())
         : visit.string();
   }

   public Setting<Boolean> getTelemetryReplace() {
      return this.telemetryReplace;
   }

   public static final class TabSizeEvent extends CancellableEvent {
      public int size;

      public void setSize(int i) {
         this.size = i;
      }

      public int getSize() {
         return this.size;
      }
   }
}
