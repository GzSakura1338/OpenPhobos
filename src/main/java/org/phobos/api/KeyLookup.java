package org.phobos.api;

import org.phobos.core.bind.BindKey;
import org.phobos.core.bind.KeyCombination;
import org.phobos.type.KeyboardMouseEnum;

public interface KeyLookup extends StreamableIterable<BindKey> {
   BindKey resolveBindKey(KeyboardMouseEnum var1, int var2);

   default boolean testKeyCombination(KeyCombination keyCombination) {
      return keyCombination.stream().anyMatch(bindKey -> this.testKeyboardMouseEnum(bindKey.getKeyboardMouseEnum(), bindKey.getIntValue()));
   }

   BindKey resolveBindKey2(String var1);

   boolean testKeyboardMouseEnum(KeyboardMouseEnum var1, int var2);
}
