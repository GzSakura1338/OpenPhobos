package org.phobos.core.block;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ToolMaterials;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Toggleable;
import org.phobos.api.TimeGate;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.client.EngineModule;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.module.player.SpeedmineModule.BreakEvent;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.Feature;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.tick.TimeUtils;

public class BlockBreakManager extends Feature {
   public final Map<BlockPos, Long> blockPosMap;
   @Nullable
   public BlockBreakManager.BreakLock breakLock;
   public final Map<BlockPos, Long> blockPosMap2;
   public final Map<BlockPos, BlockBreakManager.Progress> blockPosMap3 = new ConcurrentHashMap<>(0);

   public void handleToggleable(Toggleable toggleable) {
      if (!SpeedmineModule.getSpeedmineModule().check7()) {
         if (this.breakLock != null && Objects.equals(this.breakLock.getToggleable(), toggleable)) {
            this.breakLock = null;
         }
      }
   }

   public static float value() {
      float f = ToolMaterials.NETHERITE.getMiningSpeedMultiplier();
      if (f > 1.0F) {
         f += 26.0F;
      }

      return Math.max(f, 0.0F);
   }

   @Nullable
   public BlockPos getBlockPos() {
      return this.breakLock != null ? this.breakLock.getBlockPos() : null;
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager, BlockPos blockPos
   ) {
      if (!clientPlayerEntity.isUsingItem()) {
         if (!clientWorld.getBlockState(blockPos).isAir()) {
            Direction direction = this.resolveDirection(clientPlayerEntity, clientWorld, blockPos);
            if (clientPlayerInteractionManager.updateBlockBreakingProgress(blockPos, direction)) {
               mc.particleManager.addBlockBreakingParticles(blockPos, direction);
               clientPlayerEntity.swingHand(Hand.MAIN_HAND);
            }
         }
      } else {
         clientPlayerInteractionManager.cancelBlockBreaking();
      }
   }

   public boolean testBlockPos(BlockPos blockPos) {
      return this.blockPosMap2.containsKey(blockPos);
   }

   public void handleBlockPos(BlockPos blockPos, @Nullable PlayerEntity playerEntity, int i) {
      BlockBreakManager.Progress progress2 = this.blockPosMap3.get(blockPos);
      if (progress2 != null) {
         progress2.resolveProgress2(i);
      } else if (!EngineModule.getEngineModule().check4()) {
         this.blockPosMap3.put(blockPos, new BlockBreakManager.Progress(blockPos, TimeUtils.value(), i, playerEntity, false));
      } else {
         boolean flag = SpeedmineModule.getSpeedmineModule().getDoubleValue().getValue();
         if (flag) {
            BlockBreakManager.Progress progress = this.blockPosMap3
               .values()
               .stream()
               .filter(progressx -> progressx.getPlayerEntity() == playerEntity && !progressx.isFlag())
               .findAny()
               .orElse(null);
            if (progress != null
               && this.blockPosMap3.values().stream().noneMatch(progressx -> progressx.getPlayerEntity() == playerEntity && progressx.isFlag())) {
               this.blockPosMap3.put(blockPos, new BlockBreakManager.Progress(blockPos, TimeUtils.value(), i, playerEntity, false));
               progress.resolveProgress(true);
               return;
            }
         }

         this.blockPosMap3
            .values()
            .stream()
            .filter(progressx -> progressx.getPlayerEntity() == playerEntity && !progressx.isFlag())
            .findAny()
            .ifPresent(progressx -> this.blockPosMap3.remove(progressx.blockPos));
         this.blockPosMap3.put(blockPos, new BlockBreakManager.Progress(blockPos, TimeUtils.value(), i, playerEntity, false));
      }
   }

   public Map<BlockPos, Long> getBlockPosMap2() {
      return this.blockPosMap2;
   }

   public boolean testBlockPos2(BlockPos blockPos, ClientWorld clientWorld) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      return blockState.getHardness(clientWorld, blockPos) <= 0.0F || blockState.isAir();
   }

   public Map<BlockPos, Long> getBlockPosMap() {
      return this.blockPosMap;
   }

   public Map<BlockPos, BlockBreakManager.Progress> getBlockPosMap3() {
      return this.blockPosMap3;
   }

   public void handleBlockPos2(BlockPos blockPos, PlayerEntity playerEntity, int i) {
      BlockBreakManager.Progress progress = this.blockPosMap3.get(blockPos);
      if (progress != null) {
         progress.resolveProgress2(progress.intValue + i);
         progress.resolveProgress3(playerEntity);
      } else {
         this.blockPosMap3.put(blockPos, new BlockBreakManager.Progress(blockPos, TimeUtils.value(), i, playerEntity, false));
      }
   }

   public Direction resolveDirection(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      Direction direction2 = ShapeDirectionResolver.shapeDirectionResolver.resolveDirection(blockPos, clientPlayerEntity, clientWorld);
      Direction direction;
      if (direction2 == null) {
         direction = ExactDirectionResolver.exactDirectionResolver.resolveDirection(blockPos, clientPlayerEntity, clientWorld);
      } else {
         direction = direction2;
      }

      return direction;
   }

   public static float computeBlockPos(BlockPos blockPos, BlockState blockState, ClientWorld clientWorld) {
      float f = blockState.getHardness(clientWorld, blockPos);
      if (f == -1.0F || blockState.isAir()) {
         f = Blocks.OBSIDIAN.getDefaultState().getHardness(clientWorld, blockPos);
      }

      return value() / f / 30.0F;
   }

   public BlockBreakManager(PhobosClient phobosClient) {
      this.blockPosMap = new ConcurrentHashMap<>(0);
      this.blockPosMap2 = new ConcurrentHashMap<>();
      this.handleListener(
         new SafeListener<Receive<BlockBreakingProgressS2CPacket>>() {
            public void handleObject(
               Receive<BlockBreakingProgressS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               BlockPos blockPos = ((BlockBreakingProgressS2CPacket)receive.getPacket()).getPos();
               if (clientWorld.getEntityById(((BlockBreakingProgressS2CPacket)receive.getPacket()).getEntityId()) instanceof PlayerEntity playerEntity) {
                  if (!BlockBreakManager.this.testBlockPos2(blockPos, clientWorld)) {
                     BlockBreakManager.this.handleBlockPos(blockPos, playerEntity, ((BlockBreakingProgressS2CPacket)receive.getPacket()).getProgress());
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<BlockUpdateS2CPacket>>() {
            public void handleObject(
               Receive<BlockUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (((BlockUpdateS2CPacket)receive.getPacket()).getState().isAir()) {
                  BlockPos blockPos = ((BlockUpdateS2CPacket)receive.getPacket()).getPos();
                  if (BlockBreakManager.this.blockPosMap3.containsKey(blockPos)) {
                     BlockBreakManager.this.handleBlockPos(blockPos, null, 0);
                  }

                  BlockBreakManager.this.blockPosMap2.put(blockPos, System.currentTimeMillis() + 5000L);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               BlockBreakManager.this.blockPosMap3.entrySet().removeIf(entry -> entry.getValue().check());
               BlockBreakManager.this.blockPosMap.entrySet().removeIf(entry -> entry.getValue() + 250L < System.currentTimeMillis());
               BlockBreakManager.this.blockPosMap2.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
            }
         }
      );
      this.handleListener(
         new SafeListener<BreakEvent>(Integer.MIN_VALUE) {
            public void handleObject(
               BreakEvent breakEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!breakEvent.isCancelled() && !breakEvent.isFlag2() && breakEvent.getBlockPos() != null) {
                  BlockBreakManager.this.blockPosMap.put(breakEvent.getBlockPos(), System.currentTimeMillis());
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(1200) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AntiCheatModule.getAntiCheatModule().getBlockRotation().getValue()
                  && BlockBreakManager.this.breakLock != null
                  && !RotationManager.isFlag()) {
                  int i = clientPlayerInteractionManager.getBlockBreakingProgress();
                  if (i > 5) {
                     BlockPos blockPos = BlockBreakManager.this.breakLock.getBlockPos();
                     Direction direction = BlockBreakManager.this.resolveDirection(clientPlayerEntity, clientWorld, blockPos);
                     float[] floats = RotationUtils.values4(clientPlayerEntity, clientWorld, blockPos, direction);
                     phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.blockPosMap3::clear);
   }

   public void handleToggleable2(Toggleable toggleable) {
      if (!SpeedmineModule.getSpeedmineModule().check7()) {
         if (this.breakLock != null) {
            if (Objects.equals(this.breakLock.getToggleable(), toggleable)) {
               this.breakLock.getNormal().run();
            }
         }
      }
   }

   public boolean testClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.breakLock == null) {
         return false;
      } else if (!SpeedmineModule.getSpeedmineModule().check7()
         && this.breakLock.getToggleable().check7()
         && !this.breakLock.testWorld(clientWorld)
         && !this.breakLock.getNormal().testLong(300L)) {
         this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, this.breakLock.getBlockPos());
         return true;
      } else {
         this.breakLock = null;
         return false;
      }
   }

   public void handleToggleable3(Toggleable toggleable, @NotNull BlockPos blockPos) {
      if (!SpeedmineModule.getSpeedmineModule().check7()) {
         if (this.breakLock == null) {
            this.breakLock = new BlockBreakManager.BreakLock(toggleable, blockPos);
         } else if (this.breakLock.getBlockPos().equals(blockPos) && Objects.equals(this.breakLock.getToggleable(), toggleable)) {
            this.breakLock.getNormal().run();
         } else {
            this.breakLock = new BlockBreakManager.BreakLock(toggleable, blockPos);
         }
      }
   }

   @Nullable
   public BlockBreakManager.BreakLock getBreakLock() {
      return this.breakLock;
   }

   public boolean testToggleable(Toggleable toggleable) {
      return this.breakLock != null && Objects.equals(this.breakLock.getToggleable(), toggleable);
   }

   private static class BreakLock {
      public final BlockPos blockPos;
      public final TimeGate.Normal normal = new TimeGate.Normal();
      public final Toggleable toggleable;

      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      public TimeGate.Normal getNormal() {
         return this.normal;
      }

      public BreakLock(Toggleable toggleable, BlockPos blockPos) {
         this.toggleable = toggleable;
         this.blockPos = blockPos;
         this.normal.run();
      }

      public boolean testWorld(World world) {
         return world.getBlockState(this.blockPos).isAir();
      }

      public Toggleable getToggleable() {
         return this.toggleable;
      }
   }

   public static class Progress {
      public boolean flag;
      public PlayerEntity playerEntity;
      public int intValue;
      public final FloatAnimator floatAnimator = new FloatAnimator();
      public final BlockPos blockPos;
      public final long longValue;

      public long getLongValue() {
         return this.longValue;
      }

      public boolean check() {
         return this.playerEntity == null
            || this.playerEntity.isRemoved()
            || TimeUtils.computeLong(this.longValue) > 5000L
            || !this.flag
               && this.playerEntity.squaredDistanceTo(this.blockPos.getX() + 0.5, this.blockPos.getY(), this.blockPos.getZ() + 0.5) >= MathHelper.square(7.0);
      }

      public PlayerEntity getPlayerEntity() {
         return this.playerEntity;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public BlockBreakManager.Progress resolveProgress(boolean flag) {
         this.flag = flag;
         return this;
      }

      public Progress(BlockPos blockPos, long l, int i, PlayerEntity playerEntity, boolean flag) {
         this.blockPos = blockPos;
         this.longValue = l;
         this.intValue = i;
         this.playerEntity = playerEntity;
         this.flag = flag;
      }

      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }

      public BlockBreakManager.Progress resolveProgress2(int i) {
         this.intValue = i;
         return this;
      }

      public BlockBreakManager.Progress resolveProgress3(PlayerEntity playerEntity) {
         this.playerEntity = playerEntity;
         return this;
      }
   }
}
