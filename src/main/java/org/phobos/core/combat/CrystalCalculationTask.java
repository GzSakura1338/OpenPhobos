package org.phobos.core.combat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import org.phobos.api.TimeGate;
import org.phobos.module.combat.PlacesCrystalsModule;

public class CrystalCalculationTask {
   public final TimeGate.Threaded threaded;
   public final Object object = new Object[0];
   public final PlacesCrystalsModule placesCrystalsModule;
   public final ExecutorService executorService = Executors.newSingleThreadExecutor();
   public final AtomicReference<Object> objectAtomicReference = new AtomicReference<>();

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, int i, boolean flag, Consumer<CrystalTargetSelector> consumer) {
      synchronized (this.object) {
         if (this.objectAtomicReference.get() == null && this.threaded.testLong(i)) {
            CrystalTargetSelector crystalTargetSelector = this.placesCrystalsModule
               .createValue(this.placesCrystalsModule.getPhobosClient().getWorldHolder().getClientWorld(), clientPlayerEntity);
            consumer.accept(crystalTargetSelector);
            Runnable runnable = () -> {
               try {
                  crystalTargetSelector.run();
               } catch (Exception var6x) {
               } finally {
                  this.objectAtomicReference.set(null);
               }
            };
            this.objectAtomicReference.set(runnable);
            this.threaded.run();
            this.handleRunnable(runnable, flag);
         }
      }
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, int i, boolean flag) {
      this.handleClientPlayerEntity(clientPlayerEntity, i, flag, crystalTargetSelector -> {});
   }

   public CrystalCalculationTask(PlacesCrystalsModule placesCrystalsModule) {
      this.threaded = new TimeGate.Threaded();
      this.placesCrystalsModule = placesCrystalsModule;
   }

   public void handleRunnable(Runnable runnable, boolean flag) {
      if (flag) {
         this.executorService.submit(runnable);
      } else {
         runnable.run();
      }
   }
}
