package io.fries.ioc;

import io.fries.ioc.instantiator.Instantiator;

interface RegisteredDependency {
    int countDependencies(final Registry registry);
    Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies);
}
