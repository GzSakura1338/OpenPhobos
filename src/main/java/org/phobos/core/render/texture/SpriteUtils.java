package org.phobos.core.render.texture;

import java.util.List;
import java.util.Set;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ItemModelGenerator.Frame;
import net.minecraft.client.render.model.json.ItemModelGenerator.Side;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class SpriteUtils {
   public static final Identifier identifier = Identifier.ofVanilla("atlases/blocks.png");

   public static void handleCallbackInfoReturnable(CallbackInfoReturnable<List<ModelElement>> callbackInfoReturnable) {
      float f = (float)value();
      float f2 = (float)value2();

      for (ModelElement modelElement : (List<ModelElement>)callbackInfoReturnable.getReturnValue()) {
         Vector3f vector3f = modelElement.from;
         Vector3f vector3f2 = modelElement.to;
         Set<Direction> set = modelElement.faces.keySet();
         if (set.size() == 1) {
            Direction direction = set.stream().findAny().get();
            switch (direction) {
               case UP:
                  vector3f.set(vector3f.x - f2, vector3f.y - f, vector3f.z - f2);
                  vector3f2.set(vector3f2.x + f2, vector3f2.y - f, vector3f2.z + f2);
                  break;
               case DOWN:
                  vector3f.set(vector3f.x - f2, vector3f.y + f, vector3f.z - f2);
                  vector3f2.set(vector3f2.x + f2, vector3f2.y + f, vector3f2.z + f2);
                  break;
               case WEST:
                  vector3f.set(vector3f.x - f, vector3f.y + f2, vector3f.z - f2);
                  vector3f2.set(vector3f2.x - f, vector3f2.y - f2, vector3f2.z + f2);
                  break;
               case EAST:
                  vector3f.set(vector3f.x + f, vector3f.y + f2, vector3f.z - f2);
                  vector3f2.set(vector3f2.x + f, vector3f2.y - f2, vector3f2.z + f2);
            }
         }
      }
   }

   public static double value() {
      return 1.0E-4;
   }

   public static double value2() {
      return 0.002;
   }

   public static boolean testList(List<Frame> list, Side side, int i, int i2) {
      Frame frame2 = null;

      for (Frame frame : list) {
         if (frame.getSide() != null && frame.getSide().equals(side)) {
            int i3 = side.isVertical() ? i2 : i;
            if (frame.getLevel() == i3 && (value2() == 0.0 || frame.getMax() == (!side.isVertical() ? i2 : i) - 1)) {
               frame2 = frame;
               break;
            }
         }
      }

      int i5 = side.isVertical() ? i : i2;
      if (frame2 == null) {
         int i4 = side.isVertical() ? i2 : i;
         list.add(new Frame(side, i5, i4));
      } else {
         frame2.expand(i5);
      }

      return true;
   }

   public static void handleSpriteContents(SpriteContents spriteContents, float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
      if (spriteContents.getId().equals(identifier) && f == callbackInfoReturnable.getReturnValueF()) {
         callbackInfoReturnable.setReturnValue(0.0F);
      }
   }
}
