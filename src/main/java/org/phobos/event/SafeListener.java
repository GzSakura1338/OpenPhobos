package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.MinecraftHolder;

public abstract class SafeListener<E> extends Listener<E> implements MinecraftHolder {
   public SafeListener() {
   }

   public SafeListener(int i) {
      super(i);
   }

   public abstract void handleObject(E var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);

   public SafeListener(Class<E> classValue, int i) {
      super(classValue, i);
   }

   @Override
   public void handleObject2(E object) {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      ClientWorld clientWorld = mc.world;
      ClientPlayerInteractionManager clientPlayerInteractionManager = mc.interactionManager;
      if (clientPlayerEntity != null && clientWorld != null && clientPlayerInteractionManager != null) {
         this.handleObject(object, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
      }
   }
}
