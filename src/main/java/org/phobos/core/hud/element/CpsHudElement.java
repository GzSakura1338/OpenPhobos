package org.phobos.core.hud.element;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.AddEntityEvent;
import org.phobos.event.DoAttackEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.hud.CpsCounter;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.TextHudElement;

public class CpsHudElement extends TextHudElement {
   public final Map<BlockPos, Long> blockPosMap;
   public final Setting<CpsHudElement.Mode> mode = this.resolveSetting2(
      new EnumSetting<>("Mode", CpsHudElement.Mode.Crystals)
   );
   public final CpsCounter cpsCounter;

   public CpsHudElement() {
      super("CPS");
      this.blockPosMap = new ConcurrentHashMap<>();
      this.cpsCounter = new CpsCounter();
      this.handleListener(
         new SafeListener<Send<PlayerInteractBlockC2SPacket>>(Integer.MIN_VALUE) {
            public void handleObject(
               Send<PlayerInteractBlockC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (CpsHudElement.this.mode.testObject2(CpsHudElement.Mode.Crystals) && !send.isCancelled()) {
                  Hand hand = ((PlayerInteractBlockC2SPacket)send.getPacket()).getHand();
                  if (hand.equals(Hand.MAIN_HAND) && clientPlayerEntity.getMainHandStack().isOf(Items.END_CRYSTAL)
                     || hand.equals(Hand.OFF_HAND) && clientPlayerEntity.getOffHandStack().isOf(Items.END_CRYSTAL)) {
                     CpsHudElement.this.blockPosMap
                        .put(((PlayerInteractBlockC2SPacket)send.getPacket()).getBlockHitResult().getBlockPos(), System.currentTimeMillis() + 5000L);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               CpsHudElement.this.blockPosMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
            }
         }
      );
      this.handleListener(
         new SafeListener<AddEntityEvent>() {
            public void handleObject(
               AddEntityEvent addEntityEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (CpsHudElement.this.mode.testObject2(CpsHudElement.Mode.Crystals)
                  && addEntityEvent.getEntity() instanceof EndCrystalEntity endCrystalEntity) {
                  Vec3d vec3d = endCrystalEntity.getPos();
                  BlockPos blockPos = BlockPos.ofFloored(vec3d.add(0.0, -1.0, 0.0));
                  Long longValue = CpsHudElement.this.blockPosMap.remove(blockPos);
                  if (longValue != null) {
                     CpsHudElement.this.cpsCounter.run2();
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<DoAttackEvent>() {
            public void handleObject(
               DoAttackEvent doAttackEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (CpsHudElement.this.mode.testObject2(CpsHudElement.Mode.Clicks)) {
                  CpsHudElement.this.cpsCounter.run2();
               }
            }
         }
      );
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      int i3 = this.cpsCounter.value();
      String text = this.describeText2("CPS: ", String.valueOf(i3));
      this.handleDrawContext9(drawContext, text);
   }

   public enum Mode {
      Crystals,
      Clicks;
   }
}
