package org.phobos.core.util;

import java.util.function.Supplier;

public class DefaultSuppliers {
   public static Supplier<Short> shortValueSupplier = () -> (short)0;
   public static Supplier<Double> doubleValueSupplier = () -> 0.0;
   public static Supplier<Boolean> booleanValueSupplier = () -> true;
   public static Supplier<Long> longValueSupplier = () -> 0L;
   public static Supplier<Float> floatValueSupplier = () -> 0.0F;
   public static Supplier<Boolean> booleanValueSupplier2 = () -> false;
   public static Supplier<Integer> integerSupplier = () -> 0;
}
