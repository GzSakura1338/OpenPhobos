package org.phobos.core.prediction;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.math.MathUtils;

public class PlayerMovementTracker {
   public final FlagPair flagPair = new FlagPair();
   public final AbstractClientPlayerEntity abstractClientPlayerEntity;

   public boolean testList(List<SampleBuffer.Sample> list) {
      double d = -0.1;
      double d2 = list.getFirst().y();

      for (SampleBuffer.Sample sample : list) {
         if (sample.y() < d2 && d2 - sample.y() > d) {
            return true;
         }

         d2 = sample.y();
      }

      return false;
   }

   public void handleClientWorld(ClientWorld clientWorld) {
      List<SampleBuffer.Sample> list = Lists.newArrayList(((IAbstractClientPlayerEntity)(Object)this.abstractClientPlayerEntity).getHistory().getSampleList());
      if (!list.isEmpty()) {
         this.handleClientWorld2(clientWorld, list);
         this.flagPair.run();
      }
   }

   public boolean testList2(List<SampleBuffer.Sample> list) {
      double d2 = 1.0;
      int i2 = 0;

      for (int i = 1; i < list.size(); i++) {
         SampleBuffer.Sample sample = list.get(i);
         SampleBuffer.Sample sample2 = list.get(i - 1);
         if (sample.onGround() || sample2.onGround()) {
            return false;
         }

         double d = Math.abs(sample.y() - sample2.y());
         if (!(d < d2)) {
            i2++;
         }
      }

      return i2 >= list.size() * 0.4;
   }

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   public boolean testClientWorld(ClientWorld clientWorld) {
      for (BlockPos blockPos : BlockPosUtils.resolveSet6(this.abstractClientPlayerEntity.getBoundingBox())) {
         if (!clientWorld.getBlockState(blockPos).getFluidState().isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public void handleList(List<SampleBuffer.Sample> list, Consumer<FlagGroup> consumer) {
      FlagGroup flagGroup = new FlagGroup();
      double d2 = 0.0;
      double d3 = 0.0;

      for (SampleBuffer.Sample sample : list) {
         double d = Math.toRadians(sample.yaw());
         d2 += Math.sin(d);
         d3 += Math.cos(d);
      }

      int i = list.size();
      double d4 = d2 / i;
      double d5 = d3 / i;
      flagGroup.handleDouble2(d4);
      flagGroup.handleDouble(d5);
      consumer.accept(flagGroup);
   }

   public void handleClientWorld2(ClientWorld clientWorld, List<SampleBuffer.Sample> list) {
      boolean flag = this.testList3(list);
      boolean flag2 = this.testList(list);
      boolean flag3 = this.testClientWorld(clientWorld);
      boolean flag4 = this.testList2(list);
      this.flagPair.getFlagGroup2().handleFlag(flag, flag2, flag3, flag4);
      this.handleList(list, flagGroup -> flagGroup.handleFlagGroup(this.flagPair.getFlagGroup()));
   }

   public PlayerMovementTracker(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
   }

   public FlagPair getFlagPair() {
      return this.flagPair;
   }

   public boolean testList3(List<SampleBuffer.Sample> list) {
      for (SampleBuffer.Sample sample : list) {
         if (MathUtils.testDouble(sample.velocity().getY(), 0.42, 0.1)) {
            return true;
         }
      }

      return false;
   }
}
