package org.phobos.core.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer.NextIteration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.EntityView;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

public class SimulatedWorld implements CollisionView, EntityView {
   public final ClientWorld clientWorld;
   public final List<AbstractClientPlayerEntity> players;
   public final SimpleEntityLookup<Entity> entitySimpleEntityLookup;

   public BlockState getBlockState(BlockPos pos) {
      return this.clientWorld.getBlockState(pos);
   }

   public List<VoxelShape> getEntityCollisions(@Nullable Entity entity, Box box) {
      return EntityView.super.getEntityCollisions(entity, box);
   }

   public SimulatedWorld(SimpleEntityLookup<Entity> simpleEntityLookup, List<AbstractClientPlayerEntity> list, ClientWorld clientWorld) {
      this.entitySimpleEntityLookup = simpleEntityLookup;
      this.players = list;
      this.clientWorld = clientWorld;
   }

   @Nullable
   public BlockEntity getBlockEntity(BlockPos pos) {
      return this.clientWorld.getBlockEntity(pos);
   }

   public int getHeight() {
      return this.clientWorld.getHeight();
   }

   public <T extends Entity> void handleTypeFilter(TypeFilter<Entity, T> typeFilter, Box box, Predicate<? super T> predicate, List<? super T> list, int i) {
      this.entitySimpleEntityLookup.forEachIntersects(typeFilter, box, entity -> {
         if (predicate.test((T)entity)) {
            list.add((T)entity);
            if (list.size() >= i) {
               return NextIteration.ABORT;
            }
         }

         if (entity instanceof EnderDragonEntity enderDragonEntity) {
            for (EnderDragonPart enderDragonPart : enderDragonEntity.getBodyParts()) {
               T entity2 = (T)typeFilter.downcast(enderDragonPart);
               if (entity2 != null && predicate.test(entity2)) {
                  list.add(entity2);
                  if (list.size() >= i) {
                     return NextIteration.ABORT;
                  }
               }
            }
         }

         return NextIteration.CONTINUE;
      });
   }

   public int getBottomY() {
      return this.clientWorld.getBottomY();
   }

   public List<AbstractClientPlayerEntity> getPlayers() {
      return this.players;
   }

   public <T extends Entity> void handleTypeFilter2(TypeFilter<Entity, T> typeFilter, Box box, Predicate<? super T> predicate, List<? super T> list) {
      this.handleTypeFilter(typeFilter, box, predicate, list, Integer.MAX_VALUE);
   }

   public FluidState getFluidState(BlockPos pos) {
      return this.clientWorld.getFluidState(pos);
   }

   public WorldBorder getWorldBorder() {
      return this.clientWorld.getWorldBorder();
   }

   public <T extends Entity> List<T> getEntitiesByType(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate) {
      ArrayList<T> arrayList = Lists.newArrayList();
      this.handleTypeFilter2(filter, box, predicate, arrayList);
      return arrayList;
   }

   @Nullable
   public BlockView getChunkAsView(int chunkX, int chunkZ) {
      return this.clientWorld.getChunkAsView(chunkX, chunkZ);
   }

   public List<Entity> getOtherEntities(@Nullable Entity except, Box box, Predicate<? super Entity> predicate) {
      ArrayList<Entity> arrayList = Lists.newArrayList();
      this.entitySimpleEntityLookup.forEachIntersects(box, entity2 -> {
         if (entity2 != except && predicate.test(entity2)) {
            arrayList.add(entity2);
         }

         if (entity2 instanceof EnderDragonEntity enderDragonEntity) {
            for (EnderDragonPart enderDragonPart : enderDragonEntity.getBodyParts()) {
               if (entity2 != except && predicate.test(enderDragonPart)) {
                  arrayList.add(enderDragonPart);
               }
            }
         }
      });
      return arrayList;
   }

   public boolean doesNotIntersectEntities(@Nullable Entity except, VoxelShape shape) {
      return CollisionView.super.doesNotIntersectEntities(except, shape);
   }
}
