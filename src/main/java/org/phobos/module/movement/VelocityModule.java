package org.phobos.module.movement;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.TrapModule;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.AttackEvent2;
import org.phobos.event.Listener;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.mixin.net.interfaces.IExplosionS2CPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.combat.AuraModule;

public class VelocityModule extends TrapModule {
   public final Setting<Boolean> combat;
   public final TimeGate.Normal normal3;
   public final Setting<Boolean> lag;
   public final Setting<Boolean> knockback;
   public final Setting<Double> reduceXZ;
   public static final Logger LOGGER2 = LoggerFactory.getLogger(VelocityModule.class);
   public final Setting<Boolean> dBPI;
   public final Setting<Boolean> explosions;
   public final TimeGate.Normal normal4;
   public VelocityModule.FreezeStage freezeStage;
   public final Setting<Double> chance;
   public final Setting<Long> unlag;
   public int intValue;
   public int intValue2;
   public volatile boolean flag;
   public final Setting<Boolean> filter;
   public boolean flag2;
   public final Setting<Double> reduceY;
   public final Setting<Long> time;
   public final Setting<Long> delay;
   public final Setting<VelocityModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", VelocityModule.Mode.Normal));
   public int intValue3;
   public final Setting<Double> combatDistance;
   public final TimeGate.Normal normal5;

   public VelocityModule(PhobosClient phobosClient) {
      super(phobosClient, "Velocity", "Modifies incoming velocity", Categories.movement, 8);
      this.chance = this.resolveSetting2(
         new NumberSetting<>("Chance", 100.0, 0.0, 100.0, "%")
            .resolveSetting3(this.resolveVisibility2(this.mode, VelocityModule.Mode.Watchdog, VelocityModule.Mode.Jump))
      );
      this.knockback = this.resolveSetting2(new BooleanSetting("Knockback", true));
      this.explosions = this.resolveSetting2(
         new BooleanSetting("Explosions", true).resolveSetting3(() -> !this.mode.getValue().equals(VelocityModule.Mode.Watchdog))
      );
      this.filter = this.resolveSetting2(
         new BooleanSetting("Filter", false)
            .resolveSetting4(this.mode, VelocityModule.Mode.Grim)
            .resolveSetting7("Only sends invalid packets after taking velocity")
      );
      this.lag = this.resolveSetting2(new BooleanSetting("Lag", false).resolveSetting7("Temporary disables velocity on lag"));
      this.time = this.resolveSetting2(new LongNumberSetting("Time", 250L, 0L, 5000L, true));
      this.delay = this.resolveSetting2(
         new LongNumberSetting("Delay", 50L, 0L, 500L, true)
            .resolveSetting4(this.mode, VelocityModule.Mode.Freeze)
            .resolveSetting7("Delay between taking velocity and freezing")
      );
      this.unlag = this.resolveSetting2(
         new LongNumberSetting("Unlag", 1000L, 50L, 2000L, true).resolveSetting4(this.mode, VelocityModule.Mode.Freeze).resolveSetting7("Freeze time")
      );
      this.combat = this.resolveSetting2(new BooleanSetting("Combat").resolveSetting7("Whether the lag should be a combat related one"));
      this.combatDistance = this.resolveSetting2(new NumberSetting<>("CombatDistance", 4.0, 2.0, 10.0));
      this.reduceXZ = this.resolveSetting2(
         new NumberSetting<>("ReduceXZ", 0.3, -1.0, 1.0).resolveSetting4(this.mode, VelocityModule.Mode.Reduce).resolveSetting7("Horizontal reduction")
      );
      this.reduceY = this.resolveSetting2(
         new NumberSetting<>("ReduceY", 0.3, -1.0, 1.0).resolveSetting4(this.mode, VelocityModule.Mode.Reduce).resolveSetting7("Vertical reduction")
      );
      this.dBPI = this.resolveSetting2(
         new BooleanSetting("DBPI").resolveSetting7("Ignores explosions in double blast protection").resolveSetting4(this.mode, VelocityModule.Mode.Walls)
      );
      this.intValue2 = 0;
      this.intValue = 0;
      this.intValue3 = 0;
      this.normal5 = new TimeGate.Normal();
      this.normal3 = new TimeGate.Normal();
      this.normal4 = new TimeGate.Normal();
      this.flag2 = false;
      this.freezeStage = VelocityModule.FreezeStage.None;
      this.getSettingGroupList2().forEach(settingGroup -> settingGroup.resolveSettingGroup4(this.mode, VelocityModule.Mode.Block));
      this.resolveSettingGroup(this.lag, this.time);
      this.resolveSettingGroup(this.combat, this.combatDistance).resolveSettingGroup4(this.mode, VelocityModule.Mode.Freeze);
      this.handleListener(new Listener<Send<CommonPongC2SPacket>>() {
         public void handleObject2(Send<CommonPongC2SPacket> send) {
            if (!VelocityModule.this.check8() && VelocityModule.this.mode.getValue().equals(VelocityModule.Mode.Shift)) {
               if (VelocityModule.this.intValue2 > 0) {
                  send.setCancelled(true);
                  VelocityModule.this.intValue2--;
               }
            }
         }
      });
      this.handleListener(
         new SafeListener<Receive<EntityVelocityUpdateS2CPacket>>() {
            public void handleObject(
               Receive<EntityVelocityUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               phobosClient.getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        if (VelocityModule.this.knockback.getValue()) {
                           if (((EntityVelocityUpdateS2CPacket)receive.getPacket()).getEntityId() == clientPlayerEntity.getId()) {
                              if (!VelocityModule.this.check8()) {
                                 if (!PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)
                                    || !itemSwitcher.getSlot3().getStack().isOf(Items.FIREWORK_ROCKET)
                                       && !itemSwitcher.getSlot3().getStack().isOf(Items.FIREWORK_ROCKET)) {
                                    VelocityModule.this.run();
                                    switch ((VelocityModule.Mode)VelocityModule.this.mode.getValue()) {
                                       case Grim:
                                       case Shift:
                                       case BlocksMC:
                                          break;
                                       case Jump:
                                       case Intave:
                                       default:
                                          receive.setCancelled(true);
                                          break;
                                       case Watchdog:
                                          if (!clientPlayerEntity.isOnGround() && !VelocityModule.this.flag2) {
                                             receive.setCancelled(true);
                                             VelocityModule.this.flag2 = true;
                                             return;
                                          }

                                          PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, (int)clientPlayerEntity.getVelocity().getX() * 8000);
                                          PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, (int)clientPlayerEntity.getVelocity().getZ() * 8000);
                                          break;
                                       case Normal:
                                          clientPlayerEntity.networkHandler
                                             .sendPacket(
                                                new ClientCommandC2SPacket(
                                                   clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY
                                                )
                                             );
                                          clientPlayerEntity.networkHandler
                                             .sendPacket(
                                                new ClientCommandC2SPacket(
                                                   clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY
                                                )
                                             );
                                          receive.setCancelled(true);
                                          break;
                                       case Block:
                                          PlayerActionUtils.handleClientPlayerEntity24(
                                             clientPlayerEntity,
                                             ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityX() * VelocityModule.this.reduceXZ.getValue()
                                          );
                                          PlayerActionUtils.handleClientPlayerEntity29(
                                             clientPlayerEntity,
                                             ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityY() * VelocityModule.this.reduceY.getValue()
                                          );
                                          PlayerActionUtils.handleClientPlayerEntity26(
                                             clientPlayerEntity,
                                             ((EntityVelocityUpdateS2CPacket)receive.getPacket()).getVelocityZ() * VelocityModule.this.reduceXZ.getValue()
                                          );
                                          receive.setCancelled(true);
                                          break;
                                       case Reduce:
                                          phobosClient.getDelayedTaskManager().handleRunnable(() -> {
                                             if (VelocityModule.this.freezeStage.equals(VelocityModule.FreezeStage.None)) {
                                                VelocityModule.this.normal4.run();
                                                VelocityModule.this.freezeStage = VelocityModule.FreezeStage.Lag;
                                             }
                                          }, VelocityModule.this.delay.getValue());
                                    }
                                 }
                              }
                           }
                        }
                     }
                  );
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
               if (!receive.isCancelled()) {
                  if (clientPlayerEntity.isOnGround()) {
                     VelocityModule.this.flag2 = false;
                  }

                  if (VelocityModule.this.explosions.getValue()
                     && !VelocityModule.this.mode.getValue().equals(VelocityModule.Mode.Watchdog)
                     && !VelocityModule.this.mode.getValue().equals(VelocityModule.Mode.Jump)) {
                     IExplosionS2CPacket iExplosionS2CPacket = (IExplosionS2CPacket)(Object)receive.getPacket();
                     if (VelocityModule.this.mode.testObject2(VelocityModule.Mode.Walls) && VelocityModule.this.dBPI.getValue()) {
                        iExplosionS2CPacket.setVelocityX(0.0F);
                        iExplosionS2CPacket.setVelocityY(0.0F);
                        iExplosionS2CPacket.setVelocityZ(0.0F);
                     } else if (!VelocityModule.this.check8()) {
                        VelocityModule.this.run();
                        if (!VelocityModule.this.mode.getValue().equals(VelocityModule.Mode.Block)) {
                           iExplosionS2CPacket.setVelocityX(0.0F);
                           iExplosionS2CPacket.setVelocityY(0.0F);
                           iExplosionS2CPacket.setVelocityZ(0.0F);
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((VelocityModule.Mode)VelocityModule.this.mode.getValue()) {
                  case Walls:
                     if (VelocityModule.this.check8()) {
                        return;
                     }

                     if (!VelocityModule.this.mode.getValue().equals(VelocityModule.Mode.Grim)) {
                        return;
                     }

                     if (VelocityModule.this.filter.getValue() && !VelocityModule.this.flag) {
                        return;
                     }

                     BlockPos blockPos = BlockPos.ofFloored(clientPlayerEntity.getPos());
                     BlockPos blockPos2 = blockPos.up();
                     BlockState blockState;
                     if (!(blockState = clientWorld.getBlockState(blockPos)).isAir() && !clientWorld.getBlockState(blockPos2).isAir()) {
                        return;
                     }

                     BlockPos blockPos3 = blockState.isAir() ? blockPos : blockPos2;
                     PlayerActionUtils.handleClientPlayerEntity3(clientPlayerEntity);
                     clientPlayerEntity.networkHandler
                        .sendPacket(
                           new PlayerActionC2SPacket(
                              net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos3, Direction.DOWN
                           )
                        );
                     VelocityModule.this.flag = false;
                     break;
                  case BlocksMC:
                     if (!VelocityModule.this.flag) {
                        return;
                     }

                     VelocityModule.this.intValue++;
                     if (clientPlayerEntity.hurtTime == 2) {
                        VelocityModule.this.intValue3++;
                        if (clientPlayerEntity.isOnGround() && VelocityModule.this.intValue % 2 == 0 && VelocityModule.this.intValue3 <= 10) {
                           clientPlayerEntity.jump();
                           VelocityModule.this.intValue = 0;
                        }

                        VelocityModule.this.flag = false;
                     }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<AttackEvent2>() {
            public void handleObject(
               AttackEvent2 attackEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!attackEvent2.isCancelled()) {
                  switch ((VelocityModule.Mode)VelocityModule.this.mode.getValue()) {
                     case BlocksMC:
                        if (clientPlayerEntity.hurtTime >= 5 && clientPlayerEntity.hurtTime <= 7 && !VelocityModule.this.normal5.testLong(8000L)) {
                           PlayerActionUtils.handleClientPlayerEntity25(clientPlayerEntity, 0.6, 1.0, 0.6);
                        }

                        VelocityModule.this.normal5.run();
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PostReceive<EntityVelocityUpdateS2CPacket>>() {
            public void handleObject(
               PostReceive<EntityVelocityUpdateS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (((EntityVelocityUpdateS2CPacket)postReceive.getPacket()).getEntityId() == clientPlayerEntity.getId()) {
                  switch ((VelocityModule.Mode)VelocityModule.this.mode.getValue()) {
                     case Shift:
                        if (MathUtils.testSupplier2(VelocityModule.this.chance) && clientPlayerEntity.isOnGround()) {
                           clientPlayerEntity.jump();
                        }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<MoveEvent2>(Integer.MIN_VALUE) {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (VelocityModule.this.freezeStage.equals(VelocityModule.FreezeStage.Lag)) {
                  if (VelocityModule.this.normal4.testNumber2(VelocityModule.this.unlag.getValue())) {
                     VelocityModule.this.freezeStage = VelocityModule.FreezeStage.None;
                  } else {
                     if (VelocityModule.this.combat.getValue()) {
                        Entity entity = AuraModule.getEntity();
                        if (entity != null && clientPlayerEntity.distanceTo(entity) > VelocityModule.this.combatDistance.getValue()) {
                        }
                     }

                     if (!moveEvent2.isFlag()) {
                        moveEvent2.setVec3d(Vec3d.ZERO);
                        moveEvent2.setFlag(true);
                     }
                  }
               }
            }
         }
      );
   }

   public void run() {
      this.flag = true;
      this.normal3.run();
      this.intValue2 = 6;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public boolean check8() {
      return this.mode.getValue().equals(VelocityModule.Mode.Walls) && mc.player != null && mc.world != null && !BlockUtils.testPlayerEntity(mc.player, mc.world)
         ? true
         : this.lag.getValue() && !this.phobosClient.getTickManager().testLong(this.time.getValue());
   }

   @Override
   public void run3() {
      this.intValue2 = 0;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.normal3.testLong(1000L)
         && this.mode.getValue().equals(VelocityModule.Mode.Block)
         && EntityUtils.resolvePlayerEntity(clientWorld, clientPlayerEntity, this.phobosClient, 6.0) != null) {
         this.handlePlacementModule(this, itemSwitcher, clientPlayerEntity, clientWorld, playerEntity -> playerEntity.equals(clientPlayerEntity));
      }
   }

   private enum FreezeStage {
      None,
      Lag;
   }

   private enum Mode implements TextProvider {
      Normal("Vanilla velocity (0%, 0%)"),
      Grim("Grim 1.17 velocity (0%, 0%)"),
      Block("Attempts to add reducing collisions by placing blocks (100%, 100%)"),
      Shift("Delays transactions (0%, 0%)"),
      Watchdog("Watchdog velocity, will sometimes launch you up (0%, ?%)"),
      Jump("Jump reset (100%, 100%)"),
      Walls("Vanilla velocity that only works if you're inside of a wall (0%, 0%)"),
      BlocksMC("BlocksMC (Verus) velocity (0%, 0%)"),
      Intave("Intave 0.6 hit-reduction velocity (100%, 100%)"),
      Reduce("Simple reduction (?%, ?%)"),
      Freeze("Lags you right after you took velocity to land additional hits (100%, 100%)");

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
