package org.phobos.openapi;

import java.util.Optional;
import org.phobos.api.Exposed;

@Exposed
public interface OpenAPIProvider {
   Optional<OpenModule> resolveOptional(String var1);
}
