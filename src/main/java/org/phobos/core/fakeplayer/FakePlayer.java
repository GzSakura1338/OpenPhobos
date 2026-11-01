package org.phobos.core.fakeplayer;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;
import org.phobos.api.TimeGate;

public class FakePlayer extends AbstractClientPlayerEntity {
   public static final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "FakePlayer");
   @Nullable
   public PathRecorder pathRecorder;
   public final TimeGate timeGate = new TimeGate.Normal();
   public static final PlayerListEntry playerListEntry = new PlayerListEntry(FakePlayer.gameProfile, true);
   public boolean flag = true;
   public SkinTextures skinTextures;
   public int intValue;
   public boolean flag2 = false;

   public int getIntValue2() {
      return this.intValue;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public SkinTextures getSkinTextures2() {
      return this.skinTextures;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public FakePlayer(ClientWorld clientWorld, GameProfile gameProfile) {
      super(clientWorld, gameProfile);
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public FakePlayer(ClientWorld clientWorld, String text) {
      super(clientWorld, new GameProfile(UUID.randomUUID(), text));
   }

   public void setSkinTextures(SkinTextures skinTextures) {
      this.skinTextures = skinTextures;
   }

   public void run() {
      if (this.pathRecorder == null) {
         this.pathRecorder = new PathRecorder();
      }
   }

   public void pushAwayFrom(Entity entity) {
   }

   public void setPathRecorder(@Nullable PathRecorder pathRecorder) {
      this.pathRecorder = pathRecorder;
   }

   @Nullable
   public PathRecorder getPathRecorder() {
      return this.pathRecorder;
   }

   public FakePlayer(ClientWorld clientWorld) {
      super(clientWorld, gameProfile);
   }

   public void pushOutOfBlocks(double x, double y, double z) {
   }

   public TimeGate getTimeGate() {
      return this.timeGate;
   }

   public void setFlag2(boolean flag) {
      this.flag = flag;
   }

   public void setFlag22(boolean flag) {
      this.flag2 = flag;
   }

   public boolean isSpectator() {
      return false;
   }

   public PlayerListEntry getPlayerListEntry() {
      return playerListEntry;
   }
}
