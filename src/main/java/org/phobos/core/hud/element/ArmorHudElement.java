package org.phobos.core.hud.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import org.phobos.module.client.HudModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.hud.HudElement;
import org.phobos.core.hud.HudManager;
import org.phobos.core.setting.Setting;

public class ArmorHudElement extends HudElement {
   public final Setting<Boolean> dynamic = this.resolveSetting2(new BooleanSetting("Dynamic"));
   public final Setting<Boolean> bar = this.resolveSetting2(new BooleanSetting("Bar"));

   public void handleDrawContext(DrawContext drawContext, ItemStack itemStack, int i, int i2) {
      if (!itemStack.isEmpty()) {
         drawContext.getMatrices().push();
         if (itemStack.isItemBarVisible() && this.bar.getValue()) {
            int i3 = itemStack.getItemBarStep();
            int i4 = itemStack.getItemBarColor();
            int i5 = i + 2;
            int i6 = i2 + 13;
            drawContext.fill(RenderLayer.getGuiOverlay(), i5, i6, i5 + 13, i6 + 2, 1, -16777216);
            drawContext.fill(RenderLayer.getGuiOverlay(), i5, i6, i5 + i3, i6 + 1, 1, i4 | 0xFF000000);
         }

         ClientPlayerEntity clientPlayerEntity = mc.player;
         float f = clientPlayerEntity == null
            ? 0.0F
            : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(itemStack.getItem(), mc.getRenderTickCounter().getTickDelta(true));
         if (f > 0.0F) {
            int i7 = i2 + MathHelper.floor(16.0F * (1.0F - f));
            int i8 = i7 + MathHelper.ceil(16.0F * f);
            drawContext.fill(RenderLayer.getGuiOverlay(), i, i7, i + 16, i8, 1, Integer.MAX_VALUE);
         }

         drawContext.getMatrices().pop();
      }
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (mc.player != null) {
         int i10 = 0;
         int i11 = 0;
         if (this.dynamic.getValue()) {
            Entity entity = mc.player.getVehicle();
            int i3 = mc.player.getMaxAir();
            int i4 = Math.min(mc.player.getAir(), i3);
            if (mc.player.isSubmergedIn(FluidTags.WATER) || i4 < i3) {
               i11 -= 10;
            } else if (entity instanceof LivingEntity livingEntity) {
               i11 -= -10 + (int)Math.ceil((livingEntity.getMaxHealth() - 1.0F) / 20.0F) * 10;
            } else {
               i11 -= mc.player.isCreative() ? -17 : 0;
            }
         }

         List<ItemStack> list = new ArrayList<>(List.copyOf(mc.player.getInventory().armor));
         Collections.reverse(list);
         float f = this.getFloatValue2();
         float f2 = this.getFloatValue();

         for (ItemStack itemStack : list) {
            if (itemStack.isEmpty()) {
               i10 += 18;
            } else {
               int i5 = i10;
               int i6 = i11;
               HudManager.getHudManager().handleConsumer(drawContext2 -> {
                  drawContext2.drawItem(itemStack, (int)(f + i5), (int)f2 + i6);
                  this.handleDrawContext(drawContext, itemStack, (int)(f + i5), (int)f2 + i6);
               });
               i10 += 18;
            }
         }

         int var14 = 0;

         for (ItemStack itemStack2 : list) {
            if (itemStack2.isEmpty()) {
               var14 += 18;
            } else {
               int i7 = itemStack2.getMaxDamage();
               int i8 = itemStack2.getDamage();
               int i9 = (int)((float)(i7 - i8) / i7 * 100.0F);
               this.phobosClient
                  .getRenderManager()
                  .handleDrawContext2(
                     drawContext,
                     i9 + "%",
                     this.getFloatValue2() + var14 + 2.0F,
                     this.getFloatValue() + i11 - 5.0F,
                     ColorUtils.createColor((float)(i7 - i8) / i7 * 120.0F, 100.0F, 50.0F, 1.0F),
                     0.5F,
                     HudModule.getHudModule().shadow.getValue(),
                     HudModule.getHudModule().check4()
                  );
               var14 += 18;
            }
         }
      }
   }

   public ArmorHudElement() {
      super("Armor", 0.0F, 0.0F, 72.0F, 16.0F);
   }
}
