package io.fries.ioc.registry.managed;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

import java.util.List;
import java.util.Objects;

public class ManagedRegistrableBuilder implements RegistrableBuilder {

    private final Class<?> type;
    private Id id;
    private List<Id> dependencies;

    ManagedRegistrableBuilder(final Id id, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static ManagedRegistrableBuilder manage(final Class<?> type) {
        throw new UnsupportedOperationException();
    }

    public ManagedRegistrableBuilder with(final Object... dependencies) {
        throw new UnsupportedOperationException();
    }

    public <ID> ManagedRegistrableBuilder as(final ID id) {
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
        final ManagedRegistrableBuilder that = (ManagedRegistrableBuilder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, dependencies);
    }

    @Override
    public String toString() {
        return "ManagedRegistrableBuilder{" +
                "id=" + id +
                ", type=" + type +
                ", dependencies=" + dependencies +
                '}';
    }
}
