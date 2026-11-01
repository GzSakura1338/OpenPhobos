package org.phobos.module.combat;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.util.NullSafe;
import org.phobos.core.math.MathUtils;
import org.phobos.core.bind.BindTriggerUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.bind.BindTriggerUtils.Type;
import org.phobos.event.AutoPotPlayerTickListener;
import org.phobos.module.Module;

public class AutoPotModule extends Module {
   public Setting<AutoPotModule.Rotate> rotateSetting;
   public Setting<KeyCombination> keyCombinationSetting;
   public Setting<Float> floatValueSetting;
   public Setting<KeyCombination> keyCombinationSetting2;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Integer> integerSetting;
   public Setting<KeyCombination> keyCombinationSetting3;
   public Setting<AutoPotModule.Mode> modeSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Long> longValueSetting;
   public Setting<Silent> silentSetting;
   public Setting<Long> longValueSetting2;
   public Setting<KeyCombination> keyCombinationSetting4;
   public Setting<Double> doubleValueSetting;
   public static final Set<RegistryEntry<StatusEffect>> registryEntrySet = Set.of(
      StatusEffects.RESISTANCE, StatusEffects.STRENGTH, StatusEffects.SPEED, StatusEffects.INSTANT_HEALTH
   );
   public Normal normal;
   public Set<RegistryEntry<StatusEffect>> registryEntrySet2;
   private static Object[] phobos$g1806e5d18;
   private static Object[] phobos$g1806e5b88;

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, StatusEffect statusEffect) {
      for (Entry<RegistryEntry<StatusEffect>, StatusEffectInstance> entry : clientPlayerEntity.getActiveStatusEffects().entrySet()) {
         if (entry.getKey().value().equals(statusEffect)) {
            return true;
         }
      }

      return false;
   }

   public void handleSetting(Setting<KeyCombination> setting, RegistryEntry<StatusEffect> registryEntry) {
      AutoPotModule var10000 = this;
      RegistryEntry cap69 = registryEntry;
      AutoPotModule cap68 = var10000;
      Runnable var19 = () -> cap68.handleRegistryEntry(cap69);
      Type var20 = Type.Press;
      BindTriggerUtils.handleSupplier(setting, var19, this, var20);
   }

   public void handleRegistryEntry(RegistryEntry registryEntry) {
      NullSafe.handleMinecraftClient(
         mc, (clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> this.testClientPlayerEntity4(clientPlayerEntity, clientWorld, registryEntry)
      );
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, StatusEffect statusEffect) {
      MutableBoolean var4 = new MutableBoolean();
      PhobosClient var5 = this.phobosClient;
      InventoryManager var6 = var5.getInventoryManager();
      AutoPotModule var10000 = this;
      StatusEffect var10001 = statusEffect;
      ClientPlayerEntity var10002 = clientPlayerEntity;
      ClientWorld var10003 = clientWorld;
      MutableBoolean cap74 = var4;
      ClientWorld cap73 = var10003;
      ClientPlayerEntity cap72 = var10002;
      StatusEffect cap71 = var10001;
      AutoPotModule cap70 = var10000;
      Consumer<ItemSwitcher> var23 = var5x -> cap70.handleStatusEffect(cap71, cap72, cap73, cap74, var5x);
      var6.handleConsumer2(var23);
      Boolean var24 = var4.getValue();
      return var24;
   }

   public AutoPotModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoPot", Categories.combat, "Throws instant health potions");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      AutoPotModule.Mode var3 = AutoPotModule.Mode.Modern;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      Integer var6 = 1;
      Integer var7 = 0;
      Integer var8 = 5;
      NumberSetting var5 = new NumberSetting<>("Tick", var6, var7, var8);
      Setting var9 = this.modeSetting;
      AutoPotModule.Mode var10 = AutoPotModule.Mode.Legacy;
      Setting var11 = var5.resolveSetting4(var9, var10);
      Setting var12 = this.resolveSetting2(var11);
      this.integerSetting = var12;
      Float var14 = (float)6.0F;
      Float var15 = (float)0.1F;
      Float var16 = (float)20.0F;
      NumberSetting var13 = new NumberSetting<>("Health", var14, var15, var16);
      Setting var17 = this.modeSetting;
      AutoPotModule.Mode var18 = AutoPotModule.Mode.Legacy;
      Setting var19 = var13.resolveSetting4(var17, var18);
      Setting var20 = this.resolveSetting2(var19);
      this.floatValueSetting = var20;
      LongNumberSetting var21 = new LongNumberSetting("Delay", 0L, 0L, 1000L, true & true);
      Setting var22 = this.modeSetting;
      AutoPotModule.Mode var23 = AutoPotModule.Mode.Legacy;
      Setting var24 = var21.resolveSetting4(var22, var23);
      Setting var25 = this.resolveSetting2(var24);
      this.longValueSetting = var25;
      BindSetting var26 = new BindSetting("Turtle");
      Setting var27 = this.modeSetting;
      AutoPotModule.Mode var28 = AutoPotModule.Mode.Modern;
      Setting var29 = var26.resolveSetting4(var27, var28);
      Setting var30 = this.resolveSetting2(var29);
      this.keyCombinationSetting4 = var30;
      BindSetting var31 = new BindSetting("Strength");
      Setting var32 = this.modeSetting;
      AutoPotModule.Mode var33 = AutoPotModule.Mode.Modern;
      Setting var34 = var31.resolveSetting4(var32, var33);
      Setting var35 = this.resolveSetting2(var34);
      this.keyCombinationSetting3 = var35;
      BindSetting var36 = new BindSetting("Speed");
      Setting var37 = this.modeSetting;
      AutoPotModule.Mode var38 = AutoPotModule.Mode.Modern;
      Setting var39 = var36.resolveSetting4(var37, var38);
      Setting var40 = this.resolveSetting2(var39);
      this.keyCombinationSetting2 = var40;
      BindSetting var41 = new BindSetting("Heal");
      Setting var42 = this.modeSetting;
      AutoPotModule.Mode var43 = AutoPotModule.Mode.Modern;
      Setting var44 = var41.resolveSetting4(var42, var43);
      Setting var45 = this.resolveSetting2(var44);
      this.keyCombinationSetting = var45;
      BooleanSetting var46 = new BooleanSetting("Auto");
      Setting var47 = this.resolveSetting2(var46);
      this.booleanValueSetting2 = var47;
      LongNumberSetting var48 = new LongNumberSetting("AutoDelay", 500L, 0L, 30000L);
      Setting var49 = this.resolveSetting2(var48);
      this.longValueSetting2 = var49;
      Double var51 = 10.0;
      Double var52 = 6.0;
      Double var53 = 100.0;
      NumberSetting var50 = new NumberSetting<>("AutoRange", var51, var52, var53);
      Setting var54 = var50.resolveSetting7("Ensures distance between you and your enemies");
      Setting var55 = this.resolveSetting2(var54);
      this.doubleValueSetting = var55;
      AutoPotModule.Rotate var57 = AutoPotModule.Rotate.Normal;
      EnumSetting var56 = new EnumSetting<>("Rotate", var57);
      Setting var58 = this.modeSetting;
      AutoPotModule.Mode var59 = AutoPotModule.Mode.Modern;
      Setting var60 = var56.resolveSetting4(var58, var59);
      Setting var61 = this.resolveSetting2(var60);
      this.rotateSetting = var61;
      Silent var63 = Silent.Normal;
      EnumSetting var62 = new EnumSetting<>("Silent", var63);
      Setting var64 = this.modeSetting;
      AutoPotModule.Mode var65 = AutoPotModule.Mode.Modern;
      Setting var66 = var62.resolveSetting4(var64, var65);
      Setting var67 = this.resolveSetting2(var66);
      this.silentSetting = var67;
      BooleanSetting var68 = new BooleanSetting("Swing");
      Setting var69 = this.modeSetting;
      AutoPotModule.Mode var70 = AutoPotModule.Mode.Modern;
      Setting var71 = var68.resolveSetting4(var69, var70);
      Setting var72 = this.resolveSetting2(var71);
      this.booleanValueSetting = var72;
      Normal var73 = new Normal();
      this.normal = var73;
      HashSet var74 = new HashSet();
      this.registryEntrySet2 = var74;
      Setting var75 = this.booleanValueSetting2;
      Setting[] var76 = new Setting[2];
      Setting var77 = this.longValueSetting2;
      var76[0] = var77;
      Setting var78 = this.doubleValueSetting;
      var76[1] = var78;
      SettingGroup var79 = this.resolveSettingGroup(var75, var76);
      Setting var80 = this.modeSetting;
      AutoPotModule.Mode var81 = AutoPotModule.Mode.Modern;
      var79.resolveSettingGroup4(var80, var81);
      AutoPotPlayerTickListener var82 = new AutoPotPlayerTickListener(this);
      this.handleListener(var82);
      Setting var83 = this.keyCombinationSetting4;
      RegistryEntry var84 = StatusEffects.RESISTANCE;
      this.handleSetting(var83, var84);
      Setting var85 = this.keyCombinationSetting3;
      RegistryEntry var86 = StatusEffects.STRENGTH;
      this.handleSetting(var85, var86);
      Setting var87 = this.keyCombinationSetting2;
      RegistryEntry var88 = StatusEffects.SPEED;
      this.handleSetting(var87, var88);
      Setting var89 = this.keyCombinationSetting;
      RegistryEntry var90 = StatusEffects.INSTANT_HEALTH;
      this.handleSetting(var89, var90);
   }

   public boolean testClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, PlayerEntity playerEntity) {
      return clientPlayerEntity.distanceTo(playerEntity) <= this.doubleValueSetting.getValue();
   }

   public void handleStatusEffect(
      StatusEffect statusEffect, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, MutableBoolean mutableBoolean, ItemSwitcher itemSwitcher
   ) {
      Slot slot = itemSwitcher.resolveObject(slotx -> {
         ItemStack itemStack = slotx.getStack();
         if (itemStack.isEmpty()) {
            return null;
         }

         if (itemStack.getItem() instanceof SplashPotionItem splashPotionItem) {
            PotionContentsComponent potionContentsComponent = itemStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);

            for (StatusEffectInstance statusEffectInstance : potionContentsComponent.getEffects()) {
               if (statusEffectInstance.getEffectType().value().equals(statusEffect)) {
                  return slotx;
               }
            }

            return null;
         } else {
            return null;
         }
      });
      if (slot != null) {
         itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, this.silentSetting.getValue().getFlag() | 1);
         float f = this.phobosClient.getRotationManager().computeFloat5(clientPlayerEntity.getYaw());
         float f2 = 90.0F;
         switch ((AutoPotModule.Rotate)this.rotateSetting.getValue()) {
            case Packet:
               this.phobosClient.getRotationManager().testFloat(f, f2);
               break;
            case Normal:
            case Silent:
               this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, f, f2);
         }

         TickUtils.handleClientWorld(
            clientWorld, integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, f, f2))
         );
         if (this.booleanValueSetting.getValue()) {
            clientPlayerEntity.swingHand(Hand.MAIN_HAND);
         }

         if (this.rotateSetting.testObject2(AutoPotModule.Rotate.Silent)) {
            this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
         }

         mutableBoolean.setTrue();
      }
   }

   public boolean testClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, RegistryEntry<StatusEffect> registryEntry) {
      Object var4 = registryEntry.value();
      boolean var5 = var4 instanceof StatusEffect;
      if (var4 != null && !var5) {
         throw new ClassCastException("CHECKCAST failed");
      } else {
         return this.testClientPlayerEntity2(clientPlayerEntity, clientWorld, (StatusEffect)var4);
      }
   }

   public boolean testClientPlayerEntity5(ClientPlayerEntity clientPlayerEntity, PlayerEntity playerEntity) {
      return clientPlayerEntity.distanceTo(playerEntity) <= 5.0;
   }

   @Override
   public String nothing() {
      return switch ((AutoPotModule.Mode)this.modeSetting.getValue()) {
         case Legacy -> String.valueOf(MathUtils.computeFloat5(this.floatValueSetting.getValue(), 1));
         case Modern -> {
            if (this.booleanValueSetting2.getValue()) {
               if (this.registryEntrySet2.isEmpty()) {
                  yield "Auto";
               } else {
                  long l = this.longValueSetting2.getValue() - this.normal.value();
                  yield l <= 0L ? "Auto" : "%sms".formatted(l);
               }
            } else {
               yield "Modern";
            }
         }
      };
   }

   public enum Mode {
      Legacy,
      Modern;
   }

   public record PotionTask(StatusEffect effect) {
   }

   public enum Rotate {
      Normal,
      Packet,
      Silent;
   }
}
