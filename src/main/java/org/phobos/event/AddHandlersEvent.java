package org.phobos.event;

import io.netty.channel.ChannelPipeline;

public class AddHandlersEvent {
   public final boolean flag;
   public final ChannelPipeline channelPipeline;

   public ChannelPipeline getChannelPipeline() {
      return this.channelPipeline;
   }

   public AddHandlersEvent(ChannelPipeline channelPipeline, boolean flag) {
      this.channelPipeline = channelPipeline;
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
