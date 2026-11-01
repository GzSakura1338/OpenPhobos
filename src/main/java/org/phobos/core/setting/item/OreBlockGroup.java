package org.phobos.core.setting.item;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import org.phobos.core.block.BlockUtils;

public class OreBlockGroup extends ItemGroup<Block> {
   public static final OreBlockGroup INSTANCE = new OreBlockGroup();

   public static OreBlockGroup getINSTANCE() {
      return INSTANCE;
   }

   public OreBlockGroup() {
      super("Beds");

      for (Block block : BlockUtils.getBlockList2()) {
         if (block instanceof BedBlock bedBlock) {
            this.add(bedBlock);
         }
      }
   }
}
