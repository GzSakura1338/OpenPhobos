package org.phobos.core.setting.item;

import net.minecraft.block.Block;
import org.phobos.api.NameCodec;
import org.phobos.core.block.BlockUtils;

public class BlockCodec implements NameCodec<Block> {
   public static final BlockCodec INSTANCE = new BlockCodec();

   public Block resolveObject(String text) {
      return BlockUtils.resolveBlock(text);
   }

   public String describeBlock(Block block) {
      return block.getName().getString();
   }

   public static BlockCodec getINSTANCE() {
      return INSTANCE;
   }
}
