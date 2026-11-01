package org.phobos.mixin.game.world;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.inventory.SlotSwap;
import org.phobos.core.event.events.UpdateBreakEvent;
import org.phobos.core.event.events.StartBreakEvent;
import org.phobos.core.event.events.FinishBreakEvent;
import org.phobos.event.AttackEvent2;
import org.phobos.event.AttackEvent2;
import org.phobos.event.BreakBlockEvent;
import org.phobos.event.InteractEvent;
import org.phobos.event.UseItemEvent;
import org.phobos.module.player.SilentHotbarModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager implements org.earthhack.ducks.IClientPlayerInteractionManager, EventSubscriber {
   @Shadow
   private GameMode gameMode;
   @Shadow
   private float currentBreakingProgress;
   @Shadow
   private boolean breakingBlock;
   @Shadow
   @Final
   private ClientPlayNetworkHandler networkHandler;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   private int lastSelectedSlot;
   @Shadow
   public BlockPos currentBreakingPos;
   @Shadow
   private float blockBreakingSoundCooldown;
   @Shadow
   private int blockBreakingCooldown;

   @Shadow
   protected abstract void sendSequencedPacket(ClientWorld var1, SequencedPacketCreator var2);

   @Inject(method = "attackEntity", at = @At("HEAD"))
   private void attackHookPre(PlayerEntity player, Entity target, CallbackInfo ci) {
      this.invoke(new AttackEvent2.Pre(target), ci);
   }

   @Shadow
   public abstract boolean breakBlock(BlockPos var1);

   @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
   public void interactHook(ClientPlayerEntity player, Hand hand, BlockHitResult bhr, CallbackInfoReturnable<ActionResult> cir) {
      InteractEvent event = this.invoke(new InteractEvent(bhr, player.getStackInHand(hand)));
      if (event.isCancelled()) {
         cir.setReturnValue(ActionResult.FAIL);
      }
   }

   @Shadow
   public abstract void syncSelectedSlot();

   @Shadow
   public abstract boolean attackBlock(BlockPos var1, Direction var2);

   @Shadow
   protected abstract boolean isCurrentlyBreaking(BlockPos var1);

   @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
   public void updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
      StartBreakEvent event = this.invoke(new StartBreakEvent(pos, direction));
      if (event.isCancelled()) {
         this.breakingBlock = false;
         cir.setReturnValue(true);
      }
   }

   @Inject(method = "syncSelectedSlot", at = @At("HEAD"), cancellable = true)
   public final void syncSelectedSlot(CallbackInfo ci) {
      ci.cancel();
      if (this.client.player != null) {
         int slot = SilentHotbarModule.getSilentHotbarManager().value();
         if (slot != this.lastSelectedSlot) {
            int lastSlot = this.lastSelectedSlot;
            this.lastSelectedSlot = slot;
            this.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.lastSelectedSlot));
            this.invoke(new SlotSwap(this.resolveSlot(lastSlot), this.resolveSlot(slot)));
         }
      }
   }

   @Inject(method = "cancelBlockBreaking", at = @At("HEAD"), cancellable = true)
   public void cancelBlockBreaking(CallbackInfo ci) {
      this.invoke(new UpdateBreakEvent(), ci);
   }

   @Shadow
   public abstract int getBlockBreakingProgress();

   @Inject(
      method = "attackEntity",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift = Shift.AFTER)
   )
   private void attackHook(PlayerEntity player, Entity target, CallbackInfo ci) {
      this.invoke(new AttackEvent2(target), ci);
   }

   @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
   private void breakBlockHook(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      BreakBlockEvent event = this.invoke(new BreakBlockEvent(pos));
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }
   }

   @Override
   public boolean updateBreaking(BlockPos pos, Direction direction) {
      this.syncSelectedSlot();
      if (this.blockBreakingCooldown > 0) {
         this.blockBreakingCooldown--;
         return true;
      }

      if (this.gameMode.isCreative() && this.client.world.getWorldBorder().contains(pos)) {
         this.blockBreakingCooldown = 5;
         BlockState blockState = this.client.world.getBlockState(pos);
         this.client.getTutorialManager().onBlockBreaking(this.client.world, pos, blockState, 1.0F);
         this.sendSequencedPacket(this.client.world, sequence -> {
            this.breakBlock(pos);
            return new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, pos, direction, sequence);
         });
         return true;
      }

      if (this.isCurrentlyBreaking(pos)) {
         BlockState blockState = this.client.world.getBlockState(pos);
         if (blockState.isAir()) {
            this.breakingBlock = false;
            return false;
         }

         this.currentBreakingProgress = this.currentBreakingProgress
            + blockState.calcBlockBreakingDelta(this.client.player, this.client.player.getWorld(), pos);
         if (this.blockBreakingSoundCooldown % 4.0F == 0.0F) {
            BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
            this.client
               .getSoundManager()
               .play(
                  new PositionedSoundInstance(
                     blockSoundGroup.getHitSound(),
                     SoundCategory.BLOCKS,
                     (blockSoundGroup.getVolume() + 1.0F) / 8.0F,
                     blockSoundGroup.getPitch() * 0.5F,
                     SoundInstance.createRandom(),
                     pos
                  )
               );
         }

         this.blockBreakingSoundCooldown++;
         this.client.getTutorialManager().onBlockBreaking(this.client.world, pos, blockState, MathHelper.clamp(this.currentBreakingProgress, 0.0F, 1.0F));
         if (this.currentBreakingProgress >= 1.0F) {
            this.breakingBlock = false;
            this.sendSequencedPacket(this.client.world, sequence -> {
               this.breakBlock(pos);
               return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, direction, sequence);
            });
            this.currentBreakingProgress = 0.0F;
            this.blockBreakingSoundCooldown = 0.0F;
            this.blockBreakingCooldown = 5;
         }

         this.client.world.setBlockBreakingInfo(this.client.player.getId(), this.currentBreakingPos, this.getBlockBreakingProgress());
         return true;
      } else {
         return this.attackBlock(pos, direction);
      }
   }

   @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
   public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
      FinishBreakEvent event = this.invoke(new FinishBreakEvent(pos, direction));
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }
   }

   @Unique
   private Slot resolveSlot(int index) {
      for (int i = 36; i < 45; i++) {
         assert this.client.player != null;
         Slot slot = this.client.player.playerScreenHandler.getSlot(i);
         if (slot.getIndex() == index) {
            return slot;
         }
      }

      throw new IllegalArgumentException("Index out of bounds: " + index);
   }

   @Inject(
      method = "interactItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V"),
      cancellable = true
   )
   private void useItemHook(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
      UseItemEvent event = this.invoke(new UseItemEvent(hand, player));
      if (event.isCancelled()) {
         cir.setReturnValue(ActionResult.FAIL);
      }
   }
}
