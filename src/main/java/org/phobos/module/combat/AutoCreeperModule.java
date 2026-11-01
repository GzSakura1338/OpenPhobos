package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.bind.BindKey;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.fakeplayer.MovingFakePlayer;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.player.SilentHotbarModule;

public class AutoCreeperModule extends PlacementModule {
   public boolean flag;
   public boolean flag2;
   public final Setting<AutoCreeperModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoCreeperModule.Mode.Rage));
   public final TimeGate.Normal normal3;
   public final Setting<Boolean> auto = this.resolveSetting2(
      new BooleanSetting("Auto").resolveSetting7("Places creepers automatically").resolveSetting4(this.mode, AutoCreeperModule.Mode.Legit)
   );
   public final Setting<Float> damage = this.resolveSetting2(new NumberSetting<>("Damage", 6.0F, 1.0F, 36.0F));
   public final Setting<Long> cooldown;
   public final Setting<Boolean> prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
   public final Setting<KeyCombination> macro;
   public final Setting<Integer> predictionTicks = this.resolveSetting2(new NumberSetting<>("PredictionTicks", 1, 1, 6));

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, MovingFakePlayer movingFakePlayer) {
      float f2 = 3.0F;
      float f3 = this.damage.getValue();

      for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
         if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 32.0)) {
            float f = this.phobosClient
               .getDamageCalculator()
               .computeEntity6(this.resolvePlayerEntity(playerEntity), clientWorld, 3.0F, movingFakePlayer.getPos());
            if (f >= f3) {
               return true;
            }
         }
      }

      return false;
   }

   public float computeCreeperEntity(CreeperEntity creeperEntity) {
      return creeperEntity.explosionRadius * (creeperEntity.shouldRenderOverlay() ? 2.0F : 1.0F);
   }

   public boolean testClientPlayerEntity4(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, CreeperEntity creeperEntity, Slot slot, int i) {
      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
      movingFakePlayer.copyFrom(creeperEntity);
      movingFakePlayer.setBoundingBox(creeperEntity.getBoundingBox());
      float f = EnchantmentUtils.computeItemStack2(
         slot.getStack(), clientPlayerEntity, (float)clientPlayerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK)
      );
      movingFakePlayer.takeKnockback(
         f * 0.5F * i,
         MathHelper.sin(clientPlayerEntity.getYaw() * (float) (Math.PI / 180.0)),
         -MathHelper.cos(clientPlayerEntity.getYaw() * (float) (Math.PI / 180.0))
      );

      for (int i2 = creeperEntity.currentFuseTime; i2 < creeperEntity.fuseTime; i2++) {
         movingFakePlayer.tick();
      }

      return this.testClientPlayerEntity5(clientPlayerEntity, clientWorld, creeperEntity, movingFakePlayer);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AutoCreeperModule.Mode)this.mode.getValue()) {
         case Rage:
         default:
            return;
         case Legit:
            HitResult hitResult = mc.crosshairTarget;
            switch (hitResult) {
               case EntityHitResult entityHitResult:
                  if (!(entityHitResult.getEntity() instanceof CreeperEntity creeperEntity)) {
                     return;
                  }

                  if (this.flag) {
                     this.flag = false;
                     Slot slot = itemSwitcher.resolveObject(slotx -> {
                        ItemStack itemStack = slotx.getStack();
                        if (!(itemStack.getItem() instanceof SwordItem)) {
                           return null;
                        } else {
                           return !EnchantmentUtils.testItemStack(itemStack, Enchantments.KNOCKBACK) ? null : slotx;
                        }
                     });
                     if (slot != null) {
                        SilentHotbarModule.getSilentHotbarManager()
                           .handleObject3(this, slot, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig());
                        clientPlayerInteractionManager.attackEntity(clientPlayerEntity, creeperEntity);
                        this.phobosClient
                           .getScheduler()
                           .handleRunnable2(() -> clientPlayerInteractionManager.attackEntity(clientPlayerEntity, creeperEntity), 1);
                        this.normal3.run();
                        return;
                     }
                  }

                  if (!this.normal3.testNumber(this.cooldown.getValue())) {
                     return;
                  }

                  Slot slot2 = itemSwitcher.resolveObject(slot -> {
                     ItemStack itemStack = slot.getStack();
                     if (!(itemStack.getItem() instanceof SwordItem)) {
                        return null;
                     } else {
                        return !EnchantmentUtils.testItemStack(itemStack, Enchantments.KNOCKBACK) ? null : slot;
                     }
                  });
                  if (slot2 == null) {
                     return;
                  }

                  boolean flag = this.testClientPlayerEntity4(clientPlayerEntity, clientWorld, creeperEntity, slot2, 1);
                  if (flag) {
                     SilentHotbarModule.getSilentHotbarManager()
                        .handleObject3(this, slot2, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig());
                     this.phobosClient.getInteractionManager().handleClientPlayerEntity(clientPlayerEntity, creeperEntity);
                     this.normal3.run();
                     return;
                  }

                  boolean flag2 = this.testClientPlayerEntity4(clientPlayerEntity, clientWorld, creeperEntity, slot2, 2);
                  if (flag2 || creeperEntity.currentFuseTime + 15 > creeperEntity.fuseTime) {
                     SilentHotbarModule.getSilentHotbarManager()
                        .handleObject3(this, slot2, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig());
                     this.phobosClient.getInteractionManager().handleClientPlayerEntity(clientPlayerEntity, creeperEntity);
                     this.normal3.run();
                     return;
                  }
                  break;
               case BlockHitResult blockHitResult:
                  if (this.flag2) {
                     this.flag2 = false;
                     Slot slot3 = itemSwitcher.resolveObject(slot -> {
                        ItemStack itemStack = slot.getStack();
                        return !itemStack.isOf(Items.CREEPER_SPAWN_EGG) ? null : slot;
                     });
                     if (slot3 == null) {
                        return;
                     }

                     SilentHotbarModule.getSilentHotbarManager()
                        .handleObject3(this, slot3, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig());
                     clientPlayerInteractionManager.interactBlock(clientPlayerEntity, Hand.MAIN_HAND, blockHitResult);
                     this.flag = true;
                     return;
                  }

                  if (!this.auto.getValue()) {
                     return;
                  }

                  Vec3d vec3d = blockHitResult.getPos();
                  if (vec3d == null) {
                     return;
                  }

                  if (this.testVec3d(vec3d, clientWorld)) {
                     return;
                  }

                  Slot slot4 = itemSwitcher.resolveObject(slot -> {
                     ItemStack itemStack = slot.getStack();
                     if (!(itemStack.getItem() instanceof SwordItem)) {
                        return null;
                     } else {
                        return !EnchantmentUtils.testItemStack(itemStack, Enchantments.KNOCKBACK) ? null : slot;
                     }
                  });
                  if (slot4 == null) {
                     return;
                  }

                  Slot slot5 = itemSwitcher.resolveObject(slot -> {
                     ItemStack itemStack = slot.getStack();
                     return !itemStack.isOf(Items.CREEPER_SPAWN_EGG) ? null : slot;
                  });
                  if (slot5 == null) {
                     return;
                  }

                  if (this.testClientPlayerEntity6(clientPlayerEntity, clientWorld, vec3d, slot4, 1)
                     || this.testClientPlayerEntity6(clientPlayerEntity, clientWorld, vec3d, slot4, 2)) {
                     SilentHotbarModule.getSilentHotbarManager()
                        .handleObject3(this, slot5, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig());
                     clientPlayerInteractionManager.interactBlock(clientPlayerEntity, Hand.MAIN_HAND, blockHitResult);
                     return;
                  }

                  return;
               case null:
               default:
            }
      }
   }

   public PlayerEntity resolvePlayerEntity(PlayerEntity playerEntity) {
      if (!this.prediction.getValue()) {
         return playerEntity;
      }

      PlayerEntity playerEntity2 = this.phobosClient
         .getFeatureRegistry()
         .getPredictionManager()
         .resolvePlayerEntity3(this.predictionTicks.getValue(), playerEntity);
      return playerEntity2 != null ? playerEntity2 : playerEntity;
   }

   public boolean testVec3d(Vec3d vec3d, ClientWorld clientWorld) {
      for (Entity entity : clientWorld.getEntities()) {
         if (entity instanceof CreeperEntity && !(entity.squaredDistanceTo(vec3d) > 9.0)) {
            return true;
         }
      }

      return false;
   }

   public boolean testClientPlayerEntity5(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, CreeperEntity creeperEntity, MovingFakePlayer movingFakePlayer
   ) {
      float f2 = this.computeCreeperEntity(creeperEntity);
      float f3 = this.damage.getValue();

      for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
         if (EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 32.0)) {
            float f = this.phobosClient.getDamageCalculator().computeEntity6(this.resolvePlayerEntity(playerEntity), clientWorld, f2, movingFakePlayer.getPos());
            if (f >= f3) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean testClientPlayerEntity6(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Vec3d vec3d, Slot slot, int i) {
      MovingFakePlayer movingFakePlayer = new MovingFakePlayer(clientWorld);
      movingFakePlayer.setVelocity(0.0, 0.0, 0.0);
      movingFakePlayer.setPosition(vec3d);
      movingFakePlayer.setBoundingBox(EntityDimensions.fixed(0.6F, 1.7F).getBoxAt(vec3d));
      float f = EnchantmentUtils.computeItemStack2(
         slot.getStack(), clientPlayerEntity, (float)clientPlayerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK)
      );
      movingFakePlayer.takeKnockback(
         f * 0.5F * i,
         MathHelper.sin(clientPlayerEntity.getYaw() * (float) (Math.PI / 180.0)),
         -MathHelper.cos(clientPlayerEntity.getYaw() * (float) (Math.PI / 180.0))
      );

      for (int i2 = 0; i2 < 30; i2++) {
         movingFakePlayer.tick();
      }

      return this.testClientPlayerEntity(clientPlayerEntity, clientWorld, movingFakePlayer);
   }

   @Override
   public void run3() {
      this.flag2 = false;
   }

   public AutoCreeperModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoCreeper", Categories.combat, 5);
      this.cooldown = this.resolveSetting2(new LongNumberSetting("Cooldown", 500L, 0L, 5000L));
      this.macro = this.resolveSetting2(new BindSetting("Macro").resolveSetting4(this.mode, AutoCreeperModule.Mode.Legit));
      this.normal3 = new TimeGate.Normal();
      this.flag2 = false;
      this.flag = false;
      this.resolveSettingGroup(this.prediction, this.predictionTicks).resolveSettingGroup4(this.mode, AutoCreeperModule.Mode.Legit);
      this.getSettingGroupList2().forEach(settingGroup -> settingGroup.resolveSettingGroup4(this.mode, AutoCreeperModule.Mode.Rage));
      this.handleListener(
         new SafeListener<OnMouseButtonEvent>() {
            public void handleObject(
               OnMouseButtonEvent onMouseButtonEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (onMouseButtonEvent.check()) {
                  KeyCombination keyCombination = AutoCreeperModule.this.macro.getValue();
                  if (!keyCombination.isEmpty()) {
                     BindKey bindKey = onMouseButtonEvent.resolveBindKey(phobosClient);
                     if (bindKey != null && keyCombination.getBindKeySet().contains(bindKey)) {
                        AutoCreeperModule.this.flag2 = true;
                     }
                  }
               }
            }
         }
      );
   }

   private enum Mode {
      Rage,
      Legit;
   }

   private enum Stage {
      Await,
      Attacked;
   }
}
