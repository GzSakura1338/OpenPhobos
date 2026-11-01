package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.event.UseItemEvent;
import org.phobos.module.Module;

public class BowKillerModule extends Module {
   public final MutableObject<PlayerEntity> playerEntityMutableObject;
   public volatile int intValue;
   public final Setting<Boolean> always;
   public final Setting<BowKillerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", BowKillerModule.Mode.Default));
   public final Setting<Integer> height = this.resolveSetting2(
      new NumberSetting<>("Height", 10, 0, 170).resolveNumberSetting("Max", 170).resolveSetting4(this.mode, BowKillerModule.Mode.Height)
   );
   public final Setting<Integer> interval;
   public int intValue2;
   public final Setting<Boolean> predict;
   public final Setting<Double> buffer;
   public final Setting<Integer> ticks;
   public final Setting<Double> runs;
   public final Setting<Boolean> rotate;
   public final Setting<Integer> teleports;
   public boolean flag;

   public BowKillerModule(PhobosClient phobosClient) {
      super(phobosClient, "BowKiller", Categories.combat, "Instant bow exploit");
      this.always = this.resolveSetting2(new BooleanSetting("Always", false));
      this.interval = this.resolveSetting2(new NumberSetting<>("Interval", 25, 0, 100));
      this.runs = this.resolveSetting2(new NumberSetting<>("Runs", 10.0, 0.0, 200.0));
      this.buffer = this.resolveSetting2(new NumberSetting<>("Buffer", 70.0, 0.0, 200.0));
      this.teleports = this.resolveSetting2(new NumberSetting<>("Teleports", 0, 0, 100));
      this.rotate = this.resolveSetting2(new BooleanSetting("Rotate", false));
      this.predict = this.resolveSetting2(new BooleanSetting("Predict", false));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 0, 5));
      this.playerEntityMutableObject = new MutableObject(null);
      this.resolveSettingGroup2("Factor", this.runs, this.buffer, this.teleports);
      this.resolveSettingGroup(this.always, this.interval);
      this.resolveSettingGroup(this.rotate, this.predict, this.ticks);
      this.handleListener(
         new SafeListener<Receive<PlayerPositionLookS2CPacket>>() {
            public void handleObject(
               Receive<PlayerPositionLookS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               BowKillerModule.this.intValue = ((PlayerPositionLookS2CPacket)receive.getPacket()).getTeleportId();
            }
         }
      );
      this.handleListener(
         new SafeListener<UseItemEvent>() {
            public void handleObject(
               UseItemEvent useItemEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (useItemEvent.getPlayerEntity() instanceof ClientPlayerEntity) {
                  if (clientPlayerEntity.isOnGround()) {
                     if (useItemEvent.getHand().equals(Hand.MAIN_HAND)) {
                        if (!clientPlayerEntity.getMainHandStack().isOf(Items.BOW)) {
                           return;
                        }
                     } else if (!clientPlayerEntity.getOffHandStack().isOf(Items.BOW)) {
                        return;
                     }

                     BowKillerModule.this.flag = true;
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<PlayerActionC2SPacket>>() {
            public void handleObject(
               Send<PlayerActionC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (((PlayerActionC2SPacket)send.getPacket()).getAction().equals(Action.RELEASE_USE_ITEM)) {
                  if (clientPlayerEntity.getMainHandStack().isOf(Items.BOW) || clientPlayerEntity.getOffHandStack().isOf(Items.BOW)) {
                     BowKillerModule.this.flag = false;
                     clientPlayerEntity.networkHandler
                        .sendPacket(
                           new ClientCommandC2SPacket(clientPlayerEntity, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_SPRINTING)
                        );
                     if (!(BowKillerModule.this.intValue2 < BowKillerModule.this.runs.getValue() * 2.0) || BowKillerModule.this.always.getValue()) {
                        for (int i3 = 0; i3 < BowKillerModule.this.runs.getValue() + BowKillerModule.this.buffer.getValue(); i3++) {
                           if (BowKillerModule.this.interval.getValue() != 0 && i3 != 0 && i3 % BowKillerModule.this.interval.getValue() == 0) {
                              int i2 = BowKillerModule.this.intValue;

                              for (int i = 0; i < BowKillerModule.this.teleports.getValue(); i++) {
                                 clientPlayerEntity.networkHandler.sendPacket(new TeleportConfirmC2SPacket(i2));
                              }
                           }

                           if (BowKillerModule.this.rotate.getValue()) {
                              float[] floats = BowKillerModule.this.values(clientPlayerEntity, clientWorld);
                              switch ((BowKillerModule.Mode)BowKillerModule.this.mode.getValue()) {
                                 case Height:
                                    clientPlayerEntity.networkHandler
                                       .sendPacket(
                                          new Full(
                                             clientPlayerEntity.getX(),
                                             clientPlayerEntity.getY() + 1.3E-13,
                                             clientPlayerEntity.getZ(),
                                             floats != null ? floats[0] : clientPlayerEntity.getYaw(),
                                             floats != null ? floats[1] : clientPlayerEntity.getPitch(),
                                             true
                                          )
                                       );
                                    clientPlayerEntity.networkHandler
                                       .sendPacket(
                                          new Full(
                                             clientPlayerEntity.getX(),
                                             clientPlayerEntity.getY() + 2.7E-13,
                                             clientPlayerEntity.getZ(),
                                             floats != null ? floats[0] : clientPlayerEntity.getYaw(),
                                             floats != null ? floats[1] : clientPlayerEntity.getPitch(),
                                             false
                                          )
                                       );
                                    break;
                                 case Default:
                                    PlayerActionUtils.handleClientPlayerEntity20(clientPlayerEntity, clientWorld, BowKillerModule.this.height.getValue());
                                    PlayerActionUtils.handleClientPlayerEntity23(clientPlayerEntity, clientWorld, BowKillerModule.this.height.getValue());
                                    clientPlayerEntity.onLanding();
                              }
                           }
                        }

                        BowKillerModule.this.intValue2 = 0;
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UnusedEvent>() {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!clientPlayerEntity.getMainHandStack().isOf(Items.BOW) && !clientPlayerEntity.getOffHandStack().isOf(Items.BOW)) {
                  BowKillerModule.this.flag = false;
                  BowKillerModule.this.intValue2 = 0;
               } else if (clientPlayerEntity.isUsingItem()) {
                  if (BowKillerModule.this.flag) {
                     BowKillerModule.this.intValue2++;
                  }
               } else {
                  BowKillerModule.this.intValue2 = 0;
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public float @Nullable [] values(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (!this.rotate.getValue()) {
         return null;
      } else {
         PlayerEntity playerEntity2 = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient);
         if (playerEntity2 != null) {
            this.playerEntityMutableObject.setValue(playerEntity2);
            PlayerEntity playerEntity;
            return RotationUtils.values(
               clientPlayerEntity,
               this.predict.getValue()
                  ? (
                     (playerEntity = this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.ticks.getValue(), playerEntity2))
                           != null
                        ? playerEntity
                        : playerEntity2
                  )
                  : playerEntity2
            );
         } else {
            return null;
         }
      }
   }

   public enum Mode {
      Default,
      Height;
   }
}
