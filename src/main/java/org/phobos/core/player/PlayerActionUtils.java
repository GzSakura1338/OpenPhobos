package org.phobos.core.player;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.MinecraftHolder;
import org.phobos.mixin.game.entity.IFireworkRocketEntity;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.PhobosClient;

public final class PlayerActionUtils implements MinecraftHolder {
   public static final Text text = Text.translatable("menu.savingLevel");
   public static final double doubleValue = 33.0;

   public static void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, double d, boolean flag) {
      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(playerEntity.getX(), d, playerEntity.getZ(), flag));
   }

   public static void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
   }

   public static boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isUsingItem();
   }

   public static void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity) {
      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      clientPlayerEntity.networkHandler
         .sendPacket(
            new Full(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getYaw(), playerEntity.getPitch(), playerEntity.isOnGround())
         );
   }

   public static void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3, float f, float f2, boolean flag) {
      clientPlayerEntity.networkHandler.sendPacket(new Full(d, d2, d3, f, f2, flag));
   }

   public static boolean testPhobosClient(PhobosClient phobosClient, ClientPlayerEntity clientPlayerEntity, Vec3d vec3d) {
      if (!clientPlayerEntity.isBlocking()) {
         return false;
      }

      Vec3d vec3d2 = clientPlayerEntity.getRotationVector(0.0F, phobosClient.getRotationManager().getFloatValue15());
      Vec3d vec3d3 = vec3d.relativize(clientPlayerEntity.getPos());
      vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z).normalize();
      return vec3d3.dotProduct(vec3d2) < 0.0;
   }

   public static boolean testClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isFallFlying() && !check3();
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity, Predicate<Vec3d> predicate) {
      Vec3d vec3d = playerEntity.getPos();
      Vec3d vec3d2 = new Vec3d(playerEntity.prevX, playerEntity.prevY, playerEntity.prevZ);
      return predicate.test(vec3d) ? true : predicate.test(vec3d2);
   }

   public static void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity, double d, boolean flag) {
      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(d, playerEntity.getY(), playerEntity.getZ(), flag));
   }

   public static void handleClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3, boolean flag) {
      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(d, d2, d3, flag));
   }

   public static boolean testDouble(double d, double... doubles) {
      if (mc.player == null) {
         return false;
      }

      double d2 = doubles.length == 1 ? doubles[0] : 0.0;
      return EntityUtils.computeLivingEntity(mc.player) - d2 <= d;
   }

   public static boolean testClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.getInventory().getArmorStack(2).getItem() instanceof ElytraItem;
   }

   public static void handleClientPlayerEntity7(ClientPlayerEntity clientPlayerEntity, Hand hand) {
      if (!clientPlayerEntity.handSwinging
         || clientPlayerEntity.handSwingTicks >= clientPlayerEntity.getHandSwingDuration() / 2
         || clientPlayerEntity.handSwingTicks < 0) {
         clientPlayerEntity.handSwingTicks = -1;
         clientPlayerEntity.handSwinging = true;
         clientPlayerEntity.preferredHand = hand;
      }
   }

   public static boolean check() {
      return ClientHolder.getPhobosClient().getElytraManager().check();
   }

   public static boolean testClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isFallFlying() && !check();
   }

   public static boolean check2() {
      return !(mc.currentScreen instanceof HandledScreen)
         ? true
         : mc.currentScreen instanceof InventoryScreen || mc.currentScreen instanceof CreativeInventoryScreen;
   }

   public static boolean testClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      Box box = BlockPosUtils.createBox(clientPlayerEntity, 3.0);

      for (Entity entity : clientWorld.getOtherEntities(null, box)) {
         if (entity instanceof IFireworkRocketEntity iFireworkRocketEntity && Objects.equals(clientPlayerEntity, iFireworkRocketEntity.getShooter())) {
            return true;
         }
      }

      return false;
   }

   public static void handleClientPlayerEntity8(ClientPlayerEntity clientPlayerEntity, PlayerEntity playerEntity) {
      clientPlayerEntity.networkHandler
         .sendPacket(
            new Full(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getYaw(), playerEntity.getPitch(), playerEntity.isOnGround())
         );
   }

   public static void handleClientPlayerEntity9(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3) {
      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(d, d2, d3, playerEntity.isOnGround()));
   }

   public static void handleClientPlayerEntity10(ClientPlayerEntity clientPlayerEntity, Hand hand, Supplier<ActionResult> supplier) {
      ActionResult actionResult = supplier.get();
      if (actionResult.isAccepted() && actionResult.shouldSwingHand()) {
         clientPlayerEntity.swingHand(hand);
      }
   }

   public static boolean testClientPlayerEntity7(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isUsingItem()
         || mc.options.useKey.isPressed()
            && (InventoryUtils.testItemStack5(clientPlayerEntity.getMainHandStack()) || InventoryUtils.testItemStack5(clientPlayerEntity.getOffHandStack()));
   }

   public static void run() {
      if (mc.player != null && mc.world != null) {
         boolean flag = mc.isInSingleplayer();
         ServerInfo serverInfo = mc.getCurrentServerEntry();
         mc.world.disconnect();
         if (flag) {
            mc.disconnect(new MessageScreen(text));
         } else {
            mc.disconnect();
         }

         TitleScreen titleScreen = new TitleScreen();
         if (flag) {
            mc.setScreen(titleScreen);
         } else if (serverInfo != null && serverInfo.isRealm()) {
            mc.setScreen(new RealmsMainScreen(titleScreen));
         } else {
            mc.setScreen(new MultiplayerScreen(titleScreen));
         }
      }
   }

   public static boolean check3() {
      return ClientHolder.getPhobosClient().getElytraManager().check2();
   }

   public static void handleClientPlayerEntity11(ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler
         .sendPacket(
            new Full(
               clientPlayerEntity.getX(),
               clientPlayerEntity.getY(),
               clientPlayerEntity.getZ(),
               clientPlayerEntity.getYaw(),
               clientPlayerEntity.getPitch(),
               clientPlayerEntity.isOnGround()
            )
         );
   }

   public static boolean testClientPlayerEntity8(ClientPlayerEntity clientPlayerEntity) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      double d = vec3d.length() * 20.0;
      float f = ClientHolder.getPhobosClient().getRotationManager().getFloatValue17();
      return d >= 33.0 ? false : d < 30.0 || f < 0.0F;
   }

   public static int value() {
      return SilentHotbarModule.getSilentHotbarManager().value();
   }

   public static void handleClientPlayerEntity12(ClientPlayerEntity clientPlayerEntity, double d, boolean flag, boolean flag2) {
      Vec3d vec3d = clientPlayerEntity.getPos();

      for (int i = 0; i < 10; i++) {
         clientPlayerEntity.networkHandler.sendPacket(new OnGroundOnly(false));
      }

      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(vec3d.getX(), vec3d.getY() + d, vec3d.getZ(), flag));
      if (flag2) {
         for (int i2 = 0; i2 < 2; i2++) {
            clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(vec3d.getX(), vec3d.getY(), vec3d.getZ(), flag));
         }
      }
   }

   public static void handleClientPlayerEntity13(ClientPlayerEntity clientPlayerEntity, double d, double d2) {
      handleClientPlayerEntity24(clientPlayerEntity, d);
      handleClientPlayerEntity26(clientPlayerEntity, d2);
   }

   public static void handleClientPlayerEntity14(ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      handleClientPlayerEntity26(clientPlayerEntity, vec3d.z + d);
   }

   public static void handleClientPlayerEntity15(ClientPlayerEntity clientPlayerEntity, Consumer<Packet<?>> consumer) {
      clientPlayerEntity.sendSprintingPacket();
      boolean flag3 = clientPlayerEntity.isSneaking();
      if (flag3 != clientPlayerEntity.lastSneaking) {
         Mode mode = flag3 ? Mode.PRESS_SHIFT_KEY : Mode.RELEASE_SHIFT_KEY;
         clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, mode));
         clientPlayerEntity.lastSneaking = flag3;
      }

      if (clientPlayerEntity.isCamera()) {
         double d = clientPlayerEntity.getX() - clientPlayerEntity.lastX;
         double d2 = clientPlayerEntity.getY() - clientPlayerEntity.lastBaseY;
         double d3 = clientPlayerEntity.getZ() - clientPlayerEntity.lastZ;
         double d4 = clientPlayerEntity.getYaw() - clientPlayerEntity.lastYaw;
         double d5 = clientPlayerEntity.getPitch() - clientPlayerEntity.lastPitch;
         clientPlayerEntity.ticksSinceLastPositionPacketSent++;
         boolean flag = MathHelper.squaredMagnitude(d, d2, d3) > MathHelper.square(2.0E-4) || clientPlayerEntity.ticksSinceLastPositionPacketSent >= 20;
         boolean flag2 = d4 != 0.0 || d5 != 0.0;
         if (clientPlayerEntity.hasVehicle()) {
            Vec3d vec3d = clientPlayerEntity.getVelocity();
            clientPlayerEntity.networkHandler
               .sendPacket(new Full(vec3d.x, -999.0, vec3d.z, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch(), clientPlayerEntity.isOnGround()));
            flag = false;
         } else if (flag && flag2) {
            clientPlayerEntity.networkHandler
               .sendPacket(
                  new Full(
                     clientPlayerEntity.getX(),
                     clientPlayerEntity.getY(),
                     clientPlayerEntity.getZ(),
                     clientPlayerEntity.getYaw(),
                     clientPlayerEntity.getPitch(),
                     clientPlayerEntity.isOnGround()
                  )
               );
         } else if (flag) {
            clientPlayerEntity.networkHandler
               .sendPacket(
                  new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), clientPlayerEntity.isOnGround())
               );
         } else if (flag2) {
            clientPlayerEntity.networkHandler
               .sendPacket(new LookAndOnGround(clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch(), clientPlayerEntity.isOnGround()));
         } else if (clientPlayerEntity.lastOnGround != clientPlayerEntity.isOnGround()) {
            clientPlayerEntity.networkHandler.sendPacket(new OnGroundOnly(clientPlayerEntity.isOnGround()));
         }

         if (flag) {
            clientPlayerEntity.lastX = clientPlayerEntity.getX();
            clientPlayerEntity.lastBaseY = clientPlayerEntity.getY();
            clientPlayerEntity.lastZ = clientPlayerEntity.getZ();
            clientPlayerEntity.ticksSinceLastPositionPacketSent = 0;
         }

         if (flag2) {
            clientPlayerEntity.lastYaw = clientPlayerEntity.getYaw();
            clientPlayerEntity.lastPitch = clientPlayerEntity.getPitch();
         }

         clientPlayerEntity.lastOnGround = clientPlayerEntity.isOnGround();
         clientPlayerEntity.autoJumpEnabled = (Boolean)clientPlayerEntity.client.options.getAutoJump().getValue();
      }
   }

   public static void handleClientPlayerEntity16(ClientPlayerEntity clientPlayerEntity, int i, boolean flag) {
      if (clientPlayerEntity.getVehicle() != null) {
         for (int i2 = 0; i2 < i + (flag ? -1 : 0); i2++) {
            clientPlayerEntity.networkHandler.sendPacket(new VehicleMoveC2SPacket(clientPlayerEntity.getVehicle()));
         }
      } else {
         for (int i3 = 0; i3 < i + (flag ? -1 : 0); i3++) {
            clientPlayerEntity.networkHandler.sendPacket(new OnGroundOnly(false));
         }
      }
   }

   public static void handleClientPlayerEntity17(ClientPlayerEntity clientPlayerEntity, float f, float f2, boolean flag) {
      clientPlayerEntity.networkHandler.sendPacket(new LookAndOnGround(f, f2, flag));
   }

   public static void handleClientPlayerEntity18(ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      handleClientPlayerEntity24(clientPlayerEntity, vec3d.x + d);
   }

   public static boolean testBlockPos(BlockPos blockPos, ClientWorld clientWorld) {
      BlockState blockState;
      return (blockState = clientWorld.getBlockState(blockPos)).isReplaceable()
         && clientWorld.getFluidState(blockPos).isEmpty()
         && !blockState.isOf(Blocks.POWDER_SNOW);
   }

   public static void handleClientPlayerEntity19(ClientPlayerEntity clientPlayerEntity, double d, double d2) {
      handleClientPlayerEntity24(clientPlayerEntity, d);
      handleClientPlayerEntity29(clientPlayerEntity, d2);
   }

   public static void handleClientPlayerEntity20(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, int i) {
      int i4 = computeClientPlayerEntity(clientPlayerEntity, clientWorld, i);
      int i5 = (int)Math.abs(i4 / 10);
      i5 = i5 > 20 ? 1 : i5;
      BlockPos blockPos = BlockPos.ofFloored(clientPlayerEntity.getPos());
      BlockPos blockPos2 = blockPos.add(0, i4, 0);
      BlockPos blockPos3 = blockPos.add(0, i4 + 1, 0);
      Vec3d vec3d = clientPlayerEntity.getPos();
      if (testBlockPos2(blockPos2, blockPos3, clientWorld)) {
         if (clientPlayerEntity.getVehicle() != null) {
            for (int i2 = 0; i2 < i5 - 1; i2++) {
               clientPlayerEntity.networkHandler.sendPacket(new VehicleMoveC2SPacket(clientPlayerEntity.getVehicle()));
            }
         } else {
            for (int i3 = 0; i3 < i5 - 1; i3++) {
               clientPlayerEntity.networkHandler.sendPacket(new OnGroundOnly(false));
            }
         }
      }
   }

   public static void handleClientPlayerEntity21(ClientPlayerEntity clientPlayerEntity, double d, boolean flag) {
      PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(playerEntity.getX(), playerEntity.getY(), d, flag));
   }

   public static void handleClientPlayerEntity22(ClientPlayerEntity clientPlayerEntity, double d, double d2) {
      handleClientPlayerEntity26(clientPlayerEntity, d);
      handleClientPlayerEntity29(clientPlayerEntity, d2);
   }

   public static void handleClientPlayerEntity23(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, int i) {
      int i4 = computeClientPlayerEntity(clientPlayerEntity, clientWorld, i);
      BlockPos blockPos = BlockPos.ofFloored(clientPlayerEntity.getPos());
      BlockPos blockPos2 = blockPos.add(0, i4, 0);
      BlockPos blockPos3 = blockPos.add(0, i4 + 1, 0);
      Vec3d vec3d = clientPlayerEntity.getPos();
      if (testBlockPos2(blockPos2, blockPos3, clientWorld)) {
         if (clientPlayerEntity.getVehicle() != null) {
            double d = clientPlayerEntity.getVehicle().getX();
            double d2 = clientPlayerEntity.getVehicle().getY();
            double d3 = clientPlayerEntity.getVehicle().getZ();
            clientPlayerEntity.getVehicle().setPosition(d, d2 + i4, d3);
            clientPlayerEntity.networkHandler.sendPacket(new VehicleMoveC2SPacket(clientPlayerEntity.getVehicle()));

            for (int i2 = 0; i2 < 2; i2++) {
               clientPlayerEntity.getVehicle().setPosition(vec3d);
               clientPlayerEntity.networkHandler.sendPacket(new VehicleMoveC2SPacket(clientPlayerEntity.getVehicle()));
            }
         } else {
            clientPlayerEntity.networkHandler
               .sendPacket(new PositionAndOnGround(clientPlayerEntity.getX(), clientPlayerEntity.getY() + i4, clientPlayerEntity.getZ(), false));

            for (int i3 = 0; i3 < 2; i3++) {
               clientPlayerEntity.networkHandler.sendPacket(new PositionAndOnGround(vec3d.x, vec3d.y, vec3d.z, false));
            }
         }
      }
   }

   public static boolean testBlockPos2(BlockPos blockPos, BlockPos blockPos2, ClientWorld clientWorld) {
      return testBlockPos(blockPos, clientWorld) && testBlockPos(blockPos2, clientWorld);
   }

   public static void handleClientPlayerEntity24(ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      clientPlayerEntity.setVelocity(d, vec3d.getY(), vec3d.getZ());
   }

   public static boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.isUsingItem() && UseAction.BLOCK.equals(clientPlayerEntity.getActiveItem().getUseAction());
   }

   public static void handleClientPlayerEntity25(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      clientPlayerEntity.setVelocity(vec3d.multiply(d, d2, d3));
   }

   public static void handleClientPlayerEntity26(ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      clientPlayerEntity.setVelocity(vec3d.getX(), vec3d.getY(), d);
   }

   public static void handleClientPlayerEntity27(ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      handleClientPlayerEntity29(clientPlayerEntity, vec3d.y + d);
   }

   public static PlayerEntity resolvePlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      PlayerEntity playerEntity = new PlayerEntity(
         clientPlayerEntity.clientWorld, clientPlayerEntity.getBlockPos(), clientPlayerEntity.getYaw(), clientPlayerEntity.getGameProfile()
      ) {
         public boolean isCreative() {
            return false;
         }

         public boolean isSpectator() {
            return false;
         }
      };
      playerEntity.copyFrom(clientPlayerEntity);
      return playerEntity;
   }

   public static int computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, int i) {
      BlockPos blockPos2 = BlockPos.ofFloored(clientPlayerEntity.getPos());
      int i3 = blockPos2.getY() + i;

      for (int i2 = i3; i2 > blockPos2.getY(); i2--) {
         BlockPos blockPos;
         if (testBlockPos2(blockPos = new BlockPos(blockPos2.getX(), i2, blockPos2.getZ()), blockPos.up(), clientWorld)) {
            return i2 - blockPos2.getY();
         }
      }

      return 0;
   }

   public static void handleClientPlayerEntity28(ClientPlayerEntity clientPlayerEntity, Vec3d vec3d) {
      clientPlayerEntity.setVelocity(vec3d);
   }

   public static void handleClientPlayerEntity29(ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      clientPlayerEntity.setVelocity(vec3d.getX(), d, vec3d.getZ());
   }

   public PlayerActionUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static class PackedRangeCalculator {
      public final PlayerActionUtils.RangeCalculator[] rangeCalculatorArray;
      public final PlayerActionUtils.RangeCalculator[] rangeCalculatorArray2;

      public PackedRangeCalculator(PlayerActionUtils.RangeCalculator[] rangeCalculators, PlayerActionUtils.RangeCalculator[] rangeCalculators2) {
         this.rangeCalculatorArray = rangeCalculators;
         this.rangeCalculatorArray2 = rangeCalculators2;
      }

      public PlayerActionUtils.RangeCalculator[] getRangeCalculatorArray() {
         return this.rangeCalculatorArray;
      }

      public boolean testFunction(Function<PlayerActionUtils.RangeCalculator, Boolean> function) {
         for (PlayerActionUtils.RangeCalculator rangeCalculator : this.rangeCalculatorArray) {
            if (function.apply(rangeCalculator)) {
               return true;
            }
         }

         return false;
      }

      public PlayerActionUtils.RangeCalculator[] getRangeCalculatorArray2() {
         return this.rangeCalculatorArray2;
      }

      public boolean testFunction2(Function<PlayerActionUtils.RangeCalculator, Boolean> function) {
         for (PlayerActionUtils.RangeCalculator rangeCalculator : this.rangeCalculatorArray2) {
            if (function.apply(rangeCalculator)) {
               return true;
            }
         }

         return false;
      }
   }

   public enum RangeCalculator {
      Standing {
         @Override
         public double calculate(ClientPlayerEntity player, Box box) {
            return box.squaredMagnitude(player.getEyePos());
         }

         @Override
         public double calculate(ClientPlayerEntity player, double x, double y, double z) {
            return player.getEyePos().squaredDistanceTo(x, y, z);
         }
      },
      Base {
         @Override
         public double calculate(ClientPlayerEntity player, Box box) {
            return box.squaredMagnitude(player.getPos());
         }

         @Override
         public double calculate(ClientPlayerEntity player, double x, double y, double z) {
            return player.getPos().squaredDistanceTo(x, y, z);
         }
      },
      Sneaking {
         @Override
         public double calculate(ClientPlayerEntity player, double x, double y, double z) {
            return player.getPos().add(0.0, player.getEyeHeight(EntityPose.CROUCHING), 0.0).squaredDistanceTo(x, y, z);
         }

         @Override
         public double calculate(ClientPlayerEntity player, Box box) {
            return box.squaredMagnitude(player.getPos().add(0.0, player.getEyeHeight(EntityPose.CROUCHING), 0.0));
         }
      };

      public boolean calculate(ClientPlayerEntity player, double x, double y, double z, double range) {
         return this.calculate(player, x, y, z) <= MathHelper.square(range);
      }

      public double calculate(ClientPlayerEntity player, Vec3d vector) {
         return this.calculate(player, vector.getX(), vector.getY(), vector.getZ());
      }

      public boolean calculate(ClientPlayerEntity player, Vec3d vector, double range) {
         return this.calculate(player, vector) <= MathHelper.square(range);
      }

      public boolean calculate(ClientPlayerEntity player, Box box, double range) {
         return this.calculate(player, box) <= MathHelper.square(range);
      }

      public abstract double calculate(ClientPlayerEntity var1, Box var2);

      public abstract double calculate(ClientPlayerEntity var1, double var2, double var4, double var6);
   }
}
