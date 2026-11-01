package org.phobos.module.movement;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.IFireworkRocketEntity;
import org.phobos.module.Module;
import org.phobos.module.render.FreeLookModule;

public class LongJumpModule extends Module {
   public Setting<Double> doubleValueSetting;
   public Setting<Float> floatValueSetting;
   public int intValue;
   public int intValue2;
   public double doubleValue;
   public static LongJumpModule longJumpModule;
   public ElytraManager.FlyConfiguration flyConfiguration;
   public Setting<LongJumpModule.Mode> modeSetting;
   public Setting<Float> floatValueSetting2;
   public double doubleValue2;
   public Setting<Integer> integerSetting;

   @Override
   public void run4() {
      this.intValue = 0;
      this.doubleValue = 0.0;
   }

   public static LongJumpModule getLongJumpModule() {
      return longJumpModule;
   }

   public LongJumpModule(PhobosClient phobosClient) {
      super(phobosClient, "LongJump", Categories.movement, "Extends your jump");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      LongJumpModule.Mode var3 = LongJumpModule.Mode.Normal;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      Float var6 = (float)0.1F;
      Float var7 = (float)0.1F;
      Float var8 = (float)10.0F;
      NumberSetting var5 = new NumberSetting<>("Boost", var6, var7, var8);
      Setting var9 = this.modeSetting;
      LongJumpModule.Mode var10 = LongJumpModule.Mode.Normal;
      Setting var11 = var5.resolveSetting4(var9, var10);
      Setting var12 = var11.resolveSetting7("Movement speed");
      Setting var13 = this.resolveSetting2(var12);
      this.floatValueSetting2 = var13;
      Double var15 = 1.2;
      Double var16 = 0.0;
      Double var17 = 3.0;
      NumberSetting var14 = new NumberSetting<>("Power", var15, var16, var17);
      Setting var18 = this.modeSetting;
      LongJumpModule.Mode var19 = LongJumpModule.Mode.Boat;
      Setting var20 = var14.resolveSetting4(var18, var19);
      Setting var21 = var20.resolveSetting7("Offset boost");
      Setting var22 = this.resolveSetting2(var21);
      this.doubleValueSetting = var22;
      Integer var24 = 2;
      Integer var25 = 1;
      Integer var26 = 10;
      NumberSetting var23 = new NumberSetting<>("Ticks", var24, var25, var26);
      Setting var27 = this.modeSetting;
      LongJumpModule.Mode var28 = LongJumpModule.Mode.Spoof;
      Setting var29 = var23.resolveSetting4(var27, var28);
      Setting var30 = this.resolveSetting2(var29);
      this.integerSetting = var30;
      Float var32 = (float)30.0F;
      Float var33 = (float)0.0F;
      Float var34 = (float)80.0F;
      NumberSetting var31 = new NumberSetting<>("Angle", var32, var33, var34);
      Setting var35 = this.modeSetting;
      LongJumpModule.Mode var36 = LongJumpModule.Mode.Spoof;
      Setting var37 = var31.resolveSetting4(var35, var36);
      Setting var38 = this.resolveSetting2(var37);
      this.floatValueSetting = var38;
      ElytraManager.FlyConfiguration.Factory var39 = ElytraManager.FlyConfiguration.createFactory();
      ElytraManager.FlyConfiguration.Factory var40 = var39.resolveFactory6(0L);
      ElytraManager.FlyConfiguration.Factory var41 = var40.getFactory2();
      ElytraManager.FlyConfiguration.Factory var42 = var41.resolveFactory8(2);
      ElytraManager.FlyConfiguration.Factory var43 = var42.getFactory4();
      ElytraManager.FlyConfiguration var44 = var43.getFlyConfiguration2();
      this.flyConfiguration = var44;
      this.intValue2 = 0;
      longJumpModule = this;
      var var45 = new SafeListener<TickEvent2>() {
         public void handleObject(
            TickEvent2 tickEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((LongJumpModule.Mode)LongJumpModule.this.modeSetting.getValue()) {
               case Boat:
                  double d4 = clientPlayerEntity.getX() - clientPlayerEntity.prevX;
                  double d5 = clientPlayerEntity.getZ() - clientPlayerEntity.prevZ;
                  LongJumpModule.this.doubleValue = Math.sqrt(d4 * d4 + d5 * d5);
                  break;
               case Normal:
                  for (Entity entity : clientWorld.getEntities()) {
                     if (!entity.isRemoved() && entity.getType().equals(EntityType.BOAT) && !(clientPlayerEntity.squaredDistanceTo(entity) > 4.0)) {
                        float[] floats = RotationUtils.values(clientPlayerEntity, entity);
                        float f = floats[0];
                        double d = Math.cos(Math.toRadians(f + 180.0F) + 90.0);
                        double d2 = Math.sin(Math.toRadians(f + 180.0F) + 90.0);
                        double d3 = LongJumpModule.this.doubleValueSetting.getValue();
                        Vec3d vec3d = clientPlayerEntity.getVelocity();
                        clientPlayerEntity.setVelocity(vec3d.x + d3 * d, d3, vec3d.z + d3 * d2);
                        break;
                     }
                  }
            }
         }
      };
      this.handleListener(var45);
      var var46 = new SafeListener<MoveEvent2>() {
         public void handleObject(
            MoveEvent2 moveEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((LongJumpModule.Mode)LongJumpModule.this.modeSetting.getValue()) {
               case Boat:
                  if (!PlayerUtils.check()) {
                     return;
                  } else {
                     double d4 = 1.0;
                     double d5 = 1.0;
                     if (clientPlayerEntity.hasStatusEffect(StatusEffects.SPEED)) {
                        double d = clientPlayerEntity.getStatusEffect(StatusEffects.SPEED).getAmplifier();
                        d4 = 1.0 + 0.2 * (d + 1.0);
                     }

                     if (clientPlayerEntity.hasStatusEffect(StatusEffects.SLOWNESS)) {
                        double d2 = clientPlayerEntity.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
                        d5 = 1.0 + 0.2 * (d2 + 1.0);
                     }

                     double d6 = 0.2873F * d4 / d5;
                     if (LongJumpModule.this.intValue == 0) {
                        LongJumpModule.this.intValue = 1;
                        LongJumpModule.this.doubleValue2 = LongJumpModule.this.floatValueSetting2.getValue().floatValue() * d6 - 0.01;
                     } else if (LongJumpModule.this.intValue == 1) {
                        LongJumpModule.this.intValue = 2;
                        PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.42);
                        moveEvent2.setDoubleValue(0.42);
                        LongJumpModule.this.doubleValue2 *= 2.149;
                     } else if (LongJumpModule.this.intValue == 2) {
                        LongJumpModule.this.intValue = 3;
                        double d3 = 0.66 * (LongJumpModule.this.doubleValue - d6);
                        LongJumpModule.this.doubleValue2 = LongJumpModule.this.doubleValue - d3;
                     } else {
                        if (!clientWorld.isSpaceEmpty(
                              clientPlayerEntity, clientPlayerEntity.getBoundingBox().offset(0.0, clientPlayerEntity.getVelocity().getY(), 0.0)
                           )
                           || clientPlayerEntity.verticalCollision) {
                           LongJumpModule.this.intValue = 0;
                        }

                        LongJumpModule.this.doubleValue2 = LongJumpModule.this.doubleValue - LongJumpModule.this.doubleValue / 159.0;
                     }

                     LongJumpModule.this.doubleValue2 = Math.max(LongJumpModule.this.doubleValue2, d6);
                     moveEvent2.setCancelled(true);
                     float[] floats = PlayerUtils.values3(clientPlayerEntity, (float)LongJumpModule.this.doubleValue2);
                     moveEvent2.setDoubleValue3(floats[0]);
                     moveEvent2.setDoubleValue2(floats[1]);
                  }
               case Normal:
            }
         }
      };
      this.handleListener(var46);
      var var47 = new SafeListener<TickEvent>() {
         public void handleObject(
            TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (LongJumpModule.this.modeSetting.testObject2(LongJumpModule.Mode.Spoof)) {
               if (clientPlayerEntity.isOnGround()) {
                  LongJumpModule.this.intValue2 = 0;
               }

               tickEvent.getKeyboardInput().jumping = true;
            }
         }
      };
      this.handleListener(var47);
      var var48 = new SafeListener<PlayerTickEvent>() {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (LongJumpModule.this.modeSetting.testObject2(LongJumpModule.Mode.Spoof)) {
               var1.getElytraManager().testFlyConfiguration2(LongJumpModule.this.flyConfiguration);
               var1.getRotationManager()
                  .testFloat(FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), -LongJumpModule.this.floatValueSetting.getValue());
               Iterator var5x = clientWorld.getEntities().iterator();

               while (true) {
                  if (var5x.hasNext()) {
                     Entity entity = (Entity)var5x.next();
                     if (!(entity instanceof FireworkRocketEntity fireworkRocketEntity)) {
                        continue;
                     }

                     IFireworkRocketEntity iFireworkRocketEntity = (IFireworkRocketEntity)(Object)fireworkRocketEntity;
                     if (iFireworkRocketEntity.getShooter() == null || !clientPlayerEntity.equals(iFireworkRocketEntity.getShooter())) {
                        continue;
                     }
                     break;
                  }

                  var1.getElytraManager().handleInt(0);
                  break;
               }

               if (LongJumpModule.this.intValue2++ >= LongJumpModule.this.integerSetting.getValue()) {
                  LongJumpModule.this.run11();
               }
            }
         }
      };
      this.handleListener(var48);
   }

   @Override
   public String nothing() {
      return String.valueOf(this.modeSetting.getValue());
   }

   @Override
   public void run3() {
      this.intValue2 = 0;
   }

   private enum Mode implements TextProvider {
      Normal("Normal forward longjump"),
      Boat("Jumps off a boat nearby"),
      Spoof("Spoof");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
