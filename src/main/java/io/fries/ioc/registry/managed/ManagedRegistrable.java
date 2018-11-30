package io.fries.ioc.registry.managed;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.Registry;

import java.util.List;
import java.util.Objects;

public class ManagedRegistrable implements Registrable {

    private final Id id;
    private final Class<?> type;
    private final List<Id> dependencies;

    private ManagedRegistrable(final Id id, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static ManagedRegistrable of(final Id id, final Class<?> type, final List<Id> dependencies) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(dependencies);

        return new ManagedRegistrable(id, type, dependencies);
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public int countDependencies(final Registry registry) {
        final int firstLevelDependencies = dependencies.size();
        final int deepDependencies = countDeepDependencies(registry);

        return firstLevelDependencies + deepDependencies;
    }

    private int countDeepDependencies(final Registry registry) {
        return dependencies
                .stream()
                .map(registry::get)
                .mapToInt(registrable -> registrable.countDependencies(registry))
                .sum();
    }

    @Override
    public Component instantiate(final Instantiator instantiator, final Components components) {
        final List<Component> requiredDependencies = components.findAllById(this.dependencies);
        final Object instance = instantiator.createInstance(type, requiredDependencies);

        return Component.of(id, instance);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ManagedRegistrable that = (ManagedRegistrable) o;
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
        return "ManagedRegistrable{" +
                "id=" + id +
                ", type=" + type +
                ", components=" + dependencies +
                '}';
    }
}
