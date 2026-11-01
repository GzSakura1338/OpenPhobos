package org.phobos.core.command;

import org.phobos.api.ProxyHandlerFactory;
import org.phobos.core.util.ExceptionUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.proxy.ProxyConfig;
import org.phobos.core.proxy.ProxyUtils;

public class ProxyCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("add/del");
   public static final CommandSyntax commandSyntax2 = CommandSyntax.createCommandSyntax("http/socks");

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         case "name":
            if (texts.length != 0 && texts[0].equalsIgnoreCase("del")) {
               ProxyConfig proxyConfig = this.phobosClient.getProxyManager().resolveProxyConfig(text2);
               if (proxyConfig != null) {
                  return Suggestion.createSuggestion(proxyConfig.getName());
               }
            }
            break;
         case "type":
            if (texts.length != 0 && texts[0].equalsIgnoreCase("add")) {
               return commandSyntax2.resolveSuggestion(text2);
            }
      }

      return null;
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length > 1;
   }

   @Override
   public void handleString(String[] texts, String text) {
      switch (texts[0].toLowerCase()) {
         case "add":
            if (texts.length != 5 && texts.length != 7) {
               this.handleText2("Usage: proxy add name http/socks5 address port username password");
               return;
            }

            if (texts.length == 5) {
               ExceptionUtils.handleRunnable(
                  () -> {
                     String textx = texts[1];
                     ProxyHandlerFactory<?> proxyHandlerFactory = ProxyUtils.resolveProxyHandlerFactory(texts[2]);
                     String text2x = texts[3];
                     int i = Integer.parseInt(texts[4]);
                     ProxyConfig.Builder builder = new ProxyConfig.Builder();
                     String text3x = this.phobosClient
                           .getProxyManager()
                           .testProxyConfig2(
                              builder.resolveBuilder6(textx).resolveBuilder(text2x).resolveBuilder3(i).resolveBuilder4(proxyHandlerFactory).getProxyConfig()
                           )
                        ? "Successfully added %s".formatted(text2x + ":" + i)
                        : "Could not add proxy.";
                     this.handleText6(text3x);
                  },
                  (throwable, throwableTask) -> {
                     throwableTask.run();
                     this.handleText2("Couldn't add proxy.");
                  }
               );
               return;
            }

            ExceptionUtils.handleRunnable(
               () -> {
                  String textx = texts[1];
                  ProxyHandlerFactory<?> proxyHandlerFactory = ProxyUtils.resolveProxyHandlerFactory(texts[2]);
                  String text2x = texts[3];
                  int i = Integer.parseInt(texts[4]);
                  String text3x = texts[5];
                  String text4 = texts[6];
                  ProxyConfig.Builder builder = new ProxyConfig.Builder();
                  String text5 = this.phobosClient
                        .getProxyManager()
                        .testProxyConfig2(
                           builder.resolveBuilder6(textx)
                              .resolveBuilder(text2x)
                              .resolveBuilder3(i)
                              .resolveBuilder4(proxyHandlerFactory)
                              .resolveBuilder2(text3x, text4)
                              .getProxyConfig()
                        )
                     ? "Successfully added %s -> %s".formatted(text2x + ":" + i, text3x + ":" + text4)
                     : "Could not add proxy.";
                  this.handleText6(text5);
               },
               (throwable, throwableTask) -> {
                  throwableTask.run();
                  this.handleText2("Couldn't add proxy.");
               }
            );
            break;
         case "del":
            if (texts.length != 2) {
               this.handleText2("Usage: proxy del name");
               return;
            }

            String text2 = texts[1];
            String text3 = this.phobosClient.getProxyManager().testText(text2)
               ? "Successfully deleted %s".formatted(text2)
               : "Couldn't delete %s".formatted(text2);
            this.handleText6(text3);
      }
   }

   public ProxyCommand(PhobosClient phobosClient) {
      super(phobosClient, "proxy", "action", "name", "type", "address", "port", "username", "password");
   }
}
