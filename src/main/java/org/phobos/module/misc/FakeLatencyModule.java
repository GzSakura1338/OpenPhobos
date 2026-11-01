package org.phobos.module.misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import org.apache.commons.lang3.mutable.MutableLong;
import org.phobos.api.TextProvider;
import org.phobos.api.TickGate;
import org.phobos.api.TickGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.util.holder.IntHolder;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class FakeLatencyModule extends Module {
   public static FakeLatencyModule fakeLatencyModule;
   public final Setting<Integer> range;
   public final TickGate tickGate;
   public final Setting<Boolean> transactions;
   public final Setting<Integer> ticks;
   public final Map<Packet<?>, Long> packetMap;
   public final Setting<Boolean> spike;
   public final Setting<Integer> base;
   public final Setting<Long> delay;
   public final Setting<Float> spikeProbability;
   public final Setting<FakeLatencyModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", FakeLatencyModule.Mode.Normal));
   public final Setting<Integer> spikeSpread;
   public long longValue;
   public final Setting<Integer> spikeDistance;

   @Override
   public void run4() {
      if (mc.player != null) {
         this.packetMap.forEach((packet, longValue) -> mc.player.networkHandler.sendPacket(packet));
         this.packetMap.clear();
      }
   }

   public FakeLatencyModule(PhobosClient phobosClient) {
      super(phobosClient, "FakeLatency", Categories.misc, "Spoofs your ping");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 500L, 0L, 30000L, true).resolveSetting4(this.mode, FakeLatencyModule.Mode.Normal));
      this.transactions = this.resolveSetting2(new BooleanSetting("Transactions", false).resolveSetting4(this.mode, FakeLatencyModule.Mode.Normal));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 10, 1, 300, "t").resolveSetting4(this.mode, FakeLatencyModule.Mode.Fake));
      this.base = this.resolveSetting2(new NumberSetting<>("Base", 300, 20, 1500).resolveSetting4(this.mode, FakeLatencyModule.Mode.Fake));
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 10, 0, 30).resolveSetting4(this.mode, FakeLatencyModule.Mode.Fake));
      this.spike = this.resolveSetting2(new BooleanSetting("Spike"));
      this.spikeProbability = this.resolveSetting2(new NumberSetting<>("SpikeProbability", 25.0F, 0.1F, 100.0F, "%"));
      this.spikeDistance = this.resolveSetting2(new NumberSetting<>("SpikeDistance", 100, -1000, 1000).resolveSetting7("Calculate ping + Distance"));
      this.spikeSpread = this.resolveSetting2(new NumberSetting<>("SpikeSpread", 50, 0, 250).resolveSetting7("Randomness spread from the final spike result"));
      this.packetMap = new ConcurrentHashMap<>();
      this.tickGate = new TickGate.Normal();
      this.resolveSettingGroup(this.spike, this.spikeProbability, this.spikeDistance, this.spikeSpread)
         .resolveSettingGroup4(this.mode, FakeLatencyModule.Mode.Fake);
      this.handleListener(
         new Listener<Send<?>>() {
            public void handleObject2(Send<?> send) {
               if (MinecraftHolder.mc.player != null
                  && !MinecraftHolder.mc.isInSingleplayer()
                  && FakeLatencyModule.this.mode.testObject2(FakeLatencyModule.Mode.Normal)) {
                  Packet<?> packet = send.getPacket();
                  if (packet instanceof KeepAliveC2SPacket
                     || (packet instanceof ResourcePackStatusC2SPacket || packet instanceof CommonPongC2SPacket)
                        && FakeLatencyModule.this.transactions.getValue()) {
                     if (FakeLatencyModule.this.packetMap.containsKey(packet)) {
                        FakeLatencyModule.this.packetMap.remove(packet);
                        return;
                     }

                     FakeLatencyModule.this.packetMap.put(packet, TimeUtils.value());
                     send.setCancelled(true);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((FakeLatencyModule.Mode)FakeLatencyModule.this.mode.getValue()) {
                  case Fake:
                     FakeLatencyModule.this.packetMap.entrySet().removeIf(entry -> {
                        if (TimeUtils.computeLong(entry.getValue()) > FakeLatencyModule.this.delay.getValue()) {
                           clientPlayerEntity.networkHandler.sendPacket(entry.getKey());
                           return true;
                        } else {
                           return false;
                        }
                     });
                     break;
                  case Normal:
                     if (!FakeLatencyModule.this.tickGate.testInt2(FakeLatencyModule.this.ticks.getValue())) {
                        FakeLatencyModule.this.tickGate.run();
                        return;
                     }

                     MutableLong mutableLong = new MutableLong();
                     mutableLong.setValue(FakeLatencyModule.this.base.getValue() + MathUtils.computeInt(FakeLatencyModule.this.range.getValue()));
                     if (FakeLatencyModule.this.spike.getValue() && MathUtils.testSupplier(FakeLatencyModule.this.spikeProbability)) {
                        mutableLong.add(FakeLatencyModule.this.spikeDistance.getValue());
                        mutableLong.add(MathUtils.computeInt(FakeLatencyModule.this.spikeSpread.getValue()));
                     }

                     FakeLatencyModule.this.longValue = mutableLong.getValue();
               }
            }
         }
      );
      this.handleListener(new Listener<IntHolder>(Integer.MIN_VALUE) {
         public void handleObject2(IntHolder intHolder) {
            if (FakeLatencyModule.this.mode.testObject2(FakeLatencyModule.Mode.Fake)) {
               intHolder.setIntValue((int)FakeLatencyModule.this.longValue);
            }
         }
      });
      fakeLatencyModule = this;
   }

   public static int computeInt(int i) {
      return fakeLatencyModule.check7() && fakeLatencyModule.mode.testObject2(FakeLatencyModule.Mode.Fake) ? (int)fakeLatencyModule.longValue : i;
   }

   private enum Mode implements TextProvider {
      Normal("Default ping spoof"),
      Fake("Purely visual ping spoof");

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
