package org.phobos.core.combat;

import lombok.SneakyThrows;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import org.earthhack.ducks.ILivingEntity;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.EntityStatusEvent;
import org.phobos.event.Listener;
import org.phobos.event.RemoveEntityEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.Feature;
import org.phobos.core.FeatureRegistry;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;

public class DamageTracker extends Feature {
   public final Map<DamageTracker.Incrementor, Long> incrementorMap;
   public final Map<LivingEntity, DamageTracker.Record> livingEntityMap = new ConcurrentHashMap<>();
   public final PhobosClient phobosClient;
   public final Normal normal;
   public final Map<LivingEntity, Long> livingEntityMap2 = new ConcurrentHashMap<>();
   private static Object[] phobos$g1806bc308;

   public float computePlayerEntity(PlayerEntity playerEntity, LivingEntity livingEntity, ItemStack itemStack) {
      float f = 1.0F;
      if (itemStack.getItem() instanceof SwordItem) {
         f = 7.0F;
      }

      if (itemStack.getItem() instanceof AxeItem) {
         f = 9.0F;
      }

      if (playerEntity.fallDistance > 0.0F) {
         f += f / 2.0F;
      }

      if (playerEntity.hasStatusEffect(StatusEffects.STRENGTH)) {
         int i = Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.STRENGTH)).getAmplifier() + 1;
         f += 3 * i;
      }

      return DamageUtil.getDamageLeft(livingEntity, f, playerEntity.getDamageSources().playerAttack(playerEntity), livingEntity.getArmor(), 32.0F);
   }

   public void handleText6(String text) {
      if (CalculationsModule.check3()) {
         NotificationsModule.getNotificationsModule()
            .handleTriConsumer(
               (color, color2, color3) -> NotificationsModule.getNotificationsModule()
                  .handleText5(Text.literal("[Engine] ").withColor(color2.hashCode()).append(Text.literal(text).withColor(color3.hashCode())))
            );
      }
   }

   @SneakyThrows public void handleLivingEntity(LivingEntity livingEntity, float f) {
      Map var3 = this.livingEntityMap;
      Lookup var4 = MethodHandles.lookup();
      Class<?> var5 = this.getClass();
      ClassLoader var6 = var5.getClassLoader();
      MethodType var7 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/combat/DamageTracker;F)Ljava/util/function/BiFunction;", var6);
      Class<?> var8 = this.getClass();
      ClassLoader var9 = var8.getClassLoader();
      MethodType var10 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", var9);
      Class<?> var11 = this.getClass();
      ClassLoader var12 = var11.getClassLoader();
      MethodType var13 = MethodType.fromMethodDescriptorString(
         "(FLnet/minecraft/class_1309;Lorg/phobos/core/combat/DamageTracker$Record;)Lorg/phobos/core/combat/DamageTracker$Record;", var12
      );
      MethodHandle var14 = var4.findVirtual(DamageTracker.class, "resolveRecordValue", var13);
      Class<?> var15 = this.getClass();
      ClassLoader var16 = var15.getClassLoader();
      MethodType var17 = MethodType.fromMethodDescriptorString(
         "(Lnet/minecraft/class_1309;Lorg/phobos/core/combat/DamageTracker$Record;)Lorg/phobos/core/combat/DamageTracker$Record;", var16
      );
      Object[] var10000 = new Object[0];
      CallSite var18 = LambdaMetafactory.metafactory(var4, "apply", var7, var10, var14, var17);
      MethodHandle var19 = var18.getTarget();
      if (this != null) {
         var10000 = phobos$g1806bc308;
         if (phobos$g1806bc308 == null) {
            var10000 = phobos$g1806bc308 = new Object[2];
         }

         var10000[0] = this;
      }

      Float var20 = (float)Float.intBitsToFloat(Float.floatToRawIntBits(f));
      var10000 = phobos$g1806bc308;
      if (phobos$g1806bc308 == null) {
         var10000 = phobos$g1806bc308 = new Object[2];
      }

      var10000[1] = var20;
      Object[] var10001 = phobos$g1806bc308;
      if (phobos$g1806bc308 == null) {
         var10001 = phobos$g1806bc308 = new Object[2];
      }

      Object var21 = var19.invokeWithArguments(var10001);
      var3.compute(livingEntity, (BiFunction)var21);
   }

   public DamageTracker.Record resolveRecordValue(float f, LivingEntity livingEntity, DamageTracker.Record recordValue) {
      if (recordValue != null) {
         float f2 = recordValue.getFloatValue();
         float f3 = recordValue.absorption.floatValue;
         float f4 = f3 - f;
         if (f4 <= 0.0F) {
            recordValue.getAbsorption().setFloatValue(0.0F);
            recordValue.setFloatValue(MathUtils.computeFloat3(recordValue.getFloatValue() + f4, 0.0F, Float.MAX_VALUE));
         } else {
            recordValue.getAbsorption().setFloatValue(f4);
         }

         if (f > 0.5F) {
            this.handleText6("(%s, %s) -> (%s, %s) : %s".formatted(f2, f3, recordValue.getFloatValue(), recordValue.getAbsorption().floatValue, f4));
         }
      }

      return recordValue;
   }

   public void handleExplosionS2CPacket(ExplosionS2CPacket explosionS2CPacket, ClientWorld clientWorld) {
      float f2 = explosionS2CPacket.getRadius();
      this.handleText6("Power %s".formatted(f2));
      if (f2 < 4.0F) {
         this.handleText6("Low power explosion");
      } else {
         double d = explosionS2CPacket.getX();
         double d2 = explosionS2CPacket.getY();
         double d3 = explosionS2CPacket.getZ();

         for (Entity entity : clientWorld.getEntities()) {
            if (entity instanceof LivingEntity livingEntity && !livingEntity.isDead() && livingEntity.hurtTime <= 0) {
               float f = this.phobosClient.getDamageCalculator().computeEntity4(livingEntity, clientWorld, f2, d, d2, d3);
               this.handleText6("Damaging %s for %s".formatted(livingEntity.getNameForScoreboard(), f));
               this.handleLivingEntity(livingEntity, f);
            }
         }
      }
   }

   public DamageTracker(PhobosClient phobosClient, FeatureRegistry featureRegistry) {
      this.incrementorMap = new ConcurrentHashMap<>();
      this.normal = new Normal();
      this.phobosClient = phobosClient;
      this.handleListener(
         new SafeListener<Receive<EntityDamageS2CPacket>>() {
            public void handleObject(
               Receive<EntityDamageS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (CalculationsModule.check()) {
                  EntityDamageS2CPacket entityDamageS2CPacket = receive.getPacket();
                  DamageSource damageSource = entityDamageS2CPacket.createDamageSource(clientWorld);
                  if (clientWorld.getEntityById(entityDamageS2CPacket.entityId()) instanceof LivingEntity livingEntity2) {
                     Entity entity = damageSource.getAttacker();
                     if (entity instanceof PlayerEntity playerEntity) {
                        float f2 = playerEntity.isUsingRiptide()
                           ? playerEntity.riptideAttackDamage
                           : Math.max((float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE), 1.0F);
                        ItemStack itemStack = playerEntity.getMainHandStack();
                        float f3 = EnchantmentUtils.computeItemStack4(itemStack, playerEntity, f2) - f2;
                        float f4 = ((ILivingEntity)(Object)playerEntity).getLastAttackCooldown();
                        f2 *= 0.2F + f4 * f4 * 0.8F;
                        f3 *= f4;
                        if (f2 > 0.0F || f3 > 0.0F) {
                           boolean flag = f4 > 0.9F;
                           f2 += itemStack.getItem().getBonusAttackDamage(livingEntity2, f2, damageSource);
                           boolean flag2 = flag
                              && playerEntity.fallDistance > 0.0F
                              && !playerEntity.isOnGround()
                              && !playerEntity.isClimbing()
                              && !playerEntity.isTouchingWater()
                              && !playerEntity.hasStatusEffect(StatusEffects.BLINDNESS)
                              && !playerEntity.hasVehicle()
                              && !playerEntity.isSprinting();
                           if (flag2) {
                              f2 *= 1.5F;
                           }

                           float f = f2 + f3;
                           DamageTracker.this.handleLivingEntity(livingEntity2, f);
                        }
                     }

                     if (damageSource.isOf(DamageTypes.MOB_ATTACK) && entity instanceof LivingEntity livingEntity) {
                        ItemStack itemStack2 = damageSource.getWeaponStack();
                        if (itemStack2 == null) {
                           itemStack2 = livingEntity.getMainHandStack();
                        }

                        float f6 = (float)livingEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                        if (itemStack2 == null) {
                           DamageTracker.this.handleLivingEntity(livingEntity2, f6);
                           return;
                        }

                        float f7 = EnchantmentUtils.computeItemStack4(itemStack2, livingEntity, f6) - f6;
                        if (f6 > 0.0F || f7 > 0.0F) {
                           f6 += itemStack2.getItem().getBonusAttackDamage(livingEntity2, f6, damageSource);
                           float f5 = f6 + f7;
                           DamageTracker.this.handleLivingEntity(livingEntity2, f5);
                        }
                     }

                     if (damageSource.isOf(DamageTypes.FALL)) {
                        float f8 = livingEntity2.fallDistance;
                        float f9 = (float)livingEntity2.getAttributeValue(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE);
                        float f10 = (float)livingEntity2.getAttributeValue(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER);
                        float f11 = (float)Math.max(0.0, Math.ceil((f8 - f9) * f10));
                        DamageTracker.this.handleLivingEntity(livingEntity2, f11);
                     }

                     if (damageSource.isOf(DamageTypes.FREEZE) || damageSource.isOf(DamageTypes.IN_WALL) || damageSource.isOf(DamageTypes.ON_FIRE)) {
                        DamageTracker.this.handleLivingEntity(livingEntity2, 1.0F);
                     }

                     if (damageSource.isOf(DamageTypes.DROWN)) {
                        DamageTracker.this.handleLivingEntity(livingEntity2, 2.0F);
                     }

                     if (damageSource.isOf(DamageTypes.SONIC_BOOM)) {
                        DamageTracker.this.handleLivingEntity(livingEntity2, DamageTracker.this.computeDifficulty(clientWorld.getDifficulty(), 10.0F));
                     }

                     if (damageSource.isOf(DamageTypes.CRAMMING)) {
                        DamageTracker.this.handleLivingEntity(livingEntity2, 6.0F);
                     }

                     if (damageSource.isOf(DamageTypes.LIGHTNING_BOLT)) {
                        DamageTracker.this.handleLivingEntity(livingEntity2, 5.0F);
                     }

                     if (damageSource.isOf(DamageTypes.OUT_OF_WORLD) || damageSource.isOf(DamageTypes.LAVA)) {
                        DamageTracker.this.handleLivingEntity(livingEntity2, 4.0F);
                     }

                     if (damageSource.isOf(DamageTypes.OUTSIDE_BORDER)) {
                        double d2 = clientWorld.getWorldBorder().getDistanceInsideBorder(livingEntity2) + clientWorld.getWorldBorder().getSafeZone();
                        if (d2 < 0.0) {
                           double d = clientWorld.getWorldBorder().getDamagePerBlock();
                           if (d > 0.0) {
                              DamageTracker.this.handleLivingEntity(livingEntity2, Math.max(1, MathHelper.floor(-d2 * d)));
                           }
                        }
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<ExplosionS2CPacket>>() {
            public void handleObject(
               Receive<ExplosionS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!receive.isCancelled() && CalculationsModule.check()) {
                  DamageTracker.this.handleExplosionS2CPacket(receive.getPacket(), clientWorld);
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<EntityStatusEvent>() {
            public void handleObject(
               EntityStatusEvent entityStatusEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (CalculationsModule.check()) {
                  if (entityStatusEvent.entity() instanceof LivingEntity livingEntity) {
                     if (DamageTracker.this.livingEntityMap.containsKey(livingEntity)) {
                        DamageTracker.this.incrementorMap.entrySet().removeIf(entry -> entry.getKey().getIntValue3() == livingEntity.getId());
                        DamageTracker.Record recordValue = DamageTracker.this.livingEntityMap.get(livingEntity);
                        float f = recordValue.getFloatValue();
                        float f2 = recordValue.getAbsorption().getFloatValue();
                        recordValue.setFloatValue(1.0F);
                        DamageTracker.this.handleLivingEntity2(livingEntity, recordValue, 900, 1);
                        DamageTracker.this.handleRecordValue(recordValue, 100, 1);
                        DamageTracker.this.handleText6(
                           livingEntity.getNameForScoreboard()
                              + " popped, reset from "
                              + f
                              + ":"
                              + f2
                              + " to "
                              + recordValue.getFloatValue()
                              + ":"
                              + recordValue.getAbsorption().getFloatValue()
                        );
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
               if (CalculationsModule.check()) {
                  for (LivingEntity livingEntity : DamageTracker.this.livingEntityMap2.keySet()) {
                     Long longValue = DamageTracker.this.livingEntityMap2.get(livingEntity);
                     if (longValue != null && longValue + CalculationsModule.value() < System.currentTimeMillis()) {
                        DamageTracker.this.livingEntityMap.remove(livingEntity);
                        DamageTracker.this.livingEntityMap2.remove(livingEntity);
                     }
                  }

                  DamageTracker.this.incrementorMap.entrySet().removeIf(entry -> !entry.getKey().check());

                  for (LivingEntity livingEntity3 : DamageTracker.this.livingEntityMap.keySet()) {
                     Entity entity;
                     if ((entity = clientWorld.getEntityById(livingEntity3.getId())) != null && entity.equals(livingEntity3)) {
                        if (!entity.isAlive()) {
                           DamageTracker.this.livingEntityMap.remove(livingEntity3);
                           continue;
                        }

                        if (entity instanceof LivingEntity livingEntity2) {
                           DamageTracker.this.livingEntityMap2.remove(livingEntity2);
                           if (livingEntity2.isDead()) {
                              DamageTracker.this.livingEntityMap.remove(livingEntity3);
                              continue;
                           }
                        }
                     }

                     DamageTracker.Record recordValue = DamageTracker.this.livingEntityMap.get(livingEntity3);
                     if (recordValue != null) {
                        recordValue.getAbsorption().run();
                        float f = recordValue.getFloatValue();
                        if (f <= 0.0F) {
                           DamageTracker.this.handleText6("Removing invalid prediction for " + livingEntity3.getNameForScoreboard());
                           DamageTracker.this.livingEntityMap.remove(livingEntity3);
                        }
                     }
                  }

                  for (Entity entity2 : clientWorld.getEntities()) {
                     if (entity2 instanceof LivingEntity livingEntity4 && !DamageTracker.this.livingEntityMap.containsKey(livingEntity4)) {
                        DamageTracker.this.livingEntityMap.put(livingEntity4, DamageTracker.Record.createRecordValue());
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RemoveEntityEvent>() {
            public void handleObject(
               RemoveEntityEvent removeEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (CalculationsModule.check()) {
                  if (removeEntityEvent.getEntity() instanceof LivingEntity livingEntity) {
                     if (removeEntityEvent.getRemovalReason().equals(RemovalReason.KILLED)) {
                        DamageTracker.this.livingEntityMap.remove(livingEntity);
                     } else {
                        DamageTracker.this.livingEntityMap2.put(livingEntity, System.currentTimeMillis());
                     }
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<DisconnectEvent2>() {
         public void handleObject2(DisconnectEvent2 disconnectEvent2) {
            if (CalculationsModule.check()) {
               DamageTracker.this.run2();
            }
         }
      });
      this.handleListener(
         new SafeListener<Receive<EntityEquipmentUpdateS2CPacket>>() {
            public void handleObject(
               Receive<EntityEquipmentUpdateS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientWorld.getEntityById(receive.getPacket().getEntityId()) instanceof LivingEntity livingEntity) {
                  if (DamageTracker.this.livingEntityMap.containsKey(livingEntity)) {
                     EntityUtils.handleEntityEquipmentUpdateS2CPacket(receive.getPacket(), clientWorld, (itemStack, itemStack2, itemStack3, itemStack4) -> {
                        ItemStack itemStack7 = livingEntity.getMainHandStack();
                        ItemStack itemStack8 = livingEntity.getOffHandStack();
                        ItemStack itemStack5;
                        Hand hand;
                        if (InventoryUtils.testItemStack5(itemStack7)) {
                           itemStack5 = itemStack7;
                           hand = Hand.MAIN_HAND;
                        } else {
                           if (!InventoryUtils.testItemStack5(itemStack8)) {
                              return;
                           }

                           itemStack5 = itemStack8;
                           hand = Hand.OFF_HAND;
                        }

                        ItemStack itemStack10 = hand.equals(Hand.MAIN_HAND) ? itemStack : itemStack3;
                        int i = itemStack10.getCount();
                        if (itemStack5.isOf(itemStack10.getItem()) && itemStack5.getCount() < i) {
                           DamageTracker.Record recordValue = DamageTracker.this.livingEntityMap.get(livingEntity);
                           DamageTracker.this.handleText6("%s has consumed a food item".formatted(livingEntity.getNameForScoreboard()));
                           if (itemStack5.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
                              DamageTracker.this.handleLivingEntity2(livingEntity, recordValue, 400, 1);
                              DamageTracker.this.handleRecordValue(recordValue, 2400, 3);
                           } else {
                              if (itemStack5.isOf(Items.GOLDEN_APPLE)) {
                                 DamageTracker.this.handleLivingEntity2(livingEntity, recordValue, 100, 1);
                                 DamageTracker.this.handleRecordValue(recordValue, 2400, 0);
                              }
                           }
                        }
                     });
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>(420) {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               for (Entity entity : clientWorld.getEntities()) {
                  if (entity instanceof LivingEntity livingEntity) {
                     ILivingEntity iLivingEntity = (ILivingEntity)(Object)livingEntity;
                     if (DamageTracker.this.livingEntityMap.containsKey(livingEntity)) {
                        label52: {
                           ItemStack itemStack2 = livingEntity.getMainHandStack();
                           ItemStack itemStack3 = livingEntity.getOffHandStack();
                           ItemStack itemStack;
                           Hand hand;
                           if (InventoryUtils.testItemStack5(itemStack2)) {
                              itemStack = itemStack2;
                              hand = Hand.MAIN_HAND;
                           } else {
                              if (!InventoryUtils.testItemStack5(itemStack3)) {
                                 break label52;
                              }

                              itemStack = itemStack3;
                              hand = Hand.OFF_HAND;
                           }

                           ItemStack itemStack5 = hand.equals(Hand.MAIN_HAND) ? iLivingEntity.getItemStack2() : iLivingEntity.getItemStack();
                           int i = hand.equals(Hand.MAIN_HAND) ? iLivingEntity.getLastMainhandCount() : iLivingEntity.getLastOffhandCount();
                           if (itemStack5 != null && itemStack.isOf(itemStack5.getItem()) && itemStack.getCount() < i) {
                              DamageTracker.Record recordValue = DamageTracker.this.livingEntityMap.get(livingEntity);
                              DamageTracker.this.handleText6("%s has consumed a food item".formatted(livingEntity.getNameForScoreboard()));
                              if (itemStack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
                                 DamageTracker.this.handleLivingEntity2(livingEntity, recordValue, 400, 1);
                                 DamageTracker.this.handleRecordValue(recordValue, 2400, 3);
                              } else if (itemStack.isOf(Items.GOLDEN_APPLE)) {
                                 DamageTracker.this.handleLivingEntity2(livingEntity, recordValue, 100, 1);
                                 DamageTracker.this.handleRecordValue(recordValue, 2400, 0);
                              }
                           }
                        }

                        ItemStack itemStack6 = livingEntity.getMainHandStack();
                        ItemStack itemStack7 = livingEntity.getOffHandStack();
                        iLivingEntity.setLastMainhandStack(itemStack6);
                        iLivingEntity.setLastOffhandStack(itemStack7);
                        iLivingEntity.setLastMainhandCount(itemStack6.getCount());
                        iLivingEntity.setLastOffhandCount(itemStack7.getCount());
                     }
                  }
               }
            }
         }
      );
   }

   public float computeLivingEntity(LivingEntity livingEntity, float f) {
      DamageTracker.Record recordValue = this.livingEntityMap.get(livingEntity);
      return CalculationsModule.check() && recordValue != null ? recordValue.getFloatValue() + recordValue.getAbsorption().getFloatValue() : f;
   }

   public void handleRecordValue(DamageTracker.Record recordValue, int i, int i2) {
      float f = Math.max(recordValue.getAbsorption().getFloatValue(), 4 * (1 + i2));
      recordValue.getAbsorption().setFloatValue(f);
      recordValue.getAbsorption().setIntValue(i);
   }

   public void handleLivingEntity2(LivingEntity livingEntity, DamageTracker.Record recordValue, int i, int i2) {
      DamageTracker.Incrementor incrementor = new DamageTracker.Incrementor(recordValue, livingEntity.getId(), livingEntity.getMaxHealth(), i2, i);
      this.incrementorMap.put(incrementor, System.currentTimeMillis());
   }

   public float computeDifficulty(Difficulty difficulty, float f) {
      return switch (difficulty) {
         case PEACEFUL -> 0.0F;
         case EASY -> Math.min(f / 2.0F + 1.0F, f);
         case NORMAL -> f;
         case HARD -> f * 3.0F / 2.0F;
      };
   }

   public void handleLivingEntity3(LivingEntity livingEntity, float f) {
      Map var3 = this.livingEntityMap;
      Object var4 = var3.get(livingEntity);
      boolean var5 = var4 instanceof DamageTracker.Record;
      if (var4 != null && !var5) {
         throw new ClassCastException("CHECKCAST failed");
      }

      if (var4 != null) {
         byte var6;
         label80: {
            if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(f)))
               && !Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(f)))
               && !Float.isNaN(20.0F)
               && !Float.isNaN(20.0F)) {
               if (Float.intBitsToFloat(Float.floatToRawIntBits(f)) > 20.0F) {
                  var6 = 1;
                  break label80;
               }

               if (!Float.isNaN(Float.intBitsToFloat(Float.floatToRawIntBits(f)))
                  && !Float.isNaN(20.0F)
                  && Float.intBitsToFloat(Float.floatToRawIntBits(f)) == 20.0F) {
                  var6 = 0;
                  break label80;
               }
            }

            var6 = -1;
         }

         int var7;
         if ((int)(var6 & 4294967295L) <= 0) {
            var7 = 0;
         } else {
            var7 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(f)) - 20.0F);
         }

         byte var32;
         label89: {
            long var8 = var7 & 4294967295L;
            long var10 = var7 & 4294967295L;
            long var12 = (int)(var7 & 4294967295L) & 4294967295L;
            long var14 = (int)(var7 & 4294967295L) & 4294967295L;
            long var16 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            long var18 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            long var20 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            long var22 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            if (!Float.isNaN(Float.intBitsToFloat((int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L)))
               && !Float.isNaN(Float.intBitsToFloat((int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L)))) {
               long var24 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
               if (!Float.isNaN(0.0F) && !Float.isNaN(0.0F)) {
                  long var28 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                  if (Float.intBitsToFloat((int)((int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L)) > 0.0F) {
                     long var105 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                     var32 = 1;
                     break label89;
                  }

                  long var43 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                  if (!Float.isNaN(Float.intBitsToFloat((int)((int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L)))
                     && !Float.isNaN(0.0F)) {
                     long var97 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                     if (0.0F == 0.0F) {
                        long var99 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                        long var104 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                        var32 = 0;
                        break label89;
                     }

                     long var103 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                  } else {
                     long var102 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
                  }
               } else {
                  long var101 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
               }
            } else {
               long var26 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            }

            long var30 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            var32 = -1;
         }

         long var33 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var35 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var41;
         if ((int)(var32 & 4294967295L) > 0) {
            long var37 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            long var45 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            long var47 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            long var10000 = (int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
            long var49 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            var10000 = (int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
            long var53 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            var10000 = Float.floatToRawIntBits(
                  Float.intBitsToFloat(Float.floatToRawIntBits(f))
                     - Float.intBitsToFloat((int)((int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L))
               )
               & 4294967295L;
            long var39 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            var41 = var10000;
         } else {
            long var109 = Float.floatToRawIntBits(f) & 4294967295L;
            long var106 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
            var41 = var109;
         }

         long var57 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var59 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var61 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var63 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var65 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         ((DamageTracker.Record)var4).setFloatValue((float)Float.intBitsToFloat((int)((int)var41 & 4294967295L)));
         long var67 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var69 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var71 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var73 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var75 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var77 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         DamageTracker.Absorption var79 = ((DamageTracker.Record)var4).getAbsorption();
         long var80 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var82 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var84 = (int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L;
         long var86 = (int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
         long var88 = (int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
         long var90 = (int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
         long var92 = (int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
         long var94 = (int)((int)((int)((int)(var7 & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L) & 4294967295L;
         float var96 = Math.max((float)Float.intBitsToFloat((int)var94), (float)0.0F);
         var79.setFloatValue((float)Float.intBitsToFloat(Float.floatToRawIntBits(var96)));
      }
   }

   public void run2() {
      this.livingEntityMap.clear();
      this.livingEntityMap2.clear();
      this.incrementorMap.clear();
   }

   private static final class Absorption {
      public int intValue;
      public float floatValue;

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public Absorption() {
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public void run() {
         if (this.intValue <= 0) {
            this.setFloatValue(0.0F);
         } else {
            this.intValue--;
         }
      }
   }

   private static final class Incrementor {
      public int intValue;
      public final int intValue2;
      public final int intValue3;
      public final float floatValue;
      public final DamageTracker.Record recordValue;

      public boolean check() {
         if (this.intValue <= 0) {
            return false;
         }

         if (this.check2() && this.recordValue.floatValue < this.floatValue) {
            this.recordValue.setFloatValue(this.recordValue.getFloatValue() + 1.0F);
         }

         this.intValue--;
         return true;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public Incrementor(DamageTracker.Record recordValue, int i, float f, int i2, int i3) {
         this.recordValue = recordValue;
         this.intValue3 = i;
         this.floatValue = f;
         this.intValue2 = i2;
         this.intValue = i3;
      }

      public DamageTracker.Record getRecordValue() {
         return this.recordValue;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public boolean check2() {
         int i = 50 >> this.intValue2;
         return i > 0 ? this.intValue % i == 0 : true;
      }

      public int getIntValue2() {
         return this.intValue2;
      }

      public int getIntValue3() {
         return this.intValue3;
      }
   }

   private static final class Record {
      public final DamageTracker.Absorption absorption;
      public float floatValue;
      public long longValue;
      public float floatValue2 = 20.0F;

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public Record(float f) {
         this.absorption = new DamageTracker.Absorption();
         this.floatValue = f;
         this.longValue = System.currentTimeMillis();
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public DamageTracker.Absorption getAbsorption() {
         return this.absorption;
      }

      public static DamageTracker.Record createRecordValue() {
         return new DamageTracker.Record(20.0F);
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public void setLongValue(long l) {
         this.longValue = l;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }
   }
}
