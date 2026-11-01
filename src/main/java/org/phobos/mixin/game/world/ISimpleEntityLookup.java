package org.phobos.mixin.game.world;

import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleEntityLookup.class)
public interface ISimpleEntityLookup<T extends EntityLike> {
   @Accessor("index")
   EntityIndex<T> getEntityIndex();

   @Accessor("cache")
   SectionedEntityCache<T> getEntityCache();
}
