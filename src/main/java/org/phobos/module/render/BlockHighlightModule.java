package org.phobos.module.render;

import java.awt.Color;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.AnimatedBox;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.CancelListener;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.combat.AutoCrystalModule;
import org.phobos.module.player.SpeedmineModule;

public class BlockHighlightModule extends Module {
   public final AnimatedBox animatedBox;
   public final Setting<Boolean> depth;
   public final Setting<Float> speed;
   public final Setting<Boolean> head;
   public final Setting<Color> line;
   public final Setting<Float> width;
   public final MutableObject<Block> blockMutableObject;
   public final Setting<Long> time;
   public final Setting<Boolean> fade;
   public final Setting<Color> box;
   public final Setting<Boolean> animation = this.resolveSetting2(new BooleanSetting("Animation"));

   @Override
   public String nothing() {
      return this.blockMutableObject.getValue() != null ? ((Block)this.blockMutableObject.getValue()).getName().getString() : null;
   }

   public BlockHighlightModule(PhobosClient phobosClient) {
      super(phobosClient, "BlockHighlight", Categories.render, "Highlights the block you're looking at");
      this.speed = this.resolveSetting2(new NumberSetting<>("Speed", 0.2F, 0.0F, 1.0F));
      this.box = this.resolveSetting2(new ColorSetting("Box", new Color(255, 255, 255, 100)).getColorSetting());
      this.line = this.resolveSetting2(new ColorSetting("Line", Color.WHITE).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 2.0F));
      this.depth = this.resolveSetting2(new BooleanSetting("Depth", false));
      this.fade = this.resolveSetting2(new BooleanSetting("Fade", false));
      this.time = this.resolveSetting2(new LongNumberSetting("Time", 500L, 0L, 1000L, true));
      this.head = this.resolveSetting2(new BooleanSetting("Head", true));
      this.animatedBox = new AnimatedBox();
      this.blockMutableObject = new MutableObject();
      this.resolveSettingGroup2("Render", this.box, this.line, this.width, this.depth);
      this.resolveSettingGroup(this.animation, this.speed);
      this.resolveSettingGroup(this.fade, this.time);
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               BlockHighlightModule.this.animatedBox
                  .handleFloat(BlockHighlightModule.this.speed.getValue() * 100.0F, BlockHighlightModule.this.speed.getValue() / 10.0F);
               if (mc.crosshairTarget != null && mc.crosshairTarget.getType().equals(Type.BLOCK) && mc.crosshairTarget instanceof BlockHitResult blockHitResult
                  )
                {
                  BlockPos var13 = blockHitResult.getBlockPos();
                  BlockPos blockPos2 = BlockPos.ofFloored(mc.gameRenderer.getCamera().getPos());
                  if (BlockHighlightModule.this.head.getValue() || !var13.equals(blockPos2)) {
                     SpeedmineModule speedmineModule = SpeedmineModule.getSpeedmineModule();
                     if (!speedmineModule.getPacketBlockBreaker().testBlockPos(var13)) {
                        AutoCrystalModule autoCrystalModule = AutoCrystalModule.getAutoCrystalModule();
                        if (!Objects.equals(autoCrystalModule.getBlockPos(), var13)) {
                           BlockState blockState = clientWorld.getBlockState(var13);
                           BlockHighlightModule.this.blockMutableObject.setValue(blockState.getBlock());
                           long l = BlockUtils.computeObject(BlockHighlightModule.this, var13);
                           if (BlockHighlightModule.this.fade.getValue()) {
                              Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.testObject(l));
                           }

                           if (BlockHighlightModule.this.animation.getValue()) {
                              BlockHighlightModule.this.animatedBox.handleBox(BoxUtils.resolveBox(clientWorld, blockState, var13), false);
                              Render3d.handleCuboid(
                                 Cuboid.createBuilder()
                                    .resolveBuilder11(BlockHighlightModule.this.animatedBox.createBox())
                                    .resolveBuilder18(BlockHighlightModule.this.box.getValue(), BlockHighlightModule.this.line.getValue())
                                    .resolveBuilder12(BlockHighlightModule.this.width.getValue())
                                    .resolveBuilder4(BlockHighlightModule.this.depth.getValue())
                                    .resolveBuilder6(BlockHighlightModule.this.fade, BlockHighlightModule.this.time)
                                    .resolveBuilder14(l)
                                    .getCuboid()
                              );
                           } else {
                              Render3d.handleCuboid(
                                 Cuboid.createBuilder()
                                    .resolveBuilder11(BoxUtils.resolveBox(clientWorld, blockState, var13))
                                    .resolveBuilder18(BlockHighlightModule.this.box.getValue(), BlockHighlightModule.this.line.getValue())
                                    .resolveBuilder12(BlockHighlightModule.this.width.getValue())
                                    .resolveBuilder6(BlockHighlightModule.this.fade, BlockHighlightModule.this.time)
                                    .resolveBuilder4(BlockHighlightModule.this.depth.getValue())
                                    .resolveBuilder14(l)
                                    .getCuboid()
                              );
                           }
                        }
                     }
                  }
               } else {
                  BlockHighlightModule.this.blockMutableObject.setValue(null);
               }
            }
         }
      );
      this.handleListener(new CancelListener<>(BlockHighlightModule.Custom.class));
   }

   public static final class Custom extends CancellableEvent {
   }
}
