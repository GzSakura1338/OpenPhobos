package org.phobos.module.misc;

import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.util.holder.IntHolder;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class FastLatencyModule extends Module {
   public volatile boolean flag;
   public volatile long longValue;
   public volatile int intValue;
   public final Setting<FastLatencyModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", FastLatencyModule.Mode.Completion));
   public final TimeGate.Threaded threaded;
   public volatile long longValue2;
   public final Setting<Long> update;
   public final Setting<Boolean> screen;
   public static final int intValue2 = -9999999;
   public static FastLatencyModule fastLatencyModule;
   public final TimeGate.Threaded threaded2;
   public final Setting<String> command = this.resolveSetting2(
      new StringSetting("Command", "w").resolveSetting4(this.mode, FastLatencyModule.Mode.Completion).resolveSetting7("/command (adds space after command!)")
   );

   public TimeGate.Threaded getThreaded() {
      return this.threaded;
   }

   public static FastLatencyModule getFastLatencyModule() {
      return fastLatencyModule;
   }

   public Setting<Boolean> getScreen() {
      return this.screen;
   }

   public Setting<FastLatencyModule.Mode> getModeSetting() {
      return this.mode;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public long getLongValue22() {
      return this.longValue2;
   }

   public int getIntValue() {
      return this.intValue;
   }

   @Override
   public String nothing() {
      return FakeLatencyModule.computeInt(this.intValue) + "ms";
   }

   public Setting<String> getCommand() {
      return this.command;
   }

   public Setting<Long> getUpdate() {
      return this.update;
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, int i) {
      this.intValue = i;
      if (this.screen.getValue()) {
         PlayerListEntry playerListEntry = clientPlayerEntity.networkHandler.getPlayerListEntry(clientPlayerEntity.getUuid());
         if (playerListEntry != null) {
            playerListEntry.latency = FakeLatencyModule.computeInt(i);
         }
      }
   }

   public TimeGate.Threaded getThreaded2() {
      return this.threaded2;
   }

   public long getLongValue2() {
      return this.longValue;
   }

   public FastLatencyModule(PhobosClient phobosClient) {
      super(phobosClient, "FastLatency", Categories.misc, "Fetches your ping to the server");
      this.update = this.resolveSetting2(new LongNumberSetting("Update", 100L, 0L, 2000L));
      this.screen = this.resolveSetting2(new BooleanSetting("Screen"));
      this.threaded = new TimeGate.Threaded();
      this.threaded2 = new TimeGate.Threaded();
      this.flag = false;
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch ((FastLatencyModule.Mode)FastLatencyModule.this.mode.getValue()) {
                  case Query:
                     if ((FastLatencyModule.this.threaded.testLong(5000L) || FastLatencyModule.this.flag)
                        && FastLatencyModule.this.threaded2.testNumber2(FastLatencyModule.this.update.getValue())) {
                        FastLatencyModule.this.threaded.run();
                        FastLatencyModule.this.flag = false;
                        clientPlayerEntity.networkHandler
                           .sendPacket(new RequestCommandCompletionsC2SPacket(-9999999, "%s ".formatted(FastLatencyModule.this.command.getValue())));
                        FastLatencyModule.this.longValue = TimeUtils.value();
                     }
                     break;
                  case Completion:
                     if (FastLatencyModule.this.threaded2.testNumber2(FastLatencyModule.this.update.getValue())) {
                        clientPlayerEntity.networkHandler.sendPacket(new QueryPingC2SPacket(TimeUtils.value()));
                     }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<PingResultS2CPacket>>(Integer.MAX_VALUE) {
            public void handleObject(
               Receive<PingResultS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (FastLatencyModule.this.mode.testObject2(FastLatencyModule.Mode.Query)) {
                  long l = TimeUtils.value() - ((PingResultS2CPacket)receive.getPacket()).startTime();
                  FastLatencyModule.this.handleClientPlayerEntity(clientPlayerEntity, (int)l);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<CommandSuggestionsS2CPacket>>() {
            public void handleObject(
               Receive<CommandSuggestionsS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (FastLatencyModule.this.mode.testObject2(FastLatencyModule.Mode.Completion)) {
                  if (Objects.equals(((CommandSuggestionsS2CPacket)receive.getPacket()).id(), -9999999)) {
                     FastLatencyModule.this.handleClientPlayerEntity(
                        clientPlayerEntity, (int)MathHelper.clamp(TimeUtils.value() - FastLatencyModule.this.longValue, 0L, 2000L) / 2
                     );
                     FastLatencyModule.this.flag = true;
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<IntHolder>(Integer.MAX_VALUE) {
         public void handleObject2(IntHolder intHolder) {
            intHolder.setIntValue(FastLatencyModule.this.intValue);
         }
      });
      fastLatencyModule = this;
   }

   private enum Mode {
      Completion,
      Query;
   }
}
