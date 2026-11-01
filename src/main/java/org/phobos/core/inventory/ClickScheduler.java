package org.phobos.core.inventory;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.Window;
import net.minecraft.screen.slot.Slot;
import org.earthhack.ducks.ISlot;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.math.FloatPair;
import org.phobos.core.math.MathUtils;
import org.phobos.core.gui.UiValues;

public class ClickScheduler {
   public static final ClickScheduler INSTANCE = new ClickScheduler();
   public final Map<Slot, ScheduledClick> slotMap = new HashMap<>();
   public final UiValues.Double doubleValue = new UiValues.Double();
   public boolean flag;
   public Set<ScheduledClick> scheduledClickSet;
   public static final Robot robot;
   public final UiValues.Double doubleValue2 = new UiValues.Double();
   public FloatPair floatPair = FloatPair.getINSTANCE();

   static {
      try {
         robot = new Robot();
      } catch (AWTException aWTException) {
         throw new IllegalStateException("The client does not run in headless mode");
      }
   }

   public double[] values() {
      Point point = MouseInfo.getPointerInfo().getLocation();
      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
      int i = dimension.width;
      int i2 = dimension.height;
      return new double[]{point.getX() / i, point.getY() / i2};
   }

   @Nullable
   public ScheduledClick resolveScheduledClick(Slot slot) {
      return this.slotMap.remove(slot);
   }

   public double[] values2() {
      double[] doubles = this.values();
      return this.values3(doubles[0], doubles[1]);
   }

   public void handleSlot(Slot slot, ScheduledClick scheduledClick) {
      this.slotMap.put(slot, scheduledClick);
   }

   public double computeSlot(Slot slot) {
      ISlot iSlot = ISlot.cast(slot);
      double[] doubles = new double[]{iSlot.getLastRenderX(), iSlot.getLastRenderY()};
      double[] doubles2 = this.values2();
      return MathUtils.computeDouble12(doubles2[0], doubles2[1], doubles[0], doubles[1]);
   }

   public void handleSlot2(Slot slot, int i, long l) {
      ScheduledClick scheduledClick = this.slotMap.get(slot);
      if (scheduledClick != null) {
         scheduledClick.setFlag(true);
         scheduledClick.setLongValue(l);
         scheduledClick.setIntValue2(i);
      }
   }

   public void handleISlot(ISlot iSlot, FloatPair... floatPairs) {
      this.handleDouble(ItemSwitcher.computeFloat(iSlot.getLastRenderX()), ItemSwitcher.computeFloat(iSlot.getLastRenderY()), floatPairs);
   }

   public void run() {
      MinecraftClient minecraftClient = MinecraftClient.getInstance();
      if (this.flag) {
         if (minecraftClient.isWindowFocused() && minecraftClient.currentScreen instanceof HandledScreen) {
            Window window = minecraftClient.getWindow();
            this.doubleValue.handleDouble2(this.floatPair.value());
            this.doubleValue2.handleDouble2(this.floatPair.value());
            float f = 825535.4F;
            this.doubleValue.handleDouble3(window.getScaledWidth() / f);
            this.doubleValue2.handleDouble3(window.getScaledHeight() / f);
            double[] doubles = this.values3(this.doubleValue.getDoubleValue(), this.doubleValue2.getDoubleValue());
            robot.mouseMove((int)doubles[0], (int)doubles[1]);
            if (this.doubleValue.testDouble(0.005) && this.doubleValue2.testDouble(0.005)) {
               this.flag = false;
            }
         } else {
            this.flag = false;
         }
      }
   }

   public static ClickScheduler getINSTANCE() {
      return INSTANCE;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public double[] values3(double d, double d2) {
      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
      int i = dimension.width;
      int i2 = dimension.height;
      return new double[]{d * i, d2 * i2};
   }

   public void handleScheduledClick(ScheduledClick scheduledClick) {
      this.scheduledClickSet.add(scheduledClick);
   }

   public void handleDouble(double d, double d2, FloatPair... floatPairs) {
      MinecraftClient minecraftClient = MinecraftClient.getInstance();
      if (!(minecraftClient.currentScreen instanceof HandledScreen)) {
         this.flag = false;
      } else {
         Window window = minecraftClient.getWindow();
         d /= window.getScaledWidth();
         d2 /= window.getScaledHeight();
         if (!this.flag) {
            double[] doubles = this.values();
            if (MathUtils.testDouble(d, doubles[0], 0.001) && MathUtils.testDouble(d2, doubles[0], 0.001)) {
               this.flag = false;
            } else {
               this.doubleValue.setDoubleValue(doubles[0]);
               this.doubleValue2.setDoubleValue(doubles[1]);
               this.doubleValue.setDoubleValue2(d);
               this.doubleValue2.setDoubleValue2(d2);
               this.flag = true;
               this.floatPair = floatPairs.length > 0 ? floatPairs[0] : FloatPair.getINSTANCE();
            }
         }
      }
   }

   public ClickScheduler() {
      this.scheduledClickSet = new HashSet<>();
   }

   public void run2() {
      if (!this.scheduledClickSet.isEmpty()) {
         MinecraftClient minecraftClient = MinecraftClient.getInstance();
         if (minecraftClient.player == null) {
            this.scheduledClickSet.clear();
         } else if (minecraftClient.currentScreen instanceof ActionTimer actionTimer) {
            this.scheduledClickSet.removeIf(ScheduledClick::isFlag2);

            for (ScheduledClick scheduledClick : this.scheduledClickSet) {
               if (scheduledClick.isFlag() && actionTimer.testInt(scheduledClick.getIntValue2())) {
                  actionTimer.run();
                  if (actionTimer.testLong(scheduledClick.getLongValue())) {
                     minecraftClient.player.closeScreen();
                     scheduledClick.setFlag2(true);
                  }
               }
            }
         } else {
            this.scheduledClickSet.clear();
         }
      }
   }
}
