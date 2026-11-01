package org.phobos.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.math.MutableBox;
import org.phobos.core.PhobosClient;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d;
import org.phobos.core.block.BlockUtils;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class PhaseHighlightModule extends Module {
   public final List<BlockPos> blockPosList;
   public final Setting<Float> sealedWidth;
   public final Setting<Boolean> semi;
   public final Setting<PhaseHighlightModule.Extension> extension = this.resolveSetting2(
      new EnumSetting<>("Extension", PhaseHighlightModule.Extension.Blocked)
   );
   public final Setting<Color> unsafeLine;
   public final List<BlockPos> blockPosList2;
   public final Setting<Long> time;
   public final Setting<Boolean> collide = this.resolveSetting2(new BooleanSetting("Collide", false).resolveSetting7("Checks collision"));
   public final Setting<Color> safeBox;
   public final Setting<Color> semiBox;
   public final Setting<Boolean> sealedValue;
   public final List<BlockPos> blockPosList3;
   public final Setting<Color> unsafeBox;
   public final Setting<Float> safeWidth;
   public final Setting<Color> safeLine;
   public final Setting<Color> sealedLine;
   public final Setting<Boolean> unsafe;
   public final Setting<Float> unsafeWidth;
   public final Setting<Boolean> fade;
   public final Setting<Color> semiLine;
   public final Setting<Float> semiWidth;
   public final Setting<Color> sealedBox;
   public final Setting<Boolean> safe;
   public final List<BlockPos> blockPosList4;
   public final Setting<Double> threshold = this.resolveSetting2(new NumberSetting<>("Threshold", 0.02, 0.0, 0.5));

   public void handleList(List<BlockPos> list, BlockPos blockPos) {
      list.removeIf(blockPos2 -> blockPos2.equals(blockPos));
      list.add(blockPos);
   }

   public void handleClientWorld(ClientWorld clientWorld, BlockPos blockPos) {
      BlockPos blockPos2 = blockPos.down();
      BlockState blockState = clientWorld.getBlockState(blockPos);
      BlockState blockState2 = clientWorld.getBlockState(blockPos2);
      if (blockState.getBlock().equals(Blocks.BEDROCK)) {
         if (blockState2.isReplaceable()) {
            if (this.testBlockPos(blockPos2) && this.sealedValue.getValue()) {
               this.handleList(this.blockPosList4, blockPos);
               return;
            }

            if (this.unsafe.getValue()) {
               this.handleList(this.blockPosList3, blockPos);
            }

            return;
         }

         if (blockState2.getBlock().equals(Blocks.OBSIDIAN)) {
            if (this.semi.getValue()) {
               this.handleList(this.blockPosList2, blockPos);
            }

            return;
         }

         if (blockState2.getBlock().equals(Blocks.BEDROCK)) {
            if (this.safe.getValue()) {
               this.handleList(this.blockPosList, blockPos);
            }

            return;
         }
      }

      if (blockState.getBlock().equals(Blocks.OBSIDIAN)) {
         if (blockState2.isReplaceable() && this.unsafe.getValue()) {
            this.handleList(this.blockPosList3, blockPos);
            return;
         }

         this.handleList(this.blockPosList2, blockPos);
      }
   }

   public void run2() {
      this.blockPosList3.clear();
      this.blockPosList2.clear();
      this.blockPosList.clear();
      this.blockPosList4.clear();
   }

   public PhaseHighlightModule(PhobosClient phobosClient) {
      super(phobosClient, "PhaseHighlight", Categories.render, "Highlights phase positions");
      this.safe = this.resolveSetting2(new BooleanSetting("Safe", true).resolveSetting7("Safe positions (solid bedrock)"));
      this.safeBox = this.resolveSetting2(new ColorSetting("SafeBox", new Color(0, 255, 0, 40)));
      this.safeLine = this.resolveSetting2(new ColorSetting("SafeLine", Color.GREEN));
      this.safeWidth = this.resolveSetting2(new NumberSetting<>("SafeWidth", 1.0F, 0.0F, 3.0F));
      this.semi = this.resolveSetting2(new BooleanSetting("Semi", true).resolveSetting7("Semi-safe positions (bedrock, can fall)"));
      this.semiBox = this.resolveSetting2(new ColorSetting("SemiBox", new Color(255, 255, 0, 40)));
      this.semiLine = this.resolveSetting2(new ColorSetting("SemiLine", Color.YELLOW));
      this.semiWidth = this.resolveSetting2(new NumberSetting<>("SemiWidth", 1.0F, 0.0F, 3.0F));
      this.unsafe = this.resolveSetting2(new BooleanSetting("Unsafe", true).resolveSetting7("Unsafe positions (obsidian)"));
      this.unsafeBox = this.resolveSetting2(new ColorSetting("UnsafeBox", new Color(255, 0, 0, 40)));
      this.unsafeLine = this.resolveSetting2(new ColorSetting("UnsafeLine", Color.RED));
      this.unsafeWidth = this.resolveSetting2(new NumberSetting<>("UnsafeWidth", 1.0F, 0.0F, 3.0F));
      this.sealedValue = this.resolveSetting2(new BooleanSetting("Sealed", true).resolveSetting7("Sealed positions (can fall, bedrock all around)"));
      this.sealedBox = this.resolveSetting2(new ColorSetting("SealedBox", new Color(255, 0, 255, 40)));
      this.sealedLine = this.resolveSetting2(new ColorSetting("SealedLine", Color.MAGENTA));
      this.sealedWidth = this.resolveSetting2(new NumberSetting<>("SealedWidth", 1.0F, 0.0F, 3.0F));
      this.fade = this.resolveSetting2(new BooleanSetting("Fade", true).resolveSetting7("Adds fade animation"));
      this.time = this.resolveSetting2(new LongNumberSetting("Time", 500L, 0L, 1000L));
      this.blockPosList = new ArrayList<>();
      this.blockPosList2 = new ArrayList<>();
      this.blockPosList3 = new ArrayList<>();
      this.blockPosList4 = new ArrayList<>();
      this.resolveSettingGroup(this.collide, this.threshold);
      this.createBuilder()
         .resolveBuilder4("Render")
         .resolveBuilder5(
            this.resolveSettingGroup(this.safe, this.safeBox, this.safeLine, this.safeWidth),
            this.resolveSettingGroup(this.semi, this.semiBox, this.semiLine, this.semiWidth),
            this.resolveSettingGroup(this.unsafe, this.unsafeBox, this.unsafeLine, this.unsafeWidth),
            this.resolveSettingGroup(this.sealedValue, this.sealedBox, this.sealedLine, this.sealedWidth),
            this.resolveSettingGroup(this.fade, this.time)
         )
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld) && !clientPlayerEntity.isInSwimmingPose()) {
                  PhaseHighlightModule.this.run2();
                  PhaseHighlightModule.this.handlePlayerEntity(clientPlayerEntity, clientWorld);
                  if (PhaseHighlightModule.this.collide.getValue()) {
                     PhaseHighlightModule.this.handlePredicate(blockPos -> !PhaseHighlightModule.this.testBlockPos2(blockPos, clientPlayerEntity));
                  }
               } else {
                  PhaseHighlightModule.this.run2();
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               MutableBox mutableBox = new MutableBox();
               PhaseHighlightModule.this.handleMutableBox(
                  mutableBox,
                  PhaseHighlightModule.this.blockPosList,
                  PhaseHighlightModule.this.safeBox.getValue(),
                  PhaseHighlightModule.this.safeLine.getValue(),
                  PhaseHighlightModule.this.safeWidth.getValue()
               );
               PhaseHighlightModule.this.handleMutableBox(
                  mutableBox,
                  PhaseHighlightModule.this.blockPosList2,
                  PhaseHighlightModule.this.semiBox.getValue(),
                  PhaseHighlightModule.this.semiLine.getValue(),
                  PhaseHighlightModule.this.semiWidth.getValue()
               );
               PhaseHighlightModule.this.handleMutableBox(
                  mutableBox,
                  PhaseHighlightModule.this.blockPosList3,
                  PhaseHighlightModule.this.unsafeBox.getValue(),
                  PhaseHighlightModule.this.unsafeLine.getValue(),
                  PhaseHighlightModule.this.unsafeWidth.getValue()
               );
               PhaseHighlightModule.this.handleMutableBox(
                  mutableBox,
                  PhaseHighlightModule.this.blockPosList4,
                  PhaseHighlightModule.this.sealedBox.getValue(),
                  PhaseHighlightModule.this.sealedLine.getValue(),
                  PhaseHighlightModule.this.sealedWidth.getValue()
               );
            }
         }
      );
   }

   public boolean testBlockPos(BlockPos blockPos) {
      if (mc.world == null) {
         return false;
      }

      for (Direction direction : Direction.values()) {
         if (!mc.world.getBlockState(blockPos.offset(direction)).getBlock().equals(Blocks.BEDROCK)) {
            return false;
         }
      }

      return true;
   }

   public void handlePlayerEntity(PlayerEntity playerEntity, ClientWorld clientWorld) {
      switch ((PhaseHighlightModule.Extension)this.extension.getValue()) {
         case Blocked:
            BlockPos blockPos2 = playerEntity.getBlockPos();

            for (Direction direction : DirectionUtils.directionArray) {
               BlockPos blockPos = blockPos2.offset(direction);
               this.handleClientWorld(clientWorld, blockPos);
               this.handleClientWorld(clientWorld, blockPos.offset(DirectionUtils.resolveDirection2(direction)));
            }
            break;
         case Single:
            for (BlockPos blockPos4 : BlockPosUtils.resolveSet3(playerEntity, playerEntity.getBoundingBox().minY + 0.25)) {
               for (Direction direction2 : DirectionUtils.directionArray) {
                  BlockPos blockPos3 = blockPos4.offset(direction2);
                  this.handleClientWorld(clientWorld, blockPos3);
                  this.handleClientWorld(clientWorld, blockPos3.offset(DirectionUtils.resolveDirection2(direction2)));
               }
            }
      }
   }

   public void handlePredicate(Predicate<BlockPos> predicate) {
      this.blockPosList.removeIf(predicate);
      this.blockPosList2.removeIf(predicate);
      this.blockPosList3.removeIf(predicate);
      this.blockPosList4.removeIf(predicate);
   }

   public void handleMutableBox(MutableBox mutableBox, List<BlockPos> list, Color color, Color color2, float f) {
      for (BlockPos blockPos : list) {
         mutableBox.handleBlockPos(blockPos);
         mutableBox.setDoubleValue5(mutableBox.getDoubleValue());
         long l = BlockUtils.computeObject(this, blockPos);
         Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.testObject(l));
         Render3d.handleCuboid(
            Render3d.Cuboid.createBuilder()
               .resolveBuilder11(mutableBox.createBox())
               .resolveBuilder18(color, color2)
               .resolveBuilder12(f)
               .resolveBuilder14(l)
               .resolveBuilder6(this.fade, this.time)
               .getCuboid()
         );
      }
   }

   public boolean testBlockPos2(BlockPos blockPos, ClientPlayerEntity clientPlayerEntity) {
      Box box = new Box(blockPos);
      Box box2 = clientPlayerEntity.getBoundingBox().expand(this.threshold.getValue());
      return box.intersects(box2);
   }

   private enum Extension {
      Single,
      Blocked;
   }
}
