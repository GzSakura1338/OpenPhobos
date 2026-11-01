package org.phobos.core.gui.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.EntryContainer;
import org.phobos.api.ScreenRenderer;
import org.phobos.api.PanelHolder;
import org.phobos.api.Named;
import org.phobos.api.Visibility;
import org.phobos.api.SettingPanelBuilder;
import org.phobos.api.GuiGraphics;
import org.phobos.api.JsonSerializable;
import org.phobos.module.Module;
import org.phobos.module.client.UIModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.gui.BaseScreen;
import org.phobos.core.module.Categories;
import org.phobos.core.module.Category;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.gui.ModuleSearch;
import org.phobos.core.gui.PanelRegistry;
import org.phobos.core.PhobosClient;
import org.phobos.core.gui.ScreenDrawContext;
import org.phobos.core.gui.ScrollState;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.SettingSection;
import org.phobos.core.setting.SettingSubsection;
import org.phobos.core.gui.element.StringElement;
import org.phobos.core.user.UserSession;

public class ClickGuiScreen extends BaseScreen<ClickGuiScreen> implements SettingPanelBuilder, JsonSerializable, PanelHolder, ScreenRenderer {
   public static final Logger LOGGER = LoggerFactory.getLogger(ClickGuiScreen.class);
   public static ClickGuiScreen clickGuiScreen = new ClickGuiScreen();
   public final GuiManager guiManager;
   public final List<GuiPanel> guiPanelList;
   public final FloatAnimator floatAnimator = new FloatAnimator(0.0F, 1.0F);
   public final GuiGraphics guiGraphics;
   public final MutableObject<StringElement> stringElementMutableObject;

   public Predicate<GuiElement> getPredicate() {
      return guiElement -> UIModule.check();
   }

   @Override
   public void handleDrawContext(DrawContext drawContext, int i, int i2, float f) {
      this.handleDrawContext2(drawContext, i, i2, f);
   }

   public void handleGuiPanel(GuiPanel guiPanel, Module module) {
      ModuleElement var3 = guiPanel.resolveModuleElement(module);
      Predicate var4 = this.resolvePredicate4(module, var3);
      var3.setGuiElementPredicate(var4);
      List var6 = module.getSettingList();
      ArrayList var5 = new ArrayList(var6);
      Optional var7 = module.resolveOptional2("Bind", KeyCombination.class);
      Object var8 = var7.orElse(null);
      boolean var9 = var8 instanceof Setting;
      if (var8 != null && !var9) {
         throw new ClassCastException("CHECKCAST failed");
      }

      if (var8 != null) {
         GuiElement var10 = guiPanel.resolveGuiElement2((Setting)var8, var3);
         Predicate var11 = this.resolvePredicate3(var3, (Named)var8);
         var10.setGuiElementPredicate(var11);
         var5.remove(var8);
      }

      Optional var12 = module.resolveOptional2("Hidden", Boolean.class);
      Object var13 = var12.orElse(null);
      boolean var14 = var13 instanceof Setting;
      if (var13 != null && !var14) {
         throw new ClassCastException("CHECKCAST failed");
      }

      if (var13 != null) {
         GuiElement var15 = guiPanel.resolveGuiElement2((Setting)var13, var3);
         Predicate var16 = this.resolvePredicate3(var3, (Named)var13);
         var15.setGuiElementPredicate(var16);
         var5.remove(var13);
      }

      List var17 = module.getSettingSectionList();
      Iterator var18 = var17.iterator();

      while (true) {
         boolean var19 = var18.hasNext();
         if (!var19) {
            List var22 = module.getSettingSubsectionList();
            Iterator var23 = var22.iterator();

            while (true) {
               boolean var24 = var23.hasNext();
               if (!var24) {
                  this.handleEntryContainer(module, guiPanel, var3, var5);
                  this.handleList(var5, guiPanel, var3);
                  return;
               }

               Object var25 = var23.next();
               boolean var26 = var25 instanceof SettingSubsection;
               if (var25 != null && !var26) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               String var37 = ((SettingSubsection)var25).getName();
               Setting var38 = ((SettingSubsection)var25).getBooleanValueSetting();
               ModuleElement var39 = guiPanel.resolveModuleElement2(var37, var3, var38);
               Predicate var40 = this.resolvePredicate((SettingSubsection)var25, var3);
               var39.setGuiElementPredicate(var40);
               Setting var41 = ((SettingSubsection)var25).getBooleanValueSetting();
               if (var41 != null) {
                  var5.remove(var41);
               }

               this.handleEntryContainer((EntryContainer)var25, guiPanel, var39, var5);
               List var42 = ((SettingSubsection)var25).getSettingList();
               this.handleList(var42, guiPanel, var39);
               List var43 = ((SettingSubsection)var25).getSettingList();
               var5.removeAll(var43);
            }
         }

         Object var20 = var18.next();
         boolean var21 = var20 instanceof SettingSection;
         if (var20 != null && !var21) {
            throw new ClassCastException("CHECKCAST failed");
         }

         String var27 = ((SettingSection)var20).getName();
         Setting var28 = ((SettingSection)var20).getBooleanValueSetting();
         ModuleElement var29 = guiPanel.resolveModuleElement2(var27, var3, var28);
         Predicate var30 = this.resolvePredicate3(var3, (Named)var20);
         var29.setGuiElementPredicate(var30);
         Setting var31 = ((SettingSection)var20).getBooleanValueSetting();
         if (var31 != null) {
            var5.remove(var31);
         }

         List var32 = ((SettingSection)var20).getSettingSubsectionList();
         Iterator var33 = var32.iterator();

         while (true) {
            boolean var34 = var33.hasNext();
            if (!var34) {
               this.handleEntryContainer((EntryContainer)var20, guiPanel, var29, var5);
               List var44 = ((SettingSection)var20).getSettingList();
               this.handleList(var44, guiPanel, var29);
               List var45 = ((SettingSection)var20).getSettingList();
               var5.removeAll(var45);
               break;
            }

            Object var35 = var33.next();
            boolean var36 = var35 instanceof SettingSubsection;
            if (var35 != null && !var36) {
               throw new ClassCastException("CHECKCAST failed");
            }

            String var46 = ((SettingSubsection)var35).getName();
            Setting var47 = ((SettingSubsection)var35).getBooleanValueSetting();
            ModuleElement var48 = guiPanel.resolveModuleElement2(var46, var29, var47);
            Predicate var49 = this.resolvePredicate((SettingSubsection)var35, var29);
            var48.setGuiElementPredicate(var49);
            Setting var50 = ((SettingSubsection)var35).getBooleanValueSetting();
            if (var50 != null) {
               var5.remove(var50);
            }

            this.handleEntryContainer((EntryContainer)var35, guiPanel, var48, var5);
            List var51 = ((SettingSubsection)var35).getSettingList();
            this.handleList(var51, guiPanel, var48);
            List var52 = ((SettingSubsection)var35).getSettingList();
            var5.removeAll(var52);
         }
      }
   }

   public void run() {
      GuiManager var1 = this.guiManager;
      var1.clear();
      List var2 = this.guiPanelList;
      var2.clear();
      MinecraftClient var3 = MinecraftClient.getInstance();
      Window var4 = var3.getWindow();
      int var5 = var4.getScaledWidth();
      PhobosClient var6 = ClientHolder.getPhobosClient();
      ModuleManager var7 = var6.getModuleManager();
      Map var8 = var7.getMap();
      Set var9 = var8.entrySet();
      Iterator var10 = var9.iterator();
      long var11 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var5)) / 2.0F)) - 344.0F)
         & 4294967295L;

      while (true) {
         long var13 = var11;
         long var15 = var13;
         long var17 = var15;
         long var19 = var17;
         long var21 = var19;
         boolean var23 = var10.hasNext();
         long var24 = var21;
         long var26 = var24;
         long var28 = var26;
         if (!var23) {
            return;
         }

         long var30 = var28;
         long var32 = var30;
         long var34 = var32;
         long var36 = var34;
         long var38 = var36;
         long var40 = var38;
         long var42 = var40;
         Object var44 = var10.next();
         long var45 = var42;
         long var47 = var45;
         long var49 = var47;
         boolean var51 = var44 instanceof Entry;
         if (var44 != null && !var51) {
            throw new ClassCastException("CHECKCAST failed");
         }

         long var54 = var49;
         long var58 = var54;
         long var60 = var58;
         long var62 = var60;
         long var64 = var62;
         long var66 = var64;
         GuiManager var68 = this.guiManager;
         long var69 = var66;
         long var71 = var69;
         long var73 = var71;
         long var75 = var73;
         long var77 = var75;
         long var79 = var77;
         long var81 = var79;
         long var83 = var81;
         Object var85 = ((Entry)var44).getKey();
         long var86 = var83;
         long var88 = var86;
         long var90 = var88;
         boolean var92 = var85 instanceof Category;
         if (var85 != null && !var92) {
            throw new ClassCastException("CHECKCAST failed");
         }

         long var95 = var90;
         long var99 = var95;
         long var101 = var99;
         long var103 = var101;
         long var105 = var103;
         long var107 = var105;
         long var109 = var107;
         String var111 = ((Category)var85).getName();
         long var112 = var109;
         long var114 = var112;
         long var116 = var114;
         long var118 = var116;
         long var120 = var118;
         long var122 = var120;
         long var124 = var122;
         GuiPanel var126 = var68.resolveGuiPanel(var111);
         long var127 = var124;
         long var129 = var127;
         long var131 = var129;
         long var133 = var131;
         long var135 = var133;
         long var137 = var135;
         List var139 = this.guiPanelList;
         long var140 = var137;
         long var142 = var140;
         long var144 = var142;
         long var146 = var144;
         long var148 = var146;
         long var150 = var148;
         long var152 = var150;
         long var154 = var152;
         var139.add(var126);
         long var156 = var154;
         long var158 = var156;
         long var160 = var158;
         long var162 = var160;
         long var164 = var162;
         long var166 = var164;
         long var168 = var166;
         long var170 = var168;
         long var172 = var170;
         long var174 = var172;
         long var176 = var174;
         var126.setFloatValue4((float)15.0F);
         long var178 = var176;
         long var180 = var178;
         long var182 = var180;
         long var184 = (int)var182 & 4294967295L;
         long var186 = var184;
         long var188 = var186;
         long var190 = var188;
         long var192 = var190;
         long var194 = var192;
         long var196 = var194;
         long var198 = var196;
         long var200 = var198;
         long var202 = var200;
         var126.setFloatValue2(
            (float)Float.intBitsToFloat(
               (int)((int)(Float.floatToRawIntBits(Float.intBitsToFloat((int)((int)var182 & 4294967295L)) - 2.0F) & 4294967295L) & 4294967295L)
            )
         );
         long var204 = var202;
         long var206 = var204;
         long var208 = var206;
         long var210 = var208;
         long var212 = var210;
         long var214 = var212;
         long var216 = var214;
         long var218 = var216;
         long var220 = var218;
         var126.setFloatValue((float)30.0F);
         long var222 = var220;
         long var224 = var222;
         long var226 = var224;
         long var228 = var226;
         long var230 = var228;
         long var232 = var230;
         long var234 = var232;
         FloatAnimator var236 = var126.getFloatAnimator4();
         long var237 = var234;
         long var239 = var237;
         long var241 = var239;
         long var243 = var241;
         long var245 = var243;
         long var247 = var245;
         int var249 = this.height;
         long var250 = var247;
         long var252 = var250;
         long var254 = var252;
         long var256 = var254;
         long var258 = var256;
         long var260 = var258;
         long var262 = var260;
         long var264 = var262;
         var236.setFloatValue2((float)Float.intBitsToFloat(Float.floatToRawIntBits(var249)));
         long var266 = var264;
         long var268 = var266;
         long var270 = var268;
         long var272 = var270;
         long var274 = var272;
         long var276 = var274;
         long var278 = var276;
         FloatAnimator var280 = var126.getFloatAnimator4();
         long var281 = var278;
         long var283 = var281;
         long var285 = var283;
         long var287 = var285;
         long var289 = var287;
         long var291 = var289;
         int var293 = this.height;
         long var294 = var291;
         long var296 = var294;
         long var298 = var296;
         long var300 = var298;
         long var302 = var300;
         long var304 = var302;
         long var306 = var304;
         long var308 = var306;
         var280.setFloatValue((float)Float.intBitsToFloat(Float.floatToRawIntBits(var293)));
         long var310 = var308;
         long var312 = var310;
         long var314 = var312;
         long var316 = var314;
         long var318 = var316;
         long var320 = var318;
         long var322 = var320;
         Object var324 = ((Entry)var44).getValue();
         long var325 = var322;
         long var327 = var325;
         long var329 = var327;
         boolean var331 = var324 instanceof List;
         if (var324 != null && !var331) {
            throw new ClassCastException("CHECKCAST failed");
         }

         long var334 = var329;
         long var338 = var334;
         long var340 = var338;
         long var342 = var340;
         long var344 = var342;
         long var346 = var344;
         long var348 = var346;
         Iterator var350 = ((List)var324).iterator();
         long var351 = var348;
         long var353 = var351;
         long var355 = var353;
         long var357 = var355;

         while (true) {
            long var359 = var357;
            long var361 = var359;
            long var363 = var361;
            long var365 = var363;
            long var367 = var365;
            long var369 = var367;
            boolean var371 = var350.hasNext();
            long var372 = var369;
            long var374 = var372;
            long var376 = var374;
            if (!var371) {
               long var380 = var376;
               long var406 = var380;
               long var408 = var406;
               long var410 = var408;
               long var412 = var410;
               long var414 = var412;
               long var416 = var414;
               Object var418 = ((Entry)var44).getKey();
               long var419 = var416;
               long var421 = var419;
               long var423 = var421;
               boolean var425 = var418 instanceof Category;
               if (var418 != null && !var425) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               long var428 = var423;
               long var460 = var428;
               long var462 = var460;
               long var464 = var462;
               Category var466 = Categories.client;
               long var467 = var464;
               long var469 = var467;
               long var471 = var469;
               long var473 = var471;
               long var475 = var473;
               long var477 = var475;
               boolean var479 = var418.equals(var466);
               long var480 = var477;
               long var482 = var480;
               long var484 = var482;
               long var488;
               if (var479) {
                  long var486 = var484;
                  long var490 = var486;
                  long var492 = var490;
                  long var494 = var492;
                  Setting var496 = ModuleSearch.getSearch();
                  long var497 = var494;
                  long var499 = var497;
                  long var501 = var499;
                  long var503 = var501;
                  long var505 = var503;
                  GuiElement var507 = var126.resolveGuiElement(var496);
                  long var508 = var505;
                  long var510 = var508;
                  long var512 = var510;
                  long var514 = var512;
                  long var516 = var514;
                  Predicate var518 = this.getPredicate();
                  long var519 = var516;
                  long var521 = var519;
                  long var523 = var521;
                  long var525 = var523;
                  long var527 = var525;
                  var507.setGuiElementPredicate(var518);
                  long var529 = var527;
                  boolean var531 = var507 instanceof StringElement;
                  long var576;
                  if ((int)((!var531 ? 0 : 1) & 0xFF & 0xFF) != 0) {
                     long var532 = var529;
                     boolean var571 = var507 instanceof StringElement;
                     if (var507 != null && !var571) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var574 = var532;
                     long var583 = var574;
                     MutableObject var585 = this.stringElementMutableObject;
                     long var586 = var583;
                     long var588 = var586;
                     long var590 = var588;
                     long var592 = var590;
                     var585.setValue(var507);
                     var576 = var592;
                  } else {
                     long var534 = var529;
                     var576 = var534;
                  }

                  ScrollState var578;
                  boolean var594 = true;
                  long var595;
                  if (!var594) {
                     long var579 = var576;
                     var595 = var579;
                  } else {
                     var595 = var576;
                  }

                  long var597 = var595;
                  long var599 = var597;
                  long var601 = var599;
                  long var603 = var601;
                  var578 = new ScrollState();
                  long var605 = var603;
                  long var607 = var605;
                  long var609 = var607;
                  long var611 = var609;
                  long var613 = var611;
                  var578.setFlag2(true & true);
                  long var615 = var613;
                  long var617 = var615;
                  long var619 = var617;
                  long var621 = var619;
                  long var623 = var621;
                  Consumer var625 = this.getConsumer();
                  long var626 = var623;
                  long var628 = var626;
                  long var630 = var628;
                  long var632 = var630;
                  long var634 = var632;
                  var578.setBooleanValueConsumer(var625);
                  long var636 = var634;
                  long var638 = var636;
                  long var640 = var638;
                  long var642 = var640;
                  long var644 = var642;
                  long var646 = var644;
                  long var648 = var646;
                  long var650 = var648;
                  var578.setText("Legacy features");
                  long var652 = var650;
                  long var654 = var652;
                  long var656 = var654;
                  long var658 = var656;
                  long var660 = var658;
                  long var662 = var660;
                  long var664 = var662;
                  long var666 = var664;
                  long var668 = var666;
                  var126.handleScrollState(var578);
                  long var670 = var668;
                  ScrollState var672;
                  boolean var675 = true;
                  long var676;
                  if (!var675) {
                     long var673 = var670;
                     var676 = var673;
                  } else {
                     var676 = var670;
                  }

                  long var678 = var676;
                  long var680 = var678;
                  long var682 = var680;
                  long var684 = var682;
                  long var686 = var684;
                  long var688 = var686;
                  long var690 = var688;
                  long var692 = var690;
                  var672 = new ScrollState();
                  long var694 = var692;
                  long var696 = var694;
                  long var698 = var696;
                  long var700 = var698;
                  long var702 = var700;
                  long var704 = var702;
                  long var706 = var704;
                  long var708 = var706;
                  long var710 = var708;
                  Consumer var712 = this.getConsumer3();
                  long var713 = var710;
                  long var715 = var713;
                  long var717 = var715;
                  long var719 = var717;
                  long var721 = var719;
                  long var723 = var721;
                  long var725 = var723;
                  var672.setBooleanValueConsumer(var712);
                  long var727 = var725;
                  long var729 = var727;
                  long var731 = var729;
                  long var733 = var731;
                  long var735 = var733;
                  long var737 = var735;
                  var672.setText("Show all");
                  long var739 = var737;
                  long var741 = var739;
                  long var743 = var741;
                  long var745 = var743;
                  long var747 = var745;
                  var126.handleScrollState(var672);
                  long var749 = var747;
                  ScrollState var751;
                  boolean var754 = true;
                  long var755;
                  if (!var754) {
                     long var752 = var749;
                     var755 = var752;
                  } else {
                     var755 = var749;
                  }

                  long var757 = var755;
                  long var759 = var757;
                  long var761 = var759;
                  long var763 = var761;
                  var751 = new ScrollState();
                  long var765 = var763;
                  long var767 = var765;
                  long var769 = var767;
                  long var771 = var769;
                  long var773 = var771;
                  Consumer var775 = this.getConsumer2();
                  long var776 = var773;
                  long var778 = var776;
                  long var780 = var778;
                  long var782 = var780;
                  long var784 = var782;
                  var751.setBooleanValueConsumer(var775);
                  long var786 = var784;
                  long var788 = var786;
                  boolean var790 = UserSession.flag2;
                  long var791 = var788;
                  long var793 = var791;
                  long var795 = var793;
                  long var797 = var795;
                  var751.setFlag(var790);
                  long var799 = var797;
                  long var801 = var799;
                  boolean var803 = UserSession.flag3;
                  long var804 = var801;
                  long var806 = var804;
                  long var808 = var806;
                  long var810 = var808;
                  var751.setFlag2(var803);
                  long var812 = var810;
                  long var814 = var812;
                  long var816 = var814;
                  long var818 = var816;
                  long var820 = var818;
                  long var822 = var820;
                  var751.setText("Beta features");
                  long var824 = var822;
                  long var826 = var824;
                  long var828 = var826;
                  long var830 = var828;
                  long var832 = var830;
                  var126.handleScrollState(var751);
                  long var834 = var832;
                  var488 = var834;
               } else {
                  var488 = var484;
               }

               long var536 = (int)var488 & 4294967295L;
               long var538 = var536;
               long var540 = var538;
               long var542 = var540;
               long var544 = var542;
               long var546 = var544;
               long var548 = var546;
               long var550 = var548;
               float var552 = var126.getFloatValue3();
               long var553 = var550;
               long var555 = var553;
               long var557 = var555;
               long var559 = var557;
               long var561 = var559;
               long var563 = var561;
               long var565 = var563;
               long var567 = Float.floatToRawIntBits(
                     Float.intBitsToFloat((int)var565)
                        + Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var552)) + 8.0F))
                  )
                  & 4294967295L;
               long var569 = var567;
               var11 = (int)var569 & 4294967295L;
               break;
            }

            long var378 = var376;
            long var382 = var378;
            long var384 = var382;
            long var386 = var384;
            long var388 = var386;
            long var390 = var388;
            long var392 = var390;
            Object var394 = var350.next();
            long var395 = var392;
            long var397 = var395;
            long var399 = var397;
            boolean var401 = var394 instanceof Module;
            if (var394 != null && !var401) {
               throw new ClassCastException("CHECKCAST failed");
            }

            long var404 = var399;
            long var432 = var404;
            long var434 = var432;
            long var436 = var434;
            long var438 = var436;
            long var440 = var438;
            long var442 = var440;
            long var444 = var442;
            long var446 = var444;
            long var448 = var446;
            long var450 = var448;
            long var452 = var450;
            long var454 = var452;
            this.handleGuiPanel(var126, (Module)var394);
            long var456 = var454;
            var357 = var456;
         }
      }
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (GuiPanel guiPanel : this.guiPanelList) {
         JsonElement jsonElement2 = jsonObject.get(guiPanel.getName());
         if (jsonElement2 != null) {
            guiPanel.handleJsonElement(jsonElement2);
         }
      }
   }

   public Predicate<GuiElement> resolvePredicate(SettingSubsection settingSubsection, ModuleElement moduleElement) {
      return guiElement -> settingSubsection.getVisibility().isFlag8() && moduleElement.getModuleElement().isFlag8();
   }

   public Visibility resolveVisibility(Module module, ModuleElement moduleElement) {
      return () -> ModuleSearch.testModule(module, moduleElement) && ModuleSearch.testText(module.getName());
   }

   public void handleModule(Module module) {
      for (GuiPanel guiPanel : this.guiPanelList) {
         guiPanel.handleModule(module);
      }
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.guiPanelList.forEach(guiPanel -> guiPanel.handleDouble3(mouseX, mouseY, button));
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void init() {
      this.floatAnimator.setFloatValue2(0.0F);
      if (MenuNavigation.baseScreen == this) {
         for (GuiPanel guiPanel : this.guiPanelList) {
            guiPanel.close();
         }
      }

      MenuNavigation.baseScreen = this;

      for (GuiPanel guiPanel2 : this.guiPanelList) {
         guiPanel2.run5();
      }

      PanelRegistry.getINSTANCE().run();
   }

   @Override
   public boolean shouldPause() {
      return false;
   }

   public void run2() {
      float f = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0F - 344.0F;

      for (GuiPanel guiPanel : this.guiPanelList) {
         guiPanel.handleFloat2(f - 2.0F, 30.0F);
         f += guiPanel.getFloatValue3() + 8.0F;
      }
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   @Override
   public boolean alwaysFalse() {
      for (GuiPanel guiPanel : this.guiPanelList) {
         if (!guiPanel.check2()) {
            return true;
         }
      }

      return false;
   }

   public Predicate<GuiElement> resolvePredicate2(SettingSubsection settingSubsection, Visibility visibility) {
      return guiElement -> settingSubsection.getVisibility().isFlag8() && visibility.isFlag8();
   }

   public Consumer<Boolean> getConsumer() {
      return booleanValue -> {
         List<Module> list = ModuleGroups.getINSTANCE().resolveList("legacy");
         if (!booleanValue) {
            list.forEach(module -> {
               if (module.check7()) {
                  module.run11();
               }

               module.setFlag4(true);
               module.setFlag14(true);
            });
         } else {
            list.forEach(module -> {
               if (module.isFlag14()) {
                  module.setFlag4(false);
                  module.setFlag14(false);
               }
            });
         }
      };
   }

   public Predicate<GuiElement> resolvePredicate3(ModuleElement moduleElement, Named named) {
      return guiElement -> moduleElement.getModuleElement().isFlag8();
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (GuiPanel guiPanel : this.guiPanelList) {
         jsonObject.add(guiPanel.getName(), guiPanel.createJsonPrimitive());
      }

      return jsonObject;
   }

   public Predicate<GuiElement> resolvePredicate4(Module module, ModuleElement moduleElement) {
      return guiElement -> ModuleSearch.testModule(module, moduleElement);
   }

   public ClickGuiScreen() {
      super(Text.literal(""));
      this.guiPanelList = new CopyOnWriteArrayList<>();
      this.stringElementMutableObject = new MutableObject<>();
      this.guiGraphics = new ScreenDrawContext();
      this.guiManager = new GuiManager(this.guiGraphics);
      clickGuiScreen = this;
      this.run();
      PanelRegistry.getINSTANCE().handlePanelHolder(this);
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for (GuiPanel guiPanel : this.guiPanelList) {
         if (this.testGuiPanel(guiPanel, mouseX, mouseY)) {
            if (verticalAmount > 0.0 && guiPanel.getFloatAnimator9().getFloatValue() < 0.0F) {
               guiPanel.getFloatAnimator9().setFloatValue(Math.min(guiPanel.getFloatAnimator9().getFloatValue() + 15.0F, 0.0F));
            }

            if (verticalAmount < 0.0) {
               if (guiPanel.check()) {
                  float f = guiPanel.getFloatValue6();
                  float f2 = this.height - (guiPanel.getFloatValue() + guiPanel.getFloatValue4());
                  if (f > f2 && guiPanel.getFloatAnimator9().getFloatValue() + f > f2 - 26.0F) {
                     guiPanel.getFloatAnimator9().handleFloat2(-15.0F);
                  }
               } else {
                  float f3 = guiPanel.getFloatValue33() + guiPanel.getFloatAnimator9().getFloatValue();
                  float f4 = guiPanel.getFloatValue6();
                  if (f4 < f3) {
                     guiPanel.getFloatAnimator9().handleFloat2(-15.0F);
                  }
               }
            }
         }
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2, float f) {
      this.guiGraphics.setFlag(false);
      this.guiGraphics.setDrawContext(drawContext);
      this.guiGraphics.setIntValue2(0);
      this.start();
      this.guiManager.handleDouble2(i, i2);
      this.run4();
      if (!this.guiGraphics.isFlag5()) {
         CursorUtils.run2();
      }
   }

   public Consumer<Boolean> getConsumer2() {
      return booleanValue -> {
         List<Module> list = ModuleGroups.getINSTANCE().resolveList("beta");
         if (!booleanValue) {
            list.forEach(module -> {
               if (module.check7()) {
                  module.run11();
               }

               module.setFlag4(true);
               module.setFlag14(true);
            });
         } else {
            list.forEach(module -> {
               if (module.isFlag14()) {
                  module.setFlag4(false);
                  module.setFlag14(false);
               }
            });
         }
      };
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (hasControlDown() && keyCode == 70 && UIModule.check() && this.stringElementMutableObject.getValue() != null) {
         StringElement stringElement = this.stringElementMutableObject.getValue();
         if (!stringElement.isFlag10()) {
            stringElement.setFlag10(true);
         }

         if (!stringElement.isFlag()) {
            stringElement.run9();
         }
      }

      this.guiPanelList.forEach(guiPanel -> guiPanel.handleInt2(keyCode, scanCode, modifiers));
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public ClickGuiScreen getScriptScreen() {
      return this;
   }

   public Consumer<Boolean> getConsumer3() {
      return booleanValue -> ModuleGroups.getINSTANCE().setFlag(booleanValue);
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      this.guiPanelList.forEach(guiPanel -> guiPanel.handleChar2(chr, modifiers));
      return super.charTyped(chr, modifiers);
   }

   @Override
   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.guiPanelList.forEach(guiPanel -> guiPanel.handleDouble8(mouseX, mouseY, button));
      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean testGuiPanel(GuiPanel guiPanel, double d, double d2) {
      float f = guiPanel.getFloatValue2();
      float f2 = guiPanel.getFloatValue();
      float f3 = f + guiPanel.getFloatValue3();
      float f4 = f2 + guiPanel.getFloatValue6();
      return d >= f && d <= f3 && d2 >= f2 && d2 <= f4;
   }

   @Override
   public List<GuiPanel> getGuiPanelList() {
      return this.guiPanelList;
   }

   public MutableObject<StringElement> getStringElementMutableObject() {
      return this.stringElementMutableObject;
   }

   @Override
   public void close() {
      super.close();
      CursorUtils.run2();

      for (GuiPanel guiPanel : this.guiPanelList) {
         guiPanel.close();
      }
   }

   public GuiManager getGuiManager() {
      return this.guiManager;
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }
}
