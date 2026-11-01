package org.phobos.module.movement;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.event.PacketFlyTickListener;
import org.phobos.event.PacketFlySendListener;
import org.phobos.event.PacketFlyMoveListener;
import org.phobos.event.PacketFlyReceivePlayerPositionLookListener;
import org.phobos.event.PacketFlyPlayerMoveListener;
import org.phobos.module.Module;

public class PacketFlyModule extends Module {
   public final Set<Packet<?>> packetSet;
   public final Map<Integer, PacketFlyModule.Instance> integerMap;
   public final Setting<PacketFlyModule.Mode> mode = new EnumSetting<>("Mode", PacketFlyModule.Mode.Factor).resolveSetting6(this);
   public float floatValue;
   public int intValue;
   public final Setting<Boolean> antiKick;
   public final Setting<PacketFlyModule.Phase> phase;
   public final Setting<Float> factor;
   public final Setting<PacketFlyModule.Type> type = new EnumSetting<>("Type", PacketFlyModule.Type.Up).resolveSetting6(this);
   public final AtomicInteger atomicInteger;

   @Override
   public void run3() {
      this.run2();
   }

   public boolean testInt(int i) {
      if (++this.intValue >= i) {
         this.intValue = 0;
         return true;
      } else {
         return false;
      }
   }

   public void run2() {
      this.floatValue = 1.0F;
      this.intValue = 0;
      this.atomicInteger.set(0);
      this.packetSet.clear();
      this.integerMap.clear();
   }

   public <T extends PacketListener, P extends Packet<T>> P resolvePacket(ClientPlayerEntity clientPlayerEntity, P packet) {
      clientPlayerEntity.networkHandler.sendPacket(packet);
      return packet;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return clientWorld.getBlockCollisions(clientPlayerEntity, clientPlayerEntity.getBoundingBox().expand(-0.0625, -0.0625, -0.0625)).iterator().hasNext();
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Vec3d vec3d, boolean flag) {
      Vec3d vec3d2 = clientPlayerEntity.getPos().add(vec3d);
      Vec3d vec3d3 = this.type.getValue().get(vec3d2, 1337);
      this.packetSet.add(this.resolvePacket(clientPlayerEntity, new PositionAndOnGround(vec3d2.x, vec3d2.y, vec3d2.z, clientPlayerEntity.isOnGround())));
      if (!mc.isInSingleplayer()) {
         this.packetSet.add(this.resolvePacket(clientPlayerEntity, new PositionAndOnGround(vec3d3.x, vec3d3.y, vec3d3.z, clientPlayerEntity.isOnGround())));
      }

      if (flag) {
         int i = this.atomicInteger.incrementAndGet();
         this.resolvePacket(clientPlayerEntity, new TeleportConfirmC2SPacket(i));
         this.integerMap.put(i, new PacketFlyModule.Instance(vec3d2, System.currentTimeMillis()));
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public void handlePlayerMoveC2SPacket(PlayerMoveC2SPacket playerMoveC2SPacket, CancellableEvent cancellableEvent) {
      if (!this.packetSet.remove(playerMoveC2SPacket)) {
         cancellableEvent.setCancelled(true);
      }
   }

   public PacketFlyModule(PhobosClient phobosClient) {
      super(phobosClient, "PacketFly", Categories.movement, "Allows you to fly through blocks");
      this.phase = new EnumSetting<>("Phase", PacketFlyModule.Phase.Full).resolveSetting6(this);
      this.antiKick = new BooleanSetting("AntiKick", true).resolveSetting6(this);
      this.factor = new NumberSetting<>("Factor", 1.0F, 1.0F, 10.0F).resolveSetting6(this);
      this.integerMap = new ConcurrentHashMap<>();
      this.packetSet = ConcurrentHashMap.newKeySet();
      this.atomicInteger = new AtomicInteger();
      this.handleListener(new PacketFlyMoveListener(this));
      this.handleListener(new PacketFlyReceivePlayerPositionLookListener(this));
      this.handleListener(new PacketFlyTickListener(this));
      this.handleListener(new PacketFlyPlayerMoveListener(this));
      this.handleListener(new PacketFlySendListener(this));
      ModuleListenerUtils.handleListenerHolder4(this, this::run2);
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public record Instance(Vec3d pos, long time) {
   }

   public enum Mode {
      Fast,
      Factor,
      Jitter;
   }

   public enum Phase {
      Full,
      Semi,
      Off;
   }

   public enum Type {
      Up {
         @Override
         public Vec3d get(Vec3d vec, int invalid) {
            return vec.add(0.0, invalid, 0.0);
         }
      },
      Down {
         @Override
         public Vec3d get(Vec3d vec, int invalid) {
            return vec.add(0.0, -invalid, 0.0);
         }
      },
      Preserve {
         public final Random random = new Random();

         @Override
         public Vec3d get(Vec3d vec, int invalid) {
            return vec.add(this.randomInt(), 0.0, this.randomInt());
         }

         public int randomInt() {
            int result = this.random.nextInt(29000000);
            return this.random.nextBoolean() ? result : -result;
         }
      };

      public abstract Vec3d get(Vec3d var1, int var2);
   }
}
