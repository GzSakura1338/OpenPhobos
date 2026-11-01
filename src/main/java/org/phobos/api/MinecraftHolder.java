package org.phobos.api;

import net.minecraft.client.MinecraftClient;

public interface MinecraftHolder {
   MinecraftClient mc = MinecraftClient.getInstance();
}
