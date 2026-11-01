package org.phobos.api;

public interface SelfTyped<E extends SelfTyped<E>> {
   default E getSelfTyped() {
      return (E)this;
   }
}
