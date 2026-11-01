package org.phobos.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.module.player.BridgeModule;

public final class BridgeTickListener extends ModuleListener.Safe<BridgeModule, TickEvent> {
   public BridgeTickListener(BridgeModule bridgeModule) {
      super(bridgeModule, TickEvent.class);
   }

   public void handleObject(
      TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      boolean flag = !clientPlayerEntity.getAbilities().flying && this.module.testKeyboardInput(tickEvent.getKeyboardInput(), clientPlayerEntity);
      tickEvent.getKeyboardInput().sneaking = flag
         && this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, this.module.distance.getValue().floatValue());
   }

   public List<Box> resolveList(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d, Vec3d vec3d2) {
      EntityDimensions entityDimensions = clientPlayerEntity.getDimensions(EntityPose.STANDING);
      Box box5 = entityDimensions.getBoxAt(vec3d);
      Box box6 = entityDimensions.getBoxAt(vec3d2);
      Box box7 = box5.union(box6);
      BlockPos blockPos = BlockPos.ofFloored(box7.minX - 0.3 - 1.0E-7, box7.minY - 0.5 - 1.0E-7, box7.minZ - 0.3 - 1.0E-7);
      BlockPos blockPos2 = BlockPos.ofFloored(box7.maxX + 0.3 + 1.0E-7, box7.maxY + 1.0E-7, box7.maxZ + 0.3 + 1.0E-7);
      Vec3d vec3d3 = vec3d2.subtract(vec3d);
      Vec3d vec3d4 = vec3d.subtract(vec3d3.multiply(1000.0));
      Vec3d vec3d5 = vec3d2.add(vec3d3.multiply(1000.0));
      List<Box> list = new ArrayList<>();
      BlockState blockState = clientWorld.getBlockState(blockPos);
      VoxelShape voxelShape = blockState.getCollisionShape(clientWorld, blockPos);

      for (Box box2 : voxelShape.getBoundingBoxes()) {
         Box box = new Box(box2.minX - 0.3, box2.minY - 1.0, box2.minZ - 0.3, box2.maxX + 0.3, box2.maxY + 0.5 + 0.05, box2.maxZ + 0.3).offset(blockPos);
         if (!box.raycast(vec3d4, vec3d5).isEmpty()) {
            list.add(box);
         }
      }

      blockState = clientWorld.getBlockState(blockPos2);
      voxelShape = blockState.getCollisionShape(clientWorld, blockPos2);

      for (Box box4 : voxelShape.getBoundingBoxes()) {
         Box box3 = new Box(box4.minX - 0.3, box4.minY - 1.0, box4.minZ - 0.3, box4.maxX + 0.3, box4.maxY + 0.5 + 0.05, box4.maxZ + 0.3).offset(blockPos2);
         if (!box3.raycast(vec3d4, vec3d5).isEmpty()) {
            list.add(box3);
         }
      }

      return list;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d) {
      Box box = clientPlayerEntity.getDimensions(clientPlayerEntity.getPose())
         .getBoxAt(vec3d)
         .expand(-0.05, 0.0, -0.05)
         .offset(0.0, clientPlayerEntity.fallDistance - clientPlayerEntity.getStepHeight(), 0.0);
      return clientWorld.isSpaceEmpty(box);
   }

   @Nullable
   public Vec3d resolveVec3d(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d, Vec3d vec3d2) {
      List<Box> list2 = this.resolveList(clientPlayerEntity, clientWorld, vec3d, vec3d2);
      Vec3d vec3d4 = vec3d;
      Vec3d vec3d5 = vec3d2.subtract(vec3d);
      Vec3d vec3d6 = vec3d.subtract(vec3d5.multiply(1000.0));
      Vec3d vec3d7 = vec3d2.add(vec3d5.multiply(1000.0));

      while (true) {
         Vec3d vec3d3 = vec3d4;
         List<Box> list = list2.stream().filter(box -> box.contains(vec3d3)).toList();
         if (list.isEmpty()) {
            return vec3d4;
         }

         if (list.stream().anyMatch(box -> box.contains(vec3d2))) {
            return null;
         }

         vec3d4 = list.stream()
            .map(box -> box.raycast(vec3d7, vec3d6))
            .filter(Optional::isPresent)
            .min(Comparator.comparingDouble(optional -> optional.get().squaredDistanceTo(vec3d2)))
            .orElse(Optional.empty())
            .orElse(null);
         list2.removeAll(list);
      }
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, double d) {
      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
      movingFakePlayer.copyFrom(clientPlayerEntity);
      Vec3d vec3d = new Vec3d(clientPlayerEntity.sidewaysSpeed, clientPlayerEntity.upwardSpeed, clientPlayerEntity.forwardSpeed);
      movingFakePlayer.travel(vec3d);
      Vec3d vec3d2 = movingFakePlayer.getVelocity();
      Vec3d vec3d3 = vec3d2.multiply(1.0, 0.0, 1.0).normalize();
      Vec3d vec3d4 = clientPlayerEntity.getPos();
      Vec3d vec3d5 = vec3d4.add(0.0, -0.1, 0.0);
      Vec3d vec3d6 = vec3d5.add(vec3d3.multiply(d));
      if (this.resolveVec3d(clientPlayerEntity, clientWorld, vec3d5, vec3d6) != null) {
         return true;
      }

      Vec3d vec3d7 = movingFakePlayer.getPos().add(vec3d2.multiply(1.0, 0.0, 1.0));
      return this.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d4) || this.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d7);
   }
}
