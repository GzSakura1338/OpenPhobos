package org.phobos.core.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.phobos.api.BlockStateAccess;
import org.phobos.api.BlockStateAccess.BlockStateWorldImpl;
import org.phobos.mixin.game.world.IClientWorld;
import org.phobos.core.world.EntityLookupUtils;
import org.phobos.core.world.ProxyClientWorld;
import org.phobos.core.world.SimulatedWorld;

public abstract class ExplosionDamageCalculator {
   public abstract static class DelegatingEntityCopyingWorld extends ProxyClientWorld {
      public final SimulatedWorld simulatedWorld;

      public BlockView getChunkAsView(int chunkX, int chunkZ) {
         return this.simulatedWorld.getChunkAsView(chunkX, chunkZ);
      }

      public boolean doesNotIntersectEntities(Entity entity) {
         return this.simulatedWorld.doesNotIntersectEntities(entity);
      }

      public boolean doesNotIntersectEntities(Entity except, VoxelShape shape) {
         return this.simulatedWorld.doesNotIntersectEntities(except, shape);
      }

      public int getMaxLightLevel() {
         return this.simulatedWorld.getMaxLightLevel();
      }

      public BlockHitResult raycast(BlockStateRaycastContext context) {
         return this.simulatedWorld.raycast(context);
      }

      public <T extends Entity> void handleTypeFilter(TypeFilter<Entity, T> typeFilter, Box box, Predicate<? super T> predicate, List<? super T> list) {
         this.simulatedWorld.handleTypeFilter2(typeFilter, box, predicate, list);
      }

      public BlockState getBlockState(BlockPos pos) {
         return this.simulatedWorld.getBlockState(pos);
      }

      public List<VoxelShape> getEntityCollisions(Entity entity, Box box) {
         return this.simulatedWorld.getEntityCollisions(entity, box);
      }

      public FluidState getFluidState(BlockPos pos) {
         return this.simulatedWorld.getFluidState(pos);
      }

      public Iterable<VoxelShape> getCollisions(Entity entity, Box box) {
         return this.simulatedWorld.getCollisions(entity, box);
      }

      public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
         return this.simulatedWorld.getBlockEntity(pos, type);
      }

      public DelegatingEntityCopyingWorld(ClientWorld clientWorld, SimpleEntityLookup<Entity> simpleEntityLookup) {
         super(clientWorld);
         List<AbstractClientPlayerEntity> list = new ArrayList<>(clientWorld.getPlayers());
         this.simulatedWorld = new SimulatedWorld(simpleEntityLookup, list, clientWorld);
      }

      public <T extends Entity> void handleTypeFilter2(TypeFilter<Entity, T> typeFilter, Box box, Predicate<? super T> predicate, List<? super T> list, int i) {
         this.simulatedWorld.handleTypeFilter(typeFilter, box, predicate, list, i);
      }

      public Stream<BlockState> getStatesInBox(Box box) {
         return this.simulatedWorld.getStatesInBox(box);
      }

      public int getLuminance(BlockPos pos) {
         return this.simulatedWorld.getLuminance(pos);
      }

      public Iterable<VoxelShape> getBlockCollisions(Entity entity, Box box) {
         return this.simulatedWorld.getBlockCollisions(entity, box);
      }

      public Optional<Vec3d> findClosestCollision(Entity entity, VoxelShape shape, Vec3d target, double x, double y, double z) {
         return this.simulatedWorld.findClosestCollision(entity, shape, target, x, y, z);
      }

      public boolean isSpaceEmpty(Entity entity) {
         return this.simulatedWorld.isSpaceEmpty(entity);
      }

      @Override
      public int getBottomY() {
         return this.simulatedWorld.getBottomY();
      }

      public boolean isSpaceEmpty(Box box) {
         return this.simulatedWorld.isSpaceEmpty(box);
      }

      public boolean canCollide(Entity entity, Box box) {
         return this.simulatedWorld.canCollide(entity, box);
      }

      public boolean canPlace(BlockState state, BlockPos pos, ShapeContext context) {
         return this.simulatedWorld.canPlace(state, pos, context);
      }

      public boolean isBlockSpaceEmpty(Entity entity, Box box) {
         return this.simulatedWorld.isBlockSpaceEmpty(entity, box);
      }

      public BlockEntity getBlockEntity(BlockPos pos) {
         return this.simulatedWorld.getBlockEntity(pos);
      }

      public boolean isSpaceEmpty(Entity entity, Box box) {
         return this.simulatedWorld.isSpaceEmpty(entity, box);
      }

      public List<Entity> getOtherEntities(Entity except, Box box, Predicate<? super Entity> predicate) {
         return this.simulatedWorld.getOtherEntities(except, box, predicate);
      }

      public Optional<BlockPos> findSupportingBlockPos(Entity entity, Box box) {
         return this.simulatedWorld.findSupportingBlockPos(entity, box);
      }

      @Override
      public List<AbstractClientPlayerEntity> getPlayers() {
         return this.simulatedWorld.getPlayers();
      }

      public <T extends Entity> List<T> getEntitiesByType(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate) {
         return this.simulatedWorld.getEntitiesByType(filter, box, predicate);
      }

      @Override
      public int getHeight() {
         return this.simulatedWorld.getHeight();
      }

      public DelegatingEntityCopyingWorld(ClientWorld clientWorld) {
         this(clientWorld, EntityLookupUtils.createSimpleEntityLookup((SimpleEntityLookup)((IClientWorld)(Object)clientWorld).getEntityManager().getLookup()));
      }

      public WorldBorder getWorldBorder() {
         return this.simulatedWorld.getWorldBorder();
      }

      public <T extends Entity> List<T> getEntitiesByClass(Class<T> entityClass, Box box, Predicate<? super T> predicate) {
         return this.simulatedWorld.getEntitiesByClass(entityClass, box, predicate);
      }

      @Override
      public int getBottomSectionCoord() {
         return this.simulatedWorld.getBottomSectionCoord();
      }

      public PlayerEntity getPlayerByUuid(UUID uuid) {
         return this.simulatedWorld.getPlayerByUuid(uuid);
      }

      public Object getBlockEntityRenderData(BlockPos blockPos) {
         return this.simulatedWorld.getBlockEntityRenderData(blockPos);
      }

      @Override
      public int countVerticalSections() {
         return this.simulatedWorld.countVerticalSections();
      }

      public PlayerEntity getClosestPlayer(double x, double y, double z, double maxDistance, boolean ignoreCreative) {
         return this.simulatedWorld.getClosestPlayer(x, y, z, maxDistance, ignoreCreative);
      }

      public <T extends Entity> List<T> getNonSpectatingEntities(Class<T> entityClass, Box box) {
         return this.simulatedWorld.getNonSpectatingEntities(entityClass, box);
      }

      public List<PlayerEntity> getPlayers(TargetPredicate targetPredicate, LivingEntity entity, Box box) {
         return this.simulatedWorld.getPlayers(targetPredicate, entity, box);
      }

      public <T extends LivingEntity> List<T> getTargets(Class<T> entityClass, TargetPredicate targetPredicate, LivingEntity targetingEntity, Box box) {
         return this.simulatedWorld.getTargets(entityClass, targetPredicate, targetingEntity, box);
      }

      public int getSectionIndex(int y) {
         return this.simulatedWorld.getSectionIndex(y);
      }

      public boolean isPlayerInRange(double x, double y, double z, double range) {
         return this.simulatedWorld.isPlayerInRange(x, y, z, range);
      }

      public PlayerEntity getClosestPlayer(double x, double y, double z, double maxDistance, Predicate<Entity> targetPredicate) {
         return this.simulatedWorld.getClosestPlayer(x, y, z, maxDistance, targetPredicate);
      }

      public BlockHitResult raycastBlock(Vec3d start, Vec3d end, BlockPos pos, VoxelShape shape, BlockState state) {
         return this.simulatedWorld.raycastBlock(start, end, pos, shape, state);
      }

      public double getDismountHeight(BlockPos pos) {
         return this.simulatedWorld.getDismountHeight(pos);
      }

      public List<Entity> getOtherEntities(Entity except, Box box) {
         return this.simulatedWorld.getOtherEntities(except, box);
      }

      public <T extends LivingEntity> T getClosestEntity(
         List<? extends T> entityList, TargetPredicate targetPredicate, LivingEntity entity, double x, double y, double z
      ) {
         return (T)this.simulatedWorld.getClosestEntity(entityList, targetPredicate, entity, x, y, z);
      }

      public PlayerEntity getClosestPlayer(TargetPredicate targetPredicate, LivingEntity entity, double x, double y, double z) {
         return this.simulatedWorld.getClosestPlayer(targetPredicate, entity, x, y, z);
      }

      public BlockHitResult raycast(RaycastContext context) {
         return this.simulatedWorld.raycast(context);
      }

      public PlayerEntity getClosestPlayer(TargetPredicate targetPredicate, LivingEntity entity) {
         return this.simulatedWorld.getClosestPlayer(targetPredicate, entity);
      }

      public int sectionIndexToCoord(int index) {
         return this.simulatedWorld.sectionIndexToCoord(index);
      }

      @Override
      public int getTopY() {
         return this.simulatedWorld.getTopY();
      }

      public boolean isOutOfHeightLimit(BlockPos pos) {
         return this.simulatedWorld.isOutOfHeightLimit(pos);
      }

      public double getDismountHeight(VoxelShape blockCollisionShape, Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
         return this.simulatedWorld.getDismountHeight(blockCollisionShape, belowBlockCollisionShapeGetter);
      }

      public <T extends LivingEntity> T getClosestEntity(
         Class<? extends T> entityClass, TargetPredicate targetPredicate, LivingEntity entity, double x, double y, double z, Box box
      ) {
         return (T)this.simulatedWorld.getClosestEntity(entityClass, targetPredicate, entity, x, y, z, box);
      }

      @Override
      public int getTopSectionCoord() {
         return this.simulatedWorld.getTopSectionCoord();
      }

      public int sectionCoordToIndex(int coord) {
         return this.simulatedWorld.sectionCoordToIndex(coord);
      }

      public boolean hasBiomes() {
         return this.simulatedWorld.hasBiomes();
      }

      public boolean isOutOfHeightLimit(int y) {
         return this.simulatedWorld.isOutOfHeightLimit(y);
      }

      public PlayerEntity getClosestPlayer(TargetPredicate targetPredicate, double x, double y, double z) {
         return this.simulatedWorld.getClosestPlayer(targetPredicate, x, y, z);
      }

      public PlayerEntity getClosestPlayer(Entity entity, double maxDistance) {
         return this.simulatedWorld.getClosestPlayer(entity, maxDistance);
      }

      public RegistryEntry<Biome> getBiomeFabric(BlockPos blockPos) {
         return this.simulatedWorld.getBiomeFabric(blockPos);
      }
   }

   public abstract static class UnusableBlockStateWorld extends ProxyClientWorld implements BlockStateAccess {
      public final BlockStateWorldImpl blockStateWorldImpl;

      public boolean isSpaceEmpty(Entity entity) {
         return this.blockStateWorldImpl.isSpaceEmpty(entity);
      }

      public List<VoxelShape> getEntityCollisions(Entity entity, Box box) {
         return this.blockStateWorldImpl.getEntityCollisions(entity, box);
      }

      @Override
      public int getHeight() {
         return this.blockStateWorldImpl.getHeight();
      }

      public UnusableBlockStateWorld(ClientWorld clientWorld) {
         super(clientWorld);
         this.blockStateWorldImpl = new BlockStateWorldImpl(clientWorld);
      }

      public boolean isSpaceEmpty(Box box) {
         return this.blockStateWorldImpl.isSpaceEmpty(box);
      }

      public BlockView getBlockView() {
         return this.blockStateWorldImpl.getBlockView();
      }

      public BlockView getChunkAsView(int chunkX, int chunkZ) {
         return this.blockStateWorldImpl.getChunkAsView(chunkX, chunkZ);
      }

      public Optional<BlockPos> findSupportingBlockPos(Entity entity, Box box) {
         return this.blockStateWorldImpl.findSupportingBlockPos(entity, box);
      }

      @Override
      public int getBottomY() {
         return this.blockStateWorldImpl.getBottomY();
      }

      public BlockEntity getBlockEntity(BlockPos pos) {
         return this.blockStateWorldImpl.getBlockEntity(pos);
      }

      public Iterable<VoxelShape> getCollisions(Entity entity, Box box) {
         return this.blockStateWorldImpl.getCollisions(entity, box);
      }

      public WorldBorder getWorldBorder() {
         return this.blockStateWorldImpl.getWorldBorder();
      }

      public boolean isBlockSpaceEmpty(Entity entity, Box box) {
         return this.blockStateWorldImpl.isBlockSpaceEmpty(entity, box);
      }

      public Iterable<VoxelShape> getBlockCollisions(Entity entity, Box box) {
         return this.blockStateWorldImpl.getBlockCollisions(entity, box);
      }

      public boolean doesNotIntersectEntities(Entity except, VoxelShape shape) {
         return this.blockStateWorldImpl.doesNotIntersectEntities(except, shape);
      }

      public FluidState getFluidState(BlockPos pos) {
         return this.blockStateWorldImpl.getFluidState(pos);
      }

      @Override
      public BlockState getBlockState(BlockPos pos) {
         return this.blockStateWorldImpl.getBlockState(pos);
      }

      public boolean canCollide(Entity entity, Box box) {
         return this.blockStateWorldImpl.canCollide(entity, box);
      }

      @Override
      public ClientWorld getClientWorld() {
         return this.blockStateWorldImpl.getClientWorld();
      }

      public boolean doesNotIntersectEntities(Entity entity) {
         return this.blockStateWorldImpl.doesNotIntersectEntities(entity);
      }

      @Override
      public Map<BlockPos, BlockState> getBlockPosMap() {
         return this.blockStateWorldImpl.getBlockPosMap();
      }

      public boolean canPlace(BlockState state, BlockPos pos, ShapeContext context) {
         return this.blockStateWorldImpl.canPlace(state, pos, context);
      }

      public boolean isSpaceEmpty(Entity entity, Box box) {
         return this.blockStateWorldImpl.isSpaceEmpty(entity, box);
      }

      public Optional<Vec3d> findClosestCollision(Entity entity, VoxelShape shape, Vec3d target, double x, double y, double z) {
         return this.blockStateWorldImpl.findClosestCollision(entity, shape, target, x, y, z);
      }

      public int getMaxLightLevel() {
         return this.blockStateWorldImpl.getMaxLightLevel();
      }

      public boolean isOutOfHeightLimit(BlockPos pos) {
         return this.blockStateWorldImpl.isOutOfHeightLimit(pos);
      }

      public double getDismountHeight(VoxelShape blockCollisionShape, Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
         return this.blockStateWorldImpl.getDismountHeight(blockCollisionShape, belowBlockCollisionShapeGetter);
      }

      public int sectionCoordToIndex(int coord) {
         return this.blockStateWorldImpl.sectionCoordToIndex(coord);
      }

      public RegistryEntry<Biome> getBiomeFabric(BlockPos blockPos) {
         return this.blockStateWorldImpl.getBiomeFabric(blockPos);
      }

      public Object getBlockEntityRenderData(BlockPos blockPos) {
         return this.blockStateWorldImpl.getBlockEntityRenderData(blockPos);
      }

      public Stream<BlockState> getStatesInBox(Box box) {
         return this.blockStateWorldImpl.getStatesInBox(box);
      }

      public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
         return this.blockStateWorldImpl.getBlockEntity(pos, type);
      }

      @Override
      public int getTopSectionCoord() {
         return this.blockStateWorldImpl.getTopSectionCoord();
      }

      @Override
      public int countVerticalSections() {
         return this.blockStateWorldImpl.countVerticalSections();
      }

      public boolean isOutOfHeightLimit(int y) {
         return this.blockStateWorldImpl.isOutOfHeightLimit(y);
      }

      public BlockHitResult raycast(BlockStateRaycastContext context) {
         return this.blockStateWorldImpl.raycast(context);
      }

      public int sectionIndexToCoord(int index) {
         return this.blockStateWorldImpl.sectionIndexToCoord(index);
      }

      public BlockHitResult raycast(RaycastContext context) {
         return this.blockStateWorldImpl.raycast(context);
      }

      @Override
      public int getBottomSectionCoord() {
         return this.blockStateWorldImpl.getBottomSectionCoord();
      }

      @Override
      public int getTopY() {
         return this.blockStateWorldImpl.getTopY();
      }

      public int getSectionIndex(int y) {
         return this.blockStateWorldImpl.getSectionIndex(y);
      }

      public double getDismountHeight(BlockPos pos) {
         return this.blockStateWorldImpl.getDismountHeight(pos);
      }

      public BlockHitResult raycastBlock(Vec3d start, Vec3d end, BlockPos pos, VoxelShape shape, BlockState state) {
         return this.blockStateWorldImpl.raycastBlock(start, end, pos, shape, state);
      }

      public boolean hasBiomes() {
         return this.blockStateWorldImpl.hasBiomes();
      }

      public int getLuminance(BlockPos pos) {
         return this.blockStateWorldImpl.getLuminance(pos);
      }
   }
}
