package org.phobos.module.combat;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.render.Highlights;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.module.player.SpeedmineModule;

public class AutoTNTModule extends PlacementModule {
   public final Setting<Long> delay;
   public final Setting<Integer> cPT;
   public final Setting<Boolean> align;
   public final Setting<Integer> carts;
   public final Map<PlayerEntity, Long> playerEntityMap;
   public final Setting<AutoTNTModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoTNTModule.Mode.Block));

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Set<BlockPos> set, @Nullable Item item, @Nullable Item item2
   ) {
      if (item != null
         && item2 != null
         && !set.stream()
            .anyMatch(
               blockPosx -> clientWorld.getBlockState(blockPosx).getBlock() instanceof AbstractRailBlock
                  || this.computeBox(new Box(blockPosx), clientWorld, false) > 0
            )) {
         for (BlockPos blockPos : set) {
            if (BlockUtils.testBlockPos7(blockPos.down(), clientWorld)) {
               SpeedmineModule speedmineModule = SpeedmineModule.getSpeedmineModule();
               if (speedmineModule.check7() && speedmineModule.testBlockPos3(blockPos)) {
                  speedmineModule.handleClientPlayerEntity(clientPlayerEntity);
               }

               this.resolvePlacement(item, blockPos);
               return;
            }
         }
      }
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      Set<BlockPos> set,
      int i,
      boolean flag
   ) {
      for (BlockPos blockPos : set) {
         if (AbstractRailBlock.isRail(clientWorld, blockPos)) {
            Box box = new Box(blockPos);
            if (this.testBox(box, clientWorld, flag, i)) {
               if (clientPlayerEntity.isCreative()) {
                  clientPlayerInteractionManager.breakBlock(blockPos);
                  return;
               }

               if (SpeedmineModule.getSpeedmineModule().check7()) {
                  SpeedmineModule.getSpeedmineModule().handleBlockPos(blockPos, clientWorld, clientPlayerEntity);
                  return;
               }

               this.phobosClient.getBlockBreakManager().handleToggleable3(this, blockPos);
               return;
            }
         }
      }
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.playerEntityMap.isEmpty()) {
         this.playerEntityMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
      }

      switch ((AutoTNTModule.Mode)this.mode.getValue()) {
         case Minecart:
            Block block2 = itemSwitcher.resolveBlock(Blocks.TNT);
            Item item = itemSwitcher.resolveItem2(Items.FLINT_AND_STEEL);
            if (block2 == null || item == null) {
               return;
            }

            label97:
            for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
               if (!this.playerEntityMap.containsKey(playerEntity) && EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 7.0)) {
                  Set<BlockPos> set = BlockPosUtils.resolveSet3(playerEntity, playerEntity.getBoundingBox().minY + 2.5);

                  for (BlockPos blockPos : set) {
                     if (clientWorld.getBlockState(blockPos).isOf(Blocks.TNT)) {
                        this.phobosClient
                           .getInteractionManager()
                           .handleBlockPos2(blockPos, clientPlayerEntity, clientWorld, this.getDirectionResolver(), blockHitResult -> {
                              itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 1);
                              clientPlayerInteractionManager.interactBlock(clientPlayerEntity, Hand.MAIN_HAND, blockHitResult);
                              if (this.getBooleanValueSetting4().getValue()) {
                                 clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                              }
                           });
                        this.playerEntityMap.put(playerEntity, System.currentTimeMillis() + this.delay.getValue());
                        continue label97;
                     }
                  }

                  for (BlockPos blockPos3 : set) {
                     Highlights.handleBox(new Box(blockPos3), Color.BLUE, 50L);
                     if (!BlockUtils.testBlockPos7(blockPos3, clientWorld)) {
                        if (this.align.getValue()) {
                           Block block = this.resolveBlock3(itemSwitcher);
                           if (block != null) {
                              this.resolvePlacement8(block, blockPos3.up());

                              for (Direction direction : DirectionUtils.directionArray) {
                                 BlockPos blockPos2 = blockPos3.offset(direction);
                                 if (!BlockUtils.testBlockPos7(blockPos2, clientWorld)) {
                                    this.resolvePlacement8(block, blockPos2);
                                 }
                              }
                           }
                        }

                        this.resolvePlacement8(block2, blockPos3);
                        break;
                     }
                  }
               }
            }
            break;
         case Block:
            if (this.phobosClient.getBlockBreakManager().testToggleable(this)) {
               this.phobosClient.getBlockBreakManager().handleToggleable2(this);
               return;
            }

            int i = this.carts.getValue();
            Item item2 = itemSwitcher.resolveItem2(Items.TNT_MINECART);
            Item item3 = itemSwitcher.resolveItem2(Items.RAIL, Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Items.POWERED_RAIL);
            Item item4 = itemSwitcher.resolveItem2(Items.FLINT_AND_STEEL);
            boolean flag = item2 == null;

            for (PlayerEntity playerEntity2 : clientWorld.getPlayers()) {
               if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity2, 7.0)) {
                  Set<BlockPos> set2 = BlockPosUtils.resolveSet3(playerEntity2, playerEntity2.getBoundingBox().minY + 0.25).stream().filter(blockPos -> {
                     BlockState blockState = clientWorld.getBlockState(blockPos);
                     return AbstractRailBlock.isRail(blockState) ? true : blockState.isReplaceable();
                  }).collect(Collectors.toSet());
                  if (!this.testSet(set2, clientWorld)) {
                     this.handleClientWorld(clientWorld, set2, item4, i, flag);
                     this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, set2, item3, item2);
                     this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, itemSwitcher, set2, item2);
                     this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, set2, i, flag);
                  }
               }
            }
      }
   }

   public void handleClientWorld(ClientWorld clientWorld, Set<BlockPos> set, @Nullable Item item, int i, boolean flag) {
      if (item != null && !set.stream().anyMatch(blockPosx -> AbstractRailBlock.isRail(clientWorld, blockPosx))) {
         for (BlockPos blockPos : set) {
            if (!(clientWorld.getBlockState(blockPos).getBlock() instanceof FireBlock)) {
               Box box = new Box(blockPos);
               if (this.testBox(box, clientWorld, flag, i)) {
                  this.resolvePlacement(item, blockPos);
                  return;
               }
            }
         }
      }
   }

   public AutoTNTModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTNT", "Places and ignites TNTs on top of enemy's head", Categories.combat, 4);
      this.carts = this.resolveSetting2(new NumberSetting<>("Carts", 5, 1, 40).resolveSetting4(this.mode, AutoTNTModule.Mode.Minecart));
      this.cPT = this.resolveSetting2(
         new NumberSetting<>("CPT", 1, 1, 20).resolveSetting7("Carts per tick").resolveSetting4(this.mode, AutoTNTModule.Mode.Minecart)
      );
      this.align = this.resolveSetting2(new BooleanSetting("Align").resolveSetting4(this.mode, AutoTNTModule.Mode.Block));
      this.delay = this.resolveSetting2(
         new LongNumberSetting("Delay", 500L, 0L, 1000L, true).resolveSetting7("Per-player delay").resolveSetting4(this.mode, AutoTNTModule.Mode.Block)
      );
      this.playerEntityMap = new HashMap<>();
      this.getSetSetting().getValue().clear();
   }

   public int computeBox(Box box, World world, boolean flag) {
      return world.getEntitiesByClass(TntMinecartEntity.class, box, tntMinecartEntity -> !flag || !tntMinecartEntity.isOnRail()).size();
   }

   public void handleClientPlayerEntity3(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ItemSwitcher itemSwitcher,
      Set<BlockPos> set,
      @Nullable Item item
   ) {
      if (item != null) {
         for (BlockPos blockPos : set) {
            if (AbstractRailBlock.isRail(clientWorld, blockPos)) {
               this.phobosClient.getInteractionManager().handleBlockPos2(blockPos, clientPlayerEntity, clientWorld, this.getDirectionResolver(), blockHitResult -> {
                  itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 1);

                  for (int i = 0; i < this.cPT.getValue(); i++) {
                     clientPlayerInteractionManager.interactBlock(clientPlayerEntity, Hand.MAIN_HAND, blockHitResult);
                  }

                  if (this.getBooleanValueSetting4().getValue()) {
                     clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                  }
               });
               return;
            }
         }
      }
   }

   public boolean testSet(Set<BlockPos> set, ClientWorld clientWorld) {
      return set.stream()
         .<Box>map(Box::new)
         .anyMatch(
            box -> EntityUtils.resolveList(clientWorld, TntMinecartEntity.class, box, tntMinecartEntity -> tntMinecartEntity.getFuseTicks() != -1)
               .stream()
               .findAny()
               .isPresent()
         );
   }

   public boolean testBox(Box box, ClientWorld clientWorld, boolean flag, int i) {
      return flag ? this.computeBox(box, clientWorld, true) != 0 : this.computeBox(box, clientWorld, true) >= i;
   }

   @NotNull
   public TntMinecartEntity resolveTntMinecartEntity(Box box, World world, boolean flag) {
      return (TntMinecartEntity)world.getEntitiesByClass(TntMinecartEntity.class, box, tntMinecartEntity -> !flag || !tntMinecartEntity.isOnRail())
         .stream()
         .findAny()
         .orElseThrow();
   }

   private enum Mode {
      Block,
      Minecart;
   }
}
