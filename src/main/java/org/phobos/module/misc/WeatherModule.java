package org.phobos.module.misc;

import java.awt.Color;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.Biome.Precipitation;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class WeatherModule extends Module {
   public final Setting<Color> color;
   public static WeatherModule weatherModule;
   public static boolean flag = false;
   public static final BiomeParticleConfig biomeParticleConfig = new BiomeParticleConfig(ParticleTypes.CRIMSON_SPORE, 0.025F) {
      public boolean shouldAddParticle(Random random) {
         return random.nextFloat() <= WeatherModule.weatherModule.amount.getValue() / 10.0F;
      }
   };
   public final Setting<Float> amount;
   public final Setting<WeatherModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", WeatherModule.Mode.Snow));
   public final Setting<Float> speed;
   public final Setting<Boolean> unlock;

   @Nullable
   public static Precipitation resolvePrecipitation(Precipitation precipitation) {
      if (!weatherModule.check7()) {
         return precipitation;
      }

      return switch ((WeatherModule.Mode)weatherModule.mode.getValue()) {
         case Snow -> Precipitation.NONE;
         case Rain -> Precipitation.RAIN;
         case Ash -> Precipitation.SNOW;
         case Clear -> null;
      };
   }

   public static WeatherModule getWeatherModule() {
      return weatherModule;
   }

   @Nullable
   public static Color getColor() {
      return weatherModule.check7() && flag ? weatherModule.color.getValue() : null;
   }

   public static float getFloatValue22() {
      return weatherModule.check7() ? weatherModule.speed.getValue() / 100.0F * 10.0F : 1.0F;
   }

   public static float computeFloat(float f) {
      return f * (weatherModule.check7() ? weatherModule.speed.getValue() / 100.0F : 1.0F);
   }

   public static boolean check() {
      return check3() && weatherModule.unlock.getValue();
   }

   public static boolean check2() {
      return check3() && weatherModule.mode.getValue().equals(WeatherModule.Mode.Clear);
   }

   public WeatherModule(PhobosClient phobosClient) {
      super(phobosClient, "Weather", Categories.misc, "Controls weather");
      this.color = this.resolveSetting2(new ColorSetting("Color", Color.WHITE).getColorSetting().resolveSetting4(this.mode, WeatherModule.Mode.Ash));
      this.unlock = this.resolveSetting2(new BooleanSetting("Unlock").resolveSetting7("Makes rain and snow go through blocks"));
      this.amount = this.resolveSetting2(new NumberSetting<>("Amount", 1.0F, 0.0F, 1.0F).resolveNumberSetting("Max", 1.0F));
      this.speed = this.resolveSetting2(
         new NumberSetting<>("Speed", 100.0F, 10.0F, 500.0F, "%").resolveNumberSetting("Max", 500.0F).resolveNumberSetting("Default", 100.0F)
      );
      weatherModule = this;
   }

   public static float computeFloat2(float f) {
      return weatherModule != null && weatherModule.check7() ? weatherModule.amount.getValue() : f;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public static boolean isFlag() {
      return flag;
   }

   @Nullable
   public static BiomeParticleConfig getBiomeParticleConfig() {
      if (!weatherModule.check7()) {
         return null;
      }

      return switch ((WeatherModule.Mode)weatherModule.mode.getValue()) {
         case Clear -> biomeParticleConfig;
         default -> null;
      };
   }

   public static boolean testParticleType(ParticleType<?> particleType) {
      return weatherModule.check7() && weatherModule.mode.testObject2(WeatherModule.Mode.Ash) && particleType == ParticleTypes.CRIMSON_SPORE;
   }

   public static void setFlag(boolean flag) {
      WeatherModule.flag = flag;
   }

   public static boolean check3() {
      return weatherModule.check7();
   }

   private enum Mode {
      Clear,
      Rain,
      Snow,
      Ash;
   }
}
