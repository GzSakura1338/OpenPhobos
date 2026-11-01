package org.phobos.core.module;

import org.jetbrains.annotations.Nullable;
import org.phobos.module.Module;
import org.phobos.core.ClientHolder;

public final class ModuleLookup {
   @Nullable
   public static <C extends Module> C resolveModule(Class<C> classValue) {
      return ClientHolder.getPhobosClient().getModuleManager().resolveOptional2(classValue).orElse(null);
   }

   public static <A extends Module, B extends Module> ModulePairHandle<A, B> createModulePairHandle(Class<A> classValue, Class<B> classValue2) {
      ModuleManager moduleManager = ClientHolder.getPhobosClient().getModuleManager();
      return new ModulePairHandle<>(moduleManager.resolveOptional2(classValue).orElse(null), moduleManager.resolveOptional2(classValue2).orElse(null));
   }

   public ModuleLookup() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static <C extends Module> ModuleHandle<C> createModuleHandle(Class<C> classValue) {
      return new ModuleHandle<>(ClientHolder.getPhobosClient().getModuleManager().resolveOptional2(classValue).orElse(null));
   }
}
