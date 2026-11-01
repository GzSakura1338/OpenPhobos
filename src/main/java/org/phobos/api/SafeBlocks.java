package org.phobos.api;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public interface SafeBlocks {
   Set<Block> blockSet = Sets.newHashSet(
      new Block[]{Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.ANVIL, Blocks.ENDER_CHEST, Blocks.NETHERITE_BLOCK}
   );
   Set<Block> blockSet2 = Sets.newHashSet(new Block[]{Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.ANVIL, Blocks.ENDER_CHEST});

   default Set<Block> getSet() {
      return blockSet;
   }

   default Set<Block> getSet2() {
      return blockSet2;
   }
}
