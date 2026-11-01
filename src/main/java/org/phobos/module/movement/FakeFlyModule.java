package org.phobos.module.movement;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.mixin.game.entity.IFireworkRocketEntity;
import org.phobos.module.Module;

public class FakeFlyModule extends Module {
   public final Setting<Long> delay = this.resolveSetting2(new LongNumberSetting("Delay", 0L, 0L, 1000L));
   public final Setting<Boolean> inventory;
   public final Setting<Boolean> control;
   public final Setting<Boolean> keys;
   public final Setting<Boolean> firework;
   public final Setting<Boolean> ground;
   public final TimeGate.Normal normal;
   public final Setting<Double> horizontal;
   public final ElytraManager.FlyConfiguration.Factory factory;
   public final Setting<ElytraManager.EquipType> type = this.resolveSetting2(new EnumSetting<>("Type", ElytraManager.EquipType.Default));
   public final Setting<Boolean> water;
   public final Setting<Double> vertical;
   public static FakeFlyModule fakeFlyModule;

   @Override
   public void run3() {
      if (this.firework.getValue()) {
         this.phobosClient.getElytraManager().handleInt(0);
      }
   }

   public static FakeFlyModule getFakeFlyModule() {
      return fakeFlyModule;
   }

   public Vec3d createVec3d(ClientPlayerEntity clientPlayerEntity, float f) {
      double d = clientPlayerEntity.input.movementForward;
      double d2 = clientPlayerEntity.input.movementSideways;
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

   public FakeFlyModule(PhobosClient phobosClient) {
      super(phobosClient, "FakeFly", Categories.movement, "Makes you glide with a chestplate on");
      this.keys = this.resolveSetting2(new BooleanSetting("Keys", true).resolveSetting7("Only performs the exloit on movement keys"));
      this.ground = this.resolveSetting2(new BooleanSetting("Ground").resolveSetting7("Allows you to start flying on ground"));
      this.water = this.resolveSetting2(new BooleanSetting("Water").resolveSetting7("Allows you to start flying in water"));
      this.inventory = this.resolveSetting2(new BooleanSetting("Inventory").resolveSetting7("Allows you to silent elytra from inventory"));
      this.firework = this.resolveSetting2(new BooleanSetting("Firework").resolveSetting7("Uses a firework when enabled"));
      this.control = this.resolveSetting2(new BooleanSetting("Control"));
      this.horizontal = this.resolveSetting2(new NumberSetting<>("Horizontal", 2.0, 0.5, 2.5));
      this.vertical = this.resolveSetting2(new NumberSetting<>("Vertical", 2.0, 0.5, 2.5));
      this.normal = new TimeGate.Normal();
      this.factory = ElytraManager.FlyConfiguration.createFactory()
         .resolveFactory5(this.ground)
         .resolveFactory4(this.water)
         .resolveFactory2(this.inventory)
         .resolveFactory3(this.delay)
         .resolveFactory7(this.type)
         .getFactory2()
         .resolveFactory8(3);
      this.resolveSettingGroup(this.control, this.horizontal, this.vertical);
      this.handleListener(
         new SafeListener<PlayerTickEvent>(-5) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!FakeFlyModule.this.keys.getValue() || PlayerUtils.check()) {
                  float f = PlayerUtils.computeClientPlayerEntity6(clientPlayerEntity);
                  phobosClient.getElytraManager().testFlyConfiguration2(FakeFlyModule.this.factory.getFlyConfiguration());
                  if (FakeFlyModule.this.control.getValue()) {
                     if (!RotationManager.isFlag()) {
                        MutableBoolean mutableBoolean = new MutableBoolean();
                        Iterator flag = clientWorld.getEntities().iterator();

                        while (true) {
                           if (flag.hasNext()) {
                              Entity entity = (Entity)flag.next();
                              if (!(entity instanceof IFireworkRocketEntity iFireworkRocketEntity)
                                 || !(iFireworkRocketEntity.getShooter() instanceof ClientPlayerEntity)) {
                                 continue;
                              }

                              mutableBoolean.setTrue();
                              break;
                           }

                           if (!FakeFlyModule.this.normal.testLong(500L)) {
                              mutableBoolean.setValue(!FakeFlyModule.this.normal.testLong(75L));
                           } else {
                              FakeFlyModule.this.normal.run();
                              phobosClient.getElytraManager().handleInt(0);
                              mutableBoolean.setTrue();
                           }
                           break;
                        }

                        if (!mutableBoolean.isFalse()) {
                           boolean flagx = mc.options.jumpKey.isPressed();
                           boolean flag2 = mc.options.sneakKey.isPressed();
                           float f2 = flagx && flag2
                              ? 0.0F
                              : (flagx ? (PlayerUtils.check() ? -45.0F : -90.0F) : (flag2 ? (PlayerUtils.check() ? 45.0F : 90.0F) : 0.0F));
                           phobosClient.getRotationManager().testFloat(f, f2);
                           Vec3d vec3d = FakeFlyModule.this.createVec3d(clientPlayerEntity, f);
                           clientPlayerEntity.setVelocity(
                              new Vec3d(
                                 vec3d.x * FakeFlyModule.this.horizontal.getValue(),
                                 PlayerUtils.computeFlag(flagx, flag2) * FakeFlyModule.this.vertical.getValue(),
                                 vec3d.z * FakeFlyModule.this.horizontal.getValue()
                              )
                           );
                        }
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
               if (!FakeFlyModule.this.control.getValue()) {
                  if (!tickEvent.isCancelled() && phobosClient.getElytraManager().isFlag()) {
                     if (FakeFlyModule.this.keys.getValue() && !PlayerUtils.check()) {
                        return;
                     }

                     clientPlayerEntity.input.movementForward = 1.0F;
                     clientPlayerEntity.input.movementSideways = 0.0F;
                  }
               }
            }
         }
      );
      fakeFlyModule = this;
   }

   @Override
   public String nothing() {
      return this.type.getValue().name().replace("_", "");
   }
}
