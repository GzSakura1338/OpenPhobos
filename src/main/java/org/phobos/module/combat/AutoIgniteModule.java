package org.phobos.module.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.entity.EntityUtils;

public class AutoIgniteModule extends PlacementModule {
   public final Setting<Map<AutoIgniteModule.Target, Boolean>> targets = this.resolveSetting2(
      new MultiSelectSetting<>("Targets", AutoIgniteModule.Target.class).resolveMultiSelectSetting6(AutoIgniteModule.Target.values())
   );
   public final Map<PlayerEntity, Long> playerEntityMap;
   public final Setting<Double> range = this.resolveSetting2(
      new NumberSetting<>("Range", 4.0, 1.0, 6.0).resolveSetting3(() -> this.targets.getValue().get(AutoIgniteModule.Target.Items))
   );
   public final Map<BlockPos, Item> blockPosMap = new ConcurrentHashMap<>();

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.playerEntityMap.entrySet().removeIf(entryx -> (Long)entryx.getValue() + 1500L < System.currentTimeMillis());
      if (!this.blockPosMap.isEmpty()) {
         for (Map.Entry<BlockPos, Item> entry : this.blockPosMap.entrySet()) {
            Item item2 = entry.getValue();
            BlockPos blockPos = entry.getKey();
            if (!item2.equals(Items.LAVA_BUCKET)) {
               this.phobosClient.getScheduler().handleRunnable2(() -> clientPlayerInteractionManager.attackBlock(blockPos, Direction.UP), 1);
               this.blockPosMap.remove(blockPos);
               return;
            }

            Item item = itemSwitcher.resolveItem2(Items.BUCKET);
            if (item != null) {
               this.phobosClient.getScheduler().handleRunnable2(() -> this.resolvePlacement(item, blockPos), 4);
               this.blockPosMap.remove(blockPos);
               return;
            }

            this.blockPosMap.remove(blockPos);
         }
      }

      Item item5 = itemSwitcher.resolveItem2(Items.LAVA_BUCKET, Items.FLINT_AND_STEEL);
      if (item5 != null) {
         for (Entity entity : clientWorld.getEntities()) {
            MutableBoolean mutableBoolean;
            MutableObject<BlockPos> mutableObject;
            mutableBoolean = new MutableBoolean();
            mutableObject = new MutableObject();
            label107:
            switch (entity) {
               case ItemEntity itemEntity:
                  if (!this.targets.getValue().get(AutoIgniteModule.Target.Items) || !itemEntity.isOnGround()) {
                     continue;
                  }

                  for (BlockPos blockPos2 : BlockPosUtils.resolveSet5(itemEntity)) {
                     if (clientWorld.getBlockState(blockPos2).getFluidState().isIn(FluidTags.LAVA)) {
                        Item item3 = itemSwitcher.resolveItem2(Items.BUCKET);
                        if (item3 != null) {
                           this.resolvePlacement(item3, blockPos2);
                        }
                     } else if (this.testClientPlayerEntity4(clientPlayerEntity, clientWorld, itemEntity)
                        && !this.testClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos2, item5)
                        && (!item5.equals(Items.FLINT_AND_STEEL) || !clientWorld.getBlockState(blockPos2).isOf(Blocks.FIRE))
                        && this.resolvePlacement(item5, blockPos2).isSuccessful()) {
                        mutableObject.setValue(blockPos2);
                        mutableBoolean.setTrue();
                        break label107;
                     }
                  }
                  break;
               case PlayerEntity playerEntity:
                  label138: {
                     if (!this.targets.getValue().get(AutoIgniteModule.Target.Players) || this.playerEntityMap.containsKey(playerEntity)) {
                        continue;
                     }

                     for (BlockPos blockPos3 : BlockPosUtils.resolveSet5(playerEntity)) {
                        if (clientWorld.getBlockState(blockPos3).getFluidState().isIn(FluidTags.LAVA)) {
                           Item item4 = itemSwitcher.resolveItem2(Items.BUCKET);
                           if (item4 != null) {
                              this.resolvePlacement(item4, blockPos3);
                           }
                        } else if (!playerEntity.isOnFire()
                           && EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 6.0)
                           && !this.testClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos3, item5)
                           && (!item5.equals(Items.FLINT_AND_STEEL) || !clientWorld.getBlockState(blockPos3).isOf(Blocks.FIRE))
                           && this.resolvePlacement(item5, blockPos3).isSuccessful()) {
                           if (!item5.equals(Items.FLINT_AND_STEEL)) {
                              mutableObject.setValue(blockPos3);
                              mutableBoolean.setTrue();
                           }

                           this.playerEntityMap.put(playerEntity, System.currentTimeMillis());
                           break;
                        }
                     }
                     break label138;
                  }
               default:
            }

            if (mutableBoolean.getValue()) {
               this.blockPosMap.put((BlockPos)mutableObject.getValue(), item5);
            }
         }
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Item item) {
      if (!item.equals(Items.LAVA_BUCKET)) {
         return false;
      }

      BlockHitResult blockHitResult = clientWorld.raycast(
         new RaycastContext(clientPlayerEntity.getEyePos(), blockPos.toCenterPos(), ShapeType.OUTLINE, FluidHandling.NONE, clientPlayerEntity)
      );
      return blockHitResult == null ? true : !blockPos.equals(blockHitResult.getBlockPos());
   }

   public AutoIgniteModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoIgnite", Categories.combat, 4);
      this.playerEntityMap = new HashMap<>();
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }

   public boolean testClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ItemEntity itemEntity) {
      double d = this.range.getValue();

      for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
         if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 6.0 + d) && itemEntity.distanceTo(playerEntity) <= d) {
            return true;
         }
      }

      return false;
   }

   private enum Target {
      Players,
      Items;
   }
}
