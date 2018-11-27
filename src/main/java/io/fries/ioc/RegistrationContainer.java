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
    public RegistrationContainer register(final Id id, final Class<?> type, final Supplier<Object> instanceSupplier) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(instanceSupplier);

        final DependencySupplier supplier = DependencySupplier.of(id, type, instanceSupplier);
        return register(id, supplier);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> type, final List<Id> dependencies) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(dependencies);

        final DependencyToken token = DependencyToken.of(id, type, dependencies);
        return register(id, token);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(interfaceType);
        Objects.requireNonNull(type);
        Objects.requireNonNull(dependencies);

        if (!interfaceType.isInterface())
            throw new IllegalArgumentException("Proxied type must be an interface");

        final DependencyProxy proxy = DependencyProxy.of(id, interfaceType, type, dependencies);
        return register(id, proxy);
    }

    private RegistrationContainer register(final Id id, final RegisteredDependency registeredDependency) {
        registry = registry.add(id, registeredDependency);
        return this;
    }

    public Container instantiate() {
        final Dependencies dependencies = registry.instantiate(instantiator);
        return Container.of(dependencies);
    }
}
