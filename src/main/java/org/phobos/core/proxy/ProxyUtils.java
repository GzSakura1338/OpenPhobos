package org.phobos.core.proxy;

import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import org.phobos.api.ProxyHandlerFactory;

public class ProxyUtils {
   public static ProxyHandlerFactory<HttpProxyHandler> httpProxyHandlerProxyHandlerFactory = proxyConfig -> proxyConfig.isFlag()
         && proxyConfig.getText3() != null
         && proxyConfig.getText4() != null
      ? new HttpProxyHandler(proxyConfig.createInetSocketAddress(), proxyConfig.getText3(), proxyConfig.getText4())
      : new HttpProxyHandler(proxyConfig.createInetSocketAddress());
   public static ProxyHandlerFactory<Socks5ProxyHandler> socks5ProxyHandlerProxyHandlerFactory = proxyConfig -> proxyConfig.isFlag()
         && proxyConfig.getText3() != null
         && proxyConfig.getText4() != null
      ? new Socks5ProxyHandler(proxyConfig.createInetSocketAddress(), proxyConfig.getText3(), proxyConfig.getText4())
      : new Socks5ProxyHandler(proxyConfig.createInetSocketAddress());

   public static String describeProxyHandlerFactory(ProxyHandlerFactory<?> proxyHandlerFactory) {
      return proxyHandlerFactory.equals(httpProxyHandlerProxyHandlerFactory) ? "http" : "socks5";
   }

   public static ProxyHandlerFactory<?> resolveProxyHandlerFactory(String text) {
      return text.equals("http") ? httpProxyHandlerProxyHandlerFactory : socks5ProxyHandlerProxyHandlerFactory;
   }
}
