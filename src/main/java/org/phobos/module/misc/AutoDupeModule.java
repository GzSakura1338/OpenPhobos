package org.phobos.module.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.AutoDupePlayerTickListener;
import org.phobos.module.Module;

public class AutoDupeModule extends Module {
   public Setting<Color> colorSetting;
   public Normal normal;
   public Setting<Color> colorSetting2;
   public Setting<Boolean> booleanValueSetting;
   public Setting<AutoDupeModule.Mode> modeSetting;
   public Map<Integer, Integer> integerMap;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Double> doubleValueSetting;
   public Setting<Float> floatValueSetting;
   public Setting<Long> longValueSetting;
   public Setting<Long> longValueSetting2;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Set<Item>> setSetting;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Integer> integerSetting;

   public void handleEntity(Entity entity) {
      if (this.booleanValueSetting3.getValue()) {
         Render3d.handleCuboid(
            Cuboid.createBuilder()
               .resolveBuilder11(entity.getBoundingBox())
               .resolveBuilder18(this.colorSetting2.getValue(), this.colorSetting.getValue())
               .resolveBuilder12(this.floatValueSetting.getValue())
               .resolveBuilder5(this.longValueSetting2.getValue())
               .getCuboid()
         );
      }
   }

   @Override
   public void run3() {
      if (this.modeSetting.getValue().equals(AutoDupeModule.Mode.Paper) && mc.player != null) {
         this.phobosClient
            .getInventoryManager()
            .handleConsumer(
               itemSwitcher -> {
                  Slot slot2 = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.WRITABLE_BOOK) ? slotx : null);
                  if (slot2 == null) {
                     this.handleText6("You can't perform this dupe without a Book and Quil!");
                  } else {
                     itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot2, 2);

                     for (Slot slot : mc.player.playerScreenHandler.slots) {
                        if (!slot2.equals(slot)) {
                           itemSwitcher.handleSlot2(slot, true);
                        }
                     }

                     mc.player
                        .networkHandler
                        .sendPacket(
                           new BookUpdateC2SPacket(
                              slot2.getIndex(), List.of(ChatTextUtils.describeInt(12), ChatTextUtils.describeInt(41)), Optional.of(ChatTextUtils.describeInt(41))
                           )
                        );
                     this.run11();
                  }
               },
               true
            );
      }
   }

   public void handleClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager, Entity entity, boolean flag, boolean... flags
   ) {
      if (flag) {
         clientPlayerInteractionManager.attackEntity(clientPlayerEntity, entity);
         if (this.booleanValueSetting5.getValue() && flags.length > 0 && flags[0]) {
            clientPlayerInteractionManager.interactEntity(clientPlayerEntity, entity, Hand.MAIN_HAND);
         }
      } else {
         clientPlayerInteractionManager.interactEntity(clientPlayerEntity, entity, Hand.MAIN_HAND);
      }

      this.handleEntity(entity);
      if (this.booleanValueSetting.getValue()) {
         clientPlayerEntity.swingHand(Hand.MAIN_HAND);
      }

      this.normal.run();
   }

   public void handleClientWorld(ClientWorld clientWorld, BlockPos blockPos, BlockState blockState) {
      if (this.booleanValueSetting3.getValue()) {
         Render3d.handleCuboid(
            Cuboid.createBuilder()
               .resolveBuilder11(BoxUtils.resolveBox(clientWorld, blockState, blockPos))
               .resolveBuilder18(this.colorSetting2.getValue(), this.colorSetting.getValue())
               .resolveBuilder12(this.floatValueSetting.getValue())
               .resolveBuilder5(this.longValueSetting2.getValue())
               .getCuboid()
         );
      }
   }

   public AutoDupeModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoDupe", Categories.misc, "Dupes items");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      AutoDupeModule.Mode var3 = AutoDupeModule.Mode.Paper;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      Item[] var6 = new Item[0];
      ItemSetSetting var5 = new ItemSetSetting("Selection", var6);
      Setting var7 = this.resolveSetting2(var5);
      this.setSetting = var7;
      LongNumberSetting var8 = new LongNumberSetting("Delay", 150L, 0L, 2000L, true & true);
      Setting var9 = this.modeSetting;
      AutoDupeModule.Mode var10 = AutoDupeModule.Mode.Frame;
      Setting var11 = var8.resolveSetting4(var9, var10);
      Setting var12 = this.resolveSetting2(var11);
      this.longValueSetting = var12;
      Double var14 = 5.0;
      Double var15 = 0.5;
      Double var16 = 6.0;
      NumberSetting var13 = new NumberSetting<>("Range", var14, var15, var16);
      Setting var17 = this.modeSetting;
      AutoDupeModule.Mode[] var18 = new AutoDupeModule.Mode[2];
      AutoDupeModule.Mode var19 = AutoDupeModule.Mode.Frame;
      var18[0] = var19;
      AutoDupeModule.Mode var20 = AutoDupeModule.Mode.Piston;
      var18[1] = var20;
      Setting var21 = var13.resolveSetting8(var17, (Object[])var18);
      Setting var22 = this.resolveSetting2(var21);
      this.doubleValueSetting = var22;
      Integer var24 = 0;
      Integer var25 = 0;
      Integer var26 = 7;
      NumberSetting var23 = new NumberSetting<>("Orient", var24, var25, var26);
      Integer var27 = 0;
      NumberSetting var28 = var23.resolveNumberSetting("None", var27);
      Setting var29 = this.modeSetting;
      AutoDupeModule.Mode var30 = AutoDupeModule.Mode.Frame;
      Setting var31 = var28.resolveSetting4(var29, var30);
      Setting var32 = this.resolveSetting2(var31);
      this.integerSetting = var32;
      BooleanSetting var33 = new BooleanSetting("Multi");
      Setting var34 = this.modeSetting;
      AutoDupeModule.Mode var35 = AutoDupeModule.Mode.Frame;
      Setting var36 = var33.resolveSetting4(var34, var35);
      Setting var37 = this.resolveSetting2(var36);
      this.booleanValueSetting2 = var37;
      BooleanSetting var38 = new BooleanSetting("Replace");
      Setting var39 = var38.resolveSetting7("Replaces instantly after attacking");
      Setting var40 = this.resolveSetting2(var39);
      this.booleanValueSetting5 = var40;
      BooleanSetting var41 = new BooleanSetting("Rotate");
      Setting var42 = this.modeSetting;
      AutoDupeModule.Mode var43 = AutoDupeModule.Mode.Frame;
      Setting var44 = var41.resolveSetting4(var42, var43);
      Setting var45 = this.resolveSetting2(var44);
      this.booleanValueSetting4 = var45;
      BooleanSetting var46 = new BooleanSetting("Swing");
      Setting var47 = this.modeSetting;
      AutoDupeModule.Mode var48 = AutoDupeModule.Mode.Frame;
      Setting var49 = var46.resolveSetting4(var47, var48);
      Setting var50 = this.resolveSetting2(var49);
      this.booleanValueSetting = var50;
      BooleanSetting var51 = new BooleanSetting("Render");
      Setting var52 = this.resolveSetting2(var51);
      this.booleanValueSetting3 = var52;
      Color var54 = new Color(255, 255, 255, 100);
      ColorSetting var53 = new ColorSetting("RenderBox", var54);
      ColorSetting var55 = var53.getColorSetting();
      Setting var56 = this.resolveSetting2(var55);
      this.colorSetting2 = var56;
      Color var58 = new Color(255, 255, 255, 100);
      ColorSetting var57 = new ColorSetting("RenderLine", var58);
      ColorSetting var59 = var57.getColorSetting();
      Setting var60 = this.resolveSetting2(var59);
      this.colorSetting = var60;
      Float var62 = (float)1.0F;
      Float var63 = (float)0.1F;
      Float var64 = (float)3.0F;
      NumberSetting var61 = new NumberSetting<>("RenderWidth", var62, var63, var64);
      Setting var65 = this.resolveSetting2(var61);
      this.floatValueSetting = var65;
      LongNumberSetting var66 = new LongNumberSetting("RenderTime", 500L, 50L, 1000L);
      Setting var67 = this.resolveSetting2(var66);
      this.longValueSetting2 = var67;
      HashMap var68 = new HashMap();
      this.integerMap = var68;
      Normal var69 = new Normal();
      this.normal = var69;
      Setting var70 = this.booleanValueSetting3;
      Setting[] var71 = new Setting[4];
      Setting var72 = this.colorSetting2;
      var71[0] = var72;
      Setting var73 = this.colorSetting;
      var71[1] = var73;
      Setting var74 = this.floatValueSetting;
      var71[2] = var74;
      Setting var75 = this.longValueSetting2;
      var71[3] = var75;
      SettingGroup var76 = this.resolveSettingGroup(var70, var71);
      Setting var77 = this.modeSetting;
      AutoDupeModule.Mode[] var78 = new AutoDupeModule.Mode[2];
      AutoDupeModule.Mode var79 = AutoDupeModule.Mode.Frame;
      var78[0] = var79;
      AutoDupeModule.Mode var80 = AutoDupeModule.Mode.Piston;
      var78[1] = var80;
      var76.resolveSettingGroup3(var77, var78);
      AutoDupePlayerTickListener var81 = new AutoDupePlayerTickListener(this);
      this.handleListener(var81);
   }

   public enum Mode {
      Paper,
      Frame,
      Piston,
      SalC1;
   }
}
