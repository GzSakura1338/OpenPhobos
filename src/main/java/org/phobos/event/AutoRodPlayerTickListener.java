package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.api.TimeGate;
import org.phobos.core.fakeplayer.TrackedFakePlayer;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.mixin.game.entity.IFishingBobberEntity;
import org.phobos.module.combat.AutoRodModule;
import org.phobos.module.combat.AutoRodModule;
import org.phobos.module.player.ScaffoldModule;
import org.phobos.module.player.SilentHotbarModule;

public final class AutoRodPlayerTickListener extends ModuleListener.Safe<AutoRodModule, PlayerTickEvent> {
   public final TimeGate.Normal normal = new TimeGate.Normal();

   public PlayerEntity resolvePlayerEntity(PlayerEntity playerEntity) {
      if (!this.module.prediction.getValue()) {
         return playerEntity;
      }

      if (playerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity) {
         TrackedFakePlayer[] trackedFakePlayers = iAbstractClientPlayerEntity.getPredictions();
         TrackedFakePlayer trackedFakePlayer = trackedFakePlayers[Math.min(
            this.module.predictionTicks.getValue() - 1, trackedFakePlayers.length - 1
         )];
         if (trackedFakePlayer != null) {
            return trackedFakePlayer;
         }
      }

      return playerEntity;
   }

   public AutoRodPlayerTickListener(AutoRodModule autoRodModule) {
      super(autoRodModule, PlayerTickEvent.class, 8);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
         switch (this.module.stage) {
            case None:
               if (clientPlayerEntity.isUsingItem() || ScaffoldModule.getScaffoldModule().check7()) {
                  return;
               }

               if (!this.normal.testNumber(this.module.delay.getValue())) {
                  return;
               }

               if (!itemSwitcher.testItem2(Items.FISHING_ROD)) {
                  return;
               }

               PlayerEntity playerEntity = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient);
               if (playerEntity == null) {
                  return;
               }

               double d = clientPlayerEntity.distanceTo(playerEntity);
               if (d > this.module.range.getValue() || d < this.module.distance.getValue() && !this.module.disable.getValue()) {
                  return;
               }

               PlayerEntity playerEntity2 = this.resolvePlayerEntity(playerEntity);
               Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.FISHING_ROD) ? slotx : null);
               if (slot == null) {
                  return;
               }

               float[] floats = RotationUtils.values2(clientPlayerEntity, playerEntity2.getPos().add(0.0, 0.2 + this.module.distance.getValue() * 0.1, 0.0));
               this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
               SilentHotbarModule.getSilentHotbarManager().handleObject3(this.module, slot, this.module.hold.getValue());
               this.module.floatArray = floats;
               this.module.stage = AutoRodModule.Stage.Aim;
               this.module.intValue2 = 0;
               break;
            case Aim:
               this.phobosClient.getRotationManager().testFloat(this.module.floatArray[0], this.module.floatArray[1]);
               if (this.module.step.testObject2(0) || this.module.intValue2++ >= this.module.step.getValue()) {
                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                  this.module.stage = AutoRodModule.Stage.Throw;
                  this.module.intValue = 0;
               }
               break;
            case Throw:
               if (this.module.timeout.getValue() && this.module.intValue++ > this.module.timeoutTicks.getValue()) {
                  this.module.stage = AutoRodModule.Stage.None;
                  this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                  if (this.module.disable.getValue()) {
                     this.module.run11();
                  }
               } else if (clientPlayerEntity.fishHook != null) {
                  IFishingBobberEntity iFishingBobberEntity = (IFishingBobberEntity)(Object)clientPlayerEntity.fishHook;
                  if (iFishingBobberEntity.getHookedEntity() != null) {
                     this.module.stage = AutoRodModule.Stage.None;
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                     if (this.module.disable.getValue()) {
                        this.module.run11();
                     }
                  }
               }
         }
      });
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      TickUtils.handleClientWorld(
         clientWorld,
         integer -> clientPlayerEntity.networkHandler
            .sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch()))
      );
   }
}
