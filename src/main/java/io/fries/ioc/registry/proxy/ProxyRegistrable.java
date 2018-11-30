package io.fries.ioc.registry.proxy;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.Registry;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ProxyRegistrable implements Registrable {

    static final int NO_DEPENDENCIES = 0;

    private final Id id;
    private final Class<?> interfaceType;
    private final Class<?> type;
    private final List<Id> dependencies;

    private ProxyRegistrable(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.interfaceType = interfaceType;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static ProxyRegistrable of(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(interfaceType);
        Objects.requireNonNull(type);
        Objects.requireNonNull(dependencies);

        if (!interfaceType.isInterface())
            throw new IllegalArgumentException("Proxied type must be an interface");

        return new ProxyRegistrable(id, interfaceType, type, dependencies);
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public int countDependencies(final Registry registry) {
        return NO_DEPENDENCIES;
    }

    @Override
    public Component instantiate(final Instantiator instantiator, final Components components) {
        final Supplier<?> instanceSupplier = createInstanceSupplier(instantiator, components);
        final Object proxy = createProxy(instanceSupplier);

        return Component.of(id, proxy);
    }

    private Supplier<?> createInstanceSupplier(final Instantiator instantiator, final Components components) {
        return () -> {
            final List<Component> requiredDependencies = components.findAllById(this.dependencies);
            return instantiator.createInstance(type, requiredDependencies);
        };
    }

    private Object createProxy(final Supplier<?> instanceSupplier) {
        return Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                ProxyInvocationHandler.of(instanceSupplier)
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProxyRegistrable proxy = (ProxyRegistrable) o;
        return Objects.equals(id, proxy.id) &&
                Objects.equals(interfaceType, proxy.interfaceType) &&
                Objects.equals(type, proxy.type) &&
                Objects.equals(dependencies, proxy.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interfaceType, type, dependencies);
    }

    @Override
    public String toString() {
        return "ProxyRegistrable{" +
                "id=" + id +
                ", interfaceType=" + interfaceType +
                ", type=" + type +
                ", components=" + dependencies +
                '}';
    }
}
