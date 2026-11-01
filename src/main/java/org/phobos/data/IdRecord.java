package org.phobos.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import org.phobos.api.Named;
import org.phobos.core.user.GameTiersApi;
import org.phobos.module.client.GameTiersModule;

public record IdRecord(String id, String title) implements Named {
   public static final IdRecord INSTANCE = new IdRecord("empty", "empty");

   public Optional<Character> getOptional() {
      Pair<Character, TextColor> pair = this.createPair();
      return ((TextColor)pair.getRight()).getRgb() == 16777215 ? Optional.empty() : Optional.of((Character)pair.getLeft());
   }

   public Text describeFlag(boolean flag) {
      Pair<Character, TextColor> pair = this.createPair();
      if (((TextColor)pair.getRight()).getRgb() == 16777215 && !flag) {
         return Text.of(this.title);
      }

      Text text = Text.literal(this.title).styled(style -> style.withColor((TextColor)pair.getRight()));
      return Text.literal(pair.getLeft() + " ").append(text);
   }

   public static CompletableFuture<List<IdRecord>> resolveCompletableFuture(HttpClient httpClient) {
      String text = GameTiersModule.getGameTiersModule().describe2() + "/tierlists";
      HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(text)).GET().build();
      return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(httpResponse -> {
         JsonObject jsonObject = (JsonObject)GameTiersApi.gson.fromJson(httpResponse.body(), JsonObject.class);
         return jsonObject.entrySet().stream().map(entry -> {
            String textx = ((JsonElement)entry.getValue()).getAsJsonObject().get("title").getAsString();
            return new IdRecord((String)entry.getKey(), textx);
         }).toList();
      });
   }

   public Pair<Character, TextColor> createPair() {
      return switch (this.id) {
         case "axe" -> new Pair('\ue701', TextColor.fromFormatting(Formatting.GREEN));
         case "mace" -> new Pair('\ue702', TextColor.fromFormatting(Formatting.GRAY));
         case "nethop", "neth_pot" -> new Pair('\ue703', TextColor.fromRgb(8211008));
         case "pot" -> new Pair('\ue704', TextColor.fromRgb(16711680));
         case "smp" -> new Pair('\ue705', TextColor.fromRgb(15518533));
         case "sword" -> new Pair('\ue706', TextColor.fromRgb(10812912));
         case "uhc" -> new Pair('\ue707', TextColor.fromFormatting(Formatting.RED));
         case "vanilla" -> new Pair('\ue708', TextColor.fromFormatting(Formatting.LIGHT_PURPLE));
         case "bed" -> new Pair('\ue801', TextColor.fromRgb(16711680));
         case "bow" -> new Pair('\ue802', TextColor.fromRgb(6700304));
         case "creeper" -> new Pair('\ue803', TextColor.fromFormatting(Formatting.GREEN));
         case "debuff" -> new Pair('\ue804', TextColor.fromFormatting(Formatting.DARK_GRAY));
         case "dia_crystal" -> new Pair('\ue805', TextColor.fromFormatting(Formatting.AQUA));
         case "dia_smp" -> new Pair('\ue806', TextColor.fromRgb(9201291));
         case "elytra" -> new Pair('\ue807', TextColor.fromRgb(9276849));
         case "manhunt" -> new Pair('\ue808', TextColor.fromFormatting(Formatting.RED));
         case "minecart" -> new Pair('\ue809', TextColor.fromFormatting(Formatting.GRAY));
         case "og_vanilla" -> new Pair('\ue810', TextColor.fromFormatting(Formatting.GOLD));
         case "speed" -> new Pair('\ue811', TextColor.fromRgb(4434385));
         case "trident" -> new Pair('\ue812', TextColor.fromRgb(5741452));
         default -> new Pair('•', TextColor.fromFormatting(Formatting.WHITE));
      };
   }

   @Override
   public String getName() {
      return this.id;
   }
}
