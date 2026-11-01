package org.phobos.core.player;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.util.Identifier;
import org.earthhack.ducks.IPlayerListEntry;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.TextPairPacket235;
import org.phobos.core.net.packet.TextTriplePacket319;

public class SkinManager extends Feature {
   public static final Map<String, Identifier> textMap = new HashMap<>();
   public final Map<String, Identifier> textMap2;
   public final Queue<TextPairPacket235> textPairPacket235Queue = new ConcurrentLinkedQueue<>();
   public static final Map<String, SkinManager.SkinBase> textMap3 = new HashMap<>();
   public final Queue<TextTriplePacket319> textTriplePacket319Queue = new ConcurrentLinkedQueue<>();

   public static void handleText(String text, Identifier identifier, String text2, Model model) {
      textMap3.put(text, new SkinManager.SkinBase(identifier, text2, model));
   }

   public NativeImageBackedTexture resolveNativeImageBackedTexture(InputStream inputStream, boolean flag) {
      NativeImage nativeImage = null;

      try {
         nativeImage = NativeImage.read(inputStream);
      } catch (IOException iOException) {
         iOException.printStackTrace();
      }

      return nativeImage != null ? new NativeImageBackedTexture(flag ? this.resolveNativeImage(nativeImage) : nativeImage) : null;
   }

   public void handleText2(String text, String text2, boolean flag, Consumer<Identifier> consumer) {
      if (this.testText(text)) {
         NativeImageBackedTexture nativeImageBackedTexture = this.resolveNativeImageBackedTexture2(text, flag);
         String text3 = "phobos-" + text2 + "-" + this.describeText(text);

         try {
            Identifier identifier = mc.getTextureManager().registerDynamicTexture(text3, nativeImageBackedTexture);
            consumer.accept(identifier);
            this.textMap2.put(text, identifier);
         } catch (Throwable var8) {
         }
      }
   }

   public static SkinManager.SkinBase resolveSkinBase(String text) {
      return textMap3.get(text);
   }

   public static void handleText3(String text, Identifier identifier) {
      textMap.put(text, identifier);
   }

   public NativeImage resolveNativeImage(NativeImage nativeImage) {
      int i4 = 64;
      int i5 = 32;
      int i6 = nativeImage.getWidth();
      int i7 = nativeImage.getHeight();

      for (int i = nativeImage.getHeight(); i4 < i6 || i5 < i; i5 *= 2) {
         i4 *= 2;
      }

      NativeImage nativeImage2 = new NativeImage(i4, i5, true);

      for (int i3 = 0; i3 < i6; i3++) {
         for (int i2 = 0; i2 < i7; i2++) {
            nativeImage2.setColor(i3, i2, nativeImage.getColor(i3, i2));
         }
      }

      nativeImage.close();
      return nativeImage2;
   }

   public String describeText(String text) {
      StringBuilder stringBuilder = new StringBuilder();

      for (char c : text.toLowerCase().toCharArray()) {
         if (Identifier.isPathCharacterValid(c)) {
            stringBuilder.append(c);
         }
      }

      return stringBuilder.toString();
   }

   public boolean testText(String text) {
      return text.endsWith(".png") || text.endsWith(".jpg") || text.endsWith(".jpeg") || text.endsWith(".webp");
   }

   public SkinManager(PhobosClient phobosClient) {
      this.textMap2 = new HashMap<>();
      phobosClient.getPacketBus().handleClassValue(TextPairPacket235.class, this.textPairPacket235Queue::add);
      phobosClient.getPacketBus().handleClassValue(TextTriplePacket319.class, this.textTriplePacket319Queue::add);
      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            if (MinecraftHolder.mc.getNetworkHandler() != null) {
               SkinManager.this.textPairPacket235Queue.forEach(SkinManager.this::handleTextPairPacket235);
               SkinManager.this.textTriplePacket319Queue.forEach(SkinManager.this::handleTextTriplePacket319);
               SkinManager.this.textPairPacket235Queue.clear();
               SkinManager.this.textTriplePacket319Queue.clear();
            }
         }
      });
   }

   public static Identifier resolveIdentifier(String text) {
      return textMap.get(text);
   }

   public void handleTextPairPacket235(TextPairPacket235 textPairPacket235) {
      assert mc.getNetworkHandler() != null;

      for (PlayerListEntry playerListEntry : mc.getNetworkHandler().getPlayerList()) {
         String text = playerListEntry.getProfile().getName().startsWith("§")
            ? playerListEntry.getProfile().getName().substring(2)
            : playerListEntry.getProfile().getName();
         if (text.equalsIgnoreCase(textPairPacket235.getText2())) {
            if (this.textMap2.containsKey(textPairPacket235.getText())) {
               ((IPlayerListEntry)(Object)playerListEntry).setCape(this.textMap2.get(textPairPacket235.getText()));
               return;
            }

            this.handleText2(textPairPacket235.getText(), "cape", true, identifier -> ((IPlayerListEntry)(Object)playerListEntry).setCape(identifier));
            return;
         }
      }

      if (this.textMap2.containsKey(textPairPacket235.getText())) {
         handleText3(textPairPacket235.getText2(), this.textMap2.get(textPairPacket235.getText()));
      } else {
         this.handleText2(textPairPacket235.getText(), "cape", true, identifier -> handleText3(textPairPacket235.getText2(), identifier));
      }
   }

   public NativeImageBackedTexture resolveNativeImageBackedTexture2(String text, boolean flag) {
      try {
         URL uRL = new URL(text);
         return this.resolveNativeImageBackedTexture(uRL.openStream(), flag);
      } catch (IOException iOException) {
         return null;
      }
   }

   public void handleTextTriplePacket319(TextTriplePacket319 textTriplePacket319) {
      assert mc.getNetworkHandler() != null;

      for (PlayerListEntry playerListEntry : mc.getNetworkHandler().getPlayerList()) {
         if (playerListEntry.getProfile().getName().equalsIgnoreCase(textTriplePacket319.getText2())) {
            if (this.textMap2.containsKey(textTriplePacket319.getText())) {
               ((IPlayerListEntry)(Object)playerListEntry)
                  .setSkin(this.textMap2.get(textTriplePacket319.getText()), textTriplePacket319.getText(), Model.fromName(textTriplePacket319.getText22()));
               return;
            }

            this.handleText2(
               textTriplePacket319.getText(),
               "skin",
               false,
               identifier -> ((IPlayerListEntry)(Object)playerListEntry).setSkin(identifier, textTriplePacket319.getText(), Model.fromName(textTriplePacket319.getText22()))
            );
            return;
         }
      }

      if (this.textMap2.containsKey(textTriplePacket319.getText())) {
         handleText(textTriplePacket319.getText2(), this.textMap2.get(textTriplePacket319.getText()), textTriplePacket319.getText(), Model.fromName(textTriplePacket319.getText22()));
      } else {
         this.handleText2(
            textTriplePacket319.getText(),
            "skin",
            false,
            identifier -> handleText(textTriplePacket319.getText2(), identifier, textTriplePacket319.getText(), Model.fromName(textTriplePacket319.getText22()))
         );
      }
   }

   public static class SkinBase {
      public final Model model;
      public final String text;
      public final Identifier identifier;

      public String getText() {
         return this.text;
      }

      public Identifier getIdentifier() {
         return this.identifier;
      }

      public SkinBase(Identifier identifier, String text, Model model) {
         this.identifier = identifier;
         this.text = text;
         this.model = model;
      }

      public Model getModel() {
         return this.model;
      }
   }
}
