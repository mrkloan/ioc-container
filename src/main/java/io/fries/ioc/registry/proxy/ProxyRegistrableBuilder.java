package io.fries.ioc.registry.proxy;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

import java.util.List;
import java.util.Objects;

public class ProxyRegistrableBuilder implements RegistrableBuilder {

    private Id id;
    private Class<?> interfaceType;
    private Class<?> type;
    private List<Id> dependencies;

    ProxyRegistrableBuilder(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.interfaceType = interfaceType;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static ProxyRegistrableBuilder proxy(final Class<?> type) {
        throw new UnsupportedOperationException();
    }

    public ProxyRegistrableBuilder of(final Class<?> interfaceType) {
        throw new UnsupportedOperationException();
    }

    public ProxyRegistrableBuilder with(final Object... dependencies) {
        throw new UnsupportedOperationException();
    }

    public <ID> ProxyRegistrableBuilder as(final ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Registrable build() {
        throw new UnsupportedOperationException();
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
