package io.fries.ioc.registry.proxy;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.dependencies.DependenciesTypeScanner;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ProxyRegistrableBuilder implements RegistrableBuilder {

    private final DependenciesScanner dependenciesScanner;

    private Id id;
    private Class<?> interfaceType;
    private Class<?> type;
    private List<Id> dependencies;

    private ProxyRegistrableBuilder(final DependenciesScanner dependenciesScanner, final Id id, final Class<?> interfaceType, final Class<?> type) {
        this(dependenciesScanner, id, interfaceType, type, emptyList());
    }

    ProxyRegistrableBuilder(final DependenciesScanner dependenciesScanner, final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        this.dependenciesScanner = dependenciesScanner;
        this.id = id;
        this.interfaceType = interfaceType;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static ProxyRegistrableBuilder proxy(final Class<?> type) {
        final DependenciesScanner dependenciesScanner = new DependenciesTypeScanner();
        final Id id = Id.of(type);
        final Class<?> interfaceType = findFirstImplementedInterface(type);

        return new ProxyRegistrableBuilder(dependenciesScanner, id, interfaceType, type);
    }

    public static Class<?> findFirstImplementedInterface(final Class<?> type) {
        if (type.getInterfaces().length > 0)
            return type.getInterfaces()[0];

        if (type.getSuperclass() != null)
            return findFirstImplementedInterface(type.getSuperclass());

        throw new IllegalArgumentException("The provided type does not implement any interface");
    }

    public ProxyRegistrableBuilder of(final Class<?> interfaceType) {
        this.interfaceType = interfaceType;
        return this;
    }

    public ProxyRegistrableBuilder with(final Object... dependencies) {
        this.dependencies = stream(dependencies)
                .map(Id::of)
                .collect(toList());

        return this;
    }

    public <ID> ProxyRegistrableBuilder as(final ID id) {
        this.id = Id.of(id);
        return this;
    }

    @Override
    public Registrable build() {
        if (dependencies.isEmpty())
            dependencies = dependenciesScanner.findByConstructor(type);

        return ProxyRegistrable.of(id, interfaceType, type, dependencies);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProxyRegistrableBuilder that = (ProxyRegistrableBuilder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(interfaceType, that.interfaceType) &&
                Objects.equals(type, that.type) &&
                Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interfaceType, type, dependencies);
    }

    @Override
    public String toString() {
        return "ProxyRegistrableBuilder{" +
                "id=" + id +
                ", interfaceType=" + interfaceType +
                ", type=" + type +
                ", dependencies=" + dependencies +
                '}';
    }
}
