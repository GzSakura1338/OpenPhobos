package org.phobos.core.setting.item;

import net.minecraft.entity.EntityType;
import org.phobos.api.NameCodec;
import org.phobos.core.entity.EntityUtils;

public class EntityTypeCodec implements NameCodec<EntityType<?>> {
   public static final EntityTypeCodec INSTANCE = new EntityTypeCodec();

   public EntityType<?> resolveObject(String text) {
      return EntityUtils.resolveEntityType(text);
   }

   public static EntityTypeCodec getINSTANCE() {
      return INSTANCE;
   }

   public String describeBlock(EntityType<?> entityType) {
      return entityType.getName().getString();
   }
}
