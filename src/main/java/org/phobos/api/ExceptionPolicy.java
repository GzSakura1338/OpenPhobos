package org.phobos.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ExceptionPolicy {
   ExceptionPolicy INSTANCE = new ExceptionPolicy.Robust();
   ExceptionPolicy INSTANCE2 = new ExceptionPolicy.Suppress();
   ExceptionPolicy INSTANCE3 = new ExceptionPolicy.Log();

   void handleException(Exception var1);

   class Log implements ExceptionPolicy {
      public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionPolicy.Log.class);

      @Override
      public void handleException(Exception exception) {
         LOGGER.error(exception.getMessage(), exception);
      }
   }

   class Robust implements ExceptionPolicy {
      @Override
      public void handleException(Exception exception) {
         exception.printStackTrace();
      }
   }

   class Suppress implements ExceptionPolicy {
      @Override
      public void handleException(Exception exception) {
      }
   }
}
