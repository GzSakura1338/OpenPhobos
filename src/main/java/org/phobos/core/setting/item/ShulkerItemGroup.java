package org.phobos.core.setting.item;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.Item;
import org.phobos.core.block.BlockUtils;

public class ShulkerItemGroup extends ItemGroup<Item> {
   public static final ShulkerItemGroup INSTANCE = new ShulkerItemGroup();

   public static ShulkerItemGroup getINSTANCE() {
      return INSTANCE;
   }

   public ShulkerItemGroup() {
      super("Shulkers");

      for (Block block : BlockUtils.getBlockList2()) {
         if (block instanceof ShulkerBoxBlock shulkerBoxBlock) {
            this.add(shulkerBoxBlock.asItem());
         }
      }
   }
}
