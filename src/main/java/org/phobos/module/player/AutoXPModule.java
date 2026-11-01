package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AutoXPModule extends Module {
   public static AutoXPModule autoXPModule;
   public final Setting<Float> stopPercentage;
   public final Setting<Integer> packets = this.resolveSetting2(new NumberSetting<>("Packets", 1, 1, 10));
   public final Setting<Boolean> stop;
   public final Setting<InteractionManager.Silent> switchValue = this.resolveSetting2(new EnumSetting<>("Switch", InteractionManager.Silent.Normal));
   public final Setting<AutoXPModule.Rotate> rotate;
   public final Setting<AutoXPModule.Direction> direction = this.resolveSetting2(new EnumSetting<>("Direction", AutoXPModule.Direction.Facing));

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      if (!this.stop.getValue()) {
         return false;
      }

      for (ItemStack itemStack : clientPlayerEntity.getArmorItems()) {
         if (!itemStack.isEmpty()) {
            float f = InventoryUtils.computeItemStack(itemStack);
            if (f < this.stopPercentage.getValue()) {
               return false;
            }
         }
      }

      ItemStack itemStack2 = clientPlayerEntity.getMainHandStack();
      ItemStack itemStack3 = clientPlayerEntity.getOffHandStack();
      if (!itemStack2.isEmpty()) {
         float f2 = InventoryUtils.computeItemStack(itemStack2);
         if (f2 < this.stopPercentage.getValue()) {
            return false;
         }
      }

      if (!itemStack3.isEmpty()) {
         float f3 = InventoryUtils.computeItemStack(itemStack3);
         return !(f3 < this.stopPercentage.getValue());
      } else {
         return true;
      }
   }

   public AutoXPModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoXP", Categories.player, "Automatically throws exp");
      this.rotate = this.resolveSetting2(
         new EnumSetting<>("Rotate", AutoXPModule.Rotate.Normal)
            .resolveSetting8(this.direction, AutoXPModule.Direction.Correction, AutoXPModule.Direction.Down, AutoXPModule.Direction.Smart)
      );
      this.stop = this.resolveSetting2(new BooleanSetting("Stop").resolveSetting7("Prevents you from XPing when theres nothing to mend"));
      this.stopPercentage = this.resolveSetting2(new NumberSetting<>("StopPercentage", 100.0F, 0.1F, 100.0F, "%"));
      this.resolveSettingGroup(this.stop, this.stopPercentage);
      this.handleListener(
         new SafeListener<PlayerTickEvent>(2) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!AutoXPModule.this.testClientPlayerEntity(clientPlayerEntity)) {
                  phobosClient.getInventoryManager()
                     .handleConsumer2(
                        itemSwitcher -> {
                           SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(
                              Hand.MAIN_HAND, Items.EXPERIENCE_BOTTLE, AutoXPModule.this.switchValue.getValue().getFlag() | 1
                           );
                           if (switchResult != null) {
                              boolean flag = RotationManager.isFlag();
                              if (!flag
                                 || AutoXPModule.this.direction.testObject2(AutoXPModule.Direction.Facing)
                                 || !AutoXPModule.this.rotate.testObject2(AutoXPModule.Rotate.Normal)) {
                                 float[] floats = AutoXPModule.this.direction.getValue().evaluate(phobosClient, clientPlayerEntity, clientWorld);
                                 if (floats.length != 0) {
                                    AutoXPModule.this.rotate.getValue().rotate(phobosClient, clientPlayerEntity, floats);
                                 }

                                 float f = floats.length == 0 ? clientPlayerEntity.getYaw() : floats[0];
                                 float f2 = floats.length == 0 ? clientPlayerEntity.getPitch() : floats[1];

                                 for (int i = 0; i < AutoXPModule.this.packets.getValue(); i++) {
                                    TickUtils.handleClientWorld(
                                       clientWorld,
                                       integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, f, f2))
                                    );
                                 }

                                 if (floats.length != 0) {
                                    AutoXPModule.this.rotate.getValue().reset(phobosClient, clientPlayerEntity);
                                 }
                              }
                           }
                        }
                     );
               }
            }
         }
      );
      autoXPModule = this;
   }

   public static AutoXPModule getAutoXPModule() {
      return autoXPModule;
   }

   private enum Direction implements TextProvider {
      Facing("Throws XP as you want"),
      Correction("Ensures you're XPing the way you want") {
         @Override
         public float[] evaluate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world) {
            return new float[]{player.getYaw(), player.getPitch()};
         }
      },
      Down("Throws XP down") {
         @Override
         public float[] evaluate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world) {
            return new float[]{phobos.getRotationManager().computeFloat5(player.getYaw()), 90.0F};
         }
      },
      Smart("XPs close to you but far from enemies") {
         @Override
         public float[] evaluate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world) {
            PlayerEntity target = EntityUtils.resolvePlayerEntity(world, player, phobos, 4.0);
            if (target == null) {
               return new float[]{phobos.getRotationManager().computeFloat5(player.getYaw()), 90.0F};
            }

            float[] orientations = new float[]{0.0F, 45.0F, 90.0F, 135.0F, 180.0F, -135.0F, -90.0F, -45.0F};
            float[] positions = new float[]{0.0F, 45.0F, -45.0F};
            Vec3d start = player.getEyePos();
            MutableObject<float[]> bestRotation = new MutableObject(new float[0]);
            MutableObject<Double> bestDelta = new MutableObject(Double.MAX_VALUE);

            for (float position : positions) {
               for (float orientation : orientations) {
                  Vec3d end = start.add(MathUtils.createVec3d4(orientation, position).multiply(3.0));
                  BlockHitResult blockHitResult = world.raycast(new RaycastContext(start, end, ShapeType.COLLIDER, FluidHandling.NONE, player));
                  if (blockHitResult != null && blockHitResult.getPos() != null) {
                     Vec3d pos = blockHitResult.getPos();
                     double playerDistance = player.getBoundingBox().squaredMagnitude(pos);
                     double targetDistance = target.getBoundingBox().squaredMagnitude(pos);
                     if (!(targetDistance <= playerDistance)) {
                        double delta = playerDistance - targetDistance;
                        if (!(delta >= (Double)bestDelta.getValue())) {
                           bestRotation.setValue(new float[]{orientation, position});
                           bestDelta.setValue(delta);
                        }
                     }
                  }
               }
            }

            return (float[])bestRotation.getValue();
         }
      };

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      public float[] evaluate(PhobosClient phobos, ClientPlayerEntity player, ClientWorld world) {
         return new float[0];
      }

      Direction(final String description) {
         this.text = description;
      }
   }

   private enum Rotate implements TextProvider {
      Silent("Silent rotation") {
         @Override
         public void reset(PhobosClient phobos, ClientPlayerEntity player) {
            phobos.getRotationManager().handleClientPlayerEntity5(player);
         }

         @Override
         public void rotate(PhobosClient phobos, ClientPlayerEntity player, float[] rotation) {
            phobos.getRotationManager().handleClientPlayerEntity3(player, rotation[0], rotation[1]);
         }
      },
      Normal("Ensures that you're looking down") {
         @Override
         public void rotate(PhobosClient phobos, ClientPlayerEntity player, float[] rotation) {
            phobos.getRotationManager().testFloat(rotation[0], rotation[1]);
         }
      };

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      public void reset(PhobosClient phobos, ClientPlayerEntity player) {
      }

      Rotate(final String description) {
         this.text = description;
      }

      public abstract void rotate(PhobosClient var1, ClientPlayerEntity var2, float[] var3);
   }
}
