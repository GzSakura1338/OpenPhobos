package org.phobos.core.anticheat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.client.AntiCheatModule;

public class AntiCheatPlayer implements MinecraftHolder {
   public AntiCheatCheckRegistry antiCheatCheckRegistry;
   public Vec3d vec3d;
   public Vec3d vec3d2;
   public Vec2f vec2f;
   public Vec3d vec3d3 = Vec3d.ZERO;
   public boolean flag;
   public ItemStack itemStack;
   public Vec3d vec3d4;
   public boolean flag2;
   public Vec3d vec3d5;
   public Vec3d vec3d6;
   public final AntiCheatModule antiCheatModule;
   public Vec2f vec2f2;
   public AbstractClientPlayerEntity abstractClientPlayerEntity;
   public boolean flag3;
   public Vec3d vec3d7;
   public ItemStack itemStack2;
   public boolean flag4;
   public ItemStack itemStack3;
   public ItemStack itemStack4;
   public Vec3d vec3d8;
   public int intValue;
   public ScheduledExecutorService scheduledExecutorService2;
   public double doubleValue;
   public double doubleValue2;
   public boolean flag5;
   public int intValue2;
   public List<Boolean> booleanValueList;
   public int intValue3;
   public int intValue4;
   public float floatValue;
   public boolean flag6;
   public boolean flag7;
   public long longValue;
   public boolean flag8;
   public List<Vec3d> vec3dList;
   public boolean flag9;
   public List<Boolean> booleanValueList2;
   public boolean flag10;
   public List<Vec2f> vec2fList;
   public List<Vec3d> vec3dList2;
   public boolean flag11;

   public static AntiCheatPlayer createAntiCheatPlayer(AbstractClientPlayerEntity abstractClientPlayerEntity, AntiCheatModule antiCheatModule) {
      return new AntiCheatPlayer(abstractClientPlayerEntity, false, antiCheatModule);
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public Vec3d getVec3d3() {
      return this.vec3d3;
   }

   public Vec3d getVec3d2() {
      return this.vec3d2;
   }

   public Vec3d getVec3d6() {
      return this.vec3d6;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Vec3d getVec3d8() {
      return this.vec3d8;
   }

   public ItemStack getItemStack4() {
      return this.itemStack4;
   }

   public Vec2f getVec2f() {
      return this.vec2f;
   }

   public ItemStack getItemStack2() {
      return this.itemStack;
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public Vec3d getVec3d5() {
      return this.vec3d5;
   }

   public void handleAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
      this.vec3d3 = abstractClientPlayerEntity.getPos();
      this.vec3d2 = new Vec3d(
         abstractClientPlayerEntity.getVelocity().x, abstractClientPlayerEntity.getVelocity().y, abstractClientPlayerEntity.getVelocity().z
      );
      this.itemStack4 = abstractClientPlayerEntity.getMainHandStack();
      this.itemStack3 = abstractClientPlayerEntity.getOffHandStack();
      this.flag = abstractClientPlayerEntity.isSprinting();
      this.flag2 = abstractClientPlayerEntity.handSwinging;
      this.vec2f = new Vec2f(abstractClientPlayerEntity.getPitch(), abstractClientPlayerEntity.getYaw());
      this.flag10 = abstractClientPlayerEntity.isOnGround();
      this.flag11 = abstractClientPlayerEntity.isUsingItem();
      if (this.flag10) {
         this.intValue4 = 0;
      } else {
         this.intValue4++;
      }

      this.doubleValue = (
            abstractClientPlayerEntity.getActiveStatusEffects().containsKey(StatusEffects.SPEED)
               ? ((StatusEffectInstance)abstractClientPlayerEntity.getActiveStatusEffects().get(StatusEffects.SPEED)).getAmplifier() * 0.2 + 1.0
               : 1.0
         )
         * abstractClientPlayerEntity.getMovementSpeed()
         * 10.0;
      this.run2();
      if (mc.getNetworkHandler() != null) {
         PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(abstractClientPlayerEntity.getUuid());
         if (playerListEntry != null) {
            this.intValue = playerListEntry.getLatency();
         }
      }

      if (this.flag10) {
         this.vec3d4 = this.vec3d8;
         this.vec3d8 = this.vec3d3;
         this.flag8 = false;
         if (abstractClientPlayerEntity.isTouchingWater()) {
            this.vec3d7 = this.vec3d3;
         }
      }

      if (abstractClientPlayerEntity.isTouchingWater() || abstractClientPlayerEntity.isInLava()) {
         this.vec3d5 = this.vec3d3;
      }

      this.antiCheatCheckRegistry.run6();
      this.vec3d6 = this.vec3d3;
      this.vec3d = this.vec3d2;
      this.itemStack = this.itemStack4;
      this.itemStack2 = this.itemStack3;
      this.flag3 = this.flag;
      this.flag4 = this.flag2;
      this.vec2f2 = this.vec2f;
      this.flag5 = this.flag6;
      this.flag6 = this.flag10;
      this.flag7 = abstractClientPlayerEntity.isUsingItem();
      this.floatValue = abstractClientPlayerEntity.fallDistance;
      this.longValue++;
      this.run();
   }

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   public ItemStack getItemStack() {
      return this.itemStack2;
   }

   public boolean alwaysTrue() {
      return false;
   }

   public Vec3d getVec3d4() {
      return this.vec3d4;
   }

   public ItemStack getItemStack3() {
      return this.itemStack3;
   }

   public void run() {
      if (this.antiCheatModule.getClear().getValue()) {
         if (this.longValue % this.antiCheatModule.getVL().getValue() == 0L) {
            this.intValue3 = 0;
            this.intValue2 = 0;
            this.antiCheatCheckRegistry.handleConsumer(antiCheatCheck -> antiCheatCheck.intValue2 = 0);
         }
      }
   }

   public void run2() {
      if (this.vec3dList.size() >= 20) {
         this.vec3dList.removeLast();
      }

      this.vec3dList.addFirst(this.vec3d3);
      if (this.vec3dList2.size() >= 20) {
         this.vec3dList2.removeLast();
      }

      this.vec3dList2.addFirst(this.vec3d2);
      if (this.booleanValueList.size() >= 20) {
         this.booleanValueList.removeLast();
      }

      this.booleanValueList.addFirst(this.flag);
      if (this.booleanValueList2.size() >= 20) {
         this.booleanValueList2.removeLast();
      }

      this.booleanValueList2.addFirst(this.flag10);
      if (this.vec2fList.size() >= 20) {
         this.vec2fList.removeLast();
      }

      this.vec2fList.addFirst(this.vec2f);
   }

   public AntiCheatPlayer(AbstractClientPlayerEntity abstractClientPlayerEntity, boolean flag, AntiCheatModule antiCheatModule) {
      this.vec3d2 = Vec3d.ZERO;
      this.itemStack4 = ItemStack.EMPTY;
      this.itemStack3 = ItemStack.EMPTY;
      this.flag = false;
      this.flag2 = false;
      this.vec3d6 = Vec3d.ZERO;
      this.vec3d = Vec3d.ZERO;
      this.itemStack = ItemStack.EMPTY;
      this.itemStack2 = ItemStack.EMPTY;
      this.flag3 = false;
      this.flag4 = false;
      this.vec3d8 = Vec3d.ZERO;
      this.vec3d4 = Vec3d.ZERO;
      this.vec3d5 = Vec3d.ZERO;
      this.vec3d7 = Vec3d.ZERO;
      this.vec2f = Vec2f.ZERO;
      this.vec2f2 = Vec2f.ZERO;
      this.vec3dList = new ArrayList<>(20);
      this.booleanValueList2 = new ArrayList<>(20);
      this.vec3dList2 = new ArrayList<>(20);
      this.booleanValueList = new ArrayList<>(20);
      this.vec2fList = new ArrayList<>(20);
      this.floatValue = 0.0F;
      this.flag10 = true;
      this.flag6 = true;
      this.flag5 = true;
      this.flag9 = false;
      this.flag8 = false;
      this.flag7 = false;
      this.flag11 = false;
      this.doubleValue = 1.0;
      this.doubleValue2 = 1.0;
      this.longValue = 0L;
      this.intValue = 0;
      this.intValue4 = 0;
      this.intValue3 = 0;
      this.intValue2 = 0;
      this.scheduledExecutorService2 = Executors.newScheduledThreadPool(4);
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
      this.antiCheatModule = antiCheatModule;
      this.antiCheatCheckRegistry = flag ? AntiCheatCheckRegistry.createAntiCheatCheckRegistry2((AntiCheatScheduler)this) : AntiCheatCheckRegistry.createAntiCheatCheckRegistry(this);
      this.vec3d3 = abstractClientPlayerEntity.getPos();
      this.vec3d2 = new Vec3d(
         abstractClientPlayerEntity.getVelocity().x, abstractClientPlayerEntity.getVelocity().y, abstractClientPlayerEntity.getVelocity().z
      );
      this.flag = abstractClientPlayerEntity.isSprinting();
      this.flag2 = abstractClientPlayerEntity.handSwinging;
      this.vec2f = new Vec2f(abstractClientPlayerEntity.getPitch(), abstractClientPlayerEntity.getYaw());
      this.flag10 = this.flag6 = this.flag5 = abstractClientPlayerEntity.isOnGround();

      for (int i = 0; i < 20; i++) {
         this.vec3dList.add(this.vec3d3);
      }

      for (int i2 = 0; i2 < 20; i2++) {
         this.vec3dList2.add(this.vec3d2);
      }

      for (int i3 = 0; i3 < 20; i3++) {
         this.booleanValueList.add(this.flag);
      }

      for (int i4 = 0; i4 < 20; i4++) {
         this.booleanValueList2.add(this.flag10);
      }

      for (int i5 = 0; i5 < 20; i5++) {
         this.vec2fList.add(this.vec2f);
      }
   }

   public Vec3d getVec3d7() {
      return this.vec3d7;
   }

   public AntiCheatCheckRegistry getAntiCheatCheckRegistry() {
      return this.antiCheatCheckRegistry;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public ScheduledExecutorService getScheduledExecutorService2() {
      return this.scheduledExecutorService2;
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public List<Vec3d> getVec3dList2() {
      return this.vec3dList2;
   }

   public AntiCheatModule getAntiCheatModule() {
      return this.antiCheatModule;
   }

   public boolean isFlag9() {
      return this.flag9;
   }

   public List<Boolean> getBooleanValueList2() {
      return this.booleanValueList2;
   }

   public boolean isFlag11() {
      return this.flag11;
   }

   public List<Vec2f> getVec2fList() {
      return this.vec2fList;
   }

   public boolean isFlag7() {
      return this.flag7;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public List<Boolean> getBooleanValueList() {
      return this.booleanValueList;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public int getIntValue4() {
      return this.intValue4;
   }

   public List<Vec3d> getVec3dList() {
      return this.vec3dList;
   }

   public boolean isFlag10() {
      return this.flag10;
   }

   public Vec2f getVec2f2() {
      return this.vec2f2;
   }

   public boolean isFlag8() {
      return this.flag8;
   }
}
