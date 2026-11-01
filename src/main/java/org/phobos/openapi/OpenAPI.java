package org.phobos.openapi;

import org.phobos.api.Exposed;
import org.phobos.core.openapi.OpenApiHolder;
import org.phobos.core.openapi.OpenApiException;

@Exposed
public interface OpenAPI {
   static OpenAPIProvider getProvider() throws OpenApiException {
      return OpenApiHolder.getOpenApiHolder();
   }
}
