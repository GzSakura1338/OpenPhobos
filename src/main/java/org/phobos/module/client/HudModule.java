package org.phobos.module.client;

import java.awt.Color;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.GradientUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.Listener;
import org.phobos.event.RenderEvent2;
import org.phobos.mixin.game.render.interfaces.IDrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;

public class HudModule extends Module implements EventSubscriber {
   public final Setting<Color> color;
   public final Setting<Boolean> uI;
   public final Setting<Color> colors3;
   public final Setting<Color> colors2;
   public final Setting<HudModule.InfoStyle> style;
   public final Setting<Color> colors4;
   public final Setting<HudModule.Rainbow> rainbow = this.resolveSetting2(new EnumSetting<>("Rainbow", HudModule.Rainbow.None));
   public final Queue<Consumer<HudModule>> consumerQueue;
   public final Setting<Color> colors1;
   public final Setting<Boolean> layout;
   public final Setting<Boolean> glow;
   public final Setting<Integer> speed = this.resolveSetting2(
      new NumberSetting<>("Speed", 15, 2, 100).resolveSetting3(() -> this.rainbow.getValue().intValue != -1)
   );
   public final Setting<Boolean> brackets;
   public final Setting<HudModule.Font> font;
   public static HudModule hudModule;
   public static final Logger LOGGER = LoggerFactory.getLogger(HudModule.class);
   public final Setting<Boolean> shadow;

   public boolean check4() {
      return switch ((HudModule.Font)this.font.getValue()) {
         case Native -> false;
         case Vanilla -> FontModule.getFontModule().check7();
      };
   }

   public void handleDrawContext(DrawContext drawContext) {
      if (!mc.options.hudHidden && !mc.getDebugHud().shouldShowDebugHud() && mc.player != null && mc.world != null) {
         this.phobosClient.getHudManager().handleDrawContext(drawContext);
      }
   }

   public String describeModule(Module module) {
      String text = module.nothing();
      return text != null
         ? String.format(
            "%s §7%s§f%s§7%s",
            module.getText3() != null ? module.getText3() : module.getName(),
            this.brackets.getValue() ? "[" : "",
            module.nothing(),
            this.brackets.getValue() ? "]" : ""
         )
         : module.getName();
   }

   public int computeInt(int i, int i2, double d) {
      return (int)this.computeDouble(i, i2, (float)d);
   }

   public HudModule.Font getFont() {
      return !FontModule.getFontModule().check7() ? HudModule.Font.Vanilla : this.font.getValue();
   }

   public String describeModule2(Module module) {
      String text = module.nothing();
      return text != null
         ? String.format("%s %s%s%s", module.describe4(), this.brackets.getValue() ? "[" : "", text, this.brackets.getValue() ? "]" : "")
         : module.describe4();
   }

   public float computeText2(String text) {
      return switch (this.getFont()) {
         case Native -> {
            float var6 = mc.textRenderer.getWidth(text);
            yield var6;
         }
         case Vanilla -> {
            float var5;
            try {
               var5 = (float)this.phobosClient.getRenderManager().computeText7(text, true, this.phobosClient.getRenderManager().value2());
            } catch (Throwable throwable) {
               var5 = 0.0F;
               yield var5;
            }

            yield var5;
         }
      };
   }

   public double computeDouble(double d, double d2, double d3) {
      return d + (d2 - d) * d3;
   }

   public Color resolveColor(Color color, Color color2, double d, double d2) {
      int i = (int)((System.currentTimeMillis() / d + d2) % 360.0);
      i = (i >= 180 ? 360 - i : i) * 2;
      return this.createColor(color, color2, i / 360.0F);
   }

   public void handleDrawContext7(DrawContext drawContext, String text, float f, float f2, float f3) {
      int i = this.computeFloat(f, f2, f3);
      Color color = new Color(i);
      Color color2 = new Color(
         color.getRed() / 255.0F,
         color.getGreen() / 255.0F,
         color.getBlue() / 255.0F,
         MathUtils.computeFloat3(this.color.getValue().getAlpha() / 255.0F * f3, 0.0F, 1.0F)
      );
      String text2 = this.rainbow.getValue().getCode();
      switch (this.getFont()) {
         case Native:
            mc.textRenderer
               .draw(
                  text2 + text,
                  f,
                  f2,
                  color2.hashCode(),
                  this.shadow.getValue(),
                  drawContext.getMatrices().peek().getPositionMatrix(),
                  drawContext.getVertexConsumers(),
                  TextLayerType.NORMAL,
                  0,
                  15728880,
                  mc.textRenderer.isRightToLeft()
               );
            ((IDrawContext)(Object)drawContext).invokeDraw();
            break;
         case Vanilla:
            try {
               if (this.glow.getValue()) {
                  this.phobosClient.getRenderManager().handleText(text2 + text, f, f2, color2, 1.0F, true);
                  return;
               }

               this.phobosClient.getRenderManager().handleDrawContext(drawContext, text2 + text, f, f2, color2, 1.0F, this.shadow.getValue());
            } catch (Throwable throwable) {
               throwable.printStackTrace();
            }
      }
   }

   public void handleDrawContext4(DrawContext drawContext, String text, float f, float f2) {
      int i = this.computeFloat(f, f2, 1.0F);
      Color color = new Color(i);
      String text2 = this.rainbow.getValue().getCode();
      switch (this.getFont()) {
         case Native:
            mc.textRenderer
               .draw(
                  text2 + text,
                  f,
                  f2,
                  i,
                  this.shadow.getValue(),
                  drawContext.getMatrices().peek().getPositionMatrix(),
                  drawContext.getVertexConsumers(),
                  TextLayerType.NORMAL,
                  0,
                  15728880,
                  mc.textRenderer.isRightToLeft()
               );
            ((IDrawContext)(Object)drawContext).invokeDraw();
            break;
         case Vanilla:
            try {
               if (this.glow.getValue()) {
                  this.phobosClient.getRenderManager().handleText(text2 + text, f, f2, color, 1.0F, true);
                  return;
               }

               this.phobosClient.getRenderManager().handleDrawContext(drawContext, text2 + text, f, f2, color, 1.0F, this.shadow.getValue());
            } catch (Throwable throwable) {
               throwable.printStackTrace();
            }
      }
   }

   public String describeText2(String text, String text2) {
      String text3 = "§f";

      return switch ((HudModule.InfoStyle)this.style.getValue()) {
         case Name -> text + text2;
         case Value -> "§f" + text + "§r" + text2;
         case None -> text + "§f" + text2 + "§r";
      };
   }

   public int computeFloat(float f, float f2, float f3) {
      Color color = ColorUtils.createColor6(this.color.getValue(), MathUtils.computeFloat3(this.color.getValue().getAlpha() / 255.0F, 0.0F, 1.0F) * f3);

      return switch ((HudModule.Rainbow)this.rainbow.getValue()) {
         case Vertical, None -> color.hashCode();
         case Double -> Color.HSBtoRGB(ColorUtils.computeDouble(f2), 1.0F, 1.0F);
         case Horizontal -> ColorUtils.computeFloat((f2 + 1.0F) * 0.89F, color);
         case Static -> GradientUtils.resolveColor2(this.speed.getValue().intValue(), f2, this.colors1.getValue(), this.colors2.getValue()).hashCode();
         case Triple -> GradientUtils.resolveColor2(this.speed.getValue().intValue(), f2, this.colors1.getValue(), this.colors2.getValue(), this.colors3.getValue())
            .hashCode();
         case Quad -> GradientUtils.resolveColor2(
               this.speed.getValue().intValue(), f2, this.colors1.getValue(), this.colors2.getValue(), this.colors3.getValue(), this.colors4.getValue()
            )
            .hashCode();
      };
   }

   public HudModule(PhobosClient phobosClient) {
      super(phobosClient, "Hud", Categories.client, "Client hud");
      this.colors1 = this.resolveSetting2(new ColorSetting("Colors1", Color.WHITE));
      this.colors2 = this.resolveSetting2(new ColorSetting("Colors2", Color.WHITE));
      this.colors3 = this.resolveSetting2(
         new ColorSetting("Colors3", Color.WHITE).resolveSetting3(this.resolveVisibility2(this.rainbow, HudModule.Rainbow.Triple, HudModule.Rainbow.Quad))
      );
      this.colors4 = this.resolveSetting2(
         new ColorSetting("Colors4", Color.WHITE).resolveSetting3(this.resolveVisibility2(this.rainbow, HudModule.Rainbow.Quad))
      );
      this.color = this.resolveSetting2(
         new ColorSetting("Color", Color.WHITE).getColorSetting().resolveSetting3(() -> this.rainbow.getValue().intValue == -1)
      );
      this.style = this.resolveSetting2(new EnumSetting<>("Style", HudModule.InfoStyle.Value));
      this.font = this.resolveSetting2(new EnumSetting<>("Font", HudModule.Font.Vanilla).resolveSetting7("Font type"));
      this.glow = this.resolveSetting2(
         new BooleanSetting("Glow").resolveSetting3(() -> this.font.getValue().equals(HudModule.Font.Native)).resolveSetting7("Font glow")
      );
      this.shadow = this.resolveSetting2(new BooleanSetting("Shadow", true).resolveSetting7("Font shadow"));
      this.brackets = this.resolveSetting2(new BooleanSetting("Brackets", true).resolveSetting7("Info brackets"));
      this.uI = this.resolveSetting2(new BooleanSetting("UI", true).resolveSetting7("Draws hud in the main UI"));
      this.layout = this.resolveSetting2(new BooleanSetting("Layout").resolveSetting7("Creates vanilla hud layout for hotbar attachments"));
      this.consumerQueue = new ConcurrentLinkedQueue<>();
      this.resolveSettingGroup2("Colors", this.colors1, this.colors2, this.colors3, this.colors4)
         .resolveSettingGroup5(() -> this.rainbow.getValue().intValue != -1);
      this.handleListener(new Listener<RenderEvent2>(Integer.MIN_VALUE) {
         public void handleObject2(RenderEvent2 renderEvent2) {
            phobosClient.getRenderManager().start();
            HudModule.this.consumerQueue.forEach(consumer -> consumer.accept(HudModule.this));
            HudModule.this.consumerQueue.clear();
            HudModule.this.handleDrawContext(renderEvent2.getDrawContext());
            phobosClient.getRenderManager().run4();
         }
      });
      this.font.handleConsumer2(font -> FontModule.Dispatcher.run());
      hudModule = this;
   }

   public Setting<HudModule.Rainbow> getRainbow() {
      return this.rainbow;
   }

   public Color createColor(Color color, Color color2, float f) {
      f = Math.min(1.0F, Math.max(0.0F, f));
      return new Color(
         this.computeInt(color.getRed(), color2.getRed(), f),
         this.computeInt(color.getGreen(), color2.getGreen(), f),
         this.computeInt(color.getBlue(), color2.getBlue(), f),
         this.computeInt(color.getAlpha(), color2.getAlpha(), f)
      );
   }

   public static void handleConsumer(Consumer<HudModule> consumer) {
      hudModule.consumerQueue.add(consumer);
   }

   public String describeText3(String text) {
      return this.brackets.getValue() ? String.format("§7[§f%s§7]", text) : String.format("§f%s", text);
   }

   public void handleDrawContext8(DrawContext drawContext, String text, float f, float f2, Color color) {
      Color color2 = new Color(
         color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F * (this.color.getValue().getAlpha() / 255.0F)
      );
      String text2 = this.rainbow.getValue().getCode();
      switch (this.getFont()) {
         case Native:
            mc.textRenderer
               .draw(
                  text2 + text,
                  f,
                  f2,
                  color2.hashCode(),
                  this.shadow.getValue(),
                  drawContext.getMatrices().peek().getPositionMatrix(),
                  drawContext.getVertexConsumers(),
                  TextLayerType.NORMAL,
                  0,
                  15728880,
                  mc.textRenderer.isRightToLeft()
               );
            ((IDrawContext)(Object)drawContext).invokeDraw();
            break;
         case Vanilla:
            try {
               if (this.glow.getValue()) {
                  this.phobosClient.getRenderManager().handleText(text2 + text, f, f2, color2, 1.0F, true);
                  return;
               }

               this.phobosClient.getRenderManager().handleDrawContext(drawContext, text2 + text, f, f2, color2, 1.0F, this.shadow.getValue());
            } catch (Throwable throwable) {
               throwable.printStackTrace();
            }
      }
   }

   public float computeText(String text) {
      return switch (this.getFont()) {
         case Native -> {
            float var6 = 9.0F;
            yield var6;
         }
         case Vanilla -> {
            float var5;
            try {
               var5 = (float)this.phobosClient.getRenderManager().computeText6(text, true, this.phobosClient.getRenderManager().value2());
            } catch (Throwable throwable) {
               var5 = 0.0F;
               yield var5;
            }

            yield var5;
         }
      };
   }

   public Setting<Boolean> getGlow() {
      return this.glow;
   }

   public static HudModule getHudModule() {
      return hudModule;
   }

   public Setting<Color> getColors4() {
      return this.colors4;
   }

   public Setting<Boolean> getLayout() {
      return this.layout;
   }

   public Setting<Color> getColors3() {
      return this.colors3;
   }

   public Setting<Boolean> getUI() {
      return this.uI;
   }

   public Setting<Boolean> getShadow() {
      return this.shadow;
   }

   public Setting<Integer> getSpeed() {
      return this.speed;
   }

   public Setting<HudModule.InfoStyle> getStyle() {
      return this.style;
   }

   public Setting<Color> getColor() {
      return this.color;
   }

   public Setting<Color> getColors1() {
      return this.colors1;
   }

   public Setting<Color> getColors2() {
      return this.colors2;
   }

   public Queue<Consumer<HudModule>> getConsumerQueue() {
      return this.consumerQueue;
   }

   public Setting<Boolean> getBrackets() {
      return this.brackets;
   }

   public enum Font {
      Vanilla,
      Native;
   }

   public enum InfoStyle {
      None,
      Name,
      Value;
   }

   private enum Rainbow implements TextProvider {
      None("No rainbow effect"),
      Vertical("Vertical"),
      Horizontal("Horizontal", "§+"),
      Static("Static rainbow"),
      Double("2 Mixed colors", 2),
      Triple("3 Mixed colors", 3),
      Quad("4 Mixed colors", 4);

      public final String code;
      public final int intValue;
      public final String text;

      public String getCode() {
         return this.code;
      }

      Rainbow(String description, int count) {
         this.text = description;
         this.intValue = count;
         this.code = "";
      }

      public int getIntValue() {
         return this.intValue;
      }

      @Override
      public String getText() {
         return this.text;
      }

      Rainbow(String description) {
         this.text = description;
         this.intValue = -1;
         this.code = "";
      }

      Rainbow(String description, String code) {
         this.text = description;
         this.intValue = -1;
         this.code = code;
      }
   }
}
