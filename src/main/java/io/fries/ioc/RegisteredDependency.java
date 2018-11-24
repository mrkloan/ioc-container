package io.fries.ioc;

interface RegisteredDependency {
    int countDependencies(final Registry registry);
    Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies);
}
