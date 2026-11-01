package org.phobos.core.prediction;

import java.util.function.Consumer;

public class TimelineStep {
   public TimelineStep resolveTimelineStep(Runnable runnable, Runnable runnable2, Runnable runnable3) {
      runnable.run();
      runnable2.run();
      runnable3.run();
      return this;
   }

   public TimelineStep resolveTimelineStep2(Runnable runnable) {
      runnable.run();
      return this;
   }

   public static TimelineStep resolveTimelineStep3(Consumer<TimelineStep> consumer, Consumer<Throwable> consumer2) {
      TimelineStep timelineStep = new TimelineStep();

      try {
         consumer.accept(timelineStep);
      } catch (Throwable throwable) {
         consumer2.accept(throwable);
      }

      return timelineStep;
   }
}
