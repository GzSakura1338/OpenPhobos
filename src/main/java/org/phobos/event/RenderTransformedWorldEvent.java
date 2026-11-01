package org.phobos.event;

import net.minecraft.client.util.math.MatrixStack;

public record RenderTransformedWorldEvent(MatrixStack matrixStack, float tickDelta) {
}
