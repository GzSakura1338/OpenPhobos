package org.phobos.mixin.game.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.ComputeNextEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockCollisionSpliterator.class)
public abstract class MixinBlockCollisionSpliterator implements EventSubscriber, MinecraftHolder {
   @WrapOperation(
      method = "computeNext",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"
      )
   )
   private VoxelShape computeNextHook(BlockState state, BlockView world, BlockPos pos, ShapeContext context, Operation<VoxelShape> operator) {
      VoxelShape shape = (VoxelShape)operator.call(new Object[]{state, world, pos, context});
      if (world != mc.world) {
         return shape;
      }

      ComputeNextEvent event = this.invoke(new ComputeNextEvent(state, pos, shape));
      return event.isCancelled() ? VoxelShapes.empty() : event.getVoxelShape();
   }
}
