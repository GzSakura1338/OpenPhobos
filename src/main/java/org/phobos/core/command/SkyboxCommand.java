package org.phobos.core.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.JsonSupport;
import org.phobos.module.render.AmbienceModule;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.phobos.core.PhobosClient;
import org.phobos.core.render.SkyboxMath;

public class SkyboxCommand extends Command implements JsonSupport {
   public SkyboxCommand(PhobosClient phobosClient) {
      super(phobosClient, "skybox", "in", "out", "duration", "apply");
   }

   public void handleString(String[] texts, Path path, Path path2, Path path3, String[] texts2, long l, boolean flag) {
      try {
         JsonArray jsonArray = new JsonArray();
         ImageReader imageReader = ImageIO.getImageReadersByFormatName("gif").next();
         ImageInputStream imageInputStream = ImageIO.createImageInputStream(path2.toFile());
         imageReader.setInput(imageInputStream, false);
         int i3 = imageReader.getNumImages(true);
         MutableObject<BufferedImage> mutableObject = new MutableObject();
         this.phobosClient.getChatManager().handleText("Generating %s frames".formatted(i3));

         for (int i2 = 0; i2 < i3; i2++) {
            try {
               BufferedImage bufferedImage = imageReader.read(i2);
               IIOMetadata iIOMetadata = imageReader.getImageMetadata(i2);
               Node node3 = iIOMetadata.getAsTree("javax_imageio_gif_image_1.0");
               NodeList nodeList = node3.getChildNodes();

               for (int i = 0; i < nodeList.getLength(); i++) {
                  Node node2 = nodeList.item(i);
                  if (node2.getNodeName().equals("ImageDescriptor")) {
                     Map<String, Integer> map = new HashMap<>();

                     for (String text : texts) {
                        NamedNodeMap namedNodeMap = node2.getAttributes();
                        Node node = namedNodeMap.getNamedItem(text);
                        map.put(text, Integer.valueOf(node.getNodeValue()));
                     }

                     if (i2 == 0) {
                        mutableObject.setValue(new BufferedImage(map.get("imageWidth"), map.get("imageHeight"), 2));
                     }

                     ((BufferedImage)mutableObject.getValue())
                        .getGraphics()
                        .drawImage(bufferedImage, map.get("imageLeftPosition"), map.get("imageTopPosition"), null);
                  }
               }

               if (mutableObject.getValue() == null) {
                  this.phobosClient.getChatManager().handleText("Failed to allocate frame [%s]".formatted(texts2[i2]));
                  return;
               }

               String text2 = "%s_generated_%s.png".formatted(texts2[0], i2);
               long l2 = System.currentTimeMillis();
               this.phobosClient.getChatManager().handleText("Generating frame [%s]".formatted(i2));
               BufferedImage bufferedImage2 = SkyboxMath.resolveBufferedImage((BufferedImage)mutableObject.getValue());
               ImageIO.write(bufferedImage2, "PNG", path.resolve(text2).toFile());
               jsonArray.add(text2);
               this.phobosClient.getChatManager().handleText("Generated [%s] in %sms".formatted(i2, System.currentTimeMillis() - l2));
            } catch (Throwable throwable) {
               this.phobosClient.getChatManager().handleText2("Failed to make animated skybox");
               throwable.printStackTrace();
            }
         }

         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("duration", l);
         jsonObject.add("textures", jsonArray);
         String text3 = gson.toJson(jsonObject);
         Files.writeString(path3, text3);
         String text4 = "%s.json".formatted(texts2[1]);
         this.phobosClient.getChatManager().handleText("Successfully generated %s".formatted(text4));
         if (flag) {
            AmbienceModule.getAmbienceModule().getSkyName().setValue(text4);
            this.phobosClient.getChatManager().handleText("Applying %s".formatted(text4));
         }
      } catch (Throwable throwable2) {
         this.phobosClient.getChatManager().handleText2("Failed to make animated skybox");
         throwable2.printStackTrace();
      }
   }

   @Override
   public void handleString5(String[] texts) {
      String text = System.getProperty("user.home");
      Path path = Path.of(text, "earthhack", "textures");
      if (!path.toFile().exists()) {
         path.toFile().mkdirs();
      }

      long l;
      try {
         l = Long.parseLong(texts[2]);
      } catch (Throwable throwable) {
         this.phobosClient.getChatManager().handleText2("Invalid duration");
         return;
      }

      boolean flag;
      try {
         flag = Boolean.parseBoolean(texts[3]);
      } catch (Throwable throwable2) {
         this.phobosClient.getChatManager().handleText2("Invalid apply: true/false");
         return;
      }

      Path path2 = path.resolve("%s.gif".formatted(texts[0]));
      Path path3 = path.resolve("%s.json".formatted(texts[1]));
      if (!Files.exists(path2)) {
         this.phobosClient.getChatManager().handleText2("Input %s not found".formatted(texts[0] + ".png"));
      } else {
         String[] texts2 = new String[]{"imageLeftPosition", "imageTopPosition", "imageWidth", "imageHeight"};
         this.phobosClient.getChatManager().handleText("Generating frames, this may take a while...");
         CompletableFuture.runAsync(() -> this.handleString(texts2, path, path2, path3, texts, l, flag), this.phobosClient.getExecutorService());
      }
   }
}
