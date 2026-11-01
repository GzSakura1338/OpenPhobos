package org.phobos.core.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.JsonSerializable;
import org.phobos.api.StreamableIterable;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.type.NormalHoldEnum;
import org.phobos.core.ClientHolder;
import org.phobos.core.util.NullSafe;

public class KeyCombination implements JsonSerializable, StreamableIterable<BindKey> {
   public NormalHoldEnum normalHoldEnum = NormalHoldEnum.Normal;
   public final Set<BindKey> bindKeySet;

   public void setNormalHoldEnum(NormalHoldEnum normalHoldEnum) {
      this.normalHoldEnum = normalHoldEnum;
   }

   public NormalHoldEnum getNormalHoldEnum() {
      return this.normalHoldEnum;
   }

   public boolean isEmpty() {
      return this.bindKeySet.isEmpty();
   }

   public KeyCombination(NormalHoldEnum normalHoldEnum, BindKey... bindKeys) {
      this(new HashSet<>(Arrays.stream(bindKeys).toList()));
      this.normalHoldEnum = normalHoldEnum;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      }

      if (object instanceof KeyCombination keyCombination) {
         if (!keyCombination.testObject(this)) {
            return false;
         }

         Object object2 = this.getBindKeySet();
         Object object3 = keyCombination.getBindKeySet();
         return Objects.equals(object2, object3);
      } else {
         return false;
      }
   }

   public KeyCombination(Set<BindKey> set) {
      this.bindKeySet = set;
   }

   public static KeyCombination createKeyCombination() {
      return new KeyCombination();
   }

   public KeyCombination(BindKey... bindKeys) {
      this(new HashSet<>(Arrays.stream(bindKeys).toList()));
   }

   @Override
   public Stream<BindKey> stream() {
      return this.bindKeySet.stream();
   }

   @Override
   public String toString() {
      return this.getBindKeySet().isEmpty() ? "None" : this.getBindKeySet().stream().map(BindKey::getName).collect(Collectors.joining(" / "));
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.bindKeySet.clear();

      for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
         JsonObject jsonObject = jsonElement2.getAsJsonObject();
         NullSafe.handleObject(jsonObject.get("Toggle"), jsonElementx -> this.setNormalHoldEnum(NormalHoldEnum.fromString(jsonElementx.getAsString())));
         NullSafe.handleObject(jsonObject.get("Type"), jsonElementx -> {
            KeyboardMouseEnum keyboardMouseEnum = KeyboardMouseEnum.fromString(jsonElementx.getAsString());
            NullSafe.handleObject(jsonObject.get("Key"), jsonElementxx -> {
               int i = jsonElementxx.getAsInt();
               NullSafe.handleObject(ClientHolder.getPhobosClient().getBindManager().resolveBindKey(keyboardMouseEnum, i), this.bindKeySet::add);
            });
         });
      }
   }

   public KeyCombination createKeyCombination2(BindKey... bindKeys) {
      return new KeyCombination(this.getNormalHoldEnum(), bindKeys);
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      Object object = this.getBindKeySet();
      return i2 * 59 + (object == null ? 43 : object.hashCode());
   }

   public Set<BindKey> getBindKeySet() {
      return this.bindKeySet;
   }

   public boolean testObject(Object object) {
      return object instanceof KeyCombination;
   }

   @NotNull
   @Override
   public Iterator<BindKey> iterator() {
      return this.bindKeySet.iterator();
   }

   public boolean testBindKey(BindKey bindKey) {
      return this.bindKeySet.contains(bindKey);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonArray jsonArray = new JsonArray(this.bindKeySet.size());

      for (BindKey bindKey : this.bindKeySet) {
         try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Type", bindKey.getKeyboardMouseEnum().toString());
            jsonObject.addProperty("Toggle", NullSafe.resolveObject2(this.normalHoldEnum.toString(), NormalHoldEnum.Normal::toString));
            jsonObject.addProperty("Key", bindKey.getIntValue());
            jsonArray.add(jsonObject);
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }

      return jsonArray;
   }
}
