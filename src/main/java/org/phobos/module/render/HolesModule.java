package org.phobos.module.render;

import java.awt.Color;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chunk.Hole;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.event.HolesTickListener;
import org.phobos.event.HolesRenderContextListener;
import org.phobos.module.Module;

public class HolesModule extends Module {
   public Setting<Float> floatValueSetting;
   public Setting<Float> floatValueSetting2;
   public Setting<Color> colorSetting;
   public Setting<Float> floatValueSetting3;
   public static int intValue = 1;
   public Setting<Float> floatValueSetting4;
   public Setting<Double> doubleValueSetting;
   public Setting<Integer> integerSetting;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Color> colorSetting2;
   public Setting<Float> floatValueSetting5;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Color> colorSetting3;
   public Setting<Color> colorSetting4;
   public Setting<Float> floatValueSetting6;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Color> colorSetting5;
   public Setting<Color> colorSetting6;
   public Setting<Color> colorSetting7;
   public Setting<Color> colorSetting8;
   public Setting<Boolean> booleanValueSetting5;
   public PriorityQueue<Hole> holePriorityQueue;
   public Setting<Color> colorSetting9;
   public Setting<Float> floatValueSetting7;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Float> floatValueSetting8;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Float> floatValueSetting9;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Color> colorSetting10;
   public Setting<Float> floatValueSetting10;
   public Setting<Color> colorSetting11;
   public Setting<Boolean> booleanValueSetting10;
   public Setting<Color> colorSetting12;
   public Setting<Color> colorSetting13;
   public Setting<Double> doubleValueSetting2;
   public Setting<Double> doubleValueSetting3;
   public Setting<Color> colorSetting14;
   public Setting<Boolean> booleanValueSetting11;
   public Color color;
   public Setting<Color> colorSetting15;
   private static Object[] phobos$g1806f9f78;

   public HolesModule.PaintData createPaintData(Hole hole) {
      if (hole.isFlag6()) {
         return new HolesModule.PaintData(
            this.booleanValueSetting10.getValue(),
            this.colorSetting15.getValue(),
            this.colorSetting13.getValue(),
            this.colorSetting14.getValue(),
            this.floatValueSetting8.getValue(),
            this.floatValueSetting7.getValue(),
            this.booleanValueSetting8.getValue()
         );
      } else if (hole.isFlag()) {
         return new HolesModule.PaintData(
            this.booleanValueSetting5.getValue(),
            this.colorSetting3.getValue(),
            this.colorSetting5.getValue(),
            this.colorSetting10.getValue(),
            this.floatValueSetting6.getValue(),
            this.floatValueSetting4.getValue(),
            this.booleanValueSetting6.getValue()
         );
      } else {
         return hole.isFlag22()
            ? (
               hole.isFlag4()
                  ? new HolesModule.PaintData(
                     this.booleanValueSetting3.getValue(),
                     this.colorSetting8.getValue(),
                     this.colorSetting4.getValue(),
                     this.colorSetting2.getValue(),
                     this.floatValueSetting.getValue(),
                     this.floatValueSetting2.getValue(),
                     this.booleanValueSetting.getValue()
                  )
                  : new HolesModule.PaintData(
                     this.booleanValueSetting4.getValue(),
                     this.colorSetting7.getValue(),
                     this.colorSetting6.getValue(),
                     this.colorSetting.getValue(),
                     this.floatValueSetting3.getValue(),
                     this.floatValueSetting5.getValue(),
                     this.booleanValueSetting2.getValue()
                  )
            )
            : new HolesModule.PaintData(
               this.booleanValueSetting7.getValue(),
               this.colorSetting9.getValue(),
               this.colorSetting12.getValue(),
               this.colorSetting11.getValue(),
               this.floatValueSetting9.getValue(),
               this.floatValueSetting10.getValue(),
               this.booleanValueSetting11.getValue()
            );
      }
   }

   public HolesModule(PhobosClient phobosClient) {
      super(phobosClient, "Holes", Categories.render, "Shows you holes in the ground");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Double var3 = 15.0;
      Double var4 = 1.0;
      Double var5 = 100.0;
      NumberSetting var2 = new NumberSetting<>("Range", var3, var4, var5);
      Setting var6 = this.resolveSetting2(var2);
      this.doubleValueSetting = var6;
      Integer var8 = 30;
      Integer var9 = 0;
      Integer var10 = 300;
      NumberSetting var7 = new NumberSetting<>("Limit", var8, var9, var10);
      Setting var11 = this.resolveSetting2(var7);
      this.integerSetting = var11;
      BooleanSetting var12 = new BooleanSetting("Safe");
      Setting var13 = this.resolveSetting2(var12);
      this.booleanValueSetting3 = var13;
      Color var15 = new Color(0, 255, 0, 50);
      ColorSetting var14 = new ColorSetting("SafeBox", var15);
      Setting var16 = this.resolveSetting2(var14);
      this.colorSetting8 = var16;
      Color var18 = new Color(0, 255, 0, 255);
      ColorSetting var17 = new ColorSetting("SafeLine", var18);
      Setting var19 = this.resolveSetting2(var17);
      this.colorSetting4 = var19;
      Float var21 = (float)1.0F;
      Float var22 = (float)0.0F;
      Float var23 = (float)3.0F;
      NumberSetting var20 = new NumberSetting<>("SafeWidth", var21, var22, var23);
      Setting var24 = this.resolveSetting2(var20);
      this.floatValueSetting = var24;
      Float var26 = (float)1.0F;
      Float var27 = (float)-1.0F;
      Float var28 = (float)2.0F;
      NumberSetting var25 = new NumberSetting<>("SafeHeight", var26, var27, var28);
      Setting var29 = this.resolveSetting2(var25);
      this.floatValueSetting2 = var29;
      BooleanSetting var30 = new BooleanSetting("SafeGlow");
      Setting var31 = this.resolveSetting2(var30);
      this.booleanValueSetting = var31;
      Color var33 = new Color(0, 255, 0, 30);
      ColorSetting var32 = new ColorSetting("SafeGlowColor", var33);
      Setting var34 = this.resolveSetting2(var32);
      this.colorSetting2 = var34;
      BooleanSetting var35 = new BooleanSetting("Unsafe");
      Setting var36 = this.resolveSetting2(var35);
      this.booleanValueSetting4 = var36;
      Color var38 = new Color(255, 0, 0, 50);
      ColorSetting var37 = new ColorSetting("UnsafeBox", var38);
      Setting var39 = this.resolveSetting2(var37);
      this.colorSetting7 = var39;
      Color var41 = new Color(255, 0, 0, 255);
      ColorSetting var40 = new ColorSetting("UnsafeLine", var41);
      Setting var42 = this.resolveSetting2(var40);
      this.colorSetting6 = var42;
      Float var44 = (float)1.0F;
      Float var45 = (float)0.0F;
      Float var46 = (float)3.0F;
      NumberSetting var43 = new NumberSetting<>("UnsafeWidth", var44, var45, var46);
      Setting var47 = this.resolveSetting2(var43);
      this.floatValueSetting3 = var47;
      Float var49 = (float)1.0F;
      Float var50 = (float)-1.0F;
      Float var51 = (float)2.0F;
      NumberSetting var48 = new NumberSetting<>("UnsafeHeight", var49, var50, var51);
      Setting var52 = this.resolveSetting2(var48);
      this.floatValueSetting5 = var52;
      BooleanSetting var53 = new BooleanSetting("UnsafeGlow");
      Setting var54 = this.resolveSetting2(var53);
      this.booleanValueSetting2 = var54;
      Color var56 = new Color(255, 0, 0, 30);
      ColorSetting var55 = new ColorSetting("UnsafeGlowColor", var56);
      Setting var57 = this.resolveSetting2(var55);
      this.colorSetting = var57;
      BooleanSetting var58 = new BooleanSetting("Long");
      Setting var59 = this.resolveSetting2(var58);
      this.booleanValueSetting5 = var59;
      Color var61 = new Color(255, 0, 255, 50);
      ColorSetting var60 = new ColorSetting("LongBox", var61);
      Setting var62 = this.resolveSetting2(var60);
      this.colorSetting3 = var62;
      Color var64 = new Color(255, 0, 255, 255);
      ColorSetting var63 = new ColorSetting("LongLine", var64);
      Setting var65 = this.resolveSetting2(var63);
      this.colorSetting5 = var65;
      Float var67 = (float)1.0F;
      Float var68 = (float)0.0F;
      Float var69 = (float)3.0F;
      NumberSetting var66 = new NumberSetting<>("LongWidth", var67, var68, var69);
      Setting var70 = this.resolveSetting2(var66);
      this.floatValueSetting6 = var70;
      Float var72 = (float)1.0F;
      Float var73 = (float)-1.0F;
      Float var74 = (float)2.0F;
      NumberSetting var71 = new NumberSetting<>("LongHeight", var72, var73, var74);
      Setting var75 = this.resolveSetting2(var71);
      this.floatValueSetting4 = var75;
      BooleanSetting var76 = new BooleanSetting("LongGlow");
      Setting var77 = this.resolveSetting2(var76);
      this.booleanValueSetting6 = var77;
      Color var79 = new Color(255, 0, 255, 30);
      ColorSetting var78 = new ColorSetting("LongGlowColor", var79);
      Setting var80 = this.resolveSetting2(var78);
      this.colorSetting10 = var80;
      BooleanSetting var81 = new BooleanSetting("Wide");
      Setting var82 = this.resolveSetting2(var81);
      this.booleanValueSetting10 = var82;
      Color var84 = new Color(0, 255, 255, 50);
      ColorSetting var83 = new ColorSetting("WideBox", var84);
      Setting var85 = this.resolveSetting2(var83);
      this.colorSetting15 = var85;
      Color var87 = new Color(0, 255, 255, 255);
      ColorSetting var86 = new ColorSetting("WideLine", var87);
      Setting var88 = this.resolveSetting2(var86);
      this.colorSetting13 = var88;
      Float var90 = (float)1.0F;
      Float var91 = (float)0.0F;
      Float var92 = (float)3.0F;
      NumberSetting var89 = new NumberSetting<>("WideWidth", var90, var91, var92);
      Setting var93 = this.resolveSetting2(var89);
      this.floatValueSetting8 = var93;
      Float var95 = (float)1.0F;
      Float var96 = (float)-1.0F;
      Float var97 = (float)2.0F;
      NumberSetting var94 = new NumberSetting<>("WideHeight", var95, var96, var97);
      Setting var98 = this.resolveSetting2(var94);
      this.floatValueSetting7 = var98;
      BooleanSetting var99 = new BooleanSetting("WideGlow");
      Setting var100 = this.resolveSetting2(var99);
      this.booleanValueSetting8 = var100;
      Color var102 = new Color(0, 255, 255, 30);
      ColorSetting var101 = new ColorSetting("WideGlowColor", var102);
      Setting var103 = this.resolveSetting2(var101);
      this.colorSetting14 = var103;
      BooleanSetting var104 = new BooleanSetting("Partial");
      Setting var105 = this.resolveSetting2(var104);
      this.booleanValueSetting7 = var105;
      Color var107 = new Color(0, 255, 255, 50);
      ColorSetting var106 = new ColorSetting("PartialBox", var107);
      Setting var108 = this.resolveSetting2(var106);
      this.colorSetting9 = var108;
      Color var110 = new Color(0, 255, 255, 255);
      ColorSetting var109 = new ColorSetting("PartialLine", var110);
      Setting var111 = this.resolveSetting2(var109);
      this.colorSetting12 = var111;
      Float var113 = (float)1.0F;
      Float var114 = (float)0.0F;
      Float var115 = (float)3.0F;
      NumberSetting var112 = new NumberSetting<>("PartialWidth", var113, var114, var115);
      Setting var116 = this.resolveSetting2(var112);
      this.floatValueSetting9 = var116;
      Float var118 = (float)-1.0F;
      Float var119 = (float)0.0F;
      Float var120 = (float)2.0F;
      NumberSetting var117 = new NumberSetting<>("PartialHeight", var118, var119, var120);
      Setting var121 = this.resolveSetting2(var117);
      this.floatValueSetting10 = var121;
      BooleanSetting var122 = new BooleanSetting("PartialGlow");
      Setting var123 = this.resolveSetting2(var122);
      this.booleanValueSetting11 = var123;
      Color var125 = new Color(0, 255, 255, 30);
      ColorSetting var124 = new ColorSetting("PartialGlowColor", var125);
      Setting var126 = this.resolveSetting2(var124);
      this.colorSetting11 = var126;
      BooleanSetting var127 = new BooleanSetting("Fade");
      Setting var128 = this.resolveSetting2(var127);
      this.booleanValueSetting9 = var128;
      Double var130 = 6.0;
      Double var131 = 1.0;
      Double var132 = 64.0;
      NumberSetting var129 = new NumberSetting<>("MinRange", var130, var131, var132);
      Setting var133 = this.resolveSetting2(var129);
      this.doubleValueSetting3 = var133;
      Double var135 = 12.0;
      Double var136 = 2.0;
      Double var137 = 128.0;
      NumberSetting var134 = new NumberSetting<>("MaxRange", var135, var136, var137);
      Setting var138 = this.resolveSetting2(var134);
      this.doubleValueSetting2 = var138;
      Color var139 = new Color(0, 0, 0, 0);
      this.color = var139;
      Builder var140 = this.createBuilder();
      Setting var141 = this.booleanValueSetting3;
      Builder var142 = var140.resolveBuilder6(var141);
      SettingGroup[] var143 = new SettingGroup[1];
      Setting var144 = this.booleanValueSetting;
      Setting[] var145 = new Setting[1];
      Setting var146 = this.colorSetting2;
      var145[0] = var146;
      SettingGroup var147 = this.resolveSettingGroup(var144, var145);
      var143[0] = var147;
      Builder var148 = var142.resolveBuilder5(var143);
      Setting[] var149 = new Setting[4];
      Setting var150 = this.colorSetting8;
      var149[0] = var150;
      Setting var151 = this.colorSetting4;
      var149[1] = var151;
      Setting var152 = this.floatValueSetting;
      var149[2] = var152;
      Setting var153 = this.floatValueSetting2;
      var149[3] = var153;
      Builder var154 = var148.resolveBuilder(var149);
      var154.getSettingSubsection();
      Builder var155 = this.createBuilder();
      Setting var156 = this.booleanValueSetting4;
      Builder var157 = var155.resolveBuilder6(var156);
      SettingGroup[] var158 = new SettingGroup[1];
      Setting var159 = this.booleanValueSetting2;
      Setting[] var160 = new Setting[1];
      Setting var161 = this.colorSetting;
      var160[0] = var161;
      SettingGroup var162 = this.resolveSettingGroup(var159, var160);
      var158[0] = var162;
      Builder var163 = var157.resolveBuilder5(var158);
      Setting[] var164 = new Setting[4];
      Setting var165 = this.colorSetting7;
      var164[0] = var165;
      Setting var166 = this.colorSetting6;
      var164[1] = var166;
      Setting var167 = this.floatValueSetting3;
      var164[2] = var167;
      Setting var168 = this.floatValueSetting5;
      var164[3] = var168;
      Builder var169 = var163.resolveBuilder(var164);
      var169.getSettingSubsection();
      Builder var170 = this.createBuilder();
      Setting var171 = this.booleanValueSetting5;
      Builder var172 = var170.resolveBuilder6(var171);
      SettingGroup[] var173 = new SettingGroup[1];
      Setting var174 = this.booleanValueSetting6;
      Setting[] var175 = new Setting[1];
      Setting var176 = this.colorSetting10;
      var175[0] = var176;
      SettingGroup var177 = this.resolveSettingGroup(var174, var175);
      var173[0] = var177;
      Builder var178 = var172.resolveBuilder5(var173);
      Setting[] var179 = new Setting[4];
      Setting var180 = this.colorSetting3;
      var179[0] = var180;
      Setting var181 = this.colorSetting5;
      var179[1] = var181;
      Setting var182 = this.floatValueSetting6;
      var179[2] = var182;
      Setting var183 = this.floatValueSetting4;
      var179[3] = var183;
      Builder var184 = var178.resolveBuilder(var179);
      var184.getSettingSubsection();
      Builder var185 = this.createBuilder();
      Setting var186 = this.booleanValueSetting10;
      Builder var187 = var185.resolveBuilder6(var186);
      SettingGroup[] var188 = new SettingGroup[1];
      Setting var189 = this.booleanValueSetting8;
      Setting[] var190 = new Setting[1];
      Setting var191 = this.colorSetting14;
      var190[0] = var191;
      SettingGroup var192 = this.resolveSettingGroup(var189, var190);
      var188[0] = var192;
      Builder var193 = var187.resolveBuilder5(var188);
      Setting[] var194 = new Setting[4];
      Setting var195 = this.colorSetting15;
      var194[0] = var195;
      Setting var196 = this.colorSetting13;
      var194[1] = var196;
      Setting var197 = this.floatValueSetting8;
      var194[2] = var197;
      Setting var198 = this.floatValueSetting7;
      var194[3] = var198;
      Builder var199 = var193.resolveBuilder(var194);
      var199.getSettingSubsection();
      Builder var200 = this.createBuilder();
      Setting var201 = this.booleanValueSetting7;
      Builder var202 = var200.resolveBuilder6(var201);
      SettingGroup[] var203 = new SettingGroup[1];
      Setting var204 = this.booleanValueSetting11;
      Setting[] var205 = new Setting[1];
      Setting var206 = this.colorSetting11;
      var205[0] = var206;
      SettingGroup var207 = this.resolveSettingGroup(var204, var205);
      var203[0] = var207;
      Builder var208 = var202.resolveBuilder5(var203);
      Setting[] var209 = new Setting[4];
      Setting var210 = this.colorSetting9;
      var209[0] = var210;
      Setting var211 = this.colorSetting12;
      var209[1] = var211;
      Setting var212 = this.floatValueSetting9;
      var209[2] = var212;
      Setting var213 = this.floatValueSetting10;
      var209[3] = var213;
      Builder var214 = var208.resolveBuilder(var209);
      var214.getSettingSubsection();
      Setting var215 = this.booleanValueSetting9;
      Setting[] var216 = new Setting[2];
      Setting var217 = this.doubleValueSetting3;
      var216[0] = var217;
      Setting var218 = this.doubleValueSetting2;
      var216[1] = var218;
      this.resolveSettingGroup(var215, var216);
      HolesRenderContextListener var219 = new HolesRenderContextListener(this);
      this.handleListener(var219);
      HolesTickListener var220 = new HolesTickListener(this);
      this.handleListener(var220);
      ToDoubleFunction<Hole> var238 = HolesModule::computeHole;
      Comparator var239 = Comparator.comparingDouble(var238);
      PriorityQueue var221 = new PriorityQueue(var239);
      this.holePriorityQueue = var221;
   }

   public static double computeHole(Hole hole) {
      return -hole.computeEntity(Objects.requireNonNull(mc.player));
   }

   public Color resolveColor(double d, Color color) {
      if (!this.booleanValueSetting9.getValue()) {
         return color;
      }

      if (d > this.doubleValueSetting2.getValue()) {
         return this.color;
      }

      if (d < this.doubleValueSetting3.getValue()) {
         return color;
      }

      float f = color.getAlpha() / 255.0F;
      float f2 = (float)MathUtils.computeDouble7(d, this.doubleValueSetting3.getValue(), this.doubleValueSetting2.getValue());
      return ColorUtils.createColor6(color, f * (1.0F - f2));
   }

   public record PaintData(boolean draw, Color box, Color line, Color glowColor, float width, float height, boolean glow) {
   }
}
