package org.phobos.module.combat;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;

public class AntiPhaseModule extends PlacementModule {
   public final Setting<AntiPhaseModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AntiPhaseModule.Mode.Scaffold));
   public final Setting<Boolean> reduce;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 6.0, 0.1, 8.0));

   public boolean testClientWorld(ClientWorld clientWorld, Set<BlockPos> set) {
      Box box = BoxUtils.createBox7(set.stream().<BlockPos>map(BlockPos::up).collect(Collectors.toSet()));

      for (Entity entity : clientWorld.getEntities()) {
         if (entity.getBoundingBox().intersects(box) && entity instanceof ItemFrameEntity) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AntiPhaseModule.Mode)this.mode.getValue()) {
         case Scaffold:
            Block block = itemSwitcher.resolveBlock(Blocks.LADDER);
            if (block == null) {
               return;
            }

            for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
               if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, this.range.getValue())
                  && (!this.reduce.getValue() || !this.phobosClient.getSurroundManager().testPlayerEntity2(playerEntity))) {
                  for (BlockPos blockPos : BlockPosUtils.resolveSet3(playerEntity, playerEntity.getY() - 0.5)) {
                     this.resolvePlacement6(block.asItem(), blockPos.up(), null, false);
                  }
               }
            }
            break;
         case Frame:
            Block block2 = itemSwitcher.resolveBlock(Blocks.SCAFFOLDING);
            if (block2 == null) {
               return;
            }

            for (PlayerEntity playerEntity2 : clientWorld.getPlayers()) {
               if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity2, this.range.getValue())
                  && (!this.reduce.getValue() || !this.phobosClient.getSurroundManager().testPlayerEntity2(playerEntity2))) {
                  for (BlockPos blockPos2 : BlockPosUtils.resolveSet3(playerEntity2, playerEntity2.getY() - 0.5)) {
                     this.resolvePlacement6(block2.asItem(), blockPos2.up(), null, false);
                  }
               }
            }
            break;
         case Ladder:
            Item item = itemSwitcher.resolveItem2(Items.ITEM_FRAME, Items.GLOW_ITEM_FRAME);
            if (item == null) {
               return;
            }

            for (PlayerEntity playerEntity3 : clientWorld.getPlayers()) {
               if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity3, this.range.getValue())
                  && (!this.reduce.getValue() || !this.phobosClient.getSurroundManager().testPlayerEntity2(playerEntity3))) {
                  Set<BlockPos> set3 = BlockPosUtils.resolveSet3(playerEntity3, playerEntity3.getY() - 0.5)
                     .stream()
                     .filter(blockPos -> BlockUtils.testBlockPos7(blockPos, clientWorld) && !BlockUtils.testBlockPos7(blockPos.up(), clientWorld))
                     .filter(blockPos -> clientPlayerEntity.getEyePos().distanceTo(blockPos.toCenterPos()) <= this.range.getValue())
                     .collect(Collectors.toSet());
                  if (!set3.isEmpty() && !this.testClientWorld(clientWorld, set3)) {
                     for (BlockPos blockPos3 : new HashSet<>(set3)) {
                        this.resolvePlacement13(item, blockPos3.up(), null, false, false, blockPos3);
                     }
                  }
               }
            }
      }
   }

   public AntiPhaseModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiPhase", "Places certain to prevent targets from phasing", Categories.combat, 8);
      this.reduce = this.resolveSetting2(new BooleanSetting("Reduce").resolveSetting7("Only places when the target is surrounded"));
   }

   private enum Mode {
      Ladder,
      Scaffold,
      Frame;
   }
}
