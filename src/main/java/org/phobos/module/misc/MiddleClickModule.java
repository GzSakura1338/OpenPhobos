package org.phobos.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.bind.BindKey;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.SafeListener;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.module.Module;
import org.phobos.module.player.AutoBlinkModule;

public class MiddleClickModule extends Module {
   public static MiddleClickModule middleClickModule;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Silent> silentSetting;
   public Setting<MiddleClickModule.Rotate> rotateSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<KeyCombination> keyCombinationSetting;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Silent> silentSetting2;

   public Setting<Boolean> getBooleanValueSetting3() {
      return this.booleanValueSetting3;
   }

   public static MiddleClickModule getMiddleClickModule() {
      return middleClickModule;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.booleanValueSetting2;
   }

   public Setting<Silent> getSilentSetting2() {
      return this.silentSetting2;
   }

   public Setting<MiddleClickModule.Rotate> getRotate() {
      return this.rotateSetting;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager) {
      MutableBoolean mutableBoolean = new MutableBoolean(false);
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
         SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.FIREWORK_ROCKET, this.silentSetting2.getValue().getFlag() | 1);
         if (switchResult != null) {
            clientPlayerInteractionManager.interactItem(clientPlayerEntity, switchResult.hand());
            mutableBoolean.setTrue();
         }
      });
      return mutableBoolean.getValue();
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager) {
      if (this.phobosClient.getElytraManager().isFlag()) {
         this.phobosClient.getElytraManager().handleInt(this.silentSetting2.getValue().getFlag());
         return true;
      } else if (PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity)) {
         MutableBoolean mutableBoolean = new MutableBoolean(false);
         this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
            SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, Items.FIREWORK_ROCKET, this.silentSetting2.getValue().getFlag() | 1);
            if (switchResult != null) {
               clientPlayerInteractionManager.interactItem(clientPlayerEntity, switchResult.hand());
               mutableBoolean.setTrue();
            }
         });
         return mutableBoolean.getValue();
      } else {
         return false;
      }
   }

   public MiddleClickModule(PhobosClient phobosClient) {
      super(phobosClient, "MiddleClick", Categories.misc, "Middle-click (M3) extensions");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      Boolean var3 = false & true;
      BooleanSetting var2 = new BooleanSetting("Friend", var3);
      Setting var4 = var2.resolveSetting7("Friend cursor entity");
      Setting var5 = this.resolveSetting2(var4);
      this.booleanValueSetting2 = var5;
      Boolean var7 = false & true;
      BooleanSetting var6 = new BooleanSetting("Pearl", var7);
      Setting var8 = var6.resolveSetting7("Throw a pearl");
      Setting var9 = this.resolveSetting2(var8);
      this.booleanValueSetting = var9;
      Silent var11 = Silent.Normal;
      EnumSetting var10 = new EnumSetting<>("PearlSwitch", var11);
      Setting var12 = this.resolveSetting2(var10);
      this.silentSetting = var12;
      MiddleClickModule.Rotate var14 = MiddleClickModule.Rotate.Normal;
      EnumSetting var13 = new EnumSetting<>("PearlRotate", var14);
      Setting var15 = var13.resolveSetting7("Additional rotation");
      Setting var16 = this.resolveSetting2(var15);
      this.rotateSetting = var16;
      Boolean var18 = false & true;
      BooleanSetting var17 = new BooleanSetting("Firework", var18);
      Setting var19 = var17.resolveSetting7("Uses a firework");
      Setting var20 = this.resolveSetting2(var19);
      this.booleanValueSetting3 = var20;
      Silent var22 = Silent.Normal;
      EnumSetting var21 = new EnumSetting<>("FireworkSwitch", var22);
      Setting var23 = this.resolveSetting2(var21);
      this.silentSetting2 = var23;
      BindSetting var24 = new BindSetting("Key");
      Setting var25 = var24.resolveSetting7("Additional key");
      Setting var26 = this.resolveSetting2(var25);
      this.keyCombinationSetting = var26;
      Setting var27 = this.booleanValueSetting;
      Setting[] var28 = new Setting[2];
      Setting var29 = this.silentSetting;
      var28[0] = var29;
      Setting var30 = this.rotateSetting;
      var28[1] = var30;
      this.resolveSettingGroup(var27, var28);
      Setting var31 = this.booleanValueSetting3;
      Setting[] var32 = new Setting[1];
      Setting var33 = this.silentSetting2;
      var32[0] = var33;
      this.resolveSettingGroup(var31, var32);
      var var34 = new SafeListener<OnMouseButtonEvent>() {
         public void handleObject(
            OnMouseButtonEvent onMouseButtonEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (onMouseButtonEvent.getIntValue() == 1 && mc.currentScreen == null) {
               if (!MiddleClickModule.this.keyCombinationSetting.getValue().isEmpty()) {
                  BindKey bindKey = var1.getBindManager()
                     .resolveBindKey(onMouseButtonEvent.getKeyboardMouseEnum(), onMouseButtonEvent.getIntValue2());
                  if (bindKey == null || !MiddleClickModule.this.keyCombinationSetting.getValue().getBindKeySet().contains(bindKey)) {
                     return;
                  }
               } else if (!onMouseButtonEvent.getKeyboardMouseEnum().equals(KeyboardMouseEnum.Mouse) || onMouseButtonEvent.getIntValue2() != 2) {
                  return;
               }

               if (!MiddleClickModule.this.booleanValueSetting3.getValue()
                  || !MiddleClickModule.this.testClientPlayerEntity2(clientPlayerEntity, clientPlayerInteractionManager)) {
                  if (MiddleClickModule.this.booleanValueSetting2.getValue()
                     && mc.crosshairTarget instanceof EntityHitResult entityHitResult
                     && entityHitResult.getEntity() instanceof PlayerEntity playerEntity) {
                     if (var1.getFriendList().testUUID(playerEntity.getUuid())) {
                        var1.getFriendList().handleUUID(playerEntity.getUuid());
                     } else {
                        var1.getFriendList().handleText(playerEntity.getNameForScoreboard(), playerEntity.getUuid());
                     }
                  } else {
                     if (MiddleClickModule.this.booleanValueSetting.getValue()) {
                        if (RotationManager.isFlag() && MiddleClickModule.this.rotateSetting.getValue().equals(MiddleClickModule.Rotate.None)) {
                           return;
                        }

                        var1.getInventoryManager()
                           .handleConsumer2(
                              itemSwitcher -> {
                                 SwitchResult switchResult = itemSwitcher.resolveSwitchResult3(
                                    Hand.MAIN_HAND, Items.ENDER_PEARL, MiddleClickModule.this.silentSetting.getValue().getFlag() | 1
                                 );
                                 if (switchResult != null) {
                                    boolean flag = false;
                                    if (MiddleClickModule.this.rotateSetting.getValue().equals(MiddleClickModule.Rotate.Silent)) {
                                       var1.getRotationManager()
                                          .handleClientPlayerEntity3(clientPlayerEntity, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch());
                                       flag = true;
                                    }

                                    TickUtils.handleClientWorld(
                                       clientWorld,
                                       integer -> clientPlayerEntity.networkHandler
                                          .sendPacket(
                                             new PlayerInteractItemC2SPacket(
                                                switchResult.hand(), integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch()
                                             )
                                          )
                                    );
                                    if (flag) {
                                       var1.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
                                    }

                                    AutoBlinkModule.getAutoBlinkModule().run();
                                 }
                              }
                           );
                     }
                  }
               }
            }
         }
      };
      this.handleListener(var34);
      middleClickModule = this;
   }

   public static boolean check() {
      return middleClickModule.check7() && middleClickModule.keyCombinationSetting.getValue().isEmpty();
   }

   public Setting<KeyCombination> getKeyCombinationSetting() {
      return this.keyCombinationSetting;
   }

   public Setting<Silent> getSilentSetting() {
      return this.silentSetting;
   }

   private enum Rotate {
      None,
      Normal,
      Silent;
   }
}
