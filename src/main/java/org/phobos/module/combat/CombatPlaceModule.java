package org.phobos.module.combat;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.placement.TimedPositionPoolBuilder;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;

public class CombatPlaceModule extends PlacementModule {
   public final Setting<Boolean> laggy;
   public final Setting<Boolean> scaffold = this.resolveSetting2(new BooleanSetting("Scaffold").resolveSetting7("Places blocks below the enemy"));
   public final Setting<Boolean> offset;
   public final Setting<Integer> scaffoldTicks = this.resolveSetting2(new NumberSetting<>("ScaffoldTicks", 2, 1, 5));
   public final Setting<Boolean> eyesExtend;
   public final Setting<Boolean> eyes;
   public final Setting<Integer> eyesTiming;

   public CombatPlaceModule(PhobosClient phobosClient) {
      super(phobosClient, "CombatPlace", "Places blocks to block enemy movement", Categories.combat, 18);
      this.laggy = this.resolveSetting2(new BooleanSetting("Laggy").resolveSetting7("Uses E-Chests"));
      this.eyes = this.resolveSetting2(new BooleanSetting("Eyes"));
      this.eyesTiming = this.resolveSetting2(new NumberSetting<>("EyesTiming", 4, 1, 5));
      this.eyesExtend = this.resolveSetting2(new BooleanSetting("EyesExtend"));
      this.offset = this.resolveSetting2(new BooleanSetting("Offset").resolveSetting7("Uses real prediction Y instead of base Y"));
      this.resolveSettingGroup(this.scaffold, this.scaffoldTicks, this.laggy);
      this.resolveSettingGroup(this.eyes, this.eyesTiming, this.eyesExtend);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.laggy.getValue() ? itemSwitcher.resolveBlock(Blocks.ENDER_CHEST) : this.resolveBlock2(itemSwitcher);
      if (block != null) {
         for (PlayerEntity playerEntity4 : clientWorld.getPlayers()) {
            if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity4, 8.0)) {
               PlayerEntity playerEntity3 = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(1, playerEntity4);
               if (playerEntity4.isOnGround() || playerEntity3 != null && playerEntity3.isOnGround()) {
                  if (this.scaffold.getValue()) {
                     for (int i = 1; i < this.scaffoldTicks.getValue(); i++) {
                        PlayerEntity playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(i, playerEntity4);
                        if (playerEntity != null) {
                           BlockPos blockPos = new BlockPos(
                              (int)Math.floor(playerEntity.getX()),
                              (int)Math.floor(this.offset.getValue() ? playerEntity.getY() : playerEntity4.getY()),
                              (int)Math.floor(playerEntity.getZ())
                           );
                           BlockPos blockPos2 = blockPos.down();
                           if (!BlockUtils.testBlockPos7(blockPos2, clientWorld)) {
                              this.resolvePlacement8(block, blockPos2);
                           }
                        }
                     }
                  }

                  if (this.eyes.getValue()) {
                     Map<BlockPos, Long> map = new HashMap<>();
                     TimedPositionPoolBuilder timedPositionPoolBuilder = new TimedPositionPoolBuilder(map);
                     map.put(
                        new BlockPos((int)Math.floor(playerEntity4.getX()), (int)Math.floor(playerEntity4.getY()), (int)Math.floor(playerEntity4.getZ())), 0L
                     );

                     for (int i2 = this.eyesTiming.getValue(); i2 < 5; i2++) {
                        PlayerEntity playerEntity2 = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(i2, playerEntity4);
                        if (playerEntity2 != null) {
                           BlockPos blockPos3 = new BlockPos(
                              (int)Math.floor(playerEntity2.getX()),
                              (int)Math.floor(this.offset.getValue() ? playerEntity2.getY() : playerEntity4.getY()),
                              (int)Math.floor(playerEntity2.getZ())
                           );
                           map.put(blockPos3, 0L);
                           BlockPos blockPos4 = blockPos3.up();
                           if (!BlockUtils.testBlockPos7(blockPos4, clientWorld)) {
                              PlacementModule.Placement placement = this.resolvePlacement8(block, blockPos4);
                              if (placement.isSuccessful()) {
                                 this.resolvePlacement8(block, blockPos4.up());
                                 break;
                              }

                              if (this.eyesExtend.getValue()
                                 && placement != PlacementModule.Placement.BLOCKED
                                 && this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, blockPos4, timedPositionPoolBuilder)) {
                                 this.resolvePlacement8(block, blockPos4.up());
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
