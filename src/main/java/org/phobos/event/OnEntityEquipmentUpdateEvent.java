package org.phobos.event;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;

public record OnEntityEquipmentUpdateEvent(EntityEquipmentUpdateS2CPacket packet, Entity entity) {
}
