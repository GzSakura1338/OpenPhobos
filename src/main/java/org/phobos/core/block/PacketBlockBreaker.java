package org.phobos.core.block;

import lombok.SneakyThrows;
import java.awt.Color;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.KeyValue;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.inventory.SilentHotbarManager.SelectionConfig;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.placement.DefaultDirectionResolver;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.rotation.LookDirection;
import org.phobos.core.math.MathUtils;
import org.phobos.core.movement.MovementSimulator;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.inventory.SilentHotbarManager;
import org.phobos.core.tick.TickUtils;

public class PacketBlockBreaker extends BlockBreaker {
   @NotNull
   public BlockState blockState = Blocks.AIR.getDefaultState();
   public boolean flag;
   public boolean flag2 = true;
   public final Normal normal;
   public float floatValue;
   public Box box;
   public boolean flag3;
   private static Object[] phobos$g1806ef188;
   private static Object[] phobos$g1806ef270;
   private static Object[] phobos$g1806ef998;
   private static Object[] phobos$g1806eefd0;

   public float computeFloat(float f) {
      float f2 = 0.0F;
      float f3 = this.speedmineModule.damage.getValue();
      return (f - f2) / (f3 - f2);
   }

   public void setBox(Box box) {
      this.box = box;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public Box getBox() {
      return this.box;
   }

   public void handleBlockState(@NotNull BlockState blockState) {
      if (blockState == null) {
         throw new NullPointerException("oldState is marked non-null but is null");
      }

      this.blockState = blockState;
   }

   @SneakyThrows public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      Direction direction,
      boolean flag
   ) {
      BlockState var6 = this.blockState2;
      boolean var7 = var6.isAir();
      if (!var7) {
         boolean var8 = this.flag3;
         if (var8) {
            ClientPlayNetworkHandler var9 = clientPlayerEntity.networkHandler;
            Action var11 = Action.START_DESTROY_BLOCK;
            BlockPos var12 = this.blockPos;
            PlayerActionC2SPacket var10 = new PlayerActionC2SPacket(var11, var12, direction);
            var9.sendPacket(var10);
            this.flag3 = false & true;
         }
      }

      BlockState var13 = this.blockState2;
      boolean var14 = var13.isAir();
      if (!var14) {
         BlockState var15 = this.blockState2;
         this.blockState = var15;
      }

      label101: {
         BlockPos var16 = this.blockPos;
         BlockState var17 = clientWorld.getBlockState(var16);
         this.blockState2 = var17;
         BlockState var18 = this.blockState;
         boolean var19 = var18.isAir();
         if (!var19) {
            BlockState var20 = this.blockState2;
            boolean var21 = var20.isAir();
            if (var21) {
               SpeedmineModule var22 = this.speedmineModule;
               Setting var23 = var22.always;
               Object var24 = var23.getValue();
               boolean var25 = var24 instanceof Boolean;
               if (var24 != null && !var25) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var43 = (Boolean)var24;
               if (var43) {
                  BlockState var44 = this.blockState;
                  BlockPos var45 = this.blockPos;
                  VoxelShape var46 = var44.getOutlineShape(clientWorld, var45);
                  boolean var47 = var46.isEmpty();
                  Box var48;
                  if (!var47) {
                     Box var75 = var46.getBoundingBox();
                     BlockPos var76 = this.blockPos;
                     Box var77 = var75.offset(var76);
                     var48 = var77;
                  } else {
                     var48 = null;
                  }

                  this.box = var48;
                  break label101;
               }
            }
         }

         BlockState var26 = this.blockState2;
         BlockPos var27 = this.blockPos;
         VoxelShape var28 = var26.getOutlineShape(clientWorld, var27);
         boolean var29 = var28.isEmpty();
         Box var33;
         if (!var29) {
            Box var30 = var28.getBoundingBox();
            BlockPos var31 = this.blockPos;
            Box var32 = var30.offset(var31);
            var33 = var32;
         } else {
            var33 = null;
         }

         this.box = var33;
      }

      SpeedmineModule var34 = this.speedmineModule;
      BlockPos var35 = this.blockPos;
      BlockState var36 = this.blockState2;
      boolean var37 = var34.testBlockPos(var35, var36, clientPlayerEntity, clientWorld);
      if (var37) {
         boolean var38 = this.flag2;
         if (var38) {
            SpeedmineModule var39 = this.speedmineModule;
            Setting var40 = var39.mode;
            Object var41 = var40.getValue();
            boolean var42 = var41 instanceof SpeedmineModule.Mode;
            if (var41 != null && !var42) {
               throw new ClassCastException("CHECKCAST failed");
            }

            SpeedmineModule.Mode var69 = SpeedmineModule.Mode.Fast;
            boolean var70 = ((SpeedmineModule.Mode)var41).equals(var69);
            if (var70) {
               SpeedmineModule var71 = this.speedmineModule;
               Setting var72 = var71.abort;
               Object var73 = var72.getValue();
               boolean var74 = var73 instanceof Boolean;
               if (var73 != null && !var74) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var78 = (Boolean)var73;
               if (var78) {
                  ClientPlayNetworkHandler var79 = clientPlayerEntity.networkHandler;
                  Action var81 = Action.ABORT_DESTROY_BLOCK;
                  BlockPos var82 = this.blockPos;
                  PlayerActionC2SPacket var80 = new PlayerActionC2SPacket(var81, var82, direction);
                  var79.sendPacket(var80);
                  this.flag2 = false & true;
               }
            }
         }
      }

      PhobosClient var49 = this.phobosClient;
      InventoryManager var50 = var49.getInventoryManager();
      Lookup var51 = MethodHandles.lookup();
      Class<?> var52 = this.getClass();
      ClassLoader var53 = var52.getClassLoader();
      MethodType var54 = MethodType.fromMethodDescriptorString(
         "(Lorg/phobos/core/block/PacketBlockBreaker;Lnet/minecraft/class_746;Lnet/minecraft/class_638;Lnet/minecraft/class_636;Z)Ljava/util/function/Consumer;", var53
      );
      Class<?> var55 = this.getClass();
      ClassLoader var56 = var55.getClassLoader();
      MethodType var57 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", var56);
      Class<?> var58 = this.getClass();
      ClassLoader var59 = var58.getClassLoader();
      MethodType var60 = MethodType.fromMethodDescriptorString(
         "(Lnet/minecraft/class_746;Lnet/minecraft/class_638;Lnet/minecraft/class_636;ZLorg/phobos/core/inventory/ItemSwitcher;)V", var59
      );
      MethodHandle var61 = var51.findVirtual(PacketBlockBreaker.class, "handleClientPlayerEntity6", var60);
      Class<?> var62 = this.getClass();
      ClassLoader var63 = var62.getClassLoader();
      MethodType var64 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/inventory/ItemSwitcher;)V", var63);
      Object[] var10000 = new Object[0];
      CallSite var65 = LambdaMetafactory.metafactory(var51, "accept", var54, var57, var61, var64);
      MethodHandle var66 = var65.getTarget();
      if (this != null) {
         var10000 = phobos$g1806eefd0;
         if (phobos$g1806eefd0 == null) {
            var10000 = phobos$g1806eefd0 = new Object[5];
         }

         var10000[0] = this;
      }

      if (clientPlayerEntity != null) {
         var10000 = phobos$g1806eefd0;
         if (phobos$g1806eefd0 == null) {
            var10000 = phobos$g1806eefd0 = new Object[5];
         }

         var10000[1] = clientPlayerEntity;
      }

      if (clientWorld != null) {
         var10000 = phobos$g1806eefd0;
         if (phobos$g1806eefd0 == null) {
            var10000 = phobos$g1806eefd0 = new Object[5];
         }

         var10000[2] = clientWorld;
      }

      if (clientPlayerInteractionManager != null) {
         var10000 = phobos$g1806eefd0;
         if (phobos$g1806eefd0 == null) {
            var10000 = phobos$g1806eefd0 = new Object[5];
         }

         var10000[3] = clientPlayerInteractionManager;
      }

      Boolean var67 = flag;
      var10000 = phobos$g1806eefd0;
      if (phobos$g1806eefd0 == null) {
         var10000 = phobos$g1806eefd0 = new Object[5];
      }

      var10000[4] = var67;
      Object[] var10001 = phobos$g1806eefd0;
      if (phobos$g1806eefd0 == null) {
         var10001 = phobos$g1806eefd0 = new Object[5];
      }

      Object var68 = var66.invokeWithArguments(var10001);
      var50.handleConsumer2((Consumer<ItemSwitcher>)var68);
   }

   @NotNull
   public BlockState getBlockState() {
      return this.blockState;
   }

   public PacketBlockBreaker(PhobosClient phobosClient, SpeedmineModule speedmineModule) {
      super(phobosClient, speedmineModule);
      this.flag = true;
      this.normal = new Normal();
      this.floatValue = 0.0F;
   }

   public boolean testDirectionResolver(
      DirectionResolver directionResolver, ClientWorld clientWorld, PlayerEntity playerEntity, ClientPlayerEntity clientPlayerEntity, BlockHitResult blockHitResult
   ) {
      return !directionResolver.testBlockPos(blockHitResult.getBlockPos(), blockHitResult.getSide(), clientWorld, playerEntity)
         ? false
         : this.speedmineModule.testClientPlayerEntity3(clientPlayerEntity, clientWorld, blockHitResult.getBlockPos(), blockHitResult.getSide());
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, Direction direction, Integer integer) {
      clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, this.blockPos, direction, integer));
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void handleClientPlayerEntity6(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      boolean flag,
      ItemSwitcher itemSwitcher
   ) {
      this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, flag);
   }

   @Override
   public void handleClientWorld(ClientWorld clientWorld) {
      if (this.blockPos == null) {
         this.handleFlag(false);
      } else {
         this.doubleValue.setDoubleValue2(MathUtils.computeFloat3(this.floatValue2 / 2.0F, 0.0F, 1.0F));
         this.doubleValue.handleDouble(10.0, 0.1F);
         Color color = new Color(0, 255, 0, this.speedmineModule.box.getValue().getAlpha());
         Color color2 = new Color(255, 0, 0, this.speedmineModule.box.getValue().getAlpha());
         Color color3 = new Color(0, 255, 0, this.speedmineModule.line.getValue().getAlpha());
         Color color4 = new Color(255, 0, 0, this.speedmineModule.line.getValue().getAlpha());
         Color color5 = this.speedmineModule.custom.getValue()
            ? this.speedmineModule.box.getValue()
            : (
               this.speedmineModule.flow.getValue()
                  ? ColorUtils.createColor8(color2, color, this.computeFloat(this.floatValue3))
                  : (MathUtils.computeDouble5(this.computeFloat(this.floatValue3), 0.0, 1.0) >= 0.7 ? color : color2)
            );
         Color color6 = this.speedmineModule.custom.getValue()
            ? this.speedmineModule.line.getValue()
            : (
               this.speedmineModule.flow.getValue()
                  ? ColorUtils.createColor8(color4, color3, this.computeFloat(this.floatValue3))
                  : (MathUtils.computeDouble5(this.computeFloat(this.floatValue3), 0.0, 1.0) >= 0.7 ? color3 : color4)
            );
         double d = MathUtils.computeDouble5(this.computeDouble(this.doubleValue.getDoubleValue() / 2.0 * 2.0) / 2.0, 0.0, 0.5);
         double d2 = d * 2.0;
         if (this.box != null) {
            Box box;
            if (this.speedmineModule.animate.getValue()) {
               double[] doubles = this.values(this.box);
               box = this.box.expand(-doubles[0] + doubles[0] * d2, -doubles[1] + doubles[1] * d2, -doubles[2] + doubles[2] * d2);
            } else {
               box = this.box;
            }

            if (this.speedmineModule.fade.getValue()) {
               Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.getIntValue() == 3);
            }

            Render3d.handleCuboid(
               Cuboid.createBuilder()
                  .resolveBuilder11(box)
                  .resolveBuilder18(color5, color6)
                  .resolveBuilder12(this.speedmineModule.width.getValue())
                  .resolveBuilder6(this.speedmineModule.fade, this.speedmineModule.fadeTime)
                  .resolveBuilder15(3)
                  .getCuboid()
            );
         } else {
            this.handleFlag(false, this.speedmineModule.renderReset.getValue() && this.speedmineModule.mode.testObject2(SpeedmineModule.Mode.Instant));
         }
      }
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   @Override
   public void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity) {
      boolean var2 = this.check();
      if (var2) {
         Direction var3 = this.getDirection();
         int var41;
         Object var42;
         if (var3 != null) {
            BlockState var4 = this.blockState2;
            boolean var5 = var4.isAir();
            if (var5) {
               this.flag3 = true & true;
               return;
            }

            PacketBlockBreaker var10000 = this;
            ClientPlayerEntity var10001 = clientPlayerEntity;
            Direction cap7 = var3;
            ClientPlayerEntity cap6 = var10001;
            PacketBlockBreaker cap5 = var10000;
            Consumer<Integer> var22 = var3x -> cap5.handleClientPlayerEntity7(cap6, cap7, var3x);
            TickUtils.handleConsumer2(var22);
            var10000 = this;
            var10001 = clientPlayerEntity;
            Direction cap10 = var3;
            ClientPlayerEntity cap9 = var10001;
            PacketBlockBreaker cap8 = var10000;
            Consumer<Integer> var39 = var3x -> cap8.handleClientPlayerEntity3(cap9, cap10, var3x);
            TickUtils.handleConsumer2(var39);
            this.handleFloat2((float)0.0F);
            Normal var40 = this.normal;
            var41 = 7271088;
            var42 = var40;
         } else {
            var41 = 7270528;
            var42 = this;
         }

         switch (var41) {
            case 7270528:
            default:
               ((PacketBlockBreaker)var42).run2();
               break;
            case 7271088:
               ((Normal)var42).run();
         }
      }
   }

   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      boolean flag
   ) {
      int var21 = 0;
      BlockPos var6 = this.blockPos;
      if (var6 != null) {
         Direction var7 = this.getDirection();
         if (var7 != null) {
            KeyValue var8 = this.resolveKeyValue(itemSwitcher, clientPlayerEntity, clientWorld);
            Object var9 = var8.getObject();
            boolean var10 = var9 instanceof Slot;
            if (var9 != null && !var10) {
               throw new ClassCastException("CHECKCAST failed");
            }

            Object var11 = var8.getObject2();
            boolean var12 = var11 instanceof Float;
            if (var11 != null && !var12) {
               throw new ClassCastException("CHECKCAST failed");
            }

            float var13 = (Float)var11;
            if (var9 != null) {
               this.floatValue3 = var13;
               BlockState var14 = this.blockState2;
               boolean var15 = var14.isAir();
               float var20;
               if (!var15) {
                  BlockState var16 = this.blockState2;
                  ItemStack var17 = ((Slot)var9).getStack();
                  boolean var18 = clientPlayerEntity.isOnGround();
                  float var19 = this.computeBlockPos(var6, var16, clientWorld, clientPlayerEntity, var17, var18);
                  var20 = var19;
               } else {
                  float var30 = this.floatValue;
                  int var10000 = Float.floatToRawIntBits(var13);
                  var20 = var30;
                  var21 = var10000;
               }

               long var22 = Float.floatToRawIntBits(var20) & 4294967295L | var21 & -4294967296L;
               long var24 = Float.floatToRawIntBits(var20) & 4294967295L | var21 & -4294967296L;
               if (!var15) {
                  long var26 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var31 = Float.floatToRawIntBits(var20) & 4294967295L;
                  this.floatValue = Float.intBitsToFloat((int)((int)((int)(Float.floatToRawIntBits(var20) & 4294967295L) & 4294967295L) & 4294967295L));
                  long var28 = Float.floatToRawIntBits(var20) & 4294967295L;
               } else {
                  long var221 = Float.floatToRawIntBits(var20) & 4294967295L;
               }

               byte var55;
               label270: {
                  long var33 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var35 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var37 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var39 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var41 = Float.floatToRawIntBits(var20) & 4294967295L;
                  float var43 = this.floatValue2;
                  long var44 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var46 = Float.floatToRawIntBits(var20) & 4294967295L;
                  long var48 = (int)(Float.floatToRawIntBits(var20) & 4294967295L) & 4294967295L;
                  long var50 = (int)(Float.floatToRawIntBits(var20) & 4294967295L) & 4294967295L;
                  long var52 = Float.floatToRawIntBits(
                        Float.intBitsToFloat(Float.floatToRawIntBits(var43))
                           + Float.intBitsToFloat((int)((int)(Float.floatToRawIntBits(var20) & 4294967295L) & 4294967295L))
                     )
                     & 4294967295L;
                  this.floatValue2 = Float.intBitsToFloat((int)((int)var52 & 4294967295L));
                  float var54 = this.floatValue2;
                  if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var54)))
                     && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var54)))
                     && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var13)) * 2.0F)))) {
                     if (Float.intBitsToFloat(Float.floatToRawIntBits(var54))
                        > Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var13)) * 2.0F))) {
                        var55 = 1;
                        break label270;
                     }

                     if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var54)))
                        && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var13)) * 2.0F)))
                        && Float.intBitsToFloat(Float.floatToRawIntBits(var54))
                           == Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var13)) * 2.0F))) {
                        var55 = 0;
                        break label270;
                     }
                  }

                  var55 = -1;
               }

               if ((int)(var55 & 4294967295L) > 0) {
                  this.floatValue2 = Float.intBitsToFloat(Float.floatToRawIntBits(var13)) * 2.0F;
               }

               SpeedmineModule var56 = this.speedmineModule;
               Setting var57 = var56.damage;
               Object var58 = var57.getValue();
               boolean var59 = var58 instanceof Float;
               if (var58 != null && !var59) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               byte var61;
               label256: {
                  float var60 = (Float)var58;
                  if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var13)))
                     && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var13)))
                     && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var60)))) {
                     if (Float.intBitsToFloat(Float.floatToRawIntBits(var13)) > Float.intBitsToFloat(Float.floatToRawIntBits(var60))) {
                        var61 = 1;
                        break label256;
                     }

                     if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var13)))
                        && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var60)))
                        && Float.intBitsToFloat(Float.floatToRawIntBits(var13)) == Float.intBitsToFloat(Float.floatToRawIntBits(var60))) {
                        var61 = 0;
                        break label256;
                     }
                  }

                  var61 = -1;
               }

               label238:
               if ((int)(var61 & 4294967295L) >= 0) {
                  label243: {
                     SpeedmineModule var62 = this.speedmineModule;
                     Setting var63 = var62.mode;
                     SpeedmineModule.Mode var64 = SpeedmineModule.Mode.Instant;
                     boolean var65 = var63.testObject2(var64);
                     if (var65) {
                        SpeedmineModule var66 = this.speedmineModule;
                        Setting var67 = var66.fast;
                        Object var68 = var67.getValue();
                        boolean var69 = var68 instanceof Boolean;
                        if (var68 != null && !var69) {
                           throw new ClassCastException("CHECKCAST failed");
                        }

                        boolean var83 = (Boolean)var68;
                        if (var83) {
                           SpeedmineModule var84 = this.speedmineModule;
                           BlockState var85 = this.blockState2;
                           boolean var86 = var84.testBlockPos2(var6, var85, clientPlayerEntity, clientWorld);
                           if (!var86) {
                              break label238;
                           }
                           break label243;
                        }
                     }

                     SpeedmineModule var76 = this.speedmineModule;
                     BlockState var77 = this.blockState2;
                     boolean var78 = var76.testBlockPos(var6, var77, clientPlayerEntity, clientWorld);
                     if (!var78) {
                        break label238;
                     }
                  }

                  SpeedmineModule var79 = this.speedmineModule;
                  Setting var80 = var79.rotate;
                  Object var81 = var80.getValue();
                  boolean var82 = var81 instanceof Boolean;
                  if (var81 != null && !var82) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  Direction var88;
                  boolean var87 = (Boolean)var81;
                  label231:
                  if (var87) {
                     SpeedmineModule var89 = this.speedmineModule;
                     Setting var90 = var89.strictDirection;
                     Object var91 = var90.getValue();
                     boolean var92 = var91 instanceof Boolean;
                     if (var91 != null && !var92) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var97 = (Boolean)var91;
                     int var98;
                     if (!var97) {
                        var98 = 7272592;
                     } else {
                        SpeedmineModule var130 = this.speedmineModule;
                        Setting var131 = var130.grim;
                        Object var132 = var131.getValue();
                        boolean var133 = var132 instanceof Boolean;
                        if (var132 != null && !var133) {
                           throw new ClassCastException("CHECKCAST failed");
                        }

                        boolean var152 = (Boolean)var132;
                        if (!var152) {
                           var98 = 7272576;
                        } else {
                           var98 = 7272560;
                        }
                     }
                     DirectionResolver var99 = switch (var98) {
                        default -> ExactDirectionResolver.exactDirectionResolver;
                        case 7272576 -> ShapeDirectionResolver.shapeDirectionResolver;
                        case 7272592 -> DefaultDirectionResolver.defaultDirectionResolver;
                     };
                     PhobosClient var100 = this.phobosClient;
                     MovementSimulator var101 = var100.getMovementSimulator();
                     PlayerEntity var102 = var101.resolvePlayerEntity(clientPlayerEntity);
                     PacketBlockBreaker var222 = this;
                     ClientWorld var10002 = clientWorld;
                     ClientPlayerEntity cap15 = clientPlayerEntity;
                     PlayerEntity cap14 = var102;
                     ClientWorld cap13 = var10002;
                     DirectionResolver cap12 = var99;
                     PacketBlockBreaker cap11 = var222;
                     Predicate<BlockHitResult> var119 = var5 -> cap11.testDirectionResolver(cap12, cap13, cap14, cap15, var5);
                     BlockHitResult var120 = RaycastUtils.resolveBlockHitResult3(var102, clientWorld, var6, var119, true & true);
                     if (var120 != null) {
                        Direction var121 = var120.getSide();
                        boolean var122 = var99.testBlockPos(var6, var121, clientWorld, clientPlayerEntity);
                        if (var122) {
                           Direction var123 = var120.getSide();
                           var88 = var123;
                           break label231;
                        }
                     }

                     Direction var134 = var99.resolveDirection(var6, clientPlayerEntity, clientWorld);
                     if (var134 == null) {
                        this.handleClientPlayerEntity(clientPlayerEntity);
                        this.run2();
                        return;
                     }

                     float[] var135 = RotationUtils.values4(clientPlayerEntity, clientWorld, var6, var134);
                     int[] var136 = new int[SpeedmineModule.Rotate.values().length];
                     var136[SpeedmineModule.Rotate.Normal.ordinal()] = 1;
                     var136[SpeedmineModule.Rotate.Packet.ordinal()] = 2;
                     var136[SpeedmineModule.Rotate.Silent.ordinal()] = 3;
                     SpeedmineModule var137 = this.speedmineModule;
                     Setting var138 = var137.rotateType;
                     Object var139 = var138.getValue();
                     boolean var140 = var139 instanceof SpeedmineModule.Rotate;
                     if (var139 != null && !var140) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     int var153 = ((SpeedmineModule.Rotate)var139).ordinal();
                     int var154 = var136[var153];
                     label220:
                     if (var154 + -1 != 0) {
                        if (var154 + -1 + -1 != 0) {
                           if (var154 + -1 + -1 != 1) {
                              var88 = var7;
                              break label220;
                           }

                           PhobosClient var155 = this.phobosClient;
                           RotationManager var156 = var155.getRotationManager();
                           float var157 = var135[0];
                           float var158 = var135[1];
                           var156.handleClientPlayerEntity3(
                              clientPlayerEntity,
                              (float)Float.intBitsToFloat(Float.floatToRawIntBits(var157)),
                              (float)Float.intBitsToFloat(Float.floatToRawIntBits(var158))
                           );
                        } else {
                           float var180 = var135[0];
                           float var181 = var135[1];
                           boolean var182 = var102.isOnGround();
                           PlayerActionUtils.handleClientPlayerEntity17(
                              clientPlayerEntity,
                              (float)Float.intBitsToFloat(Float.floatToRawIntBits(var180)),
                              (float)Float.intBitsToFloat(Float.floatToRawIntBits(var181)),
                              var182
                           );
                        }

                        var88 = var7;
                     } else {
                        PhobosClient var172 = this.phobosClient;
                        RotationManager var173 = var172.getRotationManager();
                        boolean var174 = var173.isFlag2();
                        if (!var174) {
                           return;
                        }

                        boolean var175 = RotationManager.isFlag();
                        if (var175) {
                           return;
                        }

                        PhobosClient var176 = this.phobosClient;
                        RotationManager var177 = var176.getRotationManager();
                        float var178 = var135[0];
                        float var179 = var135[1];
                        var177.testFloat(
                           (float)Float.intBitsToFloat(Float.floatToRawIntBits(var178)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var179))
                        );
                        var88 = var7;
                     }
                  } else {
                     var88 = var7;
                  }

                  SpeedmineModule var93 = this.speedmineModule;
                  Setting var94 = var93.booleanValueSetting;
                  Object var95 = var94.getValue();
                  boolean var96 = var95 instanceof Boolean;
                  if (var95 != null && !var96) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  boolean var124 = (Boolean)var95;
                  boolean var125 = itemSwitcher.testSlot6((Slot)var9, var124);
                  ClientPlayNetworkHandler var187;
                  HandSwingC2SPacket var188;
                  if (!var125) {
                     SpeedmineModule var126 = this.speedmineModule;
                     Setting var127 = var126.swap;
                     Object var128 = var127.getValue();
                     boolean var129 = var128 instanceof Boolean;
                     if (var128 != null && !var129) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var147 = (Boolean)var128;
                     if (!var147) {
                        return;
                     }

                     SpeedmineModule var148 = this.speedmineModule;
                     Setting var149 = var148.swapMultiTask;
                     Object var150 = var149.getValue();
                     boolean var151 = var150 instanceof Boolean;
                     if (var150 != null && !var151) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var164 = (Boolean)var150;
                     if (!var164) {
                        boolean var165 = PlayerActionUtils.testClientPlayerEntity2(clientPlayerEntity);
                        if (var165) {
                           return;
                        }
                     }

                     SpeedmineModule var166 = this.speedmineModule;
                     boolean var167 = var166.testClientPlayerEntity(clientPlayerEntity, var6, false & true);
                     if (!var167) {
                        return;
                     }

                     SpeedmineModule var168 = this.speedmineModule;
                     Setting var169 = var168.booleanValueSetting;
                     Object var170 = var169.getValue();
                     boolean var171 = var170 instanceof Boolean;
                     if (var170 != null && !var171) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var189 = (Boolean)var170;
                     if (var189) {
                        SilentHotbarManager var195 = SilentHotbarModule.getSilentHotbarManager();
                        SpeedmineModule var196 = this.speedmineModule;
                        Setting var197 = var196.swapDelay;
                        Object var198 = var197.getValue();
                        boolean var199 = var198 instanceof Integer;
                        if (var198 != null && !var199) {
                           throw new ClassCastException("CHECKCAST failed");
                        }

                        int var205 = (Integer)var198;
                        SelectionConfig[] var206 = new SelectionConfig[0];
                        var195.handleObject3(this, (Slot)var9, var205, var206);
                        return;
                     }

                     Hand var190 = Hand.MAIN_HAND;
                     SpeedmineModule var191 = this.speedmineModule;
                     Setting var192 = var191.swapType;
                     Object var193 = var192.getValue();
                     boolean var194 = var193 instanceof Silent;
                     if (var193 != null && !var194) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     int var200 = ((Silent)var193).getFlag();
                     itemSwitcher.resolveSwitchResult(var190, (Slot)var9, var200 | 1);
                     this.handleClientPlayerEntity8(clientPlayerEntity, var88, clientWorld, clientPlayerInteractionManager);
                     SpeedmineModule var201 = this.speedmineModule;
                     Setting var202 = var201.swing;
                     Object var203 = var202.getValue();
                     boolean var204 = var203 instanceof Boolean;
                     if (var203 != null && !var204) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var207 = (Boolean)var203;
                     if (!var207) {
                        return;
                     }

                     SpeedmineModule var208 = this.speedmineModule;
                     Setting var209 = var208.end;
                     Object var210 = var209.getValue();
                     boolean var211 = var210 instanceof Boolean;
                     if (var210 != null && !var211) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var212 = (Boolean)var210;
                     if (!var212) {
                        return;
                     }

                     ClientPlayNetworkHandler var213 = clientPlayerEntity.networkHandler;
                     Hand var215 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var214 = new HandSwingC2SPacket(var215);
                     var187 = var213;
                     var188 = var214;
                  } else {
                     SpeedmineModule var141 = this.speedmineModule;
                     boolean var142 = var141.testClientPlayerEntity(clientPlayerEntity, var6, false & true);
                     if (!var142) {
                        return;
                     }

                     this.handleClientPlayerEntity8(clientPlayerEntity, var88, clientWorld, clientPlayerInteractionManager);
                     SpeedmineModule var143 = this.speedmineModule;
                     Setting var144 = var143.swing;
                     Object var145 = var144.getValue();
                     boolean var146 = var145 instanceof Boolean;
                     if (var145 != null && !var146) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var159 = (Boolean)var145;
                     if (!var159) {
                        return;
                     }

                     SpeedmineModule var160 = this.speedmineModule;
                     Setting var161 = var160.end;
                     Object var162 = var161.getValue();
                     boolean var163 = var162 instanceof Boolean;
                     if (var162 != null && !var163) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     boolean var183 = (Boolean)var162;
                     if (!var183) {
                        return;
                     }

                     ClientPlayNetworkHandler var184 = clientPlayerEntity.networkHandler;
                     Hand var186 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var185 = new HandSwingC2SPacket(var186);
                     var187 = var184;
                     var188 = var185;
                  }

                  var187.sendPacket(var188);
                  return;
               }

               byte var70;
               label167: {
                  if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var13))) && !Float.isNaN(0.4F)) {
                     if (Float.intBitsToFloat(Float.floatToRawIntBits(var13)) > 0.4F) {
                        var70 = 1;
                        break label167;
                     }

                     if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var13)))
                        && !Float.isNaN(0.4F)
                        && Float.intBitsToFloat(Float.floatToRawIntBits(var13)) == 0.4F) {
                        var70 = 0;
                        break label167;
                     }
                  }

                  var70 = -1;
               }

               if (var70 >= 0) {
                  boolean var71 = this.flag;
                  if (var71) {
                     Hand var72 = Hand.MAIN_HAND;
                     itemSwitcher.resolveSwitchResult(var72, (Slot)var9, 1);
                     ClientPlayNetworkHandler var73 = clientPlayerEntity.networkHandler;
                     Hand var75 = Hand.MAIN_HAND;
                     HandSwingC2SPacket var74 = new HandSwingC2SPacket(var75);
                     var73.sendPacket(var74);
                     this.flag = false & true;
                  }
               }
            }
         }
      }
   }

   public void handleClientPlayerEntity7(ClientPlayerEntity clientPlayerEntity, Direction direction, Integer integer) {
      clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, this.blockPos, direction, integer));
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   @Nullable
   public Direction getDirection() {
      if (!this.speedmineModule.strictDirection.getValue()) {
         return Direction.DOWN;
      }

      ClientPlayerEntity clientPlayerEntity = mc.player;
      ClientWorld clientWorld = mc.world;
      return clientPlayerEntity != null && clientWorld != null && this.blockPos != null
         ? (this.speedmineModule.grim.getValue() ? ExactDirectionResolver.exactDirectionResolver : ShapeDirectionResolver.shapeDirectionResolver)
            .resolveDirection(this.blockPos, clientPlayerEntity, clientWorld)
         : null;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public Normal getNormal() {
      return this.normal;
   }

   @Override
   public void handleClientPlayerEntity4(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager, boolean flag
   ) {
      boolean var5 = this.check();
      if (var5) {
         Direction var6 = this.getDirection();
         if (var6 != null) {
            boolean var7 = this.testClientPlayerEntity(clientPlayerEntity);
            if (!var7) {
               this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, var6, flag);
               return;
            }

            this.handleClientPlayerEntity(clientPlayerEntity);
         } else {
            this.handleClientPlayerEntity(clientPlayerEntity);
         }

         this.run2();
      }
   }

   public double computeDouble(double d) {
      double d2 = 0.0;
      double d3 = this.speedmineModule.damage.getValue().floatValue();
      return (d - d2) / (d3 - d2);
   }

   @Override
   public void run2() {
      this.blockPos = null;
      this.blockState2 = Blocks.AIR.getDefaultState();
      this.blockState = Blocks.AIR.getDefaultState();
      this.box = null;
      this.handleFloat2(0.0F);
      this.normal.run();
   }

   public KeyValue<Slot, Float> resolveKeyValue(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      MutableFloat mutableFloat = new MutableFloat(0.0F);
      MutableObject<Slot> mutableObject = new MutableObject<>();
      itemSwitcher.resolveObject(slot -> {
         ItemStack itemStack = slot.getStack();
         double d = this.normal.value() / 50.0 - this.speedmineModule.ticks.getValue().intValue();
         float f = 0.0F;

         for (LookDirection lookDirection : this.getPhobosClient().getMovementSimulator().getLookDirectionDeque()) {
            f += this.computeBlockPos(this.blockPos, this.blockState2, clientWorld, clientPlayerEntity, itemStack, lookDirection.isFlag());
            if (--d <= 0.0) {
               break;
            }
         }

         while (d > 0.0) {
            f += this.computeBlockPos(this.blockPos, this.blockState2, clientWorld, clientPlayerEntity, itemStack, clientPlayerEntity.isOnGround());
            d--;
         }

         boolean flag = itemSwitcher.testSlot3(slot) && f >= this.speedmineModule.damage.getValue();
         if (!(f > mutableFloat.getValue()) && !flag) {
            return null;
         }

         mutableFloat.setValue(f);
         mutableObject.setValue(slot);
         return !flag ? null : slot;
      });
      return KeyValue.createValue(mutableObject.getValue(), mutableFloat.floatValue());
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void handleClientPlayerEntity8(
      ClientPlayerEntity clientPlayerEntity, Direction direction, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator()) {
         if (!this.speedmineModule.mode.testObject2(SpeedmineModule.Mode.Instant)
            || this.speedmineModule.normal2.testNumber(this.speedmineModule.mineDelay.getValue())) {
            if (this.speedmineModule.cancel.getValue()) {
               clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, this.blockPos.up(500), direction));
            }

            clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, this.blockPos, direction));
            if (this.speedmineModule.air.getValue()) {
               this.speedmineModule
                  .handleClientPlayerInteractionManager(clientPlayerInteractionManager, this.blockPos, clientWorld.getBlockState(this.blockPos));
            }

            this.speedmineModule.setFlag(false);
            this.speedmineModule.mode.getValue().execute(clientPlayerEntity, clientWorld, this.blockPos, direction, this);
            if (this.speedmineModule.check3()) {
               clientPlayerEntity.networkHandler
                  .sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, Objects.requireNonNullElse(this.blockPos, BlockPos.ORIGIN), direction));
            }

            if (this.speedmineModule.rotateType.getValue().equals(SpeedmineModule.Rotate.Silent)) {
               this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
            }

            this.speedmineModule.normal2.run();
         }
      }
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }
}
