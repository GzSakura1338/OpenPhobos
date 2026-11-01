package org.phobos.core.anticheat;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.phobos.api.MinecraftHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.anticheat.check.AimACheck;
import org.phobos.core.anticheat.check.AimBCheck;
import org.phobos.core.anticheat.check.AimCCheck;
import org.phobos.core.anticheat.check.AimDCheck;
import org.phobos.core.anticheat.check.AimECheck;
import org.phobos.core.anticheat.check.AimFCheck;
import org.phobos.core.anticheat.check.AuraACheck;
import org.phobos.core.anticheat.check.AuraBCheck;
import org.phobos.core.anticheat.check.AutoClickerACheck;
import org.phobos.core.anticheat.check.AverageACheck;
import org.phobos.core.anticheat.check.BlinkACheck;
import org.phobos.core.anticheat.check.FlyACheck;
import org.phobos.core.anticheat.check.FlyBCheck;
import org.phobos.core.anticheat.check.FlyCCheck;
import org.phobos.core.anticheat.check.GroundSpoofACheck;
import org.phobos.core.anticheat.check.GroundSpoofBCheck;
import org.phobos.core.anticheat.check.HighJumpACheck;
import org.phobos.core.anticheat.check.ImprobableCheck;
import org.phobos.core.anticheat.check.InvalidPitchCheck;
import org.phobos.core.anticheat.check.InvalidYawCheck;
import org.phobos.core.anticheat.check.MovementACheck;
import org.phobos.core.anticheat.check.MovementBCheck;
import org.phobos.core.anticheat.check.MovementDCheck;
import org.phobos.core.anticheat.check.MultitaskACheck;
import org.phobos.core.anticheat.check.NoSlowACheck;
import org.phobos.core.anticheat.check.PlaceACheck;
import org.phobos.core.anticheat.check.PlaceBCheck;
import org.phobos.core.anticheat.check.ReachACheck;
import org.phobos.core.anticheat.check.ScaffoldACheck;
import org.phobos.core.anticheat.check.ScaffoldBCheck;
import org.phobos.core.anticheat.check.SpeedACheck;
import org.phobos.core.anticheat.check.SpeedBCheck;
import org.phobos.core.anticheat.check.SpeedCCheck;
import org.phobos.core.anticheat.check.SpeedDCheck;
import org.phobos.core.anticheat.check.StepACheck;
import org.phobos.core.anticheat.check.StrafeACheck;
import org.phobos.core.anticheat.check.StrafeBCheck;
import org.phobos.core.anticheat.check.StrafeCCheck;
import org.phobos.core.anticheat.check.TickACheck;
import org.phobos.core.anticheat.check.TimerACheck;
import org.phobos.core.anticheat.check.VelocityACheck;

public class AntiCheatCheckRegistry implements MinecraftHolder {
   public static final Logger LOGGER = LoggerFactory.getLogger(AntiCheatCheckRegistry.class);
   public final AntiCheatPlayer antiCheatPlayer;
   public final Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> classValueMap;
   public int intValue;
   public final Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> classValueMap2;
   public int intValue2;
   public final Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> classValueMap3;
   public final Map<Class<? extends RunnableCheck>, RunnableCheck> classValueMap4;

   public void handleConsumer(Consumer<AntiCheatCheck> consumer) {
      if (this.antiCheatPlayer != null) {
         for (AntiCheatCheck antiCheatCheck : this.classValueMap3.values()) {
            consumer.accept(antiCheatCheck);
         }

         for (AntiCheatCheck antiCheatCheck2 : this.classValueMap.values()) {
            consumer.accept(antiCheatCheck2);
         }

         for (AntiCheatCheck antiCheatCheck3 : this.classValueMap2.values()) {
            consumer.accept(antiCheatCheck3);
         }
      }
   }

   public static AntiCheatCheckRegistry createAntiCheatCheckRegistry(AntiCheatPlayer antiCheatPlayer) {
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map = new HashMap<>();
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map2 = new HashMap<>();
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map3 = new HashMap<>();
      Map<Class<? extends RunnableCheck>, RunnableCheck> map4 = new HashMap<>();
      map.put(GroundSpoofACheck.class, new GroundSpoofACheck(antiCheatPlayer));
      map.put(GroundSpoofBCheck.class, new GroundSpoofBCheck(antiCheatPlayer));
      map2.put(TickACheck.class, new TickACheck(antiCheatPlayer));
      map2.put(FlyACheck.class, new FlyACheck(antiCheatPlayer));
      map2.put(FlyBCheck.class, new FlyBCheck(antiCheatPlayer));
      map2.put(FlyCCheck.class, new FlyCCheck(antiCheatPlayer));
      map2.put(PlaceACheck.class, new PlaceACheck(antiCheatPlayer));
      map2.put(PlaceBCheck.class, new PlaceBCheck(antiCheatPlayer));
      map2.put(ScaffoldACheck.class, new ScaffoldACheck(antiCheatPlayer));
      map2.put(ScaffoldBCheck.class, new ScaffoldBCheck(antiCheatPlayer));
      map2.put(SpeedACheck.class, new SpeedACheck(antiCheatPlayer));
      map2.put(SpeedBCheck.class, new SpeedBCheck(antiCheatPlayer));
      map2.put(SpeedCCheck.class, new SpeedCCheck(antiCheatPlayer));
      map2.put(SpeedDCheck.class, new SpeedDCheck(antiCheatPlayer));
      map2.put(StrafeACheck.class, new StrafeACheck(antiCheatPlayer));
      map2.put(StrafeBCheck.class, new StrafeBCheck(antiCheatPlayer));
      map2.put(StrafeCCheck.class, new StrafeCCheck(antiCheatPlayer));
      map2.put(AverageACheck.class, new AverageACheck(antiCheatPlayer));
      map2.put(AimACheck.class, new AimACheck(antiCheatPlayer));
      map2.put(AimBCheck.class, new AimBCheck(antiCheatPlayer));
      map2.put(AimCCheck.class, new AimCCheck(antiCheatPlayer));
      map2.put(AimDCheck.class, new AimDCheck(antiCheatPlayer));
      map2.put(AimECheck.class, new AimECheck(antiCheatPlayer));
      map4.put(AimFCheck.class, new AimFCheck(antiCheatPlayer));
      map2.put(InvalidPitchCheck.class, new InvalidPitchCheck(antiCheatPlayer));
      map2.put(InvalidYawCheck.class, new InvalidYawCheck(antiCheatPlayer));
      map2.put(VelocityACheck.class, new VelocityACheck(antiCheatPlayer));
      map2.put(TimerACheck.class, new TimerACheck(antiCheatPlayer));
      map2.put(NoSlowACheck.class, new NoSlowACheck(antiCheatPlayer));
      map2.put(HighJumpACheck.class, new HighJumpACheck(antiCheatPlayer));
      map2.put(AutoClickerACheck.class, new AutoClickerACheck(antiCheatPlayer));
      map2.put(ReachACheck.class, new ReachACheck(antiCheatPlayer));
      map2.put(AuraACheck.class, new AuraACheck(antiCheatPlayer));
      map2.put(AuraBCheck.class, new AuraBCheck(antiCheatPlayer));
      map2.put(MovementACheck.class, new MovementACheck(antiCheatPlayer));
      map2.put(MovementBCheck.class, new MovementBCheck(antiCheatPlayer));
      map2.put(MovementDCheck.class, new MovementDCheck(antiCheatPlayer));
      map2.put(StepACheck.class, new StepACheck(antiCheatPlayer));
      map2.put(BlinkACheck.class, new BlinkACheck(antiCheatPlayer));
      map2.put(MultitaskACheck.class, new MultitaskACheck(antiCheatPlayer));
      map3.put(ImprobableCheck.class, new ImprobableCheck(antiCheatPlayer));
      return new AntiCheatCheckRegistry(map, map2, map3, map4, antiCheatPlayer);
   }

   public boolean testAntiCheatPlayer(AntiCheatPlayer antiCheatPlayer) {
      if (antiCheatPlayer != null && mc.world != null) {
         if (!antiCheatPlayer.itemStack4.equals(ItemStack.EMPTY) && !antiCheatPlayer.itemStack.equals(ItemStack.EMPTY)
            || !antiCheatPlayer.itemStack3.equals(ItemStack.EMPTY) && !antiCheatPlayer.itemStack2.equals(ItemStack.EMPTY)) {
            return !(antiCheatPlayer.itemStack.getItem() instanceof BlockItem) && !(antiCheatPlayer.itemStack2.getItem() instanceof BlockItem)
               ? false
               : antiCheatPlayer.itemStack4.getCount() == antiCheatPlayer.itemStack.getCount() - 1;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public AntiCheatCheckRegistry(
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map,
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map2,
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map3,
      Map<Class<? extends RunnableCheck>, RunnableCheck> map4,
      AntiCheatPlayer antiCheatPlayer
   ) {
      this.classValueMap3 = map;
      this.classValueMap = map2;
      this.classValueMap2 = map3;
      this.classValueMap4 = map4;
      this.antiCheatPlayer = antiCheatPlayer;
   }

   public void run5() {
      this.antiCheatPlayer.flag8 = true;
      this.handleConsumer(AntiCheatCheck::run5);
   }

   public static AntiCheatCheckRegistry createAntiCheatCheckRegistry2(AntiCheatScheduler antiCheatScheduler) {
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map = new HashMap<>();
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map2 = new HashMap<>();
      Map<Class<? extends AntiCheatCheck>, AntiCheatCheck> map3 = new HashMap<>();
      Map<Class<? extends RunnableCheck>, RunnableCheck> map4 = new HashMap<>();
      map.put(GroundSpoofACheck.class, new GroundSpoofACheck(antiCheatScheduler));
      map.put(GroundSpoofBCheck.class, new GroundSpoofBCheck(antiCheatScheduler));
      map2.put(TickACheck.class, new TickACheck(antiCheatScheduler));
      map2.put(FlyACheck.class, new FlyACheck(antiCheatScheduler));
      map2.put(FlyBCheck.class, new FlyBCheck(antiCheatScheduler));
      map2.put(FlyCCheck.class, new FlyCCheck(antiCheatScheduler));
      map2.put(PlaceACheck.class, new PlaceACheck(antiCheatScheduler));
      map2.put(PlaceBCheck.class, new PlaceBCheck(antiCheatScheduler));
      map2.put(ScaffoldACheck.class, new ScaffoldACheck(antiCheatScheduler));
      map2.put(ScaffoldBCheck.class, new ScaffoldBCheck(antiCheatScheduler));
      map2.put(SpeedACheck.class, new SpeedACheck(antiCheatScheduler));
      map2.put(SpeedBCheck.class, new SpeedBCheck(antiCheatScheduler));
      map2.put(SpeedCCheck.class, new SpeedCCheck(antiCheatScheduler));
      map2.put(SpeedDCheck.class, new SpeedDCheck(antiCheatScheduler));
      map2.put(StrafeACheck.class, new StrafeACheck(antiCheatScheduler));
      map2.put(StrafeBCheck.class, new StrafeBCheck(antiCheatScheduler));
      map2.put(StrafeCCheck.class, new StrafeCCheck(antiCheatScheduler));
      map2.put(AverageACheck.class, new AverageACheck(antiCheatScheduler));
      map2.put(AimACheck.class, new AimACheck(antiCheatScheduler));
      map2.put(AimBCheck.class, new AimBCheck(antiCheatScheduler));
      map2.put(AimCCheck.class, new AimCCheck(antiCheatScheduler));
      map2.put(AimDCheck.class, new AimDCheck(antiCheatScheduler));
      map2.put(AimECheck.class, new AimECheck(antiCheatScheduler));
      map4.put(AimFCheck.class, new AimFCheck(antiCheatScheduler));
      map2.put(InvalidPitchCheck.class, new InvalidPitchCheck(antiCheatScheduler));
      map2.put(InvalidYawCheck.class, new InvalidYawCheck(antiCheatScheduler));
      map2.put(VelocityACheck.class, new VelocityACheck(antiCheatScheduler));
      map2.put(TimerACheck.class, new TimerACheck(antiCheatScheduler));
      map2.put(NoSlowACheck.class, new NoSlowACheck(antiCheatScheduler));
      map2.put(HighJumpACheck.class, new HighJumpACheck(antiCheatScheduler));
      map2.put(AutoClickerACheck.class, new AutoClickerACheck(antiCheatScheduler));
      map2.put(ReachACheck.class, new ReachACheck(antiCheatScheduler));
      map2.put(AuraACheck.class, new AuraACheck(antiCheatScheduler));
      map2.put(AuraBCheck.class, new AuraBCheck(antiCheatScheduler));
      map2.put(MovementACheck.class, new MovementACheck(antiCheatScheduler));
      map2.put(MovementBCheck.class, new MovementBCheck(antiCheatScheduler));
      map2.put(MovementDCheck.class, new MovementDCheck(antiCheatScheduler));
      map2.put(StepACheck.class, new StepACheck(antiCheatScheduler));
      map2.put(BlinkACheck.class, new BlinkACheck(antiCheatScheduler));
      map3.put(ImprobableCheck.class, new ImprobableCheck(antiCheatScheduler));
      return new AntiCheatCheckRegistry(map, map2, map3, map4, antiCheatScheduler);
   }

   public void run6() {
      if (this.intValue2 > 0) {
         this.intValue2--;
      }

      if (++this.intValue > 20) {
         for (RunnableCheck runnableCheck : this.classValueMap4.values()) {
            runnableCheck.run();
         }

         this.intValue = 0;
      }

      if (!this.antiCheatPlayer.abstractClientPlayerEntity.isCreative() && !this.antiCheatPlayer.abstractClientPlayerEntity.isSpectator()) {
         if (!this.antiCheatPlayer.abstractClientPlayerEntity.getAbilities().flying) {
            if (this.antiCheatPlayer.flag6 && !this.antiCheatPlayer.flag10) {
               this.run5();
            }

            if (this.testAntiCheatPlayer(this.antiCheatPlayer)) {
               this.handleConsumer(AntiCheatCheck::run4);
            }

            this.handleConsumer(AntiCheatCheck::run6);
         }
      }
   }
}
