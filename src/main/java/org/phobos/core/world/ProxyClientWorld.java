package org.phobos.core.world;

import lombok.SneakyThrows;
import com.google.gson.internal.UnsafeAllocator;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.ClientWorld.Properties;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.map.MapState;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.DynamicRegistryManager.Entry;
import net.minecraft.registry.DynamicRegistryManager.Immutable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.DummyProfiler;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;
import net.minecraft.world.tick.QueryableTickScheduler;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.phobos.mixin.game.world.IClientWorld;

public abstract class ProxyClientWorld extends ClientWorld implements WorldAccess {
   public final ClientWorld clientWorld;

   public ProxyClientWorld(ClientWorld clientWorld) {
      this(clientWorld, resolveClientPlayNetworkHandler(clientWorld));
   }

   public void setTime(long time) {
      this.clientWorld.setTime(time);
   }

   public int countVerticalSections() {
      return this.getTopSectionCoord() - this.getBottomSectionCoord();
   }

   public int getTopY() {
      return this.getBottomY() + this.getHeight();
   }

   public void handleBlockUpdate(BlockPos pos, BlockState state, int flags) {
      this.clientWorld.handleBlockUpdate(pos, state, flags);
   }

   public void handlePlayerActionResponse(int sequence) {
      this.clientWorld.handlePlayerActionResponse(sequence);
   }

   public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
      return this.clientWorld.setBlockState(pos, state, flags, maxUpdateDepth);
   }

   public static ClientPlayNetworkHandler resolveClientPlayNetworkHandler(ClientWorld clientWorld) {
      return clientWorld instanceof IClientWorld iClientWorld
         ? iClientWorld.getConnection()
         : ProxyClientWorld.DummyClientPlayNetworkHandler.resolveDummyClientPlayNetworkHandler(clientWorld.getRegistryManager());
   }

   public Profiler getProfiler() {
      return DummyProfiler.INSTANCE;
   }

   public void enqueueChunkUpdate(Runnable updater) {
      this.clientWorld.enqueueChunkUpdate(updater);
   }

   public int getTopSectionCoord() {
      return ChunkSectionPos.getSectionCoord(this.getTopY() - 1) + 1;
   }

   public void tick(BooleanSupplier shouldKeepTicking) {
      this.clientWorld.tick(shouldKeepTicking);
   }

   public void calculateAmbientDarkness() {
      if (this.clientWorld != null) {
         this.clientWorld.calculateAmbientDarkness();
      }
   }

   public int getHeight() {
      return this.getDimension().height();
   }

   public boolean hasNoChunkUpdaters() {
      return this.clientWorld.hasNoChunkUpdaters();
   }

   public void runQueuedChunkUpdates() {
      this.clientWorld.runQueuedChunkUpdates();
   }

   public void setTimeOfDay(long timeOfDay) {
      this.clientWorld.setTimeOfDay(timeOfDay);
   }

   public ProxyClientWorld(ClientWorld clientWorld, ClientPlayNetworkHandler clientPlayNetworkHandler) {
      super(
         clientPlayNetworkHandler,
         clientWorld.getLevelProperties(),
         clientWorld.getRegistryKey(),
         clientWorld.getDimensionEntry(),
         0,
         clientWorld.getSimulationDistance(),
         () -> DummyProfiler.INSTANCE,
         null,
         clientWorld.isDebugWorld(),
         0L
      );
      this.clientWorld = clientWorld;
   }

   public void setSpawnPos(BlockPos pos, float angle) {
      if (this.clientWorld != null) {
         this.clientWorld.setSpawnPos(pos, angle);
      }
   }

   public int getBottomY() {
      return this.getDimension().minY();
   }

   public int getBottomSectionCoord() {
      return ChunkSectionPos.getSectionCoord(this.getBottomY());
   }

   public DimensionEffects getDimensionEffects() {
      return this.clientWorld.getDimensionEffects();
   }

   public void processPendingUpdate(BlockPos pos, BlockState state, Vec3d playerPos) {
      this.clientWorld.processPendingUpdate(pos, state, playerPos);
   }

   public void unloadBlockEntities(WorldChunk chunk) {
      this.clientWorld.unloadBlockEntities(chunk);
   }

   public void addFireworkParticle(
      double x, double y, double z, double velocityX, double velocityY, double velocityZ, List<FireworkExplosionComponent> explosions
   ) {
      this.clientWorld.addFireworkParticle(x, y, z, velocityX, velocityY, velocityZ, explosions);
   }

   public void addEntity(Entity entity) {
      this.clientWorld.addEntity(entity);
   }

   public int getRegularEntityCount() {
      return this.clientWorld.getRegularEntityCount();
   }

   public CrashReportSection addDetailsToCrashReport(CrashReport report) {
      return this.clientWorld.addDetailsToCrashReport(report);
   }

   public Iterable<Entity> getEntities() {
      return this.clientWorld.getEntities();
   }

   public void sendPacket(Packet<?> packet) {
      this.clientWorld.sendPacket(packet);
   }

   @Nullable
   public Entity getEntityById(int id) {
      return this.clientWorld.getEntityById(id);
   }

   public void playSoundFromEntity(Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
      this.clientWorld.playSoundFromEntity(entity, sound, category, volume, pitch);
   }

   public void playSoundFromEntity(
      @Nullable PlayerEntity source, Entity entity, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed
   ) {
      this.clientWorld.playSoundFromEntity(source, entity, sound, category, volume, pitch, seed);
   }

   public void tickEntity(Entity entity) {
      this.clientWorld.tickEntity(entity);
   }

   public boolean shouldUpdatePostDeath(Entity entity) {
      return this.clientWorld.shouldUpdatePostDeath(entity);
   }

   public void reloadColor() {
      this.clientWorld.reloadColor();
   }

   public void playSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
      this.clientWorld.playSound(x, y, z, sound, category, volume, pitch, useDistance);
   }

   public void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, @Nullable Block block, Mutable pos) {
      this.clientWorld.randomBlockDisplayTick(centerX, centerY, centerZ, radius, random, block, pos);
   }

   public boolean isChunkLoaded(int chunkX, int chunkZ) {
      return this.clientWorld.isChunkLoaded(chunkX, chunkZ);
   }

   public void disconnect() {
      this.clientWorld.disconnect();
   }

   public void playSound(
      @Nullable PlayerEntity source,
      double x,
      double y,
      double z,
      RegistryEntry<SoundEvent> sound,
      SoundCategory category,
      float volume,
      float pitch,
      long seed
   ) {
      this.clientWorld.playSound(source, x, y, z, sound, category, volume, pitch, seed);
   }

   public void removeEntity(int entityId, RemovalReason removalReason) {
      this.clientWorld.removeEntity(entityId, removalReason);
   }

   public void resetChunkColor(ChunkPos chunkPos) {
      this.clientWorld.resetChunkColor(chunkPos);
   }

   public void tickEntities() {
      this.clientWorld.tickEntities();
   }

   public void doRandomBlockDisplayTicks(int centerX, int centerY, int centerZ) {
      this.clientWorld.doRandomBlockDisplayTicks(centerX, centerY, centerZ);
   }

   public RecipeManager getRecipeManager() {
      return this.clientWorld.getRecipeManager();
   }

   public void updateListeners(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
      this.clientWorld.updateListeners(pos, oldState, newState, flags);
   }

   public void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
      this.clientWorld.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
   }

   public List<AbstractClientPlayerEntity> getPlayers() {
      return this.clientWorld.getPlayers();
   }

   public void syncGlobalEvent(int eventId, BlockPos pos, int data) {
      this.clientWorld.syncGlobalEvent(eventId, pos, data);
   }

   public ClientChunkManager getChunkManager() {
      return this.clientWorld.getChunkManager();
   }

   public QueryableTickScheduler<Block> getBlockTickScheduler() {
      return this.clientWorld.getBlockTickScheduler();
   }

   public void addImportantParticle(
      ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ
   ) {
      this.clientWorld.addImportantParticle(parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
   }

   public void putMapState(MapIdComponent id, MapState state) {
      this.clientWorld.putMapState(id, state);
   }

   public Vec3d getSkyColor(Vec3d cameraPos, float tickDelta) {
      return this.clientWorld.getSkyColor(cameraPos, tickDelta);
   }

   public void addImportantParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
      this.clientWorld.addImportantParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
   }

   @Nullable
   public MapState getMapState(MapIdComponent id) {
      return this.clientWorld.getMapState(id);
   }

   public void scheduleBlockRenders(int x, int y, int z) {
      this.clientWorld.scheduleBlockRenders(x, y, z);
   }

   public float getSkyBrightness(float tickDelta) {
      return this.clientWorld.getSkyBrightness(tickDelta);
   }

   public MapIdComponent increaseAndGetMapId() {
      return this.clientWorld.increaseAndGetMapId();
   }

   public void addParticle(ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
      this.clientWorld.addParticle(parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ);
   }

   public void scheduleBlockRerenderIfNeeded(BlockPos pos, BlockState old, BlockState updated) {
      this.clientWorld.scheduleBlockRerenderIfNeeded(pos, old, updated);
   }

   public void putClientsideMapState(MapIdComponent id, MapState state) {
      this.clientWorld.putClientsideMapState(id, state);
   }

   public TickManager getTickManager() {
      return this.clientWorld.getTickManager();
   }

   public QueryableTickScheduler<Fluid> getFluidTickScheduler() {
      return this.clientWorld.getFluidTickScheduler();
   }

   public Scoreboard getScoreboard() {
      return this.clientWorld.getScoreboard();
   }

   public void setBlockBreakingInfo(int entityId, BlockPos pos, int progress) {
      this.clientWorld.setBlockBreakingInfo(entityId, pos, progress);
   }

   public RegistryEntry<Biome> getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
      return this.clientWorld.getGeneratorStoredBiome(biomeX, biomeY, biomeZ);
   }

   public void syncWorldEvent(@Nullable PlayerEntity player, int eventId, BlockPos pos, int data) {
      this.clientWorld.syncWorldEvent(player, eventId, pos, data);
   }

   public float getBrightness(Direction direction, boolean shaded) {
      return this.clientWorld.getBrightness(direction, shaded);
   }

   public String asString() {
      return this.clientWorld.asString();
   }

   public Vec3d getCloudsColor(float tickDelta) {
      return this.clientWorld.getCloudsColor(tickDelta);
   }

   public int getColor(BlockPos pos, ColorResolver colorResolver) {
      return this.clientWorld.getColor(pos, colorResolver);
   }

   public Properties getLevelProperties() {
      return this.clientWorld.getLevelProperties();
   }

   public BrewingRecipeRegistry getBrewingRecipeRegistry() {
      return this.clientWorld.getBrewingRecipeRegistry();
   }

   public int getSimulationDistance() {
      return this.clientWorld.getSimulationDistance();
   }

   public void setLightningTicksLeft(int lightningTicksLeft) {
      this.clientWorld.setLightningTicksLeft(lightningTicksLeft);
   }

   public float getStarBrightness(float tickDelta) {
      return this.clientWorld.getStarBrightness(tickDelta);
   }

   public String toString() {
      return this.clientWorld.toString();
   }

   public void addBlockBreakParticles(BlockPos pos, BlockState state) {
      this.clientWorld.addBlockBreakParticles(pos, state);
   }

   public ClientWorld getClientWorld() {
      return this.clientWorld;
   }

   public void emitGameEvent(RegistryEntry<GameEvent> event, Vec3d emitterPos, Emitter emitter) {
      this.clientWorld.emitGameEvent(event, emitterPos, emitter);
   }

   public FeatureSet getEnabledFeatures() {
      return this.clientWorld.getEnabledFeatures();
   }

   public int getLightningTicksLeft() {
      return this.clientWorld.getLightningTicksLeft();
   }

   public void setSimulationDistance(int simulationDistance) {
      this.clientWorld.setSimulationDistance(simulationDistance);
   }

   public int calculateColor(BlockPos pos, ColorResolver colorResolver) {
      return this.clientWorld.calculateColor(pos, colorResolver);
   }

   @VisibleForTesting
   public static final class DummyClientPlayNetworkHandler extends ClientPlayNetworkHandler {
      public Immutable registryManager;

      public Immutable getRegistryManager() {
         return this.registryManager;
      }

      @SneakyThrows
      public static ProxyClientWorld.DummyClientPlayNetworkHandler resolveDummyClientPlayNetworkHandler(DynamicRegistryManager dynamicRegistryManager) {
         Immutable immutable2 = dynamicRegistryManager instanceof Immutable immutable ? immutable : new Immutable() {
            public Stream<Entry<?>> streamAllRegistries() {
               return dynamicRegistryManager.streamAllRegistries();
            }

            public <E> Optional<Registry<E>> getOptional(RegistryKey<? extends Registry<? extends E>> key) {
               return dynamicRegistryManager.getOptional(key);
            }
         };
         if (MinecraftClient.getInstance() == null) {
            ProxyClientWorld.DummyClientPlayNetworkHandler dummyClientPlayNetworkHandler = (ProxyClientWorld.DummyClientPlayNetworkHandler)UnsafeAllocator.INSTANCE
               .newInstance(ProxyClientWorld.DummyClientPlayNetworkHandler.class);
            dummyClientPlayNetworkHandler.registryManager = immutable2;
            return dummyClientPlayNetworkHandler;
         } else {
            return new ProxyClientWorld.DummyClientPlayNetworkHandler(immutable2);
         }
      }

      public DummyClientPlayNetworkHandler(Immutable immutable) {
         super(
            MinecraftClient.getInstance(),
            new ClientConnection(NetworkSide.SERVERBOUND),
            new ClientConnectionState(null, null, null, null, null, null, null, null, null, false, null, null)
         );
         this.registryManager = immutable;
      }
   }
}
