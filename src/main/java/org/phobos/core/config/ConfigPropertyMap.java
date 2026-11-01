package org.phobos.core.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigPropertyMap {
   public final Map<String, ConfigProperty<?>> textMap = new HashMap<>();

   public Collection<ConfigProperty<?>> getCollection() {
      return this.textMap.values();
   }

   public ConfigPropertyMap resolveConfigPropertyMap(String text, String text2) {
      return this.resolveConfigPropertyMap3(text, () -> text2);
   }

   public <T> ConfigProperty<T> resolveConfigProperty(String text, Class<T> classValue) {
      return (ConfigProperty<T>)this.textMap.get(text);
   }

   public <T> ConfigPropertyMap resolveConfigPropertyMap2(String text, Function<T, String> function, Function<String, T> function2, Supplier<T> supplier) {
      ConfigProperty<T> configProperty = new ConfigProperty<>(text, function, function2, supplier);
      this.textMap.put(text, configProperty);
      return this;
   }

   public ConfigPropertyMap resolveConfigPropertyMap3(String text, Supplier<String> supplier) {
      return this.resolveConfigPropertyMap2(text, Function.identity(), Function.identity(), supplier);
   }
}
