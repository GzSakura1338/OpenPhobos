package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.slot.SlotActionType;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class InvSyncModule extends Module {
   public final Setting<Long> delay;
   public final Setting<InvSyncModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", InvSyncModule.Mode._2b2t));
   public final TimeGate.Normal normal;

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public InvSyncModule(PhobosClient phobosClient) {
      super(phobosClient, "InvSync", Categories.player, "Synchronizes inventory");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 50L, 0L, 1000L));
      this.normal = new TimeGate.Normal();
      this.handleListener(
         new SafeListener<TickCounter>() {
            public void handleObject(
               TickCounter tickCounter,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (InvSyncModule.this.normal.testNumber2(InvSyncModule.this.delay.getValue())) {
                  switch ((InvSyncModule.Mode)InvSyncModule.this.mode.getValue()) {
                     case _2b2t:
                        clientPlayerInteractionManager.clickSlot(clientPlayerEntity.playerScreenHandler.syncId, 0, 0, SlotActionType.SWAP, clientPlayerEntity);
                  }
               }
            }
         }
      );
   }

   private enum Mode {
      _2b2t;
   }
}
