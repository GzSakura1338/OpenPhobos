package org.phobos.mixin.game.items;

import net.minecraft.item.ItemStack;
import org.earthhack.ducks.IItemStack;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements IItemStack {
   @Unique
   private boolean glintOverride = false;

   @Override
   public void glint() {
      this.glintOverride = true;
   }

   @Inject(method = "hasGlint", at = @At("RETURN"), cancellable = true)
   private void hasGlintHook(CallbackInfoReturnable<Boolean> cir) {
      if (this.glintOverride) {
         cir.setReturnValue(true);
      } else {
         cir.setReturnValue(NoRenderModule.testItemStack((ItemStack)(Object)this, (Boolean)cir.getReturnValue()));
      }
   }
}
