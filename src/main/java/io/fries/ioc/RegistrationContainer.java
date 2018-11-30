package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;
import io.fries.ioc.registry.Registry;
import io.fries.ioc.registry.managed.ManagedRegistrable;
import io.fries.ioc.registry.proxy.ProxyRegistrable;
import io.fries.ioc.registry.supplied.SuppliedRegistrable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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
    public RegistrationContainer register(final Id id, final Supplier<?> instanceSupplier) {
        final SuppliedRegistrable supplier = SuppliedRegistrable.of(id, instanceSupplier);
        return register(supplier);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Class<?> type) {
        final Id id = Id.of(type);
        return register(id, type);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> type) {
        final List<Id> dependencies = inferDependenciesFrom(type);
        return register(id, type, dependencies);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> type, final List<Id> dependencies) {
        final ManagedRegistrable registrable = ManagedRegistrable.of(id, type, dependencies);
        return register(registrable);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        final ProxyRegistrable proxy = ProxyRegistrable.of(id, interfaceType, type, dependencies);
        return register(proxy);
    }

    private RegistrationContainer register(final Registrable registrable) {
        registry = registry.add(registrable);
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final RegistrableBuilder registrableBuilder) {
        throw new UnsupportedOperationException();
    }

    List<Id> inferDependenciesFrom(final Class<?> type) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Parameter[] constructorParameters = constructor.getParameters();

        return stream(constructorParameters)
                .map(Parameter::getType)
                .map(Id::of)
                .collect(Collectors.toList());
    }

    public Container instantiate() {
        final Components components = registry.instantiate(instantiator);
        return Container.of(components);
    }
}
