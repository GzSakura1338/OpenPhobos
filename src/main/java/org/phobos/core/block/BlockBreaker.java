package org.phobos.core.block;

import java.util.Objects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;

public abstract class BlockBreaker implements MinecraftHolder {
   public final FloatAnimator.Double doubleValue = new FloatAnimator.Double();
   @NotNull
   public BlockState blockState2;
   public float floatValue2;
   public BlockPos blockPos;
   public final PhobosClient phobosClient;
   public final SpeedmineModule speedmineModule;
   public float floatValue3 = 0.0F;

   @NotNull
   public BlockState getBlockState2() {
      return this.blockState2;
   }

   public void handleFloat2(float f) {
      this.floatValue3 = f;
      this.floatValue2 = f;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public void setBlockPos(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public abstract void handleClientPlayerEntity4(ClientPlayerEntity var1, ClientWorld var2, ClientPlayerInteractionManager var3, boolean var4);

   public abstract void handleClientWorld(ClientWorld var1);

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public SpeedmineModule getSpeedmineModule() {
      return this.speedmineModule;
   }

   public void handleBlockState2(@NotNull BlockState blockState) {
      if (blockState == null) {
         throw new NullPointerException("state is marked non-null but is null");
      }

      this.blockState2 = blockState;
   }

   public float computeBlockPos(
      BlockPos blockPos, BlockState blockState, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, ItemStack itemStack, boolean flag
   ) {
      float f = blockState.getHardness(clientWorld, blockPos);
      if (f == -1.0F) {
         return 0.0F;
      }

      int i = blockState.isToolRequired() && !itemStack.isSuitableFor(blockState) ? 100 : 30;
      return this.computeClientPlayerEntity(clientPlayerEntity, itemStack, blockState, flag) / f / i;
   }

   public float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ItemStack itemStack, BlockState blockState, boolean flag) {
      float f = itemStack.getMiningSpeedMultiplier(blockState);
      if (f > 1.0F) {
         int i = EnchantmentUtils.computeItemStack3(itemStack, Enchantments.EFFICIENCY);
         if (i > 0 && !itemStack.isEmpty()) {
            f += i * i + 1;
         }
      }

      if (StatusEffectUtil.hasHaste(clientPlayerEntity)) {
         f *= 1.0F + (StatusEffectUtil.getHasteAmplifier(clientPlayerEntity) + 1) * 0.2F;
      }

      if (clientPlayerEntity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
         f *= switch (clientPlayerEntity.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
            case 0 -> 0.3F;
            case 1 -> 0.09F;
            case 2 -> 0.0027F;
            default -> 8.1E-4F;
         };
      }

      f *= (float)clientPlayerEntity.getAttributeValue(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
      if (clientPlayerEntity.isSubmergedIn(FluidTags.WATER)) {
         f /= 5.0F;
      }

      if (!flag) {
         f /= 5.0F;
      }

      return Math.max(f, 0.0F);
   }

   public FloatAnimator.Double getDoubleValue() {
      return this.doubleValue;
   }

   public BlockBreaker(PhobosClient phobosClient, SpeedmineModule speedmineModule) {
      this.floatValue2 = 0.0F;
      this.blockState2 = Blocks.AIR.getDefaultState();
      this.phobosClient = phobosClient;
      this.speedmineModule = speedmineModule;
   }

   public boolean testBlockPos(BlockPos blockPos) {
      return this.blockPos != null && this.blockPos.equals(blockPos);
   }

   public boolean check() {
      return this.blockPos != null;
   }

   public abstract void handleClientPlayerEntity5(ClientPlayerEntity var1);

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      if (this.blockPos != null) {
         clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, this.blockPos, Direction.DOWN));
      }
   }

   public abstract void run2();

   public void handleFlag(boolean flag, boolean... flags) {
      this.doubleValue.setDoubleValue2(0.0);
      if ((flags.length == 0 || !flags[0]) && (flag || this.speedmineModule.renderReset.getValue())) {
         this.doubleValue.setDoubleValue(0.0);
      }
   }

   public double[] values(Box box) {
      return new double[]{(box.maxX - box.minX) / 2.0, (box.maxY - box.minY) / 2.0, (box.maxZ - box.minZ) / 2.0};
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public void handleClientPlayerEntity9(ClientPlayerEntity clientPlayerEntity, BlockPos blockPos) {
      if (Objects.equals(blockPos, this.blockPos)) {
         this.handleClientPlayerEntity(clientPlayerEntity);
      }
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return this.blockPos == null
         ? true
         : clientPlayerEntity.getEyePos().squaredDistanceTo(this.blockPos.getX() + 0.5, this.blockPos.getY() + 0.5, this.blockPos.getZ() + 0.5)
            > MathHelper.square(this.speedmineModule.range.getValue());
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }
}
