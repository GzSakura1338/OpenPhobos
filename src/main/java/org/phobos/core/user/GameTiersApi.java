package org.phobos.core.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.MinecraftHolder;
import org.phobos.data.IdRecord;
import org.phobos.data.UuidRecord;
import org.phobos.data.UuidRecord;
import org.phobos.data.UuidRecord;
import org.phobos.module.client.GameTiersModule;
import org.phobos.module.client.GameTiersModule;

public class GameTiersApi implements MinecraftHolder {
   public static final Gson gson = new GsonBuilder().create();
   public static final HttpClient httpClient = HttpClient.newHttpClient();

   @NotNull
   public static String describeRanking(UuidRecord.Ranking ranking) {
      return GameTiersModule.getGameTiersModule().getRetired().getValue() && ranking.retired() && ranking.peakTier() != null && ranking.peakPos() != null
         ? "R" + (ranking.peakPos() == 0 ? "H" : "L") + "T" + ranking.peakTier()
         : (ranking.pos() == 0 ? "H" : "L") + "T" + ranking.tier();
   }

   public static HttpClient getHttpClient() {
      return httpClient;
   }

   public static Optional<UuidRecord.NamedRanking> resolveOptional(UUID uUID) {
      IdRecord idRecord = UuidResolver.resolveIdRecord2("vanilla");
      return UuidResolver.resolveOptional(uUID).map(uuidRecord -> {
         if (uuidRecord.rankings() == null) {
            return null;
         }

         UuidRecord.Ranking ranking = uuidRecord.rankings().get(idRecord.id());
         Optional<UuidRecord.NamedRanking> optional = uuidRecord.getOptional();
         return ranking == null ? optional.orElse(null) : ranking.createNamedRanking(idRecord);
      });
   }

   public static Text describePlayerEntity(PlayerEntity playerEntity, Text text) {
      if (playerEntity instanceof ClientPlayerEntity && GameTiersModule.getGameTiersModule().getOverride().getValue()) {
         String text2 = GameTiersModule.getGameTiersModule().describe();
         MutableText mutableText = Text.literal(text2).withColor(computeText(text2));
         IdRecord idRecord = resolveIdRecord("vanilla");
         if (GameTiersModule.getGameTiersModule().getIcons().getValue() && idRecord != null && idRecord.getOptional().isPresent()) {
            Text.literal(idRecord.getOptional().get().toString()).append(mutableText).append(Text.literal(" | ").formatted(Formatting.GRAY));
            return mutableText.append(text);
         } else {
            mutableText.append(Text.literal(" | ").formatted(Formatting.GRAY));
            return mutableText.append(text);
         }
      } else if (!GameTiersModule.getGameTiersModule().check() && !mc.isInSingleplayer()) {
         MutableText mutableText2 = switch ((GameTiersModule.Statistic)GameTiersModule.getGameTiersModule().getDisplay().getValue()) {
            case Tier -> (MutableText)resolveOptional(playerEntity.getUuid())
               .map(
                  namedRanking -> {
                     String textx = describeRanking(namedRanking.ranking());
                     MutableText mutableTextx = Text.literal(textx).withColor(computeText(textx));
                     return GameTiersModule.getGameTiersModule().getIcons().getValue()
                           && namedRanking.mode() != null
                           && namedRanking.mode().getOptional().isPresent()
                        ? Text.literal(namedRanking.mode().getOptional().get().toString()).append(mutableTextx)
                        : mutableTextx;
                  }
               )
               .orElse(null);
            case Rank -> (MutableText)UuidResolver.resolveOptional(playerEntity.getUuid()).map(uuidRecord -> Text.literal("#" + uuidRecord.overall())).orElse(null);
         };
         if (mutableText2 != null) {
            mutableText2.append(Text.literal(" | ").formatted(Formatting.GRAY));
            return mutableText2.append(text);
         } else {
            return text;
         }
      } else {
         return text;
      }
   }

   public static int computeText(String text) {
      return text.startsWith("R") ? 10671871 : (Integer)GameTiersModule.getGameTiersModule().getTextLinkedTreeMap().getOrDefault(text, 13882323);
   }

   public static IdRecord resolveIdRecord(String text) {
      return UuidResolver.resolveIdRecord2(text);
   }

   public static Text describeUuidRecord(UuidRecord uuidRecord) {
      MutableText mutableText = Text.empty().append("=== Rankings for " + uuidRecord.name() + " ===");
      uuidRecord.rankings().forEach((text, ranking) -> {
         if (text != null) {
            IdRecord idRecord = UuidResolver.resolveIdRecord2(text);
            String text2 = describeRanking(ranking);
            Text text3 = Text.literal(text2).styled(style -> style.withColor(computeText(text2)));
            mutableText.append(Text.literal("\n").append(idRecord.describeFlag(true)).append(": ").append(text3));
         }
      });
      return mutableText;
   }
}
