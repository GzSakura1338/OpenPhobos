package org.phobos.module.player;

import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SneakPlayerTickListener;
import org.phobos.event.SneakTickListener;
import org.phobos.module.Module;

public class SneakModule extends Module {
   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      this.handleClientPlayerEntity2(clientPlayerEntity, () -> new ClientCommandC2SPacket(clientPlayerEntity, Mode.RELEASE_SHIFT_KEY));
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      this.handleClientPlayerEntity2(clientPlayerEntity, () -> new ClientCommandC2SPacket(clientPlayerEntity, Mode.PRESS_SHIFT_KEY));
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, Supplier<Packet<?>> supplier) {
      if (clientPlayerEntity != null) {
         clientPlayerEntity.networkHandler.sendPacket(supplier.get());
      }
   }

   @Override
   public void run4() {
      this.handleClientPlayerEntity4(mc.player);
   }

   public SneakModule(PhobosClient phobosClient) {
      super(phobosClient, "Sneak", Categories.player, "Makes you appear sneaking to others");
      this.handleListener(new SneakPlayerTickListener(this));
      this.handleListener(new SneakTickListener(this));
   }
}
