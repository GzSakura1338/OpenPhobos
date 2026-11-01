package org.phobos.module.movement;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.EndTickEvent;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.IClientPlayerEntity;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.Module;

public class FlightModule extends Module {
   public Setting<FlightModule.Mode> modeSetting;
   public Setting<Integer> integerSetting;
   public int intValue;
   public Setting<FlightModule.Ground> groundSetting;
   public Setting<Double> doubleValueSetting;
   public Setting<Integer> integerSetting2;
   public int intValue2;
   public Normal normal;
   public double doubleValue;
   public Setting<Boolean> booleanValueSetting;
   public Normal normal2;
   public static FlightModule flightModule;
   public Setting<Float> floatValueSetting;

   public boolean testDouble(double d, double d2) {
      return d >= d2 ? true : d2 - d < 0.0313;
   }

   public void handlePlayerMoveC2SPacket(PlayerMoveC2SPacket playerMoveC2SPacket, ClientPlayerEntity clientPlayerEntity, double d) {
      if (this.intValue <= 0 && this.doubleValue != Double.MAX_VALUE && this.testDouble(d, this.doubleValue) && this.testEntity(clientPlayerEntity)) {
         ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setY(this.doubleValue - 0.0313);
      } else {
         this.doubleValue = d;
      }
   }

   public double computeFlag(boolean flag, boolean flag2) {
      if (flag && flag2) {
         return 0.0;
      } else if (flag) {
         return 1.0;
      } else {
         return flag2 ? -1.0 : 0.0;
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.modeSetting.getValue());
   }

   public boolean testEntity(Entity entity) {
      return entity.getWorld().getStatesInBox(entity.getBoundingBox().expand(0.0625).stretch(0.0, -0.55, 0.0)).allMatch(AbstractBlockState::isAir);
   }

   public static FlightModule getFlightModule() {
      return flightModule;
   }

   public FlightModule(PhobosClient phobosClient) {
      super(phobosClient, "Flight", Categories.movement, "Makes you fly");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      FlightModule.Mode var3 = FlightModule.Mode.Vanilla;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      Double var6 = 2.0;
      Double var7 = 0.0;
      Double var8 = 20.0;
      NumberSetting var5 = new NumberSetting<>("Speed", var6, var7, var8);
      Setting var9 = this.modeSetting;
      FlightModule.Mode var10 = FlightModule.Mode.Vanilla;
      Setting var11 = var5.resolveSetting4(var9, var10);
      Setting var12 = var11.resolveSetting7("Movement speed");
      Setting var13 = this.resolveSetting2(var12);
      this.doubleValueSetting = var13;
      Float var15 = (float)1.0F;
      Float var16 = (float)0.0F;
      Float var17 = (float)1.0F;
      NumberSetting var14 = new NumberSetting<>("Vertical", var15, var16, var17);
      Setting var18 = this.modeSetting;
      FlightModule.Mode var19 = FlightModule.Mode.Vanilla;
      Setting var20 = var14.resolveSetting4(var18, var19);
      Setting var21 = var20.resolveSetting7("Vertical factor");
      Setting var22 = this.resolveSetting2(var21);
      this.floatValueSetting = var22;
      FlightModule.Ground var24 = FlightModule.Ground.Default;
      EnumSetting var23 = new EnumSetting<>("Ground", var24);
      Setting var25 = var23.resolveSetting7("Spoofs ground");
      Setting var26 = this.resolveSetting2(var25);
      this.groundSetting = var26;
      BooleanSetting var27 = new BooleanSetting("Anti-Kick");
      Setting var28 = this.resolveSetting2(var27);
      this.booleanValueSetting = var28;
      Integer var30 = 20;
      Integer var31 = 1;
      Integer var32 = 200;
      NumberSetting var29 = new NumberSetting<>("Anti-KickDelay", var30, var31, var32);
      Setting var33 = this.resolveSetting2(var29);
      this.integerSetting2 = var33;
      Integer var35 = 1;
      Integer var36 = 1;
      Integer var37 = 20;
      NumberSetting var34 = new NumberSetting<>("Anti-KickSync", var35, var36, var37);
      Setting var38 = this.resolveSetting2(var34);
      this.integerSetting = var38;
      Normal var39 = new Normal();
      this.normal2 = var39;
      Normal var40 = new Normal();
      this.normal = var40;
      Setting var41 = this.integerSetting2;
      Object var42 = var41.getValue();
      boolean var43 = var42 instanceof Integer;
      if (var42 != null && !var43) {
         throw new ClassCastException("CHECKCAST failed");
      }

      int var44 = (Integer)var42;
      this.intValue = var44;
      Setting var45 = this.integerSetting;
      Object var46 = var45.getValue();
      boolean var47 = var46 instanceof Integer;
      if (var46 != null && !var47) {
         throw new ClassCastException("CHECKCAST failed");
      }

      int var48 = (Integer)var46;
      this.intValue2 = var48;
      this.doubleValue = Double.MAX_VALUE;
      Setting var49 = this.booleanValueSetting;
      Setting[] var50 = new Setting[2];
      Setting var51 = this.integerSetting2;
      var50[0] = var51;
      Setting var52 = this.integerSetting;
      var50[1] = var52;
      SettingGroup var53 = this.resolveSettingGroup(var49, var50);
      Setting var54 = this.modeSetting;
      FlightModule.Mode var55 = FlightModule.Mode.Vanilla;
      var53.resolveSettingGroup4(var54, var55);
      var var56 = new SafeListener<MoveEvent2>(1000) {
         public void handleObject(
            MoveEvent2 moveEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            boolean flag = mc.options.jumpKey.isPressed();
            boolean flag2 = mc.options.sneakKey.isPressed();
            double d = FlightModule.this.computeFlag(flag, flag2);
            switch ((FlightModule.Mode)FlightModule.this.modeSetting.getValue()) {
               case Sentinel:
                  Vec3d vec3d = FlightModule.this.createVec3d2(clientPlayerEntity);
                  moveEvent2.setVec3d(
                     new Vec3d(
                        vec3d.x * FlightModule.this.doubleValueSetting.getValue(),
                        d * FlightModule.this.doubleValueSetting.getValue() * FlightModule.this.floatValueSetting.getValue().floatValue(),
                        vec3d.z * FlightModule.this.doubleValueSetting.getValue()
                     )
                  );
                  break;
               case Radon:
                  Vec3d vec3d2 = FlightModule.this.createVec3d2(clientPlayerEntity);
                  moveEvent2.setVec3d(new Vec3d(vec3d2.x * 0.5, d * 0.3, vec3d2.z * 0.5));
            }
         }
      };
      this.handleListener(var56);
      var var57 = new SafeListener<Send<?>>() {
         public void handleObject(
            Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
               switch ((FlightModule.Ground)FlightModule.this.groundSetting.getValue()) {
                  case Disabled:
                  default:
                     break;
                  case Enabled:
                     ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setOnGround(true);
                     break;
                  case Default:
                     ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setOnGround(false);
               }
            }
         }
      };
      this.handleListener(var57);
      var var58 = new SafeListener<TickEvent2>() {
         public void handleObject(
            TickEvent2 tickEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((FlightModule.Mode)FlightModule.this.modeSetting.getValue()) {
               case Vanilla:
                  if (clientPlayerEntity.isOnGround()) {
                     return;
                  } else if (!FlightModule.this.normal2.testLong(300L)) {
                     return;
                  } else {
                     PlayerActionUtils.handleClientPlayerEntity29(
                        clientPlayerEntity, clientPlayerEntity.isSneaking() ? -0.4 : (clientPlayerEntity.input.jumping ? 0.42 : 0.2)
                     );
                     float[] floats = PlayerUtils.values3(clientPlayerEntity, MathUtils.computeFloat8(0.33F, 0.34F));
                     PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, floats[0]);
                     PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, floats[1]);
                     FlightModule.this.normal2.run();
                  }
            }
         }
      };
      this.handleListener(var58);
      var var59 = new SafeListener<Receive<ExplosionS2CPacket>>() {
         public void handleObject(
            Receive<ExplosionS2CPacket> receive,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (!receive.isCancelled()) {
               FlightModule.this.normal.run();
            }
         }
      };
      this.handleListener(var59);
      var var60 = new SafeListener<TickEvent>() {
         public void handleObject(
            TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((FlightModule.Mode)FlightModule.this.modeSetting.getValue()) {
               case Sentinel:
               case Radon:
                  tickEvent.getKeyboardInput().sneaking = false;
            }
         }
      };
      this.handleListener(var60);
      var var61 = new SafeListener<EndTickEvent>() {
         public void handleObject(
            EndTickEvent endTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (FlightModule.this.modeSetting.testObject2(FlightModule.Mode.Vanilla) && FlightModule.this.booleanValueSetting.getValue()) {
               if (FlightModule.this.intValue > 0) {
                  FlightModule.this.intValue--;
               }

               if (FlightModule.this.intValue2 <= 0 && FlightModule.this.intValue <= 0) {
                  FlightModule.this.intValue = FlightModule.this.integerSetting2.getValue();
                  FlightModule.this.intValue2 = FlightModule.this.integerSetting.getValue();
                  ((IClientPlayerEntity)(Object)clientPlayerEntity).setTicksSinceLastPosition(20);
               } else {
                  if (FlightModule.this.intValue <= 0) {
                     if (FlightModule.this.intValue2 == FlightModule.this.integerSetting.getValue()) {
                        ((IClientPlayerEntity)(Object)clientPlayerEntity).setTicksSinceLastPosition(20);
                     }

                     FlightModule.this.intValue2--;
                  }
               }
            }
         }
      };
      this.handleListener(var61);
      var var62 = new SafeListener<Send<?>>() {
         public void handleObject(
            Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (FlightModule.this.modeSetting.testObject2(FlightModule.Mode.Vanilla) && FlightModule.this.booleanValueSetting.getValue()) {
               if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
                  double d = playerMoveC2SPacket.getY(Double.MAX_VALUE);
                  if (d != Double.MAX_VALUE) {
                     FlightModule.this.handlePlayerMoveC2SPacket(playerMoveC2SPacket, clientPlayerEntity, d);
                  } else {
                     PlayerMoveC2SPacket playerMoveC2SPacket2;
                     if (playerMoveC2SPacket.changesLook()) {
                        playerMoveC2SPacket2 = new Full(
                           clientPlayerEntity.getX(),
                           clientPlayerEntity.getY(),
                           clientPlayerEntity.getZ(),
                           playerMoveC2SPacket.getYaw(0.0F),
                           playerMoveC2SPacket.getPitch(0.0F),
                           playerMoveC2SPacket.isOnGround()
                        );
                     } else {
                        playerMoveC2SPacket2 = new PositionAndOnGround(
                           clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), playerMoveC2SPacket.isOnGround()
                        );
                     }

                     send.setCancelled(true);
                     FlightModule.this.handlePlayerMoveC2SPacket(playerMoveC2SPacket2, clientPlayerEntity, clientPlayerEntity.getY());
                     SkyboxTextureUtils.handlePacket(playerMoveC2SPacket2);
                  }
               }
            }
         }
      };
      this.handleListener(var62);
      flightModule = this;
   }

   public Vec3d createVec3d2(ClientPlayerEntity clientPlayerEntity) {
      double d = clientPlayerEntity.input.movementForward;
      double d2 = clientPlayerEntity.input.movementSideways;
      float f = clientPlayerEntity.prevYaw + (clientPlayerEntity.getYaw() - clientPlayerEntity.prevYaw) * mc.getRenderTickCounter().getTickDelta(false);
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

   private enum Ground {
      Default,
      Enabled,
      Disabled;
   }

   private enum Mode {
      Vanilla,
      Radon,
      Sentinel;
   }
}
