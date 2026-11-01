package org.phobos.core.proxy;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.ProxyHandlerFactory;
import org.phobos.api.Named;
import org.phobos.api.JsonSerializable;
import org.phobos.type.PUBLICPRIVATEEnum;

public class ProxyConfig implements Named, JsonSerializable {
   @Nullable
   public String text;
   public String text2;
   public boolean flag;
   public String name;
   public boolean flag2;
   public int port;
   public ProxyHandlerFactory<?> proxyHandlerFactory;
   @Nullable
   public String text3;
   public boolean flag3 = false;
   @Nullable
   public String text4;
   @Nullable
   public PUBLICPRIVATEEnum pUBLICPRIVATEEnum;

   public ProxyHandlerFactory<?> getProxyHandlerFactory() {
      return this.proxyHandlerFactory;
   }

   public ChannelFuture resolveChannelFuture(InetSocketAddress inetSocketAddress, boolean flag, ClientConnection clientConnection) {
      Class<? extends io.netty.channel.socket.SocketChannel> classValue2;
      EventLoopGroup eventLoopGroup;
      if (Epoll.isAvailable() && flag) {
         classValue2 = EpollSocketChannel.class;
         eventLoopGroup = (EventLoopGroup)ClientConnection.EPOLL_CLIENT_IO_GROUP.get();
      } else {
         classValue2 = NioSocketChannel.class;
         eventLoopGroup = (EventLoopGroup)ClientConnection.CLIENT_IO_GROUP.get();
      }

      return ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(eventLoopGroup))
               .handler(new ProxyConfig.ProxyChannelInitializer(this, clientConnection)))
            .channel(classValue2))
         .connect(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Address", this.text2);
      jsonObject.addProperty("Port", this.port);
      jsonObject.addProperty("Type", ProxyUtils.describeProxyHandlerFactory(this.proxyHandlerFactory));
      jsonObject.addProperty("Secure", this.flag);
      jsonObject.addProperty("Username", this.text3 == null ? "None" : this.text3);
      jsonObject.addProperty("Password", this.text4 == null ? "None" : this.text4);
      return jsonObject;
   }

   @Nullable
   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setText4(@Nullable String text) {
      this.text4 = text;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public int getPort() {
      return this.port;
   }

   public InetSocketAddress createInetSocketAddress() {
      return new InetSocketAddress(this.text2, this.port);
   }

   public ProxyConfig() {
   }

   public void setText(@Nullable String text) {
      this.text = text;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public void setPUBLICPRIVATEEnum(@Nullable PUBLICPRIVATEEnum pUBLICPRIVATEEnum) {
      this.pUBLICPRIVATEEnum = pUBLICPRIVATEEnum;
   }

   public void setText3(@Nullable String text) {
      this.text3 = text;
   }

   @Nullable
   public String getText3() {
      return this.text3;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      this.setText2(jsonObject.get("Address").getAsString());
      this.setPort(jsonObject.get("Port").getAsInt());
      this.setProxyHandlerFactory(ProxyUtils.resolveProxyHandlerFactory(jsonObject.get("Type").getAsString()));
      this.setFlag(jsonObject.get("Secure").getAsBoolean());
      String text2 = jsonObject.get("Username").getAsString();
      String text = jsonObject.get("Password").getAsString();
      this.setText3(text2.equals("None") ? null : text2);
      this.setText4(text.equals("None") ? null : text);
   }

   public void setName(String text) {
      this.name = text;
   }

   public ProxyConfig(String text) {
      this.name = text;
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Nullable
   public String getText2() {
      return this.text;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public void setPort(int i) {
      this.port = i;
   }

   @Nullable
   public String getText4() {
      return this.text4;
   }

   public String getText22() {
      return this.text2;
   }

   public void setProxyHandlerFactory(ProxyHandlerFactory<?> proxyHandlerFactory) {
      this.proxyHandlerFactory = proxyHandlerFactory;
   }

   public static class Builder {
      public final ProxyConfig proxyConfig = new ProxyConfig();

      public ProxyConfig.Builder getBuilder() {
         this.proxyConfig.setFlag(true);
         return this;
      }

      @Contract(pure = true)
      public ProxyConfig.Builder resolveBuilder(String text) {
         this.proxyConfig.setText2(text);
         return this;
      }

      public ProxyConfig.Builder resolveBuilder2(@NotNull String text, @NotNull String text2) {
         this.proxyConfig.setText3(text);
         this.proxyConfig.setText4(text2);
         return this.getBuilder();
      }

      @Contract(pure = true)
      public ProxyConfig.Builder resolveBuilder3(int i) {
         this.proxyConfig.setPort(i);
         return this;
      }

      public ProxyConfig.Builder resolveBuilder4(ProxyHandlerFactory<?> proxyHandlerFactory) {
         this.proxyConfig.setProxyHandlerFactory(proxyHandlerFactory);
         return this;
      }

      @Contract(pure = true)
      public ProxyConfig.Builder resolveBuilder5(String text, PUBLICPRIVATEEnum pUBLICPRIVATEEnum) {
         this.proxyConfig.setFlag2(true);
         this.proxyConfig.setText(text);
         this.proxyConfig.setPUBLICPRIVATEEnum(pUBLICPRIVATEEnum);
         return this;
      }

      @Contract("-> new")
      public ProxyConfig getProxyConfig() {
         Preconditions.checkArgument(this.proxyConfig.getText22() != null, "Cannot build a proxy without an address!");
         if (this.proxyConfig.isFlag()) {
            Preconditions.checkArgument(
               this.proxyConfig.getText3() != null && this.proxyConfig.getText4() != null,
               "A secure proxy cannot be initialized without username and password"
            );
         }

         return this.proxyConfig;
      }

      public ProxyConfig.Builder resolveBuilder6(String text) {
         this.proxyConfig.setName(text);
         return this;
      }
   }

   private static class ProxyChannelInitializer extends ChannelInitializer<Channel> {
      public final ClientConnection clientConnection;
      public final ProxyConfig proxyConfig;

      public ProxyChannelInitializer(ProxyConfig proxyConfig, ClientConnection clientConnection) {
         this.proxyConfig = proxyConfig;
         this.clientConnection = clientConnection;
      }

      public void initChannel(Channel channel) throws Exception {
         try {
            channel.config().setOption(ChannelOption.TCP_NODELAY, true);
         } catch (Throwable var3) {
         }

         ChannelPipeline channelPipeline = channel.pipeline();
         channelPipeline.addFirst("proxy", ProxyManager.createSocks5ProxyHandler(this.proxyConfig));
         ClientConnection.addHandlers(channelPipeline, NetworkSide.CLIENTBOUND, false, null);
         this.clientConnection.addFlowControlHandler(channelPipeline);
      }
   }
}
