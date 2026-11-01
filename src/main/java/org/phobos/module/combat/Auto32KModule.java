package org.phobos.module.combat;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import org.phobos.api.AreaScanner;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.inventory.InventoryUtils;

public class Auto32KModule extends PlacementModule {
   public final Setting<Auto32KModule.SlotIndex> slotIndex = this.resolveSetting2(new EnumSetting<>("SlotIndex", Auto32KModule.SlotIndex.Sword));

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (mc.currentScreen instanceof HopperScreen hopperScreen) {
         Slot slot2 = switch ((Auto32KModule.SlotIndex)this.slotIndex.getValue()) {
            case Empty -> (Slot)itemSwitcher.resolveObject(slot -> slot.getStack().getItem() instanceof SwordItem ? slot : null);
            case Sword -> itemSwitcher.getSlot3();
            case Selected -> itemSwitcher.resolveSlot3(itemSwitcher::testSlot4);
         };
         if (slot2 != null) {
            for (Slot slot : InventoryUtils.resolveList(hopperScreen.getScreenHandler())) {
               if (InventoryUtils.testItemStack(slot.getStack())) {
                  itemSwitcher.handleInt4(slot.id, slot2.getIndex(), SlotActionType.SWAP, hopperScreen.getScreenHandler());
                  break;
               }
            }
         }

         this.run11();
      } else {
         AreaScanner.Area area = new AreaScanner.Area();
         area.handleVec3d(clientPlayerEntity.getEyePos(), this.getDoubleValueSetting().getValue(), Predicates.alwaysTrue());
         List<BlockPos> list = area.resolveList(clientPlayerEntity);

         for (BlockPos blockPos : list) {
            if (clientWorld.getBlockState(blockPos).isOf(Blocks.HOPPER) && clientWorld.getBlockState(blockPos.up()).getBlock() instanceof ShulkerBoxBlock) {
               this.phobosClient
                  .getInteractionManager()
                  .handleBlockPos2(
                     blockPos,
                     clientPlayerEntity,
                     clientWorld,
                     this.getDirectionResolver(),
                     blockHitResult -> InteractionManager.Interaction.INSTANCE
                        .handleClientPlayerEntity(clientPlayerEntity, clientWorld, Hand.MAIN_HAND, blockHitResult)
                  );
               return;
            }
         }

         Block block = itemSwitcher.resolveBlock(Blocks.HOPPER);
         Block block2 = itemSwitcher.resolveObject(slot -> {
            if (slot.getStack().isEmpty()) {
               return null;
            }

            if (slot.getStack().getItem() instanceof BlockItem blockItem) {
               if (blockItem.getBlock() instanceof ShulkerBoxBlock shulkerBoxBlock) {
                  return InventoryUtils.testItemStack4(slot.getStack()) ? shulkerBoxBlock : null;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         });
         if (block != null && block2 != null) {
            for (BlockPos blockPos2 : list) {
               if (this.testBlockPos9(blockPos2, clientWorld)) {
                  this.resolvePlacement8(block, blockPos2);
                  this.resolvePlacement8(block2, blockPos2.up());
                  return;
               }
            }
         } else {
            this.run11();
         }
      }
   }

   public boolean testBlockPos9(BlockPos blockPos, ClientWorld clientWorld) {
      return !BlockUtils.testBlockPos7(blockPos, clientWorld)
         && !BlockUtils.testBlockPos7(blockPos.up(), clientWorld)
         && !BlockUtils.testBlockPos11(blockPos, VoxelShapes.fullCube(), clientWorld, this.getBooleanValueSetting2().getValue())
         && !BlockUtils.testBlockPos11(blockPos.up(), VoxelShapes.fullCube(), clientWorld, this.getBooleanValueSetting2().getValue());
   }

   public Auto32KModule(PhobosClient phobosClient) {
      super(phobosClient, "Auto32K", "Hopper 32k", Categories.combat, 2);
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   private enum SlotIndex {
      Sword,
      Selected,
      Empty;
   }
}
