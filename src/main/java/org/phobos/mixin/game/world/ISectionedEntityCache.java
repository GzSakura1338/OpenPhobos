package org.phobos.mixin.game.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.EntityTrackingStatus;
import net.minecraft.world.entity.SectionedEntityCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SectionedEntityCache.class)
public interface ISectionedEntityCache<T extends EntityLike> {
   @Accessor("trackedPositions")
   LongSortedSet getTrackedPositions();

   @Accessor("trackingSections")
   Long2ObjectMap<EntityTrackingSection<T>> getTrackingSections();

   @Accessor("posToStatus")
   Long2ObjectFunction<EntityTrackingStatus> getPosToStatus();

   @Accessor("entityClass")
   Class<T> getEntityClass();
}
