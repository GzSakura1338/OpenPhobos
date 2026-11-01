package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.earthhack.ducks.ISlot;
import org.phobos.core.math.FloatPair;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.inventory.ScheduledClick;
import org.phobos.core.inventory.ActionTimer;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.player.SilentHotbarModule;

public final class BreakerPlayerTickListener extends ModuleListener.Safe<BreakerModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((BreakerModule.Mode)this.module.mode.getValue()) {
         case Phase:
            if (!this.module.update.testObject2(BreakerModule.Update.Async)) {
               return;
            }

            this.module.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, BreakerModule.Update.Async);
            break;
         case Shield:
            if (this.module.inventory.getValue()
               && this.module.flag
               && this.module.flag2
               && this.module.normal2.testNumber(this.module.inventoryTimeout.getValue())) {
               this.module.run();
               this.module.flag = false;
            }

            if (mc.currentScreen != null) {
               return;
            }

            if (this.module.manual.getValue()) {
               return;
            }

            if (!this.module.normal.testNumber(this.module.delay.getValue())) {
               return;
            }

            this.phobosClient
               .getInventoryManager()
               .handleConsumer2(
                  itemSwitcher -> {
                     if (mc.crosshairTarget instanceof EntityHitResult entityHitResult) {
                        if (entityHitResult.getEntity() instanceof PlayerEntity playerEntity) {
                           if (!this.module.testLivingEntity(playerEntity)) {
                              Slot slot3 = itemSwitcher.resolveObject(
                                 slotx -> itemSwitcher.testSlot4(slotx) && slotx.getStack().getItem() instanceof AxeItem ? slotx : null
                              );
                              if (slot3 == null) {
                                 if (this.module.inventory.getValue()) {
                                    Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().getItem() instanceof AxeItem ? slotx : null);
                                    Slot slot2 = itemSwitcher.resolveObject(
                                       slotx -> (
                                                this.module.inventoryReplace.testObject2(BreakerModule.Replace.Sword)
                                                   ? slotx.getStack().getItem() instanceof SwordItem
                                                   : slotx.getStack().getItem() instanceof ShieldItem
                                             )
                                             && itemSwitcher.testSlot4(slotx)
                                          ? slotx
                                          : null
                                    );
                                    if (slot != null && slot2 != null) {
                                       ISlot iSlot = ISlot.cast(slot);
                                       if (iSlot.isLoaded()) {
                                          if (mc.currentScreen == null) {
                                             mc.setScreen(new ActionTimer(clientPlayerEntity, 3));
                                             ClickScheduler.getINSTANCE().handleISlot(iSlot, FloatPair.createFloatPair(30.0F, 70.0F));
                                             ClickScheduler.getINSTANCE().handleSlot(slot, new ScheduledClick(slot2.getIndex(), SlotActionType.SWAP));
                                             ClickScheduler.getINSTANCE().handleSlot2(slot, 3, this.module.inventoryClose.getValue().getNumber4());
                                             this.module.slot = slot;
                                             this.module.intValue = slot2.getIndex();
                                             this.module.flag2 = true;
                                             this.module.normal2.run();
                                          }
                                       }
                                    }
                                 }
                              } else if (!this.module.inventory.getValue()
                                 || !this.module.flag2
                                 || this.module.normal2.testNumber(this.module.inventoryTimeout.getValue())) {
                                 SilentHotbarModule.getSilentHotbarManager()
                                    .handleObject3(
                                       this.module,
                                       slot3,
                                       1,
                                       SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                    );
                                 clientPlayerInteractionManager.attackEntity(clientPlayerEntity, playerEntity);
                                 clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                                 if (this.module.stun.getValue() && playerEntity.isOnGround()) {
                                    this.phobosClient.getScheduler().handleRunnable2(() -> {
                                       if (!playerEntity.isDead() && this.module.check7()) {
                                          clientPlayerInteractionManager.attackEntity(clientPlayerEntity, playerEntity);
                                          clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                                          this.module.normal2.run();
                                       }
                                    }, 1);
                                 }

                                 this.module.flag = true;
                                 this.module.normal2.run();
                                 this.module.normal.run();
                              }
                           }
                        }
                     }
                  }
               );
      }
   }

   public BreakerPlayerTickListener(BreakerModule breakerModule) {
      super(breakerModule, PlayerTickEvent.class);
   }
}
