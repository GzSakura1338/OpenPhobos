package org.phobos.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public record PlayerRecord(
   PlayerEntity player, Vec3d position, float bodyYaw, float headYaw, float handSwingingProgress, int handSwingingTicks, boolean shift, float speed, float pos
) {
}
