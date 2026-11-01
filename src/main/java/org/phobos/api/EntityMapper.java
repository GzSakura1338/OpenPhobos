package org.phobos.api;

import net.minecraft.entity.Entity;

public interface EntityMapper<K extends Entity, T> {
   T resolveObject(K var1);
}
