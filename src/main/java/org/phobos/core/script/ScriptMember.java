package org.phobos.core.script;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Fluent;
import org.phobos.api.ScriptObject;
import org.phobos.core.script.ScriptManager.UnknownType;

public class ScriptMember<T> implements Fluent<ScriptMember<T>>, ScriptObject {
   public final Class<? super T> valueClassValue;
   public final List<Script.Var<?>> varValueList;
   public final Script.Arg<?>[] argArray;
   public Object object;
   public boolean flag;
   public boolean flag2;
   public final List<ScriptTypeRef> scriptTypeRefList;
   public final List<Script.Invoker> invokerList;
   public final List<ScriptHook> scriptHookList = new CopyOnWriteArrayList<>();
   public final String name;
   public final Script script;
   public boolean completed;

   public static Script.Arg<?> resolveArg(ScriptMember<?> scriptMember, String text) {
      for (Script.Arg<?> arg : scriptMember.argArray) {
         if (arg.getName().equals(text)) {
            return arg;
         }
      }

      return null;
   }

   @Override
   public List<ScriptMember<?>> getScriptMemberList() {
      return this.script.getScriptMemberList();
   }

   public boolean testClassValue(Class<?> classValue) {
      return classValue.equals(this.valueClassValue);
   }

   public void handleScriptHook(ScriptHook scriptHook) {
      this.scriptHookList.add(scriptHook);
   }

   public void handleObject(Object... objects) {
      this.varValueList.clear();
      if (objects.length > 0 && this.argArray.length > 0) {
         for (int i = 0; i < objects.length; i++) {
            this.argArray[i].setValue(objects[i]);
         }
      }

      for (ScriptHook scriptHook : this.scriptHookList) {
         try {
            this.handleScriptHook2(scriptHook, objects);
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }
   }

   public void handleText(String text) {
      this.handleText2(text);
      this.handleText3(text);
   }

   public void handleText2(String text) {
      String text3 = "^([a-zA-Z_][\\w.]*\\.\\w+|\\w+)\\(.*\\);?$";
      Pattern pattern = Pattern.compile(text3);
      Matcher matcher = pattern.matcher(text.trim());
      if (matcher.matches()) {
         final String text2 = matcher.group(1);
         final Object[] objects = ScriptCompiler.values(ScriptCompiler.S.values2(text));
         this.scriptHookList.add(new ScriptHook("invokedynamic&" + text2) {
            @Override
            public void handleScriptObject(ScriptObject scriptObject, Object... objects) {
               ScriptMember<?> scriptMember = scriptObject.resolveScriptMember(text2);
               if (scriptMember != null) {
                  scriptMember.handleObject(ScriptMember.values(ScriptMember.this, objects));
               }
            }
         });
      }
   }

   public ScriptMember<T> get() {
      return this;
   }

   public void handleList(List<String> list) {
      for (String text : list) {
         this.handleText(text);
      }
   }

   public void handleText3(String text) {
      String text6 = "^(int|integer|double|float|floating|precise|boolean|bool|bl|string)\\s+[a-zA-Z_]\\w*\\s*(=\\s*[^;]*)?;";
      Pattern pattern = Pattern.compile(text6);
      Matcher matcher = pattern.matcher(text.trim());
      if (matcher.matches()) {
         final String text2 = matcher.group(1);
         String text3 = matcher.group(2);
         final String text4 = matcher.group(3);
         final String text5 = matcher.group(4);
         final boolean flag = text5.equals("null") || text5.equals("nil") || text5.equals("nullptr");
         final boolean flag2 = text3 != null;
         this.scriptHookList.add(new ScriptHook("assigndynamic&" + text4) {
            @Override
            public void handleScriptObject(ScriptObject scriptObject, Object... objects) {
               Class<?> classValue = ScriptCompiler.resolveClassValue4(text2);
               Script.Var<?> varValue2 = null;
               if (classValue.equals(Integer.class)) {
                  varValue2 = new Script.Var<>(text4, Integer.class, flag ? null : (int)Long.parseLong(text5));
               }

               if (classValue.equals(Double.class)) {
                  varValue2 = new Script.Var<>(text4, Double.class, flag ? null : Double.parseDouble(text5));
               }

               if (classValue.equals(String.class)) {
                  varValue2 = new Script.Var<>(text4, String.class, flag ? null : text5);
               }

               if (varValue2 != null) {
                  if (flag2) {
                     varValue2.run();
                  }

                  Script.Var<?> varValue = ScriptMember.resolveVarValue(ScriptMember.this, text4);
                  if (varValue != null) {
                     if (!varValue.testClassValue(classValue)) {
                        throw new UnknownType();
                     }

                     varValue.set(varValue2.getValue());
                     return;
                  }

                  ScriptMember.this.varValueList.add(varValue2);
               }
            }
         });
      }
   }

   public static Object resolveObject3(ScriptMember<?> scriptMember, Object object) {
      String text = object.toString();
      Script.Var<?> varValue;
      if (object.getClass().equals(String.class) && text.startsWith("&") && (varValue = resolveVarValue(scriptMember, text.substring(1))) != null) {
         return varValue.getValue();
      } else {
         Script.Arg<?> arg;
         if (object.getClass().equals(String.class) && text.startsWith("&") && (arg = resolveArg(scriptMember, text.substring(1))) != null) {
            Object object2 = arg.getValue();
            Script.Var<?> varValue2;
            return object2.getClass().equals(String.class) && (varValue2 = resolveVarValue(scriptMember, object2.toString().substring(1))) != null
               ? varValue2.getValue()
               : arg.getValue();
         } else {
            return object;
         }
      }
   }

   public static Object[] values(ScriptMember<?> scriptMember, Object[] objects) {
      Object[] objects2 = new Object[objects.length];

      for (int i = 0; i < objects.length; i++) {
         objects2[i] = resolveObject3(scriptMember, objects[i]);
      }

      return objects2;
   }

   public static Script.Var<?> resolveVarValue(ScriptMember<?> scriptMember, String text) {
      for (Script.Var<?> varValue : scriptMember.varValueList) {
         if (varValue.getName().equals(text)) {
            return varValue;
         }
      }

      for (Script.Var<?> varValue2 : scriptMember.script.getVarValueList()) {
         if (varValue2.getName().equals(text)) {
            return varValue2;
         }
      }

      return null;
   }

   public void run() throws Exception {
      if (!this.flag2) {
         throw new ScriptManager.UnknownMethod();
      }
   }

   @Override
   public String toString() {
      return "Method{name='"
         + this.name
         + "', type="
         + this.valueClassValue
         + ", args="
         + ArrayUtils.toString(this.argArray)
         + ", eventhandler="
         + this.flag
         + "}";
   }

   @Nullable
   @Override
   public ScriptMember<?> resolveScriptMember(String text) {
      return this.script.resolveScriptMember(text);
   }

   public static List<ScriptMember<?>> resolveList(Object object, String text, Class<?> classValue) {
      List<ScriptMember<?>> list = new ArrayList<>();

      for (final Method method : classValue.getDeclaredMethods()) {
         Script.Arg<?>[] args = new Script.Arg[method.getParameters().length];

         for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            args[i] = new Script.Arg<>(parameter.getName(), parameter.getType());
         }

         String text2 = text + "." + method.getName();
         ScriptMember<?> scriptMember = new ScriptMember<>(null, text2, method.getReturnType() == Void.class ? null : method.getReturnType(), args);
         scriptMember.scriptHookList.add(new ScriptHook("invokedynamic&" + method.getName()) {
            @Override
            public void handleScriptObject(ScriptObject scriptObject, Object... objects) {
               try {
                  method.invoke(object, objects);
               } catch (Exception var4) {
               }
            }
         });
         list.add(scriptMember);
      }

      return list;
   }

   @Override
   public List<Script.Var<?>> getVarValueList() {
      return this.varValueList;
   }

   public List<Script.Invoker> getInvokerList() {
      return this.invokerList;
   }

   public void setObject(Object object) {
      this.object = object;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public Script.Arg<?>[] getArgArray() {
      return this.argArray;
   }

   @Override
   public List<ScriptTypeRef> getScriptTypeRefList() {
      return this.scriptTypeRefList;
   }

   public Script getScript() {
      return this.script;
   }

   public Object getObject() {
      return this.object;
   }

   @Override
   public List<ScriptHook> getScriptHookList() {
      return this.scriptHookList;
   }

   public Class<? super T> getValueClassValue2() {
      return this.valueClassValue;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public List<Script.Var<?>> getVarValueList2() {
      return this.varValueList;
   }

   @Override
   public boolean isCompleted() {
      return this.completed;
   }

   public String getName() {
      return this.name;
   }

   public boolean isFlag22() {
      return this.flag2;
   }

   public ScriptMember(Script script, String text, Class<? super T> classValue, Script.Arg<?>[] args) {
      this.scriptTypeRefList = new CopyOnWriteArrayList<>();
      this.varValueList = new CopyOnWriteArrayList<>();
      this.invokerList = new ArrayList<>();
      this.object = null;
      this.flag2 = false;
      this.flag = false;
      this.script = script;
      this.name = text;
      this.valueClassValue = classValue;
      this.argArray = args;
   }

   @Override
   public void setCompleted(boolean flag) {
      this.completed = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
