package org.phobos.core.setting;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import org.phobos.core.setting.item.EntityTypeCodec;
import org.phobos.core.entity.EntityUtils;

public class EntityTypeSetSetting extends SetSetting<EntityType<?>> {
   public EntityTypeSetSetting(String text, Set<EntityType<?>> set) {
      super(text, "", set, EntityTypeCodec.getINSTANCE(), entityType -> ItemStack.EMPTY, entityType -> entityType.getName().getString(), EntityUtils::describeText);
   }

   public EntityTypeSetSetting(String text, EntityType<?>... entityTypes) {
      this(text, new HashSet<>(Set.of(entityTypes)));
   }
}
