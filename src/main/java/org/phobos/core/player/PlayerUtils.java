package org.phobos.core.player;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.earthhack.ducks.IClientPlayerEntity;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.MoveEvent2;
import org.phobos.module.render.FreeLookModule;
import org.phobos.module.render.FreecamModule;
import org.phobos.module.movement.TargetStrafeModule;
import org.phobos.core.util.CastUtils;
import org.phobos.core.ClientHolder;

public final class PlayerUtils implements MinecraftHolder {
   public static boolean flag;
   public static boolean flag2;
   public static boolean flag3;
   public static boolean flag4;
   public static boolean flag5;

   public static boolean testLivingEntity(LivingEntity livingEntity) {
      return livingEntity.getVelocity().lengthSquared() > 0.03;
   }

   public static int computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return CastUtils.resolveObject(IClientPlayerEntity.class, clientPlayerEntity).getGroundTicks();
   }

   public static float value() {
      boolean flag = mc.options.forwardKey.isPressed();
      boolean flag2 = mc.options.backKey.isPressed();
      if ((!flag || !flag2) && (flag || flag2)) {
         return flag ? 1.0F : -1.0F;
      } else {
         return 0.0F;
      }
   }

   public static double[] values(double d, float f) {
      float f2 = 1.0F;
      float f3 = 0.0F;
      double d2 = Math.sin(Math.toRadians(f + 90.0F));
      double d3 = Math.cos(Math.toRadians(f + 90.0F));
      double d4 = f2 * d * d3 + f3 * d * d2;
      double d5 = f2 * d * d2 - f3 * d * d3;
      return new double[]{d4, d5};
   }

   public static int computeFlag(boolean flag, boolean flag2) {
      if ((!flag || !flag2) && (flag || flag2)) {
         return flag ? 1 : -1;
      } else {
         return 0;
      }
   }

   public static boolean check() {
      GameOptions gameOptions = mc.options;
      return (gameOptions.backKey.isPressed() || gameOptions.forwardKey.isPressed() || gameOptions.rightKey.isPressed() || gameOptions.leftKey.isPressed())
         && !FreecamModule.getFreecamModule().check7();
   }

   public static boolean check2() {
      return mc.player == null ? false : testClientPlayerEntity(mc.player);
   }

   public static boolean check3() {
      return mc.options.sneakKey.isPressed();
   }

   public static double[] values2(ClientPlayerEntity clientPlayerEntity, double d) {
      return values4(clientPlayerEntity, d, TargetStrafeModule.computeFloat(FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity)));
   }

   public static double computeClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      return Math.hypot(clientPlayerEntity.getVelocity().getX(), clientPlayerEntity.getVelocity().getZ());
   }

   public static float[] values3(ClientPlayerEntity clientPlayerEntity, float f) {
      float f2 = clientPlayerEntity.input.movementForward;
      float f3 = clientPlayerEntity.input.movementSideways;
      float f4 = TargetStrafeModule.computeFloat(
         clientPlayerEntity.prevYaw + (clientPlayerEntity.getYaw() - clientPlayerEntity.prevYaw) * mc.getRenderTickCounter().getTickDelta(false)
      );
      if (f2 == 0.0F && f3 == 0.0F) {
         return new float[]{0.0F, 0.0F};
      }

      if (f2 != 0.0F) {
         if (f3 >= 1.0F) {
            f4 += f2 > 0.0F ? -45.0F : 45.0F;
            f3 = 0.0F;
         } else if (f3 <= -1.0F) {
            f4 += f2 > 0.0F ? 45.0F : -45.0F;
            f3 = 0.0F;
         }

         if (f2 > 0.0F) {
            f2 = 1.0F;
         } else if (f2 < 0.0F) {
            f2 = -1.0F;
         }
      }

      float f5 = (float)Math.cos(Math.toRadians(f4));
      float f6 = (float)(-Math.sin(Math.toRadians(f4)));
      return new float[]{f2 * f * f6 + f3 * f * f5, f2 * f * f5 - f3 * f * f6};
   }

   public static float value2() {
      boolean flag = mc.options.leftKey.isPressed();
      boolean flag2 = mc.options.rightKey.isPressed();
      if ((!flag || !flag2) && (flag || flag2)) {
         return flag ? 1.0F : -1.0F;
      } else {
         return 0.0F;
      }
   }

   public static float computeFlag2(boolean flag) {
      boolean flag2 = mc.options.jumpKey.isPressed();
      boolean flag3 = mc.options.sneakKey.isPressed();
      if (!flag) {
         return (!flag2 || !flag3) && (flag2 || flag3) ? (flag2 ? -90.0F : 90.0F) : 0.0F;
      }

      boolean flag4 = check();
      return (!flag2 || !flag3) && (flag2 || flag3) ? (flag2 ? (flag4 ? -45.0F : -90.0F) : (flag4 ? 45.0F : 90.0F)) : 0.0F;
   }

   public static int computeClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity) {
      return CastUtils.resolveObject(IClientPlayerEntity.class, clientPlayerEntity).getIntValue();
   }

   public static double[] values4(ClientPlayerEntity clientPlayerEntity, double d, float f) {
      float f2 = clientPlayerEntity.input.movementForward;
      float f3 = clientPlayerEntity.input.movementSideways;
      if (f2 != 0.0F) {
         if (f3 > 0.0F) {
            f += f2 > 0.0F ? -45 : 45;
         } else if (f3 < 0.0F) {
            f += f2 > 0.0F ? 45 : -45;
         }

         f3 = 0.0F;
         if (f2 > 0.0F) {
            f2 = 1.0F;
         } else if (f2 < 0.0F) {
            f2 = -1.0F;
         }
      }

      double d2 = Math.sin(Math.toRadians(f + 90.0F));
      double d3 = Math.cos(Math.toRadians(f + 90.0F));
      double d4 = f2 * d * d3 + f3 * d * d2;
      double d5 = f2 * d * d2 - f3 * d * d3;
      return new double[]{d4, d5};
   }

   public PlayerUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return clientPlayerEntity.input.movementForward != 0.0F || clientPlayerEntity.input.movementSideways != 0.0F;
   }

   public static double[] values5(double d) {
      if (mc.player == null) {
         return new double[]{0.0, 0.0};
      }

      float f = mc.player.getYaw();
      return values6(d, f);
   }

   public static Vec3d createVec3d(ClientPlayerEntity clientPlayerEntity) {
      double d = value();
      double d2 = value2();
      float f = TargetStrafeModule.computeFloat(
         clientPlayerEntity.prevYaw + (clientPlayerEntity.getYaw() - clientPlayerEntity.prevYaw) * mc.getRenderTickCounter().getTickDelta(false)
      );
      if (d == 0.0 && d2 == 0.0) {
         return new Vec3d(0.0, clientPlayerEntity.getVelocity().y, 0.0);
      }

      if (d != 0.0) {
         if (d2 > 0.0) {
            f += d > 0.0 ? -45 : 45;
         } else if (d2 < 0.0) {
            f += d > 0.0 ? 45 : -45;
         }

         d2 = 0.0;
         if (d > 0.0) {
            d = 1.0;
         } else if (d < 0.0) {
            d = -1.0;
         }
      }

      return new Vec3d(
         d * -Math.sin(Math.toRadians(f)) + d2 * Math.cos(Math.toRadians(f)),
         clientPlayerEntity.getVelocity().y,
         d * Math.cos(Math.toRadians(f)) - d2 * -Math.sin(Math.toRadians(f))
      );
   }

   public static double computeClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      double d3 = 1.0;
      double d4 = 1.0;
      if (clientPlayerEntity.hasStatusEffect(StatusEffects.SPEED)) {
         double d = clientPlayerEntity.getStatusEffect(StatusEffects.SPEED).getAmplifier();
         d3 = 1.0 + 0.2 * (d + 1.0);
      }

      if (clientPlayerEntity.hasStatusEffect(StatusEffects.SLOWNESS)) {
         double d2 = clientPlayerEntity.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
         d4 = 1.0 + 0.2 * (d2 + 1.0);
      }

      return 0.2873F * d3 / d4;
   }

   public static double computeClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity) {
      Vec3d vec3d = clientPlayerEntity.getVelocity();
      return Math.sqrt(vec3d.getX() * vec3d.getX() + vec3d.getY() * vec3d.getY());
   }

   public static void handleMoveEvent2(MoveEvent2 moveEvent2, ClientPlayerEntity clientPlayerEntity, double d) {
      double d4 = clientPlayerEntity.input.movementForward;
      double d5 = clientPlayerEntity.input.movementSideways;
      float f = clientPlayerEntity.getYaw();
      if (d4 == 0.0 && d5 == 0.0) {
         moveEvent2.setDoubleValue3(0.0);
         moveEvent2.setDoubleValue2(0.0);
      } else {
         if (d4 != 0.0) {
            if (d5 > 0.0) {
               f += d4 > 0.0 ? -45 : 45;
            } else if (d5 < 0.0) {
               f += d4 > 0.0 ? 45 : -45;
            }

            d5 = 0.0;
            if (d4 > 0.0) {
               d4 = 1.0;
            } else if (d4 < 0.0) {
               d4 = -1.0;
            }
         }

         double d2 = Math.sin(Math.toRadians(f + 90.0F));
         double d3 = Math.cos(Math.toRadians(f + 90.0F));
         moveEvent2.setDoubleValue3(d4 * d * d3 + d5 * d * d2);
         moveEvent2.setDoubleValue2(d4 * d * d2 - d5 * d * d3);
      }
   }

   public static boolean check4() {
      GameOptions gameOptions = mc.options;
      return (check() || gameOptions.jumpKey.isPressed() || gameOptions.sneakKey.isPressed()) && !FreecamModule.getFreecamModule().check7();
   }

   public static void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f, boolean flag) {
      double d = 1.0E-7;
      double d2 = Math.cos(Math.toRadians(f + 90.0F));
      double d3 = Math.sin(Math.toRadians(f + 90.0F));
      double d4 = clientPlayerEntity.getX() + d * d2;
      double d5 = clientPlayerEntity.getZ() + d * d3;
      clientPlayerEntity.setPosition(d4, clientPlayerEntity.getY(), d5);
      if (flag) {
         PlayerEntity playerEntity = ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
         clientPlayerEntity.networkHandler
            .sendPacket(new Full(d4, playerEntity.getY(), d5, playerEntity.getYaw(), playerEntity.getPitch(), playerEntity.isOnGround()));
      }
   }

   public static float computeClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity) {
      return computeFloat(
         FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity),
         mc.options.forwardKey.isPressed(),
         mc.options.backKey.isPressed(),
         mc.options.rightKey.isPressed(),
         mc.options.leftKey.isPressed()
      );
   }

   public static void handleKeyboardInput(KeyboardInput keyboardInput, float f, float f2) {
      float f3 = keyboardInput.movementForward;
      float f4 = keyboardInput.movementSideways;
      float f5 = (f - f2) * (float) (Math.PI / 180.0);
      float f6 = MathHelper.cos(f5);
      float f7 = MathHelper.sin(f5);
      keyboardInput.movementSideways = Math.round(f4 * f6 - f3 * f7);
      keyboardInput.movementForward = Math.round(f3 * f6 + f4 * f7);
   }

   public static float computeFloat(float f, boolean flag, boolean flag2, boolean flag3, boolean flag4) {
      float f2 = 0.0F;
      if (flag) {
         if (flag3) {
            f2 = 45.0F;
         } else if (flag4) {
            f2 = -45.0F;
         }
      } else if (flag2) {
         if (flag3) {
            f2 = 135.0F;
         } else if (flag4) {
            f2 = -135.0F;
         } else {
            f2 = 180.0F;
         }
      } else if (flag3) {
         f2 = 90.0F;
      } else if (flag4) {
         f2 = -90.0F;
      }

      return f + f2;
   }

   public static Vec3d createVec3d2(ClientPlayerEntity clientPlayerEntity) {
      double d = clientPlayerEntity.input.movementForward;
      double d2 = clientPlayerEntity.input.movementSideways;
      float f = TargetStrafeModule.computeFloat(
         clientPlayerEntity.prevYaw + (clientPlayerEntity.getYaw() - clientPlayerEntity.prevYaw) * mc.getRenderTickCounter().getTickDelta(false)
      );
      if (d == 0.0 && d2 == 0.0) {
         return new Vec3d(0.0, clientPlayerEntity.getVelocity().y, 0.0);
      }

      if (d != 0.0) {
         if (d2 > 0.0) {
            f += d > 0.0 ? -45 : 45;
         } else if (d2 < 0.0) {
            f += d > 0.0 ? 45 : -45;
         }

         d2 = 0.0;
         if (d > 0.0) {
            d = 1.0;
         } else if (d < 0.0) {
            d = -1.0;
         }
      }

      return new Vec3d(
         d * -Math.sin(Math.toRadians(f)) + d2 * Math.cos(Math.toRadians(f)),
         clientPlayerEntity.getVelocity().y,
         d * Math.cos(Math.toRadians(f)) - d2 * -Math.sin(Math.toRadians(f))
      );
   }

   public static boolean check5() {
      GameOptions gameOptions = mc.options;
      return (check() || gameOptions.jumpKey.isPressed()) && !FreecamModule.getFreecamModule().check7();
   }

   public static double[] values6(double d, float f) {
      double d2 = d * Math.sin(Math.toRadians(f + 90.0F));
      double d3 = d * Math.cos(Math.toRadians(f + 90.0F));
      return new double[]{d3, d2};
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity, World world) {
      Box box = playerEntity.getBoundingBox().expand(-0.3F, 0.0, -0.3F);
      Box box2 = new Box(box.minX, box.minY - 0.5, box.minZ, box.maxX, box.minY - 0.25, box.maxZ);
      return world.getBlockCollisions(playerEntity, box2).iterator().hasNext();
   }
}
