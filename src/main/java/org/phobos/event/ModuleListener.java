package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.PhobosClient;
import org.phobos.module.Module;

public abstract class ModuleListener<M extends Module, E> extends Listener<E> implements MinecraftHolder {
   public final M module;
   public final PhobosClient phobosClient;

   public ModuleListener(M module, Class<? super E> classValue, Class<?> classValue2) {
      this(module, classValue, classValue2, 10);
   }

   public ModuleListener(M module, Class<? super E> classValue, int i) {
      this(module, classValue, null, i);
   }

   public ModuleListener(M module, Class<? super E> classValue, Class<?> classValue2, int i) {
      super(classValue, classValue2, i);
      this.module = module;
      this.phobosClient = module.getPhobosClient();
   }

   public ModuleListener(M module, Class<? super E> classValue) {
      this(module, classValue, 10);
   }

   public abstract static class Safe<M extends Module, E> extends ModuleListener<M, E> {
      public Safe(M module, Class<? super E> classValue) {
         super(module, classValue);
      }

      public Safe(M module, Class<? super E> classValue, Class<?> classValue2) {
         super(module, classValue, classValue2, 10);
      }

      public abstract void handleObject(E var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);

      public Safe(M module, Class<? super E> classValue, Class<?> classValue2, int i) {
         super(module, classValue, classValue2, i);
      }

      public Safe(M module, Class<? super E> classValue, int i) {
         super(module, classValue, i);
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
}
