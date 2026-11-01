package org.phobos.core.inventory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.event.Listener;
import org.phobos.event.RunEvent;
import org.phobos.event.SafeListener;
import org.phobos.core.Feature;
import org.phobos.core.util.NullSafe;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.event.events.UnusedEvent;

public class InventoryManager extends Feature {
   public final Lock lock;
   public static ReentrantLock reentrantLock = new ReentrantLock();
   public ItemSwitcher itemSwitcher;
   public final AtomicBoolean atomicBoolean = new AtomicBoolean(true);

   public void handleConsumer(Consumer<ItemSwitcher> consumer, boolean flag) {
      if (!reentrantLock.isLocked()) {
         NullSafe.handleMinecraftClient(mc, (clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> {
            if (this.itemSwitcher == null || !mc.isOnThread()) {
               try {
                  this.lock.lock();
                  ItemSwitcher itemSwitcher2 = new ItemSwitcher(this, clientPlayerEntity, clientPlayerInteractionManager);
                  consumer.accept(itemSwitcher2);
                  itemSwitcher2.run4();
               } finally {
                  this.lock.unlock();
               }
            } else if (flag) {
               ItemSwitcher itemSwitcher = new ItemSwitcher(this, clientPlayerEntity, clientPlayerInteractionManager);
               consumer.accept(itemSwitcher);
               itemSwitcher.run4();
            } else {
               consumer.accept(this.itemSwitcher);
            }
         });
      }
   }

   public Lock getLock() {
      return this.lock;
   }

   public static void handleLock(Lock lock, Runnable runnable) {
      try {
         lock.lock();
         runnable.run();
      } finally {
         lock.unlock();
      }
   }

   public InventoryManager() {
      this.lock = new ReentrantLock();
      this.handleListener(
         new SafeListener<PlayerMoveEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               PlayerMoveEvent playerMoveEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.isOnThread()) {
                  InventoryManager.this.lock.lock();
                  InventoryManager.this.itemSwitcher = new ItemSwitcher(InventoryManager.this, clientPlayerEntity, clientPlayerInteractionManager);
               }
            }
         }
      );
      this.handleListener(new Listener<UnusedEvent>(Integer.MIN_VALUE) {
         public void handleObject2(UnusedEvent unusedEvent) {
            InventoryManager.this.run();
         }
      });
      this.handleListener(new Listener<TickCounter>() {
         public void handleObject2(TickCounter tickCounter) {
            ClickScheduler.getINSTANCE().run2();
            InventoryManager.this.run();
         }
      });
      this.handleListener(new Listener<RunEvent>() {
         public void handleObject2(RunEvent runEvent) {
            if (InventoryManager.this.itemSwitcher != null) {
               InventoryManager.this.run();
            }
         }
      });
   }

   public void handleConsumer2(Consumer<ItemSwitcher> consumer) {
      if (!reentrantLock.isLocked()) {
         this.handleConsumer(consumer, false);
      }
   }

   public ItemSwitcher getItemSwitcher() {
      return this.itemSwitcher;
   }

   public AtomicBoolean getAtomicBoolean() {
      return this.atomicBoolean;
   }

   public void run() {
      if (this.itemSwitcher != null && mc.isOnThread()) {
         this.itemSwitcher.run4();

         try {
            this.lock.unlock();
         } catch (IllegalMonitorStateException var2) {
         }

         this.itemSwitcher = null;
      }
   }
}
