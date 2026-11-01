package org.phobos.core.hud;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Named;
import org.phobos.api.GuiGraphics;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.JsonSerializable;
import org.phobos.module.client.HudModule;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.core.hud.element.AntiCheatHudElement;
import org.phobos.core.hud.element.ArmorHudElement;
import org.phobos.core.hud.element.ArrayListHudElement;
import org.phobos.core.hud.element.ArrowsHudElement;
import org.phobos.core.ClientHolder;
import org.phobos.core.hud.element.ClockHudElement;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.hud.element.ConfigPresetHudElement;
import org.phobos.core.hud.element.CoordsHudElement;
import org.phobos.core.hud.element.CpsHudElement;
import org.phobos.core.hud.element.CrystalsHudElement;
import org.phobos.core.hud.element.CustomTextHudElement;
import org.phobos.core.hud.element.DirectionHudElement;
import org.phobos.core.Feature;
import org.phobos.core.hud.element.FpsHudElement;
import org.phobos.core.hud.element.GapsHudElement;
import org.phobos.core.gui.screen.HudScreen;
import org.phobos.core.hud.element.ImageHudElement;
import org.phobos.core.hud.element.KillfeedHudElement;
import org.phobos.core.hud.element.LagMeterHudElement;
import org.phobos.core.hud.element.MemoryHudElement;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.hud.element.NotificationsHudElement;
import org.phobos.core.system.OsDetector;
import org.phobos.core.hud.element.PacketsHudElement;
import org.phobos.core.hud.element.PearlsHudElement;
import org.phobos.core.PhobosClient;
import org.phobos.core.hud.element.PingHudElement;
import org.phobos.core.hud.element.PlayerListHudElement;
import org.phobos.core.hud.element.PotionsHudElement;
import org.phobos.core.gui.ScreenDrawContext;
import org.phobos.core.hud.element.ServerBrandHudElement;
import org.phobos.core.hud.element.SpeedHudElement;
import org.phobos.core.hud.element.SpotifyHudElement;
import org.phobos.core.hud.element.TextRadarHudElement;
import org.phobos.core.hud.element.TotemsHudElement;
import org.phobos.core.hud.element.TpsHudElement;
import org.phobos.core.hud.element.WatermarkHudElement;
import org.phobos.core.hud.element.WelcomerHudElement;
import org.phobos.core.hud.element.XpHudElement;

public class HudManager extends Feature implements Named, JsonSerializable, MinecraftHolder {
   public final PhobosClient phobosClient;
   public final HudLayoutManager hudLayoutManager;
   public static HudManager hudManager;
   public final List<HudElement> hudElementList = new ArrayList<>();
   public final GuiGraphics guiGraphics = new ScreenDrawContext();
   private static Object[] phobos$g1806d45b8;

   public void handleConsumer(Consumer<DrawContext> consumer) {
      ClientHolder.getPhobosClient().getRenderManager().handleConsumer(consumer);
   }

   public GuiGraphics getGuiGraphics() {
      return this.guiGraphics;
   }

   @Override
   public String getName() {
      return "hud.json";
   }

   public static HudManager getHudManager() {
      return hudManager;
   }

   public void handleHudElement(HudElement hudElement) {
      List var2 = this.hudElementList;
      boolean var3 = var2.contains(hudElement);
      if (!var3) {
         List var4 = this.hudElementList;

         try {
            var4.add(hudElement);
         } catch (Throwable var8) {
            boolean var6 = true;
            if (!var6) {
               throw var8;
            }

            PrintStream var7 = System.err;
            var8.printStackTrace(var7);
         }
      }
   }

   public void run5() {
      WatermarkHudElement var1 = new WatermarkHudElement();
      this.handleHudElement(var1);
      FpsHudElement var2 = new FpsHudElement();
      this.handleHudElement(var2);
      TpsHudElement var3 = new TpsHudElement();
      this.handleHudElement(var3);
      PingHudElement var4 = new PingHudElement();
      this.handleHudElement(var4);
      SpeedHudElement var5 = new SpeedHudElement();
      this.handleHudElement(var5);
      ArrayListHudElement var6 = new ArrayListHudElement();
      this.handleHudElement(var6);
      PotionsHudElement var7 = new PotionsHudElement();
      this.handleHudElement(var7);
      ArmorHudElement var8 = new ArmorHudElement();
      this.handleHudElement(var8);
      ServerBrandHudElement var9 = new ServerBrandHudElement();
      this.handleHudElement(var9);
      CoordsHudElement var10 = new CoordsHudElement();
      this.handleHudElement(var10);
      TotemsHudElement var11 = new TotemsHudElement();
      this.handleHudElement(var11);
      CrystalsHudElement var12 = new CrystalsHudElement();
      this.handleHudElement(var12);
      XpHudElement var13 = new XpHudElement();
      this.handleHudElement(var13);
      PearlsHudElement var14 = new PearlsHudElement();
      this.handleHudElement(var14);
      AntiCheatHudElement var15 = new AntiCheatHudElement();
      this.handleHudElement(var15);
      MemoryHudElement var16 = new MemoryHudElement();
      this.handleHudElement(var16);
      WelcomerHudElement var17 = new WelcomerHudElement();
      this.handleHudElement(var17);
      ClockHudElement var18 = new ClockHudElement();
      this.handleHudElement(var18);
      PlayerListHudElement var19 = new PlayerListHudElement();
      this.handleHudElement(var19);
      GapsHudElement var20 = new GapsHudElement();
      this.handleHudElement(var20);
      KillfeedHudElement var21 = new KillfeedHudElement();
      this.handleHudElement(var21);
      ArrowsHudElement var22 = new ArrowsHudElement();
      this.handleHudElement(var22);
      ConfigPresetHudElement var23 = new ConfigPresetHudElement();
      this.handleHudElement(var23);
      CpsHudElement var24 = new CpsHudElement();
      this.handleHudElement(var24);
      TextRadarHudElement var25 = new TextRadarHudElement();
      this.handleHudElement(var25);
      NotificationsHudElement var26 = new NotificationsHudElement();
      this.handleHudElement(var26);
      DirectionHudElement var27 = new DirectionHudElement();
      this.handleHudElement(var27);
      PacketsHudElement var28 = new PacketsHudElement();
      this.handleHudElement(var28);
      LagMeterHudElement var29 = new LagMeterHudElement();
      this.handleHudElement(var29);
      ImageHudElement var30 = new ImageHudElement();
      this.handleHudElement(var30);
      CustomTextHudElement var31 = new CustomTextHudElement();
      this.handleHudElement(var31);
      OsDetector var32 = OsDetector.getINSTANCE();
      boolean var33 = var32.check();
      if (var33) {
         SpotifyHudElement var34 = new SpotifyHudElement();
         this.handleHudElement(var34);
      }

      List var35 = this.hudElementList;
      Function<HudElement, String> var52 = HudElement::getName;
      Comparator var53 = Comparator.comparing(var52);
      var35.sort(var53);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      if (ConfigManager.getConfigSectionFilter().testBINDSMODULESEnum(BINDSMODULESEnum.HUD)) {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         String text = jsonObject.get("Mainframe").getAsString();
         String[] texts = text.split("&");
         HudScreen.hudScreen.handleFloat2(Float.parseFloat(texts[0]), Float.parseFloat(texts[1]));

         for (HudElement hudElement : this.hudElementList) {
            JsonElement jsonElement2 = jsonObject.get(hudElement.getName());
            if (jsonElement2 == null) {
               hudElement.handleFlag2(true);
               hudElement.run6();
            } else {
               hudElement.handleJsonElement(jsonElement2);
            }
         }
      }
   }

   @Nullable
   public static HudElement resolveHudElement(String text) {
      for (HudElement hudElement : hudManager.hudElementList) {
         if (hudElement.getName().equals(text)) {
            return hudElement;
         }
      }

      return null;
   }

   public List<HudElement> getColorPickerList() {
      return this.hudElementList;
   }

   public HudManager(PhobosClient phobosClient) {
      this.hudLayoutManager = new HudLayoutManager();
      this.phobosClient = phobosClient;
      hudManager = this;
      this.run5();
      HudScreen.hudScreen = new HudScreen(this);

      for (HudElement hudElement : this.hudElementList) {
         try {
            hudElement.run7();
         } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
         }
      }
   }

   public void handleHudElement2(HudElement hudElement) {
      this.hudElementList.remove(hudElement);
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public void handleDouble(double d) {
      if (HudModule.getHudModule().check7()) {
         double d2 = MenuNavigation.value();
         double d3 = MenuNavigation.value2();

         for (HudElement hudElement : this.hudElementList) {
            if (hudElement.testDouble(d2, d3)) {
               hudElement.handleDouble2(d2, d3, d);
            }
         }
      }
   }

   public HudLayoutManager getHudLayoutManager() {
      return this.hudLayoutManager;
   }

   public void handleDrawContext(DrawContext drawContext) {
      this.guiGraphics.setDrawContext(drawContext);
      this.hudLayoutManager.handleDrawContext4(drawContext);
      this.hudLayoutManager.run();

      for (HudElement hudElement : this.hudElementList) {
         if (!hudElement.isFlag3()) {
            hudElement.handleDrawContext3(drawContext, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
         }
      }
   }

   public List<HudElement> getHudElementList() {
      return this.hudElementList;
   }

   public Optional<HudElement> resolveOptional3(String text) {
      for (HudElement hudElement : this.hudElementList) {
         if (hudElement.getName().equalsIgnoreCase(text)) {
            return Optional.of(hudElement);
         }
      }

      return Optional.empty();
   }

   @Nullable
   public HudElement resolveHudElement2(String text) {
      for (HudElement hudElement : this.hudElementList) {
         if (hudElement.describe3().startsWith(text)) {
            return hudElement;
         }
      }

      return null;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();
      float[] floats = HudScreen.hudScreen.values();
      jsonObject.add("Mainframe", new JsonPrimitive(floats[0] + "&" + floats[1]));

      for (HudElement hudElement : this.hudElementList) {
         jsonObject.add(hudElement.getName(), hudElement.createJsonPrimitive());
      }

      return jsonObject;
   }
}
