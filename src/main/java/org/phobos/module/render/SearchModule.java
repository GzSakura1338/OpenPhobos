package org.phobos.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.setting.EntityTypeSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.task.LockedExecutor;
import org.phobos.core.render.RenderContext;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class SearchModule extends Module {
   public final Setting<Color> line;
   public final Setting<Set<EntityType<?>>> entities;
   public final Setting<Double> range;
   public final Setting<Color> tracersColor;
   public final Setting<Float> width;
   public final Setting<Set<Block>> selection;
   public final Setting<Float> tracersWidth;
   public final TimeGate.Normal normal;
   public final Setting<Color> box;
   public final Map<BlockPos, Box> blockPosMap;
   public final Setting<Long> update;
   public final Object object = new Object[0];
   public final LockedExecutor lockedExecutor;
   public final Setting<Boolean> tracers;

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      Map<BlockPos, Box> map = new HashMap<>();

      for (int i3 = (int)Math.floor(clientPlayerEntity.getX() - this.range.getValue());
         i3 <= Math.ceil(clientPlayerEntity.getX() + this.range.getValue());
         i3++
      ) {
         for (int i2 = (int)Math.floor(clientPlayerEntity.getY() - this.range.getValue());
            i2 <= Math.ceil(clientPlayerEntity.getY() + this.range.getValue());
            i2++
         ) {
            for (int i = (int)Math.floor(clientPlayerEntity.getZ() - this.range.getValue());
               i <= Math.ceil(clientPlayerEntity.getZ() + this.range.getValue());
               i++
            ) {
               BlockPos blockPos = new BlockPos(i3, i2, i);
               BlockState blockState = clientWorld.getBlockState(blockPos);
               Block block = blockState.getBlock();
               if (this.selection.getValue().contains(block)) {
                  map.put(blockPos, BoxUtils.resolveBox(clientWorld, blockState, blockPos));
               }
            }
         }
      }

      this.blockPosMap.clear();
      this.blockPosMap.putAll(map);
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, boolean flag) {
      synchronized (this.object) {
         if (flag || this.normal.testNumber2(this.update.getValue())) {
            this.lockedExecutor.handleRunnable(() -> this.handleClientPlayerEntity(clientPlayerEntity, clientWorld));
         }
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.blockPosMap.size());
   }

   @Override
   public void run4() {
      this.blockPosMap.clear();
   }

   public SearchModule(PhobosClient phobosClient) {
      super(phobosClient, "Search", Categories.render, "Looks up blocks");
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 150.0, 0.0, 300.0));
      this.update = this.resolveSetting2(new LongNumberSetting("Update", 1000L, 0L, 3000L));
      this.selection = this.resolveSetting2(new BlockSetSetting("Selection"));
      this.entities = this.resolveSetting2(new EntityTypeSetSetting("Entities"));
      this.box = this.resolveSetting2(new ColorSetting("Box", new Color(255, 255, 255, 70)).getColorSetting());
      this.line = this.resolveSetting2(new ColorSetting("Line", new Color(255, 255, 255, 255)).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F).resolveSetting7("Line width"));
      this.tracers = this.resolveSetting2(new BooleanSetting("Tracers"));
      this.tracersColor = this.resolveSetting2(new ColorSetting("TracersColor", Color.WHITE).getColorSetting());
      this.tracersWidth = this.resolveSetting2(new NumberSetting<>("TracersWidth", 1.0F, 0.1F, 3.0F));
      this.lockedExecutor = new LockedExecutor();
      this.blockPosMap = new ConcurrentHashMap<>();
      this.normal = new TimeGate.Normal();
      this.resolveSettingGroup2("Render", this.box, this.line, this.width);
      this.resolveSettingGroup(this.tracers, this.tracersColor, this.tracersWidth);
      this.selection.handleConsumer2(set -> {
         if (mc.player != null && mc.world != null) {
            this.handleClientPlayerEntity2(mc.player, mc.world, true);
         }
      });
      this.handleListener(
         new SafeListener<TickCounter>() {
            public void handleObject(
               TickCounter tickCounter,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               SearchModule.this.handleClientPlayerEntity2(clientPlayerEntity, clientWorld, false);
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
               float f = mc.getRenderTickCounter().getTickDelta(false);
               Vec3d vec3d3 = Vec3d.fromPolar(clientPlayerEntity.getPitch(f), clientPlayerEntity.getYaw(f)).multiply(0.2);
               Vec3d vec3d4 = Render3d.vec3d.add(vec3d3);
               Color color = SearchModule.this.tracersColor.getValue();
               List<Line> list = new ArrayList<>();
               Set<EntityType<?>> set = SearchModule.this.entities.getValue();
               if (!set.isEmpty()) {
                  for (Entity entity : clientWorld.getEntities()) {
                     if (set.contains(entity.getType())) {
                        Box box = EntityInterpolation.resolveBox(entity, f);
                        Render3d.handleCuboid(
                           Cuboid.createBuilder()
                              .resolveBuilder11(box)
                              .resolveBuilder18(SearchModule.this.box.getValue(), SearchModule.this.line.getValue())
                              .resolveBuilder12(SearchModule.this.width.getValue())
                              .getCuboid()
                        );
                        if (SearchModule.this.tracers.getValue()) {
                           Vec3d vec3d = box.getCenter();
                           list.add(new Line(vec3d4, vec3d, color));
                        }
                     }
                  }
               }

               for (Entry<BlockPos, Box> entry : SearchModule.this.blockPosMap.entrySet()) {
                  Render3d.handleCuboid(
                     Cuboid.createBuilder()
                        .resolveBuilder11(entry.getValue())
                        .resolveBuilder18(SearchModule.this.box.getValue(), SearchModule.this.line.getValue())
                        .resolveBuilder12(SearchModule.this.width.getValue())
                        .getCuboid()
                  );
                  if (SearchModule.this.tracers.getValue()) {
                     Vec3d vec3d2 = entry.getKey().toCenterPos();
                     list.add(new Line(vec3d4, vec3d2, color));
                  }
               }

               if (!list.isEmpty()) {
                  Render3d.handleLines(Lines.createBuilder().resolveBuilder4(list).resolveBuilder2(SearchModule.this.tracersWidth.getValue()).getLines());
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this.blockPosMap::clear);
   }
}
