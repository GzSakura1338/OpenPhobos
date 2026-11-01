package org.phobos.core.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IDamageProtectionEntity;
import org.phobos.api.Raytracer;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.client.CalculationsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.math.MutableBox;

public class DamageCalculator {
   public final Raytracer raytracer;
   public static final float floatValue = 5.0F;
   public static final float floatValue2 = 5.0F;
   public static final Logger LOGGER = LoggerFactory.getLogger(DamageCalculator.class);
   public static final float floatValue3 = 6.0F;

   public float computeEntity(Entity entity, World world, BlockPos blockPos) {
      return this.computeEntity2(entity, world, 6.0F, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, null);
   }

   public float computeEntity2(Entity entity, World world, float f, double d, double d2, double d3, Collection<BlockPos> collection) {
      if (entity instanceof PlayerEntity playerEntity && playerEntity.isCreative()) {
         return 0.0F;
      } else {
         float f2 = f * 2.0F;
         double d4 = Math.sqrt(entity.squaredDistanceTo(d, d2, d3)) / f2;
         if (!(d4 > 1.0) && !this.testEntity(entity) && this.resolveDifficulty(world) != Difficulty.PEACEFUL) {
            double d5 = this.computeRaytracer(this.raytracer, world, entity, d, d2, d3, DamageCalculator.CalcType.Normal, collection);
            double d6 = (1.0 - d4) * d5;
            float f3 = (int)((d6 * d6 + d6) / 2.0 * 7.0 * f2 + 1.0);
            if (f3 == 0.0F) {
               return 0.0F;
            }

            if (entity instanceof LivingEntity livingEntity) {
               if (entity instanceof PlayerEntity playerEntity2) {
                  f3 = this.computeWorld(world, f3);
               }

               f3 = this.computeFloat(f3, livingEntity.getArmor(), (float)livingEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
               StatusEffectInstance statusEffectInstance = livingEntity.getStatusEffect(StatusEffects.RESISTANCE);
               if (statusEffectInstance != null) {
                  int i = (statusEffectInstance.getAmplifier() + 1) * 5;
                  f3 = Math.max(f3 * (25 - i) / 25.0F, 0.0F);
                  if (f3 <= 0.0F) {
                     return 0.0F;
                  }
               }

               int i2 = this.computeLivingEntity(livingEntity);
               if (i2 > 0) {
                  f3 = DamageUtil.getInflictedDamage(f3, i2);
               }
            }

            return Math.max(f3, 0.0F);
         } else {
            return 0.0F;
         }
      }
   }

   public float computeEntity3(Entity entity, World world, float f, Vec3d vec3d, List<BlockPos> list) {
      return this.computeEntity2(entity, world, f, vec3d.x, vec3d.y, vec3d.z, list);
   }

   public float computeEntity4(Entity entity, World world, float f, double d, double d2, double d3) {
      return this.computeEntity2(entity, world, f, d, d2, d3, new ArrayList<>());
   }

   public DamageCalculator(Raytracer raytracer) {
      this.raytracer = raytracer;
   }

   public boolean testEntity(Entity entity) {
      return false;
   }

   public float computeEntity5(Entity entity, World world, Entity entity2, Collection<BlockPos> collection) {
      return this.computeEntity2(entity, world, 6.0F, entity2.getX(), entity2.getY(), entity2.getZ(), collection);
   }

   public Difficulty resolveDifficulty(World world) {
      return world.getDifficulty();
   }

   public float computeRaytracer(
      Raytracer raytracer, World world, Entity entity, double d, double d2, double d3, DamageCalculator.CalcType calcType, Collection<BlockPos> collection
   ) {
      MutableBox mutableBox = new MutableBox();
      Box box = entity.getBoundingBox();
      Vec3d vec3d2 = new Vec3d(d, d2, d3);
      double d10 = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
      double d11 = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
      double d12 = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
      double d13 = (1.0 - Math.floor(1.0 / d10) * d10) / 2.0;
      double d14 = (1.0 - Math.floor(1.0 / d12) * d12) / 2.0;
      if (!(d10 < 0.0) && !(d11 < 0.0) && !(d12 < 0.0)) {
         int i = 0;
         int i2 = 0;

         for (double d9 = 0.0; d9 <= 1.0; d9 += d10) {
            for (double d8 = 0.0; d8 <= 1.0; d8 += d11) {
               for (double d7 = 0.0; d7 <= 1.0; d7 += d12) {
                  double d4 = MathHelper.lerp(d9, box.minX, box.maxX);
                  double d5 = MathHelper.lerp(d8, box.minY, box.maxY);
                  double d6 = MathHelper.lerp(d7, box.minZ, box.maxZ);
                  Vec3d vec3d = new Vec3d(d4 + d13, d5, d6 + d14);
                  BlockHitResult blockHitResult = raytracer.resolveBlockHitResult2(
                     world, mutableBox, new RaycastContext(vec3d, vec3d2, ShapeType.COLLIDER, FluidHandling.NONE, entity)
                  );
                  if (blockHitResult.getType() == Type.MISS) {
                     i++;
                  }

                  if (blockHitResult.getType() == Type.BLOCK) {
                     BlockPos blockPos = blockHitResult.getBlockPos();
                     BlockState blockState = world.getBlockState(blockPos);
                     if (calcType.equals(DamageCalculator.CalcType.Bed) && blockState.getBlock() instanceof BedBlock) {
                        i++;
                     }

                     if (calcType.equals(DamageCalculator.CalcType.Anchor) && world.getBlockState(blockPos).getBlock() instanceof RespawnAnchorBlock) {
                        i++;
                     }

                     if (collection != null && collection.contains(blockPos)) {
                        i++;
                     }

                     if (CalculationsModule.check2() && blockState.getBlock().getBlastResistance() < 35.0F) {
                        i++;
                     }
                  }

                  i2++;
               }
            }
         }

         return (float)i / i2;
      } else {
         return 0.0F;
      }
   }

   public float computeEntity6(Entity entity, World world, float f, Vec3d vec3d) {
      return this.computeEntity3(entity, world, f, vec3d, new ArrayList<>());
   }

   public static void handleClientWorld(
      ClientWorld clientWorld, Predicate<PlayerEntity> predicate, Function<PlayerEntity, Float> function, Consumer<Float> consumer
   ) {
      MutableObject<Float> mutableObject = new MutableObject(0.0F);

      for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
         if (predicate.test(playerEntity)) {
            float f = function.apply(playerEntity);
            if ((Float)mutableObject.getValue() < f) {
               mutableObject.setValue(f);
            }
         }
      }

      consumer.accept((Float)mutableObject.getValue());
   }

   public Raytracer getRaytracer() {
      return this.raytracer;
   }

   public float computeEntity7(Entity entity, World world, BlockPos blockPos, Collection<BlockPos> collection) {
      return this.computeEntity2(entity, world, 6.0F, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, collection);
   }

   public float computeFloat(float f, float f2, float f3) {
      float f4 = 2.0F + f3 / 4.0F;
      float f5 = MathHelper.clamp(f2 - f / f4, f2 * 0.2F, 20.0F);
      return f * (1.0F - f5 / 25.0F);
   }

   public float computeEntity8(Entity entity, World world, Entity entity2) {
      return this.computeEntity2(entity, world, 6.0F, entity2.getX(), entity2.getY(), entity2.getZ(), null);
   }

   public int computeLivingEntity(LivingEntity livingEntity) {
      if (livingEntity instanceof FakePlayer fakePlayer && fakePlayer.isFlag2()) {
         return fakePlayer.getIntValue2();
      } else {
         return CalculationsModule.getDamage().equals(CalculationsModule.Damage.Generic) ? 32 : ((IDamageProtectionEntity)(Object)livingEntity).getIntValue();
      }
   }

   public float computeEntity9(Entity entity, World world, BlockPos blockPos) {
      BlockState blockState = world.getBlockState(blockPos);
      BlockPos blockPos2;
      if (blockState.get(BedBlock.PART) != BedPart.HEAD) {
         blockPos2 = blockPos.offset((Direction)blockState.get(BedBlock.FACING));
      } else {
         blockPos2 = blockPos;
      }

      return this.computeEntity6(entity, world, 5.0F, blockPos2.toCenterPos());
   }

   public float computeWorld(World world, float f) {
      return switch (this.resolveDifficulty(world)) {
         case PEACEFUL -> 0.0F;
         case EASY -> Math.min(f / 2.0F + 1.0F, f);
         case HARD -> f * 3.0F / 2.0F;
         default -> f;
      };
   }

   public enum CalcType {
      Normal,
      Bed,
      Anchor;
   }
}
