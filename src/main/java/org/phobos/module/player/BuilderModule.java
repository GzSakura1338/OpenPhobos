package org.phobos.module.player;

import com.google.common.base.Predicates;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.TextProvider;
import org.phobos.api.AreaScanner;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.placement.BuildPlan;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.chunk.Hole;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.event.BuilderPlayerTickListener;

public class BuilderModule extends PlacementModule {
   public final Setting<Double> nametagRange;
   public final Random random;
   public final Setting<Integer> expand;
   public final Setting<Integer> width;
   public final Setting<Set<Block>> blocks;
   public static final BuildPlan buildPlan = BuildPlan.createBuilder()
      .resolveBuilder2(0, 0, 0, Blocks.SOUL_SAND)
      .resolveBuilder2(0, 1, 0, Blocks.SOUL_SAND)
      .resolveBuilder2(0, 1, 1, Blocks.SOUL_SAND)
      .resolveBuilder2(0, 1, -1, Blocks.SOUL_SAND)
      .resolveBuilder2(0, 0, 1, Blocks.AIR)
      .resolveBuilder2(0, 0, -1, Blocks.AIR)
      .resolveBuilder2(0, 2, 0, Blocks.WITHER_SKELETON_SKULL)
      .resolveBuilder2(0, 2, 0, Blocks.WITHER_SKELETON_WALL_SKULL)
      .resolveBuilder2(0, 2, 1, Blocks.WITHER_SKELETON_SKULL)
      .resolveBuilder2(0, 2, 1, Blocks.WITHER_SKELETON_WALL_SKULL)
      .resolveBuilder2(0, 2, -1, Blocks.WITHER_SKELETON_SKULL)
      .resolveBuilder2(0, 2, -1, Blocks.WITHER_SKELETON_WALL_SKULL)
      .getBuildPlan();
   public static final BuildPlan buildPlan2 = BuildPlan.createBuilder()
      .resolveBuilder2(0, 0, 0, Blocks.SOUL_SAND)
      .resolveBuilder2(0, 1, 0, Blocks.SOUL_SAND)
      .resolveBuilder2(1, 1, 0, Blocks.SOUL_SAND)
      .resolveBuilder2(-1, 1, 0, Blocks.SOUL_SAND)
      .resolveBuilder2(1, 0, 0, Blocks.AIR)
      .resolveBuilder2(-1, 0, 0, Blocks.AIR)
      .resolveBuilder2(0, 2, 0, Blocks.WITHER_SKELETON_SKULL)
      .resolveBuilder2(0, 2, 0, Blocks.WITHER_SKELETON_WALL_SKULL)
      .resolveBuilder2(1, 2, 0, Blocks.WITHER_SKELETON_SKULL)
      .resolveBuilder2(1, 2, 0, Blocks.WITHER_SKELETON_WALL_SKULL)
      .resolveBuilder2(-1, 2, 0, Blocks.WITHER_SKELETON_SKULL)
      .resolveBuilder2(-1, 2, 0, Blocks.WITHER_SKELETON_WALL_SKULL)
      .getBuildPlan();
   public final Setting<BuilderModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", BuilderModule.Mode.Highway));
   public final Setting<Boolean> nametag;
   public final Setting<Double> distance;
   public final Setting<PlacementModule.Rotation> nametagRotation;
   public final Setting<Integer> average;
   public final Setting<Integer> seed;

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      BlockPos blockPos14 = BlockPos.ofFloored(clientPlayerEntity.getPos()).down();
      Direction direction = clientPlayerEntity.getHorizontalFacing();
      Direction direction2 = direction.rotateYClockwise();
      Direction direction3 = direction.rotateYCounterclockwise();
      switch ((BuilderModule.Mode)this.mode.getValue()) {
         case Wither:
            Block block3 = itemSwitcher.resolveBlock2(this.blocks.getValue().stream());
            if (block3 == null) {
               return;
            }

            for (int i3 = 0; i3 < this.expand.getValue(); i3++) {
               BlockPos blockPos5 = blockPos14.offset(direction, i3);
               this.resolvePlacement8(block3, blockPos5);
               int i2 = this.width.getValue();

               for (int i = 0; i < i2; i++) {
                  BlockPos blockPos3 = blockPos5.offset(direction2, i);
                  BlockPos blockPos4 = blockPos5.offset(direction3, i);
                  this.resolvePlacement8(block3, blockPos3);
                  this.resolvePlacement8(block3, blockPos4);
                  if (i == i2 - 1) {
                     this.resolvePlacement8(block3, blockPos3.up());
                     this.resolvePlacement8(block3, blockPos4.up());
                  }
               }
            }
            break;
         case Swastika:
            Block block4 = itemSwitcher.resolveBlock2(this.blocks.getValue().stream());
            if (block4 == null) {
               return;
            }

            BlockPos blockPos6 = blockPos14.offset(direction, 2);
            BlockPos blockPos7 = blockPos6.up();
            BlockPos blockPos8 = blockPos6.up(2);
            BlockPos blockPos9 = blockPos6.up(3);
            BlockPos blockPos10 = blockPos6.up(4);
            BlockPos blockPos11 = blockPos6.up(5);
            this.resolvePlacement8(block4, blockPos7);
            this.resolvePlacement8(block4, blockPos7.offset(direction3));
            this.resolvePlacement8(block4, blockPos7.offset(direction3, 2));
            this.resolvePlacement8(block4, blockPos7.offset(direction2, 2));
            this.resolvePlacement8(block4, blockPos8);
            this.resolvePlacement8(block4, blockPos8.offset(direction2, 2));
            this.resolvePlacement8(block4, blockPos9);
            this.resolvePlacement8(block4, blockPos9.offset(direction2));
            this.resolvePlacement8(block4, blockPos9.offset(direction2, 2));
            this.resolvePlacement8(block4, blockPos9.offset(direction3));
            this.resolvePlacement8(block4, blockPos9.offset(direction3, 2));
            this.resolvePlacement8(block4, blockPos10);
            this.resolvePlacement8(block4, blockPos10.offset(direction3, 2));
            this.resolvePlacement8(block4, blockPos11);
            this.resolvePlacement8(block4, blockPos11.offset(direction3, 2));
            this.resolvePlacement8(block4, blockPos11.offset(direction2));
            this.resolvePlacement8(block4, blockPos11.offset(direction2, 2));
            break;
         case Highway:
            Block block2 = itemSwitcher.resolveBlock2(this.blocks.getValue().stream());
            if (block2 == null) {
               return;
            }

            AreaScanner.Area area = new AreaScanner.Area();
            area.handleVec3d(clientPlayerEntity.getEyePos(), this.getDoubleValueSetting().getValue(), Predicates.alwaysTrue());
            area.handleInt(this.average.getValue(), this.seed.testObject2(0) ? clientPlayerEntity.getBlockPos().hashCode() : this.seed.getValue().intValue());

            for (BlockPos blockPos2 : area) {
               this.resolvePlacement8(block2, blockPos2);
            }
            break;
         case Holes:
            Block block5 = itemSwitcher.resolveBlock(Blocks.SOUL_SAND);
            Block block6 = itemSwitcher.resolveBlock(Blocks.WITHER_SKELETON_SKULL);
            if (block5 == null || block6 == null) {
               return;
            }

            BlockPos blockPos12 = blockPos14.offset(direction, 2);
            BlockPos blockPos13 = blockPos12.up();
            if (buildPlan2.testBlockPos(blockPos13, clientWorld)) {
               buildPlan2.handleBlockPos(blockPos13, (blockPos, blockCollection) -> {
                  for (Block block : blockCollection) {
                     if (this.resolvePlacement8(block, blockPos).isSuccessful()) {
                        break;
                     }
                  }
               });
               return;
            }

            if (buildPlan.testBlockPos(blockPos13, clientWorld)) {
               buildPlan.handleBlockPos(blockPos13, (blockPos, blockCollection) -> {
                  for (Block block : blockCollection) {
                     if (this.resolvePlacement8(block, blockPos).isSuccessful()) {
                        break;
                     }
                  }
               });
            }
            break;
         case Random:
            Block block = this.resolveBlock2(itemSwitcher);
            if (block == null) {
               return;
            }

            double d2 = MathHelper.square(this.getDoubleValueSetting().getValue());
            double d3 = this.distance.getValue();
            Set<Hole> set = new HashSet<>();

            for (Hole hole : this.getPhobosClient().getHoleChunkScanner().getCollection()) {
               if (hole.computeEntity(clientPlayerEntity) <= d2 && hole.isFlag2() && !hole.isFlag22()) {
                  set.add(hole);
               }
            }

            if (set.isEmpty()) {
               return;
            }

            Hole hole3 = null;
            double d4 = Double.MAX_VALUE;

            for (Hole hole2 : set) {
               double d = hole2.computeEntity(clientPlayerEntity);
               if (d < d4) {
                  hole3 = hole2;
                  d4 = d;
               }
            }

            BlockPos blockPos;
            if (hole3 != null && d4 <= MathHelper.square(d3) && (blockPos = hole3.getBlockPos()) != null) {
               this.resolvePlacement8(block, blockPos);
            }
      }
   }

   public BuilderModule(PhobosClient phobosClient) {
      super(phobosClient, "Builder", "Builds something", Categories.player, 3);
      this.width = this.resolveSetting2(
         new NumberSetting<>("Width", 5, 1, 5).resolveSetting7("Highway width").resolveSetting4(this.mode, BuilderModule.Mode.Highway)
      );
      this.expand = this.resolveSetting2(new NumberSetting<>("Expand", 2, 1, 4).resolveSetting4(this.mode, BuilderModule.Mode.Highway));
      this.blocks = this.resolveSetting2(
         new BlockSetSetting("Blocks")
            .resolveSetting3(() -> !this.mode.testObject2(BuilderModule.Mode.Wither) && !this.mode.testObject2(BuilderModule.Mode.Holes))
      );
      this.nametag = this.resolveSetting2(
         new BooleanSetting("Nametag").resolveSetting7("Nametag the wither").resolveSetting4(this.mode, BuilderModule.Mode.Wither)
      );
      this.nametagRange = this.resolveSetting2(new NumberSetting<>("NametagRange", 6.0, 3.0, 6.0));
      this.nametagRotation = this.resolveSetting2(new EnumSetting<>("NametagRotation", PlacementModule.Rotation.Normal));
      this.seed = this.resolveSetting2(
         new NumberSetting<>("Seed", 0, 0, Integer.MAX_VALUE).resolveNumberSetting("Dynamic", 0).resolveSetting4(this.mode, BuilderModule.Mode.Random)
      );
      this.average = this.resolveSetting2(new NumberSetting<>("Average", 5, 1, 20).resolveSetting4(this.mode, BuilderModule.Mode.Random));
      this.distance = this.resolveSetting2(new NumberSetting<>("Distance", 2.0, 0.5, 4.0).resolveSetting4(this.mode, BuilderModule.Mode.Holes));
      this.random = new Random();
      this.resolveSettingGroup(this.nametag, this.nametagRange, this.nametagRotation).resolveSettingGroup4(this.mode, BuilderModule.Mode.Wither);
      this.handleListener(new BuilderPlayerTickListener(this));
   }

   public enum Mode implements TextProvider {
      Highway("Highway builder"),
      Swastika("Builds something not very good"),
      Random("Random blocks within the range"),
      Wither("Builds a wither"),
      Holes("Builds holes");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
