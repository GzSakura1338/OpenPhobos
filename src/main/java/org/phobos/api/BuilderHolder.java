package org.phobos.api;

import org.phobos.core.setting.SettingBuilder;
import org.phobos.core.setting.Setting;

public interface BuilderHolder<K, V extends Setting<K>> {
   SettingBuilder<K, V> getSettingBuilder();
}
