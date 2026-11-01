package org.phobos.core.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.ElementCreator;
import org.phobos.api.Named;
import org.phobos.api.ListenerHolder;
import org.phobos.api.PanelRenderer;
import org.phobos.api.DragCapture;
import org.phobos.core.gui.CursorUtils.Style;
import org.phobos.event.Listener;
import org.phobos.module.client.ColorsModule;
import org.phobos.module.Module;
import org.phobos.module.client.UIModule;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.util.JsonUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosLog;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;

public class GuiPanel extends GuiElement implements Named, PanelRenderer, ListenerHolder, DragCapture {
   public final FloatAnimator floatAnimator3;
   public final List<Listener<?>> listenerList = new ArrayList<>();
   public final FloatAnimator floatAnimator4;
   public final String name;
   public final FloatAnimator floatAnimator5;
   public float floatValue;
   public final List<ModuleElement> moduleElementList = new CopyOnWriteArrayList<>();
   public boolean flag2;
   public int intValue;
   public final FloatAnimator floatAnimator6;
   public float floatValue2;
   public float floatValue3;
   public final FloatAnimator floatAnimator7;
   public final FloatAnimator floatAnimator9;
   public Supplier<Float> floatValueSupplier;
   public final List<GuiElement> guiElementList;
   public boolean flag3;
   public boolean flag4;
   public final List<ScrollState> scrollStateList;
   public boolean flag5;
   public float floatValue4;
   public float floatValue13;
   public boolean flag10;
   public boolean flag11;
   public boolean flag12;
   public final List<GuiElement> guiElementList2 = new CopyOnWriteArrayList<>();
   public Supplier<Float> floatValueSupplier2;
   public final FloatAnimator floatAnimator10;
   public float floatValue14;
   private static final java.util.Set<String> drawFailures = java.util.concurrent.ConcurrentHashMap.newKeySet();

   private static void reportDrawFailure(Throwable throwable) {
      StackTraceElement[] trace = throwable.getStackTrace();
      String key = throwable.getClass().getName() + (trace.length > 0 ? trace[0] : "");
      if (drawFailures.add(key)) {
         throwable.printStackTrace();
      }
   }

   public GuiPanel(GuiManager guiManager, String text, float f, float f2, float f3, float f4) {
      super(guiManager, null, f, f2, f3, f4, null);
      this.guiElementList = new CopyOnWriteArrayList<>();
      this.scrollStateList = new ArrayList<>();
      this.floatAnimator9 = new FloatAnimator();
      this.floatAnimator4 = new FloatAnimator();
      this.floatAnimator10 = new FloatAnimator();
      this.floatAnimator7 = new FloatAnimator();
      this.floatAnimator6 = new FloatAnimator();
      this.floatAnimator5 = new FloatAnimator();
      this.floatAnimator3 = new FloatAnimator();
      this.flag4 = true;
      this.flag10 = false;
      this.flag11 = false;
      this.flag2 = false;
      this.flag12 = false;
      this.floatValue4 = Float.NaN;
      this.name = text;
      this.handleFloat3(f, f2);
   }

   public void handleDouble5(double d, double d2) {
      this.floatAnimator6.setFloatValue(this.computeDouble2(d - this.floatValue14));
      this.floatAnimator5.setFloatValue(this.computeDouble(d2 - this.floatValue2));
   }

   public boolean check() {
      return Float.isNaN(this.floatValue4);
   }

   public float computeDouble(double d) {
      return (float)MathUtils.computeDouble5(d, 2.0, mc.getWindow().getScaledHeight() - 2 - this.getFloatValue4());
   }

   public void handleModule(Module module) {
      this.moduleElementList.removeIf(moduleElement -> moduleElement.getModule() != null && moduleElement.getModule().equals(module));
   }

   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      this.setFloatValue2(JsonUtils.resolveFloatValue(jsonObject.get("X"), this.getFloatValue12()));
      this.setFloatValue(JsonUtils.resolveFloatValue(jsonObject.get("Y"), this.getFloatValue10()));
      this.setFlag10(JsonUtils.resolveBooleanValue(jsonObject.get("Open"), this.isFlag10()));
      this.handleFloat3(this.floatValue12, this.floatValue10);
   }

   public float computeDouble2(double d) {
      return (float)MathUtils.computeDouble5(d, 2.0, mc.getWindow().getScaledWidth() - 2 - this.getFloatValue3());
   }

   public void run6() {
      float f = UIModule.getUIModule().limit.getValue();
      if (f <= 10.0F) {
         this.floatValue4 = Float.NaN;
      } else {
         this.floatValue4 = f;
      }
   }

   @Override
   public void close() {
      this.getFloatAnimator8().setFloatValue(0.0F);
      this.handleFlag(true);

      for (GuiElement guiElement : this.guiElementList) {
         guiElement.close();
      }
   }

   public void handleFloat(float f, float f2) {
      this.setFloatValue2(f);
      this.setFloatValue(f2);
      this.floatAnimator6.setFloatValue2(f);
      this.floatAnimator5.setFloatValue2(f2);
      this.floatAnimator6.setFloatValue(f);
      this.floatAnimator5.setFloatValue(f2);
   }

   @Override
   public void handleDouble4(double d, double d2) {
      if (!this.flag11) {
         if (this.getGuiGraphics().testInt(1)) {
            this.flag5 = this.isFlag7();
         } else {
            if (this.flag5 && this.isFlag7()) {
               this.setFlag10(!this.isFlag10());
               this.run14();
            }

            this.flag5 = false;
         }
      }

      if (!this.flag10) {
         if (this.getGuiGraphics().testInt(0)) {
            if (this.getGuiManager().getDragCapture() == null || this.getGuiManager().getDragCapture() == this) {
               if (!this.flag3 && this.isFlag7()) {
                  this.floatValue14 = (float)(d - this.getFloatValue12());
                  this.floatValue2 = (float)(d2 - this.getFloatValue10());
                  this.flag3 = true;
                  this.getGuiManager().setDragCapture(this);
               }

               if (this.flag3) {
                  this.handleDouble5(d, d2);
               }
            }
         } else if (this.flag3) {
            this.handleDouble5(d, d2);
            this.flag3 = false;
            if (this.getGuiManager().getDragCapture() == this) {
               this.getGuiManager().setDragCapture(null);
            }
         }
      }
   }

   public void handleFloat2(float f, float f2) {
      this.floatAnimator6.setFloatValue(f);
      this.floatAnimator5.setFloatValue(f2);
   }

   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("X", this.getFloatValue12());
      jsonObject.addProperty("Y", this.getFloatValue10());
      jsonObject.addProperty("Open", this.isFlag10());
      return jsonObject;
   }

   public void handleGuiElement2(GuiElement guiElement) {
      this.guiElementList2.add(guiElement);
      this.guiElementList.add(guiElement);
   }

   public GuiPanel(GuiManager guiManager, String text) {
      super(guiManager, null, 0.0F, 0.0F, 108.0F, 15.0F, null);
      this.guiElementList = new CopyOnWriteArrayList<>();
      this.scrollStateList = new ArrayList<>();
      this.floatAnimator9 = new FloatAnimator();
      this.floatAnimator4 = new FloatAnimator();
      this.floatAnimator10 = new FloatAnimator();
      this.floatAnimator7 = new FloatAnimator();
      this.floatAnimator6 = new FloatAnimator();
      this.floatAnimator5 = new FloatAnimator();
      this.floatAnimator3 = new FloatAnimator();
      this.flag4 = true;
      this.flag10 = false;
      this.flag11 = false;
      this.flag2 = false;
      this.flag12 = false;
      this.floatValue4 = Float.NaN;
      this.name = text;
   }

   public void run2() {
      this.setFloatValue2(this.floatValueSupplier.get());
      this.setFloatValue(this.floatValueSupplier2.get());
   }

   public void handleFloat3(float f, float f2) {
      this.floatAnimator6.setFloatValue(f);
      this.floatAnimator5.setFloatValue(f2);
      this.floatAnimator6.setFloatValue2(f);
      this.floatAnimator5.setFloatValue2(f2);
   }

   @Override
   public void run5() {
      super.run5();
      UIModule var1 = UIModule.getUIModule();
      Setting var2 = var1.lock;
      Object var3 = var2.getValue();
      boolean var4 = var3 instanceof Boolean;
      if (var3 != null && !var4) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var5 = (Boolean)var3;
      this.setFlag102(var5);
      this.handleFlag(false & true);
      FloatAnimator var6 = this.floatAnimator4;
      var6.setFloatValue2((float)0.0F);
      FloatAnimator var7 = this.floatAnimator4;
      var7.setFloatValue((float)0.0F);
      List var8 = this.guiElementList;
      Iterator var9 = var8.iterator();

      while (true) {
         boolean var10 = var9.hasNext();
         if (!var10) {
            return;
         }

         Object var11 = var9.next();
         boolean var12 = var11 instanceof GuiElement;
         if (var11 != null && !var12) {
            throw new ClassCastException("CHECKCAST failed");
         }

         ((GuiElement)var11).run5();
      }
   }

   public void handleScrollState(ScrollState scrollState) {
      this.scrollStateList.add(scrollState);
   }

   @Override
   public void handleDouble2(double d, double d2) {
      this.run6();
      this.floatAnimator10.handleFloat3(10.0F, 0.1F);
      this.floatAnimator7.handleFloat3(10.0F, 0.1F);
      this.floatAnimator6.handleFloat3(10.0F, 0.25F);
      this.floatAnimator5.handleFloat3(10.0F, 0.25F);
      this.setFloatValue7(this.floatAnimator10.getFloatValue2());
      this.setFloatValue8(this.floatAnimator7.getFloatValue2());
      this.setFloatValue2(this.floatAnimator6.getFloatValue2());
      this.setFloatValue(this.floatAnimator5.getFloatValue2());
      int i = mc.getWindow().getScaledHeight();
      this.floatValue3 = this.value2();
      float f8 = this.check() ? this.floatValue3 : MathUtils.computeFloat3(this.floatValue3, 0.0F, this.floatValue4);
      this.floatAnimator4.setFloatValue(this.flag4 ? 1.0F : 0.0F);
      this.floatAnimator4.handleFloat3(10.0F, 0.06F);
      float f9 = this.floatAnimator4.getFloatValue2() > 0.9998F ? 1.0F : this.floatAnimator4.getFloatValue2();
      this.floatValue13 = f8 * f9;
      if (this.check()) {
         if (this.floatAnimator9.getFloatValue() + this.getFloatValue6() <= i - (this.getFloatValue() + this.getFloatValue4() + 10.0F)
            && this.floatAnimator9.getFloatValue() < 0.0F) {
            this.floatAnimator9.setFloatValue(Math.min(0.0F, this.floatAnimator9.getFloatValue() + 4.0F));
         }
      } else {
         float f = this.floatValue3 + this.floatAnimator9.getFloatValue();
         float f2 = this.floatValue13;
         if (f2 >= f && this.floatAnimator9.getFloatValue() < 0.0F) {
            this.floatAnimator9.setFloatValue(Math.min(0.0F, this.floatAnimator9.getFloatValue() + (f2 - f)));
         }
      }

      if (this.floatAnimator9.getFloatValue() > 0.0F) {
         this.floatAnimator9.setFloatValue(0.0F);
      }

      if (!this.flag12 && this.floatAnimator4.getFloatValue2() > 5.0E-6F) {
         float f7 = 4.0F + (this.check() ? this.floatValue13 + this.floatAnimator9.getFloatValue2() : Math.min(this.floatValue13, this.floatValue4 * f9));
         this.handleGuiPanel(this, d, d2, this.getFloatValue() + this.getFloatValue4() + this.floatAnimator9.getFloatValue2() - 11.0F, 2.0F);
         if (this.flag11) {
            this.handleDouble11(
               0.0,
               0.0,
               this.getFloatValue3(),
               this.getFloatValue4() + f7 - 1.0F,
               this.getGuiManager().getUiTheme().getColor(),
               this.getGuiManager().getUiTheme().getUIShader6()
            );
            this.handleText2(
               "Temporary unavailable",
               2.0F,
               this.getFloatValue4() + 6.0F,
               ColorUtils.resolveColor(this.getGuiManager().getUiTheme().getColor10(), 0.4F),
               true
            );
         } else {
            if (!this.scrollStateList.isEmpty()) {
               float f5 = this.scrollStateList.size() * 12.0F;
               float f6 = this.getFloatValue2() + this.getFloatValue3();
               this.floatAnimator3.setFloatValue(!this.testDouble2(d, d2, this.getFloatValue2(), this.getFloatValue(), 300.0, f8) ? 80.0F : 0.0F);
               this.floatAnimator3.handleFloat3(10.0F, 0.1F);
               if (this.floatAnimator3.getFloatValue2() <= 79.0F) {
                  float f3 = f6 + 4.0F + -this.floatAnimator3.getFloatValue2();
                  float f4 = this.getFloatValue() + 4.0F;
                  this.run10();
                  this.handleDouble(f6 + 2.0F, this.getFloatValue() + -2.0F, 100.0, f5 + 2.0F);
                  this.run13();

                  for (ScrollState scrollState : this.scrollStateList) {
                     scrollState.getFloatAnimator3().setFloatValue(scrollState.testDouble(d, d2) ? 1.0F : 0.0F);
                     scrollState.getFloatAnimator3().handleFloat3(10.0F, 0.1F);
                     scrollState.handleRenderManager(
                        ClientHolder.getPhobosClient().getRenderManager(),
                        f3,
                        f4,
                        ColorsModule.getColorsModule().color2.getValue(),
                        (text, floatValue, floatValue2) -> {
                           this.run10();
                           this.handleText4(text, floatValue, floatValue2, Color.WHITE, 0.6F, true);
                           this.run13();
                        }
                     );
                     f4 += 12.0F;
                  }

                  this.run3();
               }
            }

            if (UIModule.getUIModule().outline.getValue()) {
               RenderUtils.handleMatrixStack14(
                  this.getMatrixStack(),
                  this.getGuiManager().getUiTheme().getColor11(),
                  this.getFloatValue2(),
                  this.getFloatValue() + this.getFloatValue4(),
                  this.getFloatValue3(),
                  f7 - 1.0F
               );
            }

            boolean flag = d >= this.getFloatValue2()
               && d <= this.getFloatValue2() + this.getFloatValue3()
               && d2 >= this.getFloatValue() + this.getFloatValue4()
               && d2 <= this.getFloatValue() + this.getFloatValue4() + this.floatValue13;
            this.floatValue = this.floatValue13;
            this.handleDouble11(
               0.0,
               0.0,
               this.getFloatValue3(),
               this.getFloatValue4() + f7 - 1.0F,
               this.getGuiManager().getUiTheme().getColor(),
               this.getGuiManager().getUiTheme().getUIShader6()
            );
            this.handleDouble(0.0, this.getFloatValue4() + 2.0F, this.getFloatValue3(), f7 - 5.0F);

            for (ModuleElement moduleElement : this.moduleElementList) {
               if (!moduleElement.isFlag4() && moduleElement.isFlag8()) {
                  if (!this.getGuiManager().testGuiElement(moduleElement)) {
                     moduleElement.run11();
                  } else {
                     moduleElement.handleDouble7(d, d2, flag);
                     this.getGuiManager().getDrawStats().run2();

                     try {
                        moduleElement.handleDouble2(d, d2);
                     } catch (Throwable throwable) {
                        reportDrawFailure(throwable);
                        PhobosLog.handleText("UI/%s".formatted(this.getName()), "Failed to draw highlighted element");
                        this.setFlag9(true);
                        this.handleDouble12(
                           moduleElement.getFloatValue2(),
                           moduleElement.getFloatValue(),
                           moduleElement.getFloatValue2() + moduleElement.getFloatValue3(),
                           moduleElement.getFloatValue() + moduleElement.getFloatValue4(),
                           Color.RED
                        );
                        this.setFlag9(false);
                     }

                     this.getGuiManager().getDrawStats().run();
                     this.getGuiManager().getDrawStats().run5();
                  }
               }
            }

            for (GuiElement guiElement : this.guiElementList2) {
               if (guiElement.isFlag8()) {
                  if (!this.getGuiManager().testGuiElement(guiElement)) {
                     guiElement.run11();
                  } else {
                     guiElement.handleDouble7(d, d2, flag);
                     this.getGuiManager().getDrawStats().run2();

                     try {
                        guiElement.handleDouble2(d, d2);
                     } catch (Throwable throwable2) {
                        reportDrawFailure(throwable2);
                        PhobosLog.handleText("UI/%s".formatted(this.getName()), "Failed to draw highlighted element");
                        this.setFlag9(true);
                        this.handleDouble12(
                           guiElement.getFloatValue2(),
                           guiElement.getFloatValue(),
                           guiElement.getFloatValue2() + guiElement.getFloatValue3(),
                           guiElement.getFloatValue() + guiElement.getFloatValue4(),
                           Color.RED
                        );
                        this.setFlag9(false);
                     }

                     this.getGuiManager().getDrawStats().run();
                     this.getGuiManager().getDrawStats().run5();
                  }
               }
            }

            this.run3();
         }
      }

      this.handleDouble11(
         0.0,
         0.0,
         this.getFloatValue3(),
         this.getFloatValue4(),
         this.flag11
            ? ColorUtils.resolveColor(this.getGuiManager().getUiTheme().getColor15(), 0.4F)
            : this.getGuiManager().getUiTheme().getColor15(),
         this.getGuiManager().getUiTheme().getUIShader7()
      );
      this.handleText4(
         this.getName(),
         4.0F,
         (int)(3.5 * UIModule.getUIModule().height.getValue().floatValue()),
         this.flag11
            ? ColorUtils.resolveColor(this.getGuiManager().getUiTheme().getColor10(), 0.4F)
            : this.getGuiManager().getUiTheme().getColor10(),
         1.25F,
         true
      );
   }

   public void handleFlag(boolean flag) {
      if (UIModule.getUIModule().animate.getValue()) {
         float f = mc.getWindow().getScaledWidth() / 2.0F;
         float f2 = mc.getWindow().getScaledHeight() / 2.0F;
         float f3 = this.floatValue12 + this.floatValue6 / 2.0F;
         float f4 = this.floatValue10 + this.floatValue11 / 2.0F;
         float f5 = f3 - f;
         float f6 = f4 - f2;
         float f7 = (float)Math.atan2(f6, f5);
         float f8 = (float)Math.cos(f7);
         float f9 = (float)Math.sin(f7);
         float f10 = (float)Math.sqrt(f5 * f5 + f6 * f6);
         float f11 = flag ? 0.15F : 0.3F;
         float f12 = f8 * f10 * f11;
         float f13 = f9 * f10 * f11;
         if (flag) {
            this.floatAnimator10.setFloatValue(f12);
            this.floatAnimator7.setFloatValue(f13);
         } else {
            this.floatAnimator10.setFloatValue2(f12);
            this.floatAnimator7.setFloatValue2(f13);
            this.floatAnimator10.setFloatValue(0.0F);
            this.floatAnimator7.setFloatValue(0.0F);
         }
      }
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      this.getStream().forEach(guiElement -> guiElement.handleInt2(i, i2, i3));
   }

   @Override
   public String getName() {
      return this.name;
   }

   public void run() {
   }

   public Stream<ModuleElement> stream() {
      return this.moduleElementList.stream();
   }

   public <M extends Module> ModuleElement resolveModuleElement(M module) {
      GuiManager var3 = this.getGuiManager();
      float var4 = this.getFloatValue2();
      int var5 = this.getIntValue();
      float var6 = this.getFloatValue3();
      ModuleElement var2 = new ModuleElement(
         var3,
         this,
         module,
         (float)Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var4)) + 2.0F)),
         (float)Float.intBitsToFloat(Float.floatToRawIntBits(var5 * 14 + 14)),
         (float)Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var6)) - 4.0F)),
         (float)15.0F
      );
      return this.resolveModuleElement3(var2);
   }

   public String describeModuleElement(ModuleElement moduleElement, Setting<?> setting) {
      return moduleElement != null
            && !(moduleElement instanceof ModuleElement moduleElement2 && moduleElement2.getModule() != null)
         ? setting.getName().replaceAll(moduleElement.describe(), "")
         : setting.getName();
   }

   public int getIntValue() {
      return this.intValue;
   }

   public boolean testDouble4(double d, double d2) {
      double d3 = this.getFloatValue2();
      double d4 = this.getFloatValue();
      double d5 = d3 + this.getFloatValue3();
      double d6 = d4 + this.getFloatValue4() + this.floatValue13 - 1.0;
      return d >= d3 && d <= d5 && d2 >= d4 && d2 <= d6;
   }

   public <T, S extends Setting<T> & ElementCreator> GuiElement resolveGuiElement(S setting) {
      GuiManager var2 = this.getGuiManager();
      String var3 = setting.getName();
      GuiElement var4 = setting.createEnumElement(var2, this, var3, (ModuleElement)null);
      float var5 = this.getFloatValue2();
      var4.setFloatValue2((float)Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var5)) + 2.0F)));
      int var6 = this.getIntValue();
      var4.setFloatValue((float)Float.intBitsToFloat(Float.floatToRawIntBits(var6 * 14 + 14)));
      float var7 = this.getFloatValue3();
      var4.setFloatValue3((float)Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var7)) - 4.0F)));
      var4.setFloatValue4((float)15.0F);
      float var8 = var4.getFloatValue3();
      var4.setFloatValue5((float)Float.intBitsToFloat(Float.floatToRawIntBits(var8)));
      float var9 = var4.getFloatValue4();
      var4.setFloatValue9((float)Float.intBitsToFloat(Float.floatToRawIntBits(var9)));
      this.handleGuiElement2(var4);
      return var4;
   }

   public float getFloatValue6() {
      return this.floatValue13;
   }

   public ModuleElement resolveModuleElement2(String text, ModuleElement moduleElement, Setting<Boolean> setting) {
      GuiElement var4 = moduleElement.getGuiElement();
      boolean var5;
      if (var4 == null) {
         var5 = false;
      } else {
         var5 = true;
      }

      byte var8;
      label60: {
         if (var5) {
            GuiElement var6 = moduleElement.getGuiElement();
            GuiElement var7 = var6.getGuiElement();
            if (var7 != null) {
               var8 = 1;
               break label60;
            }
         }

         var8 = 0;
      }

      byte var12;
      label55: {
         if ((int)(var8 & 4294967295L) != 0) {
            GuiElement var9 = moduleElement.getGuiElement();
            GuiElement var10 = var9.getGuiElement();
            GuiElement var11 = var10.getGuiElement();
            if (var11 != null) {
               var12 = 1;
               break label55;
            }
         }

         var12 = 0;
      }

      GuiManager var14;
      float var15;
      int var17;
      label50: {
         var14 = this.getGuiManager();
         var15 = this.getFloatValue2();
         int var16;
         if ((int)(var8 & 4294967295L) == 0) {
            if (var5) {
               var17 = 1086324736;
               break label50;
            }

            var16 = 1082130432;
         } else {
            var16 = 1090519040;
         }

         var17 = var16;
      }

      int var38;
      float var77;
      long var106;
      label44: {
         long var21 = var17 & 4294967295L;
         long var24 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var26 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var28 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var30 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var32 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var34 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var36 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         var38 = this.getIntValue();
         long var39 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var41 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var43 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var45 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var47 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var49 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var51 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var53 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var55 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var57 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var59 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var61 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var63 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var65 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var67 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var69 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var71 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var73 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var75 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         var77 = this.getFloatValue3();
         long var78 = Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var80 = Float.floatToRawIntBits(var77) & 4294967295L
            | (
                  Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
                     | (
                           Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                                 & 4294967295L
                              | var17 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var82 = Float.floatToRawIntBits(var77) & 4294967295L
            | (
                  Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
                     | (
                           Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                                 & 4294967295L
                              | var17 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var84 = 1073741824L
            | (
                  Float.floatToRawIntBits(var77) & 4294967295L
                     | (
                           Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
                              | (
                                    Float.floatToRawIntBits(
                                             Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))
                                          )
                                          & 4294967295L
                                       | var17 & -4294967296L
                                 )
                                 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var86 = 1073741824L
            | (
                  Float.floatToRawIntBits(var77) & 4294967295L
                     | (
                           Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
                              | (
                                    Float.floatToRawIntBits(
                                             Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))
                                          )
                                          & 4294967295L
                                       | var17 & -4294967296L
                                 )
                                 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var88 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var77)) - 2.0F) & 4294967295L
            | (
                  1073741824L
                     | (
                           Float.floatToRawIntBits(var77) & 4294967295L
                              | (
                                    Float.floatToRawIntBits(var38 * 14 + 14) & 4294967295L
                                       | (
                                             Float.floatToRawIntBits(
                                                      Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))
                                                   )
                                                   & 4294967295L
                                                | var17 & -4294967296L
                                          )
                                          & -4294967296L
                                 )
                                 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var104;
         if ((int)(var12 & 4294967295L) == 0) {
            if ((int)(var8 & 4294967295L) == 0) {
               if (!var5) {
                  var106 = 1086324736L | var88 & -4294967296L;
                  break label44;
               }

               var104 = 1092616192L | var88 & -4294967296L;
            } else {
               var104 = 1096810496L | var88 & -4294967296L;
            }
         } else {
            var104 = 1099956224L | var88 & -4294967296L;
         }

         var106 = var104;
      }

      ModuleElement var13 = new ModuleElement(
         var14,
         this,
         text,
         moduleElement,
         setting,
         (float)Float.intBitsToFloat(
            (int)(
               Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))) & 4294967295L
            )
         ),
         (float)Float.intBitsToFloat(Float.floatToRawIntBits(var38 * 14 + 14)),
         (float)Float.intBitsToFloat(
            Float.floatToRawIntBits(
               Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var77)) - 2.0F)) - Float.intBitsToFloat((int)var106)
            )
         ),
         (float)15.0F
      );
      GuiElement var108 = this.resolveGuiElement3(var13);
      boolean var109 = var108 instanceof ModuleElement;
      if (var108 != null && !var109) {
         throw new ClassCastException("CHECKCAST failed");
      } else {
         return (ModuleElement)var108;
      }
   }

   @Override
   public Style getStyle() {
      return UIModule.check2() && this.getGuiGraphics().testInt(0) ? Style.Move : Style.Default;
   }

   @Override
   public void handleDouble8(double d, double d2, int i) {
      this.getStream().forEach(guiElement -> guiElement.handleDouble8(d, d2, i));
   }

   public float value2() {
      if (this.flag11) {
         return this.getFloatValue4() + 2.0F;
      }

      float f = 0.0F;

      for (ModuleElement moduleElement : this.moduleElementList) {
         if (!moduleElement.isFlag4() && moduleElement.isFlag8()) {
            f += moduleElement.getFloatValue33() + 2.0F;
         }
      }

      for (GuiElement guiElement : this.guiElementList2) {
         if (guiElement.isFlag8()) {
            if (guiElement instanceof InteractiveElement interactiveElement) {
               f += interactiveElement.getFloatValue33() + 2.0F;
            } else {
               f += guiElement.getFloatValue4() + 2.0F;
            }
         }
      }

      return f;
   }

   @Override
   public void handleDouble3(double d, double d2, int i) {
      this.getStream().forEach(guiElement -> guiElement.handleDouble3(d, d2, i));
      if (i == 0 && !this.scrollStateList.isEmpty()) {
         for (ScrollState scrollState : this.scrollStateList) {
            if (scrollState.testDouble(d, d2)) {
               scrollState.run();
            }
         }
      }
   }

   public Stream<GuiElement> getStream() {
      return this.guiElementList.stream().filter(GuiElement::isFlag8);
   }

   public <T, S extends Setting<T> & ElementCreator> GuiElement resolveGuiElement2(S setting, @Nullable ModuleElement moduleElement) {
      byte var4;
      label63: {
         if (moduleElement != null) {
            GuiElement var3 = moduleElement.getGuiElement();
            if (var3 != null) {
               var4 = 1;
               break label63;
            }
         }

         var4 = 0;
      }

      byte var7;
      label58: {
         if ((int)(var4 & 4294967295L) != 0) {
            GuiElement var5 = moduleElement.getGuiElement();
            GuiElement var6 = var5.getGuiElement();
            if (var6 != null) {
               var7 = 1;
               break label58;
            }
         }

         var7 = 0;
      }

      byte var11;
      label53: {
         if ((int)(var7 & 4294967295L) != 0) {
            GuiElement var8 = moduleElement.getGuiElement();
            GuiElement var9 = var8.getGuiElement();
            GuiElement var10 = var9.getGuiElement();
            if (var10 != null) {
               var11 = 1;
               break label53;
            }
         }

         var11 = 0;
      }

      GuiElement var14;
      float var15;
      int var17;
      label48: {
         GuiManager var12 = this.getGuiManager();
         String var13 = this.describeModuleElement(moduleElement, setting);
         var14 = setting.createEnumElement(var12, this, var13, moduleElement);
         setting.setGuiElement(var14);
         var15 = this.getFloatValue2();
         int var16;
         if ((int)(var7 & 4294967295L) == 0) {
            if ((int)(var4 & 4294967295L) != 0) {
               var17 = 1086324736;
               break label48;
            }

            var16 = 1082130432;
         } else {
            var16 = 1090519040;
         }

         var17 = var16;
      }

      float var103;
      long var132;
      label42: {
         long var21 = var17 & 4294967295L;
         long var24 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var26 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var28 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var30 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var32 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         var14.setFloatValue2(
            (float)Float.intBitsToFloat(
               (int)(
                  (int)(
                        Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                           & 4294967295L
                     )
                     & 4294967295L
               )
            )
         );
         long var34 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var36 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var38 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var40 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var42 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var44 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var46 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var48 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         int var50 = this.getIntValue();
         long var51 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var53 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var55 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var57 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var59 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var61 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var63 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var65 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var67 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var69 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var71 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
               & 4294967295L
            | var17 & -4294967296L;
         long var73 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var75 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var77 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var79 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var81 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var83 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         var14.setFloatValue((float)Float.intBitsToFloat(Float.floatToRawIntBits(var50 * 14 + 14)));
         long var85 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var87 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var89 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var91 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var93 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var95 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var97 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var99 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var101 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         var103 = this.getFloatValue3();
         long var104 = Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
            | (
                  Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                        & 4294967295L
                     | var17 & -4294967296L
               )
               & -4294967296L;
         long var106 = Float.floatToRawIntBits(var103) & 4294967295L
            | (
                  Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
                     | (
                           Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                                 & 4294967295L
                              | var17 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var108 = Float.floatToRawIntBits(var103) & 4294967295L
            | (
                  Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
                     | (
                           Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L)))
                                 & 4294967295L
                              | var17 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var110 = 1073741824L
            | (
                  Float.floatToRawIntBits(var103) & 4294967295L
                     | (
                           Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
                              | (
                                    Float.floatToRawIntBits(
                                             Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))
                                          )
                                          & 4294967295L
                                       | var17 & -4294967296L
                                 )
                                 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var112 = 1073741824L
            | (
                  Float.floatToRawIntBits(var103) & 4294967295L
                     | (
                           Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
                              | (
                                    Float.floatToRawIntBits(
                                             Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))
                                          )
                                          & 4294967295L
                                       | var17 & -4294967296L
                                 )
                                 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var114 = Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var103)) - 2.0F) & 4294967295L
            | (
                  1073741824L
                     | (
                           Float.floatToRawIntBits(var103) & 4294967295L
                              | (
                                    Float.floatToRawIntBits(var50 * 14 + 14) & 4294967295L
                                       | (
                                             Float.floatToRawIntBits(
                                                      Float.intBitsToFloat(Float.floatToRawIntBits(var15)) + Float.intBitsToFloat((int)(var17 & 4294967295L))
                                                   )
                                                   & 4294967295L
                                                | var17 & -4294967296L
                                          )
                                          & -4294967296L
                                 )
                                 & -4294967296L
                        )
                        & -4294967296L
               )
               & -4294967296L;
         long var130;
         if ((int)(var11 & 4294967295L) == 0) {
            if ((int)(var7 & 4294967295L) == 0) {
               if ((int)(var4 & 4294967295L) == 0) {
                  var132 = 1086324736L | var114 & -4294967296L;
                  break label42;
               }

               var130 = 1092616192L | var114 & -4294967296L;
            } else {
               var130 = 1096810496L | var114 & -4294967296L;
            }
         } else {
            var130 = 1099956224L | var114 & -4294967296L;
         }

         var132 = var130;
      }

      var14.setFloatValue3(
         (float)Float.intBitsToFloat(
            (int)(
               (int)(
                     Float.floatToRawIntBits(
                           Float.intBitsToFloat(Float.floatToRawIntBits(Float.intBitsToFloat(Float.floatToRawIntBits(var103)) - 2.0F))
                              - Float.intBitsToFloat((int)((int)var132 & 4294967295L))
                        )
                        & 4294967295L
                  )
                  & 4294967295L
            )
         )
      );
      var14.setFloatValue4((float)15.0F);
      float var134 = var14.getFloatValue3();
      var14.setFloatValue5((float)Float.intBitsToFloat(Float.floatToRawIntBits(var134)));
      float var135 = var14.getFloatValue4();
      var14.setFloatValue9((float)Float.intBitsToFloat(Float.floatToRawIntBits(var135)));
      return this.resolveGuiElement3(var14);
   }

   public <G extends ModuleElement> G resolveModuleElement3(G moduleElement) {
      this.moduleElementList.add(moduleElement);
      return this.resolveGuiElement3(moduleElement);
   }

   public void handleGuiElement3(GuiElement guiElement) {
      this.guiElementList.remove(guiElement);
      this.guiElementList2.remove(guiElement);
   }

   public <E extends GuiElement> E resolveGuiElement3(E guiElement) {
      this.guiElementList.add(guiElement);
      return guiElement;
   }

   @Override
   public void handleChar2(char c, int i) {
      this.getStream().forEach(guiElement -> guiElement.handleChar2(c, i));
   }

   public List<GuiElement> getColorPickerList() {
      return this.guiElementList;
   }

   public boolean isFlag3() {
      return this.flag5;
   }

   public FloatAnimator getFloatAnimator4() {
      return this.floatAnimator4;
   }

   public FloatAnimator getFloatAnimator10() {
      return this.floatAnimator10;
   }

   public FloatAnimator getFloatAnimator6() {
      return this.floatAnimator6;
   }

   public boolean isFlag11() {
      return this.flag11;
   }

   public FloatAnimator getFloatAnimator7() {
      return this.floatAnimator7;
   }

   public Supplier<Float> getFloatValueSupplier2() {
      return this.floatValueSupplier2;
   }

   public int getIntValue2() {
      return this.intValue;
   }

   public List<ScrollState> getScrollStateList() {
      return this.scrollStateList;
   }

   public boolean isFlag10() {
      return this.flag4;
   }

   public FloatAnimator getFloatAnimator9() {
      return this.floatAnimator9;
   }

   public List<ModuleElement> getModuleElementList() {
      return this.moduleElementList;
   }

   public FloatAnimator getFloatAnimator32() {
      return this.floatAnimator3;
   }

   public float getFloatValue33() {
      return this.floatValue3;
   }

   public float getFloatValue11() {
      return this.floatValue;
   }

   public List<GuiElement> getGuiElementList2() {
      return this.guiElementList2;
   }

   public boolean isFlag102() {
      return this.flag10;
   }

   @Override
   public List<Listener<?>> getListenerList() {
      return this.listenerList;
   }

   public Supplier<Float> getFloatValueSupplier() {
      return this.floatValueSupplier;
   }

   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator5;
   }

   public void setFloatValueSupplier2(Supplier<Float> supplier) {
      this.floatValueSupplier2 = supplier;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFloatValueSupplier(Supplier<Float> supplier) {
      this.floatValueSupplier = supplier;
   }

   public float getFloatValue43() {
      return this.floatValue4;
   }

   public void setFlag102(boolean flag) {
      this.flag10 = flag;
   }

   public boolean isFlag12() {
      return this.flag12;
   }

   public boolean isFlag22() {
      return this.flag2;
   }

   public float getFloatValue23() {
      return this.floatValue14;
   }

   public float getFloatValue32() {
      return this.floatValue2;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setFlag10(boolean flag) {
      this.flag4 = flag;
   }

   public boolean isFlag72() {
      return this.flag3;
   }

   public void setFlag11(boolean flag) {
      this.flag11 = flag;
   }
}
