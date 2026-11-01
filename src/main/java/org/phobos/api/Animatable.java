package org.phobos.api;

import java.util.List;
import org.phobos.core.math.Vector3;

public interface Animatable {
   float getFloatValue2();

   boolean testInt(int var1);

   List<VectorFunction> getList();

   boolean isFlag();

   void setFlag(boolean var1);

   void handleVector3(Vector3 var1);

   boolean check();

   void setVector3(Vector3 var1);

   void setFloatValue2(float var1);

   void run3();
}
