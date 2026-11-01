package org.phobos.module.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.SwordItem;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.BlockTweaksSendPlayerActionListener;
import org.phobos.module.Module;

public class BlockTweaksModule extends Module {
   public final Setting<Boolean> skipFalling;
   public final Setting<Boolean> entityTrace;
   public static BlockTweaksModule blockTweaksModule;
   public final Setting<Integer> delay = new NumberSetting<>("Delay", 0, 0, 5).resolveSetting6(this);
   public final Setting<Boolean> noBreakAnim = new BooleanSetting("NoBreakAnim").resolveSetting7("Cancels breaking animation").resolveSetting6(this);

   public static boolean check() {
      if (!blockTweaksModule.check7() || mc.player == null) {
         return false;
      } else {
         return mc.player.getMainHandStack().getItem() instanceof SwordItem ? false : blockTweaksModule.entityTrace.getValue();
      }
   }

   public BlockTweaksModule(PhobosClient phobosClient) {
      super(phobosClient, "BlockTweaks", Categories.player, "Block tweaks");
      this.entityTrace = new BooleanSetting("EntityTrace").resolveSetting7("Allows you to click blocks through entities").resolveSetting6(this);
      this.skipFalling = new BooleanSetting("SkipFalling").resolveSetting7("Ignores falling blocks").resolveSetting6(this);
      this.handleListener(new BlockTweaksSendPlayerActionListener(this));
      blockTweaksModule = this;
   }

   public static boolean testEntity(Entity entity) {
      return blockTweaksModule.check7() && blockTweaksModule.skipFalling.getValue() && entity instanceof FallingBlockEntity;
   }

   public static BlockTweaksModule getBlockTweaksModule() {
      return blockTweaksModule;
   }
}
