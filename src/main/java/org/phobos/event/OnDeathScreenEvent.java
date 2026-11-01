package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;

public record OnDeathScreenEvent(ClientPlayerEntity player) {
}
