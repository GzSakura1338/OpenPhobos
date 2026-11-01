package org.phobos.api;

import java.util.Collection;
import net.minecraft.block.Block;

public interface BlockCollection extends Iterable<Block> {
   Collection<Block> getBlockList();

   void handleBlock(Block var1);

   void handleCollection(Collection<Block> var1);

   boolean testBlock(Block var1);
}
