package org.phobos.module.combat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;

public class AutoPearlBlockModule extends PlacementModule {
   public final Setting<AutoPearlBlockModule.Mode> mode = new EnumSetting<>("Mode", AutoPearlBlockModule.Mode.Single).resolveSetting6(this);
   public final Map<Integer, Long> integerMap;
   public final Setting<Float> delta;
   public final Setting<Long> delay = new LongNumberSetting("Delay", 1000L, 0L, 2000L, true).resolveSetting6(this);
   public final Setting<Integer> prediction = new NumberSetting<>("Prediction", 2, 0, 5).resolveSetting6(this);

   public AutoPearlBlockModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoPearlBlock", "Blocks pearls with blocks", Categories.combat, 4);
      this.delta = new NumberSetting<>("Delta", 0.5F, 0.0F, 1.0F).resolveSetting6(this);
      this.integerMap = new HashMap<>();
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.resolveBlock2(itemSwitcher);
      if (block != null) {
         this.integerMap.entrySet().removeIf(entry -> entry.getValue() + this.delay.getValue() < System.currentTimeMillis());

         for (Entity entity2 : clientWorld.getEntities()) {
            if (entity2 instanceof EnderPearlEntity enderPearlEntity && !this.integerMap.containsKey(entity2.getId())) {
               Entity entity = enderPearlEntity.getOwner();
               if (entity != null
                  && !Objects.equals(entity, clientPlayerEntity)
                  && !this.phobosClient.getFriendList().testUUID(entity.getUuid())
                  && !(entity2.squaredDistanceTo(clientPlayerEntity.getEyePos()) > MathHelper.square(8.0))) {
                  Vec3d vec3d = entity2.getPos();
                  Vec3d vec3d2 = entity2.getVelocity();
                  TrajectoryUtils.TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
                     clientPlayerEntity, clientWorld, entity2, vec3d, vec3d2, TrajectoryUtils.EntityGravityType.Normal, TrajectoryUtils.Drag.Normal
                  );
                  if (trajectoryData != null) {
                     List<Vec3d> list = trajectoryData.getVec3dList2();
                     if (list.size() > this.prediction.getValue() + 1) {
                        Vec3d vec3d3 = list.get(this.prediction.getValue());
                        Vec3d vec3d4 = list.get(this.prediction.getValue() + 1);
                        Vec3d vec3d5 = vec3d3.lerp(vec3d4, this.delta.getValue().floatValue());
                        Box box = new Box(vec3d5, vec3d5).expand(0.5, 0.5, 0.5);
                        switch ((AutoPearlBlockModule.Mode)this.mode.getValue()) {
                           case Single:
                              this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, BlockPos.ofFloored(vec3d5));
                              break;
                           case Box:
                              BlockPos.stream(box).forEach(blockPos -> this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, blockPos));
                        }

                        this.integerMap.put(entity2.getId(), System.currentTimeMillis());
                     }
                  }
               }
            }
         }
      }
   }

   public enum Mode {
      Single,
      Box;
   }
}
