package org.phobos.module.movement;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.IceSpeedTickListener;
import org.phobos.mixin.game.world.IAbstractBlock;
import org.phobos.module.Module;

public class IceSpeedModule extends Module {
   public final Setting<Float> speed = new NumberSetting<>("Speed", 0.01F, 0.01F, 0.2F).resolveSetting6(this);

   public IAbstractBlock resolveIAbstractBlock(Block block) {
      return (IAbstractBlock)(Object)block;
   }

   public IceSpeedModule(PhobosClient phobosClient) {
      super(phobosClient, "IceSpeed", Categories.movement, "Makes you faster on ice");
      this.handleListener(new IceSpeedTickListener(this));
      this.handleTriConsumer4((clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> {
         this.resolveIAbstractBlock(Blocks.ICE).setSlipperiness(0.98F);
         this.resolveIAbstractBlock(Blocks.PACKED_ICE).setSlipperiness(0.98F);
         this.resolveIAbstractBlock(Blocks.FROSTED_ICE).setSlipperiness(0.98F);
      });
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
