package org.phobos.core.module;

import java.util.function.Function;
import org.phobos.module.Module;

public class ModuleHandle<M extends Module> {
   public final M module;

   public void handleInvokable(ModuleHandle.Invokable invokable) {
      if (this.module == null) {
         invokable.run();
      }
   }

   public boolean check() {
      return this.module != null && this.module.check7();
   }

   public ModuleHandle(M module) {
      this.module = module;
   }

   public void handleComputable(ModuleHandle.Computable<M> computable) {
      if (this.module != null) {
         computable.handleObject(this.module);
      }
   }

   public <F> F resolveObject(Function<M, F> function, F object) {
      if (this.check2()) {
         function.apply(this.module);
      }

      return object;
   }

   public void handleRunnable(Runnable... runnables) {
      if (this.module != null) {
         for (Runnable runnable : runnables) {
            runnable.run();
         }
      }
   }

   public boolean check2() {
      return this.module != null;
   }

   public void handleRunnable2(Runnable... runnables) {
      if (this.module == null) {
         for (Runnable runnable : runnables) {
            runnable.run();
         }
      }
   }

   public M getModule() {
      return this.module;
   }

   public interface Computable<M> {
      void handleObject(M var1);
   }

   public interface Invokable {
      void run();
   }
}
