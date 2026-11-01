package org.phobos.core.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.phobos.data.IdRecord;
import org.phobos.data.PlayersRecord;
import org.phobos.data.UuidRecord;

public class UuidResolver {
   public static final List<IdRecord> idRecordList = new ArrayList<>();
   public static final Map<UUID, Optional<UuidRecord>> uUIDMap = new ConcurrentHashMap<>();
   public static final AtomicBoolean atomicBoolean = new AtomicBoolean(true);

   public static CompletableFuture<UuidRecord> resolveCompletableFuture(String text) {
      return UuidRecord.resolveCompletableFuture(GameTiersApi.getHttpClient(), text).thenApply(uuidRecord -> {
         UUID uUID = createUUID(uuidRecord.uuid());
         uUIDMap.put(uUID, Optional.of(uuidRecord));
         return (UuidRecord)uuidRecord;
      });
   }

   public static IdRecord resolveIdRecord(IdRecord idRecord) {
      return idRecordList.get((idRecordList.indexOf(idRecord) + 1) % idRecordList.size());
   }

   public static Optional<UuidRecord> resolveOptional(UUID uUID) {
      return atomicBoolean.get() ? uUIDMap.computeIfAbsent(uUID, uUID2 -> {
         if (uUID.version() == 4) {
            UuidRecord.resolveCompletableFuture2(GameTiersApi.getHttpClient(), uUID).thenAccept(uuidRecord -> uUIDMap.put(uUID, Optional.ofNullable(uuidRecord)));
         }

         return Optional.empty();
      }) : uUIDMap.getOrDefault(uUID, Optional.empty());
   }

   public static void run() {
      PlayersRecord.resolveCompletableFuture(GameTiersApi.getHttpClient())
         .thenAccept(
            playersRecord -> {
               Map<UUID, Optional<UuidRecord>> map = playersRecord.players()
                  .stream()
                  .collect(Collectors.toMap(uuidRecord -> createUUID(uuidRecord.uuid()), Optional::of));
               Map<UUID, Optional<UuidRecord>> map2 = playersRecord.unknown().stream().collect(Collectors.toMap(uUID -> (UUID)uUID, uUID -> Optional.empty()));
               uUIDMap.clear();
               uUIDMap.putAll(map);
               uUIDMap.putAll(map2);
               if (playersRecord.fetchUnknown() != null) {
                  atomicBoolean.set(playersRecord.fetchUnknown());
               }
            }
         );

      try {
         idRecordList.clear();
         idRecordList.addAll(IdRecord.resolveCompletableFuture(GameTiersApi.getHttpClient()).get());
      } catch (ExecutionException var1) {
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
      }
   }

   public static IdRecord resolveIdRecord2(String text) {
      return idRecordList.stream().filter(idRecord -> idRecord.id().equalsIgnoreCase(text)).findFirst().orElse(IdRecord.INSTANCE);
   }

   public static UUID createUUID(String text) {
      try {
         return UUID.fromString(text);
      } catch (Exception exception) {
         long l = Long.parseUnsignedLong(text.substring(0, 16), 16);
         long l2 = Long.parseUnsignedLong(text.substring(16), 16);
         return new UUID(l, l2);
      }
   }

   static {
      idRecordList.add(IdRecord.INSTANCE);
   }

   public static void run2() {
      uUIDMap.clear();
   }
}
