package org.phobos.core.openapi;

import java.util.Optional;
import org.phobos.module.Module;
import org.phobos.openapi.OpenAPIProvider;
import org.phobos.openapi.OpenModule;
import org.phobos.core.ClientHolder;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.PhobosClient;

public class OpenApiHolder implements OpenAPIProvider {
   private static OpenApiHolder openApiHolder;

   @Override
   public Optional<OpenModule> resolveOptional(String text) {
      PhobosClient var2 = ClientHolder.getPhobosClient();
      ModuleManager var3 = var2.getModuleManager();
      Optional var4 = var3.resolveOptional3(text);
      boolean var5 = var4.isPresent();
      Optional var7;
      if (!var5) {
         Optional var6 = Optional.empty();
         var7 = var6;
      } else {
         Object var8 = var4.get();
         boolean var9 = var8 instanceof Module;
         if (var8 != null && !var9) {
            throw new ClassCastException("CHECKCAST failed");
         }

         OpenModuleImpl var10 = new OpenModuleImpl();
         Optional var11 = Optional.of(var10);
         var7 = var11;
      }

      return var7;
   }

   public static OpenApiHolder getOpenApiHolder() {
      PhobosClient var0 = ClientHolder.getPhobosClient();
      if (var0 != null) {
         PhobosClient var1 = ClientHolder.getPhobosClient();
         ModuleManager var2 = var1.getModuleManager();
         if (var2 != null) {
            OpenApiHolder var4 = getOpenApiHolder2();
            if (var4 == null) {
               OpenApiHolder var5 = new OpenApiHolder();
               setOpenApiHolder(var5);
            }

            return getOpenApiHolder2();
         }
      }

      IllegalStateException var3 = new IllegalStateException("OpenAPI access out of sync!");
      throw var3;
   }

   public static OpenApiHolder getOpenApiHolder2() {
      return openApiHolder;
   }

   public static void setOpenApiHolder(OpenApiHolder var0) {
      openApiHolder = var0;
   }
}
