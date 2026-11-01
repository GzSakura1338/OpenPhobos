package org.phobos.api;

import org.phobos.api.ListenerAdapter;

public interface ListenerAdapterSource<T> {
    default public ListenerAdapter<T> getListenerAdapter() {
        return this::lambda$getListenerAdapter$0;
    }

    public /* synthetic */ void lambda$getListenerAdapter$0(T var1);
}
