package org.phobos.core.kit;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class RegearPlan {
   public final List<SlotMoveResult> slotMoveResultList = new ArrayList<>();
   public final List<RegearSlot> regearSlotList = new ArrayList<>();
   public final List<SlotPair> slotPairList = new ArrayList<>();
   public SlotHandle slotHandle;

   public void setSlotHandle(SlotHandle slotHandle) {
      this.slotHandle = slotHandle;
   }

   public void handleSlot(Slot slot) {
      this.setSlotHandle(new SlotHandle(slot));
   }

   public void handleSlot2(Slot slot) {
      this.regearSlotList.add(new RegearSlot(slot));
   }

   public void handleRegearSingleton(RegearPlan.RegearSingleton regearSingleton) {
      SlotMoveResult slotMoveResult = this.slotMoveResultList.stream().findFirst().orElse(null);
      if (slotMoveResult != null) {
         regearSingleton.handleSlotMoveResult(slotMoveResult, null, null, null);
      } else {
         RegearSlot regearSlot = this.regearSlotList.stream().findFirst().orElse(null);
         if (regearSlot != null) {
            regearSingleton.handleSlotMoveResult(null, regearSlot, null, null);
         } else {
            SlotPair slotPair = this.slotPairList.stream().findFirst().orElse(null);
            if (slotPair != null) {
               regearSingleton.handleSlotMoveResult(null, null, slotPair, null);
            } else {
               regearSingleton.handleSlotMoveResult(null, null, null, this.slotHandle);
            }
         }
      }
   }

   public void handleSlot3(Slot slot, Slot slot2) {
      this.slotPairList.add(new SlotPair(slot, slot2));
   }

   public void handleSlot4(Slot slot, Slot slot2, boolean flag) {
      this.slotMoveResultList.add(new SlotMoveResult(slot, slot2, flag));
   }

   public interface RegearSingleton {
      void handleSlotMoveResult(@Nullable SlotMoveResult var1, @Nullable RegearSlot var2, @Nullable SlotPair var3, @Nullable SlotHandle var4);
   }
}
