package org.phobos.module.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TimeGate;
import org.phobos.api.TimeGate.Threaded;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.render.CameraState;
import org.phobos.core.render.PlayerModelCopy;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.core.render.Render3d.Fade;
import org.phobos.data.EntityRecord;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.ILightningEntity;
import org.phobos.mixin.game.entity.ILivingEntity;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.Module;

public class KillEffectModule extends Module {
   public Setting<Long> longValueSetting;
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Integer> integerSetting;
   public static final Identifier identifier = Identifier.of("phoboslite", "textures/effect/circle.png");
   public CopyOnWriteArrayList<PlayerModelCopy> playerModelCopyCopyOnWriteArrayList;
   public Setting<Boolean> booleanValueSetting4;
   public List<KillEffectModule.Beam> beamList;
   public CameraState cameraState;
   public Setting<Color> colorSetting;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Long> longValueSetting2;
   public Setting<Color> colorSetting2;
   public Setting<Integer> integerSetting2;
   public List<KillEffectModule.Circle> circleList;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Integer> integerSetting3;
   public Setting<Boolean> booleanValueSetting7;
   public Map<Integer, Long> integerMap;
   public static final Identifier identifier2 = Identifier.of("phoboslite", "textures/effect/arrow.png");
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Color> colorSetting3;
   private static Object[] phobos$g1806f4728;
   private static Object[] phobos$g1806f4820;
   private static Object[] phobos$g1806f4918;

   public void handleBooleanValue2(Boolean booleanValue) {
      if (!this.booleanValueSetting3.getValue()) {
         this.circleList.clear();
      }
   }

   public void handleBooleanValue3(Boolean booleanValue) {
      if (this.booleanValueSetting4.getValue()) {
         ClientPlayerEntity clientPlayerEntity = mc.player;
         if (clientPlayerEntity != null) {
            Vec3d vec3d = clientPlayerEntity.getRotationVec(TimerManager.getFloatValue());
            Vec3d vec3d2 = clientPlayerEntity.getPos();
            Vec3d vec3d3 = vec3d2.add(vec3d.x * 4.0, vec3d.y * 4.0, vec3d.z * 4.0);
            this.circleList.add(new KillEffectModule.Circle(vec3d3, this.integerSetting3.getValue(), this.longValueSetting2.getValue()));
         }

         this.booleanValueSetting4.setValue(false);
      }
   }

   public void handleBooleanValue(Boolean booleanValue) {
      if (!this.booleanValueSetting.getValue()) {
         this.beamList.clear();
      }
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f) {
      Render3d.run6();

      for (PlayerModelCopy playerModelCopy : this.playerModelCopyCopyOnWriteArrayList) {
         Render3d.handlePlayerEntityModel4(playerModelCopy.getPlayerEntityPlayerEntityModel());
         Render3d.handlePlayerEntity(
            playerModelCopy.getPlayerEntity(),
            playerModelCopy.getPlayerEntityPlayerEntityModel(),
            playerModelCopy.getLongValue2(),
            this.colorSetting2,
            this.longValueSetting,
            this.booleanValueSetting8,
            this.colorSetting3,
            this.floatValueSetting,
            playerModelCopy.getFloatValue()
         );
      }

      Render3d.run2();

      for (KillEffectModule.Beam beam : this.beamList) {
         beam.run();
      }

      if (this.booleanValueSetting3.getValue()) {
         this.cameraState.setCamera(mc.gameRenderer.getCamera());
         this.cameraState.setEntity(mc.getCameraEntity());
         this.cameraState.setClientPlayerEntity(clientPlayerEntity);

         for (KillEffectModule.Circle circle : this.circleList) {
            circle.handleCameraState(this.cameraState, f, this.booleanValueSetting7.getValue());
         }
      }
   }

   public KillEffectModule(PhobosClient phobosClient) {
      super(phobosClient, "KillEffect", Categories.render, "Creates effects when a player dies");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      Boolean var3 = true & true;
      BooleanSetting var2 = new BooleanSetting("Lightning", var3);
      Setting var4 = var2.resolveSetting7("Summons a lightning");
      Setting var5 = this.resolveSetting2(var4);
      this.booleanValueSetting2 = var5;
      Integer var7 = 0;
      Integer var8 = 0;
      Integer var9 = 20;
      NumberSetting var6 = new NumberSetting<>("Flashes", var7, var8, var9);
      Setting var10 = this.resolveSetting2(var6);
      this.integerSetting = var10;
      Integer var12 = 2;
      Integer var13 = 0;
      Integer var14 = 20;
      NumberSetting var11 = new NumberSetting<>("Life", var12, var13, var14);
      Setting var15 = this.resolveSetting2(var11);
      this.integerSetting2 = var15;
      Boolean var17 = false & true;
      BooleanSetting var16 = new BooleanSetting("Model", var17);
      Setting var18 = var16.resolveSetting7("Custom model");
      Setting var19 = this.resolveSetting2(var18);
      this.booleanValueSetting9 = var19;
      Color var21 = Color.WHITE;
      Color var22 = ColorUtils.createColor6(var21, (float)0.2F);
      ColorSetting var20 = new ColorSetting("ModelColor", var22);
      ColorSetting var23 = var20.getColorSetting();
      Setting var24 = this.resolveSetting2(var23);
      this.colorSetting2 = var24;
      BooleanSetting var25 = new BooleanSetting("Lines");
      Setting var26 = var25.resolveSetting7("Model lines");
      Setting var27 = this.resolveSetting2(var26);
      this.booleanValueSetting8 = var27;
      Color var29 = Color.WHITE;
      ColorSetting var28 = new ColorSetting("LinesColor", var29);
      ColorSetting var30 = var28.getColorSetting();
      Setting var31 = this.resolveSetting2(var30);
      this.colorSetting3 = var31;
      Float var33 = (float)1.0F;
      Float var34 = (float)0.0F;
      Float var35 = (float)3.0F;
      NumberSetting var32 = new NumberSetting<>("Width", var33, var34, var35);
      Setting var36 = var32.resolveSetting7("Line width");
      Setting var37 = this.resolveSetting2(var36);
      this.floatValueSetting = var37;
      LongNumberSetting var38 = new LongNumberSetting("Fade", 500L, 0L, 5000L);
      Setting var39 = var38.resolveSetting7("Fade time");
      Setting var40 = this.resolveSetting2(var39);
      this.longValueSetting = var40;
      BooleanSetting var41 = new BooleanSetting("Animations");
      Setting var42 = var41.resolveSetting7("Copy limb animations");
      Setting var43 = this.resolveSetting2(var42);
      this.booleanValueSetting5 = var43;
      BooleanSetting var44 = new BooleanSetting("Hive");
      Setting var45 = var44.resolveSetting7("Hive effect");
      Setting var46 = this.resolveSetting2(var45);
      this.booleanValueSetting = var46;
      Color var48 = Color.WHITE;
      ColorSetting var47 = new ColorSetting("HiveColor", var48);
      ColorSetting var49 = var47.getColorSetting();
      Setting var50 = this.resolveSetting2(var49);
      this.colorSetting = var50;
      BooleanSetting var51 = new BooleanSetting("Circle");
      Setting var52 = this.resolveSetting2(var51);
      this.booleanValueSetting3 = var52;
      LongNumberSetting var53 = new LongNumberSetting("CircleSwitch", 1000L, 10L, 1000L, true & true);
      Setting var54 = this.resolveSetting2(var53);
      this.longValueSetting2 = var54;
      Integer var56 = 3;
      Integer var57 = 0;
      Integer var58 = 100;
      NumberSetting var55 = new NumberSetting<>("CircleTimes", var56, var57, var58);
      Setting var59 = this.resolveSetting2(var55);
      this.integerSetting3 = var59;
      BooleanSetting var60 = new BooleanSetting("Arrow");
      Setting var61 = this.resolveSetting2(var60);
      this.booleanValueSetting7 = var61;
      BooleanSetting var62 = new BooleanSetting("Sound");
      Setting var63 = this.resolveSetting2(var62);
      this.booleanValueSetting6 = var63;
      BooleanSetting var64 = new BooleanSetting("Test");
      Setting var65 = this.resolveSetting2(var64);
      this.booleanValueSetting4 = var65;
      CopyOnWriteArrayList var66 = new CopyOnWriteArrayList();
      this.playerModelCopyCopyOnWriteArrayList = var66;
      HashMap var67 = new HashMap();
      this.integerMap = var67;
      ArrayList var68 = new ArrayList();
      this.beamList = var68;
      ArrayList var69 = new ArrayList();
      this.circleList = var69;
      CameraState var70 = new CameraState();
      this.cameraState = var70;
      Setting var71 = this.booleanValueSetting2;
      Setting[] var72 = new Setting[2];
      Setting var73 = this.integerSetting;
      var72[0] = var73;
      Setting var74 = this.integerSetting2;
      var72[1] = var74;
      this.resolveSettingGroup(var71, var72);
      Builder var75 = this.createBuilder();
      Setting var76 = this.booleanValueSetting9;
      Builder var77 = var75.resolveBuilder6(var76);
      SettingGroup[] var78 = new SettingGroup[1];
      Setting var79 = this.booleanValueSetting8;
      Setting[] var80 = new Setting[2];
      Setting var81 = this.colorSetting3;
      var80[0] = var81;
      Setting var82 = this.floatValueSetting;
      var80[1] = var82;
      SettingGroup var83 = this.resolveSettingGroup(var79, var80);
      var78[0] = var83;
      Builder var84 = var77.resolveBuilder5(var78);
      Setting[] var85 = new Setting[3];
      Setting var86 = this.colorSetting2;
      var85[0] = var86;
      Setting var87 = this.longValueSetting;
      var85[1] = var87;
      Setting var88 = this.booleanValueSetting5;
      var85[2] = var88;
      Builder var89 = var84.resolveBuilder(var85);
      var89.getSettingSubsection();
      Setting var90 = this.booleanValueSetting;
      Setting[] var91 = new Setting[1];
      Setting var92 = this.colorSetting;
      var91[0] = var92;
      this.resolveSettingGroup(var90, var91);
      Setting var93 = this.booleanValueSetting3;
      Setting[] var94 = new Setting[4];
      Setting var95 = this.longValueSetting2;
      var94[0] = var95;
      Setting var96 = this.integerSetting3;
      var94[1] = var96;
      Setting var97 = this.booleanValueSetting7;
      var94[2] = var97;
      Setting var98 = this.booleanValueSetting6;
      var94[3] = var98;
      this.resolveSettingGroup(var93, var94);
      var var99 = new SafeListener<EntityRecord>() {
         public void handleObject(
            EntityRecord entityRecord,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            KillEffectModule.this.integerMap.entrySet().removeIf(entry -> entry.getValue() + 1000L < System.currentTimeMillis());
            if (entityRecord.entity() instanceof PlayerEntity playerEntity
               && !playerEntity.equals(clientPlayerEntity)
               && !KillEffectModule.this.integerMap.containsKey(playerEntity.getId())) {
               double d = playerEntity.getX();
               double d2 = playerEntity.getY();
               double d3 = playerEntity.getZ();
               KillEffectModule.this.integerMap.put(playerEntity.getId(), System.currentTimeMillis());
               if (KillEffectModule.this.booleanValueSetting2.getValue()) {
                  LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(clientWorld);
                  if (lightningEntity != null) {
                     lightningEntity.refreshPositionAndAngles(d, d2, d3, 0.0F, 0.0F);
                     clientWorld.addEntity(lightningEntity);
                     clientWorld.spawnEntity(lightningEntity);
                     if (KillEffectModule.this.integerSetting.getValue() > 0) {
                        ((ILightningEntity)(Object)lightningEntity).setRemainingActions(KillEffectModule.this.integerSetting.getValue());
                     }

                     ((ILightningEntity)(Object)lightningEntity).setAmbientTick(KillEffectModule.this.integerSetting2.getValue());
                  }
               }

               if (KillEffectModule.this.booleanValueSetting9.getValue()) {
                  PlayerEntity playerEntity2 = new PlayerEntity(
                     clientWorld, BlockPos.ORIGIN, playerEntity.bodyYaw, new GameProfile(playerEntity.getUuid(), playerEntity.getName().getString())
                  ) {
                     @Override
                     public boolean isSpectator() {
                        return false;
                     }

                     @Override
                     public boolean isCreative() {
                        return false;
                     }
                  };
                  playerEntity2.copyFrom(playerEntity);
                  playerEntity2.prevYaw = playerEntity.getYaw();
                  playerEntity2.setYaw(playerEntity.getYaw());
                  playerEntity2.prevBodyYaw = playerEntity.bodyYaw;
                  playerEntity2.bodyYaw = playerEntity.bodyYaw;
                  playerEntity2.prevHeadYaw = playerEntity.headYaw;
                  playerEntity2.headYaw = playerEntity.headYaw;
                  playerEntity2.handSwingTicks = playerEntity.handSwingTicks;
                  playerEntity2.setSneaking(playerEntity.isSneaking());
                  playerEntity2.setPose(playerEntity.getPose());
                  ILivingEntity iLivingEntity = (ILivingEntity)(Object)playerEntity2;
                  iLivingEntity.setLeaningPitch(((ILivingEntity)(Object)playerEntity).getLeaningPitch());
                  iLivingEntity.setLastLeaningPitch(((ILivingEntity)(Object)playerEntity).getLeaningPitch());
                  if (KillEffectModule.this.booleanValueSetting5.getValue()) {
                     playerEntity2.limbAnimator.setSpeed(playerEntity.limbAnimator.getSpeed());
                     ((ILimbAnimator)(Object)playerEntity2.limbAnimator).setPos(playerEntity.limbAnimator.getPos());
                  }

                  KillEffectModule.this.playerModelCopyCopyOnWriteArrayList.add(new PlayerModelCopy(playerEntity2, mc.getRenderTickCounter().getTickDelta(false)));
               }

               if (KillEffectModule.this.booleanValueSetting.getValue()) {
                  KillEffectModule.this.beamList.add(new KillEffectModule.Beam(new Vec3d(d, d2, d3), KillEffectModule.this.colorSetting.getValue()).getBeam());
               }

               if (KillEffectModule.this.booleanValueSetting3.getValue()) {
                  KillEffectModule.this.circleList
                     .add(
                        new KillEffectModule.Circle(
                           new Vec3d(d, d2, d3), KillEffectModule.this.integerSetting3.getValue(), KillEffectModule.this.longValueSetting2.getValue()
                        )
                     );
                  if (KillEffectModule.this.booleanValueSetting6.getValue()) {
                     var1.getPhobosSounds().handleSoundEvent(var1.getPhobosSounds().soundEvent, 1.0F);
                  }
               }
            }
         }
      };
      this.handleListener(var99);
      var var100 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            KillEffectModule.this.handleClientPlayerEntity(clientPlayerEntity, renderContext.getFloatValue());
         }
      };
      this.handleListener(var100);
      var var101 = new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            for (PlayerModelCopy playerModelCopy : KillEffectModule.this.playerModelCopyCopyOnWriteArrayList) {
               playerModelCopy.handleCopyOnWriteArrayList(
                  KillEffectModule.this.playerModelCopyCopyOnWriteArrayList, KillEffectModule.this.longValueSetting.getValue()
               );
            }

            KillEffectModule.this.beamList.removeIf(KillEffectModule.Beam::isFlag);
            KillEffectModule.this.circleList.removeIf(KillEffectModule.Circle::check);
         }
      };
      this.handleListener(var101);
      Setting var102 = this.booleanValueSetting;
      Consumer<Boolean> var119 = this::handleBooleanValue;
      var102.handleConsumer2(var119);
      Setting var120 = this.booleanValueSetting3;
      Consumer<Boolean> var137 = this::handleBooleanValue2;
      var120.handleConsumer2(var137);
      Setting var138 = this.booleanValueSetting4;
      Consumer<Boolean> var155 = this::handleBooleanValue3;
      var138.handleConsumer2(var155);
   }

   private static class Beam {
      public boolean flag;
      public final Vec3d vec3d;
      public final Color color;
      public final Color color2;
      public final FloatAnimator floatAnimator = new FloatAnimator();

      public KillEffectModule.Beam getBeam() {
         this.floatAnimator.setFloatValue(1.0F);
         return this;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public void run() {
         this.floatAnimator.handleFloat3(10.0F, 0.1F);
         this.flag = this.floatAnimator.getFloatValue2() >= 0.97F;
         double d = this.floatAnimator.getFloatValue2() * 8.0;
         double d2 = (1.0F - this.floatAnimator.getFloatValue2()) * 1.2F;
         Box box = new Box(this.vec3d.x - d2, this.vec3d.y, this.vec3d.z - d2, this.vec3d.x + d2, this.vec3d.y + d, this.vec3d.z + d2);
         Render3d.handleFade(Fade.createBuilder().resolveBuilder2(box).resolveBuilder(this.color2, this.color).getFade());
      }

      public Beam(Vec3d vec3d, Color color) {
         this.color = new Color(0, 0, 0, 0);
         this.flag = true;
         this.vec3d = vec3d;
         this.color2 = color;
      }
   }

   private static class Circle {
      public final Vec3d vec3d;
      public boolean flag;
      public final TimeGate timeGate;
      public final long longValue;
      public final int intValue;
      public final double doubleValue = 120.0;
      public int intValue2;
      public boolean flag2;

      public void run() {
         if (!this.flag2 && this.timeGate.testLong2(this.longValue)) {
            this.flag = !this.flag;
            this.intValue2++;
         }
      }

      public Circle(Vec3d vec3d, int i, long l) {
         this.timeGate = new Threaded();
         this.flag = true;
         this.intValue2 = 0;
         this.flag2 = false;
         this.vec3d = vec3d;
         this.intValue = i;
         this.longValue = l;
         this.timeGate.run();
      }

      public void handleCameraState(CameraState cameraState, float f, boolean flag) {
         this.run();
         if (this.flag) {
            cameraState.run();
            cameraState.setVec3d(this.vec3d);
            cameraState.handleFloat(f, 1.0F, (matrixStack, vec3d) -> {
               RenderSystem.setShaderTexture(0, KillEffectModule.identifier);
               RenderUtils.handleMatrixStack2(matrixStack, -60.0, -60.0, 120.0);
               if (flag) {
                  double d = 100.0;
                  RenderSystem.setShaderTexture(0, KillEffectModule.identifier2);
                  RenderUtils.handleMatrixStack2(matrixStack, 0.0, -95.0, 100.0);
               }
            });
         }
      }

      public boolean check() {
         this.flag2 = this.intValue2 >= this.intValue;
         return this.flag2 && this.timeGate.testLong(this.longValue);
      }
   }
}
