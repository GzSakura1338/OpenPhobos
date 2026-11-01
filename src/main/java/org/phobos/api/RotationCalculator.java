package org.phobos.api;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

public interface RotationCalculator {
   float[] values(ClientPlayerEntity var1, Entity var2);
}
