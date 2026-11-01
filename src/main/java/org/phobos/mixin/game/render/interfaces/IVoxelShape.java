package org.phobos.mixin.game.render.interfaces;

import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VoxelShape.class)
public interface IVoxelShape {
   @Accessor("voxels")
   VoxelSet getVoxels();

   @Invoker("getCoordIndex")
   int index(Axis var1, double var2);
}
