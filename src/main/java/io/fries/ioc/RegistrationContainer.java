package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;
import io.fries.ioc.registry.Registry;

public class RegistrationContainer {

    private final Instantiator instantiator;
    private Registry registry;

    private RegistrationContainer(final Instantiator instantiator, final Registry registry) {
        this.instantiator = instantiator;
        this.registry = registry;
    }

    static RegistrationContainer of(final Instantiator instantiator, final Registry registry) {
        return new RegistrationContainer(instantiator, registry);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final RegistrableBuilder registrableBuilder) {
        final Registrable registrable = registrableBuilder.build();
        registry = registry.add(registrable);

        return this;
    }

    public Container instantiate() {
        final Components components = registry.instantiate(instantiator);
        return Container.of(components);
    }
}
