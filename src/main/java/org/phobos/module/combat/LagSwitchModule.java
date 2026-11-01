package org.phobos.module.combat;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.net.LagState;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.AttackEvent2;
import org.phobos.event.MoveEvent2;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;
import org.phobos.module.player.ScaffoldModule;

public class LagSwitchModule extends Module {
   public final Setting<Boolean> invisible;
   public final Set<Packet<?>> packetSet;
   public final Setting<Double> maxRange;
   public int intValue;
   public final Setting<Boolean> velocity;
   public final Setting<Integer> lag;
   public final Setting<Long> delay;
   public final Setting<Boolean> debug;
   public LagSwitchModule.State state;
   public final Setting<Boolean> scaffold;
   public final Setting<Boolean> resync;
   public final Setting<Integer> timer;
   public final Setting<Boolean> pause;
   public final Setting<Boolean> ground;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> reset;
   public final Setting<Double> minRange;
   public final Setting<LagSwitchModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", LagSwitchModule.Mode.Lag));
   public final Setting<Boolean> breakValue;

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (clientPlayerEntity != null && clientWorld != null) {
         if (this.pause.getValue()) {
            if (this.ground.getValue() && !clientPlayerEntity.isOnGround()) {
               return false;
            }

            if (this.velocity.getValue() && clientPlayerEntity.hurtTime > 0) {
               return false;
            }

            if (this.scaffold.getValue() && ScaffoldModule.getScaffoldModule().check7()) {
               return false;
            }
         }

         if (!PlayerUtils.check5()) {
            return false;
         } else {
            return !this.normal.testNumber(this.delay.getValue()) ? false : this.testClientPlayerEntity2(clientPlayerEntity, clientWorld);
         }
      } else {
         return false;
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.timer.getValue());
   }

   public LagSwitchModule(PhobosClient phobosClient) {
      super(phobosClient, "LagSwitch", "Performs lag/blink/teleport at contact moments to get a first hit", Categories.combat);
      this.lag = this.resolveSetting2(new NumberSetting<>("Lag", 2, 0, 15));
      this.timer = this.resolveSetting2(new NumberSetting<>("Timer", 2, 0, 15));
      this.minRange = this.resolveSetting2(new NumberSetting<>("MinRange", 3.6, 0.0, 8.0));
      this.maxRange = this.resolveSetting2(new NumberSetting<>("MaxRange", 5.0, 0.0, 8.0));
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 500L, 0L, 4000L, true));
      this.reset = this.resolveSetting2(new BooleanSetting("Reset", false));
      this.breakValue = this.resolveSetting2(new BooleanSetting("Break", "Stops lagging/shifting if the conditions are met", true));
      this.pause = this.resolveSetting2(new BooleanSetting("Pause", false));
      this.ground = this.resolveSetting2(new BooleanSetting("Ground", false));
      this.velocity = this.resolveSetting2(new BooleanSetting("Velocity", false));
      this.scaffold = this.resolveSetting2(new BooleanSetting("Scaffold", false));
      this.invisible = this.resolveSetting2(new BooleanSetting("Invisible", false));
      this.resync = this.resolveSetting2(new BooleanSetting("Resync", "Stops lag on attack", false));
      this.debug = this.resolveSetting2(new BooleanSetting("Debug"));
      this.normal = new TimeGate.Normal();
      this.state = LagSwitchModule.State.None;
      this.intValue = 0;
      this.packetSet = new LinkedHashSet<>();
      this.resolveSettingGroup(this.pause, this.ground, this.velocity, this.scaffold, this.invisible);
      this.handleListener(
         new SafeListener<AttackEvent2>() {
            public void handleObject(
               AttackEvent2 attackEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (LagSwitchModule.this.resync.getValue() && !LagSwitchModule.this.mode.testObject3(LagSwitchModule.Mode.Lag)) {
                  LagSwitchModule.this.handleClientPlayerEntity(clientPlayerEntity);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>(300) {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((LagSwitchModule.Mode)LagSwitchModule.this.mode.getValue()) {
                  case Lag:
                     switch (LagSwitchModule.this.state) {
                        case Lag:
                           if (LagSwitchModule.this.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
                              LagState.flag = true;
                              LagSwitchModule.this.state = LagSwitchModule.State.Timer;
                              LagSwitchModule.this.intValue = 0;
                           }

                           return;
                        case None:
                           for (int i = 0; i < LagSwitchModule.this.timer.getValue(); i++) {
                              clientPlayerEntity.tick();
                              if (LagSwitchModule.this.breakValue.getValue() && !LagSwitchModule.this.testClientPlayerEntity2(clientPlayerEntity, clientWorld)) {
                                 break;
                              }
                           }

                           LagSwitchModule.this.state = LagSwitchModule.State.Lag;
                           return;
                        case Timer:
                           if (LagSwitchModule.this.intValue >= LagSwitchModule.this.lag.getValue()) {
                              LagState.flag = false;
                              LagSwitchModule.this.handleClientPlayerEntity(clientPlayerEntity);
                           } else {
                              LagSwitchModule.this.intValue++;
                           }

                           return;
                        default:
                           return;
                     }
                  case Shift:
                     switch (LagSwitchModule.this.state) {
                        case Lag:
                           if (LagSwitchModule.this.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
                              LagState.flag = true;
                              LagState.handleClientPlayerEntity(clientPlayerEntity);
                              LagSwitchModule.this.state = LagSwitchModule.State.Lag;
                              LagSwitchModule.this.intValue = 0;
                              if (LagSwitchModule.this.debug.getValue()) {
                                 PlayerEntity playerEntity = LagSwitchModule.this.resolvePlayerEntity(clientPlayerEntity, clientWorld);
                                 if (playerEntity != null) {
                                    Box box = playerEntity.getBoundingBox();
                                    double var12 = box.squaredMagnitude(clientPlayerEntity.getEyePos());
                                 }
                              }
                           }
                           break;
                        case Timer:
                           if (LagSwitchModule.this.breakValue.getValue() && !LagSwitchModule.this.testClientPlayerEntity2(clientPlayerEntity, clientWorld)) {
                              LagSwitchModule.this.handleClientPlayerEntity(clientPlayerEntity);
                           } else if (LagSwitchModule.this.intValue >= LagSwitchModule.this.lag.getValue()) {
                              LagSwitchModule.this.handleClientPlayerEntity(clientPlayerEntity);
                              if (LagSwitchModule.this.debug.getValue()) {
                                 PlayerEntity playerEntity2 = LagSwitchModule.this.resolvePlayerEntity(clientPlayerEntity, clientWorld);
                                 if (playerEntity2 != null) {
                                    Box box2 = playerEntity2.getBoundingBox();
                                    double var7 = box2.squaredMagnitude(clientPlayerEntity.getEyePos());
                                 }
                              }
                           } else {
                              LagSwitchModule.this.intValue++;
                           }
                     }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerTickEvent>(300) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (LagSwitchModule.this.mode.getValue().equals(LagSwitchModule.Mode.Lag)) {
                  ;
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<?>>(2000) {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((LagSwitchModule.Mode)LagSwitchModule.this.mode.getValue()) {
                  case Lag:
                     switch (LagSwitchModule.this.state) {
                        case None:
                           synchronized (LagSwitchModule.this.packetSet) {
                              LagSwitchModule.this.packetSet.add(send.getPacket());
                              send.setCancelled(true);
                              return;
                           }
                        case Timer:
                           if (send.getPacket() instanceof PlayerMoveC2SPacket) {
                              send.setCancelled(true);
                              return;
                           }

                           synchronized (LagSwitchModule.this.packetSet) {
                              LagSwitchModule.this.packetSet.add(send.getPacket());
                              send.setCancelled(true);
                              return;
                           }
                        default:
                           return;
                     }
                  case Shift:
                     if (LagSwitchModule.this.state.equals(LagSwitchModule.State.Lag)) {
                        synchronized (LagSwitchModule.this.packetSet) {
                           LagSwitchModule.this.packetSet.add(send.getPacket());
                           send.setCancelled(true);
                        }
                     }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<MoveEvent2>() {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (LagSwitchModule.this.mode.getValue().equals(LagSwitchModule.Mode.Lag)) {
                  if (LagSwitchModule.this.state.equals(LagSwitchModule.State.Lag)) {
                     moveEvent2.setVec3d(Vec3d.ZERO);
                  }
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public PlayerEntity resolvePlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return clientWorld.getPlayers()
         .parallelStream()
         .filter(
            abstractClientPlayerEntity -> this.pause.getValue() && abstractClientPlayerEntity.isInvisible() && this.invisible.getValue()
               ? false
               : EntityUtils.testPhobosClient2(this.phobosClient, clientPlayerEntity, abstractClientPlayerEntity)
         )
         .map(abstractClientPlayerEntity -> new Pair<>(abstractClientPlayerEntity, clientPlayerEntity.squaredDistanceTo(abstractClientPlayerEntity)))
         .min(Comparator.comparing(Pair::getRight))
         .<PlayerEntity>map(Pair::getLeft)
         .orElse(null);
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      PlayerEntity playerEntity = this.resolvePlayerEntity(clientPlayerEntity, clientWorld);
      if (playerEntity == null) {
         return false;
      }

      double d = playerEntity.distanceTo(clientPlayerEntity);
      return d >= this.minRange.getValue() && d <= this.maxRange.getValue();
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      if (clientPlayerEntity != null) {
         LagState.flag = false;
         this.state = LagSwitchModule.State.None;
         this.intValue = 0;
         this.normal.run();
         synchronized (this.packetSet) {
            for (Packet<?> packet : this.packetSet) {
               clientPlayerEntity.networkHandler.sendPacket(packet);
            }

            this.packetSet.clear();
         }
      }
   }

   @Override
   public void run4() {
      this.handleClientPlayerEntity(mc.player);
   }

   private enum Mode {
      Lag,
      Shift;
   }

   private enum State {
      None,
      Timer,
      Lag;
   }
}
