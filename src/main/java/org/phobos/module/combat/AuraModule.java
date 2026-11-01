package org.phobos.module.combat;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.api.NumberRange;
import org.phobos.api.TickGate.Normal;
import org.phobos.api.NumberRange.Mutable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.AnimatedVec3d;
import org.phobos.core.render.RenderContext;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.animation.TimedFloatAnimator;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.task.Scheduler;
import org.phobos.core.combat.AutoBlockHandler;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.module.TargetModule;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.combat.AutoBlockHandler.AutoBlock;
import org.phobos.core.combat.AutoBlockHandler.Unblock;
import org.phobos.core.movement.SilentMovementManager.SilentMovement;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.render.FreeLookModule;

public class AuraModule extends TargetModule {
   public Setting<Boolean> booleanValueSetting;
   public static final Logger LOGGER = LoggerFactory.getLogger(AuraModule.class);
   public Setting<AuraModule.Rotate> rotateSetting;
   public Setting<AuraModule.Swing> swingSetting;
   public Setting<Mutable<Float>> mutableSetting;
   public Setting<Silent> silentSetting;
   public Setting<Double> doubleValueSetting;
   public Setting<AuraModule.Target> targetSetting;
   public Setting<Mutable<Float>> mutableSetting2;
   public Setting<Double> doubleValueSetting2;
   public Setting<AuraModule.Timing> timingSetting;
   public Setting<Mutable<Float>> mutableSetting3;
   public Setting<AuraModule.Switch> switchValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Integer> integerSetting;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Mutable<Double>> mutableSetting4;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Integer> integerSetting2;
   public Setting<Boolean> booleanValueSetting5;
   public static AuraModule auraModule;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Mutable<Float>> mutableSetting5;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Integer> integerSetting3;
   public Setting<AuraModule.Vector> vectorSetting;
   public Setting<Integer> integerSetting4;
   public Setting<AuraModule.Distance> distanceSetting;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Boolean> booleanValueSetting10;
   public Setting<Unblock> unblockSetting;
   public Setting<AuraModule.Raytrace> raytraceSetting;
   public Setting<AuraModule.Jump> jumpSetting;
   public Setting<Integer> integerSetting5;
   public Setting<Mutable<Float>> mutableSetting6;
   public Setting<Integer> integerSetting6;
   public Setting<Boolean> booleanValueSetting11;
   public Setting<Boolean> booleanValueSetting12;
   public Setting<AutoBlock> autoBlockSetting;
   public Setting<Boolean> booleanValueSetting13;
   public Setting<Map<AuraModule.Source, Boolean>> mapSetting;
   public Setting<Long> longValueSetting;
   public Setting<Boolean> booleanValueSetting14;
   public Setting<Boolean> booleanValueSetting15;
   public Setting<Boolean> booleanValueSetting16;
   public Setting<Boolean> booleanValueSetting17;
   public Setting<Float> floatValueSetting;
   public Setting<Float> floatValueSetting2;
   public Setting<Double> doubleValueSetting3;
   public Setting<Boolean> booleanValueSetting18;
   public Setting<Float> floatValueSetting3;
   public Setting<Boolean> booleanValueSetting19;
   public Setting<Color> colorSetting;
   public Setting<Boolean> booleanValueSetting20;
   public Setting<Float> floatValueSetting4;
   public Setting<Boolean> booleanValueSetting21;
   public Setting<Color> colorSetting2;
   public Setting<Boolean> booleanValueSetting22;
   public Setting<Boolean> booleanValueSetting23;
   public Setting<Color> colorSetting3;
   public Setting<Boolean> booleanValueSetting24;
   public Setting<Color> colorSetting4;
   public Entity entity;
   public int intValue;
   public boolean flag;
   public boolean flag2;
   public Normal normal;
   public FloatAnimator floatAnimator;
   public TimedFloatAnimator timedFloatAnimator;
   public Setting<Boolean> booleanValueSetting25;
   public org.phobos.api.TimeGate.Normal normal2;
   public TimedFloatAnimator timedFloatAnimator2;
   public Vec3d vec3d;
   public AnimatedVec3d animatedVec3d;
   public boolean flag3;
   public boolean flag4;
   public AutoBlockHandler autoBlockHandler;
   public int intValue2;
   public Setting<Integer> integerSetting7;
   public Vec3d vec3d2;
   public float[] floatArray;
   private static Object[] phobos$g1806d8ad8;
   private static Object[] phobos$g1806d8be0;
   private static Object[] phobos$g1806d8cd8;
   private static Object[] phobos$g1806d8dd0;
   private static Object[] phobos$g1806d90b8;
   private static Object[] phobos$g1806d92e0;
   private static Object[] phobos$g1806d9418;
   private static Object[] phobos$g1806d9db8;
   private static Object[] phobos$g1806d88b0;

   public Entity resolveEntity(Entity entity) {
      if (!this.booleanValueSetting25.getValue()) {
         return entity;
      } else {
         return entity instanceof PlayerEntity playerEntity
            ? Objects.requireNonNullElse(
               this.phobosClient.getFeatureRegistry().getPredictionManager().resolvePlayerEntity3(this.integerSetting7.getValue(), playerEntity), playerEntity
            )
            : entity;
      }
   }

   public boolean testEntity(Entity entity) {
      return !(entity instanceof PlayerEntity playerEntity) ? false : this.booleanValueSetting12.getValue() && playerEntity.isBlocking();
   }

   @Nullable
   public Vec3d resolveVec3d(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity) {
      if (!this.testEntity3(entity, clientPlayerEntity, this.doubleValueSetting.getValue() + this.doubleValueSetting2.getValue())) {
         return null;
      }

      float f = clientPlayerEntity.getEyeHeight(clientPlayerEntity.getPose());
      Vec3d vec3d = clientPlayerEntity.getPos();
      Vec3d vec3d2 = vec3d.add(0.0, f, 0.0);
      Vec3d vec3d3 = BlockPosUtils.createVec3d(entity.getBoundingBox(), vec3d2);
      Vec3d vec3d4 = new Vec3d(vec3d3.getX(), vec3d2.getY(), vec3d3.getZ());
      double d = MathHelper.square(5.9999999);
      double d2 = vec3d4.squaredDistanceTo(vec3d3);
      double d3 = Math.sqrt(d - d2);
      Vec3d vec3d5 = vec3d4.add(vec3d2.subtract(vec3d4).normalize().multiply(d3));
      Vec3d vec3d6 = vec3d5.subtract(0.0, f, 0.0);
      Vec3d vec3d7 = vec3d6.subtract(vec3d);
      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
      movingFakePlayer.setPosition(vec3d);
      movingFakePlayer.setVelocity(vec3d7);
      movingFakePlayer.setOnGround(clientPlayerEntity.isOnGround());
      movingFakePlayer.move(MovementType.SELF, vec3d7);
      return !movingFakePlayer.getPos().equals(vec3d6) ? null : vec3d6;
   }

   public Entity resolveEntity2(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      MutableObject<LivingEntity> mutableObject = new MutableObject<>(null);
      MutableFloat mutableFloat = new MutableFloat(360.0F);
      MutableFloat mutableFloat2 = new MutableFloat(Float.MAX_VALUE);

      for (Entity entity2 : clientWorld.getEntities()) {
         if (entity2 != null
            && !entity2.isRemoved()
            && entity2.isAlive()
            && !entity2.equals(clientPlayerEntity)
            && clientWorld.getWorldBorder().contains(entity2.getBlockPos())
            && !AntiBotModule.testEntity2(entity2)) {
            Entity entity = this.resolveEntity(entity2);
            double d = RotationUtils.computeClientPlayerEntity3(clientPlayerEntity, entity, 1.0);
            if (!(d > this.floatValueSetting2.getValue().floatValue())
               && this.testEntity3(entity, clientPlayerEntity, 10.0)
               && !this.phobosClient.getFriendList().testUUID(entity2.getUuid())) {
               if (this.phobosClient.getEnemyList().testUUID(entity2.getUuid()) && this.testEntity3(entity2, clientPlayerEntity, this.value())) {
                  return entity2;
               }

               if (this.booleanValueSetting20.getValue()) {
                  if (clientPlayerEntity.isTeammate(entity2)) {
                     continue;
                  }

                  Text text = clientPlayerEntity.getDisplayName();
                  Text text2 = entity2.getDisplayName();
                  if (text != null
                     && text2 != null
                     && text.getStyle().getColor() != null
                     && text2.getStyle().getColor() != null
                     && text.getStyle().getColor().equals(text2.getStyle().getColor())) {
                     continue;
                  }
               }

               if (this.testEntity2(entity2)
                  && !(
                     entity2 instanceof PlayerEntity playerEntity
                        && (playerEntity.isCreative() && !this.booleanValueSetting9.getValue() || playerEntity.isSpectator())
                  )) {
                  if (this.raytraceSetting.getValue().equals(AuraModule.Raytrace.Legit)) {
                     Vec3d vec3d = this.resolveVec3d2(
                        this.vectorSetting.getValue(),
                        clientPlayerEntity,
                        clientWorld,
                        entity,
                        (vec3dx, vec3d2) -> {
                           HitResult hitResult = clientWorld.raycast(
                              new RaycastContext(vec3dx, vec3d2, ShapeType.OUTLINE, FluidHandling.NONE, clientPlayerEntity)
                           );
                           return hitResult == null || !hitResult.getType().equals(Type.BLOCK);
                        }
                     );
                     if (vec3d == null) {
                        continue;
                     }
                  }

                  LivingEntity livingEntity = (LivingEntity)entity2;
                  switch ((AuraModule.Target)this.targetSetting.getValue()) {
                     case Angle:
                        float f2 = clientPlayerEntity.distanceTo(entity);
                        if (mutableObject.getValue() == null) {
                           mutableObject.setValue(livingEntity);
                        } else if (f2 < clientPlayerEntity.distanceTo(mutableObject.getValue())) {
                           mutableObject.setValue(livingEntity);
                        }
                        break;
                     case Health:
                        if (!this.testClientPlayerEntity(clientPlayerEntity, clientWorld, entity2)) {
                           if (mutableObject.getValue() == null) {
                              mutableObject.setValue(livingEntity);
                           }
                        } else {
                           float f = this.phobosClient
                              .getFeatureRegistry()
                              .getDamageTracker()
                              .computeLivingEntity(livingEntity, EntityUtils.computeLivingEntity(livingEntity));
                           if (mutableObject.getValue() == null) {
                              mutableObject.setValue(livingEntity);
                           } else if (f < mutableFloat2.getValue()) {
                              mutableObject.setValue(livingEntity);
                              mutableFloat2.setValue(f);
                           }
                        }
                        break;
                     case Distance:
                        if (mutableObject.getValue() == null) {
                           mutableObject.setValue(livingEntity);
                        } else if (d < mutableFloat.getValue().floatValue()) {
                           mutableObject.setValue(livingEntity);
                           mutableFloat.setValue(d);
                        }
                  }
               }
            }
         }
      }

      return mutableObject.getValue();
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager, Entity entity) {
      this.run();
      clientPlayerInteractionManager.attackEntity(clientPlayerEntity, entity);
      switch ((AuraModule.Swing)this.swingSetting.getValue()) {
         case None:
            clientPlayerEntity.swingHand(Hand.MAIN_HAND);
            break;
         case Packet:
            clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }

      if (this.booleanValueSetting6.getValue() && this.booleanValueSetting11.getValue()) {
         this.intValue2 = MathUtils.computeInt5(0, this.integerSetting5.getValue());
      }

      this.normal2.run();
   }

   public static Entity getEntity() {
      return auraModule.check7() ? auraModule.entity : null;
   }

   public void handleClientPlayerEntity3(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      PhobosClient var4 = this.phobosClient;
      InventoryManager var5 = var4.getInventoryManager();
      AuraModule var10000 = this;
      ClientPlayerEntity var10001 = clientPlayerEntity;
      ClientWorld var10002 = clientWorld;
      ClientPlayerInteractionManager cap58 = clientPlayerInteractionManager;
      ClientWorld cap57 = var10002;
      ClientPlayerEntity cap56 = var10001;
      AuraModule cap55 = var10000;
      Consumer<ItemSwitcher> var22 = var4x -> cap55.handleClientPlayerEntity6(cap56, cap57, cap58, var4x);
      var5.handleConsumer2(var22);
   }

   public Vec3d resolveVec3d2(
      AuraModule.Vector vector, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, BiPredicate<Vec3d, Vec3d> biPredicate
   ) {
      return this.resolveObject(clientPlayerEntity, vec3d -> vector.calculate(vec3d, clientPlayerEntity, clientWorld, entity, biPredicate));
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity) {
      if (!this.testEntity3(entity, clientPlayerEntity, this.value())) {
         return !this.check() ? false : this.resolveVec3d(clientPlayerEntity, clientWorld, entity) != null;
      } else {
         return true;
      }
   }

   public double value() {
      return this.doubleValueSetting.getValue();
   }

   public <T> T resolveObject(ClientPlayerEntity clientPlayerEntity, Function<Vec3d, @Nullable T> function) {
      for (Entry<AuraModule.Source, Boolean> entry : this.mapSetting.getValue().entrySet()) {
         if (entry.getValue()) {
            T object = function.apply(entry.getKey().getInterpolatedVector(clientPlayerEntity, TimerManager.getFloatValue()));
            if (object != null) {
               return object;
            }
         }
      }

      return null;
   }

   public float[] values(ClientPlayerEntity clientPlayerEntity, Entity entity, Vec3d vec3d) {
      float[] floats = RotationUtils.values2(clientPlayerEntity, vec3d);
      if (this.booleanValueSetting6.getValue()) {
         AuraModule.Spread spread = this.createSpread(entity);
         float f = spread.offset();
         float f2 = floats[0] + spread.computeFloat(f);
         float f3 = MathUtils.computeFloat3(floats[1] + spread.computeFloat(f), -90.0F, 90.0F);
         this.timedFloatAnimator.setFloatValue(f2);
         this.timedFloatAnimator2.setFloatValue(f3);
         float f4 = this.timedFloatAnimator.getFloatValue2();
         float f5 = this.timedFloatAnimator2.getFloatValue2();
         return new float[]{f4, MathUtils.computeFloat3(f5, -90.0F, 90.0F)};
      }

      if (this.rotateSetting.getValue().equals(AuraModule.Rotate.Fast)) {
         float f6 = MathHelper.wrapDegrees(floats[0] - this.floatArray[0]);
         float f7 = floats[1] - this.floatArray[1];
         float f8 = this.mutableSetting.getValue().getNumber4();
         float f9 = this.mutableSetting2.getValue().getNumber4();
         if (f6 > 180.0F) {
            f6 -= 180.0F;
         }

         float f10 = MathUtils.computeFloat3(MathHelper.abs(f6), -f8, f8);
         float f11 = MathUtils.computeFloat3(f7, -f9, f9);
         float f12 = MathHelper.wrapDegrees(this.floatArray[0]) + (f6 > 0.0F ? f10 : -f10);
         float f13 = MathUtils.computeFloat3(this.floatArray[1] + f11, -90.0F, 90.0F);
         return new float[]{f12, f13};
      } else {
         return floats;
      }
   }

   public void handleClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity) {
      if (this.booleanValueSetting16.getValue() && this.booleanValueSetting18.getValue() && mc.cameraEntity != null) {
         EntityHitResult entityHitResult = RaycastUtils.resolveEntityHitResult3(clientPlayerEntity, this.entity, this.floatArray);
         if (entityHitResult != null) {
            this.vec3d = entityHitResult.getPos();
         }
      }
   }

   public void run() {
      this.floatAnimator.setFloatValue2(1.0F);
   }

   public boolean testItemSwitcher(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity) {
      if (this.booleanValueSetting5.getValue() && clientPlayerEntity.isUsingItem() && clientPlayerEntity.getActiveHand() == Hand.MAIN_HAND) {
         return false;
      }

      float f = this.phobosClient.getTimerManager().getFloatValue22();
      float f2 = clientPlayerEntity.getAttackCooldownProgress(0.5F) / f;
      ItemStack itemStack = clientPlayerEntity.getMainHandStack();
      if (!this.booleanValueSetting3.getValue()
         || !InventoryUtils.testItemStack(itemStack)
            && (itemSwitcher.resolveObject(slot -> InventoryUtils.testItemStack(slot.getStack()) ? slot : null) == null || !this.booleanValueSetting2.getValue())) {
         if (this.booleanValueSetting17.getValue()) {
            if (this.booleanValueSetting24.getValue() && clientPlayerEntity.fallDistance < this.floatValueSetting4.getValue()) {
               return false;
            }

            if (this.booleanValueSetting23.getValue() && clientPlayerEntity.fallDistance > this.floatValueSetting4.getValue()) {
               return true;
            }
         }

         if (this.switchValueSetting.testObject2(AuraModule.Switch.Silent) && this.booleanValueSetting.getValue() && !InventoryUtils.testItem4(itemStack.getItem())) {
            return this.normal2.testLong(250L);
         }

         switch ((AuraModule.Timing)this.timingSetting.getValue()) {
            case Custom:
               if (f2 < 1.0F) {
                  return false;
               }
               break;
            case Normal:
               if (f2 < 0.9F) {
                  return false;
               }
               break;
            case Fast:
               if (!this.normal2.testNumber(1000.0F - this.mutableSetting3.getValue().getNumber() * 50.0F)) {
                  return false;
               }

               this.mutableSetting3.getValue().run6();
         }

         if (!this.booleanValueSetting4.getValue()) {
            return true;
         } else if (this.normal.check2()) {
            this.normal.setIntValue(MathUtils.computeInt5(this.integerSetting.getValue(), this.integerSetting2.getValue()));
            return true;
         } else {
            this.normal.run2();
            return false;
         }
      } else {
         return true;
      }
   }

   public boolean check() {
      return this.doubleValueSetting.getValue() >= 4.5 && this.doubleValueSetting2.getValue() > 0.0;
   }

   public boolean testDistance(
      AuraModule.Distance distance, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Entity entity, Vec3d vec3d, double d
   ) {
      MutableBoolean mutableBoolean = new MutableBoolean();
      this.resolveObject(clientPlayerEntity, vec3d2 -> {
         mutableBoolean.setValue(mutableBoolean.getValue() || distance.isInRange(vec3d2, clientWorld, entity, vec3d, d));
         return null;
      });
      return mutableBoolean.getValue();
   }

   public boolean testEntity3(Entity entity, ClientPlayerEntity clientPlayerEntity, double d) {
      Vec3d vec3d = clientPlayerEntity.getEyePos();
      return entity.getBoundingBox().squaredMagnitude(vec3d) < MathHelper.square(d);
   }

   public AuraModule.Spread createSpread(Entity entity) {
      if (this.booleanValueSetting6.getValue() && this.booleanValueSetting11.getValue() && this.intValue2 > 0) {
         float f = (float)(this.intValue2--) / this.integerSetting5.getValue().intValue();
         return new AuraModule.Spread(15.0F * f, false);
      } else {
         return RaycastUtils.testPhobosClient(this.phobosClient, entity)
            ? AuraModule.Spread.INSTANCE
            : new AuraModule.Spread(this.mutableSetting6.getValue().getNumber4(), true);
      }
   }

   public AuraModule(PhobosClient phobosClient) {
      super(phobosClient, "Aura", "Attacks entities", Categories.combat);
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Double var3 = 100.0;
      Double var4 = 0.0;
      Double var5 = 100.0;
      String[] var6 = new String[1];
      var6[0] = "%";
      MutableNumberSetting var2 = new MutableNumberSetting<>("Accuracy", var3, var4, var5, var6);
      Setting var7 = this.resolveSetting2(var2);
      this.mutableSetting4 = var7;
      AuraModule.Target var9 = AuraModule.Target.Health;
      EnumSetting var8 = new EnumSetting<>("Target", var9);
      Setting var10 = var8.resolveSetting7("Target preference");
      Setting var11 = this.resolveSetting2(var10);
      this.targetSetting = var11;
      AuraModule.Rotate var13 = AuraModule.Rotate.None;
      EnumSetting var12 = new EnumSetting<>("Rotate", var13);
      Setting var14 = var12.resolveSetting7("Rotation type");
      Setting var15 = this.resolveSetting2(var14);
      this.rotateSetting = var15;
      Float var17 = (float)50.0F;
      Float var18 = (float)70.0F;
      Mutable var19 = NumberRange.createMutable(var17, var18);
      Float var20 = (float)1.0F;
      Float var21 = (float)180.0F;
      String[] var22 = new String[0];
      MutableNumberSetting var16 = new MutableNumberSetting<>("YawSpeed", var19, var20, var21, var22);
      Setting var23 = this.rotateSetting;
      AuraModule.Rotate var24 = AuraModule.Rotate.Fast;
      Setting var25 = var16.resolveSetting4(var23, var24);
      Setting var26 = this.resolveSetting2(var25);
      this.mutableSetting = var26;
      Float var28 = (float)1.0F;
      Float var29 = (float)10.0F;
      Mutable var30 = NumberRange.createMutable(var28, var29);
      Float var31 = (float)1.0F;
      Float var32 = (float)180.0F;
      String[] var33 = new String[0];
      MutableNumberSetting var27 = new MutableNumberSetting<>("PitchSpeed", var30, var31, var32, var33);
      Setting var34 = this.rotateSetting;
      AuraModule.Rotate var35 = AuraModule.Rotate.Fast;
      Setting var36 = var27.resolveSetting4(var34, var35);
      Setting var37 = this.resolveSetting2(var36);
      this.mutableSetting2 = var37;
      AuraModule.Switch var39 = AuraModule.Switch.Require;
      EnumSetting var38 = new EnumSetting<>("Switch", var39);
      Setting var40 = var38.resolveSetting7("Switch type");
      Setting var41 = this.resolveSetting2(var40);
      this.switchValueSetting = var41;
      Silent var43 = Silent.Normal;
      EnumSetting var42 = new EnumSetting<>("Silent", var43);
      Setting var44 = this.switchValueSetting;
      AuraModule.Switch var45 = AuraModule.Switch.Silent;
      Setting var46 = var42.resolveSetting4(var44, var45);
      Setting var47 = this.resolveSetting2(var46);
      this.silentSetting = var47;
      BooleanSetting var48 = new BooleanSetting("Spam");
      Setting var49 = var48.resolveSetting7("Spams if you aren't holding required item");
      Setting var50 = this.switchValueSetting;
      AuraModule.Switch var51 = AuraModule.Switch.Silent;
      Setting var52 = var49.resolveSetting4(var50, var51);
      Setting var53 = this.resolveSetting2(var52);
      this.booleanValueSetting = var53;
      AuraModule.Swing var55 = AuraModule.Swing.Normal;
      EnumSetting var54 = new EnumSetting<>("Swing", var55);
      Setting var56 = var54.resolveSetting7("Hand swing");
      Setting var57 = this.resolveSetting2(var56);
      this.swingSetting = var57;
      Double var59 = 6.0;
      Double var60 = 0.1;
      Double var61 = 6.0;
      NumberSetting var58 = new NumberSetting<>("Range", var59, var60, var61);
      Setting var62 = var58.resolveSetting7("Attack range");
      Setting var63 = this.resolveSetting2(var62);
      this.doubleValueSetting = var63;
      Double var65 = 0.0;
      Double var66 = 0.0;
      Double var67 = 1.5;
      NumberSetting var64 = new NumberSetting<>("Extension", var65, var66, var67);
      Visibility var84 = this::check5;
      Setting var85 = var64.resolveSetting3(var84);
      Setting var86 = this.resolveSetting2(var85);
      this.doubleValueSetting2 = var86;
      BooleanSetting var87 = new BooleanSetting("Pause");
      Setting var88 = var87.resolveSetting7("Stops aura while eating");
      Setting var89 = this.resolveSetting2(var88);
      this.booleanValueSetting5 = var89;
      AuraModule.Timing var91 = AuraModule.Timing.Normal;
      EnumSetting var90 = new EnumSetting<>("Timing", var91);
      Setting var92 = var90.resolveSetting7("Attack timing");
      Setting var93 = this.resolveSetting2(var92);
      this.timingSetting = var93;
      BooleanSetting var94 = new BooleanSetting("32k");
      Setting var95 = this.resolveSetting2(var94);
      this.booleanValueSetting3 = var95;
      BooleanSetting var96 = new BooleanSetting("32kSilent");
      Setting var97 = this.resolveSetting2(var96);
      this.booleanValueSetting2 = var97;
      BooleanSetting var98 = new BooleanSetting("Legitimize");
      Setting var99 = var98.resolveSetting7("Creates a tick buffer to randomize pattern");
      Setting var100 = this.resolveSetting2(var99);
      this.booleanValueSetting4 = var100;
      Integer var102 = 1;
      Integer var103 = 1;
      Integer var104 = 6;
      NumberSetting var101 = new NumberSetting<>("LegitimizeMin", var102, var103, var104);
      Setting var105 = this.resolveSetting2(var101);
      this.integerSetting = var105;
      Integer var107 = 5;
      Integer var108 = 1;
      Integer var109 = 16;
      NumberSetting var106 = new NumberSetting<>("LegitimizeMax", var107, var108, var109);
      Setting var110 = this.resolveSetting2(var106);
      this.integerSetting2 = var110;
      Float var112 = (float)15.0F;
      Float var113 = (float)15.0F;
      Mutable var114 = NumberRange.createMutable(var112, var113);
      Float var115 = (float)0.1F;
      Float var116 = (float)20.0F;
      String[] var117 = new String[0];
      MutableNumberSetting var111 = new MutableNumberSetting<>("CPS", var114, var115, var116, var117);
      Setting var118 = this.timingSetting;
      AuraModule.Timing var119 = AuraModule.Timing.Custom;
      Setting var120 = var111.resolveSetting4(var118, var119);
      Setting var121 = var120.resolveSetting7("Clicks per second");
      Setting var122 = this.resolveSetting2(var121);
      this.mutableSetting3 = var122;
      Boolean var124 = false & true;
      BooleanSetting var123 = new BooleanSetting("Shield", var124);
      Setting var125 = var123.resolveSetting7("Shield breaker");
      Setting var126 = this.resolveSetting2(var125);
      this.booleanValueSetting12 = var126;
      MultiSelectSetting var127 = new MultiSelectSetting<>("Sources", AuraModule.Source.class);
      AuraModule.Source[] var128 = AuraModule.Source.values();
      Function[] var129 = new Function[0];
      MultiSelectSetting var130 = var127.resolveMultiSelectSetting6(var128, var129);
      AuraModule.Source var131 = AuraModule.Source.Default;
      MultiSelectSetting var132 = var130.resolveMultiSelectSetting3(var131);
      Setting var133 = this.resolveSetting2(var132);
      this.mapSetting = var133;
      AuraModule.Vector var135 = AuraModule.Vector.Default;
      EnumSetting var134 = new EnumSetting<>("Vector", var135);
      Setting var136 = this.resolveSetting2(var134);
      this.vectorSetting = var136;
      AuraModule.Distance var138 = AuraModule.Distance.Hitbox;
      EnumSetting var137 = new EnumSetting<>("Distance", var138);
      Setting var139 = this.resolveSetting2(var137);
      this.distanceSetting = var139;
      AuraModule.Raytrace var141 = AuraModule.Raytrace.Normal;
      EnumSetting var140 = new EnumSetting<>("Raytrace", var141);
      Setting var142 = var140.resolveSetting7("Raytrace type");
      Setting var143 = this.resolveSetting2(var142);
      this.raytraceSetting = var143;
      AuraModule.Jump var145 = AuraModule.Jump.None;
      EnumSetting var144 = new EnumSetting<>("Jump", var145);
      Setting var146 = var144.resolveSetting7("Pauses attack & rotation when jumping to preserve velocity");
      Setting var147 = this.resolveSetting2(var146);
      this.jumpSetting = var147;
      BooleanSetting var148 = new BooleanSetting("Creative");
      Setting var149 = var148.resolveSetting7("Attack creative mode players");
      Setting var150 = this.resolveSetting2(var149);
      this.booleanValueSetting9 = var150;
      Boolean var152 = false & true;
      BooleanSetting var151 = new BooleanSetting("Simulate", var152);
      Setting var153 = var151.resolveSetting7("Aim simulation");
      Setting var154 = this.resolveSetting2(var153);
      this.booleanValueSetting6 = var154;
      BooleanSetting var155 = new BooleanSetting("Adjust");
      Setting var156 = var155.resolveSetting7("Only move aim if ray does not hit target");
      Setting var157 = this.resolveSetting2(var156);
      this.booleanValueSetting7 = var157;
      Integer var159 = 100;
      Integer var160 = 0;
      Integer var161 = 3000;
      NumberSetting var158 = new NumberSetting<>("AdjustFrames", var159, var160, var161);
      Setting var162 = this.resolveSetting2(var158);
      this.integerSetting4 = var162;
      Integer var164 = 0;
      Integer var165 = 0;
      Integer var166 = 6;
      NumberSetting var163 = new NumberSetting<>("AdjustSpread", var164, var165, var166);
      Setting var167 = this.resolveSetting2(var163);
      this.integerSetting6 = var167;
      BooleanSetting var168 = new BooleanSetting("ROH");
      Setting var169 = var168.resolveSetting7("Re-adjust on hit");
      Setting var170 = this.resolveSetting2(var169);
      this.booleanValueSetting11 = var170;
      Integer var172 = 4;
      Integer var173 = 1;
      Integer var174 = 18;
      NumberSetting var171 = new NumberSetting<>("ROHTicks", var172, var173, var174);
      Setting var175 = this.resolveSetting2(var171);
      this.integerSetting5 = var175;
      Float var177 = (float)50.0F;
      Float var178 = (float)50.0F;
      Mutable var179 = NumberRange.createMutable(var177, var178);
      Float var180 = (float)0.0F;
      Float var181 = (float)100.0F;
      String[] var182 = new String[0];
      MutableNumberSetting var176 = new MutableNumberSetting<>("Acceleration", var179, var180, var181, var182);
      Setting var183 = var176.resolveSetting7("Simulation speed");
      Setting var184 = this.resolveSetting2(var183);
      this.mutableSetting5 = var184;
      Float var186 = (float)1.0F;
      Float var187 = (float)1.0F;
      Mutable var188 = NumberRange.createMutable(var186, var187);
      Float var189 = (float)0.0F;
      Float var190 = (float)10.0F;
      String[] var191 = new String[0];
      MutableNumberSetting var185 = new MutableNumberSetting<>("Desync", var188, var189, var190, var191);
      Setting var192 = var185.resolveSetting7("Pointer jitter");
      Setting var193 = this.resolveSetting2(var192);
      this.mutableSetting6 = var193;
      BooleanSetting var194 = new BooleanSetting("AutoBlock");
      Setting var195 = var194.resolveSetting7("Auto sword block");
      Setting var196 = this.resolveSetting2(var195);
      this.booleanValueSetting8 = var196;
      BooleanSetting var197 = new BooleanSetting("AutoBlockVisual");
      Setting var198 = this.resolveSetting2(var197);
      this.booleanValueSetting10 = var198;
      AutoBlock var200 = AutoBlock.Vanilla;
      EnumSetting var199 = new EnumSetting<>("AutoBlockMode", var200);
      Setting var201 = this.resolveSetting2(var199);
      this.autoBlockSetting = var201;
      Unblock var203 = Unblock.Release;
      EnumSetting var202 = new EnumSetting<>("AutoBlockUnblock", var203);
      Setting var204 = this.resolveSetting2(var202);
      this.unblockSetting = var204;
      BooleanSetting var205 = new BooleanSetting("Instant");
      Setting var206 = var205.resolveSetting7("Re-block after attack");
      Setting var207 = this.resolveSetting2(var206);
      this.booleanValueSetting13 = var207;
      Integer var209 = 1;
      Integer var210 = 1;
      Integer var211 = 6;
      NumberSetting var208 = new NumberSetting<>("Ticks", var209, var210, var211);
      Setting var212 = this.autoBlockSetting;
      AutoBlock var213 = AutoBlock.Partial;
      Setting var214 = var208.resolveSetting4(var212, var213);
      Setting var215 = this.resolveSetting2(var214);
      this.integerSetting3 = var215;
      BooleanSetting var216 = new BooleanSetting("Render");
      Setting var217 = this.resolveSetting2(var216);
      this.booleanValueSetting16 = var217;
      BooleanSetting var218 = new BooleanSetting("Point");
      Setting var219 = var218.resolveSetting7("Rotation point");
      Setting var220 = this.resolveSetting2(var219);
      this.booleanValueSetting18 = var220;
      Color var222 = new Color(255, 255, 255, 50);
      ColorSetting var221 = new ColorSetting("PointBox", var222);
      ColorSetting var223 = var221.getColorSetting();
      Setting var224 = this.resolveSetting2(var223);
      this.colorSetting2 = var224;
      Color var226 = Color.WHITE;
      ColorSetting var225 = new ColorSetting("PointLine", var226);
      ColorSetting var227 = var225.getColorSetting();
      Setting var228 = this.resolveSetting2(var227);
      this.colorSetting3 = var228;
      Float var230 = (float)1.0F;
      Float var231 = (float)0.0F;
      Float var232 = (float)3.0F;
      NumberSetting var229 = new NumberSetting<>("PointWidth", var230, var231, var232);
      Setting var233 = this.resolveSetting2(var229);
      this.floatValueSetting = var233;
      BooleanSetting var234 = new BooleanSetting("Main");
      Setting var235 = this.resolveSetting2(var234);
      this.booleanValueSetting19 = var235;
      BooleanSetting var236 = new BooleanSetting("Dynamic");
      Setting var237 = var236.resolveSetting7("Only renders box when attacking");
      Setting var238 = this.resolveSetting2(var237);
      this.booleanValueSetting21 = var238;
      Color var240 = new Color(255, 255, 255, 50);
      ColorSetting var239 = new ColorSetting("Box", var240);
      ColorSetting var241 = var239.getColorSetting();
      Setting var242 = this.resolveSetting2(var241);
      this.colorSetting = var242;
      Color var244 = Color.WHITE;
      ColorSetting var243 = new ColorSetting("Line", var244);
      ColorSetting var245 = var243.getColorSetting();
      Setting var246 = this.resolveSetting2(var245);
      this.colorSetting4 = var246;
      Float var248 = (float)1.0F;
      Float var249 = (float)0.0F;
      Float var250 = (float)3.0F;
      NumberSetting var247 = new NumberSetting<>("Width", var248, var249, var250);
      Setting var251 = this.resolveSetting2(var247);
      this.floatValueSetting3 = var251;
      LongNumberSetting var252 = new LongNumberSetting("Fade", 500L, -1L, 3000L, true & true);
      Long var253 = -1L;
      NumberSetting var254 = var252.resolveNumberSetting("Disabled", var253);
      Setting var255 = this.resolveSetting2(var254);
      this.longValueSetting = var255;
      BooleanSetting var256 = new BooleanSetting("Click");
      Setting var257 = var256.resolveSetting7("Simulate legit clicking without performing attack");
      Setting var258 = this.resolveSetting2(var257);
      this.booleanValueSetting15 = var258;
      BooleanSetting var259 = new BooleanSetting("ClickBlock");
      Setting var260 = var259.resolveSetting7("Use autoblock while clicking");
      Setting var261 = this.resolveSetting2(var260);
      this.booleanValueSetting22 = var261;
      Double var263 = 3.4;
      Double var264 = 0.0;
      Double var265 = 10.0;
      NumberSetting var262 = new NumberSetting<>("ClickRange", var263, var264, var265);
      Setting var266 = var262.resolveSetting7("Pre-aim range for clicking");
      Setting var267 = this.resolveSetting2(var266);
      this.doubleValueSetting3 = var267;
      Float var269 = (float)360.0F;
      Float var270 = (float)15.0F;
      Float var271 = (float)360.0F;
      NumberSetting var268 = new NumberSetting<>("Angle", var269, var270, var271);
      Setting var272 = var268.resolveSetting7("Maximum scan angle");
      Setting var273 = this.resolveSetting2(var272);
      this.floatValueSetting2 = var273;
      BooleanSetting var274 = new BooleanSetting("Teams");
      Setting var275 = var274.resolveSetting7("Use tab colors to evaluate teammates");
      Setting var276 = this.resolveSetting2(var275);
      this.booleanValueSetting20 = var276;
      BooleanSetting var277 = new BooleanSetting("Breach");
      Setting var278 = var277.resolveSetting7("Uses mace attribute to deal more damage (requires mace)");
      Setting var279 = this.resolveSetting2(var278);
      this.booleanValueSetting14 = var279;
      BooleanSetting var280 = new BooleanSetting("Mace");
      Setting var281 = var280.resolveSetting7("Uses a mace whenever it's possible");
      Setting var282 = this.resolveSetting2(var281);
      this.booleanValueSetting17 = var282;
      Float var284 = (float)1.0F;
      Float var285 = (float)0.1F;
      Float var286 = (float)10.0F;
      NumberSetting var283 = new NumberSetting<>("MaceHeight", var284, var285, var286);
      Setting var287 = this.resolveSetting2(var283);
      this.floatValueSetting4 = var287;
      BooleanSetting var288 = new BooleanSetting("MaceSpam");
      Setting var289 = var288.resolveSetting7("No delay when macing");
      Setting var290 = this.resolveSetting2(var289);
      this.booleanValueSetting23 = var290;
      BooleanSetting var291 = new BooleanSetting("MaceAwait");
      Setting var292 = var291.resolveSetting7("Wait until you can mace hit before hitting");
      Setting var293 = this.resolveSetting2(var292);
      this.booleanValueSetting24 = var293;
      BooleanSetting var294 = new BooleanSetting("Prediction");
      Setting var295 = this.resolveSetting2(var294);
      this.booleanValueSetting25 = var295;
      Integer var297 = 1;
      Integer var298 = 1;
      Integer var299 = 5;
      NumberSetting var296 = new NumberSetting<>("PredictionTicks", var297, var298, var299);
      Setting var300 = this.resolveSetting2(var296);
      this.integerSetting7 = var300;
      AutoBlockHandler var301 = new AutoBlockHandler(this);
      this.autoBlockHandler = var301;
      org.phobos.api.TimeGate.Normal var302 = new org.phobos.api.TimeGate.Normal();
      this.normal2 = var302;
      AnimatedVec3d var303 = new AnimatedVec3d();
      this.animatedVec3d = var303;
      TimedFloatAnimator var304 = new TimedFloatAnimator();
      this.timedFloatAnimator2 = var304;
      TimedFloatAnimator var305 = new TimedFloatAnimator();
      this.timedFloatAnimator = var305;
      FloatAnimator var306 = new FloatAnimator();
      this.floatAnimator = var306;
      float[] var307 = new float[2];
      this.floatArray = var307;
      this.intValue = 0;
      this.intValue2 = 0;
      Normal var308 = new Normal();
      this.normal = var308;
      Setting var309 = this.booleanValueSetting8;
      Setting[] var310 = new Setting[5];
      Setting var311 = this.booleanValueSetting10;
      var310[0] = var311;
      Setting var312 = this.autoBlockSetting;
      var310[1] = var312;
      Setting var313 = this.unblockSetting;
      var310[2] = var313;
      Setting var314 = this.booleanValueSetting13;
      var310[3] = var314;
      Setting var315 = this.integerSetting3;
      var310[4] = var315;
      this.resolveSettingGroup(var309, var310);
      Setting var316 = this.booleanValueSetting15;
      Setting[] var317 = new Setting[2];
      Setting var318 = this.booleanValueSetting22;
      var317[0] = var318;
      Setting var319 = this.doubleValueSetting3;
      var317[1] = var319;
      this.resolveSettingGroup(var316, var317);
      Setting var320 = this.booleanValueSetting17;
      Setting[] var321 = new Setting[3];
      Setting var322 = this.floatValueSetting4;
      var321[0] = var322;
      Setting var323 = this.booleanValueSetting24;
      var321[1] = var323;
      Setting var324 = this.booleanValueSetting23;
      var321[2] = var324;
      this.resolveSettingGroup(var320, var321);
      Builder var325 = this.createBuilder();
      Builder var326 = var325.resolveBuilder4("Delay");
      SettingGroup[] var327 = new SettingGroup[2];
      Setting var328 = this.booleanValueSetting4;
      Setting[] var329 = new Setting[2];
      Setting var330 = this.integerSetting;
      var329[0] = var330;
      Setting var331 = this.integerSetting2;
      var329[1] = var331;
      SettingGroup var332 = this.resolveSettingGroup(var328, var329);
      var327[0] = var332;
      Setting var333 = this.booleanValueSetting3;
      Setting[] var334 = new Setting[1];
      Setting var335 = this.booleanValueSetting2;
      var334[0] = var335;
      SettingGroup var336 = this.resolveSettingGroup(var333, var334);
      var327[1] = var336;
      Builder var337 = var326.resolveBuilder5(var327);
      Setting[] var338 = new Setting[2];
      Setting var339 = this.timingSetting;
      var338[0] = var339;
      Setting var340 = this.mutableSetting3;
      var338[1] = var340;
      Builder var341 = var337.resolveBuilder(var338);
      var341.getSettingSubsection();
      Builder var342 = this.createBuilder();
      Setting var343 = this.booleanValueSetting16;
      Builder var344 = var342.resolveBuilder6(var343);
      SettingGroup[] var345 = new SettingGroup[2];
      Setting var346 = this.booleanValueSetting19;
      Setting[] var347 = new Setting[4];
      Setting var348 = this.booleanValueSetting21;
      var347[0] = var348;
      Setting var349 = this.colorSetting;
      var347[1] = var349;
      Setting var350 = this.colorSetting4;
      var347[2] = var350;
      Setting var351 = this.floatValueSetting3;
      var347[3] = var351;
      SettingGroup var352 = this.resolveSettingGroup(var346, var347);
      var345[0] = var352;
      Setting var353 = this.booleanValueSetting18;
      Setting[] var354 = new Setting[3];
      Setting var355 = this.colorSetting2;
      var354[0] = var355;
      Setting var356 = this.colorSetting3;
      var354[1] = var356;
      Setting var357 = this.floatValueSetting;
      var354[2] = var357;
      SettingGroup var358 = this.resolveSettingGroup(var353, var354);
      var345[1] = var358;
      Builder var359 = var344.resolveBuilder5(var345);
      Setting[] var360 = new Setting[1];
      Setting var361 = this.longValueSetting;
      var360[0] = var361;
      Builder var362 = var359.resolveBuilder(var360);
      var362.getSettingSubsection();
      Builder var363 = this.createBuilder();
      Setting var364 = this.booleanValueSetting6;
      Builder var365 = var363.resolveBuilder6(var364);
      SettingGroup[] var366 = new SettingGroup[2];
      Setting var367 = this.booleanValueSetting7;
      Setting[] var368 = new Setting[2];
      Setting var369 = this.integerSetting4;
      var368[0] = var369;
      Setting var370 = this.integerSetting6;
      var368[1] = var370;
      SettingGroup var371 = this.resolveSettingGroup(var367, var368);
      var366[0] = var371;
      Setting var372 = this.booleanValueSetting11;
      Setting[] var373 = new Setting[1];
      Setting var374 = this.integerSetting5;
      var373[0] = var374;
      SettingGroup var375 = this.resolveSettingGroup(var372, var373);
      var366[1] = var375;
      Builder var376 = var365.resolveBuilder5(var366);
      Setting[] var377 = new Setting[2];
      Setting var378 = this.mutableSetting5;
      var377[0] = var378;
      Setting var379 = this.mutableSetting6;
      var377[1] = var379;
      Builder var380 = var376.resolveBuilder(var377);
      var380.getSettingSubsection();
      Setting var381 = this.booleanValueSetting25;
      Setting[] var382 = new Setting[1];
      Setting var383 = this.integerSetting7;
      var382[0] = var383;
      this.resolveSettingGroup(var381, var382);
      var var384 = new SafeListener<PlayerTickEvent>(18) {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            AuraModule.this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
         }
      };
      this.handleListener(var384);
      var var385 = new SafeListener<RenderContext>() {
         public void handleObject(
            RenderContext renderContext,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (AuraModule.this.entity == null) {
               if (AuraModule.this.vec3d2 != null) {
                  AuraModule.this.vec3d2 = null;
               }
            } else {
               if (AuraModule.this.booleanValueSetting16.getValue()) {
                  if (AuraModule.this.booleanValueSetting18.getValue()) {
                     if (AuraModule.this.vec3d != null && AuraModule.this.flag) {
                        if (AuraModule.this.flag4) {
                           AuraModule.this.animatedVec3d.handleVec3d2(AuraModule.this.vec3d);
                           AuraModule.this.flag4 = false;
                        }

                        AuraModule.this.animatedVec3d.handleVec3d(AuraModule.this.vec3d);
                        AuraModule.this.animatedVec3d.handleFloat(10.0F, 0.1F);
                        Render3d.handleCuboid(
                           Cuboid.createBuilder()
                              .resolveBuilder11(new Box(AuraModule.this.animatedVec3d.createVec3d(), AuraModule.this.animatedVec3d.createVec3d()).expand(0.02))
                              .resolveBuilder18(AuraModule.this.colorSetting2.getValue(), AuraModule.this.colorSetting3.getValue())
                              .resolveBuilder12(AuraModule.this.floatValueSetting.getValue())
                              .getCuboid()
                        );
                     } else {
                        AuraModule.this.flag4 = true;
                     }
                  }

                  if (AuraModule.this.flag && AuraModule.this.booleanValueSetting19.getValue()) {
                     Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.getIntValue() == 1);
                     org.phobos.core.render.Render3d.Cuboid.Builder builder = Cuboid.createBuilder()
                        .resolveBuilder11(EntityInterpolation.resolveBox(AuraModule.this.entity, renderContext.getFloatValue()))
                        .resolveBuilder18(AuraModule.this.colorSetting.getValue(), AuraModule.this.colorSetting4.getValue())
                        .resolveBuilder12(AuraModule.this.floatValueSetting3.getValue())
                        .resolveBuilder5(AuraModule.this.longValueSetting.getValue())
                        .resolveBuilder15(1);
                     if (AuraModule.this.booleanValueSetting21.getValue()) {
                        AuraModule.this.floatAnimator.handleFloat3(10.0F, 0.02F);
                        builder.resolveBuilder16(AuraModule.this.colorSetting.getValue().getAlpha() / 255.0F * AuraModule.this.floatAnimator.getFloatValue2());
                        builder.resolveBuilder8(AuraModule.this.colorSetting4.getValue().getAlpha() / 255.0F * AuraModule.this.floatAnimator.getFloatValue2());
                     }

                     Render3d.handleCuboid(builder.getCuboid());
                  }
               }

               if (AuraModule.this.entity != null) {
                  AuraModule.this.vec3d2 = AuraModule.this.resolveVec3d2(
                     AuraModule.this.vectorSetting.getValue(),
                     clientPlayerEntity,
                     clientWorld,
                     AuraModule.this.resolveEntity(AuraModule.this.entity),
                     (vec3d, vec3d2) -> {
                        if (!AuraModule.this.raytraceSetting.testObject2(AuraModule.Raytrace.Legit)) {
                           return true;
                        }

                        HitResult hitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, FluidHandling.NONE, clientPlayerEntity));
                        return hitResult == null || !hitResult.getType().equals(Type.BLOCK);
                     }
                  );
               }

               if (AuraModule.this.booleanValueSetting6.getValue() && AuraModule.this.vec3d2 != null) {
                  if (AuraModule.this.booleanValueSetting7.getValue()) {
                     if (RaycastUtils.testEntity3(
                        clientPlayerEntity,
                        AuraModule.this.entity,
                        new float[]{AuraModule.this.timedFloatAnimator.getFloatValue2(), AuraModule.this.timedFloatAnimator2.getFloatValue2()}
                     )) {
                        if (AuraModule.this.intValue-- > 0) {
                           return;
                        }
                     } else {
                        AuraModule.this.intValue = AuraModule.this.integerSetting4.getValue()
                           + (AuraModule.this.integerSetting6.testObject2(0) ? 0 : MathUtils.computeInt(AuraModule.this.integerSetting6.getValue()));
                     }
                  }

                  float f = AuraModule.this.mutableSetting5.getValue().getNumber4();
                  if (f == 100.0F) {
                     AuraModule.this.timedFloatAnimator.setFloatValue2(AuraModule.this.timedFloatAnimator.getFloatValue());
                     AuraModule.this.timedFloatAnimator2.setFloatValue2(AuraModule.this.timedFloatAnimator2.getFloatValue());
                     return;
                  }

                  float f2 = f / 10.0F;
                  SecureRandom secureRandom = new SecureRandom();
                  AuraModule.this.timedFloatAnimator
                     .handleFloat4(
                        MathUtils.computeFloat8(5.0F, 25.0F) * f2,
                        MathUtils.computeFloat8(0.01F, 0.3F) * f2,
                        (floatValue, floatValue2) -> secureRandom.nextBoolean() ? floatValue : floatValue2
                     );
                  AuraModule.this.timedFloatAnimator2
                     .handleFloat4(
                        MathUtils.computeFloat8(5.0F, 25.0F) * f2,
                        MathUtils.computeFloat8(0.01F, 0.3F) * f2,
                        (floatValue, floatValue2) -> secureRandom.nextBoolean() ? floatValue : floatValue2
                     );
               }
            }
         }
      };
      this.handleListener(var385);
      var var386 = new SafeListener<TickEvent>(-6) {
         public void handleObject(
            TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (AuraModule.this.jumpSetting.testObject2(AuraModule.Jump.Strafe)
               && clientPlayerEntity.isOnGround()
               && RotationManager.isFlag()
               && tickEvent.getKeyboardInput().jumping
               && AuraModule.this.flag2) {
               tickEvent.getKeyboardInput().movementForward = 1.0F;
               tickEvent.getKeyboardInput().movementSideways = 0.0F;
            }

            AuraModule.this.flag2 = false;
         }
      };
      this.handleListener(var386);
      auraModule = this;
   }

   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      boolean flag,
      boolean flag2
   ) {
      if (!flag2) {
         Function[] var6 = new Function[1];
         Function<Slot, Slot> var23 = AuraModule::resolveSlot5;
         var6[0] = var23;
         Object var24 = itemSwitcher.resolveObject(var6);
         boolean var25 = var24 instanceof Slot;
         if (var24 != null && !var25) {
            throw new ClassCastException("CHECKCAST failed");
         }

         Function[] var27 = new Function[1];
         Function<Slot, Slot> var44 = AuraModule::resolveSlot;
         var27[0] = var44;
         Object var45 = itemSwitcher.resolveObject(var27);
         boolean var46 = var45 instanceof Slot;
         if (var45 != null && !var46) {
            throw new ClassCastException("CHECKCAST failed");
         }

         Function[] var66 = new Function[1];
         Function<Slot, Slot> var83 = AuraModule::resolveSlot3;
         var66[0] = var83;
         Object var84 = itemSwitcher.resolveObject(var66);
         boolean var85 = var84 instanceof Slot;
         if (var84 != null && !var85) {
            throw new ClassCastException("CHECKCAST failed");
         }

         Setting var86 = this.booleanValueSetting17;
         Object var87 = var86.getValue();
         boolean var88 = var87 instanceof Boolean;
         if (var87 != null && !var88) {
            throw new ClassCastException("CHECKCAST failed");
         }

         byte var94;
         label130: {
            boolean var89 = (Boolean)var87;
            if (var89) {
               float var90 = clientPlayerEntity.fallDistance;
               Setting var91 = this.floatValueSetting4;
               Object var92 = var91.getValue();
               boolean var93 = var92 instanceof Float;
               if (var92 != null && !var93) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               byte var99;
               label126: {
                  float var98 = (Float)var92;
                  if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var90))) && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var98)))) {
                     if (Float.intBitsToFloat(Float.floatToRawIntBits(var90)) > Float.intBitsToFloat(Float.floatToRawIntBits(var98))) {
                        var99 = 1;
                        break label126;
                     }

                     if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var90)))
                        && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(var98)))
                        && Float.intBitsToFloat(Float.floatToRawIntBits(var90)) == Float.intBitsToFloat(Float.floatToRawIntBits(var98))) {
                        var99 = 0;
                        break label126;
                     }
                  }

                  var99 = -1;
               }

               if (var99 >= 0 && var24 != null) {
                  var94 = 1;
                  break label130;
               }
            }

            var94 = 0;
         }

         Setting var95 = this.booleanValueSetting3;
         Object var96 = var95.getValue();
         boolean var97 = var96 instanceof Boolean;
         if (var96 != null && !var97) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var104;
         label112: {
            boolean var100 = (Boolean)var96;
            if (var100) {
               Setting var101 = this.booleanValueSetting2;
               Object var102 = var101.getValue();
               boolean var103 = var102 instanceof Boolean;
               if (var102 != null && !var103) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var109 = (Boolean)var102;
               if (var109) {
                  var104 = true;
                  break label112;
               }
            }

            var104 = false;
         }

         ItemStack var105 = clientPlayerEntity.getMainHandStack();
         InventoryUtils.testItemStack2(var105);
         Setting var106 = this.booleanValueSetting12;
         Object var107 = var106.getValue();
         boolean var108 = var107 instanceof Boolean;
         if (var107 != null && !var108) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var113;
         label105: {
            boolean var110 = (Boolean)var107;
            if (var110) {
               Entity var111 = this.entity;
               boolean var112 = this.testEntity(var111);
               if (var112) {
                  var113 = true;
                  break label105;
               }
            }

            var113 = false;
         }

         label143: {
            ItemStack var114 = clientPlayerEntity.getMainHandStack();
            Item var115 = var114.getItem();
            boolean var116 = InventoryUtils.testItem4(var115);
            Hand var118;
            int var119;
            long var120;
            Object var122;
            if (var104 && var84 != null) {
               Hand var117 = Hand.MAIN_HAND;
               var118 = var117;
               var119 = 7180240;
               var120 = 1L;
               var122 = var84;
            } else if (!var113) {
               Setting var126 = this.switchValueSetting;
               AuraModule.Switch var127 = AuraModule.Switch.Silent;
               boolean var128 = var126.testObject2(var127);
               if (!var128 || var116 || var45 == null) {
                  break label143;
               }

               Hand var129 = Hand.MAIN_HAND;
               Setting var130 = this.silentSetting;
               Object var131 = var130.getValue();
               boolean var132 = var131 instanceof Silent;
               if (var131 != null && !var132) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               int var176 = ((Silent)var131).getFlag();
               long var10001 = (var176 | 1) & 4294967295L;
               var118 = var129;
               var119 = 7180632;
               var120 = var10001;
               var122 = var45;
            } else {
               Hand var158 = Hand.MAIN_HAND;
               Function<Slot, Slot> var175 = AuraModule::resolveSlot2;
               var118 = var158;
               var119 = 7180488;
               var120 = 1L;
               var122 = var175;
            }

            switch (var119) {
               case 7180240:
               default:
                  itemSwitcher.resolveSwitchResult(var118, (Slot)var122, (int)var120);
                  break;
               case 7180488:
                  itemSwitcher.resolveSwitchResult2(var118, (Function<Slot, Slot>)var122, (int)var120);
                  break;
               case 7180632:
                  itemSwitcher.resolveSwitchResult(var118, (Slot)var122, (int)var120);
            }
         }

         this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientPlayerInteractionManager, flag, true & true);
         if ((int)(var94 & 4294967295L) == 0) {
            Setting var123 = this.booleanValueSetting14;
            Object var124 = var123.getValue();
            boolean var125 = var124 instanceof Boolean;
            if (var124 != null && !var125) {
               throw new ClassCastException("CHECKCAST failed");
            }

            boolean var134 = (Boolean)var124;
            if (var134) {
               Slot var135 = itemSwitcher.getSlot3();
               ItemStack var136 = var135.getStack();
               Item var137 = var136.getItem();
               Function[] var138 = new Function[1];
               Function<Slot, Slot> var155 = AuraModule::resolveSlot4;
               var138[0] = var155;
               Object var156 = itemSwitcher.resolveObject(var138);
               boolean var157 = var156 instanceof Slot;
               if (var156 != null && !var157) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               if (var156 != null) {
                  Hand var177 = Hand.MAIN_HAND;
                  itemSwitcher.resolveSwitchResult(var177, (Slot)var156, 2);
                  PhobosClient var178 = this.phobosClient;
                  Scheduler var179 = var178.getScheduler();
                  AuraModule var10000 = this;
                  Item cap63 = var137;
                  AuraModule cap62 = var10000;
                  Runnable var196 = () -> cap62.handleItem(cap63);
                  var179.handleRunnable2(var196, 0);
               }
            }
         } else {
            Hand var133 = Hand.MAIN_HAND;
            itemSwitcher.resolveSwitchResult(var133, (Slot)var24, 3);
            this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientPlayerInteractionManager, flag, true & true);
         }
      } else if (!flag) {
         Entity var26 = this.entity;
         this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager, var26);
      } else {
         PhobosClient var47 = this.phobosClient;
         RotationManager var48 = var47.getRotationManager();
         AuraModule var202 = this;
         ClientPlayerEntity var203 = clientPlayerEntity;
         ClientPlayerInteractionManager cap61 = clientPlayerInteractionManager;
         ClientPlayerEntity cap60 = var203;
         AuraModule cap59 = var202;
         Runnable var65 = () -> cap59.handleClientPlayerEntity5(cap60, cap61);
         var48.setRunnable(var65);
      }
   }

   public void handleItem(Item item) {
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, 2));
   }

   @Override
   public String nothing() {
      if (this.entity == null) {
         return null;
      } else if (this.entity instanceof PlayerEntity playerEntity) {
         return playerEntity.getNameForScoreboard();
      } else {
         EntityType<?> entityType = this.entity.getType();
         String text = entityType.getTranslationKey();
         return Text.translatable(text).getString();
      }
   }

   public static Slot resolveSlot(Slot slot) {
      return InventoryUtils.testItem4(slot.getStack().getItem()) ? slot : null;
   }

   public static Slot resolveSlot2(Slot slot) {
      return slot.getStack().getItem() instanceof AxeItem ? slot : null;
   }

   public void handleClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager) {
      this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager, this.entity);
   }

   @Override
   public void run4() {
      if (this.autoBlockHandler.check() && mc.player != null) {
         this.autoBlockHandler.handleClientPlayerEntity2(mc.player);
      }

      ClientHolder.getEventBus().handleObject4(this.autoBlockHandler);
   }

   public static AuraModule getAuraModule() {
      return auraModule;
   }

   public static Slot resolveSlot3(Slot slot) {
      return InventoryUtils.testItemStack(slot.getStack()) ? slot : null;
   }

   public boolean check5() {
      return this.doubleValueSetting.getValue() >= 4.5;
   }

   public static Slot resolveSlot4(Slot slot) {
      if (EnchantmentUtils.testItemStack(slot.getStack(), Enchantments.BREACH)) {
         return slot;
      } else {
         return InventoryUtils.testItemStack2(slot.getStack()) ? slot : null;
      }
   }

   @Override
   public void run3() {
      ClientHolder.getEventBus().handleObject(this.autoBlockHandler);
      this.flag4 = true;
      this.entity = null;
      if (mc.player != null) {
         this.timedFloatAnimator.setFloatValue2(mc.player.getYaw());
         this.timedFloatAnimator2.setFloatValue2(mc.player.getPitch());
      }
   }

   public Setting<Double> getDoubleValueSetting() {
      return this.doubleValueSetting;
   }

   public static Slot resolveSlot5(Slot slot) {
      return InventoryUtils.testItemStack2(slot.getStack()) ? slot : null;
   }

   public void handleClientPlayerEntity6(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager, ItemSwitcher itemSwitcher
   ) {
      boolean flag5 = clientPlayerEntity.getMainHandStack().getItem() instanceof SwordItem;
      this.entity = this.resolveEntity2(clientWorld, clientPlayerEntity);
      if (this.entity != null) {
         Entity entity = this.resolveEntity(this.entity);
         if (this.booleanValueSetting8.getValue() && !this.autoBlockHandler.check()) {
            switch ((AutoBlock)this.autoBlockSetting.getValue()) {
               case Partial:
               case Override:
               case Watchdog:
                  this.autoBlockHandler.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager);
            }
         }

         Vec3d vec3d2 = this.vec3d2;
         label226:
         if (vec3d2 != null) {
            if (clientPlayerEntity.isOnGround() && clientPlayerEntity.input.jumping) {
               switch ((AuraModule.Jump)this.jumpSetting.getValue()) {
                  case Correction:
                     break label226;
                  case Strafe:
                     float f = PlayerUtils.computeClientPlayerEntity6(clientPlayerEntity);
                     this.phobosClient.getRotationManager().testFloat2(f, FreeLookModule.computeFloat2(clientPlayerEntity.getPitch()), true);
                     this.flag2 = true;
                     break label226;
               }
            }

            this.floatArray = this.values(clientPlayerEntity, this.entity, vec3d2);
            label221:
            if (!this.switchValueSetting.getValue().equals(AuraModule.Switch.Require) || InventoryUtils.testItem4(clientPlayerEntity.getMainHandStack().getItem())) {
               boolean flag3 = this.testDistance(this.distanceSetting.getValue(), clientPlayerEntity, clientWorld, entity, vec3d2, this.value());
               MutableBoolean mutableBoolean = new MutableBoolean(false);
               if (!this.booleanValueSetting15.getValue() && !flag3) {
                  if (!this.check()) {
                     break label221;
                  }

                  Vec3d vec3d = this.resolveVec3d(clientPlayerEntity, clientWorld, this.entity);
                  if (vec3d == null) {
                     break label221;
                  }

                  this.phobosClient.getMovementSimulator().setSilentMovement(new SilentMovement(vec3d.getX(), vec3d.getY(), vec3d.getZ()));
                  mutableBoolean.setTrue();
               }

               this.flag = false;
               if (this.entity != null) {
                  if (flag3
                     || mutableBoolean.getValue()
                     || this.testDistance(this.distanceSetting.getValue(), clientPlayerEntity, clientWorld, entity, vec3d2, this.doubleValueSetting3.getValue())
                     )
                   {
                     this.flag = true;
                     if (this.rotateSetting.getValue().equals(AuraModule.Rotate.Hold)) {
                        this.phobosClient.getRotationManager().testFloat(this.floatArray[0], this.floatArray[1]);
                     }
                  }

                  label253:
                  if (flag3 || mutableBoolean.getValue()) {
                     this.handleClientPlayerEntity4(clientPlayerEntity);
                     if (!this.testItemSwitcher(itemSwitcher, clientPlayerEntity)) {
                        return;
                     }

                     if (CriticalsModule.check2() && (CriticalsModule.check() || mc.options.jumpKey.isPressed())) {
                        boolean flag2 = clientPlayerEntity.fallDistance > 0.0F
                           && !clientPlayerEntity.isOnGround()
                           && !clientPlayerEntity.isClimbing()
                           && !clientPlayerEntity.isTouchingWater();
                        if (!flag2) {
                           if (CriticalsModule.check() && clientPlayerEntity.isOnGround() && PlayerUtils.computeClientPlayerEntity(clientPlayerEntity) >= 1) {
                              clientPlayerEntity.jump();
                              PlayerActionUtils.handleClientPlayerEntity24(clientPlayerEntity, 0.0);
                              PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, clientPlayerEntity.getVelocity().getY() / 8.0);
                              PlayerActionUtils.handleClientPlayerEntity26(clientPlayerEntity, 0.0);
                           }

                           return;
                        }
                     }

                     if (this.booleanValueSetting8.getValue()) {
                        switch ((AutoBlock)this.autoBlockSetting.getValue()) {
                           case Partial:
                           default:
                              break;
                           case Override:
                           case Watchdog:
                              if (this.autoBlockHandler.check()) {
                                 this.autoBlockHandler.handleClientPlayerEntity2(clientPlayerEntity);
                              }
                              break;
                           case None:
                              if (!this.autoBlockHandler.testInt(this.integerSetting3.getValue())) {
                                 this.flag3 = true;
                                 break label253;
                              }

                              this.flag3 = false;
                              break;
                           case Vanilla:
                              if (!this.unblockSetting.testObject2(Unblock.None)) {
                                 this.autoBlockHandler.handleClientPlayerEntity2(clientPlayerEntity);
                              }
                        }
                     }

                     switch ((AuraModule.Rotate)this.rotateSetting.getValue()) {
                        case Strict:
                           PlayerActionUtils.handleClientPlayerEntity17(clientPlayerEntity, this.floatArray[0], this.floatArray[1], clientPlayerEntity.isOnGround());
                           break;
                        case Instant:
                        case Fast:
                        case Silent:
                           this.phobosClient.getRotationManager().testFloat(this.floatArray[0], this.floatArray[1]);
                        case Normal:
                        case Hold:
                        default:
                           break;
                        case Manual:
                           this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, this.floatArray[0], this.floatArray[1]);
                     }

                     boolean flag4 = RaycastUtils.testEntity3(clientPlayerEntity, entity, this.floatArray) || Objects.equals(mc.targetedEntity, this.entity);
                     double d = this.mutableSetting4.getValue().getNumber4();
                     if (!flag4 && d != 100.0 && Math.random() * 100.0 > d) {
                        mc.doAttack();
                        return;
                     }

                     if (this.rotateSetting.getValue().raycast) {
                        switch ((AuraModule.Raytrace)this.raytraceSetting.getValue()) {
                           case None:
                           case Legit:
                              if (!flag4) {
                                 return;
                              }
                              break;
                           case Immediate:
                              if (!RaycastUtils.testPhobosClient(this.phobosClient, this.entity)) {
                                 return;
                              }
                        }
                     }

                     if (!this.raytraceSetting.getValue().equals(AuraModule.Raytrace.Normal)
                        || !this.rotateSetting.getValue().raycast
                        || RaycastUtils.testPhobosClient(this.phobosClient, this.entity)) {
                        this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientPlayerInteractionManager, mutableBoolean.getValue(), false);
                        if (this.booleanValueSetting8.getValue() && this.booleanValueSetting13.getValue() && flag5) {
                           this.autoBlockHandler.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager);
                        }

                        if (this.rotateSetting.getValue().equals(AuraModule.Rotate.Silent)) {
                           this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                        }

                        return;
                     }
                  } else {
                     label198:
                     if (this.booleanValueSetting15.getValue()
                        && this.testDistance(
                           this.distanceSetting.getValue(), clientPlayerEntity, clientWorld, entity, vec3d2, this.doubleValueSetting3.getValue()
                        )) {
                        if (this.testItemSwitcher(itemSwitcher, clientPlayerEntity)) {
                           this.run();
                           boolean flag = this.booleanValueSetting22.getValue() && this.booleanValueSetting8.getValue();
                           if (flag) {
                              switch ((AutoBlock)this.autoBlockSetting.getValue()) {
                                 case Partial:
                                 default:
                                    break;
                                 case Override:
                                 case Watchdog:
                                    if (this.autoBlockHandler.check()) {
                                       this.autoBlockHandler.handleClientPlayerEntity2(clientPlayerEntity);
                                    }
                                    break;
                                 case None:
                                    if (!this.autoBlockHandler.testInt(this.integerSetting3.getValue())) {
                                       this.flag3 = true;
                                       break label198;
                                    }

                                    this.flag3 = false;
                              }
                           }

                           clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                           this.normal2.run();
                           if (flag) {
                              if (this.booleanValueSetting8.getValue() && this.booleanValueSetting13.getValue() && flag5) {
                                 this.autoBlockHandler.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager);
                              }

                              this.handleClientPlayerEntity4(clientPlayerEntity);
                              return;
                           }
                        }

                        this.handleClientPlayerEntity4(clientPlayerEntity);
                     }
                  }
               }
            }
         }
      }

      if (this.booleanValueSetting8.getValue() && flag5) {
         switch ((AutoBlock)this.autoBlockSetting.getValue()) {
            case Partial:
            case Override:
            case Watchdog:
               BiPredicate<Entity, Double> biPredicate = (entity, doubleValue) -> this.vec3d2 == null
                  ? false
                  : this.testDistance(this.distanceSetting.getValue(), clientPlayerEntity, clientWorld, entity, this.vec3d2, this.value());
               if (this.entity != null && biPredicate.test(this.entity, this.value())
                  || this.booleanValueSetting15.getValue()
                     && this.booleanValueSetting22.getValue()
                     && this.booleanValueSetting8.getValue()
                     && this.entity != null
                     && biPredicate.test(this.entity, this.doubleValueSetting3.getValue())) {
                  return;
               }
            default:
               if (this.autoBlockHandler.check()) {
                  this.autoBlockHandler.handleClientPlayerEntity2(clientPlayerEntity);
               }
               break;
            case None:
               if (this.flag3) {
                  this.autoBlockHandler.handleClientPlayerEntity2(clientPlayerEntity);
               }

               return;
         }
      }
   }

   public enum Distance {
      Hitbox {
         @Override
         public boolean isInRange(Vec3d source, ClientWorld world, Entity target, Vec3d point, double range) {
            return target.getBoundingBox().squaredMagnitude(source) < MathHelper.square(range);
         }
      },
      Vector {
         @Override
         public boolean isInRange(Vec3d source, ClientWorld world, Entity target, Vec3d point, double range) {
            return point.squaredDistanceTo(source) < MathHelper.square(range);
         }
      };

      public boolean isInRange(Vec3d source, ClientWorld world, Entity target, Vec3d point, double range) {
         return false;
      }
   }

   public enum Jump {
      None,
      Correction,
      Strafe;
   }

   private enum Raytrace implements TextProvider {
      None("No raytrace checks"),
      Immediate("Raytrace via client rotation"),
      Normal("Raytrace via server rotation"),
      Legit("Legit raytrace (does not hit through walls)");

      public final String text;

      Raytrace(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Rotate {
      None(false),
      Instant(false),
      Normal(true),
      Hold(true),
      Fast(true),
      Strict(true),
      Manual(true),
      Silent(false);

      public final boolean raycast;

      Rotate(boolean raycast) {
         this.raycast = raycast;
      }

      public boolean isRaycast() {
         return this.raycast;
      }
   }

   public enum Source {
      Default {
         @Override
         public float getEyeHeight(ClientPlayerEntity player) {
            return player.getEyeHeight(player.getPose());
         }
      },
      Sneak {
         @Override
         public float getEyeHeight(ClientPlayerEntity player) {
            return player.getEyeHeight(EntityPose.CROUCHING);
         }
      },
      Swim {
         @Override
         public float getEyeHeight(ClientPlayerEntity player) {
            return player.getEyeHeight(EntityPose.SWIMMING);
         }
      };

      public Vec3d getSourceVector(ClientPlayerEntity player) {
         return player.getPos().add(0.0, this.getEyeHeight(player), 0.0);
      }

      public Vec3d getInterpolatedVector(ClientPlayerEntity player, float delta) {
         return EntityInterpolation.createVec3d(player, delta).add(0.0, this.getEyeHeight(player), 0.0);
      }

      public abstract float getEyeHeight(ClientPlayerEntity var1);
   }

   private record Spread(float offset, boolean random) {
      public static final AuraModule.Spread INSTANCE = new AuraModule.Spread(0.0F, false);

      public float computeFloat(float f) {
         return this.random ? MathUtils.computeFloat9(f) : 0.0F;
      }
   }

   public enum Swing {
      Normal,
      Packet,
      None;
   }

   private enum Switch {
      Require,
      Silent,
      None;
   }

   private enum Target implements TextProvider {
      Distance("Sort by distance"),
      Health("Sort by health"),
      Angle("Sort by angle");

      public final String text;

      Target(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Timing {
      Normal,
      Fast,
      Custom;
   }

   public enum Vector {
      Default,
      Closest {
         @Override
         public Vec3d calculate(Vec3d source, ClientPlayerEntity player, ClientWorld world, Entity target, BiPredicate<Vec3d, Vec3d> test) {
            float delta = TimerManager.getFloatValue();
            Box box = EntityInterpolation.resolveBox(target, delta);
            List<Vec3d> valid = new ArrayList<>();
            List<Entity> entities = List.of(target);

            for (Vec3d pos : BoxUtils.values2(box, 4, 8, 4)) {
               HitResult result = RaycastUtils.createEntityHitResult2(player, entities, source, pos, box.expand(10.0), target::equals, MathHelper.square(10.0));
               if (result != null) {
                  Vec3d position = result.getPos();
                  if (test.test(source, position)) {
                     valid.add(position);
                  }
               }
            }

            if (valid.isEmpty()) {
               return null;
            }

            double bestDistance = Float.MAX_VALUE;
            Vec3d bestVec = null;

            for (Vec3d vec : valid) {
               double distance = source.distanceTo(vec);
               if (distance < bestDistance) {
                  bestVec = vec;
                  bestDistance = distance;
               }
            }

            return bestVec;
         }
      },
      Feet {
         @Override
         public Vec3d calculate(Vec3d source, ClientPlayerEntity player, ClientWorld world, Entity target, BiPredicate<Vec3d, Vec3d> test) {
            return new Vec3d(target.getX(), target.getY(), target.getZ());
         }
      },
      Eyes {
         @Override
         public Vec3d calculate(Vec3d source, ClientPlayerEntity player, ClientWorld world, Entity target, BiPredicate<Vec3d, Vec3d> test) {
            return target.getEyePos();
         }
      };

      public Vec3d calculate(Vec3d source, ClientPlayerEntity player, ClientWorld world, Entity target, BiPredicate<Vec3d, Vec3d> test) {
         return new Vec3d(target.getX(), target.getY() + target.getStandingEyeHeight() / 2.0, target.getZ());
      }
   }
}
