package org.phobos.core.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.phobos.api.TooltipDataProvider;

public class ContainerTooltip implements TooltipComponent, TooltipDataProvider {
   public final Color color;
   public static final Identifier identifier = Identifier.of("phobos", "textures/container.png");
   public final ItemStack[] itemStackArray;

   public ContainerTooltip(ItemStack[] itemStacks, Color color) {
      this.itemStackArray = itemStacks;
      this.color = color;
   }

   public int getHeight() {
      return 67;
   }

   public int getWidth(TextRenderer textRenderer) {
      return 176;
   }

   public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      RenderSystem.setShaderColor(this.color.getRed() / 255.0F, this.color.getGreen() / 255.0F, this.color.getBlue() / 255.0F, this.color.getAlpha() / 255.0F);
      context.drawTexture(identifier, x, y, 0, 0.0F, 0.0F, 176, 67, 176, 67);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      int i = 0;
      int i2 = 0;

      for (ItemStack itemStack : this.itemStackArray) {
         MatrixStack matrixStack = context.getMatrices();
         matrixStack.push();
         matrixStack.scale(1.0F, 1.0F, 1.0F);
         matrixStack.translate(0.0F, 0.0F, 401.0F);
         context.drawItem(itemStack, x + 8 + i * 18, y + 7 + i2 * 18);
         context.drawItemInSlot(textRenderer, itemStack, x + 8 + i * 18, y + 7 + i2 * 18, null);
         matrixStack.pop();
         if (++i >= 9) {
            i = 0;
            i2++;
         }
      }
   }

   @Override
   public TooltipComponent getTooltipComponent() {
      return this;
   }
}
