package org.phobos.module.combat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.earthhack.ducks.ISlot;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.TimeGate.Normal;
import org.phobos.api.NumberRange.Mutable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.math.FloatPair;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.inventory.ScheduledClick;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.inventory.ActionTimer;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.event.BreakerAttackPreListener;
import org.phobos.event.BreakerRenderContextListener;
import org.phobos.event.BreakerPlayerTickListener;
import org.phobos.module.Module;

public class BreakerModule extends Module {
   public final Setting<Boolean> swing;
   public final Setting<BreakerModule.Update> update;
   public final Setting<Map<BreakerModule.Type, Boolean>> types;
   public final Setting<Long> delay;
   public final Setting<Boolean> inventory;
   public final Normal normal;
   public final Setting<Long> inventoryTimeout;
   public final Setting<BreakerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", BreakerModule.Mode.Phase));
   public final Normal normal2;
   public boolean flag;
   public Slot slot;
   public final Setting<Mutable<Long>> inventoryClose;
   public final Setting<Boolean> manual;
   public int intValue;
   public final Setting<PlacementModule.Rotation> rotate;
   public final Setting<BreakerModule.Replace> inventoryReplace;
   public final Setting<Boolean> stun;
   public boolean flag2;
   public final Setting<Boolean> verify;

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      @NotNull BreakerModule.Update update
   ) {
      if (this.types.getValue().get(BreakerModule.Type.Frames)) {
         Set<Entity> set = this.resolveSet(clientPlayerEntity, clientWorld);
         if (set.isEmpty()) {
            return;
         }

         for (Entity entity : set) {
            if (!this.rotate.testObject2(PlacementModule.Rotation.None) && !RaycastUtils.testPhobosClient(this.phobosClient, entity)) {
               if (update.equals(BreakerModule.Update.Async)) {
                  if (this.rotate.testObject2(PlacementModule.Rotation.Normal)) {
                     continue;
                  }
               } else {
                  if (RotationManager.isFlag()) {
                     continue;
                  }

                  float[] floats = RotationUtils.values(clientPlayerEntity, entity);
                  switch ((PlacementModule.Rotation)this.rotate.getValue()) {
                     case Instant:
                        this.phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                        break;
                     case Normal:
                        PlayerActionUtils.handleClientPlayerEntity17(clientPlayerEntity, floats[0], floats[1], clientPlayerEntity.isOnGround());
                        break;
                     case Hybrid:
                        this.phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                  }
               }
            }

            this.phobosClient.getInteractionManager().handleEntity3(entity, clientPlayerEntity);
            if (this.swing.getValue()) {
               clientPlayerEntity.swingHand(Hand.MAIN_HAND);
            }

            if (this.rotate.testObject2(PlacementModule.Rotation.Silent)) {
               this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
            }
         }
      }

      if (this.types.getValue().get(BreakerModule.Type.Scaffolding)) {
         for (BlockPos blockPos : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getX() + 0.25)) {
            if (clientWorld.getBlockState(blockPos).getBlock() instanceof ScaffoldingBlock) {
               clientPlayerInteractionManager.attackBlock(blockPos, Direction.UP);
            }
         }
      }
   }

   public void run() {
      if (mc.player != null) {
         if (mc.currentScreen == null) {
            if (this.flag2) {
               if (this.slot != null && this.intValue != -1) {
                  ISlot iSlot = ISlot.cast(this.slot);
                  mc.setScreen(new ActionTimer(mc.player, 3));
                  ClickScheduler.getINSTANCE().handleISlot(iSlot, FloatPair.createFloatPair(30.0F, 70.0F));
                  ClickScheduler.getINSTANCE().handleSlot(this.slot, new ScheduledClick(this.intValue, SlotActionType.SWAP));
                  ClickScheduler.getINSTANCE().handleSlot2(this.slot, 3, this.inventoryClose.getValue().getNumber4());
                  this.run2();
               } else {
                  this.flag2 = false;
               }
            }
         }
      }
   }

   public Set<Entity> resolveSet(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      Box box = clientPlayerEntity.getBoundingBox();
      Set<Entity> set = new HashSet<>();

      for (Entity entity : clientWorld.getEntities()) {
         if (entity.getBoundingBox().intersects(box) && entity instanceof ItemFrameEntity) {
            set.add(entity);
         }
      }

      return set;
   }

   public void run5() {
      Setting var1 = this.inventory;
      Setting[] var2 = new Setting[3];
      Setting var3 = this.inventoryTimeout;
      var2[0] = var3;
      Setting var4 = this.inventoryClose;
      var2[1] = var4;
      Setting var5 = this.inventoryReplace;
      var2[2] = var5;
      SettingGroup var6 = this.resolveSettingGroup(var1, var2);
      Setting var7 = this.mode;
      BreakerModule.Mode var8 = BreakerModule.Mode.Shield;
      var6.resolveSettingGroup4(var7, var8);
      BreakerAttackPreListener var9 = new BreakerAttackPreListener(this);
      this.handleListener(var9);
      BreakerPlayerTickListener var10 = new BreakerPlayerTickListener(this);
      this.handleListener(var10);
      BreakerRenderContextListener var11 = new BreakerRenderContextListener(this);
      this.handleListener(var11);
   }

   public void run2() {
      this.flag2 = false;
      this.slot = null;
      this.intValue = -1;
   }

   public boolean testLivingEntity(LivingEntity livingEntity) {
      return this.verify.getValue()
         ? livingEntity.isBlocking()
         : livingEntity.getMainHandStack().isOf(Items.SHIELD) || livingEntity.getOffHandStack().isOf(Items.SHIELD);
   }

   public BreakerModule(PhobosClient phobosClient) {
      super(phobosClient, "Breaker", Categories.combat, "Breaks item frames and scaffolding near you");
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 150L, 0L, 1000L, true).resolveSetting4(this.mode, BreakerModule.Mode.Shield));
      this.update = this.resolveSetting2(
         new EnumSetting<>("Update", BreakerModule.Update.Async).resolveSetting7("Update stage").resolveSetting4(this.mode, BreakerModule.Mode.Phase)
      );
      this.types = this.resolveSetting2(new MultiSelectSetting<>("Types", BreakerModule.Type.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (BreakerModule.Type type : BreakerModule.Type.values()) {
            multiSelectSetting.resolveMultiSelectSetting(type, type.name());
         }
      }).resolveSetting4(this.mode, BreakerModule.Mode.Phase));
      this.rotate = this.resolveSetting2(new EnumSetting<>("Rotate", PlacementModule.Rotation.None).resolveSetting4(this.mode, BreakerModule.Mode.Phase));
      this.swing = this.resolveSetting2(new BooleanSetting("Swing").resolveSetting4(this.mode, BreakerModule.Mode.Phase));
      this.inventory = this.resolveSetting2(new BooleanSetting("Inventory").resolveSetting7("Gets the axe from inventory"));
      this.inventoryTimeout = this.resolveSetting2(new LongNumberSetting("InventoryTimeout", 200L, 0L, 500L));
      this.inventoryClose = this.resolveSetting2(new MutableLongSetting("InventoryClose", 100L, 0L, 1000L, true));
      this.inventoryReplace = this.resolveSetting2(new EnumSetting<>("InventoryReplace", BreakerModule.Replace.Sword));
      this.manual = this.resolveSetting2(new BooleanSetting("Manual", true).resolveSetting7("Requires you to attack the enemy"));
      this.stun = this.resolveSetting2(new BooleanSetting("Stun").resolveSetting4(this.mode, BreakerModule.Mode.Shield));
      this.verify = this.resolveSetting2(
         new BooleanSetting("Verify").resolveSetting7("Checks if the target is using the shield").resolveSetting4(this.mode, BreakerModule.Mode.Shield)
      );
      this.normal2 = new Normal();
      this.normal = new Normal();
      this.flag2 = false;
      this.slot = null;
      this.flag = false;
      this.intValue = -1;
      this.run5();
   }

   @Override
   public void run4() {
      this.run();
      this.flag = false;
   }

   @Override
   public String nothing() {
      return switch ((BreakerModule.Mode)this.mode.getValue()) {
         case Phase -> "Phase " + this.update.getValue();
         case Shield -> "Shield";
      };
   }

   public enum Mode {
      Phase,
      Shield;
   }

   public enum Replace {
      Sword,
      Shield;
   }

   public enum Type {
      Frames,
      Scaffolding;
   }

   public enum Update {
      Sync,
      Async;
   }
}
