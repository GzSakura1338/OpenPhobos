package org.phobos.module.misc;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.util.NullSafe;
import org.phobos.core.math.MathUtils;
import org.phobos.event.AutoClickerHandleInputEventsListener;
import org.phobos.module.Module;

public class AutoClickerModule extends Module {
   public final TimeGate.Normal normal;
   public final MutableObject<AutoClickerModule.ClickPattern> clickPatternMutableObject;
   public final Setting<Integer> size;
   public long longValue;
   public final Setting<Map<AutoClickerModule.Disabler, Boolean>> disablers;
   public final Setting<Long> delay;
   public final Setting<Map<AutoClickerModule.Key, Boolean>> keys;
   public final Setting<Integer> offset;
   public final Setting<Long> spread;
   public final Setting<AutoClickerModule.Pattern> pattern = this.resolveSetting2(new EnumSetting<>("Pattern", AutoClickerModule.Pattern.Random));
   public final Setting<Integer> random;

   public void run() {
      NullSafe.handleMinecraftClient(mc, (clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> {
         for (AutoClickerModule.Disabler disabler : this.disablers.getValue().keySet()) {
            if (this.disablers.getValue().get(disabler)) {
               switch (disabler) {
                  case Mining:
                     if (clientPlayerInteractionManager.isBreakingBlock()) {
                        return;
                     }
                     break;
                  case Use:
                     if (clientPlayerEntity.isUsingItem()) {
                        return;
                     }
               }
            }
         }

         for (AutoClickerModule.Key key : this.keys.getValue().keySet()) {
            if (this.keys.getValue().get(key)) {
               key.run();
            }
         }
      });
   }

   public AutoClickerModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoClicker", Categories.misc, "Clicks for you");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 500L, 0L, 10000L, true).resolveSetting7("Working delay"));
      this.spread = this.resolveSetting2(
         new LongNumberSetting("Spread", 100L, 1L, 500L, true)
            .resolveSetting7("Random: delay + random(-spread, spread), Burst: Delay between steps")
            .resolveSetting3(this.resolveVisibility2(this.pattern, AutoClickerModule.Pattern.Random, AutoClickerModule.Pattern.Burst))
      );
      this.size = this.resolveSetting2(
         new NumberSetting<>("Size", 4, 2, 10).resolveSetting7("Burst pattern size").resolveSetting4(this.pattern, AutoClickerModule.Pattern.Burst)
      );
      this.random = this.resolveSetting2(
         new NumberSetting<>("Random", 1, 1, 5)
            .resolveSetting7("Burst pattern size formula: size + random(-random, random)")
            .resolveSetting4(this.pattern, AutoClickerModule.Pattern.Burst)
      );
      this.offset = this.resolveSetting2(
         new NumberSetting<>("Offset", 10, 1, 40)
            .resolveSetting7("For random value, formula: random(spread - offset, spread + offset)")
            .resolveSetting4(this.pattern, AutoClickerModule.Pattern.Burst)
      );
      this.keys = this.resolveSetting2(new MultiSelectSetting<>("Keys", AutoClickerModule.Key.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (AutoClickerModule.Key key : AutoClickerModule.Key.values()) {
            multiSelectSetting.resolveMultiSelectSetting(key, key.name());
         }
      }));
      this.disablers = this.resolveSetting2(new MultiSelectSetting<>("Disablers", AutoClickerModule.Disabler.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (AutoClickerModule.Disabler disabler : AutoClickerModule.Disabler.values()) {
            multiSelectSetting.resolveMultiSelectSetting(disabler, disabler.name());
         }
      }));
      this.normal = new TimeGate.Normal();
      this.clickPatternMutableObject = new MutableObject();
      this.longValue = -1L;
      this.handleListener(new AutoClickerHandleInputEventsListener(this));
   }

   public static class ClickPattern {
      public final Queue<AutoClickerModule.ClickPattern.ClickStep> clickStepQueue = new ConcurrentLinkedQueue<>();
      public final long longValue;

      public ClickPattern(int i, long l) {
         this.longValue = l;

         for (int i2 = 0; i2 < i; i2++) {
            this.clickStepQueue.add(new AutoClickerModule.ClickPattern.ClickStep());
         }
      }

      public void run() {
         AutoClickerModule.ClickPattern.ClickStep clickStep = this.clickStepQueue.peek();
         if (clickStep != null) {
            clickStep.normal.run();
         }
      }

      public void handleInt(int i, Runnable runnable) {
         if (!this.check()) {
            AutoClickerModule.ClickPattern.ClickStep clickStep = this.clickStepQueue.peek();
            if (clickStep != null) {
               long l = Math.max(MathUtils.computeLong5(this.longValue - i, this.longValue + i), 0L);
               if (clickStep.normal.testLong(l)) {
                  runnable.run();
                  this.clickStepQueue.poll();
                  this.run();
               }
            }
         }
      }

      public boolean check() {
         return this.clickStepQueue.isEmpty();
      }

      public static class ClickStep {
         public final TimeGate.Normal normal = new TimeGate.Normal();
      }
   }

   public enum Disabler implements TextProvider {
      Mining("Disables while mining"),
      Use("Disables when using an item");

      public final String text;

      Disabler(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   public enum Key {
      Left {
         @Override
         public void run() {
            MinecraftHolder.mc.doAttack();
         }
      },
      Right {
         @Override
         public void run() {
            MinecraftHolder.mc.doItemUse();
         }
      };

      public abstract void run();
   }

   public enum Pattern implements TextProvider {
      Constant("Constant delay"),
      Random("Random delay"),
      Burst("Burst-like delay");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Pattern(final String description) {
         this.text = description;
      }
   }
}
