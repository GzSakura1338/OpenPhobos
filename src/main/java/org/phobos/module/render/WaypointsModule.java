package org.phobos.module.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.lwjgl.opengl.GL11;
import org.phobos.api.JsonSerializable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.render.CameraState;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.data.EntityRecord;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class WaypointsModule extends Module {
   public final Setting<Color> box = this.resolveSetting2(new ColorSetting("Box", new Color(255, 255, 255, 50)).getColorSetting());
   public final Setting<Float> width;
   public final Setting<Float> scale;
   public final Setting<Boolean> deaths;
   public final Setting<Boolean> shadow;
   public WaypointsModule.Waypoint waypoint;
   public final Setting<Color> line = this.resolveSetting2(new ColorSetting("Line", Color.WHITE).getColorSetting());
   public final Setting<Boolean> font = this.resolveSetting2(new BooleanSetting("Font").resolveSetting7("Custom font"));
   public final CameraState cameraState;

   public boolean testDouble(double d) {
      return true;
   }

   public void handleRenderContext(RenderContext renderContext, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, WaypointsModule.Waypoint waypoint) {
      double d8;
      d8 = 1.0;
      WaypointsModule.WaypointDimension waypointDimension = WaypointsModule.WaypointDimension.fromDimension(clientWorld.getDimensionEntry());
      label24:
      if (!waypoint.waypointDimension.equals(waypointDimension)) {
         if (waypointDimension.equals(WaypointsModule.WaypointDimension.Overworld)
            && waypoint.waypointDimension.equals(WaypointsModule.WaypointDimension.Nether)) {
            d8 = 8.0;
            if (this.testDouble(8.0)) {
               break label24;
            }
         }

         if (!waypointDimension.equals(WaypointsModule.WaypointDimension.Nether)
            || !waypoint.waypointDimension.equals(WaypointsModule.WaypointDimension.Overworld)) {
            return;
         }

         d8 = 0.125;
         if (!this.testDouble(0.125)) {
            return;
         }
      }

      double d = Math.floor(waypoint.doubleValue2 * d8);
      double d2 = waypoint.doubleValue;
      double d3 = Math.floor(waypoint.doubleValue3 * d8);
      Vec3d vec3d = EntityInterpolation.createVec3d(clientPlayerEntity, renderContext.getFloatValue());
      double d4 = MathUtils.computeDouble5(d, vec3d.getX() - 150.0, vec3d.getX() + 150.0);
      double d5 = MathUtils.computeDouble5(d2, vec3d.getY() - 150.0, vec3d.getY() + 150.0);
      double d6 = MathUtils.computeDouble5(d3, vec3d.getZ() - 150.0, vec3d.getZ() + 150.0);
      Box box = BoxUtils.createBox2(new Box(d4, d5, d6, d4, d5, d6).expand(0.3, 0.0, 0.3), 0.0, 0.0, 0.0, 0.0, 1.8, 0.0);
      Render3d.handleCuboid(
         Render3d.Cuboid.createBuilder()
            .resolveBuilder11(box)
            .resolveBuilder18(this.box.getValue(), this.line.getValue())
            .resolveBuilder12(this.width.getValue())
            .getCuboid()
      );
      Vec3d vec3d2 = new Vec3d(d4, d5, d6);
      Vec3d vec3d3 = new Vec3d(d, d2, d3);
      double d7 = MathUtils.computeDouble(EntityInterpolation.createVec3d(clientPlayerEntity, TimerManager.getFloatValue()).distanceTo(vec3d3), 2);
      this.cameraState.setClientPlayerEntity(mc.player);
      this.cameraState.setCamera(mc.gameRenderer.getCamera());
      this.cameraState.setVec3d(vec3d2.add(0.0, 2.4, 0.0));
      this.cameraState.setEntity(mc.getCameraEntity());
      this.cameraState.handleFloat(renderContext.getFloatValue(), this.scale.getValue(), (matrixStack, vec3dx) -> {
         String text = waypoint.name + " - " + d7 + "m";
         float f = this.font.getValue() ? (int)this.phobosClient.getRenderManager().computeText3(text) : mc.textRenderer.getWidth(text);
         waypoint.getFloatAnimator4().setFloatValue(f);
         waypoint.getFloatAnimator4().handleFloat3(10.0F, 0.2F);
         GL11.glDepthFunc(519);
         this.handleMatrixStack(matrixStack, text, -(waypoint.getFloatAnimator4().getFloatValue2() / 2.0F));
         GL11.glDepthFunc(515);
      });
   }

   public WaypointsModule(PhobosClient phobosClient) {
      super(phobosClient, "Waypoints", Categories.render, "Shows waypoints");
      this.shadow = this.resolveSetting2(new BooleanSetting("Shadow").resolveSetting7("Font shadow"));
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F).resolveSetting7("Line width"));
      this.scale = this.resolveSetting2(new NumberSetting<>("Scale", 1.0F, 0.1F, 5.0F).resolveSetting7("Scaling"));
      this.deaths = this.resolveSetting2(new BooleanSetting("Deaths").resolveSetting7("Displays last death position"));
      this.cameraState = new CameraState();
      this.waypoint = null;
      this.createBuilder()
         .resolveBuilder4("Render")
         .resolveBuilder5(this.resolveSettingGroup(this.font, this.shadow))
         .resolveBuilder(this.box, this.line, this.width, this.scale)
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (WaypointsModule.Waypoint waypoint : phobosClient.getWaypointManager().getList2()) {
                  WaypointsModule.this.handleRenderContext(renderContext, clientPlayerEntity, clientWorld, waypoint);
               }

               if (WaypointsModule.this.waypoint != null && WaypointsModule.this.deaths.getValue()) {
                  WaypointsModule.this.handleRenderContext(renderContext, clientPlayerEntity, clientWorld, WaypointsModule.this.waypoint);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<EntityRecord>() {
            public void handleObject(
               EntityRecord entityRecord,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (entityRecord.entity() instanceof ClientPlayerEntity) {
                  WaypointsModule.this.waypoint = new WaypointsModule.Waypoint(
                     clientPlayerEntity.getX(),
                     clientPlayerEntity.getY(),
                     clientPlayerEntity.getZ(),
                     "Last Death",
                     WaypointsModule.WaypointDimension.fromDimension(clientWorld.getDimensionEntry())
                  );
               }
            }
         }
      );
      this.handleListener(new Listener<DisconnectEvent2>() {
         public void handleObject2(DisconnectEvent2 disconnectEvent2) {
            WaypointsModule.this.waypoint = null;
         }
      });
   }

   public void handleMatrixStack(MatrixStack matrixStack, String text, float f) {
      if (this.font.getValue()) {
         this.phobosClient.getRenderManager().handleMatrixStack2(matrixStack, text, f, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, this.shadow.getValue());
      } else {
         this.phobosClient.getRenderManager().getText3dRenderer().handleMatrixStack3(matrixStack, text, f, 0.0F, -1, this.shadow.getValue());
      }
   }

   public static class Waypoint implements JsonSerializable {
      public double doubleValue;
      public double doubleValue2;
      public final FloatAnimator floatAnimator = new FloatAnimator();
      public double doubleValue3;
      public String name;
      public WaypointsModule.WaypointDimension waypointDimension;

      public void setName(String text) {
         this.name = text;
      }

      public WaypointsModule.WaypointDimension getWaypointDimension() {
         return this.waypointDimension;
      }

      @Override
      public JsonElement createJsonPrimitive() {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Position", this.doubleValue2 + "&" + this.doubleValue + "&" + this.doubleValue3);
         jsonObject.addProperty("Dimension", this.waypointDimension.toString());
         return jsonObject;
      }

      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public Waypoint(String text) {
         this.name = text;
      }

      @Override
      public void handleJsonElement(JsonElement jsonElement) {
         JsonObject jsonObject = jsonElement.getAsJsonObject();

         try {
            String text = jsonObject.get("Position").getAsString();
            String[] texts = text.split("&");
            this.setDoubleValue2(Double.parseDouble(texts[0]));
            this.setDoubleValue4(Double.parseDouble(texts[1]));
            this.setDoubleValue3(Double.parseDouble(texts[2]));
            this.setWaypointDimension(WaypointsModule.WaypointDimension.fromString(jsonObject.get("Dimension").getAsString()));
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }

      public void setDoubleValue3(double d) {
         this.doubleValue3 = d;
      }

      public double getDoubleValue22() {
         return this.doubleValue2;
      }

      public void setDoubleValue4(double d) {
         this.doubleValue = d;
      }

      public double getDoubleValue3() {
         return this.doubleValue3;
      }

      public Waypoint(double d, double d2, double d3, String text, WaypointsModule.WaypointDimension waypointDimension) {
         this.doubleValue2 = d;
         this.doubleValue = d2;
         this.doubleValue3 = d3;
         this.name = text;
         this.waypointDimension = waypointDimension;
      }

      public double getDoubleValue42() {
         return this.doubleValue;
      }

      public String getName() {
         return this.name;
      }

      public void setWaypointDimension(WaypointsModule.WaypointDimension waypointDimension) {
         this.waypointDimension = waypointDimension;
      }

      public FloatAnimator getFloatAnimator4() {
         return this.floatAnimator;
      }
   }

   public enum WaypointDimension {
      Overworld("O"),
      Nether("N"),
      End("E");

      public final String name;

      public static WaypointsModule.WaypointDimension fromString(String string) {
         return switch (string) {
            case "N" -> Nether;
            case "E" -> End;
            default -> Overworld;
         };
      }

      public static WaypointsModule.WaypointDimension fromDimension(RegistryEntry<DimensionType> type) {
         if (DimensionTypes.THE_NETHER.equals(type)) {
            return Nether;
         } else {
            return DimensionTypes.THE_END.equals(type) ? Nether : Overworld;
         }
      }

      WaypointDimension(final String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
