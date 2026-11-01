package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.MoveEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AutoAlignModule extends Module {
   public final Setting<Double> drag;
   public final Setting<Integer> depth = this.resolveSetting2(new NumberSetting<>("Depth", 1, 1, 3).resolveNumberSetting("None", 1));

   public AutoAlignModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoAlign", Categories.movement);
      this.drag = this.resolveSetting2(new NumberSetting<>("Drag", 2.0, 1.0, 3.0));
      this.handleListener(
         new SafeListener<MoveEvent2>() {
            public void handleObject(
               MoveEvent2 moveEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!PlayerUtils.check5()) {
                  Vec3d vec3d = clientPlayerEntity.getPos();
                  BlockPos blockPos = BlockPosUtils.createBlockPos(vec3d);

                  for (int i = 0; i < AutoAlignModule.this.depth.getValue(); i++) {
                     if (BlockUtils.testBlockPos7(blockPos.down(i), clientWorld)) {
                        return;
                     }
                  }

                  Vec3d vec3d2 = blockPos.toCenterPos();
                  Vec3d vec3d3 = new Vec3d(vec3d2.x, vec3d.y, vec3d2.z);
                  double d = vec3d.distanceTo(vec3d3);
                  double d2 = Math.toRadians(RotationUtils.resolveVector2d(vec3d, vec3d3).x);
                  double d3 = PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity);
                  double d4 = clientPlayerEntity.isOnGround() ? -Math.min(d3, d / AutoAlignModule.this.drag.getValue()) : -d3;
                  double d5 = -Math.sin(d2) * d4;
                  double d6 = Math.cos(d2) * d4;
                  moveEvent2.setDoubleValue3(d5);
                  moveEvent2.setDoubleValue2(d6);
               }
            }
         }
      );
   }
}
