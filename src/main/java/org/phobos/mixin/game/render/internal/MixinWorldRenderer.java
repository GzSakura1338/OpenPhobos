package org.phobos.mixin.game.render.internal;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Precipitation;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.phobos.api.WorldRendererCallback;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.render.Rect4f;
import org.phobos.core.render.RenderContext;
import org.phobos.core.entity.EntityFadeManager;
import org.phobos.core.render.shader.ShaderEffectManager;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.net.LagState;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.event.DrawBlockOutlineEvent;
import org.phobos.event.PreRenderEvent;
import org.phobos.event.RenderSkyEvent;
import org.phobos.event.SpawnEvent;
import org.phobos.module.render.AmbienceModule;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.BlockHighlightModule;
import org.phobos.module.render.FreecamModule;
import org.phobos.module.render.ShaderModule;
import org.phobos.module.misc.WeatherModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldRenderer.class, priority = Integer.MAX_VALUE)
public abstract class MixinWorldRenderer implements MinecraftHolder, EventSubscriber {
   @Shadow
   @Final
   private float[] NORMAL_LINE_DZ;
   @Shadow
   @Final
   private static Identifier SNOW;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Final
   private static Identifier RAIN;
   @Shadow
   @Final
   private float[] NORMAL_LINE_DX;
   @Shadow
   private int ticks;
   @Unique
   private static final Rect4f[] TEXTURE_FACES = new Rect4f[]{
      new Rect4f(0.0F, 0.0F, 0.33333334F, 0.5F),
      new Rect4f(0.33333334F, 0.5F, 0.6666667F, 1.0F),
      new Rect4f(0.6666667F, 0.0F, 1.0F, 0.5F),
      new Rect4f(0.33333334F, 0.0F, 0.6666667F, 0.5F),
      new Rect4f(0.6666667F, 0.5F, 1.0F, 1.0F),
      new Rect4f(0.0F, 0.5F, 0.33333334F, 1.0F)
   };
   @Shadow
   @Final
   private BufferBuilderStorage bufferBuilders;
   @Shadow
   @Final
   private EntityRenderDispatcher entityRenderDispatcher;
   @Shadow
   @Nullable
   private VertexBuffer lightSkyBuffer;

   @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal = 0))
   private void replaceShaderHook(PostEffectProcessor instance, float tickDelta) {
      ShaderModule module = ShaderModule.getShaderModule();
      if (module == null) {
         instance.render(tickDelta);
      } else {
         ShaderEffectManager.Shader shader = module.shaderSetting.getValue();
         if (!module.check7() || mc.world == null) {
            instance.render(tickDelta);
         } else if (!ClientHolder.getPhobosClient().getShaderEffectManager().check()) {
            ClientHolder.getPhobosClient()
               .getShaderEffectManager()
               .handleShader(shader, ClientHolder.getPhobosClient().getShaderEffectManager().resolvePostEffectShader(shader));
         }
      }
   }

   @Inject(method = "render", at = @At("HEAD"))
   private void preRenderHook(
      RenderTickCounter tickCounter,
      boolean renderBlockOutline,
      Camera camera,
      GameRenderer gameRenderer,
      LightmapTextureManager lightmapTextureManager,
      Matrix4f matrix4f,
      Matrix4f matrix4f2,
      CallbackInfo ci
   ) {
      this.invoke(new PreRenderEvent(camera));
   }

   @Redirect(method = "render", at = @At(value = "NEW", target = "()Lnet/minecraft/client/util/math/MatrixStack;"))
   private MatrixStack renderMatrixHook() {
      MatrixStack matrices = new MatrixStack();
      Vec3d pos = mc.gameRenderer.getCamera().getPos();
      double cameraX = pos.getX();
      double cameraY = pos.getY();
      double cameraZ = pos.getZ();
      EntityFadeManager.handleEntityRenderDispatcher2(
         this.entityRenderDispatcher, matrices, cameraX, cameraY, cameraZ, mc.getRenderTickCounter().getTickDelta(false)
      );
      return matrices;
   }

   @Redirect(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"
      )
   )
   private void renderHook(
      WorldRenderer instance,
      Entity entity,
      double cameraX,
      double cameraY,
      double cameraZ,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers
   ) {
      float jitter = AnimationsModule.computeFloat3(tickDelta);
      boolean rotations = AnimationsModule.check4();
      boolean position = AnimationsModule.check8();
      double x = MathHelper.lerp(position ? jitter : tickDelta, entity.lastRenderX, entity.getX());
      double y = MathHelper.lerp(position ? jitter : tickDelta, entity.lastRenderY, entity.getY());
      double z = MathHelper.lerp(position ? jitter : tickDelta, entity.lastRenderZ, entity.getZ());
      float yaw = MathHelper.lerp(rotations ? jitter : tickDelta, entity.prevYaw, entity.getYaw());
      if (entity instanceof ClientPlayerEntity && LagState.flag && LagState.movingFakePlayer != null) {
         double x1 = MathHelper.lerp(position ? jitter : tickDelta, LagState.movingFakePlayer.lastRenderX, LagState.movingFakePlayer.getX());
         double y1 = MathHelper.lerp(position ? jitter : tickDelta, LagState.movingFakePlayer.lastRenderY, LagState.movingFakePlayer.getY());
         double z1 = MathHelper.lerp(position ? jitter : tickDelta, LagState.movingFakePlayer.lastRenderZ, LagState.movingFakePlayer.getZ());
         this.entityRenderDispatcher
            .render(
               entity,
               x1 - cameraX,
               y1 - cameraY,
               z1 - cameraZ,
               yaw,
               tickDelta,
               matrices,
               vertexConsumers,
               this.entityRenderDispatcher.getLight(entity, tickDelta)
            );
      }

      if (entity instanceof FakePlayer fakePlayer && !fakePlayer.isFlag()) {
         tickDelta = 0.0F;
      }

      this.entityRenderDispatcher
         .render(
            entity, x - cameraX, y - cameraY, z - cameraZ, yaw, tickDelta, matrices, vertexConsumers, this.entityRenderDispatcher.getLight(entity, tickDelta)
         );
   }

   @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
   private void renderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
      ci.cancel();
      if (!WeatherModule.check2()) {
         assert this.client != null && this.client.world != null;
         float f = this.client.world.getRainGradient(tickDelta);
         if (!(f <= 0.0F)) {
            manager.enable();
            World world = this.client.world;
            int i = MathHelper.floor(cameraX);
            int j = MathHelper.floor(cameraY);
            int k = MathHelper.floor(cameraZ);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = null;
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int l = 5;
            if (MinecraftClient.isFancyGraphicsOrBetter()) {
               l = 10;
            }

            RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
            int m = -1;
            float g = this.ticks + tickDelta;
            RenderSystem.setShader(GameRenderer::getParticleProgram);
            Mutable mutable = new Mutable();

            for (int n = k - l; n <= k + l; n++) {
               for (int o = i - l; o <= i + l; o++) {
                  int p = (n - k + 16) * 32 + o - i + 16;
                  double d = this.NORMAL_LINE_DX[p] * 0.5;
                  double e = this.NORMAL_LINE_DZ[p] * 0.5;
                  mutable.set(o, cameraY, n);
                  Biome biome = (Biome)world.getBiome(mutable).value();
                  if (WeatherModule.check3() || biome.hasPrecipitation()) {
                     int q = WeatherModule.check() ? -64 : world.getTopY(Type.MOTION_BLOCKING, o, n);
                     int r = j - l;
                     int s = j + l;
                     if (r < q) {
                        r = q;
                     }

                     if (s < q) {
                        s = q;
                     }

                     int t = q;
                     if (t < j) {
                        t = j;
                     }

                     if (r != s) {
                        Random random = Random.create((long)o * o * 3121L + o * 45238971L ^ (long)n * n * 418711L + n * 13761L);
                        mutable.set(o, r, n);
                        Precipitation precipitation = WeatherModule.resolvePrecipitation(biome.getPrecipitation(mutable));
                        if (precipitation == Precipitation.RAIN) {
                           if (m != 0) {
                              if (m >= 0) {
                                 BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                              }

                              m = 0;
                              RenderSystem.setShaderTexture(0, RAIN);
                              bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                           }

                           int u = this.ticks & 131071;
                           int v = o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 0xFF;
                           float h = 3.0F + random.nextFloat();
                           float w = -(u + v + tickDelta) / 32.0F * WeatherModule.computeFloat(h);
                           float x = w % 32.0F;
                           double y = o + 0.5 - cameraX;
                           double z = n + 0.5 - cameraZ;
                           float aa = (float)Math.sqrt(y * y + z * z) / l;
                           float ab = ((1.0F - aa * aa) * 0.5F + 0.5F) * f;
                           mutable.set(o, t, n);
                           int ac = getLightmapCoordinates(world, mutable);
                           bufferBuilder.vertex((float)(o - cameraX - d + 0.5), (float)(s - cameraY), (float)(n - cameraZ - e + 0.5))
                              .texture(0.0F, r * 0.25F + x)
                              .color(1.0F, 1.0F, 1.0F, ab)
                              .light(ac);
                           bufferBuilder.vertex((float)(o - cameraX + d + 0.5), (float)(s - cameraY), (float)(n - cameraZ + e + 0.5))
                              .texture(1.0F, r * 0.25F + x)
                              .color(1.0F, 1.0F, 1.0F, ab)
                              .light(ac);
                           bufferBuilder.vertex((float)(o - cameraX + d + 0.5), (float)(r - cameraY), (float)(n - cameraZ + e + 0.5))
                              .texture(1.0F, s * 0.25F + x)
                              .color(1.0F, 1.0F, 1.0F, ab)
                              .light(ac);
                           bufferBuilder.vertex((float)(o - cameraX - d + 0.5), (float)(r - cameraY), (float)(n - cameraZ - e + 0.5))
                              .texture(0.0F, s * 0.25F + x)
                              .color(1.0F, 1.0F, 1.0F, ab)
                              .light(ac);
                        } else if (precipitation == Precipitation.SNOW) {
                           if (m != 1) {
                              if (m == 0) {
                                 BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                              }

                              m = 1;
                              RenderSystem.setShaderTexture(0, SNOW);
                              bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                           }

                           float ad = -((this.ticks & 511) + tickDelta) / (512.0F / WeatherModule.getFloatValue22());
                           float ae = (float)(random.nextDouble() + g * 0.01 * (float)random.nextGaussian());
                           float h = (float)(random.nextDouble() + g * (float)random.nextGaussian() * 0.001);
                           double af = o + 0.5 - cameraX;
                           double y = n + 0.5 - cameraZ;
                           float ag = (float)Math.sqrt(af * af + y * y) / l;
                           float ah = ((1.0F - ag * ag) * 0.3F + 0.5F) * f;
                           mutable.set(o, t, n);
                           int ai = getLightmapCoordinates(world, mutable);
                           int aj = ai >> 16 & 65535;
                           int ac = ai & 65535;
                           int ak = (aj * 3 + 240) / 4;
                           int al = (ac * 3 + 240) / 4;
                           bufferBuilder.vertex((float)(o - cameraX - d + 0.5), (float)(s - cameraY), (float)(n - cameraZ - e + 0.5))
                              .texture(0.0F + ae, r * 0.25F + ad + h)
                              .color(1.0F, 1.0F, 1.0F, ah)
                              .light(al, ak);
                           bufferBuilder.vertex((float)(o - cameraX + d + 0.5), (float)(s - cameraY), (float)(n - cameraZ + e + 0.5))
                              .texture(1.0F + ae, r * 0.25F + ad + h)
                              .color(1.0F, 1.0F, 1.0F, ah)
                              .light(al, ak);
                           bufferBuilder.vertex((float)(o - cameraX + d + 0.5), (float)(r - cameraY), (float)(n - cameraZ + e + 0.5))
                              .texture(1.0F + ae, s * 0.25F + ad + h)
                              .color(1.0F, 1.0F, 1.0F, ah)
                              .light(al, ak);
                           bufferBuilder.vertex((float)(o - cameraX - d + 0.5), (float)(r - cameraY), (float)(n - cameraZ - e + 0.5))
                              .texture(0.0F + ae, s * 0.25F + ad + h)
                              .color(1.0F, 1.0F, 1.0F, ah)
                              .light(al, ak);
                        }
                     }
                  }
               }
            }

            if (m >= 0) {
               BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            manager.disable();
         }
      }
   }

   @Shadow
   public abstract void reload();

   @ModifyArg(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"
      ),
      index = 3
   )
   private boolean renderHook(boolean hasForcedFrustum) {
      return hasForcedFrustum || FreecamModule.getFreecamModule().check7();
   }

   @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
   private void drawBlockOutlineHook(CallbackInfo ci) {
      this.invoke(new DrawBlockOutlineEvent());
      this.invoke(new BlockHighlightModule.Custom(), ci);
   }

   @Redirect(
      method = "tickRainSplashing",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"
      )
   )
   private Precipitation getPrecipitationHook2(Biome biome, BlockPos blockPos) {
      return WeatherModule.resolvePrecipitation(biome.getPrecipitation(blockPos));
   }

   @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
   private void renderSkyHook(
      Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci
   ) {
      RenderSkyEvent event = this.invoke(new RenderSkyEvent());
      if (event.isCancelled() && this.lightSkyBuffer != null) {
         ci.cancel();
         MatrixStack matrixStack = new MatrixStack();
         matrixStack.multiplyPositionMatrix(matrix4f);
         float r = event.getVector3f().x;
         float g = event.getVector3f().y;
         float b = event.getVector3f().z;
         switch (event.getSkyMode()) {
            case Texture: {
               if (event.isFlag() && !SkyboxTextureUtils.check() && !AmbienceModule.check2()) {
                  return;
               }

               RenderSystem.depthMask(false);
               RenderSystem.enableBlend();
               RenderSystem.setShader(GameRenderer::getPositionTexProgram);
               matrixStack.push();
               Tessellator tessellator = Tessellator.getInstance();
               BufferBuilder bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
               if (event.isFlag()) {
                  if (AmbienceModule.check2()) {
                     AmbienceModule.run();
                  } else {
                     SkyboxTextureUtils.run();
                  }
               } else {
                  RenderSystem.setShaderTexture(0, SkyboxTextureUtils.identifier);
               }

               RenderSystem.setShaderColor(r, g, b, 1.0F);

               for (int face = 0; face < 6; face++) {
                  Rect4f tex = TEXTURE_FACES[face];
                  matrixStack.push();
                  switch (face) {
                     case 1:
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                        break;
                     case 2:
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                        break;
                     case 3:
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
                        break;
                     case 4:
                        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
                        break;
                     case 5:
                        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                  }

                  Matrix4f matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, -100.0F, -100.0F, -100.0F).texture(tex.getFloatValue3(), tex.getFloatValue());
                  bufferBuilder.vertex(matrix4f2, -100.0F, -100.0F, 100.0F).texture(tex.getFloatValue3(), tex.getFloatValue2());
                  bufferBuilder.vertex(matrix4f2, 100.0F, -100.0F, 100.0F).texture(tex.getFloatValue4(), tex.getFloatValue2());
                  bufferBuilder.vertex(matrix4f2, 100.0F, -100.0F, -100.0F).texture(tex.getFloatValue4(), tex.getFloatValue());
                  matrixStack.pop();
               }

               BufferUtils.handleBufferBuilder(bufferBuilder);
               matrixStack.pop();
               RenderSystem.depthMask(true);
               RenderSystem.disableBlend();
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               break;
            }
            case Shader:
               Render3d.skyboxShader.run2();
               Render3d.skyboxShader.run3();
               matrixStack.push();
               Tessellator tessellator = Tessellator.getInstance();
               BufferBuilder bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);

               for (int face = 0; face < 6; face++) {
                  matrixStack.push();
                  switch (face) {
                     case 1:
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                        break;
                     case 2:
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                        break;
                     case 3:
                        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
                        break;
                     case 4:
                        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
                        break;
                     case 5:
                        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
                        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                  }

                  Matrix4f matrix4f2 = matrixStack.peek().getPositionMatrix();
                  bufferBuilder.vertex(matrix4f2, -100.0F, -100.0F, -100.0F).color(1.0F, 1.0F, 1.0F, 1.0F).texture(0.0F, 0.0F).normal(0.0F, -1.0F, 0.0F);
                  bufferBuilder.vertex(matrix4f2, -100.0F, -100.0F, 100.0F).color(1.0F, 1.0F, 1.0F, 1.0F).texture(0.0F, 0.0F).normal(0.0F, -1.0F, 0.0F);
                  bufferBuilder.vertex(matrix4f2, 100.0F, -100.0F, 100.0F).color(1.0F, 1.0F, 1.0F, 1.0F).texture(0.0F, 0.0F).normal(0.0F, -1.0F, 0.0F);
                  bufferBuilder.vertex(matrix4f2, 100.0F, -100.0F, -100.0F).color(1.0F, 1.0F, 1.0F, 1.0F).texture(0.0F, 0.0F).normal(0.0F, -1.0F, 0.0F);
                  matrixStack.pop();
               }

               BufferUtils.handleBufferBuilder(bufferBuilder);
               matrixStack.pop();
         }
      }
   }

   @Shadow
   public static int getLightmapCoordinates(BlockRenderView world, BlockPos pos) {
      return 0;
   }

   @Inject(method = "reload()V", at = @At("RETURN"))
   private void reloadHook(CallbackInfo ci) {
      ((WorldRendererCallback)WorldRendererCallback.worldRendererCallbackEvent.invoker()).handleWorldRenderer((WorldRenderer)(Object)this);
   }

   @Inject(
      method = "spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void spawnHook(
      ParticleEffect parameters,
      boolean alwaysSpawn,
      boolean canSpawnOnMinimal,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ,
      CallbackInfoReturnable<Particle> cir
   ) {
      this.invoke(new SpawnEvent(parameters.getType()), cir);
   }

   @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"))
   private void renderHook(OutlineVertexConsumerProvider instance, int red, int green, int blue, int alpha, @Local Entity entity) {
      ShaderModule module = ShaderModule.getShaderModule();
      if (module == null) {
         instance.setColor(red, green, blue, alpha);
      } else if (module.check7() && mc.world != null) {
         Color abstractColor = module.resolveColor(entity);
         instance.setColor(abstractColor.getRed(), abstractColor.getGreen(), abstractColor.getBlue(), abstractColor.getAlpha());
      } else {
         instance.setColor(red, green, blue, alpha);
      }
   }

   @Inject(method = "render", at = @At("RETURN"))
   private void renderHook1(
      RenderTickCounter tickCounter,
      boolean renderBlockOutline,
      Camera camera,
      GameRenderer gameRenderer,
      LightmapTextureManager lightmapTextureManager,
      Matrix4f matrix4f,
      Matrix4f matrix4f2,
      CallbackInfo ci
   ) {
      mc.getProfiler().push("PhobosLite-World");
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.multiplyPositionMatrix(matrix4f);
      RenderContext event = RenderContext.getINSTANCE();
      event.setFloatValue(tickCounter.getTickDelta(false));
      event.setMatrixStack(matrixStack);
      event.setCamera(camera);
      event.setMatrix4f(matrix4f);
      event.setMatrix4f2(matrix4f2);
      this.invoke(event);
      mc.getProfiler().pop();
   }
}
