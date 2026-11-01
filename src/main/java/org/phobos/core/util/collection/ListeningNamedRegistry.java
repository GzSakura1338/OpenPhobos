package org.phobos.core.util.collection;

import java.util.ArrayList;
import java.util.List;
import org.phobos.api.Named;
import org.phobos.api.ListenerHolder;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.PrefixNamedCollection;
import org.phobos.event.Listener;

public class ListeningNamedRegistry<T extends Named> extends SortedNamedRegistry<T> implements PrefixNamedCollection<T>, ListenerHolder, MinecraftHolder {
   public final List<Listener<?>> listenerList = new ArrayList<>();

   @Override
   public List<Listener<?>> getListenerList() {
      return this.listenerList;
   }
}
