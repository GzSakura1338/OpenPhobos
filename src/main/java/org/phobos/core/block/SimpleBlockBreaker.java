package org.phobos.core.block;

import lombok.SneakyThrows;
import java.awt.Color;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.KeyValue;
import org.phobos.api.DelayedTask;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.inventory.SilentHotbarManager.SelectionConfig;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.inventory.SilentHotbarManager;

public class SimpleBlockBreaker extends BlockBreaker {
   private static Object[] phobos$g1806f0698;

   @Override
   public void handleClientWorld(ClientWorld clientWorld) {
      if (this.blockPos == null) {
         this.handleFlag(false);
      } else {
         this.doubleValue.setDoubleValue2(this.floatValue2);
         this.doubleValue.handleDouble(10.0, 0.1F);
         Color color = new Color(0, 255, 0, this.speedmineModule.box.getValue().getAlpha());
         Color color2 = new Color(255, 0, 0, this.speedmineModule.box.getValue().getAlpha());
         Color color3 = new Color(0, 255, 0, this.speedmineModule.line.getValue().getAlpha());
         Color color4 = new Color(255, 0, 0, this.speedmineModule.line.getValue().getAlpha());
         Color color5 = this.speedmineModule.custom.getValue()
            ? this.speedmineModule.box.getValue()
            : (
               this.speedmineModule.flow.getValue()
                  ? ColorUtils.createColor8(color2, color, this.floatValue3)
                  : (MathUtils.computeDouble5(this.floatValue3, 0.0, 1.0) >= 0.7 ? color : color2)
            );
         Color color6 = this.speedmineModule.custom.getValue()
            ? this.speedmineModule.line.getValue()
            : (
               this.speedmineModule.flow.getValue()
                  ? ColorUtils.createColor8(color4, color3, this.floatValue3)
                  : (MathUtils.computeDouble5(this.floatValue3, 0.0, 1.0) >= 0.7 ? color3 : color4)
            );
         double d = MathUtils.computeDouble5(this.doubleValue.getDoubleValue() / 2.0, 0.0, 0.5);
         double d2 = d * 2.0;
         VoxelShape voxelShape = this.blockState2.getOutlineShape(clientWorld, this.blockPos);
         if (voxelShape != null && !voxelShape.isEmpty()) {
            if (this.blockPos == null) {
               this.handleFlag(false);
            } else {
               Box box2 = voxelShape.getBoundingBox().offset(this.blockPos);
               Box box;
               if (this.speedmineModule.animate.getValue()) {
                  double[] doubles = this.values(box2);
                  box = box2.expand(-doubles[0] + doubles[0] * d2, -doubles[1] + doubles[1] * d2, -doubles[2] + doubles[2] * d2);
               } else {
                  box = box2;
               }

               if (this.speedmineModule.fade.getValue()) {
                  Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.getIntValue() == 4);
               }

               Render3d.handleCuboid(
                  Cuboid.createBuilder()
                     .resolveBuilder11(box)
                     .resolveBuilder18(color5, color6)
                     .resolveBuilder12(this.speedmineModule.width.getValue())
                     .resolveBuilder6(this.speedmineModule.fade, this.speedmineModule.fadeTime)
                     .resolveBuilder15(4)
                     .getCuboid()
               );
            }
         }
      }
   }

   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher,
      ClientWorld clientWorld,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      boolean flag
   ) {
      KeyValue var6 = this.resolveKeyValue(itemSwitcher, clientPlayerEntity, clientWorld);
      Object var7 = var6.getObject();
      boolean var8 = var7 instanceof Slot;
      if (var7 != null && !var8) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Object var9 = var6.getObject2();
      boolean var10 = var9 instanceof Float;
      if (var9 != null && !var10) {
         throw new ClassCastException("CHECKCAST failed");
      }

      float var11 = (Float)var9;
      if (var7 != null) {
         if (flag) {
            this.handleFloat((float)Float.intBitsToFloat(Float.floatToRawIntBits(var11)));
         }

         SpeedmineModule var12 = this.speedmineModule;
         Setting var13 = var12.negate;
         Object var14 = var13.getValue();
         boolean var15 = var14 instanceof Float;
         if (var14 != null && !var15) {
            throw new ClassCastException("CHECKCAST failed");
         }

         byte var18;
         label60: {
            float var16 = (Float)var14;
            float var17 = this.getFloatValue3();
            if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var17)))
               && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var17)))
               && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(1.0F - Float.intBitsToFloat(Float.floatToRawIntBits(var16)))))
               && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(1.0F - Float.intBitsToFloat(Float.floatToRawIntBits(var16)))))) {
               if (Float.intBitsToFloat(Float.floatToRawIntBits(var17))
                  > Float.intBitsToFloat(Float.floatToRawIntBits(1.0F - Float.intBitsToFloat(Float.floatToRawIntBits(var16))))) {
                  var18 = 1;
                  break label60;
               }

               if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var17)))
                  && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(1.0F - Float.intBitsToFloat(Float.floatToRawIntBits(var16)))))
                  && Float.intBitsToFloat(Float.floatToRawIntBits(var17))
                     == Float.intBitsToFloat(Float.floatToRawIntBits(1.0F - Float.intBitsToFloat(Float.floatToRawIntBits(var16))))) {
                  var18 = 0;
                  break label60;
               }
            }

            var18 = -1;
         }

         if ((int)(var18 & 4294967295L) >= 0) {
            boolean var19 = itemSwitcher.testSlot6((Slot)var7, true & true);
            if (!var19) {
               SpeedmineModule var20 = this.speedmineModule;
               Setting var21 = var20.swap;
               Object var22 = var21.getValue();
               boolean var23 = var22 instanceof Boolean;
               if (var22 != null && !var23) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var29 = (Boolean)var22;
               if (var29) {
                  boolean var30 = PlayerActionUtils.testClientPlayerEntity2(clientPlayerEntity);
                  if (!var30) {
                     SilentHotbarManager var31 = SilentHotbarModule.getSilentHotbarManager();
                     SpeedmineModule var32 = this.speedmineModule;
                     Setting var33 = var32.doubleTicks;
                     Object var34 = var33.getValue();
                     boolean var35 = var34 instanceof Integer;
                     if (var34 != null && !var35) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     int var40 = (Integer)var34;
                     SelectionConfig[] var41 = new SelectionConfig[0];
                     var31.handleObject3(this, (Slot)var7, var40, var41);
                     SpeedmineModule var42 = this.speedmineModule;
                     BlockPos var43 = this.blockPos;
                     var42.testClientPlayerEntity(clientPlayerEntity, var43, true & true);
                     this.handleClientPlayerEntity3(clientPlayerEntity, clientPlayerInteractionManager);
                     SpeedmineModule var44 = this.speedmineModule;
                     Normal var45 = var44.normal3;
                     var45.run();
                  }
               }
            } else {
               SilentHotbarManager var24 = SilentHotbarModule.getSilentHotbarManager();
               SpeedmineModule var25 = this.speedmineModule;
               Setting var26 = var25.doubleTicks;
               Object var27 = var26.getValue();
               boolean var28 = var27 instanceof Integer;
               if (var27 != null && !var28) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               int var36 = (Integer)var27;
               SelectionConfig[] var37 = new SelectionConfig[0];
               var24.handleObject3(this, (Slot)var7, var36, var37);
               SpeedmineModule var38 = this.speedmineModule;
               BlockPos var39 = this.blockPos;
               var38.testClientPlayerEntity(clientPlayerEntity, var39, true & true);
               this.handleClientPlayerEntity3(clientPlayerEntity, clientPlayerInteractionManager);
            }
         }
      }
   }

   public KeyValue<Slot, Float> resolveKeyValue(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      MutableFloat mutableFloat = new MutableFloat(0.0F);
      MutableObject<Slot> mutableObject = new MutableObject<>();
      itemSwitcher.resolveObject(slot -> {
         ItemStack itemStack = slot.getStack();
         float f = this.computeBlockPos(this.blockPos, this.blockState2, clientWorld, clientPlayerEntity, itemStack, true);
         if (f <= mutableFloat.getValue()) {
            return null;
         }

         mutableObject.setValue(slot);
         mutableFloat.setValue(f);
         return null;
      });
      return KeyValue.createValue(mutableObject.getValue(), mutableFloat.floatValue());
   }

   @SneakyThrows public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager, boolean flag
   ) {
      BlockPos var5 = this.blockPos;
      BlockState var6 = clientWorld.getBlockState(var5);
      this.blockState2 = var6;
      BlockState var7 = this.blockState2;
      boolean var8 = var7.isAir();
      if (!var8) {
         SpeedmineModule var9 = this.speedmineModule;
         BlockPos var10 = this.blockPos;
         BlockState var11 = this.blockState2;
         boolean var12 = var9.testBlockPos(var10, var11, clientPlayerEntity, clientWorld);
         if (var12) {
            PhobosClient var13 = this.getPhobosClient();
            InventoryManager var14 = var13.getInventoryManager();
            Lookup var15 = MethodHandles.lookup();
            Class<?> var16 = this.getClass();
            ClassLoader var17 = var16.getClassLoader();
            MethodType var18 = MethodType.fromMethodDescriptorString(
               "(Lorg/phobos/core/block/SimpleBlockBreaker;Lnet/minecraft/class_638;Lnet/minecraft/class_746;Lnet/minecraft/class_636;Z)Ljava/util/function/Consumer;",
               var17
            );
            Class<?> var19 = this.getClass();
            ClassLoader var20 = var19.getClassLoader();
            MethodType var21 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", var20);
            Class<?> var22 = this.getClass();
            ClassLoader var23 = var22.getClassLoader();
            MethodType var24 = MethodType.fromMethodDescriptorString(
               "(Lnet/minecraft/class_638;Lnet/minecraft/class_746;Lnet/minecraft/class_636;ZLorg/phobos/core/inventory/ItemSwitcher;)V", var23
            );
            MethodHandle var25 = var15.findVirtual(SimpleBlockBreaker.class, "handleClientWorld2", var24);
            Class<?> var26 = this.getClass();
            ClassLoader var27 = var26.getClassLoader();
            MethodType var28 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/inventory/ItemSwitcher;)V", var27);
            Object[] var10000 = new Object[0];
            CallSite var29 = LambdaMetafactory.metafactory(var15, "accept", var18, var21, var25, var28);
            MethodHandle var30 = var29.getTarget();
            if (this != null) {
               var10000 = phobos$g1806f0698;
               if (phobos$g1806f0698 == null) {
                  var10000 = phobos$g1806f0698 = new Object[5];
               }

               var10000[0] = this;
            }

            if (clientWorld != null) {
               var10000 = phobos$g1806f0698;
               if (phobos$g1806f0698 == null) {
                  var10000 = phobos$g1806f0698 = new Object[5];
               }

               var10000[1] = clientWorld;
            }

            if (clientPlayerEntity != null) {
               var10000 = phobos$g1806f0698;
               if (phobos$g1806f0698 == null) {
                  var10000 = phobos$g1806f0698 = new Object[5];
               }

               var10000[2] = clientPlayerEntity;
            }

            if (clientPlayerInteractionManager != null) {
               var10000 = phobos$g1806f0698;
               if (phobos$g1806f0698 == null) {
                  var10000 = phobos$g1806f0698 = new Object[5];
               }

               var10000[3] = clientPlayerInteractionManager;
            }

            Boolean var31 = flag;
            var10000 = phobos$g1806f0698;
            if (phobos$g1806f0698 == null) {
               var10000 = phobos$g1806f0698 = new Object[5];
            }

            var10000[4] = var31;
            Object[] var10001 = phobos$g1806f0698;
            if (phobos$g1806f0698 == null) {
               var10001 = phobos$g1806f0698 = new Object[5];
            }

            Object var32 = var30.invokeWithArguments(var10001);
            var14.handleConsumer((Consumer<ItemSwitcher>)var32, true & true);
         }
      } else {
         this.run2();
      }
   }

   public SimpleBlockBreaker(PhobosClient phobosClient, SpeedmineModule speedmineModule) {
      super(phobosClient, speedmineModule);
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager) {
      if (!clientPlayerEntity.isCreative() && !clientPlayerEntity.isSpectator() && !(this.getFloatValue3() < 1.0F)) {
         if (this.speedmineModule.doubleAir.getValue()) {
            this.speedmineModule.handleClientPlayerInteractionManager(clientPlayerInteractionManager, this.blockPos, this.blockState2);
         }

         DelayedTask.handleLong(this.speedmineModule.reset.getValue(), this::run2);
      }
   }

   public void handleFloat(float f) {
      this.floatValue3 += f;
      this.floatValue3 = MathUtils.computeFloat3(this.floatValue3, 0.0F, 1.0F);
      this.handleFloat2(this.floatValue3);
   }

   public void handleClientWorld2(
      ClientWorld clientWorld,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      boolean flag,
      ItemSwitcher itemSwitcher
   ) {
      this.handleItemSwitcher(itemSwitcher, clientWorld, clientPlayerEntity, clientPlayerInteractionManager, flag);
   }

   @Override
   public void handleClientPlayerEntity4(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager, boolean flag
   ) {
      boolean var5 = this.check();
      if (var5) {
         this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, flag);
      }
   }

   @Override
   public void run2() {
      SilentHotbarModule.getSilentHotbarManager().handleObject2(this);
      this.blockPos = null;
      this.blockState2 = Blocks.AIR.getDefaultState();
      this.handleFloat2(0.0F);
   }

   @Override
   public void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity) {
   }
}
