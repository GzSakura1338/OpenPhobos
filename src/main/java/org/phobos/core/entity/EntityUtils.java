package org.phobos.core.entity;

import com.google.common.base.Predicates;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IEntity;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.combat.AntiBotModule;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.ClientHolder;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;

public final class EntityUtils implements MinecraftHolder {
   public static final List<EntityType<?>> entityTypeList = new ArrayList<>();

   @SafeVarargs
   public static PlayerEntity resolvePlayerEntity(
      ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, PhobosClient phobosClient, double d, Predicate<PlayerEntity>... predicates
   ) {
      if (clientWorld != null && clientPlayerEntity != null) {
         List<PlayerEntity> list = new ArrayList<>();
         Predicate<PlayerEntity> predicate = (Predicate<PlayerEntity>)(predicates.length == 1 ? predicates[0] : Predicates.alwaysTrue());

         for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
            if (playerEntity != clientPlayerEntity
               && !phobosClient.getFriendList().testUUID(playerEntity.getUuid())
               && !playerEntity.isCreative()
               && !playerEntity.isSpectator()
               && playerEntity.isAlive()
               && !(clientPlayerEntity.distanceTo(playerEntity) > d)
               && predicate.test(playerEntity)) {
               list.add(playerEntity);
            }
         }

         return list.stream().min(Comparator.comparingDouble(clientPlayerEntity::distanceTo)).orElse(null);
      } else {
         return null;
      }
   }

   public static Iterable<EndCrystalEntity> resolveIterable(PlayerEntity playerEntity, ClientWorld clientWorld, double d) {
      return clientWorld.getEntitiesByType(
         EntityType.END_CRYSTAL,
         BlockPosUtils.createBox(playerEntity, 8.0),
         endCrystalEntity -> endCrystalEntity.isAlive()
            && testPlayerEntity2(playerEntity, endCrystalEntity, d)
            && clientWorld.getWorldBorder().contains(endCrystalEntity.getBlockPos())
      );
   }

   public static void run5() {
      for (EntityType<?> entityType : Registries.ENTITY_TYPE) {
         entityTypeList.add(entityType);
      }
   }

   public static <T extends Entity> List<T> resolveList(ClientWorld clientWorld, Class<T> classValue, Box box, Predicate<T> predicate) {
      return clientWorld.getOtherEntities(null, box).stream().filter(entity -> {
         if (classValue.isInstance(entity)) {
            T entity2 = (T)entity;
            return predicate.test(entity2);
         } else {
            return false;
         }
      }).map(entity -> (T)entity).toList();
   }

   public static float computeLivingEntity(LivingEntity livingEntity) {
      return livingEntity.getHealth() + livingEntity.getAbsorptionAmount();
   }

   public static boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Entity entity) {
      Vec3d vec3d = clientPlayerEntity.getEyePos();
      Vec3d vec3d2 = entity.getRotationVector(0.0F, entity.getHeadYaw());
      Vec3d vec3d3 = vec3d.relativize(entity.getPos());
      vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z).normalize();
      return vec3d3.dotProduct(vec3d2) < 0.0;
   }

   public static boolean testPhobosClient(PhobosClient phobosClient, PlayerEntity playerEntity, PlayerEntity playerEntity2, double d) {
      return testPhobosClient2(phobosClient, playerEntity, playerEntity2) && playerEntity.squaredDistanceTo(playerEntity2) <= d * d;
   }

   public static boolean testLivingEntity(LivingEntity livingEntity) {
      return !livingEntity.isAlive() || livingEntity.isDead();
   }

   public static void handleEntity(Entity entity, double d, double d2, double d3) {
      entity.setPosition(d, d2, d3);
      entity.lastRenderX = d;
      entity.lastRenderY = d2;
      entity.lastRenderZ = d3;
      entity.prevX = d;
      entity.prevY = d2;
      entity.prevZ = d3;
   }

   public static void handleEntityEquipmentUpdateS2CPacket(
      EntityEquipmentUpdateS2CPacket entityEquipmentUpdateS2CPacket, World world, EntityUtils.EquipmentUpdate equipmentUpdate
   ) {
      List<Pair<EquipmentSlot, ItemStack>> list = entityEquipmentUpdateS2CPacket.getEquipmentList();
      if (world.getEntityById(entityEquipmentUpdateS2CPacket.getEntityId()) instanceof LivingEntity livingEntity) {
         ItemStack itemStack2 = livingEntity.getMainHandStack();
         ItemStack itemStack3 = livingEntity.getOffHandStack();
         MutableObject mutableObject = new MutableObject(ItemStack.EMPTY);
         MutableObject mutableObject2 = new MutableObject(ItemStack.EMPTY);

         for (Pair<EquipmentSlot, ItemStack> pair : list) {
            EquipmentSlot equipmentSlot = (EquipmentSlot)pair.getFirst();
            ItemStack itemStack = (ItemStack)pair.getSecond();
            if (!equipmentSlot.isArmorSlot()) {
               switch (equipmentSlot) {
                  case MAINHAND:
                     mutableObject.setValue(new ItemStack(itemStack2.getItem(), itemStack.getCount()));
                     break;
                  case OFFHAND:
                     mutableObject2.setValue(new ItemStack(itemStack3.getItem(), itemStack.getCount()));
               }
            }
         }

         equipmentUpdate.handleItemStack(itemStack2, (ItemStack)mutableObject.getValue(), itemStack3, (ItemStack)mutableObject2.getValue());
      }
   }

   public static double computeEntity(Entity entity) {
      double d = entity.getX() - entity.prevX;
      double d2 = entity.getZ() - entity.prevZ;
      double d3 = Math.sqrt(d * d + d2 * d2) / 1000.0;
      double d4 = 1.388888888888889E-5;
      return MathUtils.computeDouble(d3 / d4, 2);
   }

   public static boolean testLivingEntity2(LivingEntity livingEntity) {
      return livingEntity instanceof ClientPlayerEntity clientPlayerEntity ? PlayerActionUtils.testClientPlayerEntity3(clientPlayerEntity) : livingEntity.isFallFlying();
   }

   public static boolean testLivingEntity3(LivingEntity livingEntity) {
      return livingEntity instanceof ClientPlayerEntity clientPlayerEntity ? PlayerActionUtils.testClientPlayerEntity5(clientPlayerEntity) : livingEntity.isFallFlying();
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity) {
      for (ItemStack itemStack : playerEntity.getArmorItems()) {
         if (!itemStack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public static boolean testPlayerEntity2(PlayerEntity playerEntity, Entity entity, double d) {
      return entity.getBoundingBox().squaredMagnitude(playerEntity.getEyePos()) < MathHelper.square(d);
   }

   public static Box resolveBox(Entity entity) {
      PhobosClient phobosClient = ClientHolder.getPhobosClient();
      return phobosClient.getEntityPoseManager().resolveBox(entity, phobosClient.getEntityPoseManager().resolveEntityPose(entity));
   }

   public static double computeEntity2(Entity entity) {
      double d = entity.getX() - entity.prevX;
      double d2 = entity.getY() - entity.prevY;
      double d3 = entity.getZ() - entity.prevZ;
      d *= 20.0;
      d2 *= 20.0;
      d3 *= 20.0;
      return new Vec3d(d, d2, d3).horizontalLength();
   }

   public static String describeText(String text) {
      List<String> list = new ArrayList<>();

      for (EntityType<?> entityType : entityTypeList) {
         String text2 = entityType.getName().getString();
         if (text2.toLowerCase().startsWith(text.toLowerCase())) {
            list.add(text2);
         }
      }

      if (list.isEmpty()) {
         return null;
      } else {
         return list.size() == 1 ? list.getFirst() : ChatTextUtils.describeList(list);
      }
   }

   public static boolean testPhobosClient2(PhobosClient phobosClient, PlayerEntity playerEntity, PlayerEntity playerEntity2) {
      return playerEntity2 instanceof IEntity iEntity && iEntity.isClientSide()
         ? false
         : playerEntity != null
            && playerEntity2 != null
            && !testLivingEntity(playerEntity2)
            && !playerEntity.equals(playerEntity2)
            && !phobosClient.getFriendList().testUUID(playerEntity2.getUuid())
            && !playerEntity2.isCreative()
            && !playerEntity2.isSpectator()
            && !AntiBotModule.testEntity2(playerEntity2);
   }

   public static EntityType<?> resolveEntityType(String text) {
      for (EntityType<?> entityType : entityTypeList) {
         if (entityType.getName().getString().equalsIgnoreCase(text)) {
            return entityType;
         }
      }

      return null;
   }

   public EntityUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Optional<PlayerListEntry> resolveOptional(String text) {
      return mc.getNetworkHandler() == null ? Optional.empty() : Optional.ofNullable(mc.getNetworkHandler().getPlayerListEntry(text));
   }

   public static PlayerEntity resolvePlayerEntity2(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, PhobosClient phobosClient) {
      if (clientWorld != null && clientPlayerEntity != null) {
         List<PlayerEntity> list = new ArrayList<>();

         for (PlayerEntity playerEntity : clientWorld.getPlayers()) {
            if (playerEntity != clientPlayerEntity
               && !phobosClient.getFriendList().testUUID(playerEntity.getUuid())
               && !playerEntity.isCreative()
               && !playerEntity.isSpectator()
               && playerEntity.isAlive()) {
               list.add(playerEntity);
            }
         }

         return list.stream().min(Comparator.comparingDouble(clientPlayerEntity::distanceTo)).orElse(null);
      } else {
         return null;
      }
   }

   public interface EquipmentUpdate {
      void handleItemStack(ItemStack var1, ItemStack var2, ItemStack var3, ItemStack var4);
   }
}
