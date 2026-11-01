package org.phobos.data;

public record TypeRecord<T>(Class<T> type, Class<?> generic) {
}
