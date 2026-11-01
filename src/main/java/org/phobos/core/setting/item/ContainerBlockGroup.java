package org.phobos.core.setting.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class ContainerBlockGroup extends ItemGroup<Block> {
   public static final ContainerBlockGroup INSTANCE = new ContainerBlockGroup();

   public static ContainerBlockGroup getINSTANCE() {
      return INSTANCE;
   }

   public ContainerBlockGroup() {
      super("Containers");
      this.add(Blocks.CHEST);
      this.add(Blocks.TRAPPED_CHEST);
      this.add(Blocks.HOPPER);
      this.add(Blocks.BARREL);
      this.add(Blocks.FURNACE);
      this.add(Blocks.DISPENSER);
      this.add(Blocks.DROPPER);
   }
}
