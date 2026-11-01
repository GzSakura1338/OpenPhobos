package org.phobos.core.prediction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.math.MathUtils;

public class SampleBuffer implements Iterable<SampleBuffer.Sample> {
   public final int size;
   public final List<SampleBuffer.Sample> sampleList = new ArrayList<>();

   @NotNull
   @Override
   public Iterator<SampleBuffer.Sample> iterator() {
      return this.sampleList.iterator();
   }

   public void handleInt(int i) {
      List<SampleBuffer.Sample> list = new ArrayList<>();

      for (int i2 = this.sampleList.size() - 1; i2 >= 0; i2--) {
         if (i2 >= i) {
            list.add(this.sampleList.get(i2));
         }
      }

      this.sampleList.clear();
      this.sampleList.addAll(list);
   }

   public void handleInt2(int i) {
      List<SampleBuffer.Sample> list = new ArrayList<>();

      for (int i2 = 0; i2 < this.sampleList.size(); i2++) {
         if (i2 <= i) {
            list.add(this.sampleList.get(i2));
         }
      }

      this.sampleList.clear();
      this.sampleList.addAll(list);
   }

   public boolean check() {
      double d5 = Double.NaN;
      double d6 = Double.NaN;
      double d7 = Double.NaN;

      for (SampleBuffer.Sample sample : this.sampleList) {
         double d = sample.x;
         double d2 = sample.y;
         double d3 = sample.z;
         if (!Double.isNaN(d5) && !Double.isNaN(d6) && !Double.isNaN(d7)) {
            double d4 = MathUtils.computeDouble13(d5, d6, d7, d, d2, d3);
            d5 = d;
            d6 = d2;
            d7 = d3;
            if (d4 > 0.05) {
               return true;
            }
         } else {
            d5 = d;
            d6 = d2;
            d7 = d3;
         }
      }

      return false;
   }

   public List<SampleBuffer.Sample> getSampleList() {
      return this.sampleList;
   }

   public SampleBuffer(int i) {
      this.size = i;
   }

   public void clear() {
      this.sampleList.clear();
   }

   public int getSize() {
      return this.size;
   }

   public void handleEntity(Entity entity) {
      SampleBuffer.Sample sample = new SampleBuffer.Sample(
         entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch(), entity.isOnGround(), entity.getVelocity(), System.currentTimeMillis()
      );
      this.sampleList.add(sample);
      if (this.sampleList.size() > this.size) {
         this.sampleList.removeFirst();
      }
   }

   public SampleBuffer getSampleBuffer() {
      SampleBuffer sampleBuffer = new SampleBuffer(this.size);
      sampleBuffer.sampleList.addAll(this.sampleList);
      return sampleBuffer;
   }

   public boolean check2() {
      float f3 = Float.NaN;
      float f4 = Float.NaN;

      for (SampleBuffer.Sample sample : this.sampleList) {
         float f = sample.yaw;
         float f2 = sample.pitch;
         if (!Float.isNaN(f3) && !Float.isNaN(f4)) {
            double d = MathHelper.angleBetween(f3, f) + MathHelper.angleBetween(f4, f2);
            f3 = f;
            f4 = f2;
            if (d > 0.1) {
               return true;
            }
         } else {
            f3 = f;
            f4 = f2;
         }
      }

      return false;
   }

   public record Sample(double x, double y, double z, float yaw, float pitch, boolean onGround, Vec3d velocity, long timeStamp) {

      public PlayerEntity resolvePlayerEntity(PlayerEntity playerEntity, ClientWorld clientWorld) {
         FakePlayer fakePlayer = new FakePlayer(clientWorld);
         fakePlayer.copyFrom(playerEntity);
         fakePlayer.setPosition(this.x, this.y, this.z);
         fakePlayer.setYaw(this.yaw);
         fakePlayer.setPitch(this.pitch);
         fakePlayer.updatePositionAndAngles(this.x, this.y, this.z, this.yaw, this.pitch);
         return fakePlayer;
      }
   }
}
