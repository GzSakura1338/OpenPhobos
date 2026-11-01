package org.phobos.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.phobos.api.KeyValue;
import org.phobos.module.Module;
import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.TextPairPacket286;

public class ImportHandler {
   public static final Map<String, ImportHandler.Target> textMap = new HashMap<>();
   public final PhobosClient phobosClient;

   static {
      for (ImportHandler.Target target : ImportHandler.Target.values()) {
         String text = target.name().toLowerCase();
         textMap.put(text, target);
      }
   }

   public void run() {
      this.phobosClient
         .getPacketBus()
         .handleClassValue(
            TextPairPacket286.class,
            textPairPacket286 -> this.resolveOptional(textPairPacket286.getText3())
               .ifPresent(
                  keyValue -> this.phobosClient
                     .getChatManager()
                     .handleText7(
                        "Do you want to import %s %s from %s?".formatted(keyValue.getObject(), keyValue.getObject2(), textPairPacket286.getText()),
                        () -> this.handleText(textPairPacket286.getText3())
                     )
               )
         );
   }

   public ImportHandler(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
   }

   public void handleText(String text) {
      try {
         String[] texts = text.split("##");
         String text2 = texts[0];
         switch ((ImportHandler.Target)textMap.get(text2)) {
            case Module:
               this.phobosClient.getModuleManager().handleText(text);
         }
      } catch (Throwable var4) {
      }
   }

   public Optional<KeyValue<String, String>> resolveOptional(String text) {
      try {
         String[] texts = text.split("##");
         return Optional.of(KeyValue.createValue(texts[0], texts[1]));
      } catch (Throwable throwable) {
         return Optional.empty();
      }
   }

   public Optional<KeyValue<Module, String>> resolveOptional2(String text, String text2) {
      switch ((ImportHandler.Target)textMap.get(text)) {
         case Module:
            return this.phobosClient.getModuleManager().resolveOptional(text2);
         default:
            throw new MatchException(null, null);
      }
   }

   public enum Target {
      Module;
   }
}
