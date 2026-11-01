package org.phobos.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.data.VecRecord;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class BreadcrumbsModule extends Module {
   public Setting<Color> colorSetting;
   public Setting<Boolean> booleanValueSetting;
   public Map<Entity, List<BreadcrumbsModule.Trail>> entityMap;
   public Setting<Long> longValueSetting;
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting2;

   public void handlePlayerEntity(PlayerEntity playerEntity, float f) {
      Vec3d vec3d = EntityInterpolation.createVec3d(playerEntity, f);
      VecRecord vecRecord = new VecRecord(vec3d, System.currentTimeMillis());
      List<BreadcrumbsModule.Trail> list = this.entityMap.get(playerEntity);
      if (list == null) {
         this.entityMap.put(playerEntity, new ArrayList<>(List.of(new BreadcrumbsModule.Trail(vecRecord, this.colorSetting.getValue()))));
      } else {
         list.add(new BreadcrumbsModule.Trail(vecRecord, this.colorSetting.getValue()));
      }
   }

   public BreadcrumbsModule(PhobosClient phobosClient) {
      super(phobosClient, "Breadcrumbs", Categories.render, "Draws a trail");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Color var3 = new Color(255, 255, 255, 255);
      ColorSetting var2 = new ColorSetting("Color", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.colorSetting = var4;
      Float var6 = (float)1.0F;
      Float var7 = (float)0.0F;
      Float var8 = (float)2.0F;
      NumberSetting var5 = new NumberSetting<>("Width", var6, var7, var8);
      Setting var9 = this.resolveSetting2(var5);
      this.floatValueSetting = var9;
      LongNumberSetting var10 = new LongNumberSetting("Fade", 250L, 0L, 5000L);
      Setting var11 = this.resolveSetting2(var10);
      this.longValueSetting = var11;
      Boolean var13 = true & true;
      BooleanSetting var12 = new BooleanSetting("Self", var13);
      Setting var14 = this.resolveSetting2(var12);
      this.booleanValueSetting2 = var14;
      Boolean var16 = false & true;
      BooleanSetting var15 = new BooleanSetting("Others", var16);
      Setting var17 = this.resolveSetting2(var15);
      this.booleanValueSetting = var17;
      HashMap var18 = new HashMap();
      this.entityMap = var18;
      Setting[] var19 = new Setting[3];
      Setting var20 = this.colorSetting;
      var19[0] = var20;
      Setting var21 = this.floatValueSetting;
      var19[1] = var21;
      Setting var22 = this.longValueSetting;
      var19[2] = var22;
      this.resolveSettingGroup2("Trail", var19);
      Setting[] var23 = new Setting[2];
      Setting var24 = this.booleanValueSetting2;
      var23[0] = var24;
      Setting var25 = this.booleanValueSetting;
      var23[1] = var25;
      this.resolveSettingGroup2("Params", var23);
      var var26 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (BreadcrumbsModule.this.booleanValueSetting2.getValue()) {
               BreadcrumbsModule.this.handlePlayerEntity(clientPlayerEntity, renderContext.getFloatValue());
            }

            if (BreadcrumbsModule.this.booleanValueSetting.getValue()) {
               for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
                  if (!playerEntity.equals(clientPlayerEntity)) {
                     BreadcrumbsModule.this.handlePlayerEntity(playerEntity, renderContext.getFloatValue());
                  }
               }
            }

            Color color = BreadcrumbsModule.this.colorSetting.getValue();
            BreadcrumbsModule.this.entityMap
               .values()
               .forEach(
                  list -> {
                     list.removeIf(trailx -> trailx.vec.time() + BreadcrumbsModule.this.longValueSetting.getValue() < System.currentTimeMillis());
                     float f = color.getAlpha() / BreadcrumbsModule.this.longValueSetting.getValue().floatValue();
                     List<Line> list2 = new ArrayList<>();

                     for (int i2 = 1; i2 < list.size(); i2++) {
                        Vec3d vec3d = list.get(i2 - 1).vec.vec();
                        Vec3d vec3d2 = list.get(i2).vec.vec();
                        BreadcrumbsModule.Trail trail = list.get(i2);
                        int i = MathHelper.clamp(
                           (int)(f * (float)(trail.vec.time() + BreadcrumbsModule.this.longValueSetting.getValue() - System.currentTimeMillis())), 0, 255
                        );
                        list2.add(new Line(vec3d, vec3d2, ColorUtils.createColor6(trail.color, i / 255.0F)));
                     }

                     Render3d.handleLines(
                        Lines.createBuilder().resolveBuilder4(list2).resolveBuilder2(BreadcrumbsModule.this.floatValueSetting.getValue()).getLines()
                     );
                  }
               );
         }
      };
      this.handleListener(var26);
   }

   private record Trail(VecRecord vec, Color color) {
   }
}
