package org.phobos.core.world;

import lombok.SneakyThrows;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.JsonSupport;
import org.phobos.module.render.WaypointsModule.Waypoint;
import org.phobos.module.render.WaypointsModule.WaypointDimension;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;

public class WaypointManager extends Feature implements JsonSupport, Iterable<Waypoint> {
   public static final List<Waypoint> waypointList = new ArrayList<>();
   public static final Path path = FabricLoader.getInstance().getGameDir().resolve("phoboslite");

   public boolean testDouble(double d, double d2, double d3, String text, WaypointDimension waypointDimension) {
      if (waypointList.stream().anyMatch(waypoint -> waypoint.getName().equals(text))) {
         return false;
      }

      waypointList.add(new Waypoint(d, d2, d3, text, waypointDimension));
      return true;
   }

   public List<Waypoint> getList2() {
      return waypointList;
   }

   public Optional<Waypoint> resolveOptional2(String text) {
      return waypointList.stream().filter(waypoint -> waypoint.getName().equals(text)).findFirst();
   }

   @Nullable
   public Waypoint resolveWaypoint(String text) {
      for (Waypoint waypoint : waypointList) {
         if (waypoint.getName().startsWith(text)) {
            return waypoint;
         }
      }

      return null;
   }

   public boolean testText(String text) {
      MutableObject<Boolean> mutableObject = new MutableObject<>(false);
      waypointList.removeIf(waypoint -> {
         if (waypoint.getName().equals(text)) {
            mutableObject.setValue(true);
            return true;
         } else {
            return false;
         }
      });
      return mutableObject.getValue();
   }

   public static void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (String text : jsonObject.keySet()) {
         JsonElement jsonElement2 = jsonObject.get(text);
         if (jsonElement2 != null) {
            Waypoint waypoint = new Waypoint(text);

            try {
               waypoint.handleJsonElement(jsonElement2);
               waypointList.add(waypoint);
            } catch (Throwable var7) {
            }
         }
      }
   }

   @SneakyThrows public static void handlePhobosClient2(PhobosClient phobosClient) {
      Path var1 = path;
      Path var2 = var1.resolve("waypoints.json");
      File var3 = var2.toFile();
      boolean var4 = var3.exists();
      if (var4) {
         label63: {
            JsonElement var5;
            try {
               var5 = createJsonPrimitive();
            } catch (Throwable var17) {
               boolean var11 = true;
               if (var11) {
                  break label63;
               }

               throw var17;
            }

            Gson var6 = JsonSupport.gson;

            String var7;
            try {
               var7 = var6.toJson(var5);
            } catch (Throwable var16) {
               boolean var18 = true;
               if (var18) {
                  break label63;
               }

               throw var16;
            }

            OpenOption[] var8 = new OpenOption[0];

            try {
               Files.writeString(var2, var7, var8);
               return;
            } catch (Throwable var15) {
               boolean var19 = true;
               if (!var19) {
                  throw var15;
               }
            }
         }

         phobosClient.handleText5("WaypointManager", "Failed to load data.");
      } else {
         FileAttribute[] var9 = new FileAttribute[0];

         try {
            Files.createFile(var2, var9);
         } catch (Throwable var14) {
            boolean var13 = var14 instanceof IOException;
            if (!var13) {
               throw var14;
            }
         }
      }
   }

   public static JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Waypoint waypoint : waypointList) {
         try {
            JsonElement jsonElement = waypoint.createJsonPrimitive();
            if (jsonElement != null) {
               jsonObject.add(waypoint.getName(), jsonElement);
            }
         } catch (Throwable var4) {
         }
      }

      return jsonObject;
   }

   @SneakyThrows public static void handlePhobosClient(PhobosClient phobosClient) {
      Path var1 = path;
      Path var2 = var1.resolve("waypoints.json");
      File var3 = var2.toFile();
      boolean var4 = var3.exists();
      Throwable var13;
      if (var4) {
         Throwable var11;
         label80: {
            label68: {
               String var5;
               try {
                  var5 = Files.readString(var2);
               } catch (Throwable var18) {
                  var11 = var18;
                  boolean var12 = true;
                  if (!var12) {
                     break label80;
                  }
                  break label68;
               }

               JsonElement var6;
               try {
                  var6 = JsonParser.parseString(var5);
               } catch (Throwable var17) {
                  var11 = var17;
                  boolean var22 = true;
                  if (!var22) {
                     break label80;
                  }
                  break label68;
               }

               try {
                  handleJsonElement(var6);
                  return;
               } catch (Throwable var16) {
                  var11 = var16;
                  boolean var23 = true;
                  if (!var23) {
                     break label80;
                  }
               }
            }

            phobosClient.handleText5("WaypointsManager", "Failed to load data.");
            return;
         }

         var13 = var11;
      } else {
         Throwable var14;
         label69: {
            JsonElement var7;
            try {
               var7 = createJsonPrimitive();
            } catch (Throwable var21) {
               var14 = var21;
               boolean var15 = var21 instanceof IOException;
               if (var15) {
                  return;
               }
               break label69;
            }

            Gson var8 = JsonSupport.gson;

            String var9;
            try {
               var9 = var8.toJson(var7);
            } catch (Throwable var20) {
               var14 = var20;
               boolean var24 = var20 instanceof IOException;
               if (var24) {
                  return;
               }
               break label69;
            }

            OpenOption[] var10 = new OpenOption[0];

            try {
               Files.writeString(var2, var9, var10);
               return;
            } catch (Throwable var19) {
               var14 = var19;
               boolean var25 = var19 instanceof IOException;
               if (var25) {
                  return;
               }
            }
         }

         var13 = var14;
      }

      throw var13;
   }

   public boolean testDouble2(double d, double d2, double d3, String text, String text2) {
      return this.testDouble(d, d2, d3, text, WaypointDimension.fromString(text2));
   }

   @NotNull
   @Override
   public Iterator<Waypoint> iterator() {
      return waypointList.iterator();
   }
}
