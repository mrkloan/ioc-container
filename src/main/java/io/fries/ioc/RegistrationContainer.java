package io.fries.ioc;

import java.util.List;
import java.util.function.Supplier;

class RegistrationContainer {

    private final Instantiator instantiator;
    private Registry registry;

    private RegistrationContainer(final Instantiator instantiator, final Registry registry) {
        this.instantiator = instantiator;
        this.registry = registry;
    }

    static RegistrationContainer of(final Instantiator instantiator, final Registry registry) {
        return new RegistrationContainer(instantiator, registry);
    }

    RegistrationContainer register(final Id id, final Class<?> type, final List<Id> dependencies) {
        final DependencyToken token = DependencyToken.of(id, type, dependencies);
        registry = registry.add(id, token);

        return this;
    }

    RegistrationContainer register(final Id id, final Supplier<Object> instanceSupplier) {
        final DependencySupplier supplier = DependencySupplier.of(id, instanceSupplier);
        registry = registry.add(id, supplier);

        return this;
    }

    Container instantiate() {
        final Dependencies dependencies = registry.instantiate(instantiator);
        return Container.of(dependencies);
    }
}
