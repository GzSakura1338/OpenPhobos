package org.phobos.module.movement;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.EntitySpeedTravelPreListener;
import org.phobos.module.Module;

public class EntitySpeedModule extends Module {
   public final Setting<Float> speed = new NumberSetting<>("Speed", 0.6F, 0.1F, 10.0F).resolveSetting6(this);

   public EntitySpeedModule(PhobosClient phobosClient) {
      super(phobosClient, "EntitySpeed", Categories.movement, "Makes you travel faster while riding");
      this.handleListener(new EntitySpeedTravelPreListener(this));
   }
}
