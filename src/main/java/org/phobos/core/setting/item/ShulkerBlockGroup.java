package org.phobos.core.setting.item;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import org.phobos.core.block.BlockUtils;

public class ShulkerBlockGroup extends ItemGroup<Block> {
   public static final ShulkerBlockGroup INSTANCE = new ShulkerBlockGroup();

   public static ShulkerBlockGroup getINSTANCE() {
      return INSTANCE;
   }

   public ShulkerBlockGroup() {
      super("Shulkers");

      for (Block block : BlockUtils.getBlockList2()) {
         if (block instanceof ShulkerBoxBlock shulkerBoxBlock) {
            this.add(shulkerBoxBlock);
         }
      }
   }
}
