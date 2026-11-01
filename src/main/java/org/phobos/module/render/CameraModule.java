package org.phobos.module.render;

import net.minecraft.util.hit.HitResult.Type;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public class CameraModule extends Module {
   public final Setting<Float> distance;
   public static CameraModule cameraModule;
   public final Setting<Boolean> clip = this.resolveSetting2(new BooleanSetting("Clip"));

   public static Type resolveType(Type type) {
      return cameraModule.check7() && cameraModule.clip.getValue() ? Type.MISS : type;
   }

   public static float computeFloat(float f) {
      return cameraModule.check7() ? cameraModule.distance.getValue() : f;
   }

   public static CameraModule getCameraModule() {
      return cameraModule;
   }

   public CameraModule(PhobosClient phobosClient) {
      super(phobosClient, "Camera", Categories.render, "Manages camera");
      this.distance = this.resolveSetting2(new NumberSetting<>("Distance", 4.0F, 0.0F, 20.0F));
      this.resolveSettingGroup(this.clip, this.distance);
      this.handleListener(new Listener<CameraModule.Distance>() {
         public void handleObject2(CameraModule.Distance distance) {
            distance.setFloatValue(-CameraModule.this.distance.getValue());
         }
      });
      this.handleListener(new Listener<CameraModule.Clip>() {
         public void handleObject2(CameraModule.Clip clip) {
            clip.setCancelled(CameraModule.this.clip.getValue());
         }
      });
      cameraModule = this;
   }

   public static class Clip extends CancellableEvent {
      public float floatValue;

      public float getFloatValue() {
         return this.floatValue;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public Clip(float f) {
         this.floatValue = f;
      }
   }

   public static class Distance {
      public float floatValue;

      public Distance(float f) {
         this.floatValue = f;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public float getFloatValue() {
         return this.floatValue;
      }
   }
}
