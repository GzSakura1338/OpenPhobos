package org.phobos.core.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.BlockCollection;

public class BlockList implements BlockCollection {
   public final List<Block> blockList = new ArrayList<>();

   @Override
   public void handleBlock(Block block) {
      this.blockList.add(block);
   }

   public BlockList(Block... blocks) {
      this.blockList.addAll(Arrays.asList(blocks));
   }

   public List<Block> getBlockList() {
      return this.blockList;
   }

   @Override
   public void handleCollection(Collection<Block> collection) {
      this.blockList.addAll(collection);
   }

   @Override
   public boolean testBlock(Block block) {
      return this.blockList.contains(block);
   }

   @NotNull
   @Override
   public Iterator<Block> iterator() {
      return this.blockList.iterator();
   }
}
