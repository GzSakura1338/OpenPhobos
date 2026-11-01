package org.phobos.mixin.game.render.interfaces;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HeldItemRenderer.class)
public interface IHeldItemRenderer {
   @Accessor("equipProgressMainHand")
   void setEquipProgressMainHand(float var1);

   @Accessor("equipProgressOffHand")
   float getEquipProgressOffHand();

   @Accessor("mainHand")
   void setMainHand(ItemStack var1);

   @Accessor("offHand")
   void setOffHand(ItemStack var1);

   @Accessor("equipProgressOffHand")
   void setEquipProgressOffHand(float var1);

   @Accessor("equipProgressMainHand")
   float getEquipProgressMainHand();
}
