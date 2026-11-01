package org.phobos.core.hud;

import java.util.LinkedList;

public class CpsCounter {
   public final LinkedList<Long> longValueLinkedList = new LinkedList<>();

   public void run() {
      this.longValueLinkedList.clear();
   }

   public int value() {
      long l = System.currentTimeMillis();

      try {
         while (!this.longValueLinkedList.isEmpty() && this.longValueLinkedList.peek() != null && this.longValueLinkedList.peek() < l) {
            this.longValueLinkedList.remove();
         }
      } catch (Throwable throwable) {
         this.longValueLinkedList.clear();
      }

      return this.longValueLinkedList.size();
   }

   public void run2() {
      this.longValueLinkedList.add(System.currentTimeMillis() + 1000L);
   }
}
