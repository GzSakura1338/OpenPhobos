package org.phobos.module.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.TimeGate;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;

public class TestModule2Module extends PlacementModule {
   public final TimeGate.Normal normal3 = new TimeGate.Normal();

   public TestModule2Module(PhobosClient phobosClient) {
      super(phobosClient, "TestModule2", Categories.client, 90);
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.normal3.testLong2(2000L)) {
         if (itemSwitcher.testItem2(Items.WATER_BUCKET)) {
            BlockPos blockPos = clientPlayerEntity.getBlockPos();
            this.resolvePlacement(Items.WATER_BUCKET, blockPos);
         }
      }
   }
}
