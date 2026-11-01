package org.phobos.module.combat;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.chunk.Hole;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;

public class HoleFillModule extends PlacementModule {
   public final Setting<Integer> predictionTicks;
   public final Setting<Boolean> prediction;
   public final Setting<Boolean> auto = this.resolveSetting2(new BooleanSetting("Auto"));
   public final Setting<Double> distance = this.resolveSetting2(new NumberSetting<>("Distance", 2.5, 1.0, 8.0));
   public final Setting<Boolean> partial;

   public HoleFillModule(PhobosClient phobosClient) {
      super(phobosClient, "HoleFill", "Fills holes around you", Categories.combat, 6);
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
      this.predictionTicks = this.resolveSetting2(new NumberSetting<>("PredictionTicks", 2, 1, 5).resolveNumberSetting("Latency", 5));
      this.partial = this.resolveSetting2(new BooleanSetting("Partial"));
      this.createBuilder()
         .resolveBuilder6(this.auto)
         .resolveBuilder5(this.resolveSettingGroup(this.prediction, this.predictionTicks))
         .resolveBuilder(this.distance)
         .getSettingSubsection();
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = itemSwitcher.resolveBlock(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.ENDER_CHEST);
      if (block != null) {
         Set<Hole> set = new HashSet<>();
         double d3 = MathHelper.square(this.distance.getValue());
         double d4 = MathHelper.square(this.getDoubleValueSetting().getValue());

         for (Hole hole : this.getPhobosClient().getHoleChunkScanner().getCollection()) {
            if (hole.computeEntity(clientPlayerEntity) <= d4
               && hole.isFlag2()
               && hole.testWorld(clientWorld)
               && (this.partial.getValue() || hole.isFlag22())) {
               set.add(hole);
            }
         }

         if (!set.isEmpty()) {
            if (!this.auto.getValue()) {
               for (Hole hole2 : set) {
                  for (BlockPos blockPos : hole2.getBlockPosSet()) {
                     this.resolvePlacement8(block, blockPos);
                  }
               }

               this.run11();
            } else {
               for (PlayerEntity playerEntity2 : clientWorld.getPlayers()) {
                  if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity2, 14.0)
                     && !this.phobosClient.getSurroundManager().testPlayerEntity2(playerEntity2)
                     && !BlockUtils.testPlayerEntity(playerEntity2, clientWorld)
                     && !this.phobosClient.getSurroundManager().testPlayerEntity(playerEntity2, true)) {
                     if (this.prediction.getValue()) {
                        PlayerEntity playerEntity = this.predictionTicks.getValue().equals(5)
                           ? this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity(playerEntity2)
                           : this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.predictionTicks.getValue(), playerEntity2);
                        if (playerEntity != null) {
                           playerEntity2 = playerEntity;
                        }
                     }

                     Hole hole4 = null;
                     double d2 = Double.MAX_VALUE;

                     for (Hole hole3 : set) {
                        double d = hole3.computeEntity(playerEntity2);
                        if (!(d >= d2)) {
                           hole4 = hole3;
                           d2 = d;
                        }
                     }

                     if (hole4 != null && !(d2 > d3) && !hole4.testEntity2(playerEntity2)) {
                        if (hole4.isFlag6()) {
                           Vec3d vec3d = playerEntity2.getPos();

                           for (BlockPos blockPos2 : hole4.getBlockPosSet()
                              .stream()
                              .sorted(Comparator.comparingDouble(blockPos -> -blockPos.getSquaredDistanceFromCenter(vec3d.x, vec3d.y, vec3d.z)))
                              .toList()) {
                              if (this.resolvePlacement8(block, blockPos2).isSuccessful()) {
                                 break;
                              }
                           }
                        } else {
                           for (BlockPos blockPos3 : hole4.getBlockPosSet()) {
                              this.resolvePlacement8(block, blockPos3);
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
