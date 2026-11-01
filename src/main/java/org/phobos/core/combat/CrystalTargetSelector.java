package org.phobos.core.combat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.OffThreadException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.BlockStateAccess;
import org.phobos.mixin.game.entity.IPersistentProjectileEntity;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.module.player.SuicideModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.task.RetryUtils;
import org.phobos.core.placement.TimedPositionPoolBuilder;

public class CrystalTargetSelector extends CrystalSafetyCalculator implements Runnable {
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject;
   public boolean flag4;
   public double doubleValue;
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject2;
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject3;
   public double doubleValue2;
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject4;
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject5;
   public final MutableBlockPos mutableBlockPos = new MutableBlockPos();
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject6;
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject7;
   public static final EntityDimensions entityDimensions = EntityDimensions.changing(0.25F, 0.25F);
   public static final Logger LOGGER2 = LoggerFactory.getLogger(CrystalTargetSelector.class);
   public final MutableObject<CrystalPlacement> crystalPlacementMutableObject8 = new MutableObject();

   public void handleCrystalPlacement(
      CrystalPlacement crystalPlacement, MutableObject<CrystalPlacement> mutableObject, MutableObject<CrystalPlacement> mutableObject2
   ) {
      if (crystalPlacement.isFlag42() && !this.flag3) {
         if (mutableObject2.getValue() == null) {
            mutableObject2.setValue(crystalPlacement);
         } else {
            if (crystalPlacement.getFloatValue() >= 0.25F
               && crystalPlacement.getFloatValue() < ((CrystalPlacement)mutableObject2.getValue()).getFloatValue()) {
               mutableObject2.setValue(crystalPlacement);
            }
         }
      } else if (mutableObject.getValue() == null) {
         mutableObject.setValue(crystalPlacement);
      } else {
         if (crystalPlacement.getFloatValue() < ((CrystalPlacement)mutableObject.getValue()).getFloatValue()) {
            mutableObject.setValue(crystalPlacement);
         }
      }
   }

   public void handleCrystalPlacement2(
      CrystalPlacement crystalPlacement, MutableObject<CrystalPlacement> mutableObject, MutableObject<CrystalPlacement> mutableObject2
   ) {
      this.placesCrystalsModule.handleCrystalPlacement(crystalPlacement, mutableObject, mutableObject2, this.flag3);
   }

   public double value() {
      double d = 0.0;

      for (Entity entity : this.clientWorld.getEntities()) {
         if (entity instanceof LivingEntity livingEntity && this.testLivingEntity2(livingEntity)) {
            d = Math.max(livingEntity.getY() + 1.0, d);
         }
      }

      return d;
   }

   public boolean check3() {
      return this.placesCrystalsModule.getBooleanValueSetting24().getValue()
         && (
            this.value2() > this.placesCrystalsModule.getFloatValueSetting42().getValue()
               || this.placesCrystalsModule.getBooleanValueSetting23().getValue()
                  && this.value3() > this.placesCrystalsModule.getFloatValueSetting5().getValue()
         );
   }

   public boolean testCrystalPlacement(CrystalPlacement crystalPlacement, ClientWorld clientWorld, ClientWorld clientWorld2) {
      float f7 = this.computeEntity2(this.clientPlayerEntity, this.computeBlockPos2(crystalPlacement, clientWorld));
      crystalPlacement.setFloatValue(f7);
      if (!(f7 < EntityUtils.computeLivingEntity(this.clientPlayerEntity)) && !this.placesCrystalsModule.getBooleanValueSetting18().getValue()) {
         return false;
      }

      for (Entity entity3 : clientWorld2.getEntities()) {
         if (entity3 instanceof LivingEntity livingEntity) {
            if (this.testLivingEntity(livingEntity, crystalPlacement.getX() + 0.5, crystalPlacement.getY() + 1.0, crystalPlacement.getZ() + 0.5)) {
               if (!(this.placesCrystalsModule instanceof SuicideModule) && this.phobosClient.getFriendList().testUUID(livingEntity.getUuid())) {
                  float f3 = this.computeBlockPos3(crystalPlacement, livingEntity, clientWorld);
                  crystalPlacement.setFloatValue3(crystalPlacement.getFloatValue3() + f3);
               } else {
                  float f6 = this.computeEntity2(livingEntity, this.computeBlockPos3(crystalPlacement, livingEntity, clientWorld));
                  if (f6 > crystalPlacement.getFloatValue2()) {
                     crystalPlacement.setFloatValue2(f6);
                     crystalPlacement.setInteger(livingEntity.getId());
                     float f5 = this.phobosClient
                        .getFeatureRegistry()
                        .getDamageTracker()
                        .computeLivingEntity(livingEntity, EntityUtils.computeLivingEntity(livingEntity));
                     if (f6 >= f5) {
                        crystalPlacement.setFlag6(true);
                     }

                     if (livingEntity instanceof PlayerEntity playerEntity) {
                        if (!crystalPlacement.isFlag5() && this.testPlayerEntity(playerEntity, crystalPlacement)) {
                           crystalPlacement.setFlag5(true);
                        }

                        crystalPlacement.setFlag8(true);
                        if (this.placesCrystalsModule.getBooleanValueSetting34().getValue()) {
                           if (this.placesCrystalsModule.getBooleanValueSetting41().getValue()
                              && this.phobosClient.getFeatureRegistry().getPredictionManager().testPlayerEntity(playerEntity, clientWorld, crystalPlacement)) {
                              crystalPlacement.setFlag10(true);
                           }

                           if (f5 <= this.placesCrystalsModule.getDoubleValueSetting12().getValue()) {
                              crystalPlacement.setFlag2(true);
                           }

                           if (this.placesCrystalsModule.getDoubleValueSetting11().getValue() > 0.0) {
                              for (ItemStack itemStack : playerEntity.getInventory().armor) {
                                 if (!itemStack.isEmpty()) {
                                    float f4 = (float)(itemStack.getMaxDamage() - itemStack.getDamage()) / itemStack.getMaxDamage() * 100.0F;
                                    if (f4 >= 0.0 && f4 < this.placesCrystalsModule.getDoubleValueSetting11().getValue()) {
                                       crystalPlacement.setFlag2(true);
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                     } else {
                        crystalPlacement.setFlag8(false);
                     }
                  }
               }
            }
         } else if (entity3 instanceof ItemEntity itemEntity) {
            if (this.check()) {
               Entity entity = this.resolveEntity2(itemEntity);
               if (entity != null) {
                  float f = this.computeBlockPos(crystalPlacement, entity3, clientWorld);
                  if (f > 5.0F && crystalPlacement.getFloatValue2() < f) {
                     crystalPlacement.setInteger(entity.getId());
                     crystalPlacement.setFlag8(false);
                     crystalPlacement.setFloatValue2(f);
                     crystalPlacement.setFlag6(true);
                  }
               }
            }
         } else if (entity3 instanceof Ownable ownable
            && this.check2()
            && (entity3 instanceof EnderPearlEntity || entity3 instanceof ArrowEntity arrowEntity && !((IPersistentProjectileEntity)(Object)arrowEntity).collided())
            && this.testEntity(entity3)) {
            Entity entity2 = ownable.getOwner();
            if (entity2 != null && this.testEntity2(entity2) && !this.phobosClient.getFriendList().testUUID(entity2.getUuid())) {
               switch ((PlacesCrystalsModule.Deflector)this.placesCrystalsModule.getDeflectorSetting().getValue()) {
                  case Boost:
                     float f2 = this.computeBlockPos(crystalPlacement, entity3, clientWorld) / this.placesCrystalsModule.getFloatValueSetting7().getValue();
                     if (f2 >= this.placesCrystalsModule.getFloatValueSetting8().getValue() && crystalPlacement.getFloatValue2() < f2) {
                        crystalPlacement.setInteger(entity2.getId());
                        crystalPlacement.setFlag8(false);
                        crystalPlacement.setFloatValue2(f2);
                        crystalPlacement.setFlag6(true);
                     }
                     break;
                  case Block:
                     if (!(this.clientPlayerEntity.distanceTo(entity3) > 16.0)) {
                        Box box2 = crystalPlacement.getBox();
                        TrajectoryUtils.TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
                           this.clientPlayerEntity,
                           clientWorld,
                           entity3,
                           entity3.getPos(),
                           entity3.getVelocity(),
                           TrajectoryUtils.EntityGravityType.Normal,
                           TrajectoryUtils.Drag.Normal
                        );
                        if (trajectoryData != null) {
                           for (int i = 0; i < trajectoryData.intValue / 2; i++) {
                              Vec3d vec3d = trajectoryData.resolveVec3d(i);
                              if (vec3d == null) {
                                 break;
                              }

                              Box box = entityDimensions.getBoxAt(vec3d);
                              if (box2.intersects(box)) {
                                 crystalPlacement.setInteger(entity2.getId());
                                 crystalPlacement.setFlag8(false);
                                 crystalPlacement.setFloatValue2(10.0F);
                                 crystalPlacement.setFlag6(true);
                              }
                           }
                        }
                     }
               }
            }
         }
      }

      if (!(this.placesCrystalsModule instanceof SuicideModule)
         && this.placesCrystalsModule.getBooleanValueSetting25().getValue()
         && crystalPlacement.getFloatValue3() > this.placesCrystalsModule.getFloatValueSetting5().getValue()) {
         return false;
      }

      if (crystalPlacement.getFloatValue2() < 1.0F) {
         return false;
      }

      return switch ((PlacesCrystalsModule.CalculationType)this.placesCrystalsModule.getCalculationTypeSetting().getValue()) {
         case Damage -> this.placesCrystalsModule.getBooleanValueSetting20().getValue() && crystalPlacement.isFlag6()
            || !(crystalPlacement.getFloatValue() > this.placesCrystalsModule.getDoubleValueSetting4().getValue());
         case Balance -> crystalPlacement.computeFloat2(this.placesCrystalsModule.getFloatValueSetting3().getValue())
            > this.placesCrystalsModule.getFloatValueSetting2().getValue();
      };
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public CrystalTargetSelector(
      PlacesCrystalsModule placesCrystalsModule,
      PhobosClient phobosClient,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      DamageCalculator damageCalculator
   ) {
      super(placesCrystalsModule, phobosClient, clientPlayerEntity, clientWorld, damageCalculator);
      this.crystalPlacementMutableObject5 = new MutableObject();
      this.crystalPlacementMutableObject4 = new MutableObject();
      this.crystalPlacementMutableObject6 = new MutableObject();
      this.crystalPlacementMutableObject3 = new MutableObject();
      this.crystalPlacementMutableObject2 = new MutableObject();
      this.crystalPlacementMutableObject7 = new MutableObject();
      this.crystalPlacementMutableObject = new MutableObject();
      this.flag4 = false;
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity, CrystalPlacement crystalPlacement) {
      return playerEntity.getEyePos().distanceTo(crystalPlacement.toCenterPos()) < this.placesCrystalsModule.getDoubleValueSetting8().getValue();
   }

   @Override
   public void run() throws OffThreadException {
      this.run2();
      this.run4();
   }

   public float computeBlockPos2(BlockPos blockPos, World world) {
      return RetryUtils.resolveObject(
         3, () -> OffThreadException.INSTANCE, () -> this.damageCalculator.computeEntity(this.resolveEntity(this.clientPlayerEntity, true), world, blockPos)
      );
   }

   public void handleValue(CrystalPlacement... crystalPlacements) {
      if (this.placesCrystalsModule.getBooleanValueSetting38().getValue()) {
         MutableObject<Boolean> mutableObject = new MutableObject();
         this.phobosClient
            .getInventoryManager()
            .handleConsumer2(itemSwitcher -> mutableObject.setValue(itemSwitcher.computePredicate(itemStack -> itemStack.isOf(Items.END_CRYSTAL)) <= 0));
         if (!(Boolean)mutableObject.getValue()) {
            Map<BlockPos, Long> map = new HashMap<>();
            TimedPositionPoolBuilder timedPositionPoolBuilder = new TimedPositionPoolBuilder(map);
            BlockStateAccess.Delegating delegating = new BlockStateAccess.Delegating(this.clientWorld2);

            for (CrystalPlacement crystalPlacement : crystalPlacements) {
               if (!(crystalPlacement.getVec3i().getY() + this.doubleValue2 > this.doubleValue)
                  && crystalPlacement.isFlag3()
                  && crystalPlacement.isFlag32()) {
                  delegating.getBlockPosMap().clear();
                  delegating.getBlockPosMap().put(crystalPlacement, Blocks.OBSIDIAN.getDefaultState());
                  this.mutableBlockPos.set(crystalPlacement);
                  this.mutableBlockPos.handleInt3(1);
                  map.put(this.mutableBlockPos, 0L);
                  List<BlockPos> list = timedPositionPoolBuilder.resolveList2(
                     crystalPlacement,
                     Blocks.OBSIDIAN,
                     this.clientPlayerEntity,
                     delegating,
                     this.placesCrystalsModule.getIntegerSetting3().getValue(),
                     this.placesCrystalsModule
                  );
                  if (!list.isEmpty()) {
                     for (int i = 1; i < list.size(); i++) {
                        delegating.getBlockPosMap().put(list.get(i), Blocks.OBSIDIAN.getDefaultState());
                     }

                     if (this.placesCrystalsModule.getDirectionResolver().resolveDirection(crystalPlacement, this.clientPlayerEntity, delegating) != null
                        && this.testCrystalPlacement(crystalPlacement, delegating, delegating)
                        && (
                           crystalPlacement.getFloatValue2() > this.placesCrystalsModule.getDoubleValueSetting13().getValue()
                              || crystalPlacement.isFlag6()
                                 && crystalPlacement.getFloatValue2() > this.placesCrystalsModule.getDoubleValueSetting2().getValue()
                        )) {
                        crystalPlacement.setBlockPosList(list);
                        this.handleCrystalPlacement2(crystalPlacement, this.crystalPlacementMutableObject3, this.crystalPlacementMutableObject2);
                     }
                  }
               }
            }

            if (this.crystalPlacementMutableObject3.getValue() != null) {
               this.handleCrystalPlacement4((CrystalPlacement)this.crystalPlacementMutableObject3.getValue());
            } else if (this.crystalPlacementMutableObject2.getValue() != null) {
               this.testCrystalPlacement2((CrystalPlacement)this.crystalPlacementMutableObject2.getValue());
            }
         }
      }
   }

   public void handleCrystalPlacement3(
      CrystalPlacement crystalPlacement, MutableObject<CrystalPlacement> mutableObject, MutableObject<CrystalPlacement> mutableObject2
   ) {
      if (crystalPlacement.isFlag42() && !this.flag3) {
         if (crystalPlacement.testCrystalPlacement2((CrystalPlacement)mutableObject2.getValue())) {
            mutableObject2.setValue(crystalPlacement);
         }
      } else if (crystalPlacement.testCrystalPlacement2((CrystalPlacement)mutableObject.getValue())) {
         mutableObject.setValue(crystalPlacement);
      }
   }

   public void handleCrystalPlacement4(CrystalPlacement crystalPlacement) {
      if (this.placesCrystalsModule.testClientPlayerEntity6(this.clientPlayerEntity)) {
         this.placesCrystalsModule.getModuleRecord().handleClientPlayerEntity2(this.clientPlayerEntity, this.clientWorld, crystalPlacement);
         Integer integer = crystalPlacement.getInteger();
         if (integer != null) {
            try {
               Entity entity = this.clientWorld.getEntityById(integer);
               this.placesCrystalsModule.resolvePlacesCrystalsModule3(entity);
               if (entity != null) {
                  if (entity instanceof PlayerEntity playerEntity) {
                     this.placesCrystalsModule.getTextMutableObject().setValue(playerEntity.getNameForScoreboard());
                  } else {
                     this.placesCrystalsModule.getTextMutableObject().setValue(entity.getClass().getSimpleName());
                  }
               }
            } catch (Throwable var5) {
            }
         }

         this.placesCrystalsModule.getFloatValueMutableObject2().setValue(crystalPlacement.getFloatValue2());
         this.placesCrystalsModule.getFloatValueMutableObject().setValue(crystalPlacement.getFloatValue());
         this.flag4 = true;
      }
   }

   public void run3() {
      this.doubleValue = this.value();
      this.doubleValue2 = this.clientPlayerEntity.getEyeY();
   }

   public float value2() {
      float f = 0.0F;

      for (CrystalPlacement crystalPlacement : this.placesCrystalsModule.getCrystalPlacementBlockPosGrid().getObjectArray()) {
         if (crystalPlacement.isFlag3() && !crystalPlacement.isFlag32() && crystalPlacement.isFlag5()) {
            f = Math.max(crystalPlacement.getFloatValue(), f);
         }
      }

      return f;
   }

   public float computeBlockPos3(BlockPos blockPos, LivingEntity livingEntity, World world) {
      return RetryUtils.resolveObject(
         3, () -> OffThreadException.INSTANCE, () -> this.damageCalculator.computeEntity(this.resolveEntity(livingEntity, true), world, blockPos)
      );
   }

   public boolean testCrystalPlacement2(CrystalPlacement crystalPlacement) {
      if (!this.flag3 && !this.placesCrystalsModule.alwaysTrue2()) {
         return false;
      }

      if (!this.flag2 && !this.flag3) {
         this.setFlag2(true);
         this.run2();
      }

      if (this.flag3) {
         this.handleCrystalPlacement4(crystalPlacement);
         return true;
      }

      EndCrystalEntity endCrystalEntity2 = null;
      float f2 = Float.MAX_VALUE;

      for (EndCrystalEntity endCrystalEntity : crystalPlacement.getEndCrystalEntityArray()) {
         if (endCrystalEntity != null && endCrystalEntity.isAlive() && this.clientWorld.getWorldBorder().contains(endCrystalEntity.getBlockPos())) {
            double d = CrystalUtils.testPlayerEntity2(
                  this.clientPlayerEntity, this.clientWorld, endCrystalEntity, AntiCheatModule.getAntiCheatModule().getSplitter().getValue()
               )
               ? this.placesCrystalsModule.getDoubleValueSetting5().getValue()
               : this.placesCrystalsModule.value2();
            PlayerActionUtils.PackedRangeCalculator packedRangeCalculator = this.placesCrystalsModule.createPackedRangeCalculator();
            if (packedRangeCalculator.testFunction2(rangeCalculator -> rangeCalculator.calculate(this.clientPlayerEntity, endCrystalEntity.getBoundingBox(), d))
               && (
                  !this.placesCrystalsModule.getBooleanValueSetting17().getValue()
                     || !this.placesCrystalsModule.getBooleanValueSetting14().getValue()
                     || RaycastUtils.testPhobosClient(this.phobosClient, endCrystalEntity)
               )) {
               float f = this.computeEntity4(endCrystalEntity);
               if (f < f2) {
                  endCrystalEntity2 = endCrystalEntity;
                  f2 = f;
               }
            }
         }
      }

      if (endCrystalEntity2 != null
         && f2 < EntityUtils.computeLivingEntity(this.clientPlayerEntity)
         && f2 <= crystalPlacement.getFloatValue()
         && this.testEndCrystalEntity(endCrystalEntity2)) {
         this.handleCrystalPlacement4(crystalPlacement);
         return true;
      } else {
         return false;
      }
   }

   public float value3() {
      float f = 0.0F;

      for (CrystalPlacement crystalPlacement : this.placesCrystalsModule.getCrystalPlacementBlockPosGrid().getObjectArray()) {
         if (crystalPlacement.isFlag3() && !crystalPlacement.isFlag32() && crystalPlacement.isFlag5()) {
            f = Math.max(crystalPlacement.getFloatValue3(), f);
         }
      }

      return f;
   }

   public void run4() {
      if (this.placesCrystalsModule.alwaysTrue3() && (!this.flag || this.flag3)) {
         this.run3();

         for (CrystalPlacement crystalPlacement : this.placesCrystalsModule.getCrystalPlacementBlockPosGrid().getObjectArray()) {
            if (!(crystalPlacement.getVec3i().getY() + this.doubleValue2 > this.doubleValue)) {
               crystalPlacement.handleServerDetector3(
                  this.phobosClient.getServerDetector(),
                  this.clientWorld,
                  this.clientPlayerEntity,
                  this.placesCrystalsModule.getDoubleValueSetting8().getValue(),
                  this.placesCrystalsModule.value3(),
                  this.placesCrystalsModule.getDoubleValueSetting5().getValue(),
                  this.placesCrystalsModule.value2(),
                  this.placesCrystalsModule.getLongValueSetting3().getValue(),
                  this.placesCrystalsModule.getDirectionResolver(),
                  this.placesCrystalsModule.createPackedRangeCalculator(),
                  this.placesCrystalsModule.createPlacementResult(this.computeFlag(true)),
                  this.placesCrystalsModule.value()
               );
               if (crystalPlacement.isFlag3() && !crystalPlacement.isFlag32()) {
                  this.placesCrystalsModule.resolvePlacesCrystalsModule5(true);
                  if (this.testCrystalPlacement(crystalPlacement, this.clientWorld2, this.clientWorld)) {
                     if (!crystalPlacement.isFlag6() && crystalPlacement.getFloatValue2() < this.placesCrystalsModule.getDoubleValueSetting2().getValue()) {
                        this.handleCrystalPlacement2(crystalPlacement, this.crystalPlacementMutableObject4, this.crystalPlacementMutableObject6);
                     } else {
                        this.handleCrystalPlacement2(crystalPlacement, this.crystalPlacementMutableObject8, this.crystalPlacementMutableObject5);
                     }

                     this.handleCrystalPlacement3(crystalPlacement, this.crystalPlacementMutableObject7, this.crystalPlacementMutableObject);
                  }
               }
            }
         }

         if (this.crystalPlacementMutableObject8.getValue() != null) {
            this.placesCrystalsModule.resolvePlacesCrystalsModule4(true);
            this.handleCrystalPlacement4((CrystalPlacement)this.crystalPlacementMutableObject8.getValue());
         } else if (this.crystalPlacementMutableObject5.getValue() == null
            || !this.testCrystalPlacement2((CrystalPlacement)this.crystalPlacementMutableObject5.getValue())) {
            if (!this.flag4) {
               if (this.placesCrystalsModule.getThreaded4().testNumber(this.placesCrystalsModule.getLongValueSetting7().getValue())) {
                  this.handleValue(this.placesCrystalsModule.getCrystalPlacementBlockPosGrid().getObjectArray());
                  this.placesCrystalsModule.getThreaded4().run();
               }

               if (this.placesCrystalsModule.getBooleanValueSetting34().getValue()) {
                  this.placesCrystalsModule.resolvePlacesCrystalsModule4(false);
                  if (this.crystalPlacementMutableObject4.getValue() != null
                     && this.testCrystalPlacement3((CrystalPlacement)this.crystalPlacementMutableObject4.getValue())) {
                     this.handleCrystalPlacement4((CrystalPlacement)this.crystalPlacementMutableObject4.getValue());
                     return;
                  }

                  if (this.crystalPlacementMutableObject6.getValue() != null
                     && this.testCrystalPlacement3((CrystalPlacement)this.crystalPlacementMutableObject6.getValue())) {
                     this.testCrystalPlacement2((CrystalPlacement)this.crystalPlacementMutableObject6.getValue());
                     return;
                  }
               }

               if (this.placesCrystalsModule.getBooleanValueSetting24().getValue()) {
                  float f = this.placesCrystalsModule.getFloatValueSetting42().getValue();
                  float f2 = this.placesCrystalsModule.getFloatValueSetting6().getValue();
                  float f3 = this.value2();
                  float f4 = this.value3();
                  if (f3 > f || this.placesCrystalsModule.getBooleanValueSetting23().getValue() && f4 > f) {
                     if (this.crystalPlacementMutableObject7.getValue() != null
                        && ((CrystalPlacement)this.crystalPlacementMutableObject7.getValue()).getFloatValue() < f2) {
                        this.handleCrystalPlacement4((CrystalPlacement)this.crystalPlacementMutableObject7.getValue());
                        return;
                     }

                     if (this.crystalPlacementMutableObject.getValue() != null
                        && ((CrystalPlacement)this.crystalPlacementMutableObject.getValue()).getFloatValue() < f2) {
                        this.testCrystalPlacement2((CrystalPlacement)this.crystalPlacementMutableObject.getValue());
                        return;
                     }
                  }
               }
            }

            this.placesCrystalsModule.resolvePlacesCrystalsModule5(false);
         }
      }
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public boolean testCrystalPlacement3(CrystalPlacement crystalPlacement) {
      boolean flag = this.check3();
      boolean flag2 = crystalPlacement.isFlag10();
      boolean flag3 = this.placesCrystalsModule.getThreaded3().testNumber(this.placesCrystalsModule.getLongValueSetting6().getValue());
      if (!flag3 && !flag2 && !flag) {
         return false;
      } else if (flag) {
         return crystalPlacement.isFlag7() && crystalPlacement.getFloatValue() <= this.placesCrystalsModule.getFloatValueSetting6().getValue();
      } else {
         return crystalPlacement.isFlag2()
            ? true
            : crystalPlacement.getFloatValue2() > 1.0F
               && (
                  this.placesCrystalsModule.getBooleanValueSetting44().getValue()
                     || this.phobosClient.getBindManager().testKeyCombination(this.placesCrystalsModule.getKeyCombinationSetting().getValue())
                     || flag2
               );
      }
   }
}
