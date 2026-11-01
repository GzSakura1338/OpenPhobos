package org.phobos.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Visibility;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.AnimatedVec3d;
import org.phobos.core.render.RenderContext;
import org.phobos.core.combat.MaceAttackTask;
import org.phobos.core.bind.BindKey;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.module.TargetModule;
import org.phobos.core.math.DistanceUtils;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.event.AttackEvent2;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.phobos.module.client.ColorsModule;
import org.phobos.module.misc.MiddleClickModule;
import org.phobos.module.movement.FakeFlyModule;
import org.phobos.module.player.SilentHotbarModule;

public class AutoMaceModule extends TargetModule {
   public Setting<Double> doubleValueSetting;
   public Setting<Double> doubleValueSetting2;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<AutoMaceModule.Mode> modeSetting;
   public Setting<Float> floatValueSetting2;
   public Setting<Long> longValueSetting;
   public Setting<AutoMaceModule.Movement> movementSetting;
   public static AutoMaceModule autoMaceModule;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Long> longValueSetting2;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Float> floatValueSetting3;
   public Setting<Integer> integerSetting;
   public Setting<Double> doubleValueSetting3;
   public AutoMaceModule.DashRenderer dashRenderer;
   public MaceAttackTask maceAttackTask;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Color> colorSetting;
   public boolean flag;
   public AutoMaceModule.DashTarget dashTarget;
   public Setting<Float> floatValueSetting4;
   public Setting<Integer> integerSetting2;
   public Setting<Boolean> booleanValueSetting8;
   public LivingEntity livingEntity;
   public Normal normal;
   public Setting<Float> floatValueSetting5;
   public Normal normal2;
   public AutoMaceModule.DashTarget dashTarget2;
   public AutoMaceModule.Stage stage;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<KeyCombination> keyCombinationSetting;
   private static Object[] phobos$g1806e37e8;
   private static Object[] phobos$g1806e38d0;

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d, float f, float f2, double d, BiConsumer<BlockHitResult, Vec3d> biConsumer
   ) {
      Vec3d vec3d2 = vec3d.add(MathUtils.createVec3d4(f, f2).multiply(d));
      BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, FluidHandling.ANY, clientPlayerEntity));
      biConsumer.accept(blockHitResult, vec3d2);
   }

   public void handleClientPlayerEntity3(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d, float f, double d, BiFunction<BlockHitResult, Vec3d, Boolean> biFunction
   ) {
      float f4 = this.floatValueSetting5.getValue();

      for (float f3 = f - 35.0F; f3 < f + 35.0F; f3 += f4) {
         for (float f2 = -45.0F; f2 > -90.0F; f2 -= f4) {
            Vec3d vec3d2 = vec3d.add(MathUtils.createVec3d4(f3, f2).multiply(d));
            BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.COLLIDER, FluidHandling.ANY, clientPlayerEntity));
            if (biFunction.apply(blockHitResult, vec3d2)) {
               return;
            }
         }
      }
   }

   @Nullable
   public Vec3d resolveVec3d(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, MutableDouble mutableDouble, double d, float[] floats
   ) {
      if (clientPlayerEntity.isOnGround()) {
         return null;
      }

      double d2 = clientPlayerEntity.getY();
      double d3 = entity.getY();
      if (d2 >= d3) {
         return null;
      }

      if (d2 - d3 < 4.0) {
         return null;
      }

      MutableObject<BlockHitResult> mutableObject = new MutableObject<>();
      this.handleClientPlayerEntity5(
         clientPlayerEntity,
         clientWorld,
         clientPlayerEntity.getPos(),
         floats[0],
         floats[1],
         45.0F,
         45.0F,
         this.doubleValueSetting3.getValue(),
         (blockHitResultx, vec3d) -> {
            Vec3d vec3d2 = blockHitResultx.getPos();
            if (vec3d2 == null) {
               return false;
            }

            double d2x = entity.getBoundingBox().squaredMagnitude(vec3d2);
            if (d2x > d) {
               return false;
            }

            if (!this.testClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerEntity.getPos(), vec3d2)) {
               return false;
            }

            if (mutableObject.getValue() == null || d2x < mutableDouble.getValue()) {
               mutableObject.setValue(blockHitResultx);
               mutableDouble.setValue(d2x);
            }

            return false;
         }
      );
      BlockHitResult blockHitResult = mutableObject.getValue();
      return blockHitResult != null ? blockHitResult.getPos() : null;
   }

   public void handleClientPlayerEntity4(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      Vec3d vec3d,
      float f,
      float f2,
      double d,
      BiFunction<BlockHitResult, Vec3d, Boolean> biFunction
   ) {
      Vec3d vec3d2 = vec3d.add(MathUtils.createVec3d4(f, f2).multiply(d));
      BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.COLLIDER, FluidHandling.ANY, clientPlayerEntity));
      if (!biFunction.apply(blockHitResult, vec3d2)) {
         ;
      }
   }

   public void run5() {
      Setting var1 = this.booleanValueSetting;
      Setting[] var2 = new Setting[1];
      Setting var3 = this.longValueSetting2;
      var2[0] = var3;
      SettingGroup var4 = this.resolveSettingGroup(var1, var2);
      Setting var5 = this.modeSetting;
      AutoMaceModule.Mode var6 = AutoMaceModule.Mode.Legit;
      var4.resolveSettingGroup4(var5, var6);
      Setting var7 = this.booleanValueSetting3;
      Setting[] var8 = new Setting[1];
      Setting var9 = this.doubleValueSetting;
      var8[0] = var9;
      SettingGroup var10 = this.resolveSettingGroup(var7, var8);
      Setting var11 = this.modeSetting;
      AutoMaceModule.Mode var12 = AutoMaceModule.Mode.Attack;
      var10.resolveSettingGroup4(var11, var12);
      Setting var13 = this.booleanValueSetting9;
      Setting[] var14 = new Setting[2];
      Setting var15 = this.colorSetting;
      var14[0] = var15;
      Setting var16 = this.floatValueSetting4;
      var14[1] = var16;
      SettingGroup var17 = this.resolveSettingGroup(var13, var14);
      Setting var18 = this.modeSetting;
      AutoMaceModule.Mode var19 = AutoMaceModule.Mode.Dash;
      var17.resolveSettingGroup4(var18, var19);
      Setting var20 = this.booleanValueSetting8;
      Setting[] var21 = new Setting[2];
      Setting var22 = this.integerSetting2;
      var21[0] = var22;
      Setting var23 = this.booleanValueSetting7;
      var21[1] = var23;
      SettingGroup var24 = this.resolveSettingGroup(var20, var21);
      Setting var25 = this.modeSetting;
      AutoMaceModule.Mode[] var26 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var27 = AutoMaceModule.Mode.Dash;
      var26[0] = var27;
      AutoMaceModule.Mode var28 = AutoMaceModule.Mode.Continuous;
      var26[1] = var28;
      var24.resolveSettingGroup3(var25, var26);
      Setting var29 = this.getTargets();
      Setting var30 = this.modeSetting;
      AutoMaceModule.Mode[] var31 = new AutoMaceModule.Mode[4];
      AutoMaceModule.Mode var32 = AutoMaceModule.Mode.Rage;
      var31[0] = var32;
      AutoMaceModule.Mode var33 = AutoMaceModule.Mode.Legit;
      var31[1] = var33;
      AutoMaceModule.Mode var34 = AutoMaceModule.Mode.Dash;
      var31[2] = var34;
      AutoMaceModule.Mode var35 = AutoMaceModule.Mode.Continuous;
      var31[3] = var35;
      Visibility var36 = Visibility.resolveVisibility3(var30, var31);
      var29.resolveSetting3(var36);
   }

   public Entity resolveEntity(Entity entity, boolean flag) {
      if (!this.booleanValueSetting8.getValue()) {
         return entity;
      } else if (entity instanceof PlayerEntity playerEntity) {
         return !flag && this.booleanValueSetting7.getValue()
            ? entity
            : Objects.requireNonNullElse(
               this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.integerSetting2.getValue(), playerEntity), entity
            );
      } else {
         return entity;
      }
   }

   @Nullable
   public AutoMaceModule.DashTarget createDashTarget(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, float f) {
      Vec3d vec3d2 = clientPlayerEntity.getPos().add(0.0, PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) ? 0.0 : 1.5, 0.0);
      MutableDouble mutableDouble = new MutableDouble(0.0);
      MutableObject<BlockHitResult> mutableObject = new MutableObject<>();
      Entity entity2 = this.resolveEntity(entity, false);
      float[] floats = RotationUtils.values(clientPlayerEntity, entity);
      double d2 = AuraModule.getAuraModule().getDoubleValueSetting().getValue();
      double d3 = MathHelper.square(d2);
      Vec3d vec3d3 = this.resolveVec3d(clientPlayerEntity, clientWorld, entity2, mutableDouble, d3, floats);
      float f3 = clientPlayerEntity.fallDistance;
      if (vec3d3 != null) {
         Vec3d vec3d = vec3d2.add(vec3d3.subtract(vec3d2).multiply(0.5));
         float f2 = (float)(f3 + vec3d.distanceTo(vec3d3));
         double d = this.computeFloat(f2);
         return new AutoMaceModule.DashTarget(entity, d, vec3d2, vec3d, vec3d3);
      }

      mutableDouble.setValue(0.0);
      this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, vec3d2, floats[0], f, (blockHitResultx, vec3d2x) -> {
         Vec3d vec3d3x = blockHitResultx.getPos();
         if (vec3d3x == null) {
            return false;
         }

         if (!vec3d2x.equals(vec3d3x)) {
            return false;
         }

         if (!this.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d2, vec3d3x)) {
            return false;
         }

         double d = vec3d2.distanceTo(vec3d3x);
         if (mutableObject.getValue() == null || d > mutableDouble.getValue()) {
            mutableObject.setValue(blockHitResultx);
            mutableDouble.setValue(d);
         }

         return false;
      });
      BlockHitResult blockHitResult = mutableObject.getValue();
      if (blockHitResult == null) {
         return null;
      }

      Vec3d vec3d4 = blockHitResult.getPos();
      float[] floats2 = RotationUtils.values6(
         vec3d4.getX(), vec3d4.getY(), vec3d4.getZ(), entity2.getX(), entity2.getY(), entity2.getZ(), clientPlayerEntity.prevYaw
      );
      mutableDouble.setValue(0.0);
      MutableObject<BlockHitResult> mutableObject2 = new MutableObject<>();
      this.handleClientPlayerEntity5(clientPlayerEntity, clientWorld, vec3d4, floats2[0], floats2[1], 30.0F, 20.0F, f * 1.25, (blockHitResultx, vec3d2x) -> {
         Vec3d vec3d3x = blockHitResultx.getPos();
         if (vec3d3x == null) {
            return false;
         }

         double d2x = entity2.getBoundingBox().squaredMagnitude(vec3d3x);
         if (d2x > d3) {
            return false;
         }

         if (!this.testClientPlayerEntity(clientPlayerEntity, clientWorld, vec3d4, vec3d3x)) {
            return false;
         }

         if (mutableObject2.getValue() == null || d2x < mutableDouble.getValue()) {
            mutableObject2.setValue(blockHitResultx);
            mutableDouble.setValue(d2x);
         }

         return false;
      });
      if (mutableObject2.getValue() == null) {
         return null;
      }

      Vec3d vec3d5 = mutableObject2.getValue().getPos();
      float f4 = (float)(f3 + vec3d4.distanceTo(vec3d5));
      double d4 = this.computeFloat(f4);
      return new AutoMaceModule.DashTarget(entity, d4, vec3d2, vec3d4, vec3d5);
   }

   public boolean testBox(Box box, World world) {
      return BlockPosUtils.resolveSet6(box).stream().anyMatch(blockPos -> BlockUtils.testBlockPos7(blockPos, world));
   }

   public int computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, LivingEntity livingEntity) {
      float f = EntityUtils.computeLivingEntity(livingEntity);
      float f2 = clientPlayerEntity.fallDistance;

      for (int i = 14; i < 100; i += 2) {
         double d = this.computeFloat(f2 + i);
         if (d >= f) {
            return i;
         }
      }

      return 20;
   }

   @Override
   public void run3() {
      this.run2();
   }

   public void run2() {
      this.maceAttackTask.run();
      this.livingEntity = null;
      this.dashRenderer.handleDashTarget(null);
      this.dashTarget = null;
      this.dashTarget2 = null;
   }

   public void handleMode(AutoMaceModule.Mode mode) {
      this.run2();
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d, Vec3d vec3d2, EntityPose... entityPoses) {
      Box box2 = clientPlayerEntity.getBoundingBox(entityPoses.length == 0 ? EntityPose.FALL_FLYING : entityPoses[0]);
      Vec3d vec3d4 = vec3d2.subtract(vec3d);
      double d2 = vec3d4.length();
      if (d2 == 0.0) {
         return !this.testBox(box2.offset(vec3d), clientWorld);
      }

      Vec3d vec3d5 = vec3d4.normalize();
      double d3 = box2.getAverageSideLength() / 2.0;

      for (double d = 0.0; d <= d2; d += d3) {
         Vec3d vec3d3 = vec3d.add(vec3d5.multiply(Math.min(d, d2)));
         Box box = box2.offset(vec3d3);
         if (this.testBox(box, clientWorld)) {
            return false;
         }
      }

      return true;
   }

   public double computeFloat(float f) {
      float f2;
      if (f <= 3.0F) {
         f2 = 4.0F * f;
      } else if (f <= 8.0F) {
         f2 = 12.0F + 2.0F * (f - 3.0F);
      } else {
         f2 = 22.0F + f - 8.0F;
      }

      return f2;
   }

   public Entity resolveEntity2(Entity entity) {
      if (!this.booleanValueSetting8.getValue()) {
         return entity;
      } else {
         return entity instanceof PlayerEntity playerEntity
            ? Objects.requireNonNullElse(
               this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.integerSetting2.getValue(), playerEntity), entity
            )
            : entity;
      }
   }

   public void handleClientPlayerEntity5(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      Vec3d vec3d,
      float f,
      float f2,
      float f3,
      float f4,
      double d,
      BiFunction<BlockHitResult, Vec3d, Boolean> biFunction
   ) {
      float f7 = this.floatValueSetting5.getValue();

      for (float f6 = f - f3; f6 < f + f3; f6 += f7) {
         for (float f5 = f2 + f4; f5 > f2 - f4; f5 -= f7) {
            Vec3d vec3d2 = vec3d.add(MathUtils.createVec3d4(f6, f5).multiply(d));
            BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, FluidHandling.ANY, clientPlayerEntity));
            if (biFunction.apply(blockHitResult, vec3d2)) {
               return;
            }
         }
      }
   }

   @Override
   public void run4() {
      if (this.stage != AutoMaceModule.Stage.None && FakeFlyModule.getFakeFlyModule().check7()) {
         FakeFlyModule.getFakeFlyModule().run11();
      }

      this.stage = AutoMaceModule.Stage.None;
      this.run2();
   }

   @Override
   public String nothing() {
      return switch ((AutoMaceModule.Mode)this.modeSetting.getValue()) {
         case Continuous -> this.livingEntity != null ? this.livingEntity.getNameForScoreboard() : null;
         case Rage -> "Legit";
         case Dash -> "Attack";
         case Attack -> this.dashTarget2 != null
            ? "%s, %s".formatted(this.dashTarget2.getEntity().getNameForScoreboard(), MathUtils.computeDouble(this.dashTarget2.getDoubleValue(), 2))
            : (
               this.dashTarget != null
                  ? "%s, %s, %s"
                     .formatted(
                        this.dashTarget.getEntity().getNameForScoreboard(),
                        MathUtils.computeDouble(this.dashTarget.getDoubleValue(), 2),
                        MathUtils.computeDouble(this.dashTarget.computeClientPlayerEntity(mc.player), 2)
                     )
                  : null
            );
         case Legit -> this.maceAttackTask.describe();
      };
   }

   @Nullable
   public AutoMaceModule.DashTarget resolveDashTarget(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity) {
      AutoMaceModule.DashTarget dashTarget = this.createDashTarget(clientPlayerEntity, clientWorld, entity, this.floatValueSetting3.getValue());
      if (dashTarget != null) {
         return dashTarget;
      }

      AutoMaceModule.DashTarget dashTarget2 = this.createDashTarget(clientPlayerEntity, clientWorld, entity, this.floatValueSetting2.getValue());
      return dashTarget2 != null ? dashTarget2 : this.createDashTarget(clientPlayerEntity, clientWorld, entity, this.floatValueSetting.getValue());
   }

   public int computeClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, LivingEntity livingEntity) {
      return this.integerSetting.testObject2(0) ? this.computeClientPlayerEntity(clientPlayerEntity, livingEntity) : this.integerSetting.getValue();
   }

   @Override
   public boolean testLivingEntity(LivingEntity livingEntity) {
      return this.modeSetting.testObject2(AutoMaceModule.Mode.Legit) ? !livingEntity.isFallFlying() : super.testLivingEntity(livingEntity);
   }

   public static boolean check() {
      return autoMaceModule.check7() && autoMaceModule.modeSetting.testObject2(AutoMaceModule.Mode.Rage);
   }

   public static AutoMaceModule getAutoMaceModule() {
      return autoMaceModule;
   }

   public AutoMaceModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoMace", Categories.combat, "Mace combat");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      AutoMaceModule.Mode var3 = AutoMaceModule.Mode.Rage;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      AutoMaceModule.Movement var6 = AutoMaceModule.Movement.Vertical;
      EnumSetting var5 = new EnumSetting<>("Movement", var6);
      Setting var7 = this.modeSetting;
      AutoMaceModule.Mode var8 = AutoMaceModule.Mode.Continuous;
      Setting var9 = var5.resolveSetting4(var7, var8);
      Setting var10 = this.resolveSetting2(var9);
      this.movementSetting = var10;
      BooleanSetting var11 = new BooleanSetting("Post");
      Setting var12 = var11.resolveSetting7("After attack gets an elytra and gives you fireworks");
      Setting var13 = this.resolveSetting2(var12);
      this.booleanValueSetting = var13;
      LongNumberSetting var14 = new LongNumberSetting("PostDelay", 500L, 50L, 2000L, true & true);
      Setting var15 = this.resolveSetting2(var14);
      this.longValueSetting2 = var15;
      BooleanSetting var16 = new BooleanSetting("Switch");
      Setting var17 = var16.resolveSetting7("Switches to mace after swapping");
      Setting var18 = this.modeSetting;
      AutoMaceModule.Mode var19 = AutoMaceModule.Mode.Legit;
      Setting var20 = var17.resolveSetting4(var18, var19);
      Setting var21 = this.resolveSetting2(var20);
      this.booleanValueSetting4 = var21;
      Double var23 = 4.0;
      Double var24 = 0.0;
      Double var25 = 6.0;
      NumberSetting var22 = new NumberSetting<>("Range", var23, var24, var25);
      Setting var26 = var22.resolveSetting7("Target range");
      Setting var27 = this.modeSetting;
      AutoMaceModule.Mode[] var28 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var29 = AutoMaceModule.Mode.Legit;
      var28[0] = var29;
      AutoMaceModule.Mode var30 = AutoMaceModule.Mode.Rage;
      var28[1] = var30;
      Visibility var31 = Visibility.resolveVisibility3(var27, var28);
      Setting var32 = var26.resolveSetting3(var31);
      Setting var33 = this.resolveSetting2(var32);
      this.doubleValueSetting3 = var33;
      LongNumberSetting var34 = new LongNumberSetting("Delay", 0L, 0L, 2000L);
      Setting var35 = this.modeSetting;
      AutoMaceModule.Mode[] var36 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var37 = AutoMaceModule.Mode.Rage;
      var36[0] = var37;
      AutoMaceModule.Mode var38 = AutoMaceModule.Mode.Attack;
      var36[1] = var38;
      Visibility var39 = Visibility.resolveVisibility3(var35, var36);
      Setting var40 = var34.resolveSetting3(var39);
      Setting var41 = this.resolveSetting2(var40);
      this.longValueSetting = var41;
      Integer var43 = 10;
      Integer var44 = 0;
      Integer var45 = 170;
      NumberSetting var42 = new NumberSetting<>("Height", var43, var44, var45);
      Integer var46 = 170;
      NumberSetting var47 = var42.resolveNumberSetting("Max", var46);
      Integer var48 = 0;
      NumberSetting var49 = var47.resolveNumberSetting("Lethal", var48);
      Setting var50 = this.modeSetting;
      AutoMaceModule.Mode[] var51 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var52 = AutoMaceModule.Mode.Rage;
      var51[0] = var52;
      AutoMaceModule.Mode var53 = AutoMaceModule.Mode.Continuous;
      var51[1] = var53;
      Setting var54 = var49.resolveSetting8(var50, (Object[])var51);
      Setting var55 = this.resolveSetting2(var54);
      this.integerSetting = var55;
      BooleanSetting var56 = new BooleanSetting("Wind");
      Setting var57 = var56.resolveSetting7("Use wind charges to avoid excessive fall damage");
      Setting var58 = this.modeSetting;
      AutoMaceModule.Mode var59 = AutoMaceModule.Mode.Rage;
      Setting var60 = var57.resolveSetting4(var58, var59);
      Setting var61 = this.resolveSetting2(var60);
      this.booleanValueSetting2 = var61;
      BooleanSetting var62 = new BooleanSetting("Factorize");
      Setting var63 = var62.resolveSetting7("Uses exponential damage to deal damage without delay");
      Setting var64 = this.modeSetting;
      AutoMaceModule.Mode var65 = AutoMaceModule.Mode.Rage;
      Setting var66 = var63.resolveSetting4(var64, var65);
      Setting var67 = this.resolveSetting2(var66);
      this.booleanValueSetting5 = var67;
      BooleanSetting var68 = new BooleanSetting("Stun");
      Setting var69 = this.resolveSetting2(var68);
      this.booleanValueSetting3 = var69;
      Double var71 = 30.0;
      Double var72 = 1.0;
      Double var73 = 100.0;
      NumberSetting var70 = new NumberSetting<>("StunChance", var71, var72, var73);
      Setting var74 = this.resolveSetting2(var70);
      this.doubleValueSetting = var74;
      BooleanSetting var75 = new BooleanSetting("Swing");
      Setting var76 = this.modeSetting;
      AutoMaceModule.Mode[] var77 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var78 = AutoMaceModule.Mode.Rage;
      var77[0] = var78;
      AutoMaceModule.Mode var79 = AutoMaceModule.Mode.Legit;
      var77[1] = var79;
      Setting var80 = var75.resolveSetting8(var76, (Object[])var77);
      Setting var81 = this.resolveSetting2(var80);
      this.booleanValueSetting6 = var81;
      Double var83 = 60.0;
      Double var84 = 30.0;
      Double var85 = 100.0;
      NumberSetting var82 = new NumberSetting<>("Distance", var83, var84, var85);
      Setting var86 = this.modeSetting;
      AutoMaceModule.Mode[] var87 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var88 = AutoMaceModule.Mode.Dash;
      var87[0] = var88;
      AutoMaceModule.Mode var89 = AutoMaceModule.Mode.Continuous;
      var87[1] = var89;
      Setting var90 = var82.resolveSetting8(var86, (Object[])var87);
      Setting var91 = this.resolveSetting2(var90);
      this.doubleValueSetting2 = var91;
      Float var93 = (float)8.0F;
      Float var94 = (float)0.1F;
      Float var95 = (float)15.0F;
      NumberSetting var92 = new NumberSetting<>("Low", var93, var94, var95);
      Setting var96 = this.modeSetting;
      AutoMaceModule.Mode var97 = AutoMaceModule.Mode.Dash;
      Setting var98 = var92.resolveSetting4(var96, var97);
      Setting var99 = this.resolveSetting2(var98);
      this.floatValueSetting = var99;
      Float var101 = (float)25.0F;
      Float var102 = (float)15.0F;
      Float var103 = (float)35.0F;
      NumberSetting var100 = new NumberSetting<>("Mid", var101, var102, var103);
      Setting var104 = this.modeSetting;
      AutoMaceModule.Mode var105 = AutoMaceModule.Mode.Dash;
      Setting var106 = var100.resolveSetting4(var104, var105);
      Setting var107 = this.resolveSetting2(var106);
      this.floatValueSetting2 = var107;
      Float var109 = (float)45.0F;
      Float var110 = (float)35.0F;
      Float var111 = (float)50.0F;
      NumberSetting var108 = new NumberSetting<>("High", var109, var110, var111);
      Setting var112 = this.modeSetting;
      AutoMaceModule.Mode var113 = AutoMaceModule.Mode.Dash;
      Setting var114 = var108.resolveSetting4(var112, var113);
      Setting var115 = this.resolveSetting2(var114);
      this.floatValueSetting3 = var115;
      Float var117 = (float)5.0F;
      Float var118 = (float)2.0F;
      Float var119 = (float)10.0F;
      NumberSetting var116 = new NumberSetting<>("Step", var117, var118, var119);
      Setting var120 = this.modeSetting;
      AutoMaceModule.Mode[] var121 = new AutoMaceModule.Mode[2];
      AutoMaceModule.Mode var122 = AutoMaceModule.Mode.Dash;
      var121[0] = var122;
      AutoMaceModule.Mode var123 = AutoMaceModule.Mode.Continuous;
      var121[1] = var123;
      Setting var124 = var116.resolveSetting8(var120, (Object[])var121);
      Setting var125 = this.resolveSetting2(var124);
      this.floatValueSetting5 = var125;
      BindSetting var126 = new BindSetting("Key");
      Setting var127 = this.modeSetting;
      AutoMaceModule.Mode var128 = AutoMaceModule.Mode.Dash;
      Setting var129 = var126.resolveSetting4(var127, var128);
      Setting var130 = this.resolveSetting2(var129);
      this.keyCombinationSetting = var130;
      BooleanSetting var131 = new BooleanSetting("Render");
      Setting var132 = this.resolveSetting2(var131);
      this.booleanValueSetting9 = var132;
      Color var134 = new Color(255, 255, 255, 100);
      ColorSetting var133 = new ColorSetting("RenderColor", var134);
      ColorSetting var135 = var133.getColorSetting();
      Setting var136 = this.resolveSetting2(var135);
      this.colorSetting = var136;
      Float var138 = (float)1.0F;
      Float var139 = (float)0.1F;
      Float var140 = (float)3.0F;
      NumberSetting var137 = new NumberSetting<>("RenderWidth", var138, var139, var140);
      Setting var141 = this.resolveSetting2(var137);
      this.floatValueSetting4 = var141;
      BooleanSetting var142 = new BooleanSetting("Prediction");
      Setting var143 = this.resolveSetting2(var142);
      this.booleanValueSetting8 = var143;
      Integer var145 = 1;
      Integer var146 = 1;
      Integer var147 = 5;
      NumberSetting var144 = new NumberSetting<>("PredictionTicks", var145, var146, var147);
      Setting var148 = this.resolveSetting2(var144);
      this.integerSetting2 = var148;
      BooleanSetting var149 = new BooleanSetting("PredictionActive");
      Setting var150 = var149.resolveSetting7("Only uses prediction when recalculating");
      Setting var151 = this.resolveSetting2(var150);
      this.booleanValueSetting7 = var151;
      Normal var152 = new Normal();
      this.normal2 = var152;
      Normal var153 = new Normal();
      this.normal = var153;
      AutoMaceModule.DashRenderer var154 = new AutoMaceModule.DashRenderer();
      this.dashRenderer = var154;
      MaceAttackTask var155 = new MaceAttackTask(this);
      this.maceAttackTask = var155;
      AutoMaceModule.Stage var156 = AutoMaceModule.Stage.None;
      this.stage = var156;
      this.run5();
      var var157 = new SafeListener<OnMouseButtonEvent>() {
         public void handleObject(
            OnMouseButtonEvent onMouseButtonEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (onMouseButtonEvent.getIntValue() == 1 && onMouseButtonEvent.check3()) {
               BindKey bindKey = onMouseButtonEvent.resolveBindKey(var1);
               if (bindKey != null) {
                  if (AutoMaceModule.this.keyCombinationSetting.getValue().testBindKey(bindKey)) {
                     AutoMaceModule.this.dashTarget = AutoMaceModule.this.dashTarget2;
                     AutoMaceModule.this.dashRenderer.handleDashTarget(null);
                     AutoMaceModule.this.dashTarget2 = null;
                     AutoMaceModule.this.dashRenderer.floatAnimator.setFloatValue2(0.0F);
                     AutoMaceModule.this.dashRenderer.handleDashTarget(AutoMaceModule.this.dashTarget);
                  }
               }
            }
         }
      };
      this.handleListener(var157);
      var var158 = new SafeListener<PlayerTickEvent>(3) {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((AutoMaceModule.Mode)AutoMaceModule.this.modeSetting.getValue()) {
               case Continuous:
                  AutoMaceModule.this.handleClientPlayerEntity2(
                     clientPlayerEntity,
                     clientWorld,
                     AutoMaceModule.this.doubleValueSetting3.getValue(),
                     livingEntity -> {
                        AutoMaceModule.this.livingEntity = livingEntity;
                        DamageSource damageSource;
                        if ((
                              !AutoMaceModule.this.livingEntity.isBlocking()
                                 || (damageSource = AutoMaceModule.this.livingEntity.getRecentDamageSource()) == null
                                 || !AutoMaceModule.this.livingEntity.blockedByShield(damageSource)
                           )
                           && !AutoMaceModule.this.livingEntity.isInCreativeMode()
                           && !AutoMaceModule.this.livingEntity.isInvulnerable()) {
                           if (AutoMaceModule.this.normal.testNumber(AutoMaceModule.this.longValueSetting.getValue())) {
                              var1.getInventoryManager().handleConsumer2(itemSwitcher -> {
                                 if (itemSwitcher.testItem2(Items.MACE)) {
                                    if (AutoMaceModule.this.booleanValueSetting2.getValue()) {
                                       if (itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.WIND_CHARGE, 1) == null) {
                                          return;
                                       }

                                       TickUtils.handleClientWorld(clientWorld, integer -> {
                                          var1.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, 0.0F, 90.0F);
                                          clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, 0.0F, 90.0F));
                                          var1.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                                       });
                                    }

                                    if (itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.MACE, 1) != null) {
                                       if (AutoMaceModule.this.booleanValueSetting5.getValue()) {
                                          int[] ints = new int[]{30, 60, 113, 152, 171, 218, 1000, 1000};
                                          PlayerActionUtils.handleClientPlayerEntity16(clientPlayerEntity, 4, false);

                                          for (int i : ints) {
                                             PlayerActionUtils.handleClientPlayerEntity23(clientPlayerEntity, clientWorld, i);
                                             clientPlayerInteractionManager.attackEntity(clientPlayerEntity, AutoMaceModule.this.livingEntity);
                                             if (AutoMaceModule.this.booleanValueSetting6.getValue()) {
                                                clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                                             }

                                             AutoMaceModule.this.normal.run();
                                          }

                                          clientPlayerEntity.onLanding();
                                          return;
                                       }

                                       PlayerActionUtils.handleClientPlayerEntity20(clientPlayerEntity, clientWorld, AutoMaceModule.this.integerSetting.getValue());
                                       PlayerActionUtils.handleClientPlayerEntity23(clientPlayerEntity, clientWorld, AutoMaceModule.this.integerSetting.getValue());
                                       clientPlayerInteractionManager.attackEntity(clientPlayerEntity, AutoMaceModule.this.livingEntity);
                                       if (AutoMaceModule.this.booleanValueSetting6.getValue()) {
                                          clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                                       }

                                       clientPlayerEntity.onLanding();
                                       AutoMaceModule.this.normal.run();
                                    }
                                 }
                              });
                           }
                        }
                     },
                     () -> AutoMaceModule.this.livingEntity = null
                  );
                  break;
               case Rage:
                  var1.getInventoryManager()
                     .handleConsumer2(
                        itemSwitcher -> {
                           if (clientPlayerEntity.isFallFlying()) {
                              if (clientPlayerEntity.getInventory().getArmorStack(2).isOf(Items.ELYTRA) && itemSwitcher.testItem(Items.MACE)) {
                                 if (!(clientPlayerEntity.getPitch(TimerManager.getFloatValue()) < 0.0F)) {
                                    if (clientPlayerEntity.getFallFlyingTicks() >= 10) {
                                       AutoMaceModule.this.handleClientPlayerEntity2(
                                          clientPlayerEntity,
                                          clientWorld,
                                          AutoMaceModule.this.doubleValueSetting3.getValue(),
                                          livingEntity -> {
                                             double d = DistanceUtils.computeClientPlayerEntity(clientPlayerEntity, livingEntity);
                                             if (!(d > AutoMaceModule.this.doubleValueSetting3.getValue())) {
                                                Slot slotx = itemSwitcher.resolveObject(
                                                   slotxx -> itemSwitcher.testSlot4(slotxx)
                                                         && slotxx.getStack().getItem() instanceof ArmorItem armorItem
                                                         && armorItem.getSlotType().equals(EquipmentSlot.CHEST)
                                                      ? slotxx
                                                      : null
                                                );
                                                if (slotx != null) {
                                                   SilentHotbarModule.getSilentHotbarManager()
                                                      .handleObject3(
                                                         AutoMaceModule.this,
                                                         slotx,
                                                         1,
                                                         SilentHotbarModule.getSilentHotbarManager()
                                                            .getBuilder()
                                                            .resolveBuilder4(
                                                               () -> {
                                                                  if (AutoMaceModule.this.booleanValueSetting4.getValue()) {
                                                                     var1.getInventoryManager()
                                                                        .handleConsumer2(
                                                                           itemSwitcherxx -> {
                                                                              Slot slotxx = itemSwitcherxx.resolveObject(
                                                                                 slotxxx -> itemSwitcherxx.testSlot4(slotxxx)
                                                                                       && slotxxx.getStack().isOf(Items.MACE)
                                                                                    ? slotxxx
                                                                                    : null
                                                                              );
                                                                              if (slotxx != null) {
                                                                                 itemSwitcherxx.resolveSwitchResult(Hand.MAIN_HAND, slotxx, 2);
                                                                              }
                                                                           }
                                                                        );
                                                                  }

                                                                  AutoMaceModule.this.normal2.run();
                                                                  AutoMaceModule.this.flag = true;
                                                               }
                                                            )
                                                            .getSelectionConfig()
                                                      );
                                                   clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
                                                   if (AutoMaceModule.this.booleanValueSetting6.getValue()) {
                                                      clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                                                   }
                                                }
                                             }
                                          }
                                       );
                                    }
                                 }
                              }
                           } else if (AutoMaceModule.this.flag) {
                              if (AutoMaceModule.this.normal.testNumber(AutoMaceModule.this.longValueSetting2.getValue())) {
                                 AutoMaceModule.this.flag = false;
                                 Slot slot = itemSwitcher.resolveObject(
                                    slotx -> itemSwitcher.testSlot4(slotx) && slotx.getStack().isOf(Items.ELYTRA) ? slotx : null
                                 );
                                 if (slot == null) {
                                    return;
                                 }

                                 SilentHotbarModule.getSilentHotbarManager()
                                    .handleObject3(
                                       AutoMaceModule.this,
                                       slot,
                                       1,
                                       SilentHotbarModule.getSilentHotbarManager()
                                          .getBuilder()
                                          .resolveBuilder4(
                                             () -> var1.getInventoryManager()
                                                .handleConsumer2(
                                                   itemSwitcherx -> {
                                                      Slot slotx = itemSwitcherx.resolveObject(
                                                         slotxx -> itemSwitcherx.testSlot4(slotxx) && slotxx.getStack().isOf(Items.FIREWORK_ROCKET)
                                                            ? slotxx
                                                            : null
                                                      );
                                                      if (AutoMaceModule.this.booleanValueSetting4 != null) {
                                                         itemSwitcherx.resolveSwitchResult(Hand.MAIN_HAND, slotx, 2);
                                                      }
                                                   }
                                                )
                                          )
                                          .getSelectionConfig()
                                    );
                                 clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
                              }
                           }
                        }
                     );
                  break;
               case Dash:
                  if (clientPlayerEntity.isFallFlying()
                     || clientPlayerEntity.fallDistance < 3.0F
                     || var1.getRotationDelay().check()
                     || mc.currentScreen != null) {
                     return;
                  }

                  var1.getInventoryManager()
                     .handleConsumer2(
                        itemSwitcher -> itemSwitcher.handleRunnable3(
                           () -> {
                              if (AutoMaceModule.this.normal.testNumber(AutoMaceModule.this.longValueSetting.getValue())) {
                                 if (mc.crosshairTarget instanceof EntityHitResult entityHitResult) {
                                    if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                                       Slot slot2 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.MACE) ? slotx : null);
                                       if (slot2 != null) {
                                          if (livingEntity.isBlocking()
                                             && EntityUtils.testClientPlayerEntity(clientPlayerEntity, livingEntity)
                                             && AutoMaceModule.this.booleanValueSetting3.getValue()
                                             && MathUtils.testSupplier2(AutoMaceModule.this.doubleValueSetting)) {
                                             Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().getItem() instanceof AxeItem ? slotx : null);
                                             if (slot != null) {
                                                SilentHotbarModule.getSilentHotbarManager()
                                                   .handleObject3(
                                                      AutoMaceModule.this,
                                                      slot,
                                                      2,
                                                      SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                                   );
                                                var1.getInteractionManager().handleClientPlayerEntity(clientPlayerEntity, livingEntity);
                                                return;
                                             }
                                          }

                                          SilentHotbarModule.getSilentHotbarManager()
                                             .handleObject3(
                                                AutoMaceModule.this,
                                                slot2,
                                                2,
                                                SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                                             );
                                          var1.getInteractionManager().handleClientPlayerEntity(clientPlayerEntity, livingEntity);
                                          AutoMaceModule.this.normal.run();
                                       }
                                    }
                                 }
                              }
                           }
                        )
                     );
                  break;
               case Attack:
                  if (AutoMaceModule.this.dashTarget == null) {
                     var1.getInventoryManager()
                        .handleConsumer2(
                           itemSwitcher -> {
                              Slot slot = itemSwitcher.resolveObject(slotx -> InventoryUtils.testItemStack2(slotx.getStack()) ? slotx : null);
                              Slot slot2 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.ELYTRA) ? slotx : null);
                              Slot slot3 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.FIREWORK_ROCKET) ? slotx : null);
                              if (slot != null && slot2 != null && slot3 != null) {
                                 MutableBoolean mutableBoolean = new MutableBoolean(false);
                                 AutoMaceModule.this.handleClientPlayerEntity2(
                                    clientPlayerEntity, clientWorld, AutoMaceModule.this.doubleValueSetting2.getValue(), livingEntity -> {
                                       AutoMaceModule.this.dashTarget2 = AutoMaceModule.this.resolveDashTarget(clientPlayerEntity, clientWorld, livingEntity);
                                       AutoMaceModule.this.dashRenderer.handleDashTarget(AutoMaceModule.this.dashTarget2);
                                       mutableBoolean.setTrue();
                                    }
                                 );
                                 if (!mutableBoolean.getValue()) {
                                    AutoMaceModule.this.dashRenderer.handleDashTarget(null);
                                 }
                              } else {
                                 AutoMaceModule.this.dashRenderer.handleDashTarget(null);
                                 AutoMaceModule.this.dashTarget2 = null;
                              }
                           }
                        );
                     return;
                  }

                  AutoMaceModule.this.dashTarget.handleClientPlayerEntity(clientPlayerEntity, clientWorld, AutoMaceModule.this);
                  AutoMaceModule.this.dashRenderer.handleDashTarget(AutoMaceModule.this.dashTarget);
                  if (AutoMaceModule.this.dashTarget.isCompleted()) {
                     AutoMaceModule.this.dashRenderer.handleDashTarget(null);
                     AutoMaceModule.this.dashTarget = null;
                     return;
                  }

                  AutoMaceModule.this.dashTarget.handlePhobosClient(var1, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
                  if (AutoMaceModule.this.dashTarget.isCompleted()) {
                     AutoMaceModule.this.dashRenderer.handleDashTarget(null);
                     AutoMaceModule.this.dashTarget = null;
                     return;
                  }
                  break;
               case Legit:
                  AutoMaceModule.this.handleClientPlayerEntity2(
                     clientPlayerEntity,
                     clientWorld,
                     100.0,
                     livingEntity -> AutoMaceModule.this.maceAttackTask
                        .handleClientPlayerEntity2(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, livingEntity),
                     AutoMaceModule.this.maceAttackTask::run
                  );
            }
         }
      };
      this.handleListener(var158);
      var var159 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (AutoMaceModule.this.booleanValueSetting9.getValue()) {
               AutoMaceModule.this.dashRenderer.run();
               Color color2 = AutoMaceModule.this.colorSetting.getValue();
               float f = AutoMaceModule.this.dashRenderer.floatAnimator.getFloatValue2();
               if (AutoMaceModule.this.dashTarget != null && !AutoMaceModule.this.dashTarget.isCompleted()) {
                  Color color = ColorUtils.createColor6(color2, color2.getAlpha() / 255.0F * f);
                  Vec3d[] vec3ds = AutoMaceModule.this.dashRenderer.values();
                  Vec3d vec3d = vec3ds[2];
                  int i = AutoMaceModule.this.dashTarget.getEntity().getId();
                  Box box = new Box(vec3d, vec3d).expand(0.5, 0.0, 0.5);
                  Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.testObject(i));
                  Render3d.handleCuboid(
                     Cuboid.createBuilder()
                        .resolveBuilder11(box)
                        .resolveBuilder18(ColorsModule.color, color)
                        .resolveBuilder12(AutoMaceModule.this.floatValueSetting4.getValue())
                        .resolveBuilder14(i)
                        .resolveBuilder5(500L)
                        .getCuboid()
                  );
               } else if (!(f <= 0.05F)) {
                  Color color3 = ColorUtils.createColor6(color2, color2.getAlpha() / 255.0F * f);
                  List<Line> list = new ArrayList<>();
                  Vec3d[] vec3ds2 = AutoMaceModule.this.dashRenderer.values();
                  list.add(new Line(EntityInterpolation.createVec3d3(clientPlayerEntity, 1.25, renderContext.getFloatValue()), vec3ds2[1], color3));
                  list.add(new Line(vec3ds2[1], vec3ds2[2], color3));
                  Render3d.handleLines(
                     Lines.createBuilder().resolveBuilder4(list).resolveBuilder2(AutoMaceModule.this.floatValueSetting4.getValue()).getLines()
                  );
               }
            }
         }
      };
      this.handleListener(var159);
      var var160 = new SafeListener<TickEvent>() {
         public void handleObject(
            TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            switch ((AutoMaceModule.Mode)AutoMaceModule.this.modeSetting.getValue()) {
               case Attack:
                  if (AutoMaceModule.this.dashTarget == null) {
                     return;
                  }

                  if (AutoMaceModule.this.dashTarget.flag3) {
                     tickEvent.getKeyboardInput().jumping = true;
                     AutoMaceModule.this.dashTarget.flag3 = false;
                  }
                  break;
               case Legit:
                  AutoMaceModule.this.maceAttackTask.handleTickEvent(tickEvent, clientPlayerEntity);
            }
         }
      };
      this.handleListener(var160);
      var var161 = new SafeListener<AttackEvent2>() {
         public void handleObject(
            AttackEvent2 attackEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (AutoMaceModule.this.modeSetting.testObject2(AutoMaceModule.Mode.Continuous)) {
               AutoMaceModule.this.maceAttackTask.handleEntity(attackEvent2.getEntity());
            }
         }
      };
      this.handleListener(var161);
      Setting var162 = this.modeSetting;
      Consumer<AutoMaceModule.Mode> var179 = this::handleMode;
      var162.handleConsumer2(var179);
      Runnable var196 = this::run2;
      ModuleListenerUtils.handleListenerHolder4(this, var196);
      autoMaceModule = this;
   }

   private static class DashRenderer {
      public final AnimatedVec3d animatedVec3d = new AnimatedVec3d();
      public final AnimatedVec3d animatedVec3d2 = new AnimatedVec3d();
      public boolean flag;
      public final AnimatedVec3d animatedVec3d3 = new AnimatedVec3d();
      public final FloatAnimator floatAnimator;

      public void handleDashTarget(@Nullable AutoMaceModule.DashTarget dashTarget) {
         if (dashTarget == null) {
            this.flag = true;
            this.floatAnimator.setFloatValue(0.0F);
         } else {
            this.floatAnimator.setFloatValue(1.0F);
            if (this.flag) {
               this.animatedVec3d.handleVec3d2(dashTarget.getVec3d());
               this.animatedVec3d2.handleVec3d2(dashTarget.getVec3d2());
               this.animatedVec3d3.handleVec3d2(dashTarget.getVec3d3());
               this.flag = false;
            } else {
               this.animatedVec3d.handleVec3d(dashTarget.getVec3d());
               this.animatedVec3d2.handleVec3d(dashTarget.getVec3d2());
               this.animatedVec3d3.handleVec3d(dashTarget.getVec3d3());
            }
         }
      }

      public void run() {
         this.animatedVec3d.handleFloat(10.0F, 0.5F);
         this.animatedVec3d2.handleFloat(10.0F, 0.03F);
         this.animatedVec3d3.handleFloat(10.0F, 0.1F);
         this.floatAnimator.handleFloat3(10.0F, 0.1F);
      }

      public Vec3d[] values() {
         return new Vec3d[]{this.animatedVec3d.createVec3d(), this.animatedVec3d2.createVec3d(), this.animatedVec3d3.createVec3d()};
      }

      public DashRenderer() {
         this.flag = true;
         this.floatAnimator = new FloatAnimator();
      }
   }

   private static class DashTarget {
      public boolean flag;
      public final Vec3d vec3d;
      public final Vec3d vec3d2;
      public boolean flag2;
      public boolean flag3;
      public int intValue = 0;
      public Vec3d vec3d3;
      public boolean completed;
      public Vec3d vec3d4;
      public final double doubleValue;
      public final Entity entity;
      private static Object[] phobos$g1806e23c8;

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public void setVec3d4(Vec3d vec3d) {
         this.vec3d4 = vec3d;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public DashTarget(Entity entity, double d, Vec3d vec3d, Vec3d vec3d2, Vec3d vec3d3) {
         this.entity = entity;
         this.doubleValue = d;
         this.vec3d = vec3d;
         this.vec3d2 = vec3d2;
         this.vec3d3 = vec3d3;
      }

      public double computeClientPlayerEntity(@Nullable ClientPlayerEntity clientPlayerEntity) {
         if (clientPlayerEntity == null) {
            return 0.0;
         } else {
            return this.vec3d4 == this.vec3d2
               ? clientPlayerEntity.getPos().distanceTo(this.vec3d2) + this.vec3d2.distanceTo(this.vec3d3)
               : clientPlayerEntity.getPos().distanceTo(this.vec3d3);
         }
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void handlePhobosClient(
         PhobosClient phobosClient,
         ClientPlayerEntity clientPlayerEntity,
         ClientWorld clientWorld,
         ClientPlayerInteractionManager clientPlayerInteractionManager
      ) {
         label45: {
            label37: {
               Entity var5 = this.entity;
               int var6 = var5.getId();
               Entity var7 = clientWorld.getEntityById(var6);
               if (var7 != null) {
                  boolean var8 = clientPlayerEntity.isOnGround();
                  if (!var8) {
                     break label37;
                  }

                  int var9 = this.intValue;
                  if (var9 <= 4) {
                     break label37;
                  }
               }

               boolean var14 = PlayerActionUtils.testClientPlayerEntity4(clientPlayerEntity);
               if (var14) {
                  boolean var15 = this.flag;
                  if (var15) {
                     AutoArmorModule var16 = AutoArmorModule.getAutoArmorModule();
                     var16.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                  }
               }
               break label45;
            }

            Vec3d var10 = this.vec3d4;
            Vec3d var11 = this.vec3d2;
            Object var12 = Objects.requireNonNullElse(var10, var11);
            boolean var13 = var12 instanceof Vec3d;
            if (var12 != null && !var13) {
               throw new ClassCastException("CHECKCAST failed");
            }

            float[] var17 = RotationUtils.values2(clientPlayerEntity, (Vec3d)var12);
            float var18 = var17[0];
            float var19 = var17[1];
            Vec3d var20 = MathUtils.createVec3d4(
               (float)Float.intBitsToFloat(Float.floatToRawIntBits(var18)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var19))
            );
            Vec3d var21 = this.vec3d3;
            double[] var22 = new double[0];
            boolean var23 = this.testClientPlayerEntity(clientPlayerEntity, var21, var20, var22);
            if (var23) {
               int var24 = this.intValue;
               if (var24 > 4) {
                  boolean var25 = PlayerActionUtils.testClientPlayerEntity4(clientPlayerEntity);
                  if (var25) {
                     boolean var26 = this.flag;
                     if (var26) {
                        AutoArmorModule var27 = AutoArmorModule.getAutoArmorModule();
                        var27.handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
                     }
                  }
                  break label45;
               }
            }

            RotationManager var28 = phobosClient.getRotationManager();
            float var29 = var17[0];
            float var30 = var17[1];
            var28.testFloat((float)Float.intBitsToFloat(Float.floatToRawIntBits(var29)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var30)));
            InventoryManager var31 = phobosClient.getInventoryManager();
            AutoMaceModule.DashTarget var10000 = this;
            ClientPlayerEntity var10001 = clientPlayerEntity;
            ClientWorld var10002 = clientWorld;
            ClientPlayerInteractionManager var10003 = clientPlayerInteractionManager;
            Vec3d cap86 = var20;
            ClientPlayerInteractionManager cap85 = var10003;
            ClientWorld cap84 = var10002;
            ClientPlayerEntity cap83 = var10001;
            AutoMaceModule.DashTarget cap82 = var10000;
            Consumer<ItemSwitcher> var48 = var5x -> cap82.handleClientPlayerEntity2(cap83, cap84, cap85, cap86, var5x);
            var31.handleConsumer2(var48);
            return;
         }

         this.completed = true & true;
      }

      public Vec3d getVec3d4() {
         return this.vec3d4;
      }

      public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, AutoMaceModule autoMaceModule) {
         if (this.entity != null) {
            Entity entity = autoMaceModule.resolveEntity(this.entity, true);
            float[] floats = RotationUtils.values6(
               this.vec3d2.getX(), this.vec3d2.getY(), this.vec3d2.getZ(), entity.getX(), entity.getY(), entity.getZ(), clientPlayerEntity.prevYaw
            );
            double d = MathHelper.square(AuraModule.getAuraModule().getDoubleValueSetting().getValue());
            MutableDouble mutableDouble = new MutableDouble(0.0);
            MutableObject<BlockHitResult> mutableObject = new MutableObject<>();
            autoMaceModule.handleClientPlayerEntity5(
               clientPlayerEntity, clientWorld, this.vec3d2, floats[0], floats[1], 30.0F, 20.0F, 60.0, (blockHitResult, vec3d) -> {
                  Vec3d vec3d2 = blockHitResult.getPos();
                  if (vec3d2 == null) {
                     return false;
                  }

                  double d2 = entity.getBoundingBox().squaredMagnitude(vec3d2);
                  if (d2 > d) {
                     return false;
                  }

                  if (mutableObject.getValue() == null || d2 < mutableDouble.getValue()) {
                     mutableObject.setValue(blockHitResult);
                     mutableDouble.setValue(d2);
                  }

                  return false;
               }
            );
            if (mutableObject.getValue() == null) {
               if (PlayerActionUtils.testClientPlayerEntity4(clientPlayerEntity) && this.flag) {
                  AutoArmorModule.getAutoArmorModule().handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
               }

               this.completed = true;
            } else {
               this.setVec3d3(mutableObject.getValue().getPos());
            }
         }
      }

      public Vec3d getVec3d3() {
         return this.vec3d3;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public void setVec3d3(Vec3d vec3d) {
         this.vec3d3 = vec3d;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public boolean isFlag3() {
         return this.flag3;
      }

      public boolean isCompleted() {
         return this.completed;
      }

      public void setCompleted(boolean flag) {
         this.completed = flag;
      }

      public Entity getEntity() {
         return this.entity;
      }

      public void handleClientPlayerEntity2(
         ClientPlayerEntity clientPlayerEntity,
         ClientWorld clientWorld,
         ClientPlayerInteractionManager clientPlayerInteractionManager,
         Vec3d vec3d,
         ItemSwitcher itemSwitcher
      ) {
         switch (this.intValue) {
            case 0:
               boolean flag = PlayerActionUtils.testClientPlayerEntity4(clientPlayerEntity);
               if (!flag) {
                  AutoArmorModule.getAutoArmorModule().handleClientPlayerEntity(clientPlayerEntity, clientWorld);
                  this.flag = true;
               }

               this.vec3d4 = this.vec3d2;
               this.intValue = 1;
               break;
            case 1:
               this.intValue = 2;
               break;
            case 2:
               if (clientPlayerEntity.isOnGround()) {
                  this.flag3 = true;
                  this.intValue = 1;
                  return;
               }

               if (!PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) && PlayerActionUtils.testClientPlayerEntity4(clientPlayerEntity)) {
                  clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                  clientPlayerEntity.startFallFlying();
               }

               this.intValue = 3;
               break;
            case 3:
               this.intValue = 4;
               break;
            case 4:
               MiddleClickModule.getMiddleClickModule().testClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager);
               this.intValue = 5;
               break;
            case 5:
               if (!PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) && PlayerActionUtils.testClientPlayerEntity4(clientPlayerEntity)) {
                  clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                  clientPlayerEntity.startFallFlying();
               }

               if (this.testClientPlayerEntity(clientPlayerEntity, this.vec3d2, vec3d, 3.0)) {
                  this.flag2 = true;
               }

               if (this.flag2) {
                  this.vec3d4 = this.vec3d3;
               }

               if (!this.testClientPlayerEntity(clientPlayerEntity, this.vec3d3, vec3d)) {
                  return;
               }

               AutoArmorModule.getAutoArmorModule().handleClientPlayerEntity2(clientPlayerEntity, clientWorld);
         }
      }

      public Vec3d getVec3d2() {
         return this.vec3d2;
      }

      public void setFlag3(boolean flag) {
         this.flag3 = flag;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Vec3d vec3d, Vec3d vec3d2, double... doubles) {
         Vec3d vec3d3 = clientPlayerEntity.getPos();
         if (vec3d3.distanceTo(vec3d) <= 1.0) {
            return true;
         }

         double d = doubles.length > 0 ? doubles[0] : 1.5;
         Vec3d vec3d4 = vec3d3.add(vec3d2.multiply(d));
         return vec3d4.distanceTo(vec3d) <= d;
      }

      public int getIntValue() {
         return this.intValue;
      }
   }

   private enum Mode {
      Rage("Instant mace, for no anticheat servers"),
      Legit("Closet cheating mace"),
      Attack("Only attacks with a mace"),
      Dash("Uses elytra to dash onto a target"),
      Continuous("Uses FakeFly to mace");

      public final String description;

      public String getDescription() {
         return this.description;
      }

      Mode(final String description) {
         this.description = description;
      }
   }

   public enum Movement {
      Vertical,
      Dynamic;
   }

   private enum Stage {
      None,
      Flight,
      AirBone;
   }
}
