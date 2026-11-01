package org.phobos.core.fakeplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.AttributeModifiersComponent.Entry;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.earthhack.ducks.ILivingEntity;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.net.interfaces.IEntityPositionS2SPacket;
import org.phobos.module.client.FakePlayerModule2;
import org.phobos.module.client.FakePlayerModule2.Movement;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.entity.EntityPositionUtils;
import org.phobos.core.Feature;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.combat.SurroundManager;
import org.phobos.core.event.events.UnusedEvent;

public class PlayerStateTracker extends Feature {
   public final List<FakePlayer> fakePlayerList;
   @Nullable
   public FakePlayer fakePlayer;
   public final PhobosClient phobosClient;
   @Nullable
   public FakePlayer fakePlayer2;
   public final List<FakePlayer> fakePlayerList2 = new ArrayList<>();
   public final AtomicInteger atomicInteger = new AtomicInteger(-2352352);

   public void run() {
      List var1 = this.fakePlayerList2;
      boolean var2 = var1.isEmpty();
      if (!var2) {
         List var3 = this.fakePlayerList2;
         Object var4 = var3.getLast();
         boolean var5 = var4 instanceof FakePlayer;
         if (var4 != null && !var5) {
            throw new ClassCastException("CHECKCAST failed");
         }

         this.fakePlayer2 = (FakePlayer)var4;
      }
   }

   public void handleFakePlayer(FakePlayer fakePlayer, DamageSource damageSource, float f) {
      if (!fakePlayer.isInvulnerableTo(damageSource)) {
         f = this.computeFakePlayer2(fakePlayer, damageSource, f);
         f = this.computeFakePlayer(fakePlayer, damageSource, f);
         float f2 = f;
         f = Math.max(f - fakePlayer.getAbsorptionAmount(), 0.0F);
         fakePlayer.setAbsorptionAmount(fakePlayer.getAbsorptionAmount() - (f2 - f));
         float f3 = f2 - f;
         if (f3 > 0.0F && f3 < 3.4028235E37F) {
            fakePlayer.increaseStat(Stats.DAMAGE_ABSORBED, Math.round(f3 * 10.0F));
         }

         if (f != 0.0F) {
            fakePlayer.addExhaustion(damageSource.getExhaustion());
            fakePlayer.getDamageTracker().onDamage(damageSource, f);
            fakePlayer.setHealth(fakePlayer.getHealth() - f);
            if (f < 3.4028235E37F) {
               fakePlayer.increaseStat(Stats.DAMAGE_TAKEN, Math.round(f * 10.0F));
            }

            fakePlayer.emitGameEvent(GameEvent.ENTITY_DAMAGE);
         }
      }
   }

   public void handleItemStack(ItemStack itemStack, @NotNull FakePlayer fakePlayer, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      PlayerEntity playerEntity = this.phobosClient.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
      DamageSource damageSource = clientPlayerEntity.getDamageSources().playerAttack(clientPlayerEntity);
      float f4 = clientPlayerEntity.isUsingRiptide()
         ? clientPlayerEntity.riptideAttackDamage
         : this.computeItemStack(itemStack, EntityAttributes.GENERIC_ATTACK_DAMAGE);
      float f5 = EnchantmentUtils.computeItemStack4(itemStack, fakePlayer, f4);
      float f6 = ((ILivingEntity)(Object)clientPlayerEntity).getLastAttackCooldown();
      f4 *= 0.2F + f6 * f6 * 0.8F;
      f5 *= f6;
      clientPlayerEntity.resetLastAttackedTicks();
      if (f4 > 0.0F || f5 > 0.0F) {
         boolean flag = f6 > 0.9F;
         boolean flag2 = clientPlayerEntity.isSprinting() && flag;
         if (flag2) {
            clientWorld.playSound(
               clientPlayerEntity,
               clientPlayerEntity.getX(),
               clientPlayerEntity.getY(),
               clientPlayerEntity.getZ(),
               SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK,
               clientPlayerEntity.getSoundCategory(),
               1.0F,
               1.0F
            );
         }

         f4 += itemStack.getItem().getBonusAttackDamage(fakePlayer, f4, damageSource);
         boolean flag3 = flag
            && clientPlayerEntity.fallDistance > 0.0F
            && !clientPlayerEntity.isOnGround()
            && !clientPlayerEntity.isClimbing()
            && !clientPlayerEntity.isTouchingWater()
            && !clientPlayerEntity.hasStatusEffect(StatusEffects.BLINDNESS)
            && !clientPlayerEntity.hasVehicle()
            && !clientPlayerEntity.isSprinting();
         if (flag3) {
            f4 *= 1.5F;
         }

         float f3 = f4 + f5;
         boolean flag4 = false;
         double d3 = clientPlayerEntity.horizontalSpeed - clientPlayerEntity.prevHorizontalSpeed;
         if (flag && !flag3 && !flag2 && clientPlayerEntity.isOnGround() && d3 < clientPlayerEntity.getMovementSpeed()) {
            ItemStack itemStack2 = clientPlayerEntity.getStackInHand(Hand.MAIN_HAND);
            if (itemStack2.getItem() instanceof SwordItem) {
               flag4 = true;
            }
         }

         if (this.testFakePlayer(fakePlayer, damageSource, f3)) {
            if (FakePlayerModule2.getFakePlayerModule2().getKnockback().getValue()) {
               float f = this.computeItemStack(itemStack, EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
               float f2 = EnchantmentUtils.computeItemStack2(itemStack, fakePlayer, f) + (flag2 ? 1.0F : 0.0F);
               if (f2 > 0.0F) {
                  fakePlayer.takeKnockback(
                     f2 * 0.5F,
                     MathHelper.sin(playerEntity.getYaw() * (float) (Math.PI / 180.0)),
                     -MathHelper.cos(playerEntity.getYaw() * (float) (Math.PI / 180.0))
                  );
               }
            }

            if (flag4) {
               for (LivingEntity livingEntity : clientWorld.getNonSpectatingEntities(LivingEntity.class, fakePlayer.getBoundingBox().expand(1.0, 0.25, 1.0))) {
                  if (livingEntity != clientPlayerEntity
                     && livingEntity != fakePlayer
                     && !clientPlayerEntity.isTeammate(livingEntity)
                     && livingEntity instanceof ArmorStandEntity
                     && !((ArmorStandEntity)livingEntity).isMarker()) {
                  }
               }

               clientWorld.playSound(
                  clientPlayerEntity,
                  clientPlayerEntity.getX(),
                  clientPlayerEntity.getY(),
                  clientPlayerEntity.getZ(),
                  SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                  clientPlayerEntity.getSoundCategory(),
                  1.0F,
                  1.0F
               );
               double d = -MathHelper.sin(playerEntity.getYaw() * (float) (Math.PI / 180.0));
               double d2 = MathHelper.cos(playerEntity.getYaw() * (float) (Math.PI / 180.0));
               clientWorld.addParticle(
                  ParticleTypes.SWEEP_ATTACK, true, clientPlayerEntity.getX() + d, clientPlayerEntity.getBodyY(0.5), clientPlayerEntity.getZ() + d2, d, 0.0, d2
               );
            }

            if (flag3) {
               clientWorld.playSound(
                  clientPlayerEntity,
                  clientPlayerEntity.getX(),
                  clientPlayerEntity.getY(),
                  clientPlayerEntity.getZ(),
                  SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                  clientPlayerEntity.getSoundCategory(),
                  1.0F,
                  1.0F
               );
               clientPlayerEntity.addCritParticles(fakePlayer);
            }

            if (!flag3 && !flag4) {
               if (flag) {
                  clientWorld.playSound(
                     clientPlayerEntity,
                     clientPlayerEntity.getX(),
                     clientPlayerEntity.getY(),
                     clientPlayerEntity.getZ(),
                     SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                     clientPlayerEntity.getSoundCategory(),
                     1.0F,
                     1.0F
                  );
               } else {
                  clientWorld.playSound(
                     clientPlayerEntity,
                     clientPlayerEntity.getX(),
                     clientPlayerEntity.getY(),
                     clientPlayerEntity.getZ(),
                     SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                     clientPlayerEntity.getSoundCategory(),
                     1.0F,
                     1.0F
                  );
               }
            }

            if (f5 > 0.0F) {
               clientPlayerEntity.addEnchantedHitParticles(fakePlayer);
            }

            clientPlayerEntity.onAttacking(fakePlayer);
         } else {
            clientWorld.playSound(
               clientPlayerEntity,
               clientPlayerEntity.getX(),
               clientPlayerEntity.getY(),
               clientPlayerEntity.getZ(),
               SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE,
               clientPlayerEntity.getSoundCategory(),
               1.0F,
               1.0F
            );
         }
      }
   }

   public boolean check() {
      return !this.fakePlayerList2.isEmpty();
   }

   public PlayerStateTracker(PhobosClient phobosClient) {
      this.fakePlayerList = new ArrayList<>();
      this.phobosClient = phobosClient;
      this.handleListener(
         new SafeListener<PlayerMoveEvent>() {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (FakePlayer fakePlayer : PlayerStateTracker.this.fakePlayerList) {
                  if (fakePlayer.getPathRecorder() != null) {
                     fakePlayer.getPathRecorder().handleFakePlayer(fakePlayer);
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
               if (PlayerStateTracker.this.fakePlayer2 != null) {
                  PlayerStateTracker.this.fakePlayer2.run();
                  PlayerStateTracker.this.fakePlayer2.getPathRecorder().handleClientPlayerEntity(clientPlayerEntity, PlayerStateTracker.this.fakePlayer2);
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
               if (!postReceive.isCancelled() && FakePlayerModule2.getFakePlayerModule2().getDamage().getValue()) {
                  ExplosionS2CPacket explosionS2CPacket = postReceive.getPacket();

                  for (FakePlayer fakePlayer : PlayerStateTracker.this.fakePlayerList2) {
                     if (fakePlayer.hurtTime != 0) {
                        return;
                     }

                     DamageSource damageSource = clientWorld.getDamageSources().explosion(null);
                     fakePlayer.onDamaged(damageSource);
                     fakePlayer.applyDamage(
                        damageSource,
                        phobosClient.getDamageCalculator()
                           .computeEntity4(fakePlayer, clientWorld, 6.0F, explosionS2CPacket.getX(), explosionS2CPacket.getY(), explosionS2CPacket.getZ())
                     );
                     if (fakePlayer.isDead() && fakePlayer.tryUseTotem(damageSource)) {
                        PlayerStateTracker.this.handleLivingEntity(fakePlayer);
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Send<PlayerInteractEntityC2SPacket>>() {
            public void handleObject(
               Send<PlayerInteractEntityC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!send.isCancelled() && FakePlayerModule2.getFakePlayerModule2().getDamage().getValue()) {
                  Entity entity = ((IPlayerInteractEntityC2SPacket)(Object)send.getPacket()).entity();

                  for (FakePlayer fakePlayer : PlayerStateTracker.this.fakePlayerList2) {
                     if (fakePlayer.equals(entity)) {
                        PlayerStateTracker.this.handleItemStack(clientPlayerEntity.getMainHandStack(), fakePlayer, clientPlayerEntity, clientWorld);
                        send.setCancelled(true);
                        return;
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (FakePlayer fakePlayer : PlayerStateTracker.this.fakePlayerList2) {
                  if (FakePlayerModule2.getFakePlayerModule2().getHeal().getValue() && fakePlayer.getTimeGate().testLong(1600L)) {
                     fakePlayer.setAbsorptionAmount(16.0F);
                     fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
                     fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
                     fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0));
                     fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
                     fakePlayer.getTimeGate().run();
                  }

                  if (!fakePlayer.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
                     fakePlayer.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
                  }

                  switch ((Movement)FakePlayerModule2.getFakePlayerModule2().getMovement().getValue()) {
                     case Forward:
                        float f = fakePlayer.getYaw();
                        double[] doubles = PlayerUtils.values(1.0, f);
                        MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld).resolveMovingFakePlayer(fakePlayer);
                        movingFakePlayer.setFlag3(true);
                        Vec3d vec3d = movingFakePlayer.getVelocity();
                        Vec3d vec3d2 = EntityPositionUtils.resolveVec3d(movingFakePlayer, new Vec3d(doubles[0], -0.0784, doubles[1]), 2.0F);
                        movingFakePlayer.move(MovementType.SELF, vec3d2);
                        fakePlayer.setVelocity(vec3d2);
                        EntityPositionS2CPacket entityPositionS2CPacket = new EntityPositionS2CPacket(movingFakePlayer);
                        ((IEntityPositionS2SPacket)(Object)entityPositionS2CPacket).setEntityId(fakePlayer.getId());
                        entityPositionS2CPacket.apply(clientPlayerEntity.networkHandler);
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::clear);
   }

   public float computeFakePlayer(FakePlayer fakePlayer, DamageSource damageSource, float f) {
      if (damageSource.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
         return f;
      }

      if (fakePlayer.hasStatusEffect(StatusEffects.RESISTANCE) && !damageSource.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
         int i = (fakePlayer.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
         int i2 = 25 - i;
         float f2 = f * i2;
         float f3 = f;
         f = Math.max(f2 / 25.0F, 0.0F);
         float var8 = f3 - f;
      }

      if (f <= 0.0F) {
         return 0.0F;
      }

      if (damageSource.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
         return f;
      }

      float f4 = fakePlayer.getIntValue2();
      if (f4 > 0.0F) {
         f = DamageUtil.getInflictedDamage(f, f4);
      }

      return f;
   }

   public float computeFakePlayer2(FakePlayer fakePlayer, DamageSource damageSource, float f) {
      if (!damageSource.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
         f = DamageUtil.getDamageLeft(fakePlayer, f, damageSource, fakePlayer.getArmor(), fakePlayer.getIntValue2());
      }

      return f;
   }

   public boolean testFakePlayer(FakePlayer fakePlayer, DamageSource damageSource, float f) {
      if (fakePlayer.isInvulnerableTo(damageSource)) {
         return false;
      }

      if (EnchantmentUtils.testLivingEntity(fakePlayer)) {
         return false;
      }

      if (fakePlayer.isDead()) {
         return false;
      }

      if (damageSource.isIn(DamageTypeTags.IS_FIRE) && fakePlayer.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
         return false;
      }

      if (damageSource.isIn(DamageTypeTags.DAMAGES_HELMET) && !fakePlayer.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
         f *= 0.75F;
      }

      if (fakePlayer.hurtTime == 0) {
         fakePlayer.limbAnimator.setSpeed(1.5F);
      }

      if (fakePlayer.timeUntilRegen > 10.0F) {
         if (f <= fakePlayer.lastDamageTaken) {
            return false;
         }

         this.handleFakePlayer(fakePlayer, damageSource, f - fakePlayer.lastDamageTaken);
         fakePlayer.lastDamageTaken = f;
      } else {
         fakePlayer.lastDamageTaken = f;
         fakePlayer.timeUntilRegen = 20;
         this.handleFakePlayer(fakePlayer, damageSource, f);
         fakePlayer.maxHurtTime = 10;
         fakePlayer.hurtTime = fakePlayer.maxHurtTime;
      }

      if (fakePlayer.isDead() && fakePlayer.tryUseTotem(damageSource)) {
         this.handleLivingEntity(fakePlayer);
      }

      for (StatusEffectInstance statusEffectInstance : fakePlayer.getStatusEffects()) {
         statusEffectInstance.onEntityDamage(fakePlayer, damageSource, f);
      }

      return true;
   }

   public void run2() {
      FakePlayer var1 = this.fakePlayer2;
      if (var1 != null) {
         FakePlayer var2 = this.fakePlayer2;
         PathRecorder var3 = var2.getPathRecorder();
         if (var3 != null) {
            FakePlayer var4 = this.fakePlayer2;
            PathRecorder var5 = var4.getPathRecorder();
            var5.stop();
         }

         List var6 = this.fakePlayerList;
         FakePlayer var7 = this.fakePlayer2;
         var6.remove(var7);
         List var8 = this.fakePlayerList;
         FakePlayer var9 = this.fakePlayer2;
         var8.add(var9);
         this.fakePlayer2 = (FakePlayer)null;
      }
   }

   public void remove() {
      List var1 = this.fakePlayerList2;
      boolean var2 = var1.isEmpty();
      if (!var2) {
         MinecraftClient var3 = MinecraftHolder.mc;
         ClientWorld var4 = var3.world;
         if (var4 != null) {
            List var5 = this.fakePlayerList2;
            Object var6 = var5.removeLast();
            boolean var7 = var6 instanceof FakePlayer;
            if (var6 != null && !var7) {
               throw new ClassCastException("CHECKCAST failed");
            }

            if (var6 != null) {
               List var8 = this.fakePlayerList;
               var8.remove(var6);
               FakePlayer var9 = this.fakePlayer2;
               boolean var10 = var9 == var6;
               if (var10) {
                  this.fakePlayer2 = (FakePlayer)null;
               }

               MinecraftClient var11 = MinecraftHolder.mc;
               ClientWorld var12 = var11.world;
               int var13 = ((FakePlayer)var6).getId();
               RemovalReason var14 = RemovalReason.DISCARDED;
               var12.removeEntity(var13, var14);
               AtomicInteger var15 = this.atomicInteger;
               var15.incrementAndGet();
            }
         }
      }
   }

   public void clear() {
      long var5 = 0L;
      MinecraftClient var1 = MinecraftHolder.mc;
      ClientWorld var2 = var1.world;
      if (var2 != null) {
         List var3 = this.fakePlayerList2;
         Iterator var4 = var3.iterator();

         while (true) {
            long var7 = var5;
            long var9 = var7;
            long var11 = var9;
            long var13 = var11;
            long var15 = var13;
            boolean var17 = var4.hasNext();
            long var18 = var15;
            long var20 = var18;
            long var22 = var20;
            if (!var17) {
               List var50 = this.fakePlayerList2;
               var50.clear();
               this.fakePlayer2 = (FakePlayer)null;
               List var51 = this.fakePlayerList;
               var51.clear();
               break;
            }

            long var24 = var22;
            long var26 = var24;
            long var28 = var26;
            long var30 = var28;
            long var32 = var30;
            long var34 = var32;
            long var36 = var34;
            Object var38 = var4.next();
            long var39 = var36;
            long var41 = var39;
            long var43 = var41;
            boolean var45 = var38 instanceof FakePlayer;
            if (var38 != null && !var45) {
               throw new ClassCastException("CHECKCAST failed");
            }

            long var48 = var43;
            long var54 = var48;
            long var56 = var54;
            long var58 = var56;
            MinecraftClient var60 = MinecraftHolder.mc;
            long var61 = var58;
            long var63 = var61;
            long var65 = var63;
            ClientWorld var67 = var60.world;
            long var68 = var65;
            long var70 = var68;
            long var72 = var70;
            long var74 = var72;
            long var76 = var74;
            long var78 = var76;
            long var80 = var78;
            long var82 = var80;
            int var84 = ((FakePlayer)var38).getId();
            long var85 = var82;
            long var87 = var85;
            long var89 = var87;
            long var91 = var89;
            RemovalReason var93 = RemovalReason.DISCARDED;
            long var94 = var91;
            long var96 = var94;
            long var98 = var84 & 4294967295L | var96 & -4294967296L;
            long var100 = var98;
            long var102 = var100;
            long var104 = var102;
            var67.removeEntity((int)var104, var93);
            long var106 = var104;
            long var108 = var106;
            long var110 = var108;
            long var112 = var110;
            AtomicInteger var114 = this.atomicInteger;
            long var115 = var112;
            long var117 = var115;
            long var119 = var117;
            long var121 = var119;
            long var123 = var121;
            long var125 = var123;
            var114.incrementAndGet();
            long var127 = var125;
            long var129 = var127;
            var5 = var129;
         }
      }
   }

   public float computeItemStack(ItemStack itemStack, RegistryEntry<EntityAttribute> registryEntry) {
      AttributeModifiersComponent attributeModifiersComponent = itemStack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
      if (attributeModifiersComponent == null) {
         return 1.0F;
      }

      for (Entry entry : attributeModifiersComponent.modifiers()) {
         RegistryEntry<EntityAttribute> registryEntry2 = entry.attribute();
         if (registryEntry.equals(registryEntry2)) {
            return (float)entry.modifier().value();
         }
      }

      return 1.0F;
   }

   public void handleLivingEntity(LivingEntity livingEntity) {
      if (mc.player != null) {
         livingEntity.setHealth(10.0F);
         livingEntity.clearStatusEffects();
         livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
         livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
         livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
         new EntityStatusS2CPacket(livingEntity, (byte)35).apply(mc.player.networkHandler);
      }
   }

   public void run3() {
      long var31 = 0L;
      this.run2();
      MinecraftClient var1 = MinecraftHolder.mc;
      ClientPlayerEntity var2 = var1.player;
      if (var2 != null) {
         MinecraftClient var3 = MinecraftHolder.mc;
         ClientWorld var4 = var3.world;
         if (var4 != null) {
            List var5 = this.fakePlayerList2;
            int var6 = var5.size();
            FakePlayerModule2 var7 = FakePlayerModule2.getFakePlayerModule2();
            Setting var8 = var7.getLimit();
            Object var9 = var8.getValue();
            boolean var10 = var9 instanceof Integer;
            if (var9 != null && !var10) {
               throw new ClassCastException("CHECKCAST failed");
            }

            int var11 = (Integer)var9;
            if (var6 <= var11) {
               MinecraftClient var13 = MinecraftHolder.mc;
               ClientWorld var14 = var13.world;
               FakePlayerModule2 var15 = FakePlayerModule2.getFakePlayerModule2();
               Setting var16 = var15.getName2();
               Object var17 = var16.getValue();
               boolean var18 = var17 instanceof String;
               if (var17 != null && !var18) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               FakePlayer var12 = new FakePlayer(var14, (String)var17);
               var12.setFlag22(true & true);
               AtomicInteger var19 = this.atomicInteger;
               int var20 = var19.getAndDecrement();
               var12.setId(var20);
               MinecraftClient var21 = MinecraftHolder.mc;
               ClientPlayerEntity var22 = var21.player;
               var12.copyFrom(var22);
               PhobosClient var23 = this.phobosClient;
               SurroundManager var24 = var23.getSurroundManager();
               MinecraftClient var25 = MinecraftHolder.mc;
               ClientPlayerEntity var26 = var25.player;
               Iterable var27 = var26.getArmorItems();
               int var28 = var24.computeIterable(var27);
               var12.setIntValue(var28);
               long var29 = 0L;

               while (true) {
                  long var33 = (int)var29 & 4294967295L | var31 & -4294967296L;
                  long var35 = var33;
                  long var37 = var35;
                  long var39 = var37;
                  long var41 = var39;
                  long var43 = var41;
                  long var45 = var43;
                  PlayerInventory var47 = var12.getInventory();
                  long var48 = var45;
                  long var50 = var48;
                  long var52 = var50;
                  long var54 = var52;
                  long var56 = var54;
                  long var58 = var56;
                  long var60 = var58;
                  int var62 = var47.size();
                  long var63 = var60;
                  long var65 = var63;
                  long var67 = var65;
                  if ((int)((int)var29 & 4294967295L) >= var62) {
                     MinecraftClient var152 = MinecraftHolder.mc;
                     ClientWorld var153 = var152.world;
                     var153.addEntity(var12);
                     AttributeContainer var154 = var12.getAttributes();
                     MinecraftClient var155 = MinecraftHolder.mc;
                     ClientPlayerEntity var156 = var155.player;
                     AttributeContainer var157 = var156.getAttributes();
                     var154.setFrom(var157);
                     var12.clearStatusEffects();
                     RegistryEntry var159 = StatusEffects.REGENERATION;
                     StatusEffectInstance var158 = new StatusEffectInstance(var159, 400, 1);
                     var12.addStatusEffect(var158);
                     RegistryEntry var161 = StatusEffects.RESISTANCE;
                     StatusEffectInstance var160 = new StatusEffectInstance(var161, 6000, 0);
                     var12.addStatusEffect(var160);
                     RegistryEntry var163 = StatusEffects.FIRE_RESISTANCE;
                     StatusEffectInstance var162 = new StatusEffectInstance(var163, 6000, 0);
                     var12.addStatusEffect(var162);
                     RegistryEntry var165 = StatusEffects.ABSORPTION;
                     StatusEffectInstance var164 = new StatusEffectInstance(var165, 2400, 3);
                     var12.addStatusEffect(var164);
                     List var166 = this.fakePlayerList2;
                     var166.add(var12);
                     break;
                  }

                  long var69 = var67;
                  long var71 = var69;
                  long var73 = var71;
                  long var75 = var73;
                  long var77 = var75;
                  long var79 = var77;
                  long var81 = var79;
                  PlayerInventory var83 = var12.getInventory();
                  long var84 = var81;
                  long var86 = var84;
                  long var88 = var86;
                  long var90 = var88;
                  long var92 = var90;
                  long var94 = var92;
                  MinecraftClient var96 = MinecraftHolder.mc;
                  long var97 = var94;
                  long var99 = var97;
                  long var101 = var99;
                  ClientPlayerEntity var103 = var96.player;
                  long var104 = var101;
                  long var106 = var104;
                  long var108 = var106;
                  long var110 = var108;
                  long var112 = var110;
                  long var114 = var112;
                  PlayerInventory var116 = var103.getInventory();
                  long var117 = var114;
                  long var119 = var117;
                  long var121 = var119;
                  long var123 = var121;
                  long var125 = var123;
                  long var127 = var125;
                  long var129 = var127;
                  long var131 = var129;
                  long var133 = var131;
                  ItemStack var135 = var116.getStack((int)((int)((int)var121 & 4294967295L) & 4294967295L));
                  long var136 = var133;
                  long var138 = var136;
                  long var140 = var138;
                  long var142 = var140;
                  long var144 = var142;
                  long var146 = var144;
                  long var148 = var146;
                  var83.setStack((int)((int)((int)var88 & 4294967295L) & 4294967295L), var135);
                  long var150 = var148;
                  long var10000 = (int)var150 + 1 & 4294967295L;
                  var31 = var150;
                  var29 = var10000;
               }
            }
         }
      }
   }
}
