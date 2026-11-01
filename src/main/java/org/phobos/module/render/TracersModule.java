package org.phobos.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class TracersModule extends Module {
   public final Setting<Color> hostilesColor;
   public final Setting<TracersModule.Bone> bone;
   public final Setting<Color> playersColor;
   public final Setting<Color> bossesColor;
   public final Setting<Boolean> bosses;
   public final Setting<Color> animalsColor;
   public final Setting<Float> width;
   public final Setting<Boolean> players = this.resolveSetting2(new BooleanSetting("Players").resolveSetting7("Players"));
   public final Setting<Color> enemies;
   public final Setting<Boolean> hostiles;
   public final Setting<Boolean> animals;
   public final Setting<Color> friends;

   public TracersModule(PhobosClient phobosClient) {
      super(phobosClient, "Tracers", Categories.render, "Draws lines to entities");
      this.playersColor = this.resolveSetting2(new ColorSetting("PlayersColor", Color.WHITE));
      this.friends = this.resolveSetting2(new ColorSetting("Friends", Color.CYAN).resolveSetting7("Friend color"));
      this.enemies = this.resolveSetting2(new ColorSetting("Enemies", Color.RED).resolveSetting7("Enemy color"));
      this.hostiles = this.resolveSetting2(new BooleanSetting("Hostiles").resolveSetting7("Hostiles (zombies, phantoms)"));
      this.hostilesColor = this.resolveSetting2(new ColorSetting("HostilesColor", Color.RED));
      this.animals = this.resolveSetting2(new BooleanSetting("Animals").resolveSetting7("Animals (cows, chickens, etc)"));
      this.animalsColor = this.resolveSetting2(new ColorSetting("AnimalsColor", Color.GREEN));
      this.bosses = this.resolveSetting2(new BooleanSetting("Bosses").resolveSetting7("Bosses (wither, dragon, etc)"));
      this.bossesColor = this.resolveSetting2(new ColorSetting("BossesColor", Color.BLACK));
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F).resolveSetting7("Line width"));
      this.bone = this.resolveSetting2(new EnumSetting<>("Bone", TracersModule.Bone.Feet));
      this.resolveSettingGroup(this.players, this.playersColor, this.friends, this.enemies);
      this.resolveSettingGroup(this.hostiles, this.hostilesColor);
      this.resolveSettingGroup(this.animals, this.animalsColor);
      this.resolveSettingGroup(this.bosses, this.bossesColor);
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               List<Line> list = new ArrayList<>();
               float f = mc.getRenderTickCounter().getTickDelta(false);
               Vec3d vec3d2 = Vec3d.fromPolar(clientPlayerEntity.getPitch(f), clientPlayerEntity.getYaw(f)).multiply(0.2);
               Vec3d vec3d3 = Render3d.vec3d.add(vec3d2);

               for (Entity entity : clientWorld.getEntities()) {
                  if (!entity.equals(clientPlayerEntity)) {
                     Vec3d vec3d = TracersModule.this.bone.getValue().getTarget(entity, f);
                     if (entity instanceof PlayerEntity playerEntity && TracersModule.this.players.getValue()) {
                        Color color = phobosClient.getFriendList().testUUID(playerEntity.getUuid())
                           ? TracersModule.this.friends.getValue()
                           : (
                              phobosClient.getEnemyList().testUUID(playerEntity.getUuid())
                                 ? TracersModule.this.enemies.getValue()
                                 : TracersModule.this.playersColor.getValue()
                           );
                        list.add(new Line(vec3d3, vec3d, color));
                     }

                     if (entity instanceof HostileEntity && TracersModule.this.hostiles.getValue()) {
                        list.add(new Line(vec3d3, vec3d, TracersModule.this.hostilesColor.getValue()));
                     }

                     if (entity instanceof AnimalEntity && TracersModule.this.animals.getValue()) {
                        list.add(new Line(vec3d3, vec3d, TracersModule.this.animalsColor.getValue()));
                     }

                     if ((entity instanceof WitherEntity || entity instanceof EnderDragonEntity || entity instanceof WardenEntity)
                        && TracersModule.this.bosses.getValue()) {
                        list.add(new Line(vec3d3, vec3d, TracersModule.this.bossesColor.getValue()));
                     }
                  }
               }

               if (!list.isEmpty()) {
                  Render3d.handleLines(Lines.createBuilder().resolveBuilder4(list).resolveBuilder2(TracersModule.this.width.getValue()).getLines());
               }
            }
         }
      );
   }

   private enum Bone {
      Feet {
         @Override
         public Vec3d getTarget(Entity entity, float delta) {
            return this.interpolate(entity, delta);
         }
      },
      Body {
         @Override
         public Vec3d getTarget(Entity entity, float delta) {
            return this.interpolate(entity, delta).add(0.0, entity.getHeight() / 2.0F, 0.0);
         }
      },
      Eyes {
         @Override
         public Vec3d getTarget(Entity entity, float delta) {
            return this.interpolate(entity, delta).add(0.0, entity.getEyeHeight(entity.getPose()), 0.0);
         }
      };

      public Vec3d interpolate(Entity entity, float delta) {
         return EntityInterpolation.createVec3d(entity, delta);
      }

      public abstract Vec3d getTarget(Entity var1, float var2);
   }
}
