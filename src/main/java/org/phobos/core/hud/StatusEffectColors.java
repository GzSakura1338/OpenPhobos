package org.phobos.core.hud;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public final class StatusEffectColors {
   public static final Map<RegistryEntry<StatusEffect>, Color> registryEntryMap = new HashMap<>();

   public static void run() {
      registryEntryMap.put(StatusEffects.SPEED, new Color(124, 175, 198));
      registryEntryMap.put(StatusEffects.SLOWNESS, new Color(90, 108, 129));
      registryEntryMap.put(StatusEffects.HASTE, new Color(217, 192, 67));
      registryEntryMap.put(StatusEffects.MINING_FATIGUE, new Color(74, 66, 23));
      registryEntryMap.put(StatusEffects.STRENGTH, new Color(147, 36, 35));
      registryEntryMap.put(StatusEffects.INSTANT_HEALTH, new Color(67, 10, 9));
      registryEntryMap.put(StatusEffects.INSTANT_DAMAGE, new Color(67, 10, 9));
      registryEntryMap.put(StatusEffects.JUMP_BOOST, new Color(34, 255, 76));
      registryEntryMap.put(StatusEffects.NAUSEA, new Color(85, 29, 74));
      registryEntryMap.put(StatusEffects.REGENERATION, new Color(205, 92, 171));
      registryEntryMap.put(StatusEffects.RESISTANCE, new Color(153, 69, 58));
      registryEntryMap.put(StatusEffects.FIRE_RESISTANCE, new Color(228, 154, 58));
      registryEntryMap.put(StatusEffects.WATER_BREATHING, new Color(46, 82, 153));
      registryEntryMap.put(StatusEffects.INVISIBILITY, new Color(127, 131, 146));
      registryEntryMap.put(StatusEffects.BLINDNESS, new Color(31, 31, 35));
      registryEntryMap.put(StatusEffects.NIGHT_VISION, new Color(31, 31, 161));
      registryEntryMap.put(StatusEffects.HUNGER, new Color(88, 118, 83));
      registryEntryMap.put(StatusEffects.WEAKNESS, new Color(72, 77, 72));
      registryEntryMap.put(StatusEffects.POISON, new Color(78, 147, 49));
      registryEntryMap.put(StatusEffects.WITHER, new Color(53, 42, 39));
      registryEntryMap.put(StatusEffects.HEALTH_BOOST, new Color(248, 125, 35));
      registryEntryMap.put(StatusEffects.ABSORPTION, new Color(37, 82, 165));
      registryEntryMap.put(StatusEffects.SATURATION, new Color(248, 36, 35));
      registryEntryMap.put(StatusEffects.GLOWING, new Color(148, 160, 97));
      registryEntryMap.put(StatusEffects.LEVITATION, new Color(206, 255, 255));
      registryEntryMap.put(StatusEffects.LUCK, new Color(51, 153, 0));
      registryEntryMap.put(StatusEffects.UNLUCK, new Color(192, 164, 77));
   }

   static {
      run();
   }

   @Nullable
   public static Color resolveColor(RegistryEntry<StatusEffect> registryEntry) {
      return registryEntryMap.get(registryEntry);
   }

   public StatusEffectColors() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
