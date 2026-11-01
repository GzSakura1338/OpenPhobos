package org.phobos.core.hud;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.nanovg.NanoVG;
import org.phobos.api.EntryContainer;
import org.phobos.api.Named;
import org.phobos.api.TextProvider;
import org.phobos.api.DragCapture;
import org.phobos.api.Bounds;
import org.phobos.api.Markable;
import org.phobos.api.SettingContainer;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.JsonSerializable;
import org.phobos.api.HudAnchors;
import org.phobos.api.Subscribe;
import org.phobos.module.misc.ChatModule;
import org.phobos.module.client.HudModule;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopBottomEnum;
import org.phobos.type.TopCenterEnum;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.ClipBounds;
import org.phobos.core.gui.ClipStack;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.Feature;
import org.phobos.core.math.FloatRange;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.screen.HudScreen;
import org.phobos.core.math.MathUtils;
import org.phobos.core.util.NullSafe;
import org.phobos.core.PhobosClient;
import org.phobos.core.PhobosLog;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.setting.SettingSection;
import org.phobos.core.setting.SettingSubsection;

public abstract class HudElement
   extends Feature
   implements MinecraftHolder,
   Named,
   JsonSerializable,
   Markable,
   DragCapture,
   TextProvider,
   EntryContainer<SettingGroup>,
   SettingContainer,
   Bounds {
   public final Setting<Boolean> reset;
   public final Setting<Color> backgroundColor;
   public final List<SettingSection> settingSectionList;
   public final FloatRange floatRange;
   public final List<SettingSubsection> settingSubsectionList;
   public final Setting<Boolean> snapping;
   public final Map<HudElement, Set<TopBottomEnum>> hudElementMap;
   public final List<Setting<?>> settingList = new ArrayList<>();
   public final List<SettingGroup> settingGroupList = new ArrayList<>();
   public final ClipStack clipStack;
   public final PhobosClient phobosClient;
   public float floatValue;
   public final Set<HudElement> hudElementSet;
   public final Setting<GuiManager.UIShader> backgroundShader;
   @Nullable
   public String text3;
   public final Setting<Boolean> background;
   public final String name;
   public boolean flag2;
   public boolean flag3;
   public float floatValue2;
   public CenterRightEnum centerRightEnum;
   public HudAnchor hudAnchor;
   public boolean flag4;
   public float floatValue3;
   public float floatValue4;
   public boolean flag5;
   public float floatValue5;
   public TopCenterEnum topCenterEnum;
   public float floatValue6;
   public float floatValue7;
   public float floatValue8;
   public boolean flag6;
   public boolean flag7;
   public boolean flag8;

   public abstract void handleDrawContext2(DrawContext var1, int var2, int var3);

   public void handleFloat(float f, float f2, float f3, float f4) {
      this.handleFloat3(f - 2.0F, f2 - 1.0F, f3 + 4.0F, f4 + 2.0F);
   }

   public HudElement(String text, float f, float f2, float f3, float f4) {
      this.settingSubsectionList = new ArrayList<>();
      this.settingSectionList = new ArrayList<>();
      this.snapping = this.resolveSetting2(new BooleanSetting("Snapping", true));
      this.background = this.resolveSetting2(new BooleanSetting("Background", false));
      this.backgroundColor = this.resolveSetting2(new ColorSetting("BackgroundColor", new Color(0, 0, 0, 50)));
      this.backgroundShader = this.resolveSetting2(new EnumSetting<>("BackgroundShader", GuiManager.UIShader.Blur));
      this.reset = this.resolveSetting2(new BooleanSetting("Reset"));
      this.hudElementSet = new HashSet<>();
      this.hudElementMap = new LinkedHashMap<>();
      this.clipStack = new ClipStack();
      this.flag2 = true;
      this.flag8 = true;
      this.centerRightEnum = CenterRightEnum.Left;
      this.topCenterEnum = TopCenterEnum.Top;
      this.floatRange = FloatRange.createFloatRange(f3, f4);
      this.resolveSettingGroup(this.background, this.backgroundColor, this.backgroundShader);
      this.phobosClient = ClientHolder.getPhobosClient();
      this.flag3 = true;
      this.floatValue = f;
      this.floatValue5 = f2;
      this.name = text;
      this.floatValue8 = f3;
      this.floatValue6 = f4;
      this.reset.handleConsumer2(booleanValue -> {
         if (this.reset.getValue()) {
            this.handleFlag2(true);
         }
      });
      Subscribe.Processor.handleListenerHolder(this);
   }

   public void handleFloat2(float f, float f2, float f3, float f4) {
      this.handleFloat3(f - 2.0F, f2 - 1.0F, f3 + 3.0F, f4 + 1.0F);
   }

   @Override
   public float value3() {
      return this.isEmpty() ? 0.0F : 2.0F;
   }

   @Nullable
   public Setting<?> resolveSetting(String text) {
      for (Setting<?> setting : this.settingList) {
         if (setting.describe3().startsWith(text)) {
            return setting;
         }
      }

      return null;
   }

   public void run6() {
      for (Setting<?> setting : this.settingList) {
         setting.testObject(setting.getObject());
      }
   }

   public void run7() {
   }

   public void handleFloat3(float f, float f2, float f3, float f4) {
      float f5 = f3 - f;
      float f6 = f4 - f2;
      this.phobosClient
         .getRenderManager()
         .handleConsumer(
            drawContext -> {
               MatrixStack matrixStack = drawContext.getMatrices();
               switch ((GuiManager.UIShader)this.backgroundShader.getValue()) {
                  case Plasma:
                     RenderUtils.handleMatrixStack20(matrixStack, this.backgroundColor.getValue(), f, f2, f3, f4);
                     break;
                  case Blur:
                  case Colorful:
                  case Fluid:
                  case None:
                     RenderUtils.handleMatrixStack12(
                        matrixStack,
                        this.backgroundColor.getValue(),
                        f,
                        f2,
                        f3,
                        f4,
                        0.0F,
                        () -> {
                           switch ((GuiManager.UIShader)this.backgroundShader.getValue()) {
                              case Blur:
                                 RenderUtils.blurShader
                                    .handleFloat(
                                       f, f2, f5, f6, 1.0F, this.backgroundColor.getValue(), 10.0F, 1.0F - this.backgroundColor.getValue().getAlpha() / 255.0F
                                    );
                                 RenderUtils.blurShader.run3();
                                 break;
                              case Colorful:
                                 RenderUtils.menuButtonShader.handleColor(this.backgroundColor.getValue());
                                 RenderUtils.menuButtonShader.run3();
                                 break;
                              case Fluid:
                                 RenderUtils.colorfulThemeShader.handleFloat(this.backgroundColor.getValue().getAlpha() / 255.0F);
                                 RenderUtils.colorfulThemeShader.run3();
                                 break;
                              case None:
                                 RenderUtils.plasmaThemeShader.handleColor(this.backgroundColor.getValue());
                                 RenderUtils.plasmaThemeShader.run3();
                           }
                        }
                     );
               }
            }
         );
   }

   public void handleFlag2(boolean flag) {
      this.floatValue = this.computeDouble2(2.0);
      this.floatValue5 = this.computeDouble(2.0);
      this.run8();
      this.floatValue8 = this.floatRange.getFloatValue();
      this.floatValue6 = this.floatRange.getFloatValue2();
      this.hudElementMap.clear();
      this.centerRightEnum = CenterRightEnum.Left;
      this.topCenterEnum = TopCenterEnum.Top;
      this.reset.setValue(false);
      this.run9();
      if (flag) {
         for (HudElement hudElement : this.phobosClient.getHudManager().getColorPickerList()) {
            if (!Objects.equals(hudElement, this)) {
               hudElement.getHudElementMap().remove(this);
            }
         }

         this.hudElementSet.clear();
      }
   }

   public void handleDrawContext3(DrawContext drawContext, int i, int i2) {
      if (!this.isFlag4()) {
         if (this.background.getValue() && !this.alwaysTrue()) {
            float[] floats = values2(this.floatValue, this.floatValue5, this.floatValue8, this.floatValue6);
            this.handleFloat2(floats[0], floats[1], floats[2], floats[3]);
         }

         if (!this.flag7) {
            this.floatValue = this.floatValue7 * mc.getWindow().getScaledWidth();
            this.floatValue5 = this.floatValue4 * mc.getWindow().getScaledHeight();
         }

         this.run15();
         this.run8();
         this.handleDrawContext2(drawContext, i, i2);
         if (!this.flag7) {
            this.run12();
            if (!this.hudElementMap.isEmpty() && this.hudElementSet.isEmpty()) {
               this.run11();
            }
         }
      }
   }

   public void run8() {
      this.floatValue7 = this.floatValue / mc.getWindow().getScaledWidth();
      this.floatValue4 = this.floatValue5 / mc.getWindow().getScaledHeight();
   }

   @Override
   public float value2() {
      return this.isEmpty() ? 0.0F : 4.0F;
   }

   public Optional<Setting<?>> resolveOptional(String text) {
      return this.settingList.stream().filter(setting -> setting.getName().equalsIgnoreCase(text)).findFirst();
   }

   public void setHudAnchor(HudAnchor hudAnchor) {
      this.hudAnchor = hudAnchor;
   }

   public void run9() {
      if (this.hudAnchor != null) {
         this.hudAnchor.handleHudElement(this);
      }
   }

   public boolean check2() {
      return this.centerRightEnum != null && this.centerRightEnum.equals(CenterRightEnum.Right);
   }

   public boolean isEmpty() {
      return Math.abs(this.getFloatValue4()) <= 0.1F;
   }

   public void run10() {
      for (HudElement hudElement : this.phobosClient.getHudManager().getColorPickerList()) {
         hudElement.getHudElementMap().remove(this);
      }

      this.hudElementMap.clear();
   }

   public float value5() {
      return ChatModule.check2()
         ? 12.0F * this.phobosClient.getMenuNavigation().getFloatAnimator4().getFloatValue2()
         : (mc.currentScreen instanceof ChatScreen ? 12 : 0);
   }

   @Override
   public final void handleJsonElement(JsonElement jsonElement) {
      this.handleFlag2(false);
      JsonObject jsonObject3 = jsonElement.getAsJsonObject();
      NullSafe.handleObject(jsonObject3.get("X-Position"), jsonElementx -> this.floatValue = jsonElementx.getAsFloat());
      NullSafe.handleObject(jsonObject3.get("Y-Position"), jsonElementx -> this.floatValue5 = jsonElementx.getAsFloat());
      NullSafe.handleObject(jsonObject3.get("X-Relative"), jsonElementx -> this.floatValue7 = jsonElementx.getAsFloat());
      NullSafe.handleObject(jsonObject3.get("Y-Relative"), jsonElementx -> this.floatValue4 = jsonElementx.getAsFloat());
      NullSafe.handleObject(jsonObject3.get("Hidden"), jsonElementx -> this.handleFlag3(jsonElementx.getAsBoolean()));
      NullSafe.handleObject(jsonObject3.get("Side"), jsonElementx -> this.centerRightEnum = CenterRightEnum.fromString(jsonElementx.getAsString()));
      NullSafe.handleObject(jsonObject3.get("Part"), jsonElementx -> this.topCenterEnum = TopCenterEnum.fromString(jsonElementx.getAsString()));
      NullSafe.handleObject(jsonObject3.get("Bottom"), jsonElementx -> this.flag6 = jsonElementx.getAsBoolean());
      JsonElement jsonElement3 = jsonObject3.get("Settings");
      if (jsonElement3 != null) {
         JsonObject jsonObject = jsonElement3.getAsJsonObject();

         for (Setting<?> setting : this.settingList) {
            JsonElement jsonElement2 = jsonObject.get(setting.getName());
            if (jsonElement2 == null) {
               setting.testObject(setting.getObject());
            } else {
               try {
                  setting.handleJsonElement(jsonElement2);
               } catch (Throwable throwable) {
                  PhobosLog.handleText(this.getName(), "Failed to deserialize %s".formatted(setting.getName()));
                  throwable.printStackTrace(System.err);
               }
            }
         }
      }

      JsonElement jsonElement4 = jsonObject3.get("Attached");
      if (jsonElement4 != null) {
         JsonObject jsonObject2 = jsonElement4.getAsJsonObject();

         for (Map.Entry<String, JsonElement> entry : jsonObject2.entrySet()) {
            this.phobosClient.getHudManager().resolveOptional3(entry.getKey()).ifPresent(hudElement -> {
               JsonArray jsonArray = entry.getValue().getAsJsonArray();
               Set<TopBottomEnum> set = this.hudElementMap.computeIfAbsent(hudElement, hudElementx -> new HashSet<>());

               for (JsonElement jsonElementx : jsonArray) {
                  TopBottomEnum topBottomEnum = TopBottomEnum.fromString(jsonElementx.getAsString());
                  if (topBottomEnum != null) {
                     set.add(topBottomEnum);
                  }
               }
            });
         }
      }
   }

   public void handleDouble3(double d) {
      float f = this.computeDouble(d);
      this.setFloatValue(f);
      this.setFloatValue42(f / mc.getWindow().getScaledHeight());
   }

   public float computeDouble(double d) {
      float[] floats = this.values();
      float f2 = mc.getWindow().getScaledHeight() - 2 - (floats[3] - floats[1]);
      if (this.floatValue6 < 0.0F) {
         float f = Math.abs(this.floatValue6);
         this.flag6 = this.getHudElementSet().isEmpty() && MathUtils.testDouble(d, f2 + f, 6.0);
         return (float)MathUtils.computeDouble5(d, 2.0, f2 + f);
      } else {
         this.flag6 = this.getHudElementSet().isEmpty() && MathUtils.testDouble(d, f2, 6.0);
         return (float)MathUtils.computeDouble5(d, 2.0, f2);
      }
   }

   public void handleHudElement(HudElement hudElement, TopBottomEnum topBottomEnum) {
      hudElement.getHudElementSet().add(this);
      this.hudElementMap.computeIfAbsent(hudElement, hudElementx -> new HashSet<>()).add(topBottomEnum);
   }

   public void handleDouble2(double d, double d2, double d3) {
   }

   public void run11() {
      for (Map.Entry<HudElement, Set<TopBottomEnum>> entry : this.hudElementMap.entrySet()) {
         HudElement hudElement = entry.getKey();
         if (!hudElement.isFlag72()
            && !hudElement.isFlag4()
            && !hudElement.isFlag3()
            && hudElement.getHudElementMap().remove(this) == null) {
            for (TopBottomEnum topBottomEnum : entry.getValue()) {
               topBottomEnum.retain(this, hudElement);
               if (!hudElement.hudElementMap.isEmpty()) {
                  hudElement.run11();
                  hudElement.run12();
               }
            }
         }
      }
   }

   public float computeDouble2(double d) {
      float[] floats = this.values();
      float f = this.centerRightEnum.equals(CenterRightEnum.Right) && this.floatValue8 < 0.0F
         ? mc.getWindow().getScaledWidth() - 2
         : mc.getWindow().getScaledWidth() - 2 - (floats[2] - floats[0]);
      return (float)MathUtils.computeDouble5(d, 2.0, f);
   }

   @Override
   public final JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add("X-Position", new JsonPrimitive(this.floatValue));
      jsonObject.add("Y-Position", new JsonPrimitive(this.floatValue5));
      jsonObject.add("X-Relative", new JsonPrimitive(this.floatValue7));
      jsonObject.add("Y-Relative", new JsonPrimitive(this.floatValue4));
      jsonObject.add("Hidden", new JsonPrimitive(this.isFlag3()));
      jsonObject.add("Side", new JsonPrimitive(this.centerRightEnum.toString()));
      jsonObject.add("Part", new JsonPrimitive(this.topCenterEnum.toString()));
      jsonObject.add("Bottom", new JsonPrimitive(this.flag6));
      JsonObject jsonObject2 = new JsonObject();

      for (Setting<?> setting : this.settingList) {
         try {
            JsonElement jsonElement = setting.createJsonPrimitive();
            if (jsonElement != null) {
               jsonObject2.add(setting.getName(), jsonElement);
            }
         } catch (Throwable throwable) {
            PhobosLog.handleText(this.getName(), "Failed to serialize %s".formatted(setting.getName()));
            throwable.printStackTrace(System.err);
         }
      }

      jsonObject.add("Settings", jsonObject2);
      JsonObject jsonObject3 = new JsonObject();

      for (Map.Entry<HudElement, Set<TopBottomEnum>> entry : this.hudElementMap.entrySet()) {
         HudElement hudElement = entry.getKey();
         JsonArray jsonArray = new JsonArray();

         for (TopBottomEnum topBottomEnum : entry.getValue()) {
            jsonArray.add(topBottomEnum.toString());
         }

         jsonObject3.add(hudElement.getName(), jsonArray);
      }

      jsonObject.add("Attached", jsonObject3);
      return jsonObject;
   }

   public void handleFlag3(boolean flag) {
      this.flag3 = flag;
      if (flag) {
         this.phobosClient.getEventBus().handleObject4(this);
         this.run10();
      } else {
         this.phobosClient.getEventBus().handleObject(this);
      }
   }

   public Stream<Setting<?>> stream() {
      return this.settingList.stream();
   }

   public void run12() {
      if (this.flag6) {
         float f2 = mc.getWindow().getScaledHeight() - this.value5();
         if (this.floatValue6 < 0.0F) {
            float f = f2 - 2.0F;
            this.setFloatValue(f);
            this.setFloatValue42(f / mc.getWindow().getScaledHeight());
         } else {
            float f3 = f2 - this.floatValue6 - 2.0F;
            this.setFloatValue(f3);
            this.setFloatValue42(f3 / mc.getWindow().getScaledHeight());
         }
      }
   }

   public static float[] values2(float f, float f2, float f3, float f4) {
      float f5 = f + f3;
      float f6 = f2 + f4;
      return new float[]{Math.min(f, f5), Math.min(f2, f6), Math.max(f, f5), Math.max(f2, f6)};
   }

   public void handleDouble4(double d) {
      float f = this.computeDouble2(d);
      this.setFloatValue2(f);
      this.setFloatValue7(f / mc.getWindow().getScaledWidth());
   }

   public String describeText2(String text, String text2) {
      return HudModule.getHudModule().describeText2(text, text2);
   }

   @Override
   public float[] values() {
      float f = this.floatValue6 < 0.0F ? this.floatValue5 + this.floatValue6 : this.floatValue5;
      float f2 = this.floatValue6 < 0.0F ? this.floatValue5 : this.floatValue5 + this.floatValue6;
      float f3 = this.floatValue8 < 0.0F ? this.floatValue + this.floatValue8 : this.floatValue;
      float f4 = this.floatValue8 < 0.0F ? this.floatValue : this.floatValue + this.floatValue8;
      return new float[]{f3, f, f4, f2};
   }

   public void handleHudElement2(HudElement hudElement, TopBottomEnum topBottomEnum) {
      hudElement.getHudElementSet().remove(this);
      Set<TopBottomEnum> set = this.hudElementMap.get(hudElement);
      if (set != null) {
         set.remove(topBottomEnum);
         if (set.isEmpty()) {
            this.hudElementMap.remove(hudElement);
         }
      }
   }

   public double computeRectangle(Rectangle rectangle, Rectangle rectangle2) {
      return rectangle.intersects(rectangle2) ? 0.0 : this.computeRectangle2(rectangle, rectangle2);
   }

   public void handleDrawContext4(DrawContext drawContext, String text, float f, float f2) {
      HudModule.getHudModule().handleDrawContext4(drawContext, text, this.getFloatValue7() + f, this.getFloatValue8() + f2);
   }

   public void handleHudScreen(HudScreen hudScreen, double d, double d2, List<HudElement> list, List<HudAnchors> list2) {
      if (!this.flag2) {
         this.flag7 = false;
      } else {
         this.handleHudScreen2(hudScreen, d, d2, list, list2);
         this.flag7 = false;
         if (hudScreen.dragCapture == this) {
            hudScreen.dragCapture = null;
         }
      }
   }

   public void run13() {
      if (this.check()) {
         double d = mc.getWindow().getScaledWidth() / 2.0;
         this.floatValue = (float)(d - this.floatValue8 / 2.0);
      }
   }

   public boolean check() {
      return this.centerRightEnum != null && this.centerRightEnum.equals(CenterRightEnum.Center);
   }

   public void handleHudScreen2(HudScreen hudScreen, double d, double d2, List<HudElement> list, List<HudAnchors> list2) {
      this.floatValue = this.computeDouble2(d - this.floatValue2);
      this.floatValue5 = this.computeDouble(d2 - this.floatValue3);
      float[] floats3 = this.values();
      double d5 = floats3[2];
      double d6 = mc.getWindow().getScaledWidth() / 2.0;
      double d7 = mc.getWindow().getScaledHeight() / 2.0;
      double d8 = this.floatValue + this.floatValue8 / 2.0;
      this.topCenterEnum = this.floatValue5 < d7 ? TopCenterEnum.Top : TopCenterEnum.Bottom;
      if (!this.snapping.getValue()) {
         if (Math.abs(d6 - d8) < 3.0) {
            this.centerRightEnum = CenterRightEnum.Center;
         } else {
            this.centerRightEnum = this.floatValue < d6 ? CenterRightEnum.Left : CenterRightEnum.Right;
         }

         this.floatValue = this.computeDouble2(this.floatValue);
         this.floatValue5 = this.computeDouble(this.floatValue5);
      } else {
         if (Math.abs(d6 - d8) < 3.0 && !this.check2()) {
            this.floatValue = (float)(d6 - this.floatValue8 / 2.0);
            this.centerRightEnum = CenterRightEnum.Center;
            floats3 = this.values();
            hudScreen.handleHudElement(this, floats3[0]);
            hudScreen.handleHudElement(this, floats3[2]);
         } else {
            this.centerRightEnum = this.floatValue < d6 ? CenterRightEnum.Left : CenterRightEnum.Right;
         }

         if (!this.centerRightEnum.equals(CenterRightEnum.Right)) {
            if (Math.abs(d5 - d6) < 3.0) {
               this.setFloatValue2((float)(d6 - this.floatValue8 - 2.0));
               hudScreen.handleHudElement(this, this.floatValue);
            }
         } else if (Math.abs((this.check6() ? this.floatValue + this.floatValue8 : this.floatValue) - d6) < 3.0) {
            this.setFloatValue2(this.check6() ? (float)(d6 + 2.0 - this.floatValue8) : (float)d6 + 2.0F);
            hudScreen.handleHudElement(this, this.floatValue);
         }

         if (Math.abs(d7 - MathHelper.lerp(0.5F, this.floatValue5, this.floatValue5 + this.floatValue6)) < 3.0 && !this.check5()) {
            this.floatValue5 = (float)(d7 - this.getFloatValue4() / 2.0);
            hudScreen.handleHudElement3(this, this.floatValue5);
            hudScreen.handleHudElement3(this, this.floatValue5 + this.floatValue6);
            this.topCenterEnum = TopCenterEnum.Center;
         }

         int i = 0;

         for (HudElement hudElement : list) {
            if (i >= 2) {
               break;
            }

            if (!hudElement.equals(this)) {
               float[] floats = hudElement.values();
               boolean flag = !hudElement.isFlag4()
                  && !hudElement.isFlag3()
                  && !hudElement.isEmpty()
                  && hudElement.getSnapping().getValue()
                  && this.testFloat(floats);
               AnchorOffset anchorOffset = new AnchorOffset();

               for (TopBottomEnum topBottomEnum : TopBottomEnum.values()) {
                  if (!flag) {
                     hudElement.handleHudElement2(this, topBottomEnum);
                  } else {
                     double d3 = topBottomEnum.connects(this, hudElement);
                     if (d3 == Double.MAX_VALUE) {
                        hudElement.handleHudElement2(this, topBottomEnum);
                     } else {
                        anchorOffset.handleTopBottomEnum(topBottomEnum, d3);
                     }
                  }
               }

               TopBottomEnum[] topBottomEnums = anchorOffset.values();

               for (TopBottomEnum topBottomEnum2 : topBottomEnums) {
                  topBottomEnum2.clip(hudScreen, this, hudElement);
                  hudElement.handleHudElement(this, topBottomEnum2);
                  i++;
               }
            }
         }

         for (HudAnchors hudAnchors : list2) {
            if (i >= 2) {
               break;
            }

            float[] floats2 = hudAnchors.values();
            boolean flag2 = this.testFloat(floats2);
            AnchorOffset anchorOffset2 = new AnchorOffset();

            for (TopBottomEnum topBottomEnum3 : TopBottomEnum.values()) {
               if (!flag2) {
                  hudAnchors.handleHudElement2(this, topBottomEnum3);
               } else {
                  double d4 = topBottomEnum3.connects(this, hudAnchors);
                  if (d4 == Double.MAX_VALUE) {
                     hudAnchors.handleHudElement2(this, topBottomEnum3);
                  } else {
                     anchorOffset2.handleTopBottomEnum(topBottomEnum3, d4);
                  }
               }
            }

            TopBottomEnum[] topBottomEnums2 = anchorOffset2.values();

            for (TopBottomEnum topBottomEnum4 : topBottomEnums2) {
               topBottomEnum4.clip(hudScreen, this, hudAnchors);
               hudAnchors.handleHudElement(this, topBottomEnum4);
               i++;
            }
         }

         this.floatValue = this.computeDouble2(this.floatValue);
         this.floatValue5 = this.computeDouble(this.floatValue5);
      }
   }

   public String describeText3(String text) {
      return String.format("§7[§f%s§7]", text);
   }

   public void handleHudScreen3(HudScreen hudScreen, double d, double d2, List<HudElement> list, List<HudAnchors> list2) {
      if (!this.flag2) {
         this.flag7 = false;
      } else {
         if (!this.flag7 && this.testDouble(d, d2)) {
            this.floatValue2 = (float)(d - this.floatValue);
            this.floatValue3 = (float)(d2 - this.floatValue5);
            this.flag7 = true;
            hudScreen.dragCapture = this;
         }

         if (this.flag7) {
            this.hudElementMap.clear();
            this.hudElementSet.clear();
            this.handleHudScreen2(hudScreen, d, d2, list, list2);
         }
      }
   }

   public boolean testDouble(double d, double d2) {
      float[] floats = this.values();
      return d >= floats[0] && d <= floats[2] && d2 >= floats[1] && d2 <= floats[3];
   }

   public void handleDrawContext5(DrawContext drawContext, double d, double d2, double d3, double d4, Color color) {
      RenderUtils.handleMatrixStack20(
         drawContext.getMatrices(), color, this.getFloatValue7() + d, this.getFloatValue8() + d2, this.getFloatValue7() + d3, this.getFloatValue8() + d4
      );
   }

   public void handleDrawContext6(DrawContext drawContext, String text, float f, float f2, float f3, Color color) {
      HudModule.getHudModule().handleDrawContext8(drawContext, text, this.getFloatValue7() + f, this.getFloatValue8() + f2, color);
   }

   public void run14() {
      if (this.check3()) {
         double d = mc.getWindow().getScaledHeight() / 2.0;
         this.floatValue5 = (float)(d - this.floatValue6 / 2.0);
      }
   }

   public void handleDrawContext7(DrawContext drawContext, String text, float f, float f2, float f3) {
      HudModule.getHudModule().handleDrawContext7(drawContext, text, this.getFloatValue7() + f, this.getFloatValue8() + f2, f3);
   }

   public void handleDrawContext8(DrawContext drawContext, String text, float f, float f2, Color color) {
      HudModule.getHudModule().handleDrawContext8(drawContext, text, this.getFloatValue7() + f, this.getFloatValue8() + f2, color);
   }

   public double computeRectangle2(Rectangle rectangle, Rectangle rectangle2) {
      int i = 0;
      if (rectangle.x + rectangle.width < rectangle2.x) {
         i = rectangle2.x - (rectangle.x + rectangle.width);
      } else if (rectangle2.x + rectangle2.width < rectangle.x) {
         i = rectangle.x - (rectangle2.x + rectangle2.width);
      }

      int i2 = 0;
      if (rectangle.y + rectangle.height < rectangle2.y) {
         i2 = rectangle2.y - (rectangle.y + rectangle.height);
      } else if (rectangle2.y + rectangle2.height < rectangle.y) {
         i2 = rectangle.y - (rectangle2.y + rectangle2.height);
      }

      if (i == 0) {
         return i2;
      } else {
         return i2 == 0 ? i : Math.sqrt(i * i + i2 * i2);
      }
   }

   public boolean check3() {
      return this.topCenterEnum != null && this.topCenterEnum.equals(TopCenterEnum.Center);
   }

   public void run15() {
      if (this.snapping.getValue()) {
         this.run13();
         this.run14();
      }
   }

   public boolean testFloat(float[] floats) {
      float[] floats2 = this.values();
      Rectangle rectangle = new Rectangle((int)floats2[0], (int)floats2[1], (int)(floats2[2] - floats2[0]), (int)(floats2[3] - floats2[1]));
      Rectangle rectangle2 = new Rectangle((int)floats[0], (int)floats[1], (int)(floats[2] - floats[0]), (int)(floats[3] - floats[1]));
      return this.computeRectangle(rectangle, rectangle2) < 15.0;
   }

   public SettingGroup resolveSettingGroup(Setting<Boolean> setting, Setting<?>... settings) {
      SettingGroup settingGroup = new SettingGroup(setting, settings);
      this.settingGroupList.add(settingGroup);
      return settingGroup;
   }

   public void start() {
      ClientHolder.getPhobosClient().getRenderManager().start();
   }

   public boolean alwaysTrue() {
      return false;
   }

   public float computeText(String text) {
      return HudModule.getHudModule().computeText(text);
   }

   public void handleFloat4(float f) {
      this.setFloatValue3(Math.max(this.getFloatValue3(), f));
   }

   public boolean check4() {
      return HudModule.getHudModule().check4();
   }

   public long value() {
      return ClientHolder.getPhobosClient().getRenderManager().value();
   }

   public void handleClipBounds(ClipBounds clipBounds) {
      if (clipBounds == null) {
         RenderSystem.disableScissor();
      } else {
         Window window = mc.getWindow();
         int i = window.getHeight();
         double d = window.getScaleFactor();
         double d2 = clipBounds.getDoubleValue2() * d;
         double d3 = i - clipBounds.value2() * d;
         double d4 = clipBounds.getDoubleValue3() * d;
         double d5 = clipBounds.getDoubleValue() * d;
         RenderSystem.enableScissor((int)d2, (int)d3, Math.max(0, (int)d4), Math.max(0, (int)d5));
      }
   }

   public void handleDouble5(double d, double d2, double d3, double d4) {
      long l = this.value();
      if (l != 0L) {
         NanoVG.nvgSave(l);
         NanoVG.nvgIntersectScissor(l, (float)d, (float)d2, (float)d3, (float)d4);
      }

      this.handleClipBounds(this.clipStack.resolveClipBounds(new ClipBounds(d, d2, d3, d4)));
   }

   public SettingGroup resolveSettingGroup2(String text, Setting<?>... settings) {
      SettingGroup settingGroup = new SettingGroup(text, settings);
      this.settingGroupList.add(settingGroup);
      return settingGroup;
   }

   public float getFloatValue7() {
      return this.flag8 ? this.getFloatValue2() : 0.0F;
   }

   public void run3() {
      long l = this.value();
      if (l != 0L) {
         NanoVG.nvgResetScissor(l);
         NanoVG.nvgRestore(l);
      }

      this.handleClipBounds(this.clipStack.getClipBounds());
   }

   public float computeText2(String text) {
      return HudModule.getHudModule().computeText2(text);
   }

   public void handleText3(String text) {
      this.floatValue8 = this.computeText2(text);
   }

   public void handleDouble(double d, double d2, double d3, double d4) {
      long l = this.value();
      if (l != 0L) {
         NanoVG.nvgSave(l);
         NanoVG.nvgIntersectScissor(l, (float)(this.getFloatValue2() + d), (float)(this.getFloatValue() + d2), (float)d3, (float)d4);
      }

      this.handleClipBounds(this.clipStack.resolveClipBounds(new ClipBounds(this.getFloatValue2() + d, this.getFloatValue() + d2, d3, d4)));
   }

   public void run4() {
      ClientHolder.getPhobosClient().getRenderManager().run4();
   }

   public float getFloatValue8() {
      return this.flag8 ? this.getFloatValue() : 0.0F;
   }

   public void handleRunnable(Runnable runnable) {
      this.flag8 = false;
      runnable.run();
      this.flag8 = true;
   }

   @Override
   public List<Setting<?>> getSettingList() {
      return this.settingList;
   }

   public Setting<Boolean> getBackground() {
      return this.background;
   }

   public ClipStack getClipStack() {
      return this.clipStack;
   }

   public Setting<Boolean> getReset() {
      return this.reset;
   }

   public List<SettingSection> getSettingSectionList() {
      return this.settingSectionList;
   }

   public Setting<GuiManager.UIShader> getBackgroundShader() {
      return this.backgroundShader;
   }

   public Set<HudElement> getHudElementSet() {
      return this.hudElementSet;
   }

   public List<SettingSubsection> getSettingSubsectionList() {
      return this.settingSubsectionList;
   }

   public SettingSubsection.Builder createBuilder() {
      return new SettingSubsection.Builder(this.settingSubsectionList, this.settingGroupList);
   }

   @Override
   public <S extends Setting<?>> S resolveSetting2(S setting) {
      this.settingList.add(setting);
      setting.setSettingContainer(this);
      return setting;
   }

   @Override
   public List<SettingGroup> getSettingGroupList() {
      return this.settingGroupList;
   }

   public FloatRange getFloatRange() {
      return this.floatRange;
   }

   public Setting<Color> getBackgroundColor() {
      return this.backgroundColor;
   }

   @Nullable
   @Override
   public String getText() {
      return this.text3;
   }

   public SettingSection.Builder createBuilder2() {
      return new SettingSection.Builder(this.settingSectionList, this.settingSubsectionList, this.settingGroupList);
   }

   public Setting<Boolean> getSnapping() {
      return this.snapping;
   }

   @SafeVarargs
   @Override
   public final <S extends Setting<?>> void handleG77G8vOcsOCkBD4vxs1B9Tu4ZA82vvYs(S... settings) {
      for (Setting<?> setting : settings) {
         this.resolveSetting2(setting);
      }
   }

   public Map<HudElement, Set<TopBottomEnum>> getHudElementMap() {
      return this.hudElementMap;
   }

   public float getFloatValue72() {
      return this.floatValue7;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public HudAnchor getHudAnchor() {
      return this.hudAnchor;
   }

   public boolean isFlag6() {
      return this.flag6;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   @Override
   public float getFloatValue3() {
      return this.floatValue8;
   }

   public float getFloatValue23() {
      return this.floatValue2;
   }

   @Override
   public boolean isFlag4() {
      return this.flag4;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public float getFloatValue32() {
      return this.floatValue3;
   }

   public float getFloatValue42() {
      return this.floatValue4;
   }

   @Override
   public float getFloatValue() {
      return this.floatValue5;
   }

   public boolean isFlag8() {
      return this.flag8;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   @Override
   public float getFloatValue2() {
      return this.floatValue;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   @Override
   public float getFloatValue4() {
      return this.floatValue6;
   }

   public boolean isFlag72() {
      return this.flag7;
   }

   public void setFloatValue32(float f) {
      this.floatValue3 = f;
   }

   @Override
   public void setFloatValue(float f) {
      this.floatValue5 = f;
   }

   @Override
   public void setFloatValue4(float f) {
      this.floatValue6 = f;
   }

   @Override
   public void setFloatValue3(float f) {
      this.floatValue8 = f;
   }

   public void setFloatValue22(float f) {
      this.floatValue2 = f;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFloatValue7(float f) {
      this.floatValue7 = f;
   }

   public void setFlag8(boolean flag) {
      this.flag8 = flag;
   }

   public void setFlag7(boolean flag) {
      this.flag7 = flag;
   }

   @Override
   public void setFloatValue2(float f) {
      this.floatValue = f;
   }

   public void setText3(@Nullable String text) {
      this.text3 = text;
   }

   public void setHudAnchor2(HudAnchor hudAnchor) {
      this.hudAnchor = hudAnchor;
   }

   @Override
   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public TopCenterEnum getTopCenterEnum() {
      return this.topCenterEnum;
   }

   public CenterRightEnum getCenterRightEnum() {
      return this.centerRightEnum;
   }

   public void setFlag5(boolean flag) {
      this.flag5 = flag;
   }

   public void setFlag6(boolean flag) {
      this.flag6 = flag;
   }

   public void setFloatValue42(float f) {
      this.floatValue4 = f;
   }

   public void setCenterRightEnum(CenterRightEnum centerRightEnum) {
      this.centerRightEnum = centerRightEnum;
   }

   public void setTopCenterEnum(TopCenterEnum topCenterEnum) {
      this.topCenterEnum = topCenterEnum;
   }
}
