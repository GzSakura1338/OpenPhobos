package org.phobos.event;

import org.phobos.api.ListenerAdapter;
import org.phobos.core.net.TypeRecordUtils;
import org.phobos.data.TypeRecord;

public abstract class Listener<E> implements Comparable<Listener<E>>, ListenerAdapter<E> {
   public final Class<?> classValue;
   public Runnable runnable;
   public final Class<? super E> valueClassValue;
   public static final int intValue = 10;
   public final int priority;

   public Runnable getRunnable() {
      return this.runnable;
   }

   public Listener(Class<?> classValue, int i) {
      this.classValue = TypeRecordUtils.resolveTypeRecord(this.getClass()).type();
      this.priority = i;
      this.valueClassValue = (Class<? super E>)classValue;
   }

   public void setRunnable(Runnable runnable) {
      this.runnable = runnable;
   }

   public Listener(Class<? super E> classValue, Class<?> classValue2, int i) {
      this.valueClassValue = classValue;
      this.classValue = classValue2;
      this.priority = i;
   }

   public Listener(int i) {
      TypeRecord<E> typeRecord = TypeRecordUtils.resolveTypeRecord(this.getClass());
      this.valueClassValue = typeRecord.type();
      this.classValue = typeRecord.generic();
      this.priority = i;
   }

   public void setRunnable2(Runnable runnable) {
      this.runnable = runnable;
   }

   public int compareTo(Listener<E> listener) {
      return Integer.compare(this.getPriority(), listener.getPriority());
   }

   public abstract void handleObject2(E var1);

   public Listener() {
      TypeRecord<E> typeRecord = TypeRecordUtils.resolveTypeRecord(this.getClass());
      this.valueClassValue = typeRecord.type();
      this.classValue = typeRecord.generic();
      this.priority = 10;
   }

   public int getPriority() {
      return this.priority;
   }

   public Class<? super E> getValueClassValue() {
      return this.valueClassValue;
   }

   public Listener(Class<E> classValue) {
      this.valueClassValue = classValue;
      this.classValue = null;
      this.priority = 10;
   }

   @Override
   public void handleObject3(E object) {
      this.handleObject2(object);
      if (this.runnable != null) {
         this.runnable.run();
      }
   }

   public Class<?> getClassValue() {
      return this.classValue;
   }

   public abstract static class AnyType<E> extends Listener<E> {
      public AnyType(Class<?> classValue, Class<?> classValue2, int i) {
         super((Class<? super E>)classValue, classValue2, i);
      }

      public AnyType(Class<?> classValue) {
         this(classValue, 10);
      }

      public AnyType(Class<?> classValue, Class<?> classValue2) {
         this(classValue, classValue2, 10);
      }

      public AnyType(Class<?> classValue, int i) {
         this(classValue, null, i);
      }
   }
}
