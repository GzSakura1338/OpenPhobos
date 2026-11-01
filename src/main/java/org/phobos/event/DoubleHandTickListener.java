package org.phobos.event;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.input.InputUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.module.combat.AutoTotemModule;
import org.phobos.module.combat.DoubleHandModule;
import org.phobos.module.combat.DoubleHandModule;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.player.SuicideModule;

public final class DoubleHandTickListener extends ModuleListener.Safe<DoubleHandModule, TickEvent2> {
   public DoubleHandTickListener(DoubleHandModule doubleHandModule) {
      super(doubleHandModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(
            itemSwitcher -> {
               if (this.module.shield.getValue() || !itemSwitcher.getSlot3().getStack().isOf(Items.SHIELD)) {
                  switch ((DoubleHandModule.Mode)this.module.mode.getValue()) {
                     case Legit:
                        if (AutoTotemModule.check() || this.phobosClient.getRotationDelay().check()) {
                           return;
                        }

                        Slot slot = itemSwitcher.resolveObject(
                           slotx -> slotx.getStack().isOf(Items.TOTEM_OF_UNDYING) && itemSwitcher.testSlot4(slotx) ? slotx : null
                        );
                        if (slot == null) {
                           return;
                        }

                        if (!this.module.threaded.testNumber(this.module.timeout.getValue())) {
                           InputUtils.handleSlot(slot);
                           return;
                        }

                        if (mc.currentScreen instanceof GameMenuScreen) {
                           return;
                        }

                        if (this.module.offhand.getValue() && itemSwitcher.getSlot2().getStack().isEmpty() && this.module.normal.testLong2(500L)) {
                           SilentHotbarModule.getSilentHotbarManager()
                              .handleObject3(
                                 this.module,
                                 slot,
                                 1,
                                 SilentHotbarModule.getSilentHotbarManager()
                                    .getBuilder()
                                    .resolveBuilder3(true)
                                    .resolveBuilder4(
                                       () -> clientPlayerEntity.networkHandler
                                          .sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN))
                                    )
                                    .getSelectionConfig()
                              );
                        }
                        break;
                     case Rage:
                        if (SuicideModule.getSuicideModule().check7() || DoubleHandModule.flag) {
                           return;
                        }

                        if (clientPlayerEntity.isUsingItem()) {
                           return;
                        }

                        int i = this.module.slot.getValue();
                        if (this.module.auto.getValue()) {
                           float f = EntityUtils.computeLivingEntity(clientPlayerEntity);
                           if (f < this.module.health.getValue() && (f < this.module.floatValue || Math.abs(f - this.module.floatValue) >= 10.0F)
                              || !this.phobosClient.getEntityIdManager().testLong(this.module.totem.getValue())) {
                              InputUtils.handleInt(i);
                           }

                           this.module.floatValue = f;
                        }

                        int i2 = i - 1;
                        int i3 = PlayerActionUtils.value();
                        if (i2 != i3) {
                           return;
                        }

                        itemSwitcher.setFlag(true);
                        Item item = mc.options.useKey.isPressed()
                           ? itemSwitcher.resolveItem2(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE)
                           : itemSwitcher.resolveItem2(Items.TOTEM_OF_UNDYING);
                        if (item == null) {
                           return;
                        }

                        itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 0);
                        itemSwitcher.setFlag(false);
                  }
               }
            }
         );
   }
}
