package org.phobos.module.movement;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.packet.Packet;
import org.earthhack.ducks.IClientPlayerNetworkHandler;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.movement.ElytraManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.event.DisablerJumpPostListener;
import org.phobos.event.DisablerTickListener;
import org.phobos.event.DisablerAttackListener;
import org.phobos.event.DisablerReceiveListener;
import org.phobos.event.DisablerSendListener;
import org.phobos.event.DisablerPlayerMoveListener;
import org.phobos.event.DisablerTick2Listener;
import org.phobos.event.DisablerPlayerTickListener;
import org.phobos.type.OGOPAOGEnum;
import org.phobos.module.Module;

public class DisablerModule extends Module {
   public int intValue;
   public final TimeGate.Normal normal;
   public boolean flag;
   public boolean flag2;
   public float floatValue;
   public boolean flag3;
   public final Setting<Boolean> motion;
   public boolean flag4;
   public boolean flag5;
   public int intValue2;
   public final Setting<DisablerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", DisablerModule.Mode.Verus));
   public final Setting<Long> delay = this.resolveSetting2(
      new LongNumberSetting("Delay", 120L, 0L, 2000L, true)
         .resolveSetting3(this.resolveVisibility2(this.mode, DisablerModule.Mode.Polar, DisablerModule.Mode.Vulcan))
   );
   public int intValue3;
   public boolean flag6;
   public boolean flag7;
   public boolean flag8;
   public final ElytraManager.FlyConfiguration flyConfiguration;
   public boolean flag9;
   public int intValue4;
   public final Setting<OGOPAOGEnum> vanillaPacket;
   public static DisablerModule disablerModule;
   public final Setting<Float> packets;
   public boolean flag10;
   public boolean flag11;
   public final Setting<Boolean> scaffold;
   public final Queue<Packet<?>> packetQueue;
   public int intValue5;

   public DisablerModule(PhobosClient phobosClient) {
      super(phobosClient, "Disabler", Categories.movement, "Disables parts of an anticheat");
      this.motion = this.resolveSetting2(new BooleanSetting("Motion").resolveSetting4(this.mode, DisablerModule.Mode.Watchdog));
      this.scaffold = this.resolveSetting2(new BooleanSetting("Scaffold").resolveSetting4(this.mode, DisablerModule.Mode.Watchdog));
      this.vanillaPacket = this.resolveSetting2(new EnumSetting<>("VanillaPacket", OGOPAOGEnum.OGO).resolveSetting4(this.mode, DisablerModule.Mode.Vanilla));
      this.packets = this.resolveSetting2(new NumberSetting<>("Packets", 10.0F, 1.0F, 20.0F).resolveSetting4(this.mode, DisablerModule.Mode.Vanilla));
      this.flag3 = false;
      this.flag7 = false;
      this.packetQueue = new ConcurrentLinkedQueue<>();
      this.intValue5 = -1;
      this.normal = new TimeGate.Normal();
      this.intValue2 = 0;
      this.intValue = 0;
      this.flyConfiguration = ElytraManager.FlyConfiguration.createFactory()
         .getFactory3()
         .getFactory()
         .getFactory4()
         .getFactory2()
         .getFactory5()
         .resolveFactory8(4)
         .getFlyConfiguration2();
      this.handleListener(new DisablerAttackListener(this));
      this.handleListener(new DisablerJumpPostListener(this));
      this.handleListener(new DisablerTickListener(this));
      this.handleListener(new DisablerReceiveListener(this));
      this.handleListener(new DisablerSendListener(this));
      this.handleListener(new DisablerPlayerTickListener(this));
      this.handleListener(new DisablerPlayerMoveListener(this));
      this.handleListener(new DisablerTick2Listener(this));
      ModuleListenerUtils.handleListenerHolder4(this, () -> {
         this.flag10 = false;
         this.intValue = 0;
      });
      ModuleListenerUtils.handleListenerHolder2(this, () -> {
         this.intValue3 = 0;
         this.flag11 = false;
         this.flag9 = true;
      });
      ModuleListenerUtils.handleListenerHolder3(this, () -> this.flag2 = true);
      disablerModule = this;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue()).replaceAll("_", " ");
   }

   public void handlePacket2(Packet<?> packet) {
      try {
         ((Packet)packet).apply(mc.getNetworkHandler());
      } catch (Throwable var3) {
      }
   }

   public void run() {
      if (mc.player != null) {
         this.flag7 = true;
         this.packetQueue.forEach(this::handlePacket2);
         this.flag7 = false;
      }

      this.packetQueue.clear();
      this.flag3 = false;
   }

   @Override
   public void run4() {
      this.flag6 = false;
      this.intValue5 = -1;
      this.flag10 = false;
      this.flag4 = false;
      this.run();
   }

   public void handlePacket(Packet<?> packet) {
      if (mc.player != null) {
         ((IClientPlayerNetworkHandler)(Object)mc.player.networkHandler).handlePacket(packet);
      }
   }

   public static boolean check() {
      if (!disablerModule.check7()) {
         return false;
      }

      return switch ((DisablerModule.Mode)disablerModule.mode.getValue()) {
         case Sentinel -> disablerModule.flag6;
         case Elytra -> true;
         default -> false;
      };
   }

   public static boolean check2() {
      return disablerModule.check7() && disablerModule.mode.getValue().equals(DisablerModule.Mode.Overflow);
   }

   public static boolean check3() {
      return disablerModule.check7() && disablerModule.mode.getValue().equals(DisablerModule.Mode.Direction);
   }

   @Override
   public void run3() {
      this.intValue3 = 0;
      this.flag9 = true;
      this.intValue2 = 0;
   }

   public enum Mode implements TextProvider {
      Grim("Grim post disabler"),
      Verus("Verus disabler"),
      Vulcan("Vulcan old disabler"),
      Watchdog("Watchdog motion disabler"),
      Sentinel("Sentinel disabler"),
      Grim_Full("Test server-side disabler"),
      Polar("Polar patched disabler"),
      Intave("Intave disabler"),
      Overflow("Yaw encoding overflow disabler (old 2b2t)"),
      Direction("Null direction net encoding disabler (1.8 only)"),
      Swing("No swing"),
      Matrix("Matrix elytra disabler"),
      Elytra("Elytra rotation disabler"),
      Vanilla("Vanilla speed disabler");

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
