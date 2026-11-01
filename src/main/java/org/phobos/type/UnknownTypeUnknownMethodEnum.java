package org.phobos.type;

import org.phobos.core.script.ScriptManager;
import org.phobos.core.script.ScriptManager;
import org.phobos.core.script.ScriptManager;
import org.phobos.core.script.ScriptManager;
import org.phobos.core.script.ScriptManager;
import org.phobos.core.script.ScriptManager;

public enum UnknownTypeUnknownMethodEnum {
   UnknownType("Unknown type", new ScriptManager.UnknownType()),
   UnknownMethod("Unknown method", new ScriptManager.UnknownMethod()),
   UnknownVar("Unknown variable", new ScriptManager.UnknownVar()),
   VariableIsImmutable("Variable is immutable", new ScriptManager.VariableIsImmutable()),
   UnknownArg("Unknown argument", new ScriptManager.UnknownArg()),
   MethodPipelineException("Method is not closed", new ScriptManager.MethodPipelineException());

   public final RuntimeException exception;
   public final String message;

   public String getMessage() {
      return this.message;
   }

   public RuntimeException getException() {
      return this.exception;
   }

   UnknownTypeUnknownMethodEnum(final String message, final RuntimeException exception) {
      this.message = message;
      this.exception = exception;
   }
}
