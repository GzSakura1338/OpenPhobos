package org.phobos.module.movement;

import java.util.Arrays;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.earthhack.ducks.IEntity;
import org.earthhack.mixin.EventSubscriber;
import org.joml.Vector2d;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.movement.SpeedState;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.movement.GroundMovementModel;
import org.phobos.core.movement.ConstantMovementModel;
import org.phobos.core.movement.StrictMovementModel;
import org.phobos.core.movement.ParameterizedMovementModel;
import org.phobos.core.movement.StepMovementModel;
import org.phobos.core.movement.LowHopMovementModel;
import org.phobos.core.movement.LowHopStateModel;
import org.phobos.core.movement.ScaledMovementModel;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.math.VectorMath;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.core.player.PlayerActionUtils.RangeCalculator;
import org.phobos.event.JumpEvent3;
import org.phobos.event.Listener;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.Module;
import org.phobos.module.player.ScaffoldModule;
import org.phobos.module.render.FreeLookModule;

public class SpeedModule extends Module implements EventSubscriber {
   public final Setting<Boolean> lock;
   public final Setting<Double> strafe;
   public int intValue;
   public final Setting<Boolean> boost;
   public final Setting<Float> useTimerSpeed;
   public final Setting<Double> threshold;
   public final Setting<Double> boostReduction;
   public final Setting<Integer> ticks;
   public int intValue2;
   public final Setting<SpeedModule.Watchdog> watchdog;
   public final Setting<Double> speed;
   public final Setting<Boolean> directional;
   public final Setting<SpeedModule.Mode> secondary;
   public final ElytraManager.FlyConfiguration flyConfiguration;
   public final Setting<SpeedModule.Radon> radon;
   public final Setting<SpeedModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", SpeedModule.Mode.Grim));
   public boolean flag;
   public final Setting<Boolean> instant;
   public final Setting<Boolean> rotation;
   public final Setting<Boolean> step;
   public final SpeedState speedState;
   public final Setting<SpeedModule.Mode> main = this.resolveSetting2(
      new EnumSetting<>("Main", SpeedModule.Mode.Grim).resolveSetting4(this.mode, SpeedModule.Mode.Merged)
   );
   public final MutableBoolean mutableBoolean;
   public final Setting<Double> lockDistance;
   public int intValue3;
   public float floatValue;
   public final Setting<Integer> boostTicks;
   public MovementModel.State state;
   public static SpeedModule speedModule;
   public final Setting<Float> acceleration;
   public final Setting<Boolean> useTimer;

   @Override
   public String nothing() {
      return this.mode.getValue().equals(SpeedModule.Mode.Merged)
         ? this.main.getValue() + " " + this.secondary.getValue()
         : String.valueOf(this.mode.getValue());
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, double d) {
      double[] doubles = PlayerUtils.values2(clientPlayerEntity, d);
      PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, doubles[0]);
      PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, doubles[1]);
   }

   public MovementModel createGroundMovementModel() {
      return switch ((SpeedModule.Mode)this.mode.getValue()) {
         case Split -> new LowHopMovementModel();
         case THop -> new LowHopStateModel();
         case Radon -> new StrictMovementModel();
         case LowHop -> new StepMovementModel();
         default -> new GroundMovementModel();
         case Strafe -> new ScaledMovementModel(this.speed.getValue());
         case Strict -> new ConstantMovementModel(1.0);
         case Merged -> new ScaledMovementModel(0.35);
         case Vulcan -> new ParameterizedMovementModel(0.3888882626268, 0.42);
      };
   }

   public float computeFloat(float f, boolean flag, boolean flag2, boolean flag3, boolean flag4) {
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

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, double d, float f, boolean... flags) {
      if (flags.length != 0 && flags[0]) {
         this.handleClientPlayerEntity3(clientPlayerEntity, f, d);
      } else {
         double[] doubles = PlayerUtils.values2(clientPlayerEntity, d);
         PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, doubles[0]);
         PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, doubles[1]);
      }
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, float f, double d) {
      double[] doubles = PlayerUtils.values(d, f);
      PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, doubles[0]);
      PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, doubles[1]);
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      for (VoxelShape voxelShape : clientWorld.getCollisions(clientPlayerEntity, clientPlayerEntity.getBoundingBox().offset(0.0, -0.66, 0.0))) {
         if (!voxelShape.isEmpty()) {
            return clientPlayerEntity.getVelocity().getY() < 0.0;
         }
      }

      return false;
   }

   @Override
   public void run4() {
      this.speedState.run();
      if (this.step.getValue() && StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run11();
      }

      this.phobosClient.getTimerManager().run2();
      this.intValue2 = 10000;
      if (this.useTimer.getValue() && this.phobosClient.getTimerManager().testFloat(this.useTimerSpeed.getValue())) {
         this.phobosClient.getTimerManager().run2();
      }
   }

   public Visibility resolveVisibility(SpeedModule.Mode mode) {
      return () -> this.testMode(mode);
   }

   public boolean testClientWorld(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      return clientWorld.getBlockCollisions(clientPlayerEntity, clientPlayerEntity.getBoundingBox().offset(0.0, -0.7, 0.0)).iterator().hasNext();
   }

   public Block resolveBlock(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, double d) {
      return clientWorld.getBlockState(BlockPos.ofFloored(clientPlayerEntity.getPos().add(0.0, d, 0.0))).getBlock();
   }

   public SpeedModule(PhobosClient phobosClient) {
      super(phobosClient, "Speed", Categories.movement, "Speeds up your movement");
      this.secondary = this.resolveSetting2(new EnumSetting<>("Secondary", SpeedModule.Mode.Intave).resolveSetting4(this.mode, SpeedModule.Mode.Merged));
      this.watchdog = this.resolveSetting2(
         new EnumSetting<>("Watchdog", SpeedModule.Watchdog._7Tick).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Watchdog))
      );
      this.rotation = this.resolveSetting2(
         new BooleanSetting("Rotation")
            .resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Watchdog))
            .resolveSetting2(() -> !this.watchdog.testObject2(SpeedModule.Watchdog.Strict), true)
      );
      this.speed = this.resolveSetting2(new NumberSetting<>("Speed", 1.6835, 0.1, 4.0).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Vanilla)));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 2, 1, 5).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Skip)));
      this.acceleration = this.resolveSetting2(
         new NumberSetting<>("Acceleration", 1.65F, 1.02F, 2.0F).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Skip))
      );
      this.strafe = this.resolveSetting2(new NumberSetting<>("Strafe", 0.8, 0.1, 1.4).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Radon)));
      this.radon = this.resolveSetting2(new EnumSetting<>("Radon", SpeedModule.Radon.Fast).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Radon)));
      this.directional = this.resolveSetting2(new BooleanSetting("Directional").resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Grim)));
      this.instant = this.resolveSetting2(new BooleanSetting("Instant").resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Legit)));
      this.threshold = this.resolveSetting2(new NumberSetting<>("Threshold", 1.0, 0.1, 2.0).resolveSetting3(this.resolveVisibility(SpeedModule.Mode.Grim)));
      this.lock = this.resolveSetting2(new BooleanSetting("Lock"));
      this.lockDistance = this.resolveSetting2(new NumberSetting<>("LockDistance", 0.3, 0.1, 2.0));
      this.useTimer = this.resolveSetting2(new BooleanSetting("UseTimer").resolveSetting7("Uses timer"));
      this.useTimerSpeed = this.resolveSetting2(new NumberSetting<>("UseTimerSpeed", 1.0888F, 1.0F, 2.0F).resolveNumberSetting("NCP", 1.0888F));
      this.boost = this.resolveSetting2(new BooleanSetting("Boost"));
      this.boostTicks = this.resolveSetting2(new NumberSetting<>("BoostTicks", 0, 0, 16).resolveNumberSetting("None", 0));
      this.boostReduction = this.resolveSetting2(new NumberSetting<>("BoostReduction", 1.0, 0.1, 5.0));
      this.step = this.resolveSetting2(new BooleanSetting("Step").resolveSetting7("Turns on and off step for you"));
      this.flyConfiguration = ElytraManager.FlyConfiguration.createFactory().getFactory4().getFactory2().resolveFactory8(18).getFlyConfiguration2();
      this.mutableBoolean = new MutableBoolean();
      this.state = new MovementModel.State();
      this.speedState = new SpeedState();
      this.resolveSettingGroup(this.useTimer, this.useTimerSpeed)
         .resolveSettingGroup5(
            this.resolveVisibility2(
               this.mode, SpeedModule.Mode.Strafe, SpeedModule.Mode.Strict, SpeedModule.Mode.Fast, SpeedModule.Mode.Friction, SpeedModule.Mode.THop
            )
         );
      this.resolveSettingGroup(this.boost, this.boostTicks, this.boostReduction)
         .resolveSettingGroup5(() -> Arrays.stream(this.values()).anyMatch(mode -> mode.supportsBoost));
      this.resolveSettingGroup(this.lock, this.lockDistance).resolveSettingGroup5(this.resolveVisibility(SpeedModule.Mode.Grim));
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!LongJumpModule.getLongJumpModule().check7() && !FlightModule.getFlightModule().check7()) {
                  if (SpeedModule.this.testMode(SpeedModule.Mode.Grim)
                     && (
                        clientPlayerEntity.input.movementForward > 0.0F
                           || SpeedModule.this.directional.getValue()
                              && (clientPlayerEntity.input.movementSideways != 0.0F || clientPlayerEntity.input.movementForward != 0.0F)
                     )
                     && !BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
                     Box box = clientPlayerEntity.getBoundingBox().expand(SpeedModule.this.threshold.getValue());
                     int i = 0;

                     for (Entity entity : clientWorld.getOtherEntities(null, BlockPosUtils.createBox(clientPlayerEntity, 15.0))) {
                        if (!(entity instanceof ClientPlayerEntity)
                           && entity instanceof LivingEntity
                           && !(entity instanceof ArmorStandEntity)
                           && !(entity instanceof FakePlayer)
                           && !(entity instanceof IEntity iEntity && iEntity.isClientSide())
                           && entity.getBoundingBox().intersects(box)) {
                           i++;
                        }
                     }

                     if (i > 0) {
                        float f;
                        PlayerEntity playerEntity;
                        if (SpeedModule.this.lock.getValue()
                           && (playerEntity = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient)) != null
                           && clientPlayerEntity.getPos().distanceTo(playerEntity.getPos()) <= SpeedModule.this.lockDistance.getValue()
                           && !BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)
                           && !BlockUtils.testPlayerEntity(playerEntity, clientWorld)) {
                           f = RotationUtils.values(clientPlayerEntity, playerEntity)[0];
                        } else {
                           f = AutoBoostModule.flag
                              ? AutoBoostModule.floatValue
                              : (
                                 SpeedModule.this.directional.getValue()
                                    ? SpeedModule.this.computeFloat(
                                       FreeLookModule.getFreeLookModule().check7()
                                          ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue2()
                                          : clientPlayerEntity.getYaw(),
                                       mc.options.forwardKey.isPressed(),
                                       mc.options.backKey.isPressed(),
                                       mc.options.rightKey.isPressed(),
                                       mc.options.leftKey.isPressed()
                                    )
                                    : phobosClient.getRotationManager().getFloatValue15()
                              );
                        }

                        double d = Math.toRadians(f);
                        double d2 = 0.08 * i;
                        Vec3d vec3d = clientPlayerEntity.getVelocity();
                        Vec3d vec3d2 = new Vec3d(vec3d.x + -Math.sin(d) * d2, vec3d.y, vec3d.z + Math.cos(d) * d2);
                        clientPlayerEntity.setVelocity(vec3d2);
                     }
                  }

                  SpeedModule.Mode[] var15 = SpeedModule.this.values();
                  int var16 = var15.length;
                  int var18 = 0;

                  while (var18 < var16) {
                     SpeedModule.Mode mode = var15[var18];
                     switch (mode) {
                        case Fast:
                        case Split:
                        case THop:
                        case Radon:
                        case LowHop:
                        case Strafe:
                        case Merged:
                        case Vulcan:
                           if (!mode.equals(SpeedModule.Mode.Polar) || clientPlayerEntity.isOnGround() && phobosClient.getTickManager().testLong(500L)) {
                              if (!PlayerUtils.check()) {
                                 clientPlayerEntity.setVelocity(0.0, clientPlayerEntity.getVelocity().y, 0.0);
                              }

                              SpeedModule.this.state
                                 .setDoubleValue2(
                                    Math.sqrt(
                                       (clientPlayerEntity.getX() - clientPlayerEntity.prevX) * (clientPlayerEntity.getX() - clientPlayerEntity.prevX)
                                          + (clientPlayerEntity.getZ() - clientPlayerEntity.prevZ) * (clientPlayerEntity.getZ() - clientPlayerEntity.prevZ)
                                    )
                                 );
                           }
                        case Vanilla:
                        case Polar:
                        case Legit:
                        case Watchdog:
                        case Elytra:
                        case BlocksMC:
                        case YPort:
                        case Intave:
                        case Strict:
                        case Skip:
                        case Friction:
                        case Grim:
                        default:
                           var18++;
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<MoveEvent2>() {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!LongJumpModule.getLongJumpModule().check7() && !FlightModule.getFlightModule().check7() && !moveEvent2.isFlag()) {
                  for (SpeedModule.Mode mode : SpeedModule.this.values()) {
                     switch (mode) {
                        case Fast:
                        case Split:
                        case THop:
                        case Radon:
                        case LowHop:
                        case Strafe:
                        case Strict:
                        case Merged:
                        case Vulcan:
                           if (SpeedModule.this.useTimer.getValue()) {
                              if (phobosClient.getTickManager().testLong(250L)) {
                                 phobosClient.getTimerManager().handleFloat(SpeedModule.this.useTimerSpeed.getValue(), TimerPriority.Normal);
                              } else {
                                 phobosClient.getTimerManager().run2();
                              }
                           }

                           if (!mode.equals(SpeedModule.Mode.Polar) || clientPlayerEntity.isOnGround() && phobosClient.getTickManager().testLong(500L)) {
                              MovementModel movementModel = SpeedModule.this.createGroundMovementModel();
                              if (SpeedModule.this.boost.getValue()) {
                                 SpeedModule.this.speedState.handleInt(SpeedModule.this.boostTicks.getValue());
                                 if (movementModel instanceof GroundMovementModel groundMovementModel) {
                                    groundMovementModel.setDoubleValue2(SpeedModule.this.speedState.getDoubleValue());
                                 }
                              }

                              if (clientPlayerEntity.fallDistance <= 5.0
                                 && (clientPlayerEntity.sidewaysSpeed != 0.0F || clientPlayerEntity.forwardSpeed != 0.0F)) {
                                 SpeedModule.this.state = movementModel.resolveState(
                                    clientPlayerEntity, mc.world, SpeedModule.this.state, PlayerUtils.createVec3d2(clientPlayerEntity)
                                 );
                                 if (!SpeedModule.this.state.isFlag2()) {
                                    clientPlayerEntity.setVelocity(
                                       clientPlayerEntity.getVelocity().x, SpeedModule.this.state.getVec3d().y, clientPlayerEntity.getVelocity().z
                                    );
                                    moveEvent2.setVec3d(
                                       new Vec3d(SpeedModule.this.state.getVec3d().x, SpeedModule.this.state.getVec3d().y, SpeedModule.this.state.getVec3d().z)
                                    );
                                 }
                              } else {
                                 SpeedModule.this.state.run();
                              }
                           }
                        case Vanilla:
                        case Polar:
                        case Legit:
                        case Watchdog:
                        case Elytra:
                        case BlocksMC:
                        case YPort:
                        case Intave:
                        case Skip:
                        case Friction:
                        default:
                           break;
                        case Grim:
                           if (!PlayerUtils.check()) {
                              return;
                           }

                           if (clientPlayerEntity.isTouchingWater() || clientPlayerEntity.isInLava() || clientPlayerEntity.horizontalCollision) {
                              return;
                           }

                           if (clientPlayerEntity.isOnGround()) {
                              clientPlayerEntity.jump();
                           } else {
                              PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, -1.0);
                           }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<Receive<CommonPingS2CPacket>>() {
         public void handleObject2(Receive<CommonPingS2CPacket> receive) {
            if (!LongJumpModule.getLongJumpModule().check7() && !FlightModule.getFlightModule().check7()) {
               if (SpeedModule.this.testMode(SpeedModule.Mode.Skip)) {
                  if (SpeedModule.this.intValue <= SpeedModule.this.ticks.getValue()) {
                     SpeedModule.this.intValue++;
                     SpeedModule.this.floatValue = MathHelper.clamp(SpeedModule.this.floatValue + 0.005F, 0.0F, 1.0F);
                     receive.setCancelled(true);
                  }
               }
            }
         }
      });
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               SpeedModule.this.intValue2 = clientPlayerEntity.isOnGround() ? 0 : SpeedModule.this.intValue2 + 1;
               SpeedModule.this.intValue3 = clientPlayerEntity.isOnGround() ? SpeedModule.this.intValue3 + 1 : 0;
               if (!LongJumpModule.getLongJumpModule().check7() && !FlightModule.getFlightModule().check7()) {
                  for (SpeedModule.Mode mode : SpeedModule.this.values()) {
                     switch (mode) {
                        case Vanilla:
                           if (!SpeedModule.this.lock.getValue() || RotationManager.isFlag()) {
                              return;
                           }

                           if (BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
                              return;
                           }

                           PlayerEntity playerEntity = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, phobosClient);
                           if (playerEntity == null
                              || !RangeCalculator.Standing
                                 .calculate(clientPlayerEntity, playerEntity.getBoundingBox(), SpeedModule.this.lockDistance.getValue())
                              || BlockUtils.testPlayerEntity(playerEntity, clientWorld)) {
                              return;
                           }

                           float[] floats = RotationUtils.values(clientPlayerEntity, playerEntity);
                           phobosClient.getRotationManager().testFloat2(floats[0], FreeLookModule.computeClientPlayerEntity(clientPlayerEntity), true);
                           SpeedModule.this.flag = true;
                        case Strafe:
                        case Strict:
                        default:
                           break;
                        case Polar:
                           if (PlayerUtils.check5()) {
                              boolean flag = clientPlayerEntity.isSneaking() || clientPlayerEntity.isTouchingWater() || clientPlayerEntity.hurtTime > 0;
                              MutableFloat mutableFloat = new MutableFloat(Float.NaN);
                              if (SpeedModule.this.rotation.getValue()
                                 && !SpeedModule.this.watchdog.testObject2(SpeedModule.Watchdog.Strict)
                                 && !RotationManager.isFlag()
                                 && !ScaffoldModule.getScaffoldModule().check7()) {
                                 float f3 = PlayerUtils.computeClientPlayerEntity6(clientPlayerEntity);
                                 phobosClient.getRotationManager().testFloat2(f3, FreeLookModule.computeClientPlayerEntity(clientPlayerEntity), true);
                                 mutableFloat.setValue(f3);
                                 SpeedModule.this.mutableBoolean.setTrue();
                              }

                              switch ((SpeedModule.Watchdog)SpeedModule.this.watchdog.getValue()) {
                                 case _8Tick:
                                    if (SpeedModule.this.intValue2 == 0) {
                                       if (!mc.options.jumpKey.isPressed() && !ScaffoldModule.getScaffoldModule().check7()) {
                                          SpeedModule.this.handleClientPlayerEntity2(
                                             clientPlayerEntity,
                                             PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) - Math.random() / 100.0,
                                             mutableFloat.getValue(),
                                             SpeedModule.this.mutableBoolean.getValue()
                                          );
                                          clientPlayerEntity.jump();
                                       }
                                    } else if (flag || clientPlayerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                                       break;
                                    }

                                    switch (SpeedModule.this.intValue2) {
                                       case 1:
                                          PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.39);
                                       case 2:
                                       default:
                                          continue;
                                       case 3:
                                          PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, clientPlayerEntity.getVelocity().getY() - 0.13);
                                          continue;
                                       case 4:
                                          PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, clientPlayerEntity.getVelocity().getY() - 0.2);
                                          continue;
                                    }
                                 case _7Tick:
                                    if (SpeedModule.this.intValue2 == 0) {
                                       if (!mc.options.jumpKey.isPressed() && !ScaffoldModule.getScaffoldModule().check7()) {
                                          SpeedModule.this.handleClientPlayerEntity2(
                                             clientPlayerEntity,
                                             PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) - Math.random() / 100.0,
                                             mutableFloat.getValue(),
                                             SpeedModule.this.mutableBoolean.getValue()
                                          );
                                       }
                                    } else if (SpeedModule.this.intValue2 == 5 && !flag) {
                                       PlayerActionUtils.handleClientPlayerEntity29(
                                          clientPlayerEntity, VectorMath.computeDouble(clientPlayerEntity.getVelocity().getY(), 2)
                                       );
                                    }
                                    break;
                                 case _5Tick:
                                    if (SpeedModule.this.intValue2 == 0) {
                                       if (!mc.options.jumpKey.isPressed() && !ScaffoldModule.getScaffoldModule().check7()) {
                                          SpeedModule.this.handleClientPlayerEntity2(
                                             clientPlayerEntity,
                                             PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) - Math.random() / 100.0,
                                             mutableFloat.getValue(),
                                             SpeedModule.this.mutableBoolean.getValue()
                                          );
                                          clientPlayerEntity.jump();
                                       }
                                    } else if (flag || clientPlayerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                                       break;
                                    }

                                    int i = (int)Math.round(clientPlayerEntity.getY() % 1.0 * 10000.0);
                                    switch (i) {
                                       case 1138:
                                          PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, clientPlayerEntity.getVelocity().getY() - 0.13);
                                          continue;
                                       case 2031:
                                          PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, clientPlayerEntity.getVelocity().getY() - 0.2);
                                          continue;
                                       case 4200:
                                          PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.39);
                                       default:
                                          continue;
                                    }
                                 case Strict:
                                    if (clientPlayerEntity.isOnGround()) {
                                       SpeedModule.this.handleClientPlayerEntity2(
                                          clientPlayerEntity,
                                          PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity),
                                          mutableFloat.getValue(),
                                          SpeedModule.this.mutableBoolean.getValue()
                                       );
                                    } else {
                                       switch (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity)) {
                                          case 1:
                                             SpeedModule.this.handleClientPlayerEntity(
                                                clientPlayerEntity, PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity)
                                             );
                                             PlayerActionUtils.handleClientPlayerEntity27(clientPlayerEntity, 0.0568);
                                          case 2:
                                          default:
                                             break;
                                          case 3:
                                             PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 0.95, 1.0, 0.95);
                                             PlayerActionUtils.handleClientPlayerEntity27(clientPlayerEntity, -0.13);
                                             break;
                                          case 4:
                                             PlayerActionUtils.handleClientPlayerEntity27(clientPlayerEntity, -0.2);
                                       }

                                       if (SpeedModule.this.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
                                          SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity));
                                       }

                                       if (clientPlayerEntity.hurtTime == 9) {
                                          SpeedModule.this.handleClientPlayerEntity(
                                             clientPlayerEntity, Math.min(PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity), 0.281)
                                          );
                                       }

                                       StatusEffectInstance statusEffectInstance = clientPlayerEntity.getStatusEffect(StatusEffects.SPEED);
                                       if (statusEffectInstance != null && statusEffectInstance.getAmplifier() == 2) {
                                          switch (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity)) {
                                             case 1:
                                             case 2:
                                             case 5:
                                             case 6:
                                             case 8:
                                                PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 1.2, 1.0, 1.2);
                                             case 3:
                                             case 4:
                                             case 7:
                                          }
                                       }
                                    }
                              }
                           }
                           break;
                        case Legit:
                           if (PlayerUtils.check()) {
                              if (!RotationManager.isFlag()) {
                                 phobosClient.getRotationManager().testFloat2(FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), 78.25F, true);
                              }

                              phobosClient.getElytraManager().testFlyConfiguration2(SpeedModule.this.flyConfiguration);
                           }
                           break;
                        case Watchdog:
                           if (SpeedModule.this.floatValue <= 0.0F) {
                              phobosClient.getTimerManager().run2();
                              SpeedModule.this.intValue = 0;
                           } else {
                              phobosClient.getTimerManager().handleFloat(Math.max(SpeedModule.this.acceleration.getValue(), 1.0F), TimerPriority.High);
                              SpeedModule.this.floatValue = MathHelper.clamp(
                                 SpeedModule.this.floatValue - (0.0025F * SpeedModule.this.acceleration.getValue() - 0.0025F), 0.0F, 1.0F
                              );
                           }
                           break;
                        case Elytra:
                           if (PlayerUtils.check()
                              && clientWorld.getBlockCollisions(clientPlayerEntity, clientPlayerEntity.getBoundingBox().offset(0.0, -0.3, 0.0))
                                 .iterator()
                                 .hasNext()) {
                              float f = FreeLookModule.getFreeLookModule().check7()
                                 ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue2()
                                 : clientPlayerEntity.getYaw();
                              float f2 = SpeedModule.this.computeFloat(
                                 f,
                                 mc.options.forwardKey.isPressed(),
                                 mc.options.backKey.isPressed(),
                                 mc.options.rightKey.isPressed(),
                                 mc.options.leftKey.isPressed()
                              );
                              phobosClient.getRotationManager().testFloat(f2, clientPlayerEntity.getPitch());
                           }
                           break;
                        case BlocksMC:
                           if (PlayerUtils.check5()) {
                              boolean flag2 = clientPlayerEntity.hasStatusEffect(StatusEffects.SPEED);
                              switch (SpeedModule.this.intValue2) {
                                 case 1:
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, flag2 ? 0.771 : 0.5);
                                 case 2:
                                 case 4:
                                 case 6:
                                 case 8:
                                 default:
                                    break;
                                 case 3:
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, flag2 ? 0.605 : 0.31);
                                    break;
                                 case 5:
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, flag2 ? 0.57 : 0.29);
                                    PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, flag2 ? -0.5 : -0.37);
                                    break;
                                 case 7:
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, flag2 ? 0.595 : 0.27);
                                    break;
                                 case 9:
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, flag2 ? 0.595 : 0.28);
                              }

                              if (!clientPlayerEntity.isOnGround() && Math.abs(clientPlayerEntity.fallDistance) > 0.0F && flag2) {
                                 PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 1.055, 1.0, 1.055);
                              }
                           }
                           break;
                        case YPort:
                           if (PlayerUtils.testClientPlayerEntity(clientPlayerEntity)) {
                              Vec3d vec3d = clientPlayerEntity.getVelocity();
                              double d = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                              SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, d - 0.004);
                           }

                           StatusEffectInstance statusEffectInstance2;
                           if ((statusEffectInstance2 = clientPlayerEntity.getStatusEffect(StatusEffects.SPEED)) != null
                              && statusEffectInstance2.getAmplifier() > 0
                              && PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity) == 3) {
                              PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 1.12, 1.0, 1.12);
                           }

                           if (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity) == 4 && clientPlayerEntity.getY() % 1.0 == 0.16610926093821377) {
                              PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, -0.09800000190734863);
                           }
                           break;
                        case Intave:
                           if (PlayerUtils.check5()) {
                              switch (SpeedModule.this.intValue2) {
                                 case 0:
                                    clientPlayerEntity.jump();
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, SpeedModule.this.strafe.getValue());
                                    break;
                                 case 1:
                                 case 7:
                                 case 9:
                                    SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) * 1.6);
                                    break;
                                 case 2:
                                 case 3:
                                 case 4:
                                 case 5:
                                 case 6:
                                 case 8:
                                 default:
                                    switch ((SpeedModule.Radon)SpeedModule.this.radon.getValue()) {
                                       case Fast:
                                          if (SpeedModule.this.intValue2 == 4) {
                                             PlayerActionUtils.handleClientPlayerEntity29(
                                                clientPlayerEntity, VectorMath.computeDouble(clientPlayerEntity.getVelocity().getY(), 4)
                                             );
                                          }
                                          break;
                                       case Adjust:
                                          if (SpeedModule.this.intValue2 == 5) {
                                             PlayerActionUtils.handleClientPlayerEntity29(
                                                clientPlayerEntity, VectorMath.computeDouble(clientPlayerEntity.getVelocity().getY(), 2)
                                             );
                                          }
                                    }
                              }

                              PlayerActionUtils.handleClientPlayerEntity24(
                                 clientPlayerEntity, MathUtils.computeDouble5(clientPlayerEntity.getVelocity().getX(), -1.0, 1.0)
                              );
                              PlayerActionUtils.handleClientPlayerEntity29(
                                 clientPlayerEntity, MathUtils.computeDouble5(clientPlayerEntity.getVelocity().getY(), -1.0, 1.0)
                              );
                              PlayerActionUtils.handleClientPlayerEntity26(
                                 clientPlayerEntity, MathUtils.computeDouble5(clientPlayerEntity.getVelocity().getZ(), -1.0, 1.0)
                              );
                           }
                           break;
                        case Skip:
                           switch (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity)) {
                              case 1:
                                 PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 1.04, 1.0, 1.04);
                                 break;
                              case 2:
                              case 3:
                              case 4:
                                 PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 1.02, 1.0, 1.02);
                           }

                           phobosClient.getTimerManager().handleFloat(1.002F, TimerPriority.Low);
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent>() {
            public void handleObject(
               TickEvent tickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (SpeedModule.Mode mode : SpeedModule.this.values()) {
                  switch (mode) {
                     case Vanilla:
                        if (SpeedModule.this.flag) {
                           tickEvent.getKeyboardInput().movementForward = 1.0F;
                           tickEvent.getKeyboardInput().movementSideways = 0.0F;
                           SpeedModule.this.flag = false;
                        }
                     case Strafe:
                     case Watchdog:
                     default:
                        break;
                     case Polar:
                        if (SpeedModule.this.rotation.getValue()
                           && SpeedModule.this.mutableBoolean.getValue()
                           && !SpeedModule.this.watchdog.testObject2(SpeedModule.Watchdog.Strict)) {
                           tickEvent.getKeyboardInput().movementForward = 1.0F;
                           tickEvent.getKeyboardInput().movementSideways = 0.0F;
                           SpeedModule.this.mutableBoolean.setFalse();
                        }

                        if ((
                              SpeedModule.this.watchdog.testObject2(SpeedModule.Watchdog.Strict)
                                 || SpeedModule.this.watchdog.testObject2(SpeedModule.Watchdog._5Tick)
                           )
                           && PlayerUtils.check()) {
                           tickEvent.getKeyboardInput().jumping = true;
                        }
                        break;
                     case Legit:
                        if (PlayerUtils.check()) {
                           tickEvent.getKeyboardInput().jumping = true;
                           tickEvent.getKeyboardInput().movementForward = 1.0F;
                           tickEvent.getKeyboardInput().movementSideways = 0.0F;
                        }
                        break;
                     case Elytra:
                        if (PlayerUtils.check()
                           && (
                              SpeedModule.this.instant.getValue()
                                 ? clientPlayerEntity.isOnGround()
                                 : new Vector2d(clientPlayerEntity.getVelocity().x, clientPlayerEntity.getVelocity().z).length() >= 0.1
                                    || clientPlayerEntity.horizontalCollision
                           )) {
                           tickEvent.getKeyboardInput().jumping = true;
                        }
                        break;
                     case BlocksMC:
                     case YPort:
                        if (tickEvent.getKeyboardInput().pressingForward
                           || tickEvent.getKeyboardInput().pressingBack
                           || tickEvent.getKeyboardInput().pressingRight
                           || tickEvent.getKeyboardInput().pressingLeft) {
                           tickEvent.getKeyboardInput().jumping = true;
                        }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<JumpEvent3>() {
            public void handleObject(
               JumpEvent3 jumpEvent3,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (SpeedModule.Mode mode : SpeedModule.this.values()) {
                  switch (mode) {
                     case Polar:
                        if (SpeedModule.this.watchdog.testObject2(SpeedModule.Watchdog.Strict)) {
                           double d3 = 0.247
                              + 0.15
                                 * (
                                    clientPlayerEntity.hasStatusEffect(StatusEffects.SPEED)
                                       ? Objects.requireNonNull(clientPlayerEntity.getStatusEffect(StatusEffects.SPEED)).getAmplifier()
                                       : 0
                                 );
                           SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, Math.max(d3, PlayerUtils.computeClientPlayerEntity5(clientPlayerEntity)));
                        }
                        break;
                     case YPort:
                        if (PlayerUtils.testClientPlayerEntity(clientPlayerEntity)) {
                           StatusEffectInstance statusEffectInstance;
                           double d = 0.281
                              + 0.2
                                 * (
                                    (statusEffectInstance = clientPlayerEntity.getStatusEffect(StatusEffects.SPEED)) != null
                                       ? statusEffectInstance.getAmplifier()
                                       : 0
                                 );
                           Vec3d vec3d = clientPlayerEntity.getVelocity();
                           double d2 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                           SpeedModule.this.handleClientPlayerEntity(clientPlayerEntity, Math.max(d2, d) - 0.01);
                        }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<?>>() {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               SpeedModule.Mode[] var5 = SpeedModule.this.values();
               int var6 = var5.length;
               int var7 = 0;

               while (var7 < var6) {
                  SpeedModule.Mode mode = var5[var7];
                  switch (mode) {
                     case BlocksMC:
                        if (send.getPacket() instanceof IPlayerMoveC2SPacket iPlayerMoveC2SPacket && clientPlayerEntity.getVelocity().getY() < 0.0) {
                           iPlayerMoveC2SPacket.setOnGround(true);
                        }
                     default:
                        var7++;
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<ExplosionS2CPacket>>() {
            public void handleObject(
               Receive<ExplosionS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SpeedModule.this.boost.getValue()) {
                  SpeedModule.this.speedState
                     .handleDouble2(
                        ((ExplosionS2CPacket)receive.getPacket()).getPlayerVelocityX(),
                        ((ExplosionS2CPacket)receive.getPacket()).getPlayerVelocityZ(),
                        SpeedModule.this.boostReduction.getValue()
                     );
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<EntityVelocityUpdateS2CPacket>>() {
            public void handleObject(
               Receive<EntityVelocityUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (((EntityVelocityUpdateS2CPacket)receive.getPacket()).getEntityId() == clientPlayerEntity.getId()) {
                  if (SpeedModule.this.boost.getValue()) {
                     SpeedModule.this.speedState
                        .handleDouble2(
                           ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityX(),
                           ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityZ(),
                           SpeedModule.this.boostReduction.getValue()
                        );
                  }
               }
            }
         }
      );
      speedModule = this;
   }

   public static SpeedModule getSpeedModule() {
      return speedModule;
   }

   public boolean testMode(SpeedModule.Mode mode) {
      return this.mode.getValue().equals(SpeedModule.Mode.Merged)
         ? mode.equals(this.main.getValue()) || mode.equals(this.secondary.getValue())
         : mode.equals(this.mode.getValue());
   }

   @Override
   public void run3() {
      this.speedState.run();
      if (this.step.getValue() && !StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run12();
      }
   }

   public SpeedModule.Mode[] values() {
      return this.mode.getValue().equals(SpeedModule.Mode.Merged)
         ? new SpeedModule.Mode[]{this.main.getValue(), this.secondary.getValue()}
         : new SpeedModule.Mode[]{this.mode.getValue()};
   }

   private enum Mode implements TextProvider {
      Strafe("NCP strafe", true),
      Fast("Fast NCP strafe", true),
      Friction("NCP Compatible fast low-hop", true),
      Strict("Strict NCP strafe", true),
      LowHop("Low-hop", true),
      Grim("Makes you move faster when colliding with entities"),
      Vanilla("Vanilla strafe"),
      Watchdog("Watchdog low-hop"),
      Elytra("Deploys elytra before landing to get additional boost"),
      Skip("Transaction tick-skipping"),
      Legit("Legit prediction compatible strafe"),
      Vulcan("Vulcan strafe"),
      BlocksMC("BlocksMC (Verus) strafe"),
      Radon("RadonAC strafe"),
      Split("Boost strafe"),
      Intave("Intave 1.003% speed"),
      Polar("Polar on-ground speed"),
      Merged("Multi-Mode"),
      YPort("Move up and down on ground"),
      THop("For less strict NCP servers", true);

      public final boolean supportsBoost;
      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      public boolean isSupportsBoost() {
         return this.supportsBoost;
      }

      Mode(final String description, final boolean supportsBoost) {
         this.text = description;
         this.supportsBoost = supportsBoost;
      }

      Mode(String description) {
         this(description, false);
      }
   }

   private enum Radon {
      Fast,
      Adjust;
   }

   private enum Watchdog {
      _7Tick,
      _5Tick,
      _8Tick,
      Strict;
   }
}
