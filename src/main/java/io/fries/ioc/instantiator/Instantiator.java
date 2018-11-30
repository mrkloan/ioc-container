package io.fries.ioc.instantiator;

import io.fries.ioc.components.Component;

import java.util.List;

public interface Instantiator {
    <T> T createInstance(Class<T> type, List<Component> dependencies);
}
