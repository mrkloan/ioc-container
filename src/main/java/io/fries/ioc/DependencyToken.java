package io.fries.ioc;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

class DependencyToken implements RegisteredDependency {

    private final Id id;
    private final Class<?> type;
    private final List<Id> dependencies;

    private DependencyToken(final Id id, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.type = type;
        this.dependencies = dependencies;
    }

    static DependencyToken of(final Id id, final Class<?> type, final List<Id> dependencies) {
        return new DependencyToken(id, type, dependencies);
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
                .mapToInt(token -> token.countDependencies(registry))
                .sum();
    }

    @Override
    public Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies) {
        final List<Dependency> requiredDependencies = mapRequiredDependencies(dependencies);
        final Object instance = instantiator.createInstance(type, requiredDependencies);

        return Dependency.of(id, instance);
    }

    private List<Dependency> mapRequiredDependencies(final Dependencies dependencies) {
        return this.dependencies
                .stream()
                .map(dependencies::get)
                .collect(toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DependencyToken that = (DependencyToken) o;
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
        return "DependencyToken{" +
                "id=" + id +
                ", type=" + type +
                ", dependencies=" + dependencies +
                '}';
    }
}
