package org.phobos.data;

import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.user.GameTiersApi;
import org.phobos.module.client.GameTiersModule;

public record PlayersRecord(List<UuidRecord> players, List<UUID> unknown, @SerializedName("fetch_unknown") @Nullable Boolean fetchUnknown) {

   public static CompletableFuture<PlayersRecord> resolveCompletableFuture(HttpClient httpClient) {
      String text = GameTiersModule.getGameTiersModule().describe2() + "/all";
      HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(text)).GET().build();
      return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
         .thenApply(HttpResponse::body)
         .thenApply(textx -> (PlayersRecord)GameTiersApi.gson.fromJson(textx, PlayersRecord.class))
         .whenComplete((playersRecord, throwable) -> {});
   }
}
