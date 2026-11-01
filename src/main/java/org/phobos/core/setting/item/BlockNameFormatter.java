package org.phobos.core.setting.item;

import org.phobos.api.TextFormatter;
import org.phobos.core.block.BlockUtils;

public class BlockNameFormatter implements TextFormatter {
   public static final BlockNameFormatter INSTANCE = new BlockNameFormatter();

   @Override
   public String describeText(String text) {
      return BlockUtils.describeText(text);
   }

   public static BlockNameFormatter getINSTANCE() {
      return INSTANCE;
   }
}
