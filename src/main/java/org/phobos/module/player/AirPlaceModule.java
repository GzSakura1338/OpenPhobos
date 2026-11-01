package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;

public class AirPlaceModule extends PlacementModule {
   public final MutableObject<HitResult> hitResultMutableObject = new MutableObject();

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (mc.getCameraEntity() != null) {
         this.hitResultMutableObject.setValue(mc.getCameraEntity().raycast(this.getDoubleValueSetting().getValue(), 0.0F, false));
         if (clientPlayerEntity.getMainHandStack().getItem() instanceof BlockItem blockItem
            && this.hitResultMutableObject.getValue() instanceof BlockHitResult blockHitResult) {
            if (mc.options.useKey.isPressed()) {
               this.resolvePlacement8(blockItem.getBlock(), blockHitResult.getBlockPos());
            }
         }
      }
   }

   @Override
   public boolean alwaysTrue() {
      return true;
   }

   public AirPlaceModule(PhobosClient phobosClient) {
      super(phobosClient, "AirPlace", "Allows you to place blocks in air", Categories.player, 8);
   }
}
