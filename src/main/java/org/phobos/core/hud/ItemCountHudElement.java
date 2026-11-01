package org.phobos.core.hud;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.phobos.module.client.HudModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.Setting;

public class ItemCountHudElement extends HudElement {
   public final Item item;
   public final Setting<Boolean> font = new BooleanSetting("Font").resolveSetting6(this);

   public void handleDrawContext(DrawContext drawContext, TextRenderer textRenderer, ItemStack itemStack, int i, int i2, @Nullable String text) {
      if (!itemStack.isEmpty()) {
         drawContext.getMatrices().push();
         if (itemStack.getCount() != 1 || text != null) {
            String text2 = text == null ? String.valueOf(itemStack.getCount()) : text;
            drawContext.getMatrices().translate(0.0F, 0.0F, 200.0F);
            float f = i + 19 - 2 - (this.font.getValue() ? this.computeText2(text2) : textRenderer.getWidth(text2));
            float f2 = i2 + 6 + 3;
            if (this.font.getValue()) {
               HudModule.getHudModule().handleDrawContext4(drawContext, text2, f, f2);
            } else {
               drawContext.drawText(textRenderer, text2, (int)f, (int)f2, 16777215, true);
            }
         }

         if (itemStack.isItemBarVisible()) {
            int i3 = itemStack.getItemBarStep();
            int i4 = itemStack.getItemBarColor();
            int i5 = i + 2;
            int i6 = i2 + 13;
            drawContext.fill(RenderLayer.getGuiOverlay(), i5, i6, i5 + 13, i6 + 2, -16777216);
            drawContext.fill(RenderLayer.getGuiOverlay(), i5, i6, i5 + i3, i6 + 1, i4 | 0xFF000000);
         }

         ClientPlayerEntity clientPlayerEntity = mc.player;
         float f3 = clientPlayerEntity == null
            ? 0.0F
            : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(itemStack.getItem(), mc.getRenderTickCounter().getTickDelta(true));
         if (f3 > 0.0F) {
            int i7 = i2 + MathHelper.floor(16.0F * (1.0F - f3));
            int i8 = i7 + MathHelper.ceil(16.0F * f3);
            drawContext.fill(RenderLayer.getGuiOverlay(), i, i7, i + 16, i8, Integer.MAX_VALUE);
         }

         drawContext.getMatrices().pop();
      }
   }

   public ItemCountHudElement(String text, Item item) {
      super(text, 0.0F, 0.0F, 16.0F, 18.0F);
      this.item = item;
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      AtomicInteger atomicInteger = new AtomicInteger(0);
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(itemSwitcher -> atomicInteger.set(itemSwitcher.computePredicate(itemStackx -> itemStackx.isOf(this.item))));
      ItemStack itemStack = new ItemStack(this.item, Math.max(atomicInteger.get(), 1));
      float f = this.getFloatValue2();
      float f2 = this.getFloatValue();
      boolean flag = this.font.getValue();
      HudManager.getHudManager().handleConsumer(drawContext2 -> {
         drawContext2.drawItem(itemStack, (int)f, (int)f2);
         if (!flag) {
            this.handleDrawContext(drawContext, mc.textRenderer, itemStack, (int)f, (int)f2, String.valueOf(atomicInteger.get()));
         }
      });
      if (flag) {
         this.handleDrawContext(
            drawContext, mc.textRenderer, itemStack, (int)this.getFloatValue2(), (int)this.getFloatValue(), String.valueOf(atomicInteger.get())
         );
      }
   }
}
