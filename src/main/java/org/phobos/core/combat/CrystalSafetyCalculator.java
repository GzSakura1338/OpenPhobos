package org.phobos.core.combat;

import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.OffThreadException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IEntity;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.BlockStateAccess;
import org.phobos.mixin.game.entity.IPersistentProjectileEntity;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.player.AutoEnderChestModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.player.SpeedmineModule;
import org.phobos.module.player.SuicideModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.block.BlockBreaker;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.task.RetryUtils;
import org.phobos.core.rotation.RotationUtils;

public class CrystalSafetyCalculator implements MinecraftHolder {
   public final ClientWorld clientWorld;
   public final ClientPlayerEntity clientPlayerEntity;
   public final DamageCalculator damageCalculator;
   public boolean flag;
   public boolean flag2 = true;
   public final ClientWorld clientWorld2;
   public final PhobosClient phobosClient;
   public static final Logger LOGGER = LoggerFactory.getLogger(CrystalSafetyCalculator.class);
   public final PlacesCrystalsModule placesCrystalsModule;
   public boolean flag3;

   public boolean testEndCrystalEntity(EndCrystalEntity endCrystalEntity) {
      if (!this.placesCrystalsModule.getBooleanValueSetting27().getValue() && this.clientPlayerEntity.isUsingItem()) {
         return false;
      }

      if (this.placesCrystalsModule.alwaysTrue2() && mc.interactionManager != null) {
         if (this.placesCrystalsModule.getBooleanValueSetting17().getValue()
            && !RaycastUtils.testPhobosClient(this.placesCrystalsModule.getPhobosClient(), endCrystalEntity)) {
            float[] floats = RotationUtils.values3(this.clientPlayerEntity, endCrystalEntity, this.placesCrystalsModule.getIntegerSetting9().getValue());
            switch ((PlacesCrystalsModule.Rotate)this.placesCrystalsModule.getRotateSetting().getValue()) {
               case Normal:
               case Hybrid:
                  if (!this.phobosClient.getRotationManager().isFlag2()) {
                     if (this.placesCrystalsModule.getBooleanValueSetting11().getValue()) {
                        this.placesCrystalsModule.getEndCrystalEntityMutableObject().setValue(endCrystalEntity);
                     }

                     this.flag3 = true;
                     return false;
                  }

                  this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                  break;
               case Silent:
                  if (!this.placesCrystalsModule.getIntegerSetting6().testObject3(-1)
                     && this.placesCrystalsModule.getAtomicInteger2().get() > this.placesCrystalsModule.getIntegerSetting6().getValue()) {
                     this.flag3 = true;
                     return false;
                  }

                  this.phobosClient.getRotationManager().handleClientPlayerEntity3(this.clientPlayerEntity, floats[0], floats[1]);
                  this.placesCrystalsModule.getAtomicInteger2().incrementAndGet();
            }
         }

         this.placesCrystalsModule.handleClientPlayerEntity3(this.clientPlayerEntity, endCrystalEntity, false);
         this.placesCrystalsModule.handleEndCrystalEntity3(endCrystalEntity, false);
         if (this.placesCrystalsModule.getBooleanValueSetting17().getValue() && this.placesCrystalsModule.getRotateSetting().getValue().reset()) {
            this.phobosClient.getRotationManager().handleClientPlayerEntity5(this.clientPlayerEntity);
         }

         this.placesCrystalsModule.getThreaded2().run();
         this.flag3 = true;
         return true;
      } else {
         return false;
      }
   }

   public boolean testItemStack(ItemStack itemStack) {
      if (itemStack.getItem() instanceof BlockItem blockItem) {
         Block block = blockItem.getBlock();
         if (block instanceof ShulkerBoxBlock) {
            return true;
         }
      }

      return itemStack.isOf(Items.TOTEM_OF_UNDYING) ? true : itemStack.getMaxCount() == 1;
   }

   public boolean check() {
      return !this.placesCrystalsModule.getBooleanValueSetting21().getValue()
         ? false
         : !this.phobosClient.getModuleManager().resolveOptional2(AutoEnderChestModule.class).orElseThrow().check7();
   }

   public boolean testLivingEntity(LivingEntity livingEntity, double d, double d2, double d3) {
      if (livingEntity == null) {
         return false;
      } else if (!this.placesCrystalsModule.testEntity2(livingEntity)) {
         return false;
      } else if (livingEntity instanceof IEntity iEntity && iEntity.isClientSide()) {
         return false;
      } else {
         return !(livingEntity instanceof PlayerEntity playerEntity && (playerEntity.isCreative() || playerEntity.isSpectator()))
            ? !livingEntity.isDead()
               && !livingEntity.equals(this.clientPlayerEntity)
               && livingEntity.isAlive()
               && !(livingEntity.squaredDistanceTo(this.clientPlayerEntity) > MathHelper.square(324.0))
               && !(livingEntity.squaredDistanceTo(d, d2, d3) > 144.0)
            : false;
      }
   }

   public float computeEntity(Entity entity, Entity entity2, World world) {
      return RetryUtils.resolveObject(3, () -> OffThreadException.INSTANCE, () -> this.damageCalculator.computeEntity8(entity2, world, entity));
   }

   public boolean testEndCrystalEntity2(EndCrystalEntity endCrystalEntity, MutableObject<Float> mutableObject, MutableBoolean mutableBoolean) {
      if (((IEntity)(Object)endCrystalEntity).value() < this.placesCrystalsModule.getLongValueSetting5().getValue()) {
         return false;
      }

      float f4 = this.computeEntity2(this.clientPlayerEntity, this.computeEntity4(endCrystalEntity));
      boolean flag2 = false;
      if (f4 < EntityUtils.computeLivingEntity(this.clientPlayerEntity) || this.placesCrystalsModule.getBooleanValueSetting18().getValue()) {
         for (Entity entity2 : this.clientWorld.getEntities()) {
            if (entity2 instanceof LivingEntity livingEntity) {
               if (this.testLivingEntity(livingEntity, endCrystalEntity.getX(), endCrystalEntity.getY(), endCrystalEntity.getZ())
                  && (this.placesCrystalsModule instanceof SuicideModule || !this.phobosClient.getFriendList().testUUID(livingEntity.getUuid()))) {
                  float f3 = this.computeEntity2(livingEntity, this.computeEntity3(endCrystalEntity, livingEntity));
                  boolean flag = f3
                     >= this.phobosClient
                        .getFeatureRegistry()
                        .getDamageTracker()
                        .computeLivingEntity(livingEntity, EntityUtils.computeLivingEntity(livingEntity));
                  if (f3 > (Float)mutableObject.getValue() && (flag || f3 >= 0.5) && (!mutableBoolean.getValue() || flag)) {
                     mutableBoolean.setValue(flag);
                     mutableObject.setValue(f3);
                     flag2 = true;
                  }
               }
            } else if (entity2 instanceof ItemEntity itemEntity) {
               if (this.check() && this.resolveEntity2(itemEntity) != null) {
                  float f = this.computeEntity(endCrystalEntity, itemEntity, this.clientWorld2);
                  if (f > 5.0F && (Float)mutableObject.getValue() < f) {
                     mutableBoolean.setValue(true);
                     mutableObject.setValue(f);
                     flag2 = true;
                  }
               }
            } else if (this.placesCrystalsModule.getDeflectorSetting().testObject2(PlacesCrystalsModule.Deflector.Boost)
               && entity2 instanceof Ownable ownable
               && (ownable instanceof EnderPearlEntity || ownable instanceof ArrowEntity arrowEntity && !((IPersistentProjectileEntity)(Object)arrowEntity).collided())
               && this.testEntity(entity2)) {
               Entity entity = ownable.getOwner();
               if (entity != null && !entity.equals(this.clientPlayerEntity) && !this.phobosClient.getFriendList().testUUID(entity.getUuid())) {
                  float f2 = this.computeEntity(endCrystalEntity, entity2, this.clientWorld2) / this.placesCrystalsModule.getFloatValueSetting7().getValue();
                  if (f2 >= this.placesCrystalsModule.getFloatValueSetting8().getValue() && (Float)mutableObject.getValue() < f2) {
                     mutableBoolean.setValue(true);
                     mutableObject.setValue(f2);
                     flag2 = true;
                  }
               }
            }
         }
      }

      return flag2;
   }

   public Entity resolveEntity(LivingEntity livingEntity, boolean flag) {
      if (livingEntity instanceof PlayerEntity playerEntity && !playerEntity.isInSwimmingPose()) {
         int i = this.computeFlag(flag);
         if (i == -1) {
            PlayerEntity playerEntity2 = livingEntity instanceof ClientPlayerEntity clientPlayerEntity && this.placesCrystalsModule.getSelf().getValue()
               ? this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity4(clientPlayerEntity)
               : this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity(playerEntity);
            if (playerEntity2 != null) {
               return playerEntity2;
            }
         } else if (i > 0) {
            PlayerEntity playerEntity3 = livingEntity instanceof ClientPlayerEntity clientPlayerEntity2 && this.placesCrystalsModule.getSelf().getValue()
               ? this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity2(i, clientPlayerEntity2)
               : this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(i, playerEntity);
            if (playerEntity3 != null) {
               return playerEntity3;
            }
         }

         return livingEntity;
      } else {
         return livingEntity;
      }
   }

   public CrystalSafetyCalculator(
      PlacesCrystalsModule placesCrystalsModule,
      PhobosClient phobosClient,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      DamageCalculator damageCalculator
   ) {
      this.placesCrystalsModule = placesCrystalsModule;
      this.phobosClient = phobosClient;
      this.clientPlayerEntity = clientPlayerEntity;
      this.damageCalculator = damageCalculator;
      this.clientWorld = clientWorld;
      this.clientWorld2 = placesCrystalsModule.getMapSetting3().getValue().get(PlacesCrystalsModule.Addon.Mining)
         ? new BlockStateAccess.Delegating(clientWorld).resolveDelegating(delegating -> {
            Consumer<BlockBreaker> consumer = blockBreaker -> {
               if (blockBreaker.check()) {
                  if (blockBreaker.getFloatValue3() >= 0.5) {
                     delegating.getBlockPosMap().put(blockBreaker.getBlockPos(), Blocks.AIR.getDefaultState());
                  }
               }
            };
            consumer.accept(SpeedmineModule.getSpeedmineModule().getPacketBlockBreaker());
            consumer.accept(SpeedmineModule.getSpeedmineModule().getSimpleBlockBreaker());
         })
         : clientWorld;
   }

   @Nullable
   public Entity resolveEntity2(ItemEntity itemEntity) {
      if (!this.testItemStack(itemEntity.getStack()) && itemEntity.getStack().getCount() < this.placesCrystalsModule.getIntegerSetting11().getValue()) {
         return null;
      }

      for (Entity entity : this.clientWorld
         .getOtherEntities(null, BlockPosUtils.createBox(itemEntity, this.placesCrystalsModule.getDoubleValueSetting9().getValue() + 1.0))) {
         if (entity instanceof PlayerEntity playerEntity
            && this.testLivingEntity(playerEntity, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ())
            && !this.phobosClient.getFriendList().testUUID(playerEntity.getUuid())
            && !(playerEntity.getEyePos().distanceTo(itemEntity.getPos()) > this.placesCrystalsModule.getDoubleValueSetting9().getValue())) {
            return entity;
         }
      }

      return null;
   }

   public float computeEntity2(Entity entity, float f) {
      if (this.clientPlayerEntity.equals(entity) && this.clientPlayerEntity.isCreative()) {
         return 0.0F;
      } else {
         return this.placesCrystalsModule.getBooleanValueSetting10().getValue()
               && entity.getY() >= this.placesCrystalsModule.getDoubleValueSetting3().getValue()
            ? 0.0F
            : f;
      }
   }

   public float computeEntity3(Entity entity, LivingEntity livingEntity) {
      return RetryUtils.resolveObject(
         3, () -> OffThreadException.INSTANCE, () -> this.damageCalculator.computeEntity8(this.resolveEntity(livingEntity, false), this.clientWorld2, entity)
      );
   }

   public float computeEntity4(Entity entity) {
      return RetryUtils.resolveObject(
         3, () -> OffThreadException.INSTANCE, () -> this.damageCalculator.computeEntity8(this.clientPlayerEntity, this.clientWorld2, entity)
      );
   }

   public boolean check2() {
      return this.placesCrystalsModule.getBooleanValueSetting22().getValue();
   }

   public int computeFlag(boolean flag) {
      if (!this.placesCrystalsModule.getBooleanValueSetting54().getValue()) {
         return 0;
      }

      Integer integer = flag ? this.placesCrystalsModule.getIntegerSetting15().getValue() : this.placesCrystalsModule.getIntegerSetting16().getValue();
      return integer.equals(7) ? -1 : integer;
   }

   public float computeBlockPos(BlockPos blockPos, Entity entity, World world) {
      return RetryUtils.resolveObject(3, () -> OffThreadException.INSTANCE, () -> this.damageCalculator.computeEntity(entity, world, blockPos));
   }

   public void run2() {
      EndCrystalEntity endCrystalEntity2 = null;
      MutableObject<Float> mutableObject = new MutableObject(0.0F);
      MutableBoolean mutableBoolean = new MutableBoolean(false);
      double d2 = this.placesCrystalsModule.getDoubleValueSetting5().getValue();
      double d3 = this.placesCrystalsModule.value2();
      double d4 = Math.max(d2, d3);

      for (EndCrystalEntity endCrystalEntity : EntityUtils.resolveIterable(this.clientPlayerEntity, this.clientWorld, d4)) {
         double d = CrystalUtils.testPlayerEntity2(
               this.clientPlayerEntity, this.clientWorld, endCrystalEntity, AntiCheatModule.getAntiCheatModule().getSplitter().getValue()
            )
            ? d2
            : d3;
         PlayerActionUtils.PackedRangeCalculator packedRangeCalculator = this.placesCrystalsModule.createPackedRangeCalculator();
         if (packedRangeCalculator.testFunction2(rangeCalculator -> rangeCalculator.calculate(this.clientPlayerEntity, endCrystalEntity.getBoundingBox(), d))
            && this.testEndCrystalEntity2(endCrystalEntity, mutableObject, mutableBoolean)) {
            endCrystalEntity2 = endCrystalEntity;
         }
      }

      if (endCrystalEntity2 != null) {
         this.testEndCrystalEntity(endCrystalEntity2);
      }

      this.flag = endCrystalEntity2 != null;
   }

   public boolean testEntity(Entity entity) {
      return switch (entity) {
         case ArrowEntity arrowEntity -> this.placesCrystalsModule.getBooleanValueSetting30().getValue();
         case EnderPearlEntity enderPearlEntity -> this.placesCrystalsModule.getBooleanValueSetting35().getValue();
         default -> false;
      };
   }

   public boolean testEntity2(Entity entity) {
      if (entity == null) {
         return false;
      } else if (!this.placesCrystalsModule.testEntity2(entity)) {
         return false;
      } else {
         return !(entity instanceof PlayerEntity playerEntity && (playerEntity.isCreative() || playerEntity.isSpectator()))
            ? !(entity instanceof LivingEntity livingEntity && livingEntity.isDead()) && !entity.equals(this.clientPlayerEntity) && entity.isAlive()
            : false;
      }
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public boolean testLivingEntity2(LivingEntity livingEntity) {
      if (livingEntity == null) {
         return false;
      } else if (!this.placesCrystalsModule.testEntity2(livingEntity)) {
         return false;
      } else {
         return !(livingEntity instanceof PlayerEntity playerEntity && (playerEntity.isCreative() || playerEntity.isSpectator()))
            ? !livingEntity.isDead()
               && !livingEntity.equals(this.clientPlayerEntity)
               && livingEntity.isAlive()
               && !(livingEntity.squaredDistanceTo(this.clientPlayerEntity) > MathHelper.square(324.0))
            : false;
      }
   }
}
