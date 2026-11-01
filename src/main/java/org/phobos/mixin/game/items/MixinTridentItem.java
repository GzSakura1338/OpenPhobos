package org.phobos.mixin.game.items;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.OnStoppedUsingEvent;
import org.phobos.module.movement.AutoTridentModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public abstract class MixinTridentItem implements EventSubscriber {
   @Inject(method = "use", at = @At("HEAD"), cancellable = true)
   private void useHook(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
      if (AutoTridentModule.getAutoTridentModule().check7()) {
         ItemStack itemStack = user.getStackInHand(hand);
         if (!isAboutToBreak(itemStack)) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(itemStack));
         }
      }
   }

   @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
   private void onStoppedUsingHook(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
      if (user instanceof PlayerEntity playerEntity) {
         ci.cancel();
         int ticks = this.getMaxUseTime(stack, user) - remainingUseTicks;
         float power = EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity);
         OnStoppedUsingEvent event = this.invoke(new OnStoppedUsingEvent(ticks, false, false));
         if ((event.isCancelled() || event.getIntValue() >= 10)
            && (event.isFlag() || !(power > 0.0F) || playerEntity.isTouchingWaterOrRain())
            && !isAboutToBreak(stack)) {
            RegistryEntry<SoundEvent> registryEntry = EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND)
               .orElse(SoundEvents.ITEM_TRIDENT_THROW);
            if (!world.isClient) {
               stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
               if (power == 0.0F) {
                  TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
                  tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
                  if (playerEntity.isInCreativeMode()) {
                     tridentEntity.pickupType = PickupPermission.CREATIVE_ONLY;
                  }

                  world.spawnEntity(tridentEntity);
                  world.playSoundFromEntity(null, tridentEntity, (SoundEvent)registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                  if (!playerEntity.isInCreativeMode()) {
                     playerEntity.getInventory().removeOne(stack);
                  }
               }
            }

            playerEntity.incrementStat(Stats.USED.getOrCreateStat((TridentItem)(Object)this));
            if (power > 0.0F) {
               float yaw = playerEntity.getYaw();
               float pitch = playerEntity.getPitch();
               float x = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
               float y = -MathHelper.sin(pitch * (float) (Math.PI / 180.0));
               float z = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
               float length = MathHelper.sqrt(x * x + y * y + z * z);
               x *= power / length;
               y *= power / length;
               z *= power / length;
               playerEntity.addVelocity(x, y, z);
               if (!event.isFlag2()) {
                  playerEntity.useRiptide(20, 8.0F, stack);
               }

               if (playerEntity.isOnGround()) {
                  playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999F, 0.0));
               }

               if (!event.isFlag2()) {
                  world.playSoundFromEntity(null, playerEntity, (SoundEvent)registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
               }
            }
         }
      }
   }

   @Shadow
   public abstract int getMaxUseTime(ItemStack var1, LivingEntity var2);

   @Shadow
   private static boolean isAboutToBreak(ItemStack stack) {
      return false;
   }
}
