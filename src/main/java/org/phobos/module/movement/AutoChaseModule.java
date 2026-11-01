package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AutoChaseModule extends Module {
   public final Setting<Boolean> prediction;
   public final Setting<Double> offset;
   public final Setting<Integer> ticks;
   public PlayerEntity playerEntity;
   public final Setting<Boolean> teleport;
   public final Setting<Double> speed = this.resolveSetting2(new NumberSetting<>("Speed", 2.0, 0.1, 10.0));

   @Override
   public String nothing() {
      return this.playerEntity != null ? this.playerEntity.getNameForScoreboard() : null;
   }

   public AutoChaseModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoChase", Categories.movement, "Mimics targets movements");
      this.offset = this.resolveSetting2(new NumberSetting<>("Offset", -4.0, -10.0, 10.0));
      this.teleport = this.resolveSetting2(new BooleanSetting("Teleport").resolveSetting7("Teleport when possible"));
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 1, 4));
      this.resolveSettingGroup(this.prediction, this.ticks);
      this.handleListener(
         new SafeListener<MoveEvent2>() {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               AutoChaseModule.this.playerEntity = EntityUtils.resolvePlayerEntity(clientWorld, clientPlayerEntity, phobosClient, 30.0);
               if (AutoChaseModule.this.playerEntity != null) {
                  if (AutoChaseModule.this.prediction.getValue()) {
                     PlayerEntity playerEntity = phobosClient.getFeatureRegistry()
                        .getPredictionManager()
                        .resolvePlayerEntity3(AutoChaseModule.this.ticks.getValue(), AutoChaseModule.this.playerEntity);
                     if (playerEntity != null) {
                        AutoChaseModule.this.playerEntity = playerEntity;
                     }
                  }

                  Vec3d vec3d = clientPlayerEntity.getPos();
                  Vec3d vec3d2 = AutoChaseModule.this.playerEntity.getPos();
                  BlockHitResult blockHitResult = clientWorld.raycast(
                     new RaycastContext(
                        AutoChaseModule.this.playerEntity.getEyePos(),
                        AutoChaseModule.this.playerEntity.getPos().add(0.0, AutoChaseModule.this.offset.getValue(), 0.0),
                        ShapeType.COLLIDER,
                        FluidHandling.NONE,
                        AutoChaseModule.this.playerEntity
                     )
                  );
                  Vec3d vec3d3 = blockHitResult != null
                     ? blockHitResult.getPos()
                     : new Vec3d(vec3d2.getX(), AutoChaseModule.this.playerEntity.getY() + AutoChaseModule.this.offset.getValue(), vec3d2.getZ());
                  double d = clientPlayerEntity.squaredDistanceTo(vec3d3);
                  if (d <= 9.0 && AutoChaseModule.this.teleport.getValue()) {
                     clientPlayerEntity.setPosition(vec3d3);
                     moveEvent2.setVec3d(Vec3d.ZERO);
                  } else {
                     float[] floats = RotationUtils.values6(
                        vec3d.x, vec3d.y, vec3d.z, vec3d2.x, vec3d2.y + AutoChaseModule.this.offset.getValue(), vec3d2.z, clientPlayerEntity.prevYaw
                     );
                     float f = floats[0];
                     float f2 = floats[1];
                     double d2 = AutoChaseModule.this.speed.getValue();
                     Vec3d vec3d4 = clientPlayerEntity.getVelocity();
                     double d3 = Math.sin(Math.toRadians(f + 90.0F));
                     double d4 = Math.cos(Math.toRadians(f + 90.0F));
                     double d5 = d2 * d4;
                     double d6 = -Math.sin(Math.toRadians(f2)) * d2;
                     double d7 = d2 * d3;
                     moveEvent2.setDoubleValue3(d5);
                     moveEvent2.setDoubleValue(vec3d4.y + d6);
                     moveEvent2.setDoubleValue2(d7);
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleModule(this);
   }
}
