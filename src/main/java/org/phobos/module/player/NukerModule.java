package org.phobos.module.player;

import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.api.AreaScanner;
import org.phobos.api.TimeGate;
import org.phobos.api.Subscribe;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.PlayerTickEvent;
import org.phobos.module.Module;

public class NukerModule extends Module {
   public final TimeGate.Normal normal;
   public final Setting<Set<Block>> whitelist;
   public final Setting<Long> delay;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 6.0, 0.1, 6.0));

   public NukerModule(PhobosClient phobosClient) {
      super(phobosClient, "Nuker", Categories.player, "Mines blocks around you");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 0L, 0L, 1000L, true));
      this.whitelist = this.resolveSetting2(new BlockSetSetting("Whitelist"));
      this.normal = new TimeGate.Normal();
   }

   public void handleSpeedmineModule(
      SpeedmineModule speedmineModule,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      BlockPos blockPos
   ) {
      this.normal.run();
      if (clientPlayerEntity.isCreative()) {
         clientPlayerInteractionManager.attackBlock(blockPos, Direction.DOWN);
      } else {
         if (speedmineModule.check7()
            && (
               !speedmineModule.getPacketBlockBreaker().check()
                  || speedmineModule.getPacketBlockBreaker().getBlockState2().isAir()
                  || speedmineModule.getDoubleValue().getValue() && !speedmineModule.getSimpleBlockBreaker().check()
            )) {
            speedmineModule.handleBlockPos(blockPos, clientWorld, clientPlayerEntity);
         }
      }
   }

   @Subscribe
   public void handlePlayerTickEvent(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.normal.testNumber(this.delay.getValue())) {
         SpeedmineModule speedmineModule = SpeedmineModule.getSpeedmineModule();
         if (clientPlayerEntity.isCreative() || !speedmineModule.check7() || !speedmineModule.isFlag()) {
            AreaScanner.Area area = new AreaScanner.Area();
            area.handleVec3d(
               clientPlayerEntity.getEyePos(),
               this.range.getValue(),
               blockPosx -> {
                  BlockState blockState = clientWorld.getBlockState(blockPosx);
                  Set<Block> set = this.whitelist.getValue();
                  return !set.isEmpty() && !set.contains(blockState.getBlock())
                     ? false
                     : speedmineModule.testBlockPos(blockPosx, blockState, clientPlayerEntity, clientWorld);
               }
            );

            for (BlockPos blockPos : area.resolveList(clientPlayerEntity)) {
               this.handleSpeedmineModule(speedmineModule, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, blockPos);
               if (!this.delay.testObject2(0L)) {
                  break;
               }
            }
         }
      }
   }
}
