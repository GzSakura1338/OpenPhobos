package org.phobos.module.client;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.user.UserSession;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class EngineModule extends Module {
   public static EngineModule engineModule;
   public final Setting<Boolean> movement;
   public final Setting<Boolean> movementDebug;
   public final Setting<Boolean> predictionsMachineLearning;
   public final Setting<Double> predictionsMagnitude;
   public final Setting<EngineModule.BreakingLogic> breaking;
   public final Setting<Boolean> predictionsFastFall;
   public final Setting<Boolean> teamsDebug;
   public final Setting<Boolean> teams = new BooleanSetting("Teams").resolveSetting7("Detects teams & friend groups").resolveSetting6(this);
   public final Setting<Boolean> mining;
   public final Setting<Boolean> predictionsStep;
   public final Setting<Boolean> predictions;

   public boolean check() {
      return UserSession.flag3 && this.predictionsFastFall.getValue();
   }

   public Setting<EngineModule.BreakingLogic> getBreaking() {
      return this.breaking;
   }

   public boolean check2() {
      return this.predictions.getValue();
   }

   public Setting<Boolean> getPredictionsStep() {
      return this.predictionsStep;
   }

   public Setting<Boolean> getTeamsDebug() {
      return this.teamsDebug;
   }

   public boolean isFlag42() {
      return UserSession.flag3 && this.predictionsStep.getValue();
   }

   public EngineModule(PhobosClient phobosClient) {
      super(phobosClient, "Engine", Categories.client, "Controls evaluation systems");
      this.teamsDebug = new BooleanSetting("TeamsDebug").resolveSetting6(this);
      this.movement = new BooleanSetting("Movement").resolveSetting7("Runs movement predictions").resolveSetting6(this);
      this.movementDebug = new BooleanSetting("MovementDebug").resolveSetting6(this);
      this.breaking = new EnumSetting<>("Breaking", EngineModule.BreakingLogic.Generic).resolveSetting7("Breaking logic").resolveSetting6(this);
      this.mining = new BooleanSetting("Mining").resolveSetting7("Predicts what positions will be broken").resolveSetting6(this);
      this.predictions = new BooleanSetting("Predictions", true).resolveSetting6(this);
      this.predictionsMagnitude = new NumberSetting<>("PredictionsMagnitude", 100.0, 10.0, 300.0, "%")
         .resolveNumberSetting("Default", 100.0)
         .resolveSetting6(this);
      this.predictionsStep = new BooleanSetting("PredictionsStep").resolveSetting7("Upwards step prediction").getSetting2().resolveSetting6(this);
      this.predictionsFastFall = new BooleanSetting("PredictionsFastFall").resolveSetting7("Fast fall prediction").getSetting2().resolveSetting6(this);
      this.predictionsMachineLearning = new BooleanSetting("PredictionsMachine-Learning")
         .resolveSetting7("Uses position history to evaluate movement behaviour")
         .getSetting2()
         .resolveSetting6(this);
      this.resolveSettingGroup(this.teams, this.teamsDebug);
      this.resolveSettingGroup(this.movement, this.movementDebug);
      this.resolveSettingGroup(this.predictions, this.predictionsMagnitude, this.predictionsStep, this.predictionsFastFall, this.predictionsMachineLearning);
      engineModule = this;
   }

   public Setting<Double> getPredictionsMagnitude() {
      return this.predictionsMagnitude;
   }

   public Setting<Boolean> getMining() {
      return this.mining;
   }

   public boolean check3() {
      return UserSession.flag3 && this.predictionsMachineLearning.getValue();
   }

   public Setting<Boolean> getTeams() {
      return this.teams;
   }

   public Setting<Boolean> getPredictions() {
      return this.predictions;
   }

   public Setting<Boolean> getPredictionsFastFall() {
      return this.predictionsFastFall;
   }

   public Setting<Boolean> getPredictionsMachineLearning() {
      return this.predictionsMachineLearning;
   }

   public boolean check4() {
      return this.breaking.testObject2(EngineModule.BreakingLogic.Compute);
   }

   public boolean check5() {
      return this.mining.getValue();
   }

   public Setting<Boolean> getMovementDebug() {
      return this.movementDebug;
   }

   public static EngineModule getEngineModule() {
      return engineModule;
   }

   public Setting<Boolean> getMovement() {
      return this.movement;
   }

   public enum BreakingLogic {
      Generic,
      Compute;
   }
}
