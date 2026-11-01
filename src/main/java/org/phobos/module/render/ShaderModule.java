package org.phobos.module.render;

import java.awt.Color;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import org.earthhack.ducks.IEntity;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.shader.ShaderEffectManager.Shader;
import org.phobos.event.IsGlowingEvent;
import org.phobos.event.RenderTransformedWorldEvent;
import org.phobos.event.SafeListener;
import org.phobos.mixin.game.render.interfaces.IGameRenderer;
import org.phobos.module.Module;

public class ShaderModule extends Module {
   public Setting<Color> colorSetting;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Color> colorSetting2;
   public Setting<Integer> integerSetting;
   public Setting<Boolean> booleanValueSetting4;
   public static ShaderModule shaderModule;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Color> colorSetting3;
   public Setting<Color> colorSetting4;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Float> floatValueSetting2;
   public Setting<Integer> integerSetting2;
   public Setting<Shader> shaderSetting;
   public Setting<Float> floatValueSetting3;
   public Setting<Color> colorSetting5;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Boolean> booleanValueSetting10;
   public Setting<Boolean> booleanValueSetting11;
   public Setting<Color> colorSetting6;
   public Setting<Float> floatValueSetting4;
   public Setting<ShaderModule.Outline> outlineSetting;
   public Setting<Double> doubleValueSetting;
   public Setting<Float> floatValueSetting5;
   private static Object[] phobos$g1806f6ce0;

   public Setting<Shader> getShaderSetting() {
      return this.shaderSetting;
   }

   public float value() {
      return this.floatValueSetting.getValue();
   }

   public Setting<Boolean> getBooleanValueSetting3() {
      return this.booleanValueSetting3;
   }

   public boolean testEntity(Entity entity) {
      if (entity.isInvisible() && !this.booleanValueSetting11.getValue()) {
         return false;
      }

      if (mc.player != null && this.booleanValueSetting4.getValue() && mc.player.distanceTo(entity) > this.doubleValueSetting.getValue()) {
         return false;
      }

      return switch (entity) {
         case ClientPlayerEntity clientPlayerEntity -> this.booleanValueSetting3.getValue();
         case PlayerEntity playerEntity -> this.booleanValueSetting8.getValue();
         case EndCrystalEntity endCrystalEntity -> this.booleanValueSetting6.getValue();
         case AnimalEntity animalEntity -> this.booleanValueSetting9.getValue();
         case HostileEntity hostileEntity -> this.booleanValueSetting7.getValue();
         case ThrownEntity thrownEntity -> this.booleanValueSetting.getValue();
         case ItemEntity itemEntity -> this.booleanValueSetting10.getValue();
         default -> false;
      };
   }

   public Color resolveColor(Entity entity) {
      if (entity instanceof PlayerEntity playerEntity) {
         if (this.phobosClient.getFriendList().testUUID(playerEntity.getUuid())) {
            return Color.BLUE;
         } else {
            return this.phobosClient.getEnemyList().testUUID(playerEntity.getUuid()) ? Color.RED : Color.WHITE;
         }
      } else {
         return Color.WHITE;
      }
   }

   public float value2() {
      return this.colorSetting.getValue().getAlpha() / 255.0F;
   }

   public Color getColor() {
      return this.colorSetting3.getValue();
   }

   public ShaderModule(PhobosClient phobosClient) {
      super(phobosClient, "Shader", Categories.render, "Shader effects");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      Shader var3 = Shader.Color;
      EnumSetting var2 = new EnumSetting<>("Shader", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.shaderSetting = var4;
      Boolean var6 = true & true;
      BooleanSetting var5 = new BooleanSetting("Players", var6);
      Setting var7 = this.resolveSetting2(var5);
      this.booleanValueSetting8 = var7;
      Boolean var9 = true & true;
      BooleanSetting var8 = new BooleanSetting("Self", var9);
      Setting var10 = this.resolveSetting2(var8);
      this.booleanValueSetting3 = var10;
      Boolean var12 = true & true;
      BooleanSetting var11 = new BooleanSetting("Crystals", var12);
      Setting var13 = this.resolveSetting2(var11);
      this.booleanValueSetting6 = var13;
      Boolean var15 = true & true;
      BooleanSetting var14 = new BooleanSetting("Animals", var15);
      Setting var16 = this.resolveSetting2(var14);
      this.booleanValueSetting9 = var16;
      Boolean var18 = true & true;
      BooleanSetting var17 = new BooleanSetting("Hostiles", var18);
      Setting var19 = this.resolveSetting2(var17);
      this.booleanValueSetting7 = var19;
      Boolean var21 = true & true;
      BooleanSetting var20 = new BooleanSetting("Thrown", var21);
      Setting var22 = this.resolveSetting2(var20);
      this.booleanValueSetting = var22;
      Boolean var24 = true & true;
      BooleanSetting var23 = new BooleanSetting("Hands", var24);
      Setting var25 = this.resolveSetting2(var23);
      this.booleanValueSetting2 = var25;
      Boolean var27 = true & true;
      BooleanSetting var26 = new BooleanSetting("Items", var27);
      Setting var28 = this.resolveSetting2(var26);
      this.booleanValueSetting10 = var28;
      Boolean var30 = true & true;
      BooleanSetting var29 = new BooleanSetting("Invisible", var30);
      Setting var31 = this.resolveSetting2(var29);
      this.booleanValueSetting11 = var31;
      Color var33 = new Color(255, 0, 255, 100);
      ColorSetting var32 = new ColorSetting("FillColor", var33);
      ColorSetting var34 = var32.getColorSetting();
      Setting var35 = this.resolveSetting2(var34);
      this.colorSetting2 = var35;
      Color var37 = new Color(255, 0, 255, 100);
      ColorSetting var36 = new ColorSetting("FillSecondary", var37);
      ColorSetting var38 = var36.getColorSetting();
      Setting var39 = this.shaderSetting;
      Shader[] var40 = new Shader[1];
      Shader var41 = Shader.Circle;
      var40[0] = var41;
      Visibility var42 = this.resolveVisibility2(var39, var40);
      Setting var43 = var38.resolveSetting3(var42);
      Setting var44 = this.resolveSetting2(var43);
      this.colorSetting5 = var44;
      Color var46 = Color.CYAN;
      Color var47 = ColorUtils.createColor3(var46, 100);
      ColorSetting var45 = new ColorSetting("FillFriends", var47);
      Setting var48 = this.resolveSetting2(var45);
      this.colorSetting = var48;
      Color var50 = Color.RED;
      Color var51 = ColorUtils.createColor3(var50, 100);
      ColorSetting var49 = new ColorSetting("FillEnemies", var51);
      Setting var52 = this.resolveSetting2(var49);
      this.colorSetting4 = var52;
      Color var54 = new Color(255, 0, 255, 255);
      ColorSetting var53 = new ColorSetting("OutlineColor", var54);
      ColorSetting var55 = var53.getColorSetting();
      Setting var56 = this.resolveSetting2(var55);
      this.colorSetting6 = var56;
      Color var58 = new Color(255, 0, 255, 255);
      ColorSetting var57 = new ColorSetting("OutlineSecondary", var58);
      ColorSetting var59 = var57.getColorSetting();
      Setting var60 = this.shaderSetting;
      Shader[] var61 = new Shader[1];
      Shader var62 = Shader.Circle;
      var61[0] = var62;
      Visibility var63 = this.resolveVisibility2(var60, var61);
      Setting var64 = var59.resolveSetting3(var63);
      Setting var65 = this.resolveSetting2(var64);
      this.colorSetting3 = var65;
      ShaderModule.Outline var67 = ShaderModule.Outline.Solid;
      EnumSetting var66 = new EnumSetting<>("OutlineType", var67);
      Setting var68 = this.resolveSetting2(var66);
      this.outlineSetting = var68;
      Boolean var70 = true & true;
      BooleanSetting var69 = new BooleanSetting("OutlineSharp", var70);
      Setting var71 = this.outlineSetting;
      ShaderModule.Outline[] var72 = new ShaderModule.Outline[2];
      ShaderModule.Outline var73 = ShaderModule.Outline.Overlay;
      var72[0] = var73;
      ShaderModule.Outline var74 = ShaderModule.Outline.Multilayer;
      var72[1] = var74;
      Setting var75 = var69.resolveSetting8(var71, (Object[])var72);
      Setting var76 = var75.resolveSetting7("Force draws outline");
      Setting var77 = this.resolveSetting2(var76);
      this.booleanValueSetting5 = var77;
      Integer var79 = 2;
      Integer var80 = 0;
      Integer var81 = 8;
      NumberSetting var78 = new NumberSetting<>("OutlineWidth", var79, var80, var81);
      Setting var82 = this.resolveSetting2(var78);
      this.integerSetting = var82;
      Float var84 = (float)40.0F;
      Float var85 = (float)10.0F;
      Float var86 = (float)80.0F;
      NumberSetting var83 = new NumberSetting<>("OutlineSize", var84, var85, var86);
      Setting var87 = this.outlineSetting;
      ShaderModule.Outline[] var88 = new ShaderModule.Outline[2];
      ShaderModule.Outline var89 = ShaderModule.Outline.Overlay;
      var88[0] = var89;
      ShaderModule.Outline var90 = ShaderModule.Outline.Multilayer;
      var88[1] = var90;
      Visibility var91 = Visibility.resolveVisibility3(var87, var88);
      Setting var92 = var83.resolveSetting3(var91);
      Setting var93 = this.resolveSetting2(var92);
      this.floatValueSetting4 = var93;
      Float var95 = (float)15.0F;
      Float var96 = (float)5.0F;
      Float var97 = (float)30.0F;
      NumberSetting var94 = new NumberSetting<>("OutlineScale", var95, var96, var97);
      Setting var98 = this.outlineSetting;
      ShaderModule.Outline[] var99 = new ShaderModule.Outline[2];
      ShaderModule.Outline var100 = ShaderModule.Outline.Overlay;
      var99[0] = var100;
      ShaderModule.Outline var101 = ShaderModule.Outline.Multilayer;
      var99[1] = var101;
      Visibility var102 = Visibility.resolveVisibility3(var98, var99);
      Setting var103 = var94.resolveSetting3(var102);
      Setting var104 = this.resolveSetting2(var103);
      this.floatValueSetting = var104;
      Integer var106 = 8;
      Integer var107 = 1;
      Integer var108 = 100;
      NumberSetting var105 = new NumberSetting<>("Speed", var106, var107, var108);
      Setting var109 = this.resolveSetting2(var105);
      this.integerSetting2 = var109;
      Float var111 = (float)2.5F;
      Float var112 = (float)0.0F;
      Float var113 = (float)10.0F;
      NumberSetting var110 = new NumberSetting<>("Fractal", var111, var112, var113);
      Setting var114 = this.shaderSetting;
      Shader var115 = Shader.Gradient;
      Setting var116 = var110.resolveSetting4(var114, var115);
      Setting var117 = this.resolveSetting2(var116);
      this.floatValueSetting5 = var117;
      Float var119 = (float)2.0F;
      Float var120 = (float)0.0F;
      Float var121 = (float)20.0F;
      NumberSetting var118 = new NumberSetting<>("Factor", var119, var120, var121);
      Setting var122 = this.shaderSetting;
      Shader var123 = Shader.Gradient;
      Setting var124 = var118.resolveSetting4(var122, var123);
      Setting var125 = this.resolveSetting2(var124);
      this.floatValueSetting2 = var125;
      Float var127 = (float)60.0F;
      Float var128 = (float)5.0F;
      Float var129 = (float)100.0F;
      NumberSetting var126 = new NumberSetting<>("Steps", var127, var128, var129);
      Setting var130 = this.shaderSetting;
      Shader var131 = Shader.Circle;
      Setting var132 = var126.resolveSetting4(var130, var131);
      Setting var133 = this.resolveSetting2(var132);
      this.floatValueSetting3 = var133;
      BooleanSetting var134 = new BooleanSetting("Range");
      Setting var135 = var134.resolveSetting7("Enables range calculation");
      Setting var136 = this.resolveSetting2(var135);
      this.booleanValueSetting4 = var136;
      Double var138 = 300.0;
      Double var139 = 0.1;
      Double var140 = 300.0;
      NumberSetting var137 = new NumberSetting<>("RangeValue", var138, var139, var140);
      Setting var141 = var137.resolveSetting7("Shader range");
      Setting var142 = this.resolveSetting2(var141);
      this.doubleValueSetting = var142;
      this.run5();
      var var143 = new SafeListener<IsGlowingEvent>() {
         public void handleObject(
            IsGlowingEvent isGlowingEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            isGlowingEvent.setFlag(ShaderModule.this.testEntity(isGlowingEvent.getEntity()));
         }
      };
      this.handleListener(var143);
      var var144 = new SafeListener<RenderTransformedWorldEvent>() {
         public void handleObject(
            RenderTransformedWorldEvent renderTransformedWorldEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (ShaderModule.this.booleanValueSetting2.getValue()) {
               var1.getShaderEffectManager()
                  .handleRunnable2(
                     () -> ((IGameRenderer)(Object)mc.gameRenderer)
                        .invokeRenderHand(
                           mc.gameRenderer.getCamera(),
                           mc.getRenderTickCounter().getTickDelta(false),
                           renderTransformedWorldEvent.matrixStack().peek().getPositionMatrix()
                        ),
                     ShaderModule.this.shaderSetting.getValue()
                  );
            }
         }
      };
      this.handleListener(var144);
      shaderModule = this;
   }

   @Override
   public void run4() {
      this.phobosClient.getShaderEffectManager().run();
      if (mc.world != null) {
         for (Entity entity : mc.world.getEntities()) {
            ((IEntity)(Object)entity).setPainted(true);
         }
      }
   }

   public Setting<Boolean> getBooleanValueSetting8() {
      return this.booleanValueSetting8;
   }

   public void run5() {
      Setting[] var1 = new Setting[9];
      Setting var2 = this.booleanValueSetting8;
      var1[0] = var2;
      Setting var3 = this.booleanValueSetting3;
      var1[1] = var3;
      Setting var4 = this.booleanValueSetting6;
      var1[2] = var4;
      Setting var5 = this.booleanValueSetting9;
      var1[3] = var5;
      Setting var6 = this.booleanValueSetting7;
      var1[4] = var6;
      Setting var7 = this.booleanValueSetting;
      var1[5] = var7;
      Setting var8 = this.booleanValueSetting2;
      var1[6] = var8;
      Setting var9 = this.booleanValueSetting10;
      var1[7] = var9;
      Setting var10 = this.booleanValueSetting11;
      var1[8] = var10;
      this.resolveSettingGroup2("Selection", var1);
      Setting[] var11 = new Setting[7];
      Setting var12 = this.colorSetting6;
      var11[0] = var12;
      Setting var13 = this.colorSetting3;
      var11[1] = var13;
      Setting var14 = this.outlineSetting;
      var11[2] = var14;
      Setting var15 = this.booleanValueSetting5;
      var11[3] = var15;
      Setting var16 = this.integerSetting;
      var11[4] = var16;
      Setting var17 = this.floatValueSetting4;
      var11[5] = var17;
      Setting var18 = this.floatValueSetting;
      var11[6] = var18;
      this.resolveSettingGroup2("Outline", var11);
      Setting[] var19 = new Setting[4];
      Setting var20 = this.colorSetting2;
      var19[0] = var20;
      Setting var21 = this.colorSetting5;
      var19[1] = var21;
      Setting var22 = this.colorSetting;
      var19[2] = var22;
      Setting var23 = this.colorSetting4;
      var19[3] = var23;
      this.resolveSettingGroup2("Fill", var19);
      Setting[] var24 = new Setting[4];
      Setting var25 = this.integerSetting2;
      var24[0] = var25;
      Setting var26 = this.floatValueSetting5;
      var24[1] = var26;
      Setting var27 = this.floatValueSetting2;
      var24[2] = var27;
      Setting var28 = this.floatValueSetting3;
      var24[3] = var28;
      SettingGroup var29 = this.resolveSettingGroup2("Parameters", var24);
      Visibility var46 = this::check;
      var29.resolveSettingGroup5(var46);
      Setting var47 = this.booleanValueSetting4;
      Setting[] var48 = new Setting[1];
      Setting var49 = this.doubleValueSetting;
      var48[0] = var49;
      this.resolveSettingGroup(var47, var48);
   }

   public float value3() {
      return this.colorSetting4.getValue().getAlpha() / 255.0F;
   }

   public Color getColor2() {
      return this.colorSetting4.getValue();
   }

   public Color getColor3() {
      return this.colorSetting6.getValue();
   }

   public Setting<Boolean> getBooleanValueSetting6() {
      return this.booleanValueSetting6;
   }

   public float value4() {
      return 0.5F;
   }

   public Color getColor4() {
      return this.colorSetting5.getValue();
   }

   public float value5() {
      return this.floatValueSetting4.getValue();
   }

   public int value6() {
      return this.booleanValueSetting5.getValue() ? 1 : 0;
   }

   public Color getColor5() {
      return this.colorSetting.getValue();
   }

   public Color getColor6() {
      return this.colorSetting2.getValue();
   }

   public static boolean testEntity2(Entity entity) {
      return shaderModule.check7() && shaderModule.testEntity(entity);
   }

   public float value7() {
      return this.colorSetting6.getValue().getAlpha() / 255.0F;
   }

   public float value8() {
      return this.colorSetting2.getValue().getAlpha() / 255.0F;
   }

   public Setting<Boolean> getBooleanValueSetting7() {
      return this.booleanValueSetting7;
   }

   public Setting<Color> getColorSetting() {
      return this.colorSetting;
   }

   public Setting<Color> getColorSetting5() {
      return this.colorSetting5;
   }

   public Setting<ShaderModule.Outline> getOutlineSetting() {
      return this.outlineSetting;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public boolean check() {
      return !this.shaderSetting.getValue().equals(Shader.Color);
   }

   public Setting<Float> getFloatValueSetting5() {
      return this.floatValueSetting5;
   }

   public Setting<Color> getColorSetting3() {
      return this.colorSetting3;
   }

   public Setting<Color> getColorSetting6() {
      return this.colorSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting4() {
      return this.booleanValueSetting4;
   }

   public Setting<Float> getFloatValueSetting2() {
      return this.floatValueSetting2;
   }

   public Setting<Integer> getIntegerSetting() {
      return this.integerSetting;
   }

   public Setting<Color> getBackgroundColor() {
      return this.colorSetting2;
   }

   public Setting<Integer> getSpeed() {
      return this.integerSetting2;
   }

   public Setting<Float> getFloatValueSetting() {
      return this.floatValueSetting;
   }

   public Setting<Boolean> getBooleanValueSetting11() {
      return this.booleanValueSetting11;
   }

   public Setting<Boolean> getBooleanValueSetting9() {
      return this.booleanValueSetting9;
   }

   public Setting<Double> getDoubleValueSetting() {
      return this.doubleValueSetting;
   }

   public Setting<Float> getFloatValueSetting3() {
      return this.floatValueSetting3;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.booleanValueSetting2;
   }

   public Setting<Boolean> getBooleanValueSetting10() {
      return this.booleanValueSetting10;
   }

   public static ShaderModule getShaderModule() {
      return shaderModule;
   }

   public Setting<Color> getColorSetting4() {
      return this.colorSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting5() {
      return this.booleanValueSetting5;
   }

   public enum Outline implements TextProvider {
      Solid("Solid color outline"),
      Glow("Glow outline"),
      Overlay("Solid outline with glow"),
      Multilayer("2 Sided glow with an outline");

      public final String text;

      Outline(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
