package org.phobos.core.movement;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.event.events.PacketEvent.PostSend;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.PlaySoundEvent;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.combat.AutoArmorModule;
import org.phobos.core.Feature;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.inventory.SwitchOverride;
import org.phobos.core.task.TaskScheduler;
import org.phobos.core.tick.TickUtils;

public class ElytraManager extends Feature {
   public boolean flag;
   public boolean flag2;
   public int intValue;
   public boolean flag3;
   public ElytraManager.FlyConfiguration flyConfiguration;
   public boolean flag4;
   public static final int intValue2 = 6;
   private static Object[] phobos$g1806d3b40;

   public boolean testRegistryEntry(RegistryEntry<SoundEvent> registryEntry) {
      return registryEntry.equals(SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA)
         || registryEntry.equals(SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE)
         || registryEntry.equals(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
   }

   public void handlePhobosClient(
      PhobosClient phobosClient, ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      boolean var4 = this.flag2;
      if (!var4) {
         boolean var5 = this.flag;
         if (var5) {
            PlayerInventory var6 = clientPlayerEntity.getInventory();
            ItemStack var7 = var6.getArmorStack(2);
            Item var8 = var7.getItem();
            boolean var9 = var8 instanceof ElytraItem;
            if ((int)((!var9 ? 0 : 1) & 0xFF & 0xFF) == 0) {
               ElytraManager.FlyConfiguration var10 = this.flyConfiguration;
               if (var10 != null) {
                  boolean var11 = clientPlayerEntity.isFallFlying();
                  if (!var11) {
                     ElytraManager.FlyConfiguration var12 = this.flyConfiguration;
                     boolean var13 = var12.flag4;
                     if (!var13) {
                        boolean var14 = clientPlayerEntity.isOnGround();
                        if (var14) {
                           return;
                        }
                     }

                     ElytraManager.FlyConfiguration var15 = this.flyConfiguration;
                     boolean var16 = var15.flag2;
                     if (!var16) {
                        boolean var17 = clientPlayerEntity.isTouchingWater();
                        if (var17) {
                           return;
                        }
                     }

                     InventoryManager var18 = phobosClient.getInventoryManager();
                     ElytraManager var10000 = this;
                     ClientPlayerInteractionManager var10001 = clientPlayerInteractionManager;
                     ClientPlayerEntity var10002 = clientPlayerEntity;
                     PhobosClient cap3 = phobosClient;
                     ClientPlayerEntity cap2 = var10002;
                     ClientPlayerInteractionManager cap1 = var10001;
                     ElytraManager cap0 = var10000;
                     Consumer<ItemSwitcher> var35 = var4x -> cap0.handleClientPlayerInteractionManager(cap1, cap2, cap3, var4x);
                     var18.handleConsumer2(var35);
                  }
               }
            }
         }
      }
   }

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public boolean check() {
      return this.flag && this.flyConfiguration != null && this.flyConfiguration.isFlag5();
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public ElytraManager.FlyConfiguration getFlyConfiguration() {
      return this.flyConfiguration;
   }

   public boolean check2() {
      return this.isFlag() && this.getFlyConfiguration() != null && this.getFlyConfiguration().flag3;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public void handleInt(int i) {
      this.setIntValue(i);
      this.setFlag3(true);
   }

   public boolean testFlyConfiguration(@NotNull ElytraManager.FlyConfiguration flyConfiguration) {
      return this.flyConfiguration == null ? false : this.flyConfiguration.getPriority() < flyConfiguration.getPriority();
   }

   public ElytraManager(PhobosClient phobosClient) {
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      this.flag4 = true & true;
      var var2 = new SafeListener<PlayerTickEvent>(2147483646) {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            ElytraManager.this.setFlag(false);
         }
      };
      this.handleListener(var2);
      var var3 = new SafeListener<PlayerTickEvent>(-2147483647) {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            ElytraManager.this.handlePhobosClient(var1, clientPlayerEntity, clientPlayerInteractionManager);
         }
      };
      this.handleListener(var3);
      var var4 = new SafeListener<PlaySoundEvent>() {
         public void handleObject(
            PlaySoundEvent playSoundEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (ElytraManager.this.isFlag()) {
               if (ElytraManager.this.testSoundEvent(playSoundEvent.getSoundEvent())) {
                  playSoundEvent.setCancelled(true);
               }
            }
         }
      };
      this.handleListener(var4);
      var var5 = new SafeListener<Receive<PlaySoundS2CPacket>>() {
         public void handleObject(
            Receive<PlaySoundS2CPacket> receive,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (ElytraManager.this.isFlag()) {
               if (ElytraManager.this.testRegistryEntry(receive.getPacket().getSound())) {
                  receive.setCancelled(true);
               }
            }
         }
      };
      this.handleListener(var5);
      var var6 = new SafeListener<Receive<PlaySoundFromEntityS2CPacket>>() {
         public void handleObject(
            Receive<PlaySoundFromEntityS2CPacket> receive,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (ElytraManager.this.isFlag() && receive.getPacket().getEntityId() == clientPlayerEntity.getId()) {
               if (ElytraManager.this.testRegistryEntry(receive.getPacket().getSound())) {
                  receive.setCancelled(true);
               }
            }
         }
      };
      this.handleListener(var6);
      var var7 = new SafeListener<PostSend<ClientCommandC2SPacket>>() {
         public void handleObject(
            PostSend<ClientCommandC2SPacket> postSend,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (postSend.getPacket().getMode().equals(Mode.START_FALL_FLYING)) {
               if (ElytraManager.this.isFlag() && ElytraManager.this.flag3 && ElytraManager.this.flyConfiguration != null) {
                  if (!ElytraManager.this.getFlyConfiguration().flag3) {
                     ElytraManager.this.flag3 = false;
                     var1.getInventoryManager()
                        .handleConsumer2(
                           itemSwitcher -> {
                              Item item = itemSwitcher.resolveObject(
                                 slot -> slot.getStack().getItem() instanceof FireworkRocketItem ? slot.getStack().getItem() : null
                              );
                              if (item != null) {
                                 itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, ElytraManager.this.intValue | 1);
                                 TickUtils.handleClientWorld(
                                    clientWorld,
                                    integer -> clientPlayerEntity.networkHandler
                                       .sendPacket(
                                          new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch())
                                       )
                                 );
                              }
                           }
                        );
                  }
               }
            }
         }
      };
      this.handleListener(var7);
   }

   public boolean isFlag() {
      return this.flag;
   }

   public boolean testFlyConfiguration2(@NotNull ElytraManager.FlyConfiguration flyConfiguration) {
      if (!this.flag2 && (!this.isFlag() || !this.testFlyConfiguration(flyConfiguration))) {
         this.setFlag(true);
         this.setFlyConfiguration(flyConfiguration);
         return true;
      } else {
         return false;
      }
   }

   public boolean testSoundEvent(SoundEvent soundEvent) {
      return soundEvent.equals(SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value())
         || soundEvent.equals(SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE.value())
         || soundEvent.equals(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND.value());
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void handleClientPlayerInteractionManager(
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      ClientPlayerEntity clientPlayerEntity,
      PhobosClient phobosClient,
      ItemSwitcher itemSwitcher
   ) {
      Slot slot = itemSwitcher.resolveObject(slotx -> {
         if (!this.flyConfiguration.flag && !itemSwitcher.testSlot4(slotx)) {
            return null;
         }

         ItemStack itemStack = slotx.getStack();
         return itemStack.getItem() instanceof ElytraItem && ElytraItem.isUsable(itemStack) ? slotx : null;
      });
      if (slot != null) {
         AutoArmorModule.flag = true;
         boolean flag = !itemSwitcher.testSlot4(slot);
         int i = PlayerActionUtils.value() % 8 + 1;
         int i2 = flag ? i : slot.getIndex();
         switch (this.flyConfiguration.equipType) {
            case Default:
               itemSwitcher.handleRunnable2(() -> {
                  if (flag) {
                     itemSwitcher.handleInt5(slot.id, i, SlotActionType.SWAP);
                  }

                  itemSwitcher.handleInt5(6, i2, SlotActionType.SWAP);
               });
               break;
            case Click:
               itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 65);
               clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND);
               break;
            case _2b2t:
               itemSwitcher.resolveSwitchResult(Hand.OFF_HAND, slot, 97);
               clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.OFF_HAND);
               break;
            case _2b2tAlt:
               itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 3);
               clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
               clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.OFF_HAND);
         }

         clientPlayerEntity.startFallFlying();
         clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.START_FALL_FLYING));
         switch (this.flyConfiguration.equipType) {
            case Default:
               if (this.flyConfiguration.longValue == 0L) {
                  itemSwitcher.handleRunnable2(() -> {
                     itemSwitcher.handleInt5(6, i2, SlotActionType.SWAP);
                     if (flag) {
                        itemSwitcher.handleInt5(slot.id, i, SlotActionType.SWAP);
                     }
                  });
                  AutoArmorModule.flag = false;
                  return;
               }

               TaskScheduler taskScheduler = new TaskScheduler();
               this.flag2 = true;
               taskScheduler.handleLong(this.flyConfiguration.longValue, () -> {
                  phobosClient.getInventoryManager().handleConsumer2(itemSwitcherx -> itemSwitcherx.handleRunnable2(() -> {
                     itemSwitcherx.handleInt5(6, i2, SlotActionType.SWAP);
                     if (flag) {
                        itemSwitcherx.handleInt5(slot.id, i, SlotActionType.SWAP);
                     }
                  }));
                  AutoArmorModule.flag = false;
                  this.flag2 = false;
               });
               break;
            case Click:
               if (this.flyConfiguration.longValue == 0L) {
                  SwitchOverride.handleRunnable(() -> clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND));
                  AutoArmorModule.flag = false;
                  return;
               }

               TaskScheduler taskScheduler2 = new TaskScheduler();
               this.flag2 = true;
               taskScheduler2.handleLong(this.flyConfiguration.longValue, () -> {
                  SwitchOverride.handleRunnable(() -> clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.MAIN_HAND));
                  AutoArmorModule.flag = false;
                  this.flag2 = false;
               });
               break;
            case _2b2t:
               if (this.flyConfiguration.longValue == 0L) {
                  SwitchOverride.handleRunnable(() -> clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.OFF_HAND));
                  AutoArmorModule.flag = false;
                  return;
               }

               TaskScheduler taskScheduler3 = new TaskScheduler();
               this.flag2 = true;
               taskScheduler3.handleLong(this.flyConfiguration.longValue, () -> {
                  SwitchOverride.handleRunnable(() -> clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.OFF_HAND));
                  AutoArmorModule.flag = false;
                  this.flag2 = false;
               });
               break;
            case _2b2tAlt:
               if (this.flyConfiguration.longValue == 0L) {
                  clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.OFF_HAND);
                  clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                  AutoArmorModule.flag = false;
                  return;
               }

               TaskScheduler taskScheduler4 = new TaskScheduler();
               this.flag2 = true;
               taskScheduler4.handleLong(this.flyConfiguration.longValue, () -> {
                  clientPlayerInteractionManager.interactItem(clientPlayerEntity, Hand.OFF_HAND);
                  clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                  AutoArmorModule.flag = false;
                  this.flag2 = false;
               });
         }
      }
   }

   public void setFlyConfiguration(ElytraManager.FlyConfiguration flyConfiguration) {
      this.flyConfiguration = flyConfiguration;
   }

   public enum EquipType {
      Default,
      Click,
      _2b2t,
      _2b2tAlt;
   }

   public static class FlyConfiguration {
      public int priority;
      public ElytraManager.EquipType equipType;
      public boolean flag;
      public boolean flag2;
      public boolean flag3;
      public boolean flag4 = false;
      public boolean flag5;
      public boolean flag6;
      public long longValue;

      public int getPriority() {
         return this.priority;
      }

      public boolean isFlag6() {
         return this.flag6;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setFlag6(boolean flag) {
         this.flag6 = flag;
      }

      public boolean isFlag4() {
         return this.flag4;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public FlyConfiguration() {
         this.flag = false;
         this.flag2 = false;
         this.flag3 = false;
         this.flag5 = false;
         this.flag6 = true;
         this.priority = 0;
         this.longValue = 0L;
         this.equipType = ElytraManager.EquipType.Default;
      }

      public boolean isFlag5() {
         return this.flag5;
      }

      public ElytraManager.EquipType getEquipType() {
         return this.equipType;
      }

      public void setLongValue(long l) {
         this.longValue = l;
      }

      public void setFlag3(boolean flag) {
         this.flag3 = flag;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public void setEquipType(ElytraManager.EquipType equipType) {
         this.equipType = equipType;
      }

      public void setFlag5(boolean flag) {
         this.flag5 = flag;
      }

      public void setPriority(int i) {
         this.priority = i;
      }

      public void setFlag4(boolean flag) {
         this.flag4 = flag;
      }

      public static ElytraManager.FlyConfiguration.Factory createFactory() {
         return new ElytraManager.FlyConfiguration.Factory();
      }

      public boolean isFlag() {
         return this.flag;
      }

      public boolean isFlag3() {
         return this.flag3;
      }

      public static class Factory {
         public Supplier<ElytraManager.EquipType> equipTypeSupplier;
         public final ElytraManager.FlyConfiguration flyConfiguration = new ElytraManager.FlyConfiguration();
         public Supplier<Long> longValueSupplier;
         public Supplier<Boolean> booleanValueSupplier;
         public Supplier<Boolean> booleanValueSupplier2;
         public Supplier<Boolean> booleanValueSupplier3;
         public Supplier<Boolean> booleanValueSupplier4;
         public Supplier<Boolean> booleanValueSupplier5;

         public ElytraManager.FlyConfiguration getFlyConfiguration() {
            if (this.booleanValueSupplier2 != null) {
               this.flyConfiguration.setFlag4(this.booleanValueSupplier2.get());
            }

            if (this.booleanValueSupplier != null) {
               this.flyConfiguration.setFlag(this.booleanValueSupplier.get());
            }

            if (this.booleanValueSupplier4 != null) {
               this.flyConfiguration.setFlag2(this.booleanValueSupplier4.get());
            }

            if (this.booleanValueSupplier5 != null) {
               this.flyConfiguration.setFlag3(this.booleanValueSupplier5.get());
            }

            if (this.booleanValueSupplier3 != null) {
               this.flyConfiguration.setFlag5(this.booleanValueSupplier3.get());
            }

            if (this.longValueSupplier != null) {
               this.flyConfiguration.setLongValue(this.longValueSupplier.get());
            }

            if (this.equipTypeSupplier != null) {
               this.flyConfiguration.setEquipType(this.equipTypeSupplier.get());
            }

            return this.flyConfiguration;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory(ElytraManager.EquipType equipType) {
            this.flyConfiguration.setEquipType(equipType);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory2(Supplier<Boolean> supplier) {
            this.setBooleanValueSupplier(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory getFactory() {
            this.flyConfiguration.setFlag2(true);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory getFactory2() {
            this.flyConfiguration.setFlag5(true);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory getFactory3() {
            this.flyConfiguration.setFlag4(true);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory3(Supplier<Long> supplier) {
            this.setLongValueSupplier(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory4(Supplier<Boolean> supplier) {
            this.setBooleanValueSupplier4(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory5(Supplier<Boolean> supplier) {
            this.setBooleanValueSupplier2(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory getFactory4() {
            this.flyConfiguration.setFlag(true);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory6(long l) {
            this.flyConfiguration.setLongValue(l);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory7(Supplier<ElytraManager.EquipType> supplier) {
            this.setEquipTypeSupplier(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory8(int i) {
            this.flyConfiguration.setPriority(i);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory9(Supplier<Boolean> supplier) {
            this.setBooleanValueSupplier3(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory resolveFactory10(Supplier<Boolean> supplier) {
            this.setBooleanValueSupplier5(supplier);
            return this;
         }

         public ElytraManager.FlyConfiguration.Factory getFactory5() {
            this.flyConfiguration.setFlag3(true);
            return this;
         }

         public Supplier<Boolean> getBooleanValueSupplier() {
            return this.booleanValueSupplier;
         }

         public void setBooleanValueSupplier(Supplier<Boolean> supplier) {
            this.booleanValueSupplier = supplier;
         }

         public void setLongValueSupplier(Supplier<Long> supplier) {
            this.longValueSupplier = supplier;
         }

         public Supplier<ElytraManager.EquipType> getEquipTypeSupplier() {
            return this.equipTypeSupplier;
         }

         public Supplier<Boolean> getBooleanValueSupplier4() {
            return this.booleanValueSupplier4;
         }

         public void setBooleanValueSupplier3(Supplier<Boolean> supplier) {
            this.booleanValueSupplier3 = supplier;
         }

         public Supplier<Boolean> getBooleanValueSupplier2() {
            return this.booleanValueSupplier2;
         }

         public void setBooleanValueSupplier2(Supplier<Boolean> supplier) {
            this.booleanValueSupplier2 = supplier;
         }

         public void setBooleanValueSupplier5(Supplier<Boolean> supplier) {
            this.booleanValueSupplier5 = supplier;
         }

         public Supplier<Long> getLongValueSupplier() {
            return this.longValueSupplier;
         }

         public ElytraManager.FlyConfiguration getFlyConfiguration2() {
            return this.flyConfiguration;
         }

         public void setEquipTypeSupplier(Supplier<ElytraManager.EquipType> supplier) {
            this.equipTypeSupplier = supplier;
         }

         public ElytraManager.FlyConfiguration getFlyConfiguration3() {
            return this.flyConfiguration;
         }

         public void setBooleanValueSupplier4(Supplier<Boolean> supplier) {
            this.booleanValueSupplier4 = supplier;
         }

         public Supplier<Boolean> getBooleanValueSupplier3() {
            return this.booleanValueSupplier3;
         }

         public Supplier<Boolean> getBooleanValueSupplier5() {
            return this.booleanValueSupplier5;
         }
      }
   }
}
