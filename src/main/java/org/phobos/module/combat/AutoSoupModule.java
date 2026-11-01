package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoSoupModule extends Module {
   public final Setting<NumberRange.Mutable<Float>> health = this.resolveSetting2(new MutableNumberSetting<>("Health", 6.0F, 1.0F, 16.0F));
   public final Setting<Boolean> drop = this.resolveSetting2(new BooleanSetting("Drop"));
   public final Setting<Boolean> refill = this.resolveSetting2(new BooleanSetting("Refill"));

   public void handleItemSwitcher(ItemSwitcher itemSwitcher) {
      Slot slot2 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.BOWL) ? slotx : null);
      if (slot2 != null) {
         itemSwitcher.handleSlot2(slot2, false);
      }

      if (this.refill.getValue()) {
         Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.MUSHROOM_STEW) && !itemSwitcher.testSlot4(slotx) ? slotx : null);
         if (slot != null) {
            itemSwitcher.handleRunnable2(() -> itemSwitcher.handleInt5(slot.id, 0, SlotActionType.QUICK_MOVE));
         }
      }
   }

   public AutoSoupModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoSoup", Categories.combat, "Automatically uses mushroom stew to heal you");
      this.resolveSettingGroup(this.drop, this.refill);
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!(EntityUtils.computeLivingEntity(clientPlayerEntity) > AutoSoupModule.this.health.getValue().getNumber())) {
                  AutoSoupModule.this.health.getValue().run6();
                  phobosClient.getInventoryManager()
                     .handleConsumer2(
                        itemSwitcher -> {
                           if (AutoSoupModule.this.drop.getValue()) {
                              AutoSoupModule.this.handleItemSwitcher(itemSwitcher);
                           }

                           Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.MUSHROOM_STEW) ? slotx : null);
                           if (slot != null) {
                              SwitchResult switchResult = itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 3);
                              if (switchResult != null) {
                                 TickUtils.handleClientWorld(
                                    clientWorld,
                                    integer -> clientPlayerEntity.networkHandler
                                       .sendPacket(
                                          new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch())
                                       )
                                 );
                              }
                           }
                        }
                     );
               }
            }
         }
      );
   }
}
