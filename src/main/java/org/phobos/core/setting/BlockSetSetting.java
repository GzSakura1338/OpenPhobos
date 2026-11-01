package org.phobos.core.setting;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import org.phobos.core.setting.item.BlockCodec;
import org.phobos.core.setting.item.BlockNameFormatter;
import org.phobos.core.setting.item.ContainerBlockGroup;
import org.phobos.core.setting.item.OreBlockGroup;
import org.phobos.core.setting.item.ShulkerBlockGroup;

public class BlockSetSetting extends SetSetting<Block> {
   public BlockSetSetting(String text, Set<Block> set) {
      super(
         text, "", set, BlockCodec.getINSTANCE(), block -> block.asItem().getDefaultStack(), block -> block.getName().getString(), BlockNameFormatter.getINSTANCE()
      );
      this.itemGroupSet.handleItemGroup(ShulkerBlockGroup.getINSTANCE());
      this.itemGroupSet.handleItemGroup(OreBlockGroup.getINSTANCE());
      this.itemGroupSet.handleItemGroup(ContainerBlockGroup.getINSTANCE());
   }

   public BlockSetSetting(String text, Block... blocks) {
      this(text, new HashSet<>(Set.of(blocks)));
   }
}
