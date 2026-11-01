package org.phobos.core.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.phobos.api.JsonSupport;
import org.phobos.core.PhobosClient;
import org.phobos.core.crypto.StringCipher;
import org.phobos.core.user.UserSession;

public class MakeModuleCommand extends SyntaxCommand implements JsonSupport {
   public MakeModuleCommand(PhobosClient phobosClient) {
      super(phobosClient, "makemodule", "filename", "name", "dependencies");
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (UserSession.flag2) {
         List<String> list = new ArrayList<>(Arrays.asList(texts).subList(2, texts.length));
         this.phobosClient.getModuleManager().resolveOptional3(texts[1]).ifPresent(module -> {
            Class<?> classValue = module.getClass();

            try {
               ClassReader classReader = new ClassReader(classValue.getName());
               ClassNode classNode = new ClassNode();
               classReader.accept(classNode, 0);
               String text2 = texts[2];
               classNode.name = "org/earthhack/runtime/%s".formatted(text2);
               ClassWriter classWriter = new ClassWriter(0);
               classNode.accept(classWriter);
               byte[] bytes = classWriter.toByteArray();
               JsonObject jsonObject = new JsonObject();
               jsonObject.addProperty("Name", classNode.name);
               JsonArray jsonArray = new JsonArray();

               for (String textx : list) {
                  jsonArray.add(textx);
               }

               jsonObject.add("Dependencies", jsonArray);
               jsonObject.addProperty("Data", new String(Base64.getEncoder().encode(bytes)));
               String text3 = StringCipher.describeText(gson.toJson(jsonObject));
               Files.writeString(FabricLoader.getInstance().getGameDir().resolve("phoboslite").resolve("modules").resolve(texts[0] + ".spom"), text3);
            } catch (Throwable throwable) {
               throwable.printStackTrace();
            }
         });
      }
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length > 1;
   }
}
