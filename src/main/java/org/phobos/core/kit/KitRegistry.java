package org.phobos.core.kit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.JsonSupport;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.util.collection.ListeningNamedRegistry;

public class KitRegistry extends ListeningNamedRegistry<Kit> implements JsonSupport {
   public static final Path path = Path.of(System.getProperty("user.home"), "earthhack", "kits");
   public static final KitRegistry INSTANCE = new KitRegistry();
   public final MutableObject<Kit> kitMutableObject = new MutableObject(null);
   public final MutableBoolean mutableBoolean = new MutableBoolean(false);

   public void handleInt(int i) {
      if (!this.mutableBoolean.isTrue()) {
         this.mutableBoolean.setTrue();
         if (this.kitMutableObject.getValue() != null && !((Kit)this.kitMutableObject.getValue()).isEmpty() && mc.player != null) {
            for (Map.Entry<Integer, Item> entry : ((Kit)this.kitMutableObject.getValue()).getBlockPosMap().entrySet()) {
               try {
                  Slot slot = mc.player.currentScreenHandler.getSlot(i + (entry.getKey() - 9));
                  Item item = entry.getValue();
                  if (!(item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BlockWithEntity)) {
                     GhostItems.handleObject2(this, slot, item, 0.5F);
                  }
               } catch (Throwable var7) {
               }
            }
         }
      }
   }

   public static void stop() {
      path.toFile().mkdirs();

      for (Kit kit : getINSTANCE()) {
         try {
            Files.writeString(path.resolve(kit.getName() + ".json"), kit.describeGson(gson));
         } catch (Throwable var3) {
         }
      }
   }

   public static void run7() {
      File[] files = path.toFile().listFiles();
      if (files != null) {
         KitRegistry kitRegistry = getINSTANCE();

         for (File file : files) {
            String[] texts = file.getName().split("\\.");
            String text2 = texts.length > 0 ? texts[texts.length - 2] : file.getName();

            try {
               String text = Files.readString(file.toPath());
               Kit.resolveOptional(text2, text).ifPresent(kitRegistry::testNamed4);
            } catch (Throwable var9) {
            }
         }

         String text3 = ConfigManager.getCloudSettings().kit.getValue();
         if (!text3.equalsIgnoreCase("none")) {
            kitRegistry.handleText(text3);
         }
      }
   }

   public void handleKit(Kit kit) {
      path.resolve(kit.getName() + ".json").toFile().delete();
      this.testNamed3(kit);
   }

   public static KitRegistry getINSTANCE() {
      return INSTANCE;
   }

   public void run() {
      if (!this.mutableBoolean.isFalse()) {
         GhostItems.handleObject(this);
         this.mutableBoolean.setFalse();
      }
   }

   public MutableObject<Kit> getKitMutableObject() {
      return this.kitMutableObject;
   }

   public MutableBoolean getMutableBoolean() {
      return this.mutableBoolean;
   }

   public void handleText(String text) {
      this.resolveOptional2(text).ifPresent(this.kitMutableObject::setValue);
   }

   public void handleKit2(Kit kit) {
      this.kitMutableObject.setValue(kit);
   }
}
