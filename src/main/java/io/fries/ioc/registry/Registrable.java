package io.fries.ioc.registry;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;

public interface Registrable {
    Id getId();
    int countDependencies(final Registry registry);

    Component instantiate(final Instantiator instantiator, final Components components);
}
