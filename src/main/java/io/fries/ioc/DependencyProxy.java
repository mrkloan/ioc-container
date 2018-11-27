package io.fries.ioc;

import io.fries.ioc.instantiator.Instantiator;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

class DependencyProxy implements RegisteredDependency {

    static final int NO_DEPENDENCIES = 0;

    private final Id id;
    private final Class<?> interfaceType;
    private final Class<?> type;
    private final List<Id> dependencies;

    private DependencyProxy(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.interfaceType = interfaceType;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static DependencyProxy of(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        return new DependencyProxy(id, interfaceType, type, dependencies);
    }

    @Override
    public int countDependencies(final Registry registry) {
        return NO_DEPENDENCIES;
    }

    @Override
    public Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies) {
        final Supplier<Object> instanceSupplier = createInstanceSupplier(instantiator, dependencies);
        final Object proxy = createProxy(instanceSupplier);

        return Dependency.of(id, interfaceType, proxy);
    }

    private Supplier<Object> createInstanceSupplier(final Instantiator instantiator, final Dependencies dependencies) {
        return () -> {
            final List<Dependency> requiredDependencies = dependencies.findAllById(this.dependencies);
            return instantiator.createInstance(type, requiredDependencies);
        };
    }

    private Object createProxy(final Supplier<Object> instanceSupplier) {
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
        final DependencyProxy proxy = (DependencyProxy) o;
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
        return "DependencyProxy{" +
                "id=" + id +
                ", interfaceType=" + interfaceType +
                ", type=" + type +
                ", dependencies=" + dependencies +
                '}';
    }
}
