package org.earthhack.ducks;

import java.util.List;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.network.message.MessageSignatureData;

public interface IChat {
   void delete(MessageSignatureData var1, boolean var2);

   void run();

   void backup();

   void run2();

   List<ChatHudLine> getMessages();
}
