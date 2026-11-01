package org.phobos.data;

import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.user.UuidResolver;
import org.phobos.core.user.GameTiersApi;
import org.phobos.module.client.GameTiersModule;

public record UuidRecord(
   String uuid,
   String name,
   Map<String, UuidRecord.Ranking> rankings,
   String region,
   int points,
   int overall,
   List<UuidRecord.Badge> badges,
   @SerializedName("combat_master") boolean combatMaster
) {
   public static final Map<String, Integer> textMap = Map.of(
      "NA", 16738926, "EU", 7012206, "SA", 16750848, "AU", 16167531, "ME", 16767334, "AS", 12745632, "AF", 6770343
   );

   public List<UuidRecord.NamedRanking> getList() {
      List<UuidRecord.NamedRanking> list = new ArrayList<>(
         this.rankings.entrySet().stream().map(entry -> entry.getValue().createNamedRanking(UuidResolver.resolveIdRecord2(entry.getKey()))).toList()
      );
      list.sort(
         Comparator.<UuidRecord.NamedRanking, Boolean>comparing(namedRanking -> namedRanking.ranking.retired, Boolean::compare)
            .thenComparingInt(namedRanking -> namedRanking.ranking.tier)
            .thenComparingInt(namedRanking -> namedRanking.ranking.pos)
      );
      return list;
   }

   public Optional<UuidRecord.NamedRanking> getOptional() {
      return this.rankings
         .entrySet()
         .stream()
         .filter(entry -> entry.getKey() != null)
         .min(Comparator.comparingInt(entry -> entry.getValue().value2()))
         .map(entry -> entry.getValue().createNamedRanking(UuidResolver.resolveIdRecord2(entry.getKey())));
   }

   public int value() {
      return textMap.getOrDefault(this.region.toUpperCase(Locale.ROOT), 16777215);
   }

   public static CompletableFuture<UuidRecord> resolveCompletableFuture(HttpClient httpClient, String text) {
      String text2 = GameTiersModule.getGameTiersModule().describe2() + "/search_profile/" + text;
      HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(text2)).GET().build();
      return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
         .thenApply(HttpResponse::body)
         .thenApply(textx -> (UuidRecord)GameTiersApi.gson.fromJson(textx, UuidRecord.class))
         .whenComplete((uuidRecord, throwable) -> {});
   }

   public UuidRecord.PointInfo getPointInfo() {
      if (!this.combatMaster
         && (this.points < 200 || !this.rankings.values().stream().allMatch(ranking -> ranking.tier <= 2 || ranking.peakTier != null && ranking.peakTier <= 2))
         )
       {
         if (this.points >= 100) {
            return UuidRecord.PointInfo.COMBAT_ACE;
         } else if (this.points >= 50) {
            return UuidRecord.PointInfo.COMBAT_SPECIALIST;
         } else if (this.points >= 20) {
            return UuidRecord.PointInfo.COMBAT_CADET;
         } else if (this.points >= 10) {
            return UuidRecord.PointInfo.COMBAT_NOVICE;
         } else {
            return this.points >= 1 ? UuidRecord.PointInfo.ROOKIE : UuidRecord.PointInfo.UNRANKED;
         }
      } else {
         return UuidRecord.PointInfo.COMBAT_MASTER;
      }
   }

   public static CompletableFuture<UuidRecord> resolveCompletableFuture2(HttpClient httpClient, UUID uUID) {
      String text = GameTiersModule.getGameTiersModule().describe2() + "/profile/" + uUID.toString().replace("-", "");
      HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(text)).GET().build();
      return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
         .thenApply(HttpResponse::body)
         .thenApply(textx -> (UuidRecord)GameTiersApi.gson.fromJson(textx, UuidRecord.class))
         .whenComplete((uuidRecord, throwable) -> {});
   }

   public record Badge(String title, String desc) {
   }

   public record NamedRanking(@Nullable IdRecord mode, UuidRecord.Ranking ranking) {
   }

   public enum PointInfo {
      COMBAT_MASTER("Combat Master", 16494651, 16765242),
      COMBAT_ACE("Combat Ace", 13445212, 14046324),
      COMBAT_SPECIALIST("Combat Specialist", 11368664, 13083624),
      COMBAT_CADET("Combat Cadet", 9605593, 11381986),
      COMBAT_NOVICE("Combat Novice", 9605593, 16777215),
      ROOKIE("Rookie", 7106936, 9148316),
      UNRANKED("Unranked", 16777215, 16777215);

      public final int color;
      public final int accentColor;
      public final String title;

      public int getColor() {
         return this.color;
      }

      public int getAccentColor() {
         return this.accentColor;
      }

      public String getTitle() {
         return this.title;
      }

      PointInfo(final String title, final int color, final int accentColor) {
         this.title = title;
         this.color = color;
         this.accentColor = accentColor;
      }
   }

   public record Ranking(
      int tier,
      int pos,
      @SerializedName("peak_tier") @Nullable Integer peakTier,
      @SerializedName("peak_pos") @Nullable Integer peakPos,
      long attained,
      boolean retired
   ) {

      public int value() {
         return this.peakTier != null && this.peakPos != null ? this.peakTier * 2 + this.peakPos : Integer.MAX_VALUE;
      }

      public UuidRecord.NamedRanking createNamedRanking(IdRecord idRecord) {
         return new UuidRecord.NamedRanking(idRecord, this);
      }

      public int value2() {
         return this.tier * 2 + this.pos;
      }
   }
}
