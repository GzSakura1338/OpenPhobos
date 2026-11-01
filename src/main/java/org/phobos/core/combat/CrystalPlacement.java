package org.phobos.core.combat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import org.earthhack.ducks.IEntity;
import org.jetbrains.annotations.Nullable;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.combat.PlacesCrystalsModule;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.placement.PlacementResult;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.prediction.PredictionManager;
import org.phobos.core.server.ServerDetector;

public class CrystalPlacement extends MutableBlockPos {
   public final Vec3i vec3i;
   public float floatValue;
   public boolean flag;
   public Integer integer;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;
   public boolean flag5;
   public boolean flag6;
   public List<BlockPos> blockPosList;
   public float floatValue2;
   public float floatValue3;
   public boolean flag7;
   public boolean flag8;
   public final EndCrystalEntity[] endCrystalEntityArray = new EndCrystalEntity[4];
   public static final long longValue = 100L;
   public int intValue;
   public boolean flag9;
   public boolean flag10;

   public void handleCrystalPlacement(CrystalPlacement crystalPlacement) {
      this.set(crystalPlacement);
      System.arraycopy(crystalPlacement.getEndCrystalEntityArray(), 0, this.endCrystalEntityArray, 0, this.endCrystalEntityArray.length);
      this.blockPosList = crystalPlacement.getBlockPosList();
      this.floatValue2 = crystalPlacement.getFloatValue();
      this.floatValue3 = crystalPlacement.getFloatValue3();
      this.floatValue = crystalPlacement.getFloatValue2();
      this.flag3 = crystalPlacement.isFlag32();
      this.flag4 = crystalPlacement.isFlag42();
      this.flag9 = crystalPlacement.isFlag4();
      this.flag = crystalPlacement.isFlag3();
      this.flag6 = crystalPlacement.isFlag6();
      this.flag2 = crystalPlacement.isFlag2();
   }

   @Nullable
   public static CrystalPlacement resolveCrystalPlacement(@Nullable CrystalPlacement crystalPlacement) {
      return crystalPlacement == null ? null : crystalPlacement.getCrystalPlacement();
   }

   public Vec3i getVec3i() {
      return this.vec3i;
   }

   public float computeFloat(float f) {
      return this.floatValue2 <= f ? this.floatValue - this.floatValue2 + 20.0F : this.floatValue - this.floatValue2;
   }

   public void handleClientWorld(ClientWorld clientWorld, MutableBlockPos mutableBlockPos, boolean flag, long l, PlacementResult placementResult) {
      int i2 = mutableBlockPos.getX();
      int i3 = mutableBlockPos.getY();
      int i4 = mutableBlockPos.getZ();
      int i5 = 0;
      Box box4 = BoxUtils.createBox10(new Box(i2, i3, i4, i2 + 1.0, i3 + (flag ? 1.0 : 2.0), i4 + 1.0));

      for (Entity entity : clientWorld.getEntities()) {
         if (entity != null) {
            Box box;
            if (!placementResult.isFlag()) {
               box = entity.getBoundingBox();
            } else {
               label82: {
                  if (entity instanceof PlayerEntity playerEntity2) {
                     int i = placementResult.getIntValue();
                     PredictionManager predictionManager = ClientHolder.getPhobosClient().getFeatureRegistry().getPredictionManager();
                     PlayerEntity playerEntity = i == -1
                        ? predictionManager.resolvePlayerEntity(playerEntity2)
                        : predictionManager.resolvePlayerEntity3(i, playerEntity2);
                     if (playerEntity != null) {
                        box = playerEntity.getBoundingBox();
                        break label82;
                     }
                  }

                  box = entity.getBoundingBox();
               }
            }

            if (BoxUtils.createBox4(box).intersects(box4)) {
               if (!(entity instanceof EndCrystalEntity endCrystalEntity)) {
                  if (!(entity instanceof ItemEntity itemEntity)
                     || (
                           !itemEntity.getStack().isOf(Items.OBSIDIAN)
                              || !AntiCheatModule.getAntiCheatModule().getIgnoreDrops().getValue()
                              || itemEntity.age >= 10
                        )
                        && (((IEntity)(Object)entity).value() >= l || AntiCheatModule.getAntiCheatModule().getAccurate().getValue())) {
                     this.flag9 = false;
                     return;
                  }
               } else if (((IEntity)(Object)entity).value() >= l && i5 < this.endCrystalEntityArray.length) {
                  this.endCrystalEntityArray[i5] = endCrystalEntity;
                  this.flag4 = true;
                  i5++;
               }
            }
         }
      }

      this.flag9 = true;
   }

   public void handleServerDetector(
      ServerDetector serverDetector,
      ClientWorld clientWorld,
      ClientPlayerEntity clientPlayerEntity,
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      long l,
      DirectionResolver directionResolver,
      PlayerActionUtils.PackedRangeCalculator packedRangeCalculator,
      PlacementResult placementResult,
      int i
   ) {
      this.run();
      this.handleDouble(d5, d6, d7);
      if (clientWorld.getWorldBorder().contains(this)) {
         double d8 = CrystalUtils.testClientPlayerEntity(clientPlayerEntity, clientWorld, this, i) ? d : d2;
         if (packedRangeCalculator.testFunction(
            rangeCalculator -> rangeCalculator.calculate(clientPlayerEntity, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, d8)
         )) {
            Box box = new Box(this.getX() + 0.5, this.getY() + 1.5, this.getZ() + 0.5, this.getX() + 0.5, this.getY() + 1.5, this.getZ() + 0.5);
            box.expand(1.0);
            double d9 = CrystalUtils.testPlayerEntity(clientPlayerEntity, clientWorld, box, AntiCheatModule.getAntiCheatModule().getSplitter().getValue())
               ? d3
               : d4;
            if (packedRangeCalculator.testFunction2(rangeCalculator -> rangeCalculator.calculate(clientPlayerEntity, box, d9))) {
               BlockState blockState = clientWorld.getBlockState(this);
               if (blockState.isReplaceable()) {
                  this.flag3 = true;
               } else if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK)) {
                  return;
               }

               if (this.flag3 || directionResolver.resolveDirection(this, clientPlayerEntity, clientWorld) != null) {
                  this.handleInt3(1);
                  if (!clientWorld.isAir(this)) {
                     this.handleInt3(-1);
                  } else {
                     if (AntiCheatModule.getAntiCheatModule().getBooleanValueSetting8().getValue()) {
                        this.handleInt3(1);
                        if (!clientWorld.isAir(this)) {
                           return;
                        }

                        this.handleInt3(-1);
                     }

                     this.handleClientWorld(clientWorld, this, serverDetector.isFlag2(), l, placementResult);
                     this.handleInt3(-1);
                     this.flag = this.flag9;
                  }
               }
            }
         }
      }
   }

   public float getFloatValue2() {
      return this.floatValue;
   }

   public void run() {
      this.blockPosList = null;
      this.floatValue = 0.0F;
      this.floatValue3 = 0.0F;
      this.flag6 = false;
      this.flag2 = false;
      this.flag10 = false;
      this.flag4 = false;
      this.floatValue2 = Float.MAX_VALUE;
      this.flag9 = false;
      this.flag3 = false;
      this.flag = false;
      this.integer = null;
      this.flag8 = false;
      this.flag5 = false;
      this.flag7 = false;
      Arrays.fill(this.endCrystalEntityArray, null);
   }

   public float computeFloat2(float f) {
      if (f <= 0.0) {
         f = 0.1F;
      }

      return Float.compare(this.floatValue2, 0.0F) <= 0 ? this.floatValue * f / (0.1F * (1.0F / f)) : this.floatValue * f / (this.floatValue2 * (1.0F / f));
   }

   public boolean testDouble(double d) {
      return this.floatValue2 < d;
   }

   public boolean isFlag42() {
      return this.flag4;
   }

   @Nullable
   public EndCrystalEntity getEndCrystalEntity() {
      for (EndCrystalEntity endCrystalEntity : this.endCrystalEntityArray) {
         if (endCrystalEntity != null) {
            return endCrystalEntity;
         }
      }

      return null;
   }

   public boolean isFlag4() {
      return this.flag9;
   }

   public EndCrystalEntity[] getEndCrystalEntityArray() {
      return this.endCrystalEntityArray;
   }

   public void handleFloat(float f) {
      if (f > this.floatValue) {
         this.setFloatValue2(f);
      }
   }

   public boolean testCrystalPlacement(@Nullable CrystalPlacement crystalPlacement) {
      if (crystalPlacement != null
         && (this.isFlag32() || !crystalPlacement.isFlag32())
         && (this.isFlag42() || !crystalPlacement.isFlag42() || this.isFlag32() != crystalPlacement.isFlag32())) {
         return (!this.isFlag32() || crystalPlacement.isFlag32()) && (!this.isFlag42() || crystalPlacement.isFlag42())
            ? this.getFloatValue2() > crystalPlacement.getFloatValue2()
            : false;
      } else {
         return true;
      }
   }

   public Box getBox() {
      return new Box(this.getX() + 0.5, this.getY() + 1.5, this.getZ() + 0.5, this.getX() + 0.5, this.getY() + 1.5, this.getZ() + 0.5).expand(1.0);
   }

   public List<BlockPos> getBlockPosList() {
      return this.blockPosList;
   }

   public boolean testConsumer(Consumer<EndCrystalEntity> consumer) {
      if (!this.isFlag42()) {
         return false;
      } else {
         EndCrystalEntity endCrystalEntity = this.getEndCrystalEntity();
         if (endCrystalEntity == null) {
            return false;
         } else if (Objects.equals(this, endCrystalEntity.getBlockPos().down())) {
            consumer.accept(endCrystalEntity);
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean testCrystalPlacement2(@Nullable CrystalPlacement crystalPlacement) {
      if (crystalPlacement != null
         && (this.isFlag32() || !crystalPlacement.isFlag32())
         && (this.isFlag42() || !crystalPlacement.isFlag42() || this.isFlag32() != crystalPlacement.isFlag32())) {
         return (!this.isFlag32() || crystalPlacement.isFlag32()) && (!this.isFlag42() || crystalPlacement.isFlag42())
            ? this.getFloatValue() < crystalPlacement.getFloatValue()
            : false;
      } else {
         return true;
      }
   }

   public void handleServerDetector2(
      ServerDetector serverDetector, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, PlacesCrystalsModule placesCrystalsModule
   ) {
      this.handleServerDetector(
         serverDetector,
         clientWorld,
         clientPlayerEntity,
         placesCrystalsModule.getDoubleValueSetting8().getValue(),
         placesCrystalsModule.value3(),
         placesCrystalsModule.getDoubleValueSetting5().getValue(),
         placesCrystalsModule.value2(),
         0.0,
         0.0,
         0.0,
         placesCrystalsModule.getLongValueSetting3().getValue(),
         placesCrystalsModule.getDirectionResolver(),
         placesCrystalsModule.createPackedRangeCalculator(),
         placesCrystalsModule.createPlacementResult(placesCrystalsModule.getIntegerSetting15().getValue()),
         placesCrystalsModule.value()
      );
   }

   public void handleDouble(double d, double d2, double d3) {
      this.set(Math.floor(d) + this.vec3i.getX(), Math.floor(d2) + this.vec3i.getY(), Math.floor(d3) + this.vec3i.getZ());
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public boolean isFlag32() {
      return this.flag3;
   }

   public CrystalPlacement getCrystalPlacement() {
      CrystalPlacement crystalPlacement = new CrystalPlacement(this.vec3i);
      crystalPlacement.handleCrystalPlacement(this);
      return crystalPlacement;
   }

   public void handleBlockPos(BlockPos blockPos) {
      this.handleDouble(blockPos.getX(), blockPos.getY(), blockPos.getZ());
   }

   public boolean testCrystalPlacement3(@Nullable CrystalPlacement crystalPlacement, float f) {
      if (crystalPlacement != null
         && (this.isFlag32() || !crystalPlacement.isFlag32())
         && (this.isFlag42() || !crystalPlacement.isFlag42() || this.isFlag32() != crystalPlacement.isFlag32())) {
         return (!this.isFlag32() || crystalPlacement.isFlag32()) && (!this.isFlag42() || crystalPlacement.isFlag42())
            ? this.computeFloat2(f) > crystalPlacement.computeFloat2(f)
            : false;
      } else {
         return true;
      }
   }

   public void handleServerDetector3(
      ServerDetector serverDetector,
      ClientWorld clientWorld,
      ClientPlayerEntity clientPlayerEntity,
      double d,
      double d2,
      double d3,
      double d4,
      long l,
      DirectionResolver directionResolver,
      PlayerActionUtils.PackedRangeCalculator packedRangeCalculator,
      PlacementResult placementResult,
      int i
   ) {
      this.handleServerDetector(
         serverDetector,
         clientWorld,
         clientPlayerEntity,
         d,
         d2,
         d3,
         d4,
         clientPlayerEntity.getX(),
         clientPlayerEntity.getEyeY(),
         clientPlayerEntity.getZ(),
         l,
         directionResolver,
         packedRangeCalculator,
         placementResult,
         i
      );
   }

   public void handleClientWorld2(ClientWorld clientWorld, boolean flag, long l, PlacementResult placementResult) {
      this.handleInt3(1);
      this.handleClientWorld(clientWorld, this, flag, l, placementResult);
      this.handleInt3(-1);
   }

   public void setFloatValue2(float f) {
      this.floatValue = f;
   }

   public void setFlag5(boolean flag) {
      this.flag5 = flag;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public boolean isFlag10() {
      return this.flag10;
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public void setFlag3(boolean flag) {
      this.flag = flag;
   }

   public void setFlag8(boolean flag) {
      this.flag8 = flag;
   }

   public void setFlag7(boolean flag) {
      this.flag7 = flag;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void setInteger(Integer integer) {
      this.integer = integer;
   }

   public void setFlag6(boolean flag) {
      this.flag6 = flag;
   }

   public void setBlockPosList(List<BlockPos> list) {
      this.blockPosList = list;
   }

   public boolean isFlag7() {
      return this.flag7;
   }

   public CrystalPlacement(Vec3i vec3i) {
      this.floatValue2 = Float.MAX_VALUE;
      this.floatValue3 = 0.0F;
      this.floatValue = 0.0F;
      this.vec3i = vec3i;
   }

   public boolean isFlag8() {
      return this.flag8;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFlag10(boolean flag) {
      this.flag10 = flag;
   }

   public float getFloatValue() {
      return this.floatValue2;
   }

   public void setFlag42(boolean flag) {
      this.flag4 = flag;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public boolean isFlag3() {
      return this.flag;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public void setFlag4(boolean flag) {
      this.flag9 = flag;
   }

   public void setFloatValue(float f) {
      this.floatValue2 = f;
   }

   public void setFlag32(boolean flag) {
      this.flag3 = flag;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public Integer getInteger() {
      return this.integer;
   }
}
