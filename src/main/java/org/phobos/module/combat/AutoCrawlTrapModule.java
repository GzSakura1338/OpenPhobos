package org.phobos.module.combat;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;

public class AutoCrawlTrapModule extends PlacementModule {
   public final Setting<Boolean> prediction;
   public final Setting<Boolean> full;
   public final Setting<Float> extension = new NumberSetting<>("Extension", 0.0F, 0.0F, 0.5F).resolveNumberSetting("Off", 3.0F).resolveSetting6(this);
   public final Setting<Boolean> async;
   public final Setting<Integer> predictionTicks;

   public static void handleAutoCrawlTrapModule(
      AutoCrawlTrapModule autoCrawlTrapModule,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      autoCrawlTrapModule.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
   }

   public AutoCrawlTrapModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoCrawlTrap", Categories.combat, 2);
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
      this.predictionTicks = this.resolveSetting2(new NumberSetting<>("PredictionTicks", 1, 0, 6));
      this.full = this.resolveSetting2(new BooleanSetting("Full").resolveSetting7("Places at all predicted positions"));
      this.async = this.resolveSetting2(new BooleanSetting("Async").resolveSetting7("Updates with blocks"));
      this.resolveSettingGroup(this.prediction, this.predictionTicks, this.full);
      this.handleListener(
         new SafeListener<Receive<BlockUpdateS2CPacket>>() {
            public void handleObject(
               Receive<BlockUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AutoCrawlTrapModule.this.async.getValue()) {
                  if (((BlockUpdateS2CPacket)receive.getPacket()).getState().isAir()) {
                     AutoCrawlTrapModule.handleAutoCrawlTrapModule(AutoCrawlTrapModule.this, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
                  }
               }
            }
         }
      );
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, Block block) {
      Box box = this.phobosClient.getEntityPoseManager().resolveBox(entity, this.phobosClient.getEntityPoseManager().resolveEntityPose(entity));
      double d = this.extension.getValue().floatValue();

      for (BlockPos blockPos : (Set<BlockPos>)BlockPos.stream(box.stretch(d, 0.0, d).stretch(-d, 0.0, -d)).map(BlockPos::up).collect(Collectors.toSet())) {
         if (!BlockUtils.testBlockPos7(blockPos.down(), clientWorld)) {
            this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, blockPos);
         }
      }

      for (BlockPos blockPos2 : BlockPosUtils.resolveSet2(box, entity.getY() - 0.5)) {
         this.resolvePlacement8(block, blockPos2);
      }
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block;
      if ((block = this.resolveBlock2(itemSwitcher)) != null) {
         for (PlayerEntity playerEntity3 : clientWorld.getPlayers()) {
            if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity3, 6.0)) {
               if (!playerEntity3.getPose().equals(EntityPose.SWIMMING)
                  || playerEntity3.isTouchingWater()
                  || playerEntity3.fallDistance > 0.0F
                  || !playerEntity3.isOnGround()) {
                  return;
               }

               if (this.prediction.getValue()) {
                  if (this.full.getValue() && !this.predictionTicks.testObject2(0)) {
                     for (int i = 0; i < this.predictionTicks.getValue(); i++) {
                        PlayerEntity playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(i, playerEntity3);
                        if (playerEntity != null) {
                           this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, playerEntity, block);
                        }
                     }
                  } else {
                     PlayerEntity playerEntity2 = this.phobosClient
                        .getFeatureRegistry()
                        .getPredictionManager()
                        .resolvePlayerEntity3(this.predictionTicks.getValue(), playerEntity3);
                     if (playerEntity2 != null) {
                        this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, playerEntity2, block);
                     }
                  }
               }

               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, playerEntity3, block);
            }
         }
      }
   }
}
