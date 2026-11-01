package org.phobos.event;

import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.module.misc.IllegalDisconnectModule;

public final class IllegalDisconnectDisconnectListener extends ModuleListener<IllegalDisconnectModule, DisconnectEvent2> {
   public IllegalDisconnectDisconnectListener(IllegalDisconnectModule illegalDisconnectModule) {
      super(illegalDisconnectModule, DisconnectEvent2.class);
   }

   public void handleObject2(DisconnectEvent2 disconnectEvent2) {
      this.module.mode.getValue().execute(SkyboxTextureUtils::handlePacket);
   }
}
