package org.phobos.module.misc;

import java.time.Instant;
import java.util.BitSet;
import java.util.function.Consumer;
import net.minecraft.network.message.LastSeenMessageList.Acknowledgment;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.IllegalDisconnectDisconnectListener;
import org.phobos.module.Module;

public class IllegalDisconnectModule extends Module {
   public final Setting<IllegalDisconnectModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", IllegalDisconnectModule.Mode.Attack));

   public IllegalDisconnectModule(PhobosClient phobosClient) {
      super(phobosClient, "IllegalDisconnect", Categories.misc, "Ensures proper disconnection");
      this.handleListener(new IllegalDisconnectDisconnectListener(this));
   }

   public enum Mode {
      Chat {
         @Override
         public void execute(Consumer<Packet<?>> consumer) {
            consumer.accept(new ChatMessageC2SPacket("", Instant.MIN, 0L, null, new Acknowledgment(0, BitSet.valueOf(new byte[20]))));
         }
      },
      Inventory {
         @Override
         public void execute(Consumer<Packet<?>> consumer) {
            consumer.accept(new UpdateSelectedSlotC2SPacket(10));
         }
      },
      Attack {
         @Override
         public void execute(Consumer<Packet<?>> consumer) {
            if (MinecraftHolder.mc.player != null) {
               consumer.accept(PlayerInteractEntityC2SPacket.attack(MinecraftHolder.mc.player, false));
            }
         }
      };

      public abstract void execute(Consumer<Packet<?>> var1);
   }
}
