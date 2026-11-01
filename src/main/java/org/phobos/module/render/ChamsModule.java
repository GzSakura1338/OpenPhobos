package org.phobos.module.render;

import java.awt.Color;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.CrystalRenderEvent;
import org.phobos.core.render.MultisampleFramebuffer;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.setting.SettingSubsection;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.setting.SettingSection.Builder;
import org.phobos.event.RenderArmEvent;
import org.phobos.event.RenderEvent3;
import org.phobos.event.SafeListener;
import org.phobos.type.DefaultFluidEnum;
import org.phobos.module.Module;
import org.phobos.module.client.RenderModule;

public class ChamsModule extends Module {
   public Setting<Float> floatValueSetting;
   public Setting<ChamsModule.Mode> modeSetting;
   public Setting<DefaultFluidEnum> defaultFluidEnumSetting;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Color> colorSetting;
   public Setting<Color> colorSetting2;
   public Setting<Color> colorSetting3;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Color> colorSetting4;
   public static final float floatValue = (float)Math.sin(Math.PI / 4);
   public Setting<Color> colorSetting5;
   public static ChamsModule chamsModule;
   public Setting<Color> colorSetting6;
   public static boolean flag = false;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Color> colorSetting7;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Boolean> booleanValueSetting8;
   public static final Identifier identifier = Identifier.of("phobos", "textures/glint.png");
   public static final Identifier identifier2 = Identifier.of("phobos", "textures/fade.png");
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Float> floatValueSetting2;
   public Setting<Float> floatValueSetting3;
   public Setting<Boolean> booleanValueSetting10;
   public Setting<Boolean> booleanValueSetting11;
   public Setting<Color> colorSetting8;
   public Setting<Color> colorSetting9;
   public Setting<Float> floatValueSetting4;
   public Setting<Boolean> booleanValueSetting12;
   public Setting<Boolean> booleanValueSetting13;
   public Setting<Boolean> booleanValueSetting14;
   public Setting<Boolean> booleanValueSetting15;
   public Setting<Color> colorSetting10;
   public Setting<Color> colorSetting11;
   public Setting<Boolean> booleanValueSetting16;
   public Setting<Float> floatValueSetting5;
   public Setting<Double> doubleValueSetting;
   public Setting<Color> colorSetting12;
   public Setting<Boolean> booleanValueSetting17;
   public Setting<Color> colorSetting13;
   public Setting<Boolean> booleanValueSetting18;
   public Setting<Boolean> booleanValueSetting19;
   public Setting<Boolean> booleanValueSetting20;
   public Setting<Boolean> booleanValueSetting21;
   public Setting<Boolean> booleanValueSetting22;
   public Setting<DefaultFluidEnum> defaultFluidEnumSetting2;
   public Setting<Float> floatValueSetting6;
   public Setting<Boolean> booleanValueSetting23;
   public Setting<Boolean> booleanValueSetting24;
   public Setting<Boolean> booleanValueSetting25;
   public Setting<Float> floatValueSetting7;
   public Setting<Color> colorSetting14;

   public static Color resolveColor(Color color) {
      return chamsModule.check7() && chamsModule.booleanValueSetting7.getValue() && chamsModule.booleanValueSetting5.getValue()
         ? ColorUtils.createColor6(Color.WHITE, chamsModule.floatValueSetting.getValue())
         : color;
   }

   public void handleRenderEvent3(RenderEvent3<LivingEntity, EntityModel<LivingEntity>> renderEvent3) {
      if (this.testLivingEntity2(renderEvent3.livingEntity) && !renderEvent3.isCancelled() && this.modeSetting.getValue().equals(ChamsModule.Mode.Color)) {
         Render3d.handleMatrixStack2(
            renderEvent3.matrixStack,
            renderEvent3.livingEntity,
            renderEvent3.entityModel,
            renderEvent3.featureRendererList,
            renderEvent3.floatValue,
            renderEvent3.intValue,
            this.colorSetting.getValue(),
            this.booleanValueSetting6.getValue(),
            this.colorSetting4.getValue(),
            this.booleanValueSetting23.getValue(),
            this.colorSetting3.getValue(),
            this.booleanValueSetting.getValue(),
            this.booleanValueSetting4.getValue(),
            this.booleanValueSetting2.getValue(),
            this.booleanValueSetting3.getValue(),
            this.colorSetting7.getValue(),
            this.booleanValueSetting9.getValue(),
            this.floatValueSetting2.getValue(),
            this.floatValueSetting3.getValue(),
            this.booleanValueSetting11.getValue(),
            this.colorSetting2.getValue(),
            this.defaultFluidEnumSetting.getValue().getRenderer()
         );
         if (this.booleanValueSetting8.getValue()) {
            renderEvent3.setCancelled(true);
         }
      }
   }

   public static boolean check() {
      return chamsModule.booleanValueSetting19.getValue();
   }

   public ChamsModule(PhobosClient phobosClient) {
      super(phobosClient, "Chams", Categories.render, "Draws models in a different way");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Boolean var3 = true & true;
      BooleanSetting var2 = new BooleanSetting("Players", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.booleanValueSetting7 = var4;
      ChamsModule.Mode var6 = ChamsModule.Mode.Color;
      EnumSetting var5 = new EnumSetting<>("PlayersMode", var6);
      Setting var7 = this.resolveSetting2(var5);
      this.modeSetting = var7;
      Boolean var9 = true & true;
      BooleanSetting var8 = new BooleanSetting("PlayersModel", var9);
      Setting var10 = var8.resolveSetting7("Model fill");
      Setting var11 = this.resolveSetting2(var10);
      this.booleanValueSetting4 = var11;
      Color var13 = Color.WHITE;
      Color var14 = ColorUtils.createColor6(var13, (float)0.2F);
      ColorSetting var12 = new ColorSetting("PlayersModelColor", var14);
      ColorSetting var15 = var12.getColorSetting();
      Setting var16 = this.modeSetting;
      ChamsModule.Mode[] var17 = new ChamsModule.Mode[1];
      ChamsModule.Mode var18 = ChamsModule.Mode.Color;
      var17[0] = var18;
      Visibility var19 = this.resolveVisibility2(var16, var17);
      Setting var20 = var15.resolveSetting3(var19);
      Setting var21 = this.resolveSetting2(var20);
      this.colorSetting = var21;
      BooleanSetting var22 = new BooleanSetting("PlayersModelLine");
      Setting var23 = var22.resolveSetting7("Model outline");
      Setting var24 = this.resolveSetting2(var23);
      this.booleanValueSetting6 = var24;
      BooleanSetting var25 = new BooleanSetting("PlayersModelLineExtra");
      Setting var26 = var25.resolveSetting7("Renders clothes outline");
      Setting var27 = this.resolveSetting2(var26);
      this.booleanValueSetting2 = var27;
      BooleanSetting var28 = new BooleanSetting("PlayersModelLineForce");
      Setting var29 = this.booleanValueSetting2;
      Setting var30 = var28.resolveSetting(var29);
      Setting var31 = var30.resolveSetting7("Enforces clothing");
      Setting var32 = this.resolveSetting2(var31);
      this.booleanValueSetting3 = var32;
      Color var34 = Color.WHITE;
      ColorSetting var33 = new ColorSetting("PlayersModelLineColor", var34);
      ColorSetting var35 = var33.getColorSetting();
      Setting var36 = this.resolveSetting2(var35);
      this.colorSetting4 = var36;
      Color var38 = Color.CYAN;
      ColorSetting var37 = new ColorSetting("Friends", var38);
      Setting var39 = this.resolveSetting2(var37);
      this.colorSetting6 = var39;
      Color var41 = Color.RED;
      ColorSetting var40 = new ColorSetting("Enemies", var41);
      Setting var42 = this.resolveSetting2(var40);
      this.colorSetting5 = var42;
      BooleanSetting var43 = new BooleanSetting("PlayersOverlay");
      Setting var44 = this.resolveSetting2(var43);
      this.booleanValueSetting11 = var44;
      DefaultFluidEnum var46 = DefaultFluidEnum.Default;
      EnumSetting var45 = new EnumSetting<>("PlayersOverlayType", var46);
      Setting var47 = this.resolveSetting2(var45);
      this.defaultFluidEnumSetting = var47;
      Color var49 = Color.WHITE;
      Color var50 = ColorUtils.createColor6(var49, (float)0.2F);
      ColorSetting var48 = new ColorSetting("PlayersOverlayColor", var50);
      ColorSetting var51 = var48.getColorSetting();
      Setting var52 = this.modeSetting;
      ChamsModule.Mode[] var53 = new ChamsModule.Mode[1];
      ChamsModule.Mode var54 = ChamsModule.Mode.Color;
      var53[0] = var54;
      Visibility var55 = this.resolveVisibility2(var52, var53);
      Setting var56 = var51.resolveSetting3(var55);
      Setting var57 = this.resolveSetting2(var56);
      this.colorSetting2 = var57;
      BooleanSetting var58 = new BooleanSetting("PlayersGlint");
      Setting var59 = var58.resolveSetting7("Glint texture overlay");
      Setting var60 = this.resolveSetting2(var59);
      this.booleanValueSetting = var60;
      Color var62 = Color.WHITE;
      ColorSetting var61 = new ColorSetting("PlayersGlintColor", var62);
      ColorSetting var63 = var61.getColorSetting();
      Setting var64 = this.resolveSetting2(var63);
      this.colorSetting3 = var64;
      BooleanSetting var65 = new BooleanSetting("PlayersFade");
      Setting var66 = var65.resolveSetting7("Fade effect");
      Setting var67 = this.resolveSetting2(var66);
      this.booleanValueSetting9 = var67;
      Color var69 = Color.WHITE;
      ColorSetting var68 = new ColorSetting("PlayersFadeColor", var69);
      ColorSetting var70 = var68.getColorSetting();
      Setting var71 = this.resolveSetting2(var70);
      this.colorSetting7 = var71;
      Float var73 = (float)1.0F;
      Float var74 = (float)0.0F;
      Float var75 = (float)10.0F;
      NumberSetting var72 = new NumberSetting<>("PlayersFadeScale", var73, var74, var75);
      Setting var76 = this.resolveSetting2(var72);
      this.floatValueSetting2 = var76;
      Float var78 = (float)0.5F;
      Float var79 = (float)0.0F;
      Float var80 = (float)10.0F;
      NumberSetting var77 = new NumberSetting<>("PlayersFadeSpeed", var78, var79, var80);
      Setting var81 = this.resolveSetting2(var77);
      this.floatValueSetting3 = var81;
      BooleanSetting var82 = new BooleanSetting("PlayersTransparency");
      Setting var83 = this.resolveSetting2(var82);
      this.booleanValueSetting5 = var83;
      Float var85 = (float)1.0F;
      Float var86 = (float)0.0F;
      Float var87 = (float)1.0F;
      NumberSetting var84 = new NumberSetting<>("PlayersTransparencyAlpha", var85, var86, var87);
      Setting var88 = this.resolveSetting2(var84);
      this.floatValueSetting = var88;
      BooleanSetting var89 = new BooleanSetting("PlayersTexture");
      Setting var90 = var89.resolveSetting7("See through model");
      Setting var91 = this.resolveSetting2(var90);
      this.booleanValueSetting10 = var91;
      BooleanSetting var92 = new BooleanSetting("PlayersCancel");
      Setting var93 = this.modeSetting;
      ChamsModule.Mode var94 = ChamsModule.Mode.Color;
      Setting var95 = var92.resolveSetting4(var93, var94);
      Setting var96 = var95.resolveSetting7("Removes vanilla model");
      Setting var97 = this.resolveSetting2(var96);
      this.booleanValueSetting8 = var97;
      BooleanSetting var98 = new BooleanSetting("Features");
      Setting var99 = this.modeSetting;
      ChamsModule.Mode var100 = ChamsModule.Mode.Color;
      Setting var101 = var98.resolveSetting4(var99, var100);
      Setting var102 = this.booleanValueSetting8;
      Setting var103 = var101.resolveSetting(var102);
      Setting var104 = this.resolveSetting2(var103);
      this.booleanValueSetting23 = var104;
      BooleanSetting var105 = new BooleanSetting("PlayersSelf");
      Setting var106 = var105.resolveSetting7("Apply to local player");
      Setting var107 = this.resolveSetting2(var106);
      this.booleanValueSetting15 = var107;
      Boolean var109 = true & true;
      BooleanSetting var108 = new BooleanSetting("Crystals", var109);
      Setting var110 = this.resolveSetting2(var108);
      this.booleanValueSetting17 = var110;
      Boolean var112 = true & true;
      BooleanSetting var111 = new BooleanSetting("CrystalsModel", var112);
      Setting var113 = var111.resolveSetting7("Model fill");
      Setting var114 = this.resolveSetting2(var113);
      this.booleanValueSetting24 = var114;
      Color var116 = Color.WHITE;
      Color var117 = ColorUtils.createColor6(var116, (float)0.2F);
      ColorSetting var115 = new ColorSetting("CrystalsModelColor", var117);
      ColorSetting var118 = var115.getColorSetting();
      Setting var119 = this.resolveSetting2(var118);
      this.colorSetting8 = var119;
      BooleanSetting var120 = new BooleanSetting("CrystalsModelLine");
      Setting var121 = var120.resolveSetting7("Model outline");
      Setting var122 = this.resolveSetting2(var121);
      this.booleanValueSetting25 = var122;
      Color var124 = Color.WHITE;
      ColorSetting var123 = new ColorSetting("CrystalsModelLineColor", var124);
      ColorSetting var125 = var123.getColorSetting();
      Setting var126 = this.resolveSetting2(var125);
      this.colorSetting11 = var126;
      BooleanSetting var127 = new BooleanSetting("CrystalsOverlay");
      Setting var128 = this.resolveSetting2(var127);
      this.booleanValueSetting14 = var128;
      DefaultFluidEnum var130 = DefaultFluidEnum.Default;
      EnumSetting var129 = new EnumSetting<>("CrystalsOverlayType", var130);
      Setting var131 = this.resolveSetting2(var129);
      this.defaultFluidEnumSetting2 = var131;
      Color var133 = Color.WHITE;
      Color var134 = ColorUtils.createColor6(var133, (float)0.2F);
      ColorSetting var132 = new ColorSetting("CrystalsOverlayColor", var134);
      ColorSetting var135 = var132.getColorSetting();
      Setting var136 = this.resolveSetting2(var135);
      this.colorSetting12 = var136;
      BooleanSetting var137 = new BooleanSetting("CrystalsGlint");
      Setting var138 = var137.resolveSetting7("Glint texture overlay");
      Setting var139 = this.resolveSetting2(var138);
      this.booleanValueSetting16 = var139;
      Color var141 = Color.WHITE;
      ColorSetting var140 = new ColorSetting("CrystalsGlintColor", var141);
      ColorSetting var142 = var140.getColorSetting();
      Setting var143 = this.resolveSetting2(var142);
      this.colorSetting13 = var143;
      BooleanSetting var144 = new BooleanSetting("CrystalsFade");
      Setting var145 = var144.resolveSetting7("Fade effect");
      Setting var146 = this.resolveSetting2(var145);
      this.booleanValueSetting13 = var146;
      Color var148 = Color.WHITE;
      ColorSetting var147 = new ColorSetting("CrystalsFadeColor", var148);
      ColorSetting var149 = var147.getColorSetting();
      Setting var150 = this.resolveSetting2(var149);
      this.colorSetting9 = var150;
      Float var152 = (float)1.0F;
      Float var153 = (float)0.0F;
      Float var154 = (float)10.0F;
      NumberSetting var151 = new NumberSetting<>("CrystalsFadeScale", var152, var153, var154);
      Setting var155 = this.resolveSetting2(var151);
      this.floatValueSetting6 = var155;
      Float var157 = (float)0.5F;
      Float var158 = (float)0.0F;
      Float var159 = (float)10.0F;
      NumberSetting var156 = new NumberSetting<>("CrystalsFadeSpeed", var157, var158, var159);
      Setting var160 = this.resolveSetting2(var156);
      this.floatValueSetting7 = var160;
      BooleanSetting var161 = new BooleanSetting("CrystalsTransparency");
      Setting var162 = this.resolveSetting2(var161);
      this.booleanValueSetting12 = var162;
      Float var164 = (float)1.0F;
      Float var165 = (float)0.0F;
      Float var166 = (float)1.0F;
      NumberSetting var163 = new NumberSetting<>("CrystalsTransparencyAlpha", var164, var165, var166);
      Setting var167 = this.resolveSetting2(var163);
      this.floatValueSetting5 = var167;
      BooleanSetting var168 = new BooleanSetting("CrystalsTexture");
      Setting var169 = var168.resolveSetting7("Texture");
      Setting var170 = this.resolveSetting2(var169);
      this.booleanValueSetting22 = var170;
      BooleanSetting var171 = new BooleanSetting("CrystalsCancel");
      Setting var172 = var171.resolveSetting7("Removes vanilla model");
      Setting var173 = this.resolveSetting2(var172);
      this.booleanValueSetting21 = var173;
      Boolean var175 = false & true;
      BooleanSetting var174 = new BooleanSetting("Hands", var175);
      Setting var176 = this.resolveSetting2(var174);
      this.booleanValueSetting18 = var176;
      Color var178 = Color.WHITE;
      Color var179 = ColorUtils.createColor6(var178, (float)0.2F);
      ColorSetting var177 = new ColorSetting("HandsColor", var179);
      ColorSetting var180 = var177.getColorSetting();
      Setting var181 = this.resolveSetting2(var180);
      this.colorSetting14 = var181;
      BooleanSetting var182 = new BooleanSetting("HandsLine");
      Setting var183 = var182.resolveSetting7("Hands lines");
      Setting var184 = this.resolveSetting2(var183);
      this.booleanValueSetting20 = var184;
      Color var186 = Color.WHITE;
      ColorSetting var185 = new ColorSetting("HandsLineColor", var186);
      ColorSetting var187 = var185.getColorSetting();
      Setting var188 = this.resolveSetting2(var187);
      this.colorSetting10 = var188;
      BooleanSetting var189 = new BooleanSetting("Shine");
      Setting var190 = var189.resolveSetting7("Adds shine effect");
      Setting var191 = this.resolveSetting2(var190);
      this.booleanValueSetting19 = var191;
      Float var193 = (float)1.0F;
      Float var194 = (float)0.1F;
      Float var195 = (float)3.0F;
      NumberSetting var192 = new NumberSetting<>("Width", var193, var194, var195);
      Setting var196 = var192.resolveSetting7("Line width");
      Setting var197 = this.resolveSetting2(var196);
      this.floatValueSetting4 = var197;
      Double var199 = 300.0;
      Double var200 = 0.0;
      Double var201 = 300.0;
      NumberSetting var198 = new NumberSetting<>("Range", var199, var200, var201);
      Setting var202 = var198.resolveSetting7("Chams range");
      Setting var203 = this.resolveSetting2(var202);
      this.doubleValueSetting = var203;
      chamsModule = this;
      this.run5();
      var var204 = new SafeListener<RenderArmEvent>() {
         public void handleObject(
            RenderArmEvent renderArmEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (ChamsModule.this.booleanValueSetting18.getValue()) {
               if (RenderModule.value() == -1) {
                  Render3d.handleRenderArmEvent(
                     renderArmEvent,
                     ChamsModule.this.colorSetting14.getValue(),
                     ChamsModule.this.booleanValueSetting20.getValue(),
                     ChamsModule.this.floatValueSetting4.getValue(),
                     ChamsModule.this.colorSetting10.getValue()
                  );
               } else {
                  MultisampleFramebuffer.handleRunnable(
                     () -> Render3d.handleRenderArmEvent(
                        renderArmEvent,
                        ChamsModule.this.colorSetting14.getValue(),
                        ChamsModule.this.booleanValueSetting20.getValue(),
                        ChamsModule.this.floatValueSetting4.getValue(),
                        ChamsModule.this.colorSetting10.getValue()
                     )
                  );
               }
            }
         }
      };
      this.handleListener(var204);
   }

   public static boolean check2() {
      return getChamsModule().check7() && getChamsModule().booleanValueSetting22.getValue();
   }

   public static float value() {
      return chamsModule.floatValueSetting4.getValue();
   }

   public static boolean testLivingEntity(LivingEntity livingEntity) {
      return getChamsModule().check7()
         && getChamsModule().testLivingEntity2(livingEntity)
         && getChamsModule().booleanValueSetting10.getValue()
         && !(livingEntity instanceof ClientPlayerEntity);
   }

   public static boolean check3() {
      return chamsModule.check7() && chamsModule.booleanValueSetting17.getValue() && chamsModule.booleanValueSetting12.getValue();
   }

   public static Color getColor() {
      return chamsModule.check7() && chamsModule.booleanValueSetting7.getValue() && chamsModule.booleanValueSetting5.getValue()
         ? ColorUtils.createColor6(Color.WHITE, chamsModule.floatValueSetting.getValue())
         : Color.WHITE;
   }

   public static Color resolveColor2(AbstractClientPlayerEntity abstractClientPlayerEntity, Color color) {
      PhobosClient phobosClient = ClientHolder.getPhobosClient();
      if (phobosClient.getFriendList().testUUID(abstractClientPlayerEntity.getUuid())) {
         return ColorUtils.resolveColor3(color, chamsModule.colorSetting6.getValue());
      } else {
         return phobosClient.getEnemyList().testUUID(abstractClientPlayerEntity.getUuid())
            ? ColorUtils.resolveColor3(color, chamsModule.colorSetting5.getValue())
            : color;
      }
   }

   public boolean testLivingEntity2(LivingEntity livingEntity) {
      return !this.booleanValueSetting15.getValue() && livingEntity instanceof ClientPlayerEntity
         ? false
         : this.booleanValueSetting7.getValue() && livingEntity instanceof AbstractClientPlayerEntity;
   }

   public static boolean testEntity(Entity entity) {
      return mc.player != null && mc.player.distanceTo(entity) > chamsModule.doubleValueSetting.getValue();
   }

   public void run5() {
      Builder var1 = this.createBuilder2();
      Setting var2 = this.booleanValueSetting7;
      Builder var3 = var1.resolveBuilder6(var2);
      SettingSubsection[] var4 = new SettingSubsection[1];
      org.phobos.core.setting.SettingSubsection.Builder var5 = this.createBuilder();
      Setting var6 = this.booleanValueSetting4;
      org.phobos.core.setting.SettingSubsection.Builder var7 = var5.resolveBuilder6(var6);
      SettingGroup[] var8 = new SettingGroup[1];
      Setting var9 = this.booleanValueSetting6;
      Setting[] var10 = new Setting[3];
      Setting var11 = this.colorSetting4;
      var10[0] = var11;
      Setting var12 = this.booleanValueSetting2;
      var10[1] = var12;
      Setting var13 = this.booleanValueSetting3;
      var10[2] = var13;
      SettingGroup var14 = this.resolveSettingGroup(var9, var10);
      var8[0] = var14;
      org.phobos.core.setting.SettingSubsection.Builder var15 = var7.resolveBuilder5(var8);
      Setting[] var16 = new Setting[4];
      Setting var17 = this.colorSetting;
      var16[0] = var17;
      Setting var18 = this.booleanValueSetting23;
      var16[1] = var18;
      Setting var19 = this.colorSetting6;
      var16[2] = var19;
      Setting var20 = this.colorSetting5;
      var16[3] = var20;
      org.phobos.core.setting.SettingSubsection.Builder var21 = var15.resolveBuilder(var16);
      Setting var22 = this.modeSetting;
      ChamsModule.Mode[] var23 = new ChamsModule.Mode[1];
      ChamsModule.Mode var24 = ChamsModule.Mode.Color;
      var23[0] = var24;
      Visibility var25 = this.resolveVisibility2(var22, var23);
      org.phobos.core.setting.SettingSubsection.Builder var26 = var21.resolveBuilder3(var25);
      SettingSubsection var27 = var26.getSettingSubsection();
      var4[0] = var27;
      Builder var28 = var3.resolveBuilder4(var4);
      SettingGroup[] var29 = new SettingGroup[4];
      Setting var30 = this.booleanValueSetting;
      Setting[] var31 = new Setting[1];
      Setting var32 = this.colorSetting3;
      var31[0] = var32;
      SettingGroup var33 = this.resolveSettingGroup(var30, var31);
      Setting var34 = this.modeSetting;
      ChamsModule.Mode[] var35 = new ChamsModule.Mode[1];
      ChamsModule.Mode var36 = ChamsModule.Mode.Color;
      var35[0] = var36;
      Visibility var37 = this.resolveVisibility2(var34, var35);
      SettingGroup var38 = var33.resolveSettingGroup5(var37);
      var29[0] = var38;
      Setting var39 = this.booleanValueSetting9;
      Setting[] var40 = new Setting[3];
      Setting var41 = this.colorSetting7;
      var40[0] = var41;
      Setting var42 = this.floatValueSetting2;
      var40[1] = var42;
      Setting var43 = this.floatValueSetting3;
      var40[2] = var43;
      SettingGroup var44 = this.resolveSettingGroup(var39, var40);
      Setting var45 = this.modeSetting;
      ChamsModule.Mode[] var46 = new ChamsModule.Mode[1];
      ChamsModule.Mode var47 = ChamsModule.Mode.Color;
      var46[0] = var47;
      Visibility var48 = this.resolveVisibility2(var45, var46);
      SettingGroup var49 = var44.resolveSettingGroup5(var48);
      var29[1] = var49;
      Setting var50 = this.booleanValueSetting11;
      Setting[] var51 = new Setting[2];
      Setting var52 = this.colorSetting2;
      var51[0] = var52;
      Setting var53 = this.defaultFluidEnumSetting;
      var51[1] = var53;
      SettingGroup var54 = this.resolveSettingGroup(var50, var51);
      var29[2] = var54;
      Setting var55 = this.booleanValueSetting5;
      Setting[] var56 = new Setting[1];
      Setting var57 = this.floatValueSetting;
      var56[0] = var57;
      SettingGroup var58 = this.resolveSettingGroup(var55, var56);
      var29[3] = var58;
      Builder var59 = var28.resolveBuilder7(var29);
      Setting[] var60 = new Setting[4];
      Setting var61 = this.modeSetting;
      var60[0] = var61;
      Setting var62 = this.booleanValueSetting15;
      var60[1] = var62;
      Setting var63 = this.booleanValueSetting10;
      var60[2] = var63;
      Setting var64 = this.booleanValueSetting8;
      var60[3] = var64;
      Builder var65 = var59.resolveBuilder3(var60);
      var65.getSettingSection();
      Builder var66 = this.createBuilder2();
      Setting var67 = this.booleanValueSetting17;
      Builder var68 = var66.resolveBuilder6(var67);
      SettingSubsection[] var69 = new SettingSubsection[1];
      org.phobos.core.setting.SettingSubsection.Builder var70 = this.createBuilder();
      Setting var71 = this.booleanValueSetting24;
      org.phobos.core.setting.SettingSubsection.Builder var72 = var70.resolveBuilder6(var71);
      SettingGroup[] var73 = new SettingGroup[1];
      Setting var74 = this.booleanValueSetting25;
      Setting[] var75 = new Setting[1];
      Setting var76 = this.colorSetting11;
      var75[0] = var76;
      SettingGroup var77 = this.resolveSettingGroup(var74, var75);
      var73[0] = var77;
      org.phobos.core.setting.SettingSubsection.Builder var78 = var72.resolveBuilder5(var73);
      Setting[] var79 = new Setting[1];
      Setting var80 = this.colorSetting8;
      var79[0] = var80;
      org.phobos.core.setting.SettingSubsection.Builder var81 = var78.resolveBuilder(var79);
      SettingSubsection var82 = var81.getSettingSubsection();
      var69[0] = var82;
      Builder var83 = var68.resolveBuilder4(var69);
      SettingGroup[] var84 = new SettingGroup[4];
      Setting var85 = this.booleanValueSetting16;
      Setting[] var86 = new Setting[1];
      Setting var87 = this.colorSetting13;
      var86[0] = var87;
      SettingGroup var88 = this.resolveSettingGroup(var85, var86);
      var84[0] = var88;
      Setting var89 = this.booleanValueSetting13;
      Setting[] var90 = new Setting[3];
      Setting var91 = this.colorSetting9;
      var90[0] = var91;
      Setting var92 = this.floatValueSetting6;
      var90[1] = var92;
      Setting var93 = this.floatValueSetting7;
      var90[2] = var93;
      SettingGroup var94 = this.resolveSettingGroup(var89, var90);
      var84[1] = var94;
      Setting var95 = this.booleanValueSetting14;
      Setting[] var96 = new Setting[2];
      Setting var97 = this.colorSetting12;
      var96[0] = var97;
      Setting var98 = this.defaultFluidEnumSetting2;
      var96[1] = var98;
      SettingGroup var99 = this.resolveSettingGroup(var95, var96);
      var84[2] = var99;
      Setting var100 = this.booleanValueSetting12;
      Setting[] var101 = new Setting[1];
      Setting var102 = this.floatValueSetting5;
      var101[0] = var102;
      SettingGroup var103 = this.resolveSettingGroup(var100, var101);
      var84[3] = var103;
      Builder var104 = var83.resolveBuilder7(var84);
      Setting[] var105 = new Setting[2];
      Setting var106 = this.booleanValueSetting22;
      var105[0] = var106;
      Setting var107 = this.booleanValueSetting21;
      var105[1] = var107;
      Builder var108 = var104.resolveBuilder3(var105);
      var108.getSettingSection();
      org.phobos.core.setting.SettingSubsection.Builder var109 = this.createBuilder();
      Setting var110 = this.booleanValueSetting18;
      org.phobos.core.setting.SettingSubsection.Builder var111 = var109.resolveBuilder6(var110);
      SettingGroup[] var112 = new SettingGroup[1];
      Setting var113 = this.booleanValueSetting20;
      Setting[] var114 = new Setting[1];
      Setting var115 = this.colorSetting10;
      var114[0] = var115;
      SettingGroup var116 = this.resolveSettingGroup(var113, var114);
      var112[0] = var116;
      org.phobos.core.setting.SettingSubsection.Builder var117 = var111.resolveBuilder5(var112);
      Setting[] var118 = new Setting[1];
      Setting var119 = this.colorSetting14;
      var118[0] = var119;
      org.phobos.core.setting.SettingSubsection.Builder var120 = var117.resolveBuilder(var118);
      var120.getSettingSubsection();
      Setting[] var121 = new Setting[3];
      Setting var122 = this.booleanValueSetting19;
      var121[0] = var122;
      Setting var123 = this.floatValueSetting4;
      var121[1] = var123;
      Setting var124 = this.doubleValueSetting;
      var121[2] = var124;
      this.resolveSettingGroup2("Render", var121);
   }

   public static void handleCrystalRenderEvent(CrystalRenderEvent crystalRenderEvent) {
      if (chamsModule.check7()) {
         if (!testEntity(crystalRenderEvent.getEndCrystalEntity())) {
            chamsModule.handleCrystalRenderEvent2(crystalRenderEvent);
         }
      }
   }

   public void handleCrystalRenderEvent2(CrystalRenderEvent crystalRenderEvent) {
      if (!crystalRenderEvent.isCancelled() && this.booleanValueSetting17.getValue()) {
         Render3d.handleEndCrystalEntity(
            crystalRenderEvent.getEndCrystalEntity(),
            crystalRenderEvent.getFloatValue2(),
            crystalRenderEvent.getMatrixStack(),
            crystalRenderEvent.getIntValue(),
            crystalRenderEvent.getModelPart(),
            crystalRenderEvent.getModelPart2(),
            this.colorSetting8.getValue(),
            this.booleanValueSetting25.getValue(),
            this.colorSetting11.getValue(),
            this.colorSetting13.getValue(),
            this.booleanValueSetting16.getValue(),
            this.booleanValueSetting24.getValue(),
            this.colorSetting9.getValue(),
            this.booleanValueSetting13.getValue(),
            this.floatValueSetting6.getValue(),
            this.floatValueSetting7.getValue(),
            this.booleanValueSetting14.getValue(),
            this.colorSetting12.getValue(),
            this.defaultFluidEnumSetting2.getValue().getRenderer()
         );
         if (this.booleanValueSetting21.getValue()) {
            crystalRenderEvent.setCancelled(true);
         }
      }
   }

   public static void handleRenderEvent32(RenderEvent3<LivingEntity, EntityModel<LivingEntity>> renderEvent3) {
      if (chamsModule.check7()) {
         if (!(renderEvent3.livingEntity instanceof ClientPlayerEntity) || !flag) {
            if (!testEntity(renderEvent3.livingEntity)) {
               chamsModule.handleRenderEvent3(renderEvent3);
            }
         }
      }
   }

   public static Color getColor2() {
      return chamsModule.check7() && chamsModule.booleanValueSetting17.getValue() && chamsModule.booleanValueSetting12.getValue()
         ? ColorUtils.createColor6(Color.WHITE, chamsModule.floatValueSetting5.getValue())
         : Color.WHITE;
   }

   public Setting<Color> getFriends() {
      return this.colorSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting3() {
      return this.booleanValueSetting3;
   }

   public Setting<Color> getColorSetting() {
      return this.colorSetting;
   }

   public Setting<Boolean> getBooleanValueSetting11() {
      return this.booleanValueSetting11;
   }

   public Setting<Boolean> getBooleanValueSetting7() {
      return this.booleanValueSetting7;
   }

   public Setting<Boolean> getBooleanValueSetting6() {
      return this.booleanValueSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting4() {
      return this.booleanValueSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public Setting<DefaultFluidEnum> getDefaultFluidEnumSetting() {
      return this.defaultFluidEnumSetting;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.booleanValueSetting2;
   }

   public Setting<ChamsModule.Mode> getModeSetting() {
      return this.modeSetting;
   }

   public Setting<Color> getColorSetting7() {
      return this.colorSetting7;
   }

   public Setting<Color> getColorSetting2() {
      return this.colorSetting2;
   }

   public Setting<Boolean> getBooleanValueSetting9() {
      return this.booleanValueSetting9;
   }

   public Setting<Color> getColorSetting5() {
      return this.colorSetting5;
   }

   public Setting<Color> getColorSetting3() {
      return this.colorSetting3;
   }

   public Setting<Color> getColorSetting4() {
      return this.colorSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting25() {
      return this.booleanValueSetting25;
   }

   public Setting<DefaultFluidEnum> getDefaultFluidEnumSetting2() {
      return this.defaultFluidEnumSetting2;
   }

   public Setting<Boolean> getBooleanValueSetting14() {
      return this.booleanValueSetting14;
   }

   public Setting<Boolean> getBooleanValueSetting15() {
      return this.booleanValueSetting15;
   }

   public Setting<Color> getColorSetting13() {
      return this.colorSetting13;
   }

   public Setting<Boolean> getBooleanValueSetting8() {
      return this.booleanValueSetting8;
   }

   public Setting<Boolean> getBooleanValueSetting17() {
      return this.booleanValueSetting17;
   }

   public Setting<Boolean> getBooleanValueSetting16() {
      return this.booleanValueSetting16;
   }

   public Setting<Boolean> getBooleanValueSetting23() {
      return this.booleanValueSetting23;
   }

   public Setting<Color> getColorSetting8() {
      return this.colorSetting8;
   }

   public Setting<Boolean> getBooleanValueSetting24() {
      return this.booleanValueSetting24;
   }

   public Setting<Color> getColorSetting12() {
      return this.colorSetting12;
   }

   public Setting<Boolean> getBooleanValueSetting10() {
      return this.booleanValueSetting10;
   }

   public Setting<Float> getFloatValueSetting() {
      return this.floatValueSetting;
   }

   public Setting<Float> getFloatValueSetting2() {
      return this.floatValueSetting2;
   }

   public Setting<Float> getFloatValueSetting3() {
      return this.floatValueSetting3;
   }

   public Setting<Color> getColorSetting11() {
      return this.colorSetting11;
   }

   public Setting<Double> getDoubleValueSetting() {
      return this.doubleValueSetting;
   }

   public Setting<Float> getFloatValueSetting4() {
      return this.floatValueSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting20() {
      return this.booleanValueSetting20;
   }

   public Setting<Boolean> getBooleanValueSetting13() {
      return this.booleanValueSetting13;
   }

   public static ChamsModule getChamsModule() {
      return chamsModule;
   }

   public Setting<Boolean> getBooleanValueSetting18() {
      return this.booleanValueSetting18;
   }

   public Setting<Boolean> getBooleanValueSetting19() {
      return this.booleanValueSetting19;
   }

   public Setting<Float> getFloatValueSetting7() {
      return this.floatValueSetting7;
   }

   public Setting<Float> getFloatValueSetting6() {
      return this.floatValueSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting22() {
      return this.booleanValueSetting22;
   }

   public Setting<Color> getColorSetting14() {
      return this.colorSetting14;
   }

   public Setting<Color> getColorSetting9() {
      return this.colorSetting9;
   }

   public Setting<Float> getFloatValueSetting5() {
      return this.floatValueSetting5;
   }

   public Setting<Boolean> getBooleanValueSetting21() {
      return this.booleanValueSetting21;
   }

   public Setting<Color> getColorSetting10() {
      return this.colorSetting10;
   }

   private enum Mode implements TextProvider {
      Color("Normal chams"),
      Texture("Texture through walls"),
      None("No chams are applied");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Mode(final String description) {
         this.text = description;
      }
   }
}
