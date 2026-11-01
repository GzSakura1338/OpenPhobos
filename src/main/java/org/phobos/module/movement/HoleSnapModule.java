package org.phobos.module.movement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chunk.Hole;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.tick.TickManager.Tick;
import org.phobos.core.tick.TickManager.TickData;
import org.phobos.core.tick.TimerManager.TimerPriority;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.module.Module;

public class HoleSnapModule extends Module {
   public Setting<Boolean> booleanValueSetting;
   public static HoleSnapModule holeSnapModule;
   public Setting<HoleSnapModule.Mode> modeSetting;
   public Setting<HoleSnapModule.Priority> prioritySetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Boolean> booleanValueSetting4;
   public BlockPos blockPos;
   public Normal normal;
   public Setting<Boolean> booleanValueSetting5;
   public Map<Hole, Long> holeMap;
   public Setting<Double> doubleValueSetting;
   public Setting<Long> longValueSetting;
   public Setting<Double> doubleValueSetting2;
   public Setting<Double> doubleValueSetting3;
   public Setting<Long> longValueSetting2;
   public Setting<Integer> integerSetting;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Boolean> booleanValueSetting8;
   public int intValue;
   public Setting<Double> doubleValueSetting4;
   public Setting<Boolean> booleanValueSetting9;
   public int intValue2;
   public Setting<Double> doubleValueSetting5;

   public static HoleSnapModule getHoleSnapModule() {
      return holeSnapModule;
   }

   public boolean testHole(Hole hole) {
      return this.blockPos != null && hole != null && hole.getBlockPosSet().contains(this.blockPos);
   }

   @Override
   public void run3() {
      this.blockPos = null;
      if (mc.player != null && mc.world != null) {
         this.blockPos = BlockPos.ofFloored(mc.player.getPos());
      }

      if (this.booleanValueSetting6.getValue() && !StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run12();
      }

      this.normal.run();
      this.intValue2 = 0;
   }

   @Override
   public void run4() {
      this.blockPos = null;
      if (this.booleanValueSetting6.getValue() && StepModule.getStepModule().check7()) {
         StepModule.getStepModule().run11();
      }

      this.phobosClient.getTimerManager().run2();
      this.normal.run();
      this.intValue2 = 0;
   }

   public HoleSnapModule(PhobosClient phobosClient) {
      super(phobosClient, "HoleSnap", Categories.movement, "Drags you into the nearest hole");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      Double var3 = 5.0;
      Double var4 = 0.0;
      Double var5 = 20.0;
      NumberSetting var2 = new NumberSetting<>("Range", var3, var4, var5);
      Setting var6 = this.resolveSetting2(var2);
      this.doubleValueSetting = var6;
      HoleSnapModule.Mode var8 = HoleSnapModule.Mode.Timer;
      EnumSetting var7 = new EnumSetting<>("Mode", var8);
      Setting var9 = this.resolveSetting2(var7);
      this.modeSetting = var9;
      HoleSnapModule.Priority var11 = HoleSnapModule.Priority.Distance;
      EnumSetting var10 = new EnumSetting<>("Priority", var11);
      Setting var12 = this.resolveSetting2(var10);
      this.prioritySetting = var12;
      Double var14 = 2.0;
      Double var15 = 1.0;
      Double var16 = 15.0;
      NumberSetting var13 = new NumberSetting<>("Speed", var14, var15, var16);
      Setting var17 = this.modeSetting;
      HoleSnapModule.Mode[] var18 = new HoleSnapModule.Mode[1];
      HoleSnapModule.Mode var19 = HoleSnapModule.Mode.Timer;
      var18[0] = var19;
      Visibility var20 = this.resolveVisibility2(var17, var18);
      Setting var21 = var13.resolveSetting3(var20);
      Setting var22 = this.resolveSetting2(var21);
      this.doubleValueSetting2 = var22;
      BooleanSetting var23 = new BooleanSetting("Timeout");
      Setting var24 = var23.resolveSetting7("Automatically distance the module after some time");
      Setting var25 = this.resolveSetting2(var24);
      this.booleanValueSetting8 = var25;
      LongNumberSetting var26 = new LongNumberSetting("TimeoutTime", 500L, 250L, 2000L, true & true);
      Setting var27 = this.resolveSetting2(var26);
      this.longValueSetting = var27;
      BooleanSetting var28 = new BooleanSetting("Step");
      Setting var29 = var28.resolveSetting7("Use step");
      Setting var30 = this.resolveSetting2(var29);
      this.booleanValueSetting6 = var30;
      BooleanSetting var31 = new BooleanSetting("Switch");
      Setting var32 = var31.resolveSetting7("Swaps positions with a nearby hole");
      Setting var33 = this.resolveSetting2(var32);
      this.booleanValueSetting9 = var33;
      BooleanSetting var34 = new BooleanSetting("Lag");
      Setting var35 = var34.resolveSetting7("Performs lag");
      Setting var36 = this.resolveSetting2(var35);
      this.booleanValueSetting7 = var36;
      BooleanSetting var37 = new BooleanSetting("LagSwitch");
      Setting var38 = var37.resolveSetting7("Only lags when switching");
      Setting var39 = this.resolveSetting2(var38);
      this.booleanValueSetting2 = var39;
      Boolean var41 = true & true;
      BooleanSetting var40 = new BooleanSetting("LagDeploy", var41);
      Setting var42 = var40.resolveSetting7("Dispatch events when un-lagging");
      Setting var43 = this.resolveSetting2(var42);
      this.booleanValueSetting3 = var43;
      Double var45 = 0.5;
      Double var46 = 0.1;
      Double var47 = 5.0;
      NumberSetting var44 = new NumberSetting<>("LagFactor", var45, var46, var47);
      Setting var48 = this.resolveSetting2(var44);
      this.doubleValueSetting3 = var48;
      Integer var50 = 4;
      Integer var51 = 1;
      Integer var52 = 16;
      NumberSetting var49 = new NumberSetting<>("LagMax", var50, var51, var52);
      Setting var53 = this.resolveSetting2(var49);
      this.integerSetting = var53;
      Double var55 = 2.0;
      Double var56 = 1.0;
      Double var57 = 3.0;
      NumberSetting var54 = new NumberSetting<>("Drag", var55, var56, var57);
      Setting var58 = this.resolveSetting2(var54);
      this.doubleValueSetting4 = var58;
      Double var60 = 1.0;
      Double var61 = 0.5;
      Double var62 = 2.0;
      NumberSetting var59 = new NumberSetting<>("Acceleration", var60, var61, var62);
      Setting var63 = this.resolveSetting2(var59);
      this.doubleValueSetting5 = var63;
      BooleanSetting var64 = new BooleanSetting("Partial");
      Setting var65 = this.resolveSetting2(var64);
      this.booleanValueSetting = var65;
      BooleanSetting var66 = new BooleanSetting("Teleport");
      Setting var67 = var66.resolveSetting7("Teleports you through blocks vertically");
      Setting var68 = this.resolveSetting2(var67);
      this.booleanValueSetting4 = var68;
      BooleanSetting var69 = new BooleanSetting("Blacklist");
      Setting var70 = var69.resolveSetting7("Put current hole into a temporary blacklist");
      Setting var71 = this.resolveSetting2(var70);
      this.booleanValueSetting5 = var71;
      LongNumberSetting var72 = new LongNumberSetting("BlacklistTime", 1500L, 250L, 5000L);
      Setting var73 = this.resolveSetting2(var72);
      this.longValueSetting2 = var73;
      ConcurrentHashMap var74 = new ConcurrentHashMap();
      this.holeMap = var74;
      Normal var75 = new Normal();
      this.normal = var75;
      this.intValue = 0;
      this.intValue2 = 0;
      Builder var76 = this.createBuilder();
      Setting var77 = this.booleanValueSetting9;
      Builder var78 = var76.resolveBuilder6(var77);
      SettingGroup[] var79 = new SettingGroup[1];
      Setting var80 = this.booleanValueSetting5;
      Setting[] var81 = new Setting[1];
      Setting var82 = this.longValueSetting2;
      var81[0] = var82;
      SettingGroup var83 = this.resolveSettingGroup(var80, var81);
      var79[0] = var83;
      Builder var84 = var78.resolveBuilder5(var79);
      var84.getSettingSubsection();
      Setting var85 = this.booleanValueSetting7;
      Setting[] var86 = new Setting[4];
      Setting var87 = this.booleanValueSetting2;
      var86[0] = var87;
      Setting var88 = this.booleanValueSetting3;
      var86[1] = var88;
      Setting var89 = this.doubleValueSetting3;
      var86[2] = var89;
      Setting var90 = this.integerSetting;
      var86[3] = var90;
      this.resolveSettingGroup(var85, var86);
      Setting var91 = this.booleanValueSetting8;
      Setting[] var92 = new Setting[1];
      Setting var93 = this.longValueSetting;
      var92[0] = var93;
      this.resolveSettingGroup(var91, var92);
      var var94 = new SafeListener<PlayerTickEvent>() {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            HoleSnapModule.this.holeMap
               .entrySet()
               .removeIf(entry -> entry.getValue() + HoleSnapModule.this.longValueSetting2.getValue() < System.currentTimeMillis());
            boolean flag = var1.getSurroundManager().testPlayerEntity(clientPlayerEntity, true);
            if (flag) {
               if (!HoleSnapModule.this.booleanValueSetting9.getValue()) {
                  HoleSnapModule.this.run11();
                  return;
               }

               Hole hole = HoleSnapModule.this.resolveHole(
                  clientPlayerEntity,
                  holex -> holex.testWorld(clientWorld)
                     && holex.computeEntity(clientPlayerEntity) < MathHelper.square(HoleSnapModule.this.doubleValueSetting.getValue())
                     && (holex.isFlag22() || HoleSnapModule.this.booleanValueSetting.getValue())
               );
               if (!HoleSnapModule.this.testHole(hole)) {
                  HoleSnapModule.this.run11();
                  return;
               }
            }

            if (HoleSnapModule.this.booleanValueSetting8.getValue() && HoleSnapModule.this.normal.testNumber(HoleSnapModule.this.longValueSetting.getValue())) {
               HoleSnapModule.this.run11();
            } else if (HoleSnapModule.this.intValue2 > 5 && clientPlayerEntity.speed < 0.1) {
               HoleSnapModule.this.run11();
            } else if (clientPlayerEntity.speed < 0.01) {
               HoleSnapModule.this.run11();
            } else {
               Hole hole2 = HoleSnapModule.this.resolveHole(
                  clientPlayerEntity,
                  hole -> hole.testWorld(clientWorld)
                     && hole.computeEntity(clientPlayerEntity) < MathHelper.square(HoleSnapModule.this.doubleValueSetting.getValue())
                     && !HoleSnapModule.this.testHole(hole)
                     && (!HoleSnapModule.this.booleanValueSetting5.getValue() || !HoleSnapModule.this.holeMap.containsKey(hole))
                     && (hole.isFlag22() || HoleSnapModule.this.booleanValueSetting.getValue())
               );
               if (hole2 != null
                  && !clientPlayerEntity.isTouchingWater()
                  && !clientPlayerEntity.isClimbing()
                  && !PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
                  if (HoleSnapModule.this.booleanValueSetting7.getValue()
                     && !var1.getTickManager().check()
                     && (!HoleSnapModule.this.booleanValueSetting2.getValue() || HoleSnapModule.this.booleanValueSetting9.getValue() && flag)) {
                     int i = HoleSnapModule.this.booleanValueSetting2.getValue()
                        ? HoleSnapModule.this.integerSetting.getValue()
                        : MathUtils.computeInt3(
                           (int)Math.ceil(Math.sqrt(hole2.computeEntity(clientPlayerEntity)) / HoleSnapModule.this.doubleValueSetting3.getValue()),
                           0,
                           HoleSnapModule.this.integerSetting.getValue()
                        );
                     TickData tickData = TickData.createTickData(i);
                     if (HoleSnapModule.this.booleanValueSetting3.getValue()) {
                        tickData.getTickData();
                     }

                     var1.getTickManager().handleClientPlayerEntity3(clientPlayerEntity, tickData, Tick.Player);
                  }

                  Vec3d vec3d3 = clientPlayerEntity.getPos();
                  Vec3d vec3d4 = hole2.createVec3d();
                  Vec3d vec3d5 = new Vec3d(vec3d4.x, vec3d3.y, vec3d4.z);
                  double d8 = vec3d3.distanceTo(vec3d5);
                  double d9 = Math.toRadians(RotationUtils.resolveVector2d(vec3d3, vec3d5).x);
                  if (HoleSnapModule.this.booleanValueSetting4.getValue() && BlockPosUtils.testDouble(clientPlayerEntity.getY(), hole2.getIntValue4(), 2.0)) {
                     clientPlayerEntity.setPosition(vec3d5.getX(), hole2.getIntValue4() + 1.3652E-6, vec3d5.getZ());
                  }

                  switch ((HoleSnapModule.Mode)HoleSnapModule.this.modeSetting.getValue()) {
                     case Timer:
                        var1.getTimerManager().handleFloat(HoleSnapModule.this.doubleValueSetting2.getValue().floatValue(), TimerPriority.Max);
                        if (hole2.testEntity(clientPlayerEntity)) {
                           PlayerActionUtils.handleClientPlayerEntity13(clientPlayerEntity, 0.0, 0.0);
                        } else {
                           double d = PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) * HoleSnapModule.this.doubleValueSetting5.getValue();
                           double d2 = clientPlayerEntity.isOnGround() ? -Math.min(d, d8 / HoleSnapModule.this.doubleValueSetting4.getValue()) : -d;
                           Vec3d vec3d = clientPlayerEntity.getVelocity();
                           double d3 = -Math.sin(d9) * d2;
                           double d4 = Math.cos(d9) * d2;
                           clientPlayerEntity.setVelocity(d3, vec3d.y, d4);
                        }
                        break;
                     case Motion:
                        if (hole2.testEntity(clientPlayerEntity)) {
                           PlayerActionUtils.handleClientPlayerEntity13(clientPlayerEntity, 0.0, 0.0);
                        } else {
                           double d5 = -(PlayerUtils.computeClientPlayerEntity4(clientPlayerEntity) * HoleSnapModule.this.doubleValueSetting5.getValue());
                           Vec3d vec3d2 = clientPlayerEntity.getVelocity();
                           double d6 = -Math.sin(d9) * d5;
                           double d7 = Math.cos(d9) * d5;
                           clientPlayerEntity.setVelocity(d6, vec3d2.y, d7);
                        }
                        break;
                     case Grim:
                        if (!hole2.testEntity(clientPlayerEntity) && !RotationManager.isFlag()) {
                           float[] floats = RotationUtils.values2(clientPlayerEntity, vec3d5);
                           var1.getRotationManager().testFloat2(floats[0], floats[1], true);
                           HoleSnapModule.this.intValue = 1;
                        }
                  }

                  HoleSnapModule.this.intValue2 = clientPlayerEntity.horizontalCollision ? HoleSnapModule.this.intValue2 + 1 : 0;
               } else {
                  HoleSnapModule.this.run11();
               }
            }
         }
      };
      this.handleListener(var94);
      var var95 = new SafeListener<TickEvent>(16) {
         public void handleObject(
            TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (HoleSnapModule.this.modeSetting.testObject2(HoleSnapModule.Mode.Grim) && HoleSnapModule.this.intValue == 1) {
               tickEvent.getKeyboardInput().movementSideways = 0.0F;
               tickEvent.getKeyboardInput().movementForward = 1.0F;
               HoleSnapModule.this.intValue = 0;
            }
         }
      };
      this.handleListener(var95);
      holeSnapModule = this;
   }

   public Hole resolveHole(ClientPlayerEntity clientPlayerEntity, Predicate<Hole> predicate) {
      return switch ((HoleSnapModule.Priority)this.prioritySetting.getValue()) {
         case Distance -> this.phobosClient.getHoleChunkScanner().resolveHole3(clientPlayerEntity, predicate);
         case Quality -> this.phobosClient.getHoleChunkScanner().resolveHole2(predicate, hole -> {
            if (!hole.isFlag22()) {
               return 7.0;
            } else if (hole.isFlag4()) {
               return hole.isFlag6() ? 3.0 : (hole.isFlag() ? 1.0 : 0.0);
            } else {
               return hole.isFlag6() ? 6.0 : (hole.isFlag() ? 5.0 : 4.0);
            }
         }, hole -> hole.computeEntity(clientPlayerEntity));
      };
   }

   private enum Mode implements TextProvider {
      Timer("Speeds up the game"),
      Motion("Uses strafe for movement"),
      Grim("Legit movement");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Mode(final String description) {
         this.text = description;
      }
   }

   private enum Priority {
      Distance,
      Quality;
   }
}
