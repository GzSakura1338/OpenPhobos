package org.phobos.mixin.net;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Scoreboard.class)
public abstract class MixinScoreboard {
   @Shadow
   @Final
   private Object2ObjectMap<String, Team> teamsByScoreHolder;

   @Redirect(method = "addTeam", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false))
   private void addTeamHook(Logger instance, String string, Object object) {
   }

   @Overwrite
   @SuppressWarnings("overwrite")
   public void removeScoreHolderFromTeam(String scoreHolderName, Team team) {
      if (this.getScoreHolderTeam(scoreHolderName) == team) {
         this.teamsByScoreHolder.remove(scoreHolderName);
         team.getPlayerList().remove(scoreHolderName);
      }
   }

   @Shadow
   @Nullable
   public abstract Team getScoreHolderTeam(String var1);
}
