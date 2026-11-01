package org.earthhack.ducks;

import net.minecraft.client.render.VertexConsumerProvider.Immediate;

public interface IBufferBuilderStorage {
   Immediate getBackingContext();
}
