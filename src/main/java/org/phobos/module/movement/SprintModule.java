package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.EndTickEvent;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickMovementEvent;
import org.phobos.module.Module;
import org.phobos.module.combat.CriticalsModule;
import org.phobos.module.render.FreeLookModule;

public class SprintModule extends Module {
   public boolean flag;
   public final Setting<Float> pitchAngle;
   public final Setting<Boolean> pitch;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;
   public boolean flag5;
   public boolean flag6;
   public int intValue;
   public final Setting<Boolean> diagonal;
   public boolean flag7;
   public final Setting<SprintModule.Stop> stop;
   public final Setting<Boolean> randomize;
   public boolean flag8;
   public Vec3d vec3d;
   public boolean flag9;
   public static SprintModule sprintModule;
   public final Setting<Boolean> air;
   public final Setting<SprintModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", SprintModule.Mode.Legit));
   public int intValue2;
   public final Setting<Float> amount;

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return this.diagonal.getValue()
         && (!mc.options.jumpKey.isPressed() || !clientPlayerEntity.isOnGround())
         && !PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity);
   }

   public static boolean check() {
      return sprintModule.check7() && sprintModule.mode.testObject2(SprintModule.Mode.Rage);
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

   public float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      float f = PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) ? -5.5F : 0.0F;
      if (!this.pitch.getValue()) {
         return f;
      }

      float f2 = this.pitchAngle.getValue();
      return clientPlayerEntity.input.sneaking ? f2 : (clientPlayerEntity.input.jumping ? -f2 : f);
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      return switch ((SprintModule.Mode)this.mode.getValue()) {
         case Rage -> !clientPlayerEntity.isTouchingWater();
         case Directional -> !clientPlayerEntity.isTouchingWater() && clientPlayerEntity.input.movementForward > 0.0F;
         case Always, Legit -> PlayerUtils.check()
            || !this.stop.testObject2(SprintModule.Stop.Vanilla) && this.mode.getValue().equals(SprintModule.Mode.Directional);
      };
   }

   public SprintModule(PhobosClient phobosClient) {
      super(phobosClient, "Sprint", Categories.movement, "Modifies sprint");
      this.stop = this.resolveSetting2(new EnumSetting<>("Stop", SprintModule.Stop.Vanilla).resolveSetting4(this.mode, SprintModule.Mode.Directional));
      this.randomize = this.resolveSetting2(new BooleanSetting("Randomize", false));
      this.amount = this.resolveSetting2(new NumberSetting<>("Amount", 2.0F, 0.0F, 10.0F));
      this.air = this.resolveSetting2(
         new BooleanSetting("Air", false).resolveSetting7("Rotates in air").resolveSetting4(this.mode, SprintModule.Mode.Directional)
      );
      this.diagonal = this.resolveSetting2(
         new BooleanSetting("Diagonal").resolveSetting7("Rotates sideways to gain additional speed").resolveSetting4(this.mode, SprintModule.Mode.Directional)
      );
      this.pitch = this.resolveSetting2(new BooleanSetting("Pitch"));
      this.pitchAngle = this.resolveSetting2(new NumberSetting<>("PitchAngle", 45.0F, 45.0F, 90.0F).resolveSetting7("Elytra tilt"));
      this.intValue = 0;
      this.intValue2 = 0;
      this.resolveSettingGroup(this.randomize, this.amount).resolveSettingGroup4(this.mode, SprintModule.Mode.Directional);
      this.resolveSettingGroup(this.pitch, this.pitchAngle).resolveSettingGroup4(this.mode, SprintModule.Mode.Directional);
      this.handleListener(
         new SafeListener<EndTickEvent>() {
            public void handleObject(
               EndTickEvent endTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SprintModule.this.testClientPlayerEntity2(clientPlayerEntity)) {
                  clientPlayerEntity.setSprinting(true);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(1) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SprintModule.this.testClientPlayerEntity2(clientPlayerEntity) && SprintModule.this.mode.getValue().equals(SprintModule.Mode.Directional)) {
                  float f9 = FreeLookModule.getFreeLookModule().check7()
                     ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue2()
                     : clientPlayerEntity.getYaw();
                  SprintModule.this.flag5 = mc.options.forwardKey.isPressed();
                  SprintModule.this.flag4 = mc.options.backKey.isPressed();
                  SprintModule.this.flag6 = mc.options.rightKey.isPressed();
                  SprintModule.this.flag = mc.options.leftKey.isPressed();
                  if (PlayerUtils.computeClientPlayerEntity3(clientPlayerEntity) > 1 && !SprintModule.this.air.getValue()) {
                     SprintModule.this.vec3d = null;
                     SprintModule.this.flag8 = false;
                     return;
                  }

                  if (!PlayerUtils.check()) {
                     switch ((SprintModule.Stop)SprintModule.this.stop.getValue()) {
                        case Vanilla:
                        default:
                           break;
                        case Grim:
                           if (clientPlayerEntity.horizontalCollision) {
                              SprintModule.this.flag8 = false;
                              return;
                           }

                           if (SprintModule.this.flag8) {
                              if (SprintModule.this.intValue > 0 || SprintModule.this.intValue2 <= 2) {
                                 SprintModule.this.flag8 = false;
                                 SprintModule.this.flag3 = false;
                                 SprintModule.this.flag7 = false;
                                 SprintModule.this.flag2 = false;
                                 SprintModule.this.flag9 = false;
                                 SprintModule.this.intValue2 = 0;
                                 return;
                              }

                              float f3 = SprintModule.this.computeFloat(
                                    f9, SprintModule.this.flag7, SprintModule.this.flag3, SprintModule.this.flag9, SprintModule.this.flag2
                                 )
                                 + (SprintModule.this.testClientPlayerEntity(clientPlayerEntity) ? 45.0F : 0.0F);
                              if (SprintModule.this.randomize.getValue()) {
                                 float f = -SprintModule.this.amount.getValue() - MathUtils.computeFloat8(-0.0085671F, 0.0085217F);
                                 float f2 = SprintModule.this.amount.getValue() + MathUtils.computeFloat8(-0.0085822F, 0.0085156F);
                                 f3 += MathUtils.computeFloat8(f, f2);
                              }

                              phobosClient.getRotationManager().testFloat2(f3, SprintModule.this.computeClientPlayerEntity(clientPlayerEntity), true);
                           }

                           SprintModule.this.intValue++;
                           SprintModule.this.intValue2 = 0;
                           return;
                        case Correction:
                           if (SprintModule.this.vec3d != null) {
                              if (clientPlayerEntity.horizontalCollision) {
                                 SprintModule.this.flag8 = false;
                                 SprintModule.this.vec3d = null;
                                 return;
                              }

                              if (SprintModule.this.flag8) {
                                 if (SprintModule.this.intValue > 0 || SprintModule.this.intValue2 <= 1) {
                                    SprintModule.this.flag8 = false;
                                    SprintModule.this.flag3 = false;
                                    SprintModule.this.flag7 = false;
                                    SprintModule.this.flag2 = false;
                                    SprintModule.this.flag9 = false;
                                    SprintModule.this.intValue2 = 0;
                                    return;
                                 }

                                 float f6 = RotationUtils.values2(clientPlayerEntity, SprintModule.this.vec3d)[0];
                                 if (SprintModule.this.randomize.getValue()) {
                                    float f4 = -SprintModule.this.amount.getValue() - MathUtils.computeFloat8(-0.0085671F, 0.0085217F);
                                    float f5 = SprintModule.this.amount.getValue() + MathUtils.computeFloat8(-0.0085822F, 0.0085156F);
                                    f6 += MathUtils.computeFloat8(f4, f5);
                                 }

                                 phobosClient.getRotationManager().testFloat2(f6, SprintModule.this.computeClientPlayerEntity(clientPlayerEntity), true);
                              }

                              SprintModule.this.intValue++;
                              SprintModule.this.intValue2 = 0;
                              return;
                           }
                           break;
                        case Counter:
                           if (SprintModule.this.flag8) {
                              if (clientPlayerEntity.groundCollision) {
                                 PlayerActionUtils.handleClientPlayerEntity13(clientPlayerEntity, 0.0, 0.0);
                              }

                              SprintModule.this.flag8 = false;
                           }

                           return;
                     }
                  }

                  float f10 = SprintModule.this.computeFloat(
                        f9, SprintModule.this.flag5, SprintModule.this.flag4, SprintModule.this.flag6, SprintModule.this.flag
                     )
                     + (SprintModule.this.testClientPlayerEntity(clientPlayerEntity) ? 45.0F : 0.0F);
                  if (f10 == f9 && !SprintModule.this.stop.testObject2(SprintModule.Stop.Vanilla) && !SprintModule.this.pitch.getValue()) {
                     SprintModule.this.flag3 = SprintModule.this.flag5;
                     SprintModule.this.flag7 = SprintModule.this.flag4;
                     SprintModule.this.flag2 = SprintModule.this.flag6;
                     SprintModule.this.flag9 = SprintModule.this.flag;
                     SprintModule.this.intValue = 0;
                     SprintModule.this.intValue2++;
                     SprintModule.this.flag8 = true;
                     SprintModule.this.vec3d = clientPlayerEntity.getPos();
                     return;
                  }

                  if (SprintModule.this.randomize.getValue()) {
                     float f7 = -SprintModule.this.amount.getValue() - MathUtils.computeFloat8(-0.0085671F, 0.0085217F);
                     float f8 = SprintModule.this.amount.getValue() + MathUtils.computeFloat8(-0.0085822F, 0.0085156F);
                     f10 += MathUtils.computeFloat8(f7, f8);
                  }

                  SprintModule.this.flag3 = SprintModule.this.flag5;
                  SprintModule.this.flag7 = SprintModule.this.flag4;
                  SprintModule.this.flag2 = SprintModule.this.flag6;
                  SprintModule.this.flag9 = SprintModule.this.flag;
                  SprintModule.this.intValue = 0;
                  SprintModule.this.flag8 = phobosClient.getRotationManager()
                     .testFloat2(f10, SprintModule.this.computeClientPlayerEntity(clientPlayerEntity), true);
                  if (SprintModule.this.flag8) {
                     SprintModule.this.vec3d = clientPlayerEntity.getPos();
                     SprintModule.this.intValue2++;
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
               if (SprintModule.this.testClientPlayerEntity2(clientPlayerEntity)
                  && !tickEvent.isCancelled()
                  && SprintModule.this.flag8
                  && SprintModule.this.mode.getValue().equals(SprintModule.Mode.Directional)) {
                  if (CriticalsModule.check() && !PlayerUtils.check()) {
                     return;
                  }

                  clientPlayerEntity.input.movementForward = 1.0F;
                  clientPlayerEntity.input.movementSideways = SprintModule.this.testClientPlayerEntity(clientPlayerEntity) ? 1.0F : 0.0F;
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickMovementEvent>() {
            public void handleObject(
               TickMovementEvent tickMovementEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (SprintModule.this.mode.testObject2(SprintModule.Mode.Legit)) {
                  tickMovementEvent.setFlag(true);
               }
            }
         }
      );
      sprintModule = this;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   private enum Mode implements TextProvider {
      Legit("Holds sprint key for you"),
      Always("Makes you sprint forward"),
      Rage("Sprints if you're moving"),
      Directional("Makes you sprint in any direction");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Stop {
      Vanilla("No fast-stop"),
      Counter("Saves last movement keys"),
      Correction("Moves towards last movement position"),
      Grim("Grim quick-stop");

      public final String description;

      public String getDescription() {
         return this.description;
      }

      Stop(final String description) {
         this.description = description;
      }
   }
}
