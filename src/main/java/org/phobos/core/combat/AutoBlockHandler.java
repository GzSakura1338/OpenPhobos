package org.phobos.core.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.api.TextProvider;
import org.phobos.event.HandleInputEventsEvent;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.combat.AuraModule;
import org.phobos.core.Feature;
import org.phobos.module.player.LegacyModule;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.inventory.SwitchOverride;

public class AutoBlockHandler extends Feature {
   public final PhobosClient phobosClient;
   public int intValue;
   public boolean flag = false;
   public final AuraModule auraModule;

   public AutoBlockHandler(AuraModule auraModule) {
      this.intValue = 0;
      this.auraModule = auraModule;
      this.phobosClient = auraModule.getPhobosClient();
      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            AutoBlockHandler.this.intValue = AutoBlockHandler.this.flag ? AutoBlockHandler.this.intValue + 1 : AutoBlockHandler.this.intValue - 1;
         }
      });
      this.handleListener(
         new SafeListener<HandleInputEventsEvent>() {
            public void handleObject(
               HandleInputEventsEvent handleInputEventsEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoBlockHandler.this.flag
                  && clientPlayerEntity.getMainHandStack().getItem() instanceof SwordItem
                  && PlayerActionUtils.testClientPlayerEntity(clientPlayerEntity)) {
                  handleInputEventsEvent.setCancelled(true);
               }
            }
         }
      );
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager) {
      if (!PlayerActionUtils.testClientPlayerEntity(clientPlayerEntity) || this.auraModule.autoBlockSetting.testObject2(AutoBlockHandler.AutoBlock.Watchdog)) {
         if (this.auraModule.booleanValueSetting10.getValue()) {
            LegacyModule.handleFlag(true);
         }

         AutoBlockHandler.AutoBlock autoBlock = this.auraModule.autoBlockSetting.getValue();
         if (!autoBlock.equals(AutoBlockHandler.AutoBlock.None) && !autoBlock.isDisableInteraction()) {
            clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
            this.flag = true;
            if (this.intValue < 0) {
               this.intValue = 0;
            }
         }

         switch ((AutoBlockHandler.AutoBlock)this.auraModule.autoBlockSetting.getValue()) {
            case Watchdog:
               clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
               break;
            case Override:
               SwitchOverride.handleFlag3(true);
         }
      }
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      if (this.auraModule.booleanValueSetting10.getValue()) {
         LegacyModule.handleFlag(false);
      }

      AutoBlockHandler.AutoBlock autoBlock = this.auraModule.autoBlockSetting.getValue();
      if (!autoBlock.equals(AutoBlockHandler.AutoBlock.None) && !autoBlock.isDisableInteraction() && PlayerActionUtils.testClientPlayerEntity(clientPlayerEntity)) {
         switch ((AutoBlockHandler.Unblock)this.auraModule.unblockSetting.getValue()) {
            case Release:
               clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
               break;
            case None:
               if (mc.interactionManager != null) {
                  mc.interactionManager.stopUsingItem(clientPlayerEntity);
               }
         }

         this.flag = false;
         if (this.intValue > 0) {
            this.intValue = 0;
         }
      }

      switch ((AutoBlockHandler.AutoBlock)this.auraModule.autoBlockSetting.getValue()) {
         case Watchdog:
            int i = PlayerActionUtils.value();
            int i2 = (i + 1) % 8;
            clientPlayerEntity.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i2));
            clientPlayerEntity.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
            break;
         case Override:
            SwitchOverride.run2();
      }
   }

   public AuraModule getAuraModule() {
      return this.auraModule;
   }

   public boolean testInt(int i) {
      return this.intValue <= -i;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public boolean check() {
      return switch ((AutoBlockHandler.AutoBlock)this.auraModule.autoBlockSetting.getValue()) {
         case None -> LegacyModule.check();
         case Override -> true;
         default -> this.flag;
      };
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isUsingItem() && clientPlayerEntity.getActiveItem().getUseAction().equals(UseAction.BLOCK);
   }

   public enum AutoBlock implements TextProvider {
      None("Does not do anything", false),
      Vanilla("Vanilla autoblock", false),
      Full("Releases before attack", false),
      Watchdog("Watchdog autoblock", false),
      Partial("Simulates how a legit player would block", false),
      Override("Simulates as if you were holding use key", true);

      public final String text;
      public final boolean disableInteraction;

      AutoBlock(final String description, final boolean disableInteraction) {
         this.text = description;
         this.disableInteraction = disableInteraction;
      }

      @Override
      public String getText() {
         return this.text;
      }

      public boolean isDisableInteraction() {
         return this.disableInteraction;
      }
   }

   public enum Unblock {
      Release,
      None;
   }
}
