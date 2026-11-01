package org.phobos.api;

import org.phobos.core.proxy.ProxyConfig;

public interface ProxyHandlerFactory<HANDLER> {
   HANDLER resolveObject(ProxyConfig var1);
}
