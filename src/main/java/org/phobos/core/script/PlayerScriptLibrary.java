package org.phobos.core.script;

import java.util.List;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.PhobosClient;

public class PlayerScriptLibrary extends ScriptLibrary implements MinecraftHolder {
   public boolean check() {
      return mc.player == null;
   }

   public double value() {
      return EntityUtils.computeLivingEntity(mc.player);
   }

   @Override
   public String getName() {
      return mc.player.getNameForScoreboard();
   }

   public boolean testText(String text) {
      return !mc.player.getOffHandStack().isEmpty() && mc.player.getOffHandStack().getItem().getName().getString().contains(text);
   }

   public PlayerScriptLibrary(PhobosClient phobosClient, Script script) {
      super("player", phobosClient, script);
      List<ScriptMember<?>> list = ScriptMember.resolveList(this, "player", this.getClass());
      list.forEach(this::handleScriptMember);
   }

   public void run() {
      mc.doItemUse();
   }

   public boolean testText2(String text) {
      return !mc.player.getMainHandStack().isEmpty() && mc.player.getMainHandStack().getItem().getName().getString().contains(text);
   }
}
