package org.phobos.module.movement;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;
import org.earthhack.ducks.IVec3d;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.movement.MovementSimulator;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class PhaseWalkModule extends Module {
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting;
   public boolean flag;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;
   public double doubleValue;
   public boolean flag5;
   public int intValue;
   public int intValue2;
   public int intValue3;
   public boolean flag6;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Integer> integerSetting;

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      boolean var3 = this.flag4;
      if (!var3) {
         boolean var4 = this.flag2;
         if (!var4) {
            boolean var5 = this.testClientPlayerEntity2(clientPlayerEntity, clientWorld);
            if (var5) {
               MinecraftClient var6 = MinecraftHolder.mc;
               GameOptions var7 = var6.options;
               KeyBinding var8 = var7.sneakKey;
               boolean var9 = var8.isPressed();
               if (!var9) {
                  MinecraftClient var10 = MinecraftHolder.mc;
                  GameOptions var11 = var10.options;
                  KeyBinding var12 = var11.jumpKey;
                  boolean var13 = var12.isPressed();
                  if (var13) {
                     this.flag2 = true & true;
                     this.intValue3 = 0;
                     double var14 = clientPlayerEntity.getY();
                     this.doubleValue = var14;
                  }
               } else {
                  this.flag4 = true & true;
               }
            }
         }
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, MoveEvent2 moveEvent2) {
      Vec3d var4 = this.resolveVec3d2(clientPlayerEntity);
      Vec3d var5 = clientPlayerEntity.getPos();
      Vec3d var6 = moveEvent2.getVec3d();
      Vec3d var7 = var5.add(var6);
      Vec3d var8 = var4.subtract(var7);
      double var9 = var8.horizontalLength();
      Vec3d var11 = clientPlayerEntity.getPos();
      Vec3d var12 = var4.subtract(var11);
      double var13 = var12.horizontalLength();
      boolean var15 = BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld);
      long var16;
      if (!var15) {
         var16 = 1L;
      } else {
         byte var21;
         label67: {
            boolean var18 = clientPlayerEntity.isOnGround();
            if (var18) {
               byte var19;
               label71: {
                  byte var22;
                  if (!Double.isNaN(var9) && !Double.isNaN(var9) && !Double.isNaN(0.01) && !Double.isNaN(0.01) && !(var9 > 0.01)) {
                     if (!Double.isNaN(var9) && !Double.isNaN(0.01) && var9 == 0.01) {
                        var19 = 0;
                        break label71;
                     }

                     var22 = -1;
                  } else {
                     var22 = 1;
                  }

                  var19 = var22;
               }

               if ((int)(var19 & 4294967295L) > 0) {
                  byte var20;
                  label49: {
                     if (!Double.isNaN(var13) && !Double.isNaN(var13) && !Double.isNaN(0.05) && !Double.isNaN(0.05)) {
                        if (var13 > 0.05) {
                           var20 = 1;
                           break label49;
                        }

                        if (!Double.isNaN(var13) && !Double.isNaN(0.05) && var13 == 0.05) {
                           var20 = 0;
                           break label49;
                        }
                     }

                     var20 = -1;
                  }

                  if ((int)(var20 & 4294967295L) < 0) {
                     var21 = 0;
                     break label67;
                  }
               }
            }

            var21 = 1;
         }

         var16 = var21 & 4294967295L;
      }

      return (((int)((int)var16 & 0xFF & 0xFF)) != 0);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, MoveEvent2 moveEvent2) {
      Vec3d var3 = moveEvent2.getVec3d();
      double var4 = var3.horizontalLength();
      Vec3d var6 = moveEvent2.getVec3d();
      double var7 = var6.x;
      Vec3d var9 = moveEvent2.getVec3d();
      double var10 = var9.z;
      Vec3d var12 = clientPlayerEntity.getPos();
      Vec3d var13 = moveEvent2.getVec3d();
      double var14 = var13.y;
      Vec3d var16 = var12.add(var7 / (var4 / 0.06), var14, var10 / (var4 / 0.06));
      Axis var17 = Axis.Y;
      double var18 = clientPlayerEntity.getY();
      Vec3d var20 = var16.withAxis(var17, var18);
      this.handleClientPlayerEntity6(clientPlayerEntity, var20);
      clientPlayerEntity.setPosition(var20);
      moveEvent2.setDoubleValue3(0.0);
      moveEvent2.setDoubleValue2(0.0);
   }

   public boolean alwaysFalse() {
      return false;
   }

   public Vec3d resolveVec3d(Entity entity, Vec3d vec3d) {
      Box var3;
      List var6;
      byte var9;
      label411: {
         var3 = entity.getBoundingBox();
         World var4 = entity.getWorld();
         Box var5 = var3.stretch(vec3d);
         var6 = var4.getEntityCollisions(entity, var5);
         double var7 = vec3d.lengthSquared();
         if (!Double.isNaN(var7) && !Double.isNaN(var7) && !Double.isNaN(0.0) && !Double.isNaN(0.0)) {
            if (var7 > 0.0) {
               var9 = 1;
               break label411;
            }

            if (!Double.isNaN(var7) && !Double.isNaN(0.0) && var7 == 0.0) {
               var9 = 0;
               break label411;
            }
         }

         var9 = -1;
      }

      Vec3d var12;
      if ((int)(var9 & 4294967295L) != 0) {
         World var10 = entity.getWorld();
         Vec3d var11 = Entity.adjustMovementForCollisions(entity, vec3d, var3, var10, var6);
         var12 = var11;
      } else {
         var12 = vec3d;
      }

      byte var39;
      label428: {
         double var19 = vec3d.getX();
         double var30 = var12.getX();
         if (!Double.isNaN(var19) && !Double.isNaN(var19)) {
            if (!Double.isNaN(var30) && !Double.isNaN(var30)) {
               if (var19 > var30) {
                  var39 = 1;
                  break label428;
               }

               if (!Double.isNaN(var19) && !Double.isNaN(var30)) {
                  if (var19 == var30) {
                     var39 = 0;
                     break label428;
                  }
               }
            }
         }

         var39 = -1;
      }

      boolean var47;
      if ((int)(var39 & 4294967295L) == 0) {
         var47 = false;
      } else {
         var47 = true;
      }

      byte var77;
      label429: {
         double var55 = vec3d.getY();
         double var66 = var12.getY();
         if (!Double.isNaN(var55) && !Double.isNaN(var55)) {
            if (!Double.isNaN(var66) && !Double.isNaN(var66)) {
               if (var55 > var66) {
                  var77 = 1;
                  break label429;
               }

               if (!Double.isNaN(var55) && !Double.isNaN(var66)) {
                  if (var55 == var66) {
                     var77 = 0;
                     break label429;
                  }
               }
            }
         }

         var77 = -1;
      }

      byte var86;
      if ((int)(var77 & 4294967295L) == 0) {
         var86 = 0;
      } else {
         var86 = 1;
      }

      byte var116;
      label430: {
         double var94 = vec3d.getZ();
         double var105 = var12.getZ();
         if (!Double.isNaN(var94) && !Double.isNaN(var94)) {
            if (!Double.isNaN(var105) && !Double.isNaN(var105)) {
               if (var94 > var105) {
                  var116 = 1;
                  break label430;
               }

               if (!Double.isNaN(var94) && !Double.isNaN(var105)) {
                  if (var94 == var105) {
                     var116 = 0;
                     break label430;
                  }
               }
            }
         }

         var116 = -1;
      }

      byte var125;
      if ((int)(var116 & 4294967295L) == 0) {
         var125 = 0;
      } else {
         var125 = 1;
      }

      byte var145;
      label426: {
         boolean var132 = entity.isOnGround();
         label316:
         if (!var132) {
            if ((int)((int)(var86 & 4294967295L) & 4294967295L) != 0) {
               byte var186;
               label431: {
                  double var167 = vec3d.getY();
                  if (!Double.isNaN(var167) && !Double.isNaN(var167)) {
                     if (!Double.isNaN(0.0)) {
                        if (!(var167 > 0.0)) {
                           if (!Double.isNaN(var167) && !Double.isNaN(0.0)) {
                              if (var167 == 0.0) {
                                 var186 = 0;
                                 break label431;
                              }
                           }

                           var186 = -1;
                           break label431;
                        }
                     }
                  }

                  var186 = 1;
               }

               if ((int)(var186 & 4294967295L) < 0) {
                  break label316;
               }
            }

            var145 = 0;
            break label426;
         }

         var145 = 1;
      }

      byte var179;
      label433: {
         float var152 = entity.getStepHeight();
         if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var152))) && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var152)))) {
            if (!Float.isNaN(0.0F) && !Float.isNaN(0.0F)) {
               if (Float.intBitsToFloat(Float.floatToRawIntBits(var152)) > 0.0F) {
                  var179 = 1;
                  break label433;
               }

               if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var152))) && !Float.isNaN(0.0F)) {
                  if (Float.intBitsToFloat(Float.floatToRawIntBits(var152)) == 0.0F) {
                     var179 = 0;
                     break label433;
                  }
               }
            }
         }

         var179 = -1;
      }

      if ((int)(var179 & 4294967295L) > 0) {
         if ((int)((int)(var145 & 4294967295L) & 4294967295L) != 0) {
            if (!var47) {
               if ((int)(var125 & 4294967295L) == 0) {
                  return var12;
               }
            }

            Vec3d var252;
            Vec3d var297;
            byte var319;
            label282: {
               double var220 = vec3d.getX();
               float var231 = entity.getStepHeight();
               double var237 = vec3d.getZ();
               Vec3d var207 = new Vec3d(var220, Float.intBitsToFloat(Float.floatToRawIntBits(var231)), var237);
               World var248 = entity.getWorld();
               var252 = Entity.adjustMovementForCollisions(entity, var207, var3, var248, var6);
               float var263 = entity.getStepHeight();
               Vec3d var256 = new Vec3d(0.0, Float.intBitsToFloat(Float.floatToRawIntBits(var263)), 0.0);
               double var274 = vec3d.getX();
               double var281 = vec3d.getZ();
               Box var287 = var3.stretch(var274, 0.0, var281);
               World var293 = entity.getWorld();
               var297 = Entity.adjustMovementForCollisions(entity, var256, var287, var293, var6);
               double var303 = var297.getY();
               float var312 = entity.getStepHeight();
               if (!Double.isNaN(var303)) {
                  if (!Double.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var312)))) {
                     if (!(var303 > Float.intBitsToFloat(Float.floatToRawIntBits(var312)))) {
                        if (!Double.isNaN(var303) && !Double.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var312)))) {
                           if (var303 == Float.intBitsToFloat(Float.floatToRawIntBits(var312))) {
                              var319 = 0;
                              break label282;
                           }
                        }

                        var319 = -1;
                        break label282;
                     }
                  }
               }

               var319 = 1;
            }

            Vec3d var321;
            if (var319 >= 0) {
               var321 = var252;
            } else {
               Vec3d var366;
               byte var404;
               label266: {
                  double var331 = vec3d.getX();
                  double var338 = vec3d.getZ();
                  Vec3d var324 = new Vec3d(var331, 0.0, var338);
                  Box var350 = var3.offset(var297);
                  World var356 = entity.getWorld();
                  Vec3d var360 = Entity.adjustMovementForCollisions(entity, var324, var350, var356, var6);
                  var366 = var360.add(var297);
                  double var372 = var366.horizontalLengthSquared();
                  double var379 = var252.horizontalLengthSquared();
                  if (!Double.isNaN(var372)) {
                     if (!Double.isNaN(var379)) {
                        if (var372 > var379) {
                           var404 = 1;
                           break label266;
                        }

                        if (!Double.isNaN(var372) && !Double.isNaN(var379)) {
                           if (var372 == var379) {
                              var404 = 0;
                              break label266;
                           }
                        }
                     }
                  }

                  var404 = -1;
               }

               if (var404 > 0) {
                  var321 = var366;
               } else {
                  var321 = var252;
               }
            }

            byte var408;
            label435: {
               double var388 = var321.horizontalLengthSquared();
               double var395 = var12.horizontalLengthSquared();
               if (!Double.isNaN(var388) && !Double.isNaN(var388)) {
                  if (!Double.isNaN(var395)) {
                     if (var388 > var395) {
                        var408 = 1;
                        break label435;
                     }

                     if (!Double.isNaN(var388) && !Double.isNaN(var395)) {
                        if (var388 == var395) {
                           var408 = 0;
                           break label435;
                        }
                     }
                  }
               }

               var408 = -1;
            }

            if ((int)(var408 & 4294967295L) > 0) {
               double var417 = var321.getY();
               double var419 = vec3d.getY();
               Vec3d var416 = new Vec3d(0.0, Double.longBitsToDouble(Double.doubleToRawLongBits(var417) ^ Long.MIN_VALUE) + var419, 0.0);
               Box var421 = var3.offset(var321);
               World var422 = entity.getWorld();
               Vec3d var423 = Entity.adjustMovementForCollisions(entity, var416, var421, var422, var6);
               return var321.add(var423);
            }
         }
      }

      return var12;
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, MoveEvent2 moveEvent2) {
      byte var12;
      label31: {
         int var3 = this.intValue3;
         double var4 = Math.min(var3 * 0.06, 1.0);
         Vec3d var6 = clientPlayerEntity.getPos();
         Axis var7 = Axis.Y;
         double var8 = this.doubleValue;
         Vec3d var10 = var6.withAxis(var7, var8 + var4);
         this.handleClientPlayerEntity6(clientPlayerEntity, var10);
         clientPlayerEntity.setPosition(var10);
         moveEvent2.setDoubleValue3(0.0);
         moveEvent2.setDoubleValue(0.0);
         moveEvent2.setDoubleValue2(0.0);
         int var11 = this.intValue3++;
         if (!Double.isNaN(var4) && !Double.isNaN(var4) && !Double.isNaN(1.0) && !Double.isNaN(1.0)) {
            if (var4 > 1.0) {
               var12 = 1;
               break label31;
            }

            if (!Double.isNaN(var4) && !Double.isNaN(1.0) && var4 == 1.0) {
               var12 = 0;
               break label31;
            }
         }

         var12 = -1;
      }

      if ((int)(var12 & 4294967295L) >= 0) {
         this.flag2 = false & true;
      }
   }

   public void handleVec3d(Vec3d vec3d, Vec3d vec3d2) {
      ((IVec3d)(Object)vec3d).set(vec3d2.getX(), vec3d2.getY(), vec3d2.getZ());
   }

   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, MoveEvent2 moveEvent2) {
      int var4 = this.intValue2++;
      boolean var5 = BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld);
      if (!var5) {
         boolean var6 = this.flag6;
         if (var6) {
            boolean var7 = this.flag5;
            if (var7) {
               Setting var8 = this.booleanValueSetting;
               Object var9 = var8.getValue();
               boolean var10 = var9 instanceof Boolean;
               if (var9 != null && !var10) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var15 = (Boolean)var9;
               if (var15) {
                  this.intValue = 0;
                  PhobosClient var16 = this.phobosClient;
                  MovementSimulator var17 = var16.getMovementSimulator();
                  PlayerEntity var18 = var17.resolvePlayerEntity(clientPlayerEntity);
                  ClientPlayNetworkHandler var19 = clientPlayerEntity.networkHandler;
                  double var21 = var18.getX();
                  double var23 = var18.getY();
                  double var25 = var18.getZ();
                  float var27 = var18.prevYaw;
                  float var28 = var18.prevPitch;
                  Full var20 = new Full(
                     var21,
                     var23 + 1.0,
                     var25,
                     (float)Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var27)) + 5.0F)),
                     (float)Float.intBitsToFloat(Float.floatToRawIntBits(var28)),
                     false & true
                  );
                  var19.sendPacket(var20);
               }
            }
         }

         this.flag5 = false & true;
      }

      this.flag6 = var5;
      boolean var11 = this.testClientPlayerEntity3(clientPlayerEntity, clientWorld, moveEvent2);
      if (!var11) {
         Setting var12 = this.booleanValueSetting5;
         Object var13 = var12.getValue();
         boolean var14 = var13 instanceof Boolean;
         if (var13 != null && !var14) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var29 = (Boolean)var13;
         if (var29) {
            int var30 = this.intValue2;
            Setting var31 = this.integerSetting;
            Object var32 = var31.getValue();
            boolean var33 = var32 instanceof Integer;
            if (var32 != null && !var33) {
               throw new ClassCastException("CHECKCAST failed");
            }

            int var34 = (Integer)var32;
            if (var30 < var34) {
               Setting var35 = this.booleanValueSetting6;
               Object var36 = var35.getValue();
               boolean var37 = var36 instanceof Boolean;
               if (var36 != null && !var37) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var38 = (Boolean)var36;
               if (var38) {
                  Vec3d var40 = clientPlayerEntity.getVelocity();
                  double var41 = var40.y;
                  Vec3d var39 = new Vec3d(0.0, var41, 0.0);
                  clientPlayerEntity.setVelocity(var39);
                  moveEvent2.setDoubleValue3(0.0);
                  moveEvent2.setDoubleValue2(0.0);
               }

               return;
            }
         }

         this.handleClientPlayerEntity7(clientPlayerEntity, moveEvent2);
      }
   }

   public PhaseWalkModule(PhobosClient phobosClient) {
      super(phobosClient, "PhaseWalk", Categories.movement, "Strict NCP Phase");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      BooleanSetting var2 = new BooleanSetting("Stop");
      Setting var3 = var2.resolveSetting7("Stops rotation");
      Setting var4 = this.resolveSetting2(var3);
      this.booleanValueSetting5 = var4;
      Integer var6 = 1;
      Integer var7 = 0;
      Integer var8 = 20;
      NumberSetting var5 = new NumberSetting<>("Ticks", var6, var7, var8);
      Setting var9 = this.resolveSetting2(var5);
      this.integerSetting = var9;
      Boolean var11 = true & true;
      BooleanSetting var10 = new BooleanSetting("Sync", var11);
      Setting var12 = this.resolveSetting2(var10);
      this.booleanValueSetting3 = var12;
      Boolean var14 = true & true;
      BooleanSetting var13 = new BooleanSetting("Wait", var14);
      Setting var15 = this.resolveSetting2(var13);
      this.booleanValueSetting6 = var15;
      Boolean var17 = true & true;
      BooleanSetting var16 = new BooleanSetting("Resync", var17);
      Setting var18 = this.resolveSetting2(var16);
      this.booleanValueSetting = var18;
      BooleanSetting var19 = new BooleanSetting("Inverted");
      Setting var20 = var19.resolveSetting7("Inverts yaw");
      Setting var21 = this.resolveSetting2(var20);
      this.booleanValueSetting4 = var21;
      BooleanSetting var22 = new BooleanSetting("Timer");
      Setting var23 = this.resolveSetting2(var22);
      this.booleanValueSetting2 = var23;
      Float var25 = (float)1.5F;
      Float var26 = (float)0.0F;
      Float var27 = (float)10.0F;
      NumberSetting var24 = new NumberSetting<>("TimerSpeed", var25, var26, var27);
      Setting var28 = this.resolveSetting2(var24);
      this.floatValueSetting = var28;
      this.intValue2 = 0;
      this.flag3 = false & true;
      this.flag = false & true;
      this.flag4 = false & true;
      this.flag2 = false & true;
      this.doubleValue = 0.0;
      this.intValue3 = 0;
      this.intValue = 0;
      this.flag6 = false & true;
      this.flag5 = false & true;
      Setting var29 = this.booleanValueSetting2;
      Setting[] var30 = new Setting[1];
      Setting var31 = this.floatValueSetting;
      var30[0] = var31;
      this.resolveSettingGroup(var29, var30);
      var var32 = new SafeListener<PostSend<?>>() {
         public void handleObject(
            PostSend<?> postSend, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (postSend.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
               if (playerMoveC2SPacket.changesLook() && !PhaseWalkModule.this.flag3) {
                  PhaseWalkModule.this.intValue2 = 0;
               }
            }
         }
      };
      this.handleListener(var32);
      var var33 = new SafeListener<Receive<?>>() {
         public void handleObject(
            Receive<?> receive, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PhaseWalkModule.this.flag2 = false;
            PhaseWalkModule.this.flag4 = false;
         }
      };
      this.handleListener(var33);
      var var34 = new SafeListener<MoveEvent2>() {
         public void handleObject(
            MoveEvent2 moveEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            PhaseWalkModule.this.handleClientPlayerEntity4(clientPlayerEntity, clientWorld, moveEvent2);
            if (PhaseWalkModule.this.booleanValueSetting2.getValue()) {
               PhaseWalkModule.this.intValue--;
               if (PhaseWalkModule.this.intValue >= 0) {
                  PhaseWalkModule.this.flag = true;
                  var1.getTimerManager().handleFloat(PhaseWalkModule.this.floatValueSetting.getValue(), TimerPriority.Normal);
                  return;
               }
            }

            if (PhaseWalkModule.this.flag) {
               PhaseWalkModule.this.flag = false;
               var1.getTimerManager().run2();
            }
         }
      };
      this.handleListener(var34);
   }

   public void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity, Vec3d vec3d, float f, float f2) {
      float var5;
      byte var6;
      label33: {
         var5 = MathHelper.wrapDegrees((float)Float.intBitsToFloat(Float.floatToRawIntBits(f)));
         if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var5)))
            && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var5)))
            && !Float.isNaN(0.0F)
            && !Float.isNaN(0.0F)) {
            if (Float.intBitsToFloat(Float.floatToRawIntBits(var5)) > 0.0F) {
               var6 = 1;
               break label33;
            }

            if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var5)))
               && !Float.isNaN(0.0F)
               && Float.intBitsToFloat(Float.floatToRawIntBits(var5)) == 0.0F) {
               var6 = 0;
               break label33;
            }
         }

         var6 = -1;
      }

      long var7;
      if ((int)(var6 & 4294967295L) >= 0) {
         var7 = Float.floatToRawIntBits(-180.0F - Float.intBitsToFloat(Float.floatToRawIntBits(180.0F - Float.intBitsToFloat(Float.floatToRawIntBits(var5)))))
            & 4294967295L;
      } else {
         var7 = Float.floatToRawIntBits(var5) & 4294967295L;
      }

      ClientPlayNetworkHandler var9 = clientPlayerEntity.networkHandler;
      double var11 = vec3d.getX();
      double var13 = vec3d.getY();
      double var15 = vec3d.getZ();
      Full var10 = new Full(
         var11,
         var13,
         var15,
         (float)Float.intBitsToFloat((int)((int)var7 & 4294967295L)),
         (float)Float.intBitsToFloat(Float.floatToRawIntBits(f2)),
         false & true
      );
      var9.sendPacket(var10);
   }

   public void handleClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity, Vec3d vec3d) {
      this.flag5 = true & true;
      this.intValue = 5;
      PhobosClient var3 = this.phobosClient;
      MovementSimulator var4 = var3.getMovementSimulator();
      PlayerEntity var5 = var4.resolvePlayerEntity(clientPlayerEntity);
      ClientPlayNetworkHandler var6 = clientPlayerEntity.networkHandler;
      double var8 = vec3d.x;
      double var10 = vec3d.y;
      double var12 = vec3d.z;
      boolean var14 = var5.isOnGround();
      PositionAndOnGround var7 = new PositionAndOnGround(var8, var10, var12, var14);
      var6.sendPacket(var7);
      ClientPlayNetworkHandler var15 = clientPlayerEntity.networkHandler;
      double var17 = vec3d.x;
      double var19 = vec3d.y;
      double var21 = vec3d.z;
      boolean var23 = var5.isOnGround();
      PositionAndOnGround var16 = new PositionAndOnGround(var17, var19 - 87.0, var21, var23);
      var15.sendPacket(var16);
      ClientPlayNetworkHandler var24 = clientPlayerEntity.networkHandler;
      TeleportConfirmC2SPacket var25 = this.createTeleportConfirmC2SPacket();
      var24.sendPacket(var25);
      this.flag3 = true & true;
      Setting var26 = this.booleanValueSetting3;
      Object var27 = var26.getValue();
      boolean var28 = var27 instanceof Boolean;
      if (var27 != null && !var28) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var29 = (Boolean)var27;
      if (var29) {
         float var30 = var5.prevYaw;
         float var31 = var5.prevPitch;
         this.handleClientPlayerEntity5(
            clientPlayerEntity, vec3d, (float)Float.intBitsToFloat(Float.floatToRawIntBits(var30)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var31))
         );
      }

      this.flag3 = false & true;
   }

   public Vec3d resolveVec3d2(PlayerEntity playerEntity) {
      Vec3d vec3d = new Vec3d(playerEntity.getVelocity().getX(), playerEntity.getVelocity().getY(), playerEntity.getVelocity().getZ());
      this.handleVec3d2(vec3d, playerEntity);
      return playerEntity.getPos().add(vec3d);
   }

   public void handleClientPlayerEntity7(ClientPlayerEntity clientPlayerEntity, MoveEvent2 moveEvent2) {
      boolean var3 = this.flag4;
      int var4;
      if (var3) {
         var4 = 7263792;
      } else {
         boolean var5 = this.flag2;
         if (var5) {
            var4 = 7263776;
         } else {
            var4 = 7263760;
         }
      }

      switch (var4) {
         case 7263760:
         default:
            this.handleClientPlayerEntity2(clientPlayerEntity, moveEvent2);
            break;
         case 7263776:
            this.handleClientPlayerEntity3(clientPlayerEntity, moveEvent2);
            break;
         case 7263792:
            this.handleClientPlayerEntity8(clientPlayerEntity, moveEvent2);
      }
   }

   public void handleClientPlayerEntity8(ClientPlayerEntity clientPlayerEntity, MoveEvent2 moveEvent2) {
      Vec3d var3 = clientPlayerEntity.getPos();
      Vec3d var4 = var3.add(0.0, -0.0253, 0.0);
      this.handleClientPlayerEntity6(clientPlayerEntity, var4);
      clientPlayerEntity.setPosition(var4);
      moveEvent2.setDoubleValue3(0.0);
      moveEvent2.setDoubleValue(0.0);
      moveEvent2.setDoubleValue2(0.0);
      this.flag4 = false & true;
   }

   public TeleportConfirmC2SPacket createTeleportConfirmC2SPacket() {
      return new TeleportConfirmC2SPacket(this.phobosClient.getMovementSimulator().getIntValue2() + 1);
   }

   public boolean alwaysFalse2() {
      return false;
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld) && clientPlayerEntity.isOnGround();
   }

   public boolean testClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, MoveEvent2 moveEvent2) {
      this.handleClientPlayerEntity(clientPlayerEntity, clientWorld);
      boolean var4 = this.flag4;
      if (!var4) {
         boolean var5 = this.flag2;
         if (!var5) {
            int var6 = (this.testClientPlayerEntity(clientPlayerEntity, clientWorld, moveEvent2)) ? 1 : 0;
            long var7 = (int)(var6 & 0xFF & 0xFF) & 4294967295L;
            return (((int)((int)var7 & 0xFF & 0xFF)) != 0);
         }
      }

      long var7 = 0L;
      return (((int)((int)var7 & 0xFF & 0xFF)) != 0);
   }

   @Override
   public void run4() {
      if (this.flag) {
         this.flag = false;
         this.phobosClient.getTimerManager().run2();
      }

      if (mc.player != null && mc.world != null && this.flag6 && this.booleanValueSetting.getValue()) {
         this.intValue = 0;
         PlayerEntity playerEntity = this.phobosClient.getMovementSimulator().resolvePlayerEntity(mc.player);
         mc.player
            .networkHandler
            .sendPacket(
               new Full(playerEntity.getX(), playerEntity.getY() + 1.0, playerEntity.getZ(), playerEntity.prevYaw + 5.0F, playerEntity.prevPitch, false)
            );
      }
   }

   public void handleVec3d2(Vec3d vec3d, PlayerEntity playerEntity) {
      this.handleVec3d(vec3d, playerEntity.adjustMovementForSneaking(vec3d, MovementType.SELF));
      this.handleVec3d(vec3d, this.resolveVec3d(playerEntity, vec3d));
   }
}
