package io.fries.ioc.registry;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Dependency;
import io.fries.ioc.instantiator.Instantiator;

public interface RegisteredDependency {
    int countDependencies(final Registry registry);
    Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies);
}
