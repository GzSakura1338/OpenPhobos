package org.phobos.mixin.game;

import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameOptions.class)
public abstract class MixinGameOptions {
   @ModifyConstant(method = "<init>", constant = @Constant(intValue = 110))
   private int maxFov(int constant) {
      return 180;
   }
}
