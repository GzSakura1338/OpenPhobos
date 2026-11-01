package org.phobos.module.client;

import java.awt.Color;
import java.util.Map;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.GraphSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.setting.CurvePoints;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;
import org.phobos.module.combat.AuraModule;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.XRayModule;

public class TestModuleModule extends Module {
   public final Setting<Long> test6;
   public final Setting<Boolean> test1;
   public final Setting<Float> test001;
   public final Setting<Float> test9;
   public final Setting<Integer> seg0;
   public final Setting<Float> test004;
   public final Setting<Color> test7;
   public final Setting<Float> test01;
   public static TestModuleModule testModuleModule;
   public final Setting<Integer> test02;
   public final Setting<Float> test002;
   public static final Logger LOGGER = LoggerFactory.getLogger(TestModuleModule.class);
   public final Setting<Integer> offset0;
   public final Setting<Integer> size0;
   public final Setting<Float> test03;
   public final Setting<Color> test04;
   public final Setting<Float> test005;
   public final Setting<Float> test006;
   public final Setting<Float> test003;
   public final Setting<Double> test5;
   public final Setting<String> test4;
   public final Setting<Float> test8;
   public final Setting<KeyCombination> test2;
   public final Setting<TestModuleModule.TestEnum> test0 = this.resolveSetting2(new EnumSetting<>("Test0", TestModuleModule.TestEnum.Test1));
   public final Setting<Integer> offset1;
   public final Setting<Float> test0012;
   public final Setting<Boolean> test242424;
   public final Setting<Boolean> crash;
   public final Setting<Float> test0016;
   public final Setting<Float> test0021;
   public final Setting<Integer> integerSetting;
   public final Setting<Float> test0014;
   public final Setting<Float> test008;
   public final Setting<Float> test007;
   public final Setting<Float> test0018;
   public final Setting<Integer> integerSetting2;
   public final Setting<Float> test009;
   public final Setting<Integer> integerSetting3;
   public final Setting<Map<Module, Boolean>> test9825;
   public final Setting<Integer> integerSetting4;
   public final Setting<Integer> test0010;
   public final Setting<NumberRange.Mutable<Integer>> test82854;
   public final Setting<Float> test0029;
   public final Setting<Float> test0011;
   public final Setting<Float> test0019;
   public final Setting<CurvePoints> curvePointsSetting;
   public final Setting<Float> test0017;
   public final Setting<Float> test0013;
   public final Setting<Float> test0015;

   public Setting<Float> getTest006() {
      return this.test006;
   }

   public TestModuleModule(PhobosClient phobosClient) {
      super(phobosClient, "TestModule", Categories.client);
      this.test1 = this.resolveSetting2(new BooleanSetting("Test1", false));
      this.test2 = this.resolveSetting2(new BindSetting("Test2"));
      this.test4 = this.resolveSetting2(new StringSetting("Test4", "This is a test."));
      this.test5 = this.resolveSetting2(new NumberSetting<>("Test5", 5.0, 0.0, 100.0));
      this.test6 = this.resolveSetting2(new LongNumberSetting("Test6", 500L, 0L, 2000L));
      this.test7 = this.resolveSetting2(new ColorSetting("Test7", Color.WHITE));
      this.test8 = this.resolveSetting2(new NumberSetting<>("Test8", 0.6F, -60.0F, 60.0F));
      this.test9 = this.resolveSetting2(new NumberSetting<>("Test9", 0.6F, 0.0F, 5.0F));
      this.test01 = this.resolveSetting2(new NumberSetting<>("Test01", 5.0F, 0.0F, 20.0F));
      this.test02 = this.resolveSetting2(new NumberSetting<>("Test02", 20, 0, 30));
      this.test03 = this.resolveSetting2(new NumberSetting<>("Test03", 0.3F, 0.0F, 1.0F));
      this.test04 = this.resolveSetting2(new ColorSetting("Test04", Color.WHITE));
      this.offset0 = this.resolveSetting2(new NumberSetting<>("Offset0", 4, 0, 50));
      this.offset1 = this.resolveSetting2(new NumberSetting<>("Offset1", 4, 0, 50));
      this.size0 = this.resolveSetting2(new NumberSetting<>("Size0", 14, 0, 28));
      this.seg0 = this.resolveSetting2(new NumberSetting<>("Seg0", 0, 0, 720));
      this.test001 = this.resolveSetting2(new NumberSetting<>("Test001", 1.0F, 0.0F, 2.0F));
      this.test002 = this.resolveSetting2(new NumberSetting<>("Test002", 1.0F, -1.0F, 1.0F));
      this.test003 = this.resolveSetting2(new NumberSetting<>("Test003", 1.0F, -1.0F, 1.0F));
      this.test004 = this.resolveSetting2(new NumberSetting<>("Test004", 1.0F, -1.0F, 1.0F));
      this.test005 = this.resolveSetting2(new NumberSetting<>("Test005", 0.0F, -50.0F, 50.0F));
      this.test006 = this.resolveSetting2(new NumberSetting<>("Test006", 0.0F, -20.0F, 20.0F));
      this.test007 = this.resolveSetting2(new NumberSetting<>("Test007", 0.0F, 0.0F, 5.0F));
      this.test008 = this.resolveSetting2(new NumberSetting<>("Test008", 0.0F, 0.1F, 2.0F));
      this.test009 = this.resolveSetting2(new NumberSetting<>("Test009", 1.2F, -15.0F, 15.0F));
      this.test0010 = this.resolveSetting2(new NumberSetting<>("Test0010", 0, 0, 50));
      this.test0011 = this.resolveSetting2(new NumberSetting<>("Test0011", 0.0F, 0.0F, 1.0F));
      this.test0012 = this.resolveSetting2(new NumberSetting<>("Test0012", 0.0F, 0.0F, 10.0F));
      this.test0013 = this.resolveSetting2(new NumberSetting<>("Test0013", 0.0F, 0.0F, 1.0F));
      this.test0014 = this.resolveSetting2(new NumberSetting<>("Test0014", 0.0F, 0.0F, 40.0F));
      this.test0015 = this.resolveSetting2(new NumberSetting<>("Test0015", 0.0F, 0.0F, 0.01F));
      this.test0016 = this.resolveSetting2(new NumberSetting<>("Test0016", 0.0F, 0.0F, 0.5F));
      this.test0017 = this.resolveSetting2(new NumberSetting<>("Test0017", 0.0F, 0.0F, 300.0F));
      this.test0018 = this.resolveSetting2(new NumberSetting<>("Test0018", 0.0F, 0.0F, 2.0F));
      this.test0019 = this.resolveSetting2(new NumberSetting<>("Test0019", 0.0F, 0.0F, 5.0F));
      this.test0029 = this.resolveSetting2(new NumberSetting<>("Test0029", 0.0F, 0.0F, 25.0F));
      this.test0021 = this.resolveSetting2(new NumberSetting<>("Test0021", 0.0F, 0.0F, 10.0F));
      this.test242424 = this.resolveSetting2(new BooleanSetting("Test242424"));
      this.integerSetting = this.resolveSetting2(new NumberSetting<>("A", 0, -6, 6));
      this.integerSetting4 = this.resolveSetting2(new NumberSetting<>("B", 0, -6, 6));
      this.integerSetting2 = this.resolveSetting2(new NumberSetting<>("C", 0, -2, 6));
      this.integerSetting3 = this.resolveSetting2(new NumberSetting<>("D", 0, -6, 6));
      this.crash = this.resolveSetting2(new BooleanSetting("Crash"));
      this.test9825 = this.resolveSetting2(
         new MultiSelectSetting<>("Test9825", Module.class)
            .resolveMultiSelectSetting2(AnimationsModule.getAnimationsModule(), "Animations", true)
            .resolveMultiSelectSetting2(AuraModule.getAuraModule(), "Aura", false)
            .resolveMultiSelectSetting2(XRayModule.getXRayModule(), "XRay", true)
      );
      this.test82854 = this.resolveSetting2(new MutableNumberSetting<>("Test82854", NumberRange.createMutable(4, 5), 0, 10, 1));
      this.curvePointsSetting = this.resolveSetting2(new GraphSetting("Test!"));
      testModuleModule = this;
      this.handleTriConsumer3((clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> {
         if (clientPlayerEntity == null || clientWorld == null || clientPlayerInteractionManager == null) {
            ;
         }
      });
      this.handleListener(
         new SafeListener<Send<?>>() {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               switch (send.getPacket()) {
                  case ClickSlotC2SPacket clickSlotC2SPacket:
                     TestModuleModule.this.handleText6("%s:%s".formatted(clickSlotC2SPacket.getSlot(), clickSlotC2SPacket.getButton()));
                  default:
               }
            }
         }
      );
   }

   @Override
   public void run4() {
   }

   public Setting<Boolean> getTest1() {
      return this.test1;
   }

   public Setting<Float> getTest004() {
      return this.test004;
   }

   public Setting<Long> getTest6() {
      return this.test6;
   }

   public Setting<Float> getTest007() {
      return this.test007;
   }

   public Setting<Float> getTest005() {
      return this.test005;
   }

   public Setting<KeyCombination> getTest2() {
      return this.test2;
   }

   public Setting<Float> getTest01() {
      return this.test01;
   }

   public Setting<Float> getTest9() {
      return this.test9;
   }

   public Setting<String> getTest4() {
      return this.test4;
   }

   public Setting<Float> getTest003() {
      return this.test003;
   }

   public Setting<Integer> getTest02() {
      return this.test02;
   }

   @Override
   public void run3() {
   }

   public Setting<Integer> getOffset1() {
      return this.offset1;
   }

   public Setting<Integer> getSeg0() {
      return this.seg0;
   }

   public Setting<Color> getTest7() {
      return this.test7;
   }

   public Setting<Float> getTest03() {
      return this.test03;
   }

   public Setting<TestModuleModule.TestEnum> getTest0() {
      return this.test0;
   }

   public Setting<Float> getTest001() {
      return this.test001;
   }

   public Setting<Float> getTest002() {
      return this.test002;
   }

   public Setting<Integer> getSize0() {
      return this.size0;
   }

   public Setting<Double> getTest5() {
      return this.test5;
   }

   public Setting<Color> getTest04() {
      return this.test04;
   }

   public Setting<Integer> getOffset0() {
      return this.offset0;
   }

   public Setting<Float> getTest8() {
      return this.test8;
   }

   public Setting<Float> getTest0015() {
      return this.test0015;
   }

   public Setting<Float> getTest008() {
      return this.test008;
   }

   public Setting<Float> getTest0017() {
      return this.test0017;
   }

   public Setting<Integer> getIntegerSetting3() {
      return this.integerSetting3;
   }

   public Setting<Integer> getIntegerSetting() {
      return this.integerSetting;
   }

   public Setting<Boolean> getCrash() {
      return this.crash;
   }

   public Setting<Map<Module, Boolean>> getTest9825() {
      return this.test9825;
   }

   public Setting<Float> getTest0011() {
      return this.test0011;
   }

   public Setting<Float> getTest0021() {
      return this.test0021;
   }

   public Setting<NumberRange.Mutable<Integer>> getTest82854() {
      return this.test82854;
   }

   public Setting<CurvePoints> getCurvePointsSetting() {
      return this.curvePointsSetting;
   }

   public Setting<Float> getTest0016() {
      return this.test0016;
   }

   public Setting<Integer> getIntegerSetting4() {
      return this.integerSetting4;
   }

   public Setting<Float> getTest0018() {
      return this.test0018;
   }

   public Setting<Integer> getIntegerSetting2() {
      return this.integerSetting2;
   }

   public Setting<Float> getTest0014() {
      return this.test0014;
   }

   public Setting<Float> getTest0019() {
      return this.test0019;
   }

   public Setting<Integer> getTest0010() {
      return this.test0010;
   }

   public Setting<Float> getTest0029() {
      return this.test0029;
   }

   public static TestModuleModule getTestModuleModule() {
      return testModuleModule;
   }

   public Setting<Float> getTest0013() {
      return this.test0013;
   }

   public Setting<Float> getTest0012() {
      return this.test0012;
   }

   public Setting<Boolean> getTest242424() {
      return this.test242424;
   }

   public Setting<Float> getTest009() {
      return this.test009;
   }

   private enum TestEnum {
      Test1,
      Test2,
      Test3;
   }
}
