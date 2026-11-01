package org.phobos.core.block;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.registry.Registries;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.combat.AutoCrystalModule;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;

public final class BlockUtils implements MinecraftHolder {
   public static final Set<Block> blockSet = Set.of(Blocks.BEDROCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.END_PORTAL_FRAME, Blocks.BARRIER);
   public static final List<Block> blockList = new ArrayList<>(
      List.of(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.CRYING_OBSIDIAN, Blocks.NETHERITE_BLOCK, Blocks.ENDER_CHEST)
   );
   public static final List<Block> blockList2 = new ArrayList<>();
   public static final Vec3i[] vec3iArray = new Vec3i[]{new Vec3i(1, 0, 1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(-1, 0, -1)};

   public static boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Block block) {
      Box box = clientPlayerEntity.getBoundingBox();
      VoxelShape voxelShape = VoxelShapes.cuboid(BoxUtils.createBox4(box));
      BlockState blockState = block.getDefaultState();
      VoxelShape voxelShape2 = blockState.getOutlineShape(clientWorld, blockPos);
      voxelShape2 = voxelShape2.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
      return VoxelShapes.matchesAnywhere(voxelShape, voxelShape2, BooleanBiFunction.AND);
   }

   public static boolean testBlockPos(BlockPos blockPos, ClientWorld clientWorld, boolean flag) {
      return testBlockPos2(blockPos, clientWorld, flag, false);
   }

   public static boolean testClientWorld(ClientWorld clientWorld, PlayerEntity playerEntity, BiPredicate<BlockPos, BlockState> biPredicate) {
      Box box = playerEntity.getBoundingBox();
      BlockPos blockPos2 = BlockPos.ofFloored(playerEntity.getPos());

      for (int i3 = blockPos2.getX() - 2; i3 <= blockPos2.getX() + 2; i3++) {
         for (int i2 = blockPos2.getY() - 1; i2 <= blockPos2.getY() + 4; i2++) {
            for (int i = blockPos2.getZ() - 2; i <= blockPos2.getZ() + 2; i++) {
               BlockPos blockPos = new BlockPos(i3, i2, i);
               BlockState blockState = clientWorld.getBlockState(blockPos);
               if (biPredicate.test(blockPos, blockState) && box.intersects(new Box(blockPos))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean testBlockState(BlockState blockState) {
      return !blockState.isAir() && !blockState.isReplaceable();
   }

   @Nullable
   public static BlockPos resolveBlockPos(BlockPos blockPos, BlockPos blockPos2, ClientPlayerEntity clientPlayerEntity) {
      List<BlockPos> list = new ArrayList<>();

      for (Direction direction2 : DirectionUtils.directionArray) {
         BlockPos blockPos4 = blockPos.offset(direction2);

         for (Direction direction : DirectionUtils.directionArray) {
            BlockPos blockPos3 = blockPos2.offset(direction);
            if (blockPos4.equals(blockPos3)) {
               list.add(blockPos4);
            }
         }
      }

      return !list.isEmpty()
         ? list.stream().max(Comparator.comparingDouble(blockPosx -> blockPosx.getSquaredDistance(clientPlayerEntity.getPos()))).orElse(null)
         : null;
   }

   public static boolean testBlockPos2(BlockPos blockPos, ClientWorld clientWorld, boolean flag, boolean flag2) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      Block block = blockState.getBlock();
      BlockState blockState2 = clientWorld.getBlockState(blockPos.up());
      return (flag2 && blockState.isReplaceable() || block.equals(Blocks.BEDROCK) || block.equals(Blocks.OBSIDIAN))
         && blockState2.isReplaceable()
         && !testBlockPos11(
            blockPos.up(), clientWorld.getBlockState(blockPos).getCollisionShape(clientWorld, blockPos, ShapeContext.absent()), clientWorld, !flag
         );
   }

   @Nullable
   public static Direction resolveDirection(BlockPos blockPos, BlockPos blockPos2) {
      for (Direction direction : Direction.values()) {
         if (blockPos.offset(direction).equals(blockPos2) || blockPos2.offset(direction).equals(blockPos)) {
            return direction;
         }
      }

      return null;
   }

   public static boolean testItem(Item item) {
      if (item instanceof BlockItem blockItem && mc.world != null) {
         Block block = blockItem.getBlock();
         return block instanceof FallingBlock ? false : testClientWorld2(mc.world, block);
      } else {
         return false;
      }
   }

   public static void run5() {
      for (Block block : Registries.BLOCK) {
         blockList2.add(block);
      }
   }

   @NotNull
   public static BlockPos resolveBlockPos2(Vec3d vec3d, Vec3d vec3d2) {
      Vec3d vec3d3 = new Vec3d(
         vec3d.getX() + (vec3d2.getX() - vec3d.getX()) / 2.0,
         vec3d.getY() + (vec3d2.getY() - vec3d.getY()) / 2.0,
         vec3d.getZ() + (vec3d2.getZ() - vec3d.getZ()) / 2.0
      );
      return BlockPos.ofFloored(vec3d3);
   }

   public static boolean testBox(Box box, World world, BlockPos... blockPoss) {
      boolean flag = testWorld2(world, box);
      Set<BlockPos> set = BlockPosUtils.resolveSet2(box, box.minY + 0.25);
      Set<BlockPos> set2 = BlockPosUtils.resolveSet2(box, box.minY + 1.25);
      boolean flag2 = !set.stream()
            .filter(blockPos -> testBlockPos3(blockPos, blockPoss) && !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty())
            .toList()
            .isEmpty()
         || !set2.stream()
            .filter(blockPos -> testBlockPos3(blockPos, blockPoss) && !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty())
            .toList()
            .isEmpty();
      return flag2 && flag;
   }

   public static boolean testBlockState2(BlockState blockState) {
      return !blockState.isOf(Blocks.OBSIDIAN)
            && !blockState.isOf(Blocks.CRYING_OBSIDIAN)
            && !blockState.isOf(Blocks.RESPAWN_ANCHOR)
            && !blockState.isOf(Blocks.REINFORCED_DEEPSLATE)
         ? blockState.getPistonBehavior() != PistonBehavior.BLOCK
         : false;
   }

   public static boolean testBlockPos3(BlockPos blockPos, BlockPos[] blockPoss) {
      return blockPoss.length == 0 ? true : List.of(blockPoss).contains(blockPos);
   }

   public static VoxelShape resolveVoxelShape(ClientWorld clientWorld, BlockPos blockPos) {
      return Blocks.STONE.getDefaultState().getCollisionShape(clientWorld, blockPos);
   }

   public static boolean testClientWorld2(ClientWorld clientWorld, Block block) {
      VoxelShape voxelShape;
      return !(voxelShape = block.getDefaultState().getCollisionShape(clientWorld, BlockPos.ORIGIN)).isEmpty()
         && Blocks.STONE.getDefaultState().getCollisionShape(clientWorld, BlockPos.ORIGIN).equals(voxelShape);
   }

   public static int computeDouble(double d) {
      int i = (int)d;
      return d < i ? i - 1 : i;
   }

   public static boolean testWorld(World world, PlayerEntity playerEntity) {
      if (playerEntity == null) {
         return false;
      }

      Box box = playerEntity.getBoundingBox();

      for (int i3 = computeDouble(box.minX); i3 < computeDouble(box.maxX) + 1; i3++) {
         for (int i2 = computeDouble(box.minY); i2 < computeDouble(box.maxY) + 1; i2++) {
            for (int i = computeDouble(box.minZ); i < computeDouble(box.maxZ) + 1; i++) {
               BlockPos blockPos = new BlockPos(i3, i2, i);
               BlockState blockState = world.getBlockState(blockPos);
               VoxelShape voxelShape2;
               if (!(voxelShape2 = blockState.getOutlineShape(world, blockPos)).isEmpty() && blockState.blocksMovement()) {
                  VoxelShape voxelShape = voxelShape2.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                  if (VoxelShapes.matchesAnywhere(voxelShape, VoxelShapes.cuboid(box), BooleanBiFunction.AND)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean testBlockPos4(BlockPos blockPos, ClientPlayerEntity clientPlayerEntity) {
      return blockList.contains(clientPlayerEntity.clientWorld.getBlockState(blockPos).getBlock());
   }

   public static void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, BlockPos blockPos, Direction direction) {
      TickUtils.handleConsumer2(
         integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockPos, direction, integer))
      );
      TickUtils.handleConsumer2(
         integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPos, direction, integer))
      );
   }

   public static boolean testBlockPos5(BlockPos blockPos, ClientWorld clientWorld) {
      return testBlockPos(blockPos, clientWorld, true);
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity, World world) {
      boolean flag = testWorld(world, playerEntity);
      Set<BlockPos> set = BlockPosUtils.resolveSet3(playerEntity, playerEntity.getBoundingBox().minY + 0.25);
      Set<BlockPos> set2 = BlockPosUtils.resolveSet3(playerEntity, playerEntity.getBoundingBox().minY + 1.25);
      boolean flag2 = !set.stream().filter(blockPos -> !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()).toList().isEmpty()
         || !set2.stream().filter(blockPos -> !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()).toList().isEmpty();
      return flag2 && flag;
   }

   public static boolean testBlockPos6(BlockPos blockPos, VoxelShape voxelShape, ClientWorld clientWorld, boolean flag) {
      if (voxelShape.isEmpty()) {
         return false;
      }

      VoxelShape voxelShape2 = voxelShape.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());

      for (Entity entity : clientWorld.getOtherEntities(null, voxelShape2.getBoundingBox())) {
         if (entity.intersectionChecked
            && !entity.isRemoved()
            && VoxelShapes.matchesAnywhere(voxelShape2, VoxelShapes.cuboid(BoxUtils.createBox4(EntityUtils.resolveBox(entity))), BooleanBiFunction.AND)) {
            if (flag && entity instanceof EndCrystalEntity endCrystalEntity) {
               if (AntiCheatModule.check3()) {
                  AutoCrystalModule.handleEndCrystalEntity(endCrystalEntity, false);
               }

               return true;
            }

            return true;
         }
      }

      return false;
   }

   public static boolean testWorld2(World world, Box box) {
      for (int i3 = computeDouble(box.minX); i3 < computeDouble(box.maxX) + 1; i3++) {
         for (int i2 = computeDouble(box.minY); i2 < computeDouble(box.maxY) + 1; i2++) {
            for (int i = computeDouble(box.minZ); i < computeDouble(box.maxZ) + 1; i++) {
               BlockPos blockPos = new BlockPos(i3, i2, i);
               BlockState blockState = world.getBlockState(blockPos);
               VoxelShape voxelShape2;
               if (!(voxelShape2 = blockState.getOutlineShape(world, blockPos)).isEmpty() && blockState.blocksMovement()) {
                  VoxelShape voxelShape = voxelShape2.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                  if (VoxelShapes.matchesAnywhere(voxelShape, VoxelShapes.cuboid(box), BooleanBiFunction.AND)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean testBlockPos7(BlockPos blockPos, World world) {
      if (world == null) {
         return true;
      }

      BlockState blockState = world.getBlockState(blockPos);
      return !blockState.isAir() && !blockState.isReplaceable();
   }

   public static boolean testBlockPos8(
      BlockPos blockPos,
      VoxelShape voxelShape,
      PlayerEntity playerEntity,
      ClientWorld clientWorld,
      Predicate<Entity> predicate,
      Consumer<EndCrystalEntity> consumer,
      double d
   ) {
      if (voxelShape.isEmpty()) {
         return false;
      }

      VoxelShape voxelShape2 = voxelShape.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
      boolean flag = false;

      for (Entity entity : clientWorld.getOtherEntities(null, voxelShape2.getBoundingBox())) {
         if (entity.intersectionChecked
            && !entity.isRemoved()
            && VoxelShapes.matchesAnywhere(voxelShape2, VoxelShapes.cuboid(BoxUtils.createBox4(EntityUtils.resolveBox(entity))), BooleanBiFunction.AND)
            && !predicate.test(entity)) {
            if (!(entity instanceof EndCrystalEntity)) {
               return true;
            }

            flag = true;
         }
      }

      if (flag) {
         float f2 = Float.MAX_VALUE;
         EndCrystalEntity endCrystalEntity2 = null;

         for (EndCrystalEntity endCrystalEntity : EntityUtils.resolveIterable(playerEntity, clientWorld, d)) {
            float f = ClientHolder.getPhobosClient().getDamageCalculator().computeEntity8(playerEntity, clientWorld, endCrystalEntity);
            if (testPlayerEntity2(playerEntity, f) && f < f2) {
               f2 = f;
               endCrystalEntity2 = endCrystalEntity;
            }
         }

         if (endCrystalEntity2 == null) {
            return true;
         }

         consumer.accept(endCrystalEntity2);
      }

      return false;
   }

   public static String describeText(String text) {
      List<String> list = new ArrayList<>();

      for (Block block : blockList2) {
         String text2 = block.getName().getString();
         if (text2.toLowerCase().startsWith(text.toLowerCase())) {
            list.add(text2);
         }
      }

      if (list.isEmpty()) {
         return null;
      } else {
         return list.size() == 1 ? list.getFirst() : ChatTextUtils.describeList(list);
      }
   }

   public static boolean testBlockPos9(BlockPos blockPos, World world) {
      return world.getBlockState(blockPos).getBlock().getBlastResistance() < 35.0F;
   }

   public static boolean testClientWorld3(ClientWorld clientWorld, BlockPos blockPos) {
      return blockSet.contains(clientWorld.getBlockState(blockPos).getBlock());
   }

   public static long computeObject(Object object, BlockPos blockPos) {
      return Objects.hash(object, blockPos);
   }

   public static BlockPos resolveBlockPos3(World world, BlockPos blockPos) {
      return world.getBlockState(blockPos).isReplaceable() ? blockPos.down() : blockPos;
   }

   public static Block resolveBlock(String text) {
      for (Block block : blockList2) {
         if (block.getName().getString().equalsIgnoreCase(text)) {
            return block;
         }
      }

      return null;
   }

   public static boolean testBlockPos10(BlockPos blockPos, BlockPos blockPos2) {
      for (Vec3i vec3i : vec3iArray) {
         if (blockPos.add(vec3i).equals(blockPos2)) {
            return true;
         }
      }

      return false;
   }

   public static List<Block> getBlockList2() {
      return blockList2;
   }

   public static void handleText(String text, Consumer<Block> consumer) {
      for (Block block : blockList2) {
         if (block.getName().getString().equalsIgnoreCase(text)) {
            consumer.accept(block);
            return;
         }
      }
   }

   public static Direction resolveDirection2(BlockPos blockPos, BlockPos blockPos2) {
      if (blockPos.getY() > blockPos2.getY()) {
         return Direction.UP;
      } else if (blockPos.getY() < blockPos2.getY()) {
         return Direction.DOWN;
      } else if (blockPos.getX() > blockPos2.getX()) {
         return Direction.EAST;
      } else if (blockPos.getX() < blockPos2.getX()) {
         return Direction.WEST;
      } else {
         return blockPos.getZ() > blockPos2.getZ() ? Direction.SOUTH : Direction.NORTH;
      }
   }

   public static boolean testBlockPos11(BlockPos blockPos, VoxelShape voxelShape, World world, boolean flag) {
      if (voxelShape.isEmpty()) {
         return false;
      }

      VoxelShape voxelShape2 = voxelShape.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());

      for (Entity entity : world.getOtherEntities(null, voxelShape2.getBoundingBox())) {
         if (entity.intersectionChecked
            && !entity.isRemoved()
            && VoxelShapes.matchesAnywhere(voxelShape2, VoxelShapes.cuboid(BoxUtils.createBox4(EntityUtils.resolveBox(entity))), BooleanBiFunction.AND)
            && (!flag || !(entity instanceof EndCrystalEntity))) {
            return true;
         }
      }

      return false;
   }

   public static boolean testBlockPos12(BlockPos blockPos, BlockPos blockPos2) {
      for (Direction direction : Direction.values()) {
         if (blockPos.offset(direction).equals(blockPos2)) {
            return true;
         }
      }

      return false;
   }

   public BlockUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testPlayerEntity2(PlayerEntity playerEntity, float f) {
      return f < EntityUtils.computeLivingEntity(playerEntity);
   }
}
