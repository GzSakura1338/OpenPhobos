package org.phobos.core.chunk;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TaskData;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.task.TaskQueue;

public final class Hole implements TaskData {
   public final int intValue;
   public final int intValue2;
   public Map<BlockPos, Hole> blockPosMap;
   public final boolean flag;
   public final int intValue3;
   public final int intValue4;
   public final Set<BlockPos> blockPosSet;
   public final boolean flag2;
   public int intValue5;
   public boolean flag3;
   public final int intValue6;
   public final boolean flag4;
   public final TaskQueue taskQueue;
   @Nullable
   public BlockPos blockPos;
   public final boolean flag5;
   public final Set<BlockPos> blockPosSet2 = new HashSet<>();
   public final int intValue7;
   public final boolean flag6;

   public boolean isFlag() {
      return this.flag;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public Box createBox() {
      return new Box(this.getIntValue3(), this.getIntValue4(), this.getIntValue(), this.getIntValue2(), this.getIntValue4() + 1.0, this.getIntValue7());
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, boolean flag) {
      Box box = new Box(this.intValue3, this.intValue4, this.intValue, this.intValue2, this.intValue4 + 1.0, this.intValue7);
      return clientPlayerEntity.getBoundingBox().intersects(box) ? true : !(Math.floor(clientPlayerEntity.getY()) <= this.intValue4) || flag;
   }

   public void handleBlockPos(BlockPos blockPos) {
      this.blockPosSet.add(blockPos);
   }

   public Set<BlockPos> getBlockPosSet() {
      return this.blockPosSet;
   }

   public boolean testEntity(Entity entity) {
      Box box = entity.getBoundingBox();
      Box box2 = this.createBox2();
      return BoxUtils.testBox(box, box2);
   }

   @Override
   public void run() {
      this.setFlag3(false);
      if (this.blockPosMap != null) {
         for (BlockPos blockPos : this.blockPosSet2) {
            this.blockPosMap.remove(blockPos, this);
         }
      }
   }

   public Set<BlockPos> getBlockPosSet2() {
      return this.blockPosSet2;
   }

   public boolean testWorld(World world) {
      return world.getWorldBorder().contains(this.intValue3, this.intValue);
   }

   @Override
   public boolean isFlag2() {
      return this.flag3 && this.taskQueue.value() == this.intValue6;
   }

   public boolean testEntity2(Entity entity) {
      Box box = entity.getBoundingBox();
      Box box2 = this.createBox();
      return BoxUtils.testBox(box, box2);
   }

   public Vec3d createVec3d() {
      return new Vec3d(this.intValue3 + (this.intValue2 - this.intValue3) / 2.0, this.intValue4 + 0.5, this.intValue + (this.intValue7 - this.intValue) / 2.0);
   }

   public Hole(TaskQueue taskQueue, int i, int i2, int i3, int i4, int i5, boolean flag, boolean flag2, boolean flag3, boolean flag4) {
      this(taskQueue, taskQueue.value(), i, i2, i3, i4, i5, flag3, flag4, !flag && !flag2, flag, flag2);
   }

   public Box createBox2() {
      double d = this.intValue3 + (this.intValue2 - this.intValue3) / 2.0;
      double d2 = this.intValue + (this.intValue7 - this.intValue) / 2.0;
      return new Box(d - 0.5, this.getIntValue4(), d2 - 0.5, d + 0.5, this.getIntValue4() + 1.0, d2 + 0.5);
   }

   public double computeEntity(Entity entity) {
      return entity.squaredDistanceTo(
         this.getIntValue3() + (this.getIntValue2() - this.getIntValue3()) / 2.0,
         this.getIntValue4(),
         this.getIntValue() + (this.getIntValue7() - this.getIntValue()) / 2.0
      );
   }

   public Box createBox3(float f) {
      return new Box(this.getIntValue3(), this.getIntValue4(), this.getIntValue(), this.getIntValue2(), this.getIntValue4() + f, this.getIntValue7());
   }

   public boolean testBlockPos(BlockPos blockPos) {
      return this.blockPosSet.contains(blockPos);
   }

   public double computeBlockPos(BlockPos blockPos) {
      return blockPos.getSquaredDistance(
         this.getIntValue3() + (this.getIntValue2() - this.getIntValue3()) / 2.0,
         this.getIntValue4(),
         this.getIntValue() + (this.getIntValue7() - this.getIntValue()) / 2.0
      );
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      i2 = i2 * 59 + this.getIntValue6();
      i2 = i2 * 59 + (this.isFlag2() ? 79 : 97);
      i2 = i2 * 59 + this.getIntValue3();
      i2 = i2 * 59 + this.getIntValue4();
      i2 = i2 * 59 + this.getIntValue();
      i2 = i2 * 59 + this.getIntValue2();
      i2 = i2 * 59 + this.getIntValue7();
      i2 = i2 * 59 + (this.isFlag4() ? 79 : 97);
      i2 = i2 * 59 + (this.isFlag22() ? 79 : 97);
      i2 = i2 * 59 + (this.flag5 ? 79 : 97);
      i2 = i2 * 59 + (this.flag ? 79 : 97);
      i2 = i2 * 59 + (this.flag6 ? 79 : 97);
      i2 = i2 * 59 + this.getIntValue5();
      Object object = this.getTaskQueue();
      return i2 * 59 + (object == null ? 43 : object.hashCode());
   }

   public int getIntValue5() {
      return this.intValue5;
   }

   public void setIntValue5(int i) {
      this.intValue5 = i;
   }

   public int getIntValue4() {
      return this.intValue4;
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof Hole hole)) {
         return false;
      } else {
         if (this.getIntValue6() != hole.getIntValue6()) {
            return false;
         }

         if (this.isFlag2() != hole.isFlag2()) {
            return false;
         }

         if (this.getIntValue3() != hole.getIntValue3()) {
            return false;
         }

         if (this.getIntValue4() != hole.getIntValue4()) {
            return false;
         }

         if (this.getIntValue() != hole.getIntValue()) {
            return false;
         }

         if (this.getIntValue2() != hole.getIntValue2()) {
            return false;
         }

         if (this.getIntValue7() != hole.getIntValue7()) {
            return false;
         }

         if (this.isFlag4() != hole.isFlag4()) {
            return false;
         }

         if (this.isFlag22() != hole.isFlag22()) {
            return false;
         }

         if (this.flag5 != hole.flag5) {
            return false;
         }

         if (this.flag != hole.flag) {
            return false;
         }

         if (this.flag6 != hole.flag6) {
            return false;
         }

         if (this.getIntValue5() != hole.getIntValue5()) {
            return false;
         }

         Object object2 = this.getTaskQueue();
         Object object3 = hole.getTaskQueue();
         return object2 == null ? object3 == null : object2.equals(object3);
      }
   }

   public TaskQueue getTaskQueue() {
      return this.taskQueue;
   }

   @Override
   public String toString() {
      return "Hole(positions="
         + this.getBlockPosSet2()
         + ", empty="
         + this.getBlockPosSet()
         + ", chunk="
         + this.getTaskQueue()
         + ", version="
         + this.getIntValue6()
         + ", valid="
         + this.isFlag2()
         + ", x="
         + this.getIntValue3()
         + ", y="
         + this.getIntValue4()
         + ", z="
         + this.getIntValue()
         + ", maxX="
         + this.getIntValue2()
         + ", maxZ="
         + this.getIntValue7()
         + ", safe="
         + this.isFlag4()
         + ", built="
         + this.isFlag22()
         + ", _1x1="
         + this.flag5
         + ", _2x1="
         + this.flag
         + ", _2x2="
         + this.flag6
         + ", visitId="
         + this.getIntValue5()
         + ", map="
         + this.getBlockPosMap()
         + ", buildPos="
         + this.getBlockPos()
         + ")";
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   public Map<BlockPos, Hole> getBlockPosMap() {
      return this.blockPosMap;
   }

   public int getIntValue7() {
      return this.intValue7;
   }

   public void setBlockPos(@Nullable BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public int getIntValue6() {
      return this.intValue6;
   }

   public boolean isFlag22() {
      return this.flag2;
   }

   @Nullable
   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public void setBlockPosMap(Map<BlockPos, Hole> map) {
      this.blockPosMap = map;
   }

   public Hole(
      TaskQueue taskQueue, int i, int i2, int i3, int i4, int i5, int i6, boolean flag, boolean flag2, boolean flag3, boolean flag4, boolean flag5
   ) {
      this.blockPosSet = new HashSet<>();
      this.flag3 = true;
      this.taskQueue = taskQueue;
      this.intValue6 = i;
      this.intValue3 = i2;
      this.intValue4 = i3;
      this.intValue = i4;
      this.intValue2 = i5;
      this.intValue7 = i6;
      this.flag4 = flag;
      this.flag2 = flag2;
      this.flag5 = flag3;
      this.flag = flag4;
      this.flag6 = flag5;
   }
}
