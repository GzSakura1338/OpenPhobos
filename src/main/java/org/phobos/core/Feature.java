package org.phobos.core;

import java.util.ArrayList;
import java.util.List;
import org.phobos.api.ListenerHolder;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.Listener;

public class Feature implements ListenerHolder, MinecraftHolder {
   public final List<Listener<?>> listenerList = new ArrayList<>();

   @Override
   public List<Listener<?>> getListenerList() {
      return this.listenerList;
   }
}
