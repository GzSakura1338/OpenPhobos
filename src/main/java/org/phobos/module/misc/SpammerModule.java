package org.phobos.module.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.loader.api.FabricLoader;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SpammerTickListener;
import org.phobos.module.Module;

public class SpammerModule extends Module {
   public final TimeGate.Normal normal;
   public final Setting<Long> delay;
   public final Setting<String> text;
   public final Setting<Boolean> refresh;
   public final Setting<Boolean> antiKick;
   public final Setting<Boolean> random;
   public final Setting<SpammerModule.Mode> mode = new EnumSetting<>("Mode", SpammerModule.Mode.Text).resolveSetting6(this);
   public final List<String> textList;
   public int intValue;

   @Override
   public void run3() {
      if (this.textList.isEmpty() && this.mode.getValue().equals(SpammerModule.Mode.File)) {
         this.handleText6("Spammer file is empty!");
         this.run11();
      }
   }

   public void run() {
      try {
         this.phobosClient.handleText5("Spammer", "Loading spammer file...");
         long l = System.currentTimeMillis();
         File file = FabricLoader.getInstance().getGameDir().resolve("phoboslite").resolve("spammer.txt").toFile();
         if (!file.exists()) {
            file.createNewFile();
         }

         Thread thread = new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
               List<String> list = new ArrayList<>();

               String text2;
               while ((text2 = bufferedReader.readLine()) != null) {
                  list.add(text2);
               }

               List<String> list2 = new ArrayList<>();

               for (String text : list) {
                  if (!text.isEmpty()) {
                     list2.add(text.trim());
                  }
               }

               this.textList.clear();
               this.textList.addAll(list2);
               long l2 = System.currentTimeMillis();
               this.phobosClient.handleText5("Spammer", "Loaded spammer file in " + (l2 - l) + "ms.");
            } catch (Throwable throwablex) {
               throwablex.printStackTrace();
            }
         });
         thread.start();
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }
   }

   public SpammerModule(PhobosClient phobosClient) {
      super(phobosClient, "Spammer", Categories.misc, "Automatically sends chat messages");
      this.text = new StringSetting("Text", "text").resolveSetting3(this.resolveVisibility2(this.mode, SpammerModule.Mode.Text)).resolveSetting6(this);
      this.random = new BooleanSetting("Random").resolveSetting3(this.resolveVisibility2(this.mode, SpammerModule.Mode.File)).resolveSetting6(this);
      this.antiKick = new BooleanSetting("AntiKick").resolveSetting7("Appends suffix to prevent kicks").resolveSetting6(this);
      this.delay = new LongNumberSetting("Delay", 1500L, 0L, 60000L, true).resolveSetting6(this);
      this.refresh = new BooleanSetting("Refresh")
         .resolveSetting3(this.resolveVisibility2(this.mode, SpammerModule.Mode.File))
         .resolveSetting7("Refreshes the spammer file")
         .resolveSetting6(this);
      this.normal = new TimeGate.Normal();
      this.textList = new ArrayList<>();
      this.handleListener(new SpammerTickListener(this));
      this.text.handleConsumer2(text -> {
         if (text.isEmpty()) {
            this.handleText6("Text should contain at least 1 letter!");
            this.text.setValue(this.text.getObject());
         }
      });
      this.refresh.handleConsumer2(booleanValue -> {
         if (booleanValue) {
            this.run();
            this.refresh.setValue(false);
         }
      });
      this.handleTriConsumer2((clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> this.intValue = 0);
      this.run();
   }

   public String describe() {
      Random random = new Random();
      String text = "[";
      text = text + (char)(random.nextInt(26) + 97);
      text = text + random.nextInt(10);
      text = text + (char)(random.nextInt(26) + 97);
      return text + "]";
   }

   public enum Mode {
      Text,
      File;
   }
}
