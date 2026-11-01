package org.phobos.core.world;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.phobos.core.combat.ExplosionDamageCalculator;

public class EntityLookupWorld extends ExplosionDamageCalculator.DelegatingEntityCopyingWorld {
   public EntityLookupWorld(ClientWorld clientWorld) {
      super(clientWorld);
   }

   @Override
   public int getTopY() {
      return this.getBottomY() + this.getHeight();
   }

   @Override
   public int countVerticalSections() {
      return this.getTopSectionCoord() - this.getBottomSectionCoord();
   }

   @Override
   public int getHeight() {
      return this.getDimension().height();
   }

   @Override
   public int getBottomSectionCoord() {
      return ChunkSectionPos.getSectionCoord(this.getBottomY());
   }

   public EntityLookupWorld(ClientWorld clientWorld, SimpleEntityLookup<Entity> simpleEntityLookup) {
      super(clientWorld, simpleEntityLookup);
   }

   @Override
   public int getBottomY() {
      return this.getDimension().minY();
   }

   @Override
   public int getTopSectionCoord() {
      return ChunkSectionPos.getSectionCoord(this.getTopY() - 1) + 1;
   }
}
