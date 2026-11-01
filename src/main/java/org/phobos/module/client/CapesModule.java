package org.phobos.module.client;

import java.util.function.IntUnaryOperator;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.type.BlocksSingleEnum;
import org.phobos.type.NoneWavesEnum;
import org.phobos.type.VanillaBasicEnum;
import org.phobos.module.Module;

public class CapesModule extends Module {
   public final Setting<NoneWavesEnum> wind;
   public final Setting<Boolean> animate = this.resolveSetting2(new BooleanSetting("Animate"));
   public final Setting<Integer> height;
   public final Setting<Integer> strave;
   public static CapesModule capesModule;
   public final Setting<VanillaBasicEnum> movement;
   public final Setting<BlocksSingleEnum> style;
   public final Setting<Integer> gravity;

   public static int value() {
      return capesModule.gravity.getValue();
   }

   public CapesModule(PhobosClient phobosClient) {
      super(phobosClient, "Capes", Categories.client, "Manages capes");
      this.wind = this.resolveSetting2(new EnumSetting<>("Wind", NoneWavesEnum.None));
      this.style = this.resolveSetting2(new EnumSetting<>("Style", BlocksSingleEnum.Single));
      this.movement = this.resolveSetting2(new EnumSetting<>("Movement", VanillaBasicEnum.Polygons));
      this.gravity = this.resolveSetting2(new NumberSetting<>("Gravity", 25, 1, 50));
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 6, 1, 10));
      this.strave = this.resolveSetting2(new NumberSetting<>("Strave", 2, 1, 10));
      this.resolveSettingGroup(this.animate, this.wind, this.style, this.movement, this.gravity, this.height, this.strave);
      capesModule = this;
   }

   public static ModelPart[] values(int i, int i2, IntUnaryOperator intUnaryOperator, IntUnaryOperator intUnaryOperator2) {
      ModelPart[] modelParts = new ModelPart[16];
      ModelData modelData = new ModelData();
      ModelPartData modelPartData = modelData.getRoot();

      for (int i3 = 0; i3 < 16; i3++) {
         modelPartData.addChild(
            "customCape_" + i3,
            ModelPartBuilder.create()
               .uv(intUnaryOperator.applyAsInt(i3), intUnaryOperator2.applyAsInt(i3))
               .cuboid(-5.0F, i3, -1.0F, 10.0F, 1.0F, 1.0F, Dilation.NONE, 1.0F, 0.5F),
            ModelTransform.pivot(0.0F, 0.0F, 0.0F)
         );
      }

      ModelPart modelPart = modelPartData.createPart(i, i2);

      for (int i4 = 0; i4 < 16; i4++) {
         modelParts[i4] = modelPart.getChild("customCape_" + i4);
      }

      return modelParts;
   }

   public static int value2() {
      return capesModule.height.getValue();
   }

   public static BlocksSingleEnum getBlocksSingleEnum() {
      return capesModule.style.getValue();
   }

   public static NoneWavesEnum getNoneWavesEnum() {
      return capesModule.wind.getValue();
   }

   public static VanillaBasicEnum getVanillaBasicEnum() {
      return capesModule.movement.getValue();
   }

   public static boolean check() {
      return capesModule.animate.getValue();
   }

   public static int value3() {
      return capesModule.strave.getValue();
   }
}
