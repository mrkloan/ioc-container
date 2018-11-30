package io.fries.ioc.registry.managed;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

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
        final Id id = Id.of(type);
        final List<Id> dependencies = inferDependenciesFrom(type);

        return new ManagedRegistrableBuilder(id, type, dependencies);
    }

    private static List<Id> inferDependenciesFrom(final Class<?> type) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Parameter[] constructorParameters = constructor.getParameters();

        return stream(constructorParameters)
                .map(Parameter::getType)
                .map(Id::of)
                .collect(toList());
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
