package org.phobos.core.path;

import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Unmodifiable;
import org.phobos.api.TaskData;
import org.phobos.api.PathNode;

public class PathResult<T extends PathNode<T>> implements TaskData {
   public final @Unmodifiable List<T> list;
   public boolean flag = true;
   public final Vec3d vec3d;
   public final Class<T> classValue;
   public final BlockPos blockPos;
   public final Vec3d vec3d2;
   public final BlockPos blockPos2;

   public void setFlag3(boolean flag) {
      this.flag = flag;
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      i2 = i2 * 59 + (this.isFlag2() ? 79 : 97);
      Object object = this.getVec3d2();
      i2 = i2 * 59 + (object == null ? 43 : object.hashCode());
      Object object2 = this.getVec3d();
      i2 = i2 * 59 + (object2 == null ? 43 : object2.hashCode());
      Object object3 = this.getBlockPos2();
      i2 = i2 * 59 + (object3 == null ? 43 : object3.hashCode());
      Object object4 = this.getBlockPos();
      i2 = i2 * 59 + (object4 == null ? 43 : object4.hashCode());
      Object object5 = this.getList();
      i2 = i2 * 59 + (object5 == null ? 43 : object5.hashCode());
      Object object6 = this.getClassValue();
      return i2 * 59 + (object6 == null ? 43 : object6.hashCode());
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof PathResult<?> pathResult)) {
         return false;
      } else {
         if (!pathResult.testObject(this)) {
            return false;
         }

         if (this.isFlag2() != pathResult.isFlag2()) {
            return false;
         }

         Object object2 = this.getVec3d2();
         Object object3 = pathResult.getVec3d2();
         if (object2 == null ? object3 == null : object2.equals(object3)) {
            Object object4 = this.getVec3d();
            Object object5 = pathResult.getVec3d();
            if (object4 == null ? object5 == null : object4.equals(object5)) {
               Object object6 = this.getBlockPos2();
               Object object7 = pathResult.getBlockPos2();
               if (object6 == null ? object7 == null : object6.equals(object7)) {
                  Object object8 = this.getBlockPos();
                  Object object9 = pathResult.getBlockPos();
                  if (object8 == null ? object9 == null : object8.equals(object9)) {
                     Object object10 = this.getList();
                     Object object11 = pathResult.getList();
                     if (object10 == null ? object11 == null : object10.equals(object11)) {
                        Object object12 = this.getClassValue();
                        Object object13 = pathResult.getClassValue();
                        return object12 == null ? object13 == null : object12.equals(object13);
                     } else {
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean testObject(Object object) {
      return object instanceof PathResult;
   }

   @Override
   public void run() {
      this.flag = false;
   }

   @Override
   public boolean isFlag2() {
      return this.flag;
   }

   public List<T> getList() {
      return this.list;
   }

   @Override
   public String toString() {
      return "Path(exactStart="
         + this.getVec3d2()
         + ", exactGoal="
         + this.getVec3d()
         + ", start="
         + this.getBlockPos2()
         + ", goal="
         + this.getBlockPos()
         + ", path="
         + this.getList()
         + ", type="
         + this.getClassValue()
         + ", valid="
         + this.isFlag2()
         + ")";
   }

   public PathResult(Vec3d vec3d, Vec3d vec3d2, BlockPos blockPos, BlockPos blockPos2, List<T> list, Class<T> classValue) {
      this.vec3d2 = vec3d;
      this.vec3d = vec3d2;
      this.blockPos2 = blockPos;
      this.blockPos = blockPos2;
      this.list = list;
      this.classValue = classValue;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public Vec3d getVec3d2() {
      return this.vec3d2;
   }

   public Class<T> getClassValue() {
      return this.classValue;
   }

   public BlockPos getBlockPos2() {
      return this.blockPos2;
   }

   public boolean check() {
      for (int i = 0; i < this.list.size(); i++) {
         T pathNode2 = this.list.get(i);
         if (!pathNode2.alwaysTrue()) {
            return false;
         }

         if (i + 1 >= this.list.size()) {
            break;
         }

         T pathNode3 = this.list.get(i + 1);
         boolean flag = false;
         PathNode[] var5 = pathNode2.getBlockNodeArray();
         int var6 = var5.length;
         int var7 = 0;

         while (true) {
            if (var7 < var6) {
               T pathNode = (T)var5[var7];
               if (!pathNode3.equals(pathNode)) {
                  var7++;
                  continue;
               }

               flag = true;
            }

            if (!flag) {
               return false;
            }
            break;
         }
      }

      return true;
   }
}
