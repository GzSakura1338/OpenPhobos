package org.phobos.module.render;

import java.awt.Color;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.BiMapPalette;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.WorldChunk;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.render.RenderContext;
import org.phobos.core.PhobosClient;
import org.phobos.core.placement.DirectionUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.chunk.NewChunkBlocks;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class NewChunksModule extends Module {
   public final Setting<Boolean> poll;
   public final Setting<Color> outline;
   public final Setting<Boolean> liquid;
   public final Setting<Float> width;
   public final Set<ChunkPos> chunkPosSet;
   public final Setting<Double> distance;
   public final Setting<Color> fill = this.resolveSetting2(new ColorSetting("Fill", new Color(0, 0, 0, 50)).getColorSetting());
   public final Set<ChunkPos> chunkPosSet2;
   public final Set<ChunkPos> chunkPosSet3;
   public final Set<ChunkPos> chunkPosSet4;

   public void handleSet(Set<ChunkPos> set) {
      if (mc.getCameraEntity() != null) {
         BlockPos blockPos = mc.getCameraEntity().getBlockPos();
         double d = this.distance.getValue() * 16.0;
         set.removeIf(chunkPos -> !blockPos.isWithinDistance(chunkPos.getStartPos(), d));
      }
   }

   public void handleReceive(PacketEvent.Receive<?> receive, ClientWorld clientWorld) {
      switch (receive.getPacket()) {
         case BlockUpdateS2CPacket blockUpdateS2CPacket:
            ChunkPos chunkPos = new ChunkPos(blockUpdateS2CPacket.getPos());
            if (!blockUpdateS2CPacket.getState().getFluidState().isEmpty() && !blockUpdateS2CPacket.getState().getFluidState().isStill()) {
               for (Direction direction : DirectionUtils.directionArray2) {
                  if (clientWorld.getBlockState(blockUpdateS2CPacket.getPos().offset(direction)).getFluidState().isStill()
                     && !this.chunkPosSet2.contains(chunkPos)
                     && !this.chunkPosSet4.contains(chunkPos)) {
                     this.chunkPosSet2.add(chunkPos);
                     break;
                  }
               }
            }
            break;
         case ChunkDataS2CPacket chunkDataS2CPacket:
            boolean flag9 = Objects.equals(clientWorld.getRegistryKey(), World.OVERWORLD);
            boolean flag10 = Objects.equals(clientWorld.getRegistryKey(), World.NETHER);
            boolean flag11 = Objects.equals(clientWorld.getRegistryKey(), World.END);
            ChunkPos chunkPos2 = new ChunkPos(chunkDataS2CPacket.getChunkX(), chunkDataS2CPacket.getChunkZ());
            if (clientWorld.getChunkManager().getChunk(chunkDataS2CPacket.getChunkX(), chunkDataS2CPacket.getChunkZ()) == null) {
               WorldChunk worldChunk = new WorldChunk(clientWorld, chunkPos2);

               try {
                  CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(
                     () -> worldChunk.loadFromPacket(
                        chunkDataS2CPacket.getChunkData().getSectionsDataBuf(),
                        new NbtCompound(),
                        chunkDataS2CPacket.getChunkData().getBlockEntities(chunkDataS2CPacket.getChunkX(), chunkDataS2CPacket.getChunkZ())
                     ),
                     this.phobosClient.getExecutorService()
                  );
                  completableFuture.join();
               } catch (CompletionException var36) {
               }

               boolean flag = false;
               boolean flag2 = false;
               boolean flag3 = false;
               boolean flag4 = false;
               boolean flag5 = false;
               boolean flag6 = false;
               ChunkSection[] chunkSections = worldChunk.getSectionArray();
               if (flag9 && worldChunk.getStatus().isAtLeast(ChunkStatus.FULL) && !worldChunk.isEmpty()) {
                  for (int i4 = 0; i4 < 17; i4++) {
                     ChunkSection chunkSection = chunkSections[i4];
                     if (chunkSection != null && !chunkSection.isEmpty()) {
                        for (int i3 = 0; i3 < 16; i3++) {
                           for (int i2 = 0; i2 < 16; i2++) {
                              for (int i = 0; i < 16; i++) {
                                 if (!flag4 && NewChunkBlocks.blockSet3.contains(chunkSection.getBlockState(i3, i2, i).getBlock())) {
                                    flag4 = true;
                                 }

                                 if ((i2 >= 5 && i4 == 4 || i4 > 4)
                                    && !flag5
                                    && (
                                       NewChunkBlocks.blockSet4.contains(chunkSection.getBlockState(i3, i2, i).getBlock())
                                          || NewChunkBlocks.blockSet2.contains(chunkSection.getBlockState(i3, i2, i).getBlock())
                                    )) {
                                    flag5 = true;
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }

                  if (flag4 && !flag5) {
                     flag2 = true;
                  }
               }

               if (flag10 && worldChunk.getStatus().isAtLeast(ChunkStatus.FULL) && !worldChunk.isEmpty()) {
                  label395:
                  for (int i8 = 0; i8 < 8; i8++) {
                     ChunkSection chunkSection2 = chunkSections[i8];
                     if (chunkSection2 != null && !chunkSection2.isEmpty()) {
                        for (int i7 = 0; i7 < 16; i7++) {
                           for (int i6 = 0; i6 < 16; i6++) {
                              for (int i5 = 0; i5 < 16; i5++) {
                                 if (NewChunkBlocks.blockSet.contains(chunkSection2.getBlockState(i7, i6, i5).getBlock())) {
                                    flag6 = true;
                                    break label395;
                                 }
                              }
                           }
                        }
                     }
                  }

                  if (!flag2 && !flag6) {
                     flag2 = true;
                  }
               }

               if (flag11 && worldChunk.getStatus().isAtLeast(ChunkStatus.FULL) && !worldChunk.isEmpty()) {
                  ChunkSection chunkSection3 = worldChunk.getSection(0);
                  if (chunkSection3.getBiomeContainer() instanceof PalettedContainer<RegistryEntry<Biome>> palettedContainer) {
                     Palette<RegistryEntry<Biome>> palette = palettedContainer.data.palette();

                     for (int i9 = 0; i9 < palette.getSize(); i9++) {
                        Optional<RegistryKey<Biome>> optional = ((RegistryEntry)palette.get(i9)).getKey();
                        if (optional.isPresent() && optional.get() == BiomeKeys.THE_END) {
                           flag2 = true;
                           break;
                        }
                     }
                  }
               }

               boolean flag7 = false;
               int i22 = 0;
               int i23 = 0;
               int i24 = 0;

               for (ChunkSection chunkSection4 : chunkSections) {
                  if (chunkSection4 != null) {
                     int i17 = 0;
                     int i18 = 0;
                     if (!chunkSection4.isEmpty()) {
                        PalettedContainer<BlockState> palettedContainer2 = chunkSection4.getBlockStateContainer();
                        Palette<BlockState> palette2 = palettedContainer2.data.palette();
                        int i15 = palette2.getSize();
                        if (palette2 instanceof BiMapPalette) {
                           Set<BlockState> set = new HashSet<>();

                           for (int i12 = 0; i12 < 16; i12++) {
                              for (int i11 = 0; i11 < 16; i11++) {
                                 for (int i10 = 0; i10 < 16; i10++) {
                                    set.add((BlockState)palettedContainer2.get(i12, i11, i10));
                                 }
                              }
                           }

                           int i13 = set.size();
                           if (i13 <= 1) {
                              i13 = i15;
                           }

                           if (i13 < i15) {
                              i17 = 2;
                           }
                        }

                        for (int i14 = 0; i14 < i15; i14++) {
                           BlockState blockState = (BlockState)palette2.get(i14);
                           switch (i14) {
                              case 0:
                                 if (i22 == 0 && blockState.isAir() && !flag11) {
                                    flag7 = true;
                                 }

                                 if (blockState.isAir() && flag9) {
                                    i17++;
                                 }
                                 break;
                              case 1:
                                 if ((
                                       blockState.isOf(Blocks.WATER)
                                          || blockState.isOf(Blocks.STONE)
                                          || blockState.isOf(Blocks.GRASS_BLOCK)
                                          || blockState.isOf(Blocks.SNOW_BLOCK)
                                    )
                                    && flag9) {
                                    i17++;
                                 }
                                 break;
                              case 2:
                                 if ((blockState.isOf(Blocks.SNOW_BLOCK) || blockState.isOf(Blocks.DIRT) || blockState.isOf(Blocks.POWDER_SNOW)) && flag9) {
                                    i17++;
                                 }
                           }

                           if (i22 == 4 && blockState.isOf(Blocks.BEDROCK) && flag9) {
                              flag3 = true;
                           }

                           if (blockState.isAir() && (flag10 || flag11)) {
                              i18++;
                           }
                        }

                        if (i18 >= 2) {
                           i24++;
                        }

                        if (i17 >= 2) {
                           i23++;
                        }
                     }

                     if (flag11 && chunkSection4.getBiomeContainer() instanceof PalettedContainer<RegistryEntry<Biome>> palettedContainer3) {
                        Palette<RegistryEntry<Biome>> palette3 = palettedContainer3.data.palette();

                        for (int i16 = 0; i16 < palette3.getSize(); i16++) {
                           Optional<RegistryKey<Biome>> optional2 = ((RegistryEntry)palette3.get(i16)).getKey();
                           if (optional2.isPresent() && optional2.get() == BiomeKeys.PLAINS && i16 == 0) {
                              flag = true;
                           }
                        }
                     }

                     if (!chunkSection4.isEmpty()) {
                        i22++;
                     }
                  }
               }

               if (i22 > 0) {
                  if (!flag10 && !flag11) {
                     double d2 = (double)i23 / i22 * 100.0;
                     if (d2 >= 51.0) {
                        flag = true;
                     }
                  } else {
                     double d = (double)i24 / i22 * 100.0;
                     if (d >= 25.0) {
                        flag3 = true;
                     }
                  }
               }

               if (flag7) {
                  flag = true;
               }

               boolean flag8 = flag11 ? flag : !flag2;
               if (flag && !flag3 && flag8) {
                  if (this.testChunkPos(chunkPos2)) {
                     this.chunkPosSet2.add(chunkPos2);
                     return;
                  }
               } else if (!flag && !flag3 && flag2) {
                  if (this.testChunkPos(chunkPos2)) {
                     this.chunkPosSet.add(chunkPos2);
                     return;
                  }
               } else if (flag3) {
                  if (this.testChunkPos(chunkPos2)) {
                     this.chunkPosSet3.add(chunkPos2);
                     return;
                  }
               } else if (!flag && this.testChunkPos(chunkPos2)) {
                  this.chunkPosSet4.add(chunkPos2);
                  return;
               }

               if (this.liquid.get()) {
                  for (int i21 = 0; i21 < 16; i21++) {
                     for (int i20 = clientWorld.getBottomY(); i20 < clientWorld.getTopY(); i20++) {
                        for (int i19 = 0; i19 < 16; i19++) {
                           FluidState fluidState = worldChunk.getFluidState(i21, i20, i19);
                           if (!fluidState.isEmpty() && !fluidState.isStill() && this.testChunkPos(chunkPos2)) {
                              this.chunkPosSet4.add(chunkPos2);
                              return;
                           }
                        }
                     }
                  }
               }
            }
            break;
         default:
      }
   }

   public NewChunksModule(PhobosClient phobosClient) {
      super(phobosClient, "NewChunks", Categories.render);
      this.outline = this.resolveSetting2(new ColorSetting("Outline", Color.WHITE).getColorSetting());
      this.width = this.resolveSetting2(new NumberSetting<>("Width", 1.0F, 0.0F, 2.0F));
      this.distance = this.resolveSetting2(new NumberSetting<>("Distance", 128.0, 12.0, 300.0));
      this.poll = this.resolveSetting2(new BooleanSetting("Poll"));
      this.liquid = this.resolveSetting2(new BooleanSetting("Liquid").resolveSetting7("Checks liquid flow"));
      this.chunkPosSet2 = ConcurrentHashMap.newKeySet();
      this.chunkPosSet4 = ConcurrentHashMap.newKeySet();
      this.chunkPosSet3 = ConcurrentHashMap.newKeySet();
      this.chunkPosSet = ConcurrentHashMap.newKeySet();
      this.resolveSettingGroup2("Render", this.fill, this.outline, this.width, this.distance, this.poll);
      this.handleListener(
         new SafeListener<PacketEvent.Receive<?>>() {
            public void handleObject(
               PacketEvent.Receive<?> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               NewChunksModule.this.handleReceive(receive, clientWorld);
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
               if (mc.getCameraEntity() != null) {
                  for (ChunkPos chunkPos : NewChunksModule.this.chunkPosSet2) {
                     BlockPos blockPos = chunkPos.getStartPos();
                     Box box = new Box(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 16.0, blockPos.getY(), blockPos.getZ() + 16.0);
                     Render3d.handleCuboid(
                        Cuboid.createBuilder()
                           .resolveBuilder11(box)
                           .resolveBuilder18(NewChunksModule.this.fill.getValue(), NewChunksModule.this.outline.getValue())
                           .resolveBuilder12(NewChunksModule.this.width.getValue())
                           .getCuboid()
                     );
                  }

                  if (NewChunksModule.this.poll.getValue()) {
                     NewChunksModule.this.handleSet(NewChunksModule.this.chunkPosSet2);
                     NewChunksModule.this.handleSet(NewChunksModule.this.chunkPosSet4);
                     NewChunksModule.this.handleSet(NewChunksModule.this.chunkPosSet3);
                     NewChunksModule.this.handleSet(NewChunksModule.this.chunkPosSet);
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::clear);
   }

   @Override
   public void run3() {
      this.clear();
   }

   @Override
   public String nothing() {
      return String.valueOf(this.chunkPosSet2.size());
   }

   public boolean testChunkPos(ChunkPos chunkPos) {
      return !this.chunkPosSet.contains(chunkPos)
         && !this.chunkPosSet3.contains(chunkPos)
         && !this.chunkPosSet4.contains(chunkPos)
         && !this.chunkPosSet2.contains(chunkPos);
   }

   public void clear() {
      this.chunkPosSet2.clear();
      this.chunkPosSet4.clear();
      this.chunkPosSet3.clear();
      this.chunkPosSet.clear();
   }
}
