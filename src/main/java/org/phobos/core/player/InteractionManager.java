package org.phobos.core.player;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.TextProvider;
import org.phobos.event.SafeListener;
import org.phobos.mixin.IMinecraftClient;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.core.placement.DefaultDirectionResolver;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.tick.TickUtils;

public class InteractionManager extends Feature {
   public static BiConsumer<BlockHitResult, float[]> blockHitResultBiConsumer;
   public static BiFunction<BlockHitResult, float[], @NotNull BlockHitResult> blockHitResultBiFunction;
   public final PhobosClient phobosClient;
   public volatile boolean flag;

   public static BlockHitResult resolveBlockHitResult(ClientPlayerEntity clientPlayerEntity, BlockHitResult blockHitResult) {
      if (blockHitResultBiFunction != null) {
         BlockHitResult blockHitResult2 = blockHitResultBiFunction.apply(blockHitResult, RotationUtils.values2(clientPlayerEntity, blockHitResult.getPos()));
         blockHitResultBiFunction = null;
         return blockHitResult2;
      } else {
         return blockHitResult;
      }
   }

   public void run() {
      ((IMinecraftClient)(Object)mc).setItemUseCooldown(0);
      mc.doItemUse();
   }

   public ActionResult resolveActionResult(ClientPlayerEntity clientPlayerEntity, Hand hand, Hand hand2, BlockHitResult blockHitResult) {
      assert mc.interactionManager != null;
      assert mc.world != null;
      mc.interactionManager.syncSelectedSlot();
      if (!mc.world.getWorldBorder().contains(blockHitResult.getBlockPos())) {
         return ActionResult.FAIL;
      }

      MutableObject<ActionResult> mutableObject = new MutableObject();
      TickUtils.handleClientWorld(mc.world, integer -> {
         mutableObject.setValue(this.resolveActionResult2(clientPlayerEntity, hand2, blockHitResult));
         clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, blockHitResult, integer));
      });
      return (ActionResult)mutableObject.getValue();
   }

   public Consumer<BlockHitResult> getConsumer() {
      return blockHitResult -> {
         if (mc.player != null && mc.interactionManager != null) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
         }
      };
   }

   public void handleEntity(Entity entity, ClientPlayerEntity clientPlayerEntity, InteractionManager.SwitchMode switchMode, InteractionManager.Silent silent) {
      if (entity instanceof EndCrystalEntity && this.flag && !switchMode.equals(InteractionManager.SwitchMode.Off)) {
         this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
            Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().getItem() instanceof SwordItem ? slotx : null);
            if (slot != null) {
               switch (switchMode) {
                  case Normal:
                     itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, silent.getFlag());
                     break;
                  case Silent:
                     itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, silent.getFlag() | 1);
               }
            }

            this.handleEntity3(entity, clientPlayerEntity);
         });
      } else {
         this.handleEntity3(entity, clientPlayerEntity);
      }
   }

   public void handleBiConsumer(BiConsumer<BlockHitResult, float[]> biConsumer) {
      blockHitResultBiConsumer = biConsumer;
   }

   public ActionResult resolveActionResult2(ClientPlayerEntity clientPlayerEntity, Hand hand, BlockHitResult blockHitResult) {
      assert mc.world != null;
      BlockPos blockPos = blockHitResult.getBlockPos();
      ItemStack itemStack = clientPlayerEntity.getStackInHand(hand);
      if (clientPlayerEntity.isSpectator()) {
         return ActionResult.SUCCESS;
      }

      boolean flag = !clientPlayerEntity.getMainHandStack().isEmpty() || !clientPlayerEntity.getOffHandStack().isEmpty();
      boolean flag2 = clientPlayerEntity.shouldCancelInteraction() && flag;
      if (!flag2) {
         BlockState blockState = mc.world.getBlockState(blockPos);
         if (!clientPlayerEntity.networkHandler.hasFeature(blockState.getBlock().getRequiredFeatures())) {
            return ActionResult.FAIL;
         }

         ItemActionResult itemActionResult = blockState.onUseWithItem(
            clientPlayerEntity.getStackInHand(hand), mc.world, clientPlayerEntity, hand, blockHitResult
         );
         if (itemActionResult.isAccepted()) {
            return itemActionResult.toActionResult();
         }

         if (itemActionResult == ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION && hand == Hand.MAIN_HAND) {
            ActionResult actionResult = blockState.onUse(mc.world, clientPlayerEntity, blockHitResult);
            if (actionResult.isAccepted()) {
               return actionResult;
            }
         }
      }

      if (!itemStack.isEmpty() && !clientPlayerEntity.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
         ItemUsageContext itemUsageContext = new ItemUsageContext(clientPlayerEntity, hand, blockHitResult);
         ActionResult actionResult2;
         if (clientPlayerEntity.isCreative()) {
            int i = itemStack.getCount();
            actionResult2 = itemStack.useOnBlock(itemUsageContext);
            itemStack.setCount(i);
         } else {
            actionResult2 = itemStack.useOnBlock(itemUsageContext);
         }

         return actionResult2;
      } else {
         return ActionResult.PASS;
      }
   }

   public void handleEntity2(Entity entity, ClientPlayerEntity clientPlayerEntity, Hand hand) {
      clientPlayerEntity.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(entity, false, hand));
   }

   public void handleBiFunction(BiFunction<BlockHitResult, float[], @NotNull BlockHitResult> biFunction) {
      blockHitResultBiFunction = biFunction;
   }

   public void handleBlockPos(
      BlockPos blockPos,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      Direction direction,
      DirectionResolver directionResolver,
      Consumer<BlockHitResult> consumer
   ) {
      if (directionResolver.testBlockPos(blockPos, direction, clientWorld, clientPlayerEntity)) {
         Vec3d vec3d = new Vec3d(
            blockPos.getX() + 0.5 - direction.getOffsetX() * 0.5 + direction.getOffsetX(),
            blockPos.getY() + 0.5 - direction.getOffsetY() * 0.5 + direction.getOffsetY(),
            blockPos.getZ() + 0.5 - direction.getOffsetZ() * 0.5 + direction.getOffsetZ()
         );
         BlockHitResult blockHitResult = new BlockHitResult(vec3d, direction, blockPos, false);
         consumer.accept(blockHitResult);
      }
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Entity entity, boolean... flags) {
      if (mc.interactionManager != null) {
         mc.interactionManager.attackEntity(clientPlayerEntity, entity);
         if (flags.length == 0 || flags[0]) {
            clientPlayerEntity.swingHand(Hand.MAIN_HAND);
         }
      }
   }

   public InteractionManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      this.handleListener(
         new SafeListener<PlayerMoveEvent>() {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               InteractionManager.this.flag = clientPlayerEntity.hasStatusEffect(StatusEffects.WEAKNESS);
            }
         }
      );
   }

   public static void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, BlockHitResult blockHitResult) {
      if (blockHitResultBiConsumer != null) {
         blockHitResultBiConsumer.accept(blockHitResult, RotationUtils.values2(clientPlayerEntity, blockHitResult.getPos()));
         blockHitResultBiConsumer = null;
      }
   }

   public void handleBlockPos2(
      BlockPos blockPos, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, DirectionResolver directionResolver, Consumer<BlockHitResult> consumer
   ) {
      BlockHitResult blockHitResult;
      if (!DefaultDirectionResolver.defaultDirectionResolver.equals(directionResolver)) {
         Direction direction = directionResolver.resolveDirection(blockPos, clientPlayerEntity, clientWorld);
         if (direction == null) {
            return;
         }

         Vec3d vec3d = new Vec3d(
            blockPos.getX() + 0.5 - direction.getOffsetX() * 0.5 + direction.getOffsetX(),
            blockPos.getY() + 0.5 - direction.getOffsetY() * 0.5 + direction.getOffsetY(),
            blockPos.getZ() + 0.5 - direction.getOffsetZ() * 0.5 + direction.getOffsetZ()
         );
         blockHitResult = new BlockHitResult(vec3d, direction, blockPos, false);
      } else {
         blockHitResult = new BlockHitResult(new Vec3d(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5), Direction.DOWN, blockPos, false);
      }

      consumer.accept(blockHitResult);
   }

   public void handleEntity3(Entity entity, ClientPlayerEntity clientPlayerEntity) {
      clientPlayerEntity.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(entity, false));
   }

   public interface Interaction {
      InteractionManager.Interaction INSTANCE = (clientPlayerEntity, clientWorld, hand, blockHitResult) -> TickUtils.handleClientWorld(clientWorld, integer -> {
         BlockHitResult blockHitResult2 = InteractionManager.resolveBlockHitResult(clientPlayerEntity, blockHitResult);
         clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, blockHitResult2, integer));
         InteractionManager.handleClientPlayerEntity2(clientPlayerEntity, blockHitResult2);
         clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
      });
      InteractionManager.Interaction INSTANCE2 = (clientPlayerEntity, clientWorld, hand, blockHitResult) -> TickUtils.handleClientWorld(
         clientWorld,
         integer -> {
            BlockHitResult blockHitResult2 = InteractionManager.resolveBlockHitResult(clientPlayerEntity, blockHitResult);
            clientPlayerEntity.networkHandler
               .sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockHitResult.getBlockPos(), blockHitResult.getSide(), integer));
            InteractionManager.handleClientPlayerEntity2(clientPlayerEntity, blockHitResult2);
            clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
         }
      );

      void handleClientPlayerEntity(ClientPlayerEntity var1, ClientWorld var2, Hand var3, BlockHitResult var4);
   }

   public enum Silent implements TextProvider {
      Normal(() -> 2, "Normal hotbar select action"),
      Swap(() -> 0, "Swap clicks with buttons"),
      Inventory(() -> 8, "Cycles items through inventory"),
      Pick(() -> 16, "Picks the item"),
      Sync(() -> SpeedmineModule.check2() ? 16 : 2, "Ensures that speedmine won't be interrupted"),
      Offhand(() -> 32, "Offhand silent");

      public final Supplier<Integer> flag;
      public final String text;

      Silent(final Supplier<Integer> flag, final String description) {
         this.flag = flag;
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }

      public int getFlag() {
         return this.flag.get();
      }
   }

   public enum SwitchMode implements TextProvider {
      Off("Does nothing"),
      Normal("Selects the item"),
      Silent("Selects the item and then switches back");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      SwitchMode(final String description) {
         this.text = description;
      }
   }
}
