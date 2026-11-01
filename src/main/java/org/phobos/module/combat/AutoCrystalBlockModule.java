package org.phobos.module.combat;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.BlockStateAccess;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;

public class AutoCrystalBlockModule extends PlacementModule {
   public final Setting<Float> reduction;
   public final Setting<Double> range = new NumberSetting<>("Range", 6.0, 2.0, 16.0).resolveSetting6(this);
   public final Setting<Boolean> extend;
   public final Setting<Float> damage = new NumberSetting<>("Damage", 6.0F, 0.1F, 12.0F).resolveSetting6(this);

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.resolveBlock2(itemSwitcher);
      if (block != null) {
         Vec3d vec3d2 = clientPlayerEntity.getPos();
         BlockStateAccess.Delegating delegating = new BlockStateAccess.Delegating(clientWorld);
         Iterator var8 = EntityUtils.resolveIterable(clientPlayerEntity, clientWorld, this.range.getValue()).iterator();

         while (true) {
            EndCrystalEntity endCrystalEntity;
            float f3;
            BlockPos blockPos2;
            while (true) {
               if (!var8.hasNext()) {
                  return;
               }

               endCrystalEntity = (EndCrystalEntity)var8.next();
               f3 = this.phobosClient.getDamageCalculator().computeEntity8(clientPlayerEntity, clientWorld, endCrystalEntity);
               if (!(f3 < this.damage.getValue())) {
                  Vec3d vec3d = endCrystalEntity.getPos();
                  blockPos2 = BlockUtils.resolveBlockPos2(vec3d2, vec3d);
                  if (BlockUtils.testBlockPos11(blockPos2, block.getDefaultState().getOutlineShape(clientWorld, blockPos2), clientWorld, false)) {
                     break;
                  }

                  BlockState blockState = delegating.getBlockPosMap().get(blockPos2);
                  delegating.getBlockPosMap().put(blockPos2, block.getDefaultState());
                  float f = this.phobosClient.getDamageCalculator().computeEntity8(clientPlayerEntity, delegating, endCrystalEntity);
                  delegating.getBlockPosMap().put(blockPos2, blockState);
                  if (!(f3 - f < this.reduction.getValue())) {
                     this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, blockPos2);
                     break;
                  }
               }
            }

            if (this.extend.getValue()) {
               for (Direction direction : DirectionUtils.directionArray) {
                  BlockPos blockPos = blockPos2.offset(direction);
                  if (!BlockUtils.testBlockPos11(blockPos, block.getDefaultState().getOutlineShape(clientWorld, blockPos), clientWorld, false)) {
                     BlockState blockState2 = delegating.getBlockPosMap().get(blockPos2);
                     delegating.getBlockPosMap().put(blockPos, block.getDefaultState());
                     float f2 = this.phobosClient.getDamageCalculator().computeEntity8(clientPlayerEntity, delegating, endCrystalEntity);
                     delegating.getBlockPosMap().put(blockPos, blockState2);
                     if (!(f3 - f2 < this.reduction.getValue())) {
                        this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, block, blockPos);
                     }
                  }
               }
            }
         }
      }
   }

   public AutoCrystalBlockModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoCrystalBlock", "Blocks existing crystals with blocks", Categories.combat, 7);
      this.reduction = new NumberSetting<>("Reduction", 4.0F, 0.1F, 10.0F).resolveSetting6(this);
      this.extend = new BooleanSetting("Extend").resolveSetting6(this);
   }
}
