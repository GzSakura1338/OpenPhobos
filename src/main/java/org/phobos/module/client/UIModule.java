package org.phobos.module.client;

import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.AnimationValue.Factory;
import org.phobos.api.AnimationValue.Float;
import org.phobos.core.gui.screen.ScriptScreen;
import org.phobos.core.gui.screen.ConfigScreen;
import org.phobos.core.gui.screen.HudScreen;
import org.phobos.core.gui.screen.ClickGuiScreen;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.core.gui.GuiManager.UIShader;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class UIModule extends Module {
   public final Setting<Boolean> layout = this.resolveSetting2(new BooleanSetting("Layout", true));
   public static UIModule uIModule;
   public final Setting<UIShader> bindsShader;
   public final Setting<UIShader> slidersShader;
   public final Setting<Boolean> animate = this.resolveSetting2(new BooleanSetting("Animate", true));
   public final Setting<UIShader> categoriesShader;
   public final Setting<Color> slidersColor;
   public final Setting<UIShader> backgroundShader;
   public final Setting<Boolean> lock = this.resolveSetting2(new BooleanSetting("Lock"));
   public final Setting<Color> categoriesColor;
   public final Setting<Color> switchesEnabled;
   public final Setting<Color> switchesDisabled;
   public final Setting<UIShader> modulesShader;
   public final Setting<Boolean> binds;
   public final Setting<UIShader> pickersShader;
   public final Setting<Color> backgroundColor;
   public final Setting<Color> pickersColor;
   public final Setting<Boolean> descriptions;
   public final Setting<Boolean> slide = this.resolveSetting2(new BooleanSetting("Slide"));
   public final Setting<Color> inputsColor;
   public final Setting<UIShader> inputsShader;
   public final Setting<Color> bindsColor;
   public final Setting<Color> modulesDisabled;
   public final Setting<Color> modulesEnabled;
   public final Setting<UIShader> switchesShader;
   public final Setting<Boolean> effects;
   public final Setting<Color> outlineColor;
   public final Setting<java.lang.Float> width;
   public final Setting<Boolean> search;
   public final Setting<UIModule.AnimationType> animation;
   public final Setting<java.lang.Float> scroll;
   public final Setting<Boolean> cPMODE;
   public final Setting<Color> glowColor;
   public final Setting<java.lang.Float> soundsVolume;
   public final Setting<Boolean> sounds;
   public final Setting<Color> itemsColor;
   public final Setting<String> close;
   public final Setting<String> open;
   public final Setting<java.lang.Float> limit;
   public final Setting<UIShader> selectorsShader;
   public final Setting<Color> selectorsColor;
   public final Setting<UIShader> itemsShader;
   public final Setting<UIModule.Order> order;
   public final Setting<Color> text;
   public final Setting<Boolean> glow;
   public final Setting<Boolean> debug;
   public final Setting<java.lang.Float> height;
   public final Setting<Boolean> outline;
   public final Setting<java.lang.Float> offset;

   public static boolean check() {
      return uIModule.search.getValue();
   }

   public static UIModule getUIModule() {
      return uIModule;
   }

   public void run5() {
      Setting[] var1 = new Setting[3];
      Setting var2 = this.modulesEnabled;
      var1[0] = var2;
      Setting var3 = this.modulesDisabled;
      var1[1] = var3;
      Setting var4 = this.modulesShader;
      var1[2] = var4;
      this.resolveSettingGroup2("Modules", var1);
      Setting[] var5 = new Setting[2];
      Setting var6 = this.categoriesColor;
      var5[0] = var6;
      Setting var7 = this.categoriesShader;
      var5[1] = var7;
      this.resolveSettingGroup2("Categories", var5);
      Setting[] var8 = new Setting[2];
      Setting var9 = this.backgroundColor;
      var8[0] = var9;
      Setting var10 = this.backgroundShader;
      var8[1] = var10;
      this.resolveSettingGroup2("Background", var8);
      Setting[] var11 = new Setting[2];
      Setting var12 = this.inputsColor;
      var11[0] = var12;
      Setting var13 = this.inputsShader;
      var11[1] = var13;
      this.resolveSettingGroup2("Inputs", var11);
      Setting var14 = this.binds;
      Setting[] var15 = new Setting[2];
      Setting var16 = this.bindsColor;
      var15[0] = var16;
      Setting var17 = this.bindsShader;
      var15[1] = var17;
      this.resolveSettingGroup(var14, var15);
      Setting[] var18 = new Setting[3];
      Setting var19 = this.switchesEnabled;
      var18[0] = var19;
      Setting var20 = this.switchesDisabled;
      var18[1] = var20;
      Setting var21 = this.switchesShader;
      var18[2] = var21;
      this.resolveSettingGroup2("Switches", var18);
      Setting[] var22 = new Setting[2];
      Setting var23 = this.slidersColor;
      var22[0] = var23;
      Setting var24 = this.slidersShader;
      var22[1] = var24;
      this.resolveSettingGroup2("Sliders", var22);
      Setting[] var25 = new Setting[2];
      Setting var26 = this.pickersColor;
      var25[0] = var26;
      Setting var27 = this.pickersShader;
      var25[1] = var27;
      this.resolveSettingGroup2("Pickers", var25);
      Setting[] var28 = new Setting[2];
      Setting var29 = this.selectorsColor;
      var28[0] = var29;
      Setting var30 = this.selectorsShader;
      var28[1] = var30;
      this.resolveSettingGroup2("Selectors", var28);
      Setting[] var31 = new Setting[2];
      Setting var32 = this.itemsColor;
      var31[0] = var32;
      Setting var33 = this.itemsShader;
      var31[1] = var33;
      this.resolveSettingGroup2("Items", var31);
      Setting var34 = this.outline;
      Setting[] var35 = new Setting[1];
      Setting var36 = this.outlineColor;
      var35[0] = var36;
      this.resolveSettingGroup(var34, var35);
      Setting var37 = this.glow;
      Setting[] var38 = new Setting[1];
      Setting var39 = this.glowColor;
      var38[0] = var39;
      this.resolveSettingGroup(var37, var38);
      Setting var40 = this.sounds;
      Setting[] var41 = new Setting[1];
      Setting var42 = this.soundsVolume;
      var41[0] = var42;
      this.resolveSettingGroup(var40, var41);
   }

   public UIModule(PhobosClient phobosClient) {
      super(phobosClient, "UI", Categories.client, "Client UI", 344);
      this.descriptions = this.resolveSetting2(new BooleanSetting("Descriptions"));
      this.modulesEnabled = this.resolveSetting2(new ColorSetting("ModulesEnabled", new Color(255, 0, 0, 70)).getColorSetting());
      this.modulesDisabled = this.resolveSetting2(new ColorSetting("ModulesDisabled", new Color(255, 0, 0, 45)).getColorSetting());
      this.modulesShader = this.resolveSetting2(new EnumSetting<>("ModulesShader", UIShader.None));
      this.categoriesColor = this.resolveSetting2(new ColorSetting("CategoriesColor", new Color(255, 0, 0, 150)).getColorSetting());
      this.categoriesShader = this.resolveSetting2(new EnumSetting<>("CategoriesShader", UIShader.None));
      this.backgroundColor = this.resolveSetting2(new ColorSetting("BackgroundColor", new Color(0, 0, 0, 70)));
      this.backgroundShader = this.resolveSetting2(new EnumSetting<>("BackgroundShader", UIShader.None));
      this.inputsColor = this.resolveSetting2(new ColorSetting("InputsColor", new Color(255, 0, 0, 70)).getColorSetting());
      this.inputsShader = this.resolveSetting2(new EnumSetting<>("InputsShader", UIShader.None));
      this.binds = this.resolveSetting2(new BooleanSetting("Binds").resolveSetting7("Displays binds"));
      this.bindsColor = this.resolveSetting2(new ColorSetting("BindsColor", new Color(255, 0, 0, 70)).getColorSetting());
      this.bindsShader = this.resolveSetting2(new EnumSetting<>("BindsShader", UIShader.None));
      this.switchesEnabled = this.resolveSetting2(new ColorSetting("SwitchesEnabled", new Color(255, 0, 0, 140)).getColorSetting());
      this.switchesDisabled = this.resolveSetting2(new ColorSetting("SwitchesDisabled", new Color(255, 0, 0, 45)).getColorSetting());
      this.switchesShader = this.resolveSetting2(new EnumSetting<>("SwitchesShader", UIShader.None));
      this.slidersColor = this.resolveSetting2(new ColorSetting("SlidersColor", new Color(255, 0, 0, 150)).getColorSetting());
      this.slidersShader = this.resolveSetting2(new EnumSetting<>("SlidersShader", UIShader.None));
      this.pickersColor = this.resolveSetting2(new ColorSetting("PickersColor", new Color(255, 0, 0, 70)).getColorSetting());
      this.pickersShader = this.resolveSetting2(new EnumSetting<>("PickersShader", UIShader.None));
      this.selectorsColor = this.resolveSetting2(new ColorSetting("SelectorsColor", new Color(255, 0, 0, 70)).getColorSetting());
      this.selectorsShader = this.resolveSetting2(new EnumSetting<>("SelectorsShader", UIShader.None));
      this.itemsColor = this.resolveSetting2(new ColorSetting("ItemsColor", new Color(0, 0, 0, 70)));
      this.itemsShader = this.resolveSetting2(new EnumSetting<>("ItemsShader", UIShader.None));
      this.text = this.resolveSetting2(new ColorSetting("Text", new Color(255, 255, 255, 255)));
      this.sounds = this.resolveSetting2(new BooleanSetting("Sounds"));
      this.soundsVolume = this.resolveSetting2(new NumberSetting<>("SoundsVolume", 1.0F, 0.0F, 1.0F));
      this.effects = this.resolveSetting2(new BooleanSetting("Effects"));
      this.glow = this.resolveSetting2(new BooleanSetting("Glow", false));
      this.glowColor = this.resolveSetting2(new ColorSetting("GlowColor", new Color(255, 255, 255, 255)).getColorSetting());
      this.outline = this.resolveSetting2(new BooleanSetting("Outline").resolveSetting7("Creates an outline"));
      this.outlineColor = this.resolveSetting2(new ColorSetting("OutlineColor", Color.WHITE).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.7F, 1.35F));
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 1.0F, 0.7F, 1.5F));
      this.offset = this.resolveSetting2(new NumberSetting<>("Offset", 0.0F, -3.0F, 3.0F));
      this.limit = this.resolveSetting2(
         new NumberSetting<>("Limit", 10.0F, 10.0F, 500.0F).resolveNumberSetting("None", 10.0F).resolveSetting7("Window height limit")
      );
      this.scroll = this.resolveSetting2(new NumberSetting<>("Scroll", 100.0F, 0.1F, 200.0F, "%").resolveSetting7("Scrolling speed"));
      this.open = this.resolveSetting2(new StringSetting("Open", "+"));
      this.close = this.resolveSetting2(new StringSetting("Close", "-"));
      this.animation = this.resolveSetting2(new EnumSetting<>("Animation", UIModule.AnimationType.Fast).resolveSetting7("Animation vendor"));
      this.order = this.resolveSetting2(new EnumSetting<>("Order", UIModule.Order.Default));
      this.search = this.resolveSetting2(new BooleanSetting("Search", true).resolveSetting7("Enables search"));
      this.cPMODE = this.resolveSetting2(new BooleanSetting("CPMODE", false));
      this.debug = this.resolveSetting2(new BooleanSetting("Debug"));
      this.run5();
      uIModule = this;
      this.lock.handleConsumer2(booleanValue -> {
         ClickGuiScreen.clickGuiScreen.getGuiPanelList().forEach(guiPanel -> guiPanel.setFlag102(booleanValue));
         ScriptScreen.scriptScreen.getScriptPanel().setFlag102(booleanValue);
         ScriptScreen.scriptScreen.getScriptPanel2().setFlag102(booleanValue);
         ConfigScreen.configScreen.getConfigPanel2().setFlag102(booleanValue);
         ConfigScreen.configScreen.getConfigPanel().setFlag102(booleanValue);
         HudScreen.hudScreen.getHudPanel().setFlag102(booleanValue);
      });
      this.order.handleConsumer2(order -> {
         ClickGuiScreen.clickGuiScreen.run();
         if (mc.currentScreen instanceof ClickGuiScreen) {
            this.run11();
            this.run12();
         }
      });
      this.handleListener(
         new Listener<TickEvent2>() {
            public void handleObject2(TickEvent2 tickEvent2) {
               if (!(MinecraftHolder.mc.currentScreen instanceof ClickGuiScreen)
                  && !(MinecraftHolder.mc.currentScreen instanceof HudScreen)
                  && !(MinecraftHolder.mc.currentScreen instanceof ScriptScreen)
                  && !(MinecraftHolder.mc.currentScreen instanceof ConfigScreen)) {
                  UIModule.this.run11();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<Receive<CloseScreenS2CPacket>>() {
            public void handleObject(
               Receive<CloseScreenS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               receive.setCancelled(true);
            }
         }
      );
   }

   @Override
   public void run3() {
      if (mc.currentScreen == null) {
         MenuNavigation.handleScreen(ClickGuiScreen.clickGuiScreen);
      }
   }

   @Override
   public void run4() {
      if (mc.currentScreen instanceof ClickGuiScreen) {
         mc.setScreen(null);
      }
   }

   public static boolean check2() {
      return !uIModule.layout.getValue() && !uIModule.lock.getValue();
   }

   public enum AnimationType {
      Fast(FloatAnimator::new),
      Flow(org.phobos.core.gui.UiValues.Float::new);

      public final Factory<Float> factory;

      public Factory<Float> getFactory() {
         return this.factory;
      }

      AnimationType(final Factory<Float> factory) {
         this.factory = factory;
      }
   }

   public enum Order {
      Default,
      Inverse;
   }
}
