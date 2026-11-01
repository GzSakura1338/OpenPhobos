package org.phobos.module.combat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.task.ConditionalActions;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.task.ConditionalActions.Builder;
import org.phobos.event.SurroundJumpListener;
import org.phobos.event.SurroundMoveListener;
import org.phobos.event.SurroundPostReceiveEntitySpawnListener;

public class SurroundModule extends PlacementModule {
   public Setting<SurroundModule.Disable> disableSetting;
   public Setting<Long> longValueSetting2;
   public double doubleValue;
   public Setting<Boolean> booleanValueSetting10;
   public static SurroundModule surroundModule;
   public ConditionalActions conditionalActions;
   public Normal normal3;
   public Set<BlockPos> blockPosSet;
   private static Object[] phobos$g1806e6fb0;
   private static Object[] phobos$g1806e7088;

   @Override
   public void run3() {
      this.normal3.run();
      if (mc.player != null) {
         this.doubleValue = mc.player.getY();
         if (this.booleanValueSetting10.getValue()) {
            BlockPos blockPos = BlockPos.ofFloored(mc.player.getPos());
            mc.player.setPosition(blockPos.getX() + 0.5, mc.player.getY(), blockPos.getZ() + 0.5);
         }
      }
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Block block = this.resolveBlock2(itemSwitcher);
      if (this.disableSetting.testObject2(SurroundModule.Disable.Move) && !Double.isNaN(this.doubleValue) && clientPlayerEntity.getY() > this.doubleValue) {
         this.run11();
      } else if (block != null) {
         this.resolveSet2(clientPlayerEntity, clientPlayerEntity, clientWorld, block, true);
      }
   }

   public boolean testBlockPos9(
      BlockPos blockPos,
      BlockPos blockPos2,
      Set<BlockPos> set,
      Set<BlockPos> set2,
      Set<BlockPos> set3,
      Set<Entity> set4,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      Block block
   ) {
      MutableObject<Boolean> mutableObject = new MutableObject<>(false);
      BlockUtils.testBlockPos8(blockPos2, block.getDefaultState().getCollisionShape(clientWorld, blockPos2), clientPlayerEntity, clientWorld, entity -> {
         if (!(entity instanceof EndCrystalEntity)) {
            mutableObject.setValue(true);
            if (set4.add(entity)) {
               for (BlockPos blockPos2x : BlockPosUtils.resolveSet3(entity, blockPos.getY())) {
                  this.handleBlockPos2(blockPos2x, set, set2, set3, set4, clientPlayerEntity, clientWorld, block, false);
               }
            }
         }

         return true;
      }, endCrystalEntity -> {}, this.getDoubleValueSetting().getValue());
      return mutableObject.getValue();
   }

   public void handleBlockPos(
      BlockPos blockPos,
      Set<BlockPos> set,
      Set<BlockPos> set2,
      Set<BlockPos> set3,
      Set<Entity> set4,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      Block block,
      boolean flag,
      @Nullable Direction direction
   ) {
      BlockPos blockPos2 = direction == null ? blockPos : blockPos.offset(direction);
      if (set3.add(blockPos2)) {
         if (!this.testBlockPos10(blockPos2, clientWorld, set2, flag)) {
            BlockPos blockPos3 = blockPos2.up();
            boolean flag2 = this.testBlockPos9(blockPos, blockPos3, set, set2, set3, set4, clientPlayerEntity, clientWorld, block);
            if (flag) {
               set.add(blockPos2);
               set2.add(blockPos2);
            }

            if (!flag2) {
               set2.add(blockPos3);
               set.add(blockPos3);
            }
         }
      }
   }

   public boolean testBlockPos10(BlockPos blockPos, ClientWorld clientWorld, Set<BlockPos> set, boolean flag) {
      BlockPos blockPos2 = blockPos.up();
      BlockState blockState = clientWorld.getBlockState(blockPos2);
      if (!blockState.isReplaceable()) {
         set.add(blockPos2);
         if (flag && !blockState.isOf(Blocks.BEDROCK)) {
            set.add(blockPos);
         }
      }

      return false;
   }

   public Set<BlockPos> resolveSet(PlayerEntity playerEntity, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Block block, boolean flag) {
      Set<Entity> set = new HashSet<>();
      set.add(playerEntity);
      set.add(clientPlayerEntity);
      Set<BlockPos> set2 = BlockPosUtils.resolveSet7(playerEntity, 0.5);
      Set<BlockPos> set3 = new LinkedHashSet<>(set2);
      Set<BlockPos> set4 = new HashSet<>(set2);
      Set<BlockPos> set5 = new HashSet<>();

      for (BlockPos blockPos : set2) {
         this.handleBlockPos2(blockPos, set3, set4, set5, set, clientPlayerEntity, clientWorld, block, true);
      }

      if (flag) {
         for (BlockPos blockPos3 : set3) {
            if (this.resolvePlacement8(block, blockPos3).isFailed()) {
               for (BlockPos blockPos2 : this.resolveList2(blockPos3, clientWorld, clientPlayerEntity, block)) {
                  this.resolvePlacement8(block, blockPos2);
               }

               this.resolvePlacement8(block, blockPos3);
            }
         }

         this.blockPosSet = set3;
      }

      return set4;
   }

   public SurroundModule(PhobosClient phobosClient) {
      super(phobosClient, "Surround", "Places blocks around you", Categories.combat, 8);
      this.nativeInit2(phobosClient);
   }

   public void nativeInit2(PhobosClient var1) {
      SurroundModule.Disable var3 = SurroundModule.Disable.None;
      EnumSetting var2 = new EnumSetting<>("Disable", var3);
      Setting var4 = var2.resolveSetting6(this);
      this.disableSetting = var4;
      BooleanSetting var5 = new BooleanSetting("Center");
      Setting var6 = var5.resolveSetting6(this);
      this.booleanValueSetting10 = var6;
      LongNumberSetting var7 = new LongNumberSetting("Timeout", 0L, 0L, 500L, true & true);
      Long var8 = 0L;
      NumberSetting var9 = var7.resolveNumberSetting("Disabled", var8);
      Setting var10 = var9.resolveSetting6(this);
      this.longValueSetting2 = var10;
      Builder var11 = new Builder();
      Runnable var28 = this::run11;
      Supplier<Boolean> var45 = this::getBooleanValue;
      Builder var46 = var11.resolveBuilder(var28, var45);
      ConditionalActions var47 = var46.getConditionalActions();
      this.conditionalActions = var47;
      Normal var48 = new Normal();
      this.normal3 = var48;
      HashSet var49 = new HashSet();
      this.blockPosSet = var49;
      this.doubleValue = Double.NaN;
      SurroundPostReceiveEntitySpawnListener var50 = new SurroundPostReceiveEntitySpawnListener(this);
      this.handleListener(var50);
      SurroundJumpListener var51 = new SurroundJumpListener(this);
      this.handleListener(var51);
      SurroundMoveListener var52 = new SurroundMoveListener(this);
      this.handleListener(var52);
      Setting var53 = this.booleanValueSetting10;
      Setting[] var54 = new Setting[1];
      Setting var55 = this.longValueSetting2;
      var54[0] = var55;
      this.resolveSettingGroup(var53, var54);
      surroundModule = this;
   }

   public void handleBlockPos2(
      BlockPos blockPos,
      Set<BlockPos> set,
      Set<BlockPos> set2,
      Set<BlockPos> set3,
      Set<Entity> set4,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      Block block,
      boolean flag
   ) {
      if (!this.testBlockPos10(blockPos, clientWorld, set2, flag)) {
         for (Direction direction : DirectionUtils.directionArray) {
            this.handleBlockPos(blockPos, set, set2, set3, set4, clientPlayerEntity, clientWorld, block, flag, direction);
         }
      }
   }

   @Override
   public void run4() {
      this.doubleValue = Double.NaN;
   }

   public static SurroundModule getSurroundModule() {
      return surroundModule;
   }

   public Set<BlockPos> resolveSet2(PlayerEntity playerEntity, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Block block, boolean flag) {
      Set<Entity> set = new HashSet<>();
      set.add(playerEntity);
      set.add(clientPlayerEntity);
      BlockPos blockPos9 = BlockPos.ofFloored(playerEntity.getPos());
      Set<BlockPos> set2 = BlockPosUtils.resolveSet7(playerEntity, 0.5);
      Set<BlockPos> set3 = new LinkedHashSet<>(set2);
      Set<BlockPos> set4 = new HashSet<>(set2);
      Set<BlockPos> set5 = new HashSet<>();
      List<BlockPos> list2 = new ArrayList<>(4);
      List<BlockPos> list3 = new ArrayList<>(4);

      for (BlockPos blockPos2 : set2) {
         BlockPos blockPos = blockPos2.up();
         if (clientWorld.getBlockState(blockPos).isReplaceable()) {
            list3.add(blockPos);
         } else {
            list2.add(blockPos);
         }
      }

      int i = set2.size();
      MutableObject<BlockPos> mutableObject = new MutableObject<>();
      if (i == 4 && list2.stream().filter(blockPos -> clientWorld.getBlockState(blockPos).isOf(Blocks.BEDROCK)).count() >= 2L) {
         for (BlockPos blockPos4 : set2) {
            BlockPos blockPos3 = blockPos4.up();
            if (!blockPos9.equals(blockPos3) && !clientWorld.getBlockState(blockPos3).isOf(Blocks.BEDROCK)) {
               mutableObject.setValue(blockPos3);
               break;
            }
         }
      }

      boolean flag2 = mutableObject.getValue() != null;

      for (BlockPos blockPos6 : set2) {
         BlockPos blockPos5 = blockPos6.up();
         BlockState blockState = clientWorld.getBlockState(blockPos5);
         if (blockState.isOf(Blocks.BEDROCK)) {
            set4.add(blockPos6);
            set3.add(blockPos6);
         } else if (flag2 && mutableObject.getValue().equals(blockPos5)) {
            set4.remove(blockPos6);
         } else {
            this.handleBlockPos2(blockPos6, set3, set4, set5, set, clientPlayerEntity, clientWorld, block, true);
         }
      }

      if (flag) {
         for (BlockPos blockPos8 : set3) {
            if (this.resolvePlacement8(block, blockPos8).isFailed()) {
               for (BlockPos blockPos7 : this.resolveList2(blockPos8, clientWorld, clientPlayerEntity, block)) {
                  this.resolvePlacement8(block, blockPos7);
               }

               this.resolvePlacement8(block, blockPos8);
            }
         }

         this.blockPosSet = set3;
      }

      return set4;
   }

   public Boolean getBooleanValue() {
      return this.disableSetting.testObject2(SurroundModule.Disable.Jump);
   }

   public enum Disable implements TextProvider {
      None("Do not disable automatically"),
      Jump("Disables the module after jump"),
      Move("Disables the module when you move upwards");

      public final String text;

      Disable(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
