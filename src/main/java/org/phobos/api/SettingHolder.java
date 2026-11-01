package org.phobos.api;

import java.util.List;
import org.phobos.core.setting.Setting;

public interface SettingHolder {
   List<Setting<?>> getSettingList();
}
