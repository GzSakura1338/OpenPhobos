package org.phobos.core.anticheat.check;

import java.util.List;
import java.util.Objects;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.anticheat.AntiCheatPlayer;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.anticheat.SimulationCheck;
import org.phobos.core.math.VectorMath;

public class MovementACheck extends SimulationCheck {
   public static final List<Class<? extends Block>> classValueList = List.of(BedBlock.class, SlimeBlock.class, HoneyBlock.class, PowderSnowBlock.class);
   public static final List<Double> doubleValueList = List.of(
      0.5095999912261959,
      -0.08702399309539816,
      -0.08528351489334113,
      -0.08357784622212827,
      -0.0819062908918066,
      -0.08026816663620898,
      -0.07866280483447859,
      -0.07708955023816295,
      -0.07554776070376615,
      -0.07403680693065001,
      -0.07255607220417704,
      -0.07110495214399076,
      -0.0696828544573303
   );
   public boolean flag = false;
   public static final List<RegistryEntry<StatusEffect>> registryEntryList = List.of(StatusEffects.SLOW_FALLING);
   public static final List<Double> doubleValueList2 = List.of(
      0.6076000164985658,
      -0.0889839917316434,
      -0.08720431359424546,
      -0.08546022898565087,
      -0.08375102603596235,
      -0.08207600711266715,
      -0.0804344885358894,
      -0.07882580029933772,
      -0.07724928579683382,
      -0.07570430155431032,
      -0.0741902169671691,
      -0.0727064140428918,
      -0.07125228714879878,
      -0.06982724276485225,
      -0.06843069924140427
   );
   public int intValue = 0;
   public static final List<Double> doubleValueList3 = List.of(
      0.41159999516010254,
      -0.08506399504327788,
      -0.08336271676487925,
      -0.0816954640195993,
      -0.08006155629742463,
      -0.07846032669852913,
      -0.07689112166107052,
      -0.07535330069443089,
      -0.07384623611779237,
      -0.07236931280394177,
      -0.07092192792819801
   );

   public MovementACheck(AntiCheatPlayer antiCheatPlayer) {
      super("MovementA", antiCheatPlayer);
   }

   @Override
   public int value() {
      return 10;
   }

   @Override
   public void run3() {
      if (this.intValue > 0) {
         this.intValue--;
         this.flag = false;
      } else if (!this.check()) {
         this.flag = false;
      } else {
         if (this.antiCheatPlayer.flag10 && !this.flag) {
            this.flag = true;
         }

         if (this.flag) {
            try {
               int i = this.antiCheatPlayer.intValue4 - 1;
               if (i >= 0) {
                  List<Double> list = Objects.requireNonNull(this.resolveList(this.antiCheatPlayer));
                  double d = list.get(i);
                  double d2 = this.antiCheatPlayer.vec3d2.y;
                  if (Math.abs(d2 - d) > 0.01) {
                     this.handleText("Tick %s Correct: %.2f Current: %.2f".formatted(i, d, d2));
                  }
               }
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
               this.flag = false;
            }
         }
      }
   }

   public boolean check() {
      if (VectorMath.testVec3d2(this.antiCheatPlayer.vec3d2)) {
         return false;
      } else if (this.antiCheatPlayer.abstractClientPlayerEntity.isFallFlying()) {
         this.intValue = (int)Math.ceil(this.antiCheatPlayer.intValue / 50.0) + 3;
         return false;
      } else {
         return !registryEntryList.stream().anyMatch(registryEntry -> this.antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(registryEntry))
               && !this.antiCheatPlayer.abstractClientPlayerEntity.isInsideWall()
               && !this.antiCheatPlayer.abstractClientPlayerEntity.isTouchingWater()
               && !this.antiCheatPlayer.abstractClientPlayerEntity.hasVehicle()
               && !this.antiCheatPlayer.abstractClientPlayerEntity.isUsingRiptide()
               && !this.antiCheatPlayer.abstractClientPlayerEntity.isSwimming()
               && !this.antiCheatPlayer.abstractClientPlayerEntity.isSleeping()
               && !this.antiCheatPlayer.abstractClientPlayerEntity.isClimbing()
               && this.antiCheatPlayer.abstractClientPlayerEntity.hurtTime <= 0
               && BlockUtils.testClientWorld2(mc.world, this.antiCheatPlayer.abstractClientPlayerEntity.getBlockStateAtPos().getBlock())
               && !classValueList.stream()
                  .anyMatch(classValue -> classValue.isInstance(this.antiCheatPlayer.abstractClientPlayerEntity.getBlockStateAtPos().getBlock()))
            ? !this.antiCheatPlayer.abstractClientPlayerEntity.getAbilities().flying
            : false;
      }
   }

   @Nullable
   public List<Double> resolveList(AntiCheatPlayer antiCheatPlayer) {
      List<Double> list;
      if (!antiCheatPlayer.abstractClientPlayerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
         list = doubleValueList3;
      } else {
         switch (Objects.requireNonNull(antiCheatPlayer.abstractClientPlayerEntity.getStatusEffect(StatusEffects.JUMP_BOOST)).getAmplifier()) {
            case 0:
               list = doubleValueList;
               break;
            case 1:
               list = doubleValueList2;
               break;
            default:
               list = null;
         }
      }

      return list;
   }

   @Override
   public boolean isFlag4() {
      return !this.antiCheatPlayer.getAntiCheatModule().getMovement().getValue() || !this.antiCheatPlayer.getAntiCheatModule().getSimulation().getValue();
   }
}
