package org.phobos.core.server;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.ServerMetadata.Favicon;
import net.minecraft.server.ServerMetadata.Players;
import net.minecraft.server.ServerMetadata.Version;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.phobos.api.MinecraftHolder;

public class PingBypassStatus implements MinecraftHolder {
   public static final byte[] byteArray = Base64.getDecoder()
      .decode(
         "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAABhUlEQVR42u3WMUgCUQDG8bc0OFhk\nUBE1KRFZg9LgIMUFBi2JSSlUQyY0VBiElFs0OAlNDQWFYUMFQVDU0BBtNoQRjVFLQdAS1NB2bR+v\n4eCuw3wX38dvetyd93cQRcbEbo9mQD5//9wDq+f6dT2YuddoggEM+IOAu/0oCGnyh8nnun4AwmBy\ngNEzGcAAVQLKOxE4yQ/Bc2UFrooTIL/E2+sGVM5moVxMwP15GuRzBjBAlYAfN1R5lt+HAQxgAAMY\nULMA3WCGf+BMXM8ABjgpgL9CDGAAAxjg/ACr6+9uADsv2uXzwFg0DNX+ghjAALt7OZwEO8/JpkJQ\nyA4AAxigesBCzA8JzQtm7u0LtMNyOgRBrwcYwADVA+TdbMbBzPXTI35YmwuDqNUYwACbS2o+yIz2\ngHxNsLcNCrkIdDS5gAEMcGqAvNhgJxzvTsHqkgatzW4Qqo0BDPjF3K46KOU0eLxchK+PLUiNB4AB\nDPgPAS2NLngoJeE0PwxPF/OwvR4HBjBAkYBveo6/J887s58AAAAASUVORK5CYII="
      );

   public ServerMetadata createServerMetadata() {
      return new ServerMetadata(
         this.describe(), Optional.of(new Players(1, 2, List.of())), Optional.of(new Version("1.21.1", 754)), Optional.of(new Favicon(byteArray)), true
      );
   }

   public Text describe() {
      if (mc.isInSingleplayer()) {
         return Text.literal("Singleplayer");
      }

      ServerInfo serverInfo = mc.getCurrentServerEntry();
      return serverInfo != null && mc.world != null
         ? Text.literal(serverInfo.address).append(Text.literal(", ").formatted(Formatting.GRAY).append(""))
         : Text.literal("Not connected").formatted(Formatting.RED);
   }
}
