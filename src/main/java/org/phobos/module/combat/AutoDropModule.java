package org.phobos.module.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.joml.Vector2d;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;

public class AutoDropModule extends PlacementModule {
   public final Setting<Boolean> filter;
   public final Setting<AutoDropModule.Mode> mode;
   public final Setting<AutoDropModule.Type> type = this.resolveSetting2(new EnumSetting<>("Type", AutoDropModule.Type.Anvil));
   public final Setting<Integer> height;

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = (Block)(switch ((AutoDropModule.Type)this.type.getValue()) {
         case Any -> itemSwitcher.resolveBlock(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL);
         case Anvil -> (FallingBlock)itemSwitcher.resolveObject(
            slot -> slot.getStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof FallingBlock fallingBlock ? fallingBlock : null
         );
      });
      if (block != null) {
         for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
            if (!playerEntity.equals(clientPlayerEntity)
               && !EntityUtils.testLivingEntity(playerEntity)
               && !playerEntity.isCreative()
               && !playerEntity.isSpectator()
               && !AntiBotModule.testEntity2(playerEntity)
               && !this.phobosClient.getFriendList().testUUID(playerEntity.getUuid())) {
               BlockPos blockPos4 = BlockPos.ofFloored(playerEntity.getPos());
               if (!BlockUtils.testBlockPos7(blockPos4, clientWorld)
                  && !BlockUtils.testBlockPos7(blockPos4.up(), clientWorld)
                  && (!this.filter.getValue() || !this.testBox(playerEntity.getBoundingBox(), clientWorld))) {
                  switch ((AutoDropModule.Mode)this.mode.getValue()) {
                     case Dynamic:
                        if (!(clientPlayerEntity.distanceTo(playerEntity) > 8.0)) {
                           BlockPos blockPos = blockPos4.up(this.height.getValue());
                           if (!BlockUtils.testBlockPos7(blockPos, clientWorld)) {
                              this.resolvePlacement8(block, blockPos);
                           }
                        }
                        break;
                     case Static:
                        Vector2d vector2d = new Vector2d(clientPlayerEntity.getX(), clientPlayerEntity.getZ());
                        Vector2d vector2d2 = new Vector2d(playerEntity.getX(), playerEntity.getZ());
                        if (!(vector2d.distance(vector2d2) > 8.0)) {
                           BlockPos blockPos3 = blockPos4.up(2);

                           for (int i = blockPos3.getY(); i < clientWorld.getHeight(); i++) {
                              BlockPos blockPos2 = new BlockPos(blockPos3.getX(), i, blockPos3.getZ());
                              BlockState blockState = clientWorld.getBlockState(blockPos2);
                              if (blockState.getBlock() instanceof FallingBlock && FallingBlock.canFallThrough(clientWorld.getBlockState(blockPos2.down()))
                                 || BlockUtils.testBlockPos7(blockPos2, clientWorld)
                                 || !this.resolvePlacement8(block, blockPos2).isFailed()) {
                                 break;
                              }
                           }
                        }
                  }
               }
            }
         }
      }
   }

   public boolean testBox(Box box, ClientWorld clientWorld) {
      Box box2 = box.stretch(0.0, clientWorld.getHeight() - box.maxY, 0.0);

      for (Entity entity : clientWorld.getOtherEntities(null, box2)) {
         if (entity instanceof FallingBlockEntity) {
            return true;
         }
      }

      return false;
   }

   public AutoDropModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoDrop", "Drops falling blocks on enemies", Categories.combat, 5);
      this.mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoDropModule.Mode.Dynamic));
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 2, 2, 7).resolveSetting4(this.mode, AutoDropModule.Mode.Static));
      this.filter = this.resolveSetting2(new BooleanSetting("Filter").resolveSetting7("Checks if there's a falling anvil"));
   }

   private enum Mode implements TextProvider {
      Static("Places anvils on fixed height"),
      Dynamic("Places anvils somewhere");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Type {
      Anvil,
      Any;
   }
}
