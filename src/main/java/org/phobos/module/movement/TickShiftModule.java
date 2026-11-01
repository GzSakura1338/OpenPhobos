package org.phobos.module.movement;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.event.TickShiftSendFullListener;
import org.phobos.event.TickShiftSendPlayerMoveListener;
import org.phobos.event.TickShiftTickListener;
import org.phobos.event.TickShiftSendPositionAndOnGroundListener;
import org.phobos.event.TickShiftPlayerMoveListener;
import org.phobos.event.TickShiftReceivePlayerPositionLookListener;
import org.phobos.event.TickShiftSendLookAndOnGroundListener;
import org.phobos.module.Module;

public class TickShiftModule extends Module {
   public final Setting<Boolean> instant;
   public final Setting<Boolean> ground;
   public int intValue;
   public final Setting<Boolean> rotation;
   public final Setting<Boolean> instantDisable;
   public final Setting<Float> speed = new NumberSetting<>("Speed", 2.5F, 0.1F, 10.0F).resolveSetting6(this);
   public final Setting<Boolean> sneaking;
   public final Setting<Integer> packets = new NumberSetting<>("Packets", 10, 1, 50).resolveSetting6(this);

   public void handleSend(PacketEvent.Send<? extends PlayerMoveC2SPacket> send, boolean flag) {
      if (!send.isCancelled() && mc.player != null) {
         if (flag && PlayerUtils.check() && (this.sneaking.getValue() || !mc.player.isSneaking()) && this.intValue > 0) {
            this.phobosClient.getTimerManager().handleFloat(this.speed.getValue(), TimerManager.TimerPriority.High);
         }

         this.intValue = this.intValue <= 0 ? 0 : this.intValue - 1;
         if (this.instant.getValue() && this.instantDisable.getValue() && this.intValue <= 0) {
            this.run11();
         }
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.intValue);
   }

   @Override
   public void run4() {
      this.run2();
   }

   @Override
   public void run3() {
      this.run2();
      if (this.instant.getValue()) {
         this.intValue = this.packets.getValue();
      }
   }

   public TickShiftModule(PhobosClient phobosClient) {
      super(phobosClient, "TickShift", Categories.movement, "Shifts and releases packets to make you faster");
      this.sneaking = new BooleanSetting("Sneaking").resolveSetting6(this);
      this.ground = new BooleanSetting("Ground").resolveSetting6(this);
      this.rotation = new BooleanSetting("Rotation").resolveSetting6(this);
      this.instant = new BooleanSetting("Instant").resolveSetting6(this);
      this.instantDisable = new BooleanSetting("InstantDisable").resolveSetting6(this);
      this.resolveSettingGroup(this.instant, this.instantDisable);
      this.handleListener(new TickShiftSendFullListener(this));
      this.handleListener(new TickShiftSendLookAndOnGroundListener(this));
      this.handleListener(new TickShiftSendPlayerMoveListener(this));
      this.handleListener(new TickShiftReceivePlayerPositionLookListener(this));
      this.handleListener(new TickShiftSendPositionAndOnGroundListener(this));
      this.handleListener(new TickShiftPlayerMoveListener(this));
      this.handleListener(new TickShiftTickListener(this));
      ModuleListenerUtils.handleListenerHolder4(this, this::run2);
   }

   public void run2() {
      this.phobosClient.getTimerManager().run2();
      this.intValue = 0;
   }
}
