package org.phobos.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.item.WindChargeItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Matrix4f;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.animation.AnimatedColor;
import org.phobos.core.animation.AnimatedVec3d;
import org.phobos.core.animation.AnimatedBox;
import org.phobos.core.render.RenderContext;
import org.phobos.core.rotation.RotationSmoother;
import org.phobos.core.render.CameraState;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.PhobosClient;
import org.phobos.core.util.BufferUtils;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.combat.TrajectoryUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.rotation.RotationManager.Rotation;
import org.phobos.core.combat.TrajectoryUtils.TrajectoryData;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.core.render.Render3d.Line;
import org.phobos.core.render.Render3d.Lines;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.entity.IPersistentProjectileEntity;
import org.phobos.module.Module;
import org.phobos.module.client.RotationsModule;

public class TrajectoriesModule extends Module {
   public final Setting<Boolean> pearls;
   public final Setting<Boolean> lines;
   public final Setting<Color> background;
   public final Setting<Color> handPosLine;
   public final Setting<Boolean> damage;
   public final Setting<Boolean> arrows;
   public final Setting<Color> rim;
   public final Setting<Float> handPosWidth;
   public final Setting<Color> linesColor;
   public final Setting<Double> handPosSize;
   public final Setting<Float> scale;
   public final Setting<Double> range;
   public final Setting<Color> damageNegative;
   public final Setting<Boolean> charges;
   public final Setting<Boolean> indicator;
   public final Setting<Boolean> tridents;
   public final Setting<Color> color;
   public final Setting<Boolean> handPos;
   public final Setting<Float> damageFix;
   public final Setting<Boolean> fireballs;
   public final Setting<Boolean> hand;
   public final Setting<Color> handPosFill;
   public final Setting<Boolean> predict = this.resolveSetting2(new BooleanSetting("Predict").resolveSetting7("Prediction"));
   public final Setting<Color> damagePositive;
   public static final Identifier identifier = Identifier.of("phoboslite", "textures/user.png");
   public final Map<Entity, TrajectoriesModule.Indicator> entityMap;
   public final AnimatedVec3d animatedVec3d;
   public boolean flag;
   public static final Identifier identifier2 = Identifier.of("phoboslite", "textures/vanilla/ender_pearl.png");
   public static final Identifier identifier3 = Identifier.of("phoboslite", "textures/vanilla/spectral_arrow.png");
   public static final Identifier identifier4 = Identifier.of("phoboslite", "textures/vanilla/lingering_potion.png");
   public static final Identifier identifier5 = Identifier.of("phoboslite", "textures/vanilla/arrow.png");
   public final Setting<Float> width;
   public static final Identifier identifier6 = Identifier.of("phoboslite", "textures/vanilla/snowball.png");
   public static final Identifier identifier7 = Identifier.of("phoboslite", "textures/vanilla/experience_bottle.png");
   public final AnimatedVec3d animatedVec3d2;
   public final Setting<Boolean> potions;
   public static final Identifier identifier8 = Identifier.of("phoboslite", "textures/vanilla/wind_charge.png");
   public final Setting<Boolean> xP;
   public boolean flag2;
   public final AnimatedBox animatedBox;
   public final RotationSmoother rotationSmoother;
   public final Setting<Boolean> snowballs;
   public static final Identifier identifier9 = Identifier.of("phoboslite", "textures/vanilla/splash_potion.png");
   public static final Identifier identifier10 = Identifier.of("phoboslite", "textures/vanilla/egg.png");
   public static final Identifier identifier11 = Identifier.of("phoboslite", "textures/vanilla/fire_charge.png");
   public static final Identifier identifier12 = Identifier.of("phoboslite", "textures/vanilla/trident.png");
   public final Setting<Boolean> eggs;

   public boolean testEntity(Entity entity) {
      return entity instanceof ArrowEntity || entity instanceof SpectralArrowEntity;
   }

   public TrajectoryUtils.EntityGravityType resolveEntityGravityType(Entity entity) {
      if (entity instanceof ArrowEntity || entity instanceof PotionEntity) {
         return TrajectoryUtils.EntityGravityType.High;
      } else {
         return !(entity instanceof WindChargeEntity) && !(entity instanceof FireballEntity)
            ? TrajectoryUtils.EntityGravityType.Normal
            : TrajectoryUtils.EntityGravityType.None;
      }
   }

   public TrajectoriesModule(PhobosClient phobosClient) {
      super(phobosClient, "Trajectories", Categories.render, "Predicts projectiles");
      this.indicator = this.resolveSetting2(new BooleanSetting("Indicator"));
      this.scale = this.resolveSetting2(new NumberSetting<>("Scale", 5.0F, 1.0F, 3.0F).resolveSetting7("Indicator scale"));
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 8.0, 0.0, 50.0).resolveSetting7("Indicator range"));
      this.background = this.resolveSetting2(new ColorSetting("Background", new Color(0, 0, 0, 70)).resolveSetting7("Indicator background"));
      this.rim = this.resolveSetting2(new ColorSetting("Rim", new Color(255, 255, 255, 255)).getColorSetting().resolveSetting7("Indicator rim"));
      this.damage = this.resolveSetting2(new BooleanSetting("Damage").resolveSetting7("Shows damage from potions"));
      this.damagePositive = this.resolveSetting2(new ColorSetting("DamagePositive", Color.GREEN).resolveSetting7("Positive potion color"));
      this.damageNegative = this.resolveSetting2(new ColorSetting("DamageNegative", Color.RED).resolveSetting7("Negative potion color"));
      this.damageFix = this.resolveSetting2(new NumberSetting<>("DamageFix", 0.5F, 0.0F, 1.0F));
      this.lines = this.resolveSetting2(new BooleanSetting("Lines"));
      this.linesColor = this.resolveSetting2(new ColorSetting("LinesColor", new Color(255, 255, 255, 255)).getColorSetting());
      this.hand = this.resolveSetting2(new BooleanSetting("Hand").resolveSetting7("Predicts from hand"));
      this.color = this.resolveSetting2(new ColorSetting("Color", new Color(255, 255, 255)).getColorSetting());
      this.handPos = this.resolveSetting2(new BooleanSetting("HandPos", true));
      this.handPosFill = this.resolveSetting2(new ColorSetting("HandPosFill", new Color(255, 255, 255, 30)).getColorSetting());
      this.handPosLine = this.resolveSetting2(new ColorSetting("HandPosLine", Color.WHITE).getColorSetting());
      this.handPosWidth = this.resolveSetting2(new NumberSetting<>("HandPosWidth", 1.0F, 0.1F, 3.0F));
      this.handPosSize = this.resolveSetting2(new NumberSetting<>("HandPosSize", 0.5, 0.05, 1.0));
      this.arrows = this.resolveSetting2(new BooleanSetting("Arrows", true));
      this.pearls = this.resolveSetting2(new BooleanSetting("Pearls", true));
      this.tridents = this.resolveSetting2(new BooleanSetting("Tridents", true));
      this.charges = this.resolveSetting2(new BooleanSetting("Charges", true));
      this.fireballs = this.resolveSetting2(new BooleanSetting("Fireballs", true));
      this.potions = this.resolveSetting2(new BooleanSetting("Potions"));
      this.xP = this.resolveSetting2(new BooleanSetting("XP"));
      this.snowballs = this.resolveSetting2(new BooleanSetting("Snowballs"));
      this.eggs = this.resolveSetting2(new BooleanSetting("Eggs"));
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 3.0F));
      this.entityMap = new ConcurrentHashMap<>();
      this.animatedVec3d = new AnimatedVec3d();
      this.animatedVec3d2 = new AnimatedVec3d();
      this.animatedBox = new AnimatedBox();
      this.rotationSmoother = new RotationSmoother();
      this.flag = true;
      this.flag2 = true;
      this.createBuilder2()
         .resolveBuilder6(this.predict)
         .resolveBuilder4(
            this.createBuilder()
               .resolveBuilder6(this.indicator)
               .resolveBuilder5(this.resolveSettingGroup(this.damage, this.damagePositive, this.damageNegative, this.damageFix))
               .resolveBuilder(this.scale, this.range, this.background, this.rim)
               .getSettingSubsection()
         )
         .resolveBuilder7(this.resolveSettingGroup(this.lines, this.linesColor))
         .getSettingSection();
      this.createBuilder()
         .resolveBuilder6(this.hand)
         .resolveBuilder5(this.resolveSettingGroup(this.handPos, this.handPosFill, this.handPosLine, this.handPosWidth, this.handPosSize))
         .resolveBuilder(this.color)
         .getSettingSubsection();
      this.resolveSettingGroup2(
         "Selection", this.arrows, this.pearls, this.tridents, this.charges, this.fireballs, this.potions, this.xP, this.snowballs, this.eggs
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (TrajectoriesModule.this.predict.getValue() && (TrajectoriesModule.this.lines.getValue() || TrajectoriesModule.this.indicator.getValue())) {
                  for (Entity entity : clientWorld.getEntities()) {
                     if (!(clientPlayerEntity.distanceTo(entity) > TrajectoriesModule.this.range.getValue())
                        && TrajectoriesModule.this.testEntity2(entity)
                        && !TrajectoriesModule.this.entityMap.containsKey(entity)) {
                        TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
                           clientPlayerEntity,
                           clientWorld,
                           entity,
                           entity.getPos(),
                           entity.getVelocity(),
                           TrajectoriesModule.this.resolveEntityGravityType(entity),
                           TrajectoriesModule.this.resolveDrag2(entity)
                        );
                        if (trajectoryData != null && trajectoryData.getVec3dList2() != null && !trajectoryData.getVec3dList2().isEmpty()) {
                           TrajectoriesModule.Indicator indicator = new TrajectoriesModule.Indicator(
                              trajectoryData.doubleValue, TrajectoriesModule.this.resolveIdentifier(entity), entity, trajectoryData.vec3dList2.getLast()
                           );
                           TrajectoriesModule.this.entityMap.put(entity, indicator);
                        }
                     }
                  }

                  for (TrajectoriesModule.Indicator indicator2 : TrajectoriesModule.this.entityMap.values()) {
                     indicator2.handleClientPlayerEntity(
                        clientPlayerEntity,
                        clientWorld,
                        entity -> new Pair(TrajectoriesModule.this.resolveEntityGravityType(entity), TrajectoriesModule.this.resolveDrag2(entity))
                     );
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<RenderContext>() {
            public void handleObject(
               RenderContext renderContext,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               TrajectoriesModule.this.entityMap.forEach((entity, indicator) -> indicator.handleClientWorld(clientWorld));
               TrajectoriesModule.this.entityMap
                  .entrySet()
                  .removeIf(
                     entry -> !TrajectoriesModule.this.lines.getValue() && !TrajectoriesModule.this.indicator.getValue()
                        || entry.getValue().testFlag(TrajectoriesModule.this.indicator.getValue())
                  );
               List<Line> list = new ArrayList<>();
               float f = renderContext.getFloatValue();
               if (TrajectoriesModule.this.predict.getValue() && (TrajectoriesModule.this.lines.getValue() || TrajectoriesModule.this.indicator.getValue())) {
                  for (TrajectoriesModule.Indicator indicator : TrajectoriesModule.this.entityMap.values()) {
                     indicator.handleRenderContext(
                        renderContext,
                        list,
                        TrajectoriesModule.this.indicator.getValue(),
                        TrajectoriesModule.this.lines.getValue(),
                        TrajectoriesModule.this.damage.getValue(),
                        TrajectoriesModule.this.linesColor.getValue(),
                        TrajectoriesModule.this.background.getValue(),
                        TrajectoriesModule.this.rim.getValue(),
                        TrajectoriesModule.this.damagePositive.getValue(),
                        TrajectoriesModule.this.damageNegative.getValue(),
                        TrajectoriesModule.this.range.getValue(),
                        TrajectoriesModule.this.scale.getValue(),
                        TrajectoriesModule.this.damageFix.getValue()
                     );
                  }
               }

               if (TrajectoriesModule.this.hand.getValue()) {
                  ItemStack itemStack = TrajectoriesModule.this.testItem(clientPlayerEntity.getMainHandStack().getItem())
                     ? clientPlayerEntity.getMainHandStack()
                     : (TrajectoriesModule.this.testItem(clientPlayerEntity.getOffHandStack().getItem()) ? clientPlayerEntity.getOffHandStack() : null);
                  TrajectoriesModule.this.rotationSmoother
                     .handleRotation(
                        RotationsModule.check6()
                           ? Rotation.createRotation2(phobosClient.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity))
                           : Rotation.createRotation2(clientPlayerEntity)
                     );
                  TrajectoriesModule.this.rotationSmoother.handleFloat5(10.0F, 0.2F);
                  TrajectoriesModule.this.animatedVec3d2.handleVec3d(clientPlayerEntity.getVelocity());
                  TrajectoriesModule.this.animatedVec3d2.handleFloat(10.0F, 0.1F);
                  if (itemStack != null) {
                     Rotation rotation = TrajectoriesModule.this.rotationSmoother.createRotation();
                     Vec3d vec3d3 = EntityInterpolation.createVec3d(clientPlayerEntity, f).add(0.0, clientPlayerEntity.getEyeHeight(clientPlayerEntity.getPose()), 0.0);
                     Vec3d vec3d4 = TrajectoryUtils.resolveVec3d(
                        TrajectoriesModule.this.animatedVec3d2.createVec3d(),
                        clientPlayerEntity.isOnGround(),
                        rotation.getFloatValue(),
                        rotation.getFloatValue2(),
                        TrajectoriesModule.this.computeItem(itemStack.getItem()),
                        TrajectoriesModule.this.computeItemStack(itemStack, TrajectoriesModule.this.computePlayerEntity(clientPlayerEntity))
                     );
                     TrajectoriesModule.this.animatedVec3d.handleVec3d(vec3d4);
                     if (TrajectoriesModule.this.flag) {
                        TrajectoriesModule.this.animatedVec3d.handleVec3d2(vec3d4);
                        TrajectoriesModule.this.flag = false;
                     }

                     TrajectoriesModule.this.animatedVec3d.handleFloat(10.0F, 0.1F);
                     TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
                        clientPlayerEntity,
                        clientWorld,
                        null,
                        vec3d3,
                        TrajectoriesModule.this.animatedVec3d.createVec3d(),
                        TrajectoriesModule.this.resolveEntityGravityType2(itemStack.getItem()),
                        TrajectoriesModule.this.resolveDrag(itemStack.getItem())
                     );
                     if (trajectoryData != null) {
                        if (trajectoryData.getVec3dList2() != null && !trajectoryData.getVec3dList2().isEmpty()) {
                           renderContext.getMutableVec3d2().handleVec3d(trajectoryData.getVec3dList2().getFirst());

                           for (int i = 0; i < trajectoryData.vec3dList2.size(); i++) {
                              Vec3d vec3d = trajectoryData.getVec3dList2().get(i);
                              list.add(new Line(renderContext.getMutableVec3d2().createVec3d(), vec3d, TrajectoriesModule.this.color.getValue()));
                              renderContext.getMutableVec3d2().handleVec3d(vec3d);
                           }
                        }

                        if (TrajectoriesModule.this.handPos.getValue()) {
                           Direction direction = trajectoryData.getDirection();
                           if (direction != null) {
                              Vec3d vec3d2 = trajectoryData.getVec3d();
                              double d = TrajectoriesModule.this.handPosSize.getValue();
                              Box box;
                              if (direction.getAxis().isVertical()) {
                                 box = new Box(vec3d2.getX() - d, vec3d2.getY(), vec3d2.getZ() - d, vec3d2.getX() + d, vec3d2.getY(), vec3d2.getZ() + d);
                              } else {
                                 switch (direction.getAxis()) {
                                    case X:
                                       box = new Box(vec3d2.getX(), vec3d2.getY() - d, vec3d2.getZ() - d, vec3d2.getX(), vec3d2.getY() + d, vec3d2.getZ() + d);
                                       break;
                                    case Z:
                                       box = new Box(vec3d2.getX() - d, vec3d2.getY() - d, vec3d2.getZ(), vec3d2.getX() + d, vec3d2.getY() + d, vec3d2.getZ());
                                       break;
                                    default:
                                       return;
                                 }
                              }

                              TrajectoriesModule.this.animatedBox.handleBox(box, TrajectoriesModule.this.flag2);
                              TrajectoriesModule.this.animatedBox.handleFloat(10.0F, 0.6F);
                              TrajectoriesModule.this.flag2 = false;
                              Render3d.handleCuboid(
                                 Cuboid.createBuilder()
                                    .resolveBuilder11(TrajectoriesModule.this.animatedBox.createBox())
                                    .resolveBuilder18(TrajectoriesModule.this.handPosFill.getValue(), TrajectoriesModule.this.handPosLine.getValue())
                                    .resolveBuilder12(TrajectoriesModule.this.handPosWidth.getValue())
                                    .getCuboid()
                              );
                           } else {
                              TrajectoriesModule.this.flag2 = true;
                           }
                        } else {
                           TrajectoriesModule.this.flag2 = true;
                        }
                     }
                  } else {
                     TrajectoriesModule.this.flag = true;
                     TrajectoriesModule.this.flag2 = true;
                  }
               } else {
                  TrajectoriesModule.this.flag = true;
                  TrajectoriesModule.this.flag2 = true;
               }

               if (!list.isEmpty()) {
                  Render3d.handleLines(Lines.createBuilder().resolveBuilder4(list).resolveBuilder2(TrajectoriesModule.this.width.getValue()).getLines());
               }
            }
         }
      );
   }

   public Identifier resolveIdentifier(Entity entity) {
      return switch (entity) {
         case EnderPearlEntity enderPearlEntity -> identifier2;
         case WindChargeEntity windChargeEntity -> identifier8;
         case FireballEntity fireballEntity -> identifier11;
         case PotionEntity potionEntity -> potionEntity.isLingering() ? identifier4 : identifier9;
         case ExperienceBottleEntity experienceBottleEntity -> identifier7;
         case SnowballEntity snowballEntity -> identifier6;
         case EggEntity eggEntity -> identifier10;
         case ArrowEntity arrowEntity -> identifier5;
         case SpectralArrowEntity spectralArrowEntity -> identifier3;
         case TridentEntity tridentEntity -> identifier12;
         default -> identifier;
      };
   }

   public boolean testEntity2(Entity entity) {
      return entity instanceof EnderPearlEntity && this.pearls.getValue()
         || entity instanceof WindChargeEntity && this.charges.getValue()
         || entity instanceof FireballEntity && this.fireballs.getValue()
         || entity instanceof PotionEntity && this.potions.getValue()
         || entity instanceof ExperienceBottleEntity && this.xP.getValue()
         || entity instanceof SnowballEntity && this.snowballs.getValue()
         || entity instanceof EggEntity && this.eggs.getValue()
         || entity instanceof TridentEntity && this.tridents.getValue()
         || this.testEntity(entity) && !((IPersistentProjectileEntity)(Object)entity).collided() && this.arrows.getValue();
   }

   public float computeItem(Item item) {
      return !(item instanceof SplashPotionItem) && !(item instanceof LingeringPotionItem) && !(item instanceof ExperienceBottleItem) ? 0.0F : -20.0F;
   }

   public TrajectoryUtils.Drag resolveDrag(Item item) {
      return !(item instanceof WindChargeItem) && !(item instanceof FireChargeItem)
         ? (item instanceof TridentItem ? TrajectoryUtils.Drag.Trident : TrajectoryUtils.Drag.Normal)
         : TrajectoryUtils.Drag.Immutable;
   }

   public boolean testItem(Item item) {
      return item instanceof EnderPearlItem
         || item instanceof ExperienceBottleItem
         || item instanceof SnowballItem
         || item instanceof EggItem
         || item instanceof SplashPotionItem
         || item instanceof LingeringPotionItem
         || item instanceof BowItem;
   }

   public float computeItemStack(ItemStack itemStack, int i) {
      Item item = itemStack.getItem();
      if (item instanceof ExperienceBottleItem) {
         return 0.7F;
      } else if (item instanceof ThrowablePotionItem) {
         return 0.5F;
      } else if (item instanceof BowItem) {
         return BowItem.getPullProgress(i) * 3.0F;
      } else {
         return item instanceof TridentItem && mc.player != null ? EnchantmentHelper.getTridentSpinAttackStrength(itemStack, mc.player) : 1.5F;
      }
   }

   public TrajectoryUtils.EntityGravityType resolveEntityGravityType2(Item item) {
      if (item instanceof BowItem || item instanceof SplashPotionItem || item instanceof LingeringPotionItem || item instanceof ExperienceBottleItem) {
         return TrajectoryUtils.EntityGravityType.High;
      } else {
         return !(item instanceof WindChargeItem) && !(item instanceof FireChargeItem) ? TrajectoryUtils.EntityGravityType.Normal : TrajectoryUtils.EntityGravityType.None;
      }
   }

   public TrajectoryUtils.Drag resolveDrag2(Entity entity) {
      return !(entity instanceof WindChargeEntity) && !(entity instanceof FireballEntity)
         ? (entity instanceof TridentEntity ? TrajectoryUtils.Drag.Trident : TrajectoryUtils.Drag.Normal)
         : TrajectoryUtils.Drag.Immutable;
   }

   public int computePlayerEntity(PlayerEntity playerEntity) {
      return 72000 - playerEntity.getItemUseTimeLeft();
   }

   private static class Indicator {
      public final MutableObject<Float> floatValueMutableObject;
      public final MutableObject<Boolean> booleanValueMutableObject;
      public final AnimatedColor animatedColor;
      public final MutableObject<Vec3d> vec3dMutableObject;
      public final MutableObject<TrajectoryData> trajectoryDataMutableObject;
      public final CameraState cameraState;
      public final MutableObject<Boolean> booleanValueMutableObject2;
      public final Entity entity;
      public final FloatAnimator floatAnimator = new FloatAnimator(360.0F, 360.0F);
      public final AnimatedVec3d animatedVec3d;
      public final FloatAnimator floatAnimator2 = new FloatAnimator();
      public final MutableObject<Boolean> booleanValueMutableObject3;
      public final Identifier identifier;
      public final AnimatedColor animatedColor2;
      public final double doubleValue;
      public final FloatAnimator floatAnimator3 = new FloatAnimator();
      public Vec3d vec3d;

      public FloatAnimator getFloatAnimator3() {
         return this.floatAnimator3;
      }

      public CameraState getCameraState() {
         return this.cameraState;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public MutableObject<Float> getFloatValueMutableObject() {
         return this.floatValueMutableObject;
      }

      public void handleFlag(boolean flag) {
         if (!(Boolean)this.booleanValueMutableObject3.getValue() && flag) {
            this.booleanValueMutableObject3.setValue(true);
         }

         if (flag && this.getTrajectoryDataMutableObject().getValue() != null) {
            this.vec3dMutableObject.setValue(((TrajectoryData)this.getTrajectoryDataMutableObject().getValue()).getVec3dList2().getFirst());
         }
      }

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public MutableObject<TrajectoryData> getTrajectoryDataMutableObject() {
         return this.trajectoryDataMutableObject;
      }

      public void handleClientWorld(ClientWorld clientWorld) {
         this.handleFlag(
            clientWorld.getEntityById(this.entity.getId()) == null
               || (Boolean)this.booleanValueMutableObject.getValue()
               || this.entity instanceof IPersistentProjectileEntity iPersistentProjectileEntity
                  && (iPersistentProjectileEntity.collided() || iPersistentProjectileEntity.getInGroundTime() > 0)
         );
      }

      public void run() {
         this.floatValueMutableObject.setValue(null);
      }

      public Identifier getIdentifier() {
         return this.identifier;
      }

      public FloatAnimator getFloatAnimator2() {
         return this.floatAnimator2;
      }

      public Indicator(double d, Identifier identifier, Entity entity, Vec3d vec3d) {
         this.booleanValueMutableObject = new MutableObject(false);
         this.trajectoryDataMutableObject = new MutableObject();
         this.booleanValueMutableObject3 = new MutableObject(false);
         this.vec3dMutableObject = new MutableObject();
         this.booleanValueMutableObject2 = new MutableObject(false);
         this.floatValueMutableObject = new MutableObject(null);
         this.animatedColor = new AnimatedColor();
         this.animatedColor2 = new AnimatedColor();
         this.cameraState = new CameraState();
         this.animatedVec3d = new AnimatedVec3d();
         this.doubleValue = d;
         this.identifier = identifier;
         this.entity = entity;
         this.vec3d = vec3d;
      }

      public boolean testFlag(boolean flag) {
         return (Boolean)this.booleanValueMutableObject3.getValue()
            && (!flag || this.floatAnimator3.getFloatValue2() <= 0.05F && (Boolean)this.booleanValueMutableObject2.getValue());
      }

      public MutableObject<Vec3d> getVec3dMutableObject() {
         return this.vec3dMutableObject;
      }

      public MutableObject<Boolean> getBooleanValueMutableObject() {
         return this.booleanValueMutableObject;
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }

      public void handleRenderContext(
         RenderContext renderContext,
         List<Line> list,
         boolean flag,
         boolean flag2,
         boolean flag3,
         Color color,
         Color color2,
         Color color3,
         Color color4,
         Color color5,
         double d,
         float f,
         float f2
      ) {
         if (MinecraftHolder.mc.getCameraEntity() != null && !(Boolean)this.booleanValueMutableObject.getValue() && this.vec3d != null) {
            if (flag2) {
               renderContext.getMutableVec3d2().handleVec3d(((TrajectoryData)this.trajectoryDataMutableObject.getValue()).getVec3dList2().getFirst());

               for (int i = 0; i < ((TrajectoryData)this.trajectoryDataMutableObject.getValue()).getVec3dList2().size(); i++) {
                  Vec3d vec3d = ((TrajectoryData)this.trajectoryDataMutableObject.getValue()).getVec3dList2().get(i);
                  list.add(new Line(renderContext.getMutableVec3d2().createVec3d(), vec3d, color));
                  renderContext.getMutableVec3d2().handleVec3d(vec3d);
               }
            }

            if (flag) {
               this.animatedVec3d.handleVec3d(this.vec3dMutableObject.getValue() != null ? (Vec3d)this.vec3dMutableObject.getValue() : this.vec3d);
               this.animatedVec3d.handleFloat(10.0F, 0.1F);
               float f3 = renderContext.getFloatValue();
               double d2 = ((TrajectoryData)this.trajectoryDataMutableObject.getValue()).doubleValue / this.doubleValue;
               double d3 = 1.0 - d2;
               double d4 = this.booleanValueMutableObject3.getValue() && this.booleanValueMutableObject2.getValue()
                  ? 0.0
                  : 360.0 - MathUtils.computeDouble5(d3 * 360.0 - 1.5, 0.0, 360.0);
               this.floatAnimator.setFloatValue((float)d4);
               this.floatAnimator.handleFloat3(10.0F, 0.06F);
               this.floatAnimator3
                  .setFloatValue(
                     (!this.booleanValueMutableObject3.getValue() || !this.booleanValueMutableObject2.getValue()) && !(this.cameraState.computeFloat(f3) >= d)
                        ? 1.0F
                        : 0.0F
                  );
               this.floatAnimator3.handleFloat3(10.0F, this.booleanValueMutableObject3.getValue() && this.booleanValueMutableObject2.getValue() ? 0.05F : 0.06F);
               if (!(this.floatAnimator3.getFloatValue2() < 0.05F)) {
                  if (this.floatAnimator3.getFloatValue() == 1.0F && this.floatAnimator3.getFloatValue2() >= 0.8F) {
                     this.booleanValueMutableObject2.setValue(true);
                  }

                  Color color6 = this.floatValueMutableObject.getValue() != null
                        && (!this.booleanValueMutableObject3.getValue() || !this.booleanValueMutableObject2.getValue())
                        && flag3
                     ? (this.floatValueMutableObject.getValue() > 0.0F ? color5 : (this.floatValueMutableObject.getValue() < 0.0F ? color4 : null))
                     : null;
                  this.animatedColor.handleColor2(color6 == null ? color2 : ColorUtils.resolveColor7(color2, color6, f2, color2.getAlpha() / 255.0F));
                  this.animatedColor2.handleColor2(color6 == null ? color3 : ColorUtils.createColor9(color3, color6, f2));
                  this.animatedColor.handleFloat(10.0F, 0.1F);
                  this.animatedColor2.handleFloat(10.0F, 0.1F);
                  this.cameraState.setClientPlayerEntity(MinecraftHolder.mc.player);
                  this.cameraState.setCamera(MinecraftHolder.mc.gameRenderer.getCamera());
                  this.cameraState.setVec3d(this.animatedVec3d.createVec3d());
                  this.cameraState.setEntity(MinecraftHolder.mc.getCameraEntity());
                  this.cameraState.handleFloat(f3, this.floatAnimator3.getFloatValue2() * f, (matrixStack, vec3d) -> {
                     RenderSystem.disableDepthTest();
                     RenderUtils.handleMatrixStack29(matrixStack, this.animatedColor.createColor(), 0.0, 0.0, 5.5, 5.5, 360);
                     RenderSystem.disableDepthTest();
                     RenderSystem.setShaderTexture(0, this.identifier);
                     double dx = 7.0;
                     double d2x = dx / 2.0 - 7.2;
                     RenderUtils.handleMatrixStack2(matrixStack, d2x, d2x, dx);
                     this.handleMatrixStack(matrixStack, this.animatedColor2.createColor(), 4.6, 0.0, 0.0, this.floatAnimator.getFloatValue2(), 0.0);
                     RenderSystem.enableDepthTest();
                  });
               }
            }
         }
      }

      public MutableObject<Boolean> getBooleanValueMutableObject2() {
         return this.booleanValueMutableObject2;
      }

      public void handleClientPlayerEntity(
         ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Function<Entity, Pair<TrajectoryUtils.EntityGravityType, TrajectoryUtils.Drag>> function
      ) {
         if (this.entity.isRemoved()) {
            this.booleanValueMutableObject.setValue(true);
         } else if (clientWorld.getEntityById(this.entity.getId()) == null) {
            this.booleanValueMutableObject.setValue(true);
         } else {
            Pair<TrajectoryUtils.EntityGravityType, TrajectoryUtils.Drag> pair = function.apply(this.entity);
            TrajectoryData trajectoryData = TrajectoryUtils.resolveTrajectoryData(
               clientPlayerEntity,
               clientWorld,
               this.entity,
               this.entity.getPos(),
               this.entity.getVelocity(),
               (TrajectoryUtils.EntityGravityType)pair.getLeft(),
               (TrajectoryUtils.Drag)pair.getRight()
            );
            if (trajectoryData != null && trajectoryData.getVec3dList2() != null && !trajectoryData.getVec3dList2().isEmpty()) {
               this.setVec3d(trajectoryData.getVec3dList2().getLast());
               this.trajectoryDataMutableObject.setValue(trajectoryData);
               if (!this.animatedVec3d.isFlag()) {
                  this.animatedVec3d.handleVec3d3(this.getVec3d());
               }

               if (this.entity instanceof PotionEntity potionEntity && clientWorld.getEntityById(this.entity.getId()) != null) {
                  ItemStack itemStack = potionEntity.getStack();
                  PotionContentsComponent potionContentsComponent = (PotionContentsComponent)itemStack.getOrDefault(
                     DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT
                  );
                  Iterable<StatusEffectInstance> iterable = potionContentsComponent.getEffects();
                  this.run();
                  Vec3d vec3d = this.getVec3d();
                  Box box = new Box(vec3d.x, vec3d.y, vec3d.z, vec3d.x, vec3d.y, vec3d.z).expand(0.3).expand(4.0, 2.0, 4.0);
                  List<LivingEntity> list = potionEntity.getWorld().getNonSpectatingEntities(LivingEntity.class, box);
                  if (!list.isEmpty()) {
                     Iterator var13 = list.iterator();

                     LivingEntity livingEntity;
                     do {
                        if (!var13.hasNext()) {
                           return;
                        }

                        livingEntity = (LivingEntity)var13.next();
                     } while (this.vec3d.squaredDistanceTo(livingEntity.getPos()) > 16.0 || !livingEntity.equals(clientPlayerEntity));

                     for (StatusEffectInstance statusEffectInstance : iterable) {
                        RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
                        int i = statusEffectInstance.getAmplifier();
                        if (((StatusEffect)registryEntry.value()).isInstant()) {
                           if (registryEntry.equals(StatusEffects.INSTANT_DAMAGE)) {
                              this.handleFloat(6 << i);
                           }

                           if (registryEntry.equals(StatusEffects.INSTANT_HEALTH)) {
                              this.handleFloat(-Math.max(4 << i, 0));
                           }
                        }
                     }
                  }
               }
            } else {
               this.booleanValueMutableObject.setValue(true);
            }
         }
      }

      public Entity getEntity() {
         return this.entity;
      }

      public AnimatedColor getAnimatedColor() {
         return this.animatedColor;
      }

      public void setVec3d(Vec3d vec3d) {
         this.vec3d = vec3d;
      }

      public void handleMatrixStack(MatrixStack matrixStack, Color color, double d, double d2, double d3, float f, double d4) {
         Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
         RenderSystem.setShader(GameRenderer::getPositionProgram);
         RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
         BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
         double d8 = f - d4;

         for (double d7 = d4; d7 <= d8; d7 += 0.02) {
            double d5 = Math.sin(Math.toRadians(d7)) * d;
            double d6 = Math.cos(Math.toRadians(d7)) * d;
            float f2 = (float)(d5 + d2);
            float f3 = (float)(d6 + d3);
            bufferBuilder.vertex(matrix4f, f2, f3, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            bufferBuilder.vertex(matrix4f, f2 * 1.1F, f3 * 1.1F, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
         }

         double d9 = Math.sin(Math.toRadians(d8)) * d;
         double d10 = Math.cos(Math.toRadians(d8)) * d;
         float f4 = (float)(d9 + d2);
         float f5 = (float)(d10 + d3);
         bufferBuilder.vertex(matrix4f, f4, f5, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
         BufferUtils.handleBufferBuilder(bufferBuilder);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      public AnimatedColor getAnimatedColor2() {
         return this.animatedColor2;
      }

      public MutableObject<Boolean> getBooleanValueMutableObject3() {
         return this.booleanValueMutableObject3;
      }

      public void handleFloat(float f) {
         if (this.floatValueMutableObject.getValue() == null) {
            this.floatValueMutableObject.setValue(f);
         } else {
            this.floatValueMutableObject.setValue((Float)this.floatValueMutableObject.getValue() + f);
         }
      }

      public AnimatedVec3d getAnimatedVec3d() {
         return this.animatedVec3d;
      }
   }
}
