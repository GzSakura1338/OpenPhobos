package org.phobos.module.movement;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.event.AntiWebPlayerTickListener;
import org.phobos.event.AntiWebComputeNextListener;
import org.phobos.module.Module;

public class AntiWebModule extends Module {
   public boolean flag;
   public static AntiWebModule antiWebModule;
   public final Setting<AntiWebModule.Mode> mode = new EnumSetting<>("Mode", AntiWebModule.Mode.Disable).resolveSetting6(this);

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return BlockUtils.testClientWorld(clientWorld, clientPlayerEntity, (blockPos, blockState) -> blockState.isOf(Blocks.COBWEB));
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public static boolean check() {
      return antiWebModule.check7() && antiWebModule.mode.getValue().equals(AntiWebModule.Mode.Disable);
   }

   public AntiWebModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiWeb", Categories.movement, "Prevents web slowdown");
      this.flag = false;
      this.handleListener(new AntiWebComputeNextListener(this));
      this.handleListener(new AntiWebPlayerTickListener(this));
      antiWebModule = this;
   }

   public enum Mode {
      Disable,
      Collide,
      Grim;
   }
}
