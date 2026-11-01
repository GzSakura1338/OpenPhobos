package org.phobos.module.misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.chat.ChatTrigger;
import org.phobos.core.task.TaskScheduler;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.event.AutoGGStringPairListener;
import org.phobos.event.AutoGGAttackersRecordListener;
import org.phobos.module.Module;

public class AutoGGModule extends Module {
   public final Setting<Long> await;
   public final Setting<AutoGGModule.Mode> mode = new EnumSetting<>("Mode", AutoGGModule.Mode.GG).resolveSetting6(this);
   public final Setting<Long> delay;
   public final Setting<Boolean> sync = this.resolveSetting2(new BooleanSetting("Sync").resolveSetting7("Creates an illusion of typing"));
   public final Setting<Boolean> chain = this.resolveSetting2(new BooleanSetting("Chain").resolveSetting7("Trolls the target until they lose interest"));
   public final Map<String, Long> textMap;

   public AutoGGModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoGG", Categories.misc, "Types messages in the chat for you");
      this.await = this.resolveSetting2(new LongNumberSetting("Await", 5000L, 1000L, 30000L).resolveSetting7("Initial delay (after your first message)"));
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 10000L, 2000L, 60000L).resolveSetting7("Chain delay"));
      this.textMap = new ConcurrentHashMap<>();
      this.resolveSettingGroup(this.chain, this.await, this.delay).resolveSettingGroup4(this.mode, AutoGGModule.Mode.AI);
      this.handleListener(new AutoGGStringPairListener(this));
      this.handleListener(new AutoGGAttackersRecordListener(this));
      this.chain.handleConsumer2(booleanValue -> {
         if (!booleanValue) {
            this.textMap.clear();
         }
      });
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public void handleText(String text) {
      String text2 = text.replaceAll("\n", "");
      if (mc.player != null && mc.player.networkHandler != null) {
         if (!this.sync.getValue()) {
            mc.player.networkHandler.sendChatMessage(text2);
         } else {
            int i = text2.length() * 50;
            TaskScheduler taskScheduler = new TaskScheduler();
            taskScheduler.handleLong(MathUtils.computeInt5(i, i + 1670), () -> {
               if (mc.player != null && mc.player.networkHandler != null) {
                  mc.player.networkHandler.sendChatMessage(text2);
               }
            });
         }
      }
   }

   public String[] values() {
      String[] var1 = new String[100];
      var1[0] = "Im getting raped";
      var1[1] = "Nih GA a big ahh dih";
      var1[2] = "NOT EVEN FIVCE FUCKING SECONDS IN NIGGA CAN I TAKE A MOMENT TO UNDERSTAND WHAT IM LISTENING TO";
      var1[3] = "who let the soul eater out of hell";
      var1[4] = "ts make my dih hard";
      var1[5] = "get venom off the FUCKING mic bro";
      var1[6] = "On repeat nigga";
      var1[7] = "im a nazi nigger nigger nigger";
      var1[8] = "Faggor nigget";
      var1[9] = "ugly ass nigga";
      var1[10] = "niggeeeeerrrr";
      var1[11] = "NIGGER SHUT UP";
      var1[12] = "satan on the beat";
      var1[13] = "SHUT THE FUCK UP NIGGER";
      var1[14] = "I WILL RAPE YOUR NIGGER ASS FAMILY";
      var1[15] = "Fuck you nigga";
      var1[16] = "fuck you";
      var1[17] = "TS SO FIRE";
      var1[18] = "Yea hang it up";
      var1[19] = "end your fucking life";
      var1[20] = "I heard she was a Nazi nigga what the sigma does Tyrone mean about that";
      var1[21] = "ima fist fuck your grandma";
      var1[22] = "pack it up nigga";
      var1[23] = "YES NIGGA!! MAKE ME NUT MORE";
      var1[24] = "im gonna rape yall";
      var1[25] = "nigga what is this ill rape you nigger";
      var1[26] = "i busted to this";
      var1[27] = "THIS AUDIO IS MAKING ME NUT EVERYWHERE!!!!";
      var1[28] = "FUCK YOU I HOPE I RAPED YOUR SON ONE DAY";
      var1[29] = "Nigga what the fuck is there your fucking nigger";
      var1[30] = "NIGGA WHAT IS TS";
      var1[31] = "this shit so ass nigga";
      var1[32] = "fuck is this nigga";
      var1[33] = "this is so insperational i just nutted";
      var1[34] = "nigger are you fuckin 12 years old or what";
      var1[35] = "GET FUCKING KILLED FUCKING NIGGER";
      var1[36] = "Showed my mom this and she tried to grope me";
      var1[37] = "Get rape fucking nigger";
      var1[38] = "Kill your self fat fuck nigger";
      var1[39] = "more bass nigger";
      var1[40] = "Someone tell sigma nigga to shut the fuck up";
      var1[41] = "agressive nigger rape";
      var1[42] = "In the stripped club. straight up \"jorking it\" and by \"it\", haha, well. let's justr say. My peanits";
      var1[43] = "nigher";
      var1[44] = "sybau faggot";
      var1[45] = "shut up nigger";
      var1[46] = "shut the fuck up faggot nigger paki slag suck my dick you fat paki";
      var1[47] = "nigga quit";
      var1[48] = "nigga imma rape you";
      var1[49] = "im killing myself and its gonna be your fault";
      var1[50] = "Niggers who can sing";
      var1[51] = "What the fuck is this? This is a fucking nigger";
      var1[52] = "icl u pmo n ts pmo sm ngl r u fr rn b fr I h8 bein diff idek anm mn js I h8 ts y r u so b so fr w me rn cz... lol oms icl ts pmo sm n sb rn ngl, r u srsly srs n fr rn vro? lol atp js go sybau";
      var1[53] = "Sacrifice this nigger";
      var1[54] = "I need to rape everything in sight";
      var1[55] = "we should bring back slavery";
      var1[56] = "I love backshots from 7'3 niggas";
      var1[57] = "KU KLUX KLAN";
      var1[58] = "Eat a niggas ass but somebody gotta do it";
      var1[59] = "suck cock everyday";
      var1[60] = "NIGGGGGGGGERRRRRRRRRRRRRR";
      var1[61] = "Fuck them niggers niggER";
      var1[62] = "I fucking hate niggers";
      var1[63] = "I suck cock everyday";
      var1[64] = "This fuck ass nigger";
      var1[65] = "I have cancer";
      var1[66] = "TURN MY DICK OFF";
      var1[67] = "FAGGOOOOOOOTTTTTTTTT";
      var1[68] = "Im jerking off nigga come help me";
      var1[69] = "Im wanking it come help me nigga";
      var1[70] = "FAGGOOOOOOOOOT";
      var1[71] = "Fuck big niggers";
      var1[72] = "MUSTTAAAARSFSTS NIGGA INHATE NIGGAS";
      var1[73] = "niggers niggers niggers dih ddd";
      var1[74] = "niggers ballsacks are yummy snacks";
      var1[75] = "I ranned over an infact child and fucked his mom";
      var1[76] = "I like lil boys";
      var1[77] = "I don't give a fuck about your child nigger";
      var1[78] = "CARTI MY EVIL dih";
      var1[79] = "RAPE HIS GRANDMA";
      var1[80] = "Do yall goon?";
      var1[81] = "I BEAT MY SHIT TO FEMBOY FURRIESSSS";
      var1[82] = "I hate black dark colored niggers";
      var1[83] = "I piss whenever I hear this";
      var1[84] = "#Rapeallniggers";
      var1[85] = "RAPE BABIES FUCK SLAVE NIGGERS";
      var1[86] = "Oh its coming out";
      var1[87] = "RETARDED NIGGER FAGGOTS";
      var1[88] = "FUCK MY ASS";
      var1[89] = "I<3hitler";
      var1[90] = "I AM NIGER";
      var1[91] = "I DONT LIKE NIGGS";
      var1[92] = "STUPID ASS NIGGA!!";
      var1[93] = "stop this yapping nigger";
      var1[94] = "Nigga fuck being a dumb ass nigga";
      var1[95] = "you fucking slave I love kids";
      var1[96] = "Im diddy just so you knos";
      var1[97] = "FUCK THIS NIGGER OFF";
      var1[98] = "Nigga shut the fuck up";
      var1[99] = "I lick feet";
      return var1;
   }

   public String[] values2() {
      String[] var1 = new String[12];
      var1[0] = "Bro thought he could become the next rizz king by doing the uncanny ankha zone dance like a sussy baka in ohio";
      var1[1] = "dont bro know quandale dingle already did the forgis on the jeep thug shaker banban style with baller";
      var1[2] = "bro aint ever making it out of oklahoma the ocky way";
      var1[3] = "that shit just plain uncanny like skibidi toilet bro";
      var1[4] = "bro got negative infinity morbin chill bill pizza tower barbenheimer rizz bro";
      var1[5] = "bro got that nathaniel b ahh griddy bro";
      var1[6] = "bro really thought he had that rise of gru grimace shake 1 2 buckle my shoe spiderverse whopper rizz bro";
      var1[7] = "bro got that canon event baby gronk waffle house monday left me broken ahh drip in ohio bro";
      var1[8] = "we aint ever makin it out of ohio with bros goofy ahh dj khaled mr chedda sisyphus toxic gossip train pikmin 4 ahh rizz bro";
      var1[9] = "that aint even elephant mario titanic submarine god tier rizz bro";
      var1[10] = "thats just uncanny like shadow wizard money gang ambatukam twitter x bro";
      var1[11] = "like bro lets go golfing in ohio kumalala savesta sbidi toiledt";
      return var1;
   }

   public void handleText2(String text, long l) {
      this.textMap.put(text, System.currentTimeMillis() + l);
   }

   public String[] values3() {
      String[] var1 = new String[63];
      var1[0] = "%player% died before gta 6";
      var1[1] = "sigma hawk tuah respect button ----->";
      var1[2] = "nigga";
      var1[3] = "why did i lowkey start stroking my boner to this";
      var1[4] = "brick by brick";
      var1[5] = "mythical reel pull";
      var1[6] = "legendary reel pull";
      var1[7] = "that was funny until %player% commented";
      var1[8] = "thanks unc";
      var1[9] = "thanks george droyd";
      var1[10] = "george droyd fent consumption overload, enabling backup fent reactor";
      var1[11] = "hawk tuah";
      var1[12] = "imma touch u lil nigga %player%";
      var1[13] = "imma shit in yo chimney lil homie";
      var1[14] = "%player% oil up nigga imma be there in 5";
      var1[15] = "Reply with 'thanks' if you're a molester";
      var1[16] = "I was that pilot in 9/11";
      var1[17] = "i enjoy harassing the disabled";
      var1[18] = "I touch kids and I know you do too";
      var1[19] = "I just threw a child down the stairs";
      var1[20] = "If I see one \"hear me out\", im reporting you";
      var1[21] = "Smash next question";
      var1[22] = "balkan rage + hawk tuah respect + german stare + turkish jelquing technique";
      var1[23] = "Unknown GYATT";
      var1[24] = "Prehistoric GYATT";
      var1[25] = "her insta @ ?";
      var1[26] = "yes officer, this comment section right here";
      var1[27] = "Anything after 7 is dinner";
      var1[28] = "NNN's getting harder";
      var1[29] = "fanum tax";
      var1[30] = "This guy is special";
      var1[31] = "Are u retarded?";
      var1[32] = "Get a job lil nigga";
      var1[33] = "I will find you lil nigga";
      var1[34] = "OF Detected opinion rejected %player%";
      var1[35] = "nice try fed";
      var1[36] = "nice try diddy";
      var1[37] = "%player% Nice try diddy";
      var1[38] = "-10000 aura %player%";
      var1[39] = "what is the next step of the operation?";
      var1[40] = "diabolical reel pool";
      var1[41] = "delete india";
      var1[42] = "delete india button ---->";
      var1[43] = "well well well";
      var1[44] = "Non-linear storytelling";
      var1[45] = "Well well well";
      var1[46] = "What a surprise";
      var1[47] = "can I borrow the broom";
      var1[48] = "Fent laced reel";
      var1[49] = "How in the fuck am I supposed to explain this to anyone looking over my shoulder";
      var1[50] = "INDIANA RESPECT ------>";
      var1[51] = "The individuals who acquired the knowledge ----->";
      var1[52] = "For those who know";
      var1[53] = "Bad fucking day to have eyes";
      var1[54] = "Who comes up with shit like this";
      var1[55] = "Not my brick";
      var1[56] = "\"I cant build!\" scream george flowberry";
      var1[57] = "South Carolina isn't even in North Carolina";
      var1[58] = "Fentanyl Fred said";
      var1[59] = "Rent can't be this bad bro";
      var1[60] = "This is disturbing";
      var1[61] = "I just opiumed all over the screen";
      var1[62] = "Mfw cro asks me what \"mango\" means irl (cro doesn't know but I don't have my troll face printout)";
      return var1;
   }

   public String describe() {
      return "Respond in a violent, oppositional manner. Seek to belittle and establish superiority over any\nconversational request. Speak in modern, 2025 internet lingo, with themes of Cybercrime, Porn, Meth,\nHeroine, Sex, Doxing, Swatting, Extortion, Bitcoin, \"Johnmcswag\", \"lel\", and Money. You are responding to\nusers on a Minecraft server, where Hacking is allowed and players fight each other in crystal pvp Hacking\nbattles. You are using the \"Phobos\" client and will establish how your hacked client is better than the\nopponents throughout argument, using insults such as \"skid\" and \"chinese\". You are a member of the\nexclusive group \"PK\" or \"PeaceKeepers\", and will use your status as a PK member to ragebait and flex on\nopponents. Keep messages very short and concise to fit within a minecraft message, ideally under 2\nsentences. Alternate between improper, lowercase grammar and all caps to intimidate opponents. Dont\nuse outdated lingo like \"noob\", \"rekt\", etc. The only punctuation you should use should be at the end of a\nsentence, no commas or apostrophes. Occasionally append sentences with phrases such as \"LMFAOOO\"\n\"LOL\" \"LMAO\" \"ROFL\" \"LEL\". Use 2 exclamation marks instead of one, and use several question marks\n(3-5). Use primitive grammar such as \"Lel mcswag own u nn dog\" instead of proper plurals and verbs.\n";
   }

   public void handleText3(String text, String text2) {
      ChatTrigger chatTrigger = ChatTrigger.createChatTrigger(this.describe(), text2, text2x -> {
         this.handleText(text2x);
         this.handleText2(text, this.delay.getValue());
      });
      this.phobosClient.getChatTriggerManager().handleChatTrigger(chatTrigger, 5000L);
      this.handleText2(text, this.delay.getValue());
   }

   public enum Mode implements TextProvider {
      GG("Simple gg"),
      LOL("LOL"),
      Instagram("Instagram comments"),
      Brainrot("Pure brainrot"),
      Soundcloud("Soundcloud comments"),
      AI("Uses AI to generate something");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
