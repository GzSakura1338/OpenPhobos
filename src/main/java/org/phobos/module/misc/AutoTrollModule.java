package org.phobos.module.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chat.TextPair;
import org.phobos.core.chat.ChatTemplate;
import org.phobos.core.chat.TrollMessageSet;
import org.phobos.core.chat.TrollMessageBuilder;
import org.phobos.core.combat.SurroundManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.data.AttackersRecord;
import org.phobos.event.AddMessageEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class AutoTrollModule extends Module {
   public final Setting<NumberRange.Mutable<Long>> tPM;
   public final MutableObject<TrollMessageBuilder> trollMessageBuilderMutableObject;
   public final TrollMessageSet trollMessageSet;
   public final Setting<Long> killedTimeout;
   public final List<String> textList;
   public final Map<String, Long> textMap;
   public final Setting<Boolean> mentioned;
   public String text;
   public final Setting<String> target = this.resolveSetting2(new StringSetting("Target", "Player").resolveSetting7("Specific player"));
   public boolean flag;
   public final Setting<Boolean> killed;

   @Override
   public void run4() {
      this.run2();
   }

   public String describe() {
      if (this.text == null) {
         return this.describe2();
      }

      String text = this.describe2();
      if (this.text.equals(text)) {
         return this.describe();
      }

      this.text = text;
      return text;
   }

   @Nullable
   public String describeText(String text) {
      int i = text.indexOf(60);
      int i2 = text.indexOf(62);
      return i != -1 && i2 != -1 && i2 > i ? text.substring(i + 1, i2) : null;
   }

   public int computeTextPair(TextPair textPair) {
      if (mc.world == null) {
         return MathUtils.computeInt5(10, 15);
      }

      String text = textPair.getText();

      for (PlayerEntity playerEntity : mc.world.getPlayers()) {
         if (playerEntity.getNameForScoreboard().equals(text)) {
            SurroundManager.TotemCalculation totemCalculation = this.phobosClient.getSurroundManager().resolveTotemCalculation(playerEntity);
            if (totemCalculation != null) {
               return totemCalculation.current();
            }
         }
      }

      return MathUtils.computeInt5(15, 20);
   }

   public void handleText(String text, Runnable runnable) {
      long l = 50L;

      for (int i = 0; i < text.length(); i++) {
         l += this.tPM.getValue().getNumber4();
      }

      this.phobosClient.getDelayedTaskManager().handleRunnable(() -> {
         if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendChatMessage(text);
         }

         runnable.run();
      }, l);
   }

   @Override
   public String nothing() {
      Set<String> set = this.textMap.keySet();
      return this.target.getValue() + (set.isEmpty() ? "" : ", " + String.join(", ", set));
   }

   public AutoTrollModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTroll", Categories.misc, "Trolls whoever you killed recently");
      this.tPM = this.resolveSetting2(new MutableLongSetting("TPM", NumberRange.createMutable(50L, 200L), 25L, 200L));
      this.killed = this.resolveSetting2(new BooleanSetting("Killed").resolveSetting7("Troll players you have killed"));
      this.killedTimeout = this.resolveSetting2(new LongNumberSetting("KilledTimeout", 5000L, 500L, 30000L));
      this.mentioned = this.resolveSetting2(new BooleanSetting("Mentioned"));
      this.trollMessageBuilderMutableObject = new MutableObject();
      this.textMap = new HashMap<>();
      this.flag = false;
      this.trollMessageSet = new TrollMessageSet()
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("fat")
               .resolveTrollMessageBuilder("what ur Bmi ugly fat", "fat boy")
               .resolveTrollMessageBuilder5("LOLLLL AINT U 500 POUNDS")
               .resolveTrollMessageBuilder5("projection")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("nn")
               .resolveTrollMessageBuilder6("noname")
               .resolveTrollMessageBuilder6("random")
               .resolveTrollMessageBuilder("Im known af", "Who u")
               .resolveTrollMessageBuilder5("Lel who u?")
               .resolveTrollMessageBuilder5("u Nn")
               .resolveTrollMessageBuilder("name 10 people who know u lel", "Ok name atleast 3 nn")
               .resolveTrollMessageBuilder("name 3 people who know u", "lel u wish 1 person knew u", "nn")
               .resolveTrollMessageBuilder5("lol so ironic")
               .resolveTrollMessageBuilder5("u not famous like me")
               .resolveTrollMessageBuilder("u mad im more known than u", "lol")
               .resolveTrollMessageBuilder5("never heard of u g")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("iq")
               .resolveTrollMessageBuilder6("intelligence quotient")
               .resolveTrollMessageBuilder("im smart af", "and u dumb", "thats why im a business owner and u not")
               .resolveTrollMessageBuilder("Im a genius millionaire", "u live in the slums g", "in the suburbs")
               .resolveTrollMessageBuilder3(textPair -> String.valueOf(MathUtils.computeInt5(1000, Integer.MAX_VALUE)))
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("white")
               .resolveTrollMessageBuilder6("black")
               .resolveTrollMessageBuilder6("chink")
               .resolveTrollMessageBuilder("aint u jewish", "fucking kike")
               .resolveTrollMessageBuilder("coming from a kike btw", "lel jew 4 lyfe")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("poor")
               .resolveTrollMessageBuilder6("no money")
               .resolveTrollMessageBuilder6("b4b")
               .resolveTrollMessageBuilder6("bands")
               .resolveTrollMessageBuilder("You aint even got 10 dollars to ur name")
               .resolveTrollMessageBuilder5("u poor g")
               .resolveTrollMessageBuilder5("ur house on my wrist btw")
               .resolveTrollMessageBuilder("move 100k rn g", "Lel u cant move 100k")
               .resolveTrollMessageBuilder("U prolly got no nfts")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("gg")
               .resolveTrollMessageBuilder6("good game")
               .resolveTrollMessageBuilder6("wp")
               .resolveTrollMessageBuilder("nigga said gg", "LOL")
               .resolveTrollMessageBuilder("Ye gg bro but u ass", "like below average ass")
               .resolveTrollMessageBuilder5("lel u mad")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("kys")
               .resolveTrollMessageBuilder("lel u coping", "fat fuck")
               .resolveTrollMessageBuilder5("lel u mad")
               .resolveTrollMessageBuilder5("stay mad faggot")
               .resolveTrollMessageBuilder5("u getting heated")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("dox")
               .resolveTrollMessageBuilder6("swat")
               .resolveTrollMessageBuilder("u harmless")
               .resolveTrollMessageBuilder5("try me nigga")
               .resolveTrollMessageBuilder("run those 1s", "faggot")
               .resolveTrollMessageBuilder5("lel u opsecless")
               .resolveTrollMessageBuilder5("Ur binned nigga")
               .resolveTrollMessageBuilder("watchout nigga im harmful", "id kill ur family")
               .resolveTrollMessageBuilder5("LOLL UR DOXXED TO THE BONES!!")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("bad bait")
               .resolveTrollMessageBuilder6("stop baiting")
               .resolveTrollMessageBuilder("lel what bait", "im deadass")
               .resolveTrollMessageBuilder5("irony")
               .resolveTrollMessageBuilder5("holy cope")
               .resolveTrollMessageBuilder5("u soft")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("nigga")
               .resolveTrollMessageBuilder6("nga")
               .resolveTrollMessageBuilder6("nigger")
               .resolveTrollMessageBuilder5("Im white retard")
               .resolveTrollMessageBuilder("LOLL u mad", "See how u losing ur mind over this LOLLL")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("ur bad")
               .resolveTrollMessageBuilder6("ur ass")
               .resolveTrollMessageBuilder6("ur horrible")
               .resolveTrollMessageBuilder3(textPair -> "Lol dont u carry %s totems".formatted(this.computeTextPair(textPair)))
               .resolveTrollMessageBuilder5("Still better than u")
               .resolveTrollMessageBuilder5("Is that why u dying 2 me")
               .resolveTrollMessageBuilder("ironic", "killed u w no armor a few days ago")
               .resolveTrollMessageBuilder3(textPair -> "LOL %s-0".formatted(MathUtils.computeInt5(20, 60)))
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("shut up")
               .resolveTrollMessageBuilder6("shut the fuck up")
               .resolveTrollMessageBuilder6("stfu")
               .resolveTrollMessageBuilder6("sybau")
               .resolveTrollMessageBuilder6("syfm")
               .resolveTrollMessageBuilder6("pipe down")
               .resolveTrollMessageBuilder6("shut up")
               .resolveTrollMessageBuilder5("Make me lil boy")
               .resolveTrollMessageBuilder("U mad nigga", "LOOLLOLOL")
               .resolveTrollMessageBuilder("why u angry", "im happy u angry")
               .resolveTrollMessageBuilder5("lel im living in ur head")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("not funny")
               .resolveTrollMessageBuilder6("unfunny")
               .resolveTrollMessageBuilder6("corny")
               .resolveTrollMessageBuilder6("cornball")
               .resolveTrollMessageBuilder4(
                  textPair -> "Pretty ironic",
                  textPair -> "Aint ur name %s".formatted(textPair.getText()),
                  textPair -> "U cant be typing with that name"
               )
               .resolveTrollMessageBuilder("u just retarded")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("main")
               .resolveTrollMessageBuilder6("main up")
               .resolveTrollMessageBuilder6("alias")
               .resolveTrollMessageBuilder4(textPair -> "My main is %s".formatted(textPair.getText()))
               .resolveTrollMessageBuilder3(textPair -> "Im %s".formatted(mc.player == null ? "the king" : mc.player.getNameForScoreboard()))
         )
         .resolveTrollMessageSet(new TrollMessageBuilder().resolveTrollMessageBuilder6("i win").resolveTrollMessageBuilder5("No u didnt").resolveTrollMessageBuilder5("in ur dreams buddy"))
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("u lost")
               .resolveTrollMessageBuilder6("u died")
               .resolveTrollMessageBuilder("me?", "who u talking 2")
               .resolveTrollMessageBuilder("No i didnt")
               .resolveTrollMessageBuilder("nope", "lel no kills on me")
               .resolveTrollMessageBuilder5("source?")
               .resolveTrollMessageBuilder("in ur dreams buddy", "keep dreaming")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("skid")
               .resolveTrollMessageBuilder("Aint that u", "lel u a skid")
               .resolveTrollMessageBuilder("who me?", "me?? who u talkin to")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("paste")
               .resolveTrollMessageBuilder("u use a paste lel", "nigga thinks his shitware isnt oyvey")
               .resolveTrollMessageBuilder5("didnt u skid oyvey in 2021")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("newgen")
               .resolveTrollMessageBuilder6("newfag")
               .resolveTrollMessageBuilder5("U joined this year")
               .resolveTrollMessageBuilder5("joined b4 u")
               .resolveTrollMessageBuilder5("LOLLL")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("unc")
               .resolveTrollMessageBuilder6("ur old")
               .resolveTrollMessageBuilder5("u pushing 40 urself")
               .resolveTrollMessageBuilder("let me guess ur age", "9", "Ok 8 maybe")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("prac")
               .resolveTrollMessageBuilder6("prac main")
               .resolveTrollMessageBuilder6("hop on 2b")
               .resolveTrollMessageBuilder5("dont u play 5b5t LOLLL")
               .resolveTrollMessageBuilder("u play 6b g", "pack it up")
               .resolveTrollMessageBuilder5("u main 9b wrap it up")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("u logged")
               .resolveTrollMessageBuilder6("u logging")
               .resolveTrollMessageBuilder6("ez log")
               .resolveTrollMessageBuilder5("Lel i got kicked")
               .resolveTrollMessageBuilder5("kicked")
               .resolveTrollMessageBuilder("LOL", "do something about it", "LOLOLOOL DO SOMETHING")
               .resolveTrollMessageBuilder("Me: 1 U: 20", "U winning in logs g", "the only thing u winning in btw")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("i got kicked")
               .resolveTrollMessageBuilder6("got kicked")
               .resolveTrollMessageBuilder5("Sure")
               .resolveTrollMessageBuilder5("yea keep lying")
               .resolveTrollMessageBuilder("we all know thats not true", "LOL", "keep logging fatass nigga")
         )
         .resolveTrollMessageSet(
            new TrollMessageBuilder()
               .resolveTrollMessageBuilder6("autobait")
               .resolveTrollMessageBuilder6("autotroll")
               .resolveTrollMessageBuilder6("spammer")
               .resolveTrollMessageBuilder("LOLLL U hs cant handle it", "fat retard", "u getting autobaited rn")
               .resolveTrollMessageBuilder("i just know more words than u", "3rd world ass nigga", "u live in a shed")
               .resolveTrollMessageBuilder("U GETTING BAITED BY SPAMMER LOL", "THATS SAD", "my spammer smarter than u lelmode")
               .resolveTrollMessageBuilder("lelcopter u retarded?", "lel ur falling for the bait")
         );
      this.text = null;
      this.textList = (List<String>)Util.make(new ArrayList(), arrayList -> {
         arrayList.add("LOL");
         arrayList.add("LOLOOLOL");
         arrayList.add("HUSH UP NIGGA");
         arrayList.add("PIPE DOWN LOOOL");
         arrayList.add("FAT FUCK");
         arrayList.add("UR A BITCH LOL");
         arrayList.add("LOLLL 10-0");
         arrayList.add("LOOLLL 30-0");
         arrayList.add("FRI3ND");
         arrayList.add("FR13NDDD");
         arrayList.add("LOLL");
         arrayList.add("FRIENDD");
         arrayList.add("HELLO");
         arrayList.add("HELLO Fr13NDD");
         arrayList.add("REFUND UR PASTE");
         arrayList.add("LOL AINT U RATTED");
         arrayList.add("LOL AINT U BINNED");
         arrayList.add("Move 10k rn");
         arrayList.add("Move 100k dog");
         arrayList.add("lel");
         arrayList.add("lel");
         arrayList.add("lel");
         arrayList.add("nigger");
         arrayList.add("nigga");
         arrayList.add("uninstall this game");
         arrayList.add("dumb retard");
         arrayList.add("dumb kike");
         arrayList.add("fat retard");
         arrayList.add("fat kike");
         arrayList.add("ugly nigger");
         arrayList.add("pedophile");
         arrayList.add("kike");
         arrayList.add("faggot");
         arrayList.add("3rd worlder");
         arrayList.add("yea keep talking retard");
         arrayList.add("Still cant shut up");
         arrayList.add("LOL SEE UR MAD");
         arrayList.add("nigga u mad af");
         arrayList.add("kys retard");
         arrayList.add("kys nigga");
         arrayList.add("kys nigger");
      });
      this.resolveSettingGroup(this.killed, this.killedTimeout);
      this.handleListener(
         new SafeListener<AddMessageEvent>(Integer.MAX_VALUE) {
            public void handleObject(
               AddMessageEvent addMessageEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               String text2 = addMessageEvent.getText().getString();
               if (!text2.isEmpty() && !AutoTrollModule.this.flag) {
                  Set<String> set = new HashSet<>();
                  String text3 = clientPlayerEntity.getNameForScoreboard();
                  String text;
                  if (AutoTrollModule.this.mentioned.getValue()
                     && text2.contains(text3)
                     && !text2.contains("<%s>".formatted(text3))
                     && (text = AutoTrollModule.this.describeText(text2)) != null
                     && clientPlayerEntity.networkHandler.getPlayerListEntry(text) != null) {
                     set.add(text);
                  }

                  set.add(AutoTrollModule.this.target.getValue());
                  set.addAll(AutoTrollModule.this.textMap.keySet());
                  if (AutoTrollModule.this.trollMessageSet.testText(text2, set, textx -> AutoTrollModule.this.textMap.put(textx, System.currentTimeMillis()))) {
                     AutoTrollModule.this.trollMessageSet
                        .resolveOptional(text2, set)
                        .ifPresentOrElse(
                           trollMessageBuilder -> {
                              if (trollMessageBuilder.testText2(text2, set)) {
                                 AutoTrollModule.this.trollMessageBuilderMutableObject.setValue(trollMessageBuilder);
                              }
                           },
                           () -> {
                              Supplier<String> supplier = () -> TrollMessageBuilder.describeText(
                                 AutoTrollModule.this.textList.get(MathUtils.computeInt5(0, AutoTrollModule.this.textList.size() - 1))
                              );
                              AutoTrollModule.this.flag = true;
                              AutoTrollModule.this.handleText(supplier.get(), () -> {
                                 AutoTrollModule.this.flag = false;
                                 if (MathUtils.testSupplier2(() -> 35.0)) {
                                    AutoTrollModule.this.flag = true;
                                    AutoTrollModule.this.handleText(supplier.get(), () -> AutoTrollModule.this.flag = false);
                                 }
                              });
                           }
                        );
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               AutoTrollModule.this.textMap
                  .entrySet()
                  .removeIf(entry -> entry.getValue() + AutoTrollModule.this.killedTimeout.getValue() < System.currentTimeMillis());
               if (AutoTrollModule.this.trollMessageBuilderMutableObject.getValue() != null) {
                  TrollMessageBuilder trollMessageBuilder = (TrollMessageBuilder)AutoTrollModule.this.trollMessageBuilderMutableObject.getValue();
                  if (!AutoTrollModule.this.flag) {
                     ChatTemplate chatTemplate = (ChatTemplate)trollMessageBuilder.getChatTemplateMutableObject().getValue();
                     if (chatTemplate != null) {
                        TextPair textPair = (TextPair)trollMessageBuilder.getTextPairMutableObject().getValue();
                        if (textPair != null) {
                           AutoTrollModule.this.flag = true;
                           boolean flag = chatTemplate.testTextPair(textPair, text -> {
                              if (text != null) {
                                 AutoTrollModule.this.handleText(TrollMessageBuilder.describeText(text), () -> AutoTrollModule.this.flag = false);
                              }
                           });
                           if (flag) {
                              chatTemplate.run();
                              AutoTrollModule.this.flag = false;
                              AutoTrollModule.this.trollMessageBuilderMutableObject.setValue(null);
                              trollMessageBuilder.getTextPairMutableObject().setValue(null);
                              trollMessageBuilder.getChatTemplateMutableObject().setValue(null);
                           }
                        }
                     } else {
                        trollMessageBuilder.getChatTemplateMutableObject().setValue(trollMessageBuilder.getChatTemplate());
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<AttackersRecord>() {
            public void handleObject(
               AttackersRecord attackersRecord,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (attackersRecord.attackers().contains(clientPlayerEntity)) {
                  if (!phobosClient.getFriendList().testUUID(attackersRecord.victim().getUuid())) {
                     AutoTrollModule.this.textMap.put(attackersRecord.victim().getNameForScoreboard(), System.currentTimeMillis());
                  }
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::run2);
   }

   @NotNull
   public String describe2() {
      return TrollMessageBuilder.describeText(this.textList.get(MathUtils.computeInt5(0, this.textList.size() - 1)));
   }

   public void run2() {
      this.trollMessageBuilderMutableObject.setValue(null);
   }
}
