package org.phobos.core.loader;

import lombok.SneakyThrows;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.phobos.core.PhobosClient;

public class ClassLoaderUtils {
   private static Object[] phobos$g1806c8858;
   private static Object[] phobos$g1806c8908;

   @SneakyThrows public static String describeText(String text, String text2, String text3) {
      long var49 = 0L;
      Object var15 = null;
      Thread var3 = Thread.currentThread();

      Throwable var6;
      label149: {
         ClassLoader var4;
         try {
            var4 = var3.getContextClassLoader();
         } catch (Throwable var128) {
            var6 = var128;
            break label149;
         }

         ClassLoader var5;
         if (var4 == null) {
            ClassLoader var9;
            try {
               var9 = PhobosClient.class.getClassLoader();
            } catch (Throwable var127) {
               var6 = var127;
               break label149;
            }

            var5 = var9;
         } else {
            var5 = var4;
         }

         ClassLoader var12;
         if (var5 == null) {
            ClassLoader var13;
            try {
               var13 = ClassLoaderUtils.class.getClassLoader();
            } catch (Throwable var126) {
               var6 = var126;
               break label149;
            }

            var12 = var13;
         } else {
            var12 = var5;
         }

         String var14;
         try {
            var14 = text.replace((char)('.' & '\uffff'), (char)('/' & '\uffff'));
         } catch (Throwable var125) {
            var6 = var125;
            break label149;
         }

         String var21 = var14 + ".class";

         InputStream var22;
         try {
            var22 = var12.getResourceAsStream(var21);
         } catch (Throwable var124) {
            var6 = var124;
            break label149;
         }

         label123:
         if (var22 == null) {
            String var31 = "Class not found: " + text;

            IOException var24;
            try {
               var24 = new IOException(var31);
            } catch (Throwable var114) {
               var6 = var114;
               break label123;
            }

            try {
               throw var24;
            } catch (Throwable var113) {
               var6 = var113;
            }
         } else {
            label151: {
               ByteArrayOutputStream var32;
               try {
                  var32 = new ByteArrayOutputStream();
               } catch (Throwable var123) {
                  var6 = var123;
                  break label151;
               }

               byte[] var46 = new byte[1024];

               while (true) {
                  long var51 = var49;
                  long var53 = var51;
                  long var55 = var53;
                  long var57 = var55;
                  long var59 = var57;
                  long var61 = var59;
                  long var63 = var61;
                  long var65 = var63;

                  int var67;
                  try {
                     var67 = var22.read(var46);
                  } catch (Throwable var122) {
                     var6 = var122;
                     break;
                  }

                  long var68 = var65;
                  long var70 = var68;
                  long var72 = var70;
                  long var74 = var72;
                  long var76 = var74;
                  long var78 = var76;
                  long var80 = var78;
                  if (var67 == -1) {
                     byte[] var108;
                     try {
                        var108 = var32.toByteArray();
                     } catch (Throwable var121) {
                        var6 = var121;
                        break;
                     }

                     try {
                        var22.close();
                     } catch (Throwable var120) {
                        var6 = var120;
                        break;
                     }

                     ClassReader var109;
                     try {
                        var109 = new ClassReader(var108);
                     } catch (Throwable var119) {
                        var6 = var119;
                        break;
                     }

                     final StringBuilder var110;
                     try {
                        var110 = new StringBuilder();
                     } catch (Throwable var118) {
                        var6 = var118;
                        break;
                     }

                     ClassVisitor var111;
                     try {
                        var111 = new ClassVisitor(589824) {
                           @Override
                           public MethodVisitor visitMethod(int i, String textx, String text2, String text3, String[] texts) {
                              return textx.equals(text2) && text2.equals(text3) ? new MethodVisitor(589824) {
                                 @Override
                                 public void visitCode() {
                                    super.visitCode();
                                 }

                                 @Override
                                 public void visitMethodInsn(int i, String textx, String text2xx, String text3xx, boolean flag) {
                                    var110.append(text2xx).append(" -> ").append(text3xx).append(";");
                                    super.visitMethodInsn(i, textx, text2xx, text3xx, flag);
                                 }

                                 @Override
                                 public void visitInsn(int i) {
                                    var110.append(i).append(";");
                                    super.visitInsn(i);
                                 }
                              } : null;
                           }
                        };
                     } catch (Throwable var117) {
                        var6 = var117;
                        break;
                     }

                     try {
                        var109.accept(var111, 0);
                     } catch (Throwable var116) {
                        var6 = var116;
                        break;
                     }

                     String var112;
                     try {
                        var112 = var110.toString();
                     } catch (Throwable var115) {
                        var6 = var115;
                        break;
                     }

                     return var112;
                  }

                  long var82 = var80;
                  long var84 = var82;
                  long var86 = var84;
                  long var88 = var86;
                  long var90 = var88;
                  long var92 = var90;
                  long var94 = var92;
                  long var96 = var94;
                  long var98 = var96;
                  long var100 = 0L | var98 & -4294967296L;
                  long var102 = var100;
                  long var104 = var102;
                  long var106 = var104;
                  var32.write(var46, (int)var106, var67);
                  var49 = var106;
               }
            }
         }
      }

      boolean var7 = var6 instanceof Exception;
      if (!var7) {
         throw var6;
      }

      ((Exception)var6).printStackTrace();
      return null;
   }
}
