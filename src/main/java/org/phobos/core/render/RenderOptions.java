package org.phobos.core.render;

import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.util.math.Box;
import org.phobos.api.CuboidMapper;
import org.phobos.api.ContextResult;

public class RenderOptions implements CuboidMapper<Render3d.Cuboid, Box> {
   public Supplier<Color> colorSupplier;
   public Supplier<Float> floatValueSupplier;
   public Supplier<Color> colorSupplier2;
   public Supplier<Boolean> booleanValueSupplier;
   public Supplier<Long> longValueSupplier;

   public void setFloatValueSupplier(Supplier<Float> supplier) {
      this.floatValueSupplier = supplier;
   }

   public void setLongValueSupplier(Supplier<Long> supplier) {
      this.longValueSupplier = supplier;
   }

   public void setColorSupplier(Supplier<Color> supplier) {
      this.colorSupplier = supplier;
   }

   public void setBooleanValueSupplier(Supplier<Boolean> supplier) {
      this.booleanValueSupplier = supplier;
   }

   public Render3d.Cuboid resolveCuboid(Box box) {
      return Render3d.Cuboid.createBuilder()
         .resolveBuilder11(box)
         .resolveBuilder18(this.colorSupplier.get(), this.colorSupplier2.get())
         .resolveBuilder12(this.floatValueSupplier.get())
         .resolveBuilder5(this.booleanValueSupplier.get() ? this.longValueSupplier.get() : -1L)
         .getCuboid();
   }

   public Supplier<Float> getFloatValueSupplier() {
      return this.floatValueSupplier;
   }

   public Supplier<Color> getColorSupplier() {
      return this.colorSupplier;
   }

   public Supplier<Long> getLongValueSupplier() {
      return this.longValueSupplier;
   }

   public void setColorSupplier2(Supplier<Color> supplier) {
      this.colorSupplier2 = supplier;
   }

   public Supplier<Color> getColorSupplier2() {
      return this.colorSupplier2;
   }

   public Supplier<Boolean> getBooleanValueSupplier() {
      return this.booleanValueSupplier;
   }

   public static class Builder implements ContextResult<RenderOptions, RenderOptions.Builder> {
      public final boolean[] flagArray;
      public final RenderOptions renderOptions = new RenderOptions();

      public Builder() {
         this.flagArray = new boolean[4];
      }

      @Override
      public boolean[] getFlagArray() {
         return this.flagArray;
      }

      public RenderOptions.Builder getObject() {
         return this;
      }

      public RenderOptions.Builder resolveBuilder(Supplier<Boolean> supplier, Supplier<Long> supplier2) {
         this.renderOptions.setBooleanValueSupplier(supplier);
         this.renderOptions.setLongValueSupplier(supplier2);
         return this.getObject2();
      }

      public RenderOptions.Builder resolveBuilder2(Supplier<Color> supplier) {
         this.renderOptions.setColorSupplier(supplier);
         return this.getObject2();
      }

      @Override
      public Throwable createIllegalStateException() {
         return new IllegalStateException("Unable to build block render context");
      }

      public RenderOptions.Builder resolveBuilder3(Supplier<Color> supplier) {
         this.renderOptions.setColorSupplier2(supplier);
         return this.getObject2();
      }

      public RenderOptions.Builder resolveBuilder4(Supplier<Float> supplier) {
         this.renderOptions.setFloatValueSupplier(supplier);
         return this.getObject2();
      }

      public RenderOptions get() {
         return this.renderOptions;
      }
   }
}
