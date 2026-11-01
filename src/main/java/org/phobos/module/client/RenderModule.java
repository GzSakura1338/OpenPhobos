package org.phobos.module.client;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.glfw.GLFW;
import org.phobos.api.TextProvider;
import org.phobos.api.KeyValue;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.shader.ShaderEffectManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.module.Module;

public class RenderModule extends Module {
   public final Setting<RenderModule.ShaderOverride> lineOverride;
   public static RenderModule renderModule;
   public final Setting<ShaderEffectManager.GlobalShader> lineMode;
   public final Setting<Boolean> global;
   public final Setting<RenderModule.ShaderOverride> boxOverride;
   public final Setting<Boolean> mSAA;
   public final Setting<Boolean> bFS;
   public final Setting<Float> globalSteps;
   public final Setting<Boolean> capture = this.resolveSetting2(new BooleanSetting("Capture", true).resolveSetting7("If disabled, not visible to screen-shares"));
   public final Setting<ShaderEffectManager.GlobalShader> boxMode;
   public final Setting<Float> speed;
   public final Setting<Color> lineColor;
   public final Setting<RenderModule.MSAA> samples;
   public final Setting<Color> boxColor;

   public Setting<Float> getGlobalSteps() {
      return this.globalSteps;
   }

   public Setting<Boolean> getBFS() {
      return this.bFS;
   }

   public static int value() {
      return !renderModule.mSAA.getValue() ? -1 : renderModule.samples.getValue().samples;
   }

   public Setting<Boolean> getGlobal() {
      return this.global;
   }

   public static RenderModule getRenderModule() {
      return renderModule;
   }

   public Setting<ShaderEffectManager.GlobalShader> getLineMode() {
      return this.lineMode;
   }

   public Setting<RenderModule.ShaderOverride> getLineOverride() {
      return this.lineOverride;
   }

   public static void handleCuboid(Render3d.Cuboid cuboid) {
      renderModule.handleCuboid2(cuboid);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.bFS.setFlag4(true);
      this.capture.setFlag4(true);
      super.handleJsonElement(jsonElement);
   }

   public void handleCuboid2(Render3d.Cuboid cuboid) {
      if (!this.global.getValue()) {
         RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      } else {
         switch ((ShaderEffectManager.GlobalShader)this.boxMode.getValue()) {
            case Circle:
               RenderSystem.setShader(GameRenderer::getPositionColorProgram);
               break;
            case None:
               Render3d.positionColorCircleShader
                  .handleKeyValue(this.boxOverride.getValue().mix(cuboid.getColor(), this.boxColor.getValue()), this.globalSteps.getValue());
               Render3d.positionColorCircleShader.run3();
               break;
            case Plasma:
               Render3d.positionColorPlasmaShader.handleKeyValue(this.boxOverride.getValue().mix(cuboid.getColor(), this.boxColor.getValue()));
               Render3d.positionColorPlasmaShader.run3();
         }
      }
   }

   public Setting<Color> getLineColor() {
      return this.lineColor;
   }

   public Setting<RenderModule.ShaderOverride> getBoxOverride() {
      return this.boxOverride;
   }

   public void handleCuboid3(Render3d.Cuboid cuboid) {
      if (!this.global.getValue()) {
         RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
      } else {
         switch ((ShaderEffectManager.GlobalShader)this.lineMode.getValue()) {
            case Circle:
               RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
               break;
            case None:
               Render3d.linesCircleShader
                  .handleKeyValue(this.lineOverride.getValue().mix(cuboid.getColor2(), this.lineColor.getValue()), this.globalSteps.getValue());
               Render3d.linesCircleShader.run3();
               break;
            case Plasma:
               Render3d.linesPlasmaShader.handleKeyValue(this.lineOverride.getValue().mix(cuboid.getColor2(), this.lineColor.getValue()));
               Render3d.linesPlasmaShader.run3();
         }
      }
   }

   public Setting<Boolean> getMSAA() {
      return this.mSAA;
   }

   public Setting<Boolean> getCapture() {
      return this.capture;
   }

   public static boolean check() {
      return renderModule != null && renderModule.bFS.getValue();
   }

   public static void handleCuboid4(Render3d.Cuboid cuboid) {
      renderModule.handleCuboid3(cuboid);
   }

   public Setting<Float> getSpeed() {
      return this.speed;
   }

   public Setting<ShaderEffectManager.GlobalShader> getBoxMode() {
      return this.boxMode;
   }

   public Setting<Color> getBoxColor() {
      return this.boxColor;
   }

   public RenderModule(PhobosClient phobosClient) {
      super(phobosClient, "Render", Categories.client, "Client render settings");
      this.mSAA = this.resolveSetting2(new BooleanSetting("MSAA", false).resolveSetting7("Line smoothing"));
      this.samples = this.resolveSetting2(new EnumSetting<>("Samples", RenderModule.MSAA.X8));
      this.global = this.resolveSetting2(new BooleanSetting("Global").resolveSetting7("Overrides global rendering"));
      this.boxMode = this.resolveSetting2(new EnumSetting<>("BoxMode", ShaderEffectManager.GlobalShader.Circle));
      this.lineMode = this.resolveSetting2(new EnumSetting<>("LineMode", ShaderEffectManager.GlobalShader.Circle));
      this.boxOverride = this.resolveSetting2(new EnumSetting<>("BoxOverride", RenderModule.ShaderOverride.Merge));
      this.lineOverride = this.resolveSetting2(new EnumSetting<>("LineOverride", RenderModule.ShaderOverride.Merge));
      this.boxColor = this.resolveSetting2(new ColorSetting("BoxColor", new Color(255, 0, 255, 100)).getColorSetting());
      this.lineColor = this.resolveSetting2(new ColorSetting("LineColor", Color.WHITE).getColorSetting());
      this.globalSteps = this.resolveSetting2(
         new NumberSetting<>("GlobalSteps", 60.0F, 5.0F, 100.0F)
            .resolveSetting3(
               () -> this.boxMode.testObject2(ShaderEffectManager.GlobalShader.Circle) || this.lineMode.testObject2(ShaderEffectManager.GlobalShader.Circle)
            )
      );
      this.speed = this.resolveSetting2(new NumberSetting<>("Speed", 100.0F, 10.0F, 300.0F, "%").resolveSetting7("Shader speed"));
      this.bFS = this.resolveSetting2(new BooleanSetting("BFS").resolveSetting7("Borderless fullscreen"));
      this.createBuilder()
         .resolveBuilder6(this.global)
         .resolveBuilder5(
            this.resolveSettingGroup2("Box", this.boxMode, this.boxOverride, this.boxColor),
            this.resolveSettingGroup2("Line", this.lineMode, this.lineOverride, this.lineColor)
         )
         .resolveBuilder(this.globalSteps)
         .getSettingSubsection();
      this.resolveSettingGroup(this.mSAA, this.samples);
      this.bFS.handleConsumer2(booleanValue -> {
         Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
         long l = mc.getWindow().getHandle();
         if (!booleanValue) {
            if (GLFW.glfwGetWindowAttrib(l, 131077) == 0) {
               GLFW.glfwSetWindowAttrib(l, 131077, 1);
               GLFW.glfwSetWindowMonitor(l, 0L, 30, 30, dimension.width - 30, dimension.height - 30, -1);
            }
         } else {
            GLFW.glfwSetWindowAttrib(l, 131077, 0);
            GLFW.glfwSetWindowMonitor(l, 0L, 0, 0, dimension.width, dimension.height, -1);
         }
      });
      renderModule = this;
   }

   private enum MSAA {
      X2(2),
      X4(4),
      X8(8),
      X16(16);

      public final int samples;

      public int getSamples() {
         return this.samples;
      }

      MSAA(final int samples) {
         this.samples = samples;
      }
   }

   private enum ShaderOverride implements TextProvider {
      Merge("Uses actual color as main and custom as secondary") {
         @Override
         public KeyValue<Color, Color> mix(Color original, Color custom) {
            return KeyValue.createValue(original, custom);
         }
      },
      Overwrite("Swaps actual color with custom one, and custom with actual") {
         @Override
         public KeyValue<Color, Color> mix(Color original, Color custom) {
            return KeyValue.createValue(original, custom).getKeyValue2();
         }
      };

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      ShaderOverride(final String description) {
         this.text = description;
      }

      public abstract KeyValue<Color, Color> mix(Color var1, Color var2);
   }
}
