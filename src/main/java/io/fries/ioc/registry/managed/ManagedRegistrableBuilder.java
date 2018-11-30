package io.fries.ioc.registry.managed;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableWithDependenciesBuilder;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ManagedRegistrableBuilder extends RegistrableWithDependenciesBuilder {

    private final Class<?> type;
    private Id id;
    private List<Id> dependencies;

    private ManagedRegistrableBuilder(final Id id, final Class<?> type) {
        this(id, type, emptyList());
    }

    ManagedRegistrableBuilder(final Id id, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static ManagedRegistrableBuilder managed(final Class<?> type) {
        final Id id = Id.of(type);

        return new ManagedRegistrableBuilder(id, type);
    }

    public ManagedRegistrableBuilder with(final Object... dependencies) {
        this.dependencies = stream(dependencies)
                .map(Id::of)
                .collect(toList());

        return this;
    }

    public <ID> ManagedRegistrableBuilder as(final ID id) {
        this.id = Id.of(id);
        return this;
    }

    @Override
    public Registrable build() {
        if (dependencies.isEmpty())
            dependencies = inferDependenciesFrom(type);

        return ManagedRegistrable.of(id, type, dependencies);
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
