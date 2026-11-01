package org.phobos.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.phobos.api.TextProvider;
import org.phobos.api.ShaderRegistry;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.PostEffectShader;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.texture.AnimatedTexture;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.ApplyFogEvent;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.RenderEvent;
import org.phobos.event.RenderSkyEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AmbienceModule extends Module {
   public static AmbienceModule ambienceModule;
   public final Setting<AmbienceModule.Location> skyLocation;
   public final Setting<Color> hitColor;
   public final Setting<Float> intensity;
   public final Setting<Boolean> bloom;
   public final Setting<Color> lightningColor;
   public final Setting<Color> backgroundColor;
   public long longValue;
   public final Setting<Boolean> time;
   public final PostEffectShader postEffectShader;
   public final Setting<Color> lightColor;
   public final Setting<AmbienceModule.SkyMode> skyMode;
   public final Setting<Boolean> lightning;
   public final Setting<Boolean> hit;
   public final Setting<Integer> timeValue;
   public final Setting<Boolean> sky;
   public final Setting<Boolean> light = this.resolveSetting2(new BooleanSetting("Light"));
   public final Setting<AmbienceModule.ExternalSkybox> skyType;
   public AnimatedTexture animatedTexture;
   public final Setting<Boolean> background;
   public final Setting<Color> skyColor;
   public final Setting<Float> blur;
   public final Setting<String> skyName;

   public static Color getColor() {
      return ambienceModule.hitColor.getValue();
   }

   public Setting<AmbienceModule.Location> getSkyLocation() {
      return this.skyLocation;
   }

   public Setting<Color> getLightColor() {
      return this.lightColor;
   }

   public Setting<Boolean> getBackground() {
      return this.background;
   }

   public static boolean check() {
      return ambienceModule != null && ambienceModule.check7() && ambienceModule.hit.getValue();
   }

   public Setting<Color> getSkyColor() {
      return this.skyColor;
   }

   public static boolean check2() {
      return ambienceModule.animatedTexture != null && ambienceModule.animatedTexture.check();
   }

   public static void run() {
      ambienceModule.animatedTexture.run();
      NativeImageBackedTexture nativeImageBackedTexture = ambienceModule.animatedTexture.getNativeImageBackedTexture();
      RenderSystem.setShaderTexture(0, nativeImageBackedTexture.getGlId());
   }

   public Setting<Boolean> getSky() {
      return this.sky;
   }

   public Setting<Color> getBackgroundColor() {
      return this.backgroundColor;
   }

   @Override
   public void run3() {
      this.handleExternalSkybox(this.skyType.getValue());
   }

   public AmbienceModule(PhobosClient phobosClient) {
      super(phobosClient, "Ambience", Categories.render, "Manages world look");
      this.lightColor = this.resolveSetting2(new ColorSetting("LightColor", Color.WHITE));
      this.sky = this.resolveSetting2(new BooleanSetting("Sky", true));
      this.skyMode = this.resolveSetting2(new EnumSetting<>("SkyMode", AmbienceModule.SkyMode.Color));
      this.skyColor = this.resolveSetting2(new ColorSetting("SkyColor", Color.BLACK));
      this.skyLocation = this.resolveSetting2(
         new EnumSetting<>("SkyLocation", AmbienceModule.Location.Resources).resolveSetting4(this.skyMode, AmbienceModule.SkyMode.Texture)
      );
      this.skyType = this.resolveSetting2(
         new EnumSetting<>("SkyType", AmbienceModule.ExternalSkybox.Image)
            .resolveSetting4(this.skyMode, AmbienceModule.SkyMode.Texture)
            .resolveSetting4(this.skyLocation, AmbienceModule.Location.Global)
      );
      this.skyName = this.resolveSetting2(
         new StringSetting("SkyName", "skybox.png")
            .resolveSetting4(this.skyMode, AmbienceModule.SkyMode.Texture)
            .resolveSetting4(this.skyLocation, AmbienceModule.Location.Global)
      );
      this.background = this.resolveSetting2(new BooleanSetting("Background"));
      this.backgroundColor = this.resolveSetting2(new ColorSetting("BackgroundColor", Color.WHITE));
      this.time = this.resolveSetting2(new BooleanSetting("Time", false));
      this.timeValue = this.resolveSetting2(new NumberSetting<>("TimeValue", 21, 0, 23));
      this.bloom = this.resolveSetting2(new BooleanSetting("Bloom"));
      this.blur = this.resolveSetting2(new NumberSetting<>("Blur", 512.0F, 1.0F, 1000.0F));
      this.intensity = this.resolveSetting2(new NumberSetting<>("Intensity", 0.35F, 0.0F, 1.0F));
      this.lightning = this.resolveSetting2(new BooleanSetting("Lightning"));
      this.lightningColor = this.resolveSetting2(new ColorSetting("LightningColor", Color.WHITE).getColorSetting());
      this.hit = this.resolveSetting2(new BooleanSetting("Hit"));
      this.hitColor = this.resolveSetting2(new ColorSetting("HitColor", Color.WHITE).getColorSetting());
      this.postEffectShader = ShaderRegistry.getINSTANCE().resolvePostEffectShader(Identifier.of("phoboslite", "shaders/post/bloom.json"));
      this.longValue = 0L;
      this.resolveSettingGroup(this.light, this.lightColor);
      this.resolveSettingGroup(this.sky, this.skyMode, this.skyColor, this.skyLocation, this.skyType, this.skyName);
      this.resolveSettingGroup(this.background, this.backgroundColor);
      this.resolveSettingGroup(this.time, this.timeValue);
      this.resolveSettingGroup(this.bloom, this.blur, this.intensity);
      this.resolveSettingGroup(this.lightning, this.lightningColor);
      this.resolveSettingGroup(this.hit, this.hitColor);
      this.time.handleConsumer2(booleanValue -> {
         if (mc.world != null) {
            if (booleanValue) {
               this.longValue = mc.world.getTime();
            }

            if (!booleanValue) {
               mc.world.setTimeOfDay(this.longValue);
            }
         }
      });
      this.skyLocation.handleConsumer2(location -> PhobosClient.handleRunnable2(() -> this.handleLocation(location)));
      this.skyName.handleConsumer2(text -> this.handleExternalSkybox(this.skyType.getValue()));
      this.skyType.handleConsumer2(externalSkybox -> PhobosClient.handleRunnable2(() -> this.handleExternalSkybox(externalSkybox)));
      this.sky.handleConsumer2(booleanValue -> {
         if (!booleanValue) {
            this.run8();
         } else {
            this.handleExternalSkybox(this.skyType.getValue());
         }
      });
      this.handleListener(new Listener<ApplyFogEvent>() {
         public void handleObject2(ApplyFogEvent applyFogEvent) {
            if (AmbienceModule.this.light.getValue()) {
               applyFogEvent.setCancelled(true);
               if (applyFogEvent.getFogType().equals(FogType.FOG_TERRAIN)) {
                  RenderSystem.setShaderFogStart(0.0F);
                  RenderSystem.setShaderFogEnd(0.0F);
               }

               Color color = AmbienceModule.this.lightColor.getValue();
               RenderSystem.setShaderFogColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
               RenderSystem.setShaderFogShape(FogShape.CYLINDER);
            }
         }
      });
      this.handleListener(new Listener<RenderSkyEvent>() {
         public void handleObject2(RenderSkyEvent renderSkyEvent) {
            if (AmbienceModule.this.sky.getValue() && !AmbienceModule.this.skyMode.testObject2(AmbienceModule.SkyMode.Color)) {
               Color color = AmbienceModule.this.skyColor.getValue();
               renderSkyEvent.setSkyMode(AmbienceModule.this.skyMode.getValue());
               renderSkyEvent.setVector3f(new Vector3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F));
               renderSkyEvent.setFlag(AmbienceModule.this.skyLocation.testObject2(AmbienceModule.Location.Global));
               renderSkyEvent.setCancelled(true);
            }
         }
      });
      this.handleListener(new Listener<Receive<WorldTimeUpdateS2CPacket>>() {
         public void handleObject2(Receive<WorldTimeUpdateS2CPacket> receive) {
            AmbienceModule.this.longValue = receive.getPacket().getTime();
            if (AmbienceModule.this.time.getValue()) {
               receive.setCancelled(true);
            }
         }
      });
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (AmbienceModule.this.time.getValue()) {
                  clientWorld.setTimeOfDay(AmbienceModule.this.timeValue.getValue() * 1000);
               }
            }
         }
      );
      this.handleListener(
         new Listener<RenderEvent>() {
            public void handleObject2(RenderEvent renderEvent) {
               if (AmbienceModule.this.bloom.getValue()) {
                  AmbienceModule.this.postEffectShader
                     .handleText5("resolution", MinecraftHolder.mc.getWindow().getScaledWidth(), MinecraftHolder.mc.getWindow().getScaledHeight());
                  AmbienceModule.this.postEffectShader.handleText2("blur", 1.0F / AmbienceModule.this.blur.getValue());
                  AmbienceModule.this.postEffectShader.handleText2("intensity", AmbienceModule.this.intensity.getValue());
                  AmbienceModule.this.postEffectShader.handleFloat(renderEvent.getFloatValue());
               }
            }
         }
      );
      this.run7();
      ambienceModule = this;
   }

   public Setting<Boolean> getLight() {
      return this.light;
   }

   public void run7() {
      String var1 = System.getProperty("user.home");
      String[] var2 = new String[2];
      var2[0] = "earthhack";
      var2[1] = "textures";
      Path var3 = Path.of(var1, var2);
      File var4 = var3.toFile();
      boolean var5 = var4.exists();
      if (!var5) {
         File var6 = var3.toFile();
         var6.mkdirs();
      }
   }

   public Setting<AmbienceModule.ExternalSkybox> getSkyType() {
      return this.skyType;
   }

   public void handleLocation(AmbienceModule.Location location) {
      if (location.equals(AmbienceModule.Location.Global)) {
         String text = this.skyName.getValue();
         if (!SkyboxTextureUtils.testText(text)) {
            SkyboxTextureUtils.describeText(text);
         }
      }
   }

   public void handleExternalSkybox(AmbienceModule.ExternalSkybox externalSkybox) {
      switch (externalSkybox) {
         case Complex:
            this.handleLocation(this.skyLocation.getValue());
            break;
         case Image:
            if (!this.skyLocation.testObject2(AmbienceModule.Location.Global)) {
               return;
            }

            String text = this.skyName.getValue();
            if (this.animatedTexture != null && this.animatedTexture.testText(text)) {
               return;
            }

            this.animatedTexture = new AnimatedTexture(text);
            String text2 = this.animatedTexture.describe();
            if (text2 != null) {
               this.phobosClient.getChatManager().handleText2(text2);
               this.run8();
               return;
            }

            this.phobosClient.getChatManager().handleText("Loaded %s successfully".formatted(text));
      }
   }

   public void run8() {
      if (this.animatedTexture != null) {
         this.animatedTexture.clear();
         this.animatedTexture = null;
      }
   }

   public Setting<AmbienceModule.SkyMode> getSkyMode() {
      return this.skyMode;
   }

   public Setting<String> getSkyName() {
      return this.skyName;
   }

   public Setting<Color> getLightningColor() {
      return this.lightningColor;
   }

   public Setting<Float> getBlur() {
      return this.blur;
   }

   public Setting<Float> getIntensity() {
      return this.intensity;
   }

   public Setting<Integer> getTimeValue() {
      return this.timeValue;
   }

   public Setting<Boolean> getTime() {
      return this.time;
   }

   public PostEffectShader getPostEffectShader() {
      return this.postEffectShader;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public Setting<Color> getHitColor() {
      return this.hitColor;
   }

   public static AmbienceModule getAmbienceModule() {
      return ambienceModule;
   }

   public AnimatedTexture getAnimatedTexture() {
      return this.animatedTexture;
   }

   public Setting<Boolean> getLightning() {
      return this.lightning;
   }

   public Setting<Boolean> getBloom() {
      return this.bloom;
   }

   public enum ExternalSkybox {
      Image,
      Complex;
   }

   public enum Location implements TextProvider {
      Resources("Gets the texture from assets/phobos/textures"),
      Global("Looks up specified texture from C:/Users/user/earthhack/textures");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Location(final String description) {
         this.text = description;
      }
   }

   public enum SkyMode {
      Color,
      Texture,
      Shader;
   }
}
