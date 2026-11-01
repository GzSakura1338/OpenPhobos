package org.phobos.core.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.TypeLookup;
import org.phobos.api.Named;
import org.phobos.api.NamedCollection;

public abstract class NamedRegistry<T extends Named> implements NamedCollection<T>, TypeLookup.Lookup<T> {
   public final Map<String, T> textMap;
   public final Collection<T> collection;
   public final Object object = new Object();

   @Override
   public String toString() {
      return "%s(%s)".formatted(this.getClass().getName(), this.stream().map(Named::getName).collect(Collectors.joining(", ")));
   }

   public abstract boolean testNamed2(T var1);

   @Override
   public boolean testNamed3(T named) {
      synchronized (this.object) {
         return this.testNamed(named);
      }
   }

   @Override
   public Stream<T> stream() {
      return this.collection.stream();
   }

   public NamedRegistry(Collection<T> collection) {
      this.textMap = new HashMap<>();
      this.collection = collection;
   }

   @Override
   public int size() {
      return this.textMap.size();
   }

   public boolean testNamed(T named) {
      this.textMap.remove(named.describe3());
      return this.collection.removeIf(named2 -> named2.getName().equalsIgnoreCase(named.getName()));
   }

   @NotNull
   @Override
   public Iterator<T> iterator() {
      return this.collection.iterator();
   }

   @Override
   public Optional<T> resolveOptional2(String text) {
      return Optional.ofNullable(this.textMap.get(text.toLowerCase()));
   }

   @Override
   public boolean testNamed4(T named) {
      synchronized (this.object) {
         return this.testNamed2(named);
      }
   }
}
