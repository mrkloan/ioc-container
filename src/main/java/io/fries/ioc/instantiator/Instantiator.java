package io.fries.ioc.instantiator;

import io.fries.ioc.dependencies.Dependency;

import java.util.List;

public interface Instantiator {
    <T> T createInstance(Class<T> type, List<Dependency> dependencies);
}
