package org.phobos.module.combat;

import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.entity.EntityUtils;

public class AntiMaceModule extends PlacementModule {
   public final Setting<Double> offset;
   public final Setting<Double> distance;
   public final Setting<Integer> ticks;
   public final Setting<AntiMaceModule.Blocking> block = this.resolveSetting2(new EnumSetting<>("Block", AntiMaceModule.Blocking.Original));

   public AntiMaceModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiMace", "Prevents players from macing you", Categories.combat, 11);
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 4, 1, 5));
      this.distance = this.resolveSetting2(new NumberSetting<>("Distance", 5.0, 4.0, 10.0));
      this.offset = this.resolveSetting2(new NumberSetting<>("Offset", 4.0, -1.0, 8.0));
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.resolveBlock2(itemSwitcher);
      if (block != null) {
         double d3 = clientPlayerEntity.getY();

         for (PlayerEntity playerEntity2 : clientWorld.getPlayers()) {
            if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity2, 40.0)) {
               double d = playerEntity2.getY();
               if (!(d <= d3) && !(d - d3 < this.distance.getValue())) {
                  PlayerEntity playerEntity = this.phobosClient
                     .getFeatureRegistry()
                     .getPredictionManager()
                     .resolvePlayerEntity3(this.ticks.getValue(), playerEntity2);
                  if (playerEntity != null && !(d <= playerEntity.getY())) {
                     double d2 = switch ((AntiMaceModule.Blocking)this.block.getValue()) {
                        case Prediction -> d;
                        case Original -> playerEntity.getY();
                     };

                     for (BlockPos blockPos : BlockPosUtils.resolveSet3(playerEntity, d2 - this.offset.getValue())) {
                        this.resolvePlacement8(block, blockPos);
                     }
                  }
               }
            }
         }
      }
   }

   private enum Blocking {
      Original,
      Prediction;
   }
}
