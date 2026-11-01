package org.phobos.mixin.game.items;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Optional;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.render.TooltipContext;
import org.phobos.core.rotation.RotationManager;
import org.phobos.module.player.LegacyModule;
import org.phobos.module.client.RotationsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem implements EventSubscriber {
   @ModifyReturnValue(method = "getUseAction", at = @At("RETURN"))
   private UseAction getUseActionHook(UseAction original) {
      return this.isSword() && LegacyModule.getLegacyModule().check7() ? UseAction.BLOCK : original;
   }

   @ModifyExpressionValue(
      method = "raycast",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;")
   )
   private static Vec3d raycastHook(Vec3d original) {
      return RotationsModule.check6() && !RotationManager.getRotation().check() ? RotationManager.getRotation().getVec3d() : original;
   }

   @Unique
   private boolean isSword() {
      return (Object)this instanceof SwordItem;
   }

   @Inject(method = "getTooltipData", at = @At("HEAD"), cancellable = true)
   private void getTooltipDataHook(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
      TooltipContext event = this.invoke(TooltipContext.resolveTooltipContext(stack));
      if (event.getTooltipData() != null) {
         cir.setReturnValue(Optional.of(event.getTooltipData()));
      }
   }

   @ModifyReturnValue(method = "getMaxUseTime", at = @At("RETURN"))
   private int getMaxUseItemHook(int original) {
      return this.isSword() && LegacyModule.getLegacyModule().check7() ? 72000 : original;
   }

   @Inject(method = "use", at = @At("HEAD"), cancellable = true)
   private void useHook(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
      if (this.isSword() && LegacyModule.getLegacyModule().check7()) {
         ItemStack stack = user.getStackInHand(hand);
         user.setCurrentHand(hand);
         cir.setReturnValue(TypedActionResult.consume(stack));
      }
   }
}
