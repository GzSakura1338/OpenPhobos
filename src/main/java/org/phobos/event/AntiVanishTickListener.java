package org.phobos.event;

import org.phobos.module.misc.AntiVanishModule;

public final class AntiVanishTickListener extends ModuleListener<AntiVanishModule, TickEvent2> {
   public void handleObject2(TickEvent2 tickEvent2) {
      if (mc.world != null) {
         mc.world.getEntities().forEach(entity -> entity.setInvisible(false));
      }
   }

   public AntiVanishTickListener(AntiVanishModule antiVanishModule) {
      super(antiVanishModule, TickEvent2.class);
   }
}
