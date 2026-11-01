package org.phobos.core.proxy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.handler.proxy.Socks5ProxyHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Named;
import org.phobos.api.JsonSupport;
import org.phobos.api.JsonSerializable;
import org.phobos.event.AddHandlersEvent;
import org.phobos.event.Listener;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.ClientHolder;
import org.phobos.core.config.CloudProxyRef;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.gui.screen.ProxyScreen;

public class ProxyManager extends Feature implements Named, JsonSerializable, JsonSupport, Iterable<ProxyConfig> {
   public static boolean flag;
   public final MutableObject<ProxyConfig> proxyConfigMutableObject = new MutableObject(null);
   public static int intValue;
   public static final Logger LOGGER = LoggerFactory.getLogger(ProxyManager.class);
   public static final Path path = Path.of(System.getProperty("user.home"), "earthhack");
   public final List<ProxyConfig> proxyConfigList = new ArrayList<>();
   public final PhobosClient phobosClient;
   public static int intValue2;

   public void handleProxyConfig(ProxyConfig proxyConfig) {
      this.proxyConfigMutableObject.setValue(proxyConfig);
      proxyConfig.setFlag3(true);
   }

   public void run8() {
      this.run7();

      try {
         Files.writeString(path.resolve(this.getName()), gson.toJson(this.createJsonPrimitive()));
      } catch (IOException var2) {
      }
   }

   public void run7() {
      if (!path.toFile().exists()) {
         path.toFile().mkdirs();
      }
   }

   public void handleText(String text, String text2, String text3, String text4, String text5, String text6) {
      if (!this.proxyConfigList.stream().anyMatch(proxyConfigx -> proxyConfigx.getName().equals(text))) {
         try {
            int i = Integer.parseInt(text4);
            boolean flag = text5 != null && text6 != null;
            ProxyConfig.Builder builder = new ProxyConfig.Builder();
            builder.resolveBuilder6(text).resolveBuilder(text2).resolveBuilder3(i).resolveBuilder4(ProxyUtils.resolveProxyHandlerFactory(text3));
            if (flag) {
               builder.resolveBuilder2(text5, text6);
            }

            ProxyConfig proxyConfig2 = builder.getProxyConfig();
            this.proxyConfigList.add(proxyConfig2);
            if (ProxyScreen.proxyScreen != null) {
               ProxyScreen.proxyScreen.run3();

               for (ProxyConfig proxyConfig : this) {
                  ProxyScreen.proxyScreen.handleProxyConfig(proxyConfig);
               }
            }
         } catch (Throwable throwable) {
            throwable.printStackTrace();
         }
      }
   }

   @Nullable
   public static Socks5ProxyHandler createSocks5ProxyHandler(ProxyConfig proxyConfig) {
      if (proxyConfig == null) {
         return null;
      } else {
         return proxyConfig.isFlag()
            ? new Socks5ProxyHandler(proxyConfig.createInetSocketAddress(), proxyConfig.getText3(), proxyConfig.getText4())
            : new Socks5ProxyHandler(proxyConfig.createInetSocketAddress());
      }
   }

   @Override
   public String getName() {
      return "proxies.json";
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (ProxyConfig proxyConfig : this) {
         try {
            jsonObject.add(proxyConfig.getName(), proxyConfig.createJsonPrimitive());
         } catch (Throwable throwable) {
            throwable.printStackTrace();
            ClientHolder.getPhobosClient().handleText5("ProxyManager", "Unable to save %s".formatted(proxyConfig.getText22()));
         }
      }

      return jsonObject;
   }

   public void handleProxyConfig2(ProxyConfig proxyConfig) {
      this.proxyConfigList.add(proxyConfig);
      this.run6();
   }

   @Nullable
   public ProxyConfig resolveProxyConfig(String text) {
      for (ProxyConfig proxyConfig : this.proxyConfigList) {
         if (proxyConfig.getName().startsWith(text)) {
            return proxyConfig;
         }
      }

      return null;
   }

   public boolean testText(String text) {
      MutableObject<Boolean> mutableObject = new MutableObject(false);
      this.proxyConfigList.removeIf(proxyConfig -> {
         if (proxyConfig.getName().equals(text)) {
            mutableObject.setValue(true);
            return true;
         } else {
            return false;
         }
      });
      if ((Boolean)mutableObject.getValue()) {
         this.run6();
      }

      return (Boolean)mutableObject.getValue();
   }

   public boolean testProxyConfig(ProxyConfig proxyConfig) {
      return this.proxyConfigMutableObject.getValue() != null && ((ProxyConfig)this.proxyConfigMutableObject.getValue()).equals(proxyConfig);
   }

   public void run2() {
      if (this.proxyConfigMutableObject.getValue() != null) {
         ((ProxyConfig)this.proxyConfigMutableObject.getValue()).setFlag3(false);
         this.proxyConfigMutableObject.setValue(null);
      }
   }

   public void run3() {
      this.run7();
      Path path = ProxyManager.path.resolve(this.getName());
      if (path.toFile().exists()) {
         try {
            String text = Files.readString(path);
            JsonElement jsonElement = JsonParser.parseString(text);
            this.handleJsonElement(jsonElement);
         } catch (IOException var4) {
         }
      }
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (String text : jsonObject.keySet()) {
         JsonElement jsonElement2 = jsonObject.get(text);
         if (jsonElement2 != null) {
            try {
               ProxyConfig proxyConfig = new ProxyConfig(text);
               proxyConfig.handleJsonElement(jsonElement2);
               this.proxyConfigList.add(proxyConfig);
            } catch (Throwable throwable) {
               throwable.printStackTrace();
               ClientHolder.getPhobosClient().handleText5("ProxyManager", "Unable to load %s".formatted(jsonElement2));
            }
         }
      }

      if (ProxyScreen.proxyScreen != null) {
         ProxyScreen.proxyScreen.run3();

         for (ProxyConfig proxyConfig2 : this) {
            ProxyScreen.proxyScreen.handleProxyConfig(proxyConfig2);
         }
      }
   }

   public void handleProxyConfig3(ProxyConfig proxyConfig) {
      if (this.proxyConfigMutableObject.getValue() != null && ((ProxyConfig)this.proxyConfigMutableObject.getValue()).equals(proxyConfig)) {
         this.proxyConfigMutableObject.setValue(null);
         proxyConfig.setFlag3(false);
      }
   }

   public void run() {
      this.phobosClient.getCloudConfigApi().handleConsumer3(list -> {
         ProxyScreen.proxyScreen.run();
         flag = true;
         intValue2 = 0;
         intValue = list.size();

         for (CloudProxyRef cloudProxyRef : list) {
            String text = cloudProxyRef.getName();
            String text2 = cloudProxyRef.getText();
            PUBLICPRIVATEEnum pUBLICPRIVATEEnum = cloudProxyRef.getPUBLICPRIVATEEnum();
            this.phobosClient.getCloudConfigApi().resolveCompletableFuture(text, ConfigScriptEnum.Proxy, text3 -> {
               intValue2++;

               try {
                  JsonElement jsonElement = JsonParser.parseString(text3);
                  if (jsonElement == null) {
                     return;
                  }

                  ProxyConfig proxyConfig = new ProxyConfig(text);
                  proxyConfig.setFlag2(true);
                  proxyConfig.setText(text2);
                  proxyConfig.setPUBLICPRIVATEEnum(pUBLICPRIVATEEnum);
                  proxyConfig.handleJsonElement(jsonElement);
                  ProxyScreen.proxyScreen.handleProxyConfig(proxyConfig);
               } catch (Throwable throwable) {
                  throwable.printStackTrace();
               }

               if (intValue2 >= intValue) {
                  flag = false;
               }
            });
         }
      });
   }

   public void run6() {
      if (ProxyScreen.proxyScreen != null) {
         ProxyScreen.proxyScreen.run3();

         for (ProxyConfig proxyConfig : this) {
            ProxyScreen.proxyScreen.handleProxyConfig(proxyConfig);
         }
      }
   }

   public boolean testProxyConfig2(ProxyConfig proxyConfig) {
      if (this.proxyConfigList.stream().anyMatch(proxyConfig2 -> proxyConfig2.getName().equals(proxyConfig.getName()))) {
         return false;
      }

      this.handleProxyConfig2(proxyConfig);
      return true;
   }

   public static void run9() {
      ClientHolder.getPhobosClient().getProxyManager().run();
   }

   @NotNull
   @Override
   public Iterator<ProxyConfig> iterator() {
      return this.proxyConfigList.iterator();
   }

   public ProxyManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      this.handleListener(
         new Listener<AddHandlersEvent>() {
            public void handleObject2(AddHandlersEvent addHandlersEvent) {
               if (!addHandlersEvent.isFlag()) {
                  Socks5ProxyHandler socks5ProxyHandler;
                  if ((
                        socks5ProxyHandler = ProxyManager.createSocks5ProxyHandler(
                           (ProxyConfig)ProxyManager.this.proxyConfigMutableObject.getValue()
                        )
                     )
                     != null) {
                     socks5ProxyHandler.setConnectTimeoutMillis(30000L);
                     if (addHandlersEvent.getChannelPipeline().get("proxy") != null) {
                        return;
                     }

                     addHandlersEvent.getChannelPipeline().addFirst("proxy", socks5ProxyHandler);
                  }
               }
            }
         }
      );
   }
}
