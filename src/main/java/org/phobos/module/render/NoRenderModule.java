package org.phobos.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.ApplyFogEvent;
import org.phobos.event.Listener;
import org.phobos.event.SpawnEvent;
import org.phobos.module.Module;
import org.phobos.module.misc.WeatherModule;

public class NoRenderModule extends Module {
   public final Setting<Boolean> shadow;
   public final Setting<Set<Item>> glint;
   public final Setting<Boolean> boss;
   public static NoRenderModule explosions;
   public final Setting<Set<Block>> blocks = this.resolveSetting2(new BlockSetSetting("Blocks"));

   public static boolean check() {
      return explosions.check7() && explosions.shadow.getValue();
   }

   public void run() {
      if (mc.worldRenderer != null) {
         mc.chunkCullingEnabled = false;
         mc.worldRenderer.reload();
      }
   }

   @Override
   public void run3() {
      mc.submit(() -> {
         if (!this.blocks.getValue().isEmpty()) {
            this.run();
         }
      });
   }

   public static boolean testBlock(Block block) {
      return explosions != null && explosions.check7() ? explosions.blocks.getValue().contains(block) : false;
   }

   @Override
   public void run4() {
      mc.submit(() -> {
         if (mc.worldRenderer != null) {
            mc.chunkCullingEnabled = true;
            mc.worldRenderer.reload();
         }
      });
   }

   public <T extends CancellableEvent> void handleText(String text, String text2, Class<T> classValue) {
      final Setting<Boolean> setting = this.resolveSetting2(new BooleanSetting(text, text2, true));
      this.handleListener(new Listener<T>(classValue) {
         public void handleObject2(T cancellableEvent) {
            if (setting.getValue()) {
               cancellableEvent.setCancelled(true);
            }
         }
      });
   }

   public static boolean check2() {
      return explosions.check7() && explosions.boss.getValue();
   }

   public static boolean testBlockState(BlockState blockState) {
      return testBlock(blockState.getBlock());
   }

   public static boolean testItemStack(ItemStack itemStack, boolean flag) {
      if (!explosions.check7()) {
         return flag;
      } else {
         Set<Item> set = explosions.glint.getValue();
         if (set.isEmpty()) {
            return flag;
         } else {
            return set.contains(itemStack.getItem()) ? false : flag;
         }
      }
   }

   public NoRenderModule(PhobosClient phobosClient) {
      super(phobosClient, "NoRender", Categories.render, "Prevents certain things from rendering");
      this.glint = this.resolveSetting2(new ItemSetSetting("Glint"));
      this.shadow = this.resolveSetting2(new BooleanSetting("Shadow", true).resolveSetting7("removes shadows under entities"));
      this.boss = this.resolveSetting2(new BooleanSetting("Boss", true).resolveSetting7("Removes boss bar"));
      this.blocks.handleConsumer2(set -> {
         if (set.isEmpty()) {
            if (!mc.chunkCullingEnabled) {
               mc.submit(() -> {
                  if (mc.worldRenderer != null) {
                     mc.chunkCullingEnabled = true;
                     mc.worldRenderer.reload();
                  }
               });
            }
         } else {
            mc.submit(this::run);
         }
      });
      this.handleText("Fire", "Prevents fire from rendering", NoRenderModule.Fire.class);
      this.handleText("Inside", "Prevents fire blocks and grass blocks from rendering", NoRenderModule.Blocks.class);
      this.handleText("Totems", "Stops the totem animation", NoRenderModule.Totems.class);
      this.handleText("Skylight", "Prevents Skylight updates", NoRenderModule.Skylight.class);
      this.handleText("Liquids", "Prevents the liquid overlay from rendering", NoRenderModule.Liquids.class);
      this.handleText("Hurtcam", "Stops your screen from shaking when you are hurt", NoRenderModule.Hurt.class);
      this.handleText("Bob", "Stops your screen from bobbing when you move", NoRenderModule.Bob.class);
      this.handleText("Fov", "Stops all effects changing your FOV", NoRenderModule.Fov.class);
      this.handleText("Scoreboard", "Removes scoreboard renderer", NoRenderModule.Scoreboard.class);
      this.handleText("Potions", "Stops rendering effect boxes", NoRenderModule.Potions.class);
      this.handleText("Armor", "Removes armor layers", NoRenderModule.Armor.class);
      this.handleText("Blindness", "Removes blindness", NoRenderModule.Blindness.class);
      this.handleText("Toast", "Removes in-game toast notifications", NoRenderModule.Toast.class);
      this.handleText("Break", "Removes block break particles", NoRenderModule.BlockBreak.class);
      this.handleText("ActionBar", "Removes action bar & item name display", NoRenderModule.ActionBar.class);
      final Setting<Boolean> setting = this.resolveSetting2(new BooleanSetting("Explosions", true).resolveSetting7("Removes explosion particles"));
      final Setting<Boolean> setting2 = this.resolveSetting2(new BooleanSetting("Particles", false).resolveSetting7("Removes all particles"));
      this.handleListener(
         new Listener<SpawnEvent>() {
            public void handleObject2(SpawnEvent spawnEvent) {
               if (setting2.getValue() && !WeatherModule.testParticleType(spawnEvent.getParticleType())
                  || setting.getValue()
                     && (spawnEvent.getParticleType().equals(ParticleTypes.EXPLOSION) || spawnEvent.getParticleType().equals(ParticleTypes.EXPLOSION_EMITTER))) {
                  spawnEvent.setCancelled(true);
               }
            }
         }
      );
      final Setting<Boolean> setting3 = this.resolveSetting2(new BooleanSetting("Fog", "Removes fog.", true));
      this.handleListener(new Listener<ApplyFogEvent>(-1) {
         public void handleObject2(ApplyFogEvent applyFogEvent) {
            if (!applyFogEvent.isCancelled() && setting3.getValue()) {
               if (applyFogEvent.getFogType().equals(FogType.FOG_TERRAIN)) {
                  RenderSystem.setShaderFogStart(applyFogEvent.getFloatValue22() * 4.0F);
                  RenderSystem.setShaderFogEnd(applyFogEvent.getFloatValue22() * 4.25F);
               }
            }
         }
      });
      explosions = this;
   }

   public static final class ActionBar extends CancellableEvent {
   }

   public static final class Armor extends CancellableEvent {
   }

   public static final class Blindness extends CancellableEvent {
   }

   public static final class BlockBreak extends CancellableEvent {
   }

   public static final class Blocks extends CancellableEvent {
   }

   public static final class Bob extends CancellableEvent {
   }

   public static final class Fire extends CancellableEvent {
   }

   public static final class Fov extends CancellableEvent {
   }

   public static final class Hurt extends CancellableEvent {
   }

   public static final class Liquids extends CancellableEvent {
   }

   public static final class Potions extends CancellableEvent {
   }

   public static final class Scoreboard extends CancellableEvent {
   }

   public static final class Skylight extends CancellableEvent {
   }

   public static final class Toast extends CancellableEvent {
   }

   public static final class Totems extends CancellableEvent {
   }
}
