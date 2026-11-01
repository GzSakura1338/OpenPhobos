package org.earthhack.ducks;

import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.phobos.core.user.UserTagManager;

public interface IPlayerListEntry {
   void setCape(Identifier var1);

   void setSkin(Identifier var1, String var2, Model var3);

   UserTagManager.TelemetryUser getUser();

   void setUser(UserTagManager.TelemetryUser var1);

   Text getFormattedUsername();

   Text getFormattedPlayerName();
}
