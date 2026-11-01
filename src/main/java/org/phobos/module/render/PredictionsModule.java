package org.phobos.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.render.RenderContext;
import org.phobos.core.prediction.PredictionManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.TrackedFakePlayer;
import org.phobos.core.module.Categories;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class PredictionsModule extends Module {
   public final Setting<Boolean> points;
   public final Setting<Color> color = this.resolveSetting2(new ColorSetting("Color", Color.WHITE).getColorSetting());
   public final Setting<Float> width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F));
   public final Setting<Boolean> self;

   public PredictionsModule(PhobosClient phobosClient) {
      super(phobosClient, "Predictions", Categories.render, "Renders predictions");
      this.points = this.resolveSetting2(new BooleanSetting("Points"));
      this.self = this.resolveSetting2(new BooleanSetting("Self"));
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (AbstractClientPlayerEntity abstractClientPlayerEntity : clientWorld.getPlayers()) {
                  if ((abstractClientPlayerEntity != clientPlayerEntity || PredictionsModule.this.self.getValue())
                     && abstractClientPlayerEntity.squaredDistanceTo(clientPlayerEntity) <= PredictionManager.doubleValue
                     && abstractClientPlayerEntity instanceof IAbstractClientPlayerEntity iAbstractClientPlayerEntity) {
                     List<Line> list = new ArrayList<>();
                     renderContext.getMutableVec3d2().handleVec3d(abstractClientPlayerEntity.getPos());

                     for (TrackedFakePlayer trackedFakePlayer : iAbstractClientPlayerEntity.getPredictions()) {
                        if (trackedFakePlayer != null) {
                           Vec3d vec3d = trackedFakePlayer.getPos();
                           renderContext.getMutableVec3d().handleVec3d(vec3d);
                           list.add(
                              new Line(
                                 renderContext.getMutableVec3d2().createVec3d(),
                                 renderContext.getMutableVec3d().createVec3d(),
                                 PredictionsModule.this.color.getValue()
                              )
                           );
                           renderContext.getMutableVec3d2().handleMutableVec3d(renderContext.getMutableVec3d());
                           if (PredictionsModule.this.points.getValue()) {
                              Render3d.handleCuboid(
                                 Cuboid.createBuilder()
                                    .resolveBuilder11(new Box(vec3d, vec3d).expand(0.15))
                                    .resolveBuilder18(new Color(0, 0, 0, 0), PredictionsModule.this.color.getValue())
                                    .resolveBuilder12(1.0F)
                                    .getCuboid()
                              );
                           }
                        }
                     }

                     Render3d.handleLines(Lines.createBuilder().resolveBuilder4(list).resolveBuilder2(PredictionsModule.this.width.getValue()).getLines());
                  }
               }
            }
         }
      );
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }
}
