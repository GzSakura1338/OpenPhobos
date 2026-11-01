package org.phobos.core.world;

import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.phobos.mixin.game.world.ISectionedEntityCache;
import org.phobos.mixin.game.world.ISimpleEntityLookup;

public final class EntityLookupUtils {
   public static <T extends EntityLike> EntityIndex<T> resolveEntityIndex(EntityIndex<T> entityIndex) {
      EntityIndex<T> entityIndex2 = new EntityIndex();
      entityIndex.iterate().forEach(entityIndex2::add);
      return entityIndex2;
   }

   public EntityLookupUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static <T extends EntityLike> SimpleEntityLookup<T> createSimpleEntityLookup(SimpleEntityLookup<T> simpleEntityLookup) {
      ISimpleEntityLookup<T> iSimpleEntityLookup = (ISimpleEntityLookup<T>)simpleEntityLookup;
      return new SimpleEntityLookup(resolveEntityIndex(iSimpleEntityLookup.getEntityIndex()), resolveSectionedEntityCache(iSimpleEntityLookup.getEntityCache()));
   }

   public static <T extends EntityLike> EntityTrackingSection<T> resolveEntityTrackingSection(
      Class<T> classValue, EntityTrackingSection<T> entityTrackingSection
   ) {
      EntityTrackingSection<T> entityTrackingSection2 = new EntityTrackingSection(classValue, entityTrackingSection.getStatus());
      entityTrackingSection.stream().forEach(entityTrackingSection2::add);
      return entityTrackingSection2;
   }

   public static <T extends EntityLike> SectionedEntityCache<T> resolveSectionedEntityCache(SectionedEntityCache<T> sectionedEntityCache) {
      ISectionedEntityCache<T> iSectionedEntityCache = (ISectionedEntityCache<T>)sectionedEntityCache;
      SectionedEntityCache<T> sectionedEntityCache2 = new SectionedEntityCache(iSectionedEntityCache.getEntityClass(), iSectionedEntityCache.getPosToStatus());
      iSectionedEntityCache.getTrackedPositions().forEach(l -> ((ISectionedEntityCache)(Object)sectionedEntityCache2).getTrackedPositions().add(l));
      iSectionedEntityCache.getTrackingSections()
         .forEach(
            (longValue, entityTrackingSection) -> ((ISectionedEntityCache)(Object)sectionedEntityCache2)
               .getTrackingSections()
               .putIfAbsent(longValue, resolveEntityTrackingSection(iSectionedEntityCache.getEntityClass(), entityTrackingSection))
         );
      return sectionedEntityCache2;
   }
}
