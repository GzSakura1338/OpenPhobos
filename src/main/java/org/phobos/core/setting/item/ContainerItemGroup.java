package org.phobos.core.setting.item;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.phobos.core.block.BlockUtils;

public class ContainerItemGroup extends ItemGroup<Item> {
   public static final ContainerItemGroup INSTANCE = new ContainerItemGroup();

   public ContainerItemGroup() {
      super("Beds");

      for (Block block : BlockUtils.getBlockList2()) {
         if (block instanceof BedBlock bedBlock) {
            this.add(bedBlock.asItem());
         }
      }
   }

   public static ContainerItemGroup getINSTANCE() {
      return INSTANCE;
   }
}
