package org.phobos.event;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.phobos.api.ObjectRef;

public class ConnectEvent implements ObjectRef<ConnectEvent> {
   public String text;

   public ConnectEvent resolveConnectEvent(String text) {
      String[] texts = text.split("./");
      String text2 = texts[1];
      String[] texts2 = text2.split(":");
      this.text = texts2[0];
      return this;
   }

   public ConnectEvent resolveConnectEvent2(String text, String text2) {
      text = StringUtils.removeEnd(text, ".");
      String text3 = StringUtils.removeEnd(text, "." + text2);
      if (text3.equals(text)) {
         this.text = text;
         return this;
      } else {
         List<String> list = new ArrayList<>(Lists.reverse(Arrays.asList(text3.split(Pattern.quote(".")))));
         this.text = String.join(".", Lists.reverse(list));
         return this;
      }
   }

   public ConnectEvent resolveObject(Consumer<ConnectEvent> consumer) {
      consumer.accept(this);
      return this;
   }

   public ConnectEvent getObject() {
      if (this.text == null) {
         this.text = "localhost";
      }

      return this;
   }

   public ConnectEvent resolveConnectEvent3(String text) {
      return this.resolveConnectEvent2(text, "phoboslite");
   }
}
