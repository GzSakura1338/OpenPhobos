package org.phobos.core.util.collection;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.phobos.api.Named;

public class SortedNamedRegistry<T extends Named> extends NamedRegistry<T> {
   public final List<T> list;
   public final Comparator<T> comparator = Named::computeNamed;

   public SortedNamedRegistry() {
      super(new CopyOnWriteArrayList<>());
      this.list = (List<T>)this.collection;
   }

   @Override
   public boolean testNamed2(T named) {
      this.testNamed3(named);
      this.textMap.put(named.describe3(), named);
      this.handleList(this.list, named, this.comparator);
      return true;
   }

   public void handleList(List<T> list, T named, Comparator<T> comparator) {
      for (int i = 0; i < list.size(); i++) {
         T named2 = list.get(i);
         if (comparator.compare(named2, named) >= 0) {
            list.add(i, named);
            return;
         }
      }

      list.add(named);
   }
}
