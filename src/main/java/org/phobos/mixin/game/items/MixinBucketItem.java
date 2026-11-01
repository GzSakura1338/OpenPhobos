package org.phobos.mixin.game.items;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import org.earthhack.ducks.IBucketItem;
import org.phobos.api.ValueGuard;
import org.phobos.api.ValueGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public abstract class MixinBucketItem implements IBucketItem {
   @Unique
   private final ValueGuard<BlockHitResult> placement = new ValueGuard.Normal<>();

   @Override
   public void addPlacement(BlockHitResult blockHitResult) {
      this.placement.setValue(blockHitResult);
   }

   @ModifyExpressionValue(
      method = "use",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/item/BucketItem;raycast(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;"
      )
   )
   private BlockHitResult useHook(BlockHitResult original) {
      return this.placement.resolveObject(original);
   }
}
