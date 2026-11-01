package org.phobos.module.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IDamageProtectionEntity;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleLookup;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.combat.CriticalsModule;

public class FakePlayerModule extends Module {
   public final Setting<Boolean> play = this.resolveSetting2(new BooleanSetting("Play", false));
   public final List<FakePlayerModule.Position> positionList;
   public final Setting<Boolean> loop;
   public static final int intValue = -30095827;
   public final Setting<Boolean> recordValue = this.resolveSetting2(new BooleanSetting("Record", false));
   public int intValue2;
   public final Setting<String> name2;
   public boolean flag;
   public int intValue3;
   public final Setting<Boolean> heal;
   public final TimeGate.Normal normal;
   public final Setting<Boolean> totem;
   public final MutableObject<FakePlayer> fakePlayerMutableObject;
   public static FakePlayerModule fakePlayerModule;
   public final Setting<Long> healDelay;
   public final Setting<Boolean> damage;

   @Override
   public void run4() {
      ClientWorld clientWorld = mc.world;
      if (clientWorld != null) {
         clientWorld.removeEntity(-30095827, RemovalReason.DISCARDED);
         this.fakePlayerMutableObject.setValue(null);
      }

      this.play.setValue(false);
      this.recordValue.setValue(false);
   }

   public void handleExplosionS2CPacket(ExplosionS2CPacket explosionS2CPacket, ClientWorld clientWorld) {
      if (this.fakePlayerMutableObject.getValue() != null && ((FakePlayer)this.fakePlayerMutableObject.getValue()).hurtTime == 0) {
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).onDamaged(clientWorld.getDamageSources().generic());
         ((FakePlayer)this.fakePlayerMutableObject.getValue())
            .setHealth(
               EntityUtils.computeLivingEntity((LivingEntity)this.fakePlayerMutableObject.getValue())
                  - this.phobosClient
                     .getDamageCalculator()
                     .computeEntity4(
                        (Entity)this.fakePlayerMutableObject.getValue(),
                        clientWorld,
                        6.0F,
                        explosionS2CPacket.getX(),
                        explosionS2CPacket.getY(),
                        explosionS2CPacket.getZ()
                     )
            );
         if (((FakePlayer)this.fakePlayerMutableObject.getValue()).isDead()
            && ((FakePlayer)this.fakePlayerMutableObject.getValue()).tryUseTotem(clientWorld.getDamageSources().generic())) {
            ((FakePlayer)this.fakePlayerMutableObject.getValue()).setHealth(10.0F);
            this.run();
         }
      }
   }

   public void run() {
      if (mc.player != null && this.fakePlayerMutableObject.getValue() != null) {
         new EntityStatusS2CPacket((Entity)this.fakePlayerMutableObject.getValue(), (byte)35).apply(mc.player.networkHandler);
      }
   }

   public FakePlayerModule(PhobosClient phobosClient) {
      super(phobosClient, "FakePlayer", Categories.misc, "Spawns a fake player");
      this.loop = this.resolveSetting2(new BooleanSetting("Loop", false));
      this.damage = this.resolveSetting2(new BooleanSetting("Damage", true));
      this.heal = this.resolveSetting2(new BooleanSetting("Heal", true));
      this.healDelay = this.resolveSetting2(new LongNumberSetting("HealDelay", 1600L, 1500L, 2000L, true));
      this.totem = this.resolveSetting2(new BooleanSetting("Totem", true));
      this.name2 = this.resolveSetting2(new StringSetting("Name", "FakePlayer"));
      this.fakePlayerMutableObject = new MutableObject();
      this.positionList = new ArrayList<>();
      this.normal = new TimeGate.Normal();
      this.handleListener(
         new SafeListener<Send<PlayerInteractEntityC2SPacket>>() {
            public void handleObject(
               Send<PlayerInteractEntityC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!send.isCancelled()
                  && FakePlayerModule.this.fakePlayerMutableObject.getValue() != null
                  && ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).hurtTime == 0) {
                  Entity entity = ((IPlayerInteractEntityC2SPacket)(Object)send.getPacket()).entity();
                  if (((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).equals(entity)) {
                     send.setCancelled(true);
                     phobosClient.getPhobosSounds().handleClientPlayerEntity(clientPlayerEntity, clientWorld, true);
                     ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).onDamaged(clientWorld.getDamageSources().generic());
                     if (clientPlayerEntity.getAttackCooldownProgress(0.5F) >= 0.85F) {
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue())
                           .setHealth(
                              EntityUtils.computeLivingEntity((LivingEntity)FakePlayerModule.this.fakePlayerMutableObject.getValue())
                                 - FakePlayerModule.this.computeItemStack(clientPlayerEntity.getMainHandStack(), clientPlayerEntity, clientWorld)
                           );
                     } else {
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue())
                           .setHealth(EntityUtils.computeLivingEntity((LivingEntity)FakePlayerModule.this.fakePlayerMutableObject.getValue()) - 1.0F);
                     }

                     if (((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).isDead()
                        && ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).tryUseTotem(clientWorld.getDamageSources().generic())) {
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setHealth(10.0F);
                        FakePlayerModule.this.run();
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PostReceive<ExplosionS2CPacket>>() {
            public void handleObject(
               PostReceive<ExplosionS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!postReceive.isCancelled() && FakePlayerModule.this.damage.getValue()) {
                  FakePlayerModule.this.handleExplosionS2CPacket((ExplosionS2CPacket)postReceive.getPacket(), clientWorld);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PlayerMoveEvent>() {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               FakePlayerModule.this.run6();
               if (!FakePlayerModule.this.recordValue.getValue()) {
                  if (FakePlayerModule.this.play.getValue()) {
                     if (FakePlayerModule.this.positionList.isEmpty()) {
                        FakePlayerModule.this.getPhobosClient()
                           .getChatManager()
                           .handleText9(Text.literal("No recording found!").formatted(Formatting.RED), "FakePlayerModule");
                        FakePlayerModule.this.play.setValue(false);
                        return;
                     }

                     if (FakePlayerModule.this.intValue2 >= FakePlayerModule.this.positionList.size()) {
                        if (!FakePlayerModule.this.loop.getValue()) {
                           FakePlayerModule.this.play.setValue(false);
                        }

                        FakePlayerModule.this.intValue2 = 0;
                     }

                     if (FakePlayerModule.this.intValue3++ % 2 == 0) {
                        FakePlayerModule.Position position = FakePlayerModule.this.positionList.get(FakePlayerModule.this.intValue2++);
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setYaw(position.getFloatValue());
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setPitch(position.getFloatValue2());
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setHeadYaw(position.getFloatValue3());
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue())
                           .updateTrackedPositionAndAngles(
                              position.getDoubleValue(),
                              position.getDoubleValue3(),
                              position.getDoubleValue2(),
                              position.getFloatValue(),
                              position.getFloatValue2(),
                              3
                           );
                        ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setVelocity(position.getVec3d());
                     }
                  } else {
                     FakePlayerModule.this.intValue2 = 0;
                     ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setVelocity(Vec3d.ZERO);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<UnusedEvent>() {
            public void handleObject(
               UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               FakePlayerModule.this.run6();
               if (FakePlayerModule.this.recordValue.getValue()) {
                  FakePlayerModule.this.play.setValue(false);
                  ((FakePlayer)FakePlayerModule.this.fakePlayerMutableObject.getValue()).setVelocity(Vec3d.ZERO);
                  if (!FakePlayerModule.this.flag) {
                     FakePlayerModule.this.positionList.clear();
                     FakePlayerModule.this.flag = true;
                  }

                  if (FakePlayerModule.this.intValue3++ % 2 == 0) {
                     FakePlayerModule.this.positionList.add(new FakePlayerModule.Position(clientPlayerEntity));
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleModule(this);
      fakePlayerModule = this;
   }

   @Override
   public void run6() {
      if (!this.recordValue.getValue() && this.flag) {
         this.flag = false;
      }

      if (this.heal.getValue() && this.fakePlayerMutableObject.getValue() != null && this.normal.testNumber(this.healDelay.getValue())) {
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).setHealth(36.0F);
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).setAbsorptionAmount(16.0F);
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0));
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
         assert mc.player != null;
         ((IDamageProtectionEntity)(Object)this.fakePlayerMutableObject.getValue()).setIntValue(((IDamageProtectionEntity)(Object)mc.player).getIntValue());
         this.normal.run();
      }

      if (this.totem.getValue()
         && this.fakePlayerMutableObject.getValue() != null
         && !((FakePlayer)this.fakePlayerMutableObject.getValue()).getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
         ((FakePlayer)this.fakePlayerMutableObject.getValue()).setStackInHand(Hand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
      }
   }

   public static FakePlayerModule getFakePlayerModule() {
      return fakePlayerModule;
   }

   public float computeItemStack(ItemStack itemStack, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      PlayerEntity playerEntity = (PlayerEntity)this.fakePlayerMutableObject.getValue();
      float f = 1.0F;
      if (itemStack.getItem() instanceof SwordItem) {
         f = 7.0F;
      }

      if (itemStack.getItem() instanceof AxeItem) {
         f = 9.0F;
      }

      if (clientPlayerEntity.fallDistance > 0.0F || ModuleLookup.createModuleHandle(CriticalsModule.class).check()) {
         f += f / 2.0F;
      }

      if (clientPlayerEntity.hasStatusEffect(StatusEffects.STRENGTH)) {
         int i = Objects.requireNonNull(clientPlayerEntity.getStatusEffect(StatusEffects.STRENGTH)).getAmplifier() + 1;
         f += 3 * i;
      }

      return DamageUtil.getDamageLeft(
         playerEntity, f, clientWorld.getDamageSources().generic(), ((FakePlayer)this.fakePlayerMutableObject.getValue()).getArmor(), 32.0F
      );
   }

   @Override
   public void run3() {
      if (mc.world != null && mc.player != null) {
         try {
            FakePlayer fakePlayer = new FakePlayer(mc.world, this.name2.getValue());
            fakePlayer.setId(-30095827);
            fakePlayer.copyFrom(mc.player);

            for (int i = 0; i < fakePlayer.getInventory().size(); i++) {
               fakePlayer.getInventory().setStack(i, mc.player.getInventory().getStack(i));
            }

            mc.world.addEntity(fakePlayer);
            fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
            fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
            fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0));
            fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
            this.fakePlayerMutableObject.setValue(fakePlayer);
         } catch (Throwable throwable) {
            System.out.println("An unexpected error was dropped, please try again");
            throwable.printStackTrace();
            this.run11();
         }
      } else {
         System.out.println("An unexpected error was dropped, please try again");
         this.run11();
      }
   }

   private static final class Position {
      public float floatValue;
      public double doubleValue;
      public float floatValue2;
      public float floatValue3;
      public double doubleValue2;
      public Vec3d vec3d;
      public double doubleValue3;

      public void setDoubleValue3(double d) {
         this.doubleValue3 = d;
      }

      public double getDoubleValue3() {
         return this.doubleValue3;
      }

      public void setVec3d(Vec3d vec3d) {
         this.vec3d = vec3d;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public Position(ClientPlayerEntity clientPlayerEntity) {
         this.doubleValue = clientPlayerEntity.getX();
         this.doubleValue3 = clientPlayerEntity.getY();
         this.doubleValue2 = clientPlayerEntity.getZ();
         this.floatValue2 = clientPlayerEntity.getPitch();
         this.floatValue = clientPlayerEntity.getYaw();
         this.floatValue3 = clientPlayerEntity.getHeadYaw();
         this.vec3d = clientPlayerEntity.getVelocity();
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }
   }
}
