package org.phobos.core.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.Nullable;

public class CancellableFuture<T> extends CompletableFuture<T> {
   public final CancelToken cancelToken;

   @Override
   public boolean cancel(boolean flag) {
      if (!this.isDone()) {
         this.cancelToken.setCancelled(true);
      }

      return super.cancel(flag);
   }

   public CancellableFuture(CancelToken cancelToken) {
      this.cancelToken = cancelToken;
   }

   @Override
   public <U> CompletableFuture<U> newIncompleteFuture() {
      return new CancellableFuture(this.cancelToken);
   }

   public static class WithExecutor<T> extends CancellableFuture<T> {
      @Nullable
      public final Executor executor;

      @Override
      public Executor defaultExecutor() {
         return this.executor == null ? super.defaultExecutor() : this.executor;
      }

      @Override
      public <U> CompletableFuture<U> newIncompleteFuture() {
         return new CancellableFuture.WithExecutor(this.cancelToken, this.executor);
      }

      public WithExecutor(CancelToken cancelToken, @Nullable Executor executor) {
         super(cancelToken);
         this.executor = executor;
      }
   }
}
