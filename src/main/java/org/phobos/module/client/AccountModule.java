package org.phobos.module.client;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.PhobosException;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.Listener;
import org.phobos.mixin.net.interfaces.ILoginHelloC2SPacket;
import org.phobos.type.EmptyEnum;
import org.phobos.module.Module;

public class AccountModule extends Module {
   public final Setting<String> email;
   public final Setting<String> name2 = this.resolveSetting2(new StringSetting("Name", "Player999").resolveStringSetting(16));
   public final Setting<String> password;
   public final Setting<Boolean> login;

   public AccountModule(PhobosClient phobosClient) {
      super(phobosClient, "Account", Categories.client, "Spoofs your name and UUID");
      this.email = this.resolveSetting2(new StringSetting("Email", "").getSetting());
      this.password = this.resolveSetting2(new StringSetting("Password", "").getSetting());
      this.login = this.resolveSetting2(new BooleanSetting("Login").getSetting());
      this.handleListener(new Listener<Send<LoginHelloC2SPacket>>() {
         public void handleObject2(Send<LoginHelloC2SPacket> send) {
            ILoginHelloC2SPacket iLoginHelloC2SPacket = ILoginHelloC2SPacket.class.cast(send.getPacket());
            String text = AccountModule.this.name2.getValue();
            UUID uUID = UUID.randomUUID();
            iLoginHelloC2SPacket.setName(text);
            iLoginHelloC2SPacket.setProfileId(uUID);
            IntegratedServer integratedServer = MinecraftHolder.mc.getServer();
            if (integratedServer != null) {
               integratedServer.setHostProfile(new GameProfile(uUID, text));
            }
         }
      });
      this.resolveSettingGroup2("Microsoft", this.email, this.password, this.login);
      this.login.handleConsumer2(booleanValue -> {
         if (booleanValue) {
            try {
               EmptyEnum.login(this.email.getValue(), this.password.getValue());
            } catch (PhobosException phobosException) {
               this.handleText7(Text.literal("Failed to login!").formatted(Formatting.RED));
            }
         }
      });
   }
}
