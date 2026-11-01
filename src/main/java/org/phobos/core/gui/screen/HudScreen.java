package org.phobos.core.gui.screen;

import lombok.SneakyThrows;
import java.awt.Color;
import java.awt.Rectangle;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.phobos.api.EntryContainer;
import org.phobos.api.PanelHolder;
import org.phobos.api.SettingPanelBuilder;
import org.phobos.api.HudRenderable;
import org.phobos.api.DragCapture;
import org.phobos.api.GuiGraphics;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.HudAnchors;
import org.phobos.core.gui.CursorUtils.Style;
import org.phobos.core.gui.BaseScreen;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.hud.HudElement;
import org.phobos.core.hud.HudElementEntry;
import org.phobos.core.hud.HudElementEntry2;
import org.phobos.core.hud.HudManager;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.PanelRegistry;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.gui.ScreenDrawContext;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.SettingSection;
import org.phobos.core.setting.SettingSubsection;

public class HudScreen extends BaseScreen<HudScreen> implements MinecraftHolder, SettingPanelBuilder, PanelHolder {
   public final Set<HudElementEntry> hudElementEntrySet;
   public final List<HudElement> hudElementList = new CopyOnWriteArrayList<>();
   public final Set<HudElementEntry2> hudElementEntry2Set;
   public final HudGuiManager hudGuiManager;
   public static HudScreen hudScreen;
   public final HudPanel hudPanel;
   public DragCapture dragCapture;
   public final GuiGraphics guiGraphics;
   public final Color color = new Color(255, 255, 255, 50);
   private static Object[] phobos$g1806c40b0;
   private static Object[] phobos$g1806c4188;
   private static Object[] phobos$g1806c45d8;
   private static Object[] phobos$g1806c4850;

   public HudScreen(HudManager hudManager) {
      super(Text.literal(""));
      this.hudElementEntry2Set = new HashSet<>();
      this.hudElementEntrySet = new HashSet<>();
      this.guiGraphics = new ScreenDrawContext();
      this.hudGuiManager = new HudGuiManager(this.guiGraphics);
      this.hudPanel = this.hudGuiManager.resolveGuiPanel("HUD");
      this.handleHudManager(hudManager);
      PanelRegistry.getINSTANCE().handlePanelHolder(this);
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      this.hudPanel.handleInt2(keyCode, scanCode, modifiers);
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean shouldPause() {
      return false;
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.hudPanel.handleDouble3(mouseX, mouseY, button);
      return super.mouseClicked(mouseX, mouseY, button);
   }

   public HudScreen getScriptScreen() {
      return this;
   }

   public void handleHudElement(HudElement hudElement, double d) {
      this.hudElementEntrySet.add(new HudElementEntry(hudElement, d));
   }

   @Override
   public void init() {
      MenuNavigation.baseScreen = this;
      Rectangle rectangle = new Rectangle(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
      Rectangle rectangle2 = new Rectangle(
         (int)this.hudPanel.getFloatValue2(),
         (int)this.hudPanel.getFloatValue(),
         (int)this.hudPanel.getFloatValue3(),
         (int)this.hudPanel.getFloatValue4()
      );
      if (!rectangle.contains(rectangle2)) {
         this.hudPanel.setFloatValue2(2.0F);
         this.hudPanel.setFloatValue(2.0F);
      }

      this.hudPanel.run5();
   }

   @Override
   public boolean alwaysFalse() {
      return !this.hudPanel.check2();
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   public void handleHudElement2(HudElement hudElement) {
      this.hudElementList.remove(hudElement);
      this.hudPanel
         .getGuiElementList2()
         .removeIf(
            guiElement -> guiElement instanceof ModuleElement moduleElement
               && moduleElement.getHudElement() != null
               && moduleElement.getHudElement().equals(hudElement)
         );
   }

   @Override
   public void handleDrawContext(DrawContext drawContext, int i, int i2, float f) {
      this.guiGraphics.setFlag(false);
      this.guiGraphics.setDrawContext(drawContext);
      RenderUtils.handleMatrixStack16(drawContext.getMatrices(), this.color, this.width / 2.0 - 0.5, 0.0, this.height, 0.5F);
      RenderUtils.handleMatrixStack19(drawContext.getMatrices(), this.color, 0.0, this.height / 2.0 - 0.5, this.width, 0.5F);
      this.handleSet(this.hudElementEntry2Set, drawContext.getMatrices(), this.width, this.height);
      this.handleSet(this.hudElementEntrySet, drawContext.getMatrices(), this.width, this.height);
      boolean flag = this.guiGraphics.testInt(0);
      List<HudAnchors> list = ClientHolder.getPhobosClient().getHudManager().getHudLayoutManager().getHudAnchorsList();
      this.start();
      this.hudGuiManager.handleDouble2(i, i2);

      for (HudElement hudElement : this.hudElementList) {
         if (!hudElement.isFlag3()) {
            if (flag) {
               if ((this.dragCapture == null || this.dragCapture == hudElement) && this.hudGuiManager.getDragCapture() == null) {
                  hudElement.handleHudScreen3(this, i, i2, this.hudElementList, list);
               }
            } else {
               if (hudElement.isFlag72()) {
                  hudElement.handleHudScreen(this, i, i2, this.hudElementList, list);
               }

               this.dragCapture = null;
            }

            if (!this.guiGraphics.isFlag5() && hudElement.testDouble(i, i2)) {
               CursorUtils.handleStyle(this.dragCapture != null ? Style.Link : Style.Move);
               this.guiGraphics.setFlag(true);
            }
         }
      }

      this.run4();
      if (!this.guiGraphics.isFlag5()) {
         CursorUtils.run2();
      }
   }

   public static void handleConsumer(Consumer<HudScreen> consumer) {
      hudScreen.hudPanel.setFloatValue2(hudScreen.hudPanel.computeDouble2(hudScreen.hudPanel.getFloatValue2()));
      hudScreen.hudPanel.setFloatValue(hudScreen.hudPanel.computeDouble(hudScreen.hudPanel.getFloatValue()));
      consumer.accept(hudScreen);
   }

   @Override
   public List<GuiPanel> getGuiPanelList() {
      return new ArrayList<>(List.of(this.hudPanel));
   }

   public <G extends HudRenderable> void handleSet(Set<G> set, MatrixStack matrixStack, float f, float f2) {
      if (!set.isEmpty()) {
         set.removeIf(hudRenderablex -> hudRenderablex.getFloatAnimator().getFloatValue2() <= 0.05F);

         for (G hudRenderable : set) {
            hudRenderable.handleMatrixStack(matrixStack, f, f2);
         }
      }
   }

   public void run2() {
      this.hudPanel.run2();
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (!this.testGuiPanel(this.hudPanel, mouseX, mouseY)) {
         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      }

      if (verticalAmount > 0.0 && this.hudPanel.getFloatAnimator9().getFloatValue() < 0.0F) {
         this.hudPanel.getFloatAnimator9().setFloatValue(Math.min(this.hudPanel.getFloatAnimator9().getFloatValue() + 15.0F, 0.0F));
      }

      if (verticalAmount < 0.0) {
         if (this.hudPanel.check()) {
            float f = this.hudPanel.getFloatValue6();
            float f2 = this.height - (this.hudPanel.getFloatValue() + this.hudPanel.getFloatValue4());
            if (f > f2 && this.hudPanel.getFloatAnimator9().getFloatValue() + f > f2 - 26.0F) {
               this.hudPanel.getFloatAnimator9().handleFloat2(-15.0F);
            }
         } else {
            float f3 = this.hudPanel.getFloatValue33() + this.hudPanel.getFloatAnimator9().getFloatValue();
            float f4 = this.hudPanel.getFloatValue6();
            if (f4 < f3) {
               this.hudPanel.getFloatAnimator9().handleFloat2(-15.0F);
            }
         }
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @SneakyThrows public void handleHudManager(HudManager hudManager) {
      CallSite var120 = null;
      CallSite var90 = null;
      Supplier<Float> var18 = HudScreen::getFloatValue;
      Supplier<Float> var35 = HudScreen::getFloatValue2;
      HudPanel var36 = this.hudPanel;
      var36.setFloatValue4((float)15.0F);
      HudPanel var37 = this.hudPanel;
      Object var38 = var18.get();
      boolean var39 = var38 instanceof Float;
      if (var38 != null && !var39) {
         throw new ClassCastException("CHECKCAST failed");
      }

      float var40 = (Float)var38;
      var37.setFloatValue2((float)Float.intBitsToFloat(Float.floatToRawIntBits(var40)));
      HudPanel var41 = this.hudPanel;
      var41.setFloatValueSupplier(var18);
      HudPanel var42 = this.hudPanel;
      Object var43 = var35.get();
      boolean var44 = var43 instanceof Float;
      if (var43 != null && !var44) {
         throw new ClassCastException("CHECKCAST failed");
      }

      float var45 = (Float)var43;
      var42.setFloatValue((float)Float.intBitsToFloat(Float.floatToRawIntBits(var45)));
      HudPanel var46 = this.hudPanel;
      var46.setFloatValueSupplier2(var35);
      HudPanel var47 = this.hudPanel;
      FloatAnimator var48 = var47.getFloatAnimator4();
      int var49 = this.height;
      var48.setFloatValue2((float)Float.intBitsToFloat(Float.floatToRawIntBits(var49)));
      HudPanel var50 = this.hudPanel;
      FloatAnimator var51 = var50.getFloatAnimator4();
      int var52 = this.height;
      var51.setFloatValue((float)Float.intBitsToFloat(Float.floatToRawIntBits(var52)));
      List var53 = hudManager.getColorPickerList();
      Iterator var54 = var53.iterator();

      label112:
      while (true) {
         boolean var55 = var54.hasNext();
         if (!var55) {
            hudScreen = this;
            return;
         }

         Object var56 = var54.next();
         boolean var57 = var56 instanceof HudElement;
         if (var56 != null && !var57) {
            throw new ClassCastException("CHECKCAST failed");
         }

         List var58 = this.hudElementList;
         var58.add(var56);
         HudPanel var59 = this.hudPanel;
         ModuleElement var60 = var59.resolveModuleElement4((HudElement)var56);
         ArrayList var61;
         boolean var62 = true;
         if (!var62) {
         }

         List var63 = ((HudElement)var56).getSettingList();
         var61 = new ArrayList(var63);
         List var64 = ((HudElement)var56).getSettingSectionList();
         Iterator var65 = var64.iterator();

         while (true) {
            boolean var66 = var65.hasNext();
            if (!var66) {
               List var69 = ((HudElement)var56).getSettingSubsectionList();
               Iterator var70 = var69.iterator();

               while (true) {
                  boolean var71 = var70.hasNext();
                  if (!var71) {
                     HudPanel var84 = this.hudPanel;
                     this.handleEntryContainer((EntryContainer)var56, var84, var60, var61);
                     HudPanel var85 = this.hudPanel;
                     this.handleList(var61, var85, var60);
                     continue label112;
                  }

                  Object var72 = var70.next();
                  boolean var73 = var72 instanceof SettingSubsection;
                  if (var72 != null && !var73) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  HudPanel var86 = this.hudPanel;
                  String var87 = ((SettingSubsection)var72).getName();
                  Setting var88 = ((SettingSubsection)var72).getBooleanValueSetting();
                  ModuleElement var89 = var86.resolveModuleElement2(var87, var60, var88);
                  if (var90 == null) {
                     Lookup var91 = MethodHandles.lookup();
                     Class<?> var92 = this.getClass();
                     ClassLoader var93 = var92.getClassLoader();
                     MethodType var94 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/setting/SettingSubsection;)Ljava/util/function/Predicate;", var93);
                     Class<?> var95 = this.getClass();
                     ClassLoader var96 = var95.getClassLoader();
                     MethodType var97 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Z", var96);
                     Class<?> var98 = this.getClass();
                     ClassLoader var99 = var98.getClassLoader();
                     MethodType var100 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/setting/SettingSubsection;Lorg/phobos/core/gui/GuiElement;)Z", var99);
                     MethodHandle var101 = var91.findStatic(HudScreen.class, "testSettingSubsection2", var100);
                     Class<?> var102 = this.getClass();
                     ClassLoader var103 = var102.getClassLoader();
                     MethodType var104 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/gui/GuiElement;)Z", var103);
                     Object[] var143 = new Object[0];
                     var90 = LambdaMetafactory.metafactory(var91, "test", var94, var97, var101, var104);
                  }

                  MethodHandle var105 = var90.getTarget();
                  if (var72 != null) {
                     Object[] var144 = phobos$g1806c4850;
                     if (phobos$g1806c4850 == null) {
                        var144 = phobos$g1806c4850 = new Object[1];
                     }

                     var144[0] = var72;
                  }

                  Object[] var145 = phobos$g1806c4850;
                  if (phobos$g1806c4850 == null) {
                     var145 = phobos$g1806c4850 = new Object[1];
                  }

                  Object var106 = var105.invokeWithArguments(var145);
                  var89.setGuiElementPredicate((Predicate<GuiElement>)var106);
                  Setting var107 = ((SettingSubsection)var72).getBooleanValueSetting();
                  if (var107 != null) {
                     var61.remove(var107);
                  }

                  HudPanel var108 = this.hudPanel;
                  this.handleEntryContainer((EntryContainer)var72, var108, var89, var61);
                  List var109 = ((SettingSubsection)var72).getSettingList();
                  HudPanel var110 = this.hudPanel;
                  this.handleList(var109, var110, var89);
                  List var111 = ((SettingSubsection)var72).getSettingList();
                  var61.removeAll(var111);
               }
            }

            Object var67 = var65.next();
            boolean var68 = var67 instanceof SettingSection;
            if (var67 != null && !var68) {
               throw new ClassCastException("CHECKCAST failed");
            }

            HudPanel var74 = this.hudPanel;
            String var75 = ((SettingSection)var67).getName();
            Setting var76 = ((SettingSection)var67).getBooleanValueSetting();
            ModuleElement var77 = var74.resolveModuleElement2(var75, var60, var76);
            Setting var78 = ((SettingSection)var67).getBooleanValueSetting();
            if (var78 != null) {
               var61.remove(var78);
            }

            List var79 = ((SettingSection)var67).getSettingSubsectionList();
            Iterator var80 = var79.iterator();

            while (true) {
               boolean var81 = var80.hasNext();
               if (!var81) {
                  HudPanel var112 = this.hudPanel;
                  this.handleEntryContainer((EntryContainer)var67, var112, var77, var61);
                  List var113 = ((SettingSection)var67).getSettingList();
                  HudPanel var114 = this.hudPanel;
                  this.handleList(var113, var114, var77);
                  List var115 = ((SettingSection)var67).getSettingList();
                  var61.removeAll(var115);
                  break;
               }

               Object var82 = var80.next();
               boolean var83 = var82 instanceof SettingSubsection;
               if (var82 != null && !var83) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               HudPanel var116 = this.hudPanel;
               String var117 = ((SettingSubsection)var82).getName();
               Setting var118 = ((SettingSubsection)var82).getBooleanValueSetting();
               ModuleElement var119 = var116.resolveModuleElement2(var117, var77, var118);
               if (var120 == null) {
                  Lookup var121 = MethodHandles.lookup();
                  Class<?> var122 = this.getClass();
                  ClassLoader var123 = var122.getClassLoader();
                  MethodType var124 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/setting/SettingSubsection;)Ljava/util/function/Predicate;", var123);
                  Class<?> var125 = this.getClass();
                  ClassLoader var126 = var125.getClassLoader();
                  MethodType var127 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Z", var126);
                  Class<?> var128 = this.getClass();
                  ClassLoader var129 = var128.getClassLoader();
                  MethodType var130 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/setting/SettingSubsection;Lorg/phobos/core/gui/GuiElement;)Z", var129);
                  MethodHandle var131 = var121.findStatic(HudScreen.class, "testSettingSubsection", var130);
                  Class<?> var132 = this.getClass();
                  ClassLoader var133 = var132.getClassLoader();
                  MethodType var134 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/gui/GuiElement;)Z", var133);
                  Object[] var10000 = new Object[0];
                  var120 = LambdaMetafactory.metafactory(var121, "test", var124, var127, var131, var134);
               }

               MethodHandle var135 = var120.getTarget();
               if (var82 != null) {
                  Object[] var142 = phobos$g1806c45d8;
                  if (phobos$g1806c45d8 == null) {
                     var142 = phobos$g1806c45d8 = new Object[1];
                  }

                  var142[0] = var82;
               }

               Object[] var10001 = phobos$g1806c45d8;
               if (phobos$g1806c45d8 == null) {
                  var10001 = phobos$g1806c45d8 = new Object[1];
               }

               Object var136 = var135.invokeWithArguments(var10001);
               var119.setGuiElementPredicate((Predicate<GuiElement>)var136);
               Setting var137 = ((SettingSubsection)var82).getBooleanValueSetting();
               if (var137 != null) {
                  var61.remove(var137);
               }

               HudPanel var138 = this.hudPanel;
               this.handleEntryContainer((EntryContainer)var82, var138, var119, var61);
               List var139 = ((SettingSubsection)var82).getSettingList();
               HudPanel var140 = this.hudPanel;
               this.handleList(var139, var140, var119);
               List var141 = ((SettingSubsection)var82).getSettingList();
               var61.removeAll(var141);
            }
         }
      }
   }

   public boolean check() {
      return this.dragCapture == null;
   }

   public void handleHudElement3(HudElement hudElement, double d) {
      this.hudElementEntry2Set.add(new HudElementEntry2(hudElement, d));
   }

   public DragCapture getDragCapture() {
      return this.dragCapture;
   }

   public Set<HudElementEntry> getHudElementEntrySet() {
      return this.hudElementEntrySet;
   }

   public Color getColor() {
      return this.color;
   }

   public static Float getFloatValue() {
      return (float)(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0 - 54.0 - 2.0);
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   public static boolean testSettingSubsection(SettingSubsection settingSubsection, GuiElement guiElement) {
      return settingSubsection.getVisibility().isFlag8();
   }

   public void handleFloat2(float f, float f2) {
      this.hudPanel.handleFloat(f, f2);
   }

   public Set<HudElementEntry2> getHudElementEntry2Set() {
      return this.hudElementEntry2Set;
   }

   public boolean testGuiPanel(GuiPanel guiPanel, double d, double d2) {
      float f = guiPanel.getFloatValue2();
      float f2 = guiPanel.getFloatValue();
      float f3 = f + guiPanel.getFloatValue3();
      float f4 = f2 + guiPanel.getFloatValue6();
      return d >= f && d <= f3 && d2 >= f2 && d2 <= f4;
   }

   public float[] values() {
      return new float[]{this.hudPanel.getFloatValue12(), this.hudPanel.getFloatValue10()};
   }

   public HudPanel getHudPanel() {
      return this.hudPanel;
   }

   public List<HudElement> getColorPickerList() {
      return this.hudElementList;
   }

   public static boolean testSettingSubsection2(SettingSubsection settingSubsection, GuiElement guiElement) {
      return settingSubsection.getVisibility().isFlag8();
   }

   @Override
   public void close() {
      super.close();
      CursorUtils.run2();
      this.hudPanel.close();
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      this.hudPanel.handleChar2(chr, modifiers);
      return super.charTyped(chr, modifiers);
   }

   public HudGuiManager getHudGuiManager() {
      return this.hudGuiManager;
   }

   public static Float getFloatValue2() {
      return 54.0F;
   }
}
