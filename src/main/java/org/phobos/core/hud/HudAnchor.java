package org.phobos.core.hud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopBottomEnum;
import org.phobos.type.TopCenterEnum;

public class HudAnchor {
   public final Supplier<Float> floatValueSupplier;
   public final Supplier<Float> floatValueSupplier2;
   public final CenterRightEnum centerRightEnum;
   public final Map<HudElement, Set<TopBottomEnum>> hudElementMap = new HashMap<>();
   public final TopCenterEnum topCenterEnum;

   public Supplier<Float> getFloatValueSupplier2() {
      return this.floatValueSupplier2;
   }

   public void handleHudElement(HudElement hudElement) {
      hudElement.setFloatValue2(hudElement.computeDouble2(this.floatValueSupplier.get().floatValue()));
      hudElement.setFloatValue(hudElement.computeDouble(this.floatValueSupplier2.get().floatValue()));
      hudElement.run8();
      hudElement.setTopCenterEnum(this.topCenterEnum);
      hudElement.setCenterRightEnum(this.centerRightEnum);

      for (Map.Entry<HudElement, Set<TopBottomEnum>> entry : this.hudElementMap.entrySet()) {
         HudElement hudElement2 = entry.getKey();
         if (!hudElement2.isFlag4()) {
            for (TopBottomEnum topBottomEnum : entry.getValue()) {
               hudElement2.handleHudElement(hudElement, topBottomEnum);
            }
         }
      }
   }

   public HudAnchor resolveHudAnchor(HudElement hudElement, TopBottomEnum topBottomEnum) {
      this.hudElementMap.computeIfAbsent(hudElement, hudElementx -> new HashSet<>()).add(topBottomEnum);
      return this;
   }

   public CenterRightEnum getCenterRightEnum() {
      return this.centerRightEnum;
   }

   public TopCenterEnum getTopCenterEnum() {
      return this.topCenterEnum;
   }

   public Supplier<Float> getFloatValueSupplier() {
      return this.floatValueSupplier;
   }

   public static HudAnchor createHudAnchor(
      Supplier<Float> supplier, Supplier<Float> supplier2, TopCenterEnum topCenterEnum, CenterRightEnum centerRightEnum
   ) {
      return new HudAnchor(supplier, supplier2, topCenterEnum, centerRightEnum);
   }

   public Map<HudElement, Set<TopBottomEnum>> getHudElementMap() {
      return this.hudElementMap;
   }

   public HudAnchor(Supplier<Float> supplier, Supplier<Float> supplier2, TopCenterEnum topCenterEnum, CenterRightEnum centerRightEnum) {
      this.floatValueSupplier = supplier;
      this.floatValueSupplier2 = supplier2;
      this.topCenterEnum = topCenterEnum;
      this.centerRightEnum = centerRightEnum;
   }
}
