package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.core.fakeplayer.TrackedFakePlayer;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.combat.AutoThrowModule;
import org.phobos.module.combat.AutoThrowModule;
import org.phobos.module.combat.AutoThrowModule;
import org.phobos.module.player.ScaffoldModule;
import org.phobos.module.player.SilentHotbarModule;

public final class AutoThrowPlayerTickListener extends ModuleListener.Safe<AutoThrowModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(
            itemSwitcher -> {
               switch (this.module.stage) {
                  case None:
                     if (clientPlayerEntity.isUsingItem() || ScaffoldModule.getScaffoldModule().check7()) {
                        return;
                     }
                     PlayerEntity playerEntity3 = switch ((AutoThrowModule.Target)this.module.target.getValue()) {
                        case Auto -> EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient);
                        case Sync -> AuraModule.getEntity() instanceof PlayerEntity playerEntity ? playerEntity : null;
                     };
                     if (playerEntity3 == null) {
                        return;
                     }

                     this.module.playerEntity = this.resolvePlayerEntity(playerEntity3);
                     if (this.module.playerEntity == null) {
                        return;
                     }

                     if (!this.module.testClientPlayerEntity(clientPlayerEntity, this.module.playerEntity)
                        && !this.module.normal.testNumber2(this.module.delay.getValue())) {
                        return;
                     }

                     Slot slot = itemSwitcher.resolveObject(slotx -> {
                        ItemStack itemStack = slotx.getStack();
                        return !itemStack.isOf(Items.SNOWBALL) && !itemStack.isOf(Items.EGG) ? null : slotx;
                     });
                     if (slot == null) {
                        return;
                     }

                     float[] floats = RotationUtils.values2(clientPlayerEntity, this.module.playerEntity.getPos());
                     float f = mc.getRenderTickCounter().getTickDelta(false);
                     float[] floats2 = this.values(clientPlayerEntity, clientWorld, this.module.playerEntity, floats[0], floats[1], f);
                     if (floats2.length != 2) {
                        return;
                     }

                     SilentHotbarModule.getSilentHotbarManager()
                        .handleObject3(
                           this.module,
                           slot,
                           2 + this.module.balance.getValue(),
                           SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder4(() -> {
                              if (this.module.disable.getValue() && this.module.check7()) {
                                 this.module.run11();
                              }
                           }).getSelectionConfig()
                        );
                     this.module.stage = AutoThrowModule.Stage.Aim;
                     this.phobosClient.getRotationManager().testFloat(floats2[0], floats2[1]);
                     this.module.floatArray = floats2;
                     this.module.intValue2 = 0;
                     break;
                  case Aim:
                     this.phobosClient.getRotationManager().testFloat(this.module.floatArray[0], this.module.floatArray[1]);
                     if (this.module.balance.testObject2(0) || this.module.intValue2++ >= this.module.balance.getValue()) {
                        TickUtils.handleClientWorld(
                           clientWorld,
                           integer -> clientPlayerEntity.networkHandler
                              .sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, this.module.floatArray[0], this.module.floatArray[1]))
                        );
                        this.module.stage = AutoThrowModule.Stage.Throw;
                     }
                     break;
                  case Throw:
                     SilentHotbarModule.getSilentHotbarManager().handleObject2(this.module);
                     this.module.stage = AutoThrowModule.Stage.None;
                     this.module.normal.run();
                     this.module.intValue = 0;
               }
            }
         );
   }

   public AutoThrowPlayerTickListener(AutoThrowModule autoThrowModule) {
      super(autoThrowModule, PlayerTickEvent.class, 7);
   }

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

   public float[] values(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, PlayerEntity playerEntity, float f, float f2, float f3) {
      Vec3d vec3d2 = EntityInterpolation.createVec3d(clientPlayerEntity, f3).add(0.0, clientPlayerEntity.getEyeHeight(clientPlayerEntity.getPose()), 0.0);

      for (float f5 = f2 - 30.0F; f5 <= f2 + 20.0F; f5 += 2.0F) {
         for (float f4 = f - 2.0F; f4 <= f + 2.0F; f4++) {
            Vec3d vec3d = TrajectoryUtils.resolveVec3d2(clientPlayerEntity, f5, f4, 0.0F, 1.5F);
            TrajectoryUtils.TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
               clientPlayerEntity, clientWorld, null, vec3d2, vec3d, TrajectoryUtils.EntityGravityType.Normal, TrajectoryUtils.Drag.Normal, playerEntity
            );
            if (trajectoryData != null && trajectoryData.entity != null && trajectoryData.entity.equals(playerEntity)) {
               return new float[]{f4, f5};
            }
         }
      }

      return new float[0];
   }
}
