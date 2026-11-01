package org.phobos.api;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.phobos.core.hud.HudElement;
import org.phobos.type.TopBottomEnum;

public interface HudAnchors extends Bounds {
   boolean alwaysTrue();

   void handleHudElement(HudElement var1, TopBottomEnum var2);

   Map<HudElement, Set<TopBottomEnum>> getHudElementMap();

   static HudAnchors createImpl() {
      return new HudAnchors.Impl();
   }

   void handleHudElement2(HudElement var1, TopBottomEnum var2);

   class Impl implements HudAnchors {
      public final Map<HudElement, Set<TopBottomEnum>> hudElementMap = new LinkedHashMap<>();
      public float floatValue;
      public float floatValue2;
      public float floatValue3;
      public float floatValue4;

      @Override
      public float getFloatValue3() {
         return this.floatValue3;
      }

      @Override
      public float value2() {
         return 4.0F;
      }

      @Override
      public float getFloatValue4() {
         return this.floatValue4;
      }

      @Override
      public float getFloatValue() {
         return this.floatValue;
      }

      @Override
      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      @Override
      public boolean alwaysTrue() {
         return true;
      }

      @Override
      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      @Override
      public float value3() {
         return 2.0F;
      }

      @Override
      public Map<HudElement, Set<TopBottomEnum>> getHudElementMap() {
         return this.hudElementMap;
      }

      @Override
      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      @Override
      public void handleHudElement(HudElement hudElement, TopBottomEnum topBottomEnum) {
         this.hudElementMap.computeIfAbsent(hudElement, hudElementx -> new HashSet<>()).add(topBottomEnum);
      }

      @Override
      public void handleHudElement2(HudElement hudElement, TopBottomEnum topBottomEnum) {
         Set<TopBottomEnum> set = this.hudElementMap.get(hudElement);
         if (set != null) {
            set.remove(topBottomEnum);
            if (set.isEmpty()) {
               this.hudElementMap.remove(hudElement);
            }
         }
      }

      @Override
      public void setFloatValue4(float f) {
         this.floatValue4 = f;
      }

      @Override
      public float getFloatValue2() {
         return this.floatValue2;
      }
   }
}
