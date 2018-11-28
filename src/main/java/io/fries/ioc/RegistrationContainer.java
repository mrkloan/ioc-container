package io.fries.ioc;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.*;

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
    public RegistrationContainer register(final Class<?> type, final Supplier<?> instanceSupplier) {
        final Id id = Id.of(type);
        return register(id, type, instanceSupplier);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> type, final Supplier<?> instanceSupplier) {
        final DependencySupplier supplier = DependencySupplier.of(id, type, instanceSupplier);
        return register(id, supplier);
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
        final DependencyToken token = DependencyToken.of(id, type, dependencies);
        return register(id, token);
    }

    @SuppressWarnings("WeakerAccess")
    public RegistrationContainer register(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        final DependencyProxy proxy = DependencyProxy.of(id, interfaceType, type, dependencies);
        return register(id, proxy);
    }

    private RegistrationContainer register(final Id id, final RegisteredDependency registeredDependency) {
        registry = registry.add(id, registeredDependency);
        return this;
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
        final Dependencies dependencies = registry.instantiate(instantiator);
        return Container.of(dependencies);
    }
}
