package org.phobos.core.render;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.earthhack.ducks.IEntityRenderDispatcher;
import org.earthhack.ducks.ILivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.phobos.api.CuboidProvider;
import org.phobos.api.NamedResolver;
import org.phobos.api.UniformSetup;
import org.phobos.api.RenderBatch;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.RenderArmEvent;
import org.phobos.mixin.game.render.interfaces.INativeImage;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.ChamsModule;
import org.phobos.module.render.FreecamModule;
import org.phobos.module.render.NoBobModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.client.RenderModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.util.ErrorRegistry;
import org.phobos.core.render.shader.LinesCircleShader;
import org.phobos.core.render.shader.LinesDissolveShader;
import org.phobos.core.render.shader.LinesFluidShader;
import org.phobos.core.render.shader.LinesFluxShader;
import org.phobos.core.render.shader.LinesNoiseShader;
import org.phobos.core.render.shader.LinesPlasmaShader;
import org.phobos.core.render.shader.LinesSilkShader;
import org.phobos.core.render.shader.MulticolorLinesShader;
import org.phobos.core.render.shader.MulticolorShader;
import org.phobos.core.math.MutableBox;
import org.phobos.core.render.shader.PositionColorCircleShader;
import org.phobos.core.render.shader.PositionColorPlasmaShader;
import org.phobos.core.render.shader.PositionDissolveShader;
import org.phobos.core.render.shader.PositionFluidShader;
import org.phobos.core.render.shader.PositionFluxShader;
import org.phobos.core.render.shader.PositionNoiseShader;
import org.phobos.core.render.shader.PositionSilkShader;
import org.phobos.core.setting.Setting;
import org.phobos.core.render.shader.SkyboxShader;
import org.phobos.core.tick.TimerManager;

public final class Render3d implements MinecraftHolder {
   public static LinesPlasmaShader linesPlasmaShader;
   public static PositionNoiseShader positionNoiseShader;
   public static PositionFluidShader positionFluidShader;
   public static LinesSilkShader linesSilkShader;
   public static PositionSilkShader positionSilkShader;
   public static PositionFluxShader positionFluxShader;
   public static LinesCircleShader linesCircleShader;
   public static MulticolorLinesShader multicolorLinesShader;
   public static MulticolorShader multicolorShader;
   public static final Logger LOGGER = LoggerFactory.getLogger(Render3d.class);
   public static PositionDissolveShader positionDissolveShader;
   public static SkyboxShader skyboxShader;
   public static final IEntityRenderDispatcher iEntityRenderDispatcher = Objects.requireNonNull((IEntityRenderDispatcher)(Object)mc.getEntityRenderDispatcher());
   public static final MutableBox mutableBox = new MutableBox();
   public static LinesFluidShader linesFluidShader;
   public static PositionColorCircleShader positionColorCircleShader;
   public static LinesDissolveShader linesDissolveShader;
   public static PositionColorPlasmaShader positionColorPlasmaShader;
   public static final Matrix4f matrix4f = new Matrix4f();
   public static LinesFluxShader linesFluxShader;
   public static LinesNoiseShader linesNoiseShader;
   public static final Matrix4f matrix4f3 = new Matrix4f();
   public static FogShape fogShape;
   public static final Matrix4f matrix4f2 = new Matrix4f();
   public static final char charValue = 'z';
   public static float floatValue;
   public static final char charValue2 = 'a';
   public static long longValue = 0L;
   public static float floatValue2;
   public static boolean flag = false;
   public static final Queue<Render3d.Cuboid> cuboidQueue = new ConcurrentLinkedQueue<>();
   public static final Queue<Render3d.Fade> fadeQueue = new ConcurrentLinkedQueue<>();
   public static final Queue<Render3d.Lines> linesQueue = new ConcurrentLinkedQueue<>();
   public static final Queue<Render3d.Model> modelQueue = new ConcurrentLinkedQueue<>();
   public static FogShape fogShape2;
   public static Vec3d vec3d;
   public static float floatValue3;
   public static final Queue<Render3d.TransformedLine> transformedLineQueue = new ConcurrentLinkedQueue<>();
   public static final Queue<Consumer<Matrix4f>> consumerQueue = new ConcurrentLinkedQueue<>();
   public static final Queue<Render3d.CameraLine> cameraLineQueue = new ConcurrentLinkedQueue<>();
   public static final Random random = new Random();
   public static float floatValue4;

   public static void run2() {
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   public static void handlePlayerEntityModel(PlayerEntityModel<?> playerEntityModel, Runnable runnable) {
      boolean flag = playerEntityModel.leftPants.visible;
      boolean flag2 = playerEntityModel.rightPants.visible;
      boolean flag3 = playerEntityModel.leftSleeve.visible;
      boolean flag4 = playerEntityModel.rightSleeve.visible;
      boolean flag5 = playerEntityModel.jacket.visible;
      boolean flag6 = playerEntityModel.hat.visible;
      handlePlayerEntityModel3(playerEntityModel);
      runnable.run();
      handlePlayerEntityModel2(playerEntityModel, flag, flag2, flag3, flag4, flag5, flag6);
   }

   public static void handlePlayerEntityModel2(
      PlayerEntityModel<?> playerEntityModel, boolean flag, boolean flag2, boolean flag3, boolean flag4, boolean flag5, boolean flag6
   ) {
      playerEntityModel.leftPants.visible = flag;
      playerEntityModel.rightPants.visible = flag2;
      playerEntityModel.leftSleeve.visible = flag3;
      playerEntityModel.rightSleeve.visible = flag4;
      playerEntityModel.jacket.visible = flag5;
      playerEntityModel.hat.visible = flag6;
   }

   public static void handleBufferBuilder(BufferBuilder bufferBuilder, boolean flag) {
      try {
         BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      } catch (IllegalStateException var3) {
      }

      if (flag) {
         RenderSystem.enableDepthTest();
      }

      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
   }

   public static void handleCuboid(Render3d.Cuboid cuboid) {
      cuboidQueue.add(cuboid);
   }

   public static void handlePlayerEntity(
      PlayerEntity playerEntity,
      EntityModel<PlayerEntity> entityModel,
      long l,
      Setting<Color> setting,
      Setting<Long> setting2,
      Setting<Boolean> setting3,
      Setting<Color> setting4,
      Setting<Float> setting5,
      float f
   ) {
      handlePlayerEntity3(playerEntity, entityModel, l, setting, setting2, setting3, setting4, setting5, f, model -> {});
   }

   public static void handleMatrixStack(MatrixStack matrixStack) {
      if (mc.getCameraEntity() instanceof PlayerEntity playerEntity) {
         float f = mc.getRenderTickCounter().getTickDelta(false);
         if (NoBobModule.isFlag()) {
            return;
         }

         float f2 = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
         float f3 = -(playerEntity.horizontalSpeed + f2 * f);
         float f4 = MathHelper.lerp(f, playerEntity.prevStrideDistance, playerEntity.strideDistance);
         matrixStack.translate(-(MathHelper.sin(f3 * (float) Math.PI) * f4 * 0.5), Math.abs(MathHelper.cos(f3 * (float) Math.PI) * f4), 0.0);
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(f3 * (float) Math.PI) * f4 * 3.0F));
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(f3 * (float) Math.PI - 0.2F) * f4) * 5.0F));
      }
   }

   public static void handlePlayerEntityModel3(PlayerEntityModel<?> playerEntityModel) {
      playerEntityModel.leftPants.visible = true;
      playerEntityModel.rightPants.visible = true;
      playerEntityModel.leftSleeve.visible = true;
      playerEntityModel.rightSleeve.visible = true;
      playerEntityModel.jacket.visible = true;
      playerEntityModel.hat.visible = true;
   }

   public static boolean testEntity(Entity entity) {
      if (mc.worldRenderer.frustum == null) {
         return true;
      }

      Box box = EntityInterpolation.resolveBox(entity, TimerManager.getFloatValue());
      return !box.isNaN() && box.getAverageSideLength() != 0.0 ? mc.worldRenderer.frustum.isVisible(box) : false;
   }

   public static void run3() {
      Vector3f vector3f = new Vector3f(0.0F, 0.0F, 1.0F);
      NoRenderModule.Bob bob = new NoRenderModule.Bob();
      ClientHolder.getEventBus().handleObject2(bob);
      if ((Boolean)mc.options.getBobView().getValue() && (!bob.isCancelled() || NoBobModule.isFlag())) {
         MatrixStack matrixStack = new MatrixStack();
         handleMatrixStack(matrixStack);
         vector3f.mulPosition(matrixStack.peek().getPositionMatrix().invert());
      }

      vec3d = new Vec3d(vector3f.x, -vector3f.y, vector3f.z)
         .rotateX(-((float)Math.toRadians(mc.gameRenderer.getCamera().getPitch())))
         .rotateY(-((float)Math.toRadians(mc.gameRenderer.getCamera().getYaw())))
         .add(mc.gameRenderer.getCamera().getPos());
   }

   public static void run5() {
      NamedResolver namedResolver = UniformSetup.createErrorRegistry();
      positionDissolveShader = namedResolver.resolveObject(PositionDissolveShader::new, "dissolve_shader");
      linesDissolveShader = namedResolver.resolveObject(LinesDissolveShader::new, "dissolve_lines_shader");
      multicolorShader = namedResolver.resolveObject(MulticolorShader::new, "multicolor");
      multicolorLinesShader = namedResolver.resolveObject(MulticolorLinesShader::new, "multicolor_lines");
      positionColorCircleShader = namedResolver.resolveObject(PositionColorCircleShader::new, "circle");
      linesCircleShader = namedResolver.resolveObject(LinesCircleShader::new, "circle_lines");
      positionColorPlasmaShader = namedResolver.resolveObject(PositionColorPlasmaShader::new, "plasma");
      linesPlasmaShader = namedResolver.resolveObject(LinesPlasmaShader::new, "plasma_lines");
      skyboxShader = namedResolver.resolveObject(SkyboxShader::new, "skybox");
      positionFluidShader = namedResolver.resolveObject(PositionFluidShader::new, "fluid");
      linesFluidShader = namedResolver.resolveObject(LinesFluidShader::new, "fluid_lines");
      positionSilkShader = namedResolver.resolveObject(PositionSilkShader::new, "silk");
      linesSilkShader = namedResolver.resolveObject(LinesSilkShader::new, "silk_lines");
      positionNoiseShader = namedResolver.resolveObject(PositionNoiseShader::new, "noise");
      linesNoiseShader = namedResolver.resolveObject(LinesNoiseShader::new, "noise_lines");
      positionFluxShader = namedResolver.resolveObject(PositionFluxShader::new, "flux");
      linesFluxShader = namedResolver.resolveObject(LinesFluxShader::new, "flux_lines");
      if (namedResolver instanceof ErrorRegistry errorRegistry) {
         for (Map.Entry<String, Throwable> entry : errorRegistry.getTextMap().entrySet()) {
            ClientHolder.getPhobosClient().handleText5("Renderer", "Failed to create a shader for " + entry.getKey() + ", Error: " + entry.getValue().getMessage());
         }
      }
   }

   public static void handlePlayerEntityModel4(PlayerEntityModel<?> playerEntityModel) {
      playerEntityModel.leftPants.visible = false;
      playerEntityModel.rightPants.visible = false;
      playerEntityModel.leftSleeve.visible = false;
      playerEntityModel.rightSleeve.visible = false;
      playerEntityModel.jacket.visible = false;
      playerEntityModel.hat.visible = false;
   }

   public static void handleTransformedLine(Render3d.TransformedLine transformedLine) {
      transformedLineQueue.add(transformedLine);
   }

   public static void start() {
      RenderSystem.disableCull();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void handlePlayerEntity2(
      PlayerEntity playerEntity, EntityModel<PlayerEntity> entityModel, long l, Color color, boolean flag, Color color2, float f, float f2
   ) {
      Render3d.Model.Builder builder = Render3d.Model.createBuilder()
         .resolveBuilder(entityModel)
         .resolveBuilder3(playerEntity)
         .resolveBuilder2(color, color2)
         .resolveBuilder4(f2);
      if (flag) {
         builder.resolveBuilder5(f);
      }

      handleModel(builder.getModel());
   }

   public static void handleCameraLine(Render3d.CameraLine cameraLine) {
      cameraLineQueue.add(cameraLine);
   }

   public static void run6() {
      RenderSystem.depthMask(false);
      RenderSystem.disableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 771, 0, 1);
   }

   public static void run7() {
      if (fogShape != null) {
         floatValue4 = RenderSystem.getShaderFogStart();
         floatValue2 = RenderSystem.getShaderFogEnd();
         fogShape2 = RenderSystem.getShaderFogShape();
         RenderSystem.setShaderFogStart(floatValue);
         RenderSystem.setShaderFogEnd(floatValue3);
         RenderSystem.setShaderFogShape(fogShape);
      }
   }

   public static void run8() {
      if (fogShape2 != null) {
         RenderSystem.setShaderFogStart(floatValue4);
         RenderSystem.setShaderFogEnd(floatValue2);
         RenderSystem.setShaderFogShape(fogShape2);
      }
   }

   public static boolean testBox(Box box) {
      if (mc.worldRenderer.frustum == null) {
         return true;
      } else {
         return !box.isNaN() && box.getAverageSideLength() != 0.0 ? mc.worldRenderer.frustum.isVisible(box) : false;
      }
   }

   public static void handleFade(Render3d.Fade fade) {
      fadeQueue.add(fade);
   }

   public static void handleConsumer(Consumer<Matrix4f> consumer) {
      consumerQueue.add(consumer);
   }

   public static void handleFlag(boolean flag, Runnable runnable) {
      handleFlag2(flag);
      runnable.run();
   }

   public static void handleLines(Render3d.Lines lines) {
      linesQueue.add(lines);
   }

   public static void handleFlag2(boolean flag) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO);
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      if (flag) {
         RenderSystem.disableDepthTest();
      }
   }

   public static void run4() {
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.depthFunc(515);
   }

   public static void handleModel(Render3d.Model model) {
      modelQueue.add(model);
   }

   public static void handleEndCrystalEntity(
      EndCrystalEntity endCrystalEntity,
      float f,
      MatrixStack matrixStack,
      int i,
      ModelPart modelPart,
      ModelPart modelPart2,
      Color color,
      boolean flag,
      Color color2,
      Color color3,
      boolean flag2,
      boolean flag3,
      Color color4,
      boolean flag4,
      float f2,
      float f3,
      boolean flag5,
      Color color5,
      RenderBatch renderBatch
   ) {
      if (flag2) {
         handleBufferBuilder3(Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR), () -> {
            RenderSystem.setShaderTexture(0, ChamsModule.identifier);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_COLOR, DstFactor.ONE, SrcFactor.ZERO, DstFactor.ONE);
            RenderSystem.blendFunc(SrcFactor.ONE, DstFactor.ONE);
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
            handleFloat2(ColorUtils.values4(color3));
         }, RenderSystem::defaultBlendFunc, modelPartx -> {}, endCrystalEntity, f, matrixStack, i, modelPart, modelPart2, true);
      }

      if (flag4) {
         handleBufferBuilder3(Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR), () -> {
            RenderSystem.setShaderTexture(0, ChamsModule.identifier2);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(SrcFactor.SRC_COLOR, DstFactor.ONE, SrcFactor.ZERO, DstFactor.ONE);
            RenderSystem.blendFunc(SrcFactor.ONE, DstFactor.ONE);
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(GameRenderer::getRenderTypeArmorEntityGlintProgram);
            RenderSystem.setShaderGlintAlpha(color4.getAlpha() / 255.0F);
            handleFloat2(ColorUtils.values3(ColorUtils.values4(color4), 1.0F));
            handleFloat(0.16F * f2, f3);
            run7();
         }, () -> {
            run8();
            RenderStates.runnable3.run();
            RenderSystem.defaultBlendFunc();
            RenderSystem.resetTextureMatrix();
         }, modelPartx -> {}, endCrystalEntity, f, matrixStack, i, modelPart, modelPart2, true);
      }

      if (flag3) {
         RenderBatch renderBatch2 = RenderBatch.getRenderBatch();
         handleBufferBuilder3(Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR), () -> {
            RenderSystem.enableBlend();
            run9();
            RenderSystem.disableDepthTest();
            renderBatch2.run3();
            renderBatch2.handleColor(color);
         }, renderBatch2::run4, modelPartx -> {
            if (flag) {
               handleColor6(color2, modelPartx, matrixStack);
            }
         }, endCrystalEntity, f, matrixStack, i, modelPart, modelPart2, true);
      }

      if (flag5) {
         handleBufferBuilder3(Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR), () -> {
            RenderSystem.enableBlend();
            run9();
            RenderSystem.disableDepthTest();
            renderBatch.run3();
            renderBatch.handleColor(color5);
         }, RenderSystem::defaultBlendFunc, modelPartx -> {}, endCrystalEntity, f, matrixStack, i, modelPart, modelPart2, true);
      }
   }

   public static void handleAbstractClientPlayerEntity(
      AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel
   ) {
      if (abstractClientPlayerEntity.isSpectator()) {
         playerEntityModel.setVisible(false);
         playerEntityModel.head.visible = true;
         playerEntityModel.hat.visible = true;
      } else {
         playerEntityModel.setVisible(true);
         playerEntityModel.hat.visible = abstractClientPlayerEntity.isPartVisible(PlayerModelPart.HAT);
         playerEntityModel.jacket.visible = abstractClientPlayerEntity.isPartVisible(PlayerModelPart.JACKET);
         playerEntityModel.leftPants.visible = abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
         playerEntityModel.rightPants.visible = abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
         playerEntityModel.leftSleeve.visible = abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
         playerEntityModel.rightSleeve.visible = abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
         playerEntityModel.sneaking = abstractClientPlayerEntity.isInSneakingPose();
         ArmPose armPose = PlayerEntityRenderer.getArmPose(abstractClientPlayerEntity, Hand.MAIN_HAND);
         ArmPose armPose2 = PlayerEntityRenderer.getArmPose(abstractClientPlayerEntity, Hand.OFF_HAND);
         if (armPose.isTwoHanded()) {
            armPose2 = abstractClientPlayerEntity.getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
         }

         if (abstractClientPlayerEntity.getMainArm() == Arm.RIGHT) {
            playerEntityModel.rightArmPose = armPose;
            playerEntityModel.leftArmPose = armPose2;
         } else {
            playerEntityModel.rightArmPose = armPose2;
            playerEntityModel.leftArmPose = armPose;
         }
      }
   }

   public static void handleRenderArmEvent(RenderArmEvent renderArmEvent, Color color, boolean flag, float f, Color color2) {
      Tessellator tessellator = Tessellator.getInstance();
      RenderSystem.enableBlend();
      run9();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(GameRenderer::getPositionProgram);
      RenderSystem.lineWidth(f);
      RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      BufferBuilder bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION);
      handleAbstractClientPlayerEntity(renderArmEvent.getAbstractClientPlayerEntity(), renderArmEvent.getAbstractClientPlayerEntityPlayerEntityModel());
      renderArmEvent.getAbstractClientPlayerEntityPlayerEntityModel().handSwingProgress = 0.0F;
      renderArmEvent.getAbstractClientPlayerEntityPlayerEntityModel().sneaking = false;
      renderArmEvent.getAbstractClientPlayerEntityPlayerEntityModel().leaningPitch = 0.0F;
      renderArmEvent.getAbstractClientPlayerEntityPlayerEntityModel().setAngles(renderArmEvent.getAbstractClientPlayerEntity(), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      renderArmEvent.getModelPart().pitch = 0.0F;
      renderArmEvent.getModelPart2().pitch = 0.0F;
      renderArmEvent.getModelPart().render(renderArmEvent.getMatrixStack(), bufferBuilder, 0, 0);
      renderArmEvent.getModelPart2().render(renderArmEvent.getMatrixStack(), bufferBuilder, 0, 0);
      BufferUtils.handleBufferBuilder(bufferBuilder);
      if (flag) {
         renderArmEvent.getModelPart().pitch = 0.0F;
         renderArmEvent.getModelPart2().pitch = 0.0F;
         Runnable runnable = () -> RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
         handleColor5(color2, renderArmEvent.getModelPart(), renderArmEvent.getMatrixStack(), f, runnable);
         handleColor5(color2, renderArmEvent.getModelPart2(), renderArmEvent.getMatrixStack(), f, runnable);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      renderArmEvent.setCancelled(true);
   }

   public static void handleBufferBuilder2(
      BufferBuilder bufferBuilder, RenderContext renderContext, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9
   ) {
      handleBufferBuilder8(bufferBuilder, renderContext, (float)d, (float)d2, (float)d3, (float)d4, (float)d5, (float)d6, (float)d7, (float)d8, (float)d9);
   }

   public static void handleRenderContext(RenderContext renderContext, List<Render3d.Line> list, float f, long l, long l2) {
      BufferBuilder bufferBuilder = resolveBufferBuilder(f, true);

      for (Render3d.Line line : list) {
         renderContext.getMutableVec3d2().handleVec3d(line.vec3d2);
         renderContext.getMutableVec3d().handleVec3d(line.vec3d);
         if (l != 0L) {
            float f2 = line.color.getAlpha() / 255.0F * 255.0F;
            float f3 = f2 / (float)l;
            float f4 = MathHelper.clamp((int)(f3 * (float)(l2 + l - System.currentTimeMillis())), 0, (int)f2) / 255.0F;
            Color color = ColorUtils.createColor6(line.color, f4);
            renderContext.getRect42().handleColor(color);
            handleBufferBuilder6(bufferBuilder, renderContext);
         } else {
            renderContext.getRect42().handleColor(line.color);
            handleBufferBuilder6(bufferBuilder, renderContext);
         }
      }

      handleBufferBuilder(bufferBuilder, true);
   }

   public static void handlePlayerEntity3(
      PlayerEntity playerEntity,
      EntityModel<PlayerEntity> entityModel,
      long l,
      Setting<Color> setting,
      Setting<Long> setting2,
      Setting<Boolean> setting3,
      Setting<Color> setting4,
      Setting<Float> setting5,
      float f,
      Consumer<Render3d.Model> consumer
   ) {
      float f2 = setting.getValue().getAlpha();
      float f3 = setting4.getValue().getAlpha();
      float f4 = f2 / (float)setting2.getValue().longValue();
      float f5 = f3 / (float)setting2.getValue().longValue();
      int i = MathHelper.clamp((int)(f4 * (float)(l + setting2.getValue() - System.currentTimeMillis())), 0, (int)f2);
      int i2 = MathHelper.clamp((int)(f5 * (float)(l + setting2.getValue() - System.currentTimeMillis())), 0, (int)f3);
      Color color = ColorUtils.createColor6(setting.getValue(), i / 255.0F);
      Color color2 = ColorUtils.createColor6(setting4.getValue(), i2 / 255.0F);
      Render3d.Model.Builder builder = Render3d.Model.createBuilder()
         .resolveBuilder(entityModel)
         .resolveBuilder3(playerEntity)
         .resolveBuilder2(color, color2)
         .resolveBuilder4(f);
      if (setting3.getValue()) {
         builder.resolveBuilder5(setting5.getValue());
      }

      Render3d.Model model = builder.getModel();
      consumer.accept(model);
      handleModel(model);
   }

   public static void handleBufferBuilder3(
      BufferBuilder bufferBuilder,
      Runnable runnable,
      Runnable runnable2,
      Consumer<ModelPart> consumer,
      EndCrystalEntity endCrystalEntity,
      float f,
      MatrixStack matrixStack,
      int i,
      ModelPart modelPart,
      ModelPart modelPart2,
      boolean flag
   ) {
      float f2 = ChamsModule.floatValue;
      int i2 = OverlayTexture.DEFAULT_UV;
      runnable.run();
      matrixStack.push();
      float f3 = computeEndCrystalEntity(endCrystalEntity, f);
      float f4 = AnimationsModule.computeFloat((endCrystalEntity.endCrystalAge + f) * 3.0F);
      float f5 = 0.875F;
      matrixStack.push();
      float f6 = AnimationsModule.value();
      matrixStack.scale(f6, f6, f6);
      matrixStack.translate(0.0F, -0.5F, 0.0F);
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4));
      matrixStack.translate(0.0F, 1.5F + f3 / 2.0F, 0.0F);
      matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), f2, 0.0F, f2));
      if (flag) {
         modelPart2.render(matrixStack, bufferBuilder, i, i2);
      }

      consumer.accept(modelPart2);
      matrixStack.scale(f5, f5, f5);
      matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), f2, 0.0F, f2));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4));
      if (flag) {
         modelPart2.render(matrixStack, bufferBuilder, i, i2);
      }

      consumer.accept(modelPart2);
      matrixStack.scale(f5, f5, f5);
      matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), f2, 0.0F, f2));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f4));
      if (flag) {
         modelPart.render(matrixStack, bufferBuilder, i, i2);
      }

      consumer.accept(modelPart2);
      matrixStack.pop();
      matrixStack.pop();
      if (flag) {
         BufferUtils.handleBufferBuilder(bufferBuilder);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      RenderSystem.enableDepthTest();
      runnable2.run();
   }

   public static String describeInt(int i) {
      return IntStream.range(0, i).mapToObj(ix -> String.valueOf((char)random.nextInt(97, 123))).collect(Collectors.joining());
   }

   public static Identifier getIdentifier() {
      return Identifier.of("phoboslite", "temp/" + describeInt(32));
   }

   public static void handleBufferBuilder4(
      BufferBuilder bufferBuilder, RenderContext renderContext, double d, double d2, double d3, double d4, double d5, double d6
   ) {
      double d7 = d4 - d;
      double d8 = d5 - d2;
      double d9 = d6 - d3;
      double d10 = MathHelper.sqrt((float)(d7 * d7 + d8 * d8 + d9 * d9));
      float f = (float)(d7 / d10);
      float f2 = (float)(d8 / d10);
      float f3 = (float)(d9 / d10);
      handleBufferBuilder2(bufferBuilder, renderContext, d, d2, d3, d4, d5, d6, f, f2, f3);
   }

   public static <T extends LivingEntity, M extends EntityModel<T>> void handleMatrixStack2(
      MatrixStack matrixStack,
      T livingEntity,
      M entityModel,
      List<FeatureRenderer<T, M>> list,
      float f,
      int i,
      Color color,
      boolean flag,
      Color color2,
      boolean flag2,
      Color color3,
      boolean flag3,
      boolean flag4,
      boolean flag5,
      boolean flag6,
      Color color4,
      boolean flag7,
      float f2,
      float f3,
      boolean flag8,
      Color color5,
      RenderBatch renderBatch
   ) {
      PlayerRenderState playerRenderState = ((ILivingEntity)(Object)livingEntity).getGhostModel();
      color = playerRenderState != null ? ColorUtils.createColor4(color, playerRenderState.getFloatValue2()) : color;
      color2 = playerRenderState != null ? ColorUtils.createColor4(color2, playerRenderState.getFloatValue2()) : color2;
      color4 = playerRenderState != null ? ColorUtils.createColor4(color4, playerRenderState.getFloatValue2()) : color4;
      Tessellator tessellator = Tessellator.getInstance();
      matrixStack.push();
      entityModel.handSwingProgress = livingEntity.getHandSwingProgress(f);
      entityModel.riding = livingEntity.hasVehicle();
      entityModel.child = livingEntity.isBaby();
      float f6 = MathHelper.lerpAngleDegrees(f, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
      float f7 = MathHelper.lerp(f, livingEntity.prevHeadYaw, livingEntity.headYaw);
      float f8 = livingEntity.getPitch(f);
      float f9 = f7 - f6;
      if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity livingEntity2) {
         f6 = MathHelper.lerpAngleDegrees(f, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
         f9 = f7 - f6;
         float f4 = MathHelper.wrapDegrees(f9);
         if (f4 < -85.0F) {
            f4 = -85.0F;
         }

         if (f4 >= 85.0F) {
            f4 = 85.0F;
         }

         f6 = f7 - f4;
         if (f4 * f4 > 2500.0F) {
            f6 += f4 * 0.2F;
         }

         f9 = f7 - f6;
      }

      float f10 = MathHelper.lerp(f, livingEntity.prevYaw, livingEntity.getYaw());
      if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
         f10 *= -1.0F;
         f9 *= -1.0F;
      }

      Direction direction;
      if (livingEntity.isInPose(EntityPose.SLEEPING) && (direction = livingEntity.getSleepingDirection()) != null) {
         float f5 = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
         matrixStack.translate(-direction.getOffsetX() * f5, 0.0F, -direction.getOffsetZ() * f5);
      }

      float f11 = livingEntity.getScale();
      matrixStack.scale(f11, f11, f11);
      float f12 = livingEntity.age + f;
      AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity;
      handleAbstractClientPlayerEntity2(abstractClientPlayerEntity, matrixStack, f12, f6, f, f11);
      matrixStack.scale(-1.0F, -1.0F, 1.0F);
      matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
      matrixStack.translate(0.0F, -1.501F, 0.0F);
      float f13 = 0.0F;
      float f14 = 0.0F;
      if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
         f13 = livingEntity.limbAnimator.getSpeed(f);
         f14 = livingEntity.limbAnimator.getPos(f);
         if (livingEntity.isBaby()) {
            f14 *= 3.0F;
         }

         if (f13 > 1.0F) {
            f13 = 1.0F;
         }
      }

      entityModel.animateModel(livingEntity, f14, f13, f);
      entityModel.setAngles(livingEntity, f14, f13, f12, f9, f8);
      int i2 = LivingEntityRenderer.getOverlay(livingEntity, 0.0F);
      if (flag3) {
         RenderSystem.enableBlend();
         RenderSystem.setShaderTexture(0, ChamsModule.identifier);
         RenderSystem.blendFuncSeparate(SrcFactor.SRC_COLOR, DstFactor.ONE, SrcFactor.ZERO, DstFactor.ONE);
         RenderSystem.blendFunc(SrcFactor.ONE, DstFactor.ONE);
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
         handleFloat2(ColorUtils.values4(ChamsModule.resolveColor2(abstractClientPlayerEntity, color3)));
         BufferBuilder bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         entityModel.render(matrixStack, bufferBuilder, i, i2);
         BufferUtils.handleBufferBuilder(bufferBuilder);
         RenderSystem.defaultBlendFunc();
         RenderSystem.resetTextureMatrix();
      }

      if (flag7) {
         RenderSystem.enableBlend();
         RenderSystem.setShaderTexture(0, ChamsModule.identifier2);
         RenderSystem.blendFuncSeparate(SrcFactor.SRC_COLOR, DstFactor.ONE, SrcFactor.ZERO, DstFactor.ONE);
         RenderSystem.blendFunc(SrcFactor.ONE, DstFactor.ONE);
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.setShader(GameRenderer::getRenderTypeArmorEntityGlintProgram);
         RenderSystem.setShaderGlintAlpha(color4.getAlpha() / 255.0F);
         handleColor(ChamsModule.resolveColor2(abstractClientPlayerEntity, ColorUtils.createColor6(color4, 1.0F)));
         handleFloat(0.16F * f2, f3);
         run7();
         BufferBuilder bufferBuilder2 = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         entityModel.render(matrixStack, bufferBuilder2, i, i2);
         BufferUtils.handleBufferBuilder(bufferBuilder2);
         run8();
         RenderStates.runnable3.run();
         RenderSystem.defaultBlendFunc();
         RenderSystem.resetTextureMatrix();
      }

      if (flag4) {
         RenderBatch renderBatch2 = RenderBatch.getRenderBatch();
         RenderSystem.enableBlend();
         run9();
         RenderSystem.disableCull();
         RenderSystem.depthMask(false);
         RenderSystem.disableDepthTest();
         renderBatch2.run3();
         renderBatch2.handleColor(ChamsModule.resolveColor2(abstractClientPlayerEntity, color));
         BufferBuilder bufferBuilder3 = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         entityModel.render(matrixStack, bufferBuilder3, 0, 0);
         BufferUtils.handleBufferBuilder(bufferBuilder3);
         renderBatch2.run4();
         if (flag) {
            BipedEntityModel<?> bipedEntityModel = (BipedEntityModel<?>)entityModel;
            Color color6 = ChamsModule.resolveColor2(abstractClientPlayerEntity, color2);
            handleColor6(color6, bipedEntityModel.head, matrixStack);
            handleColor6(color6, bipedEntityModel.body, matrixStack);
            handleColor6(color6, bipedEntityModel.leftArm, matrixStack);
            handleColor6(color6, bipedEntityModel.leftLeg, matrixStack);
            handleColor6(color6, bipedEntityModel.rightArm, matrixStack);
            handleColor6(color6, bipedEntityModel.rightLeg, matrixStack);
            if (flag5 && !abstractClientPlayerEntity.isSpectator() && bipedEntityModel instanceof PlayerEntityModel<?> playerEntityModel) {
               handlePlayerEntityModel(playerEntityModel, () -> {
                  handleColor6(color6, playerEntityModel.hat, matrixStack);
                  handleColor6(color6, playerEntityModel.jacket, matrixStack);
                  handleColor6(color6, playerEntityModel.rightSleeve, matrixStack);
                  handleColor6(color6, playerEntityModel.rightPants, matrixStack);
                  handleColor6(color6, playerEntityModel.leftSleeve, matrixStack);
                  handleColor6(color6, playerEntityModel.leftPants, matrixStack);
               });
            }
         }
      }

      if (flag8) {
         RenderSystem.enableBlend();
         run9();
         RenderSystem.disableCull();
         RenderSystem.depthMask(false);
         RenderSystem.disableDepthTest();
         renderBatch.run3();
         renderBatch.handleColor(ChamsModule.resolveColor2(abstractClientPlayerEntity, color5));
         BufferBuilder bufferBuilder4 = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         entityModel.render(matrixStack, bufferBuilder4, 0, 0);
         BufferUtils.handleBufferBuilder(bufferBuilder4);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      if (!livingEntity.isSpectator() && flag2) {
         RenderSystem.disableDepthTest();

         for (FeatureRenderer<T, M> featureRenderer : list) {
            featureRenderer.render(matrixStack, mc.getBufferBuilders().getEntityVertexConsumers(), i2, livingEntity, f14, f13, f, f12, f9, f8);
         }

         RenderSystem.enableDepthTest();
      }

      matrixStack.pop();
   }

   public static float computeEndCrystalEntity(EndCrystalEntity endCrystalEntity, float f) {
      float f2 = endCrystalEntity.endCrystalAge + f;
      float f3 = MathHelper.sin(f2 * 0.2F) / AnimationsModule.computeEndCrystalEntity(endCrystalEntity) + 0.5F;
      f3 = (f3 * f3 + f3) * 0.4F;
      return AnimationsModule.computeFloat2(f3) - 1.4F;
   }

   public static BufferBuilder resolveBufferBuilder(float f, boolean flag) {
      return resolveBufferBuilder2(f, flag, () -> RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram));
   }

   public static void handleMatrixStack3(
      MatrixStack matrixStack,
      PlayerEntity playerEntity,
      EntityModel<PlayerEntity> entityModel,
      Render3d.Model model,
      float f,
      Color color,
      boolean flag,
      Color color2,
      float f2
   ) {
      Vec3d vec3d = mc.gameRenderer.getCamera().getPos();
      double d = vec3d.x;
      double d2 = vec3d.y;
      double d3 = vec3d.z;
      float f3 = mc.getRenderTickCounter().getTickDelta(false);
      double d4 = MathHelper.lerp(f3, playerEntity.lastRenderX, playerEntity.getX());
      double d5 = MathHelper.lerp(f3, playerEntity.lastRenderY, playerEntity.getY());
      double d6 = MathHelper.lerp(f3, playerEntity.lastRenderZ, playerEntity.getZ());
      float f4 = MathHelper.lerp(f, playerEntity.prevYaw, playerEntity.getYaw());
      handleMatrixStack4(matrixStack, playerEntity, entityModel, model, d4 - d, d5 - d2, d6 - d3, f4, f, color, color2, flag, f2);
   }

   public static void handleBufferBuilder5(BufferBuilder bufferBuilder, RenderContext renderContext) {
      mutableBox.handleMutableBox(renderContext.getMutableBox());
      mutableBox.handleDouble2(-iEntityRenderDispatcher.renderPosX(), -iEntityRenderDispatcher.renderPosY(), -iEntityRenderDispatcher.renderPosZ());
      Matrix4f matrix4f = renderContext.getMatrixStack().peek().getPositionMatrix();
      float f = renderContext.getRect4().getFloatValue();
      float f2 = renderContext.getRect4().getFloatValue4();
      float f3 = renderContext.getRect4().getFloatValue3();
      float f4 = renderContext.getRect4().getFloatValue2();
      float f5 = (float)mutableBox.getDoubleValue6();
      float f6 = (float)mutableBox.getDoubleValue();
      float f7 = (float)mutableBox.getDoubleValue3();
      float f8 = (float)mutableBox.getDoubleValue2();
      float f9 = (float)mutableBox.getDoubleValue5();
      float f10 = (float)mutableBox.getDoubleValue4();
      bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
      bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
   }

   public static void handleMatrixStack4(
      MatrixStack matrixStack,
      PlayerEntity playerEntity,
      EntityModel<PlayerEntity> entityModel,
      Render3d.Model model,
      double d,
      double d2,
      double d3,
      float f,
      float f2,
      Color color,
      Color color2,
      boolean flag,
      float f3
   ) {
      matrixStack.push();
      matrixStack.translate(d, d2, d3);
      Tessellator tessellator = Tessellator.getInstance();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      model.getRunnable().run();
      RenderSystem.lineWidth(f3);
      BufferBuilder bufferBuilder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION);
      matrixStack.push();
      RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      entityModel.handSwingProgress = playerEntity.getHandSwingProgress(f2);
      entityModel.riding = playerEntity.hasVehicle();
      entityModel.child = playerEntity.isBaby();
      float f6 = MathHelper.lerpAngleDegrees(f2, playerEntity.prevBodyYaw, playerEntity.bodyYaw);
      float f7 = MathHelper.lerp(f2, playerEntity.prevHeadYaw, playerEntity.headYaw);
      float f8 = playerEntity.getPitch(f2);
      float f9 = f7 - f6;
      if (playerEntity.hasVehicle() && playerEntity.getVehicle() instanceof LivingEntity livingEntity) {
         f6 = MathHelper.lerpAngleDegrees(f2, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
         f9 = f7 - f6;
         float f4 = MathHelper.wrapDegrees(f9);
         if (f4 < -85.0F) {
            f4 = -85.0F;
         }

         if (f4 >= 85.0F) {
            f4 = 85.0F;
         }

         f6 = f7 - f4;
         if (f4 * f4 > 2500.0F) {
            f6 += f4 * 0.2F;
         }

         f9 = f7 - f6;
      }

      float f10 = MathHelper.lerp(f2, playerEntity.prevYaw, playerEntity.getYaw());
      if (LivingEntityRenderer.shouldFlipUpsideDown(playerEntity)) {
         f10 *= -1.0F;
         f9 *= -1.0F;
      }

      Direction direction;
      if (playerEntity.isInPose(EntityPose.SLEEPING) && (direction = playerEntity.getSleepingDirection()) != null) {
         float f5 = playerEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
         matrixStack.translate(-direction.getOffsetX() * f5, 0.0F, -direction.getOffsetZ() * f5);
      }

      float f11 = playerEntity.getScale();
      matrixStack.scale(f11, f11, f11);
      float f12 = playerEntity.age + f2;
      handlePlayerEntity4(playerEntity, matrixStack, f12, f6, f2, f11);
      matrixStack.scale(-1.0F, -1.0F, 1.0F);
      matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
      matrixStack.translate(0.0F, -1.501F, 0.0F);
      float f13 = 0.0F;
      float f14 = 0.0F;
      if (!playerEntity.hasVehicle() && playerEntity.isAlive()) {
         f13 = playerEntity.limbAnimator.getSpeed(f2);
         f14 = playerEntity.limbAnimator.getPos(f2);
         if (playerEntity.isBaby()) {
            f14 *= 3.0F;
         }

         if (f13 > 1.0F) {
            f13 = 1.0F;
         }
      }

      entityModel.animateModel(playerEntity, f14, f13, f2);
      entityModel.setAngles(playerEntity, f14, f13, f12, f9, f8);
      int i = LivingEntityRenderer.getOverlay(playerEntity, 0.0F);
      entityModel.render(matrixStack, bufferBuilder, 0, i);
      BufferUtils.handleBufferBuilder(bufferBuilder);
      if (flag && entityModel instanceof BipedEntityModel<?> bipedEntityModel) {
         handleColor5(color2, bipedEntityModel.body, matrixStack, f3, model.getRunnable2());
         handleColor5(color2, bipedEntityModel.leftArm, matrixStack, f3, model.getRunnable2());
         handleColor5(color2, bipedEntityModel.leftLeg, matrixStack, f3, model.getRunnable2());
         handleColor5(color2, bipedEntityModel.rightArm, matrixStack, f3, model.getRunnable2());
         handleColor5(color2, bipedEntityModel.rightLeg, matrixStack, f3, model.getRunnable2());
         handleColor5(color2, bipedEntityModel.head, matrixStack, f3, model.getRunnable2());
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      matrixStack.pop();
      matrixStack.pop();
   }

   public static void handleFloat(float f, double d) {
      long l = (long)(Util.getMeasuringTimeMs() * d * 8.0);
      float f2 = (float)(l % 110000L) / 110000.0F;
      float f3 = (float)(l % 30000L) / 30000.0F;
      Matrix4f matrix4f = new Matrix4f().translation(-f2, f3, 0.0F);
      matrix4f.rotateZ((float) (Math.PI / 18)).scale(f);
      RenderSystem.setTextureMatrix(matrix4f);
   }

   public static void handleIdentifier(@NonNull Identifier identifier, @NonNull BufferedImage bufferedImage) {
      if (identifier == null) {
         throw new NullPointerException("i is marked non-null but is null");
      }

      if (bufferedImage == null) {
         throw new NullPointerException("bi is marked non-null but is null");
      }

      try {
         int i8 = bufferedImage.getWidth();
         int i9 = bufferedImage.getHeight();
         NativeImage nativeImage = new NativeImage(Format.RGBA, i8, i9, false);
         long l = ((INativeImage)(Object)nativeImage).getPointer();
         IntBuffer intBuffer = MemoryUtil.memIntBuffer(l, nativeImage.getWidth() * nativeImage.getHeight());
         WritableRaster writableRaster = bufferedImage.getRaster();
         ColorModel colorModel = bufferedImage.getColorModel();
         int i10 = writableRaster.getNumBands();
         int i11 = writableRaster.getDataBuffer().getDataType();

         Object object = switch (i11) {
            case 0 -> new byte[i10];
            case 1 -> new short[i10];
            default -> throw new IllegalArgumentException("Unknown data buffer type: " + i11);
            case 3 -> new int[i10];
            case 4 -> new float[i10];
            case 5 -> new double[i10];
         };

         for (int i7 = 0; i7 < i9; i7++) {
            for (int i6 = 0; i6 < i8; i6++) {
               writableRaster.getDataElements(i6, i7, object);
               int i = colorModel.getAlpha(object);
               int i2 = colorModel.getRed(object);
               int i3 = colorModel.getGreen(object);
               int i4 = colorModel.getBlue(object);
               int i5 = i << 24 | i4 << 16 | i3 << 8 | i2;
               intBuffer.put(i5);
            }
         }

         NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
         nativeImageBackedTexture.upload();
         if (RenderSystem.isOnRenderThread()) {
            MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, nativeImageBackedTexture);
         } else {
            RenderSystem.recordRenderCall(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, nativeImageBackedTexture));
         }
      } catch (Throwable var20) {
      }
   }

   public static void handleBufferBuilder6(BufferBuilder bufferBuilder, RenderContext renderContext) {
      double d = iEntityRenderDispatcher.renderPosX();
      double d2 = iEntityRenderDispatcher.renderPosY();
      double d3 = iEntityRenderDispatcher.renderPosZ();
      double d4 = renderContext.getMutableVec3d2().getDoubleValue3() - d;
      double d5 = renderContext.getMutableVec3d2().getDoubleValue() - d2;
      double d6 = renderContext.getMutableVec3d2().getDoubleValue2() - d3;
      double d7 = renderContext.getMutableVec3d().getDoubleValue3() - d;
      double d8 = renderContext.getMutableVec3d().getDoubleValue() - d2;
      double d9 = renderContext.getMutableVec3d().getDoubleValue2() - d3;
      handleBufferBuilder4(bufferBuilder, renderContext, d4, d5, d6, d7, d8, d9);
   }

   @NotNull
   public static NativeImageBackedTexture resolveNativeImageBackedTexture(@NotNull BufferedImage bufferedImage) {
      int i8 = bufferedImage.getWidth();
      int i9 = bufferedImage.getHeight();
      NativeImage nativeImage = new NativeImage(Format.RGBA, i8, i9, false);
      long l = ((INativeImage)(Object)nativeImage).getPointer();
      IntBuffer intBuffer = MemoryUtil.memIntBuffer(l, nativeImage.getWidth() * nativeImage.getHeight());
      WritableRaster writableRaster = bufferedImage.getRaster();
      ColorModel colorModel = bufferedImage.getColorModel();
      int i10 = writableRaster.getNumBands();
      int i11 = writableRaster.getDataBuffer().getDataType();

      Object object = switch (i11) {
         case 0 -> new byte[i10];
         case 1 -> new short[i10];
         default -> throw new IllegalArgumentException("Unknown data buffer type: " + i11);
         case 3 -> new int[i10];
         case 4 -> new float[i10];
         case 5 -> new double[i10];
      };

      for (int i7 = 0; i7 < i9; i7++) {
         for (int i6 = 0; i6 < i8; i6++) {
            writableRaster.getDataElements(i6, i7, object);
            int i = colorModel.getAlpha(object);
            int i2 = colorModel.getRed(object);
            int i3 = colorModel.getGreen(object);
            int i4 = colorModel.getBlue(object);
            int i5 = i << 24 | i4 << 16 | i3 << 8 | i2;
            intBuffer.put(i5);
         }
      }

      NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
      mc.submit(nativeImageBackedTexture::upload);
      return nativeImageBackedTexture;
   }

   public static void run9() {
      if (ChamsModule.check()) {
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
      } else {
         RenderSystem.defaultBlendFunc();
      }
   }

   public static BufferBuilder resolveBufferBuilder2(float f, boolean flag, Runnable runnable) {
      handleFlag2(flag);
      RenderSystem.lineWidth(f);
      runnable.run();
      return Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);
   }

   public static Vec3d createVec3d(Vec3d vec3d) {
      MinecraftClient minecraftClient = MinecraftClient.getInstance();
      return new Vec3d(
         vec3d.x - minecraftClient.getEntityRenderDispatcher().camera.getPos().x,
         vec3d.y - minecraftClient.getEntityRenderDispatcher().camera.getPos().y,
         vec3d.z - minecraftClient.getEntityRenderDispatcher().camera.getPos().z
      );
   }

   public static void handleRenderContext2(RenderContext renderContext, float f, boolean flag, Runnable runnable, Runnable runnable2) {
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      handleFlag(flag, runnable);
      handleBufferBuilder5(bufferBuilder, renderContext);
      handleBufferBuilder(bufferBuilder, flag);
      BufferBuilder bufferBuilder2 = resolveBufferBuilder2(f, flag, runnable2);
      handleBufferBuilder7(bufferBuilder2, renderContext);
      handleBufferBuilder(bufferBuilder2, flag);
   }

   public static int value() {
      return (int)MinecraftClient.getInstance().getWindow().getScaleFactor();
   }

   public static void handleBufferBuilder7(BufferBuilder bufferBuilder, RenderContext renderContext) {
      mutableBox.handleMutableBox(renderContext.getMutableBox());
      mutableBox.handleDouble2(-iEntityRenderDispatcher.renderPosX(), -iEntityRenderDispatcher.renderPosY(), -iEntityRenderDispatcher.renderPosZ());
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         1.0,
         0.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue4(),
         0.0,
         0.0,
         1.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue4(),
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue4(),
         -1.0,
         0.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue4(),
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         0.0,
         0.0,
         -1.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue3(),
         1.0,
         0.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4(),
         0.0,
         0.0,
         1.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4(),
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4(),
         -1.0,
         0.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4(),
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue3(),
         0.0,
         0.0,
         -1.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue3(),
         0.0,
         1.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue3(),
         0.0,
         1.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue4(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4(),
         0.0,
         1.0,
         0.0
      );
      handleBufferBuilder2(
         bufferBuilder,
         renderContext,
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue4(),
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4(),
         0.0,
         1.0,
         0.0
      );
   }

   public static void handleBufferBuilder8(
      BufferBuilder bufferBuilder, RenderContext renderContext, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9
   ) {
      Matrix4f matrix4f = renderContext.getMatrixStack().peek().getPositionMatrix();
      Entry entry = renderContext.getMatrixStack().peek();
      Rect4 rect4 = renderContext.getRect42();
      bufferBuilder.vertex(matrix4f, f, f2, f3)
         .color(rect4.getFloatValue(), rect4.getFloatValue4(), rect4.getFloatValue3(), rect4.getFloatValue2())
         .normal(entry, f7, f8, f9);
      bufferBuilder.vertex(matrix4f, f4, f5, f6)
         .color(rect4.getFloatValue(), rect4.getFloatValue4(), rect4.getFloatValue3(), rect4.getFloatValue2())
         .normal(entry, f7, f8, f9);
   }

   public static <T extends LivingEntity> boolean testLivingEntity(T livingEntity) {
      return livingEntity.isFrozen();
   }

   public static void handleColor(Color color) {
      float[] floats = ColorUtils.values4(color);
      RenderSystem.setShaderColor(floats[0], floats[1], floats[2], floats[3]);
   }

   public static void handleColor2(Color color, net.minecraft.client.model.ModelPart.Cuboid cuboid, MatrixStack matrixStack) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      Vector4f vector4f = new Vector4f();
      Vector4f vector4f2 = new Vector4f();
      Vector4f vector4f3 = new Vector4f();
      Vector4f vector4f4 = new Vector4f();

      for (Quad quad : cuboid.sides) {
         vector4f.set(quad.vertices[0].pos.x / 16.0F, quad.vertices[0].pos.y / 16.0F, quad.vertices[0].pos.z / 16.0F, 1.0F);
         vector4f.mul(matrix4f);
         vector4f2.set(quad.vertices[1].pos.x / 16.0F, quad.vertices[1].pos.y / 16.0F, quad.vertices[1].pos.z / 16.0F, 1.0F);
         vector4f2.mul(matrix4f);
         vector4f3.set(quad.vertices[2].pos.x / 16.0F, quad.vertices[2].pos.y / 16.0F, quad.vertices[2].pos.z / 16.0F, 1.0F);
         vector4f3.mul(matrix4f);
         vector4f4.set(quad.vertices[3].pos.x / 16.0F, quad.vertices[3].pos.y / 16.0F, quad.vertices[3].pos.z / 16.0F, 1.0F);
         vector4f4.mul(matrix4f);
         handleDouble2(vector4f.x, vector4f.y, vector4f.z, vector4f2.x, vector4f2.y, vector4f2.z, color);
         handleDouble2(vector4f2.x, vector4f2.y, vector4f2.z, vector4f3.x, vector4f3.y, vector4f3.z, color);
         handleDouble2(vector4f3.x, vector4f3.y, vector4f3.z, vector4f4.x, vector4f4.y, vector4f4.z, color);
         handleDouble2(vector4f.x, vector4f.y, vector4f.z, vector4f.x, vector4f.y, vector4f.z, color);
      }
   }

   public static void handleColor3(Color color, Runnable runnable) {
      handleColor(color);
      runnable.run();
      run();
   }

   public static void handleAbstractClientPlayerEntity2(
      AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float f2, float f3, float f4
   ) {
      float f9 = abstractClientPlayerEntity.getLeaningPitch(f3);
      float f10 = abstractClientPlayerEntity.getPitch(f3);
      if (EntityUtils.testLivingEntity3(abstractClientPlayerEntity)) {
         handleLivingEntity(abstractClientPlayerEntity, matrixStack, f, f2, f3, f4);
         float f5 = abstractClientPlayerEntity.getFallFlyingTicks() + f3;
         float f6 = MathHelper.clamp(f5 * f5 / 100.0F, 0.0F, 1.0F);
         if (!abstractClientPlayerEntity.isUsingRiptide()) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f6 * (-90.0F - f10)));
         }

         Vec3d vec3d = abstractClientPlayerEntity.getRotationVec(f3);
         Vec3d vec3d2 = abstractClientPlayerEntity.lerpVelocity(f3);
         double d3 = vec3d2.horizontalLengthSquared();
         double d4 = vec3d.horizontalLengthSquared();
         if (d3 > 0.0 && d4 > 0.0) {
            double d = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d3 * d4);
            double d2 = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(d2) * Math.acos(d))));
         }
      } else if (f9 > 0.0F) {
         handleLivingEntity(abstractClientPlayerEntity, matrixStack, f, f2, f3, f4);
         float f7 = abstractClientPlayerEntity.isTouchingWater() ? -90.0F - f10 : -90.0F;
         float f8 = MathHelper.lerp(f9, 0.0F, f7);
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f8));
         if (abstractClientPlayerEntity.isInSwimmingPose()) {
            matrixStack.translate(0.0F, -1.0F, 0.3F);
         }
      } else {
         handleLivingEntity(abstractClientPlayerEntity, matrixStack, f, f2, f3, f4);
      }
   }

   public static void handleText(String text, float f, double d, double d2, double d3, Camera camera, Color color, float f2, boolean flag, boolean flag2) {
      float f3 = camera.getYaw();
      float f4 = camera.getPitch();
      Vec3d vec3d = camera.getPos();
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.push();
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      matrixStack.translate(d - vec3d.x, d2 - vec3d.y, d3 - vec3d.z);
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f3));
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f4));
      GL11.glDepthFunc(519);
      matrixStack.scale(-f2, -f2, -1.0F);
      handleMatrixStack5(matrixStack, text, -f, color, flag, flag2);
      GL11.glDepthFunc(515);
      matrixStack.pop();
   }

   public static Vector3f createVector3f(float f, float f2, float f3, float f4, float f5, float f6) {
      float f7 = f4 - f;
      float f8 = f5 - f2;
      float f9 = f6 - f3;
      float f10 = MathHelper.sqrt(f7 * f7 + f8 * f8 + f9 * f9);
      return new Vector3f(f7 / f10, f8 / f10, f9 / f10);
   }

   public static void handleBox(Box box, Matrix4f matrix4f, Color color, Color color2) {
      float f = (float)(box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
      float f2 = (float)(box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
      float f3 = (float)(box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
      float f4 = (float)(box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
      float f5 = (float)(box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
      float f6 = (float)(box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
      BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      handleFlag2(true);
      bufferBuilder.vertex(matrix4f, f, f2, f3).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f, f5, f3).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f5, f3).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f2, f3).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f2, f3).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f5, f3).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f5, f6).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f2, f6).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f, f2, f6).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f2, f6).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f5, f6).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f, f5, f6).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f, f2, f3).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f, f2, f6).color(color.getRGB());
      bufferBuilder.vertex(matrix4f, f, f5, f6).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f, f5, f3).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f, f5, f3).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f, f5, f6).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f5, f6).color(color2.getRGB());
      bufferBuilder.vertex(matrix4f, f4, f5, f3).color(color2.getRGB());
      handleBufferBuilder(bufferBuilder, true);
   }

   public static void handleLivingEntity(LivingEntity livingEntity, MatrixStack matrixStack, float f, float f2, float f3, float f4) {
      if (testLivingEntity(livingEntity)) {
         f2 += (float)(Math.cos(livingEntity.age * 3.25) * Math.PI * 0.4F);
      }

      if (!livingEntity.isInPose(EntityPose.SLEEPING)) {
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f2));
      }

      if (livingEntity.deathTime > 0 && !AnimationsModule.check5()) {
         float f5 = (livingEntity.deathTime + f3 - 1.0F) / 20.0F * 1.6F;
         f5 = MathHelper.sqrt(f5);
         if (f5 > 1.0F) {
            f5 = 1.0F;
         }

         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f5 * 90.0F));
      } else if (livingEntity.isUsingRiptide()) {
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F - livingEntity.getPitch()));
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((livingEntity.age + f3) * -75.0F));
      } else if (livingEntity.isInPose(EntityPose.SLEEPING)) {
         Direction direction = livingEntity.getSleepingDirection();
         float f6 = direction != null ? computeDirection(direction) : f2;
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f6));
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
         matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0F));
      } else if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
         matrixStack.translate(0.0F, (livingEntity.getHeight() + 0.1F) / f4, 0.0F);
         matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
      }
   }

   public static void handleQueue(Queue<Render3d.TransformedLine> queue) {
      if (!queue.isEmpty()) {
         handleFlag2(true);
         BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);
         RenderSystem.lineWidth(ChamsModule.value());

         for (Render3d.TransformedLine transformedLine : queue) {
            Color color = transformedLine.color;
            MatrixStack matrixStack = resolveMatrixStack(transformedLine.doubleValue3, transformedLine.doubleValue, transformedLine.doubleValue5);
            handleBufferBuilder9(
               bufferBuilder,
               matrixStack,
               0.0F,
               0.0F,
               0.0F,
               (float)(transformedLine.doubleValue6 - transformedLine.doubleValue3),
               (float)(transformedLine.doubleValue2 - transformedLine.doubleValue),
               (float)(transformedLine.doubleValue4 - transformedLine.doubleValue5),
               color
            );
         }

         RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
         handleBufferBuilder(bufferBuilder, true);
         queue.clear();
      }
   }

   public static float computeEntity(Entity entity) {
      if (AnimationsModule.check()) {
         return 0.43F - MathHelper.lerp(((ILivingEntity)(Object)entity).getGlobalAnimation().getFloatValue2(), 0.0F, 0.3F);
      } else {
         return entity.isSneaking() ? 0.4F : 0.43F;
      }
   }

   public static MatrixStack resolveMatrixStack(double d, double d2, double d3) {
      MatrixStack matrixStack = new MatrixStack();
      Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      matrixStack.translate(d, d2, d3);
      return matrixStack;
   }

   public static void handlePlayerEntity4(PlayerEntity playerEntity, MatrixStack matrixStack, float f, float f2, float f3, float f4) {
      float f9 = playerEntity.getLeaningPitch(f3);
      float f10 = playerEntity.getPitch(f3);
      if (EntityUtils.testLivingEntity3(playerEntity)) {
         handleLivingEntity(playerEntity, matrixStack, f, f2, f3, f4);
         float f5 = playerEntity.getFallFlyingTicks() + f3;
         float f6 = MathHelper.clamp(f5 * f5 / 100.0F, 0.0F, 1.0F);
         if (!playerEntity.isUsingRiptide()) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f6 * (-90.0F - f10)));
         }

         Vec3d vec3d = playerEntity.getRotationVec(f3);
         Vec3d vec3d2 = playerEntity.getVelocity();
         double d3 = vec3d2.horizontalLengthSquared();
         double d4 = vec3d.horizontalLengthSquared();
         if (d3 > 0.0 && d4 > 0.0) {
            double d = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d3 * d4);
            double d2 = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(d2) * Math.acos(d))));
         }
      } else if (f9 > 0.0F) {
         handleLivingEntity(playerEntity, matrixStack, f, f2, f3, f4);
         float f7 = playerEntity.isTouchingWater() ? -90.0F - f10 : -90.0F;
         float f8 = MathHelper.lerp(f9, 0.0F, f7);
         matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f8));
         if (playerEntity.isInSwimmingPose()) {
            matrixStack.translate(0.0F, -1.0F, 0.3F);
         }
      } else {
         handleLivingEntity(playerEntity, matrixStack, f, f2, f3, f4);
      }
   }

   public static float computeDouble(double d) {
      float f = 0.0018F + 0.003F * (float)d;
      if (d <= 8.0) {
         f = 0.0245F;
      }

      return f;
   }

   public static void handleDouble(double d, double d2, double d3, double d4, double d5, double d6, Color color, float f, Runnable runnable) {
      BufferBuilder bufferBuilder = resolveBufferBuilder(f, true);
      runnable.run();
      RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      double d7 = d4 - d;
      double d8 = d5 - d2;
      double d9 = d6 - d3;
      double d10 = MathHelper.sqrt((float)(d7 * d7 + d8 * d8 + d9 * d9));
      float f2 = (float)(d7 / d10);
      float f3 = (float)(d8 / d10);
      float f4 = (float)(d9 / d10);
      bufferBuilder.vertex(matrix4f2, (float)d, (float)d2, (float)d3)
         .normal(f2, f3, f4)
         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      bufferBuilder.vertex(matrix4f2, (float)d4, (float)d5, (float)d6)
         .normal(f2, f3, f4)
         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      handleBufferBuilder(bufferBuilder, true);
   }

   public static void handleDouble2(double d, double d2, double d3, double d4, double d5, double d6, Color color) {
      handleTransformedLine(new Render3d.TransformedLine(d, d2, d3, d4, d5, d6, color));
   }

   public static void handleColor4(Color color, net.minecraft.client.model.ModelPart.Cuboid cuboid, MatrixStack matrixStack, float f, Runnable runnable) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      Vector4f vector4f = new Vector4f();
      Vector4f vector4f2 = new Vector4f();
      Vector4f vector4f3 = new Vector4f();
      Vector4f vector4f4 = new Vector4f();

      for (Quad quad : cuboid.sides) {
         vector4f.set(quad.vertices[0].pos.x / 16.0F, quad.vertices[0].pos.y / 16.0F, quad.vertices[0].pos.z / 16.0F, 1.0F);
         vector4f.mul(matrix4f);
         vector4f2.set(quad.vertices[1].pos.x / 16.0F, quad.vertices[1].pos.y / 16.0F, quad.vertices[1].pos.z / 16.0F, 1.0F);
         vector4f2.mul(matrix4f);
         vector4f3.set(quad.vertices[2].pos.x / 16.0F, quad.vertices[2].pos.y / 16.0F, quad.vertices[2].pos.z / 16.0F, 1.0F);
         vector4f3.mul(matrix4f);
         vector4f4.set(quad.vertices[3].pos.x / 16.0F, quad.vertices[3].pos.y / 16.0F, quad.vertices[3].pos.z / 16.0F, 1.0F);
         vector4f4.mul(matrix4f);
         handleDouble(vector4f.x, vector4f.y, vector4f.z, vector4f2.x, vector4f2.y, vector4f2.z, color, f, runnable);
         handleDouble(vector4f2.x, vector4f2.y, vector4f2.z, vector4f3.x, vector4f3.y, vector4f3.z, color, f, runnable);
         handleDouble(vector4f3.x, vector4f3.y, vector4f3.z, vector4f4.x, vector4f4.y, vector4f4.z, color, f, runnable);
         handleDouble(vector4f4.x, vector4f4.y, vector4f4.z, vector4f.x, vector4f.y, vector4f.z, color, f, runnable);
      }
   }

   public static void handleBufferBuilder9(
      BufferBuilder bufferBuilder, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5, float f6, Color color
   ) {
      Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
      Entry entry = matrixStack.peek();
      Vector3f vector3f = createVector3f(f, f2, f3, f4, f5, f6);
      bufferBuilder.vertex(matrix4f, f, f2, f3)
         .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
         .normal(entry, vector3f.x(), vector3f.y(), vector3f.z());
      bufferBuilder.vertex(matrix4f, f4, f5, f6)
         .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
         .normal(entry, vector3f.x(), vector3f.y(), vector3f.z());
   }

   public static void handleColor5(Color color, ModelPart modelPart, MatrixStack matrixStack, float f, Runnable runnable) {
      if (modelPart.visible && (!modelPart.cuboids.isEmpty() || !modelPart.children.isEmpty())) {
         matrixStack.push();
         modelPart.rotate(matrixStack);

         for (net.minecraft.client.model.ModelPart.Cuboid cuboid : modelPart.cuboids) {
            handleColor4(color, cuboid, matrixStack, f, runnable);
         }

         for (ModelPart modelPart2 : modelPart.children.values()) {
            handleColor5(color, modelPart2, matrixStack, f, runnable);
         }

         matrixStack.pop();
      }
   }

   public static void setFlag(boolean flag) {
      Render3d.flag = flag;
   }

   public Render3d() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float computeDirection(Direction direction) {
      return switch (direction) {
         case SOUTH -> 90.0F;
         case NORTH -> 270.0F;
         case EAST -> 180.0F;
         default -> 0.0F;
      };
   }

   public static void run() {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void handleText2(
      String text, ClientPlayerEntity clientPlayerEntity, Entity entity, float f, float f2, Color color, boolean flag, boolean flag2
   ) {
      if (mc.getCameraEntity() != null) {
         Vec3d vec3d = FreecamModule.getFreecamModule().check7()
            ? FreecamModule.getFreecamModule().createVec3d()
            : EntityInterpolation.createVec3d5(mc.getCameraEntity(), f);
         double d = clientPlayerEntity.getX();
         double d2 = clientPlayerEntity.getY();
         double d3 = clientPlayerEntity.getZ();
         Camera camera = mc.gameRenderer.getCamera();
         Vec3d vec3d2 = EntityInterpolation.createVec3d5(entity, f);
         Vec3d vec3d3 = EntityInterpolation.createVec3d(entity, f);
         double d4 = entity.getX() - vec3d2.getX();
         double d5 = entity.getY() + entity.getHeight() + computeEntity(entity) - vec3d2.getY();
         double d6 = entity.getZ() - vec3d2.getZ();
         int i = flag ? (int)ClientHolder.getPhobosClient().getRenderManager().computeText3(text) : mc.textRenderer.getWidth(text);
         float f3 = i / 2.0F;
         double d7 = d - vec3d.getX() - d4;
         double d8 = d2 - vec3d.getY() - d5;
         double d9 = d3 - vec3d.getZ() - d6;
         double d10 = FreecamModule.getFreecamModule().check7()
            ? vec3d.distanceTo(vec3d3.add(0.0, entity.getHeight(), 0.0))
            : Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
         if (!(d10 > 4096.0)) {
            float f4 = 0.0018F + 0.003F * (float)d10;
            if (d10 <= 8.0) {
               f4 = 0.0245F;
            }

            handleText(text, f3, d4, d5, d6, camera, color, f4 * f2, flag, flag2);
         }
      }
   }

   public static void handleFloat2(float[] floats) {
      RenderSystem.setShaderColor(floats[0], floats[1], floats[2], floats[3]);
   }

   public static void handleColor6(Color color, ModelPart modelPart, MatrixStack matrixStack) {
      if (modelPart.visible && (!modelPart.cuboids.isEmpty() || !modelPart.children.isEmpty())) {
         matrixStack.push();
         modelPart.rotate(matrixStack);

         for (net.minecraft.client.model.ModelPart.Cuboid cuboid : modelPart.cuboids) {
            handleColor2(color, cuboid, matrixStack);
         }

         for (ModelPart modelPart2 : modelPart.children.values()) {
            handleColor6(color, modelPart2, matrixStack);
         }

         matrixStack.pop();
      }
   }

   public static void handleMatrixStack5(MatrixStack matrixStack, String text, float f, Color color, boolean flag, boolean flag2) {
      RenderManager renderManager = ClientHolder.getPhobosClient().getRenderManager();
      if (flag) {
         renderManager.handleMatrixStack2(
            matrixStack, text, f, 0.0F, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F, flag2
         );
      } else {
         renderManager.getText3dRenderer().handleMatrixStack3(matrixStack, text, f, 0.0F, color.hashCode(), flag2);
      }
   }

   public static Queue<Consumer<Matrix4f>> getConsumerQueue() {
      return consumerQueue;
   }

   public static boolean isFlag() {
      return flag;
   }

   public static Queue<Render3d.Cuboid> getCuboidQueue() {
      return cuboidQueue;
   }

   public static Queue<Render3d.TransformedLine> getTransformedLineQueue() {
      return transformedLineQueue;
   }

   public static Queue<Render3d.Model> getModelQueue() {
      return modelQueue;
   }

   public static Queue<Render3d.Lines> getLinesQueue() {
      return linesQueue;
   }

   public static Queue<Render3d.CameraLine> getCameraLineQueue() {
      return cameraLineQueue;
   }

   public static Queue<Render3d.Fade> getFadeQueue() {
      return fadeQueue;
   }

   public static final class CameraLine {
      public List<Color> colorList;
      public List<Optional<Vec3d>> optionalList = new ArrayList<>();
      public List<Vec3d> vec3dList = new ArrayList<>();
      public float floatValue;

      public static Render3d.CameraLine.Builder createBuilder() {
         return new Render3d.CameraLine.Builder();
      }

      public void setOptionalList(List<Optional<Vec3d>> list) {
         this.optionalList = list;
      }

      public List<Color> getColorList() {
         return this.colorList;
      }

      public CameraLine() {
         this.colorList = new ArrayList<>();
      }

      public List<Vec3d> getVec3dList() {
         return this.vec3dList;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void setColorList(List<Color> list) {
         this.colorList = list;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public List<Optional<Vec3d>> getOptionalList() {
         return this.optionalList;
      }

      public void setVec3dList(List<Vec3d> list) {
         this.vec3dList = list;
      }

      public static final class Builder {
         public final Render3d.CameraLine cameraLine = new Render3d.CameraLine();

         public Render3d.CameraLine.Builder resolveBuilder(Optional<Vec3d> optional, Vec3d vec3d, Color color) {
            this.cameraLine.getOptionalList().add(optional);
            this.cameraLine.getVec3dList().add(vec3d);
            this.cameraLine.getColorList().add(color);
            return this;
         }

         public Render3d.CameraLine.Builder resolveBuilder2(float f) {
            this.cameraLine.setFloatValue(f);
            return this;
         }

         public Render3d.CameraLine getCameraLine() {
            return this.cameraLine;
         }
      }
   }

   public static class Cuboid {
      public Consumer<Render3d.Cuboid> cuboidConsumer;
      public float floatValue;
      public Render3d.Cuboid.CompletableSupplier<BlockPos> blockPosCompletableSupplier;
      public Render3d.Cuboid.CompletableSupplier<Vec3d> vec3dCompletableSupplier = new Render3d.Cuboid.CompletableSupplier<>();
      public Consumer<Render3d.Cuboid> cuboidConsumer2;
      public boolean flag;
      public float floatValue2;
      public float floatValue3;
      public float floatValue4;
      public Box box;
      public float floatValue5;
      public float floatValue6;
      public long longValue;
      public long longValue2;
      public float floatValue7;
      public Object object;
      public float floatValue8;
      public float floatValue9;
      public int intValue;
      public Color color;
      public Color color2;

      public float getFloatValue9() {
         return this.floatValue9;
      }

      public Render3d.Cuboid.CompletableSupplier<Vec3d> getVec3dCompletableSupplier() {
         return this.vec3dCompletableSupplier;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public Consumer<Render3d.Cuboid> getCuboidConsumer() {
         return this.cuboidConsumer;
      }

      public static Render3d.Cuboid.Builder createBuilder() {
         return new Render3d.Cuboid.Builder();
      }

      public float getFloatValue8() {
         return this.floatValue8;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public Cuboid() {
         this.blockPosCompletableSupplier = new Render3d.Cuboid.CompletableSupplier<>();
         this.floatValue9 = 1.0F;
         this.longValue2 = -1L;
         this.flag = false;
         this.cuboidConsumer2 = RenderModule::handleCuboid;
         this.cuboidConsumer = RenderModule::handleCuboid4;
         this.intValue = 8;
         this.object = null;
      }

      public Consumer<Render3d.Cuboid> getCuboidConsumer2() {
         return this.cuboidConsumer2;
      }

      public float getFloatValue3() {
         return this.floatValue3;
      }

      public float getFloatValue6() {
         return this.floatValue6;
      }

      public Color getColor() {
         return this.color;
      }

      public Color getColor2() {
         return this.color2;
      }

      public Box getBox() {
         return this.box;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public float getFloatValue7() {
         return this.floatValue7;
      }

      public Render3d.Cuboid.CompletableSupplier<BlockPos> getBlockPosCompletableSupplier() {
         return this.blockPosCompletableSupplier;
      }

      public float getFloatValue4() {
         return this.floatValue4;
      }

      public boolean testObject(Object object) {
         return Objects.equals(this.object, object);
      }

      public float getFloatValue5() {
         return this.floatValue5;
      }

      public long getLongValue2() {
         return this.longValue2;
      }

      public void setFloatValue9(float f) {
         this.floatValue9 = f;
      }

      public void setBlockPosCompletableSupplier(Render3d.Cuboid.CompletableSupplier<BlockPos> completableSupplier) {
         this.blockPosCompletableSupplier = completableSupplier;
      }

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public void setFloatValue4(float f) {
         this.floatValue4 = f;
      }

      public void setFloatValue6(float f) {
         this.floatValue6 = f;
      }

      public Object getObject() {
         return this.object;
      }

      public void setColor2(Color color) {
         this.color2 = color;
      }

      public void setFloatValue5(float f) {
         this.floatValue5 = f;
      }

      public void setLongValue2(long l) {
         this.longValue2 = l;
      }

      public void setBox(Box box) {
         this.box = box;
      }

      public void setLongValue(long l) {
         this.longValue = l;
      }

      public void setFloatValue3(float f) {
         this.floatValue3 = f;
      }

      public void setCuboidConsumer2(Consumer<Render3d.Cuboid> consumer) {
         this.cuboidConsumer2 = consumer;
      }

      public void setColor(Color color) {
         this.color = color;
      }

      public void setObject(Object object) {
         this.object = object;
      }

      public void setCuboidConsumer(Consumer<Render3d.Cuboid> consumer) {
         this.cuboidConsumer = consumer;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void setFloatValue8(float f) {
         this.floatValue8 = f;
      }

      public void setVec3dCompletableSupplier(Render3d.Cuboid.CompletableSupplier<Vec3d> completableSupplier) {
         this.vec3dCompletableSupplier = completableSupplier;
      }

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public void setFloatValue7(float f) {
         this.floatValue7 = f;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public static class Builder implements CuboidProvider<Render3d.Cuboid> {
         public final Render3d.Cuboid cuboid = new Render3d.Cuboid();

         public Render3d.Cuboid.Builder resolveBuilder(Color color) {
            this.cuboid.setFloatValue5(color.getRed() / 255.0F);
            this.cuboid.setFloatValue8(color.getGreen() / 255.0F);
            this.cuboid.setFloatValue3(color.getBlue() / 255.0F);
            this.cuboid.setFloatValue(color.getRed() / 255.0F);
            this.cuboid.setFloatValue7(color.getGreen() / 255.0F);
            this.cuboid.setFloatValue2(color.getBlue() / 255.0F);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder2(float f, float f2) {
            this.cuboid.setFloatValue4(f);
            this.cuboid.setFloatValue6(f2);
            return this;
         }

         @Contract("null -> fail; !null -> this")
         public Render3d.Cuboid.Builder resolveBuilder3(BlockPos blockPos) {
            this.cuboid.setBox(new Box(blockPos));
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder4(boolean flag) {
            this.cuboid.setFlag(flag);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder5(long l) {
            this.cuboid.setLongValue(System.currentTimeMillis());
            this.cuboid.setLongValue2(l);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder6(Setting<Boolean> setting, Setting<Long> setting2) {
            this.cuboid.setLongValue(System.currentTimeMillis());
            this.cuboid.setLongValue2(setting.getValue() ? setting2.getValue() : -1L);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder7(Consumer<Render3d.Cuboid> consumer) {
            this.cuboid.setCuboidConsumer2(consumer);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder8(float f) {
            this.cuboid.setFloatValue6(f);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder9(Consumer<Render3d.Cuboid> consumer) {
            this.cuboid.setCuboidConsumer(consumer);
            return this;
         }

         @Contract("null -> fail")
         public Render3d.Cuboid.Builder resolveBuilder10(MutableBox mutableBox) {
            this.cuboid.setBox(mutableBox.createBox());
            return this;
         }

         @Contract("null -> fail; !null -> this")
         public Render3d.Cuboid.Builder resolveBuilder11(Box box) {
            this.cuboid.setBox(box);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder12(float f) {
            this.cuboid.setFloatValue9(f);
            return this;
         }

         @Contract("null -> this; !null -> this")
         public Render3d.Cuboid.Builder resolveBuilder13(Vec3d vec3d) {
            this.cuboid.setVec3dCompletableSupplier(new Render3d.Cuboid.CompletableSupplier<>(vec3d));
            return this;
         }

         public Render3d.Cuboid getCuboid() {
            return this.cuboid;
         }

         public Render3d.Cuboid.Builder resolveBuilder14(Object object) {
            this.cuboid.setObject(object);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder15(int i) {
            this.cuboid.setIntValue(i);
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder16(float f) {
            this.cuboid.setFloatValue4(f);
            return this;
         }

         @Contract("null -> this; !null -> this")
         public Render3d.Cuboid.Builder resolveBuilder17(BlockPos blockPos) {
            this.cuboid.setBlockPosCompletableSupplier(new Render3d.Cuboid.CompletableSupplier<>(blockPos));
            return this;
         }

         public Render3d.Cuboid.Builder resolveBuilder18(Color color, Color color2) {
            this.cuboid.setFloatValue5(color.getRed() / 255.0F);
            this.cuboid.setFloatValue8(color.getGreen() / 255.0F);
            this.cuboid.setFloatValue3(color.getBlue() / 255.0F);
            this.cuboid.setFloatValue4(color.getAlpha() / 255.0F);
            this.cuboid.setFloatValue(color2.getRed() / 255.0F);
            this.cuboid.setFloatValue7(color2.getGreen() / 255.0F);
            this.cuboid.setFloatValue2(color2.getBlue() / 255.0F);
            this.cuboid.setFloatValue6(color2.getAlpha() / 255.0F);
            return this;
         }
      }

      public static class CompletableSupplier<T> {
         public T object = (T)null;

         public void handleConsumer(Consumer<T> consumer) {
            if (this.object != null) {
               consumer.accept(this.object);
            }
         }

         public CompletableSupplier(T object) {
            this.object = object;
         }

         public boolean testBlockPos(BlockPos blockPos) {
            return this.object != null && this.object.equals(blockPos);
         }

         public T get() {
            return this.object;
         }

         public CompletableSupplier() {
         }
      }
   }

   public static final class Fade {
      public Color color;
      public Color color2;
      public Box box;

      public void setColor2(Color color) {
         this.color2 = color;
      }

      public void setBox(Box box) {
         this.box = box;
      }

      public Color getColor() {
         return this.color;
      }

      public static Render3d.Fade.Builder createBuilder() {
         return new Render3d.Fade.Builder();
      }

      public Box getBox() {
         return this.box;
      }

      public Color getColor2() {
         return this.color2;
      }

      public void setColor(Color color) {
         this.color = color;
      }

      public static final class Builder {
         public final Render3d.Fade fade = new Render3d.Fade();

         public Render3d.Fade getFade() {
            return this.fade;
         }

         public Render3d.Fade.Builder resolveBuilder(Color color, Color color2) {
            this.fade.setColor2(color);
            this.fade.setColor(color2);
            return this;
         }

         @Contract("null -> fail; !null -> this")
         public Render3d.Fade.Builder resolveBuilder2(Box box) {
            this.fade.setBox(box);
            return this;
         }
      }
   }

   public static class Line {
      public Vec3d vec3d;
      public Color color;
      public Vec3d vec3d2;

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public Vec3d getVec3d2() {
         return this.vec3d2;
      }

      public void setVec3d(Vec3d vec3d) {
         this.vec3d = vec3d;
      }

      public void setVec3d2(Vec3d vec3d) {
         this.vec3d2 = vec3d;
      }

      public Line(Vec3d vec3d, Vec3d vec3d2, Color color) {
         this.vec3d2 = vec3d;
         this.vec3d = vec3d2;
         this.color = color;
      }

      public Color getColor() {
         return this.color;
      }

      public void setColor(Color color) {
         this.color = color;
      }
   }

   public static final class Lines {
      public float floatValue;
      public long longValue = 0L;
      public List<Render3d.Line> lineList;
      public long longValue2;

      public void setLineList(List<Render3d.Line> list) {
         this.lineList = list;
      }

      public void setLongValue(long l) {
         this.longValue = l;
      }

      public void setLongValue2(long l) {
         this.longValue2 = l;
      }

      public long getLongValue2() {
         return this.longValue2;
      }

      public static Render3d.Lines.Builder createBuilder() {
         return new Render3d.Lines.Builder();
      }

      public long getLongValue() {
         return this.longValue;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public List<Render3d.Line> getLineList() {
         return this.lineList;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public static final class Builder {
         public final Render3d.Lines lines = new Render3d.Lines();

         public Render3d.Lines.Builder resolveBuilder(Vec3d vec3d, Vec3d vec3d2, Color color) {
            if (this.lines.getLineList() == null) {
               this.lines.setLineList(new ArrayList<>(1));
            }

            this.lines.getLineList().add(new Render3d.Line(vec3d, vec3d2, color));
            return this;
         }

         public Render3d.Lines getLines() {
            return this.lines;
         }

         public Render3d.Lines.Builder resolveBuilder2(float f) {
            this.lines.setFloatValue(f);
            return this;
         }

         public Render3d.Lines.Builder resolveBuilder3(long l) {
            this.lines.setLongValue2(System.currentTimeMillis());
            this.lines.setLongValue(l);
            return this;
         }

         @Contract("null -> fail; !null -> this")
         public Render3d.Lines.Builder resolveBuilder4(List<Render3d.Line> list) {
            this.lines.setLineList(list);
            return this;
         }
      }
   }

   public static final class Model {
      public EntityModel<PlayerEntity> playerEntityEntityModel;
      public Color color;
      public PlayerEntity playerEntity;
      public float floatValue;
      public Runnable runnable;
      public Runnable runnable2;
      public float floatValue2;
      public Color color2;
      public boolean flag;
      public final Map<Integer, Object> integerMap = new HashMap<>();

      public void setFloatValue2(float f) {
         this.floatValue2 = f;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      public Model() {
         this.runnable = () -> RenderSystem.setShader(GameRenderer::getPositionProgram);
         this.runnable2 = () -> RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
      }

      public void setRunnable(Runnable runnable) {
         this.runnable = runnable;
      }

      public Runnable getRunnable() {
         return this.runnable;
      }

      public PlayerEntity getPlayerEntity() {
         return this.playerEntity;
      }

      public boolean isFlag() {
         return this.flag;
      }

      public Color getColor() {
         return this.color;
      }

      public EntityModel<PlayerEntity> getPlayerEntityEntityModel() {
         return this.playerEntityEntityModel;
      }

      public Runnable getRunnable2() {
         return this.runnable2;
      }

      public void setColor2(Color color) {
         this.color2 = color;
      }

      public void setPlayerEntityEntityModel(EntityModel<PlayerEntity> entityModel) {
         this.playerEntityEntityModel = entityModel;
      }

      public void setRunnable2(Runnable runnable) {
         this.runnable2 = runnable;
      }

      public static Render3d.Model.Builder createBuilder() {
         return new Render3d.Model.Builder();
      }

      public void setColor(Color color) {
         this.color = color;
      }

      public float getFloatValue2() {
         return this.floatValue2;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public void setPlayerEntity(PlayerEntity playerEntity) {
         this.playerEntity = playerEntity;
      }

      public Color getColor2() {
         return this.color2;
      }

      public Map<Integer, Object> getIntegerMap() {
         return this.integerMap;
      }

      public static final class Builder {
         public final Render3d.Model model = new Render3d.Model();

         public Render3d.Model getModel() {
            return this.model;
         }

         @Contract("null -> fail; !null -> this")
         public Render3d.Model.Builder resolveBuilder(EntityModel<PlayerEntity> entityModel) {
            this.model.setPlayerEntityEntityModel(entityModel);
            return this;
         }

         public Render3d.Model.Builder resolveBuilder2(Color color, Color color2) {
            this.model.setColor(color);
            this.model.setColor2(color2);
            return this;
         }

         @Contract("null -> fail; !null -> this")
         public Render3d.Model.Builder resolveBuilder3(PlayerEntity playerEntity) {
            this.model.setPlayerEntity(playerEntity);
            return this;
         }

         public Render3d.Model.Builder resolveBuilder4(float f) {
            this.model.setFloatValue(f);
            return this;
         }

         public Render3d.Model.Builder resolveBuilder5(float f) {
            this.model.setFlag(true);
            this.model.setFloatValue2(f);
            return this;
         }
      }
   }

   public static class ModelLine {
      public double doubleValue;
      public double doubleValue2;
      public double doubleValue3;
      public double doubleValue4;
      public double doubleValue5;
      public double doubleValue6;

      public void setDoubleValue4(double d) {
         this.doubleValue4 = d;
      }

      public double getDoubleValue3() {
         return this.doubleValue3;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public double getDoubleValue5() {
         return this.doubleValue5;
      }

      public void setDoubleValue3(double d) {
         this.doubleValue3 = d;
      }

      public double getDoubleValue4() {
         return this.doubleValue4;
      }

      public void setDoubleValue5(double d) {
         this.doubleValue5 = d;
      }

      public void setDoubleValue6(double d) {
         this.doubleValue6 = d;
      }

      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public double getDoubleValue6() {
         return this.doubleValue6;
      }

      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }
   }

   public static class TransformedLine {
      public double doubleValue;
      public double doubleValue2;
      public double doubleValue3;
      public Color color;
      public double doubleValue4;
      public double doubleValue5;
      public double doubleValue6;

      public void setDoubleValue5(double d) {
         this.doubleValue5 = d;
      }

      public void setDoubleValue6(double d) {
         this.doubleValue6 = d;
      }

      public void setDoubleValue3(double d) {
         this.doubleValue3 = d;
      }

      public void setColor(Color color) {
         this.color = color;
      }

      public double getDoubleValue6() {
         return this.doubleValue6;
      }

      public TransformedLine(double d, double d2, double d3, double d4, double d5, double d6, Color color) {
         this.doubleValue3 = d;
         this.doubleValue = d2;
         this.doubleValue5 = d3;
         this.doubleValue6 = d4;
         this.doubleValue2 = d5;
         this.doubleValue4 = d6;
         this.color = color;
      }

      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public Color getColor() {
         return this.color;
      }

      public double getDoubleValue3() {
         return this.doubleValue3;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void setDoubleValue4(double d) {
         this.doubleValue4 = d;
      }

      public double getDoubleValue5() {
         return this.doubleValue5;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public double getDoubleValue4() {
         return this.doubleValue4;
      }
   }
}
