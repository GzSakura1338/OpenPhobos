package org.phobos.core.net;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.time.Instant;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.encryption.NetworkEncryptionUtils.SecureRandomUtil;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.LastSeenMessagesCollector.LastSeenMessages;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public final class PacketTypeUtils {
   public static Object2IntMap<?> object2IntMap;

   public static void setObject2IntMap(Object2IntMap<?> object2IntMap) {
      PacketTypeUtils.object2IntMap = object2IntMap;
   }

   public static int computePacketType(PacketType<?> packetType) {
      return object2IntMap.getInt(packetType);
   }

   public PacketTypeUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static ChatMessageC2SPacket createChatMessageC2SPacket(ClientPlayerEntity clientPlayerEntity, String text) {
      Instant instant = Instant.now();
      long l = SecureRandomUtil.nextLong();
      LastSeenMessages lastSeenMessages = clientPlayerEntity.networkHandler.lastSeenMessagesCollector.collect();
      MessageSignatureData messageSignatureData = clientPlayerEntity.networkHandler
         .messagePacker
         .pack(new MessageBody(text, instant, l, lastSeenMessages.lastSeen()));
      return new ChatMessageC2SPacket(text, instant, l, messageSignatureData, lastSeenMessages.update());
   }
}
