package org.phobos.core.server;

public class Endpoint {
   public final String text;
   public final int port;

   public String getText() {
      return this.text;
   }

   public Endpoint(String text, int i) {
      this.text = text;
      this.port = i;
   }

   public int getPort() {
      return this.port;
   }
}
