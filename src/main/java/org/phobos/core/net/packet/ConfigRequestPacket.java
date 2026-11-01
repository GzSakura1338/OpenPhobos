package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.core.net.ByteBufUtils;

public class ConfigRequestPacket implements NetworkPacket {
   public ConfigScriptEnum configScriptEnum;
   public String name;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.name);
      ByteBufUtils.handleByteBuf(byteBuf, this.configScriptEnum.toString());
   }

   public void setName(String text) {
      this.name = text;
   }

   public void setConfigScriptEnum(ConfigScriptEnum configScriptEnum) {
      this.configScriptEnum = configScriptEnum;
   }

   public String getName() {
      return this.name;
   }

   public ConfigRequestPacket(String text, ConfigScriptEnum configScriptEnum) {
      this.name = text;
      this.configScriptEnum = configScriptEnum;
   }

   public ConfigRequestPacket() {
   }

   public ConfigScriptEnum getConfigScriptEnum() {
      return this.configScriptEnum;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setName(ByteBufUtils.createText(byteBuf));
      this.setConfigScriptEnum(ConfigScriptEnum.fromString(ByteBufUtils.createText(byteBuf)));
   }
}
