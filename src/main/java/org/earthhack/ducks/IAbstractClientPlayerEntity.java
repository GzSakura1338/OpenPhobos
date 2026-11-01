package org.earthhack.ducks;

import org.phobos.api.PlayerTextureCache;
import org.phobos.core.player.PlayerAnimationState;
import org.phobos.core.prediction.SampleBuffer;
import org.phobos.core.fakeplayer.TrackedFakePlayer;

public interface IAbstractClientPlayerEntity extends org.phobos.mixin.game.entity.ILivingEntity {
   TrackedFakePlayer[] getPredictions();

   int getJumpScore();

   void addJumpScore();

   void setJumpScore(int var1);

   PlayerTextureCache getMovementModel();

   SampleBuffer getHistory();

   PlayerAnimationState getEntityData();
}
