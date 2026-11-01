package org.phobos.core.util;

import org.jetbrains.annotations.NotNull;

public class StagedPipeline<T> {
   @NotNull
   public StagedPipeline.Stage<T> stage;
   @NotNull
   public StagedPipeline.Stage<T> stage2;
   public final PropertyBag propertyBag = new PropertyBag();

   public static StagedPipeline<Void> createStagedPipeline(StagedPipeline.UnitStage unitStage, StagedPipeline.UnitStage unitStage2) {
      return new StagedPipeline<>(unitStage, unitStage2);
   }

   @NotNull
   public StagedPipeline.Stage<T> getStage() {
      return this.stage;
   }

   public static <T> StagedPipeline<T> createStagedPipeline2(StagedPipeline.Stage<T> stage, StagedPipeline.Stage<T> stage2) {
      return new StagedPipeline<>(stage, stage2);
   }

   @NotNull
   public StagedPipeline.Stage<T> getStage2() {
      return this.stage2;
   }

   public PropertyBag getPropertyBag() {
      return this.propertyBag;
   }

   public void handleObject(T object) {
      this.stage.handlePropertyBag(this.propertyBag, object);
   }

   public void handleRunnable(Runnable runnable, T object) {
      this.handleObject2(object);
      runnable.run();
      this.handleObject(object);
   }

   public StagedPipeline(@NotNull StagedPipeline.Stage<T> stage, @NotNull StagedPipeline.Stage<T> stage2) {
      if (stage == null) {
         throw new NullPointerException("pre is marked non-null but is null");
      }

      if (stage2 == null) {
         throw new NullPointerException("post is marked non-null but is null");
      }

      this.stage2 = stage;
      this.stage = stage2;
   }

   public void handleObject2(T object) {
      this.propertyBag.clear();
      this.stage2.handlePropertyBag(this.propertyBag, object);
   }

   public interface Stage<T> {
      void handlePropertyBag(PropertyBag var1, T var2);
   }

   public interface UnitStage extends StagedPipeline.Stage<Void> {
      default void handlePropertyBag(PropertyBag propertyBag, Void voidValue) {
         this.handlePropertyBag2(propertyBag);
      }

      void handlePropertyBag2(PropertyBag var1);
   }
}
