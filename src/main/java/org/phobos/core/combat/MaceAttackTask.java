package org.phobos.core.combat;

import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate.Normal;
import org.phobos.event.TickEvent;
import org.phobos.mixin.game.entity.IFireworkRocketEntity;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.combat.AutoMaceModule;
import org.phobos.module.render.FreeLookModule;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.setting.Setting;

public class MaceAttackTask {
   public final AutoMaceModule autoMaceModule;
   public final Normal normal;
   public boolean flag;
   public LivingEntity livingEntity;
   public MaceAttackTask.Stage stage;
   public final ElytraManager.FlyConfiguration.Factory factory = ElytraManager.FlyConfiguration.createFactory()
      .getFactory()
      .getFactory4()
      .resolveFactory6(0L)
      .getFactory2()
      .resolveFactory8(4);
   public final Normal normal2;

   public MaceAttackTask(AutoMaceModule autoMaceModule) {
      this.normal = new Normal();
      this.normal2 = new Normal();
      this.flag = false;
      this.stage = MaceAttackTask.Stage.None;
      this.autoMaceModule = autoMaceModule;
   }

   public void handleTickEvent(TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity) {
      switch (this.stage) {
         case Ascent:
         case AscentAvoid:
            tickEvent.getKeyboardInput().jumping = true;
      }
   }

   public void run() {
      this.stage = MaceAttackTask.Stage.None;
      this.livingEntity = null;
   }

   @Nullable
   public String describe() {
      if (this.livingEntity == null) {
         return null;
      } else if (this.livingEntity instanceof PlayerEntity playerEntity) {
         return playerEntity.getNameForScoreboard();
      } else {
         EntityType<?> entityType = this.livingEntity.getType();
         String text = entityType.getTranslationKey();
         return Text.translatable(text).getString();
      }
   }

   public float[] values(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, float[] floats) {
      MutableObject<Vec3d> mutableObject = new MutableObject<>(null);
      Vec3d vec3d2 = clientPlayerEntity.getPos();
      double d2 = MathHelper.square(AuraModule.getAuraModule().getDoubleValueSetting().getValue());
      this.autoMaceModule
         .handleClientPlayerEntity5(
            clientPlayerEntity,
            clientWorld,
            vec3d2,
            floats[0],
            floats[1],
            20.0F,
            10.0F,
            this.autoMaceModule.doubleValueSetting2.getValue(),
            (blockHitResult, vec3d2x) -> {
               Vec3d vec3d3x = Objects.requireNonNullElse(blockHitResult.getPos(), blockHitResult.getBlockPos().toCenterPos());
               double d2x = entity.getBoundingBox().squaredMagnitude(vec3d3x.add(0.0, clientPlayerEntity.getEyeHeight(EntityPose.STANDING), 0.0));
               if (d2x > d2) {
                  return false;
               }

               if (!this.autoMaceModule.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d2, vec3d3x, EntityPose.STANDING)) {
                  return false;
               }

               mutableObject.setValue(vec3d3x);
               return true;
            }
         );
      Vec3d vec3d3 = mutableObject.getValue();
      if (vec3d3 == null) {
         double d = clientPlayerEntity.getPos().distanceTo(entity.getPos());
         this.autoMaceModule.handleClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d2, floats[0], floats[1], d, (blockHitResult, vec3d2x) -> {
            Vec3d vec3d3x = Objects.requireNonNullElse(blockHitResult.getPos(), blockHitResult.getBlockPos().toCenterPos());
            double d2x = entity.getBoundingBox().squaredMagnitude(vec3d3x.add(0.0, clientPlayerEntity.getEyeHeight(EntityPose.STANDING), 0.0));
            if (!(d2x > d2)) {
               if (this.autoMaceModule.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d2, vec3d3x, EntityPose.STANDING)) {
                  mutableObject.setValue(vec3d3x);
               }
            }
         });
         Vec3d vec3d = mutableObject.getValue();
         return vec3d != null ? RotationUtils.values2(clientPlayerEntity, vec3d) : new float[0];
      } else {
         return RotationUtils.values2(clientPlayerEntity, vec3d3);
      }
   }

   public void handleEntity(@NotNull Entity entity) {
      if (entity.equals(this.livingEntity) && this.stage == MaceAttackTask.Stage.Descend) {
         if (!this.flag) {
            this.normal2.run();
         }

         this.flag = true;
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, float[] floats) {
      Vec3d vec3d = clientPlayerEntity.getPos();
      MutableBoolean mutableBoolean = new MutableBoolean(false);
      this.autoMaceModule
         .handleClientPlayerEntity(
            clientPlayerEntity, clientWorld, vec3d, floats[0], floats[1], vec3d.distanceTo(entity.getPos()), (blockHitResult, vec3d2) -> {
               Vec3d vec3d3 = Objects.requireNonNullElse(blockHitResult.getPos(), blockHitResult.getBlockPos().toCenterPos());
               if (this.autoMaceModule.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d, vec3d3, EntityPose.STANDING)) {
                  mutableBoolean.setTrue();
               }
            }
         );
      return mutableBoolean.getValue();
   }

   public float[] values2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, double d, float[] floats, int i) {
      MutableObject<float[]> mutableObject = new MutableObject<>(new float[0]);
      Vec3d vec3d = clientPlayerEntity.getPos();
      this.autoMaceModule
         .handleClientPlayerEntity5(clientPlayerEntity, clientWorld, vec3d, floats[0], -80.0F, 30.0F, 30.0F, i * 1.5, (blockHitResult, vec3d2) -> {
            Vec3d vec3d3 = blockHitResult.getPos();
            if (vec3d3 == null) {
               return false;
            }

            if (!(d > vec3d3.getY()) && !(vec3d3.getY() - d < i)) {
               if (!this.autoMaceModule.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d, vec3d3, EntityPose.STANDING)) {
                  return false;
               }

               mutableObject.setValue(RotationUtils.values2(clientPlayerEntity, vec3d3));
               return true;
            } else {
               return false;
            }
         });
      return mutableObject.getValue();
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.normal.testLong(1000L)) {
         if (!PlayerActionUtils.testClientPlayerEntity8(clientPlayerEntity)) {
            return;
         }

         for (Entity entity : clientWorld.getEntities()) {
            if (entity instanceof FireworkRocketEntity fireworkRocketEntity) {
               IFireworkRocketEntity iFireworkRocketEntity = (IFireworkRocketEntity)(Object)fireworkRocketEntity;
               if (iFireworkRocketEntity.getShooter() != null && clientPlayerEntity.equals(iFireworkRocketEntity.getShooter())) {
                  return;
               }
            }
         }

         this.autoMaceModule.getPhobosClient().getElytraManager().handleInt(0);
         this.normal.run();
      }
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager, LivingEntity livingEntity
   ) {
      this.livingEntity = livingEntity;
      AutoMaceModule var5 = this.autoMaceModule;
      Entity var6 = var5.resolveEntity2(livingEntity);
      double var7 = clientPlayerEntity.getX();
      double var9 = clientPlayerEntity.getZ();
      double var11 = var6.getX();
      double var13 = var6.getZ();
      double var15 = MathUtils.computeDouble12(var7, var9, var11, var13);
      AutoMaceModule var17 = this.autoMaceModule;
      Setting var18 = var17.doubleValueSetting2;
      Object var19 = var18.getValue();
      boolean var20 = var19 instanceof Double;
      if (var19 != null && !var20) {
         throw new ClassCastException("CHECKCAST failed");
      }

      byte var23;
      label372: {
         double var21 = (Double)var19;
         if (!Double.isNaN(var15) && !Double.isNaN(var15) && !Double.isNaN(var21) && !Double.isNaN(var21)) {
            if (var15 > var21) {
               var23 = 1;
               break label372;
            }

            if (!Double.isNaN(var15) && !Double.isNaN(var21) && var15 == var21) {
               var23 = 0;
               break label372;
            }
         }

         var23 = -1;
      }

      if ((int)(var23 & 4294967295L) <= 0) {
         boolean var24 = this.flag;
         label357:
         if (var24) {
            int var25 = livingEntity.hurtTime;
            if (var25 <= 0) {
               Normal var26 = this.normal2;
               boolean var27 = var26.testLong(1000L);
               if (!var27) {
                  break label357;
               }
            }

            MaceAttackTask.Stage var28 = MaceAttackTask.Stage.Ascent;
            this.stage = var28;
            this.flag = false & true;
         }

         AutoMaceModule var29 = this.autoMaceModule;
         int var30 = var29.computeClientPlayerEntity2(clientPlayerEntity, livingEntity);
         int[] var31 = new int[AutoMaceModule.Movement.values().length];
         var31[AutoMaceModule.Movement.Vertical.ordinal()] = 1;
         var31[AutoMaceModule.Movement.Dynamic.ordinal()] = 2;
         AutoMaceModule var32 = this.autoMaceModule;
         Setting var33 = var32.movementSetting;
         Object var34 = var33.getValue();
         boolean var35 = var34 instanceof AutoMaceModule.Movement;
         if (var34 != null && !var35) {
            throw new ClassCastException("CHECKCAST failed");
         }

         int var37 = ((AutoMaceModule.Movement)var34).ordinal();
         int var38 = var31[var37];
         int var76;
         AuraModule var77;
         if (var38 + -1 != 0) {
            if (var38 + -1 != 1) {
               return;
            }

            float[] var39 = RotationUtils.values(clientPlayerEntity, var6);
            double var40 = var6.getY();
            float[] var42 = this.values2(clientPlayerEntity, clientWorld, var40, var39, var30);
            MaceAttackTask.Stage var43 = this.stage;
            int var44 = var43.ordinal();
            if (var44 == 0) {
               byte var63;
               label236: {
                  double var59 = clientPlayerEntity.getY();
                  double var61 = var6.getY();
                  if (!Double.isNaN(var59 - var61) && !Double.isNaN(var30)) {
                     if (var59 - var61 > var30) {
                        var63 = 1;
                        break label236;
                     }

                     if (!Double.isNaN(var59 - var61) && !Double.isNaN(var30) && var59 - var61 == var30) {
                        var63 = 0;
                        break label236;
                     }
                  }

                  var63 = -1;
               }

               if (var63 <= 0) {
                  int var64 = var42.length;
                  if (var64 != 0) {
                     MaceAttackTask.Stage var65 = MaceAttackTask.Stage.Ascent;
                     this.stage = var65;
                  } else {
                     MaceAttackTask.Stage var131 = MaceAttackTask.Stage.AscentAvoid;
                     this.stage = var131;
                  }

                  return;
               } else {
                  MaceAttackTask.Stage var130 = MaceAttackTask.Stage.Descend;
                  this.stage = var130;
                  return;
               }
            }

            if (var44 + -1 != 0) {
               if (var44 + -1 + -1 != 0) {
                  if (var44 + -1 + -1 + -1 != 0) {
                     if (var44 + -1 + -1 + -1 == 1) {
                        int var45 = var42.length;
                        if (var45 == 0) {
                           boolean var46 = this.testClientPlayerEntity(clientPlayerEntity, clientWorld, var6, var39);
                           if (var46) {
                              AutoMaceModule var47 = this.autoMaceModule;
                              PhobosClient var48 = var47.getPhobosClient();
                              RotationManager var49 = var48.getRotationManager();
                              float var50 = var39[0];
                              byte var51;
                              if (Double.isNaN(var15) || Double.isNaN(3.0) || var15 > 3.0) {
                                 var51 = 1;
                              } else if (!Double.isNaN(var15) && !Double.isNaN(3.0) && var15 == 3.0) {
                                 var51 = 0;
                              } else {
                                 var51 = -1;
                              }

                              int var52;
                              if (var51 >= 0) {
                                 var52 = 0;
                              } else {
                                 var52 = 1127481344;
                              }

                              byte var53;
                              label212: {
                                 if (!Double.isNaN(var15) && !Double.isNaN(6.0)) {
                                    if (var15 > 6.0) {
                                       var53 = 1;
                                       break label212;
                                    }

                                    if (!Double.isNaN(var15) && !Double.isNaN(6.0) && var15 == 6.0) {
                                       var53 = 0;
                                       break label212;
                                    }
                                 }

                                 var53 = -1;
                              }

                              long var54;
                              if (var53 <= 0) {
                                 var54 = 3262119936L;
                              } else {
                                 float var106 = var39[1];
                                 float var107 = Math.max((float)0.0F, (float)Float.intBitsToFloat(Float.floatToRawIntBits(var106)));
                                 var54 = Float.floatToRawIntBits(var107) & 4294967295L;
                              }

                              var49.testFloat(
                                 (float)Float.intBitsToFloat(
                                    (int)(
                                       (int)(
                                             Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var50)) + Float.intBitsToFloat(var52))
                                                & 4294967295L
                                          )
                                          & 4294967295L
                                    )
                                 ),
                                 (float)Float.intBitsToFloat((int)((int)var54 & 4294967295L))
                              );
                              this.run2();
                              this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
                           } else {
                              MaceAttackTask.Stage var105 = MaceAttackTask.Stage.None;
                              this.stage = var105;
                           }

                           return;
                        } else {
                           MaceAttackTask.Stage var104 = MaceAttackTask.Stage.Ascent;
                           this.stage = var104;
                           return;
                        }
                     }

                     return;
                  } else {
                     boolean var90 = this.testClientPlayerEntity(clientPlayerEntity, clientWorld, var6, var39);
                     if (var90) {
                        AutoMaceModule var91 = this.autoMaceModule;
                        PhobosClient var92 = var91.getPhobosClient();
                        RotationManager var93 = var92.getRotationManager();
                        float var94 = var39[0];
                        byte var95;
                        if (Double.isNaN(var15) || Double.isNaN(3.0) || var15 > 3.0) {
                           var95 = 1;
                        } else if (!Double.isNaN(var15) && !Double.isNaN(3.0) && var15 == 3.0) {
                           var95 = 0;
                        } else {
                           var95 = -1;
                        }

                        int var96;
                        if (var95 >= 0) {
                           var96 = 0;
                        } else {
                           var96 = 1127481344;
                        }

                        byte var97;
                        label246: {
                           if (!Double.isNaN(var15) && !Double.isNaN(6.0)) {
                              if (var15 > 6.0) {
                                 var97 = 1;
                                 break label246;
                              }

                              if (!Double.isNaN(var15) && !Double.isNaN(6.0) && var15 == 6.0) {
                                 var97 = 0;
                                 break label246;
                              }
                           }

                           var97 = -1;
                        }

                        long var98;
                        if (var97 <= 0) {
                           var98 = 1114636288L;
                        } else {
                           float var143 = var39[1];
                           float var144 = Math.min((float)0.0F, (float)Float.intBitsToFloat(Float.floatToRawIntBits(var143)));
                           var98 = Float.floatToRawIntBits(var144) & 4294967295L;
                        }

                        var93.testFloat(
                           (float)Float.intBitsToFloat(
                              (int)(
                                 (int)(
                                       Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var94)) + Float.intBitsToFloat(var96))
                                          & 4294967295L
                                    )
                                    & 4294967295L
                              )
                           ),
                           (float)Float.intBitsToFloat((int)((int)var98 & 4294967295L))
                        );
                        this.run2();
                        this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
                        AuraModule var100 = AuraModule.getAuraModule();
                        boolean var101 = var100.check7();
                        if (!var101) {
                           AuraModule var102 = AuraModule.getAuraModule();
                           var102.run12();
                        }

                        MaceAttackTask.Stage var103 = MaceAttackTask.Stage.Descend;
                        this.stage = var103;
                     } else {
                        MaceAttackTask.Stage var142 = MaceAttackTask.Stage.None;
                        this.stage = var142;
                     }

                     return;
                  }
               }

               int var78 = var42.length;
               if (var78 == 0) {
                  MaceAttackTask.Stage var133 = MaceAttackTask.Stage.None;
                  this.stage = var133;
                  return;
               }

               double var79 = clientPlayerEntity.getY();
               double var81 = var6.getY();
               byte var83;
               if (Double.isNaN(var79) || Double.isNaN(var81) || var79 > var81) {
                  var83 = 1;
               } else if (!Double.isNaN(var79) && !Double.isNaN(var81) && var79 == var81) {
                  var83 = 0;
               } else {
                  var83 = -1;
               }

               if (var83 > 0) {
                  double var84 = clientPlayerEntity.getY();
                  double var86 = var6.getY();
                  byte var88;
                  if (Double.isNaN(var84 - var86) || Double.isNaN(var30) || var84 - var86 > var30) {
                     var88 = 1;
                  } else if (!Double.isNaN(var84 - var86) && !Double.isNaN(var30) && var84 - var86 == var30) {
                     var88 = 0;
                  } else {
                     var88 = -1;
                  }

                  if (var88 >= 0) {
                     MaceAttackTask.Stage var89 = MaceAttackTask.Stage.Descend;
                     this.stage = var89;
                     return;
                  }
               }

               AutoMaceModule var134 = this.autoMaceModule;
               PhobosClient var135 = var134.getPhobosClient();
               RotationManager var136 = var135.getRotationManager();
               float var137 = var42[0];
               float var138 = var42[1];
               var136.testFloat((float)Float.intBitsToFloat(Float.floatToRawIntBits(var137)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var138)));
               this.run2();
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               AuraModule var139 = AuraModule.getAuraModule();
               boolean var140 = var139.check7();
               if (!var140) {
                  return;
               }

               AuraModule var141 = AuraModule.getAuraModule();
               var76 = 7225824;
               var77 = var141;
            } else {
               float[] var66 = this.values(clientPlayerEntity, clientWorld, var6, var39);
               int var67 = var66.length;
               if (var67 == 0) {
                  MaceAttackTask.Stage var132 = MaceAttackTask.Stage.None;
                  this.stage = var132;
                  return;
               }

               AutoMaceModule var68 = this.autoMaceModule;
               PhobosClient var69 = var68.getPhobosClient();
               RotationManager var70 = var69.getRotationManager();
               float var71 = var66[0];
               float var72 = var66[1];
               var70.testFloat((float)Float.intBitsToFloat(Float.floatToRawIntBits(var71)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var72)));
               this.run2();
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               AuraModule var73 = AuraModule.getAuraModule();
               boolean var74 = var73.check7();
               if (var74) {
                  return;
               }

               AuraModule var75 = AuraModule.getAuraModule();
               var76 = 7226064;
               var77 = var75;
            }
         } else {
            MaceAttackTask.Stage var56 = this.stage;
            int var57 = var56.ordinal();
            if (var57 == 0) {
               byte var112;
               label269: {
                  double var108 = clientPlayerEntity.getY();
                  double var110 = var6.getY();
                  if (!Double.isNaN(var108 - var110) && !Double.isNaN(var30)) {
                     if (var108 - var110 > var30) {
                        var112 = 1;
                        break label269;
                     }

                     if (!Double.isNaN(var108 - var110) && !Double.isNaN(var30) && var108 - var110 == var30) {
                        var112 = 0;
                        break label269;
                     }
                  }

                  var112 = -1;
               }

               if (var112 <= 0) {
                  MaceAttackTask.Stage var145 = MaceAttackTask.Stage.Ascent;
                  this.stage = var145;
               } else {
                  MaceAttackTask.Stage var154 = MaceAttackTask.Stage.Descend;
                  this.stage = var154;
               }

               return;
            }

            if (var57 + -1 != 0) {
               if (var57 + -1 + -1 != 0) {
                  if (var57 + -1 + -1 + -1 == 0 || var57 + -1 + -1 + -1 == 1) {
                     MaceAttackTask.Stage var58 = MaceAttackTask.Stage.None;
                     this.stage = var58;
                  }

                  return;
               }

               double var120 = clientPlayerEntity.getY();
               double var122 = var6.getY();
               byte var124;
               if (Double.isNaN(var120) || Double.isNaN(var122) || var120 > var122) {
                  var124 = 1;
               } else if (!Double.isNaN(var120) && !Double.isNaN(var122) && var120 == var122) {
                  var124 = 0;
               } else {
                  var124 = -1;
               }

               if (var124 > 0) {
                  double var125 = clientPlayerEntity.getY();
                  double var127 = var6.getY();
                  byte var129;
                  if (!Double.isNaN(var125 - var127)) {
                     if (!Double.isNaN(var30)) {
                        if (!(var125 - var127 > var30)) {
                           if (!Double.isNaN(var125 - var127) && !Double.isNaN(var30) && var125 - var127 == var30) {
                              var129 = 0;
                           } else {
                              var129 = -1;
                           }
                        } else {
                           var129 = 1;
                        }
                     } else {
                        var129 = 1;
                     }
                  } else {
                     var129 = 1;
                  }

                  if (var129 >= 0) {
                     MaceAttackTask.Stage var153 = MaceAttackTask.Stage.Descend;
                     this.stage = var153;
                     return;
                  }
               }

               AutoMaceModule var146 = this.autoMaceModule;
               PhobosClient var147 = var146.getPhobosClient();
               RotationManager var148 = var147.getRotationManager();
               float var149 = FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity);
               var148.testFloat((float)Float.intBitsToFloat(Float.floatToRawIntBits(var149)), (float)-90.0F);
               this.run2();
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               AuraModule var150 = AuraModule.getAuraModule();
               boolean var151 = var150.check7();
               if (!var151) {
                  return;
               }

               AuraModule var152 = AuraModule.getAuraModule();
               var76 = 7224704;
               var77 = var152;
            } else {
               AutoMaceModule var113 = this.autoMaceModule;
               PhobosClient var114 = var113.getPhobosClient();
               RotationManager var115 = var114.getRotationManager();
               float var116 = FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity);
               var115.testFloat((float)Float.intBitsToFloat(Float.floatToRawIntBits(var116)), (float)90.0F);
               this.run2();
               this.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               AuraModule var117 = AuraModule.getAuraModule();
               boolean var118 = var117.check7();
               if (var118) {
                  return;
               }

               AuraModule var119 = AuraModule.getAuraModule();
               var76 = 7224912;
               var77 = var119;
            }
         }

         switch (var76) {
            case 7224704:
            default:
               var77.run11();
               break;
            case 7224912:
               var77.run12();
               break;
            case 7225824:
               var77.run11();
               break;
            case 7226064:
               var77.run12();
         }
      } else {
         MaceAttackTask.Stage var36 = MaceAttackTask.Stage.None;
         this.stage = var36;
      }
   }

   public void run2() {
      this.autoMaceModule.getPhobosClient().getElytraManager().testFlyConfiguration2(this.factory.getFlyConfiguration2());
   }

   private enum Stage {
      None,
      Descend,
      Ascent,
      DescendAvoid,
      AscentAvoid;
   }
}
