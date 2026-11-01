package org.phobos.module.render;

import java.awt.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.KeyValue;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class PhaseStatusModule extends Module {
   public final Setting<Color> normalBox;
   public final Setting<Boolean> fade;
   public final Setting<Color> normalLine;
   public final Setting<Color> normalBox2;
   public final Setting<Color> normalLine2;
   public final Setting<Long> fadeTime;
   public final Setting<Float> height;
   public final Setting<Color> normalLine3;
   public final Setting<Float> width;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 8.0, 3.0, 12.0).resolveSetting7("Enemy range"));
   public final Setting<Color> normalBox3;

   @Nullable
   public KeyValue<Color, Color> resolveKeyValue(BlockPos blockPos, ClientWorld clientWorld) {
      BlockState blockState = clientWorld.getBlockState(blockPos);
      if (!BlockUtils.testBlockState(blockState)) {
         return null;
      } else {
         BlockState blockState2 = clientWorld.getBlockState(blockPos.down());
         if (blockState.isOf(Blocks.BEDROCK)) {
            return blockState2.isOf(Blocks.BEDROCK)
               ? KeyValue.createValue(this.normalBox.getValue(), this.normalLine3.getValue())
               : KeyValue.createValue(this.normalBox3.getValue(), this.normalLine2.getValue());
         } else {
            return BlockUtils.testBlockState(blockState2)
               ? KeyValue.createValue(this.normalBox3.getValue(), this.normalLine2.getValue())
               : KeyValue.createValue(this.normalBox2.getValue(), this.normalLine.getValue());
         }
      }
   }

   public PhaseStatusModule(PhobosClient phobosClient) {
      super(phobosClient, "PhaseStatus", Categories.render, "Displays block type");
      this.normalBox = this.resolveSetting2(new ColorSetting("NormalBox", new Color(255, 255, 255, 30)).getColorSetting());
      this.normalLine3 = this.resolveSetting2(new ColorSetting("NormalLine", Color.WHITE).getColorSetting());
      this.normalBox3 = this.resolveSetting2(new ColorSetting("NormalBox", new Color(255, 255, 0, 30)));
      this.normalLine2 = this.resolveSetting2(new ColorSetting("NormalLine", Color.YELLOW));
      this.normalBox2 = this.resolveSetting2(new ColorSetting("NormalBox", new Color(255, 0, 0, 30)));
      this.normalLine = this.resolveSetting2(new ColorSetting("NormalLine", Color.RED));
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.1F, 3.0F));
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 0.0F, -1.0F, 1.0F));
      this.fade = this.resolveSetting2(new BooleanSetting("Fade"));
      this.fadeTime = this.resolveSetting2(new LongNumberSetting("FadeTime", 500L, 0L, 3000L));
      this.createBuilder()
         .resolveBuilder4("Render")
         .resolveBuilder5(
            this.resolveSettingGroup2("Safe", this.normalBox, this.normalLine3),
            this.resolveSettingGroup2("Normal", this.normalBox3, this.normalLine2),
            this.resolveSettingGroup2("Unsafe", this.normalBox2, this.normalLine),
            this.resolveSettingGroup(this.fade, this.fadeTime)
         )
         .resolveBuilder(this.width, this.height)
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (clientPlayerEntity.isOnGround()
                  && BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)
                  && EntityUtils.resolvePlayerEntity(clientWorld, clientPlayerEntity, phobosClient, PhaseStatusModule.this.range.getValue()) != null) {
                  for (BlockPos blockPos : BlockPosUtils.resolveSet3(clientPlayerEntity, clientPlayerEntity.getBoundingBox().minY + 0.25)) {
                     KeyValue<Color, Color> keyValue = PhaseStatusModule.this.resolveKeyValue(blockPos, clientWorld);
                     if (keyValue != null) {
                        long l = BlockUtils.computeObject(PhaseStatusModule.this, blockPos);
                        Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.testObject(l));
                        Render3d.handleCuboid(
                           Cuboid.createBuilder()
                              .resolveBuilder11(BoxUtils.createBox5(blockPos, PhaseStatusModule.this.height.getValue()))
                              .resolveBuilder18(keyValue.getObject(), keyValue.getObject2())
                              .resolveBuilder12(PhaseStatusModule.this.width.getValue())
                              .resolveBuilder6(PhaseStatusModule.this.fade, PhaseStatusModule.this.fadeTime)
                              .resolveBuilder14(l)
                              .getCuboid()
                        );
                     }
                  }
               }
            }
         }
      );
   }
}
