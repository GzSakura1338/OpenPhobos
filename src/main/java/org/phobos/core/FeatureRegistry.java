package org.phobos.core;

import org.phobos.module.client.EngineModule;
import org.phobos.module.Module;
import org.phobos.core.combat.DamageTracker;
import org.phobos.core.event.EventBus;
import org.phobos.core.module.ModuleLookup;
import org.phobos.core.movement.MovementTracker;
import org.phobos.core.player.PlayerAtPosTracker;
import org.phobos.core.prediction.PredictionManager;
import org.phobos.core.social.TeamManager;

public class FeatureRegistry {
   public PredictionManager predictionManager;
   public TeamManager teamManager;
   public PhobosClient phobosClient;
   public MovementTracker movementTracker;
   public EngineModule engineModule;
   public DamageTracker damageTracker;
   public PlayerAtPosTracker playerAtPosTracker;

   public FeatureRegistry(PhobosClient phobosClient) {
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      Module var2 = ModuleLookup.resolveModule(EngineModule.class);
      boolean var3 = var2 instanceof EngineModule;
      if (var2 != null && !var3) {
         throw new ClassCastException("CHECKCAST failed");
      }

      this.engineModule = (EngineModule)var2;
      this.phobosClient = var1;
      DamageTracker var4 = new DamageTracker(var1, this);
      this.damageTracker = var4;
      PlayerAtPosTracker var5 = new PlayerAtPosTracker(var1, this);
      this.playerAtPosTracker = var5;
      PredictionManager var6 = new PredictionManager(this);
      this.predictionManager = var6;
      TeamManager var7 = new TeamManager(var1, this);
      this.teamManager = var7;
      MovementTracker var8 = new MovementTracker(var1, this);
      this.movementTracker = var8;
   }

   public PlayerAtPosTracker getPlayerAtPosTracker() {
      return this.playerAtPosTracker;
   }

   public TeamManager getTeamManager() {
      return this.teamManager;
   }

   public PredictionManager getPredictionManager() {
      return this.predictionManager;
   }

   public void handleEventBus(EventBus eventBus) {
      eventBus.handleObject(this);
      DamageTracker var2 = this.damageTracker;
      eventBus.handleObject(var2);
      PlayerAtPosTracker var3 = this.playerAtPosTracker;
      eventBus.handleObject(var3);
      PredictionManager var4 = this.predictionManager;
      eventBus.handleObject(var4);
      MovementTracker var5 = this.movementTracker;
      eventBus.handleObject(var5);
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public EngineModule getEngineModule() {
      return EngineModule.getEngineModule();
   }

   public MovementTracker getMovementTracker() {
      return this.movementTracker;
   }

   public DamageTracker getDamageTracker() {
      return this.damageTracker;
   }

   public EngineModule getEngineModule2() {
      return this.engineModule;
   }
}
