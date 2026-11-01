package org.phobos.module.combat;

import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;

public class AutoArrowBlockModule extends PlacementModule {
   public final Setting<Integer> prediction = new NumberSetting<>("Prediction", 2, 0, 5).resolveSetting6(this);

   public AutoArrowBlockModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoArrowBlock", "Blocks arrows with blocks", Categories.combat, 6);
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.resolveBlock2(itemSwitcher);
      if (block != null) {
         for (Entity entity2 : clientWorld.getEntities()) {
            if (entity2 instanceof ArrowEntity arrowEntity) {
               Entity entity = arrowEntity.getOwner();
               if (entity != null
                  && !Objects.equals(entity, clientPlayerEntity)
                  && !this.phobosClient.getFriendList().testUUID(entity.getUuid())
                  && !(entity2.squaredDistanceTo(clientPlayerEntity.getEyePos()) > MathHelper.square(8.0))) {
                  Vec3d vec3d = entity2.getPos();
                  Vec3d vec3d2 = entity2.getVelocity();
                  TrajectoryUtils.TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
                     clientPlayerEntity, clientWorld, entity2, vec3d, vec3d2, TrajectoryUtils.EntityGravityType.High, TrajectoryUtils.Drag.Normal
                  );
                  if (trajectoryData != null) {
                     List<Vec3d> list = trajectoryData.getVec3dList2();
                     if (list.size() > this.prediction.getValue()) {
                        Vec3d vec3d3 = list.get(this.prediction.getValue());
                        Box box = new Box(vec3d3, vec3d3).expand(0.5, 0.5, 0.5);
                        BlockPos.stream(box).forEach(blockPos -> {
                           if (!this.resolvePlacement8(block, blockPos).isSuccessful()) {
                              for (BlockPos blockPos2 : this.resolveList2(blockPos, clientWorld, clientPlayerEntity, block)) {
                                 this.resolvePlacement8(block, blockPos2);
                              }

                              this.resolvePlacement8(block, blockPos);
                           }
                        });
                     }
                  }
               }
            }
         }
      }
   }
}
