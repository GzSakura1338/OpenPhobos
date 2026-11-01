package org.phobos.module.render;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.entity.DeadEntityTracker.SetDeadType;
import org.phobos.event.AttackEvent2;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class SetDeadModule extends Module {
   public SetDeadModule(PhobosClient phobosClient) {
      super(phobosClient, "SetDead", Categories.render, "Stops rendering crystals after attack");
      this.handleListener(
         new SafeListener<AttackEvent2>() {
            public void handleObject(
               AttackEvent2 attackEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (attackEvent2.getEntity() instanceof EndCrystalEntity) {
                  if (clientPlayerEntity.hasStatusEffect(StatusEffects.WEAKNESS)
                     && !(clientPlayerEntity.getMainHandStack().getItem() instanceof SwordItem)
                     && !(clientPlayerEntity.getMainHandStack().getItem() instanceof ToolItem)) {
                     return;
                  }

                  phobosClient.getDeadEntityTracker().handleSetDeadType(SetDeadType.Visibility, attackEvent2.getEntity());
               }
            }
         }
      );
   }
}
