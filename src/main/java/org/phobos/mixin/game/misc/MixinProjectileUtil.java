package org.phobos.mixin.game.misc;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.phobos.core.ClientHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileUtil.class)
public abstract class MixinProjectileUtil {
   @Redirect(
      method = "raycast",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"
      )
   )
   private static List<Entity> raycastPatch(World instance, Entity except, Box box, Predicate<? super Entity> predicate) {
      ClientWorld world = ClientHolder.getPhobosClient().getWorldHolder().getClientWorld();

      try {
         return world.getOtherEntities(except, box, predicate);
      } catch (Throwable throwable) {
         return Lists.newArrayList();
      }
   }
}
