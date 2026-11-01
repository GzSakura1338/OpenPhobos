package org.phobos.core.player;

import java.util.HashMap;
import java.util.Map;
import org.phobos.api.PlayerTextureCache;
import org.phobos.core.util.holder.TimestampPair;

public class PlayerTextureCacheImpl implements PlayerTextureCache {
   public final Map<String, TimestampPair> textMap = new HashMap<>();

   @Override
   public void run() {
   }

   @Override
   public Map<String, TimestampPair> getTextMap() {
      return this.textMap;
   }
}
