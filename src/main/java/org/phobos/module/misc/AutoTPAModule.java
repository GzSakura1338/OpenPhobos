package org.phobos.module.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.network.PlayerListEntry;
import org.phobos.api.TimeGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoTPAModule extends Module {
   public final Setting<Long> delay;
   public final Setting<AutoTPAModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoTPAModule.Mode.Normal));
   public final TimeGate.Normal normal;
   public final List<UUID> uUIDList;

   public AutoTPAModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTPA", Categories.misc, "Sends everyone tpa requests");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 500L, 0L, 2000L, true));
      this.normal = new TimeGate.Normal();
      this.uUIDList = new ArrayList<>();
      this.handleListener(
         new Listener<TickEvent2>() {
            public void handleObject2(TickEvent2 tickEvent2) {
               if (MinecraftHolder.mc.player != null
                  && MinecraftHolder.mc.getNetworkHandler() != null
                  && AutoTPAModule.this.normal.testNumber2(AutoTPAModule.this.delay.getValue())) {
                  if (AutoTPAModule.this.uUIDList.size() > 100) {
                     AutoTPAModule.this.uUIDList.removeFirst();
                  }

                  AutoTPAModule.this.uUIDList.removeIf(uUID -> MinecraftHolder.mc.getNetworkHandler().getPlayerListEntry(uUID) == null);

                  for (PlayerListEntry playerListEntry : MinecraftHolder.mc.getNetworkHandler().getPlayerList()) {
                     if (!AutoTPAModule.this.uUIDList.contains(playerListEntry.getProfile().getId())) {
                        MinecraftHolder.mc
                           .player
                           .networkHandler
                           .sendChatCommand(
                              AutoTPAModule.this.mode.getValue().equals(AutoTPAModule.Mode.Normal)
                                 ? "tpa"
                                 : "tpahere " + playerListEntry.getProfile().getName()
                           );
                        AutoTPAModule.this.uUIDList.add(playerListEntry.getProfile().getId());
                        break;
                     }
                  }
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   @Override
   public void run3() {
      this.uUIDList.clear();
   }

   @Override
   public void run4() {
      this.uUIDList.clear();
   }

   private enum Mode {
      Normal,
      Here;
   }
}
