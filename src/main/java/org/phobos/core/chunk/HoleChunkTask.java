package org.phobos.core.chunk;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.HoleScanner;
import org.phobos.api.SafeBlocks;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.task.TaskQueue;

public class HoleChunkTask extends ChunkTask<Hole, HoleChunkScanner> implements SafeBlocks, HoleScanner {
   public void handleMutableBlockPos2(MutableBlockPos mutableBlockPos) {
      Objects.requireNonNull(this.getTaskQueue());
      Boolean booleanValue = this.resolveBooleanValue(mutableBlockPos, true, false);
      if (booleanValue != null) {
         mutableBlockPos.setX(mutableBlockPos.getX() - 1);
         BlockState blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
         if (!this.getSet().contains(blockState.getBlock())) {
            booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
            if (booleanValue != null) {
               mutableBlockPos.setX(mutableBlockPos.getX() + 2);
               blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
               if (this.getSet().contains(blockState.getBlock())) {
                  mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                  mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                  blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                  if (this.getSet().contains(blockState.getBlock())) {
                     mutableBlockPos.setZ(mutableBlockPos.getZ() - 2);
                     blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                     if (this.getSet().contains(blockState.getBlock())) {
                        int i = 0;
                        mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i++;
                        }

                        mutableBlockPos.setZ(mutableBlockPos.getZ() + 2);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i++;
                        }

                        mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i++;
                        }

                        if (i != 3) {
                           mutableBlockPos.setX(mutableBlockPos.getX() + 2);
                           Hole hole = new Hole(
                              this.getTaskQueue(),
                              mutableBlockPos.getX(),
                              mutableBlockPos.getY(),
                              mutableBlockPos.getZ(),
                              mutableBlockPos.getX() + 1,
                              mutableBlockPos.getZ() + 1,
                              false,
                              false,
                              false,
                              false
                           );
                           hole.handleBlockPos(mutableBlockPos.toImmutable());
                           mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                           hole.setBlockPos(mutableBlockPos.toImmutable());
                           mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                           this.handleMutableBlockPos4(mutableBlockPos, hole, vec3iArray4);
                        }
                     }
                  }
               }
            }
         } else {
            mutableBlockPos.setX(mutableBlockPos.getX() + 1);
            mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
            blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
            if (!this.getSet().contains(blockState.getBlock())) {
               booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
               if (booleanValue != null) {
                  mutableBlockPos.setZ(mutableBlockPos.getZ() + 2);
                  blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                  if (this.getSet().contains(blockState.getBlock())) {
                     mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                     mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                     blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                     if (this.getSet().contains(blockState.getBlock())) {
                        int i2 = 0;
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i2++;
                        }

                        mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i2++;
                        }

                        mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i2++;
                        }

                        if (i2 != 3) {
                           mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                           mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                           Hole hole2 = new Hole(
                              this.getTaskQueue(),
                              mutableBlockPos.getX(),
                              mutableBlockPos.getY(),
                              mutableBlockPos.getZ(),
                              mutableBlockPos.getX() + 1,
                              mutableBlockPos.getZ() + 1,
                              false,
                              false,
                              false,
                              false
                           );
                           hole2.handleBlockPos(mutableBlockPos.toImmutable());
                           mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                           hole2.setBlockPos(mutableBlockPos.toImmutable());
                           mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                           this.handleMutableBlockPos4(mutableBlockPos, hole2, vec3iArray4);
                        }
                     }
                  }
               }
            } else {
               mutableBlockPos.setX(mutableBlockPos.getX() + 1);
               mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
               blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
               if (this.getSet().contains(blockState.getBlock())) {
                  mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                  mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                  booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
                  if (booleanValue != null) {
                     int i3 = 0;
                     mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                     blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                     if (this.getSet().contains(blockState.getBlock())) {
                        i3++;
                     }

                     mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                     mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                     blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                     if (this.getSet().contains(blockState.getBlock())) {
                        i3++;
                     }

                     mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                     mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                     blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                     if (this.getSet().contains(blockState.getBlock())) {
                        i3++;
                     }

                     if (i3 != 3) {
                        mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        Hole hole3 = new Hole(
                           this.getTaskQueue(),
                           mutableBlockPos.getX(),
                           mutableBlockPos.getY(),
                           mutableBlockPos.getZ(),
                           mutableBlockPos.getX() + 1,
                           mutableBlockPos.getZ() + 1,
                           false,
                           false,
                           false,
                           false
                        );
                        hole3.handleBlockPos(mutableBlockPos.toImmutable());
                        mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                        hole3.setBlockPos(mutableBlockPos.toImmutable());
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        this.handleMutableBlockPos4(mutableBlockPos, hole3, vec3iArray4);
                     }
                  }
               } else {
                  booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
                  if (booleanValue != null) {
                     mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                     mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                     blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                     if (this.getSet().contains(blockState.getBlock())) {
                        mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        int i4 = 0;
                        mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i4++;
                        }

                        mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i4++;
                        }

                        mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                        mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                        blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
                        if (this.getSet().contains(blockState.getBlock())) {
                           i4++;
                        }

                        if (i4 != 3) {
                           mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                           mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                           Hole hole4 = new Hole(
                              this.getTaskQueue(),
                              mutableBlockPos.getX(),
                              mutableBlockPos.getY(),
                              mutableBlockPos.getZ(),
                              mutableBlockPos.getX() + 1,
                              mutableBlockPos.getZ() + 1,
                              false,
                              false,
                              false,
                              false
                           );
                           hole4.handleBlockPos(mutableBlockPos.toImmutable());
                           mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                           hole4.setBlockPos(mutableBlockPos.toImmutable());
                           mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                           this.handleMutableBlockPos4(mutableBlockPos, hole4, vec3iArray4);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void handleMutableBlockPos3(MutableBlockPos mutableBlockPos) {
      Objects.requireNonNull(this.getTaskQueue());
      Boolean booleanValue = this.resolveBooleanValue(mutableBlockPos, true, false);
      if (booleanValue != null) {
         boolean flag2 = false;
         mutableBlockPos.setX(mutableBlockPos.getX() - 1);
         BlockState blockState = Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos);
         if (blockState.getBlock() != Blocks.BEDROCK) {
            if (!this.getSet2().contains(blockState.getBlock())) {
               return;
            }

            booleanValue = false;
         }

         mutableBlockPos.setX(mutableBlockPos.getX() + 1);
         mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
         blockState = this.getWorld().getBlockState(mutableBlockPos);
         if (blockState.getBlock() != Blocks.BEDROCK) {
            if (!this.getSet2().contains(blockState.getBlock())) {
               return;
            }

            booleanValue = false;
         }

         mutableBlockPos.setX(mutableBlockPos.getX() + 1);
         mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
         blockState = this.getWorld().getBlockState(mutableBlockPos);
         if (blockState.getBlock() != Blocks.BEDROCK) {
            if (blockState.getCollisionShape(this.getWorld(), mutableBlockPos).isEmpty()) {
               booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
               if (booleanValue == null) {
                  return;
               }

               flag2 = true;
               mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
               blockState = this.getWorld().getBlockState(mutableBlockPos);
               if (!this.getSet().contains(blockState.getBlock())) {
                  return;
               }

               mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
               mutableBlockPos.setX(mutableBlockPos.getX() + 1);
               blockState = this.getWorld().getBlockState(mutableBlockPos);
               if (!this.getSet().contains(blockState.getBlock())) {
                  return;
               }

               mutableBlockPos.setX(mutableBlockPos.getX() - 1);
            } else if (!this.getSet2().contains(blockState.getBlock())) {
               return;
            }

            booleanValue = false;
         }

         mutableBlockPos.setX(mutableBlockPos.getX() - 1);
         mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
         blockState = this.getWorld().getBlockState(mutableBlockPos);
         if (blockState.getBlock() != Blocks.BEDROCK) {
            if (blockState.getCollisionShape(this.getWorld(), mutableBlockPos).isEmpty()) {
               booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
               if (booleanValue == null) {
                  return;
               }

               boolean flag = flag2;
               mutableBlockPos.setX(mutableBlockPos.getX() - 1);
               blockState = this.getWorld().getBlockState(mutableBlockPos);
               if (!this.getSet().contains(blockState.getBlock())) {
                  return;
               }

               mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
               mutableBlockPos.setX(mutableBlockPos.getX() + 1);
               blockState = this.getWorld().getBlockState(mutableBlockPos);
               if (!this.getSet().contains(blockState.getBlock())) {
                  return;
               }

               mutableBlockPos.setX(mutableBlockPos.getX() + 1);
               mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
               blockState = this.getWorld().getBlockState(mutableBlockPos);
               if (!this.getSet().contains(blockState.getBlock())) {
                  if (blockState.getCollisionShape(this.getWorld(), mutableBlockPos).isEmpty() && flag) {
                     booleanValue = this.resolveBooleanValue(mutableBlockPos, booleanValue, true);
                     if (booleanValue == null) {
                        return;
                     }

                     mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                     blockState = this.getWorld().getBlockState(mutableBlockPos);
                     if (!this.getSet().contains(blockState.getBlock())) {
                        return;
                     }

                     mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                     mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                     blockState = this.getWorld().getBlockState(mutableBlockPos);
                     if (!this.getSet().contains(blockState.getBlock())) {
                        return;
                     }

                     mutableBlockPos.setX(mutableBlockPos.getX() - 2);
                     mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                     Hole hole = new Hole(
                        this.getTaskQueue(),
                        mutableBlockPos.getX(),
                        mutableBlockPos.getY(),
                        mutableBlockPos.getZ(),
                        mutableBlockPos.getX() + 2,
                        mutableBlockPos.getZ() + 2,
                        false,
                        true,
                        false,
                        true
                     );
                     hole.handleBlockPos(mutableBlockPos.toImmutable());
                     mutableBlockPos.setX(mutableBlockPos.getX() + 1);
                     hole.handleBlockPos(mutableBlockPos.toImmutable());
                     mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
                     hole.handleBlockPos(mutableBlockPos.toImmutable());
                     mutableBlockPos.setX(mutableBlockPos.getX() - 1);
                     hole.handleBlockPos(mutableBlockPos.toImmutable());
                     mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
                     this.handleMutableBlockPos4(mutableBlockPos, hole, vec3iArray5);
                  }

                  return;
               }

               if (flag2) {
                  return;
               }

               mutableBlockPos.setX(mutableBlockPos.getX() - 1);
               mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
               Hole hole2 = new Hole(
                  this.getTaskQueue(),
                  mutableBlockPos.getX(),
                  mutableBlockPos.getY(),
                  mutableBlockPos.getZ(),
                  mutableBlockPos.getX() + 1,
                  mutableBlockPos.getZ() + 2,
                  true,
                  false,
                  false,
                  true
               );
               hole2.handleBlockPos(mutableBlockPos.toImmutable());
               mutableBlockPos.setZ(mutableBlockPos.getZ() + 1);
               hole2.handleBlockPos(mutableBlockPos.toImmutable());
               mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
               this.handleMutableBlockPos4(mutableBlockPos, hole2, vec3iArray3);
               return;
            }

            if (!this.getSet2().contains(blockState.getBlock())) {
               return;
            }

            booleanValue = false;
         }

         if (flag2) {
            mutableBlockPos.setX(mutableBlockPos.getX() + 1);
            blockState = this.getWorld().getBlockState(mutableBlockPos);
            if (this.getSet().contains(blockState.getBlock())) {
               mutableBlockPos.setX(mutableBlockPos.getX() - 1);
               mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
               Hole hole3 = new Hole(
                  this.getTaskQueue(),
                  mutableBlockPos.getX(),
                  mutableBlockPos.getY(),
                  mutableBlockPos.getZ(),
                  mutableBlockPos.getX() + 2,
                  mutableBlockPos.getZ() + 1,
                  true,
                  false,
                  false,
                  true
               );
               hole3.handleBlockPos(mutableBlockPos.toImmutable());
               mutableBlockPos.setX(mutableBlockPos.getX() + 1);
               hole3.handleBlockPos(mutableBlockPos.toImmutable());
               mutableBlockPos.setX(mutableBlockPos.getX() - 1);
               this.handleMutableBlockPos4(mutableBlockPos, hole3, vec3iArray);
            }
         } else {
            mutableBlockPos.setZ(mutableBlockPos.getZ() - 1);
            Hole hole4 = new Hole(
               this.getTaskQueue(),
               mutableBlockPos.getX(),
               mutableBlockPos.getY(),
               mutableBlockPos.getZ(),
               mutableBlockPos.getX() + 1,
               mutableBlockPos.getZ() + 1,
               false,
               false,
               booleanValue,
               true
            );
            hole4.handleBlockPos(mutableBlockPos.toImmutable());
            this.handleMutableBlockPos4(mutableBlockPos, hole4, vec3iArray2);
         }
      }
   }

   public Boolean resolveBooleanValue(MutableBlockPos mutableBlockPos, boolean flag, boolean flag2) {
      if (flag2) {
         mutableBlockPos.setY(mutableBlockPos.getY() + 1);
      }

      if (!this.testMutableBlockPos3(mutableBlockPos) && !this.testMutableBlockPos3(mutableBlockPos) && (flag2 || !this.testMutableBlockPos3(mutableBlockPos))) {
         mutableBlockPos.setY(mutableBlockPos.getY() - 4);
         assert this.getWorld() != null;
         BlockState blockState = this.getWorld().getBlockState(mutableBlockPos);
         if (blockState.getBlock() == Blocks.BEDROCK) {
            mutableBlockPos.setY(mutableBlockPos.getY() + 1);
            return flag;
         }

         if (!this.getSet2().contains(blockState.getBlock())) {
            return null;
         }

         mutableBlockPos.setY(mutableBlockPos.getY() + 1);
         return Boolean.FALSE;
      } else {
         return null;
      }
   }

   public boolean testMutableBlockPos(MutableBlockPos mutableBlockPos) {
      return this.testMutableBlockPos2(mutableBlockPos, blockState -> blockState.isOf(Blocks.BEDROCK) || this.getSet2().contains(blockState.getBlock()));
   }

   @Override
   public void handleMutableBlockPos(MutableBlockPos mutableBlockPos, int i, int i2, int i3) {
      this.handleMutableBlockPos2(mutableBlockPos);
      mutableBlockPos.set(i, i2, i3);
      this.handleMutableBlockPos3(mutableBlockPos);
   }

   public HoleChunkTask(
      Map<BlockPos, Hole> map,
      ThreadExecutor<Runnable> threadExecutor,
      World world,
      MutableBlockPos mutableBlockPos,
      HoleChunkScanner holeChunkScanner,
      @Nullable TaskQueue taskQueue,
      int i,
      int i2,
      int i3,
      int i4,
      int i5,
      int i6
   ) {
      super(map, threadExecutor, world, mutableBlockPos, holeChunkScanner, taskQueue, i, i2, i3, i4, i5, i6);
   }

   public boolean testMutableBlockPos2(MutableBlockPos mutableBlockPos, Predicate<BlockState> predicate) {
      mutableBlockPos.setY(mutableBlockPos.getY() - 1);
      BlockState blockState = this.getWorld().getBlockState(mutableBlockPos);
      if (predicate.test(blockState)) {
         mutableBlockPos.setY(mutableBlockPos.getY() + 1);
         return true;
      } else {
         return false;
      }
   }

   public void handleMutableBlockPos4(MutableBlockPos mutableBlockPos, Hole hole, Vec3i[] vec3is) {
      hole.setBlockPosMap(this.getChunkScanner().getBlockPosMap());
      int i = mutableBlockPos.getX();
      int i2 = mutableBlockPos.getY();
      int i3 = mutableBlockPos.getZ();

      for (Vec3i vec3i : vec3is) {
         mutableBlockPos.set(i + vec3i.getX(), i2 + vec3i.getY(), i3 + vec3i.getZ());
         BlockPos blockPos = mutableBlockPos.toImmutable();
         if (hole.getBlockPosSet2().add(blockPos)) {
            Hole hole2 = this.getBlockPosMap().put(blockPos, hole);
            if (hole2 != null && hole2.testBlockPos(blockPos)) {
               hole2.run();
            }
         }
      }
   }

   public boolean testMutableBlockPos3(MutableBlockPos mutableBlockPos) {
      if (!Objects.requireNonNull(this.getWorld()).getBlockState(mutableBlockPos).getCollisionShape(this.getWorld(), mutableBlockPos).isEmpty()) {
         return true;
      }

      mutableBlockPos.setY(mutableBlockPos.getY() + 1);
      return false;
   }

   public HoleChunkTask(
      ThreadExecutor<Runnable> threadExecutor,
      WorldChunk worldChunk,
      TaskQueue taskQueue,
      int i,
      int i2,
      HoleChunkScanner holeChunkScanner
   ) {
      super(threadExecutor, worldChunk, taskQueue, i, i2, holeChunkScanner);
   }

   public void handleBlockPos(BlockPos blockPos, Hole hole, Hole hole2) {
      if (hole.testBlockPos(blockPos)) {
         hole.run();
      }
   }
}
