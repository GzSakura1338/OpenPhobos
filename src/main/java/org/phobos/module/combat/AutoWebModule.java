package org.phobos.module.combat;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.entity.EntityUtils;

public class AutoWebModule extends PlacementModule {
   public final Setting<Integer> ticks;
   public final Setting<Boolean> prediction;
   public final Setting<AutoWebModule.Mode> mode = new EnumSetting<>("Mode", AutoWebModule.Mode.Full).resolveSetting6(this);

   public AutoWebModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoWeb", "Places cobwebs at enemies", Categories.combat, 3);
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction").resolveSetting7("Extrapolates enemies"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 0, 6));
      this.resolveSettingGroup(this.prediction, this.ticks);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = itemSwitcher.resolveBlock(Blocks.COBWEB);
      if (block != null) {
         for (PlayerEntity playerEntity4 : clientWorld.getPlayers()) {
            if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity4, 7.0)) {
               PlayerEntity playerEntity2;
               if (this.prediction.getValue()) {
                  PlayerEntity playerEntity = this.phobosClient
                     .getFeatureRegistry()
                     .getPredictionManager()
                     .resolvePlayerEntity3(this.ticks.getValue(), playerEntity4);
                  playerEntity2 = playerEntity == null ? playerEntity4 : playerEntity;
               } else {
                  playerEntity2 = playerEntity4;
               }

               if (playerEntity2 != null) {
                  switch ((AutoWebModule.Mode)this.mode.getValue()) {
                     case Top:
                        this.resolvePlacement6(block.asItem(), BlockPos.ofFloored(playerEntity2.getPos()), null, false);
                        break;
                     case Bottom:
                        this.resolvePlacement6(block.asItem(), BlockPos.ofFloored(playerEntity2.getPos()).up(), null, false);
                        break;
                     case Full:
                        for (BlockPos blockPos : BlockPosUtils.resolveSet5(playerEntity2)) {
                           this.resolvePlacement6(block.asItem(), blockPos, null, false);
                        }
                  }
               }
            }
         }
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   private enum Mode {
      Bottom,
      Top,
      Full;
   }
}
