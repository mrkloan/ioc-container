package io.fries.ioc;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

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
    public RegistrationContainer register(final Id id, final Class<?> type, final List<Id> dependencies) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(dependencies);

        final DependencyToken token = DependencyToken.of(id, type, dependencies);
        registry = registry.add(id, token);

        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Supplier<Object> instanceSupplier) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(instanceSupplier);

        final DependencySupplier supplier = DependencySupplier.of(id, instanceSupplier);
        registry = registry.add(id, supplier);

        return this;
    }

    public Container instantiate() {
        final Dependencies dependencies = registry.instantiate(instantiator);
        return Container.of(dependencies);
    }
}
