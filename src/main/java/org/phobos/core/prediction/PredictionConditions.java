package org.phobos.core.prediction;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import org.phobos.module.client.EngineModule;

public class PredictionConditions {
   public final BooleanSupplier booleanSupplier;
   public final BooleanSupplier booleanSupplier2;
   public final BooleanSupplier booleanSupplier3;
   public final BooleanSupplier booleanSupplier4;
   public final DoubleSupplier doubleSupplier;

   public static PredictionConditions createPredictionConditions() {
      return new PredictionConditions(
         () -> EngineModule.getEngineModule().getPredictions().getValue(),
         () -> EngineModule.getEngineModule().getPredictionsMagnitude().getValue() / 100.0,
         () -> EngineModule.getEngineModule().isFlag42(),
         () -> EngineModule.getEngineModule().check(),
         () -> EngineModule.getEngineModule().check3()
      );
   }

   public boolean check() {
      return this.booleanSupplier3.getAsBoolean();
   }

   public BooleanSupplier getBooleanSupplier3() {
      return this.booleanSupplier3;
   }

   public boolean check2() {
      return this.booleanSupplier4.getAsBoolean();
   }

   public BooleanSupplier getBooleanSupplier4() {
      return this.booleanSupplier4;
   }

   public PredictionConditions(
      BooleanSupplier booleanSupplier,
      DoubleSupplier doubleSupplier,
      BooleanSupplier booleanSupplier2,
      BooleanSupplier booleanSupplier3,
      BooleanSupplier booleanSupplier4
   ) {
      this.booleanSupplier2 = booleanSupplier;
      this.doubleSupplier = doubleSupplier;
      this.booleanSupplier3 = booleanSupplier2;
      this.booleanSupplier = booleanSupplier3;
      this.booleanSupplier4 = booleanSupplier4;
   }

   public BooleanSupplier getBooleanSupplier2() {
      return this.booleanSupplier2;
   }

   public boolean check3() {
      return this.booleanSupplier.getAsBoolean();
   }

   public DoubleSupplier getDoubleSupplier() {
      return this.doubleSupplier;
   }

   public BooleanSupplier getBooleanSupplier() {
      return this.booleanSupplier;
   }
}
