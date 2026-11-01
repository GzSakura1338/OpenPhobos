package org.phobos.core.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.ValueSource;
import org.phobos.api.TypeBound;
import org.phobos.api.ScriptScope;
import org.phobos.api.ScriptObject;
import org.phobos.event.Listener;
import org.phobos.type.API10API11Enum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.phobos.type.UnknownTypeUnknownMethodEnum;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;

public class Script extends Feature implements ScriptObject, ScriptScope {
   public final PhobosClient phobosClient;
   public final List<Script.Var<?>> varValueList;
   public final Map<Integer, ScriptMember<?>> integerMap = new ConcurrentHashMap<>();
   public final Map<String, Integer> textMap;
   public int intValue;
   public final List<ScriptHook> scriptHookList;
   public final Map<Integer, Script.Var<?>> integerMap2;
   public final boolean flag;
   public String text;
   public final String text2;
   public int intValue2;
   public API10API11Enum aPI10API11Enum;
   public final List<ScriptMember<?>> scriptMemberList;
   public final ScriptApi scriptApi;
   public boolean flag2;
   public final MutableObject<ScriptMember<?>> scriptMemberMutableObject;
   @Nullable
   public String text3;
   public final Map<ScriptMember<?>, Integer> scriptMemberMap = new ConcurrentHashMap<>();
   public final List<ScriptTypeRef> scriptTypeRefList;
   public final Map<Script.Var<?>, Integer> varValueMap;
   public final File file;
   public final String name;
   public final PUBLICPRIVATEEnum pUBLICPRIVATEEnum;
   public boolean completed;

   @Override
   public List<ScriptHook> getScriptHookList() {
      return this.scriptHookList;
   }

   @Override
   public List<Script.Var<?>> getVarValueList() {
      return this.varValueList;
   }

   public MutableObject<ScriptMember<?>> getScriptMemberMutableObject() {
      return this.scriptMemberMutableObject;
   }

   public void run() {
      if (this.aPI10API11Enum == null) {
         throw new IllegalStateException("Unable to run script without API version");
      }

      this.flag2 = true;

      for (ScriptHook scriptHook : this.scriptHookList) {
         this.handleScriptHook2(scriptHook);
      }
   }

   public void handleString(String[] texts) {
      ScriptCompiler.S.handleScript4(this, texts);
   }

   public void handleUnknownTypeUnknownMethodEnum(UnknownTypeUnknownMethodEnum unknownTypeUnknownMethodEnum, int i) {
      this.scriptTypeRefList.add(new ScriptTypeRef(unknownTypeUnknownMethodEnum, i));
   }

   @Nullable
   @Override
   public ScriptMember<?> resolveScriptMember(String text) {
      return this.resolveScriptMember2(text);
   }

   @Override
   public Map<ScriptMember<?>, Integer> getScriptMemberMap() {
      return this.scriptMemberMap;
   }

   public void handleText(String text) {
      this.phobosClient.handleText5("Scripts", "[" + this.name + "] " + text);
   }

   @Override
   public Map<Integer, Script.Var<?>> getIntegerMap2() {
      return this.integerMap2;
   }

   public <E> void handleClassValue(Class<E> classValue, ScriptMember<?> scriptMember) {
      this.handleListener(new Listener<E>(classValue) {
         @Override
         public void handleObject2(E object) {
            scriptMember.handleObject(object);
         }
      });
   }

   public PUBLICPRIVATEEnum getPUBLICPRIVATEEnum() {
      return this.pUBLICPRIVATEEnum;
   }

   @Override
   public List<ScriptMember<?>> getScriptMemberList() {
      return new ArrayList<>(this.getIntegerMap().values());
   }

   @Override
   public Map<String, Integer> getTextMap() {
      return this.textMap;
   }

   public void run8() {
      this.flag2 = false;
      if (this.scriptMemberMutableObject.getValue() != null) {
         ((ScriptMember)this.scriptMemberMutableObject.getValue()).handleObject();
      }
   }

   @Override
   public List<ScriptTypeRef> getScriptTypeRefList() {
      return this.scriptTypeRefList;
   }

   public Script(PhobosClient phobosClient, String text, String text2, File file, PUBLICPRIVATEEnum pUBLICPRIVATEEnum) {
      this.scriptMemberMutableObject = new MutableObject();
      this.integerMap2 = new ConcurrentHashMap<>();
      this.varValueMap = new ConcurrentHashMap<>();
      this.scriptHookList = new CopyOnWriteArrayList<>();
      this.textMap = new ConcurrentHashMap<>();
      this.scriptMemberList = new CopyOnWriteArrayList<>();
      this.varValueList = new CopyOnWriteArrayList<>();
      this.scriptTypeRefList = new ArrayList<>();
      this.intValue = 0;
      this.phobosClient = phobosClient;
      this.name = text;
      this.text2 = text2;
      this.file = file;
      this.pUBLICPRIVATEEnum = pUBLICPRIVATEEnum;
      this.scriptApi = new ScriptApi(phobosClient, this);
      this.flag = file == null;
      this.scriptApi
         .getList()
         .forEach(
            scriptMember -> {
               if (scriptMember.getValueClassValue2() == null
                  && scriptMember.getName().equals("unload")
                  && scriptMember.isFlag()
                  && this.scriptMemberMutableObject.getValue() == null) {
                  this.scriptMemberMutableObject.setValue(scriptMember);
               } else if (scriptMember.getValueClassValue2() == null && scriptMember.isFlag() && scriptMember.getArgArray().length == 1) {
                  Class<?> classValue = scriptMember.getArgArray()[0].getValueClassValue();
                  this.handleClassValue(classValue, (ScriptMember<?>)scriptMember);
               } else {
                  this.getIntegerMap().put(this.getIntValue2(), (ScriptMember<?>)scriptMember);
                  this.getScriptMemberMap().put((ScriptMember<?>)scriptMember, this.getIntValue2());
                  this.getTextMap().put(scriptMember.getName(), this.getIntValue2());
                  this.getScriptMemberList().add((ScriptMember<?>)scriptMember);
                  this.setIntValue2(this.getIntValue2() + 1);
               }
            }
         );
   }

   @Override
   public Map<Integer, ScriptMember<?>> getIntegerMap() {
      return this.integerMap;
   }

   @Override
   public String toString() {
      return this.text2;
   }

   @Override
   public Map<Script.Var<?>, Integer> getVarValueMap() {
      return this.varValueMap;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   @Nullable
   public String getText2() {
      return this.text3;
   }

   public ScriptApi getScriptApi() {
      return this.scriptApi;
   }

   @Override
   public int getIntValue2() {
      return this.intValue2;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public API10API11Enum getAPI10API11Enum() {
      return this.aPI10API11Enum;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setText(@Nullable String text) {
      this.text3 = text;
   }

   public String getText() {
      return this.text;
   }

   public String getText22() {
      return this.text2;
   }

   public void setText2(String text) {
      this.text = text;
   }

   @Override
   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public void setAPI10API11Enum(API10API11Enum aPI10API11Enum) {
      this.aPI10API11Enum = aPI10API11Enum;
   }

   @Override
   public void setCompleted(boolean flag) {
      this.completed = flag;
   }

   public boolean isFlag2() {
      return this.flag;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public boolean isCompleted() {
      return this.completed;
   }

   public boolean isFlag22() {
      return this.flag2;
   }

   public File getFile() {
      return this.file;
   }

   public static class Arg<T> implements ValueSource<Object>, TypeBound<T> {
      public final String name;
      public final Class<? super T> valueClassValue;
      public Object value;

      @Override
      public boolean testClassValue(Class<?> classValue) {
         return classValue.equals(this.valueClassValue);
      }

      @Override
      public Object getValue() {
         return this.value;
      }

      @Override
      public Class<? super T> getValueClassValue() {
         return this.valueClassValue;
      }

      public void setValue(Object object) {
         this.value = object;
      }

      public String getName() {
         return this.name;
      }

      public Arg(String text, Class<? super T> classValue) {
         this.name = text;
         this.valueClassValue = classValue;
      }

      @Override
      public String toString() {
         return "Arg{type=" + this.valueClassValue + ", value='" + this.value + "'}";
      }
   }

   public abstract static class Invoker {
      public final String name;
      public final Object[] objectArray;

      public Invoker(String text, Object[] objects) {
         this.name = text;
         this.objectArray = objects;
      }

      public String getName() {
         return this.name;
      }

      public Object[] getObjectArray() {
         return this.objectArray;
      }

      public abstract void run();
   }

   public static class Var<T> implements ValueSource<T>, TypeBound<T> {
      public T value;
      public boolean flag = false;
      public final String name;
      public final Class<? super T> valueClassValue;

      public void set(Object object) {
         this.setValue((T)object);
      }

      @Override
      public Class<? super T> getValueClassValue() {
         return this.valueClassValue;
      }

      public void setValue(T object) {
         this.value = object;
      }

      @Override
      public boolean testClassValue(Class<?> classValue) {
         return classValue.equals(this.valueClassValue);
      }

      public boolean isFlag() {
         return this.flag;
      }

      public void run() {
         this.flag = true;
      }

      public String getName() {
         return this.name;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      @Override
      public String toString() {
         return "Variable{name='" + this.name + "', type=" + this.valueClassValue + ", value='" + this.value + "'}";
      }

      public Var(String text, Class<? super T> classValue, T object) {
         this.name = text;
         this.valueClassValue = classValue;
         this.value = object;
      }

      @Override
      public T getValue() {
         return this.value;
      }
   }
}
