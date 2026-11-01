package org.phobos.module.misc;

import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.event.AnnouncerReceiveListener;
import org.phobos.event.AnnouncerClearActiveItemListener;
import org.phobos.event.AnnouncerTickListener;
import org.phobos.module.Module;

public class AnnouncerModule extends Module {
   public final Setting<Boolean> join;
   public final Setting<Boolean> client;
   public final Setting<Boolean> leave;
   public final Setting<Long> delay = this.resolveSetting2(new LongNumberSetting("Delay", 2000L, 0L, 30000L));
   public final Setting<Boolean> consume;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> movement = this.resolveSetting2(new BooleanSetting("Movement"));
   public int intValue;
   public Vec3d vec3d;

   public AnnouncerModule(PhobosClient phobosClient) {
      super(phobosClient, "Announcer", Categories.misc, "Announces your actions");
      this.consume = this.resolveSetting2(new BooleanSetting("Consume"));
      this.join = this.resolveSetting2(new BooleanSetting("Join"));
      this.leave = this.resolveSetting2(new BooleanSetting("Leave"));
      this.client = this.resolveSetting2(new BooleanSetting("Client").resolveSetting7("Client-side messages"));
      this.normal = new TimeGate.Normal();
      this.intValue = 0;
      this.handleListener(new AnnouncerReceiveListener(this));
      this.handleListener(new AnnouncerClearActiveItemListener(this));
      this.handleListener(new AnnouncerTickListener(this));
      ModuleListenerUtils.handleListenerHolder4(this, this::clear);
   }

   public void clear() {
      this.vec3d = null;
      this.intValue = 0;
      this.normal.run();
   }

   public void handleText(String text, Text text2) {
      this.normal.run();
      if (this.client.getValue()) {
         this.handleText7(text2);
      } else if (mc.getNetworkHandler() != null) {
         mc.getNetworkHandler().sendChatMessage(text);
      }
   }
}
