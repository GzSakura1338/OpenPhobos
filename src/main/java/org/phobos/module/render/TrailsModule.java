package org.phobos.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.entity.PositionRotationTracker;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.data.VecRecord;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.IPersistentProjectileEntity;
import org.phobos.module.Module;

public class TrailsModule extends Module {
   public Setting<Boolean> booleanValueSetting;
   public Setting<Double> doubleValueSetting;
   public Setting<Integer> integerSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Float> floatValueSetting;
   public Setting<Long> longValueSetting;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Double> doubleValueSetting2;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Double> doubleValueSetting3;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Float> floatValueSetting2;
   public Setting<Boolean> booleanValueSetting6;
   public List<TrailsModule.Projectile> projectileList;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Double> doubleValueSetting4;
   public Setting<Boolean> booleanValueSetting10;
   public Setting<Long> longValueSetting2;
   public Setting<Boolean> booleanValueSetting11;
   public Setting<Color> colorSetting;
   public Setting<Boolean> booleanValueSetting12;
   private static Object[] phobos$g1806f88a8;

   public TrailsModule(PhobosClient phobosClient) {
      super(phobosClient, "Trails", Categories.render, "Draws trails behind projectiles");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Boolean var3 = true & true;
      BooleanSetting var2 = new BooleanSetting("Render", var3);
      Setting var4 = var2.resolveSetting7("Line");
      Setting var5 = this.resolveSetting2(var4);
      this.booleanValueSetting = var5;
      Color var7 = Color.WHITE;
      ColorSetting var6 = new ColorSetting("Color", var7);
      Setting var8 = var6.resolveSetting7("Line color");
      Setting var9 = this.resolveSetting2(var8);
      this.colorSetting = var9;
      LongNumberSetting var10 = new LongNumberSetting("Fade", 500L, 0L, 10000L, true & true);
      Setting var11 = this.resolveSetting2(var10);
      this.longValueSetting2 = var11;
      Float var13 = (float)1.0F;
      Float var14 = (float)0.0F;
      Float var15 = (float)2.0F;
      NumberSetting var12 = new NumberSetting<>("Width", var13, var14, var15);
      Setting var16 = var12.resolveSetting7("Line width");
      Setting var17 = this.resolveSetting2(var16);
      this.floatValueSetting2 = var17;
      Double var19 = 100.0;
      Double var20 = 0.0;
      Double var21 = 300.0;
      NumberSetting var18 = new NumberSetting<>("Range", var19, var20, var21);
      Setting var22 = this.resolveSetting2(var18);
      this.doubleValueSetting = var22;
      BooleanSetting var23 = new BooleanSetting("Particle");
      Setting var24 = var23.resolveSetting7("Spawn particles");
      Setting var25 = this.resolveSetting2(var24);
      this.booleanValueSetting10 = var25;
      BooleanSetting var26 = new BooleanSetting("Skip");
      Setting var27 = this.resolveSetting2(var26);
      this.booleanValueSetting6 = var27;
      LongNumberSetting var28 = new LongNumberSetting("ParticleTime", 150L, 0L, 2000L, true & true);
      Setting var29 = this.resolveSetting2(var28);
      this.longValueSetting = var29;
      Integer var31 = 10;
      Integer var32 = 0;
      Integer var33 = 50;
      NumberSetting var30 = new NumberSetting<>("ParticleAmount", var31, var32, var33);
      Setting var34 = this.resolveSetting2(var30);
      this.integerSetting = var34;
      Double var36 = 0.2;
      Double var37 = 0.0;
      Double var38 = 2.0;
      NumberSetting var35 = new NumberSetting<>("ParticleSpeed", var36, var37, var38);
      Setting var39 = this.resolveSetting2(var35);
      this.doubleValueSetting2 = var39;
      Double var41 = 0.0;
      Double var42 = 0.0;
      Double var43 = 2.0;
      NumberSetting var40 = new NumberSetting<>("ParticleRandom", var41, var42, var43);
      Setting var44 = this.resolveSetting2(var40);
      this.doubleValueSetting3 = var44;
      Float var46 = (float)0.1F;
      Float var47 = (float)0.0F;
      Float var48 = (float)2.0F;
      NumberSetting var45 = new NumberSetting<>("ParticleSize", var46, var47, var48);
      Setting var49 = this.resolveSetting2(var45);
      this.floatValueSetting = var49;
      BooleanSetting var50 = new BooleanSetting("Circle");
      Setting var51 = this.resolveSetting2(var50);
      this.booleanValueSetting5 = var51;
      Double var53 = 0.99;
      Double var54 = 0.0;
      Double var55 = 1.0;
      NumberSetting var52 = new NumberSetting<>("ParticleGravity", var53, var54, var55);
      Setting var56 = this.resolveSetting2(var52);
      this.doubleValueSetting4 = var56;
      Boolean var58 = true & true;
      BooleanSetting var57 = new BooleanSetting("Main", var58);
      Setting var59 = var57.resolveSetting7("Category control");
      Setting var60 = this.resolveSetting2(var59);
      this.booleanValueSetting12 = var60;
      Boolean var62 = true & true;
      BooleanSetting var61 = new BooleanSetting("Pearls", var62);
      Setting var63 = this.resolveSetting2(var61);
      this.booleanValueSetting11 = var63;
      Boolean var65 = false & true;
      BooleanSetting var64 = new BooleanSetting("Arrows", var65);
      Setting var66 = this.resolveSetting2(var64);
      this.booleanValueSetting4 = var66;
      Boolean var68 = false & true;
      BooleanSetting var67 = new BooleanSetting("Other", var68);
      Setting var69 = var67.resolveSetting7("Category control");
      Setting var70 = this.resolveSetting2(var69);
      this.booleanValueSetting3 = var70;
      Boolean var72 = false & true;
      BooleanSetting var71 = new BooleanSetting("XP", var72);
      Setting var73 = this.resolveSetting2(var71);
      this.booleanValueSetting7 = var73;
      Boolean var75 = false & true;
      BooleanSetting var74 = new BooleanSetting("Snowballs", var75);
      Setting var76 = this.resolveSetting2(var74);
      this.booleanValueSetting9 = var76;
      Boolean var78 = false & true;
      BooleanSetting var77 = new BooleanSetting("Fireballs", var78);
      Setting var79 = this.resolveSetting2(var77);
      this.booleanValueSetting2 = var79;
      Boolean var81 = false & true;
      BooleanSetting var80 = new BooleanSetting("Potions", var81);
      Setting var82 = this.resolveSetting2(var80);
      this.booleanValueSetting8 = var82;
      ArrayList var83 = new ArrayList();
      this.projectileList = var83;
      Setting var84 = this.booleanValueSetting;
      Setting[] var85 = new Setting[4];
      Setting var86 = this.colorSetting;
      var85[0] = var86;
      Setting var87 = this.longValueSetting2;
      var85[1] = var87;
      Setting var88 = this.floatValueSetting2;
      var85[2] = var88;
      Setting var89 = this.doubleValueSetting;
      var85[3] = var89;
      this.resolveSettingGroup(var84, var85);
      Setting var90 = this.booleanValueSetting10;
      Setting[] var91 = new Setting[8];
      Setting var92 = this.booleanValueSetting6;
      var91[0] = var92;
      Setting var93 = this.longValueSetting;
      var91[1] = var93;
      Setting var94 = this.integerSetting;
      var91[2] = var94;
      Setting var95 = this.doubleValueSetting2;
      var91[3] = var95;
      Setting var96 = this.doubleValueSetting3;
      var91[4] = var96;
      Setting var97 = this.booleanValueSetting5;
      var91[5] = var97;
      Setting var98 = this.doubleValueSetting4;
      var91[6] = var98;
      Setting var99 = this.floatValueSetting;
      var91[7] = var99;
      this.resolveSettingGroup(var90, var91);
      Setting var100 = this.booleanValueSetting12;
      Setting[] var101 = new Setting[2];
      Setting var102 = this.booleanValueSetting11;
      var101[0] = var102;
      Setting var103 = this.booleanValueSetting4;
      var101[1] = var103;
      this.resolveSettingGroup(var100, var101);
      Setting var104 = this.booleanValueSetting3;
      Setting[] var105 = new Setting[4];
      Setting var106 = this.booleanValueSetting7;
      var105[0] = var106;
      Setting var107 = this.booleanValueSetting9;
      var105[1] = var107;
      Setting var108 = this.booleanValueSetting2;
      var105[2] = var108;
      Setting var109 = this.booleanValueSetting8;
      var105[3] = var109;
      this.resolveSettingGroup(var104, var105);
      var var110 = new SafeListener<TickEvent2>() {
         public void handleObject(
            TickEvent2 tickEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            for (Entity entity : clientWorld.getOtherEntities(null, BlockPosUtils.createBox(clientPlayerEntity, TrailsModule.this.doubleValueSetting.getValue()))) {
               if (entity.age <= 1000) {
                  if (TrailsModule.this.booleanValueSetting12.getValue()) {
                     if (entity instanceof EnderPearlEntity && TrailsModule.this.booleanValueSetting11.getValue()) {
                        TrailsModule.this.handleEntity(entity);
                     }

                     if (entity instanceof IPersistentProjectileEntity iPersistentProjectileEntity
                        && TrailsModule.this.booleanValueSetting4.getValue()
                        && !iPersistentProjectileEntity.collided()) {
                        TrailsModule.this.handleEntity(entity);
                     }
                  }

                  if (TrailsModule.this.booleanValueSetting3.getValue()) {
                     if (entity instanceof PotionEntity && TrailsModule.this.booleanValueSetting8.getValue()) {
                        TrailsModule.this.handleEntity(entity);
                     }

                     if (entity instanceof ExperienceBottleEntity && TrailsModule.this.booleanValueSetting7.getValue()) {
                        TrailsModule.this.handleEntity(entity);
                     }

                     if (entity instanceof SnowballEntity && TrailsModule.this.booleanValueSetting9.getValue()) {
                        TrailsModule.this.handleEntity(entity);
                     }

                     if (entity instanceof FireballEntity && TrailsModule.this.booleanValueSetting2.getValue()) {
                        TrailsModule.this.handleEntity(entity);
                     }
                  }
               }
            }
         }
      };
      this.handleListener(var110);
      var var111 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (TrailsModule.this.booleanValueSetting.getValue()) {
               List<Line> list = new ArrayList<>();

               for (TrailsModule.Projectile projectile : TrailsModule.this.projectileList) {
                  projectile.handleList(list, TrailsModule.this.longValueSetting2.getValue());
               }

               Render3d.handleLines(Lines.createBuilder().resolveBuilder4(list).resolveBuilder2(TrailsModule.this.floatValueSetting2.getValue()).getLines());
            }
         }
      };
      this.handleListener(var111);
      List var112 = this.projectileList;
      Runnable var129 = var112::clear;
      ModuleListenerUtils.handleListenerHolder4(this, var129);
   }

   public void handleEntity(Entity entity) {
      TrailsModule.Projectile projectile = this.projectileList.stream().filter(projectilex -> projectilex.entity.equals(entity)).findAny().orElse(null);
      List<TrailsModule.VecData> list = new ArrayList<>();
      if (projectile != null) {
         list = projectile.vecs;
         list.add(new TrailsModule.VecData(new VecRecord(entity.getPos(), System.currentTimeMillis()), this.colorSetting.getValue()));
         this.projectileList.remove(projectile);
      } else {
         list.add(new TrailsModule.VecData(new VecRecord(entity.getPos(), System.currentTimeMillis()), this.colorSetting.getValue()));
      }

      this.projectileList.add(new TrailsModule.Projectile(entity, list, System.currentTimeMillis()));
      if (this.booleanValueSetting10.getValue()) {
         if (this.booleanValueSetting6.getValue() && entity.age != 1) {
            return;
         }

         double d2 = Math.toRadians(entity.getYaw());
         double d3 = Math.toRadians(entity.getPitch());
         Vec3d vec3d2 = new Vec3d(-Math.cos(d3) * Math.sin(d2), -Math.sin(d3), -Math.cos(d3) * Math.cos(d2)).normalize();
         Vec3d vec3d3 = new Vec3d(0.0, 1.0, 0.0);
         Vec3d vec3d4 = vec3d3.crossProduct(vec3d2).normalize();
         if (vec3d4.length() < 1.0E-6) {
            vec3d3 = new Vec3d(0.0, 0.0, 1.0);
            vec3d4 = vec3d3.crossProduct(vec3d2).normalize();
         }

         Vec3d vec3d5 = vec3d2.crossProduct(vec3d4).normalize();

         for (int i = 0; i < this.integerSetting.getValue(); i++) {
            double d = (Math.PI * 2) * i / this.integerSetting.getValue().intValue();
            Vec3d vec3d = vec3d4.multiply(Math.cos(d)).add(vec3d5.multiply(Math.sin(d))).normalize();
            PositionRotationTracker positionRotationTracker = new PositionRotationTracker(
               entity.getPos(),
               vec3d.multiply(this.doubleValueSetting2.getValue() + MathUtils.computeDouble9(0.0, this.doubleValueSetting3.getValue())),
               new Vec2f(this.floatValueSetting.getValue(), this.floatValueSetting.getValue())
            ) {
               @Override
               public boolean alwaysTrue() {
                  return !TrailsModule.this.booleanValueSetting5.getValue();
               }

               @Override
               public Color getColor() {
                  float f = 255.0F / (float)this.getLongValue();
                  float f2 = MathHelper.clamp((int)(f * (float)(this.getLongValue2() + this.getLongValue() - System.currentTimeMillis())), 0, 255) / 255.0F;
                  return ColorUtils.createColor6(TrailsModule.this.colorSetting.getValue(), f2);
               }

               @Override
               public void run() {
                  this.setVec3d(this.getVec3d2());
                  this.setVec3d2(this.getVec3d2().add(this.getVec3d3()));
                  this.setVec3d3(this.getVec3d3().multiply(TrailsModule.this.doubleValueSetting4.getValue()));
               }
            };
            this.phobosClient.getTrackerManager().handlePositionRotationTracker2(positionRotationTracker, this.longValueSetting.getValue());
         }
      }
   }

   private record Projectile(Entity entity, List<TrailsModule.VecData> vecs, long timeStamp) {

      public void handleList(List<Line> list, long l) {
         if (!this.vecs.isEmpty()) {
            for (int i2 = 1; i2 < this.vecs.size(); i2++) {
               Vec3d vec3d = this.vecs.get(i2 - 1).vetTime.vec();
               Vec3d vec3d2 = this.vecs.get(i2).vetTime.vec();
               int i = (int)MathUtils.computeLong2(this.timeStamp + l - System.currentTimeMillis(), 0L, 255L);
               Color color = this.vecs.get(i2 - 1).color;
               Color color2 = new Color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, i / 255.0F);
               list.add(new Line(vec3d, vec3d2, color2));
            }
         }
      }
   }

   private record VecData(VecRecord vetTime, Color color) {
   }
}
